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
@Schema(description = "Данные ответа при успешной аутентификации", required = true)
public class AuthenticateResponseDto {
    @Schema(description = "Токен доступа")
    private String accessToken;
    @Schema(description = "Токен обновления доступа")
    private String refreshToken;
}
