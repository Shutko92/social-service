package ru.skillbox.diplom.group42.social.service.entity.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = "create_sequence")
    @Column(name = "id")
    private Long id;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}
