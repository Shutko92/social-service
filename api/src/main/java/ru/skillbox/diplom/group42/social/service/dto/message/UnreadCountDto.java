package ru.skillbox.diplom.group42.social.service.dto.message;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UnreadCountDto implements Serializable {
    private Integer count;
}
