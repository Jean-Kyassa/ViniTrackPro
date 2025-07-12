// import api from '../utils/api';
// import { Vehicle } from '../types';

// export const vehicleService = {
//   getAll: async (): Promise<Vehicle[]> => {
//     try {
//       const response = await api.get('/vehicles');
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         console.warn('Vehicles endpoint not available, returning empty array');
//         return [];
//       }
//       throw error;
//     }
//   },
//   getById: async (id: number): Promise<Vehicle> => {
//     const response = await api.get(`/vehicles/${id}`);
//     return response.data;
//   },

//   create: async (vehicle: Omit<Vehicle, 'id'>): Promise<Vehicle> => {
//     const response = await api.post('/vehicles', vehicle);
//     return response.data;
//   },

//   update: async (id: number, vehicle: Partial<Vehicle>): Promise<Vehicle> => {
//     const response = await api.put(`/vehicles/${id}`, vehicle);
//     return response.data;
//   },

//   delete: async (id: number): Promise<void> => {
//     await api.delete(`/vehicles/${id}`);
//   },

//   getByType: async (type: string): Promise<Vehicle[]> => {
//     try {
//       const response = await api.get(`/vehicles?type=${type}`);
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return [];
//       }
//       throw error;
//     }
//   },

//   getUnassigned: async (): Promise<Vehicle[]> => {
//     try {
//       const response = await api.get('/vehicles?unassignedOnly=true');
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
import { Vehicle } from '../types';

export const vehicleService = {
  getAll: async (): Promise<Vehicle[]> => {
    try {
      const response = await api.get('/vehicles');
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        console.warn('Vehicles endpoint not available, returning empty array');
        return [];
      }
      throw error;
    }
  },

  getById: async (id: number): Promise<Vehicle> => {
    const response = await api.get(`/vehicles/${id}`);
    return response.data;
  },

  create: async (vehicle: Omit<Vehicle, 'id'>): Promise<Vehicle> => {
    const response = await api.post('/vehicles', vehicle);
    return response.data;
  },

  update: async (id: number, vehicle: Partial<Vehicle>): Promise<Vehicle> => {
    const response = await api.put(`/vehicles/${id}`, vehicle);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/vehicles/${id}`);
  },

  getByType: async (type: string): Promise<Vehicle[]> => {
    try {
      const response = await api.get('/vehicles', { params: { type } });
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return [];
      }
      throw error;
    }
  },

  getUnassigned: async (): Promise<Vehicle[]> => {
    try {
      const response = await api.get('/vehicles', { params: { unassignedOnly: true } });
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return [];
      }
      throw error;
    }
  },
};