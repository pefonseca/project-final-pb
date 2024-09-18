package com.blog.security.comment.services.api.rest.controller;

import com.blog.security.comment.services.api.rest.dto.request.CommentRequestDTO;
import com.blog.security.comment.services.api.rest.dto.response.CommentResponseDTO;
import com.blog.security.comment.services.domain.service.CommentService;
import com.blog.security.comment.services.infra.feign.response.PostResponse;
import com.blog.security.comment.services.infra.feign.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_ShouldReturnCommentResponse_WhenCommentExists() {
        Long commentId = 1L;
        CommentResponseDTO expectedComment = new CommentResponseDTO(commentId, "Test content", new UserResponse(), new PostResponse(), LocalDateTime.now());
        when(commentService.findById(commentId)).thenReturn(expectedComment);

        ResponseEntity<CommentResponseDTO> response = commentController.findById(commentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedComment, response.getBody());
        verify(commentService, times(1)).findById(commentId);
    }

    @Test
    void findByPostId_ShouldReturnListOfComments_WhenCommentsExist() {
        Long postId = 1L;
        List<CommentResponseDTO> expectedComments = List.of(
                new CommentResponseDTO(1L, "Test content", new UserResponse(), new PostResponse(), LocalDateTime.now()),
                new CommentResponseDTO(2L, "Test content", new UserResponse(), new PostResponse(), LocalDateTime.now())
        );
        when(commentService.findByPostId(postId)).thenReturn(expectedComments);

        ResponseEntity<List<CommentResponseDTO>> response = commentController.findByPostId(postId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedComments, response.getBody());
        verify(commentService, times(1)).findByPostId(postId);
    }

    @Test
    void create_ShouldReturnCreatedComment_WhenSuccessful() {
        CommentRequestDTO requestDTO = new CommentRequestDTO("Test content", 1L, 1L);
        CommentResponseDTO createdComment = new CommentResponseDTO(1L, "Test content", new UserResponse(), new PostResponse(), LocalDateTime.now());
        when(commentService.create(requestDTO)).thenReturn(createdComment);

        ResponseEntity<CommentResponseDTO> response = commentController.create(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdComment, response.getBody());
        verify(commentService, times(1)).create(requestDTO);
    }

    @Test
    void delete_ShouldReturnNoContent_WhenCommentDeletedSuccessfully() {
        Long commentId = 1L;
        doNothing().when(commentService).delete(commentId);

        ResponseEntity<String> response = commentController.delete(commentId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(commentService, times(1)).delete(commentId);
    }

    @Test
    void deleteCommentByPostId_ShouldReturnNoContent_WhenCommentsDeletedSuccessfully() {
        Long postId = 1L;
        doNothing().when(commentService).deleteCommentByPostId(postId);

        ResponseEntity<String> response = commentController.deleteCommentByPostId(postId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(commentService, times(1)).deleteCommentByPostId(postId);
    }
}