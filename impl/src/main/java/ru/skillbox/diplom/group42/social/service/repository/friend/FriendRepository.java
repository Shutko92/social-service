package ru.skillbox.diplom.group42.social.service.repository.friend;

import ru.skillbox.diplom.group42.social.service.dto.account.StatusCode;
import ru.skillbox.diplom.group42.social.service.entity.friend.Friend;
import ru.skillbox.diplom.group42.social.service.repository.base.BaseRepository;

import java.util.List;

public interface FriendRepository extends BaseRepository<Friend> {

    Friend findByIdFromAndIdToAndStatusCode(Long idFrom, Long idTo, String status);
    List<Friend> findByIdFromAndStatusCode(Long idFrom, String statusCode);
}
