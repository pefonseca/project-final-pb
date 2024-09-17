// src/pages/PostPage.js
import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import Navbar from '../components/Navbar';
import { useAuth } from '../contexts/AuthContext';
import Post from '../components/Post';

const PostContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa, #c3cfe2);
`;

const PostForm = styled.div`
  width: 60%;
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
`;

const PostInput = styled.textarea`
  width: 100%;
  height: 100px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 16px;
  margin-bottom: 10px;
`;

const PostButton = styled.button`
  background-color: #ff9900;
  border: none;
  border-radius: 4px;
  color: white;
  font-size: 16px;
  cursor: pointer;
  padding: 10px;

  &:hover {
    background-color: #e68a00;
  }
`;

const PostList = styled.div`
  width: 60%;
`;

const PostPage = () => {
  const [postContent, setPostContent] = useState('');
  const [posts, setPosts] = useState([]);
  const { user } = useAuth();

  // Função para submeter o post
  const handlePostSubmit = async () => {
    if (!postContent) return;

    const newPost = {
      userId: user.id,
      content: postContent,
    };

    try {
      const response = await fetch('http://localhost:8083/api/v1/post', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify(newPost),
      });

      if (response.ok) {
        setPostContent(''); // Limpa o campo de entrada
        fetchPosts(); // Recarrega os posts
      } else {
        console.error('Failed to create post');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  // Função para buscar posts existentes
  const fetchPosts = async () => {
    try {
      const response = await fetch('http://localhost:8083/api/v1/post', {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (response.ok) {
        const postsData = await response.json();
        setPosts(postsData);
      } else {
        console.error('Failed to fetch posts');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  // Carrega os posts quando o componente é montado
  useEffect(() => {
    fetchPosts();
  }, []);

  return (
    <>
      <Navbar />
      <PostContainer>
        <PostForm>
          <PostInput
            placeholder="O que você está pensando?"
            value={postContent}
            onChange={(e) => setPostContent(e.target.value)}
          />
          <PostButton onClick={handlePostSubmit}>Postar</PostButton>
        </PostForm>
        <PostList>
          {posts.map((post) => (
            <Post
              key={post.id}
              post={post}
              fetchComments={() => fetchPosts()}
              onPostDelete={fetchPosts} // Passa a função de atualizar posts
            />
          ))}
        </PostList>
      </PostContainer>
    </>
  );
};


export default PostPage;