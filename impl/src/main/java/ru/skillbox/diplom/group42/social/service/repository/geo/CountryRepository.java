package ru.skillbox.diplom.group42.social.service.repository.geo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group42.social.service.entity.geo.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    boolean existsByTitle(String title);
}
