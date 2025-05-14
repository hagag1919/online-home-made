import { useNavigate } from 'react-router-dom';
import { useAuth } from '../services/AuthContext';
import './styles.css';

function LandingPage() {
  const navigate = useNavigate();
  const { currentUser } = useAuth();

  const handleNavigate = (path) => {
    navigate(path);
  };

  return (
    <div className="landing-container">
      <div className="landing-content">
        <div className="landing-header">
          <h1>Home-Made Food Delivery</h1>
          <p className="landing-tagline">Authentic home-cooked meals from local chefs to your doorstep</p>
        </div>

        <div className="landing-features">
          <div className="feature-card">
            <div className="feature-icon">🍲</div>
            <h3>Authentic Cuisine</h3>
            <p>Enjoy genuine home-cooked meals made with love and tradition</p>
          </div>
          
          <div className="feature-card">
            <div className="feature-icon">👨‍🍳</div>
            <h3>Local Chefs</h3>
            <p>Support talented home cooks in your community</p>
          </div>
          
          <div className="feature-card">
            <div className="feature-icon">🚚</div>
            <h3>Fast Delivery</h3>
            <p>Hot and fresh meals delivered right to your door</p>
          </div>
        </div>

        <div className="landing-cta">
          {currentUser ? (
            <button 
              className="cta-button"
              onClick={() => handleNavigate(currentUser.userType === 'seller' ? '/seller-dashboard' : '/user-dashboard')}
            >
              Go to Dashboard
            </button>
          ) : (
            <div className="cta-buttons">
              <button 
                className="cta-button"
                onClick={() => handleNavigate('/register')}
              >
                Register
              </button>
              <button 
                className="cta-button secondary"
                onClick={() => handleNavigate('/login')}
              >
                Login
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default LandingPage;
