package ru.skillbox.diplom.group42.social.service.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные для смены пароля пользователя")
public class PasswordChangeDto {
    @Schema(description = "старый пароль")
    String oldPassword;
    @Schema(description = "новый пароль")
    String newPassword1;
    @Schema(description = "подтверждение нового пароля")
    String newPassword2;
}
