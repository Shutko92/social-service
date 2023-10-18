package ru.skillbox.diplom.group42.social.service.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseSearchDto;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данный поиска тега")
public class TagSearchDto extends BaseSearchDto {
    @Schema(description = "Данные запроса поиска (имя тега)")
    private String name;
}
