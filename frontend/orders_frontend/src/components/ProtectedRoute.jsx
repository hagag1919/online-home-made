import { useAuth } from '../services/AuthContext';
import { Navigate } from 'react-router-dom';

function ProtectedRoute({ children, requiredUserType }) {
  const { currentUser, userType } = useAuth();

  // If no user is logged in, redirect to login
  if (!currentUser) {
    return <Navigate to="/login" replace />;
  }

  // If a specific user type is required and doesn't match, redirect
  if (requiredUserType && userType !== requiredUserType) {
    return <Navigate to={userType === 'user' ? '/user-dashboard' : '/seller-dashboard'} replace />;
  }

  return children;
}

export default ProtectedRoute;
