package com.nueuda.crudapi.repository;

import com.nueuda.crudapi.entity.Item;
import com.nueuda.crudapi.repo.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void testFindAll() {
        List<Item> items = itemRepository.findAll();
        Assertions.assertFalse(items.isEmpty(), "Items should not be empty");
        Assertions.assertTrue(items.size() >= 5, "Should have at least 5 seeded items");
    }

    @Test
    void testFindById() {
        List<Item> items = itemRepository.findAll();
        Item first = items.get(0);
        Optional<Item> found = itemRepository.findById(first.getId());
        Assertions.assertTrue(found.isPresent(), "Item should be found by id");
        Assertions.assertEquals(first.getName(), found.get().getName());
    }

    @Test
    void testSave() {
        Item newItem = new Item("Pear", 1.49);
        Item saved = itemRepository.save(newItem);
        Assertions.assertNotNull(saved.getId(), "Saved item should have an id");
        Assertions.assertEquals("Pear", saved.getName());
    }

    @Test
    void testDelete() {
        List<Item> items = itemRepository.findAll();
        Item toDelete = items.get(0);
        itemRepository.deleteById(toDelete.getId());
        Optional<Item> found = itemRepository.findById(toDelete.getId());
        Assertions.assertFalse(found.isPresent(), "Item should be deleted");
    }
}
