package ru.skillbox.diplom.group42.social.service.dto.email.recovery;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные для смены email")
public class EmailRecoveryDto {
    @Schema(description = "captcha")
    String captchaCode;
    @Schema(description = "email")
    String email;
    @Schema(description = "temp")
    String temp;
}
