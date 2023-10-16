package ru.skillbox.diplom.group42.social.service.dto.friend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dto получения счетчика заявок в друзья", required = true)
public class CountDto{

    @Schema(description = "Счетчик заявок в друзья")
    private Long count;

}
