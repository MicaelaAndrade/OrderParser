package com.micaelaandrade.orderparser.infrastructure.db.repository;

import com.micaelaandrade.orderparser.infrastructure.db.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByExternalId(Long externalId);
}
