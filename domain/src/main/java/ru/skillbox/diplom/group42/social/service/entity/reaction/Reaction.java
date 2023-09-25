package ru.skillbox.diplom.group42.social.service.entity.reaction;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity;
import ru.skillbox.diplom.group42.social.service.entity.post.Post;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "reaction")
public class Reaction extends BaseEntity {

    @Column(name = "reaction_type")
    private String reactionType;

    @Column(name = "count")
    private Integer count;

    @ManyToMany(mappedBy = "reactions")
    private List<Post> posts;

}
