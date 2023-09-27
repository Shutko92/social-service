package ru.skillbox.diplom.group42.social.service.entity.captcha;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.time.ZonedDateTime;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Data
@Table(name = "captcha")
public class Captcha {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    private UUID secret;

    @Column(name = "code")
    private String code;


    @Column(name = "time",columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime time;


    @Transient
    private BufferedImage image;

    public Captcha(String code, ZonedDateTime time, BufferedImage image) {
        this.code = code;
        this.time = time;
        this.image = image;
    }
}
