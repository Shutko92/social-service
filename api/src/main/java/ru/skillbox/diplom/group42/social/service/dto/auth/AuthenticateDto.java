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
@Schema(description = "Данные для авторизации", required = true)
public class AuthenticateDto {
    @Schema(description = "Электронная почта пользователя")
    private String email;
    @Schema(description = "Пароль пользователя", minLength = 6)
    private String password;
}
