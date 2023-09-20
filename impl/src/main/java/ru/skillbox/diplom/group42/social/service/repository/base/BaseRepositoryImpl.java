package ru.skillbox.diplom.group42.social.service.repository.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity;

import javax.persistence.EntityManager;

@NoRepositoryBean
public class BaseRepositoryImpl<Entity extends BaseEntity> extends SimpleJpaRepository<Entity, Long> implements BaseRepository<Entity> {

    EntityManager entityManager;

    @Autowired
    public BaseRepositoryImpl(JpaEntityInformation<Entity, Long> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public void deleteById(Long id) {
        super.findById(id).ifPresent(entity -> {
            entity.setIsDeleted(true);
            super.save(entity);
        });
    }

    @Override
    public void deleteAll(Iterable<? extends Entity> entities) {
        entities.forEach(entity -> {
            entity.setIsDeleted(true);
            super.save(entity);
        });
    }

    @Override
    public void delete(Entity entity) {
        entity.setIsDeleted(true);
        super.save(entity);

    }
}
