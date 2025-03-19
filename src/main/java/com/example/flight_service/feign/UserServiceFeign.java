package com.example.flight_service.feign;

import com.example.flight_service.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceFeign {
    @GetMapping("user/profile")
    ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String token);
}
