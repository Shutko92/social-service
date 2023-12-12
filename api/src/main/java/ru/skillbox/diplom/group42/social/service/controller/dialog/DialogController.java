package ru.skillbox.diplom.group42.social.service.controller.dialog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group42.social.service.dto.dialog.DialogDto;
import ru.skillbox.diplom.group42.social.service.dto.message.MessageDto;
import ru.skillbox.diplom.group42.social.service.dto.message.MessageShortDto;
import ru.skillbox.diplom.group42.social.service.dto.message.UnreadCountDto;
import ru.skillbox.diplom.group42.social.service.util.ConstantURL;

@RestController
@RequestMapping(ConstantURL.BASE_URL + "/dialogs")
public interface DialogController {

    @PutMapping("/{dialogId}")
    ResponseEntity updateStatusMessage(@PathVariable Long dialogId);

    @PostMapping("/createMessage")
    ResponseEntity<MessageDto> createMessage(@RequestBody MessageDto messageDto);

    @PostMapping("/createDialog")
    ResponseEntity<DialogDto> createDialog(@RequestBody DialogDto dialogDto);

    @GetMapping
    ResponseEntity<Page<DialogDto>> getAllDialogs(Pageable pageable);

    @GetMapping("/unread")
    ResponseEntity<UnreadCountDto> getCountUnreadMessage();

    @GetMapping("recipientId/{id}")
    ResponseEntity<DialogDto> getDialogBetweenUsers(@PathVariable Long id);

    @GetMapping("/messages")
    ResponseEntity<Page<MessageShortDto>> getMessagesToDialog(@RequestParam("recipientId") Long recipientId, Pageable pageable);


}
