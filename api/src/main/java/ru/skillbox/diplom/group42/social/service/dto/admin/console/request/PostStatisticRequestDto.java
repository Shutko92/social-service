package ru.skillbox.diplom.group42.social.service.dto.admin.console.request;

import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostStatisticRequestDto {
    private ZonedDateTime date;
    private ZonedDateTime firstMonth;
    private ZonedDateTime lastMonth;
}
