import axios from 'axios';

const adminAPI = axios.create({
  baseURL: 'http://localhost:8088/admin',
  headers: {
    'Content-Type': 'application/json',
    'Accept': '*/*'
  }
});

const restaurantAPI = axios.create({
  baseURL: 'http://localhost:8083',
  headers: {
    'Content-Type': 'application/json',
    'Accept': '*/*'
  }
});

// Admin API calls
export const createCompany = (companyNames) => {
  return adminAPI.post('/create-company-accounts', { companyNames });
};

export const loginAdmin = (credentials) => {
  return adminAPI.post('/login', credentials);
};

export const listAllUsers = () => {
  return adminAPI.get('/list-users');
};

export const getServiceLogs = () => {
  return adminAPI.get('/service-logs');
};

export const getPaymentFailures = () => {
  return adminAPI.get('/payment-failures');
};

// Restaurant API calls
export const getAllRestaurants = () => {
  return restaurantAPI.get('/restaurants/getall');
};
