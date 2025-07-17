package com.sisimpur.library.service;

import com.sisimpur.library.dto.BookDTO;
import com.sisimpur.library.model.Book;
import com.sisimpur.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookSearchService {

    private final BookRepository bookRepository;

    public List<BookDTO> searchBooks(String authorName, String bookName, String category, Integer publishingYear) {
        List<Book> books = bookRepository.findAll();

        return books.stream()
                .filter(book -> authorName == null || book.getAuthor().getName().toLowerCase().contains(authorName.toLowerCase()))
                .filter(book -> bookName == null || book.getTitle().toLowerCase().contains(bookName.toLowerCase()))
                .filter(book -> category == null || book.getGenre() != null && book.getGenre().toLowerCase().contains(category))
                .filter(book -> publishingYear == null || book.getPublishedYear() == publishingYear)
                .map(book -> new BookDTO(
                        book.getId(),
                        book.getTitle(),
                        book.getGenre(),
                        book.getPublishedYear(),
                        book.getAuthor().getName()
                ))
                .collect(Collectors.toList());
    }
}
