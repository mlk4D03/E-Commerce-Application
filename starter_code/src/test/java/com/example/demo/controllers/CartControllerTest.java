package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    private CartRepository cartRepository = Mockito.mock(CartRepository.class);

    private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        User expectedUser = new User();
        expectedUser.setUsername("Mika");
        expectedUser.setPassword("MyPassword");
        expectedUser = Mockito.spy(expectedUser);

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Round Widget");
        item1.setPrice(BigDecimal.valueOf(2.99));
        item1.setDescription("A widget that is round");

        Cart cart = new Cart();
        ArrayList<Item> items = new ArrayList<>();
        items.add(item1);
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(4.98));
        cart.setUser(expectedUser);


        Mockito.when(userRepository.findByUsername("Mika")).thenReturn(expectedUser);
        Mockito.when(userRepository.findByUsername("Missing User")).thenReturn(null);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        Mockito.when(itemRepository.findById(2L)).thenReturn(Optional.empty());
        Mockito.when(expectedUser.getCart()).thenReturn(cart);

    }

    @Test
    public void test_addTocart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("Mika");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(10);

        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);

        Assert.assertNotNull(cartResponseEntity);

        Cart cart = cartResponseEntity.getBody();

        Assert.assertNotNull(cart);

        Assert.assertEquals(11, cart.getItems().size());
        Assert.assertEquals("Round Widget", cart.getItems().get(0).getName());
        Assert.assertEquals("Mika", cart.getUser().getUsername());
    }

    @Test
    public void test_addTocartNoItemFound() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("Mika");
        modifyCartRequest.setItemId(2L);
        modifyCartRequest.setQuantity(10);

        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);

        Assert.assertEquals(404, cartResponseEntity.getStatusCodeValue());
    }

    @Test
    public void test_addTocartNoUserFound() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("Missing User");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(10);

        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);

        Assert.assertEquals(404, cartResponseEntity.getStatusCodeValue());
    }

    @Test
    public void test_removeFromCardNoItemFound() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("Mika");
        modifyCartRequest.setItemId(2L);
        modifyCartRequest.setQuantity(10);

        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromcart(modifyCartRequest);

        Assert.assertEquals(404, cartResponseEntity.getStatusCodeValue());
    }

    @Test
    public void test_removeFromCardNoUserFound() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("Missing User");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(10);

        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromcart(modifyCartRequest);

        Assert.assertEquals(404, cartResponseEntity.getStatusCodeValue());
    }

    @Test
    public void test_removeFromCart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("Mika");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromcart(modifyCartRequest);

        Assert.assertNotNull(cartResponseEntity);

        Cart cart = cartResponseEntity.getBody();

        Assert.assertNotNull(cart);

        Assert.assertEquals(0, cart.getItems().size());
    }

}
