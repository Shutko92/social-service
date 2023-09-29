package ru.skillbox.diplom.group42.social.service.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticateResponseDto {
    private String accessToken;
    private String refreshToken;
}
