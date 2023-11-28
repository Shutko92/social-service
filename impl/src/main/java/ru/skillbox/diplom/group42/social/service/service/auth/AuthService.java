package ru.skillbox.diplom.group42.social.service.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.PasswordChangeDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group42.social.service.entity.auth.Role;
import ru.skillbox.diplom.group42.social.service.entity.auth.User;
import ru.skillbox.diplom.group42.social.service.exception.InvalidCaptchaException;
import ru.skillbox.diplom.group42.social.service.exception.RegisteringExistingUserException;
import ru.skillbox.diplom.group42.social.service.mapper.auth.AuthMapper;
import ru.skillbox.diplom.group42.social.service.repository.auth.RoleRepository;
import ru.skillbox.diplom.group42.social.service.repository.auth.UserRepository;
import ru.skillbox.diplom.group42.social.service.security.JwtTokenProvider;
import ru.skillbox.diplom.group42.social.service.security.JwtUser;
import ru.skillbox.diplom.group42.social.service.service.account.AccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil.getJwtUserFromSecurityContext;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    public static final String ROLE_USER = "user";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final AccountService accountService;
    private final AuthMapper authMapper;

    public void register(RegistrationDto registrationDto) {
        log.info("ENTERED register(User user) in AuthService");
        Optional<User> optionalUser = userRepository.findByEmail(registrationDto.getEmail());
        log.info("Looking for previously registered user with such email");
        if (optionalUser.isPresent()) {
            log.warn("User already exists");
            throw new RegisteringExistingUserException();
        }
        if (!passCaptcha(registrationDto)) {
            log.warn("In AuthService register: captcha failed");
            throw new InvalidCaptchaException();
        }
        newUserCreation(registrationDto);
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

    public void changePassword(PasswordChangeDto dto) {
        JwtUser jwtUser = getJwtUserFromSecurityContext();
        boolean isOldPasswordCorrect = new BCryptPasswordEncoder().matches(dto.getOldPassword(),jwtUser.getPassword());
        log.info("User password is correct " + isOldPasswordCorrect);
        if(!isOldPasswordCorrect){
            throw new NotFoundException("User password is not correct");
        }
        User user = userRepository.findById(jwtUser.getId()).orElse(null);
        String newPassword = new BCryptPasswordEncoder().encode(dto.getNewPassword1());
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    private boolean passCaptcha(RegistrationDto registrationDto) {
        return registrationDto.getCaptchaCode().equals(registrationDto.getCaptchaSecret());
    }

    private void newUserCreation(RegistrationDto registrationDto) {
        Role roleUser = roleRepository.findByName(ROLE_USER);
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        User user = authMapper.convertRegistrationDtoToUser(registrationDto);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole(userRoles);

        accountService.createAccount(user);

        log.info("IN register - user: {} {} with email [{}] successfully registered",
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}
