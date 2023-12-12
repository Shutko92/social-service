package ru.skillbox.diplom.group42.social.service.controller.admin.console;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.request.StatisticRequestDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.AccountStatisticResponseDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.StatisticResponseDto;
import ru.skillbox.diplom.group42.social.service.util.ConstantURL;

@RequestMapping(ConstantURL.BASE_URL + "/admin-console/statistic")
public interface AdminConsoleController {

    @GetMapping("/post")
    ResponseEntity<StatisticResponseDto> returnPost(StatisticRequestDto request);

    @GetMapping("/like")
    ResponseEntity<StatisticResponseDto> returnLike(StatisticRequestDto request);

    @GetMapping("/comment")
    ResponseEntity<StatisticResponseDto> returnComment(StatisticRequestDto request);

    @GetMapping("/account")
    ResponseEntity<AccountStatisticResponseDto> returnAccount(StatisticRequestDto request);
}
