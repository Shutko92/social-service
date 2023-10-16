package ru.skillbox.diplom.group42.social.service.dto.friend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseDto;

import java.awt.print.Pageable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageFriendShortDto extends BaseDto {

    private Integer totalElements;

    private Integer totalPages;

    private Integer number;

    private Integer size;

    private FriendShortDto content;

    private Sort sort;

    private Boolean first;

    private Boolean last;

    private Integer numberOfElements;

    private Pageable pageable;

    private Boolean empty;

}
