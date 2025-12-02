package com.example.demo.controller;

import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Resource
    private BookService bookService;
    @PostMapping
    public ResponseEntity<Book> addBook(Book book) {
        Book book1 = bookService.addBook(book);
        return ResponseEntity.ok(book1);
    }

/*    @GetMapping
    public ResponseEntity<List<Book>> searchBooks(
            String title,
            String author,
            String category,
            String isbn) {
        // 多条件查询
    }*/

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Integer id, @RequestBody Book book) {
        boolean b = bookService.updateBook(id, book);
        if (b) {
            return ResponseEntity.ok(book);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Integer id) {
        bookService.deleteBook(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Integer id) {
        Book bookById = bookService.getBookById(id);
        return ResponseEntity.ok(bookById);
    }
}
