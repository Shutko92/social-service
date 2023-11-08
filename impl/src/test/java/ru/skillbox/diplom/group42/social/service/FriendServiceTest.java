package ru.skillbox.diplom.group42.social.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.diplom.group42.social.service.dto.friend.FriendSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.friend.FriendShortDto;
import ru.skillbox.diplom.group42.social.service.entity.account.Account;
import ru.skillbox.diplom.group42.social.service.entity.friend.Friend;
import ru.skillbox.diplom.group42.social.service.mapper.friend.FriendMapper;
import ru.skillbox.diplom.group42.social.service.repository.account.AccountRepository;
import ru.skillbox.diplom.group42.social.service.repository.friend.FriendRepository;
import ru.skillbox.diplom.group42.social.service.security.JwtUser;
import ru.skillbox.diplom.group42.social.service.service.friend.FriendService;
import ru.skillbox.diplom.group42.social.service.service.notification.NotificationHandler;
import ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil;
import ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createTestFriend;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.*;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.*;

@ExtendWith(MockitoExtension.class)
class FriendServiceTest {
    private FriendService service;
    @Mock
    private FriendRepository friendRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private FriendMapper friendMapper;
    @Mock
    private NotificationHandler notificationHandler;
    private JwtUser user = createJwtUser();
    private Account account = MappingTestingDataFactory.createTestAccount(TEST_ACCOUNT_ID);
    private Friend testFriend = createTestFriend();
    @BeforeEach
    public void beforeMethod(){
        service = new FriendService(friendRepository, accountRepository, friendMapper, notificationHandler);
    }

    @Test
    void getFriendsInvokesFriendRepositoryFindAll() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserFromSecurityContext).thenReturn(user);
            FriendSearchDto friendSearchDto = createFriendSearchDto();
            List<Friend> list = new ArrayList<>();
            Page<Friend> friendPage = new PageImpl<>(list);
            when(friendRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(friendPage);
            service.getFriends(friendSearchDto, Pageable.ofSize(2));
            verify(friendRepository, times(1)).findAll(any(Specification.class),any(Pageable.class));
        }
    }

    @Test
    void friendRequestInvokesFriendRepositorySave() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            utilities.when(SecurityUtil::getJwtUserFromSecurityContext).thenReturn(user);
            when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
            when(friendRepository.findFriendByIdFromAndIdToAndIsDeletedFalse(anyLong(),anyLong())).thenReturn(testFriend);
            when(friendMapper.convertToFriendShortDto(testFriend)).thenReturn(new FriendShortDto());
            service.friendRequest(TEST_ID);
            verify(friendRepository, times(4)).findFriendByIdFromAndIdToAndIsDeletedFalse(anyLong(),anyLong());
            verify(friendRepository, times(2)).save(any(Friend.class));
        }
    }

    @Test
    void friendRequest2InvokesFriendRepositorySave() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            utilities.when(SecurityUtil::getJwtUserFromSecurityContext).thenReturn(user);
            when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
            when(friendRepository.findFriendByIdFromAndIdToAndIsDeletedFalse(anyLong(),anyLong())).thenReturn(null);
            when(friendMapper.convertToFriendShortDto(testFriend)).thenReturn(new FriendShortDto());
            when(friendMapper.convertToFriend(account)).thenReturn(testFriend);
            service.friendRequest(69L);
            verify(friendRepository, times(1)).findFriendByIdFromAndIdToAndIsDeletedFalse(anyLong(),anyLong());
            verify(friendRepository, times(2)).save(any(Friend.class));
        }
    }

    @Test
    void friendApproveInvokesFriendRepositorySave() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            utilities.when(SecurityUtil::getJwtUserFromSecurityContext).thenReturn(user);
            when(friendRepository.findByIdFromAndIdToAndStatusCode(anyLong(),anyLong(), anyString())).thenReturn(testFriend);
            service.friendApprove(TEST_ID);
            verify(friendRepository, times(2)).save(testFriend);
        }
    }

    @Test
    void getCountRequestsInvokesFriendRepositoryGetCountRequest() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            service.getCountRequests();
            verify(friendRepository, times(1)).getCountRequest(anyLong());
        }
    }

    @Test
    void subscribeByIdInvokesFriendRepositorySave() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            utilities.when(SecurityUtil::getJwtUserFromSecurityContext).thenReturn(user);
            when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
            when(friendRepository.findFriendByIdFromAndIdToAndIsDeletedFalse(anyLong(),anyLong())).thenReturn(testFriend);
            when(friendMapper.convertToFriendShortDto(testFriend)).thenReturn(new FriendShortDto());
            service.subscribeById(TEST_ID);
            verify(friendRepository, times(4)).findFriendByIdFromAndIdToAndIsDeletedFalse(anyLong(),anyLong());
            verify(friendRepository, times(2)).save(any(Friend.class));
        }
    }

    @Test
    void subscribeById2InvokesFriendRepositorySave() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            utilities.when(SecurityUtil::getJwtUserFromSecurityContext).thenReturn(user);
            when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
            when(friendRepository.findFriendByIdFromAndIdToAndIsDeletedFalse(anyLong(),anyLong())).thenReturn(null);
            when(friendMapper.convertToFriendShortDto(testFriend)).thenReturn(new FriendShortDto());
            when(friendMapper.convertToFriend(account)).thenReturn(testFriend);
            service.subscribeById(69L);
            verify(friendRepository, times(1)).findFriendByIdFromAndIdToAndIsDeletedFalse(anyLong(),anyLong());
            verify(friendRepository, times(2)).save(any(Friend.class));
        }
    }

    @Test
    void deleteByIdInvokesFriendRepositoryDelete() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            utilities.when(SecurityUtil::getJwtUserFromSecurityContext).thenReturn(user);
            when(friendRepository.findFriendByIdFromAndIdToAndIsDeletedFalse(anyLong(),anyLong())).thenReturn(testFriend);
            service.deleteById(TEST_ID);
            verify(friendRepository, times(2)).delete(testFriend);
        }
    }

    @Test
    void recommendedInvokesFriendRepositoryFindAll() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            utilities.when(SecurityUtil::getJwtUserFromSecurityContext).thenReturn(user);
            FriendSearchDto friendSearchDto = createFriendSearchDto();
            when(friendRepository.findAll(any(Specification.class))).thenReturn(List.of(testFriend));
            when(accountRepository.getRandomIds(anyLong(),anyInt())).thenReturn(List.of(TEST_SECOND_ID));
            when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
            when(friendMapper.convertToFriend(account)).thenReturn(testFriend);
            testFriend.setId(TEST_SECOND_ID);
            when(friendMapper.convertToFriendShortDto(testFriend)).thenReturn(createFriendShortDto());
            service.recommended(friendSearchDto);
            verify(friendRepository, times(1)).findAll(any(Specification.class));
            verify(friendMapper, times(1)).convertToFriendShortDto(any(Friend.class));
        }
    }
}