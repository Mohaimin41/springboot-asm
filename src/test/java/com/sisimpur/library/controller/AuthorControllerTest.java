package com.sisimpur.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sisimpur.library.dto.AuthorCreateReqDTO;
import com.sisimpur.library.dto.AuthorDTO;
import com.sisimpur.library.dto.AuthorUpdateReqDTO;
import com.sisimpur.library.dto.BookSummaryDTO;
import com.sisimpur.library.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthorDTO authorDTO;

    @BeforeEach
    void setup() {
        BookSummaryDTO bookSummary = new BookSummaryDTO(1L, "The Lightning Thief", "Fantasy", 2005);
        authorDTO = new AuthorDTO(1L, "Rick Riordan", "Author of Percy Jackson series", List.of(bookSummary));
    }

    @Test
    void testGetAllAuthors() throws Exception {
        when(authorService.getAllAuthors()).thenReturn(List.of(authorDTO));

        mockMvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Rick Riordan"));
    }

    @Test
    void testGetAuthorById() throws Exception {
        when(authorService.getAuthorById(1L)).thenReturn(authorDTO);

        mockMvc.perform(get("/api/v1/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rick Riordan"));
    }

    @Test
    void testCreateAuthor() throws Exception {
        AuthorCreateReqDTO req = new AuthorCreateReqDTO("Rick Riordan", "Author of Percy Jackson", Collections.emptyList());

        when(authorService.createAuthor(any(AuthorCreateReqDTO.class))).thenReturn(authorDTO);

        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Rick Riordan"));
    }

    @Test
    void testUpdateAuthor() throws Exception {
        AuthorUpdateReqDTO req = new AuthorUpdateReqDTO("Rick Riordan", "Updated Biography");

        when(authorService.updateAuthor(eq(1L), any(AuthorUpdateReqDTO.class))).thenReturn(authorDTO);

        mockMvc.perform(put("/api/v1/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rick Riordan"));
    }

    @Test
    void testDeleteAuthor() throws Exception {
        doNothing().when(authorService).deleteAuthor(1L);

        mockMvc.perform(delete("/api/v1/authors/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Author deleted successfully"));
    }

    @Test
    void testInvalidAuthorIdShouldReturn400() throws Exception {
        mockMvc.perform(get("/api/v1/authors/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Author ID must be positive")));
    }
}
