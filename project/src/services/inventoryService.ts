import api from '../utils/api';
import { InventoryItem, PaginatedResponse, InventoryStats } from '../types';

export const inventoryService = {
  getAll: async (page = 0, size = 10, search = '', category = ''): Promise<PaginatedResponse<InventoryItem>> => {
    const params = new URLSearchParams();
    params.append('page', page.toString());
    params.append('size', size.toString());
    if (search) params.append('search', search);
    if (category) params.append('category', category);

    const response = await api.get(`/inventory?${params.toString()}`);
    return response.data;
  },

  getById: async (id: number): Promise<InventoryItem> => {
    const response = await api.get(`/inventory/${id}`);
    return response.data;
  },

  create: async (item: Omit<InventoryItem, 'id'>): Promise<InventoryItem> => {
    const response = await api.post('/inventory', item);
    return response.data;
  },

  update: async (id: number, item: Partial<InventoryItem>): Promise<InventoryItem> => {
    const response = await api.put(`/inventory/${id}`, item);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/inventory/${id}`);
  },

  getLowStock: async (): Promise<InventoryItem[]> => {
    try {
      const response = await api.get('/inventory/low-stock');
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        console.warn('Low stock endpoint not available, returning empty array');
        return [];
      }
      throw error;
    }
  },

  updateQuantity: async (id: number, quantity: number): Promise<InventoryItem> => {
    const response = await api.put(`/inventory/${id}/quantity?quantity=${quantity}`);
    return response.data.item || response.data;
  },

  getStats: async (): Promise<InventoryStats> => {
    try {
      const response = await api.get('/inventory/stats');
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        console.warn('Stats endpoint not available, returning default stats');
        return {
          totalItems: 0,
          lowStockItems: 0,
          totalValue: 0
        };
      }
      throw error;
    }
  },

  getCategories: async (): Promise<string[]> => {
    try {
      const response = await api.get('/inventory/categories');
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return ['RAW_MATERIALS', 'WINE', 'SUPPLIES', 'EQUIPMENT'];
      }
      throw error;
    }
  },

  getUnitTypes: async (): Promise<string[]> => {
    try {
      const response = await api.get('/inventory/unit-types');
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return ['PIECES', 'BOTTLES', 'CASES', 'BARRELS', 'LITERS', 'KILOGRAMS'];
      }
      throw error;
    }
  },

  searchItems: async (query: string, page = 0, size = 10): Promise<PaginatedResponse<InventoryItem>> => {
    const response = await api.get('/inventory/search', {
      params: { query, page, size }
    });
    return response.data;
  },
};