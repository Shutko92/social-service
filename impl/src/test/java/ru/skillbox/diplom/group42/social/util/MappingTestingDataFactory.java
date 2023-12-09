package ru.skillbox.diplom.group42.social.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountDto;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountOnlineDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.AccountCountPerAgeDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.AccountStatisticResponseDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.StatisticPerDateDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.StatisticResponseDto;
import ru.skillbox.diplom.group42.social.service.dto.notification.*;
import ru.skillbox.diplom.group42.social.service.dto.post.PostDto;
import ru.skillbox.diplom.group42.social.service.dto.post.Type;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentDto;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentType;
import ru.skillbox.diplom.group42.social.service.dto.post.like.LikeDto;
import ru.skillbox.diplom.group42.social.service.dto.post.like.ReactionType;
import ru.skillbox.diplom.group42.social.service.dto.post.like.TypeLike;
import ru.skillbox.diplom.group42.social.service.dto.tag.TagDto;
import ru.skillbox.diplom.group42.social.service.entity.account.Account;
import ru.skillbox.diplom.group42.social.service.entity.auth.User;
import ru.skillbox.diplom.group42.social.service.entity.friend.Friend;
import ru.skillbox.diplom.group42.social.service.entity.geo.City;
import ru.skillbox.diplom.group42.social.service.entity.geo.Country;
import ru.skillbox.diplom.group42.social.service.entity.notification.Notification;
import ru.skillbox.diplom.group42.social.service.entity.notification.NotificationSettings;
import ru.skillbox.diplom.group42.social.service.entity.post.Post;
import ru.skillbox.diplom.group42.social.service.entity.post.comment.Comment;
import ru.skillbox.diplom.group42.social.service.entity.post.like.Like;
import ru.skillbox.diplom.group42.social.service.entity.tag.Tag;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createAccountCountPerAgeDto;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createStatisticPerDateDto;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.*;

public class MappingTestingDataFactory {

    public static User createUser(String name) {
        return new User(
                "testFirstName" + name + "User",
                "testLastName" + name + "User",
                name + "User@mail.com",
                new BCryptPasswordEncoder(12).encode("Pass"),
                null
        );
    }
    public static User createUser(String email, String password) {
        return new User(
                "testFirstName" + email,
                "testLastName" + email,
                email,
                new BCryptPasswordEncoder(12).encode(password),
                null
        );
    }
    public static Account accountCloneFromUser(User user) {
        Account account = new Account();
        account.setFirstName(user.getFirstName());
        account.setLastName(user.getLastName());
        account.setEmail(user.getEmail());
        account.setPassword(user.getPassword());
        return account;
    }
    public static Account createTestAccount(Long id) {
        Account ac = new Account();
        ac.setId(id);
        ac.setPhone("+7000000000000");
        ac.setPhoto("photo");
        ac.setProfileCover("profileCover");
        ac.setAbout("about");
        ac.setCity("City");
        ac.setCountry("Country");
        ac.setStatusCode(null);
        ac.setPassword("Pass");
        ac.setEmail("test@email.ru");
        ac.setRegDate(ZonedDateTime.now());
        ac.setBirthDate(ZonedDateTime.now());
        ac.setMessagePermission("messagePermission");
        ac.setLastOnlineTime(ZonedDateTime.now());
        ac.setIsOnline(true);
        ac.setIsBlocked(false);
        ac.setEmojiStatus("Emoji");
        ac.setCreatedOn(ZonedDateTime.now());
        ac.setUpdatedOn(ZonedDateTime.now());
        ac.setDeletionTimestamp(ZonedDateTime.now());
        return ac;
    }
    public static AccountDto createTestAccountDto(Long id) {
        AccountDto ac = new AccountDto();
        ac.setId(id);
        ac.setPhone("+7000000000000");
        ac.setPhone("photo");
        ac.setProfileCover("profileCover");
        ac.setAbout("about");
        ac.setCity("City");
        ac.setCountry("Country");
        ac.setStatusCode(ru.skillbox.diplom.group42.social.service.dto.account.StatusCode.NONE);
        ac.setRegDate(ZonedDateTime.now());
        ac.setBirthDate(ZonedDateTime.now());
        ac.setMessagePermission("messagePermission");
        ac.setLastOnlineTime(ZonedDateTime.now());
        ac.setIsOnline(true);
        ac.setIsBlocked(false);
        ac.setEmojiStatus("Emoji");
        ac.setCreatedOn(ZonedDateTime.now());
        ac.setUpdatedOn(ZonedDateTime.now());
        ac.setDeletionTimestamp(ZonedDateTime.now());
        return ac;
    }
    public static AccountOnlineDto createAccountOnlineDto(Long id) {
        return new AccountOnlineDto(
                id,
                ZonedDateTime.of(
                        2020, 12, 3, 12, 20, 59,
                        90000, ZoneId.systemDefault()
                ),
                true
        );
    }
    public static List<City> createCityList() {
        List<City> cityList = new ArrayList<>();
        cityList.add(new City("TEST_CITY_FIRST", new Random().nextLong()));
        cityList.add(new City("TEST_CITY_SECOND", new Random().nextLong()));
        return cityList;
    }
    public static List<Country> createCountryList() {
        List<Country> countryList = new ArrayList<>();
        Country countryFirst = new Country();
        countryFirst.setTitle("TEST_COUNTRY_FIRST");
        countryFirst.setCities(createCityList());
        Country countrySecond = new Country();
        countrySecond.setTitle("TEST_COUNTRY_SECOND");
        countrySecond.setCities(createCityList());
        countryList.add(countryFirst);
        countryList.add(countrySecond);
        return countryList;
    }
    public static Comment createComment(Long authorId){
        Comment comment = new Comment();
        comment.setCommentType(CommentType.COMMENT);
        comment.setTime(TIME_TEST);
        comment.setTimeChanged(TIME_TEST);
        comment.setAuthorId(authorId);
        comment.setParentId(0L);
        comment.setCommentText("test text");
        comment.setPostId(11111L);
        comment.setIsBlocked(false);
        comment.setLikeAmount(10);
        comment.setMyLike(true);
        comment.setCommentCount(401);
        comment.setImagePath("test image path");
        return comment;
    }
    public static CommentDto createCommentDto(Long authorId){
        CommentDto comment = new CommentDto();
        comment.setId(TEST_ID);
        comment.setCommentType(CommentType.COMMENT);
        comment.setTime(TIME_TEST);
        comment.setTimeChanged(TIME_TEST);
        comment.setAuthorId(authorId);
        comment.setParentId(0L);
        comment.setCommentText("test text");
        comment.setPostId(11111L);
        comment.setLikeAmount(10L);
        comment.setIsBlocked(false);
        comment.setMyLike(true);
        comment.setCommentCount(401);
        comment.setImagePath("test image path");
        comment.setIsDeleted(false);
        return comment;
    }
    public static Tag createTeg(int name){
        Tag tag = new Tag();
        tag.setName("Test TAG " + name);
        return tag;
    }
    public static TagDto createTegDto(int name){
        TagDto tag = new TagDto();
        tag.setName("Test TAG " + name);
        return tag;
    }
    public static List<Tag> createListTag(){
        List<Tag> list = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            list.add(createTeg(i));
        }
        return list;
    }
    public static List<TagDto> createListTagDto(){
        List<TagDto> list = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            list.add(createTegDto(i));
        }
        return list;
    }
    public static Like createLike(Long authorId){
        Like like = new Like();
        like.setId(500L);
        like.setIsDeleted(false);
        like.setAuthorId(authorId);
        like.setTime(TIME_TEST);
        like.setItemId(2000L);
        like.setType(TypeLike.COMMENT);
        like.setReactionType(ReactionType.FUNNY);
        return like;
    }
    public static LikeDto createLikeDto(Long authorId){
        LikeDto like = new LikeDto();
        like.setId(500L);
        like.setIsDeleted(false);
        like.setAuthorId(authorId);
        like.setTime(TIME_TEST);
        like.setItemId(2000L);
        like.setType(TypeLike.COMMENT);
        like.setReactionType("funny");
        return like;
    }
    public static Post createPost(Long authorID){
        Post post = new Post();
        post.setTime(TIME_TEST);
        post.setTimeChanged(TIME_TEST);
        post.setAuthorId(authorID);
        post.setTitle("test Title");
        post.setType(Type.POSTED);
        post.setPostText("test Text");
        post.setCommentsCount(102);
        post.setLikeAmount(105);
        post.setMyLike(true);
        post.setImagePath("test image path");
        post.setPublishDate(TIME_TEST);
        return post;
    }
    public static PostDto createPostDto(Long authorID){
        PostDto post = new PostDto();
        post.setId(TEST_ID);
        post.setIsDeleted(false);
        post.setTime(TIME_TEST);
        post.setTimeChanged(TIME_TEST);
        post.setAuthorId(authorID);
        post.setTitle("test Title");
        post.setType(Type.POSTED);
        post.setPostText("test Text");
        post.setCommentsCount(102);
        post.setMyReaction(null);
        post.setLikeAmount(105);
        post.setMyLike(true);
        post.setImagePath("test image path");
        post.setPublishDate(TIME_TEST);
        return post;
    }

    public static StatisticResponseDto createStatisticResponseDto() {
        StatisticResponseDto response = new StatisticResponseDto();
        List<StatisticPerDateDto> perDate = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            perDate.add(createStatisticPerDateDto());
        }
        response.setCount(TEST_SIZE);
        response.setDate(TIME_TEST);
        response.setCountPerHours(perDate);
        response.setCountPerMonth(perDate);
        return  response;
    }

    public static AccountStatisticResponseDto createAccountStatisticResponseDto() {
        AccountStatisticResponseDto accountResponse = new AccountStatisticResponseDto();
        List<StatisticPerDateDto> perDate = new ArrayList<>();
        List<AccountCountPerAgeDto> accountPerAge = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            perDate.add(createStatisticPerDateDto());
        }
        for (int i = 0; i < 4; i++) {
            accountPerAge.add(createAccountCountPerAgeDto());
        }
        accountResponse.setCount(TEST_SIZE);
        accountResponse.setDate(TIME_TEST);
        accountResponse.setCountPerMonth(perDate);
        accountResponse.setCountPerAge(accountPerAge);
        return accountResponse;
    }

    public static Friend createTestFriend() {
        Friend friend = new Friend();
        friend.setId(TEST_ACCOUNT_ID);
        friend.setPhoto("some photo");
        friend.setCity("some city");
        friend.setCountry("some country");
        friend.setFirstName(TEST_FIRST_NAME);
        friend.setLastName(TEST_LAST_NAME);
        friend.setIsOnline(false);
        friend.setBirthDate(TIME_TEST);
        friend.setIdFrom(TEST_SECOND_ID);
        friend.setIdTo(TEST_ID);
        friend.setPreviousStatusCode(null);
        friend.setStatusCode(null);
        friend.setRating(TEST_SIZE);
        friend.setIsDeleted(false);
        return friend;
    }

    public static Notification createTestNotification() {
        Notification notification = new Notification();
        notification.setAuthorId(TEST_ACCOUNT_ID);
        notification.setNotificationType(NotificationType.POST);
        notification.setId(TEST_ID);
        notification.setContent("Test Text");
        notification.setRecipientId(TEST_SECOND_ID);
        notification.setSentTime(TIME_TEST);
        notification.setIsDeleted(false);
        return notification;
    }

    public static NotificationDto createNotificationDto() {
        NotificationDto notification = new NotificationDto();
        notification.setAuthorId(TEST_ACCOUNT_ID);
        notification.setNotificationType(NotificationType.POST);
        notification.setId(TEST_ID);
        notification.setContent("Test Text");
        notification.setSentTime(TIME_TEST);
        return notification;
    }

    public static EventNotificationDto createEventNotificationDto() {
        EventNotificationDto dto = new EventNotificationDto();
        dto.setNotificationType(NotificationType.POST);
        dto.setContent("Test Text");
        dto.setAuthorId(TEST_ID);
        dto.setReceiverId(TEST_SECOND_ID);
        return dto;
    }

    public static NotificationSettingDto createNotificationSettingDto() {
        NotificationSettingDto dto = new NotificationSettingDto();
        dto.setId(TEST_ID);
        dto.setEnablePost(true);
        dto.setEnableMessage(true);
        dto.setEnableCommentComment(true);
        dto.setEnableFriendRequest(true);
        dto.setEnableFriendBirthday(true);
        dto.setEnablePostComment(true);
        dto.setEnableSendEmailMessage(true);
        return dto;
    }

    public static NotificationSettings createTestNotificationSettings() {
        NotificationSettings dto = new NotificationSettings();
        dto.setId(TEST_ID);
        dto.setEnablePost(true);
        dto.setEnableMessage(true);
        dto.setEnableCommentComment(true);
        dto.setEnableFriendRequest(true);
        dto.setEnableFriendBirthday(true);
        dto.setEnablePostComment(true);
        dto.setEnableSendEmailMessage(true);
        return dto;
    }

    public static NotificationSettingsUpdateDto createNotificationSettingsUpdateDto() {
        NotificationSettingsUpdateDto dto = new NotificationSettingsUpdateDto();
        dto.setNotificationType(NotificationType.POST);
        dto.setEnable(true);
        return dto;
    }
}


