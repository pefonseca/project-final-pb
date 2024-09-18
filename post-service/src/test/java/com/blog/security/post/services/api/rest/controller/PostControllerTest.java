package com.blog.security.post.services.api.rest.controller;

import com.blog.security.post.services.api.rest.dto.request.PostRequestDTO;
import com.blog.security.post.services.api.rest.dto.response.PostResponseDTO;
import com.blog.security.post.services.domain.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PostControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTimeLine_ShouldReturnListOfPosts() {
        List<PostResponseDTO> postList = Arrays.asList(
                PostResponseDTO.builder().id(1L).content("Content 1").build(),
                PostResponseDTO.builder().id(2L).content("Content 2").build()
        );
        when(postService.findAll()).thenReturn(postList);

        ResponseEntity<List<PostResponseDTO>> response = postController.getTimeLine();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(postService, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnPostById() {
        Long postId = 1L;
        PostResponseDTO postResponse = PostResponseDTO.builder().id(1L).content("Content 1").build();
        when(postService.findById(postId)).thenReturn(postResponse);

        ResponseEntity<PostResponseDTO> response = postController.findById(postId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postId, response.getBody().getId());
        verify(postService, times(1)).findById(postId);
    }

    @Test
    void create_ShouldReturnCreatedPost() {
        PostRequestDTO postRequestDTO = PostRequestDTO.builder().content("New Content").build();
        PostResponseDTO postResponse = PostResponseDTO.builder().id(1L).content(postRequestDTO.getContent()).build();
        when(postService.create(postRequestDTO)).thenReturn(postResponse);

        ResponseEntity<PostResponseDTO> response = postController.create(postRequestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(postRequestDTO.getContent(), Objects.requireNonNull(response.getBody()).getContent());
        verify(postService, times(1)).create(postRequestDTO);
    }

    @Test
    void delete_ShouldReturnNoContent() {
        Long postId = 1L;
        doNothing().when(postService).delete(postId);

        ResponseEntity<String> response = postController.delete(postId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Postagem excluída.", response.getBody());
        verify(postService, times(1)).delete(postId);
    }
}
