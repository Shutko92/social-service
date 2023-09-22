package ru.skillbox.diplom.group42.social.service.dto.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseSearchDto implements Serializable {

    private Long id;

    private Boolean isDeleted;
}
