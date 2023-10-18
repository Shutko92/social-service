package ru.skillbox.diplom.group42.social.service.entity.tag;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity;
import ru.skillbox.diplom.group42.social.service.entity.post.Post;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@Entity
@Setter
@Getter
@Table(name = "tag")
public class Tag extends BaseEntity {

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Post> post;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
