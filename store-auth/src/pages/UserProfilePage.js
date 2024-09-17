import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';

const ProfileContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 50px;
  background: linear-gradient(135deg, #f5f7fa, #c3cfe2);
  height: 100vh;
`;

const ProfileBox = styled.div`
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  width: 60%;
  position: relative;
`;

const EditButton = styled.button`
  position: absolute;
  top: 20px;
  right: 20px;
  background-color: #ff9900;
  border: none;
  border-radius: 4px;
  color: white;
  font-size: 14px;
  padding: 10px;
  cursor: pointer;

  &:hover {
    background-color: #e68a00;
  }
`;

const Title = styled.h2`
  color: #333;
  font-size: 24px;
  margin-bottom: 20px;
`;

const Info = styled.p`
  color: #555;
  font-size: 18px;
  margin: 5px 0;
`;

const UserProfilePage = () => {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/v1/auth/profile', {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json',
          },
        });

        console.log('Response Status:', response.status);
        if (response.ok) {
          const data = await response.json();
          console.log('User Data:', data);
          setUser(data);
        } else {
          console.error('Failed to fetch user data');
        }
      } catch (error) {
        console.error('Error:', error);
      }
    };

    fetchUserData();
  }, []);

  const handleLogout = () => {
    setUser(null);
    localStorage.removeItem('token'); // Remove o token ao deslogar
    navigate('/login');
  };

  const handleEditProfile = () => {
    navigate('/edit-profile');
  };

  if (!user) {
    return <p>Loading...</p>;
  }

  return (
    <>
      <Navbar user={user} onLogout={handleLogout} />
      <ProfileContainer>
        <ProfileBox>
          <EditButton onClick={handleEditProfile}>Editar Perfil</EditButton>
          <Title>Perfil do Usuário</Title>
          <Info>Nome: {user.firstName} {user.lastName}</Info>
          <Info>Email: {user.email}</Info>
          <Info>Cidade: {user.city}</Info>
          <Info>Biografia: {user.bio}</Info>
        </ProfileBox>
      </ProfileContainer>
    </>
  );
};

export default UserProfilePage;