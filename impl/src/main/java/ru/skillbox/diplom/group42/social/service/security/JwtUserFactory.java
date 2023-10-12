package ru.skillbox.diplom.group42.social.service.security;

import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.skillbox.diplom.group42.social.service.entity.auth.Role;
import ru.skillbox.diplom.group42.social.service.entity.auth.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor
public final class JwtUserFactory {

    public static JwtUser create(Optional<User> user) {
        return user.map(u -> new JwtUser(
                u.getId(),
                u.getFirstName(),
                u.getLastName(),
                u.getPassword(),
                u.getEmail(),
                mapToGrantedAuthorities(new ArrayList<>(u.getRole()))
        )).orElseThrow(() -> new UsernameNotFoundException("User was not found"));
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> userRoles) {
        return userRoles.stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getName())
                ).collect(Collectors.toList());
    }
}
