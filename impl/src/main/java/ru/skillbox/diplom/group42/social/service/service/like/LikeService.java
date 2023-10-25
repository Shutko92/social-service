package ru.skillbox.diplom.group42.social.service.service.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group42.social.service.dto.post.ReactionDto;
import ru.skillbox.diplom.group42.social.service.dto.post.like.LikeDto;
import ru.skillbox.diplom.group42.social.service.dto.post.like.TypeLike;
import ru.skillbox.diplom.group42.social.service.entity.post.Post;
import ru.skillbox.diplom.group42.social.service.entity.post.comment.Comment;
import ru.skillbox.diplom.group42.social.service.entity.post.like.Like;
import ru.skillbox.diplom.group42.social.service.exception.CommentFoundException;
import ru.skillbox.diplom.group42.social.service.exception.PostFoundException;
import ru.skillbox.diplom.group42.social.service.exception.ResourceFoundException;
import ru.skillbox.diplom.group42.social.service.mapper.like.LikeMapper;
import ru.skillbox.diplom.group42.social.service.repository.post.CommentRepository;
import ru.skillbox.diplom.group42.social.service.repository.post.LikeRepository;
import ru.skillbox.diplom.group42.social.service.repository.post.PostRepository;
import ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public LikeDto addLike(Long id, LikeDto likeDto, TypeLike typeLike) {
        Like like = likeRepository.findByAuthorIdAndItemIdAndTypeLike(SecurityUtil.getJwtUserIdFromSecurityContext(), id, typeLike).orElse(new Like());

        if (like.getId() == null) {
            like = likeMapper.createEntity(likeDto);
            like.setItemId(id);
            like.setType(typeLike);
            setLikeAmount(typeLike, id, 1);
        } else {
            like.setReactionType(likeDto.getReactionType());
            like.setTime(ZonedDateTime.now());
            if (like.getIsDeleted()) {
                like.setIsDeleted(false);
            }
            setLikeAmount(typeLike, id, 1);
        }
        return likeMapper.convertToDto(likeRepository.save(like));
    }

    public void deleteLike(Long id, TypeLike typeLike) {
        Like like = likeRepository.findByAuthorIdAndItemIdAndTypeLike(SecurityUtil.getJwtUserIdFromSecurityContext(), id, typeLike)
                .orElseThrow(() -> new ResourceFoundException(HttpStatus.NOT_FOUND, "like with id " + id + " not found"));
        if (like.getId() != null || !like.getIsDeleted()) {
            like.setReactionType(null);
            setLikeAmount(typeLike, id, -1);
            likeRepository.deleteById(like.getId());
        }
    }

    private void setLikeAmount(TypeLike typeLike, Long itemId, int value) {
        if (typeLike.equals(TypeLike.COMMENT)) {
            Comment comment = commentRepository.findById(itemId).orElseThrow(CommentFoundException::new);
            comment.setLikeAmount(comment.getLikeAmount() + value);
            commentRepository.save(comment);
        } else {
            Post post = postRepository.findById(itemId).orElseThrow(PostFoundException::new);
            post.setLikeAmount(post.getLikeAmount() + value);
            post.setMyLike(true);
            postRepository.save(post);
        }
    }

    public Set<ReactionDto> getSetReactionDto(Long postId) {
        List<Like> likeList = likeRepository.findAllByItemIdAndTypeLikeAndIsDeletedFalse(postId, TypeLike.POST);
        Set<ReactionDto> reactionDtoList = new HashSet<>();
        likeList.forEach(like -> {
            ReactionDto reactionDto = new ReactionDto();
            reactionDto.setReactionType(like.getReactionType());
            reactionDto.setCount(likeRepository.countByItemIdAndReactionType(like.getItemId(), like.getReactionType()));
            reactionDto.setType(like.getType());
            reactionDtoList.add(reactionDto);
        });
        return reactionDtoList;
    }

    public String getMyReaction(Long itemId, TypeLike typeLike) {
        Like like = likeRepository.findByAuthorIdAndItemIdAndTypeLike(SecurityUtil.getJwtUserIdFromSecurityContext(), itemId, typeLike).orElse(null);
        if (like != null) {
            return like.getReactionType();
        } else {
            return null;
        }
    }

    public boolean isThereMyLikeToComment(Long itemId, TypeLike typeLike){
        Like like = likeRepository.findByAuthorIdAndItemIdAndTypeLike(SecurityUtil.getJwtUserIdFromSecurityContext(), itemId, typeLike).orElse(null);
        if (like != null && !like.getIsDeleted()) {
            return true;
        } else {
            return false;
        }

    }

}
