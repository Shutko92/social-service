package ru.skillbox.diplom.group42.social.service.service.geo;

import com.vk.api.sdk.client.Lang;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.database.responses.GetCitiesResponse;
import com.vk.api.sdk.objects.database.responses.GetCountriesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity;
import ru.skillbox.diplom.group42.social.service.entity.geo.City;
import ru.skillbox.diplom.group42.social.service.entity.geo.Country;
import ru.skillbox.diplom.group42.social.service.repository.geo.CityRepository;
import ru.skillbox.diplom.group42.social.service.repository.geo.CountryRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class VkConnector {
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    @Value("${vk_data.app_id}")
    private Integer APP_ID;
    @Value("${vk_data.token}")
    private String access_token;
    @Value("${vk_data.country_code_list}")
    private String countryCode;

    public void vkInit() throws Exception {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);

        UserActor actor = new UserActor(APP_ID, access_token);
        collectCountries(vk, actor);
    }

    private void collectCountries(VkApiClient vk, UserActor actor) throws Exception {
        GetCountriesResponse countryResponse = vk.database().getCountries(actor).lang(Lang.RU).needAll(false).code(countryCode).execute();
        List<Country> countryHolder = countryResponse.getItems().stream().map(countryGot -> {
            Country country = new Country();
            country.setId(Long.valueOf(countryGot.getId()));
            country.setTitle(countryGot.getTitle());
            return country;
        }).collect(Collectors.toList());

        List<Long> countryIdList = countryHolder.stream().map(BaseEntity::getId).collect(Collectors.toList());
        for (Country country : countryHolder) {
            if (!countryRepository.existsByTitle(country.getTitle())) {
                countryRepository.save(country);
            }
        }

        collectCities(vk, actor, countryIdList);
    }

    private void collectCities(VkApiClient vk, UserActor actor, List<Long> idList) throws Exception {
        for (Long id : idList) {
            GetCitiesResponse cityResponse;
            Set<City> cities;
            int count = 999;
            int offset = 0;

            do {
                cityResponse = vk.database().getCities(actor, Math.toIntExact(id)).lang(Lang.RU).needAll(true).offset(offset).count(count).execute();
                cities = cityResponse.getItems().stream().map(cityGot -> {
                    City city = new City();
                    city.setTitle(cityGot.getTitle());
                    city.setCountryId(id);
                    return city;
                }).collect(Collectors.toSet());

                saveCities(cities);
                offset += count;
                Thread.sleep(3000);
            } while (cityResponse.getCount()>0);
        }
    }

    private void saveCities(Set<City> cities) {
        for (City city : cities) {
            if (!cityRepository.existsByTitle(city.getTitle())) {
                cityRepository.save(city);
            }
        }
    }
}
