package com.blog.security.infra.feign;

import com.blog.security.infra.request.UserRequest;
import com.blog.security.infra.feign.response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "http://user-service:8081")
public interface UserFeignClient {

    @GetMapping(value = "/api/v1/user/find_by_email", produces = "application/json")
    ResponseEntity<UserResponse> findByEmail(@RequestParam(value = "email") String email);

    @PostMapping(value = "/api/v1/user", produces = "application/json")
    ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request);

}
