package com.micaelaandrade.orderparser.infrastructure.db;

import com.micaelaandrade.orderparser.infrastructure.db.entity.UserEntity;
import com.micaelaandrade.orderparser.infrastructure.db.mapper.UserEntityMapper;
import com.micaelaandrade.orderparser.infrastructure.db.repository.UserRepository;
import com.micaelaandrade.orderparser.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserImp {

    private final UserRepository userRepository;

    public User findUserById(Long userExternalId) {
        return userRepository.findByExternalId(userExternalId)
                .map(UserEntityMapper::toDomain)
                .orElseGet(() -> null);
    }

    public Optional<UserEntity> findUser(UserEntity user) {
        return userRepository.findByExternalId(user.getExternalId());
    }
}
