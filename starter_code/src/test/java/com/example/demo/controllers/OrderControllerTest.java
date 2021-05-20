package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class OrderControllerTest {

    private OrderController orderController;

    private OrderRepository orderRepository = Mockito.mock(OrderRepository.class);

    private UserRepository userRepository = Mockito.mock((UserRepository.class));

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);

        User expectedUser = new User();
        expectedUser.setUsername("Mika");
        expectedUser.setPassword("MyPassword");
        expectedUser = Mockito.spy(expectedUser);

        Item item1 = new Item();
        item1.setName("Round Widget");
        item1.setPrice(BigDecimal.valueOf(2.99));
        item1.setDescription("A widget that is round");

        Item item2 = new Item();
        item2.setName("Square Widget");
        item2.setPrice(BigDecimal.valueOf(1.99));
        item2.setDescription("A widget that is square");

        Cart cart = new Cart();
        cart.setItems(Arrays.asList(item1, item2));
        cart.setTotal(BigDecimal.valueOf(4.98));
        cart.setUser(expectedUser);

        UserOrder userOrder = UserOrder.createFromCart(cart);


        Mockito.when(userRepository.findByUsername("Mika")).thenReturn(expectedUser);
        Mockito.when(userRepository.findByUsername("Missing User")).thenReturn(null);
        Mockito.when(expectedUser.getCart()).thenReturn(cart);

        Mockito.when(orderRepository.findByUser(expectedUser)).thenReturn(Arrays.asList(userOrder));
    }

    @Test
    public void test_submit() {
        ResponseEntity<UserOrder> res = orderController.submit("Mika");

        Assert.assertNotNull(res);

        UserOrder userOrder = res.getBody();
        Assert.assertNotNull(userOrder);

        Assert.assertEquals(BigDecimal.valueOf(4.98), userOrder.getTotal());
        Assert.assertEquals("Mika", userOrder.getUser().getUsername());
        Assert.assertEquals(2, userOrder.getItems().size());
    }

    @Test
    public void test_submitUserNotAvailable() {
        ResponseEntity<UserOrder> res = orderController.submit("Missing User");

        Assert.assertEquals(404,res.getStatusCodeValue());
    }

    @Test
    public void test_getOrdersForUser() {
        ResponseEntity<List<UserOrder>> res = orderController.getOrdersForUser("Mika");

        Assert.assertNotNull(res);

        List<UserOrder> userOrders = res.getBody();

        Assert.assertEquals(1, userOrders.size());
        Assert.assertEquals(BigDecimal.valueOf(4.98), userOrders.get(0).getTotal());
        Assert.assertEquals("Mika", userOrders.get(0).getUser().getUsername());
        Assert.assertEquals(2, userOrders.get(0).getItems().size());

    }

    @Test
    public void test_getOrdersForNotAvailableUser() {
        ResponseEntity<List<UserOrder>> res = orderController.getOrdersForUser("Missing User");

        Assert.assertEquals(404,res.getStatusCodeValue());
    }
}
