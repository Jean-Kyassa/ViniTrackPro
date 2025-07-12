// import api from '../utils/api';
// import { ProductionBatch, PaginatedResponse } from '../types';

// export const productionBatchService = {
//   getAll: async (): Promise<ProductionBatch[]> => {
//     try {
//       const response = await api.get('/production-batches');
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         console.warn('Production batches endpoint not available, returning empty array');
//         return [];
//       }
//       throw error;
//     }
//   },
//   getAllPaginated: async (page = 0, size = 10): Promise<PaginatedResponse<ProductionBatch>> => {
//     try {
//       const response = await api.get('/production-batches/paginated', {
//         params: { page, size }
//       });
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return {
//           content: [],
//           totalElements: 0,
//           totalPages: 0,
//           size: size,
//           number: page,
//           first: true,
//           last: true
//         };
//       }
//       throw error;
//     }
//   },

//   getById: async (id: number): Promise<ProductionBatch> => {
//     const response = await api.get(`/production-batches/${id}`);
//     return response.data;
//   },

//   create: async (batch: Omit<ProductionBatch, 'id'>): Promise<ProductionBatch> => {
//     const response = await api.post('/production-batches', batch);
//     return response.data;
//   },

//   update: async (id: number, batch: Partial<ProductionBatch>): Promise<ProductionBatch> => {
//     const response = await api.put(`/production-batches/${id}`, batch);
//     return response.data;
//   },

//   delete: async (id: number): Promise<void> => {
//     await api.delete(`/production-batches/${id}`);
//   },

//   getByProduct: async (productId: number): Promise<ProductionBatch[]> => {
//     try {
//       const response = await api.get(`/production-batches?productId=${productId}`);
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
import { ProductionBatch, PaginatedResponse } from '../types';

export const productionBatchService = {
  getAll: async (): Promise<ProductionBatch[]> => {
    try {
      const response = await api.get('/production-batches');
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        console.warn('Production batches endpoint not available, returning empty array');
        return [];
      }
      throw error;
    }
  },

  getAllPaginated: async (page = 0, size = 10): Promise<PaginatedResponse<ProductionBatch>> => {
    try {
      const response = await api.get('/production-batches/paginated', {
        params: { page, size }
      });
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
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

  getById: async (id: number): Promise<ProductionBatch> => {
    const response = await api.get(`/production-batches/${id}`);
    return response.data;
  },

  create: async (batch: Omit<ProductionBatch, 'id'>): Promise<ProductionBatch> => {
    // Debug: Check what's in localStorage
    console.log('=== DEBUG: Creating production batch ===');
    console.log('All localStorage keys:', Object.keys(localStorage));
    console.log('userId from localStorage:', localStorage.getItem('userId'));
    console.log('user from localStorage:', localStorage.getItem('user'));
    console.log('Original batch data:', batch);
    
    // Get userId from localStorage
    let userId = localStorage.getItem('userId');
    
    // Fallback: try to get from user object if userId is not directly stored
    if (!userId) {
      const userStr = localStorage.getItem('user');
      if (userStr) {
        try {
          const user = JSON.parse(userStr);
          console.log('Parsed user object:', user);
          userId = user.id?.toString();
        } catch (e) {
          console.error('Error parsing user from localStorage:', e);
        }
      }
    }
    
    console.log('Final userId found:', userId);
    
    if (!userId) {
      console.error('No userId found in localStorage');
      throw new Error('User not authenticated - please log in again');
    }
    
    // Ensure userId is a valid number
    const userIdNumber = parseInt(userId);
    if (isNaN(userIdNumber)) {
      console.error('Invalid userId format:', userId);
      throw new Error('Invalid user ID format');
    }
    
    const batchWithUserId = {
      ...batch,
      userId: userIdNumber
    };
    
    console.log('Final batch data being sent:', batchWithUserId);
    
    try {
      const response = await api.post('/production-batches', batchWithUserId);
      console.log('Production batch created successfully:', response.data);
      return response.data;
    } catch (error: any) {
      console.error('Error creating production batch:', error);
      if (error.response?.data) {
        console.error('Server error details:', error.response.data);
      }
      throw error;
    }
  },

  update: async (id: number, batch: Partial<ProductionBatch>): Promise<ProductionBatch> => {
    const response = await api.put(`/production-batches/${id}`, batch);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/production-batches/${id}`);
  },

  getByProduct: async (productId: number): Promise<ProductionBatch[]> => {
    try {
      const response = await api.get(`/production-batches?productId=${productId}`);
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return [];
      }
      throw error;
    }
  },
};
