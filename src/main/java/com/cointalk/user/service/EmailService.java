package com.cointalk.user.service;

import com.cointalk.user.dto.MailDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
@AllArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendEmail(String toAddress, String subject, String body) {
        System.out.println("보낼 주소 : " + toAddress);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(body);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            log.error(toAddress + "에게 이메일 전송 실패");
            return false;
        }
        log.info(toAddress + "에게 이메일 전송 성공");
        return true;
    }


}



