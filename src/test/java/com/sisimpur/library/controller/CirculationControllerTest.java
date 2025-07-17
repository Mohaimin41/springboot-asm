package com.sisimpur.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sisimpur.library.dto.BorrowRequestDTO;
import com.sisimpur.library.dto.BorrowResponseDTO;
import com.sisimpur.library.service.CirculationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CirculationController.class)
public class CirculationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CirculationService circulationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testBorrowBooks() throws Exception {
        BorrowRequestDTO requestDTO = new BorrowRequestDTO(1L, List.of(1L, 2L));

        List<BorrowResponseDTO> mockResponses = Arrays.asList(
                new BorrowResponseDTO(1L, true, "Book borrowed successfully"),
                new BorrowResponseDTO(2L, false, "Book is already borrowed"));

        Mockito.when(circulationService.borrowBooks(any())).thenReturn(mockResponses);

        mockMvc.perform(post("/api/v1/circulation/borrow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].bookId").value(1))
                .andExpect(jsonPath("$[0].success").value(true))
                .andExpect(jsonPath("$[0].message").value("Book borrowed successfully"));
    }

    @Test
    void testReturnBooks() throws Exception {
        BorrowRequestDTO requestDTO = new BorrowRequestDTO(1L, List.of(1L));

        List<BorrowResponseDTO> mockResponses = List.of(
                new BorrowResponseDTO(1L, true, "Book returned successfully"));

        Mockito.when(circulationService.returnBooks(any())).thenReturn(mockResponses);

        mockMvc.perform(post("/api/v1/circulation/return")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].bookId").value(1))
                .andExpect(jsonPath("$[0].success").value(true))
                .andExpect(jsonPath("$[0].message").value("Book returned successfully"));
    }

    @Test
    void testBorrowBooksWithMissingUserId() throws Exception {
        // missing "user_id"
        String requestJson = """
                    {
                        "book_ids": [1, 2]
                    }
                """;

        mockMvc.perform(post("/api/v1/circulation/borrow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("user_id")));
    }

    @Test
    void testBorrowBooksWithEmptyBookIds() throws Exception {
        BorrowRequestDTO invalidRequest = new BorrowRequestDTO(1L, List.of());

        mockMvc.perform(post("/api/v1/circulation/borrow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("book_ids")));
    }

    @Test
    void testBorrowBooksWithNullBookIds() throws Exception {
        String requestJson = """
                    {
                        "user_id": 1
                    }
                """;

        mockMvc.perform(post("/api/v1/circulation/borrow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("book_ids")));
    }

    @Test
    void testReturnBooksWithNonNumericBookId() throws Exception {
        String invalidJson = """
                    {
                        "user_id": 1,
                        "book_ids": ["abc"]
                    }
                """;

        mockMvc.perform(post("/api/v1/circulation/return")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testReturnBooksWithMissingPayload() throws Exception {
        mockMvc.perform(post("/api/v1/circulation/return")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
