package ru.skillbox.diplom.group42.social.service.repository.post;

import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group42.social.service.dto.post.like.ReactionType;
import ru.skillbox.diplom.group42.social.service.dto.post.like.TypeLike;
import ru.skillbox.diplom.group42.social.service.entity.post.like.Like;
import ru.skillbox.diplom.group42.social.service.repository.base.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends BaseRepository<Like> {
    Optional<Like> findByAuthorIdAndItemIdAndTypeLike(Long authorId, Long itemId, TypeLike typeLike);
    boolean existsByAuthorIdAndItemId(Long authorId, Long itemId);
    List<Like> findAllByItemIdAndTypeLikeAndIsDeletedFalse(Long itemId, TypeLike typeLike);
    Integer countByItemIdAndReactionType(Long itemId, ReactionType reactionType);
}
