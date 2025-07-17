package com.sisimpur.library.controller;

import com.sisimpur.library.dto.BookDTO;
import com.sisimpur.library.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book-search")
@RequiredArgsConstructor
public class BookSearchController {

    private final BookSearchService bookSearchService;

    @GetMapping
    public ResponseEntity<List<BookDTO>> searchBooks(
            @RequestParam(required = false) String author_name,
            @RequestParam(required = false) String book_name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer publishing_year,
            @RequestParam(required = false) Boolean available
    ) {
        return ResponseEntity.ok(
                bookSearchService.searchBooks(author_name, book_name, category, publishing_year, available)
        );
    }
}
