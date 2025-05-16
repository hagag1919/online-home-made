import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../services/AuthContext';
import { createCompany, listAllUsers, getAllRestaurants, getServiceLogs, getPaymentFailures } from '../services/api';
import './styles.css';

function AdminDashboard() {
  const [companyName, setCompanyName] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [createdCompanies, setCreatedCompanies] = useState([]);
  const [success, setSuccess] = useState('');
  
  // User state
  const [users, setUsers] = useState([]);
  const [loadingUsers, setLoadingUsers] = useState(false);
  const [userError, setUserError] = useState(null);
  
  // Restaurant state
  const [restaurants, setRestaurants] = useState([]);
  const [loadingRestaurants, setLoadingRestaurants] = useState(false);
  const [restaurantError, setRestaurantError] = useState(null);
  
  // Service logs state
  const [serviceLogs, setServiceLogs] = useState([]);
  const [loadingServiceLogs, setLoadingServiceLogs] = useState(false);
  const [serviceLogError, setServiceLogError] = useState(null);
  
  // Payment failures state
  const [paymentFailures, setPaymentFailures] = useState([]);
  const [loadingPaymentFailures, setLoadingPaymentFailures] = useState(false);
  const [paymentFailureError, setPaymentFailureError] = useState(null);
  
  const { currentUser, logout } = useAuth();
  const navigate = useNavigate();
  
  // Fetch data when component mounts
  useEffect(() => {
    fetchUsers();
    fetchRestaurants();
    fetchServiceLogs();
    fetchPaymentFailures();
  }, []);
  
  const fetchUsers = async () => {
    setLoadingUsers(true);
    setUserError(null);
    
    try {
      const response = await listAllUsers();
      setUsers(response.data);
    } catch (err) {
      console.error('Error fetching users:', err);
      setUserError('Failed to load users. Please try again.');
    } finally {
      setLoadingUsers(false);
    }
  };
  
  const fetchRestaurants = async () => {
    setLoadingRestaurants(true);
    setRestaurantError(null);
    
    try {
      const response = await getAllRestaurants();
      setRestaurants(response.data);
    } catch (err) {
      console.error('Error fetching restaurants:', err);
      setRestaurantError('Failed to load restaurants. Please try again.');
    } finally {
      setLoadingRestaurants(false);
    }
  };
  
  const fetchServiceLogs = async () => {
    setLoadingServiceLogs(true);
    setServiceLogError(null);
    
    try {
      const response = await getServiceLogs();
      setServiceLogs(response.data);
    } catch (err) {
      console.error('Error fetching service logs:', err);
      setServiceLogError('Failed to load service logs. Please try again.');
    } finally {
      setLoadingServiceLogs(false);
    }
  };
  
  const fetchPaymentFailures = async () => {
    setLoadingPaymentFailures(true);
    setPaymentFailureError(null);
    
    try {
      const response = await getPaymentFailures();
      setPaymentFailures(response.data);
    } catch (err) {
      console.error('Error fetching payment failures:', err);
      setPaymentFailureError('Failed to load payment failures. Please try again.');
    } finally {
      setLoadingPaymentFailures(false);
    }
  };

  const handleCreateCompany = async (e) => {
    e.preventDefault();
    
    if (!companyName.trim()) {
      setError('Please enter a restaurant name');
      return;
    }
    
    setLoading(true);
    setError(null);
    setSuccess('');
    
    try {
      const response = await createCompany([companyName.trim()]);
      console.log('Company created:', response.data);
      setCreatedCompanies([...createdCompanies, ...response.data]);
      setCompanyName('');
      setSuccess('Restaurant account created successfully!');
    } catch (err) {
      console.error('Error creating company:', err);
      setError('Failed to create restaurant account. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h1 className="dashboard-title">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" className="dashboard-icon" viewBox="0 0 16 16">
            <path d="M8 4a.5.5 0 0 1 .5.5V6a.5.5 0 0 1-1 0V4.5A.5.5 0 0 1 8 4zM3.732 5.732a.5.5 0 0 1 .707 0l.915.914a.5.5 0 1 1-.708.708l-.914-.915a.5.5 0 0 1 0-.707zM2 10a.5.5 0 0 1 .5-.5h1.586a.5.5 0 0 1 0 1H2.5A.5.5 0 0 1 2 10zm9.5 0a.5.5 0 0 1 .5-.5h1.5a.5.5 0 0 1 0 1H12a.5.5 0 0 1-.5-.5zm.754-4.246a.389.389 0 0 0-.527-.02L7.547 9.31a.91.91 0 1 0 1.302 1.258l3.434-4.297a.389.389 0 0 0-.029-.518z"/>
            <path fillRule="evenodd" d="M0 10a8 8 0 1 1 15.547 2.661c-.442 1.253-1.845 1.602-2.932 1.25C11.309 13.488 9.475 13 8 13c-1.474 0-3.31.488-4.615.911-1.087.352-2.49.003-2.932-1.25A7.988 7.988 0 0 1 0 10zm8-7a7 7 0 0 0-6.603 9.329c.203.575.923.876 1.68.63C4.397 12.533 6.358 12 8 12s3.604.532 4.923.96c.757.245 1.477-.056 1.68-.631A7 7 0 0 0 8 3z"/>
          </svg>
          Admin Dashboard
        </h1>
        <div className="dashboard-actions">
          <span className="user-info">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
              <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H3Zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6Z"/>
            </svg>
            <strong>{currentUser?.username || 'Admin'}</strong>
          </span>
          <button className="btn btn-secondary logout-btn" onClick={handleLogout}>
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
              <path fillRule="evenodd" d="M10 12.5a.5.5 0 0 1-.5.5h-8a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 .5.5v2a.5.5 0 0 0 1 0v-2A1.5 1.5 0 0 0 9.5 2h-8A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h8a1.5 1.5 0 0 0 1.5-1.5v-2a.5.5 0 0 0-1 0v2z"/>
              <path fillRule="evenodd" d="M15.854 8.354a.5.5 0 0 0 0-.708l-3-3a.5.5 0 0 0-.708.708L14.293 7.5H5.5a.5.5 0 0 0 0 1h8.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3z"/>
            </svg>
            Logout
          </button>
        </div>
      </div>

      <div className="stats-overview">
        <div className="stat-card">
          <div className="stat-icon users-icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 16 16">
              <path d="M15 14s1 0 1-1-1-4-5-4-5 3-5 4 1 1 1 1h8Zm-7.978-1A.261.261 0 0 1 7 12.996c.001-.264.167-1.03.76-1.72C8.312 10.629 9.282 10 11 10c1.717 0 2.687.63 3.24 1.276.593.69.758 1.457.76 1.72l-.008.002a.274.274 0 0 1-.014.002H7.022ZM11 7a2 2 0 1 0 0-4 2 2 0 0 0 0 4Zm3-2a3 3 0 1 1-6 0 3 3 0 0 1 6 0ZM6.936 9.28a5.88 5.88 0 0 0-1.23-.247A7.35 7.35 0 0 0 5 9c-4 0-5 3-5 4 0 .667.333 1 1 1h4.216A2.238 2.238 0 0 1 5 13c0-1.01.377-2.042 1.09-2.904.243-.294.526-.569.846-.816ZM4.92 10A5.493 5.493 0 0 0 4 13H1c0-.26.164-1.03.76-1.724.545-.636 1.492-1.256 3.16-1.275ZM1.5 5.5a3 3 0 1 1 6 0 3 3 0 0 1-6 0Zm3-2a2 2 0 1 0 0 4 2 2 0 0 0 0-4Z"/>
            </svg>
          </div>
          <div className="stat-content">
            <span className="stat-title">Users</span>
            <span className="stat-value">{users.length}</span>
          </div>
        </div>
        
        <div className="stat-card">
          <div className="stat-icon restaurants-icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 16 16">
              <path d="M7.657 6.247c.11-.33.576-.33.686 0l.645 1.937a2.89 2.89 0 0 0 1.829 1.828l1.936.645c.33.11.33.576 0 .686l-1.937.645a2.89 2.89 0 0 0-1.828 1.829l-.645 1.936a.361.361 0 0 1-.686 0l-.645-1.937a2.89 2.89 0 0 0-1.828-1.828l-1.937-.645a.361.361 0 0 1 0-.686l1.937-.645a2.89 2.89 0 0 0 1.828-1.828l.645-1.937zM3.794 1.148a.217.217 0 0 1 .412 0l.387 1.162c.173.518.579.924 1.097 1.097l1.162.387a.217.217 0 0 1 0 .412l-1.162.387A1.734 1.734 0 0 0 4.593 5.69l-.387 1.162a.217.217 0 0 1-.412 0L3.407 5.69A1.734 1.734 0 0 0 2.31 4.593l-1.162-.387a.217.217 0 0 1 0-.412l1.162-.387A1.734 1.734 0 0 0 3.407 2.31l.387-1.162zM10.863.099a.145.145 0 0 1 .274 0l.258.774c.115.346.386.617.732.732l.774.258a.145.145 0 0 1 0 .274l-.774.258a1.156 1.156 0 0 0-.732.732l-.258.774a.145.145 0 0 1-.274 0l-.258-.774a1.156 1.156 0 0 0-.732-.732L9.1 2.137a.145.145 0 0 1 0-.274l.774-.258c.346-.115.617-.386.732-.732L10.863.1z"/>
            </svg>
          </div>
          <div className="stat-content">
            <span className="stat-title">Restaurants</span>
            <span className="stat-value">{restaurants.length}</span>
          </div>
        </div>
        
        <div className="stat-card">
          <div className="stat-icon logs-icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 16 16">
              <path d="M8.515 1.019A7 7 0 0 0 8 1V0a8 8 0 0 1 .589.022l-.074.997zm2.004.45a7.003 7.003 0 0 0-.985-.299l.219-.976c.383.086.76.2 1.126.342l-.36.933zm1.37.71a7.01 7.01 0 0 0-.439-.27l.493-.87a8.025 8.025 0 0 1 .979.654l-.615.789a6.996 6.996 0 0 0-.418-.302zm1.834 1.79a6.99 6.99 0 0 0-.653-.796l.724-.69c.27.285.52.59.747.91l-.818.576zm.744 1.352a7.08 7.08 0 0 0-.214-.468l.893-.45a7.976 7.976 0 0 1 .45 1.088l-.95.313a7.023 7.023 0 0 0-.179-.483zm.53 2.507a6.991 6.991 0 0 0-.1-1.025l.985-.17c.067.386.106.778.116 1.17l-1 .025zm-.131 1.538c.033-.17.06-.339.081-.51l.993.123a7.957 7.957 0 0 1-.23 1.155l-.964-.267c.046-.165.086-.332.12-.501zm-.952 2.379c.184-.29.346-.594.486-.908l.914.405c-.16.36-.345.706-.555 1.038l-.845-.535zm-.964 1.205c.122-.122.239-.248.35-.378l.758.653a8.073 8.073 0 0 1-.401.432l-.707-.707z"/>
              <path d="M8 1a7 7 0 1 0 4.95 11.95l.707.707A8.001 8.001 0 1 1 8 0v1z"/>
              <path d="M7.5 3a.5.5 0 0 1 .5.5v5.21l3.248 1.856a.5.5 0 0 1-.496.868l-3.5-2A.5.5 0 0 1 7 9V3.5a.5.5 0 0 1 .5-.5z"/>
            </svg>
          </div>
          <div className="stat-content">
            <span className="stat-title">Service Logs</span>
            <span className="stat-value">{serviceLogs.length}</span>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon payments-icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 16 16">
              <path d="M0 3.5A1.5 1.5 0 0 1 1.5 2h9A1.5 1.5 0 0 1 12 3.5V5h1.02a1.5 1.5 0 0 1 1.17.563l1.481 1.85a1.5 1.5 0 0 1 .329.938V10.5a1.5 1.5 0 0 1-1.5 1.5H14a2 2 0 1 1-4 0H5a2 2 0 1 1-3.998-.085A1.5 1.5 0 0 1 0 10.5v-7zm1.294 7.456A1.999 1.999 0 0 1 4.732 11h5.536a2.01 2.01 0 0 1 .732-.732V3.5a.5.5 0 0 0-.5-.5h-9a.5.5 0 0 0-.5.5v7a.5.5 0 0 0 .294.456zM12 10a2 2 0 0 1 1.732 1h.768a.5.5 0 0 0 .5-.5V8.35a.5.5 0 0 0-.11-.312l-1.48-1.85A.5.5 0 0 0 13.02 6H12v4zm-9 1a1 1 0 1 0 0 2 1 1 0 0 0 0-2zm9 0a1 1 0 1 0 0 2 1 1 0 0 0 0-2z"/>
            </svg>
          </div>
          <div className="stat-content">
            <span className="stat-title">Payment Failures</span>
            <span className="stat-value">{paymentFailures.length}</span>
          </div>
        </div>
      </div>

      <div className="card action-card">
        <h2>
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 16 16">
            <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>
            <path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z"/>
          </svg>
          Create Restaurant Account
        </h2>
        <p>Enter restaurant name to create a new account for a restaurant company.</p>
        
        {error && <div className="alert alert-danger">{error}</div>}
        {success && <div className="alert alert-success">{success}</div>}
        
        <form onSubmit={handleCreateCompany}>
          <div className="form-group">
            <label className="form-label">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="form-icon" viewBox="0 0 16 16">
                <path d="M2 2.5a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 .5.5v10a.5.5 0 0 1-.5.5h-10a.5.5 0 0 1-.5-.5v-10zm1 0v10h10v-10h-10zm7.5 3a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-5a.5.5 0 0 1-.5-.5v-1a.5.5 0 0 1 .5-.5h5zm0 3a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-5a.5.5 0 0 1-.5-.5v-1a.5.5 0 0 1 .5-.5h5zm0 3a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-5a.5.5 0 0 1-.5-.5v-1a.5.5 0 0 1 .5-.5h5z"/>
              </svg>
              Restaurant Name
            </label>
            <input
              className="form-control"
              type="text"
              value={companyName}
              onChange={(e) => setCompanyName(e.target.value)}
              placeholder="Enter restaurant name"
              disabled={loading}
            />
          </div>
          
          <button 
            className="btn btn-primary create-btn" 
            type="submit"
            disabled={loading}
          >
            {loading ? (
              <span className="loading-container">
                <span className="loading-dot"></span>
                <span className="loading-dot"></span>
                <span className="loading-dot"></span>
              </span>
            ) : (
              <>
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
                  <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>
                  <path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z"/>
                </svg>
                Create Restaurant Account
              </>
            )}
          </button>
        </form>
      </div>

      {createdCompanies.length > 0 && (
        <div className="card created-accounts-card">
          <h2>
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 16 16">
              <path d="M14 4.5V14a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V2a2 2 0 0 1 2-2h5.5L14 4.5zm-3 0A1.5 1.5 0 0 1 9.5 3V1H4a1 1 0 0 0-1 1v12a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1V4.5h-2z"/>
              <path d="M4.5 12.5A.5.5 0 0 1 5 12h3a.5.5 0 0 1 0 1H5a.5.5 0 0 1-.5-.5zm0-2A.5.5 0 0 1 5 10h6a.5.5 0 0 1 0 1H5a.5.5 0 0 1-.5-.5zm1.639-3.708 1.33.886 1.854-1.855a.25.25 0 0 1 .289-.047l1.888.974V7.5a.5.5 0 0 1-.5.5H5a.5.5 0 0 1-.5-.5V7s1.54-1.274 1.639-1.208zM6.25 6a.75.75 0 1 0 0-1.5.75.75 0 0 0 0 1.5z"/>
            </svg>
            Created Restaurant Accounts
          </h2>
          <p>Share these credentials with restaurant companies. They will need this information to access the system.</p>
          
          <div className="companies-container">
            {createdCompanies.map((company) => (
              <div key={company.id} className="company-card">
                <div className="company-header">
                  <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" viewBox="0 0 16 16">
                    <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H3Zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6Z"/>
                  </svg>
                  <span className="company-name">{company.name}</span>
                </div>
                <div className="company-info">
                  <div>
                    <strong>Restaurant ID:</strong> {company.id}
                  </div>
                  <div>
                    <strong>Password:</strong> 
                    <span className="company-password">{company.password}</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Data Tables Section - Users and Restaurants side by side */}
      <div className="tables-container">
        {/* Users Table */}
        <div className="card flex-card">
          <div className="section-header">
            <h2>
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" viewBox="0 0 16 16">
                <path d="M15 14s1 0 1-1-1-4-5-4-5 3-5 4 1 1 1 1h8Zm-7.978-1A.261.261 0 0 1 7 12.996c.001-.264.167-1.03.76-1.72C8.312 10.629 9.282 10 11 10c1.717 0 2.687.63 3.24 1.276.593.69.758 1.457.76 1.72l-.008.002a.274.274 0 0 1-.014.002H7.022ZM11 7a2 2 0 1 0 0-4 2 2 0 0 0 0 4Zm3-2a3 3 0 1 1-6 0 3 3 0 0 1 6 0ZM6.936 9.28a5.88 5.88 0 0 0-1.23-.247A7.35 7.35 0 0 0 5 9c-4 0-5 3-5 4 0 .667.333 1 1 1h4.216A2.238 2.238 0 0 1 5 13c0-1.01.377-2.042 1.09-2.904.243-.294.526-.569.846-.816ZM4.92 10A5.493 5.493 0 0 0 4 13H1c0-.26.164-1.03.76-1.724.545-.636 1.492-1.256 3.16-1.275ZM1.5 5.5a3 3 0 1 1 6 0 3 3 0 0 1-6 0Zm3-2a2 2 0 1 0 0 4 2 2 0 0 0 0-4Z"/>
              </svg>
              System Users
            </h2>
            <button 
              className="btn btn-secondary refresh-button" 
              onClick={fetchUsers}
              disabled={loadingUsers}
            >
              {loadingUsers ? (
                <>
                  <span className="loading-dot"></span>
                  <span className="loading-dot"></span>
                  <span className="loading-dot"></span>
                </>
              ) : 'Refresh'}
            </button>
          </div>
          
          {userError && <div className="alert alert-danger">{userError}</div>}
          
          {loadingUsers ? (
            <div className="table-loading">
              <div className="table-spinner"></div>
              <p>Loading users data...</p>
            </div>
          ) : users.length === 0 ? (
            <div className="no-data-message">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 16 16">
                <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>
                <path d="M7.002 11a1 1 0 1 1 2 0 1 1 0 0 1-2 0zM7.1 4.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 4.995z"/>
              </svg>
              <p>No users found in the system</p>
            </div>
          ) : (
            <div className="table-container">
              <table className="data-table">
                <thead>
                  <tr>
                    <th>User ID</th>
                    <th>Username</th>
                  </tr>
                </thead>
                <tbody>
                  {users.map(user => (
                    <tr key={user.id}>
                      <td>{user.id}</td>
                      <td>
                        <div className="user-name-cell">
                          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
                            <path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0zm4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4zm-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664h10z"/>
                          </svg>
                          {user.username}
                        </div>
                      </td>
                      <td>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>

        {/* Restaurants Table */}
        <div className="card flex-card">
          <div className="section-header">
            <h2>
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" viewBox="0 0 16 16">
                <path d="M3.1.7a.5.5 0 0 1 .4-.2h9a.5.5 0 0 1 .4.2l2.976 3.974c.149.185.156.45.01.644L8.4 15.3a.5.5 0 0 1-.8 0L.1 5.3a.5.5 0 0 1 0-.6l3-4zm11.386 3.785-1.806-2.41-.776 2.413 2.582-.003zm-3.633.004.961-2.989H4.186l.963 2.995 5.704-.006zM5.47 5.495 8 13.366l2.532-7.876-5.062.005zm-1.371-.999-.78-2.422-1.818 2.425 2.598-.003zM1.499 5.5l5.113 6.817-2.192-6.82L1.5 5.5zm7.889 6.817 5.123-6.83-2.928.002-2.195 6.828z"/>
              </svg>
              Restaurants
            </h2>
            <button 
              className="btn btn-secondary refresh-button" 
              onClick={fetchRestaurants}
              disabled={loadingRestaurants}
            >
              {loadingRestaurants ? (
                <>
                  <span className="loading-dot"></span>
                  <span className="loading-dot"></span>
                  <span className="loading-dot"></span>
                </>
              ) : 'Refresh'}
            </button>
          </div>
          
          {restaurantError && <div className="alert alert-danger">{restaurantError}</div>}
          
          {loadingRestaurants ? (
            <div className="table-loading">
              <div className="table-spinner"></div>
              <p>Loading restaurants data...</p>
            </div>
          ) : restaurants.length === 0 ? (
            <div className="no-data-message">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 16 16">
                <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>
                <path d="M7.002 11a1 1 0 1 1 2 0 1 1 0 0 1-2 0zM7.1 4.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 4.995z"/>
              </svg>
              <p>No restaurants found in the system</p>
            </div>
          ) : (
            <div className="table-container">
              <table className="data-table">
                <thead>
                  <tr>
                    <th>Restaurant ID</th>
                    <th>Restaurant Name</th>
                  </tr>
                </thead>
                <tbody>
                  {restaurants.map(restaurant => (
                    <tr key={restaurant.id}>
                      <td>{restaurant.id}</td>
                      <td>
                        <div className="restaurant-name-cell">
                          {restaurant.name}
                        </div>
                      </td>
                      <td>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>

      {/* Service Logs Section */}
      <div className="card logs-card">
        <div className="section-header">
          <h2>
            System Service Logs
          </h2>
          <button 
            className="btn btn-secondary refresh-button" 
            onClick={fetchServiceLogs}
            disabled={loadingServiceLogs}
          >
            {loadingServiceLogs ? (
              <>
                <span className="loading-dot"></span>
                <span className="loading-dot"></span>
                <span className="loading-dot"></span>
              </>
            ) : 'Refresh'}
          </button>
        </div>
        
        {serviceLogError && <div className="alert alert-danger">{serviceLogError}</div>}
        
        {loadingServiceLogs ? (
          <div className="table-loading">
            <div className="table-spinner"></div>
            <p>Loading service logs...</p>
          </div>
        ) : serviceLogs.length === 0 ? (
          <div className="no-data-message">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 16 16">
              <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>
              <path d="M7.002 11a1 1 0 1 1 2 0 1 1 0 0 1-2 0zM7.1 4.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 4.995z"/>
            </svg>
            <p>No service logs found in the system.</p>
            <p className="no-data-subtext">Service logs will appear here when system services report issues.</p>
          </div>
        ) : (
          <div className="table-container">
            <table className="data-table logs-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Service</th>
                  <th>Severity</th>
                  <th>Message</th>
                  <th>Occurred At</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {serviceLogs.map(log => (
                  <tr key={log.id} className={`severity-${log.severity.toLowerCase()}`}>
                    <td>{log.id}</td>
                    <td>
                      <div className="service-name-cell">
                        
                        {log.serviceName}
                      </div>
                    </td>
                    <td>
                      <span className={`severity-badge severity-${log.severity.toLowerCase()}`}>
                        {log.severity}
                      </span>
                    </td>
                    <td className="message-cell">{log.message}</td>
                    <td>{new Date(log.occurredAt).toLocaleString()}</td>
                    <td>
                      {log.notified ? (
                        <span className="status-badge status-notified">Notified</span>
                      ) : (
                        <span className="status-badge status-pending">Pending</span>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Payment Failures Section */}
      <div className="card payments-card">
        <div className="section-header">
          <h2>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" viewBox="0 0 16 16">
              <path d="M11.534 7h3.932a.25.25 0 0 1 .192.41l-1.966 2.36a.25.25 0 0 1-.384 0l-1.966-2.36a.25.25 0 0 1 .192-.41zm-11 2h3.932a.25.25 0 0 0 .192-.41L2.692 6.23a.25.25 0 0 0-.384 0L.342 8.59A.25.25 0 0 0 .534 9z"/>
              <path fillRule="evenodd" d="M8 3c-1.552 0-2.94.707-3.857 1.818a.5.5 0 1 1-.771-.636A6.002 6.002 0 0 1 13.917 7H12.9A5.002 5.002 0 0 0 8 3zM3.1 9a5.002 5.002 0 0 0 8.757 2.182.5.5 0 1 1 .771.636A6.002 6.002 0 0 1 2.083 9H3.1z"/>
            </svg>
            Payment Failures
          </h2>
          <button 
            className="btn btn-secondary refresh-button" 
            onClick={fetchPaymentFailures}
            disabled={loadingPaymentFailures}
          >
            {loadingPaymentFailures ? (
              <>
                <span className="loading-dot"></span>
                <span className="loading-dot"></span>
                <span className="loading-dot"></span>
              </>
            ) : 'Refresh'}
          </button>
        </div>
        
        {paymentFailureError && <div className="alert alert-danger">{paymentFailureError}</div>}
        
        {loadingPaymentFailures ? (
          <div className="table-loading">
            <div className="table-spinner"></div>
            <p>Loading payment failures...</p>
          </div>
        ) : paymentFailures.length === 0 ? (
          <div className="no-data-message">
            <p>No payment failures found in the system.</p>
            <p className="no-data-subtext">All payment transactions are processing normally.</p>
          </div>
        ) : (
          <div className="table-container">
            <table className="data-table logs-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>User ID</th>
                  <th>Amount</th>
                  <th>Error Message</th>
                  <th>Failed At</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {paymentFailures.map(failure => (
                  <tr key={failure.id} className="severity-error">
                    <td>{failure.id}</td>
                    <td>
                      <div className="user-id-cell">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
                          <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H3Zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6Z"/>
                        </svg>
                        {failure.userId}
                      </div>
                    </td>
                    <td>
                      <span className="amount-cell">
                        ${failure.amount ? failure.amount.toFixed(2) : 'N/A'}
                      </span>
                    </td>
                    <td className="message-cell">{failure.errorMessage}</td>
                    <td>{failure.failedAt ? new Date(failure.failedAt).toLocaleString() : 'N/A'}</td>
                    <td>
                      <span className="status-badge status-failed">Failed</span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}

export default AdminDashboard;
