import React from 'react';
import styled from 'styled-components';

const CommentContainerStyled = styled.div`
  background-color: #f9f9f9;
  padding: 10px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 10px;
  position: relative;
`;

const CommentUser = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 5px;
`;

const CommentUserImage = styled.img`
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-right: 10px;
`;

const CommentContent = styled.p`
  font-size: 14px;
  color: #555;
`;

const CommentDate = styled.p`
  font-size: 12px;
  color: #888;
`;

const DeleteButton = styled.button`
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: #ff4d4d;
  border: none;
  border-radius: 4px;
  color: white;
  font-size: 12px;
  cursor: pointer;
  padding: 5px 10px;

  &:hover {
    background-color: #e60000;
  }
`;

const Comment = ({ user, content, date, showDeleteButton, onDelete }) => {
  // Gera uma URL aleatória para o perfil
  const getRandomProfileImage = () => `https://robohash.org/${user.id}?set=set2`;

  return (
    <CommentContainerStyled>
      {showDeleteButton && <DeleteButton onClick={onDelete}>Excluir</DeleteButton>}
      <CommentUser>
        <CommentUserImage src={getRandomProfileImage()} alt="User profile" />
        <span>{user.firstName} {user.lastName}</span>
      </CommentUser>
      <CommentContent>{content}</CommentContent>
      <CommentDate>{date}</CommentDate>
    </CommentContainerStyled>
  );
};

export default Comment;
