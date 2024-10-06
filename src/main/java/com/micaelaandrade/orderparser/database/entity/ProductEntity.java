package com.micaelaandrade.orderparser.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "`product`")
@Setter
@EqualsAndHashCode(exclude = "order")

public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long externalId;

    private BigDecimal value;

    private BigDecimal totalValue;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

}