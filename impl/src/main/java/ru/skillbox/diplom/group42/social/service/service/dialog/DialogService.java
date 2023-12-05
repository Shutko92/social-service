package ru.skillbox.diplom.group42.social.service.service.dialog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group42.social.service.dto.dialog.DialogDto;
import ru.skillbox.diplom.group42.social.service.dto.dialog.DialogSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.message.*;
import ru.skillbox.diplom.group42.social.service.dto.notification.NotificationType;
import ru.skillbox.diplom.group42.social.service.entity.dialog.Dialog;
import ru.skillbox.diplom.group42.social.service.entity.dialog.Dialog_;
import ru.skillbox.diplom.group42.social.service.entity.message.Message;
import ru.skillbox.diplom.group42.social.service.entity.message.Message_;
import ru.skillbox.diplom.group42.social.service.exception.ResourceFoundException;
import ru.skillbox.diplom.group42.social.service.mapper.dialog.DialogMapper;
import ru.skillbox.diplom.group42.social.service.mapper.message.MessageMapper;
import ru.skillbox.diplom.group42.social.service.repository.dialog.DialogRepository;
import ru.skillbox.diplom.group42.social.service.repository.message.MessageRepository;
import ru.skillbox.diplom.group42.social.service.service.notification.NotificationHandler;
import ru.skillbox.diplom.group42.social.service.utils.SpecificationUtil;
import ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DialogService {

    private final DialogRepository dialogRepository;
    private final DialogMapper dialogMapper;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final NotificationHandler notificationHandler;


    /**
     * Метод делает с передаваемым идентификатором пользователя форму поиска, по которой ищет через репозиторий подходящие диалоги.
     * Если ничего не находится, создается новый диалог и сохраняется. В противном случае достается первый диалог и конвертируется в ответ.
     * @param id идентификатор пользователя.
     * @return информация о диалоге.
     */
    public DialogDto getDialogBetweenUsers(Long id) {
        DialogSearchDto dialogSearchDto = new DialogSearchDto();
        dialogSearchDto.setConversationPartner1(SecurityUtil.getJwtUserIdFromSecurityContext());
        dialogSearchDto.setConversationPartner2(id);
        List<Dialog> dialogList = dialogRepository.findAll(getSpecificationDialog(dialogSearchDto));
        DialogDto dialogDto;
        Dialog dialog = new Dialog();
        if (dialogList.isEmpty()) {
            dialog.setUnreadCount(0);
            dialog.setIsDeleted(false);
            dialog.setConversationPartner1(SecurityUtil.getJwtUserIdFromSecurityContext());
            dialog.setConversationPartner2(id);
            dialogDto = dialogMapper.convertToDto(dialogRepository.save(dialog));
            dialogDto.setLastMessage(new ArrayList<>());
            dialogRepository.save(dialog);
            return dialogDto;
        } else {
            dialog = dialogList.get(0);
            dialogDto = dialogMapper.convertToDto(dialog);
            dialogDto.setLastMessage(messageMapper.convertToListDTO(dialog.getLastMessage().stream()
                    .sorted(Comparator.comparing(Message::getTime).reversed()).collect(Collectors.toList())));
            return dialogDto;
        }
    }

    /**
     * Метод делает с переданным идентификатором пользователя форму поиска, по которой ищет подходящие сообщения
     * с помощью репозитория. Результат конвертируется в ответ.
     * @param recipientId идентификатор пользователя.
     * @param pageable параметр для разделения информации на страницы.
     * @return постраничная информация о сообщениях.
     */
    public Page<MessageShortDto> getMessagesToDialog(Long recipientId, Pageable pageable) {
        MessageSearchDto messageSearchDto = new MessageSearchDto();
        messageSearchDto.setConversationPartner1(SecurityUtil.getJwtUserIdFromSecurityContext());
        messageSearchDto.setConversationPartner2(recipientId);
        messageSearchDto.setIsDeleted(false);
        Page<Message> messagePage = messageRepository.findAll(getSpecificationMessage(messageSearchDto)
                , PageRequest.of(pageable.getPageNumber(), Integer.MAX_VALUE, pageable.getSort()));
        Page<MessageShortDto> messageShortDtoPage = messagePage.map(messageMapper::convertToShortDto);
        return messageShortDtoPage;
    }

    /**
     * Метод делает форму поиска, по которой ищет через репозиторий подходящие диалоги. Результат обрабатывается,
     * сортируется и конвертируется в ответ.
     * @param pageable параметр для разделения информации на страницы.
     * @return постраничная информация о диалогах.
     */
    public Page<DialogDto> getAllDialogs(Pageable pageable) {
        DialogSearchDto dialogSearchDto = new DialogSearchDto();
        dialogSearchDto.setConversationPartner1(SecurityUtil.getJwtUserIdFromSecurityContext());
        Page<Dialog> dialogPage = dialogRepository.findAll(getSpecificationDialog(dialogSearchDto), pageable);
        return new PageImpl<>(dialogPage.map(dialog -> {
            DialogDto dialogDto = dialogMapper.convertToDto(dialog);
            dialogDto.setLastMessage(messageMapper.convertToListDTO(dialog.getLastMessage()
                    .stream().sorted(Comparator.comparing(Message::getTime).reversed()).collect(Collectors.toList())));
            return dialogDto;
        }).toList(), pageable, dialogPage.getTotalElements());

    }

    /**
     * Метод ищет диалог через репозиторийпо идентификатору. Если ничего не найдено, выбрасывается исключение.
     * Для каждого найденного сообщения, если оно со статусом "отправлено", и один из партнеров - конкретный пользователь,
     * проставляется статус "прочитано" и актуализируется счетчик.
     * @param dialogId идентификатор диалога.
     */
    public void updateStatusMessage(Long dialogId) {
        AtomicInteger count = new AtomicInteger();
        Dialog dialog = dialogRepository.findById(dialogId)
                .orElseThrow(() -> new ResourceFoundException(HttpStatus.NOT_FOUND, "dialog with id " + dialogId + "not found"));
        dialog.getLastMessage().forEach(message -> {
            if (message.getReadStatus().equals(ReadStatus.SENT)
                    && message.getConversationPartner2().equals(SecurityUtil.getJwtUserIdFromSecurityContext())) {
                message.setReadStatus(ReadStatus.READ);
                count.getAndDecrement();
            }
            messageRepository.save(message);
        });
        updateCountUnreadMessageToDialog(dialogId, count.get());
    }

    private void updateCountUnreadMessageToDialog(Long dialogId, int count) {
        Dialog dialog = dialogRepository.findById(dialogId)
                .orElseThrow(() -> new ResourceFoundException(HttpStatus.NOT_FOUND, "dialog with id " + dialogId + "not found"));
        dialog.setUnreadCount(dialog.getUnreadCount() + count);
        dialogRepository.save(dialog);
    }

    /**
     * Метод считает сообщения пользователя по статусу "отправлено" через репозиторий.
     * @return количество сообщений.
     */
    public UnreadCountDto getCountUnreadMessage() {
        UnreadCountDto unreadCountDto = new UnreadCountDto();
        unreadCountDto.setCount(messageRepository
                .countByConversationPartner2AndReadStatusIs(SecurityUtil.getJwtUserIdFromSecurityContext(), ReadStatus.SENT));
        return unreadCountDto;
    }

    /**
     * Метод конвертирует параметры поиска в сущность, ищет через репозиторий диалог, соответствующий идентификатору
     * поиска, актуализирует счетчик непрочитанных сообщений, отправляет нотификацию партнеру диалога, сохраняет его
     * и конвертирует в ответ.
     * @param messageDto параметры поиска сообщений.
     * @return информация о диалоге.
     */
    public MessageDto createMessage(MessageDto messageDto) {
        Message messageEntity = messageMapper.convertToEntity(messageDto);
        Dialog dialog = dialogRepository.getById(messageDto.getDialogId());
        messageEntity.setDialogId(dialog);
        updateCountUnreadMessageToDialog(dialog.getId(), 1);
        notificationHandler.sendNotifications(messageDto.getConversationPartner1(),messageDto.getConversationPartner2(), NotificationType.MESSAGE, messageEntity.getMessageText());
        return messageMapper.convertToDto(messageRepository.save(messageEntity));
    }


    private Specification<Dialog> getSpecificationDialog(DialogSearchDto dto) {
        return SpecificationUtil.getBaseSpecification(dto)
                .and(SpecificationUtil.equal(Dialog_.conversationPartner1, dto.getConversationPartner1(), true)
                        .and(SpecificationUtil.equal(Dialog_.conversationPartner2, dto.getConversationPartner2(), true)))
                .or(SpecificationUtil.equal(Dialog_.conversationPartner1, dto.getConversationPartner2(), true)
                        .and(SpecificationUtil.equal(Dialog_.conversationPartner2, dto.getConversationPartner1(), true)))
                .and(SpecificationUtil.equal(Dialog_.isDeleted, dto.getIsDeleted(), true));

    }

    private Specification<Message> getSpecificationMessage(MessageSearchDto messageSearchDto) {
        return SpecificationUtil.getBaseSpecification(messageSearchDto)
                .and(SpecificationUtil.equal(Message_.conversationPartner1, messageSearchDto.getConversationPartner1(), true)
                        .and(SpecificationUtil.equal(Message_.conversationPartner2, messageSearchDto.getConversationPartner2(), true)))
                .or(SpecificationUtil.equal(Message_.conversationPartner1, messageSearchDto.getConversationPartner2(), true)
                        .and(SpecificationUtil.equal(Message_.conversationPartner2, messageSearchDto.getConversationPartner1(), true)))
                .and(SpecificationUtil.equal(Message_.readStatus, messageSearchDto.getReadStatus(), true))
                .and(SpecificationUtil.equal(Message_.isDeleted, messageSearchDto.getIsDeleted(), true));

    }


}
