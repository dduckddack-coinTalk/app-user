package com.cointalk.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.net.URI;

@Slf4j
@Service
@AllArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendEmail(String toAddress, String subject, String body) {
        log.info("보낼 주소 : " + toAddress);

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            log.error(toAddress + "에게 이메일 전송 실패");
            return false;
        }
        log.info(toAddress + "에게 이메일 전송 성공");
        return true;
    }

    public String generateAuthenticationEmailBody(String authUrl) {
        return "<div>"
                .concat("<a href=\"" + authUrl + "\">인증 확인</a>")
                .concat("</div>");
    }

    public String getHostPath(ServerRequest request) {
        URI uri = request.uri();
        return uri.getScheme() + "://" + uri.getHost() + ":" + uri.getPort();
    }

    public String generateAuthUrl(String hostPath, String email) {
        return hostPath + "/user/email/" + email + "/authentication/confirm";
    }
}



