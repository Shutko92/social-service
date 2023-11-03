package ru.skillbox.diplom.group42.social.util;

import ru.skillbox.diplom.group42.social.service.dto.account.AccountSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.account.StatusCode;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.RegistrationDto;

import static ru.skillbox.diplom.group42.social.util.TestingConstant.*;

public class ControllerTestingDataFactory {
    public static RegistrationDto createRegistrationDtoForNewUser(){
        return new RegistrationDto(
                TEST_R_EMAIL,
                TEST_R_PASSWORD,
                TEST_R_PASSWORD,
                TEST_R_FIRST_NAME,
                TEST_R_LAST_NAME,
                TEST_R_CAPTCHA,
                TEST_R_CAPTCHA
        );
    }
    public static RegistrationDto createRegistrationDtoForTestUser(String email){
        return new RegistrationDto(
                email,
                TEST_PASSWORD,
                TEST_PASSWORD,
                TEST_FIRST_NAME,
                TEST_LAST_NAME,
                TEST_CAPTCHA,
                TEST_CAPTCHA
        );
    }
    public static AuthenticateDto createAuthenticateDto(String email, String password){
        AuthenticateDto authenticateDto = new AuthenticateDto();
        authenticateDto.setEmail(email);
        authenticateDto.setPassword(password);
        return authenticateDto;
    }
    public static AccountSearchDto createAccountSearchDto(){
        return new AccountSearchDto(
                null,
                null,
                null,
                TEST_FIRST_NAME,
                null,
                null,
                null,
                false,
                StatusCode.NONE,
                null,
                null
        );
    }
}
