package ru.skillbox.diplom.group42.social.service.repository.captcha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group42.social.service.entity.captcha.Captcha;


@Repository
public interface CaptchaRepository extends JpaRepository<Captcha, Long> {

}
