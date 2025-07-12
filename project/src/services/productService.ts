import api from '../utils/api';
import { Product } from '../types';

export const productService = {
  getAll: async (): Promise<Product[]> => {
    try {
      const response = await api.get('/products');
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        console.warn('Products endpoint not available, returning empty array');
        return [];
      }
      throw error;
    }
  },

  getById: async (id: number): Promise<Product> => {
    const response = await api.get(`/products/${id}`);
    return response.data;
  },

  create: async (product: Omit<Product, 'productId'>): Promise<Product> => {
    const response = await api.post('/products', product);
    return response.data;
  },

  update: async (id: number, product: Partial<Product>): Promise<Product> => {
    const response = await api.put(`/products/${id}`, product);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/products/${id}`);
  },

  getByCategory: async (category: string): Promise<Product[]> => {
    try {
      const response = await api.get(`/products?category=${category}`);
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return [];
      }
      throw error;
    }
  },

  search: async (query: string): Promise<Product[]> => {
    try {
      const response = await api.get(`/products?search=${query}`);
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return [];
      }
      throw error;
    }
  },
};