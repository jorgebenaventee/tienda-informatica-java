package dev.clownsinformatics.tiendajava.rest.users.dto;

import dev.clownsinformatics.tiendajava.rest.users.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String name;
    private String lastName;
    private String username;
    private String email;
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);
    @Builder.Default
    private Boolean isDeleted = false;
    @Builder.Default
    private List<String> orders = new ArrayList<>();
}
