import React, { createContext, useState, useContext, useEffect } from 'react';

// Cria o contexto de autenticação
const AuthContext = createContext();

// Provedor do contexto de autenticação
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  // Função para buscar dados do usuário
  const fetchUser = async () => {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const response = await fetch('http://localhost:8080/api/v1/auth/profile', {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });

        if (response.ok) {
          const data = await response.json();
          setUser(data);
        } else {
          console.error('Failed to fetch user data');
          setUser(null); // Reseta o usuário se a busca falhar
        }
      } catch (error) {
        console.error('Error fetching user data:', error);
        setUser(null); // Reseta o usuário se ocorrer um erro
      }
    } else {
      setUser(null); // Reseta o usuário se não houver token
    }
  };

  useEffect(() => {
    fetchUser(); // Busca os dados do usuário ao montar o componente
  }, []);

  const login = (token) => {
    localStorage.setItem('token', token);
    fetchUser(); // Atualiza os dados do usuário após login
  };

  const logout = () => {
    localStorage.removeItem('token');
    setUser(null); // Reseta o usuário ao sair
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

// Hook para usar o contexto de autenticação
export const useAuth = () => useContext(AuthContext);
