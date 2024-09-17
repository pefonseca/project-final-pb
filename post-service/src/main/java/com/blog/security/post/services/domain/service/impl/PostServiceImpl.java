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
        return repository.findAll().stream().map(post -> {
            var userResponse = feignClient.findById(post.getUserId()).getBody();
            return PostResponseDTO.builder()
                                  .id(post.getId())
                                  .userResponse(userResponse)
                                  .content(post.getContent())
                                  .createdAt(post.getCreatedAt())
                                  .updateAt(post.getUpdatedAt())
                                  .build();
        }).toList();
    }

    /**
     * @param requestDTO
     * @return
     */
    @Override
    public PostResponseDTO create(PostRequestDTO requestDTO) {
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

        producer.integration(auditMessageDTO);

        return postResponseDTO;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public PostResponseDTO findById(Long id) {
        PostResponseDTO postResponseDTO = new PostResponseDTO();

        Post postDB = repository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));

        UserResponse userResponse = feignClient.findById(postDB.getUserId()).getBody();

        buildPostResponse(postResponseDTO, postDB, userResponse);

        return postResponseDTO;
    }

    /**
     * @param id
     */
    @Override
    public void delete(Long id) {
        commentFeignClient.deleteCommentByPostId(id);
        repository.deleteById(id);

        AuditMessageDTO auditMessageDTO = AuditMessageDTO.builder()
                                                         .entityName("POST")
                                                         .entityId(2L)
                                                         .action("DELETE")
                                                         .performedBy("User Id: There is no user to delete")
                                                         .details("Postagem excluída com sucesso.")
                                                         .build();

        producer.integration(auditMessageDTO);
    }

    private static void buildPostResponse(PostResponseDTO postResponseDTO, Post postDB, UserResponse userResponse) {
        postResponseDTO.setId(postDB.getId());
        postResponseDTO.setContent(postDB.getContent());
        postResponseDTO.setUpdateAt(postDB.getCreatedAt());
        postResponseDTO.setCreatedAt(postDB.getCreatedAt());
        postResponseDTO.setUserResponse(userResponse);
    }

    /**
     * @param payload
     */
    @Override
    public void errorAuditLog(String payload) {
        System.err.println(" ===== RESPOSTA ERRO AUDITORIA ===== " + payload);
        log.error("[UserServiceImpl] -> (successAuditLog): Erro ao fazer auditoria.");
    }

    /**
     * @param payload
     */
    @Override
    public void successAuditLog(String payload) {
        System.out.println(" ===== RESPOSTA SUCESSO AUDITORIA ===== " + payload);
        log.info("[UserServiceImpl] -> (successAuditLog): Sucesso ao fazer auditoria.");
    }
}
