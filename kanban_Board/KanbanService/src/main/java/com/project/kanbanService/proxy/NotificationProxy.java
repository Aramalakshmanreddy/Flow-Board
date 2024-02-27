package com.project.kanbanService.proxy;

import com.project.kanbanService.domain.Task;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="notification-service",url="localhost:8085")
public interface NotificationProxy {

    @PostMapping("/api/v3/notification/save")
    String saveNotification(@RequestBody Task task);

    @GetMapping("/api/v3/notification/all")
    public ResponseEntity<?> getAllNotifications();
}
