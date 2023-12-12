package ru.skillbox.diplom.group42.social.service.service.friend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group42.social.service.dto.friend.CountDto;
import ru.skillbox.diplom.group42.social.service.dto.friend.FriendSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.friend.FriendShortDto;
import ru.skillbox.diplom.group42.social.service.dto.notification.NotificationType;
import ru.skillbox.diplom.group42.social.service.entity.account.Account;
import ru.skillbox.diplom.group42.social.service.entity.account.StatusCode;
import ru.skillbox.diplom.group42.social.service.entity.friend.Friend;
import ru.skillbox.diplom.group42.social.service.mapper.friend.FriendMapper;
import ru.skillbox.diplom.group42.social.service.repository.account.AccountRepository;
import ru.skillbox.diplom.group42.social.service.repository.friend.FriendRepository;
import ru.skillbox.diplom.group42.social.service.service.notification.NotificationHandler;
import ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.skillbox.diplom.group42.social.service.utils.friend.FriendsUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final AccountRepository accountRepository;
    private final FriendMapper friendMapper;
    private final NotificationHandler notificationHandler;

    /**
     * Метод ищет друзей по спецификации и параметрам через репозиторий и конвертирует результат в ответ.
     *
     * @param friendSearchDto параметры поиска друзей.
     * @param page            параметр для разделения на страницы.
     * @return страничная информация о друзьях.
     */
    public Page<FriendShortDto> getFriends(FriendSearchDto friendSearchDto, Pageable page) {
        return friendRepository.findAll(
                getFriendSpecification(friendSearchDto),
                page
        ).map(friendMapper::convertToFriendShortDto);
    }

    /**
     * Метод вызывает идентификаторы пользователя и его собеседника и ищет аккаунты через репозиторий или выбрасывает исключение.
     * Вызывает другой метод, если ответ отрицательный, ищет записи друзей через репозиторий, проставляет им статусы друзей.
     * Иначе конвертирует аккаунты в сущность друзей, устанавливает им связи друзей, отправляет нотификацию на дружбу,
     * сохраняет друзей, конвертирует друга в ответ.
     *
     * @param \id идентифиеатор аккаунта.
     * @return информация о друге.
     */
    public FriendShortDto friendRequest(Long idTo) throws BadCredentialsException {
        Long idFrom = SecurityUtil.getJwtUserIdFromSecurityContext();
        Account accountTo = getAccount(idTo);
        Account accountFrom = getAccount(idFrom);
        Friend friendTo;
        Friend friendFrom;
        if (!shouldCreateNewEntries(idTo, StatusCode.REQUEST_TO)) {
            Specification<Friend> specificationTo = getSpecificationAboutFriendRecords(idFrom, idTo, StatusCode.REQUEST_TO);
            friendTo = friendRepository.findAll(specificationTo).get(0);
            refreshStatus(friendTo, false, StatusCode.REQUEST_TO.toString());
            Specification<Friend> specificationFrom = getSpecificationAboutFriendRecords(idTo, idFrom, StatusCode.REQUEST_FROM);
            friendFrom = friendRepository.findAll(specificationFrom).get(0);
            refreshStatus(friendFrom, false, StatusCode.REQUEST_FROM.toString());
        } else {
            friendTo = friendMapper.convertToFriendTo(accountTo, idFrom);
            friendFrom = friendMapper.convertToFriendFrom(accountFrom, accountTo);
            notificationHandler.sendNotifications(idFrom,
                    idTo,
                    NotificationType.FRIEND_REQUEST,
                    "Поступила заявка в друзья!"
            );
        }
        friendRepository.save(friendTo);
        friendRepository.save(friendFrom);
        return friendMapper.convertToFriendShortDto(friendTo);
    }

    /**
     * Метод Ищет записи о дружбе по идентификаторам через репозитории, проставляет им статус дружбы, сохраняет друзей,
     * конвертирует пользователя в ответ
     *
     * @param id идентифиеатор аккаунта.
     * @return информация о пользователе.
     */
    public FriendShortDto friendApprove(Long id) {
        Friend friend = friendRepository.findByIdFromAndIdToAndStatusCode(id, SecurityUtil.getJwtUserIdFromSecurityContext(), "REQUEST_TO");
        friend.setStatusCode(StatusCode.FRIEND.toString());
        friendRepository.save(friend);
        Friend friend2 = friendRepository.findByIdFromAndIdToAndStatusCode(SecurityUtil.getJwtUserIdFromSecurityContext(), id, "REQUEST_FROM");
        friend2.setStatusCode(StatusCode.FRIEND.toString());
        friendRepository.save(friend2);
        return friendMapper.convertToFriendShortDto(friend);
    }

    public CountDto getCountRequests() {
        Specification<Friend> specification = getSpecificationFriendCount(SecurityUtil.getJwtUserIdFromSecurityContext());
        return new CountDto((long) friendRepository.findAll(specification).size());
    }

    /**
     * Метод вызывает идентификаторы пользователя и его собеседника и ищет аккаунты через репозиторий или выбрасывает исключение.
     * Вызывает другой метод, если ответ отрицательный, ищет записи друзей через репозиторий, проставляет им статусы друзей.
     * Иначе конвертирует аккаунты в сущность друзей, устанавливает им связи друзей, отправляет нотификацию на дружбу,
     * сохраняет друзей, конвертирует друга в ответ.
     *
     * @param \id идентифиеатор аккаунта.
     * @return информация о пользователе.
     */
    public FriendShortDto subscribeById(Long watchingId) {
        Long subscriberId = SecurityUtil.getJwtUserFromSecurityContext().getId();
        Account accountWatching = getAccount(watchingId);
        Account accountSubscribed = getAccount(subscriberId);
        Friend friendWatching;
        Friend friendSubscribed;
        if (!shouldCreateNewEntries(watchingId, StatusCode.SUBSCRIBED)) {
            friendWatching = getFriendWithActualStatus(accountWatching, subscriberId, watchingId, StatusCode.WATCHING);
            friendSubscribed = getFriendWithActualStatus(accountSubscribed, watchingId, subscriberId, StatusCode.SUBSCRIBED);
        } else {
            friendWatching = friendMapper.convertFriendToFriend(
                    new Friend(),
                    accountWatching.getStatusCode() != null ? accountWatching.getStatusCode() : null,
                    subscriberId,
                    watchingId,
                    StatusCode.WATCHING,
                    accountWatching.getIsOnline()
            );
            friendSubscribed = friendMapper.convertFriendToFriend(
                    new Friend(),
                    accountSubscribed.getStatusCode() != null ? accountSubscribed.getStatusCode() : null,
                    watchingId,
                    subscriberId,
                    StatusCode.SUBSCRIBED,
                    accountSubscribed.getIsOnline()
            );
        }
        friendRepository.save(friendWatching);
        friendRepository.save(friendSubscribed);
        return friendMapper.convertToFriendShortDto(friendWatching);
    }

    private Friend getFriendWithActualStatus(Account account, Long idFrom, Long idTo, StatusCode statusCode) {
        Specification<Friend> specificationUser = getSpecificationAboutFriendRecords(idFrom, idTo, statusCode);
        Friend friend = friendRepository.findAll(specificationUser).get(0);
        friend = friendMapper.convertFriendToFriend(
                friend,
                account.getStatusCode() != null ? account.getStatusCode() : null,
                idFrom,
                idTo,
                statusCode,
                account.getIsOnline()
        );
        return friend;
    }

    /**
     * Метод ищет записи дружбы пользователя и собеседника, меняет им статусы, что дружбы нет, удаляет друзей, конвертирует
     * пользователя в ответ.
     *
     * @param \id идентификатор друга.
     * @return информация о пользователе.
     */
    public FriendShortDto deleteById(Long deleteId) {
        Long userId = SecurityUtil.getJwtUserFromSecurityContext().getId();
        String previousStatus1 = StatusCode.NONE.toString();
        String previousStatus2 = StatusCode.NONE.toString();
        Friend friend = friendRepository.findByIdFromAndIdToAndStatusCode(userId, deleteId, StatusCode.FRIEND.toString());
        Friend friend2 = friendRepository.findByIdFromAndIdToAndStatusCode(deleteId, userId, StatusCode.FRIEND.toString());
        if (friend == null) {
            friend = friendRepository.findByIdFromAndIdToAndStatusCode(userId, deleteId, StatusCode.REQUEST_FROM.toString());
            friend2 = friendRepository.findByIdFromAndIdToAndStatusCode(deleteId, userId, StatusCode.REQUEST_TO.toString());
            previousStatus1 = StatusCode.REQUEST_FROM.toString();
            previousStatus2 = StatusCode.REQUEST_TO.toString();
        } else {
            previousStatus1 = StatusCode.FRIEND.toString();
            previousStatus2 = StatusCode.FRIEND.toString();
        }
        try {
            friend.setPreviousStatusCode(previousStatus1);
            friend2.setPreviousStatusCode(previousStatus2);
            friend.setStatusCode(StatusCode.NONE.toString());
            friend2.setStatusCode(StatusCode.NONE.toString());
            FriendShortDto shortDto = friendMapper.convertToFriendShortDto(friend);
            friendRepository.save(friend);
            friendRepository.save(friend2);
            return shortDto;
        } catch (Exception exception) {
            throw new UsernameNotFoundException("User " + deleteId + " not found");
        }
    }

    /**
     * Метод вызывает другой метод, если ответ отрицательный, ищет записи о дружбе по спецификации, конвертирует результат в ответ.
     * Иначе вызывает другой метод, ищет записи о дружбе по спецификации, конвертирует результат в ответ.
     *
     * @param friendSearchDto параметры поиска друзей.
     * @return информация о друзьях.
     */
    public List<FriendShortDto> recommended(FriendSearchDto friendSearchDto) {
        int connectionAdd = MAX_CONNECTION - hasConnections() <= 0 ? MAX_CONNECTION : MAX_CONNECTION - hasConnections();
        createRandomConnections(connectionAdd);
        return friendRepository.findAll(getRecommendationsSpecification(friendSearchDto)).stream()
                .map(friendMapper::convertToFriendShortDto).collect(Collectors.toList());
    }

    public FriendShortDto block(Long idBlocked) throws UsernameNotFoundException {
        Long userId = SecurityUtil.getJwtUserFromSecurityContext().getId();
        Friend friend = friendRepository.findByIdFromAndIdToAndStatusCode(userId, idBlocked, StatusCode.FRIEND.toString());
        Friend friend2 = friendRepository.findByIdFromAndIdToAndStatusCode(idBlocked, userId, StatusCode.FRIEND.toString());
        Friend rawRecommendation = friendRepository.findByIdFromAndIdToAndStatusCode(
                userId,
                idBlocked,
                StatusCode.RECOMMENDATION.toString()
        );
        if (rawRecommendation != null) {
            rawRecommendation.setPreviousStatusCode(StatusCode.RECOMMENDATION.toString());
            rawRecommendation.setStatusCode(StatusCode.REJECTING.toString());
            friendRepository.save(rawRecommendation);
        }
        if (friend2 != null) {
            friend2.setPreviousStatusCode(String.valueOf(StatusCode.FRIEND));
            friend2.setStatusCode(String.valueOf(StatusCode.NONE));
            friendRepository.save(friend2);
        }
        if (friend != null) {
            friend.setPreviousStatusCode(String.valueOf(StatusCode.FRIEND));
            friend.setStatusCode(String.valueOf(StatusCode.BLOCKED));

            friendRepository.save(friend);
            return friendMapper.convertToFriendShortDto(friend);
        }
        throw new UsernameNotFoundException("User " + idBlocked + " not found.");
    }

    public FriendShortDto unBlock(Long idUnBlocked) throws UsernameNotFoundException {
        Long userId = SecurityUtil.getJwtUserFromSecurityContext().getId();
        Friend friend = friendRepository.findByIdFromAndIdToAndStatusCode(userId, idUnBlocked, StatusCode.BLOCKED.toString());
        Friend rawRecommendation = friendRepository.findByIdFromAndIdToAndStatusCode(
                userId,
                idUnBlocked,
                StatusCode.REJECTING.toString()
        );
        if (rawRecommendation != null) {
            rawRecommendation.setPreviousStatusCode(StatusCode.REJECTING.toString());
            rawRecommendation.setStatusCode(StatusCode.RECOMMENDATION.toString());
            friendRepository.save(rawRecommendation);
        }
        if (friend != null) {
            friend.setPreviousStatusCode(String.valueOf(StatusCode.BLOCKED));
            friend.setStatusCode(String.valueOf(StatusCode.FRIEND));
            friendRepository.save(friend);
            return friendMapper.convertToFriendShortDto(friend);
        }
        throw new UsernameNotFoundException("User " + idUnBlocked + " not found.");
    }

    private Specification<Friend> getRecommendationsSpecification(FriendSearchDto friendSearchDto) {
        Long userId = SecurityUtil.getJwtUserFromSecurityContext().getId();
        Specification<Friend> specification = getSpecificationOfRecommendedUserId(userId);
        Set<Long> recommendation = friendRepository.findAll(specification)
                .stream()
                .map(Friend::getId)
                .collect(Collectors.toSet());
        return getSpecificationByRecommendation(friendSearchDto, recommendation);
    }

    private Integer hasConnections() {
        Long userId = SecurityUtil.getJwtUserFromSecurityContext().getId();
        Specification<Friend> specification = getSpecificationOfRecommendedUserId(userId);
        return friendRepository.findAll(specification).size();
    }

    private boolean shouldCreateNewEntries(Long friendId, StatusCode statusCode) {
        List<StatusCode> statuses = new ArrayList<>();
        if (statusCode.equals(StatusCode.REQUEST_TO) || statusCode.equals(StatusCode.REQUEST_FROM)) {
            statuses.add(StatusCode.REQUEST_TO);
            statuses.add(StatusCode.REQUEST_FROM);
        } else if (statusCode.equals(StatusCode.WATCHING) || statusCode.equals(StatusCode.SUBSCRIBED)) {
            statuses.add(StatusCode.WATCHING);
            statuses.add(StatusCode.SUBSCRIBED);
        }
        Long userId = SecurityUtil.getJwtUserFromSecurityContext().getId();
        Specification<Friend> specificationTo = getSpecificationAboutFriendRecords(userId, friendId, statuses.get(0));
        Specification<Friend> specificationFrom = getSpecificationAboutFriendRecords(userId, friendId, statuses.get(1));
        return friendRepository.findAll(specificationTo).isEmpty() && friendRepository.findAll(specificationFrom).isEmpty();
    }

    private void createRandomConnections(int number) throws BadCredentialsException {
        Long userId = SecurityUtil.getJwtUserFromSecurityContext().getId();
        List<Long> listId = accountRepository.getRandomIds(userId, number);
        if (!listId.isEmpty()) {
            for (int i = 0; i < listId.size() && i <= number; i++) {
                Account account = accountRepository.findById(listId.get(i))
                        .orElseThrow(() -> new BadCredentialsException("User with id " + listId.get(0) + " does not exist"));
                if (isRawNoBlockedOrRejecting(userId, account.getId())) {
                    Friend friend = friendMapper.convertToFriend(account);
                    friend.setRating(1);
                    friend.setStatusCode(StatusCode.RECOMMENDATION.toString());
                    friend.setIdFrom(userId);
                    friend.setIdTo(account.getId());
                    friendRepository.save(friend);
                }
            }
        }
    }

    private Boolean isRawNoBlockedOrRejecting(Long idFrom, Long idTo) {
        Friend blocked = friendRepository.findByIdFromAndIdToAndStatusCode(idFrom, idTo, StatusCode.BLOCKED.toString());
        Friend rejecting = friendRepository.findByIdFromAndIdToAndStatusCode(idFrom, idTo, StatusCode.REJECTING.toString());
        Friend exist = friendRepository.findByIdFromAndIdToAndStatusCode(idFrom, idTo, StatusCode.RECOMMENDATION.toString());
        return rejecting == null && blocked == null && exist == null;
    }

    private Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new BadCredentialsException("User with id " + id + " does not exist"));
    }

    private void refreshStatus(Friend friend, Boolean isDelete, String statusCode) {
        friend.setIsDeleted(isDelete);
        friend.setStatusCode(statusCode);
    }
}
