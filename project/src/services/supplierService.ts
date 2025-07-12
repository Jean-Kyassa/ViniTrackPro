import api from '../utils/api';
import { Supplier, PaginatedResponse } from '../types';

export const supplierService = {
  getAll: async (page = 0, size = 10, search = ''): Promise<PaginatedResponse<Supplier>> => {
    try {
      const response = await api.get('/suppliers', {
        params: { page, size, search }
      });
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        console.warn('Suppliers endpoint not available, returning empty response');
        return {
          content: [],
          totalElements: 0,
          totalPages: 0,
          size: size,
          number: page,
          first: true,
          last: true
        };
      }
      throw error;
    }
  },

  getById: async (id: number): Promise<Supplier> => {
    const response = await api.get(`/suppliers/${id}`);
    return response.data;
  },

  create: async (supplier: Omit<Supplier, 'id'>): Promise<Supplier> => {
    const response = await api.post('/suppliers', supplier);
    return response.data;
  },

  update: async (id: number, supplier: Partial<Supplier>): Promise<Supplier> => {
    const response = await api.put(`/suppliers/${id}`, supplier);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/suppliers/${id}`);
  },

  getStats: async () => {
    try {
      const response = await api.get('/suppliers/stats');
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return { totalSuppliers: 0 };
      }
      throw error;
    }
  },

  search: async (query: string, page = 0, size = 10): Promise<PaginatedResponse<Supplier>> => {
    const response = await api.get('/suppliers/search', {
      params: { query, page, size }
    });
    return response.data;
  },

  getCategories: async (): Promise<string[]> => {
    try {
      const response = await api.get('/suppliers/product-categories');
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return ['RAW_MATERIALS', 'PACKAGING', 'EQUIPMENT', 'SERVICES'];
      }
      throw error;
    }
  },

  getCurrencies: async (): Promise<string[]> => {
    try {
      const response = await api.get('/suppliers/currencies');
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return ['USD', 'EUR', 'GBP', 'CAD'];
      }
      throw error;
    }
  },

  getPaymentTerms: async (): Promise<string[]> => {
    try {
      const response = await api.get('/suppliers/payment-terms');
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return ['NET_30', 'NET_15', 'NET_60', 'COD', 'PREPAID'];
      }
      throw error;
    }
  },
};