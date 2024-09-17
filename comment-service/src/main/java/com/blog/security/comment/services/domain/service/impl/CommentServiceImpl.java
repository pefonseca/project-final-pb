package com.blog.security.comment.services.domain.service.impl;

import com.blog.security.comment.services.api.rest.dto.request.CommentRequestDTO;
import com.blog.security.comment.services.api.rest.dto.response.CommentResponseDTO;
import com.blog.security.comment.services.domain.model.dto.AuditMessageDTO;
import com.blog.security.comment.services.domain.model.entity.Comment;
import com.blog.security.comment.services.domain.producer.AuditLogRequestProducer;
import com.blog.security.comment.services.infra.repository.CommentRepository;
import com.blog.security.comment.services.domain.service.CommentService;
import com.blog.security.comment.services.infra.feign.PostFeignClient;
import com.blog.security.comment.services.infra.feign.UserFeignClient;
import com.blog.security.comment.services.infra.feign.response.PostResponse;
import com.blog.security.comment.services.infra.feign.response.UserResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;
    private final AuditLogRequestProducer producer;
    private final UserFeignClient userFeignClient;
    private final PostFeignClient postFeignClient;

    /**
     * @param id
     * @return
     */
    @Override
    public CommentResponseDTO findById(Long id) {
        log.info("[CommentServiceImpl] -> (findById): Buscando comentário com o ID: {}", id);
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
        var commentDB = repository.findById(id)
                                  .orElseThrow(() -> {
                                      log.error("[CommentServiceImpl] -> (findById): Comentário com o ID: {} não encontrado.", id);
                                      return new RuntimeException("Comment not found.");
                                  });

        var userResponse = userFeignClient.findById(commentDB.getUserId()).getBody();
        var postResponse = postFeignClient.findById(commentDB.getPostId()).getBody();

        buildCommentResponse(commentResponseDTO, commentDB, userResponse, postResponse);

        log.info("[CommentServiceImpl] -> (findById): Comentário encontrado: {}", commentResponseDTO);
        return commentResponseDTO;
    }

    private static void buildCommentResponse(CommentResponseDTO commentResponseDTO, Comment commentDB, UserResponse userResponse, PostResponse postResponse) {
        commentResponseDTO.setId(commentDB.getId());
        commentResponseDTO.setContent(commentDB.getContent());
        commentResponseDTO.setUserResponse(userResponse);
        commentResponseDTO.setPostResponse(postResponse);
        commentResponseDTO.setCreateAt(commentDB.getCreateAt());
    }

    /**
     * @param requestDTO
     * @return
     */
    @Override
    public CommentResponseDTO create(CommentRequestDTO requestDTO) {
        log.info("[CommentServiceImpl] -> (create): Criando um novo comentário com dados: {}", requestDTO);
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();

        var commentDB = repository.save(requestDTO.toEntity());

        var postResponse = postFeignClient.findById(commentDB.getPostId()).getBody();
        var userResponse = userFeignClient.findById(commentDB.getUserId()).getBody();

        buildCommentResponse(commentResponseDTO, commentDB, userResponse, postResponse);

        AuditMessageDTO auditMessageDTO = AuditMessageDTO.builder()
                                                         .entityName("COMMENT")
                                                         .entityId(2L)
                                                         .action("CREATE")
                                                         .performedBy("User Id: " + commentResponseDTO.getUserResponse().getId())
                                                         .details("Comentário criado com sucesso.")
                                                         .build();

        log.info("[CommentServiceImpl] -> (create): Enviando mensagem de auditoria: {}", auditMessageDTO);
        producer.integration(auditMessageDTO);

        log.info("[CommentServiceImpl] -> (create): Comentário criado com sucesso: {}", commentResponseDTO);
        return commentResponseDTO;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public List<CommentResponseDTO> findByPostId(Long id) {
        log.info("[CommentServiceImpl] -> (findByPostId): Buscando comentários para o Post ID: {}", id);
        List<CommentResponseDTO> comments = repository.findByPostId(id).stream().map(comment -> {
            UserResponse userResponse = userFeignClient.findById(comment.getUserId()).getBody();
            PostResponse postResponse = postFeignClient.findById(comment.getPostId()).getBody();
            return CommentResponseDTO.builder()
                                     .id(comment.getId())
                                     .content(comment.getContent())
                                     .userResponse(userResponse)
                                     .postResponse(postResponse)
                                     .createAt(comment.getCreateAt())
                                     .build();
        }).toList();

        log.info("[CommentServiceImpl] -> (findByPostId): Comentários encontrados: {}", comments);
        return comments;
    }

    /**
     * @param id
     */
    @Override
    public void delete(Long id) {
        log.info("[CommentServiceImpl] -> (delete): Excluindo comentário com o ID: {}", id);
        repository.deleteById(id);
        log.info("[CommentServiceImpl] -> (delete): Comentário com o ID: {} excluído com sucesso", id);
    }

    /**
     * @param id
     */
    @Override
    @Transactional
    public void deleteCommentByPostId(Long id) {
        log.info("[CommentServiceImpl] -> (deleteCommentByPostId): Excluindo comentários para o Post ID: {}", id);
        repository.deleteByPostId(id);

        AuditMessageDTO auditMessageDTO = AuditMessageDTO.builder()
                                                        .entityName("COMMENT")
                                                        .entityId(3L)
                                                        .action("DELETE")
                                                        .performedBy("User Id: There is no user to delete")
                                                        .details("Comentário criado com sucesso.")
                                                        .build();

        log.info("[CommentServiceImpl] -> (deleteCommentByPostId): Enviando mensagem de auditoria: {}", auditMessageDTO);
        producer.integration(auditMessageDTO);

        log.info("[CommentServiceImpl] -> (deleteCommentByPostId): Comentários para o Post ID: {} excluídos com sucesso", id);
    }
}
