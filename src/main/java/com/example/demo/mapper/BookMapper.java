package com.example.demo.mapper;

import com.example.demo.entity.Book;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookMapper {
    int addBook(Book book);

    boolean updateBook(Book book);

    boolean deleteBook(Integer id);

    Book getBookById(Integer id);

    List<Book> getBooksByCategory(String category);

    boolean updateBookCopies(Integer id, Integer change);

    /**
     * 直接减少库存
     * @return 受影响行数（1=成功，0=库存不足或图书不存在）
     */
    int decreaseAvailableCopies(Integer bookId, Integer count);

    /**
     * 直接增加库存
     */
    int increaseAvailableCopies(Integer bookId, Integer count);
}
