import { useState, useEffect } from 'react';
import { createOrder, getAllRestaurants, getDishesByRestaurantId } from '../services/api';
import { useAuth } from '../services/AuthContext';
import { useNavigate } from 'react-router-dom';
import './styles.css';

function UserDashboard() {
  const [restaurants, setRestaurants] = useState([]);
  const [selectedRestaurant, setSelectedRestaurant] = useState(null);
  const [dishes, setDishes] = useState([]);
  const [selectedDishes, setSelectedDishes] = useState([]);
  const [orderSuccess, setOrderSuccess] = useState(false);
  const [orderError, setOrderError] = useState(false);
  const [loading, setLoading] = useState(true);
  const { currentUser, logout } = useAuth();
  const navigate = useNavigate();

  // Fetch real restaurants data from API
  useEffect(() => {
    const fetchRestaurants = async () => {
      try {
        const response = await getAllRestaurants();
        setRestaurants(response.data.map(restaurant => ({
          ...restaurant // Adding default cuisine as it's used in UI
        })));
        setLoading(false);
      } catch (error) {
        console.error('Failed to fetch restaurants:', error);
        setLoading(false);
      }
    };
    
    fetchRestaurants();
  }, []);

  useEffect(() => {
    // Fetch restaurant's dishes when a restaurant is selected
    if (selectedRestaurant) {
      const fetchDishes = async () => {
        try {
          const response = await getDishesByRestaurantId(selectedRestaurant.id);
          setDishes(response.data);
        } catch (error) {
          console.error('Failed to fetch dishes:', error);
        }
      };
      
      fetchDishes();
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
      // Only add dish if amount is greater than 0
      if (dish.amount > 0) {
        setSelectedDishes([...selectedDishes, { ...dish, quantity: 1 }]);
      }
    }
  };

  const handleQuantityChange = (dishId, quantity) => {
    // Find the dish in the dishes array to check its amount
    const dish = dishes.find(d => d.id === dishId);
    // Only update if the new quantity is within the available amount
    if (dish && quantity <= dish.amount) {
      setSelectedDishes(
        selectedDishes.map(d => 
          d.id === dishId ? { ...d, quantity } : d
        )
      );
    }
  };

  const handleCreateOrder = async () => {
    if (!selectedDishes.length || !selectedRestaurant) return;
    
    try {
      // Calculate total price
      const totalPrice = selectedDishes.reduce(
        (sum, dish) => sum + (dish.price * dish.quantity), 
        0
      );
      
      const orderData = {
        userID: currentUser?.id || 1, // Use currentUser.id if available, fallback to 1
        restaurantID: selectedRestaurant.id,
        orderDishes: selectedDishes.map(dish => ({
          dishId: dish.id,
          dishName: dish.name,
          quantity: dish.quantity,
          unitPrice: dish.price
        })),
        destination: currentUser?.address || "123 Main Street", // You may want to add address to user profile
        shippingCompany: "FastDelivery", // This could be a selection in the future
        totalPrice: totalPrice
      };
      
      const response = await createOrder(orderData);
      
      if (response.status === 202) {
        setOrderSuccess(true);
        setSelectedDishes([]);
        
        // Reset success message after 3 seconds
        setTimeout(() => {
          setOrderSuccess(false);
        }, 3000);
      } else {
        console.error('Order was not accepted:', response);
        setOrderError(true);
        setTimeout(() => {
          setOrderError(false);
        }, 3000);
      }
    } catch (err) {
      console.error('Failed to create order:', err);
      setOrderError(true);
      setTimeout(() => {
        setOrderError(false);
      }, 3000);
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
            {dishes.length > 0 ? (
              <div className="dish-list">
                {dishes.map(dish => {
                  const isSelected = selectedDishes.some(d => d.id === dish.id);
                  const selectedDish = selectedDishes.find(d => d.id === dish.id);
                  
                  return (
                    <div key={dish.id} className={`dish-card ${isSelected ? 'selected' : ''} ${dish.amount <= 0 ? 'out-of-stock' : ''}`}>
                      <div className="dish-info">
                        <h3>{dish.name}</h3>
                        <p>{dish.description}</p>
                        <p className="dish-price">${dish.price}</p>
                        <p className="dish-availability">
                          {dish.amount > 0 
                            ? `Available: ${dish.amount}` 
                            : <span className="out-of-stock-text">Out of stock</span>
                          }
                        </p>
                      </div>
                      <div className="dish-actions">
                        <button 
                          onClick={() => handleDishToggle(dish)}
                          className={isSelected ? 'remove-dish' : 'add-dish'}
                          disabled={!isSelected && dish.amount <= 0}
                        >
                          {isSelected ? 'Remove' : 'Add to Order'}
                        </button>
                        
                        {isSelected && (
                          <div className="quantity-selector">
                            <button 
                              onClick={() => handleQuantityChange(dish.id, Math.max(1, selectedDish.quantity - 1))}
                              disabled={selectedDish.quantity <= 1}
                              aria-label="Decrease quantity"
                            >
                              <span style={{color:"black"}}>-</span>
                            </button>
                            <span>{selectedDish.quantity}</span>
                            <button 
                              onClick={() => handleQuantityChange(dish.id, selectedDish.quantity + 1)}
                              disabled={selectedDish.quantity >= dish.amount}
                              aria-label="Increase quantity"
                            >
                              <span style={{color:"black"}}>+</span>
                            </button>
                          </div>
                        )}
                        {isSelected && selectedDish.quantity === dish.amount && (
                          <p className="max-quantity-warning">Maximum available reached</p>
                        )}
                      </div>
                    </div>
                  );
                })}
              </div>
            ) : (
              <div className="no-dishes-message">
                <p>This restaurant doesn't offer any dishes at the moment.</p>
              </div>
            )}
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
                Your order has been placed successfully! Your food is being prepared.
              </div>
            )}
            
            {orderError && (
              <div className="error-message">
                Failed to place order. Please try again later.
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}

export default UserDashboard;
