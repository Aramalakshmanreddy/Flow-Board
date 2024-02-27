package com.project.notification.service;

import com.project.notification.domain.Notification;
import com.project.notification.domain.Task;
import com.project.notification.exception.NotificationAlreadyExistException;

import java.util.List;

public interface INotificationService {
    String saveNotification(Task task) throws NotificationAlreadyExistException;
    List<Notification> getAllMessages() throws Exception;
}
