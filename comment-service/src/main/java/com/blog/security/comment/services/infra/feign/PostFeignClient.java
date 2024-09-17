package com.blog.security.comment.services.infra.feign;

import com.blog.security.comment.services.infra.feign.response.PostResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "post-service", url = "http://post-service:8083")
public interface PostFeignClient {

    @GetMapping(value = "/api/v1/post/{id}", produces = "application/json")
    ResponseEntity<PostResponse> findById(@PathVariable(value = "id") Long id);

}
