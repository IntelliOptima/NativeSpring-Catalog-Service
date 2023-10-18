package com.nativespring.catalogservice.persistence;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import com.nativespring.catalogservice.domain.Book;
import com.nativespring.catalogservice.domain.BookRepository;

@Repository
public class InMemoryBookRepository implements BookRepository {

    private static final Map<String, Book> books = new ConcurrentHashMap<>();

    @Override
    public Iterable<Book> findAll() {
        return books.values();
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return existsByIsbn(isbn) ? Optional.of(books.get(isbn)) : Optional.empty();
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return books.containsKey(isbn);
    }

    @Override
    public Book save(Book book) {
        books.put(book.isbn(), book);
        return book;
    }

    @Override
    public void deleteByIsbn(String isbn) {
        books.remove(isbn);
    }

}
