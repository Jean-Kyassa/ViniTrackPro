// import axios from 'axios';

// const API_BASE_URL = 'http://localhost:8083/api';

// // Create axios instance with proper CORS configuration
// const api = axios.create({
//   baseURL: API_BASE_URL,
//   headers: {
//     'Content-Type': 'application/json',
//     'Accept': 'application/json',
//   },
//   withCredentials: true, // Important for CORS with credentials
//   timeout: 15000, // 15 seconds timeout
// });

// // Request interceptor to add auth token
// api.interceptors.request.use(
//   (config) => {
//     const token = localStorage.getItem('token');
//     if (token) {
//       config.headers.Authorization = `Bearer ${token}`;
//     }
    
//     // Add user ID header if available
//     const userId = localStorage.getItem('userId');
//     if (userId) {
//       config.headers['X-User-Id'] = userId;
//     }

//     // Ensure proper CORS headers
//     config.headers['Access-Control-Allow-Credentials'] = 'true';
    
//     return config;
//   },
//   (error) => {
//     console.error('Request interceptor error:', error);
//     return Promise.reject(error);
//   }
// );

// // Response interceptor to handle errors
// api.interceptors.response.use(
//   (response) => {
//     return response;
//   },
//   (error) => {
//     console.error('API Error:', error);
    
//     // Handle network/CORS errors
//     if (error.code === 'ERR_NETWORK' || !error.response) {
//       console.error('Network/CORS error - Backend may not be running or CORS not configured');
//       // Show user-friendly error
//       if (window.location.pathname !== '/login') {
//         alert('Unable to connect to server. Please ensure the backend is running on port 8083 and CORS is properly configured.');
//       }
//     }
    
//     // Handle authentication errors
//     if (error.response?.status === 401) {
//       console.log('Authentication error - clearing tokens');
//       localStorage.removeItem('token');
//       localStorage.removeItem('user');
//       localStorage.removeItem('userId');
      
//       // Only redirect if not already on auth pages
//       if (!window.location.pathname.includes('/login') && 
//           !window.location.pathname.includes('/signup') &&
//           !window.location.pathname.includes('/forgot-password')) {
//         window.location.href = '/login';
//       }
//     }
    
//     // Handle forbidden errors
//     if (error.response?.status === 403) {
//       console.error('Access forbidden - insufficient permissions');
//     }
    
//     return Promise.reject(error);
//   }
// );

// export default api;

// import axios from 'axios';

// const API_BASE_URL = 'http://localhost:8083/api';

// // Create axios instance with proper CORS configuration
// const api = axios.create({
//   baseURL: API_BASE_URL,
//   headers: {
//     'Content-Type': 'application/json',
//     'Accept': 'application/json',
//   },
//   withCredentials: true,
//   timeout: 15000,
// });

// // Request interceptor to add auth token
// api.interceptors.request.use(
//   (config) => {
//     const token = localStorage.getItem('token');
//     if (token) {
//       config.headers.Authorization = `Bearer ${token}`;
//     }
    
//     const userId = localStorage.getItem('userId');
//     if (userId) {
//       config.headers['X-User-Id'] = userId;
//     }
    
//     // Log request for debugging
//     console.log('Making API request:', {
//       method: config.method,
//       url: config.url,
//       data: config.data,
//       headers: config.headers
//     });
    
//     return config;
//   },
//   (error) => {
//     console.error('Request interceptor error:', error);
//     return Promise.reject(error);
//   }
// );

// // Response interceptor to handle errors
// api.interceptors.response.use(
//   (response) => {
//     console.log('API response:', response.status, response.data);
//     return response;
//   },
//   (error) => {
//     console.error('API Error:', error);
//     console.error('Error details:', {
//       status: error.response?.status,
//       data: error.response?.data,
//       config: error.config
//     });
    
//     if (error.code === 'ERR_NETWORK' || !error.response) {
//       console.error('Network/CORS error - Backend may not be running or CORS not configured');
//       alert('Unable to connect to server. Please ensure the backend is running on port 8083.');
//     }
    
//     if (error.response?.status === 401) {
//       console.log('Authentication error - clearing tokens');
//       localStorage.removeItem('token');
//       localStorage.removeItem('user');
//       localStorage.removeItem('userId');
      
//       if (!window.location.pathname.includes('/login') &&
//           !window.location.pathname.includes('/signup') &&
//           !window.location.pathname.includes('/forgot-password')) {
//         window.location.href = '/login';
//       }
//     }
    
//     if (error.response?.status === 403) {
//       console.error('Access forbidden - insufficient permissions');
//       alert('You do not have permission to perform this action.');
//     }
    
//     return Promise.reject(error);
//   }
// );

// export default api;


import axios from 'axios';

const API_BASE_URL = 'http://localhost:8083/api';

// Create axios instance with proper CORS configuration
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  },
  withCredentials: true,
  timeout: 15000,
});

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    const userId = localStorage.getItem('userId');
    if (userId) {
      config.headers['X-User-Id'] = userId;
    }
    
    // Log request for debugging
    console.log('Making API request:', {
      method: config.method,
      url: config.url,
      data: config.data,
      headers: config.headers
    });
    
    return config;
  },
  (error) => {
    console.error('Request interceptor error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor to handle errors
api.interceptors.response.use(
  (response) => {
    console.log('API response:', response.status, response.data);
    return response;
  },
  (error) => {
    console.error('API Error:', error);
    console.error('Error details:', {
      status: error.response?.status,
      data: error.response?.data,
      config: error.config,
      url: error.config?.url
    });
    
    if (error.code === 'ERR_NETWORK' || !error.response) {
      console.error('Network/CORS error - Backend may not be running or CORS not configured');
      alert('Unable to connect to server. Please ensure the backend is running on port 8083.');
    }
    
    if (error.response?.status === 404) {
      console.error(`404 Error - Endpoint not found: ${error.config?.url}`);
      alert(`API endpoint not found: ${error.config?.url}. Please check your backend configuration.`);
    }
    
    if (error.response?.status === 401) {
      console.log('Authentication error - clearing tokens');
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      localStorage.removeItem('userId');
      
      if (!window.location.pathname.includes('/login') &&
          !window.location.pathname.includes('/signup') &&
          !window.location.pathname.includes('/forgot-password')) {
        window.location.href = '/login';
      }
    }
    
    if (error.response?.status === 403) {
      console.error('Access forbidden - insufficient permissions');
      alert('You do not have permission to perform this action.');
    }
    
    return Promise.reject(error);
  }
);

export default api;
