package com.example.teamproject.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "MEDICINES")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entpName;
    private String itemName;
    private String itemSeq;
    private String efcyQesitm;
    private String useMethodQesitm;
    private String atpnWarnQesitm;
    private String atpnQesitm;
    private String intrcQesitm;
    private String seQesitm;
    private String depositMethodQesitm;
    private String openDe;
    private String updateDe;
    private String itemImage;
    private String bizrno;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}