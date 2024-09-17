import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';

const EditProfileContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 50px;
  background: linear-gradient(135deg, #f5f7fa, #c3cfe2);
  height: 100vh;
`;

const EditProfileBox = styled.div`
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  width: 60%;
`;

const Title = styled.h2`
  color: #333;
  font-size: 24px;
  margin-bottom: 20px;
  text-align: center;
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
`;

const Label = styled.label`
  font-size: 16px;
  color: #555;
  margin-bottom: 8px;
`;

const Input = styled.input`
  font-size: 16px;
  padding: 10px;
  margin-bottom: 20px;
  border: 1px solid #ddd;
  border-radius: 4px;
`;

const TextArea = styled.textarea`
  font-size: 16px;
  padding: 10px;
  margin-bottom: 20px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: none;
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: space-between;
`;

const SaveButton = styled.button`
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background-color: #45a049;
  }
`;

const CancelButton = styled.button`
  background-color: #f44336;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background-color: #da190b;
  }
`;

const EditProfilePage = () => {
  const [user, setUser] = useState({
    id: '',
    firstName: '',
    lastName: '',
    email: '',
    city: '',
    bio: ''
  });

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

        if (response.ok) {
          const data = await response.json();
          console.log('Response Data:', data); // Imprime a resposta completa

          // Atualize a desestruturação para refletir a estrutura correta da resposta
          if (data) {
            setUser({
              id: data.id || '',
              firstName: data.firstName || '',
              lastName: data.lastName || '',
              email: data.email || '',
              city: data.city || '',
              bio: data.bio || '',
            });
          } else {
            console.error('User response is undefined or missing');
          }
        } else {
          console.error('Failed to fetch user data');
        }
      } catch (error) {
        console.error('Error:', error);
      }
    };

    fetchUserData();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUser((prevUser) => ({ ...prevUser, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch(`http://localhost:8081/api/v1/user/${user.id}`, {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          firstName: user.firstName,
          lastName: user.lastName,
          email: user.email,
          bio: user.bio,
          city: user.city,
        }),
      });

      if (response.ok) {
        console.log('Profile updated successfully');
        navigate('/profile');
      } else {
        console.error('Failed to update profile');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const handleCancel = () => {
    navigate('/profile');
  };

  return (
    <>
      <Navbar user={user} />
      <EditProfileContainer>
        <EditProfileBox>
          <Title>Editar Perfil</Title>
          <Form onSubmit={handleSubmit}>
            <Label htmlFor="firstName">Nome:</Label>
            <Input
              type="text"
              id="firstName"
              name="firstName"
              value={user.firstName}
              onChange={handleInputChange}
              required
            />

            <Label htmlFor="lastName">Sobrenome:</Label>
            <Input
              type="text"
              id="lastName"
              name="lastName"
              value={user.lastName}
              onChange={handleInputChange}
              required
            />

            <Label htmlFor="email">Email:</Label>
            <Input
              type="email"
              id="email"
              name="email"
              value={user.email}
              onChange={handleInputChange}
              required
            />

            <Label htmlFor="city">Cidade:</Label>
            <Input
              type="text"
              id="city"
              name="city"
              value={user.city}
              onChange={handleInputChange}
            />

            <Label htmlFor="bio">Biografia:</Label>
            <TextArea
              id="bio"
              name="bio"
              rows="4"
              value={user.bio}
              onChange={handleInputChange}
            />

            <ButtonContainer>
              <SaveButton type="submit">Salvar</SaveButton>
              <CancelButton type="button" onClick={handleCancel}>Cancelar</CancelButton>
            </ButtonContainer>
          </Form>
        </EditProfileBox>
      </EditProfileContainer>
    </>
  );
};

export default EditProfilePage;
