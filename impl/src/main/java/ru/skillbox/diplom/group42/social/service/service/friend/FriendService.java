package ru.skillbox.diplom.group42.social.service.service.friend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group42.social.service.dto.friend.CountDto;
import ru.skillbox.diplom.group42.social.service.dto.friend.FriendSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.friend.FriendShortDto;
import ru.skillbox.diplom.group42.social.service.dto.notification.NotificationType;
import ru.skillbox.diplom.group42.social.service.entity.account.Account;
import ru.skillbox.diplom.group42.social.service.entity.account.StatusCode;
import ru.skillbox.diplom.group42.social.service.entity.friend.Friend;
import ru.skillbox.diplom.group42.social.service.entity.friend.Friend_;
import ru.skillbox.diplom.group42.social.service.mapper.friend.FriendMapper;
import ru.skillbox.diplom.group42.social.service.repository.account.AccountRepository;
import ru.skillbox.diplom.group42.social.service.repository.friend.FriendRepository;
import ru.skillbox.diplom.group42.social.service.service.notification.NotificationHandler;
import ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.skillbox.diplom.group42.social.service.utils.SpecificationUtil.*;

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
     * @param friendSearchDto параметры поиска друзей.
     * @param page параметр для разделения на страницы.
     * @return страничная информация о друзьях.
     */
    public Page<FriendShortDto> getFriends(FriendSearchDto friendSearchDto, Pageable page) {

        log.info("FriendService method search(FriendSearchDto friendSearchDto, Pageable page) executed");
        return friendRepository.findAll(getFriendSpecification(friendSearchDto)
                , page).map(friendMapper::convertToFriendShortDto);
    }

    /**
     * Метод вызывает идентификаторы пользователя и его собеседника и ищет аккаунты через репозиторий или выбрасывает исключение.
     * Вызывает другой метод, если ответ отрицательный, ищет записи друзей через репозиторий, проставляет им статусы друзей.
     * Иначе конвертирует аккаунты в сущность друзей, устанавливает им связи друзей, отправляет нотификацию на дружбу,
     * сохраняет друзей, конвертирует друга в ответ.
     * @param id идентифиеатор аккаунта.
     * @return информация о друге.
     */
    public FriendShortDto friendRequest(Long id) {
        Long myId = SecurityUtil.getJwtUserIdFromSecurityContext();
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new BadCredentialsException("User with id " + id + " does not exist"));
        Account accountFrom = accountRepository.findById(SecurityUtil.getJwtUserIdFromSecurityContext())
                .orElseThrow(() -> new BadCredentialsException("User with id " + id + " does not exist"));
        Friend friendTo;
        Friend friendFrom;
        if (!shouldCreateNewEntries(id)) {
            log.info("Previous records found");
            friendTo = friendRepository.findFriendByIdFromAndIdToAndIsDeletedFalse(SecurityUtil.getJwtUserIdFromSecurityContext(), id);
            friendTo.setIsDeleted(false);
            friendFrom = friendRepository.findFriendByIdFromAndIdToAndIsDeletedFalse(id, SecurityUtil.getJwtUserIdFromSecurityContext());
            friendFrom.setIsDeleted(false);
            friendTo.setStatusCode(StatusCode.REQUEST_TO.toString());
            friendFrom.setStatusCode(StatusCode.REQUEST_FROM.toString());
        } else {
            friendTo = friendMapper.convertToFriend(account);
            friendTo.setStatusCode(StatusCode.REQUEST_TO.toString());
            friendTo.setIdFrom(myId);
            friendTo.setIdTo(id);
            friendTo.setPreviousStatusCode(account.getStatusCode() != null ? account.getStatusCode().toString() : null);
            friendTo.setRating(0);
            friendTo.setOnline(account.getIsOnline());


            friendFrom = friendMapper.convertToFriend(accountFrom);
            friendFrom.setStatusCode(StatusCode.REQUEST_FROM.toString());
            friendFrom.setIdFrom(id);
            friendFrom.setIdTo(myId);
            friendFrom.setPreviousStatusCode(account.getStatusCode() != null ? account.getStatusCode().toString() : null);
            friendFrom.setRating(0);
            friendFrom.setOnline(account.getIsOnline());
            notificationHandler.sendNotifications(id, NotificationType.FRIEND_REQUEST, "Поступила заявка в друзья!");
        }

        friendRepository.save(friendTo);
        friendRepository.save(friendFrom);


        return friendMapper.convertToFriendShortDto(friendTo);
    }

    /**
     * Метод Ищет записи о дружбе по идентификаторам через репозитории, проставляет им статус дружбы, сохраняет друзей,
     * конвертирует пользователя в ответ
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

    private static Specification<Friend> getFriendSpecification(FriendSearchDto friendSearchDto) {
        log.debug("Entering getFriendSpecification method");

        System.out.println("IdFrom: " + friendSearchDto.getIdFrom() + ", and IdTo: " + friendSearchDto.getIdTo());
        System.out.println("For id: " + SecurityUtil.getJwtUserFromSecurityContext().getId() + " : " + isItself(friendSearchDto));


        return getBaseSpecification(friendSearchDto)
                .and(equal(Friend_.isDeleted, false, true))
                .and(equal(Friend_.idFrom, friendSearchDto.getIdFrom(), true))
                .and(equal(Friend_.statusCode, friendSearchDto.getStatusCode(), true))
                //.and(equal(Friend_.idTo, friendSearchDto.getIdTo(), true))
                .and(equal(Friend_.previousStatusCode, friendSearchDto.getPreviousStatusCode(), true))
                .and(equal(Friend_.idFrom, SecurityUtil.getJwtUserFromSecurityContext().getId(), true))
                .and(notIn(Friend_.idTo, Collections.singleton(SecurityUtil.getJwtUserFromSecurityContext().getId()), true))
                ;

    }


    public CountDto getCountRequests() {

        return new CountDto(friendRepository.getCountRequest(SecurityUtil.getJwtUserIdFromSecurityContext()));
    }

    /**
     * Метод вызывает идентификаторы пользователя и его собеседника и ищет аккаунты через репозиторий или выбрасывает исключение.
     * Вызывает другой метод, если ответ отрицательный, ищет записи друзей через репозиторий, проставляет им статусы друзей.
     * Иначе конвертирует аккаунты в сущность друзей, устанавливает им связи друзей, отправляет нотификацию на дружбу,
     * сохраняет друзей, конвертирует друга в ответ.
     * @param id идентифиеатор аккаунта.
     * @return информация о пользователе.
     */
    public FriendShortDto subscribeById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new BadCredentialsException("User with id " + id + " does not exist"));
        Account accountTo = accountRepository.findById(SecurityUtil.getJwtUserFromSecurityContext().getId())
                .orElseThrow(() -> new BadCredentialsException("User with id " + SecurityUtil.getJwtUserFromSecurityContext().getId() + " does not exist"));
        Friend friend;
        Friend friendTo;
        if (!shouldCreateNewEntries(id)) {
            friend = friendRepository.findFriendByIdFromAndIdToAndIsDeletedFalse(SecurityUtil.getJwtUserFromSecurityContext().getId(), id);
            friend.setPreviousStatusCode(account.getStatusCode() != null ? account.getStatusCode().toString() : null);
            friend.setStatusCode(StatusCode.WATCHING.toString());
            friend.setIdFrom(SecurityUtil.getJwtUserFromSecurityContext().getId());
            friend.setIdTo(id);
            friend.setIsDeleted(false);

            friendTo = friendRepository.findFriendByIdFromAndIdToAndIsDeletedFalse(id, SecurityUtil.getJwtUserFromSecurityContext().getId());
            friendTo.setPreviousStatusCode(account.getStatusCode() != null ? account.getStatusCode().toString() : null);
            friendTo.setStatusCode(StatusCode.SUBSCRIBED.toString());
            friendTo.setIdFrom(id);
            friendTo.setIdTo(SecurityUtil.getJwtUserFromSecurityContext().getId());
            friendTo.setIsDeleted(false);
        }
//            friend = new Friend(
//                    account.getPhoto(),
//                    StatusCode.WATCHING.toString(),
//                    account.getFirstName(),
//                    account.getLastName(),
//                    account.getCity(),
//                    account.getCountry(),
//                    account.getBirthDate(),
//                    account.isOnline(),
//                    SecurityUtil.getJwtUserFromSecurityContext().getId(),
//                    id,
//                    account.getStatusCode()!=null?account.getStatusCode().toString():null,
//                    0
//            );

        else {
            friend = friendMapper.convertToFriend(account);
            friendTo = friendMapper.convertToFriend(accountTo);
            friend.setPreviousStatusCode(account.getStatusCode() != null ? account.getStatusCode().toString() : null);
            friend.setStatusCode(StatusCode.WATCHING.toString());
            friend.setIdFrom(SecurityUtil.getJwtUserFromSecurityContext().getId());
            friend.setIdTo(id);
            friend.setRating(0);
            friend.setOnline(account.getIsOnline());
            friend.setIsDeleted(false);
            friendTo.setPreviousStatusCode(account.getStatusCode() != null ? account.getStatusCode().toString() : null);
            friendTo.setStatusCode(StatusCode.SUBSCRIBED.toString());
            friendTo.setIdFrom(id);
            friendTo.setIdTo(SecurityUtil.getJwtUserFromSecurityContext().getId());
            friendTo.setRating(0);
            friendTo.setOnline(account.getIsOnline());
            friendTo.setIsDeleted(false);

        }
        friendRepository.save(friend);
        friendRepository.save(friendTo);
        return friendMapper.convertToFriendShortDto(friend);
    }

    /**
     * Метод ищет записи дружбы пользователя и собеседника, меняет им статусы, что дружбы нет, удаляет друзей, конвертирует
     * пользователя в ответ.
     * @param id идентификатор друга.
     * @return информация о пользователе.
     */
    public FriendShortDto deleteById(Long id) {
        Friend friend = friendRepository.findFriendByIdFromAndIdToAndIsDeletedFalse(SecurityUtil.getJwtUserFromSecurityContext().getId(), id);

        Friend friend2 = friendRepository.findFriendByIdFromAndIdToAndIsDeletedFalse(id, SecurityUtil.getJwtUserFromSecurityContext().getId());
        friend.setStatusCode(StatusCode.NONE.toString());

        friend2.setStatusCode(StatusCode.NONE.toString());
        FriendShortDto shortDto = friendMapper.convertToFriendShortDto(friend);
        friendRepository.delete(friend);
        friendRepository.delete(friend2);
        return shortDto;

    }

    private static boolean isItself(FriendSearchDto friendSearchDto) {
        if (friendSearchDto.getIdTo() != SecurityUtil.getJwtUserFromSecurityContext().getId() && friendSearchDto.getIdFrom() != SecurityUtil.getJwtUserFromSecurityContext().getId()) {
            return false;
        }
        return true;
    }

    /**
     * Метод вызывает другой метод, если ответ отрицательный, ищет записи о дружбе по спецификации, конвертирует результат в ответ.
     * Иначе вызывает другой метод, ищет записи о дружбе по спецификации, конвертирует результат в ответ.
     * @param friendSearchDto параметры поиска друзей.
     * @return информация о друзьях.
     */
    public List<FriendShortDto> recommended(FriendSearchDto friendSearchDto) {

        log.info("FriendService method recommended(FriendSearchDto friendSearchDto) executed");
        if (hasConnections() > 0) {
            System.out.println("Has connections");
            System.out.println(hasConnections());
            // createRandomConnections(3-hasConnections()<0?0:3-hasConnections());
            return friendRepository.findAll(getRecommendationsSpecification(friendSearchDto)).stream()
                    .map(friendMapper::convertToFriendShortDto).collect(Collectors.toList());
        }
        System.out.println("No connections");

        createRandomConnections(3);
        // createConnections(listId);
        return friendRepository.findAll(getRecommendationsSpecification(friendSearchDto)).stream()
                .map(friendMapper::convertToFriendShortDto).collect(Collectors.toList());
    }

    private Specification<Friend> getRecommendationsSpecification(FriendSearchDto friendSearchDto) {
        return getBaseSpecification(friendSearchDto)
                .and(equal(Friend_.isDeleted, false, true))
                .and(in(Friend_.id, friendRepository.getListOfRecommendedUserId(SecurityUtil.getJwtUserFromSecurityContext().getId()), true));
    }

    private Specification<Friend> getDefaultRecommendationsSpecification(FriendSearchDto friendSearchDto) {
        return getBaseSpecification(friendSearchDto)
                .and(equal(Friend_.isDeleted, false, true))
                .and(equal(Friend_.idFrom, friendSearchDto.getIdFrom(), true))
                .and(equal(Friend_.statusCode, friendSearchDto.getStatusCode(), true))
                //.and(equal(Friend_.idTo, friendSearchDto.getIdTo(), true))
                .and(equal(Friend_.previousStatusCode, friendSearchDto.getPreviousStatusCode(), true))
                .and(equal(Friend_.idFrom, SecurityUtil.getJwtUserFromSecurityContext().getId(), true))
                .and(notIn(Friend_.idTo, Collections.singleton(SecurityUtil.getJwtUserFromSecurityContext().getId()), true))
                ;


    }

    private void createRecommendationConnections() {

    }

    private void updateStatus(Friend friend, StatusCode statusCode) {
        friend.setPreviousStatusCode(friend.getStatusCode());
        friend.setStatusCode(statusCode.toString());

    }

    private Integer hasConnections() {
        return friendRepository.getCountRelations(SecurityUtil.getJwtUserFromSecurityContext().getId());
    }

    private boolean shouldCreateNewEntries(Long friendId) {
        Long myId = SecurityUtil.getJwtUserFromSecurityContext().getId();
        if (friendRepository.findFriendByIdFromAndIdToAndIsDeletedFalse(myId, friendId) != null
                && friendRepository.findFriendByIdFromAndIdToAndIsDeletedFalse(friendId, myId) != null) {
            return false;

        }
        return true;
    }

    private void createRandomConnections(int number) {
        List<Long> listId = accountRepository.getRandomIds(SecurityUtil.getJwtUserFromSecurityContext().getId(), number);

        if (!listId.isEmpty()) {
            for (int i = 0; i < listId.size() && i <= number; i++) {
                Account account = accountRepository.findById(listId.get(i))
                        .orElseThrow(() -> new BadCredentialsException("User with id " + listId.get(0) + " does not exist"));
                Friend friend = friendMapper.convertToFriend(account);
                friend.setRating(1);
                friend.setStatusCode(StatusCode.RECOMMENDATION.toString());
                friend.setIdFrom(SecurityUtil.getJwtUserFromSecurityContext().getId());
                friend.setIdTo(account.getId());
                friendRepository.save(friend);
            }
        }
//        Account account1 = accountRepository.findById(listId.get(0))
//                .orElseThrow(() -> new BadCredentialsException("User with id " + listId.get(0) + " does not exist"));
//        Account account2 = accountRepository.findById(listId.get(1))
//                .orElseThrow(() -> new BadCredentialsException("User with id " + listId.get(1) + " does not exist"));
//        Account account3 = accountRepository.findById(listId.get(2))
//                .orElseThrow(() -> new BadCredentialsException("User with id " + listId.get(2) + " does not exist"));
//        Friend friend1 = friendMapper.convertToFriend(account1);
//
//        friend1.setStatusCode(StatusCode.RECOMMENDATION.toString());
//        friend1.setIdFrom(SecurityUtil.getJwtUserFromSecurityContext().getId());
//        friend1.setIdTo(account1.getId());
//
//
//        Friend friend2 = friendMapper.convertToFriend(account2);
//
//        friend2.setStatusCode(StatusCode.RECOMMENDATION.toString());
//        friend2.setIdFrom(SecurityUtil.getJwtUserFromSecurityContext().getId());
//        friend2.setIdTo(account2.getId());
//
//
//        Friend friend3 = friendMapper.convertToFriend(account3);
//
//        friend3.setStatusCode(StatusCode.RECOMMENDATION.toString());
//        friend3.setIdFrom(SecurityUtil.getJwtUserFromSecurityContext().getId());
//        friend3.setIdTo(account3.getId());
//
//
//        friendRepository.save(friend1);
//        friendRepository.save(friend2);
//        friendRepository.save(friend3);
    }


}
