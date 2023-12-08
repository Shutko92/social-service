package ru.skillbox.diplom.group42.social.service.entity.friend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.ZonedDateTime;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "friend")
public class Friend extends BaseEntity {

 @Column(name = "photo",columnDefinition = "TEXT")
 private String photo;

 @Column(name = "status_code")
 private String statusCode;

 @Column(name = "first_name")
 private String firstName;

 @Column(name = "last_name")
 private String lastName;

 @Column(name = "city")
 private String city;

 @Column(name = "country")
 private String country;

 @Column(name = "birth_date")
 private ZonedDateTime birthDate;

 @Column(name = "is_online")
 private Boolean isOnline;

 @Column(name = "id_from")
 private Long idFrom;

 @Column(name = "id_to")
 private Long idTo;

 @Column(name = "previous_status")
 private String previousStatusCode;

 @Column(name = "rating")
 private Integer rating;

}
