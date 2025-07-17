package com.sisimpur.library.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sisimpur.library.dto.BookCreateReqDTO;
import com.sisimpur.library.dto.BookDTO;
import com.sisimpur.library.exception.ResourceNotFoundException;
import com.sisimpur.library.model.Author;
import com.sisimpur.library.model.Book;
import com.sisimpur.library.repository.AuthorRepository;
import com.sisimpur.library.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

        private final BookRepository bookRepository;
        private final AuthorRepository authorRepository;

        public BookDTO getBookDTO(Long id) {
                Book book = bookRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

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

        public BookDTO createBook(BookCreateReqDTO request) {
                Author author = authorRepository.findById(request.getAuthor_id())
                                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

                Book book = new Book();
                book.setTitle(request.getTitle());
                book.setGenre(request.getGenre());
                book.setPublishedYear(request.getPublished_year());
                book.setAuthor(author);

                Book savedBook = bookRepository.save(book);

                return new BookDTO(
                                savedBook.getId(),
                                savedBook.getTitle(),
                                savedBook.getGenre(),
                                savedBook.getPublishedYear(),
                                savedBook.getAuthor().getName());
        }

        public void deleteBookById(Long id) {
                Book book = bookRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
                bookRepository.delete(book);
        }

        public BookDTO updateBook(Long id, BookCreateReqDTO request) {
                Book book = bookRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

                Author author = authorRepository.findById(request.getAuthor_id())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Author not found with id: " + request.getAuthor_id()));

                book.setTitle(request.getTitle());
                book.setGenre(request.getGenre());
                book.setPublishedYear(request.getPublished_year());
                book.setAuthor(author);

                Book updatedBook = bookRepository.save(book);

                return new BookDTO(
                                updatedBook.getId(),
                                updatedBook.getTitle(),
                                updatedBook.getGenre(),
                                updatedBook.getPublishedYear(),
                                updatedBook.getAuthor().getName());
        }

}
