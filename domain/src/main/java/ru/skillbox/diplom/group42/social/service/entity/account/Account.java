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
    @Column(name = "phone")
    private String phone;
    @Column(name = "photo")
    private String photo;
    @Column(name = "profile_cover")
    private String profileCover;
    @Column(name = "about")
    private String about;
    @Column(name = "city")
    private String city;
    @Column(name = "country")
    private String country;
    @Column(name = "status_code")
    private StatusCode statusCode;
    @Column(name = "reg_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime regDate;
    @Column(name = "birth_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime birthDate;
    @Column(name = "message_permission")
    private String messagePermission;
    @Column(name = "last_online_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime lastOnlineTime;
    @Column(name = "is_online")
    private Boolean isOnline;
    @Column(name = "is_blocked")
    private Boolean isBlocked;
    @Column(name = "emoji_status")
    private String emojiStatus;
    @Column(name = "created_on", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdOn;
    @Column(name = "updated_on", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime updatedOn;
    @Column(name = "deletion_timestamp", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime deletionTimestamp;
}