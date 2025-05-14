import { useState, useEffect } from 'react';
import { createOrder } from '../services/api';
import { useAuth } from '../services/AuthContext';
import { useNavigate } from 'react-router-dom';
import './styles.css';

function UserDashboard() {
  const [restaurants, setRestaurants] = useState([]);
  const [selectedRestaurant, setSelectedRestaurant] = useState(null);
  const [dishes, setDishes] = useState([]);
  const [selectedDishes, setSelectedDishes] = useState([]);
  const [orderSuccess, setOrderSuccess] = useState(false);
  const { currentUser, logout } = useAuth();
  const navigate = useNavigate();

  // Mock data for restaurants - replace with actual API call
  useEffect(() => {
    // Fetch restaurants from API
    setRestaurants([
      { id: 1, name: "Mom's Kitchen", cuisine: "Home Cooking" },
      { id: 2, name: "Taste of India", cuisine: "Indian" },
      { id: 3, name: "Homemade Italian", cuisine: "Italian" }
    ]);
  }, []);

  useEffect(() => {
    // Fetch restaurant's dishes when a restaurant is selected
    if (selectedRestaurant) {
      // Replace with actual API call
      setDishes([
        { id: 1, name: "Spaghetti Bolognese", price: 12.99, description: "Homemade pasta with rich meat sauce" },
        { id: 2, name: "Lasagna", price: 14.99, description: "Layered pasta with cheese and meat sauce" },
        { id: 3, name: "Garlic Bread", price: 4.99, description: "Freshly baked bread with garlic butter" }
      ]);
    }
  }, [selectedRestaurant]);

  const handleRestaurantSelect = (restaurant) => {
    setSelectedRestaurant(restaurant);
    setSelectedDishes([]);
  };

  const handleDishToggle = (dish) => {
    if (selectedDishes.find(d => d.id === dish.id)) {
      setSelectedDishes(selectedDishes.filter(d => d.id !== dish.id));
    } else {
      setSelectedDishes([...selectedDishes, { ...dish, quantity: 1 }]);
    }
  };

  const handleQuantityChange = (dishId, quantity) => {
    setSelectedDishes(
      selectedDishes.map(dish => 
        dish.id === dishId ? { ...dish, quantity } : dish
      )
    );
  };

  const handleCreateOrder = async () => {
    if (!selectedDishes.length || !selectedRestaurant) return;
    
    try {
      const orderData = {
        restaurantId: selectedRestaurant.id,
        userId: currentUser.id,
        items: selectedDishes.map(dish => ({
          dishId: dish.id,
          quantity: dish.quantity
        }))
      };
      
      await createOrder(orderData);
      setOrderSuccess(true);
      setSelectedDishes([]);
      
      // Reset success message after 3 seconds
      setTimeout(() => {
        setOrderSuccess(false);
      }, 3000);
    } catch (err) {
      console.error('Failed to create order:', err);
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <h1>Welcome, {currentUser?.username || 'User'}</h1>
        <button onClick={handleLogout} className="logout-button">Logout</button>
      </header>

      <div className="dashboard-content">
        <div className="restaurants-section">
          <h2>Available Restaurants</h2>
          <div className="restaurant-list">
            {restaurants.map(restaurant => (
              <div 
                key={restaurant.id} 
                className={`restaurant-card ${selectedRestaurant?.id === restaurant.id ? 'selected' : ''}`}
                onClick={() => handleRestaurantSelect(restaurant)}
              >
                <h3>{restaurant.name}</h3>
                <p>{restaurant.cuisine} Cuisine</p>
              </div>
            ))}
          </div>
        </div>

        {selectedRestaurant && (
          <div className="dishes-section">
            <h2>Dishes from {selectedRestaurant.name}</h2>
            <div className="dish-list">
              {dishes.map(dish => {
                const isSelected = selectedDishes.some(d => d.id === dish.id);
                const selectedDish = selectedDishes.find(d => d.id === dish.id);
                
                return (
                  <div key={dish.id} className={`dish-card ${isSelected ? 'selected' : ''}`}>
                    <div className="dish-info">
                      <h3>{dish.name}</h3>
                      <p>{dish.description}</p>
                      <p className="dish-price">${dish.price}</p>
                    </div>
                    <div className="dish-actions">
                      <button 
                        onClick={() => handleDishToggle(dish)}
                        className={isSelected ? 'remove-dish' : 'add-dish'}
                      >
                        {isSelected ? 'Remove' : 'Add to Order'}
                      </button>
                      
                      {isSelected && (
                        <div className="quantity-selector">
                          <button 
                            onClick={() => handleQuantityChange(dish.id, Math.max(1, selectedDish.quantity - 1))}
                            disabled={selectedDish.quantity <= 1}
                          >
                            -
                          </button>
                          <span>{selectedDish.quantity}</span>
                          <button 
                            onClick={() => handleQuantityChange(dish.id, selectedDish.quantity + 1)}
                          >
                            +
                          </button>
                        </div>
                      )}
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        )}

        {selectedDishes.length > 0 && (
          <div className="order-summary">
            <h2>Your Order</h2>
            <ul className="order-items">
              {selectedDishes.map(dish => (
                <li key={dish.id}>
                  <span>{dish.quantity}x {dish.name}</span>
                  <span>${(dish.price * dish.quantity).toFixed(2)}</span>
                </li>
              ))}
            </ul>
            <div className="order-total">
              <span>Total:</span>
              <span>
                ${selectedDishes.reduce((sum, dish) => sum + dish.price * dish.quantity, 0).toFixed(2)}
              </span>
            </div>
            <button onClick={handleCreateOrder} className="create-order-button">
              Place Order
            </button>
            
            {orderSuccess && (
              <div className="success-message">
                Your order has been placed successfully!
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}

export default UserDashboard;
