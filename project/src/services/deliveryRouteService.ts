import api from '../utils/api';
import { DeliveryRoute } from '../types';

export const deliveryRouteService = {
  getAll: async (): Promise<DeliveryRoute[]> => {
    try {
      const response = await api.get('/delivery-routes');
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        console.warn('Delivery routes endpoint not available, returning empty array');
        return [];
      }
      throw error;
    }
  },

  getById: async (id: number): Promise<DeliveryRoute> => {
    const response = await api.get(`/delivery-routes/${id}`);
    return response.data;
  },

  create: async (route: Omit<DeliveryRoute, 'id'>): Promise<DeliveryRoute> => {
    const response = await api.post('/delivery-routes', route);
    return response.data;
  },

  update: async (id: number, route: Partial<DeliveryRoute>): Promise<DeliveryRoute> => {
    const response = await api.put(`/delivery-routes/${id}`, route);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/delivery-routes/${id}`);
  },

  getByDriver: async (driverId: number): Promise<DeliveryRoute[]> => {
    try {
      const response = await api.get(`/delivery-routes?driverId=${driverId}`);
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return [];
      }
      throw error;
    }
  },
};