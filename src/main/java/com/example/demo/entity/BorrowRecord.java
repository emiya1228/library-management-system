package com.example.demo.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "borrow_records")
@Data
public class BorrowRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "user_id", nullable = false)
    private Integer userId;


    @JoinColumn(name = "book_id", nullable = false)
    private Integer bookId;

    @Column(name = "borrow_date")
    private LocalDate borrowDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;


    @Column(name = "status")
    private String status;

    @Column(name = "renew_count")
    private Integer renewCount = 0;
}