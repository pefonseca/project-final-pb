package com.blog.security.comment.services;

import com.blog.security.comment.services.infra.feign.PostFeignClient;
import com.blog.security.comment.services.infra.feign.UserFeignClient;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableRabbit
@EnableFeignClients(clients = {UserFeignClient.class, PostFeignClient.class})
@SpringBootApplication
public class CommentServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommentServicesApplication.class, args);
    }

}
