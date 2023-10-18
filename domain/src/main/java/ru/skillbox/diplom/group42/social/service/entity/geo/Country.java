package ru.skillbox.diplom.group42.social.service.entity.geo;

import lombok.*;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "country")
public class Country extends BaseEntity {
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "cities", nullable = false)
    @OneToMany(mappedBy = "countryId", cascade = CascadeType.ALL)
    private List<City> cities;
}
