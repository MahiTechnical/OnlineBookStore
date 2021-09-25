package com.digitaldubai.bookstore;

import com.digitaldubai.bookstore.entity.Book;
import com.digitaldubai.bookstore.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class OnlineBookStore {

    // start everything
    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStore.class, args);
    }

    // run this only on profile 'demo', avoid run this in test
    @Profile("demo")
    @Bean
    CommandLineRunner initDatabase(BookRepository repository) {

        return args -> {
            repository.save(new Book("A Guide to the Bodhisattva Way of Life", "Santideva", 15.41,"","9780080391683","Fiction"));
            repository.save(new Book("The Life-Changing Magic of Tidying Up", "Marie Kondo", 9.69,"","9780067091683","Comics"));
            repository.save(new Book("Refactoring: Improving the Design of Existing Code", "Martin Fowler", 47.99,"","9780060391683","Computers"));
        };
    }
}