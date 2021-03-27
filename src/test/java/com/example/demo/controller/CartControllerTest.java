package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();

        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCart() {
        ModifyCartRequest request = new ModifyCartRequest();
        User user = new User();
        Item item = new Item();

        item.setId(0L);
        item.setName("item");
        item.setDescription("Dummy item");
        item.setPrice(BigDecimal.valueOf(13.99));

        user.setId(0);
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setCart(new Cart());

        request.setItemId(0L);
        request.setQuantity(2);
        request.setUsername("test");


        when(userRepository.findByUsername("test")).thenReturn(user);
        when(itemRepository.findById(0L)).thenReturn(Optional.of(item));
        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(response.getBody().getTotal(), BigDecimal.valueOf(27.98));

        request.setUsername("noUser");
        response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());

        request.setUsername("test");
        request.setItemId(2L);
        response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void removeFromCart() {
        ModifyCartRequest request = new ModifyCartRequest();
        User user = new User();
        Item item = new Item();

        item.setId(0L);
        item.setName("item");
        item.setDescription("Dummy item");
        item.setPrice(BigDecimal.valueOf(13.99));

        user.setId(0);
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setCart(new Cart());

        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("test");

        request.setQuantity(2);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(itemRepository.findById(0L)).thenReturn(Optional.of(item));
        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

        request.setUsername("noUser");
        request.setQuantity(2);

        response = cartController.removeFromcart(request);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());

        request.setItemId(2L);
        response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());

    }
}
