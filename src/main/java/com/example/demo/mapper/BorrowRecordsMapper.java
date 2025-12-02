package com.example.demo.mapper;

import com.example.demo.entity.BorrowRecord;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BorrowRecordsMapper {
    BorrowRecord selectById(Integer id);
    List<BorrowRecord> selectByUserId(Integer userId);
    List<BorrowRecord> selectByBookId(Integer bookId);
    List<BorrowRecord> selectCurrentBorrowsByUserId(Integer userId);
    List<BorrowRecord> selectOverdueRecords();
    int insert(BorrowRecord record);
    int update(BorrowRecord record);
    int returnBook(Integer recordId);
    int renewBook(Integer recordId);
    int countUserBorrows(Integer userId);
}