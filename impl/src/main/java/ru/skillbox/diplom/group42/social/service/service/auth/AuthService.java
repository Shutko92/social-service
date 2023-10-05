package ru.skillbox.diplom.group42.social.service.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group42.social.service.entity.auth.Role;
import ru.skillbox.diplom.group42.social.service.entity.auth.User;
import ru.skillbox.diplom.group42.social.service.repository.auth.RoleRepository;
import ru.skillbox.diplom.group42.social.service.repository.auth.UserRepository;
import ru.skillbox.diplom.group42.social.service.security.jwt.JwtTokenProvider;
import ru.skillbox.diplom.group42.social.service.service.account.AccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    public static final String ROLE_USER = "user";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final AccountService accountService;

    public void register(User user) {
        log.info("ENTERED register(User user) in AuthService");
        Role roleUser = roleRepository.findByName(ROLE_USER);
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole(userRoles);

        accountService.createAccount(user);

        log.info("IN register - user: {} {} with email [{}] successfully registered",
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

    public AuthenticateResponseDto login(AuthenticateDto authenticateDto) {
        try {
            log.info("Method login(AuthenticateDto authenticateDto) in AuthService. User with email: {} is attempting to login", authenticateDto.getEmail());

            String email = authenticateDto.getEmail();
            Optional<User> user = userRepository.findByEmail(email);

            if (!user.isPresent()) {
                log.error("User with email: {} was not found", email);
                throw new UsernameNotFoundException("User with email: " + email + " was not found");
            }

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
            if (!passwordEncoder.matches(authenticateDto.getPassword(), user.get().getPassword())) {
                log.error("Invalid password for user with email: {}", email);
                throw new BadCredentialsException("Invalid email or password");
            }

            String token = jwtTokenProvider.createToken(email, user.get().getId(), user.get().getRole());

            AuthenticateResponseDto responseDto = new AuthenticateResponseDto(token, token);
            authenticateDto.setEmail(email);

            log.info("User {} ({} {}) logged in",
                    user.get().getEmail(),
                    user.get().getFirstName(),
                    user.get().getLastName());

            return responseDto;
        } catch (AuthenticationException e) {
            log.error("AuthenticationException occurred: {}", e.getMessage());
            throw new BadCredentialsException("Invalid email or password");
        }
    }
}
