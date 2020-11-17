package com.ithd.chat.chatapp.controller.booking;

import com.ithd.chat.chatapp.chatserver.model.MessageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class BookingController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat/{to}")
    public void getChatBooking(String to, String fromId, MessageModel message) {
        log.info("handling and message "  + " to " + to);
        simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);
    }
}
