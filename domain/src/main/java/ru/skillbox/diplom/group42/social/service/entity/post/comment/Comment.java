package ru.skillbox.diplom.group42.social.service.entity.post.comment;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentType;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "comment_type")
    private CommentType commentType;

    @Column(name = "time")
    private ZonedDateTime time;

    @Column(name = "time_changed")
    private ZonedDateTime timeChanged;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "comment_text")
    private String commentText;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @Column(name = "like_amount")
    private Integer likeAmount;

    @Column(name = "my_like")
    private Boolean myLike;

    @Column(name = "comment_count")
    private Integer commentCount;

    @Column(name = "image_path")
    private String imagePath;

}
