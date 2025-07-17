package com.sisimpur.library.controller;

import com.sisimpur.library.dto.AuthorCreateReqDTO;
import com.sisimpur.library.dto.AuthorDTO;
import com.sisimpur.library.dto.AuthorUpdateReqDTO;
import com.sisimpur.library.service.AuthorService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
@Validated
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<AuthorDTO> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(
            @PathVariable @Positive(message = "Author ID must be positive") Long id) {
        AuthorDTO author = authorService.getAuthorById(id);
        return ResponseEntity.ok(author);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(
            @PathVariable @Positive(message = "Author ID must be positive") Long id,
            @Valid @RequestBody AuthorUpdateReqDTO request) {

        AuthorDTO updated = authorService.updateAuthor(id, request);
        return ResponseEntity.ok(updated);
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@Valid @RequestBody AuthorCreateReqDTO request) {
        AuthorDTO createdAuthor = authorService.createAuthor(request);
        return ResponseEntity.status(201).body(createdAuthor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthor(
            @PathVariable @Positive(message = "Author ID must be positive") Long id) {

        authorService.deleteAuthor(id);
        return ResponseEntity.ok("Author deleted successfully");
    }

}
