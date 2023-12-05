package ru.skillbox.diplom.group42.social.service.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group42.social.service.entity.auth.User;
import ru.skillbox.diplom.group42.social.service.repository.base.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {
    Optional<User> findByEmail(String email);
    List<User> findAllByFirstNameIgnoreCaseOrLastNameIgnoreCase(String firstName, String lastName);

}
