package com.micaelaandrade.orderparser.database.repository;

import com.micaelaandrade.orderparser.database.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByExternalId(Long externalId);
    List<OrderEntity> findAllByUserId(Long userId);

}
