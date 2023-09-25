package ru.skillbox.diplom.group42.social.service.dto.statistic;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
@Getter
@Setter
public class RequestDto {
    private ZonedDateTime date;
    private ZonedDateTime firstMonth;
    private ZonedDateTime lastMonth;
}
