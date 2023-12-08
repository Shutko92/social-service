package ru.skillbox.diplom.group42.social.service.utils.friend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseSearchDto;

@Getter
@Setter
@AllArgsConstructor
public class FriendRequestFilter extends BaseSearchDto {
    private Long idOne;
    private Long idTwo;
    private String statusCode;
}
