package com.blog.security.comment.services.api.rest.dto.response;

import com.blog.security.comment.services.infra.feign.response.PostResponse;
import com.blog.security.comment.services.infra.feign.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {

    private Long id;
    private String content;
    private UserResponse userResponse;
    private PostResponse postResponse;
    private LocalDateTime createAt;

}
