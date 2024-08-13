package com.sns.gobong.controller.user;

import com.sns.gobong.domain.dto.request.user.UserSendMessageRequestDto;
import com.sns.gobong.service.user.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public String sendMail(@RequestBody UserSendMessageRequestDto dto) {
        return String.valueOf(messageService.sendMessage(dto.getReceiver()));
    }
}
