package ru.skillbox.diplom.group42.social.service.service.email.recovery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group42.social.service.dto.email.recovery.EmailDetailsDto;
import ru.skillbox.diplom.group42.social.service.dto.email.recovery.EmailRecoveryDto;
import ru.skillbox.diplom.group42.social.service.entity.auth.User;
import ru.skillbox.diplom.group42.social.service.entity.email.recovery.RecoveryLink;
import ru.skillbox.diplom.group42.social.service.repository.auth.UserRepository;
import ru.skillbox.diplom.group42.social.service.repository.email_recovery.EmailRecoveryRepository;
import ru.skillbox.diplom.group42.social.service.security.JwtUser;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static ru.skillbox.diplom.group42.social.service.utils.email.EmailRecoveryUtil.*;
import static ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil.getJwtUserFromSecurityContext;
import static ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil.getJwtUserIdFromSecurityContext;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl {

    private final JavaMailSender javaMailSender;
    private final EmailRecoveryRepository emailRecoveryRepository;
    private final UserRepository userRepository;


    @Value("${spring.mail.username}")
    String USER_NAME;
    @Value("${host.local}")
    String LOCAL_HOST;

    /**
     * Метод вызывает пользователя, генерирует пароль, передает его в другой сервис, формирует E-mail, вызывает
     * Другие методы. В случае ошибки выбрасывает исключение.
     */
    public void sendLinkByEmail() {
        try {
            JwtUser user = getJwtUserFromSecurityContext();
            UUID uuid = UUID.randomUUID();
            String confirmLink = createLink(LOCAL_HOST, uuid);
            EmailDetailsDto email = new EmailDetailsDto(
                    user.getEmail(),
                    buildEmailBody(confirmLink),
                    THEME_EMAIL
            );
            saveRecoveryLinkToDataBase(user.getId(), uuid);
            sendSimpleMail(email);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Метод берет header из параметров, на его основе генерирует пароль, вызывает идентификатор пользователя,
     * ищет ссылку через репозиторий по паролю и с негативным указателем удалениия. Если ссылка находится,
     * ищется пользователь по ндентификатору, обновляется и сохраняется. Идентификатор передается в другой метод.
     * @param request сервлет-запрос.
     * @param dto параметры для смены E-mail.
     * @return HttpStatus.
     */
    public HttpStatus changeEmailByLink(HttpServletRequest request, EmailRecoveryDto dto) {
        String referer = request.getHeader("referer");
        UUID uuid = UUID.fromString(Objects.requireNonNull(getRefererUUID(referer)));
        Long userId = getJwtUserIdFromSecurityContext();
        RecoveryLink currentRecoveryLink = emailRecoveryRepository.findByRecoveryLinkAndIsDeleted(
                uuid,
                false
        ).orElse(null);
        if (currentRecoveryLink != null && currentRecoveryLink.getUserId().equals(userId)) {
            Optional<User> user = userRepository.findById(userId);
            user.ifPresent(value -> {
                value.setEmail(dto.getEmail());
                userRepository.save(user.get());
                deleteRecoveryLinkInDataBase(user.get().getId());
            });
            return HttpStatus.OK;
        } else {
            return HttpStatus.NOT_ACCEPTABLE;
        }
    }

    private void sendSimpleMail(EmailDetailsDto details) throws IOException {
        try {

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(USER_NAME);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    private void sendMailWithAttachment(EmailDetailsDto details) throws IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {

            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(USER_NAME);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(
                    details.getSubject());

            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
            mimeMessageHelper.addAttachment(file.getFilename(), file);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IOException(e.getMessage());
        }
    }

    private void saveRecoveryLinkToDataBase(Long user_id, UUID uuid) {
        deleteRecoveryLinkInDataBase(user_id);
        RecoveryLink recoveryLink = new RecoveryLink();
        recoveryLink.setRecoveryLink(uuid);
        recoveryLink.setUserId(user_id);
        emailRecoveryRepository.save(recoveryLink);
    }

    private void deleteRecoveryLinkInDataBase(Long user_id) {
        RecoveryLink currentRecoveryLink = emailRecoveryRepository.findByUserIdAndIsDeleted(
                user_id,
                false
        ).orElse(null);
        if (currentRecoveryLink != null) emailRecoveryRepository.delete(currentRecoveryLink);
    }
}