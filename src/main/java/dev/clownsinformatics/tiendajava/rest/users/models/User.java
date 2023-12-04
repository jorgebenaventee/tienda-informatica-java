package dev.clownsinformatics.tiendajava.rest.users.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "User's id", example = "1")
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Column(nullable = false)
    @Schema(description = "User's name", example = "David")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Last name cannot be empty")
    @Schema(description = "User's last name", example = "Jaraba")
    private String lastName;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username cannot be empty")
    @Schema(description = "User's username", example = "davidjaraba")
    private String username;

    @Column(unique = true, nullable = false)
    @Email(regexp = ".*@.*\\..*", message = "Email must be valid")
    @NotBlank(message = "Email cannot be empty")
    @Schema(description = "User's email", example = "david@gmail.com")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Length(min = 5, message = "Password must be at least 5 characters long")
    @Column(nullable = false)
    @Schema(description = "User's password", example = "123456")
    private String password;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    @Schema(description = "User's creation date", example = "2021-10-10T10:10:10")
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Column(updatable = true, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    @Schema(description = "User's last update date", example = "2021-10-10T10:10:10")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(columnDefinition = "boolean default false")
    @Builder.Default
    @Schema(description = "User's deleted status", example = "false")
    private Boolean isDeleted = false;


    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Schema(description = "User's roles", example = "USER")
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !isDeleted;
    }
}