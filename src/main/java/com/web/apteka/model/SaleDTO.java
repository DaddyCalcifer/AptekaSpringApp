package com.web.apteka.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table(name = "sales")
@Entity
public class SaleDTO {
    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "aid_id")
    private Integer aid_id;

    @Column(name = "SALE_PERCENT")
    private Integer salePercent;

    @Column(name = "valid_until", updatable = false)
    private LocalDateTime validUntil;
}
