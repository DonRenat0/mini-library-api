package com.example.mini_library_api.repository;

import com.example.mini_library_api.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = "authors")
    Optional<Book> findWithAuthorsById(Long id);
}