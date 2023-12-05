package ru.skillbox.diplom.group42.social.service.repository.friend;

import org.springframework.data.jpa.repository.Query;
import ru.skillbox.diplom.group42.social.service.entity.friend.Friend;
import ru.skillbox.diplom.group42.social.service.repository.base.BaseRepository;

import java.util.List;
import java.util.Set;

public interface FriendRepository extends BaseRepository<Friend> {

    Friend findFriendByIdFromAndIdToAndIsDeletedFalse(Long idFrom, Long idTo);

    Friend findByIdFromAndIdToAndStatusCode(Long idFrom, Long idTo, String status);

    List<Friend> findByIdFromOrIdToAndStatusCode(Long idFrom, Long idTo, String status);
    List<Friend> findAllByIsDeletedAndStatusCodeAndAndBirthDateIsNotNull(Boolean isDeleted, String status);

    @Query(value = "SELECT COUNT(*) FROM friend WHERE id_to = ?1 AND status_code='REQUEST_TO' AND is_deleted=false", nativeQuery = true)
    Long getCountRequest(Long id);


//    @Query(value = "SELECT id FROM friend WHERE id_from IN( SELECT id_to FROM friend WHERE id_from=?1 ) AND id_to!=?1",nativeQuery = true)
//    Set<Long> getListOfRecommendedUserId(Long id);

    @Query(value = "SELECT id FROM friend WHERE id_from=?1 AND status_code='RECOMMENDATION'", nativeQuery = true)
    Set<Long> getListOfRecommendedUserId(Long id);

//    @Query(value = "SELECT COUNT(id)FROM friend WHERE id_to=?1 OR id_from=?1",nativeQuery = true)
//    Integer getCountRelations(Long id);


    @Query(value = "SELECT COUNT(id)FROM friend WHERE id_from=?1 AND status_code='RECOMMENDATION'AND is_deleted=false", nativeQuery = true)
    Integer getCountRelations(Long id);

}
