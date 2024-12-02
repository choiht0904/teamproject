package com.example.teamproject.dto;

import com.example.teamproject.entity.User;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Slf4j
public class MemberForm {
    private Long id;
    private String email;
    private String password;


    public User toEntity() {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }


    public void logInfo() {
        log.info("email: {}, password: {}, medicine: {}", email, password );
    }
}
