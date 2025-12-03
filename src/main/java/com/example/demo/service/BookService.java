package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.utils.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Book> recommendBooks (Integer userId){
        //获取该用户借阅最多的三种类别
        List<Book> categoryByUserId = bookMapper.getCategoryByUserId(userId);
        if (categoryByUserId == null) {
            return new ArrayList<>();
        }
        List<String> categories = categoryByUserId.stream().map(Book::getCategory).collect(Collectors.toList());
        //获取该用户指定类别里没看过的且库存里还有的最热门的书
        List<Book> recommendBooks = bookMapper.getRecommendBooks(categories, userId);
        if (recommendBooks == null) {
            return new ArrayList<>();
        }
        return recommendBooks;
    }
}
