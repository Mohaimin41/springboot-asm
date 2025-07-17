package com.sisimpur.library.service;

import com.sisimpur.library.dto.BookDTO;
import com.sisimpur.library.model.Book;
import com.sisimpur.library.repository.BookRepository;
import com.sisimpur.library.repository.BorrowRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookSearchService {

    private final BookRepository bookRepository;
    private final BorrowRepository borrowRepository;

    public List<BookDTO> searchBooks(String authorName, String bookName, String category, Integer publishingYear, Boolean available) {
        List<Book> books = bookRepository.findAll();

        // Get set of IDs that are currently borrowed
        Set<Long> borrowedIds = borrowRepository.findActiveBorrowsByBookIds(
                books.stream().map(Book::getId).toList()).stream().map(b -> b.getBook().getId())
                .collect(Collectors.toSet());

        return books.stream()
                .filter(book -> authorName == null
                        || book.getAuthor().getName().toLowerCase().contains(authorName.toLowerCase()))
                .filter(book -> bookName == null || book.getTitle().toLowerCase().contains(bookName.toLowerCase()))
                .filter(book -> category == null
                        || book.getGenre() != null && book.getGenre().toLowerCase().contains(category))
                .filter(book -> publishingYear == null || book.getPublishedYear() == publishingYear)
                .filter(book -> {
                    boolean isAvailable = !borrowedIds.contains(book.getId());
                    return available == null || isAvailable == available;
                })
                .map(book -> new BookDTO(
                        book.getId(),
                        book.getTitle(),
                        book.getGenre(),
                        book.getPublishedYear(),
                        book.getAuthor().getName(),
                        !borrowedIds.contains(book.getId())))
                .collect(Collectors.toList());
    }
}
