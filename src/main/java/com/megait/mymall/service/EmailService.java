package com.megait.mymall.service;
import com.megait.mymall.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Transactional
    public void sendEmail(Member member) {
        // TOKEN 생성
        String token = UUID.randomUUID().toString();

        // TOKEN 을 DB 에 update
        member.setEmailCheckToken(token);

        // 일반 메일 보내기
        // sendSimpleMailMessage(member);
        sendHtmlMailMessage(member);
    }

    private void sendHtmlMailMessage(Member member) {
        String html = "<html><body>" +
                "<p style=\"background:red\">링크 : <a href=\"http://localhost:8080/email-check?email=" + member.getEmail()
                + "&token=" + member.getEmailCheckToken() + "\">이메일 인증을 원하시는 경우 이곳을 클릭하세요.</a></p>" +
                "</body></html>";

        // html 페이지를 보낼 때는 일반 메시지가 아니라, MIME 타입의 메시지를 보내야 함.
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper =
                    new MimeMessageHelper(mimeMessage, false, "UTF-8");

            String title = "[My Mall] 회원 가입에 감사드립니다. 딱 한 가지 과정이 남았습니다!";
            mimeMessageHelper.setTo(member.getEmail());
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(html, true);

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e){
            log.error("이메일 전송 실패. ({})", member.getEmail());
        }

    }

    private void sendSimpleMailMessage(Member member) {
        // 이메일 날리기
        String url =
                "http://localhost:8080/email-check?email=" + member.getEmail()
                + "&token=" + member.getEmailCheckToken();

        String title = "[My Mall] 회원 가입에 감사드립니다. 딱 한 가지 과정이 남았습니다!";
        String message = "다음 링크를 브라우저에 붙여넣어주세요. 링크 : " + url;
        String sender = "mymall-admin-noreply@mymall.com";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(member.getEmail());
        mailMessage.setSubject(title);
        mailMessage.setText(message);
        mailMessage.setFrom(sender);
        javaMailSender.send(mailMessage);
    }
}
