package ru.skillbox.diplom.group42.social.service.dto.post.comment;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseDto;

import java.time.ZonedDateTime;

@Getter
@Setter
@EqualsAndHashCode
public class CommentDto extends BaseDto {

    private CommentType commentType;
    private ZonedDateTime time;
    private ZonedDateTime timeChanged;
    private Long authorId;
    private Long parentId;
    private String commentText;
    private Long postId;
    private Boolean isBlocked;
    private Long likeAmount;
    private Boolean myLike;
    private Integer commentCount;
    private String imagePath;


    @Override
    public String toString() {
        return "CommentDto{" +
                "commentType=" + commentType +
                ", time=" + time +
                ", timeChanged=" + timeChanged +
                ", authorId=" + authorId +
                ", parentId=" + parentId +
                ", commentText='" + commentText + '\'' +
                ", postId=" + postId +
                ", isBlocked=" + isBlocked +
                ", likeAmount=" + likeAmount +
                ", myLike=" + myLike +
                ", commentCount=" + commentCount +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
