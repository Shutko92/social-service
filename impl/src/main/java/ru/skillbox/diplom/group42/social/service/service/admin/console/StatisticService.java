package ru.skillbox.diplom.group42.social.service.service.admin.console;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.request.StatisticRequestDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.request.StatisticType;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.AccountCountPerAgeDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.AccountStatisticResponseDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.StatisticPerDateDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.StatisticResponseDto;
import ru.skillbox.diplom.group42.social.service.entity.account.Account;
import ru.skillbox.diplom.group42.social.service.entity.account.Account_;
import ru.skillbox.diplom.group42.social.service.entity.post.Post;
import ru.skillbox.diplom.group42.social.service.entity.post.Post_;
import ru.skillbox.diplom.group42.social.service.entity.post.comment.Comment;
import ru.skillbox.diplom.group42.social.service.entity.post.comment.Comment_;
import ru.skillbox.diplom.group42.social.service.entity.post.like.Like;
import ru.skillbox.diplom.group42.social.service.entity.post.like.Like_;
import ru.skillbox.diplom.group42.social.service.mapper.admin.console.AdminConsoleMapper;
import ru.skillbox.diplom.group42.social.service.repository.account.AccountRepository;
import ru.skillbox.diplom.group42.social.service.repository.post.CommentRepository;
import ru.skillbox.diplom.group42.social.service.repository.post.LikeRepository;
import ru.skillbox.diplom.group42.social.service.repository.post.PostRepository;
import ru.skillbox.diplom.group42.social.service.utils.SpecificationUtil;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final AccountRepository accountRepository;
    private final AdminConsoleMapper adminConsoleMapper;

    /**
     * Метод обрабатывает данные трех возможных видов в зависимости от указателя, делая запрос в базу данных,
     * и преобразуя их в статистику за последние 12 часов и по месяцам.
     * @param request dto запроса на статистику за определенный период.
     * @param type указатель, определяющий какой тип данных запрашивается в конкретном случае: post, comment, или like.
     * @return результат обработкт статистики.
     */
    public StatisticResponseDto postLikeCommentStatistic(StatisticRequestDto request, StatisticType type) {
        ZonedDateTime firstMonth = ZonedDateTime.parse(request.getFirstMonth());
        ZonedDateTime lastMonth = ZonedDateTime.parse(request.getLastMonth());
        ZonedDateTime date = ZonedDateTime.parse(request.getDate());

        List<ZonedDateTime> dataList = collectDates(firstMonth, lastMonth, type);
        List<StatisticPerDateDto> perHour = calculateHourRatio(dataList);
        List<StatisticPerDateDto> monthly = calculateMonthlyRatio(dataList, firstMonth);

        return adminConsoleMapper.dataToStatisticResponse(date, dataList.size(), monthly, perHour);
    }

    /**
     * Метод обрабатывает данные аккаунтов, делая запрос в базу данных, и преобразуя их в статистику по возрастным
     * категориям и месяцам.
     * @param request dto запроса на статистику за определенный период.
     * @return результат обработкт статистики.
     */
    public AccountStatisticResponseDto accountStatistic(StatisticRequestDto request) {
        ZonedDateTime firstMonth = ZonedDateTime.parse(request.getFirstMonth());
        ZonedDateTime lastMonth = ZonedDateTime.parse(request.getLastMonth());
        ZonedDateTime date = ZonedDateTime.parse(request.getDate());

        List<Account> accountList = accountRepository.findAll(
                SpecificationUtil.between(Account_.createdOn, firstMonth, lastMonth, true));
        List<ZonedDateTime> accountDatesList = accountList.stream().map(Account::getCreatedOn).collect(Collectors.toList());
        List<AccountCountPerAgeDto> perAge = calculateAgeRatio(accountList);
        List<StatisticPerDateDto> monthly = calculateMonthlyRatio(accountDatesList, firstMonth);

        return adminConsoleMapper.dataToAccountStatisticResponse(date, accountList.size(), monthly, perAge);
    }

    private List<ZonedDateTime> collectDates(ZonedDateTime firstMonth, ZonedDateTime lastMonth, StatisticType type) {
        if (type.equals(StatisticType.POST)) {
            return postRepository.findAll(SpecificationUtil.between(Post_.publishDate, firstMonth, lastMonth, true))
                    .stream().map(Post::getTime).collect(Collectors.toList());
        }
        if (type.equals(StatisticType.COMMENT)) {
            return commentRepository.findAll(SpecificationUtil.between(Comment_.time, firstMonth, lastMonth, true))
                    .stream().map(Comment::getTime).collect(Collectors.toList());
        }
        return likeRepository.findAll(SpecificationUtil.between(Like_.time, firstMonth, lastMonth, true))
                .stream().map(Like::getTime).collect(Collectors.toList());
    }

    private List<StatisticPerDateDto> calculateMonthlyRatio(List<ZonedDateTime> list, ZonedDateTime date) {
        Map<Integer, Integer> monthToAmountMap = list.stream().collect(Collectors.groupingBy(ZonedDateTime::getMonth))
                .entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey().getValue(), entry -> entry.getValue().size()));
        List<StatisticPerDateDto> statisticList = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            StatisticPerDateDto statisticPerDate = new StatisticPerDateDto();
            statisticPerDate.setDate(date.plusMonths(i-1));
            statisticPerDate.setCount(monthToAmountMap.getOrDefault(i, 0));
            statisticList.add(statisticPerDate);
        }
        return statisticList;
    }

    private List<StatisticPerDateDto> calculateHourRatio(List<ZonedDateTime> list) {
        ZonedDateTime functionDate = ZonedDateTime.now().minusHours(11);
        Map<Integer, Integer> monthToAmountMap = list.stream()
                .filter(time -> time.isAfter(functionDate)).collect(Collectors.groupingBy(ZonedDateTime::getHour))
                .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size()));

        List<StatisticPerDateDto> statisticList = new ArrayList<>();
            int hour = functionDate.getHour();
            for (int i = 1; i <= 12;i++) {
                StatisticPerDateDto statisticPerDate = new StatisticPerDateDto();
                statisticPerDate.setDate(functionDate.plusHours(i-1));
                statisticPerDate.setCount(monthToAmountMap.getOrDefault(hour, 0));
                statisticList.add(statisticPerDate);
                if (hour== 23) {
                    hour=-1;
                }
                hour++;
            }
        return statisticList;
    }

    private List<AccountCountPerAgeDto> calculateAgeRatio(List<Account> accountList) {
        if (accountList.stream().noneMatch(account -> account.getBirthDate() != null)) {
            return List.of(new AccountCountPerAgeDto(0,0));
        }
        Map<Integer, Integer> ageGrouped = accountList.stream().filter(account -> account.getBirthDate() != null).collect(Collectors.groupingBy(Account::getBirthDate))
                .entrySet().stream().collect(Collectors.toMap(entry -> Period.between(entry.getKey().toLocalDate(), LocalDate.now()).getYears(),
                        entry -> entry.getValue().size()));
        List<AccountCountPerAgeDto> statisticList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : ageGrouped.entrySet()) {
            statisticList.add(new AccountCountPerAgeDto(entry.getKey(), entry.getValue()));
        }
        return statisticList;
    }
}
