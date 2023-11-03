package ru.skillbox.diplom.group42.social.util;

import ru.skillbox.diplom.group42.social.service.dto.account.AccountSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.account.StatusCode;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group42.social.service.dto.post.PostSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentType;
import ru.skillbox.diplom.group42.social.service.dto.tag.TagDto;
import ru.skillbox.diplom.group42.social.service.entity.auth.User;
import ru.skillbox.diplom.group42.social.service.entity.tag.Tag;
import ru.skillbox.diplom.group42.social.service.security.JwtUser;

import java.util.*;

import static ru.skillbox.diplom.group42.social.util.TestingConstant.*;

public class ServiceTestingDataFactory {
    public static RegistrationDto createRegistrationDto(){
        return new RegistrationDto(
                "TEST EMAIL",
                "TEST PASSWORD",
                "TEST PASSWORD",
                "TEST FIRST NAME",
                "TEST LAST NAME",
                "captcha",
                "captcha"
        );
    }
    public static AuthenticateDto createAuthenticateDto(User user){
        return new AuthenticateDto(user.getEmail(), "Pass");
    }
    public static CommentSearchDto createCommentSearchDto(){
        CommentSearchDto searchDto = new CommentSearchDto();
        searchDto.setParentId(0L);
        searchDto.setId(TEST_ID);
        searchDto.setPostId(TEST_ID);
        searchDto.setCommentType(CommentType.COMMENT);
        searchDto.setAuthorId(TEST_ACCOUNT_ID);
        searchDto.setIsDeleted(false);
        return searchDto;
    }
    public static AccountSearchDto createAccountSearchDto(){
        AccountSearchDto searchDto = new AccountSearchDto();
        searchDto.setId(TEST_ID);
        searchDto.setIsDeleted(false);
        searchDto.setIds(new ArrayList<>());
        searchDto.setBlockedByIds(new ArrayList<>());
        searchDto.setAuthor("test author");
        searchDto.setFirstName(TEST_FIRST_NAME);
        searchDto.setLastName(TEST_LAST_NAME);
        searchDto.setCity("test City");
        searchDto.setCountry("test Country");
        searchDto.setIsBlocked(false);
        searchDto.setAgeFrom(10);
        searchDto.setAgeTo(50);
        searchDto.setStatusCode(StatusCode.RECOMMENDATION);
        return searchDto;
    }

    public  static JwtUser createJwtUser() {
        JwtUser user = new JwtUser(
                TEST_ID,
                "first name",
                "last name",
                "Pass",
                "email@email.com",
                Collections.emptyList()
        );
        return user;
    }
    public static PostSearchDto createPostSearchDto(){
        List<String> tagsList = new ArrayList<>();
        tagsList.add("tag");
        PostSearchDto post = new PostSearchDto();
        post.setId(TEST_ID);
        post.setIsDeleted(false);
        post.setIds(new ArrayList<>());
        post.setTags(tagsList);
        post.setAccountIds(new ArrayList<>());
        post.setBlockedIds(new ArrayList<>());
        post.setAuthor("test Author");
        post.setWithFriends(true);
        post.setReaction(new ArrayList<>());
        post.setDateFrom(TIME_TEST);
        post.setDateTo(TIME_TEST);
        post.setText("test Text");
        return post;
    }
    public static Set<TagDto> createSetTagDto(){
        TagDto tagDto = new TagDto();
        tagDto.setId(TEST_ID);
        tagDto.setIsDeleted(false);
        tagDto.setName("testTag");
        HashSet<TagDto> set = new HashSet<>();
        set.add(tagDto);
        return set;
    }
    public static List<Tag> createListTag(){
        List<Tag> list = new ArrayList<>();
        list.add(createTag());
        return list;
    }
    public static Tag createTag(){
        Tag tag = new Tag();
        tag.setId(TEST_ID);
        tag.setIsDeleted(false);
        tag.setName("testTag");
        return tag;
    }
    public static TagDto createTagDto(){
        TagDto tag = new TagDto();
        tag.setId(TEST_ID);
        tag.setIsDeleted(false);
        tag.setName("testTag");
        return tag;
    }
}