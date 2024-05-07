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

    public void sendMailRegisterSuccess(UserDto userDto) throws MessagingException {
        String from = "noreply@spring.com";
        String to = userDto.getEmail();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setSubject("Account registration successful!");
        helper.setFrom(from);
        helper.setTo(to);

        boolean html = true;
        helper.setText("<b>Hi " + userDto.getUsername() + "</b>," +
                        "<br><i>Your username: <b>" + userDto.getUsername() + "</b></i>" +
                        "<br><i>Your password: <b>" + userDto.getPassword() + "</b></i>" +
                        "<br>Do not share this email with anyone!",
                html);

        javaMailSender.send(message);
    }

    public void sendMailNewPassword(String email, String newPassword) throws MessagingException {
        String from = "noreply@spring.com";
        String to = email;

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setSubject("Reset password!!");
        helper.setFrom(from);
        helper.setTo(to);

        boolean html = true;
        helper.setText("Your new password is: <strong> " + newPassword + "</strong>" +
                "<br> <p style=\"color: red;\">Do not share this email with anyone!</p>", html);

        javaMailSender.send(message);
    }
}

