package com.lunchteam.lunchrestapi.ws.controller;

import com.lunchteam.lunchrestapi.ws.dto.ChatMessage;
import com.lunchteam.lunchrestapi.ws.dto.ChatMessage.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        final String ENTER_MSG = "님이 입장하셨습니다.";
        log.debug("[MSG] "
            + message.getSender() + ": "
            + (message.getMessage() == null ? ENTER_MSG : message.getMessage()));
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + ENTER_MSG);
        }
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping("/chat/action")
    public void action(ChatMessage message) {
        log.debug("[ACTION]" + message.getSender() + ": " + message.getMessage());
        if (MessageType.ACTION.equals(message.getType())) {
            messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        }
    }
}
