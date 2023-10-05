package ru.skillbox.diplom.group42.social.service.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends BaseDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

}