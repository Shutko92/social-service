package ru.skillbox.diplom.group42.social.service.mapper.dialog;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group42.social.service.dto.dialog.DialogDto;
import ru.skillbox.diplom.group42.social.service.entity.dialog.Dialog;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DialogMapper {

    @Mapping(target = "lastMessage", ignore = true)
    DialogDto convertToDto(Dialog dialog);
    @Mapping(target = "lastMessage", ignore = true)
    List<DialogDto> convertToListDto(List<Dialog> dialog);
}
