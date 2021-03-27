package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();

        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        User user = new User();
        Cart cart = new Cart();
        List<Item> itemList = new ArrayList<>();
        Item item = new Item();
        List<UserOrder> userOrderList = new ArrayList<>();
        UserOrder userOrder = new UserOrder();

        userOrder.setId(1L);
        userOrder.setUser(user);
        userOrder.setItems(itemList);
        userOrder.setTotal(BigDecimal.valueOf(0.99));

        item.setId(0L);
        item.setName("item");
        item.setDescription("item desc");
        item.setPrice(BigDecimal.valueOf(13.99));

        itemList.add(item);

        cart.setId(0L);
        cart.setUser(user);
        cart.setItems(itemList);
        cart.setTotal(BigDecimal.valueOf(13.99));

        user.setId(0L);
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setCart(cart);

        userOrderList.add(userOrder);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(userOrderList);
    }

    @Test
    public void submitNewOrder() {
        ResponseEntity<UserOrder> response = orderController.submit("test");

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getTotal(), BigDecimal.valueOf(13.99));
        assertEquals(response.getBody().getItems().size(), 1);

        response = orderController.submit("nonUser");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void getOrders() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().size(), 1);

        response = orderController.getOrdersForUser("nonUser");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

}
