package ru.skillbox.diplom.group42.social.service.entity.auth;

import lombok.*;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"user\"")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseEntity {

    private String firstName;

    private String lastName;

    @Column(name = "email",unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> role;

}
