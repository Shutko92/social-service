package ru.skillbox.diplom.group42.social.service.repository.account;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.skillbox.diplom.group42.social.service.entity.account.Account;
import ru.skillbox.diplom.group42.social.service.repository.base.BaseRepository;

import java.util.List;

public interface AccountRepository extends BaseRepository<Account> {

    @Query(value = "SELECT id FROM account WHERE id!=?1 ORDER BY RANDOM() LIMIT ?2", nativeQuery = true)
    List<Long> getRandomIds(Long id, Integer num);

    @Query(value = "SELECT acc FROM Account AS acc WHERE extract(MONTH FROM acc.birthDate) = :month AND extract(DAY FROM acc.birthDate) = :day")
    List<Account> getAccountsByBirthDateMonthValueAndBirthDateDayOfMonth(@Param("month") Integer month,@Param("day") Integer day);


}