package ru.skillbox.diplom.group42.social.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skillbox.diplom.group42.social.service.entity.captcha.Captcha;
import ru.skillbox.diplom.group42.social.service.repository.captcha.CaptchaRepository;
import ru.skillbox.diplom.group42.social.service.service.captcha.CaptchaService;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CaptchaServiceTest {
    @Mock
    private CaptchaRepository captchaRepository;

    private CaptchaService captchaService;

    @BeforeEach
    public void beforeMethod(){
        captchaService = new CaptchaService(captchaRepository);
    }

    @Test
    public void funGetCaptchaShouldInvokeCaptchaRepositorySave(){
        captchaService.getCaptcha();
        verify(captchaRepository, times(1)).save(isA(Captcha.class));
    }

    @Test
    public void funGenerateCaptchaShouldReturnNotNull(){
        Captcha captcha =  captchaService.generateCaptcha();
        assert captcha != null;
    }
}
