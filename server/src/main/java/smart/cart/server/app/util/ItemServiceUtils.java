package smart.cart.server.app.util;

public final class ItemServiceUtils {
    private ItemServiceUtils() {
        // Protected
    }

    public static final String ITEMS = "items";
    public static final String CARTS = "carts";
    public static final String REGISTERED_CARTS = "registered-carts";
    public static final String TAGS_COLLECTION = "tagData";
    public static final String TAG_ITEM = "tagItem";
    public static final String ITEM_ID = "itemId";
    public static final String ITEM_NAME = "itemName";
    public static final String PRICE = "price";
    public static final String QUANTITY = "quantity";
    public static final String PATH_ADDITION = "-items";
    public static final String UPDATE = "Update";
    public static final String SAVE = "Save";
    public static final String DELETE = "Delete";
    public static final String RESPONSE_MESSAGE = "%s time: %s\nCartID: %s";
    public static final String CART_ID_DOCUMENT_IS_MISSING = "Document for the specified cartId doesn't exist!";
    public static final String CART_IS_NOT_REGISTERED = "Provided cartId is not registered in the system!";
    public static final String ITEM_DOES_NOT_EXIST = "Provided itemId is not registered in the system!";
    public static final String CART_ITEM_DOES_NOT_EXIST = "Item with the item ID \"%s\" does not exist in the cart with the ID \"%s\".";
    public static final String TAG_ITEM_DOES_NOT_EXIST = "Item ID for the Tag does not exist, make sure to create it in the database.";
}
