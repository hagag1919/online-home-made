import axios from 'axios';

const userAPI = axios.create({
  baseURL: 'http://localhost:8083',
  headers: {
    'Content-Type': 'application/json',
    'Accept': '*/*'
  }
});

const restaurantAPI = axios.create({
  baseURL: 'http://localhost:8082',
  headers: {
    'Content-Type': 'application/json',
    'Accept': '*/*'
  }
});

const orderAPI = axios.create({
  baseURL: 'http://localhost:8080/system-order-1.0-SNAPSHOT/api/order',
  headers: {
    'Content-Type': 'application/json',
    'Accept': '*/*'
  }
});

// User API calls
export const registerUser = (userData) => {
  return userAPI.post('/accounts/register', userData);
};

export const loginUser = (userData) => {
  return userAPI.post('/accounts/login', userData);
};

// Restaurant API calls
export const loginRestaurant = (restaurantData) => {
  return restaurantAPI.post('/login', restaurantData);
};

export const getAllRestaurants = () => {
  return userAPI.get('/restaurants/getall', {
    headers: {
    }
  });
};

// Dish API calls
export const getDishesByRestaurantId = (restaurantId) => {
  return userAPI.get(`/restaurants/getbyrestaurantid?restaurantId=${restaurantId}`, {
    headers: {
    }
  });
};

export const createDish = (restaurantId, dishData) => {
  return restaurantAPI.post(`/dishes/create?restaurantId=${restaurantId}`, dishData);
};

export const updateDish = (restaurantId, dishId, dishData) => {
  return restaurantAPI.put(`/dishes/update?restaurantId=${restaurantId}&dishId=${dishId}`, dishData);
};

export const deleteDish = (restaurantId, dishId) => {
  return restaurantAPI.delete(`/dishes/delete?restaurantId=${restaurantId}&DishId=${dishId}`);
};

// Order API calls
export const createOrder = (orderData) => {
  const config = {
    method: 'post',
    url: 'http://localhost:8080/system-order-1.0-SNAPSHOT/api/order/placeOrder',
    headers: { 
      'Content-Type': 'application/json',
      'Accept': '*/*',
      'Origin': window.location.origin
    },
    data: orderData
  };
  
  return axios.request(config);
};


// Get order history for a user
export const getUserOrderHistory = (userId) => {
  return orderAPI.get(`/getAllOrdersByUserID?userID=${userId}`);
};

// Get order dishes by order ID
export const getOrderDishesByOrderID = (orderId) => {
  const config = {
    method: 'get',
    url: `http://localhost:8080/system-order-1.0-SNAPSHOT/api/order/getOrderDishesByOrderID?orderID=${orderId}`,
    headers: { 
      'Content-Type': 'application/json',
      'Accept': '*/*',
      'Origin': window.location.origin
    }
  };
  
  return axios.request(config);
};

// Get order status updates for a user
export const getOrderStatusUpdates = (userId) => {
  console.log(`Requesting order status updates from: /accounts/getOrderStatus?userId=${userId}`);
  
  // Add a timeout to the API call to prevent long hangs
  const timeoutPromise = new Promise((_, reject) => {
    setTimeout(() => reject(new Error('Request timeout')), 10000); // 10-second timeout
  });
  
  const fetchPromise = userAPI.get(`/accounts/getOrderStatus?userId=${userId}`)
    .then(response => {
      console.log('Order status updates response:', response);
      
      // Validate the response structure
      if (!response.data) {
        console.warn('Missing data property in response:', response);
        return { data: [] }; // Return empty array as fallback
      }
      
      // Ensure we have an array
      if (!Array.isArray(response.data)) {
        console.warn('Response data is not an array:', response.data);
        return { data: [] }; // Return empty array as fallback
      }
      
      return response;
    })
    .catch(error => {
      console.error('Error fetching order status updates:', error);
      throw error;
    });
  
  // Race between the timeout and the actual fetch
  return Promise.race([fetchPromise, timeoutPromise]);
};

// Get all orders for a restaurant by restaurant ID
export const getRestaurantOrderHistory = (restaurantId) => {
  const config = {
    method: 'get',
    url: `http://localhost:8080/system-order-1.0-SNAPSHOT/api/order/getAllOrdersByRestaurantID?restaurantID=${restaurantId}`,
    headers: { 
      'Content-Type': 'application/json',
      'Accept': '*/*',
      'Origin': window.location.origin
    }
  };
  
  return axios.request(config);
};


