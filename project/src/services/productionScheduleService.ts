import api from '../utils/api';
import { ProductionSchedule } from '../types';

export const productionScheduleService = {
  getAll: async (): Promise<ProductionSchedule[]> => {
    try {
      const response = await api.get('/production-schedules');
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        console.warn('Production schedules endpoint not available, returning empty array');
        return [];
      }
      throw error;
    }
  },

  getById: async (id: number): Promise<ProductionSchedule> => {
    const response = await api.get(`/production-schedules/${id}`);
    return response.data;
  },

  create: async (schedule: Omit<ProductionSchedule, 'id'>): Promise<ProductionSchedule> => {
    const response = await api.post('/production-schedules', schedule);
    return response.data;
  },

  update: async (id: number, schedule: Partial<ProductionSchedule>): Promise<ProductionSchedule> => {
    const response = await api.put(`/production-schedules/${id}`, schedule);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/production-schedules/${id}`);
  },

  updateStatus: async (id: number, status: string): Promise<ProductionSchedule> => {
    const response = await api.put(`/production-schedules/${id}/status?status=${status}`);
    return response.data;
  },

  getByLine: async (lineId: number): Promise<ProductionSchedule[]> => {
    try {
      const response = await api.get(`/production-schedules/line/${lineId}`);
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return [];
      }
      throw error;
    }
  },
};