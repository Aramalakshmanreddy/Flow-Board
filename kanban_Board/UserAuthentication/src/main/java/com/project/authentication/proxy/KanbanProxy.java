package com.project.authentication.proxy;

import com.project.authentication.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "kanban-service", url = "localhost:8082")
public interface KanbanProxy {
    @PostMapping("/api/v2/user/save")
    ResponseEntity<User> saveUserInMongoDb(@RequestBody User user);
}
