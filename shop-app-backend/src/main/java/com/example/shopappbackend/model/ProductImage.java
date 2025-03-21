package com.example.shopappbackend.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_image")
@NoArgsConstructor
@Data
public class ProductImage implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "image")
    private byte[] image;
    @Column(name = "product_id")
    private String productId;
}
