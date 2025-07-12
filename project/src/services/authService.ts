import api from '../utils/api';
import { AuthResponse, LoginRequest, SignupRequest, ForgotPasswordRequest, ResetPasswordRequest } from '../types';

export const authService = {
  login: async (credentials: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post('/auth/signin', credentials);
    return response.data;
  },

  signup: async (userData: SignupRequest): Promise<{ message: string }> => {
    const response = await api.post('/auth/signup', userData);
    return response.data;
  },

  logout: async (): Promise<{ message: string }> => {
    const response = await api.post('/auth/signout');
    return response.data;
  },

  getUserProfile: async () => {
    const response = await api.get('/auth/user/profile');
    return response.data;
  },

  updateProfile: async (userData: any) => {
    const response = await api.put('/auth/user/profile', userData);
    return response.data;
  },

  forgotPassword: async (data: ForgotPasswordRequest): Promise<{ message: string }> => {
    const response = await api.post('/auth/forgot-password', data);
    return response.data;
  },

  resetPassword: async (data: ResetPasswordRequest): Promise<{ message: string }> => {
    const response = await api.post('/auth/reset-password', data);
    return response.data;
  },

  verifyOTP: async (otp: string): Promise<{ message: string }> => {
    const response = await api.post('/auth/verify-otp', { otp });
    return response.data;
  },

  resendOTP: async (email: string): Promise<{ message: string }> => {
    const response = await api.post('/auth/resend-otp', { email });
    return response.data;
  },
};