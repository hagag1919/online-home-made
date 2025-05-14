import { useState } from 'react';
import { loginUser, loginRestaurant } from '../services/api';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../services/AuthContext';
import './styles.css';

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [userType, setUserType] = useState('user'); // Default to user login
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    const credentials = { 
      username, 
      password 
    };

    try {
      let response;

      if (userType === 'user') {
        response = await loginUser(credentials);
        login(response.data, 'user');
        navigate('/user-dashboard');
      } else {
        // For restaurant login, use name instead of username
        response = await loginRestaurant({ 
          name: username, 
          password 
        });
        // Save restaurant data with its ID
        login(response.data, 'seller');
        navigate('/seller-dashboard');
      }
    } catch (err) {
      setError('Login failed: ' + (err.response?.data?.message || 'Invalid credentials'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-form-container">
        <h2>Login</h2>
        {error && <div className="error-message">{error}</div>}
        
        <div className="user-type-toggle">
          <button 
            className={userType === 'user' ? 'active' : ''}
            onClick={() => setUserType('user')}
          >
            User
          </button>
          <button 
            className={userType === 'seller' ? 'active' : ''}
            onClick={() => setUserType('seller')}
          >
            Seller
          </button>
        </div>
        
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <button type="submit" className="submit-button" disabled={loading}>
            {loading ? 'Logging in...' : 'Login'}
          </button>
        </form>
        <div className="auth-footer">
          Don't have an account? <a href="/register">Register here</a>
        </div>
      </div>
    </div>
  );
}

export default Login;
