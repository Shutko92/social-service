package ru.skillbox.diplom.group42.social.service.service.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group42.social.service.dto.post.like.LikeDto;
import ru.skillbox.diplom.group42.social.service.dto.post.like.TypeLike;
import ru.skillbox.diplom.group42.social.service.entity.post.Post;
import ru.skillbox.diplom.group42.social.service.entity.post.comment.Comment;
import ru.skillbox.diplom.group42.social.service.entity.post.like.Like;
import ru.skillbox.diplom.group42.social.service.exception.CommentFoundException;
import ru.skillbox.diplom.group42.social.service.exception.PostFoundException;
import ru.skillbox.diplom.group42.social.service.mapper.like.LikeMapper;
import ru.skillbox.diplom.group42.social.service.repository.post.CommentRepository;
import ru.skillbox.diplom.group42.social.service.repository.post.LikeRepository;
import ru.skillbox.diplom.group42.social.service.repository.post.PostRepository;
import ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public LikeDto addLike(Long id, TypeLike typeLike) {
        log.info("Start LikeService createLike Id" + id + " likeDto " + typeLike.toString());
        if (typeLike.equals(TypeLike.COMMENT)) {
            Comment comment = commentRepository.findById(id).orElseThrow(CommentFoundException::new);
            comment.setLikeAmount(comment.getLikeAmount() + 1);
            comment.setMyLike(true);
            commentRepository.save(comment);
            return likeMapper.convertToDto(likeRepository.save(createLike(id, typeLike)));
        } else {
            Post post = postRepository.findById(id).orElseThrow(PostFoundException::new);
            post.setLikeAmount(post.getLikeAmount() + 1);
            post.setMyLike(true);
            postRepository.save(post);
            return likeMapper.convertToDto(likeRepository.save(createLike(id, typeLike)));
        }
    }

    public void deleteLike(Long id, TypeLike typeLike) {
        Like like = likeRepository.findByAuthorIdAndItemId(SecurityUtil.getJwtUserIdFromSecurityContext(), id);
        log.info("like" + like.toString());

        if (like != null) {
            likeRepository.deleteById(like.getId());

            if (typeLike.equals(TypeLike.COMMENT)) {
                Comment comment = commentRepository.getReferenceById(id);
                comment.setLikeAmount(comment.getLikeAmount() - 1);
                comment.setMyLike(false);
                commentRepository.save(comment);
            } else {
                Post post = postRepository.getById(id);
                post.setLikeAmount(post.getLikeAmount() - 1);
                post.setMyLike(false);
                postRepository.save(post);

            }
        }


    }


    private Like createLike(Long id, TypeLike typeLike) {
        Like like = new Like();
        like.setItemId(id);
        like.setType(typeLike);
        like.setTime(ZonedDateTime.now());
        like.setIsDeleted(false);
        like.setAuthorId(SecurityUtil.getJwtUserIdFromSecurityContext());
        like.setReactionType(null);

        log.info(like.toString());
        return like;
    }

}
