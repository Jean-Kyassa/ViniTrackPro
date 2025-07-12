import api from '../utils/api';
import { Report } from '../types';

export const reportService = {
  getAll: async (): Promise<Report[]> => {
    const response = await api.get('/reports');
    return response.data;
  },

  getById: async (id: number): Promise<Report> => {
    const response = await api.get(`/reports/${id}`);
    return response.data;
  },

  create: async (report: Omit<Report, 'id'>): Promise<Report> => {
    const userId = localStorage.getItem('userId');
    const response = await api.post('/reports', report, {
      headers: {
        'X-User-Id': userId || '1'
      }
    });
    return response.data;
  },

  update: async (id: number, report: Omit<Report, 'id'>): Promise<Report> => {
    const response = await api.put(`/reports/${id}`, report);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/reports/${id}`);
  },

  getByType: async (type: string): Promise<Report[]> => {
    const response = await api.get(`/reports/type/${type}`);
    return response.data;
  },

  getByUser: async (userId: number): Promise<Report[]> => {
    const response = await api.get(`/reports/user/${userId}`);
    return response.data;
  },

  exportReport: async (id: number): Promise<Blob> => {
    const response = await api.get(`/reports/${id}/export`, {
      responseType: 'blob'
    });
    return response.data;
  },
};