package com.web.apteka.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "aids")
@Data
public class AidDTO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "imageURL")
    private String imageURL;

    @Column(name = "description")
    private String description;

    @Column(name="price")
    private double price;

    @Column(name = "quantity")
    private Integer quantity;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "discount_percent", nullable = false)
    private Integer discountPercent;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
