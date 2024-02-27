package com.project.kanbanService.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "email-sender-service", url = "localhost:8090")
public interface EmailProxy {
    @PostMapping("/api/v4/confirm/task/completed")
    void completedTaskConfirmation(@RequestParam("creatorEmail") String creatorEmail, @RequestParam("taskMessage") String taskMessage);

}
