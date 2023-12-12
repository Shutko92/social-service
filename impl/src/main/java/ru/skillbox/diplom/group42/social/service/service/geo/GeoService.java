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

    /**
     * Метод обращается к Классу VkConnector, чтобы запустить процесс сбора данных через Vk open API, как внешний ресурс.
     * @throws Exception при невозможности подключения к внешнему API.
     */
//    @Scheduled(cron = "0 0 2 1 1-12 *")
    @Scheduled(cron = "@midnight")
    public void loadCountries() throws Exception {
        vkConnector.vkInit();
    }

    /**
     *Метод запрашивает имеющиеся страны из базы данных.
     * @return имеющиеся страны.
     */
    public List<CountryDto> getAllCountries() {
        return geoMapper.countriesToDtoList(countryRepository.findAll());
    }

    /**
     *Метод запрашивает из базы данных города, которые соответствуют id определенной страны.
     * @param countryId идентификатор страны.
     * @return города.
     */
    public List<CityDto> getCitiesBy(Long countryId) {
        return geoMapper.citiesToDtoList(cityRepository.findAllByCountryId(countryId));
    }
}