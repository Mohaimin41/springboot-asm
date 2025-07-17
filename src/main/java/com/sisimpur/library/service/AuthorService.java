package com.sisimpur.library.service;

import com.sisimpur.library.dto.AuthorDTO;
import com.sisimpur.library.dto.BookSummaryDTO;
import com.sisimpur.library.exception.ResourceNotFoundException;
import com.sisimpur.library.model.Author;
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
                books
        );
    }
}
