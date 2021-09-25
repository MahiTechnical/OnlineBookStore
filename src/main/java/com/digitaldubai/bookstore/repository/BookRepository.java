package com.digitaldubai.bookstore.repository;

import com.digitaldubai.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
