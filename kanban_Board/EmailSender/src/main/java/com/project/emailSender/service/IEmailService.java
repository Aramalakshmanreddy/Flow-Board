package com.project.emailSender.service;

import com.project.emailSender.domain.Email;
import com.project.emailSender.domain.User;

import javax.mail.MessagingException;

public interface IEmailService {
    String confirmRegisterForCreator(User user) throws MessagingException;
    String confirmRegisterForMember(User user) throws MessagingException;
    String memberRemovedConfirmation(User user) throws MessagingException;
    String completedTaskConfirmation(String creatorEmail, String taskMessage) throws MessagingException;
}
