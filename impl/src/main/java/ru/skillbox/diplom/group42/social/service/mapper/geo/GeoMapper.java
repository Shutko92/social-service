package ru.skillbox.diplom.group42.social.service.mapper.geo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group42.social.service.dto.geo.CityDto;
import ru.skillbox.diplom.group42.social.service.dto.geo.CountryDto;
import ru.skillbox.diplom.group42.social.service.entity.geo.City;
import ru.skillbox.diplom.group42.social.service.entity.geo.Country;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface GeoMapper {
    List<CityDto> citiesToDtoList(List<City> cities);

    @Mapping(target = "cities", expression = "java(pickNames(cities))")
    List<CountryDto> countriesToDtoList(List<Country> countries);

    default List<String> pickNames(List<City> cities) {
        return cities.stream().map(City::getTitle).collect(Collectors.toList());
    }
}