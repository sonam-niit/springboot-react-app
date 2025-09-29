package com.nueuda.crudapi.controller;

import java.util.List;

import com.nueuda.crudapi.entity.Item;
import com.nueuda.crudapi.exception.InvalidParamsException;
import com.nueuda.crudapi.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<Item>> getItems() {
        logger.info("GET /items called");
        List<Item> items = itemService.getAllItems();
        for (Item item : items) {
            logger.info("Item details: {}", item);
        }
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        logger.info("GET /items/{} called", id);
        if (id == null) {
            throw new InvalidParamsException("Item id must be provided");
        }
        Item item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        logger.info("POST /items called with item: {}", item);
        if (item == null || item.getName() == null || item.getName().isBlank() || item.getPrice() == 0) {
            throw new InvalidParamsException("Item name and price must be provided");
        }
        Item createdItem = itemService.createItem(item);
        return ResponseEntity.ok(createdItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item item) {
        logger.info("PUT /items/{} called with item: {}", id, item);
        if (id == null || item == null || item.getName() == null || item.getName().isBlank() || item.getPrice() == 0) {
            throw new InvalidParamsException("Item id, name, and price must be provided");
        }
        Item updatedItem = itemService.updateItem(id, item);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        logger.info("DELETE /items/{} called", id);
        if (id == null) {
            throw new InvalidParamsException("Item id must be provided");
        }
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}