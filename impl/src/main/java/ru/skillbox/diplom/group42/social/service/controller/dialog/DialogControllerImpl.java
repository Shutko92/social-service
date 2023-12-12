package ru.skillbox.diplom.group42.social.service.controller.dialog;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group42.social.service.dto.dialog.DialogDto;
import ru.skillbox.diplom.group42.social.service.dto.message.MessageDto;
import ru.skillbox.diplom.group42.social.service.dto.message.MessageShortDto;
import ru.skillbox.diplom.group42.social.service.dto.message.UnreadCountDto;
import ru.skillbox.diplom.group42.social.service.service.dialog.DialogService;

@RestController
@AllArgsConstructor
public class DialogControllerImpl implements DialogController {

    private final DialogService dialogService;

    @Override
    public ResponseEntity updateStatusMessage(Long dialogId) {
        dialogService.updateStatusMessage(dialogId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<MessageDto> createMessage(MessageDto messageDto) {
        return null;
    }

    @Override
    public ResponseEntity<DialogDto> createDialog(DialogDto dialogDto) {
        return null;
    }

    @Override
    public ResponseEntity<Page<DialogDto>> getAllDialogs(Pageable pageable) {
        return ResponseEntity.ok(dialogService.getAllDialogs(pageable));
    }

    @Override
    public ResponseEntity<UnreadCountDto> getCountUnreadMessage() {
        return ResponseEntity.ok(dialogService.getCountUnreadMessage());
    }

    @Override
    public ResponseEntity<DialogDto> getDialogBetweenUsers(Long id) {
        return ResponseEntity.ok(dialogService.getDialogBetweenUsers(id));
    }

    @Override
    public ResponseEntity<Page<MessageShortDto>> getMessagesToDialog(Long recipientId, Pageable pageable) {
        return ResponseEntity.ok(dialogService.getMessagesToDialog(recipientId, pageable));
    }
}
