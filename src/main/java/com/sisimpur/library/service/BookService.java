package com.sisimpur.library.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sisimpur.library.dto.BookDTO;
import com.sisimpur.library.model.Book;
import com.sisimpur.library.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public BookDTO getBookDTO(Long id) {
        Book book = bookRepository.findById(id).orElse(null);

        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getGenre(),
                book.getPublishedYear(),
                book.getAuthor().getName());
    }

    public List<BookDTO> getAllBookDTOs() {
        return bookRepository.findAll().stream()
                .map(book -> new BookDTO(
                        book.getId(),
                        book.getTitle(),
                        book.getGenre(),
                        book.getPublishedYear(),
                        book.getAuthor().getName()))
                .collect(Collectors.toList());
    }
}
