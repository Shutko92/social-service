package ru.skillbox.diplom.group42.social.service.repository.geo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group42.social.service.entity.geo.City;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    List<City> findAllByCountryId(Long countryId);

    boolean existsByTitle(String title);
}
