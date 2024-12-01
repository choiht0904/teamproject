package com.example.teamproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@ToString
@Slf4j
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String email;
    private String password;
    private String medicine="복용중인 약품을 입력하세요.";

    public void logInfo() {
        log.info("id: {}, email: {}, password: {}, medicine: {}", id, email, password, medicine);
    }

}
