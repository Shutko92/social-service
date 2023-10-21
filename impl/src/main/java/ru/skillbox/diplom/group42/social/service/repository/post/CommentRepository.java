package ru.skillbox.diplom.group42.social.service.repository.post;

import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group42.social.service.entity.post.comment.Comment;
import ru.skillbox.diplom.group42.social.service.repository.base.BaseRepository;

@Repository
public interface CommentRepository extends BaseRepository<Comment> {

}
