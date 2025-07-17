package com.sisimpur.library.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sisimpur.library.dto.BorrowRequestDTO;
import com.sisimpur.library.dto.BorrowResponseDTO;
import com.sisimpur.library.exception.ResourceNotFoundException;
import com.sisimpur.library.model.Book;
import com.sisimpur.library.model.Borrow;
import com.sisimpur.library.model.User;
import com.sisimpur.library.repository.BookRepository;
import com.sisimpur.library.repository.BorrowRepository;
import com.sisimpur.library.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CirculationService {

    private final BorrowRepository borrowRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public List<BorrowResponseDTO> borrowBooks(BorrowRequestDTO request) {
        // check user exists
        User user = userRepository.findById(request.getUser_id())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Book> books = bookRepository.findAllById(request.getBook_ids());

        List<BorrowResponseDTO> responses = new ArrayList<>();

        // loop over all requested books
        for (Book book : books) {
            // find lates borrow entry of book
            Optional<Borrow> latestBorrow = borrowRepository.findLatestByBookId(book.getId());

            // if book has a latest borrow date and no return date, it is not available in library
            if (latestBorrow.isPresent() && latestBorrow.get().getReturnDate() == null) {
                responses.add(new BorrowResponseDTO(book.getId(), false, "Book is already borrowed"));
                continue;
            }

            Borrow borrow = new Borrow();
            borrow.setUser(user);
            borrow.setBook(book);
            borrow.setBorrowDate(LocalDate.now());
            borrowRepository.save(borrow);

            responses.add(new BorrowResponseDTO(book.getId(), true, "Book borrowed successfully"));
        }

        return responses;
    }

    public List<BorrowResponseDTO> returnBooks(BorrowRequestDTO request) {
        // check user exists
        userRepository.findById(request.getUser_id())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<BorrowResponseDTO> responses = new ArrayList<>();

        // loop over returned books
        for (Long bookId : request.getBook_ids()) {
            // need to check if book is actually borrowed right now
            Optional<Borrow> borrowOpt = borrowRepository.findLatestByBookId(bookId);
            if (borrowOpt.isEmpty() || borrowOpt.get().getReturnDate() != null) {
                responses.add(new BorrowResponseDTO(bookId, false, "Book is not currently borrowed"));
                continue;
            }

            Borrow borrow = borrowOpt.get();
            borrow.setReturnDate(LocalDate.now());
            borrowRepository.save(borrow);

            responses.add(new BorrowResponseDTO(bookId, true, "Book returned successfully"));
        }

        return responses;
    }
}