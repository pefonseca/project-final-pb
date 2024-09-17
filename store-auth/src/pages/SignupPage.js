import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import Navbar from '../components/Navbar';
import { useAuth } from '../contexts/AuthContext'; // Importe o hook useAuth

const SignupContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background: linear-gradient(135deg, #f5f7fa, #c3cfe2); // Substitua pelo gradiente da Home
`;

const Title = styled.h2`
  color: #333;
  font-size: 24px;
  margin-bottom: 20px;
`;

const Input = styled.input`
  width: 300px;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 16px;
`;

const Button = styled.button`
  width: 320px;
  padding: 10px;
  background-color: #ff9900;
  border: none;
  border-radius: 4px;
  color: white;
  font-size: 18px;
  cursor: pointer;
  margin-top: 10px;

  &:hover {
    background-color: #e68a00;
  }
`;

const SignupPage = () => {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [bio, setBio] = useState('');
  const [city, setCity] = useState('');
  const navigate = useNavigate(); // Para navegar entre as páginas
  const { login } = useAuth(); // Usa o hook useAuth para acessar a função de login

  const handleSignup = async () => {
    const requestBody = {
      firstname: firstName,
      lastname: lastName,
      email: email,
      password: password,
      bio: bio,
      city: city,
    };

    try {
      const response = await fetch('http://localhost:8080/api/v1/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        body: JSON.stringify(requestBody),
      });

      if (response.ok) {
        const data = await response.json();
        login(data.token); // Usa a função de login do contexto
        console.log('Stored Token:', localStorage.getItem('token'));
        navigate('/profile'); // Navega para a tela de perfil
      } else {
        console.error('Erro ao registrar', response.status);
      }
    } catch (error) {
      console.error('Erro na requisição', error);
    }
  };

  return (
    <>
      <Navbar />
      <SignupContainer>
        <Title>Cadastro</Title>
        <Input
          type="text"
          placeholder="Nome"
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
        />
        <Input
          type="text"
          placeholder="Sobrenome"
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
        />
        <Input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <Input
          type="password"
          placeholder="Senha"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <Input
          type="text"
          placeholder="Biografia"
          value={bio}
          onChange={(e) => setBio(e.target.value)}
        />
        <Input
          type="text"
          placeholder="Cidade"
          value={city}
          onChange={(e) => setCity(e.target.value)}
        />
        <Button onClick={handleSignup}>Cadastrar</Button>
      </SignupContainer>
    </>
  );
};

export default SignupPage;
