package com.example.teamproject.controller;

import com.example.teamproject.service.MedicineService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineService medicineService;

    @GetMapping("/search")
    public String search() {
        return "search";
    }

    @GetMapping("/medicine")
    public String showMedicine(Model model, HttpSession session) {
        Long id = Long.parseLong(session.getAttribute("id").toString());
        if(id == null) {
            return "/login";
        }
        model.addAttribute("medicines", medicineService.getMedicineListByUserId(id));
        return "showMedicine";
    }

    @GetMapping("/medicine/{id}/detail")
    public String showMedicineDetail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("medicine", medicineService.getMedicine(id));
        return "detail";
    }

    @GetMapping("/medicine/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        medicineService.deleteMedicine(id);
        return "redirect:/medicine";
    }
}
