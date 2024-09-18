package com.blog.security.comment.services.domain.service.impl;

import com.blog.security.comment.services.api.rest.dto.request.CommentRequestDTO;
import com.blog.security.comment.services.api.rest.dto.response.CommentResponseDTO;
import com.blog.security.comment.services.domain.model.dto.AuditMessageDTO;
import com.blog.security.comment.services.domain.model.entity.Comment;
import com.blog.security.comment.services.domain.producer.AuditLogRequestProducer;
import com.blog.security.comment.services.infra.feign.PostFeignClient;
import com.blog.security.comment.services.infra.feign.UserFeignClient;
import com.blog.security.comment.services.infra.feign.response.PostResponse;
import com.blog.security.comment.services.infra.feign.response.UserResponse;
import com.blog.security.comment.services.infra.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommentServiceImplTest {

    @Mock
    private CommentRepository repository;

    @Mock
    private AuditLogRequestProducer producer;

    @Mock
    private UserFeignClient userFeignClient;

    @Mock
    private PostFeignClient postFeignClient;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_ShouldReturnComment_WhenFound() {
        Long commentId = 1L;
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setUserId(2L);
        comment.setPostId(3L);

        UserResponse userResponse = new UserResponse();
        PostResponse postResponse = new PostResponse();

        when(repository.findById(commentId)).thenReturn(Optional.of(comment));
        when(userFeignClient.findById(comment.getUserId())).thenReturn(ResponseEntity.ok(userResponse));
        when(postFeignClient.findById(comment.getPostId())).thenReturn(ResponseEntity.ok(postResponse));

        CommentResponseDTO result = commentService.findById(commentId);

        assertNotNull(result);
        assertEquals(commentId, result.getId());
        verify(repository).findById(commentId);
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        Long commentId = 1L;

        when(repository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> commentService.findById(commentId));
        verify(repository).findById(commentId);
    }

    @Test
    void create_ShouldSaveCommentAndSendAuditMessage() {
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUserId(2L);
        comment.setPostId(3L);

        UserResponse userResponse = new UserResponse();
        PostResponse postResponse = new PostResponse();

        when(repository.save(any(Comment.class))).thenReturn(comment);
        when(userFeignClient.findById(comment.getUserId())).thenReturn(ResponseEntity.ok(userResponse));
        when(postFeignClient.findById(comment.getPostId())).thenReturn(ResponseEntity.ok(postResponse));

        CommentResponseDTO result = commentService.create(requestDTO);

        assertNotNull(result);
        verify(repository).save(any(Comment.class));
        verify(producer).integration(any(AuditMessageDTO.class));
    }

    @Test
    void findByPostId_ShouldReturnComments() {
        Long postId = 1L;
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUserId(2L);
        comment.setPostId(postId);

        UserResponse userResponse = new UserResponse();
        PostResponse postResponse = new PostResponse();

        when(repository.findByPostId(postId)).thenReturn(List.of(comment));
        when(userFeignClient.findById(comment.getUserId())).thenReturn(ResponseEntity.ok(userResponse));
        when(postFeignClient.findById(comment.getPostId())).thenReturn(ResponseEntity.ok(postResponse));

        List<CommentResponseDTO> result = commentService.findByPostId(postId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).findByPostId(postId);
    }

    @Test
    void delete_ShouldRemoveComment() {
        Long commentId = 1L;

        commentService.delete(commentId);

        verify(repository).deleteById(commentId);
    }

    @Test
    void deleteCommentByPostId_ShouldRemoveCommentsAndSendAuditMessage() {
        Long postId = 1L;

        commentService.deleteCommentByPostId(postId);

        verify(repository).deleteByPostId(postId);
        verify(producer).integration(any(AuditMessageDTO.class));
    }
}
