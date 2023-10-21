package ru.skillbox.diplom.group42.social.service.dto.admin.console.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StatisticRequestDto {
    private String date;
    private String firstMonth;
    private String lastMonth;
}
