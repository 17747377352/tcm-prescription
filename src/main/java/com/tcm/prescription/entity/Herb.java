package com.tcm.prescription.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tcm_herb")
public class Herb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String nameLatin;
    private String nameAlias;
    private String source;
    private String processing;
    private String nature;          // 四气
    private String flavor;          // 五味(JSON)
    private String meridian;        // 归经(JSON)
    private String direction;       // 升降浮沉
    @Column(columnDefinition = "TEXT")
    private String efficacy;
    @Column(columnDefinition = "TEXT")
    private String indications;
    @Column(columnDefinition = "TEXT")
    private String pharmacology;
    private Integer toxicityLevel;
    private BigDecimal dosageMin;
    private BigDecimal dosageMax;
    private String dosageUnit;
    @Column(columnDefinition = "TEXT")
    private String contraindications;
    private String incompatibility;
    @Column(columnDefinition = "TEXT")
    private String classicFormulas;
    private String imageUrl;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}