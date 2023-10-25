package ru.skillbox.diplom.group42.social.service.repository.dialog;

import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group42.social.service.entity.dialog.Dialog;
import ru.skillbox.diplom.group42.social.service.repository.base.BaseRepository;

import java.util.Optional;

@Repository
public interface DialogRepository extends BaseRepository<Dialog> {

    Optional<Dialog> getByConversationPartner1AndAndConversationPartner2(Long userId1, Long userId2);
}
