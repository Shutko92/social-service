package ru.skillbox.diplom.group42.social.service.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity;

@NoRepositoryBean
public interface BaseRepository<Entity extends BaseEntity> extends JpaRepository<Entity, Long>, JpaSpecificationExecutor<Entity> {

    void deleteById(Long id);

    void deleteAll(Iterable<? extends Entity> entities);

    void delete(Entity entity);
}
