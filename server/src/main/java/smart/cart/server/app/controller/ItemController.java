package smart.cart.server.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import smart.cart.server.app.service.ItemService;
import smart.cart.server.app.exception.MissingDocumentException;
import smart.cart.server.shared.Item;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@SuppressWarnings("unused")
public class ItemController {
    private static final String INVALID_QUERY = "One or more query parameters are invalid!";
    private final ItemService itemsService;

    public ItemController(ItemService itemService) {
        this.itemsService = itemService;
    }

    @GetMapping("/items")
    public ResponseEntity<List<Item>> getAllItems(@RequestParam("cartId") String cartId) throws InterruptedException {
        if (cartId == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            return ResponseEntity.ok(itemsService.getAllItems(cartId));
        } catch (ExecutionException | MissingDocumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/items")
    public ResponseEntity<String> saveItem(@RequestParam("cartId") String cartId,
                                           @RequestParam("itemId") String itemId) throws InterruptedException {
        if (cartId == null || itemId == null || cartId.isEmpty() || itemId.isEmpty()) {
            return ResponseEntity.status(400).body(INVALID_QUERY);
        }
        try {
            return ResponseEntity.ok(itemsService.saveItem(cartId, itemId));
        } catch (MissingDocumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/items")
    public ResponseEntity<String> deleteItem(@RequestParam("cartId") String cartId,
                                             @RequestParam("itemId") String itemId) throws InterruptedException {
        if (cartId == null || itemId == null || cartId.isEmpty() || itemId.isEmpty()) {
            return ResponseEntity.status(400).body(INVALID_QUERY);
        }
        try {
            return ResponseEntity.ok(itemsService.deleteItem(cartId, itemId));
        } catch (MissingDocumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/tags")
    public ResponseEntity<String> getItemIdForTag() throws InterruptedException {
        try {
            return ResponseEntity.ok(itemsService.getItemIdForTag());
        } catch (MissingDocumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/tags")
    public ResponseEntity<String> setItemIdForTag(@RequestParam("itemId") String itemId) throws InterruptedException {
        try {
            return ResponseEntity.ok(itemsService.setItemIdForTag(itemId));
        } catch (MissingDocumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
