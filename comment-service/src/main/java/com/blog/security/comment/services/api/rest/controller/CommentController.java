package com.blog.security.comment.services.api.rest.controller;

import com.blog.security.comment.services.api.rest.dto.request.CommentRequestDTO;
import com.blog.security.comment.services.api.rest.dto.response.CommentResponseDTO;
import com.blog.security.comment.services.domain.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<CommentResponseDTO> findById(@PathVariable(value = "id") Long id) {
        log.info("[CommentController] -> (findById): Recebendo solicitação para buscar comentário pelo id: {}", id);
        CommentResponseDTO comment = service.findById(id);
        log.info("[CommentController] -> (findById): Comentário encontrado: {}", comment);
        return ResponseEntity.ok(comment);
    }

    @GetMapping(value = "/post/{id}")
    public ResponseEntity<List<CommentResponseDTO>> findByPostId(@PathVariable(value = "id") Long id) {
        log.info("[CommentController] -> (findByPostId): Recebendo solicitação para buscar comentários pelo id do post: {}", id);
        List<CommentResponseDTO> comments = service.findByPostId(id);
        log.info("[CommentController] -> (findByPostId): Comentários encontrados: {}", comments);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentResponseDTO> create(@Valid @RequestBody CommentRequestDTO commentRequestDTO) {
        log.info("[CommentController] -> (create): Recebendo solicitação para criar comentário: {}", commentRequestDTO);
        CommentResponseDTO createdComment = service.create(commentRequestDTO);
        log.info("[CommentController] -> (create): Comentário criado: {}", createdComment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable(value = "id") Long id) {
        log.info("[CommentController] -> (delete): Recebendo solicitação para excluir comentário pelo id: {}", id);
        service.delete(id);
        log.info("[CommentController] -> (delete): Comentário com id {} excluído com sucesso", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Comentário excluído com sucesso.");
    }

    @DeleteMapping(value = "/post/{id}")
    public ResponseEntity<String> deleteCommentByPostId(@PathVariable(value = "id") Long id) {
        log.info("[CommentController] -> (deleteCommentByPostId): Recebendo solicitação para excluir comentários pelo id do post: {}", id);
        service.deleteCommentByPostId(id);
        log.info("[CommentController] -> (deleteCommentByPostId): Comentários do post com id {} excluídos com sucesso", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Comentários excluídos com sucesso!");
    }
}
