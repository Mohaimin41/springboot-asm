package com.sisimpur.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sisimpur.library.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long>{
    
}
