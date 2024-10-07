package com.micaelaandrade.orderparser.database.mapper;

import com.micaelaandrade.orderparser.database.entity.UserEntity;
import com.micaelaandrade.orderparser.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserEntityMapperTest {

    @InjectMocks
    private UserEntityMapper userEntityMapper;

    @Test
    public void shouldMapUserEntityToDomain() {

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .externalId(1001L)
                .name("Micaela")
                .build();

        User user = userEntityMapper.toDomain(userEntity);


        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUserId()).isEqualTo(1001L);
        assertThat(user.getName()).isEqualTo("Micaela");
    }
}