package com.example.shop.dto.user;

import com.example.shop.domain.user.RoleType;
import com.example.shop.domain.user.UserAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private RoleType role;

    @Builder
    private User(Long id, String username, String password, String email, RoleType role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override public String getUsername() { return username; }
    @Override public String getPassword() { return password; }

    @JsonIgnore
    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() { return true; }

    @JsonIgnore
    @Override public boolean isAccountNonLocked() { return true; }

    @JsonIgnore
    @Override public boolean isCredentialsNonExpired() { return true; }

    @JsonIgnore
    @Override public boolean isEnabled() { return true; }

    public static User fromEntity(UserAccount userAccount){
        System.out.println("userAccount.getId() = " + userAccount.getId());
        return User.builder()
                .id(userAccount.getId())
                .username(userAccount.getUsername())
                .password(userAccount.getPassword())
                .role(userAccount.getRole())
                .build();
    }
}
