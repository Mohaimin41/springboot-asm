package com.sisimpur.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sisimpur.library.model.Borrow;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    @Query("SELECT b FROM Borrow b WHERE b.book.id = :bookId ORDER BY b.borrowDate DESC LIMIT 1")
    Optional<Borrow> findLatestByBookId(@Param("bookId") Long bookId);

    @Query("SELECT b FROM Borrow b WHERE b.book.id IN :bookIds AND b.returnDate IS NULL")
    List<Borrow> findActiveBorrowsByBookIds(@Param("bookIds") List<Long> bookIds);
}