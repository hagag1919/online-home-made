import { useState, useEffect } from 'react';
import { useAuth } from '../services/AuthContext';
import { useNavigate } from 'react-router-dom';
import { getDishesByRestaurantId, createDish, updateDish, deleteDish, getRestaurantOrderHistory, getOrderDishesByOrderID } from '../services/api';
import './styles.css';

function SellerDashboard() {
  const [dishes, setDishes] = useState([]);
  const [showAddForm, setShowAddForm] = useState(false);
  const [newDish, setNewDish] = useState({
    name: '',
    description: '',
    price: '',
    amount: 1
  });
  const [editingDish, setEditingDish] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showOrderHistory, setShowOrderHistory] = useState(false);
  const [orderHistory, setOrderHistory] = useState([]);
  const [orderHistoryLoading, setOrderHistoryLoading] = useState(false);
  const [expandedOrderId, setExpandedOrderId] = useState(null);
  const [orderDishes, setOrderDishes] = useState([]);
  const [loadingOrderDishes, setLoadingOrderDishes] = useState(false);
  const { currentUser, logout } = useAuth();
  const navigate = useNavigate();
  
  // Get restaurant ID from local storage
  const restaurantId = localStorage.getItem('restaurantId') || (currentUser?.id ? currentUser.id : null);

  // Fetch dishes on component mount
  useEffect(() => {
    const fetchDishes = async () => {
      if (!restaurantId) {
        setError('Restaurant ID not found. Please login again.');
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        setError(null);
        const response = await getDishesByRestaurantId(restaurantId);
        
        // Map response data to include available property based on amount
        const dishesWithAvailability = response.data.map(dish => ({
          ...dish,
          available: dish.amount > 0
        }));
        
        setDishes(dishesWithAvailability);
      } catch (err) {
        setError('Failed to load dishes. Please try again later.');
        console.error('Error fetching dishes:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchDishes();
  }, [restaurantId]);

  const handleAddDish = async (e) => {
    e.preventDefault();
    
    if (!restaurantId) {
      alert('Restaurant ID not found. Please login again.');
      return;
    }
    
    try {
      const dishData = {
        name: newDish.name,
        description: newDish.description,
        price: parseFloat(newDish.price),
        amount: parseInt(newDish.amount) || 1
      };
      
      const response = await createDish(restaurantId, dishData);
      
      const createdDish = {
        ...response.data,
        available: response.data.amount > 0
      };
      
      setDishes([...dishes, createdDish]);
      setNewDish({ name: '', description: '', price: '', amount: 1 });
      setShowAddForm(false);
    } catch (err) {
      console.error('Error creating dish:', err);
      alert('Failed to add dish. Please try again.');
    }
  };

  const handleEditDish = async () => {
    if (!editingDish || !restaurantId) return;
    
    try {
      const dishData = {
        name: editingDish.name,
        description: editingDish.description,
        price: parseFloat(editingDish.price),
        amount: parseInt(editingDish.amount) || 0
      };
      
      await updateDish(restaurantId, editingDish.id, dishData);
      
      // Update the dish in the local state
      setDishes(dishes.map(dish => 
        dish.id === editingDish.id 
          ? { ...editingDish, available: parseInt(editingDish.amount) > 0 } 
          : dish
      ));
      
      setEditingDish(null);
    } catch (err) {
      console.error('Error updating dish:', err);
      alert('Failed to update dish. Please try again.');
    }
  };

  const handleToggleAvailability = async (dish) => {
    if (!restaurantId) return;
    
    // Toggle amount between 0 (unavailable) and previous amount or 1
    const newAmount = dish.available ? 0 : (dish.previousAmount || 1);
    
    try {
      const updatedDish = {
        ...dish,
        amount: newAmount,
        // Store previous amount when making unavailable
        previousAmount: dish.available ? dish.amount : dish.previousAmount
      };
      
      const dishData = {
        name: dish.name,
        description: dish.description,
        price: parseFloat(dish.price),
        amount: newAmount
      };
      
      await updateDish(restaurantId, dish.id, dishData);
      
      setDishes(dishes.map(d => 
        d.id === dish.id 
          ? { ...updatedDish, available: newAmount > 0 } 
          : d
      ));
      
    } catch (err) {
      console.error('Error toggling dish availability:', err);
      alert('Failed to update dish availability. Please try again.');
    }
  };

  const handleDeleteDish = async (id) => {
    if (!restaurantId) {
      alert('Restaurant ID not found. Please login again.');
      return;
    }
    
    if (!window.confirm("Are you sure you want to delete this dish?")) {
      return;
    }
    
    try {
      // Show loading indicator or disable the button here if needed
      const response = await deleteDish(restaurantId, id);
      
      // Remove the dish from the local state
      setDishes(dishes.filter(dish => dish.id !== id));
      
      // Optional: Show success message
      console.log('Dish deleted successfully', response.data);
    } catch (err) {
      console.error('Error deleting dish:', err);
      
      // Provide more specific error messages based on the error response
      if (err.response) {
        switch(err.response.status) {
          case 404:
            alert('Dish not found. It may have been already deleted.');
            break;
          case 403:
            alert('You do not have permission to delete this dish.');
            break;
          case 500:
            alert('Server error. Please try again later.');
            break;
          default:
            alert(`Failed to delete dish: ${err.response.data.message || 'Unknown error'}`);
        }
      } else if (err.request) {
        // The request was made but no response was received
        alert('Unable to reach the server. Please check your connection.');
      } else {
        // Something else caused the error
        alert('Failed to delete dish. Please try again.');
      }
    }
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
    if (!restaurantId) return;
    
    setOrderHistoryLoading(true);
    try {
      const response = await getRestaurantOrderHistory(restaurantId);
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

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <h1>Restaurant Dashboard: {currentUser?.name || 'Seller'}</h1>
        <div className="header-buttons">
          <button onClick={fetchOrderHistory} className="order-history-button">Order History</button>
          <button onClick={handleLogout} className="logout-button">Logout</button>
        </div>
      </header>
      
      {/* Order History Modal */}
      {showOrderHistory && (
        <div className="modal-overlay">
          <div className="order-history-modal">
            <div className="modal-header">
              <h2>Restaurant Order History</h2>
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
                          <p><strong>User ID:</strong> #{order.userID}</p>
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
                <p>No order history found for this restaurant.</p>
              )}
            </div>
          </div>
        </div>
      )}

      <div className="dashboard-content">
        <div className="dishes-management">
          <div className="section-header">
            <h2>Your Dishes</h2>
            <button 
              className="add-dish-button"
              onClick={() => setShowAddForm(true)}
            >
              Add New Dish
            </button>
          </div>

          {showAddForm && (
            <div className="dish-form">
              <h3>Add New Dish</h3>
              <form onSubmit={handleAddDish}>
                <div className="form-group">
                  <label>Name</label>
                  <input 
                    type="text" 
                    value={newDish.name} 
                    onChange={(e) => setNewDish({...newDish, name: e.target.value})}
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Description</label>
                  <textarea 
                    value={newDish.description} 
                    onChange={(e) => setNewDish({...newDish, description: e.target.value})}
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Price ($)</label>
                  <input 
                    type="number" 
                    min="0.01" 
                    step="0.01" 
                    value={newDish.price} 
                    onChange={(e) => setNewDish({...newDish, price: e.target.value})}
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Amount Available</label>
                  <input 
                    type="number" 
                    min="1" 
                    step="1" 
                    value={newDish.amount} 
                    onChange={(e) => setNewDish({...newDish, amount: e.target.value})}
                    required
                  />
                </div>
                <div className="form-buttons">
                  <button type="submit">Add Dish</button>
                  <button 
                    type="button" 
                    className="cancel-button"
                    onClick={() => setShowAddForm(false)}
                  >
                    Cancel
                  </button>
                </div>
              </form>
            </div>
          )}

          {editingDish && (
            <div className="dish-form">
              <h3>Edit Dish</h3>
              <form onSubmit={(e) => { e.preventDefault(); handleEditDish(); }}>
                <div className="form-group">
                  <label>Name</label>
                  <input 
                    type="text" 
                    value={editingDish.name} 
                    onChange={(e) => setEditingDish({...editingDish, name: e.target.value})}
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Description</label>
                  <textarea 
                    value={editingDish.description} 
                    onChange={(e) => setEditingDish({...editingDish, description: e.target.value})}
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Price ($)</label>
                  <input 
                    type="number" 
                    min="0.01" 
                    step="0.01" 
                    value={editingDish.price} 
                    onChange={(e) => setEditingDish({...editingDish, price: parseFloat(e.target.value)})}
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Amount Available</label>
                  <input 
                    type="number" 
                    min="0" 
                    step="1" 
                    value={editingDish.amount} 
                    onChange={(e) => setEditingDish({...editingDish, amount: parseInt(e.target.value)})}
                    required
                  />
                </div>
                <div className="form-buttons">
                  <button type="submit">Update Dish</button>
                  <button 
                    type="button" 
                    className="cancel-button"
                    onClick={() => setEditingDish(null)}
                  >
                    Cancel
                  </button>
                </div>
              </form>
            </div>
          )}

          {loading ? (
            <div className="loading-state">Loading dishes...</div>
          ) : error ? (
            <div className="error-state">{error}</div>
          ) : (
            <div className="dish-list seller-dish-list">
              {dishes.map(dish => (
                <div key={dish.id} className="dish-card seller-dish-card">
                  <div className="dish-info">
                    <h3>{dish.name}</h3>
                    <p>{dish.description}</p>
                    <p className="dish-price">${Number(dish.price).toFixed(2)}</p>
                    <div className={`availability-badge ${dish.available ? 'available' : 'unavailable'}`}>
                      {dish.available ? `Available (${dish.amount})` : 'Unavailable'}
                    </div>
                  </div>
                  <div className="dish-actions">
                    <button 
                      className="edit-button"
                      onClick={() => setEditingDish(dish)}
                    >
                      Edit
                    </button>
                    <button 
                      className={`availability-button ${dish.available ? 'mark-unavailable' : 'mark-available'}`}
                      onClick={() => handleToggleAvailability(dish)}
                    >
                      {dish.available ? 'Mark Unavailable' : 'Mark Available'}
                    </button>
                    <button 
                      className="delete-button"
                      onClick={() => handleDeleteDish(dish.id)}
                    >
                      Delete
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
          
          {dishes.length === 0 && (
            <div className="empty-state">
              <p>You haven't added any dishes yet. Click "Add New Dish" to get started.</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default SellerDashboard;
