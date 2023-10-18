package ru.skillbox.diplom.group42.social.service.repository.tag;

import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group42.social.service.entity.tag.Tag;
import ru.skillbox.diplom.group42.social.service.repository.base.BaseRepository;

import java.util.List;

@Repository
public interface TagRepository extends BaseRepository<Tag> {

    List<Tag> findByNameIn(List<String> nameList);

    Tag findByName(String tag);
}
