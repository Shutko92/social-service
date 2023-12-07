package ru.skillbox.diplom.group42.social.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skillbox.diplom.group42.social.service.dto.dialog.DialogDto;
import ru.skillbox.diplom.group42.social.service.entity.dialog.Dialog;
import ru.skillbox.diplom.group42.social.service.mapper.dialog.DialogMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createTestDialog;

class DialogMapperTest {
    private DialogMapper mapper = Mappers.getMapper(DialogMapper.class);

    @Test
    void convertToDtoIsCorrect() {

        Dialog dialog = createTestDialog();
        DialogDto dialogDto = mapper.convertToDto(dialog);
        assertEquals(dialog.getId(), dialogDto.getId());
        assertEquals(dialog.getUnreadCount(), dialogDto.getUnreadCount());
        assertEquals(dialog.getConversationPartner1(), dialogDto.getConversationPartner1());
        assertEquals(dialog.getConversationPartner2(), dialogDto.getConversationPartner2());
        assertEquals(dialog.getIsDeleted(), dialogDto.getIsDeleted());
    }

    @Test
    void convertToListDtoIsCorrect() {
        List<Dialog> dialogs = new ArrayList<>();
        Dialog dialog = createTestDialog();
        for (int i = 0; i < 3; i++) {
            dialogs.add(dialog);
        }
        List<DialogDto> dialogDtoList = mapper.convertToListDto(dialogs);
        for (DialogDto dialogDto : dialogDtoList) {
            assertEquals(dialog.getId(), dialogDto.getId());
            assertEquals(dialog.getUnreadCount(), dialogDto.getUnreadCount());
            assertEquals(dialog.getConversationPartner1(), dialogDto.getConversationPartner1());
            assertEquals(dialog.getConversationPartner2(), dialogDto.getConversationPartner2());
            assertEquals(dialog.getIsDeleted(), dialogDto.getIsDeleted());
        }
    }
}