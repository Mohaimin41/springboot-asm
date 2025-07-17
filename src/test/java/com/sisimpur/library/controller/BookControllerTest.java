package com.sisimpur.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sisimpur.library.dto.BookCreateReqDTO;
import com.sisimpur.library.dto.BookDTO;
import com.sisimpur.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllBooks() throws Exception {
        BookDTO book = new BookDTO(1L, "The Hobbit", "Fantasy", 1937, "J.R.R. Tolkien", false);
        Mockito.when(bookService.getAllBookDTOs()).thenReturn(List.of(book));

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("The Hobbit"));
    }

    @Test
    public void testGetBookById() throws Exception {
        BookDTO book = new BookDTO(1L, "The Hobbit", "Fantasy", 1937, "J.R.R. Tolkien", true);
        Mockito.when(bookService.getBookDTO(1L)).thenReturn(book);

        mockMvc.perform(get("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Hobbit"));
    }

    @Test
    public void testCreateBook() throws Exception {
        BookCreateReqDTO req = new BookCreateReqDTO("The Hobbit", 1L, "Fantasy", 1937);
        BookDTO res = new BookDTO(1L, "The Hobbit", "Fantasy", 1937, "J.R.R. Tolkien", true);

        Mockito.when(bookService.createBook(any(BookCreateReqDTO.class))).thenReturn(res);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("The Hobbit"));
    }

    @Test
    public void testUpdateBook() throws Exception {
        BookCreateReqDTO req = new BookCreateReqDTO("Updated Book", 1L, "Sci-Fi", 2022);
        BookDTO res = new BookDTO(1L, "Updated Book", "Sci-Fi", 2022, "Author", true);

        Mockito.when(bookService.updateBook(eq(1L), any(BookCreateReqDTO.class))).thenReturn(res);

        mockMvc.perform(put("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Book"));
    }

    @Test
    public void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book deleted successfully"));
    }
}
