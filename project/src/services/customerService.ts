import api from '../utils/api';
import { Customer, PaginatedResponse } from '../types';

export const customerService = {
  getAll: async (page = 0, size = 10, search = ''): Promise<PaginatedResponse<Customer>> => {
    const response = await api.get('/customers', {
      params: { page, size, search }
    });
    return response.data;
  },

  getById: async (id: number): Promise<Customer> => {
    const response = await api.get(`/customers/${id}`);
    return response.data;
  },

  create: async (customer: Omit<Customer, 'id'>): Promise<Customer> => {
    const response = await api.post('/customers', customer);
    return response.data;
  },

  update: async (id: number, customer: Partial<Customer>): Promise<Customer> => {
    const response = await api.put(`/customers/${id}`, customer);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/customers/${id}`);
  },

  search: async (query: string, page = 0, size = 10): Promise<PaginatedResponse<Customer>> => {
    const response = await api.get('/customers/search', {
      params: { query, page, size }
    });
    return response.data;
  },
};