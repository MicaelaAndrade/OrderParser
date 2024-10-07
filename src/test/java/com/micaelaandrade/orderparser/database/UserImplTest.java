package com.micaelaandrade.orderparser.database;

import com.micaelaandrade.orderparser.database.entity.UserEntity;
import com.micaelaandrade.orderparser.database.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserImpl userImpl;

    private UserEntity buildUserEntity() {
        return UserEntity.builder()
                .id(1L)
                .name("Micaela")
                .externalId(1001L)
                .build();
    }

    @Test
    public void shouldFindOrSaveUserWhenUserExists() {
        UserEntity userEntity = buildUserEntity();

        when(userRepository.findByExternalId(any())).thenReturn(Optional.of(userEntity));

        UserEntity response = userImpl.findOrSaveUser(userEntity);

        assertThat(response).usingRecursiveComparison().isEqualTo(userEntity);
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    public void shouldFindOrSaveUserWhenUserDoesNotExist() {
        UserEntity userEntity = buildUserEntity();

        when(userRepository.findByExternalId(any())).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity response = userImpl.findOrSaveUser(userEntity);

        assertThat(response).usingRecursiveComparison().isEqualTo(userEntity);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void shouldFindByUserIdWhenUserExists() {
        UserEntity userEntity = buildUserEntity();

        when(userRepository.findByExternalId(anyLong())).thenReturn(Optional.of(userEntity));

        UserEntity response = userImpl.findByUserId(1L);

        assertThat(response).usingRecursiveComparison().isEqualTo(userEntity);
        verify(userRepository, times(1)).findByExternalId(anyLong());
    }

    @Test
    public void shouldReturnNullWhenUserNotFoundByUserId() {
        when(userRepository.findByExternalId(anyLong())).thenReturn(Optional.empty());

        UserEntity response = userImpl.findByUserId(1L);

        assertThat(response).isNull();
        verify(userRepository, times(1)).findByExternalId(anyLong());
    }

    @Test
    public void shouldReturnAllUsers() {

        List<UserEntity> userEntities = List.of(buildUserEntity());

        when(userRepository.findAll()).thenReturn(userEntities);

        List<UserEntity> users = userImpl.findAll();

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getId()).isEqualTo(1L);
        assertThat(users.get(0).getName()).isEqualTo("Micaela");

        verify(userRepository, times(1)).findAll();
    }


    @Test
    public void shouldReturnEmptyListWhenNoUsersFound() {

        when(userRepository.findAll()).thenReturn(List.of());
        List<UserEntity> users = userImpl.findAll();

        assertThat(users).isEmpty();
        verify(userRepository, times(1)).findAll();
    }
}