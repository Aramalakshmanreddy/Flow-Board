package com.project.emailSender.service;

import com.project.emailSender.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements IEmailService {
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public String confirmRegisterForCreator(User user) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        String body = "<div style=\"border: 2px solid rebeccapurple; padding: 1px; width: 700px;\">" +
                "    <h1 style=\"color: rebeccapurple; text-align: center;\">Registration for the Kanban Board Completed.</h1>" +
                "    <p style=\"font-size: 25px; text-align: center;\">Your Login Details:</p>" +
                "    <p style=\"font-size: 20px; padding-left: 25px;\">Login Email Id: " + user.getEmail() + "</p>" +
                "    <p style=\"font-size: 20px; padding-left: 25px;\">Login User Id: " + user.getUserName() + "</p>" +
                "    <p style=\"font-size: 20px; padding-left: 25px;\">Login Password: " + user.getPassword() + "</p>" +
                "    <p style=\"font-size: 20px; padding-left: 25px;\">To visit, click here: <a target=\"_blank\" href=\"http://localhost:4200/home\">Kanban Board</a></p>" +
                "</div>";
        message.setFrom(sender);
        message.setRecipients(MimeMessage.RecipientType.TO, user.getEmail());
        message.setSubject("Registration for the Kanban Board Completed");
        message.setContent(body, "text/html; charset=utf-8");
        javaMailSender.send(message);

        return "Mail Sent Successfully...";
    }

    @Override
    public String confirmRegisterForMember(User user) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        String body = "<div style=\"border: 2px solid rebeccapurple; padding: 1px; width: 700px;\">" +
                "    <h1 style=\"color: rebeccapurple; text-align: center;\">Access Granted for Kanban Board Project</h1>" +
                "    <h2 style=\"color: rebeccapurple; text-align: center;\">You have been added as a member by " + user.getCreatorEmail() + ".</h2>" +
                "    <p style=\"font-size: 25px; text-align: center;\">Your Login Details:</p>" +
                "    <p style=\"font-size: 20px; padding-left: 25px;\">Login Email Id: " + user.getEmail() + "</p>" +
                "    <p style=\"font-size: 20px; padding-left: 25px;\">Login User Id: " + user.getUserName() + "</p>" +
                "    <p style=\"font-size: 20px; padding-left: 25px;\">Login Password: " + user.getPassword() + "</p>" +
                "    <p style=\"font-size: 20px; padding-left: 25px;\">To visit, click here: <a target=\"_blank\" href=\"http://localhost:4200/home\">Kanban Board</a></p>" +
                "</div>";

        message.setFrom(sender);
        message.setRecipients(MimeMessage.RecipientType.TO, user.getEmail());
        message.setSubject("Access Granted for Kanban Board Project");
        message.setContent(body, "text/html; charset=utf-8");
        javaMailSender.send(message);

        return "Mail Sent Successfully...";
    }

    @Override
    public String memberRemovedConfirmation(User user) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        String body = "<div style=\"border: 2px solid rebeccapurple; padding: 1px; width: 700px;\">" +
                "    <h1 style=\"color: rebeccapurple; text-align: center;\">Access Removed for Kanban Board Project</h1>" +
                "    <h2 style=\"color: rebeccapurple; text-align: center;\">Your member access has been revoked by " + user.getCreatorEmail() + ".</h2>" +
                "    <p style=\"font-size: 20px; padding-left: 25px;\">Your login credentials will be invalid. For future use, please register again</p>" +
                "    <p style=\"font-size: 20px; padding-left: 25px;\">To visit, click here: <a target=\"_blank\" href=\"http://localhost:4200/home\">Kanban Board</a></p>" +
                "</div>";

        message.setFrom(sender);
        message.setRecipients(MimeMessage.RecipientType.TO, user.getEmail());
        message.setSubject("Access Revoked for Kanban Board Project");
        message.setContent(body, "text/html; charset=utf-8");
        javaMailSender.send(message);

        return "Mail Sent Successfully...";
    }

    @Override
    public String completedTaskConfirmation(String creatorEmail, String taskMessage) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        String body = "<div style=\"border: 2px solid rebeccapurple; padding: 1px; width: 700px;\">" +
                "    <h1 style=\"color: rebeccapurple; text-align: center;\">Task Completed</h1>" +
                "    <p style=\"font-size: 20px; padding-left: 25px;\">"+taskMessage+".</p>" +
                "    <p style=\"font-size: 20px; padding-left: 25px;\">To visit, click here: <a target=\"_blank\" href=\"http://localhost:4200/home\">Kanban Board</a></p>" +
                "</div>";

        message.setFrom(sender);
        message.setRecipients(MimeMessage.RecipientType.TO, creatorEmail);
        message.setSubject("Task Completed");
        message.setContent(body, "text/html; charset=utf-8");
        javaMailSender.send(message);

        return "Mail Sent Successfully...";
    }
}
