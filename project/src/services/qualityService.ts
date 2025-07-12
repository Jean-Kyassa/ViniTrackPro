// import api from '../utils/api';
// import { QualityCheck, QualityStats } from '../types';

// export const qualityService = {
//   getAll: async (): Promise<QualityCheck[]> => {
//     const response = await api.get('/quality-checks');
//     return response.data;
//   },

//   getById: async (id: number): Promise<QualityCheck> => {
//     const response = await api.get(`/quality-checks/${id}`);
//     return response.data;
//   },

//   create: async (check: Omit<QualityCheck, 'id'>): Promise<QualityCheck> => {
//     const response = await api.post('/quality-checks', check);
//     return response.data;
//   },

//   update: async (id: number, check: Omit<QualityCheck, 'id'>): Promise<QualityCheck> => {
//     const response = await api.put(`/quality-checks/${id}`, check);
//     return response.data;
//   },

//   delete: async (id: number): Promise<void> => {
//     await api.delete(`/quality-checks/${id}`);
//   },

//   getByStatus: async (status: string): Promise<QualityCheck[]> => {
//     const response = await api.get(`/quality-checks/status/${status}`);
//     return response.data;
//   },

//   getStatistics: async (): Promise<QualityStats> => {
//     const response = await api.get('/quality-checks/statistics');
//     return response.data;
//   },

//   updateStatus: async (id: number, status: string): Promise<QualityCheck> => {
//     const response = await api.patch(`/quality-checks/${id}/status`, null, {
//       params: { status }
//     });
//     return response.data;
//   },

//   getByBatch: async (batchId: number): Promise<QualityCheck[]> => {
//     const response = await api.get(`/quality-checks/batch/${batchId}`);
//     return response.data;
//   },

//   getByType: async (type: string): Promise<QualityCheck[]> => {
//     const response = await api.get(`/quality-checks/type/${type}`);
//     return response.data;
//   },

//   searchByName: async (name: string): Promise<QualityCheck[]> => {
//     const response = await api.get('/quality-checks/search', {
//       params: { name }
//     });
//     return response.data;
//   },
// };

// import api from '../utils/api';
// import { QualityCheck } from '../types';

// export const qualityService = {
//   async getAll(): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get('/quality-checks');
//       return response.data;
//     } catch (error) {
//       console.error('Error fetching quality checks:', error);
//       throw error;
//     }
//   },

//   async getById(id: number): Promise<QualityCheck> {
//     try {
//       const response = await api.get(`/quality-checks/${id}`);
//       return response.data;
//     } catch (error) {
//       console.error('Error fetching quality check:', error);
//       throw error;
//     }
//   },

//   async create(data: Omit<QualityCheck, 'id'>): Promise<QualityCheck> {
//     try {
//       console.log('Creating quality check with data:', data);
//       const response = await api.post('/quality-checks', data);
//       return response.data;
//     } catch (error) {
//       console.error('Error creating quality check:', error);
//       console.error('Request data was:', data);
//       throw error;
//     }
//   },

//   async update(id: number, data: Partial<QualityCheck>): Promise<QualityCheck> {
//     try {
//       console.log('Updating quality check with data:', data);
//       const response = await api.put(`/quality-checks/${id}`, data);
//       return response.data;
//     } catch (error) {
//       console.error('Error updating quality check:', error);
//       throw error;
//     }
//   },

//   async updateStatus(id: number, status: string): Promise<QualityCheck> {
//     try {
//       const response = await api.patch(`/quality-checks/${id}/status?status=${status}`);
//       return response.data;
//     } catch (error) {
//       console.error('Error updating quality check status:', error);
//       throw error;
//     }
//   },

//   async delete(id: number): Promise<void> {
//     try {
//       await api.delete(`/quality-checks/${id}`);
//     } catch (error) {
//       console.error('Error deleting quality check:', error);
//       throw error;
//     }
//   },

//   async getByBatch(batchId: number): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get(`/quality-checks/batch/${batchId}`);
//       return response.data;
//     } catch (error) {
//       console.error('Error fetching quality checks by batch:', error);
//       throw error;
//     }
//   },

//   async getByStatus(status: string): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get(`/quality-checks/status/${status}`);
//       return response.data;
//     } catch (error) {
//       console.error('Error fetching quality checks by status:', error);
//       throw error;
//     }
//   }
// };

// import api from '../utils/api';
// import { QualityCheck } from '../types';

// export const qualityService = {
//   async getAll(): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get('quality-checks'); // Remove leading slash
//       return response.data;
//     } catch (error) {
//       console.error('Error fetching quality checks:', error);
//       if (error.response?.status === 404) {
//         console.warn('Quality checks endpoint not found, returning empty array');
//         return [];
//       }
//       throw error;
//     }
//   },

//   async create(data: Omit<QualityCheck, 'id'>): Promise<QualityCheck> {
//     try {
//       console.log('Creating quality check with data:', data);
      
//       if (!data.name || !data.type || !data.batchId) {
//         throw new Error('Missing required fields: name, type, or batchId');
//       }
      
//       const transformedData = {
//         name: data.name,
//         type: data.type.toUpperCase(),
//         batchId: Number(data.batchId),
//         checkDate: data.checkDate ? new Date(data.checkDate).toISOString() : new Date().toISOString(),
//         status: data.status.toUpperCase(),
//         notes: data.notes || '',
//         checkedById: Number(data.checkedById)
//       };
      
//       console.log('Transformed data:', transformedData);
      
//       const response = await api.post('quality-checks', transformedData); // Remove leading slash
//       return response.data;
//     } catch (error) {
//       console.error('Error creating quality check:', error);
//       console.error('Request data was:', data);
      
//       if (error.response?.status === 404) {
//         throw new Error('Quality checks API endpoint not found. Please check your backend configuration.');
//       }
      
//       if (error.response?.status === 403) {
//         throw new Error('Access denied. You may not have the required permissions (ADMIN or QUALITY role).');
//       }
      
//       if (error.response?.status === 400) {
//         const errorData = error.response.data;
//         if (typeof errorData === 'object' && errorData !== null) {
//           const errorMessages = Object.entries(errorData).map(([field, message]) => `${field}: ${message}`).join(', ');
//           throw new Error(`Validation errors: ${errorMessages}`);
//         }
//         throw new Error('Invalid data provided');
//       }
      
//       throw error;
//     }
//   },

//   // Update all other methods similarly - remove leading slashes
//   async getById(id: number): Promise<QualityCheck> {
//     const response = await api.get(`quality-checks/${id}`);
//     return response.data;
//   },

//   async update(id: number, data: Partial<QualityCheck>): Promise<QualityCheck> {
//     const transformedData = {
//       ...data,
//       type: data.type?.toUpperCase(),
//       status: data.status?.toUpperCase(),
//       batchId: data.batchId ? Number(data.batchId) : undefined,
//       checkedById: data.checkedById ? Number(data.checkedById) : undefined,
//       checkDate: data.checkDate ? new Date(data.checkDate).toISOString() : undefined
//     };
    
//     const response = await api.put(`quality-checks/${id}`, transformedData);
//     return response.data;
//   },

//   async updateStatus(id: number, status: string): Promise<QualityCheck> {
//     const response = await api.patch(`quality-checks/${id}/status?status=${status.toUpperCase()}`);
//     return response.data;
//   },

//   async delete(id: number): Promise<void> {
//     await api.delete(`quality-checks/${id}`);
//   },

//   async getByBatch(batchId: number): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get(`quality-checks/batch/${batchId}`);
//       return response.data;
//     } catch (error) {
//       if (error.response?.status === 404) {
//         return [];
//       }
//       throw error;
//     }
//   },

//   async getByStatus(status: string): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get(`quality-checks/status/${status.toUpperCase()}`);
//       return response.data;
//     } catch (error) {
//       if (error.response?.status === 404) {
//         return [];
//       }
//       throw error;
//     }
//   }
// };


// import api from '../utils/api';
// import { QualityCheck } from '../types';

// export const qualityService = {
//   async getAll(): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get('/quality-checks');
//       return response.data;
//     } catch (error: any) {
//       console.error('Error fetching quality checks:', error);
//       if (error.response?.status === 404) {
//         console.warn('Quality checks endpoint not found, returning empty array');
//         return [];
//       }
//       throw error;
//     }
//   },

//   async create(data: Omit<QualityCheck, 'id'>): Promise<QualityCheck> {
//     try {
//       console.log('Creating quality check with data:', data);
      
//       if (!data.name || !data.type || !data.batchId) {
//         throw new Error('Missing required fields: name, type, or batchId');
//       }
      
//       const transformedData = {
//         name: data.name,
//         type: data.type.toUpperCase(),
//         batchId: Number(data.batchId),
//         checkDate: data.checkDate ? new Date(data.checkDate).toISOString() : new Date().toISOString(),
//         status: data.status?.toUpperCase() || 'PENDING',
//         notes: data.notes || '',
//         checkedById: Number(data.checkedById)
//       };
      
//       console.log('Transformed data:', transformedData);
      
//       const response = await api.post('/quality-checks', transformedData);
//       return response.data;
//     } catch (error: any) {
//       console.error('Error creating quality check:', error);
//       console.error('Request data was:', data);
      
//       if (error.response?.status === 404) {
//         throw new Error('Quality checks API endpoint not found. Please check your backend configuration.');
//       }
      
//       if (error.response?.status === 403) {
//         throw new Error('Access denied. You may not have the required permissions (ADMIN or QUALITY role).');
//       }
      
//       if (error.response?.status === 400) {
//         const errorData = error.response.data;
//         if (typeof errorData === 'object' && errorData !== null) {
//           const errorMessages = Object.entries(errorData).map(([field, message]) => `${field}: ${message}`).join(', ');
//           throw new Error(`Validation errors: ${errorMessages}`);
//         }
//         throw new Error('Invalid data provided');
//       }
      
//       throw error;
//     }
//   },

//   async getById(id: number): Promise<QualityCheck> {
//     const response = await api.get(`/quality-checks/${id}`);
//     return response.data;
//   },

//   async update(id: number, data: Partial<QualityCheck>): Promise<QualityCheck> {
//     const transformedData = {
//       ...data,
//       type: data.type?.toUpperCase(),
//       status: data.status?.toUpperCase(),
//       batchId: data.batchId ? Number(data.batchId) : undefined,
//       checkedById: data.checkedById ? Number(data.checkedById) : undefined,
//       checkDate: data.checkDate ? new Date(data.checkDate).toISOString() : undefined
//     };
    
//     const response = await api.put(`/quality-checks/${id}`, transformedData);
//     return response.data;
//   },

//   async updateStatus(id: number, status: string): Promise<QualityCheck> {
//     const response = await api.patch(`/quality-checks/${id}/status?status=${status.toUpperCase()}`);
//     return response.data;
//   },

//   async delete(id: number): Promise<void> {
//     await api.delete(`/quality-checks/${id}`);
//   },

//   async getByBatch(batchId: number): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get(`/quality-checks/batch/${batchId}`);
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return [];
//       }
//       throw error;
//     }
//   },

//   async getByStatus(status: string): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get(`/quality-checks/status/${status.toUpperCase()}`);
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return [];
//       }
//       throw error;
//     }
//   }
// };

// import api from '../utils/api';
// import { QualityCheck } from '../types';

// export const qualityService = {
//   async getAll(): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get('/quality-checks');
//       return response.data;
//     } catch (error: any) {
//       console.error('Error fetching quality checks:', error);
//       if (error.response?.status === 404) {
//         console.warn('Quality checks endpoint not found, returning empty array');
//         return [];
//       }
//       throw error;
//     }
//   },

//   async create(data: Omit<QualityCheck, 'id'>): Promise<QualityCheck> {
//     try {
//       console.log('Creating quality check with data:', data);
      
//       if (!data.name || !data.type || !data.batchId) {
//         throw new Error('Missing required fields: name, type, or batchId');
//       }
      
//       const transformedData = {
//         name: data.name,
//         type: data.type.toUpperCase(),
//         batchId: Number(data.batchId),
//         checkDate: data.checkDate ? new Date(data.checkDate).toISOString() : new Date().toISOString(),
//         status: data.status?.toUpperCase() || 'PENDING',
//         notes: data.notes || '',
//         checkedById: Number(data.checkedById)
//       };
      
//       console.log('Transformed data:', transformedData);
      
//       const response = await api.post('/quality-checks', transformedData);
//       return response.data;
//     } catch (error: any) {
//       console.error('Error creating quality check:', error);
//       console.error('Request data was:', data);
      
//       if (error.response?.status === 404) {
//         throw new Error('Quality checks API endpoint not found. Please check your backend configuration.');
//       }
      
//       if (error.response?.status === 403) {
//         throw new Error('Access denied. You may not have the required permissions.');
//       }
      
//       if (error.response?.status === 400) {
//         const errorData = error.response.data;
//         if (typeof errorData === 'object' && errorData !== null && errorData.error) {
//           throw new Error(errorData.error);
//         }
//         throw new Error('Invalid data provided');
//       }
      
//       throw new Error('Failed to create quality check. Please try again.');
//     }
//   },

//   async getById(id: number): Promise<QualityCheck> {
//     const response = await api.get(`/quality-checks/${id}`);
//     return response.data;
//   },

//   async update(id: number, data: Partial<QualityCheck>): Promise<QualityCheck> {
//     const transformedData = {
//       ...data,
//       type: data.type?.toUpperCase(),
//       status: data.status?.toUpperCase(),
//       batchId: data.batchId ? Number(data.batchId) : undefined,
//       checkedById: data.checkedById ? Number(data.checkedById) : undefined,
//       checkDate: data.checkDate ? new Date(data.checkDate).toISOString() : undefined
//     };
    
//     const response = await api.put(`/quality-checks/${id}`, transformedData);
//     return response.data;
//   },

//   async updateStatus(id: number, status: string): Promise<QualityCheck> {
//     const response = await api.patch(`/quality-checks/${id}/status?status=${status.toUpperCase()}`);
//     return response.data;
//   },

//   async delete(id: number): Promise<void> {
//     await api.delete(`/quality-checks/${id}`);
//   },

//   async getByBatch(batchId: number): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get(`/quality-checks/batch/${batchId}`);
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return [];
//       }
//       throw error;
//     }
//   },

//   async getByStatus(status: string): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get(`/quality-checks/status/${status.toUpperCase()}`);
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return [];
//       }
//       throw error;
//     }
//   }
// };

// import api from '../utils/api';
// import { QualityCheck } from '../types';

// export const qualityService = {
//   async getAll(): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get('/quality-checks');
//       return response.data;
//     } catch (error: any) {
//       console.error('Error fetching quality checks:', error);
//       if (error.response?.status === 404) {
//         console.warn('Quality checks endpoint not found, returning empty array');
//         return [];
//       }
//       throw error;
//     }
//   },

//   async create(data: Omit<QualityCheck, 'id'>): Promise<QualityCheck> {
//     try {
//       console.log('Creating quality check with data:', data);
      
//       if (!data.name || !data.type || !data.batchId) {
//         throw new Error('Missing required fields: name, type, or batchId');
//       }
      
//       // Transform data to match backend DTO
//       const transformedData = {
//         name: data.name,
//         type: data.type.toUpperCase(),
//         batchId: Number(data.batchId),
//         checkDate: data.checkDate ? new Date(data.checkDate).toISOString() : new Date().toISOString(),
//         status: data.status?.toUpperCase() || 'PENDING',
//         notes: data.notes || '',
//         checkedById: Number(data.checkedById)
//       };
      
//       console.log('Transformed data:', transformedData);
      
//       const response = await api.post('/quality-checks', transformedData);
//       return response.data;
//     } catch (error: any) {
//       console.error('Error creating quality check:', error);
//       console.error('Request data was:', data);
      
//       if (error.response?.status === 404) {
//         throw new Error('Quality checks API endpoint not found. Please check your backend configuration.');
//       }
      
//       if (error.response?.status === 403) {
//         throw new Error('Access denied. You may not have the required permissions.');
//       }
      
//       if (error.response?.status === 400) {
//         const errorData = error.response.data;
//         if (typeof errorData === 'object' && errorData !== null && errorData.error) {
//           throw new Error(errorData.error);
//         }
//         throw new Error('Invalid data provided');
//       }
      
//       throw new Error('Failed to create quality check. Please try again.');
//     }
//   },

//   async getById(id: number): Promise<QualityCheck> {
//     const response = await api.get(`/quality-checks/${id}`);
//     return response.data;
//   },

//   async update(id: number, data: Partial<QualityCheck>): Promise<QualityCheck> {
//     const transformedData = {
//       name: data.name,
//       type: data.type?.toUpperCase(),
//       batchId: data.batchId ? Number(data.batchId) : undefined,
//       checkDate: data.checkDate ? new Date(data.checkDate).toISOString() : undefined,
//       status: data.status?.toUpperCase(),
//       notes: data.notes || '',
//       checkedById: data.checkedById ? Number(data.checkedById) : undefined
//     };
    
//     const response = await api.put(`/quality-checks/${id}`, transformedData);
//     return response.data;
//   },

//   async updateStatus(id: number, status: string): Promise<QualityCheck> {
//     const response = await api.patch(`/quality-checks/${id}/status?status=${status.toUpperCase()}`);
//     return response.data;
//   },

//   async delete(id: number): Promise<void> {
//     await api.delete(`/quality-checks/${id}`);
//   },

//   async getByBatch(batchId: number): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get(`/quality-checks/batch/${batchId}`);
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return [];
//       }
//       throw error;
//     }
//   },

//   async getByStatus(status: string): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get(`/quality-checks/status/${status.toUpperCase()}`);
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return [];
//       }
//       throw error;
//     }
//   },

//   async getStatistics(): Promise<{ [key: string]: number }> {
//     try {
//       const response = await api.get('/quality-checks/statistics');
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return { PASSED: 0, FAILED: 0, PENDING: 0 };
//       }
//       throw error;
//     }
//   }
// };


// import api from '../utils/api';
// import { QualityCheck } from '../types';

// export const qualityService = {
//   async getAll(): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get('/quality-checks');
//       return response.data;
//     } catch (error: any) {
//       console.error('Error fetching quality checks:', error);
//       if (error.response?.status === 404) {
//         console.warn('Quality checks endpoint not found, returning empty array');
//         return [];
//       }
//       throw error;
//     }
//   },

//   async create(data: Omit<QualityCheck, 'id'>): Promise<QualityCheck> {
//     try {
//       console.log('Creating quality check with data:', data);
      
//       if (!data.name || !data.type || !data.batchId) {
//         throw new Error('Missing required fields: name, type, or batchId');
//       }
      
//       // Transform data to match backend DTO
//       const transformedData = {
//         name: data.name,
//         type: data.type.toUpperCase(),
//         batchId: Number(data.batchId),
//         checkDate: data.checkDate ? new Date(data.checkDate).toISOString() : new Date().toISOString(),
//         status: data.status?.toUpperCase() || 'PENDING',
//         notes: data.notes || '',
//         checkedById: Number(data.checkedById)
//       };
      
//       console.log('Transformed data:', transformedData);
      
//       const response = await api.post('/quality-checks', transformedData);
//       return response.data;
//     } catch (error: any) {
//       console.error('Error creating quality check:', error);
//       console.error('Request data was:', data);
      
//       if (error.response?.status === 404) {
//         throw new Error('Quality checks API endpoint not found. Please check your backend configuration.');
//       }
      
//       if (error.response?.status === 403) {
//         throw new Error('Access denied. You may not have the required permissions.');
//       }
      
//       if (error.response?.status === 400) {
//         const errorData = error.response.data;
//         if (typeof errorData === 'object' && errorData !== null && errorData.error) {
//           throw new Error(errorData.error);
//         }
//         throw new Error('Invalid data provided');
//       }
      
//       // Handle network errors
//       if (error.code === 'ECONNREFUSED' || error.message.includes('Network Error')) {
//         throw new Error('Cannot connect to server. Please check if the backend is running.');
//       }
      
//       throw new Error('Failed to create quality check. Please try again.');
//     }
//   },

//   async getById(id: number): Promise<QualityCheck> {
//     const response = await api.get(`/quality-checks/${id}`);
//     return response.data;
//   },

//   async update(id: number, data: Partial<QualityCheck>): Promise<QualityCheck> {
//     const transformedData = {
//       name: data.name,
//       type: data.type?.toUpperCase(),
//       batchId: data.batchId ? Number(data.batchId) : undefined,
//       checkDate: data.checkDate ? new Date(data.checkDate).toISOString() : undefined,
//       status: data.status?.toUpperCase(),
//       notes: data.notes || '',
//       checkedById: data.checkedById ? Number(data.checkedById) : undefined
//     };
    
//     const response = await api.put(`/quality-checks/${id}`, transformedData);
//     return response.data;
//   },

//   async updateStatus(id: number, status: string): Promise<QualityCheck> {
//     const response = await api.patch(`/quality-checks/${id}/status?status=${status.toUpperCase()}`);
//     return response.data;
//   },

//   async delete(id: number): Promise<void> {
//     await api.delete(`/quality-checks/${id}`);
//   },

//   async getByBatch(batchId: number): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get(`/quality-checks/batch/${batchId}`);
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return [];
//       }
//       throw error;
//     }
//   },

//   async getByStatus(status: string): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get(`/quality-checks/status/${status.toUpperCase()}`);
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return [];
//       }
//       throw error;
//     }
//   },

//   async getStatistics(): Promise<{ [key: string]: number }> {
//     try {
//       const response = await api.get('/quality-checks/statistics');
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return { PASSED: 0, FAILED: 0, PENDING: 0 };
//       }
//       throw error;
//     }
//   }
// };



// import api from '../utils/api';
// import { QualityCheck } from '../types';

// export const qualityService = {
//   async getAll(): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get('/quality-checks');
//       return response.data;
//     } catch (error: any) {
//       console.error('Error fetching quality checks:', error);
//       if (error.response?.status === 404) {
//         console.warn('Quality checks endpoint not found, returning empty array');
//         return [];
//       }
//       throw error;
//     }
//   },

//   async create(data: Omit<QualityCheck, 'id'>): Promise<QualityCheck> {
//     try {
//       console.log('Creating quality check with data:', data);
      
//       if (!data.name || !data.type || !data.batchId) {
//         throw new Error('Missing required fields: name, type, or batchId');
//       }
      
//       // Transform data to match backend DTO
//       const transformedData = {
//         name: data.name,
//         type: data.type.toUpperCase(),
//         batchId: Number(data.batchId),
//         checkDate: data.checkDate ? new Date(data.checkDate).toISOString() : new Date().toISOString(),
//         status: data.status?.toUpperCase() || 'PENDING',
//         notes: data.notes || '',
//         checkedById: Number(data.checkedById)
//       };
      
//       console.log('Transformed data:', transformedData);
      
//       const response = await api.post('/quality-checks', transformedData);
//       return response.data;
//     } catch (error: any) {
//       console.error('Error creating quality check:', error);
//       console.error('Request data was:', data);
      
//       if (error.response?.status === 404) {
//         throw new Error('Quality checks API endpoint not found. Please check your backend configuration.');
//       }
      
//       if (error.response?.status === 403) {
//         throw new Error('Access denied. You may not have the required permissions.');
//       }
      
//       if (error.response?.status === 400) {
//         const errorData = error.response.data;
//         if (typeof errorData === 'object' && errorData !== null && errorData.error) {
//           throw new Error(errorData.error);
//         }
//         throw new Error('Invalid data provided');
//       }
      
//       throw new Error('Failed to create quality check. Please try again.');
//     }
//   },

//   async getById(id: number): Promise<QualityCheck> {
//     const response = await api.get(`/quality-checks/${id}`);
//     return response.data;
//   },

//   async update(id: number, data: Partial<QualityCheck>): Promise<QualityCheck> {
//     const transformedData = {
//       name: data.name,
//       type: data.type?.toUpperCase(),
//       batchId: data.batchId ? Number(data.batchId) : undefined,
//       checkDate: data.checkDate ? new Date(data.checkDate).toISOString() : undefined,
//       status: data.status?.toUpperCase(),
//       notes: data.notes || '',
//       checkedById: data.checkedById ? Number(data.checkedById) : undefined
//     };
    
//     const response = await api.put(`/quality-checks/${id}`, transformedData);
//     return response.data;
//   },

//   async updateStatus(id: number, status: string): Promise<QualityCheck> {
//     const response = await api.patch(`/quality-checks/${id}/status?status=${status.toUpperCase()}`);
//     return response.data;
//   },

//   async delete(id: number): Promise<void> {
//     await api.delete(`/quality-checks/${id}`);
//   },

//   async getByBatch(batchId: number): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get(`/quality-checks/batch/${batchId}`);
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return [];
//       }
//       throw error;
//     }
//   },

//   async getByStatus(status: string): Promise<QualityCheck[]> {
//     try {
//       const response = await api.get(`/quality-checks/status/${status.toUpperCase()}`);
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return [];
//       }
//       throw error;
//     }
//   },

//   async getStatistics(): Promise<{ [key: string]: number }> {
//     try {
//       const response = await api.get('/quality-checks/statistics');
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return { PASSED: 0, FAILED: 0, PENDING: 0 };
//       }
//       throw error;
//     }
//   }
// };


import api from '../utils/api';
import { QualityCheck } from '../types';

export const qualityService = {
  async getAll(): Promise<QualityCheck[]> {
    try {
      const response = await api.get('/quality-checks');
      return response.data;
    } catch (error: any) {
      console.error('Error fetching quality checks:', error);
      if (error.response?.status === 404) {
        console.warn('Quality checks endpoint not found, returning empty array');
        return [];
      }
      throw error;
    }
  },

  async create(data: Omit<QualityCheck, 'id'>): Promise<QualityCheck> {
    try {
      console.log('Creating quality check with data:', data);
      
      if (!data.name || !data.type || !data.batchId) {
        throw new Error('Missing required fields: name, type, or batchId');
      }

      // Transform data to match backend DTO exactly
      const transformedData = {
        name: String(data.name).trim(),
        type: String(data.type).toUpperCase(),
        batchId: Number(data.batchId),
        checkDate: data.checkDate ? new Date(data.checkDate).toISOString() : null,
        status: String(data.status || 'PENDING').toUpperCase(),
        notes: data.notes ? String(data.notes).trim() : null,
        checkedById: Number(data.checkedById)
      };

      // Validate transformed data
      if (!transformedData.name) {
        throw new Error('Name cannot be empty');
      }
      if (isNaN(transformedData.batchId) || transformedData.batchId <= 0) {
        throw new Error('Valid batch ID is required');
      }
      if (isNaN(transformedData.checkedById) || transformedData.checkedById <= 0) {
        throw new Error('Valid user ID is required');
      }

      console.log('Transformed data:', transformedData);
      
      const response = await api.post('/quality-checks', transformedData);
      return response.data;
    } catch (error: any) {
      console.error('Error creating quality check:', error);
      console.error('Request data was:', data);
      
      if (error.response?.status === 404) {
        throw new Error('Quality checks API endpoint not found. Please check your backend configuration.');
      }
      
      if (error.response?.status === 403) {
        throw new Error('Access denied. You may not have the required permissions.');
      }
      
      if (error.response?.status === 400) {
        const errorData = error.response.data;
        if (typeof errorData === 'object' && errorData !== null) {
          // Handle validation errors
          if (errorData.error) {
            throw new Error(errorData.error);
          }
          // Handle field validation errors
          const fieldErrors = Object.entries(errorData)
            .map(([field, message]) => `${field}: ${message}`)
            .join(', ');
          if (fieldErrors) {
            throw new Error(`Validation errors: ${fieldErrors}`);
          }
        }
        throw new Error('Invalid data provided. Please check all required fields.');
      }
      
      throw new Error('Failed to create quality check. Please try again.');
    }
  },

  async getById(id: number): Promise<QualityCheck> {
    const response = await api.get(`/quality-checks/${id}`);
    return response.data;
  },

  async update(id: number, data: Partial<QualityCheck>): Promise<QualityCheck> {
    const transformedData = {
      name: data.name ? String(data.name).trim() : undefined,
      type: data.type ? String(data.type).toUpperCase() : undefined,
      batchId: data.batchId ? Number(data.batchId) : undefined,
      checkDate: data.checkDate ? new Date(data.checkDate).toISOString() : undefined,
      status: data.status ? String(data.status).toUpperCase() : undefined,
      notes: data.notes ? String(data.notes).trim() : null,
      checkedById: data.checkedById ? Number(data.checkedById) : undefined
    };
    
    const response = await api.put(`/quality-checks/${id}`, transformedData);
    return response.data;
  },

  async updateStatus(id: number, status: string): Promise<QualityCheck> {
    const response = await api.patch(`/quality-checks/${id}/status?status=${status.toUpperCase()}`);
    return response.data;
  },

  async delete(id: number): Promise<void> {
    await api.delete(`/quality-checks/${id}`);
  },

  async getByBatch(batchId: number): Promise<QualityCheck[]> {
    try {
      const response = await api.get(`/quality-checks/batch/${batchId}`);
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return [];
      }
      throw error;
    }
  },

  async getByStatus(status: string): Promise<QualityCheck[]> {
    try {
      const response = await api.get(`/quality-checks/status/${status.toUpperCase()}`);
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return [];
      }
      throw error;
    }
  },

  async getStatistics(): Promise<{ [key: string]: number }> {
    try {
      const response = await api.get('/quality-checks/statistics');
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return { PASSED: 0, FAILED: 0, PENDING: 0 };
      }
      throw error;
    }
  }
};
