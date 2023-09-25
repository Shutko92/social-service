package ru.skillbox.diplom.group42.social.service.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Типы постов")
public enum Type {
    @Schema(description = "Опубликован/отложен")
    POSTED,QUEUED
}
