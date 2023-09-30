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
@Schema(description = "Данные для регистрации", required = true)
public class RegistrationDto {
    @Schema(description = "Электронная почта пользователя")
    private String email;
    @Schema(description = "Пароль", minLength = 8)
    private String password1;
    @Schema(description = "Повторение пароля", minLength = 8)
    private String password2;
    @Schema(description = "Имя пользователя")
    private String firstName;
    @Schema(description = "Фамилия пользователя")
    private String lastName;
    @Schema(description = "Капча вводимая пользователем")
    private String captchaCode;
    @Schema(description = "Капча угадываемая пользователем")
    private String captchaSecret;

}
