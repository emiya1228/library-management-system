package com.example.demo.service;

import com.example.demo.constant.Constants;
import com.example.demo.entity.Book;
import com.example.demo.entity.BorrowRecord;
import com.example.demo.entity.Fine;
import com.example.demo.entity.User;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.BookMapper;
import javax.annotation.Resource;
import com.example.demo.mapper.BorrowRecordsMapper;
import com.example.demo.mapper.FineMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.utils.RedisUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Transactional
public class BorrowService {

    @Resource
    private BorrowRecordsMapper borrowRecordsMapper;

    @Resource
    private BookMapper bookMapper;
    @Resource
    private FineMapper fineMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisUtil redisUtil;
    private final Map<Integer, Object> bookLocks = new ConcurrentHashMap<>();
    // 业务规则常量
    private static final int MAX_BORROW_COUNT = 5;
    private static final int BORROW_DAYS = 30;
    private static final int RENEW_DAYS = 15;
    private static final int MAX_RENEW_COUNT = 1;

    public BorrowRecord borrowBook(Integer userId, Integer bookId) {
        // 1. 验证用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在", 40001);
        }

        // 2. 验证图书是否存在
        Book book = bookMapper.getBookById(bookId);
        if (book == null) {
            throw new ServiceException("图书不存在", 40002);
        }

        // 3. 检查图书是否可借
        if (book.getAvailableCopies() <= 0) {
            throw new ServiceException("图书已被借完", 40003);
        }

        // 4. 检查用户借阅数量
/*        int currentBorrowCount = borrowRecordsMapper.countUserBorrows(userId);
        if (currentBorrowCount >= MAX_BORROW_COUNT) {
            throw new ServiceException("借阅数量已达上限（最多" + MAX_BORROW_COUNT + "本）", 40004);
        }*/

        // 5. 检查用户是否已借阅该书
        List<BorrowRecord> userBorrows = borrowRecordsMapper.selectCurrentBorrowsByUserId(userId);
        boolean alreadyBorrowed = userBorrows.stream()
                .anyMatch(record -> record.getBookId().equals(bookId));
        if (alreadyBorrowed) {
            throw new ServiceException("您已借阅该图书", 40005);
        }

        try {
            // 6. 创建借阅记录
            BorrowRecord record = new BorrowRecord();
            record.setUserId(userId);
            record.setBookId(bookId);

            record.setBorrowDate(LocalDate.now());
            // borrow_date使用数据库默认值CURRENT_TIMESTAMP
            record.setDueDate(LocalDate.now().plusDays(30));
            // return_date默认为null
            record.setStatus(Constants.STATUS_BORROW);
            record.setRenewCount(0);
            Object lock = bookLocks.computeIfAbsent(bookId, k -> new Object());

            synchronized (lock) {
                int insertResult = borrowRecordsMapper.insert(record);
                if (insertResult <= 0) {
                    throw new ServiceException("创建借阅记录失败", 50001);
                }

                // 7. 更新图书库存
                int updateResult = bookMapper.decreaseAvailableCopies(bookId, 1);
                if (updateResult <= 0) {
                    throw new ServiceException("更新图书库存失败", 50002);
                }
            }

            // 8. 返回完整的借阅记录信息
            return borrowRecordsMapper.selectById(record.getId());

        } catch (Exception e) {
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new ServiceException("借阅图书失败: " + e.getMessage(), 50000);
        }
    }

    public BorrowRecord returnBook(Integer userId, Integer recordId, Integer bookId) {
        BorrowRecord borrowRecord = borrowRecordsMapper.selectById(recordId);
        if(borrowRecord == null) {
            throw new ServiceException("未查询到此借阅记录");
        }
        User user = userMapper.selectById(userId);
        if(user == null || !user.getId().equals(borrowRecord.getUserId())) {
            throw new ServiceException("用户信息不匹配，请核对");
        }
        Book book = bookMapper.getBookById(bookId);
        if(book == null || !book.getId().equals(borrowRecord.getBookId())) {
            throw new ServiceException("书籍信息不匹配，请核对");
        }
        borrowRecord.setStatus("已归还");
        borrowRecord.setReturnDate(LocalDate.now());

        Object lock = bookLocks.computeIfAbsent(bookId, k -> new Object());

        synchronized (lock) {
            int insertResult =borrowRecordsMapper.update(borrowRecord);

            if (insertResult <= 0) {
                throw new ServiceException("归还书籍失败", 50001);
            }

            // 7. 更新图书库存
            int updateResult = bookMapper.increaseAvailableCopies(bookId, 1);
            if (updateResult <= 0) {
                throw new ServiceException("更新图书库存失败", 50002);
            }
            if (borrowRecord.getReturnDate().isAfter(borrowRecord.getDueDate())) {
                Fine fine = new Fine();
                fine.setUserId(userId);
                fine.setBorrowRecordId(recordId);
                fine.setStatus(Constants.STATUS_UNPAID);
                fine.setCreateTime(LocalDateTime.now());
                fine.setAmount(ChronoUnit.DAYS.between(borrowRecord.getDueDate(), borrowRecord.getReturnDate()));
                fine.setId(redisUtil.incr(Constants.FINE, 1));
                fineMapper.insert(fine);
            }
        }
        borrowRecordsMapper.update(borrowRecord);
        return borrowRecord;
    }

    public BorrowRecord renewBook(Integer recordId) {
        BorrowRecord borrowRecord = borrowRecordsMapper.selectById(recordId);
        if(borrowRecord == null) {
            throw new ServiceException("未查询到此借阅记录");
        }

        if(borrowRecord.getRenewCount() >= MAX_RENEW_COUNT) {
            throw new ServiceException("已超过续借次数");
        }
        borrowRecord.setDueDate(borrowRecord.getDueDate().plusDays(RENEW_DAYS));
        borrowRecord.setRenewCount(borrowRecord.getRenewCount() + 1);
        borrowRecordsMapper.update(borrowRecord);
        return borrowRecord;
    }
}