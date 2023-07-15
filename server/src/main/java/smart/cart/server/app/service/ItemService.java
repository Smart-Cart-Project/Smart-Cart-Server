package smart.cart.server.app.service;

import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import smart.cart.server.app.exception.MissingDocumentException;
import smart.cart.server.shared.Item;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static smart.cart.server.app.util.ItemServiceUtils.*;

@Service
public class ItemService {
    private final Firestore firestoreDatabase;

    @Autowired
    public ItemService() {
        this.firestoreDatabase = FirestoreClient.getFirestore();
    }

    @SuppressWarnings("ConstantConditions")
    public String saveItem(String cartId, String itemId) throws ExecutionException, InterruptedException {
        // Cart should be registered in the database
        DocumentReference registeredCartRef = firestoreDatabase.collection(REGISTERED_CARTS).document(cartId);
        if (registeredCartRef.get().get().exists()) {
            // Item should be registered in the database
            DocumentReference itemRef = firestoreDatabase.collection(ITEMS).document(itemId);
            if (itemRef.get().get().exists()) {
                // Prepare the item data for the create operation
                DocumentSnapshot itemSnap = itemRef.get().get();
                Map<String, Object> data = new HashMap<>();
                data.put(ITEM_ID, itemId);
                data.put(ITEM_NAME, itemSnap.get(ITEM_NAME));
                data.put(PRICE, itemSnap.get(PRICE));
                data.put(QUANTITY, itemSnap.get(QUANTITY));

                DocumentReference cartRef = firestoreDatabase.collection(CARTS).document(cartId);
                // Make sure that cart document exists in the carts collection
                if (!cartRef.get().get().exists()) {
                    firestoreDatabase.collection(CARTS).document(cartId).create(Map.of("exists", true));
                }
                // Verify if the item already exists in the cart
                DocumentReference itemRefInCartSubCollection = cartRef.collection(cartId + PATH_ADDITION)
                                                                      .document(itemId);
                DocumentSnapshot itemSnapInCartSubCollection = itemRefInCartSubCollection.get().get();
                // If the item does not exist create a Document, update the quantity otherwise.
                if (itemSnapInCartSubCollection.exists()) {
                    return String.format(RESPONSE_MESSAGE, UPDATE, itemRefInCartSubCollection.update(QUANTITY,
                                                                                                     (Long) itemSnapInCartSubCollection.get(
                                                                                                             QUANTITY) +
                                                                                                     1).get()
                                                                                             .getUpdateTime(),
                                         cartId);
                } else {
                    return String.format(RESPONSE_MESSAGE, SAVE,
                                         itemRefInCartSubCollection.create(data).get().getUpdateTime(), cartId);
                }
            } else {
                throw new MissingDocumentException(ITEM_DOES_NOT_EXIST);
            }
        } else {
            throw new MissingDocumentException(CART_IS_NOT_REGISTERED);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public String deleteItem(String cartId, String itemId) throws ExecutionException, InterruptedException {
        // Cart should be registered in the database
        DocumentReference registeredCartRef = firestoreDatabase.collection(REGISTERED_CARTS).document(cartId);
        if (registeredCartRef.get().get().exists()) {
            // Item should be registered in the database
            DocumentReference itemRef = firestoreDatabase.collection(ITEMS).document(itemId);
            if (itemRef.get().get().exists()) {
                DocumentReference cartRef = firestoreDatabase.collection(CARTS).document(cartId);
                // Verify if the item exists in the cart
                DocumentReference itemRefInCart = cartRef.collection(cartId + PATH_ADDITION)
                                                         .document(itemId);
                DocumentSnapshot itemSnapInCart = itemRefInCart.get().get();
                if (itemSnapInCart.exists()) {
                    Long quantity = (Long) itemSnapInCart.get(QUANTITY);
                    // If there is one item then just delete it, update the quantity otherwise.
                    if (quantity == 1) {
                        return String.format(RESPONSE_MESSAGE, DELETE,
                                             itemRefInCart.delete().get().getUpdateTime(), cartId);
                    } else {
                        return String.format(RESPONSE_MESSAGE, UPDATE,
                                             itemRefInCart.update(QUANTITY, quantity - 1).get().getUpdateTime(),
                                             cartId);
                    }
                } else {
                    throw new MissingDocumentException(String.format(CART_ITEM_DOES_NOT_EXIST, itemId, cartId));
                }
            } else {
                throw new MissingDocumentException(ITEM_DOES_NOT_EXIST);
            }
        } else {
            throw new MissingDocumentException(CART_IS_NOT_REGISTERED);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public List<Item> getAllItems(String cartId) throws ExecutionException, InterruptedException {
        // Cart should be registered in the database
        DocumentReference registeredCartRef = firestoreDatabase.collection(REGISTERED_CARTS).document(cartId);
        if (registeredCartRef.get().get().exists()) {
            DocumentReference cartRef = firestoreDatabase.collection(CARTS).document(cartId);
            // Cart document must exist
            if (cartRef.get().get().exists()) {
                QuerySnapshot collectionQuerySnapshot = cartRef.collection(cartId + PATH_ADDITION).get().get();
                // If collection is empty then return empty list.
                if (collectionQuerySnapshot.isEmpty()) {
                    return Collections.emptyList();
                } else {
                    // Create items list from the collection's documents.
                    List<QueryDocumentSnapshot> itemsDocumentSnapshots = collectionQuerySnapshot.getDocuments();
                    return itemsDocumentSnapshots.stream().map(document -> new Item((String) document.get(ITEM_ID),
                                                                                    (String) document.get(ITEM_NAME),
                                                                                    (Number) document.get(PRICE),
                                                                                    (Number) document.get(QUANTITY)))
                                                 .toList();
                }
            } else {
                throw new MissingDocumentException(CART_ID_DOCUMENT_IS_MISSING);
            }
        } else {
            throw new MissingDocumentException(CART_IS_NOT_REGISTERED);
        }
    }
}
