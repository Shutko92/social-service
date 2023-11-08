package ru.skillbox.diplom.group42.social.service.service.captcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group42.social.service.dto.captcha.CaptchaDto;
import ru.skillbox.diplom.group42.social.service.entity.captcha.Captcha;
import ru.skillbox.diplom.group42.social.service.repository.captcha.CaptchaRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Base64;

@Service
public class CaptchaService {

    private final String imageFormat = "png";

    @Value("${vars.captchaCharStr}")
    private String elegibleChars;

    private final CaptchaRepository captchaRepository;

    @Autowired
    public CaptchaService(CaptchaRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }

    /**
     * Метод инициализирует генератор капчи, формирует фон и содержание, собирает капчу.
     * @return капча.
     */
    public Captcha generateCaptcha(){
        CaptchaBuilder builder = new CaptchaBuilder();
        BufferedImage captchaImage = builder.setBackgroundColor(Color.ORANGE).build();
        Captcha captcha = new Captcha(builder.getCaptchaString(), ZonedDateTime.now(),captchaImage);
        return captcha;
    }

    /**
     * Метод вызывает другой метод, берет его результат, сохраняет капчу через репозиторий, форматирует информацию о ней,
     * формирует ответ.
     * @return информация о капче.
     */
    public CaptchaDto getCaptcha() {
        Captcha captcha= generateCaptcha();
        captchaRepository.save(captcha);
        CaptchaDto captchaDto = new CaptchaDto();
        captchaDto.setSecret(captcha.getCode());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try{
            ImageIO.write(captcha.getImage(),imageFormat,stream);
            String prefix = "data:image/" + imageFormat + ";base64, ";
            captchaDto.setImage(prefix+ Base64.getEncoder().encodeToString(stream.toByteArray()));
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return  captchaDto;
    }
}
