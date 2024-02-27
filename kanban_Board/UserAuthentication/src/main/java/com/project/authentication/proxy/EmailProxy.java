package com.project.authentication.proxy;

import com.project.authentication.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "email-sender-service", url = "localhost:8090")
public interface EmailProxy {
    @PostMapping("/api/v4/confirm/creator")
    ResponseEntity<String> confirmRegisterForCreator(@RequestBody User user);
    @PostMapping("/api/v4/confirm/member")
    ResponseEntity<String> confirmRegisterForMember(@RequestBody User user);
    @PostMapping("/api/v4/remove/member")
    ResponseEntity<String> memberRemovedConfirmation(@RequestBody User user);
}
