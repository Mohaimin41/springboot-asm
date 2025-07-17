package com.sisimpur.library.service;

import com.sisimpur.library.dto.AuthorCreateReqDTO;
import com.sisimpur.library.dto.AuthorDTO;
import com.sisimpur.library.dto.AuthorUpdateReqDTO;
import com.sisimpur.library.dto.BookSummaryDTO;
import com.sisimpur.library.exception.ResourceNotFoundException;
import com.sisimpur.library.model.Author;
import com.sisimpur.library.model.Book;
import com.sisimpur.library.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        return convertToDTO(author);
    }

    public AuthorDTO createAuthor(AuthorCreateReqDTO request) {
        Author author = new Author();
        author.setName(request.getName());
        author.setBiography(request.getBiography());

        // map books
        List<Book> books = request.getBooks().stream().map(bookDto -> {
            Book book = new Book();
            book.setTitle(bookDto.getTitle());
            book.setGenre(bookDto.getGenre());
            book.setPublishedYear(bookDto.getPublished_year());
            book.setAuthor(author); // link back to author
            return book;
        }).collect(Collectors.toList());

        author.setBooks(books); // link books to author

        Author savedAuthor = authorRepository.save(author);
        return convertToDTO(savedAuthor);
    }

    public AuthorDTO updateAuthor(Long id, AuthorUpdateReqDTO request) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));

        author.setName(request.getName());
        author.setBiography(request.getBiography());

        Author updated = authorRepository.save(author);
        return convertToDTO(updated);
    }

    private AuthorDTO convertToDTO(Author author) {
        List<BookSummaryDTO> books = author.getBooks().stream()
                .map(book -> new BookSummaryDTO(
                        book.getId(),
                        book.getTitle(),
                        book.getGenre(),
                        book.getPublishedYear()))
                .collect(Collectors.toList());

        return new AuthorDTO(
                author.getId(),
                author.getName(),
                author.getBiography(),
                books);
    }
}
