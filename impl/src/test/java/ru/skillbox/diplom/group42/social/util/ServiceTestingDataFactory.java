package ru.skillbox.diplom.group42.social.util;

import ru.skillbox.diplom.group42.social.service.dto.account.AccountSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.account.StatusCode;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.request.StatisticRequestDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.AccountCountPerAgeDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.StatisticPerDateDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group42.social.service.dto.friend.FriendSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.friend.FriendShortDto;
import ru.skillbox.diplom.group42.social.service.dto.message.MessageDto;
import ru.skillbox.diplom.group42.social.service.dto.message.ReadStatus;
import ru.skillbox.diplom.group42.social.service.dto.post.PostSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentType;
import ru.skillbox.diplom.group42.social.service.dto.tag.TagDto;
import ru.skillbox.diplom.group42.social.service.dto.tag.TagSearchDto;
import ru.skillbox.diplom.group42.social.service.entity.auth.User;
import ru.skillbox.diplom.group42.social.service.entity.dialog.Dialog;
import ru.skillbox.diplom.group42.social.service.entity.message.Message;
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

    public static TagSearchDto createTagSearchDto() {
        TagSearchDto tagSearchDto = new TagSearchDto();
        tagSearchDto.setId(TEST_ID);
        tagSearchDto.setName("test_tag");
        tagSearchDto.setIsDeleted(false);
        return  tagSearchDto;
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
        post.setAccountIds(null);
        post.setBlockedIds(null);
        post.setAuthor(null);
        post.setWithFriends(true);
        post.setReaction(new ArrayList<>());
        post.setDateFrom(TIME_TEST.toString());
        post.setDateTo(TIME_TEST.toString());
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
    public static StatisticRequestDto createStatisticRequestDto() {
        StatisticRequestDto request = new StatisticRequestDto();
        request.setDate(String.valueOf(TIME_TEST));
        request.setFirstMonth(String.valueOf(FIRST_MONTH));
        request.setLastMonth(String.valueOf(LAST_MONTH));
        return request;
    }

    public static StatisticPerDateDto createStatisticPerDateDto() {
        StatisticPerDateDto perDate = new StatisticPerDateDto();
        perDate.setCount(TEST_SIZE);
        perDate.setDate(TIME_TEST);
        return perDate;
    }

    public static AccountCountPerAgeDto createAccountCountPerAgeDto() {
        AccountCountPerAgeDto accountPerAge = new AccountCountPerAgeDto();
        accountPerAge.setAge(TEST_AGE);
        accountPerAge.setCount(TEST_SIZE);
        return accountPerAge;
    }

    public static Dialog createTestDialog() {
        Dialog dialog = new Dialog();
        dialog.setId(TEST_ACCOUNT_ID);
        dialog.setLastMessage(List.of());
        dialog.setUnreadCount(TEST_SIZE);
        dialog.setConversationPartner1(TEST_ID);
        dialog.setConversationPartner2(TEST_SECOND_ID);
        dialog.setIsDeleted(false);
        return  dialog;
    }

    public static Message createTestMessage(Dialog dialog) {
        Message message = new Message();
        message.setTime(TIME_TEST);
        message.setMessageText("test text");
        message.setConversationPartner1(TEST_ID);
        message.setConversationPartner2(TEST_SECOND_ID);
        message.setDialogId(dialog);
        message.setReadStatus(ReadStatus.SENT);
        message.setIsDeleted(false);
        return  message;
    }

    public static MessageDto createMessageDto(Long dialogId) {
        MessageDto message = new MessageDto();
        message.setId(TEST_ID);
        message.setTime(TIME_TEST);
        message.setMessageText("test text");
        message.setConversationPartner1(TEST_ID);
        message.setConversationPartner2(TEST_SECOND_ID);
        message.setDialogId(dialogId);
        message.setReadStatus(ReadStatus.SENT);
        message.setIsDeleted(false);
        return  message;
    }

    public static FriendSearchDto createFriendSearchDto() {
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setIdFrom(TEST_ID);
        friendSearchDto.setIdTo(TEST_SECOND_ID);
        friendSearchDto.setStatusCode(null);
        friendSearchDto.setPreviousStatusCode(null);
        friendSearchDto.setIsDeleted(false);
        return friendSearchDto;
    }

    public static FriendShortDto createFriendShortDto() {
        FriendShortDto friendShortDto = new FriendShortDto();
        friendShortDto.setId(TEST_ACCOUNT_ID);
        friendShortDto.setFriendId(TEST_SECOND_ID);
        friendShortDto.setRating(0);
        friendShortDto.setIsDeleted(false);
        friendShortDto.setStatusCode(null);
        friendShortDto.setPreviousStatusCode(null);
        friendShortDto.setIsDeleted(false);
        return friendShortDto;
    }
}