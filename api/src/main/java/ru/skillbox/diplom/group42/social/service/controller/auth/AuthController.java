package ru.skillbox.diplom.group42.social.service.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group42.social.service.dto.captcha.CaptchaDto;


@RestController
@RequestMapping("/api/v1/auth/")
public interface AuthController {
    @PostMapping("register")
    void register(@RequestBody RegistrationDto registrationDto);

    @PostMapping("login")
    ResponseEntity<AuthenticateResponseDto> login(@RequestBody AuthenticateDto authenticateDto);

    @PostMapping("logout")
    void logout();

    @GetMapping("captcha")
    ResponseEntity<CaptchaDto> getCaptchaImage();
}
