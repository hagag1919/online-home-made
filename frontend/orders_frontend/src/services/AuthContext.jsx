import { createContext, useState, useContext, useEffect } from 'react';

const AuthContext = createContext();

export function useAuth() {
  return useContext(AuthContext);
}

export function AuthProvider({ children }) {
  const [currentUser, setCurrentUser] = useState(null);
  const [userType, setUserType] = useState(null);
  const [loading, setLoading] = useState(true);

  // Check for user in local storage on initial load
  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    const storedUserType = localStorage.getItem('userType');
    
    if (storedUser) {
      setCurrentUser(JSON.parse(storedUser));
      setUserType(storedUserType);
    }
    
    setLoading(false);
    
    // Add event listener to handle balance updates or other user data changes
    const handleStorageChange = () => {
      const updatedUser = localStorage.getItem('user');
      if (updatedUser) {
        try {
          const parsedUser = JSON.parse(updatedUser);
          setCurrentUser(parsedUser);
        } catch (error) {
          console.error('Failed to parse updated user data:', error);
        }
      }
    };
    
    window.addEventListener('storage', handleStorageChange);
    
    return () => {
      window.removeEventListener('storage', handleStorageChange);
    };
  }, []);

  const login = (user, type) => {
    setCurrentUser(user);
    setUserType(type);
    localStorage.setItem('user', JSON.stringify(user));
    localStorage.setItem('userType', type);
    
    // Store restaurant ID if this is a seller
    if (type === 'seller') {
      // Check different possible ID field names that might be in the response
      const restaurantId = user.id || user.restaurantId || user.restaurant_id;
      if (restaurantId) {
        console.log('Setting restaurant ID:', restaurantId);
        localStorage.setItem('restaurantId', restaurantId);
      } else {
        // If no ID field is found, try to use the whole user object as it might be just the ID
        console.log('Setting restaurant ID from user object:', user);
        localStorage.setItem('restaurantId', user);
      }
    }
  };

  const logout = () => {
    setCurrentUser(null);
    setUserType(null);
    localStorage.removeItem('user');
    localStorage.removeItem('userType');
  };

  const value = {
    currentUser,
    userType,
    login,
    logout,
    isUser: () => userType === 'user',
    isSeller: () => userType === 'seller',
  };

  return (
    <AuthContext.Provider value={value}>
      {!loading && children}
    </AuthContext.Provider>
  );
}
