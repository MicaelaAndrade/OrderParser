package com.micaelaandrade.orderparser.database.repository;

import com.micaelaandrade.orderparser.database.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {}
