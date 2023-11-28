package ru.skillbox.diplom.group42.social.service.entity.account;

import lombok.*;
import ru.skillbox.diplom.group42.social.service.entity.auth.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "account")
public class Account extends User {

    private String phone;
    private String photo;
    private String profileCover;
    private String about;
    private String city;
    private String country;
    private StatusCode statusCode;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime regDate;

    private ZonedDateTime birthDate;

    private String messagePermission;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime lastOnlineTime;

    private Boolean isOnline;
    private Boolean isBlocked;
    private String emojiStatus;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdOn;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime updatedOn;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime deletionTimestamp;
}