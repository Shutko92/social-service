package ru.skillbox.diplom.group42.social.service.controller.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group42.social.service.dto.friend.CountDto;
import ru.skillbox.diplom.group42.social.service.dto.friend.FriendSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.friend.FriendShortDto;
import ru.skillbox.diplom.group42.social.service.service.friend.FriendService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FriendControllerImpl implements FriendController{


    private final FriendService friendService;
    @Override
    public ResponseEntity<FriendShortDto> getById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Page<FriendShortDto>> getAll(
            FriendSearchDto friendSearchDto
            ,Pageable pageable) {
        return ResponseEntity.ok(friendService.getFriends(friendSearchDto,pageable));
    }
    @Override
    public ResponseEntity<FriendShortDto> create(FriendShortDto dto) {
        return null;
    }

    @Override
    public ResponseEntity<FriendShortDto> update(FriendShortDto dto) {
        return null;
    }

    @Override
    public ResponseEntity deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(friendService.deleteById(id));
    }

    @Override
    public ResponseEntity<FriendShortDto> friendRequest(Long id) {
        return ResponseEntity.ok(friendService.friendRequest(id));
    }
    @Override
    public ResponseEntity<FriendShortDto> friendApprove(Long id) {
        return ResponseEntity.ok(friendService.friendApprove(id));
    }

    @Override
    public ResponseEntity<CountDto> countFriendRequests() {
        return ResponseEntity.ok(friendService.getCountRequests());
    }

    @Override
    public ResponseEntity<FriendShortDto> subscribe(Long id) {
        return ResponseEntity.ok(friendService.subscribeById(id));
    }

    @Override
    public ResponseEntity<List<FriendShortDto>> recommendations(FriendSearchDto friendSearchDto) {
        return ResponseEntity.ok(friendService.recommended(friendSearchDto));
    }

    @Override
    public ResponseEntity<FriendShortDto> friendBlock(@PathVariable Long id){
        return ResponseEntity.ok(friendService.block(id));
    }
    @Override
    public ResponseEntity<FriendShortDto> friendUnBlock(@PathVariable Long id){
        return ResponseEntity.ok(friendService.unBlock(id));
    }
}

