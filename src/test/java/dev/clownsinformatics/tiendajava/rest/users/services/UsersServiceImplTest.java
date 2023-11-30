package dev.clownsinformatics.tiendajava.rest.users.services;

import dev.clownsinformatics.tiendajava.rest.orders.repository.OrderRepository;
import dev.clownsinformatics.tiendajava.rest.users.dto.UserInfoResponse;
import dev.clownsinformatics.tiendajava.rest.users.dto.UserRequest;
import dev.clownsinformatics.tiendajava.rest.users.dto.UserResponse;
import dev.clownsinformatics.tiendajava.rest.users.exceptions.UserNameOrEmailExists;
import dev.clownsinformatics.tiendajava.rest.users.exceptions.UserNotFound;
import dev.clownsinformatics.tiendajava.rest.users.mappers.UsersMapper;
import dev.clownsinformatics.tiendajava.rest.users.models.User;
import dev.clownsinformatics.tiendajava.rest.users.repositories.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceImplTest {

    private final UserRequest userRequest = UserRequest.builder().username("test").email("test@test.com").build();
    private final User user = User.builder().id(99L).username("test").email("test@test.com").build();
    private final UserResponse userResponse = UserResponse.builder().username("test").email("test@test.com").build();
    private final UserInfoResponse userIResponse = UserInfoResponse.builder().username("test").email("test@test.com").build();
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UsersMapper usersMapper;
    @InjectMocks
    private UsersServiceImpl usersService;

    @Test
    void findAll() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        Page<User> page = new PageImpl<>(users);
        when(usersRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(usersMapper.toUserResponse(any(User.class))).thenReturn(new UserResponse());
        Page<UserResponse> result = usersService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Pageable.unpaged());
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.getTotalElements())
        );

        verify(usersRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findById() {
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderRepository.findOrderIdsByIdUser(userId)).thenReturn(List.of());
        when(usersMapper.toUserInfoResponse(any(User.class), anyList())).thenReturn(userIResponse);

        UserInfoResponse result = usersService.findById(userId);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userResponse.getUsername(), result.getUsername()),
                () -> assertEquals(userResponse.getEmail(), result.getEmail())
        );

        verify(usersRepository, times(1)).findById(userId);
        verify(orderRepository, times(1)).findOrderIdsByIdUser(userId);
        verify(usersMapper, times(1)).toUserInfoResponse(user, List.of());
    }

    @Test
    void findByIdNotFound() {
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFound.class, () -> usersService.findById(userId));
        verify(usersRepository, times(1)).findById(userId);
    }

    @Test
    void save() {
        when(usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString())).thenReturn(Optional.empty());
        when(usersMapper.toUser(userRequest)).thenReturn(user);
        when(usersMapper.toUserResponse(user)).thenReturn(userResponse);
        when(usersRepository.save(user)).thenReturn(user);

        UserResponse result = usersService.save(userRequest);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userRequest.getUsername(), result.getUsername()),
                () -> assertEquals(userRequest.getEmail(), result.getEmail())
        );

        verify(usersRepository, times(1)).findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString());
        verify(usersMapper, times(1)).toUser(userRequest);
        verify(usersMapper, times(1)).toUserResponse(user);
        verify(usersRepository, times(1)).save(user);
    }

    @Test
    void saveDuplicateUsername() {
        when(usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString())).thenReturn(Optional.of(user));
        assertThrows(UserNameOrEmailExists.class, () -> usersService.save(userRequest));
        verify(usersRepository, times(1)).findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString());
    }

    @Test
    void update() {
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString())).thenReturn(Optional.empty());
        when(usersMapper.toUser(userRequest, userId)).thenReturn(user);
        when(usersMapper.toUserResponse(user)).thenReturn(userResponse);
        when(usersRepository.save(user)).thenReturn(user);

        UserResponse result = usersService.update(userId, userRequest);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userRequest.getUsername(), result.getUsername()),
                () -> assertEquals(userRequest.getEmail(), result.getEmail())
        );

        verify(usersRepository, times(1)).findById(userId);
        verify(usersRepository, times(1)).findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString());
        verify(usersMapper, times(1)).toUser(userRequest, userId);
        verify(usersMapper, times(1)).toUserResponse(user);
        verify(usersRepository, times(1)).save(user);
    }

    @Test
    void updateDuplicateUsername() {
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString())).thenReturn(Optional.of(user));
        assertThrows(UserNameOrEmailExists.class, () -> usersService.update(userId, userRequest));
        verify(usersRepository, times(1)).findById(userId);
        verify(usersRepository, times(1)).findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString());
    }

    @Test
    void updateNotFound() {
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFound.class, () -> usersService.update(userId, userRequest));
        verify(usersRepository, times(1)).findById(userId);
    }

    @Test
    void deleteByIdPhysicalDelete() {
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        usersService.deleteById(userId);
        verify(usersRepository, times(1)).findById(userId);
        verify(usersRepository, times(1)).delete(user);
    }

    @Test
    void deleteByIdLogicalDelete() {
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderRepository.existsByIdUser(userId)).thenReturn(true);
        doNothing().when(usersRepository).updateIsDeletedToTrueById(userId);
        usersService.deleteById(userId);
        verify(usersRepository, times(1)).updateIsDeletedToTrueById(userId);
        verify(orderRepository, times(1)).existsByIdUser(userId);
    }

    @Test
    void deleteByIdNotFound() {
        Long userId = 1L;
        User user = new User();
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderRepository.existsByIdUser(userId)).thenReturn(true);

        // Act
        usersService.deleteById(userId);

        // Verify
        verify(usersRepository, times(1)).updateIsDeletedToTrueById(userId);
        verify(orderRepository, times(1)).existsByIdUser(userId);
    }
}