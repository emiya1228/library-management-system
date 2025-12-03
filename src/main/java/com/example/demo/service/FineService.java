package com.example.demo.service;

import com.example.demo.constant.Constants;
import com.example.demo.entity.Fine;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.FineMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FineService {
    @Resource
    private FineMapper fineMapper;

    public Fine payFine(Long id) {
        Fine fine = fineMapper.selectById(id);
        if (fine == null) {
            throw new ServiceException("未查询到执行罚金记录");
        }
        fine.setStatus(Constants.STATUS_PAID);
        fine.setPayTime(LocalDateTime.now());
        int res = fineMapper.updateToPaid(id, LocalDateTime.now());
        if (res != 1) {
            throw new ServiceException("支付罚金失败");
        }
        return fine;
    }

    public Fine getFineById(Long id) {
        Fine fine = fineMapper.selectById(id);
        if (fine == null) {
            throw new ServiceException("未查询到执行罚金记录");
        }
        return fine;
    }

    public List<Fine> getFineByUserId(Integer userId) {
        List<Fine> fines = fineMapper.selectByUserId(userId);
        if (fines.isEmpty()) {
            throw new ServiceException("未查询到执行罚金记录");
        }
        return fines;
    }

    public List<Fine> getUnpaidFineByUserId(Integer userId) {
        List<Fine> fines = fineMapper.selectUnpaidByUserId(userId);
        if (fines.isEmpty()) {
            throw new ServiceException("未查询到执行罚金记录");
        }
        return fines;
    }
}
