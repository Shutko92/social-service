package ru.skillbox.diplom.group42.social.service.dto.friend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sort {

    private Boolean empty;

    private Boolean sorted;

    private Boolean unsorted;
}
