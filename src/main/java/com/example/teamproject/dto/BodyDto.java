package com.example.teamproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BodyDto {
    private int pageNo;
    private int totalCount;
    private int numOfRows;
    private List<MedicineResultDto> items;
}
