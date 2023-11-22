package com.example.backend.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_details")
public class OrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private OrderDetailPK orderId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "price")
    private BigDecimal price;

    public OrderDetailPK getOrderId() {
        return orderId;
    }

    public void setOrderId(OrderDetailPK orderId) {
        this.orderId = orderId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Embeddable
    public static class OrderDetailPK implements Serializable {
        @Column(name = "detail_id")
        private Long detailId;
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "order_id")
        private Orders orders;

        public Long getDetailId() {
            return detailId;
        }

        public void setDetailId(Long detailId) {
            this.detailId = detailId;
        }

        public Orders getOrders() {
            return orders;
        }

        public void setOrders(Orders orders) {
            this.orders = orders;
        }

    }

}
