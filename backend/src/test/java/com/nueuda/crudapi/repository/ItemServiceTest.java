package com.nueuda.crudapi.repository;

import com.nueuda.crudapi.entity.Item;
import com.nueuda.crudapi.exception.ItemNotFoundException;
import com.nueuda.crudapi.repo.ItemRepository;
import com.nueuda.crudapi.service.ItemService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    void testGetAllItems() {
        List<Item> mockItems = Arrays.asList(
                new Item("Apple", 0.99),
                new Item("Banana", 0.59)
        );
        Mockito.when(itemRepository.findAll()).thenReturn(mockItems);
        List<Item> items = itemService.getAllItems();
        Assertions.assertEquals(2, items.size());
    }

    @Test
    void testGetItemById_found() {
        Item mockItem = new Item("Apple", 0.99);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(mockItem));
        Item item = itemService.getItemById(1L);
        Assertions.assertEquals("Apple", item.getName());
    }

    @Test
    void testGetItemById_notFound() {
        Mockito.when(itemRepository.findById(2L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ItemNotFoundException.class, () -> itemService.getItemById(2L));
    }

    @Test
    void testCreateItem() {
        Item toSave = new Item("Orange", 1.29);
        Mockito.when(itemRepository.save(toSave)).thenReturn(toSave);
        Item saved = itemService.createItem(toSave);
        Assertions.assertEquals("Orange", saved.getName());
    }

    @Test
    void testUpdateItem_found() {
        Item existing = new Item("Apple", 0.99);
        Item update = new Item("Green Apple", 1.09);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(existing));
        Mockito.when(itemRepository.save(existing)).thenReturn(existing);
        Item updated = itemService.updateItem(1L, update);
        Assertions.assertEquals("Green Apple", updated.getName());
        Assertions.assertEquals(1.09, updated.getPrice());
    }

    @Test
    void testUpdateItem_notFound() {
        Item update = new Item("Green Apple", 1.09);
        Mockito.when(itemRepository.findById(2L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ItemNotFoundException.class, () -> itemService.updateItem(2L, update));
    }

    @Test
    void testDeleteItem_found() {
        Mockito.when(itemRepository.existsById(1L)).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> itemService.deleteItem(1L));
        Mockito.verify(itemRepository).deleteById(1L);
    }

    @Test
    void testDeleteItem_notFound() {
        Mockito.when(itemRepository.existsById(2L)).thenReturn(false);
        Assertions.assertThrows(ItemNotFoundException.class, () -> itemService.deleteItem(2L));
    }
}