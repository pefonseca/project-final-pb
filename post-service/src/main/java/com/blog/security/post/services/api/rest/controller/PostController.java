package com.blog.security.post.services.api.rest.controller;

import com.blog.security.post.services.api.rest.dto.request.PostRequestDTO;
import com.blog.security.post.services.api.rest.dto.response.PostResponseDTO;
import com.blog.security.post.services.domain.service.PostService;
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
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getTimeLine() {
        log.info("[PostController] -> (getTimeLine): Buscando todas as postagens");
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> findById(@PathVariable(value = "id") Long id) {
        log.info("[PostController] -> (findById): Buscando postagem com ID: {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> create(@Valid @RequestBody PostRequestDTO requestDTO) {
        log.info("[PostController] -> (create): Criando nova postagem com dados: {}", requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(value = "id") Long id) {
        log.info("[PostController] -> (delete): Excluindo postagem com ID: {}", id);
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Postagem excluída.");
    }
}
