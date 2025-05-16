import { BrowserRouter as Router, Routes, Route, Navigate, useLocation } from 'react-router-dom'
import { AuthProvider, useAuth } from './services/AuthContext'
import Login from './pages/Login'
import AdminDashboard from './pages/AdminDashboard'
import { useEffect, useState } from 'react'
import './App.css'

// AnimatedRoutes component to handle transitions
const AnimatedRoutes = ({ children }) => {
  const location = useLocation();
  const [displayLocation, setDisplayLocation] = useState(location);
  const [transitionStage, setTransitionStage] = useState("fadeIn");

  useEffect(() => {
    if (location !== displayLocation) {
      setTransitionStage("fadeOut");
    }
  }, [location, displayLocation]);

  return (
    <div 
      className={`route-transition ${transitionStage}`}
      onAnimationEnd={() => {
        if (transitionStage === "fadeOut") {
          setTransitionStage("fadeIn");
          setDisplayLocation(location);
        }
      }}
    >
      {children}
    </div>
  );
};

// Protected route component
const ProtectedRoute = ({ children }) => {
  const { currentUser } = useAuth();
  
  if (!currentUser) {
    return <Navigate to="/login" replace />;
  }
  
  return children;
};

// Auth check component to redirect based on login status
const AuthCheck = () => {
  const { currentUser } = useAuth();
  
  if (currentUser) {
    return <Navigate to="/dashboard" replace />;
  } else {
    return <Navigate to="/login" replace />;
  }
};

function AppRoutes() {
  return (
    <AnimatedRoutes>
      <Routes>
        {/* Root path - checks auth and redirects accordingly */}
        <Route path="/" element={<AuthCheck />} />
        
        {/* Login page - accessible when not logged in */}
        <Route path="/login" element={<Login />} />
        
        {/* Protected admin dashboard - only accessible when logged in */}
        <Route 
          path="/dashboard" 
          element={
            <ProtectedRoute>
              <AdminDashboard />
            </ProtectedRoute>
          } 
        />
        
        {/* Fallback for unknown routes */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </AnimatedRoutes>
  );
}

function App() {
  return (
    <Router>
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </Router>
  );
}

export default App
