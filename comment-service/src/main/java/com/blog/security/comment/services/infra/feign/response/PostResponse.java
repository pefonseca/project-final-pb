package com.blog.security.comment.services.infra.feign.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private String content;
    private UserResponse userResponse;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

}
