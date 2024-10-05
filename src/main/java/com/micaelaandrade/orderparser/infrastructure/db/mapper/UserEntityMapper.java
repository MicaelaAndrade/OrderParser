package com.micaelaandrade.orderparser.infrastructure.db.mapper;

import com.micaelaandrade.orderparser.infrastructure.db.entity.UserEntity;
import com.micaelaandrade.orderparser.controller.domain.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserEntityMapper {

    public static User toDomain(UserEntity userEntity) {
        return User.builder()
                .userId(userEntity.getId())
                .name(userEntity.getName())
                .id(userEntity.getExternalId())
                .build();
    }
}
