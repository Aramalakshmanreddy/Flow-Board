package com.project.notification.service;

import com.project.notification.domain.Notification;
import com.project.notification.domain.Task;
import com.project.notification.exception.NotificationAlreadyExistException;
import com.project.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements INotificationService{
    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public String saveNotification(Task task) throws NotificationAlreadyExistException {
        int id = 0;
        if (notificationRepository.findAll().isEmpty())
            id = 1;
        else {
            List<Notification> list = notificationRepository.findAll();
            int size = list.size();
            id = list.get(size-1).getNotificationId()+1;
        }
        if (notificationRepository.findById(id).isPresent())
            throw new NotificationAlreadyExistException();

        Notification notification = new Notification();
        notification.setNotificationId(id);
        notification.setMessage(task.getTaskName().concat(" task completed by ").concat(task.getAssignee()));
        notification.setTask(task);

        notificationRepository.save(notification);
        return notification.getMessage();
    }

    @Override
    public List<Notification> getAllMessages() throws Exception {
        return notificationRepository.findAll();
    }
}
