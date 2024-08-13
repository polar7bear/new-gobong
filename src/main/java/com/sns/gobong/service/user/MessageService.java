package com.sns.gobong.service.user;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.MessageType;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
public class MessageService {

    private DefaultMessageService defaultMessageService;

    @Value("${message.api-key}")
    private String apiKey;

    @Value("${message.api-secret-key}")
    private String apiSecretKey;

    @Value("${message.sender}")
    private String sender;

    @PostConstruct
    public void init() {
        this.defaultMessageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
    }

    public int sendMessage(String to) {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;

        Message message = new Message();
        message.setTo(to);
        message.setFrom(sender);
        message.setType(MessageType.SMS);
        message.setText("[gobong 회원가입]" + "\n" + "인증번호는 [" + code + "]" + "입니다.");
        this.defaultMessageService.sendOne(new SingleMessageSendingRequest(message));

        return code;
    }
    // TODO: 입력받은 인증코드 저장 및 검증 처리
    // TODO: 인증된 코드 만료처리, 유효기간 및 중복 요청 방지
}
