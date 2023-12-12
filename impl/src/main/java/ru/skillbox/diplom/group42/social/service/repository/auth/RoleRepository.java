package ru.skillbox.diplom.group42.social.service.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group42.social.service.entity.auth.Role;
import ru.skillbox.diplom.group42.social.service.repository.base.BaseRepository;

@Repository
public interface RoleRepository extends BaseRepository<Role> {
    Role findByName(String name);
}
