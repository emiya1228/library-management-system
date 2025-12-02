package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.utils.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class BookService {
    @Resource
    private BookMapper bookMapper;
    @Resource
    private RedisUtil redisUtil;

    private static final String KEY_PREFIX = "book:";

    public Book addBook(Book book){
        book.setCreatedTime(LocalDateTime.now());
        int result = bookMapper.addBook(book);

        if (result > 0) {
            // 插入成功，返回带有id的book对象
            return book;
        } else {
            throw new ServiceException("添加图书失败");
        }
    };
    public boolean updateBook(Integer id, Book book){
        book.setId(id);
        redisUtil.del(KEY_PREFIX + id);
        return bookMapper.updateBook(book);
    };
    public boolean deleteBook(Integer bookId){
        return bookMapper.deleteBook(bookId);
    };
    public Book getBookById(Integer bookId){
        Book res = (Book)redisUtil.get(KEY_PREFIX + bookId);
        if (res != null) {
            return res;
        }
        Book book = bookMapper.getBookById(bookId);
        if (book != null) {
            redisUtil.set(KEY_PREFIX + bookId, book);
        }
        return bookMapper.getBookById(bookId);
    };
    //List<Book> searchBooks(BookQuery query){};
    public List<Book> getBooksByCategory(String category){
        return bookMapper.getBooksByCategory(category);
    };
    public boolean updateBookCopies(Integer bookId, Integer change){
        return bookMapper.updateBookCopies(bookId, change);
    };
}
