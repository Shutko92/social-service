package ru.skillbox.diplom.group42.social.service.dto.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BaseDto implements Serializable {

    private Long id;
    private Boolean isDeleted;

}
