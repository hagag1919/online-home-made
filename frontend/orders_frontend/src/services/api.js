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
