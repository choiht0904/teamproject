package com.example.teamproject.controller;

import com.example.teamproject.dto.MedicineResultDto;
import com.example.teamproject.service.MedicineService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MedicineApiController {
    private final MedicineService medicineService;

    @GetMapping("/search")
    public ResponseEntity<List<MedicineResultDto>> search(@RequestParam(name = "query") String query,
                                                          @RequestParam(name = "pageno", defaultValue = "1") int no,
                                                          @RequestParam(name = "sort", defaultValue = "up") String sort) throws Exception {
        List<MedicineResultDto> results = medicineService.searchMedicine(query, no, sort);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/medicine")
    public ResponseEntity<String> create(@RequestBody MedicineResultDto medicineResultDto, HttpSession session) {
        Long id = Long.parseLong(session.getAttribute("id").toString());
        medicineService.createMedicine(id, medicineResultDto);
        return ResponseEntity.ok("delete complete");
    }
}
