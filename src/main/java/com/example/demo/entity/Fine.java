package com.example.demo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Fine {
    private Long id;                // 罚金ID（使用Redis生成）
    private Integer userId;         // 用户ID
    private Integer borrowRecordId; // 借阅记录ID
    private Long amount;      // 罚金金额
    private Integer status;         // 状态：0-未支付，1-已支付
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime payTime;  // 支付时间（新增）


}
