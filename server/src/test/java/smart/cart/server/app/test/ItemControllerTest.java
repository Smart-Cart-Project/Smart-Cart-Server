package smart.cart.server.app.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import smart.cart.server.app.ServerApplication;
import smart.cart.server.app.controller.ItemController;
import smart.cart.server.app.service.ItemService;
import smart.cart.server.shared.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest(ItemController.class)
@ContextConfiguration(classes = {ServerApplication.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SuppressWarnings({"unchecked","UnusedDeclaration"})
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemsService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String CART_ID = "cartId";
    private final String ITEM_ID = "itemId";

    @Test
    @DisplayName("Get all items")
    void getEndpointTest() throws Exception {
        List<Item> testItems = List.of(new Item(), new Item());
        when(itemsService.getAllItems(CART_ID)).thenReturn(testItems);

        var result = mockMvc.perform(get("/items").queryParam(CART_ID, CART_ID)
                                                  .accept(MediaType.APPLICATION_JSON)
                                                  .contentType(MediaType.APPLICATION_JSON))
                            .andDo(print()).andExpect(status().isOk()).andReturn().getResponse();

        try {
            List<Item> retrievedItems = objectMapper.readValue(result.getContentAsString(), List.class);
            if (retrievedItems.size() != 2) {
                fail("The \"getAllItems\" endpoint didn't return the correct amount of items in the list.");
            }
        } catch (JsonProcessingException k) {
            fail("The \"getAllItems\" endpoint didn't return the correct data, make sure to respond with the items list.");
        }
    }

    @Test
    @DisplayName("Save item")
    void postEndpointTest() throws Exception {
        when(itemsService.saveItem(CART_ID, ITEM_ID)).thenReturn("Success");

        var result = mockMvc.perform(post("/items").queryParam(CART_ID, CART_ID)
                                                   .queryParam(ITEM_ID, ITEM_ID))
                            .andDo(print()).andExpect(status().isOk()).andReturn().getResponse();


        if (!result.getContentAsString().equals("Success")) {
            fail("The \"saveItem\" endpoint response is not correct.\nExpected: Success\nBut was: " +
                 result.getContentAsString());
        }
    }

    @Test
    @DisplayName("Delete item")
    void deleteEndpointTest() throws Exception {
        when(itemsService.deleteItem(CART_ID, ITEM_ID)).thenReturn("Success");

        var result = mockMvc.perform(delete("/items").queryParam(CART_ID, CART_ID)
                                                     .queryParam(ITEM_ID, ITEM_ID))
                            .andDo(print()).andExpect(status().isOk()).andReturn().getResponse();

        if (!result.getContentAsString().equals("Success")) {
            fail("The \"deleteItem\" endpoint response is not correct.\nExpected: Success\nBut was: " +
                 result.getContentAsString());
        }
    }
}
