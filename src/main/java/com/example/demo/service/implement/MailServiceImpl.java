package com.example.demo.service.implement;

import com.example.demo.model.DTO.user.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class MailServiceImpl {
    private final JavaMailSender javaMailSender;

    public void sendMail(UserDto userDto) throws MessagingException {
        String from = "noreply@spring.com";
        String to = userDto.getEmail();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setSubject("Account registration successful!");
        helper.setFrom(from);
        helper.setTo(to);

        boolean html = true;
        helper.setText("<b>Hi " + userDto.getUsername() + "</b>," +
                        "<br><i>This is your password: <b>" + userDto.getPassword() + "</b>. </i>" +
                        "<br>Do not share this email with anyone!",
                html);

        javaMailSender.send(message);
    }
}

