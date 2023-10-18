package ru.skillbox.diplom.group42.social.service.repository.post;

import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group42.social.service.entity.post.like.Like;
import ru.skillbox.diplom.group42.social.service.repository.base.BaseRepository;

@Repository
public interface LikeRepository extends BaseRepository<Like> {
    Like findByAuthorIdAndItemId(Long authorId, Long itemId);
    boolean existsByAuthorIdAndItemId(Long authorId, Long itemId);
}
