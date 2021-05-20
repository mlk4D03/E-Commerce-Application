package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

        Item item1 = new Item();
        item1.setName("Round Widget");
        item1.setPrice(BigDecimal.valueOf(2.99));
        item1.setDescription("A widget that is round");

        Item item2 = new Item();
        item2.setName("Square Widget");
        item2.setPrice(BigDecimal.valueOf(1.99));
        item2.setDescription("A widget that is square");

        Mockito.when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2));
        Mockito.when(itemRepository.findByName(Mockito.anyString())).thenReturn(Arrays.asList(item1));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item2));
    }

    @Test
    public void test_getAllItems() {
        ResponseEntity<List<Item>> items = itemController.getItems();

        Assert.assertNotNull(items);
        Assert.assertEquals(200, items.getStatusCodeValue());

        List<Item> itemsList = items.getBody();

        Assert.assertEquals(2, itemsList.size());
        Assert.assertEquals("Round Widget", itemsList.get(0).getName());
        Assert.assertEquals("Square Widget", itemsList.get(1).getName());

    }

    @Test
    public void test_getItemById() {

        ResponseEntity<Item> items = itemController.getItemById(2L);

        Assert.assertNotNull(items);
        Assert.assertEquals(200, items.getStatusCodeValue());

        Item item = items.getBody();

        Assert.assertEquals("Square Widget", item.getName());

    }

    @Test
    public void test_getItemsByName() {
        ResponseEntity<List<Item>> items = itemController.getItemsByName("Round Widget");

        Assert.assertNotNull(items);
        Assert.assertEquals(200, items.getStatusCodeValue());

        List<Item> itemsList = items.getBody();

        Assert.assertEquals(1, itemsList.size());
        Assert.assertEquals("Round Widget", itemsList.get(0).getName());
    }


}
