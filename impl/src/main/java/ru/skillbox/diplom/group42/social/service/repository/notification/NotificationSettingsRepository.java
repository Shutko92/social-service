package ru.skillbox.diplom.group42.social.service.repository.notification;

import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group42.social.service.entity.notification.NotificationSettings;
import ru.skillbox.diplom.group42.social.service.repository.base.BaseRepository;

import java.util.Optional;

@Repository
public interface NotificationSettingsRepository extends BaseRepository<NotificationSettings> {

    Optional<NotificationSettings> findByAccountId(Long accountId);
}
