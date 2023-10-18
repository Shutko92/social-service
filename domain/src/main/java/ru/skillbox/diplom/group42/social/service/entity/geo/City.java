package ru.skillbox.diplom.group42.social.service.entity.geo;

import lombok.*;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "city", indexes = @Index(name = "idx_city_name", columnList = "title"))
public class City extends BaseEntity {
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "country_id")
    private Long countryId;
}
