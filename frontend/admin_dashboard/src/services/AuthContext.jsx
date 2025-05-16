import { createContext, useContext, useState, useEffect } from 'react';
import { loginAdmin } from './api';

const AuthContext = createContext();

export function useAuth() {
  return useContext(AuthContext);
}

export function AuthProvider({ children }) {
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Check if user is stored in localStorage
    const storedUser = localStorage.getItem('adminUser');
    if (storedUser) {
      setCurrentUser(JSON.parse(storedUser));
    }
    setLoading(false);
  }, []);

  // Login function with hardcoded credentials
  const login = async (credentials) => {
    try {
      // Hardcoded admin credentials check
      if (credentials.username === 'admin' && credentials.password === 'admin') {
        const user = {
          username: 'admin',
          role: 'administrator'
        };
        
        // Store user in state and localStorage
        setCurrentUser(user);
        localStorage.setItem('adminUser', JSON.stringify(user));
        return user;
      } else {
        throw new Error("Invalid credentials");
      }
    } catch (error) {
      console.error("Login failed:", error);
      throw error;
    }
  };

  // Logout function
  const logout = () => {
    setCurrentUser(null);
    localStorage.removeItem('adminUser');
  };

  const value = {
    currentUser,
    login,
    logout
  };

  return (
    <AuthContext.Provider value={value}>
      {!loading && children}
    </AuthContext.Provider>
  );
}
