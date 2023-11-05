package ru.skillbox.diplom.group42.social.service.controller.admin.console;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.request.StatisticRequestDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.request.StatisticType;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.AccountStatisticResponseDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.StatisticResponseDto;
import ru.skillbox.diplom.group42.social.service.service.admin.console.StatisticService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminConsoleControllerImpl implements AdminConsoleController {
    private final StatisticService service;

    @Override
    public ResponseEntity<StatisticResponseDto> returnPost(StatisticRequestDto request) {
        return ResponseEntity.ok(service.postLikeCommentStatistic(request, StatisticType.POST));
    }

    @Override
    public ResponseEntity<StatisticResponseDto> returnLike(StatisticRequestDto request) {
        return ResponseEntity.ok(service.postLikeCommentStatistic(request, StatisticType.LIKE));
    }

    @Override
    public ResponseEntity<StatisticResponseDto> returnComment(StatisticRequestDto request) {
        return ResponseEntity.ok(service.postLikeCommentStatistic(request, StatisticType.COMMENT));
    }

    @Override
    public ResponseEntity<AccountStatisticResponseDto> returnAccount(StatisticRequestDto request) {
        return ResponseEntity.ok(service.accountStatistic(request));
    }
}
