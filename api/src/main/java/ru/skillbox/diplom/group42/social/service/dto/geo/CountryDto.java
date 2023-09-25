package ru.skillbox.diplom.group42.social.service.dto.geo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseDto;

import java.util.List;

@Setter
@Getter
@Schema(description = "Данные страны", required = true)
public class CountryDto extends BaseDto {
    @Schema(description = "Название страны", maxLength = 90)
    private String title;
    @Schema(description = "список названий городов")
    private List<String> cities;
}