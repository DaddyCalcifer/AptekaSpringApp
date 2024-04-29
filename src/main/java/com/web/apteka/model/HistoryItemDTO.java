package com.web.apteka.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "history_item")
@Data
public class HistoryItemDTO {
    @Id
    @Column(name = "id", unique = true, updatable = false)
    private Integer id;

    @Column(name = "user_id")
    private UUID user_id;

    @Column(name = "aid_id")
    private Integer aid_id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private double price;

    @Column(name = "bought_at", updatable = false)
    private LocalDateTime boughtAt;
}
