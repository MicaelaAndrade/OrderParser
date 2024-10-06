package com.micaelaandrade.orderparser.database.mapper;

import com.micaelaandrade.orderparser.database.entity.UserEntity;
import com.micaelaandrade.orderparser.domain.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserEntityMapper {

    public User toDomain(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .userId(userEntity.getId())
                .name(userEntity.getName())
                .userId(userEntity.getExternalId())
                .build();
    }
}
