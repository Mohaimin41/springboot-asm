package com.sisimpur.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sisimpur.library.dto.*;
import com.sisimpur.library.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetAllUsers() throws Exception {
        List<UserDTO> users = List.of(
                new UserDTO(1L, "Alice", "alice@example.com", List.of()),
                new UserDTO(2L, "Bob", "bob@example.com", List.of()));

        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice"));
    }

    @Test
    void testGetUserById() throws Exception {
        List<BookSummaryDTO> books = List.of(
                new BookSummaryDTO(1L, "Book One", "Fiction", 2020));

        UserDTO user = new UserDTO(1L, "Alice", "alice@example.com", books);

        Mockito.when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.currentlyBorrowedBooks[0].title").value("Book One"));
    }

    @Test
    void testCreateUser() throws Exception {
        UserCreateReqDTO req = new UserCreateReqDTO("Alice", "alice@example.com");
        UserDTO created = new UserDTO(1L, "Alice", "alice@example.com", List.of());

        Mockito.when(userService.createUser(any(UserCreateReqDTO.class))).thenReturn(created);

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.currentlyBorrowedBooks").isArray());
    }

    @Test
    void testUpdateUser() throws Exception {
        UserCreateReqDTO req = new UserCreateReqDTO("Updated", "updated@example.com");
        UserDTO updated = new UserDTO(1L, "Updated", "updated@example.com", List.of());

        Mockito.when(userService.updateUser(eq(1L), any(UserCreateReqDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }

    @Test
    void testCreateUser_InvalidRequest() throws Exception {
        UserCreateReqDTO req = new UserCreateReqDTO("", "invalid-email");

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("name")))
                .andExpect(content().string(containsString("email")));
    }

    @Test
    void testGetUserById_InvalidId() throws Exception {
        mockMvc.perform(get("/api/v1/users/-5"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("must be positive")));
    }
}
