package ru.skillbox.diplom.group42.social.service.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.diplom.group42.social.service.entity.account.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}