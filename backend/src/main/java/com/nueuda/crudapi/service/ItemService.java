package com.nueuda.crudapi.service;

import com.nueuda.crudapi.entity.Item;
import com.nueuda.crudapi.exception.ItemNotFoundException;
import com.nueuda.crudapi.repo.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final Logger logger = LoggerFactory.getLogger(ItemService.class);

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Item with id " + id + " not found"));
    }

    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    public Item updateItem(Long id, Item updatedItem) {
        return itemRepository.findById(id)
                .map(existingItem -> {
                    existingItem.setName(updatedItem.getName());
                    existingItem.setPrice(updatedItem.getPrice());
                    return itemRepository.save(existingItem);
                })
                .orElseThrow(() -> new ItemNotFoundException("Item with id " + id + " not found"));
    }

    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new ItemNotFoundException("Item with id " + id + " not found");
        }
        itemRepository.deleteById(id);
    }
}