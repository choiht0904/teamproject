package com.example.teamproject.dto;

import com.example.teamproject.entity.Member;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private String medicine="복용중인 약품을 입력하세요.";


    public Member toEntity() {
        return new Member(id,email, password, medicine);
    }


    public void logInfo() {
        log.info("email: {}, password: {}, medicine: {}", email, password, medicine );
    }
}
