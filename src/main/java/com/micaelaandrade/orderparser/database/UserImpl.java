package com.micaelaandrade.orderparser.database;

import com.micaelaandrade.orderparser.database.entity.UserEntity;
import com.micaelaandrade.orderparser.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserImpl {
    private final UserRepository userRepository;

    public UserEntity findOrSaveUser(UserEntity userEntity) {
        return userRepository.findByExternalId(userEntity.getExternalId())
                .orElseGet(() -> userRepository.save(userEntity));

    }

    public UserEntity findByUserId(Long externalId) {
        return userRepository.findByExternalId(externalId)
                .orElseGet(() -> null);

    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }


}