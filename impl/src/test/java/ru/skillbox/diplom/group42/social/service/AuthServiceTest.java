package ru.skillbox.diplom.group42.social.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group42.social.service.entity.auth.Role;
import ru.skillbox.diplom.group42.social.service.entity.auth.User;
import ru.skillbox.diplom.group42.social.service.mapper.auth.AuthMapper;
import ru.skillbox.diplom.group42.social.service.repository.auth.RoleRepository;
import ru.skillbox.diplom.group42.social.service.repository.auth.UserRepository;
import ru.skillbox.diplom.group42.social.service.security.JwtTokenProvider;
import ru.skillbox.diplom.group42.social.service.service.account.AccountService;
import ru.skillbox.diplom.group42.social.service.service.auth.AuthService;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createUser;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createAuthenticateDto;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createRegistrationDto;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private AccountService accountService;
    @Mock
    private AuthMapper authMapper;
    private AuthService authService;
    private User user = createUser("USER");
    private RegistrationDto registrationDto = createRegistrationDto();

    @BeforeEach
    public void beforeMethod(TestInfo info){
        authService = new AuthService(userRepository, roleRepository, jwtTokenProvider, accountService, authMapper);
        if (
                info.getDisplayName().equals("funLoginShouldInvokeUserRepositoryFindByEmailAndJwtTokenProvider")
        ) {
            return;
        }
        when(userRepository.findByEmail(registrationDto.getEmail())).thenReturn(Optional.ofNullable(null));
        when(authMapper.convertRegistrationDtoToUser(registrationDto)).thenReturn(user);
    }
    @Test
    @DisplayName("funLoginShouldInvokeUserRepositoryFindByEmailAndJwtTokenProvider")
    public void funLoginShouldInvokeUserRepositoryFindByEmailAndJwtTokenProvider(){
        AuthenticateDto authenticateDto = createAuthenticateDto(user);
        when(userRepository.findByEmail(authenticateDto.getEmail())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.createToken(user.getEmail(), user.getId(), user.getRole())).thenReturn("TEST TOKEN");
        authService.login(authenticateDto);
        verify(jwtTokenProvider, times(1)).createToken(user.getEmail(), user.getId(), user.getRole());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }
    @Test
    public void funRegisterShouldInvokeAuthMapperConvertRegistrationDtoToUser() {
        authService.register(registrationDto);
        verify(authMapper, times(1)).convertRegistrationDtoToUser(registrationDto);
    }
    @Test
    public void funRegisterShouldInvokeUserRepositoryFindByEmail() {
        authService.register(registrationDto);
        verify(userRepository, times(1)).findByEmail(registrationDto.getEmail());
    }
    @Test
    public void funRegisterShouldInvokeAccountServiceCreateAccount() {
        authService.register(registrationDto);
        verify(accountService, times(1)).createAccount(user);
    }
    @Test
    public void funRegisterShouldInvokeRoleRepositoryFindByName() {
        when(roleRepository.findByName("user")).thenReturn(new Role());
        authService.register(registrationDto);
        verify(roleRepository, times(1)).findByName("user");
    }
}
