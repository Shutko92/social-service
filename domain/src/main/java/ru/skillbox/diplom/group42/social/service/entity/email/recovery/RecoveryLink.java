package ru.skillbox.diplom.group42.social.service.entity.email.recovery;

import lombok.*;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recovery_link")
public class RecoveryLink extends BaseEntity {
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "link")
    private UUID recoveryLink;
}

