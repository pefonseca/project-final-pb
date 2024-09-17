import React from 'react';
import styled from 'styled-components';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext'; // Importe o hook useAuth

const Nav = styled.nav`
  background-color: #333;
  color: white;
  padding: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const NavLinks = styled.div`
  display: flex;
  gap: 15px;
`;

const NavLink = styled(Link)`
  color: white;
  text-decoration: none;
  font-size: 18px;

  &:hover {
    text-decoration: underline;
  }
`;

const UserInfo = styled.div`
  display: flex;
  align-items: center;
  gap: 10px;
`;

const LogoutButton = styled.button`
  background-color: #ff9900;
  border: none;
  border-radius: 4px;
  color: white;
  font-size: 16px;
  cursor: pointer;
  padding: 5px 10px;

  &:hover {
    background-color: #e68a00;
  }
`;

const ProfileButton = styled.button`
  background-color: #ff9900;
  border: none;
  border-radius: 4px;
  color: white;
  font-size: 16px;
  cursor: pointer;
  padding: 5px 10px;

  &:hover {
    background-color: #e68a00;
  }
`;

const Navbar = () => {
  const { user, logout } = useAuth(); // Usa o hook useAuth para acessar o contexto
  const navigate = useNavigate();

  const handleLogout = () => {
    logout(); // Chama a função de logout do contexto
    navigate('/login'); // Redireciona para a página de login
  };

  const handleProfileClick = () => {
    navigate('/profile'); // Redireciona para a página de perfil
  };

  return (
    <Nav>
      <NavLinks>
        <NavLink to="/">Home</NavLink>
        {user && <NavLink to="/posts">Postagens</NavLink>} {/* Exibe o link de postagens se o usuário estiver logado */}
      </NavLinks>
      <NavLinks>
        {user ? (
          <UserInfo>
            <span>{user.firstName} {user.lastName}</span> {/* Exibe o nome completo */}
            <ProfileButton onClick={handleProfileClick}>Perfil</ProfileButton> {/* Botão para a página de perfil */}
            <LogoutButton onClick={handleLogout}>Sair</LogoutButton>
          </UserInfo>
        ) : (
          <>
            <NavLink to="/login">Login</NavLink>
            <NavLink to="/signup">Cadastrar</NavLink>
          </>
        )}
      </NavLinks>
    </Nav>
  );
};

export default Navbar;
