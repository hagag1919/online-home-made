import { useState, useEffect, useCallback } from 'react';
import { createOrder, getAllRestaurants, getDishesByRestaurantId, getUserOrderHistory, getOrderDishesByOrderID, getOrderStatusUpdates } from '../services/api';
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
  const [showOrderHistory, setShowOrderHistory] = useState(false);
  const [orderHistory, setOrderHistory] = useState([]);
  const [orderHistoryLoading, setOrderHistoryLoading] = useState(false);
  const [expandedOrderId, setExpandedOrderId] = useState(null);
  const [orderDishes, setOrderDishes] = useState([]);
  const [loadingOrderDishes, setLoadingOrderDishes] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const [notificationsLoading, setNotificationsLoading] = useState(false);
  const [notificationCount, setNotificationCount] = useState(0);
  // Track which notifications have been read
  const [readNotifications, setReadNotifications] = useState(() => {
    // Try to get read notification ids from localStorage
    const savedIds = localStorage.getItem('readNotificationIds');
    if (savedIds) {
      try {
        return new Set(JSON.parse(savedIds));
      } catch (e) {
        console.error('Failed to parse saved notifications:', e);
        return new Set();
      }
    }
    return new Set();
  });
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
  
  // Helper function to format timestamps in a user-friendly way
  const formatTimestamp = (timestamp) => {
    const date = new Date(timestamp);
    return date.toLocaleString('en-US', {
      weekday: 'short',
      month: 'short', 
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };
  
  const fetchOrderHistory = async () => {
    if (!currentUser?.id) return;
    
    setOrderHistoryLoading(true);
    try {
      const response = await getUserOrderHistory(currentUser.id);
      setOrderHistory(response.data);
      setShowOrderHistory(true);
    } catch (error) {
      console.error('Failed to fetch order history:', error);
    } finally {
      setOrderHistoryLoading(false);
    }
  };
  
  const toggleOrderDetails = async (orderId) => {
    console.log(`Toggling order details for order ID: ${orderId}`);
    
    if (expandedOrderId === orderId) {
      // If already expanded, collapse it
      console.log('Collapsing order details');
      setExpandedOrderId(null);
      setOrderDishes([]);
      return;
    }
    
    console.log('Expanding order details');
    setExpandedOrderId(orderId);
    setLoadingOrderDishes(true);
    
    try {
      console.log(`Fetching dishes for order ID: ${orderId}`);
      const response = await getOrderDishesByOrderID(orderId);
      console.log('Order dishes response:', response);
      setOrderDishes(response.data);
    } catch (error) {
      console.error('Failed to fetch order dishes:', error);
      setOrderDishes([]);
    } finally {
      setLoadingOrderDishes(false);
    }
  };

  const fetchNotifications = useCallback(async (isBackgroundRefresh = false) => {
    console.log(`fetchNotifications called - background refresh: ${isBackgroundRefresh}`);
    
    if (!currentUser?.id) {
      console.log('No user ID available, skipping notification fetch');
      return;
    }
    
    // Only show loading state if it's not a background refresh
    if (!isBackgroundRefresh) {
      console.log('Setting loading state to true');
      setNotificationsLoading(true);
    }
    
    try {
      console.log(`Fetching notifications for user ID: ${currentUser.id}`);
      const response = await getOrderStatusUpdates(currentUser.id);
      console.log('Notifications response:', response?.data);
      
      // Validate that we have data in the response
      if (response?.data) {
        console.log(`Received ${response.data.length} notifications`);
        setNotifications(response.data);
      
        // Only toggle visibility if it's not a background refresh
        if (!isBackgroundRefresh) {
          console.log('Showing notifications modal');
          setShowNotifications(true);
          
          // Mark all notifications as read when opening the modal
          console.log('Marking notifications as read');
          const newReadNotifications = new Set([...readNotifications]);
          response.data.forEach(notification => {
            if (notification.id) {
              newReadNotifications.add(notification.id);
              console.log(`Marked notification ${notification.id} as read`);
            } else {
              console.warn('Found notification without ID:', notification);
            }
          });
          
          // Update state and persist to localStorage
          setReadNotifications(newReadNotifications);
          localStorage.setItem('readNotificationIds', JSON.stringify([...newReadNotifications]));
          console.log('Saved read status to localStorage');
          
          // Reset notification count when opening modal
          console.log('Resetting notification count');
          setNotificationCount(0);
        }
      } else {
        console.log('No data received in response');
        if (!isBackgroundRefresh) {
          // Still show the modal with empty state
          setShowNotifications(true);
          setNotifications([]);
        }
      }
    } catch (error) {
      console.error('Failed to fetch notifications:', error);
      if (!isBackgroundRefresh) {
        // Show modal with error state
        setShowNotifications(true);
        setNotifications(null); // Set to null to indicate error state
      }
    } finally {
      if (!isBackgroundRefresh) {
        console.log('Setting loading state to false');
        setNotificationsLoading(false);
      }
    }
  }, [currentUser, readNotifications]);

  // Check for notifications periodically
  useEffect(() => {
    let intervalId;
    let retryCount = 0;
    const MAX_RETRIES = 3;
    
    const checkNotifications = async () => {
      if (!currentUser?.id) {
        console.log('No user ID available, skipping notification check');
        return;
      }
      
      try {
        console.log(`Checking notifications for user ID: ${currentUser.id}`);
        const response = await getOrderStatusUpdates(currentUser.id);
        
        // Reset retry count on successful fetch
        retryCount = 0;
        
        if (response && response.data) {
          console.log(`Retrieved ${response.data.length} notifications`);
          console.log('Current read notifications:', [...readNotifications]);
          
          // Validate notification data
          const validNotifications = response.data.filter(n => n && n.id);
          if (validNotifications.length < response.data.length) {
            console.warn(`Found ${response.data.length - validNotifications.length} notifications with missing IDs`);
          }
          
          // Only show count when notifications modal is not open
          if (!showNotifications) {
            // Count only unread notifications
            const unreadCount = validNotifications.filter(
              notification => !readNotifications.has(notification.id)
            ).length;
            
            console.log(`Found ${unreadCount} unread notifications out of ${validNotifications.length} valid notifications`);
            
            if (unreadCount > 0) {
              console.log(`Setting notification count to ${unreadCount}`);
              setNotificationCount(unreadCount);
            } else if (notificationCount > 0) {
              // Reset only if we previously had notifications
              console.log('Resetting notification count to 0');
              setNotificationCount(0);
            }
          }
        } else {
          console.log('No notifications data in response');
        }
      } catch (error) {
        console.error('Failed to check notifications:', error);
        retryCount++;
        
        if (retryCount >= MAX_RETRIES) {
          console.warn(`Failed to check notifications ${MAX_RETRIES} times in a row. Will try again on next interval.`);
          retryCount = 0;
        }
      }
    };
    
    // Check notifications on component mount
    console.log('Setting up notification check interval');
    checkNotifications();
    
    // Set up interval for periodic checks
    intervalId = setInterval(checkNotifications, 60000); // Check every minute
    
    return () => {
      if (intervalId) {
        console.log('Cleaning up notification check interval');
        clearInterval(intervalId);
      }
    };
  }, [currentUser, showNotifications, readNotifications]);

  // Auto-refresh notifications every 30 seconds when modal is open
  useEffect(() => {
    let intervalId;
    
    if (showNotifications && currentUser?.id) {
      // Do an immediate refresh when the modal opens
      fetchNotifications(true);
      
      // Set up interval for periodic refresh
      intervalId = setInterval(() => {
        console.log('Auto-refreshing notifications');
        fetchNotifications(true); // Pass true for background refresh
      }, 30000); // 30 seconds
    }
    
    // Clean up interval when modal closes or component unmounts
    return () => {
      if (intervalId) {
        clearInterval(intervalId);
      }
    };
  }, [showNotifications, currentUser, fetchNotifications]);

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <h1>Welcome, {currentUser?.username || 'User'}</h1>
        <div className="header-buttons">
          <button 
            onClick={() => {
              // First set the modal to visible, then fetch notifications
              setShowNotifications(true);
              fetchNotifications();
            }}
            className="notifications-button"
            aria-label={`Notifications ${notificationCount > 0 ? `(${notificationCount} new)` : ''}`}
          >
            Notifications
            {notificationCount > 0 && (
              <span className="notification-count" title={`${notificationCount} unread notifications`}>
                {notificationCount > 99 ? '99+' : notificationCount}
              </span>
            )}
          </button>
          <button onClick={fetchOrderHistory} className="order-history-button">Order History</button>
          <button onClick={handleLogout} className="logout-button">Logout</button>
        </div>
      </header>
      
      {/* Order History Modal */}
      {showOrderHistory && (
        <div className="modal-overlay">
          <div className="order-history-modal">
            <div className="modal-header">
              <h2>Your Order History</h2>
              <button onClick={() => setShowOrderHistory(false)} className="close-modal">×</button>
            </div>
            <div className="modal-content">
              {orderHistoryLoading ? (
                <p>Loading order history...</p>
              ) : orderHistory.length > 0 ? (
                <div className="order-history-list">
                  {orderHistory.map(order => (
                    <div key={order.id} className="order-card">
                      <div 
                        className="order-header" 
                        onClick={() => toggleOrderDetails(order.id)}
                      >
                        <div className="order-info">
                          <h3>Order #{order.id}</h3>
                          <div className="order-meta">
                            <span className={`status-badge ${order.status.toLowerCase()}`}>
                              {order.status}
                            </span>
                            <span className="total-price">${parseFloat(order.totalPrice).toFixed(2)}</span>
                          </div>
                        </div>
                        <div className="order-details">
                          <p><strong>Restaurant:</strong> #{order.restaurantID}</p>
                          <p><strong>Destination:</strong> {order.destination}</p>
                          <p><strong>Shipping:</strong> {order.shippingCompany}</p>
                          <span className="expand-icon">
                            {expandedOrderId === order.id ? '▼' : '►'}
                          </span>
                        </div>
                      </div>
                      
                      {expandedOrderId === order.id && (
                        <div className="order-dishes">
                          <h4>Ordered Items {loadingOrderDishes ? '(Loading...)' : ''}</h4>
                          {console.log('Order dishes state:', orderDishes, 'Loading:', loadingOrderDishes, 'Expanded ID:', expandedOrderId)}
                          {loadingOrderDishes ? (
                            <p>Loading order items...</p>
                          ) : orderDishes && orderDishes.length > 0 ? (
                            <table className="dishes-table">
                              <thead>
                                <tr>
                                  <th>Dish Name</th>
                                  <th>Quantity</th>
                                  <th>Price</th>
                                  <th>Total</th>
                                </tr>
                              </thead>
                              <tbody>
                                {orderDishes.map(dish => (
                                  <tr key={dish.id}>
                                    <td>{dish.dishName || 'N/A'}</td>
                                    <td>{dish.quantity}</td>
                                    <td>${parseFloat(dish.unitPrice).toFixed(2)}</td>
                                    <td>${(parseFloat(dish.unitPrice) * dish.quantity).toFixed(2)}</td>
                                  </tr>
                                ))}
                              </tbody>
                            </table>
                          ) : (
                            <p>No items found for this order.</p>
                          )}
                        </div>
                      )}
                    </div>
                  ))}
                </div>
              ) : (
                <p>No order history found.</p>
              )}
            </div>
          </div>
        </div>
      )}

      {/* Notifications Modal */}
      {showNotifications && (
        <div className="modal-overlay">
          <div className="notifications-modal">
            <div className="modal-header">
              <h2>Order Status Notifications</h2>
              <div className="modal-actions">
                <button 
                  onClick={() => {
                    // Just fetch notifications, don't need to set modal visibility again
                    fetchNotifications();
                  }} 
                  className="refresh-button" 
                  title="Refresh notifications"
                  disabled={notificationsLoading}
                >
                  ↻
                </button>

                <button onClick={() => setShowNotifications(false)} className="close-modal">×</button>
              </div>
            </div>
            <div className="modal-content">
              {notificationsLoading ? (
                <div className="loading-state">
                  <p className="loading-message">
                    <span className="loading-spinner"></span>
                    Loading notifications...
                  </p>
                </div>
              ) : notifications === null ? (
                <div className="error-state">
                  <p className="error-message">Failed to load notifications.</p>
                  <p>There was a problem retrieving your notifications. Please try again later.</p>
                  <button 
                    onClick={() => {
                      // Just fetch notifications, modal is already visible
                      fetchNotifications();
                    }}
                    className="submit-button"
                    style={{ width: 'auto', margin: '1rem auto', display: 'block' }}
                  >
                    Try Again
                  </button>
                </div>
              ) : notifications && notifications.length > 0 ? (
                <div className="notification-list">
                  {notifications.map((notification, index) => {
                    const isRead = readNotifications.has(notification.id);
                    return (
                      <div 
                        key={notification.id || `notification-${index}`} 
                        className={`notification-card ${notification.status ? notification.status.toLowerCase() : 'unknown'} ${isRead ? 'read' : 'unread'}`}
                      >
                        <div className="notification-header">
                          <h3>
                            Order #{notification.orderID || notification.id || 'Unknown'}
                            {!isRead && <span className="unread-indicator"></span>}
                          </h3>
                          <span className={`status-badge ${notification.status ? notification.status.toLowerCase() : 'unknown'}`}>
                            {notification.status || 'Unknown'}
                          </span>
                        </div>
                        <div className="notification-details">
                          {notification.reason && (
                            <p> {notification.reason}</p>
                          )}
                          {/* <p><strong>Updated:</strong> {notification.timestamp ? formatTimestamp(notification.timestamp) : 'N/A'}</p> */}
                        </div>
                      </div>
                    );
                  })}
                </div>
              ) : (
                <div className="empty-state">
                  <p className="empty-message">No notifications found</p>
                  <p className="empty-description">When you receive order status updates, they will appear here.</p>
                </div>
              )}
            </div>
          </div>
        </div>
      )}

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
