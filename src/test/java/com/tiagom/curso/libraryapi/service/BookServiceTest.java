package com.tiagom.curso.libraryapi.service;

import com.tiagom.curso.libraryapi.exception.BusinessException;
import com.tiagom.curso.libraryapi.model.entity.Book;
import com.tiagom.curso.libraryapi.model.repository.BookRepository;
import com.tiagom.curso.libraryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest() {
        //cenário
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(book)).thenReturn(
                Book.builder()
                        .id(11)
                        .isbn("123")
                        .title("chanas viajantes")
                        .author("Belmito Amaral")
                        .build());

        //execução
        Book savedBook = service.save(book);

        //verificação
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTitle()).isEqualTo("chanas viajantes");
        assertThat(savedBook.getAuthor()).isEqualTo("Belmito Amaral");
    }

    @Test
    @DisplayName("Deve lançar um erro de validação ao tentar cadastrar livro com isbn já em uso")
    public void shouldNotSaveABookWithDuplicatedIsbn() {
        //cenário
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        //execução
        Throwable ex = Assertions.catchThrowable(() -> service.save(book));

        //verificação
        assertThat(ex)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado.");

        Mockito.verify(repository, Mockito.never()).save(book);
    }

    private Book createValidBook() {
        return Book.builder().isbn("123").author("Fulano").title("As piripécias").build();
    }
}
