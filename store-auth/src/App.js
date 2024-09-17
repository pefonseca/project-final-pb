import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage';
import UserProfilePage from './pages/UserProfilePage';
import HomePage from './pages/HomePage';
import EditProfilePage from './pages/EditProfilePage';
import PostPage from './pages/PostPage'; // Importe a nova página de postagens
import { AuthProvider } from './contexts/AuthContext'; // Importe o AuthProvider

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignupPage />} />
          <Route path="/profile" element={<UserProfilePage />} />
          <Route path="/edit-profile" element={<EditProfilePage />} />
          <Route path="/posts" element={<PostPage />} /> {/* Adicione a nova rota */}
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
