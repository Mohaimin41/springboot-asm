package com.sisimpur.library.controller;

import com.sisimpur.library.dto.BookDTO;
import com.sisimpur.library.service.BookSearchService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookSearchController.class)
public class BookSearchControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private BookSearchService bookSearchService;

        @Test
        void testSearchBooks_NoFilters_ReturnsAll() throws Exception {
                List<BookDTO> mockBooks = List.of(
                                new BookDTO(1L, "Book A", "Fiction", 2022, "Author X", true),
                                new BookDTO(2L, "Book B", "Sci-Fi", 2020, "Author Y", true));

                Mockito.when(bookSearchService.searchBooks(
                                isNull(), isNull(), isNull(), isNull(), isNull())).thenReturn(mockBooks); 
                                                                                                          

                mockMvc.perform(get("/api/v1/book-search"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.size()").value(2))
                                .andExpect(jsonPath("$[0].title").value("Book A"))
                                .andExpect(jsonPath("$[1].authorName").value("Author Y"));
        }

        @Test
        void testSearchBooks_WithAuthorName() throws Exception {
                List<BookDTO> mockBooks = List.of(
                                new BookDTO(1L, "Book A", "Fiction", 2022, "Author X", true));

                Mockito.when(bookSearchService.searchBooks(
                                eq("X"), isNull(), isNull(), isNull(), isNull())).thenReturn(mockBooks); 
                                                                                                         

                mockMvc.perform(get("/api/v1/book-search")
                                .param("author_name", "X"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.size()").value(1))
                                .andExpect(jsonPath("$[0].authorName").value("Author X"));
        }

        @Test
        void testSearchBooks_WithCategoryAndYear() throws Exception {
                List<BookDTO> mockBooks = List.of(
                                new BookDTO(1L, "Book C", "Fantasy", 2021, "Author Z", false));

                Mockito.when(bookSearchService.searchBooks(
                                isNull(), isNull(), eq("Fantasy"), eq(2021), isNull())).thenReturn(mockBooks);

                mockMvc.perform(get("/api/v1/book-search")
                                .param("category", "Fantasy")
                                .param("publishing_year", "2021"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.size()").value(1))
                                .andExpect(jsonPath("$[0].genre").value("Fantasy"))
                                .andExpect(jsonPath("$[0].publishedYear").value(2021));
        }

        @Test
        void testSearchBooks_WithAllFilters() throws Exception {
                List<BookDTO> mockBooks = List.of(
                                new BookDTO(3L, "Mystery Novel", "Mystery", 2020, "Jane Doe", true));

                Mockito.when(bookSearchService.searchBooks(
                                eq("Jane Doe"), eq("Mystery"), eq("Mystery"), eq(2020), eq(true)))
                                .thenReturn(mockBooks);

                mockMvc.perform(get("/api/v1/book-search")
                                .param("author_name", "Jane Doe")
                                .param("book_name", "Mystery")
                                .param("category", "Mystery")
                                .param("publishing_year", "2020")
                                .param("available", "true"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.size()").value(1))
                                .andExpect(jsonPath("$[0].title").value("Mystery Novel"))
                                .andExpect(jsonPath("$[0].authorName").value("Jane Doe"));
        }
}
