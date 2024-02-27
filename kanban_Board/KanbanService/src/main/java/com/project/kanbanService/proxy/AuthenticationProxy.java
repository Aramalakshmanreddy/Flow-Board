package com.project.kanbanService.proxy;

import com.project.kanbanService.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="user-authentication-service",url="localhost:8081")
public interface AuthenticationProxy {
    @PostMapping("/api/v1/user/save")
    User  saveUserInMysql(@RequestBody User user);

    @DeleteMapping("/api/v1/user/delete")
    void deleteUserInMysql(@RequestBody User user);
}
