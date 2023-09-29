package ru.skillbox.diplom.group42.social.service.dto.auth;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class RegistrationDto {

    private String email;
    private String password1;
    private String password2;
    private String firstName;
    private String lastName;
    private String captchaCode;
    private String captchaSecret = "1";

}
