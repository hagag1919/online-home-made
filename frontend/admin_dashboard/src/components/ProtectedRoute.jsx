import { Navigate } from 'react-router-dom';
import { useAuth } from '../services/AuthContext';

function ProtectedRoute({ children }) {
  const { currentUser } = useAuth();
  
  if (!currentUser) {
    return <Navigate to="/login" replace />;
  }
  
  return children;
}

export default ProtectedRoute;
