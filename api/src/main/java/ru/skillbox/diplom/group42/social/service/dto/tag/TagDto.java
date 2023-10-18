package ru.skillbox.diplom.group42.social.service.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseDto;

@Getter
@Setter
@Schema(description = "Данные тега")
public class TagDto extends BaseDto {
    @Schema(description = "Наименование тега")
    private String name;
}
