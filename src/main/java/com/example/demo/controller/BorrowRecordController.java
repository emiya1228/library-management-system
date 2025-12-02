package com.example.demo.controller;


import com.example.demo.entity.BorrowRecord;
import com.example.demo.service.BorrowService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrow")
@Validated
public class BorrowRecordController {

    @Autowired
    private BorrowService borrowService;

    /**
     * 借阅图书
     */
    @PostMapping
    public ResponseEntity<?> borrowBook(@RequestBody @Validated BorrowRecord in) {
        BorrowRecord borrowRecord = borrowService.borrowBook(in.getUserId(), in.getBookId());
        return new ResponseEntity<>(borrowRecord, HttpStatus.OK);
    }

    /**
     * 归还图书
     */
    @PutMapping("/return")
    public ResponseEntity<?> returnBook(@RequestBody @Validated BorrowRecord in) {
        BorrowRecord borrowRecord = borrowService.returnBook(in.getUserId(), in.getId(), in.getBookId());
        return new ResponseEntity<>(borrowRecord, HttpStatus.OK);
    }

    /**
     * 续借图书
     */
    @PutMapping("/{recordId}/renew")
    public ResponseEntity<?> renewBook(@PathVariable Integer recordId) {
        BorrowRecord borrowRecord = borrowService.renewBook(recordId);
        return new ResponseEntity<>(borrowRecord, HttpStatus.OK);
    }



}