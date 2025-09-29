package com.nueuda.crudapi.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nueuda.crudapi.entity.Item;
import com.nueuda.crudapi.repo.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void testGetAllItems() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    void testGetItemById() throws Exception {
        Item item = itemRepository.save(new Item("TestItem", 2.99));
        mockMvc.perform(get("/items/" + item.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestItem"));
    }

    @Test
    void testCreateItem() throws Exception {
        Item item = new Item("CreatedItem", 3.99);
        String json = objectMapper.writeValueAsString(item);
        mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("CreatedItem"));
    }

    @Test
    void testUpdateItem() throws Exception {
        Item item = itemRepository.save(new Item("OldName", 1.99));
        Item update = new Item("UpdatedName", 2.99);
        String json = objectMapper.writeValueAsString(update);
        mockMvc.perform(put("/items/" + item.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedName"));
    }

    @Test
    void testDeleteItem() throws Exception {
        Item item = itemRepository.save(new Item("ToDelete", 0.99));
        mockMvc.perform(delete("/items/" + item.getId()))
                .andExpect(status().isNoContent());
        Assertions.assertFalse(itemRepository.findById(item.getId()).isPresent());
    }

    @Test
    void testGetItemById_NotFound() throws Exception {
        mockMvc.perform(get("/items/99999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("not found")));
    }

    @Test
    void testCreateItem_InvalidParams() throws Exception {
        Item item = new Item(null, 0);
        String json = objectMapper.writeValueAsString(item);
        mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("must be provided")));
    }
}