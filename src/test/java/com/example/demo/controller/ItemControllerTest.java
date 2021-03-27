package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

    }

    @Test
    public void getItems() {
        Item item = new Item();
        List<Item> itemList = new ArrayList<>();

        item.setId(0L);
        item.setName("item");
        item.setDescription("item desc");
        item.setPrice(BigDecimal.valueOf(13.99));

        itemList.add(item);
        when(itemRepository.findAll()).thenReturn(itemList);
        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void getItemWithId() {
        Item item = new Item();
        List<Item> itemList = new ArrayList<>();

        item.setId(0L);
        item.setName("item");
        item.setDescription("item desc");
        item.setPrice(BigDecimal.valueOf(13.99));

        itemList.add(item);

        when(itemRepository.findById(0L)).thenReturn(Optional.of(item));
        ResponseEntity<Item> response = itemController.getItemById(0L);
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(response.getBody().getName(), "item");
        assertEquals(response.getBody().getDescription(), "item desc");
        assertEquals(response.getBody().getPrice(), BigDecimal.valueOf(13.99));

        response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());

    }

    @Test
    public void getItemWithName() {
        Item item = new Item();
        List<Item> itemList = new ArrayList<>();

        item.setId(0L);
        item.setName("item");
        item.setDescription("item desc");
        item.setPrice(BigDecimal.valueOf(13.99));

        itemList.add(item);
        when(itemRepository.findByName("item desc")).thenReturn(itemList);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("item desc");

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        response = itemController.getItemsByName("different item desc");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());

    }
}
