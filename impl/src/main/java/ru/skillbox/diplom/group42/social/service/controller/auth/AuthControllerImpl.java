package ru.skillbox.diplom.group42.social.service.controller.auth;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.PasswordChangeDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group42.social.service.dto.captcha.CaptchaDto;
import ru.skillbox.diplom.group42.social.service.dto.email.recovery.EmailRecoveryDto;
import ru.skillbox.diplom.group42.social.service.service.auth.AuthService;
import ru.skillbox.diplom.group42.social.service.service.captcha.CaptchaService;
import ru.skillbox.diplom.group42.social.service.service.email.recovery.EmailServiceImpl;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@AllArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    private final CaptchaService captchaService;

    private final EmailServiceImpl emailService;

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void register(RegistrationDto registrationDto) {
        authService.register(registrationDto);
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

    @Override
    public ResponseEntity<CaptchaDto> getCaptchaImage() {
        return ResponseEntity.ok(captchaService.getCaptcha());
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void handlerRequestChangeEmail() {
        emailService.sendLinkByEmail();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public HttpStatus confirmChangeEmail(HttpServletRequest request, @RequestBody EmailRecoveryDto dto) {
        return emailService.changeEmailByLink(request, dto);
    }

    @Override
    public HttpStatus handlerRequestChangePassword(@RequestBody PasswordChangeDto dto) {
        try {
            authService.changePassword(dto);
            return HttpStatus.OK;
        } catch (NotFoundException exception) {
            return HttpStatus.FORBIDDEN;
        }
    }
}
