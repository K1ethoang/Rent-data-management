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
    private static final String COMPANY_NAME = "MiuKit Company: ";
    private static final String FROM = "noreply@miukit.com";

    private final JavaMailSender javaMailSender;

    public void sendMailRegisterSuccess(UserDto userDto) throws MessagingException {
        String to = userDto.getEmail();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setSubject(COMPANY_NAME + "Account registration successful!");
        helper.setFrom(FROM);
        helper.setTo(to);

        boolean html = true;
        helper.setText("<b>Hi " + userDto.getFullName() + "</b>," +
                        "<br><i>Your username: <b>" + userDto.getEmail() + "</b></i>" +
                        "<br><i>Your password: <b>" + userDto.getPassword() + "</b></i>" +
                        "<br> <p style=\"color: red;\">Do not share this email with anyone!</p>",
                html);

        javaMailSender.send(message);
    }

    public void sendMailNewPassword(String email, String newPassword) throws MessagingException {
        String to = email;

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setSubject(COMPANY_NAME + "Reset password!!");
        helper.setFrom(FROM);
        helper.setTo(to);

        boolean html = true;
        helper.setText("Your new password is: <strong> " + newPassword + "</strong>" +
                "<br> <p style=\"color: red;\">Do not share this email with anyone!</p>", html);

        javaMailSender.send(message);
    }

    public void sendMailEmailChanged(String oldEmail, String newEmail) throws MessagingException {
        String to = oldEmail;

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setSubject(COMPANY_NAME + "Email Changed");
        helper.setFrom(FROM);
        helper.setTo(to);

        boolean html = true;
        helper.setText("Account's email is changed to: <strong> " + newEmail + "</strong>", html);

        javaMailSender.send(message);
    }
}

