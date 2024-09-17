package com.blog.security.comment.services.api.rest.dto.request;

import com.blog.security.comment.services.domain.model.entity.Comment;
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
public class CommentRequestDTO {

    private String content;
    private Long userId;
    private Long postId;

    public Comment toEntity() {
        return Comment.builder()
                .content(this.content)
                .userId(this.userId)
                .postId(this.postId)
                .createAt(LocalDateTime.now())
                .build();
    }

}
