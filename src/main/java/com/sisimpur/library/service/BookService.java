package com.sisimpur.library.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sisimpur.library.dto.BookCreateReqDTO;
import com.sisimpur.library.dto.BookDTO;
import com.sisimpur.library.exception.ResourceNotFoundException;
import com.sisimpur.library.model.Author;
import com.sisimpur.library.model.Book;
import com.sisimpur.library.model.Borrow;
import com.sisimpur.library.repository.AuthorRepository;
import com.sisimpur.library.repository.BookRepository;
import com.sisimpur.library.repository.BorrowRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

        private final BookRepository bookRepository;
        private final AuthorRepository authorRepository;
        private final BorrowRepository borrowRepository;

        public BookDTO getBookDTO(Long id) {
                Book book = bookRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

                boolean isAvailable = borrowRepository.findLatestByBookId(book.getId())
                                .map(b -> b.getReturnDate() != null)
                                .orElse(true);

                return new BookDTO(
                                book.getId(),
                                book.getTitle(),
                                book.getGenre(),
                                book.getPublishedYear(),
                                book.getAuthor().getName(),
                                isAvailable);
        }

        public List<BookDTO> getAllBookDTOs() {
                List<Borrow> activeBorrows = borrowRepository.findActiveBorrowsByBookIds(
                                bookRepository.findAll().stream().map(Book::getId).toList());
                Set<Long> borrowedBookIds = activeBorrows.stream()
                                .map(b -> b.getBook().getId())
                                .collect(Collectors.toSet());

                return bookRepository.findAll().stream()
                                .map(book -> new BookDTO(
                                                book.getId(),
                                                book.getTitle(),
                                                book.getGenre(),
                                                book.getPublishedYear(),
                                                book.getAuthor().getName(),
                                                !borrowedBookIds.contains(book.getId())))
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
                                savedBook.getAuthor().getName(),
                                true); // new books are available
        }

        public void deleteBookById(Long id) {
                Book book = bookRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

                boolean isBorrowed = borrowRepository.findLatestByBookId(id)
                                .map(b -> b.getReturnDate() == null)
                                .orElse(false);

                if (isBorrowed) {
                        throw new IllegalArgumentException("Cannot delete a book that is currently borrowed.");
                }

                bookRepository.delete(book);
        }

        public BookDTO updateBook(Long id, BookCreateReqDTO request) {
                Book book = bookRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

                boolean isAvailable = borrowRepository.findLatestByBookId(book.getId())
                                .map(b -> b.getReturnDate() != null)
                                .orElse(true);

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
                                updatedBook.getAuthor().getName(),
                                isAvailable);
        }

}
