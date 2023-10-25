package ru.skillbox.diplom.group42.social.service.entity.post.like;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.dto.post.like.ReactionType;
import ru.skillbox.diplom.group42.social.service.dto.post.like.TypeLike;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Table(name = "`like`")
@EqualsAndHashCode
public class Like extends BaseEntity {

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "time")
    private ZonedDateTime time;

    @Column(name = "item_id")
    private Long itemId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeLike type;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type")
    private ReactionType reactionType;

}
