package com.adminmodule.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class GradeInputDTO {
    private UUID courseId;
    private UUID studentId;
    
    // BigDecimal: đảm bảo độ chính xác của số thập phân (vd: 8.5)
    private BigDecimal grade; 
}