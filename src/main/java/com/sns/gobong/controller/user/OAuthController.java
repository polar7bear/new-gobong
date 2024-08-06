package com.sns.gobong.controller.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/login")
public class OAuthController {

    @GetMapping("/google")
    public String google() {
        return "";
    }

    @GetMapping("/naver")
    public String naver() {
        return "";
    }

    @GetMapping("/kakao")
    public String kakao() {
        return "";
    }


}
