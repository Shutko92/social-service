package ru.skillbox.diplom.group42.social.service.controller.auth;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group42.social.service.entity.auth.User;
import ru.skillbox.diplom.group42.social.service.mapper.auth.AuthMapper;
import ru.skillbox.diplom.group42.social.service.service.auth.AuthService;

@Slf4j
@RestController
@AllArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;
    private final AuthMapper authMapper;

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void register(RegistrationDto registrationDto) {
        User user = authMapper.convertRegistrationDtoToUser(registrationDto);
        authService.register(user);
    }


    @Override
    public ResponseEntity<AuthenticateResponseDto> login(AuthenticateDto authenticateDto) {
        AuthenticateResponseDto dto = authService.login(authenticateDto);
        return ResponseEntity.ok(dto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void logout() {
    }
}
