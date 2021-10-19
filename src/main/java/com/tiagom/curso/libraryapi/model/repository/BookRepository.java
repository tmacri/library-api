package com.tiagom.curso.libraryapi.model.repository;

import com.tiagom.curso.libraryapi.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
    boolean existsByIsbn(String isbn);
}
