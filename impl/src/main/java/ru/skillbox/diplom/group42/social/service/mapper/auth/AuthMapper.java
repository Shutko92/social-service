package ru.skillbox.diplom.group42.social.service.mapper.auth;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group42.social.service.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group42.social.service.entity.auth.User;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "password", source = "password1")
    User convertRegistrationDtoToUser(RegistrationDto registrationDto);
}
