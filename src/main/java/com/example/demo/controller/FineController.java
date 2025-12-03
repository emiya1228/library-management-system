package com.example.demo.controller;

import com.example.demo.config.UserContext;
import com.example.demo.entity.Fine;
import com.example.demo.service.FineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/fine")
public class FineController {

    @Resource
    private FineService fineService;

    @PostMapping("/{id}")
    public ResponseEntity<?> payFine(@PathVariable Long id) {
        Fine fine = fineService.payFine(id);
        return ResponseEntity.ok(fine);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserFine() {
        Integer currentUserId = UserContext.getCurrentUserId();
        List<Fine> fineByUserId = fineService.getFineByUserId(currentUserId);
        return ResponseEntity.ok(fineByUserId);
    }

    @GetMapping("/fineId/{id}")
    public ResponseEntity<?> getUserFine(@PathVariable Long id) {
        Fine fineByUserId = fineService.getFineById(id);
        return ResponseEntity.ok(fineByUserId);
    }

    @GetMapping("/unpaid")
    public ResponseEntity<?> getUnpaidUserFine() {
        Integer currentUserId = UserContext.getCurrentUserId();
        List<Fine> fineByUserId = fineService.getUnpaidFineByUserId(currentUserId);
        return ResponseEntity.ok(fineByUserId);
    }
}
