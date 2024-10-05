package com.micaelaandrade.orderparser.infrastructure.db.repository;

import com.micaelaandrade.orderparser.infrastructure.db.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {}

