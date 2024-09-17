package com.blog.security.comment.services.domain.service;

import com.blog.security.comment.services.api.rest.dto.request.CommentRequestDTO;
import com.blog.security.comment.services.api.rest.dto.response.CommentResponseDTO;

import java.util.List;

public interface CommentService {

    CommentResponseDTO findById(Long id);
    CommentResponseDTO create(CommentRequestDTO requestDTO);
    List<CommentResponseDTO> findByPostId(Long id);
    void delete(Long id);
    void deleteCommentByPostId(Long id);

}
