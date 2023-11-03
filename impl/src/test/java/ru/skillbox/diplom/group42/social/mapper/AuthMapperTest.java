package ru.skillbox.diplom.group42.social.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skillbox.diplom.group42.social.service.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group42.social.service.entity.auth.User;
import ru.skillbox.diplom.group42.social.service.mapper.auth.AuthMapper;

import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createUser;

public class AuthMapperTest {
    private final AuthMapper authMapper = Mappers.getMapper(AuthMapper.class);
    private final User testUser = createUser("TEST_USER");
    @Test
    public void funConvertRegistrationDtoToUserIsCorrect(){
        RegistrationDto registrationDto = new RegistrationDto(
                testUser.getEmail(),
                testUser.getPassword(),
                testUser.getPassword(),
                testUser.getFirstName(),
                testUser.getLastName(),
                " ",
                " "
        );
        User actualUser = authMapper.convertRegistrationDtoToUser(registrationDto);
        Assertions.assertEquals(testUser, actualUser);
    }
}
