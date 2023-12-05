package ru.skillbox.diplom.group42.social.service.controller.friend;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group42.social.service.controller.base.BaseController;
import ru.skillbox.diplom.group42.social.service.dto.friend.CountDto;
import ru.skillbox.diplom.group42.social.service.dto.friend.FriendSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.friend.FriendShortDto;
import ru.skillbox.diplom.group42.social.service.util.ConstantURL;

import java.util.List;


@RequestMapping(ConstantURL.BASE_URL + "/friends")
public interface FriendController extends BaseController<FriendShortDto, FriendSearchDto> {

    @PostMapping("/{id}/request")
    ResponseEntity<FriendShortDto> friendRequest(@PathVariable Long id);

    @PutMapping("/{id}/approve")
    ResponseEntity<FriendShortDto> friendApprove(@PathVariable Long id);

    @GetMapping("/count")
    ResponseEntity<CountDto> countFriendRequests();


    @PostMapping("/subscribe/{id}")
    ResponseEntity<FriendShortDto> subscribe(@PathVariable Long id);

    @GetMapping("/recommendations")
    ResponseEntity<List<FriendShortDto>> recommendations(FriendSearchDto friendSearchDto);
    @PutMapping("/block/{id}")
    ResponseEntity<FriendShortDto> friendBlock(@PathVariable Long id);
    @PutMapping("/unblock/{id}")
    ResponseEntity<FriendShortDto> friendUnBlock(@PathVariable Long id);
}