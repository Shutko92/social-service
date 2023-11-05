package ru.skillbox.diplom.group42.social.service.dto.admin.console.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticPerDateDto {
    private ZonedDateTime date;
    private int count;
}
