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

// Dish API calls
export const getDishesByRestaurantId = (restaurantId) => {
  return restaurantAPI.get(`/dishes/getbyrestaurantid?restaurantId=${restaurantId}`);
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

// Order API calls (to be implemented)
export const createOrder = (orderData) => {
  return userAPI.post('/orders/create', orderData);
};
