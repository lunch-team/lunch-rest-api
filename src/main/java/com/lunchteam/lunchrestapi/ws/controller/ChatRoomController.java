package com.lunchteam.lunchrestapi.ws.controller;

import com.lunchteam.lunchrestapi.ws.dto.ChatRoom;
import com.lunchteam.lunchrestapi.ws.repository.ChatRoomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatRoomRepository.findAllRoom();
    }

    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name) {
        log.debug("Create Room: " + name);
        return chatRoomRepository.createChatRoom(name);
    }

    @PostMapping("/room/remove")
    @ResponseBody
    public HttpStatus removeRoom(@RequestParam String roomId) {
        if (chatRoomRepository.findRoomById(roomId) == null) {
            return HttpStatus.NOT_FOUND;
        }
        log.debug("Remove Room: " + chatRoomRepository.findRoomById(roomId).getName());
        chatRoomRepository.removeChatRoom(roomId);
        return HttpStatus.OK;
    }

    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        log.debug("Get Room Info: " + chatRoomRepository.findRoomById(roomId).getName());
        return chatRoomRepository.findRoomById(roomId);
    }
}
