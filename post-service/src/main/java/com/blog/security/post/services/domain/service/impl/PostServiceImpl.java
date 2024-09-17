package com.blog.security.post.services.domain.service.impl;

import com.blog.security.post.services.api.rest.dto.request.PostRequestDTO;
import com.blog.security.post.services.api.rest.dto.response.PostResponseDTO;
import com.blog.security.post.services.domain.model.dto.AuditMessageDTO;
import com.blog.security.post.services.domain.model.entity.Post;
import com.blog.security.post.services.domain.producer.AuditLogRequestProducer;
import com.blog.security.post.services.infra.repository.PostRepository;
import com.blog.security.post.services.domain.service.PostService;
import com.blog.security.post.services.infra.feign.CommentFeignClient;
import com.blog.security.post.services.infra.feign.UserFeignClient;
import com.blog.security.post.services.infra.feign.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository repository;
    private final AuditLogRequestProducer producer;
    private final UserFeignClient feignClient;
    private final CommentFeignClient commentFeignClient;

    /**
     * @return
     */
    @Override
    public List<PostResponseDTO> findAll() {
        log.info("[PostServiceImpl] -> (findAll): Buscando todas as postagens.");
        List<PostResponseDTO> posts = repository.findAll().stream().map(post -> {
            var userResponse = feignClient.findById(post.getUserId()).getBody();
            return PostResponseDTO.builder()
                                  .id(post.getId())
                                  .userResponse(userResponse)
                                  .content(post.getContent())
                                  .createdAt(post.getCreatedAt())
                                  .updateAt(post.getUpdatedAt())
                                  .build();
        }).toList();

        log.info("[PostServiceImpl] -> (findAll): Postagens encontradas: {}", posts.size());
        return posts;
    }

    /**
     * @param requestDTO
     * @return
     */
    @Override
    public PostResponseDTO create(PostRequestDTO requestDTO) {
        log.info("[PostServiceImpl] -> (create): Criando nova postagem com dados: {}", requestDTO);
        PostResponseDTO postResponseDTO;
        postResponseDTO = repository.save(requestDTO.toEntity()).toDTO();

        var userResponse = feignClient.findById(requestDTO.getUserId()).getBody();

        postResponseDTO.setUserResponse(userResponse);

        AuditMessageDTO auditMessageDTO = AuditMessageDTO.builder()
                                                         .entityName("POST")
                                                         .entityId(2L)
                                                         .action("CREATE")
                                                         .performedBy("User Id: " + postResponseDTO.getUserResponse().getId())
                                                         .details("Postagem criada com sucesso.")
                                                         .build();

        log.info("[PostServiceImpl] -> (create): Enviando mensagem de auditoria: {}", auditMessageDTO);
        producer.integration(auditMessageDTO);

        log.info("[PostServiceImpl] -> (create): Postagem criada com sucesso: {}", postResponseDTO);
        return postResponseDTO;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public PostResponseDTO findById(Long id) {
        log.info("[PostServiceImpl] -> (findById): Buscando postagem com ID: {}", id);
        PostResponseDTO postResponseDTO = new PostResponseDTO();

        Post postDB = repository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));

        UserResponse userResponse = feignClient.findById(postDB.getUserId()).getBody();

        buildPostResponse(postResponseDTO, postDB, userResponse);

        log.info("[PostServiceImpl] -> (findById): Postagem encontrada: {}", postResponseDTO);
        return postResponseDTO;
    }

    /**
     * @param id
     */
    @Override
    public void delete(Long id) {
        log.info("[PostServiceImpl] -> (delete): Excluindo postagem com ID: {}", id);
        commentFeignClient.deleteCommentByPostId(id);
        repository.deleteById(id);

        AuditMessageDTO auditMessageDTO = AuditMessageDTO.builder()
                                                         .entityName("POST")
                                                         .entityId(2L)
                                                         .action("DELETE")
                                                         .performedBy("User Id: There is no user to delete")
                                                         .details("Postagem excluída com sucesso.")
                                                         .build();

        log.info("[PostServiceImpl] -> (delete): Enviando mensagem de auditoria: {}", auditMessageDTO);
        producer.integration(auditMessageDTO);

        log.info("[PostServiceImpl] -> (delete): Postagem excluída com sucesso.");
    }

    private static void buildPostResponse(PostResponseDTO postResponseDTO, Post postDB, UserResponse userResponse) {
        postResponseDTO.setId(postDB.getId());
        postResponseDTO.setContent(postDB.getContent());
        postResponseDTO.setUpdateAt(postDB.getCreatedAt());
        postResponseDTO.setCreatedAt(postDB.getCreatedAt());
        postResponseDTO.setUserResponse(userResponse);
    }
}
