// import api from '../utils/api';
// import { Driver, PaginatedResponse } from '../types';

// export const driverService = {
//   getAll: async (): Promise<Driver[]> => {
//     try {
//       const response = await api.get('/drivers');
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         console.warn('Drivers endpoint not available, returning empty array');
//         return [];
//       }
//       throw error;
//     }
//   },

//   getById: async (id: number): Promise<Driver> => {
//     const response = await api.get(`/drivers/${id}`);
//     return response.data;
//   },

//   create: async (driver: Omit<Driver, 'id'>): Promise<Driver> => {
//     const response = await api.post('/drivers', driver);
//     return response.data;
//   },

//   update: async (id: number, driver: Partial<Driver>): Promise<Driver> => {
//     const response = await api.put(`/drivers/${id}`, driver);
//     return response.data;
//   },

//   delete: async (id: number): Promise<void> => {
//     await api.delete(`/drivers/${id}`);
//   },

//   getByStatus: async (status: string): Promise<Driver[]> => {
//     try {
//       const response = await api.get(`/drivers?status=${status}`);
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return [];
//       }
//       throw error;
//     }
//   },

//   getAvailable: async (): Promise<Driver[]> => {
//     try {
//       const response = await api.get('/drivers?availableOnly=true');
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return [];
//       }
//       throw error;
//     }
//   },
// };




import api from '../utils/api';
import { Driver } from '../types';

export const driverService = {
  getAll: async (): Promise<Driver[]> => {
    try {
      const response = await api.get('/drivers');
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        console.warn('Drivers endpoint not available, returning empty array');
        return [];
      }
      throw error;
    }
  },

  getById: async (id: number): Promise<Driver> => {
    const response = await api.get(`/drivers/${id}`);
    return response.data;
  },

  create: async (driver: Omit<Driver, 'id'>): Promise<Driver> => {
    const response = await api.post('/drivers', driver);
    return response.data;
  },

  update: async (id: number, driver: Partial<Driver>): Promise<Driver> => {
    const response = await api.put(`/drivers/${id}`, driver);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/drivers/${id}`);
  },

  getByStatus: async (status: string): Promise<Driver[]> => {
    try {
      const response = await api.get('/drivers', { params: { status } });
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return [];
      }
      throw error;
    }
  },

  getAvailable: async (): Promise<Driver[]> => {
    try {
      const response = await api.get('/drivers', { params: { availableOnly: true } });
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return [];
      }
      throw error;
    }
  },
};