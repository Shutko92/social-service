package ru.skillbox.diplom.group42.social.service.entity.post;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.dto.post.Type;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity;
import ru.skillbox.diplom.group42.social.service.entity.reaction.Reaction;
import ru.skillbox.diplom.group42.social.service.entity.tag.Tag;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "post")
public class Post extends BaseEntity {

    @Column(name = "time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime time;

    @Column(name = "time_changed", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime timeChanged;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Column(name = "post_text")
    private String postText;

    @Column(name = "comments_count")
    private Integer commentsCount;

    @ManyToMany()
    @JoinTable(name = "tags_post", joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private Set<Tag> tags;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "reaction_post", joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "reaction_id", referencedColumnName = "id"))
    private Set<Reaction> reactions;

    @Column(name = "my_reaction")
    private String myReaction;

    @Column(name = "like_amount")
    private Integer likeAmount;

    @Column(name = "my_like")
    private Boolean myLike;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "publish_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime publishDate;
}
