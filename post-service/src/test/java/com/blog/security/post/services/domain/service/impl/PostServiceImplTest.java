package com.blog.security.post.services.domain.service.impl;

import com.blog.security.post.services.api.rest.dto.request.PostRequestDTO;
import com.blog.security.post.services.api.rest.dto.response.PostResponseDTO;
import com.blog.security.post.services.domain.model.dto.AuditMessageDTO;
import com.blog.security.post.services.domain.model.entity.Post;
import com.blog.security.post.services.domain.producer.AuditLogRequestProducer;
import com.blog.security.post.services.infra.feign.CommentFeignClient;
import com.blog.security.post.services.infra.feign.UserFeignClient;
import com.blog.security.post.services.infra.feign.response.UserResponse;
import com.blog.security.post.services.infra.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private AuditLogRequestProducer auditLogRequestProducer;

    @Mock
    private UserFeignClient userFeignClient;

    @Mock
    private CommentFeignClient commentFeignClient;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ShouldReturnListOfPosts() {
        List<Post> posts = Arrays.asList(
                new Post(1L, "Content 1", 1L, LocalDateTime.now(), LocalDateTime.now()),
                new Post(2L, "Content 2", 2L, LocalDateTime.now(), LocalDateTime.now())
        );

        UserResponse userResponse1 = new UserResponse(1L, "User1", "", "", "", "" ,"" ,"", "", "", "");
        UserResponse userResponse2 = new UserResponse(2L, "User2", "", "", "", "" ,"" ,"", "", "", "");

        when(postRepository.findAll()).thenReturn(posts);
        when(userFeignClient.findById(1L)).thenReturn(ResponseEntity.ok(userResponse1));
        when(userFeignClient.findById(2L)).thenReturn(ResponseEntity.ok(userResponse2));

        List<PostResponseDTO> result = postService.findAll();

        assertEquals(2, result.size());
        verify(postRepository, times(1)).findAll();
        verify(userFeignClient, times(2)).findById(anyLong());
    }

    @Test
    void create_ShouldReturnCreatedPost() {
        PostRequestDTO postRequestDTO = PostRequestDTO.builder().userId(1L).content("New Post").build();
        Post post = new Post(1L, "New Post", 1L, LocalDateTime.now(), LocalDateTime.now());
        UserResponse userResponse = new UserResponse(1L, "User2", "", "", "", "" ,"" ,"", "", "", "");

        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(userFeignClient.findById(1L)).thenReturn(ResponseEntity.ok(userResponse));

        PostResponseDTO result = postService.create(postRequestDTO);

        assertEquals("New Post", result.getContent());
        assertEquals(1L, result.getUserResponse().getId());
        verify(postRepository, times(1)).save(any(Post.class));
        verify(userFeignClient, times(1)).findById(1L);
        verify(auditLogRequestProducer, times(1)).integration(any(AuditMessageDTO.class));
    }

    @Test
    void findById_ShouldReturnPostById() {
        Long postId = 1L;
        Post post = new Post(1L, "Content 1", 1L, LocalDateTime.now(), LocalDateTime.now());
        UserResponse userResponse = new UserResponse(1L, "User2", "", "", "", "" ,"" ,"", "", "", "");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userFeignClient.findById(1L)).thenReturn(ResponseEntity.ok(userResponse));

        PostResponseDTO result = postService.findById(postId);

        assertEquals(postId, result.getId());
        assertEquals("Content 1", result.getContent());
        assertEquals(1L, result.getUserResponse().getId());
        verify(postRepository, times(1)).findById(postId);
        verify(userFeignClient, times(1)).findById(1L);
    }

    @Test
    void delete_ShouldRemovePost() {
        Long postId = 1L;
        assertDoesNotThrow(() -> postService.delete(postId));
    }
}
