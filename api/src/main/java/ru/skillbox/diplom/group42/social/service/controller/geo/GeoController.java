package ru.skillbox.diplom.group42.social.service.controller.geo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group42.social.service.dto.geo.CityDto;
import ru.skillbox.diplom.group42.social.service.dto.geo.CountryDto;
import ru.skillbox.diplom.group42.social.service.util.ConstantURL;

import java.util.List;

@Tag(name = "Страны", description = "Определение страны пользователя")
@RestController
@RequestMapping(value = ConstantURL.BASE_URL + "/geo")
public interface GeoController {

    @Operation(summary = "Загрузка стран", description = "Страны загружаются с внешнего источника. Проходит по расписанию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный сбор данных о странах"),
            @ApiResponse(responseCode = "500", description = "Запрос не может быть выполнен из-за ошибки сервера")
    })
    @PutMapping("/load")
    void loadCountries() throws Exception;

    @Operation(summary = "Запрос стран", description = "Список стран с привязанными городами выводится из базы данных")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос списка стран")
    })
    @GetMapping("/country")
    List<CountryDto> getCountries();

    @Operation(summary = "Запрос городов", description = "Список городов по id страны выводится из базы данных")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос списка городов")
    })
    @GetMapping("/country/{countryId}/city")
    List<CityDto> getCities(@PathVariable Long countryId);
}
