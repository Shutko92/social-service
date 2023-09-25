package ru.skillbox.diplom.group42.social.service.dto.post.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseSearchDto;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentSearchDto extends BaseSearchDto {

    private CommentType commentType;
    private Long authorId;
    private Long parentId;
    private Long postId;

    @Override
    public String toString() {
        return "CommentSearchDto{" +
                "commentType=" + commentType +
                ", authorId=" + authorId +
                ", parentId=" + parentId +
                ", postId=" + postId +
                '}';
    }
}
