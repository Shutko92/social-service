package ru.skillbox.diplom.group42.social.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skillbox.diplom.group42.social.service.mapper.geo.GeoMapper;
import ru.skillbox.diplom.group42.social.service.repository.geo.CityRepository;
import ru.skillbox.diplom.group42.social.service.repository.geo.CountryRepository;
import ru.skillbox.diplom.group42.social.service.service.geo.GeoService;
import ru.skillbox.diplom.group42.social.service.service.geo.VkConnector;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GeoServiceTest {
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private GeoMapper geoMapper;
    @Mock
    private VkConnector vkConnector;
    private GeoService geoService;

    @BeforeEach
    public void beforeMethod(){
        geoService = new GeoService(countryRepository, cityRepository, geoMapper, vkConnector);
    }
    @Test
    public void funLoadCountriesShouldInvokeVkConnectorInit(){
        try {
            geoService.loadCountries();
            verify(vkConnector).vkInit();
        } catch (Exception ex){

        }
    }
    @Test
    public void funGetAllCountriesShouldInvokeCountryRepositoryFindAll(){
        geoService.getAllCountries();
        verify(countryRepository).findAll();
    }
    @Test
    public void funGetCitiesByShouldInvokeCountryRepositoryFindByCountryId(){
        geoService.getCitiesBy(1000L);
        verify(cityRepository).findAllByCountryId(anyLong());
    }
}
