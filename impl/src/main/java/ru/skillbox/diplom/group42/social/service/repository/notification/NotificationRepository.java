package ru.skillbox.diplom.group42.social.service.repository.notification;

import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group42.social.service.entity.notification.Notification;
import ru.skillbox.diplom.group42.social.service.repository.base.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends BaseRepository<Notification> {
    Optional<Notification> getByAuthorId(Long authorId);
    Integer countByRecipientIdAndIsDeleted(Long authorId, Boolean isDeleted);
    List<Notification> findAllByRecipientIdAndIsDeleted(Long recipientId, Boolean isDeleted);

}
