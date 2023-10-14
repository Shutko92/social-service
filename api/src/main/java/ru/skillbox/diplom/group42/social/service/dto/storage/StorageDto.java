package ru.skillbox.diplom.group42.social.service.dto.storage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные хранилища")
public class StorageDto {

    @Schema(description = "URL файла в хранилище")
    private String fileName;
}
