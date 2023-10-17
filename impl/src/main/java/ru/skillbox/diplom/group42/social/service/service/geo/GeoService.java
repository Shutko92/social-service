package ru.skillbox.diplom.group42.social.service.service.geo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group42.social.service.dto.geo.CityDto;
import ru.skillbox.diplom.group42.social.service.dto.geo.CountryDto;
import ru.skillbox.diplom.group42.social.service.mapper.geo.GeoMapper;
import ru.skillbox.diplom.group42.social.service.repository.geo.CityRepository;
import ru.skillbox.diplom.group42.social.service.repository.geo.CountryRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoService {
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final GeoMapper geoMapper;
    private final VkConnector vkConnector;

//    @Scheduled(cron = "0 0 2 1 1-12 *")
    @Scheduled(cron = "@midnight")
    public void loadCountries() throws Exception {
        log.info("Method loadCountries was executed in " + this.getClass().getName());
        vkConnector.vkInit();
    }

    public List<CountryDto> getAllCountries() {
        log.info("Method getAllCountries was executed in " + this.getClass().getName());
        return geoMapper.countriesToDtoList(countryRepository.findAll());
    }

    public List<CityDto> getCitiesBy(Long countryId) {
        log.info("Method getCitiesBy country_id " + countryId + " was executed in " + this.getClass().getName());
        return geoMapper.citiesToDtoList(cityRepository.findAllByCountryId(countryId));
    }
}