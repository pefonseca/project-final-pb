// Post.js
import React, { useState, useEffect, useCallback } from 'react';
import styled from 'styled-components';
import Comment from './Comment';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';

const PostContainerStyled = styled.div`
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  position: relative;
`;

const PostUser = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 10px;
`;

const PostUserImage = styled.img`
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
`;

const PostContent = styled.p`
  font-size: 16px;
  color: #555;
`;

const PostDate = styled.p`
  font-size: 14px;
  color: #888;
`;

const CommentInput = styled.textarea`
  width: 100%;
  height: 60px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
  margin-bottom: 10px;
`;

const CommentButton = styled.button`
  background-color: #ff9900;
  border: none;
  border-radius: 4px;
  color: white;
  font-size: 14px;
  cursor: pointer;
  padding: 10px;

  &:hover {
    background-color: #e68a00;
  }
`;

const DeleteButton = styled.button`
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: #ff4d4d;
  border: none;
  border-radius: 4px;
  color: white;
  font-size: 14px;
  cursor: pointer;
  padding: 5px 10px;

  &:hover {
    background-color: #e60000;
  }
`;

const CommentsContainer = styled.div`
  margin-top: 20px;
`;

const formatDate = (dateString) => {
  const date = new Date(dateString);
  return isNaN(date) ? 'Data inválida' : date.toLocaleString();
};

const Post = ({ post, fetchComments, onPostDelete }) => {
  const [commentContent, setCommentContent] = useState('');
  const [comments, setComments] = useState([]);
  const { user } = useAuth();
  const navigate = useNavigate();

  const handleCommentSubmit = async () => {
    if (!commentContent) return;

    const newComment = {
      content: commentContent,
      userId: user.id,
      postId: post.id,
    };

    try {
      const response = await fetch('http://localhost:8084/api/v1/comment', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify(newComment),
      });

      if (response.ok) {
        setCommentContent('');
        loadComments(); // Recarrega os comentários
      } else {
        console.error('Failed to create comment');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const handlePostDelete = async () => {
    try {
      const response = await fetch(`http://localhost:8083/api/v1/post/${post.id}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (response.ok) {
        console.log('Post deleted successfully');
        onPostDelete(); // Chama a função de callback após a exclusão
        navigate('/posts'); // Redireciona para a lista de posts
      } else {
        console.error('Failed to delete post');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const handleCommentDelete = async (commentId) => {
    try {
      const response = await fetch(`http://localhost:8084/api/v1/comment/${commentId}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (response.ok) {
        setComments((prevComments) => prevComments.filter(comment => comment.id !== commentId));
        console.log('Comment deleted successfully');
      } else {
        console.error('Failed to delete comment');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const loadComments = useCallback(async () => {
    try {
      const response = await fetch(`http://localhost:8084/api/v1/comment/post/${post.id}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (response.ok) {
        const commentsData = await response.json();
        setComments(commentsData);
      } else {
        console.error('Failed to fetch comments');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  }, [post.id]);

  useEffect(() => {
    loadComments();
  }, [loadComments]);

  const getRandomProfileImage = (userId) => `https://robohash.org/${userId}?set=set2`;

  return (
    <PostContainerStyled>
      {user.id === post.userResponse.id && (
        <DeleteButton onClick={handlePostDelete}>Excluir</DeleteButton>
      )}
      <PostUser>
        <PostUserImage src={getRandomProfileImage(post.userResponse.id)} alt="User profile" />
        <span>{post.userResponse.firstName} {post.userResponse.lastName}</span>
      </PostUser>
      <PostContent>{post.content}</PostContent>
      <PostDate>{formatDate(post.createdAt)}</PostDate>
      <CommentInput
        placeholder="Adicione um comentário..."
        value={commentContent}
        onChange={(e) => setCommentContent(e.target.value)}
      />
      <CommentButton onClick={handleCommentSubmit}>Comentar</CommentButton>
      <CommentsContainer>
        {comments.map(comment => (
          <Comment
            key={comment.id}
            user={{ ...comment.userResponse, profilePicture: getRandomProfileImage(comment.userResponse.id) }}
            content={comment.content}
            date={formatDate(comment.createAt)}
            showDeleteButton={user.id === post.userResponse.id || user.id === comment.userResponse.id} // Passa se o botão de deletar deve ser exibido
            onDelete={() => handleCommentDelete(comment.id)}
          />
        ))}
      </CommentsContainer>
    </PostContainerStyled>
  );
};

export default Post;
