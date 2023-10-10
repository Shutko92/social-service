package ru.skillbox.diplom.group42.social.service.dto.captcha;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для запроса капчи", required = true)

public class CaptchaDto {

    @Schema(description = "Идентификатор капчи")
    private String secret;

    @Schema(description = "Изображение капчи, закодированное в Base64")
    private String image;


}
