package ru.skillbox.diplom.group42.social.service.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostStatisticRequestDto implements Serializable {

    private ZonedDateTime date;
    private ZonedDateTime firstMonth;
    private ZonedDateTime lastMonth;

}
