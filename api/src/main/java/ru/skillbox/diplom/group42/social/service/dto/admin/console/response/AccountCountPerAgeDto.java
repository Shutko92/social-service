package ru.skillbox.diplom.group42.social.service.dto.admin.console.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountCountPerAgeDto {
    private int age;
    private int count;
}
