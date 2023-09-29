package ru.skillbox.diplom.group42.social.service.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.RegistrationDto;



@RestController
@RequestMapping("/api/v1/auth/")
public interface AuthController {
    @PostMapping("register")
    void register(@RequestBody RegistrationDto registrationDto);

    @PostMapping("login")
    ResponseEntity<AuthenticateResponseDto> login(@RequestBody AuthenticateDto authenticateDto);
}
