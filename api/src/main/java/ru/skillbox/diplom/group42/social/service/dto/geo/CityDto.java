package ru.skillbox.diplom.group42.social.service.dto.geo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseDto;

@Setter
@Getter
@Schema(description = "Данные города", required = true)
public class CityDto extends BaseDto {
    @Schema(description = "Название города", maxLength = 90)
    private String title;
    @Schema(description = "Id страны, которой принадлежит город")
    private Long countryId;
}