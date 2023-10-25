package ru.skillbox.diplom.group42.social.service.repository.message;

import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group42.social.service.dto.message.ReadStatus;
import ru.skillbox.diplom.group42.social.service.entity.message.Message;
import ru.skillbox.diplom.group42.social.service.repository.base.BaseRepository;
@Repository
public interface MessageRepository extends BaseRepository<Message> {

    Integer countByConversationPartner2AndReadStatusIs(Long id, ReadStatus readStatus);
}
