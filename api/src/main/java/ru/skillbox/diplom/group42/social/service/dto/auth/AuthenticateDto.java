package ru.skillbox.diplom.group42.social.service.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticateDto {

    private String email;
    private String password;
}
