package ru.skillbox.diplom.group42.social.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skillbox.diplom.group42.social.service.dto.geo.CityDto;
import ru.skillbox.diplom.group42.social.service.dto.geo.CountryDto;
import ru.skillbox.diplom.group42.social.service.entity.geo.City;
import ru.skillbox.diplom.group42.social.service.entity.geo.Country;
import ru.skillbox.diplom.group42.social.service.mapper.geo.GeoMapper;

import java.util.List;

import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createCityList;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createCountryList;

public class GeoMapperTest {
    private final List<City> cityList = createCityList();
    private final List<Country> countryList = createCountryList();
    private final GeoMapper geoMapper = Mappers.getMapper(GeoMapper.class);

    @Test
    public void funCitiesToDtoListIsCorrect(){
        List<CityDto> citiesToDtoList = geoMapper.citiesToDtoList(cityList);
        for (int next = 0; next < cityList.size(); next++) {
            Assertions.assertEquals(cityList.get(next).getTitle(), citiesToDtoList.get(next).getTitle());
            Assertions.assertEquals(cityList.get(next).getCountryId(), citiesToDtoList.get(next).getCountryId());
        }
    }
    @Test
    public void funCountriesToDtoListIsCorrect(){
        List<CountryDto> countryDtoList = geoMapper.countriesToDtoList(countryList);
        for (int next = 0; next < countryList.size(); next++) {
            Assertions.assertEquals(countryList.get(next).getTitle(), countryDtoList.get(next).getTitle());
            List<String> cityList1 = countryDtoList.get(next).getCities();
            for (int i = 0; i < cityList1.size(); i++) {
                Assertions.assertEquals(countryList.get(next).getCities().get(i).getTitle(), cityList1.get(i));
            }
        }
    }
}
