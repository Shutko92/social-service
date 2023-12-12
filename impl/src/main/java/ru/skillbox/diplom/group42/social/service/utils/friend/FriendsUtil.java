package ru.skillbox.diplom.group42.social.service.utils.friend;

import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.friend.FriendSearchDto;
import ru.skillbox.diplom.group42.social.service.entity.account.StatusCode;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity_;
import ru.skillbox.diplom.group42.social.service.entity.friend.Friend;
import ru.skillbox.diplom.group42.social.service.entity.friend.Friend_;
import ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static ru.skillbox.diplom.group42.social.service.utils.SpecificationUtil.*;
public class FriendsUtil {
        public static final int MAX_CONNECTION = 3;
        public static List<String> getListOnlyRequestStatuses(){
            List<String> list = new ArrayList<>();
            list.add(StatusCode.REQUEST_TO.toString());
            list.add(StatusCode.REQUEST_FROM.toString());
            return list;
        }
        public static List<String> getListOnlySubscriberStatuses(){
            List<String> list = new ArrayList<>();
            list.add(StatusCode.SUBSCRIBED.toString());
            list.add(StatusCode.WATCHING.toString());
            return list;
        }

        public static Specification<Friend> getSpecificationAboutFriendRecords(Long userId, Long friendId, StatusCode statusCode) {
            List<String> statuses = new ArrayList<>();
            if(statusCode.equals(StatusCode.REQUEST_TO) || statusCode.equals(StatusCode.REQUEST_FROM)){
                statuses = getListOnlyRequestStatuses();
            } else if (statusCode.equals(StatusCode.WATCHING) || statusCode.equals(StatusCode.SUBSCRIBED)){
                statuses = getListOnlySubscriberStatuses();
            }
            FriendRequestFilter request = new FriendRequestFilter(
                    userId,
                    friendId,
                    statusCode.toString()
            );
            return getBaseSpecification(request)
                    .and(equal(Friend_.isDeleted, false, true))
                    .and(equal(Friend_.idFrom, request.getIdOne(), true))
                    .and(equal(Friend_.idTo, request.getIdTwo(), true))
                    .and(in(Friend_.statusCode, statuses, true));
        }
        public static Specification<Friend> getSpecificationFriendCount(Long id){
            return getBaseFriendSpecification(new BaseSearchDto(id, false))
                    .and(equal(Friend_.idTo, id, true))
                    .and(equal(Friend_.statusCode,StatusCode.REQUEST_TO.toString(), true));
        }

        public static Specification<Friend> getSpecificationOfRecommendedUserId(Long id){
            return getBaseFriendSpecification(new BaseSearchDto(id, false))
                    .and(equal(Friend_.idFrom, id, true))
                    .and(equal(Friend_.statusCode,StatusCode.RECOMMENDATION.toString(), true));
        }
        public static Specification getBaseFriendSpecification(BaseSearchDto searchDto) {
            return equal(BaseEntity_.isDeleted, searchDto.getIsDeleted(), true);
        }
        public static Specification<Friend> getSpecificationByRecommendation(
                FriendSearchDto friendSearchDto,
                Set<Long> recommendation
        ){
            return getBaseSpecification(friendSearchDto)
                    .and(equal(Friend_.isDeleted, false, true))
                    .and(in(Friend_.id, recommendation, true));
        }
        public static Specification<Friend> getFriendSpecification(FriendSearchDto friendSearchDto) {
            return getBaseSpecification(friendSearchDto)
                    .and(equal(Friend_.isDeleted, false, true))
                    .and(equal(Friend_.idFrom, friendSearchDto.getIdFrom(), true))
                    .and(equal(Friend_.statusCode, friendSearchDto.getStatusCode(), true))
                    .and(equal(Friend_.idTo, friendSearchDto.getIdTo(), true))
                    .and(equal(Friend_.previousStatusCode, friendSearchDto.getPreviousStatusCode(), true))
                    .and(equal(Friend_.idFrom, SecurityUtil.getJwtUserFromSecurityContext().getId(), true))
                    .and(notIn(Friend_.idTo, Collections.singleton(SecurityUtil.getJwtUserFromSecurityContext().getId()), true));
        }
    }

