package com.blog.security.post.services.infra.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "comment-service", url = "http://comment-service:8084")
public interface CommentFeignClient {

    @DeleteMapping(value = "/api/v1/comment/post/{id}")
    ResponseEntity<String> deleteCommentByPostId(@PathVariable(value = "id") Long id);

}
