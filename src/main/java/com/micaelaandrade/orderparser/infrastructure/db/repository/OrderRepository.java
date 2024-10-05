package com.micaelaandrade.orderparser.infrastructure.db.repository;

import com.micaelaandrade.orderparser.infrastructure.db.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByExternalId(Long externalId);

}
