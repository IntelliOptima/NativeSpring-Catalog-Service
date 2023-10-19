package com.nativespring.catalogservice.BookSliceTests;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.nativespring.catalogservice.config.DataConfig;
import com.nativespring.catalogservice.domain.Book;
import com.nativespring.catalogservice.domain.BookRepository;

@DataJpaTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Disables the default behavior of relying
                                                                             // on an embedded database since we want to
                                                                             // use Testcontainers
@ActiveProfiles("integration")
public class BookRepositoryJPATests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findBookByIsbnWhenExisting() {
        String bookIsbn = "1234561237";
        Book book = Book.of(bookIsbn, "Title", "Author", 12.90, "Polarsophia");
        entityManager.persist(book);
        entityManager.flush();

        Optional<Book> actualBook = bookRepository.findByIsbn(bookIsbn);

        assertTrue(actualBook.isPresent());
        assertEquals(bookIsbn, actualBook.get().getIsbn());
    }

}
