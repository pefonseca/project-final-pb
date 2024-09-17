package com.blog.security.comment.services.infra.repository;

import com.blog.security.comment.services.domain.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(Long id);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.postId = :postId")
    void deleteByPostId(@Param("postId") Long postId);
}