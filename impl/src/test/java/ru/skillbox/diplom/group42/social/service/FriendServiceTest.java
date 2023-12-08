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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createTestFriend;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createFriendSearchDto;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createJwtUser;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_ACCOUNT_ID;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_ID;

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
    void friendApproveInvokesFriendRepositorySave() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            utilities.when(SecurityUtil::getJwtUserFromSecurityContext).thenReturn(user);
            when(friendRepository.findByIdFromAndIdToAndStatusCode(anyLong(),anyLong(), anyString())).thenReturn(testFriend);
            service.friendApprove(TEST_ID);
            verify(friendRepository, times(2)).save(testFriend);
        }
    }
}