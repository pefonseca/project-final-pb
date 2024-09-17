package com.blog.security.infra.service;

import com.blog.security.infra.request.UserRequest;
import com.blog.security.infra.feign.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserFeignService {

    UserResponse create(UserRequest request);
    UserResponse findByEmail(String email);

}
