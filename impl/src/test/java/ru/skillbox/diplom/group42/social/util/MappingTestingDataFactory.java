package ru.skillbox.diplom.group42.social.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountDto;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountOnlineDto;
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
import ru.skillbox.diplom.group42.social.service.entity.geo.City;
import ru.skillbox.diplom.group42.social.service.entity.geo.Country;
import ru.skillbox.diplom.group42.social.service.entity.post.Post;
import ru.skillbox.diplom.group42.social.service.entity.post.comment.Comment;
import ru.skillbox.diplom.group42.social.service.entity.post.like.Like;
import ru.skillbox.diplom.group42.social.service.entity.tag.Tag;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_ID;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TIME_TEST;

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
        ac.setOnline(true);
        ac.setBlocked(false);
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
        ac.setOnline(true);
        ac.setBlocked(false);
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
}


