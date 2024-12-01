package com.example.teamproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeaderDto {
    private String resultCode;
    private String resultMsg;
}
