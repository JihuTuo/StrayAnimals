package com.shiep.controller;

import com.shiep.service.IMailService;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@RequestMapping("email")
public class MailController {
    @Resource
    private IMailService mailService;

    @GetMapping("generateCode")
    private ResponseEntity<String> saveAndGenerateCode(@RequestParam("email") String mail) {
        if (!this.mailService.sendMailAndGenerateCode(mail)) {
            return null;
        }
        String code = this.mailService.generateCodeFromRedis(mail);
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return ResponseEntity.ok(code);
    }
}
