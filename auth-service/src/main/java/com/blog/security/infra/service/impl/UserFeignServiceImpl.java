package com.blog.security.infra.service.impl;

import com.blog.security.infra.feign.UserFeignClient;
import com.blog.security.infra.request.UserRequest;
import com.blog.security.infra.feign.response.UserResponse;
import com.blog.security.infra.service.UserFeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFeignServiceImpl implements UserFeignService {

    private final UserFeignClient userFeignClient;

    /**
     * @param request
     * @return
     */
    @Override
    public UserResponse create(UserRequest request) {
        log.info("[UserFeignServiceImpl] -> (create): Criando usuário com o email: {}", request.getEmail());
        var userResponse = new UserResponse();

        var userResponseExchange = userFeignClient.create(request);

        if(userResponseExchange.getStatusCode() == HttpStatus.CREATED) {
            userResponse = userResponseExchange.getBody();
            log.info("[UserFeignServiceImpl] -> (create): Usuário criado com sucesso. ID: {}", Objects.requireNonNull(userResponse).getId());
        } else {
            log.error("[UserFeignServiceImpl] -> (create): Falha ao criar usuário. Status: {}", userResponseExchange.getStatusCode());
        }

        return userResponse;
    }

    /**
     * @param email
     * @return
     */
    @Override
    public UserResponse findByEmail(String email) {
        log.info("[UserFeignServiceImpl] -> (findByEmail): Buscando usuário com o email: {}", email);
        var userResponse = new UserResponse();

        var userResponseExchange = userFeignClient.findByEmail(email);

        if(userResponseExchange.getStatusCode() == HttpStatus.OK) {
            userResponse = userResponseExchange.getBody();
            log.info("[UserFeignServiceImpl] -> (findByEmail): Usuário encontrado. ID: {}", Objects.requireNonNull(userResponse).getId());
        } else {
            log.error("[UserFeignServiceImpl] -> (findByEmail): Falha ao buscar usuário. Status: {}", userResponseExchange.getStatusCode());
        }

        return userResponse;
    }
}
