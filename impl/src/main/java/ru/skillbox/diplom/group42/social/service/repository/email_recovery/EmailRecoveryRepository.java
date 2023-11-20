package ru.skillbox.diplom.group42.social.service.repository.email_recovery;

import ru.skillbox.diplom.group42.social.service.entity.email.recovery.RecoveryLink;
import ru.skillbox.diplom.group42.social.service.repository.base.BaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailRecoveryRepository extends BaseRepository<RecoveryLink> {

    Optional<RecoveryLink> findByUserIdAndIsDeleted(Long userId, boolean isDeleted);
    Optional<RecoveryLink> findByRecoveryLinkAndIsDeleted(UUID link, boolean isDeleted);
    Optional<RecoveryLink> findByRecoveryLink(String link);
}
