import React from 'react';
import styled from 'styled-components';
import Navbar from '../components/Navbar';

const HomePageContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background: linear-gradient(135deg, #f5f7fa, #c3cfe2);
`;

const WelcomeMessage = styled.h1`
  font-size: 48px;
  color: #333;
  margin-bottom: 20px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
`;

const IllustrationContainer = styled.div`
  position: relative;
  width: 100%;
  max-width: 600px;
  height: 300px;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  overflow: hidden;
  margin-top: 20px;
`;

const Circle = styled.div`
  position: absolute;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: linear-gradient(135deg, #ff9a9e, #fad0c4);
  top: ${({ top }) => top || '50%'};
  left: ${({ left }) => left || '50%'};
  transform: translate(-50%, -50%);
  animation: float 6s ease-in-out infinite;

  @keyframes float {
    0%, 100% {
      transform: translate(-50%, -50%) translateY(0);
    }
    50% {
      transform: translate(-50%, -50%) translateY(-20px);
    }
  }
`;

const Square = styled.div`
  position: absolute;
  width: 120px;
  height: 120px;
  background: linear-gradient(135deg, #84fab0, #8fd3f4);
  top: ${({ top }) => top || '50%'};
  left: ${({ left }) => left || '50%'};
  transform: translate(-50%, -50%) rotate(45deg);
  animation: rotate 8s linear infinite;

  @keyframes rotate {
    0% {
      transform: translate(-50%, -50%) rotate(0deg);
    }
    100% {
      transform: translate(-50%, -50%) rotate(360deg);
    }
  }
`;

const Triangle = styled.div`
  position: absolute;
  width: 0;
  height: 0;
  border-left: 60px solid transparent;
  border-right: 60px solid transparent;
  border-bottom: 120px solid #fbc2eb;
  top: ${({ top }) => top || '50%'};
  left: ${({ left }) => left || '50%'};
  transform: translate(-50%, -50%);
  animation: bounce 5s ease-in-out infinite;

  @keyframes bounce {
    0%, 100% {
      transform: translate(-50%, -50%) translateY(0);
    }
    50% {
      transform: translate(-50%, -50%) translateY(-30px);
    }
  }
`;

const HomePage = () => {
  return (
    <>
      <Navbar />
      <HomePageContainer>
        <WelcomeMessage>Bem-vindo à Nossa Aplicação!</WelcomeMessage>
        <IllustrationContainer>
          <Circle top="30%" left="30%" />
          <Square top="70%" left="70%" />
          <Triangle top="50%" left="50%" />
        </IllustrationContainer>
      </HomePageContainer>
    </>
  );
};

export default HomePage;
