package dev.clownsinformatics.tiendajava.rest.users.mappers;

import dev.clownsinformatics.tiendajava.rest.users.dto.UserRequest;
import dev.clownsinformatics.tiendajava.rest.users.models.Role;
import dev.clownsinformatics.tiendajava.rest.users.models.User;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UsersMapperTest {
    private final UsersMapper usersMapper = new UsersMapper();


    @Test
    void toUser() {
        UserRequest userRequest = new UserRequest("name", "surnames", "username", "email", "password", new HashSet<>(List.of(Role.USER)), false);
        var res = usersMapper.toUser(userRequest);

        assertAll(
                () -> assertEquals(userRequest.getName(), res.getName()),
                () -> assertEquals(userRequest.getLastName(), res.getLastName()),
                () -> assertEquals(userRequest.getUsername(), res.getUsername()),
                () -> assertEquals(userRequest.getEmail(), res.getEmail()),
                () -> assertEquals(userRequest.getPassword(), res.getPassword()),
                () -> assertEquals(userRequest.getRoles(), res.getRoles()),
                () -> assertEquals(userRequest.getIsDeleted(), res.getIsDeleted())
        );

    }

    @Test
    void testToUser() {
        UserRequest userRequest = new UserRequest("name", "surnames", "username", "email", "password", null, false);
        var res = usersMapper.toUser(userRequest, 1L);

        assertAll(
                () -> assertEquals(1L, res.getId()),
                () -> assertEquals(userRequest.getName(), res.getName()),
                () -> assertEquals(userRequest.getLastName(), res.getLastName()),
                () -> assertEquals(userRequest.getUsername(), res.getUsername()),
                () -> assertEquals(userRequest.getEmail(), res.getEmail()),
                () -> assertEquals(userRequest.getPassword(), res.getPassword()),
                () -> assertEquals(userRequest.getRoles(), res.getRoles()),
                () -> assertEquals(userRequest.getIsDeleted(), res.getIsDeleted())
        );
    }

    @Test
    void toUserResponse() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .lastName("surnames")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(List.of(Role.USER)))
                .isDeleted(false)
                .build();
        var res = usersMapper.toUserResponse(user);

        assertAll(
                () -> assertEquals(user.getId(), res.getId()),
                () -> assertEquals(user.getName(), res.getName()),
                () -> assertEquals(user.getLastName(), res.getLastName()),
                () -> assertEquals(user.getUsername(), res.getUsername()),
                () -> assertEquals(user.getEmail(), res.getEmail()),
                () -> assertEquals(user.getRoles(), res.getRoles()),
                () -> assertEquals(user.getIsDeleted(), res.getIsDeleted())
        );

    }

    @Test
    void toUserInfoResponse() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .lastName("surnames")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(List.of(Role.USER)))
                .isDeleted(false)
                .build();
        var res = usersMapper.toUserInfoResponse(user, List.of("order1", "order2"));

        assertAll(
                () -> assertEquals(user.getId(), res.getId()),
                () -> assertEquals(user.getName(), res.getName()),
                () -> assertEquals(user.getLastName(), res.getLastName()),
                () -> assertEquals(user.getUsername(), res.getUsername()),
                () -> assertEquals(user.getEmail(), res.getEmail()),
                () -> assertEquals(user.getRoles(), res.getRoles()),
                () -> assertEquals(user.getIsDeleted(), res.getIsDeleted()),
                () -> assertEquals(List.of("order1", "order2"), res.getOrders())
        );
    }
}