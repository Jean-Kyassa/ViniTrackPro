// import api from '../utils/api';
// import { ProductionTask } from '../types';

// export const productionTaskService = {
//   getAll: async (): Promise<ProductionTask[]> => {
//     try {
//       const response = await api.get('/production-tasks');
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         console.warn('Production tasks endpoint not available, returning empty array');
//         return [];
//       }
//       throw error;
//     }
//   },

//   getById: async (id: number): Promise<ProductionTask> => {
//     const response = await api.get(`/production-tasks/${id}`);
//     return response.data;
//   },

//   create: async (task: Omit<ProductionTask, 'id'>): Promise<ProductionTask> => {
//     const response = await api.post('/production-tasks', task);
//     return response.data;
//   },

//   update: async (id: number, task: Partial<ProductionTask>): Promise<ProductionTask> => {
//     const response = await api.put(`/production-tasks/${id}`, task);
//     return response.data;
//   },

//   delete: async (id: number): Promise<void> => {
//     await api.delete(`/production-tasks/${id}`);
//   },

//   getByStatus: async (status: string): Promise<ProductionTask[]> => {
//     try {
//       const response = await api.get(`/production-tasks?status=${status}`);
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return [];
//       }
//       throw error;
//     }
//   },

//   getBySchedule: async (scheduleId: number): Promise<ProductionTask[]> => {
//     try {
//       const response = await api.get(`/production-tasks?scheduleId=${scheduleId}`);
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return [];
//       }
//       throw error;
//     }
//   },

//   startTask: async (id: number): Promise<ProductionTask> => {
//     const response = await api.put(`/production-tasks/${id}/start`);
//     return response.data;
//   },

//   completeTask: async (id: number): Promise<ProductionTask> => {
//     const response = await api.put(`/production-tasks/${id}/complete`);
//     return response.data;
//   },
// };

import api from '../utils/api';
import { ProductionTask } from '../types';

export const productionTaskService = {
  getAll: async (): Promise<ProductionTask[]> => {
    try {
      const response = await api.get('/production-tasks');
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        console.warn('Production tasks endpoint not available, returning empty array');
        return [];
      }
      throw error;
    }
  },

  getById: async (id: number): Promise<ProductionTask> => {
    const response = await api.get(`/production-tasks/${id}`);
    return response.data;
  },

  create: async (task: Omit<ProductionTask, 'id'>): Promise<ProductionTask> => {
    console.log('Creating production task with data:', task);
    
    // Format the task data properly
    const formattedTask = {
      ...task,
    };

    // Format date fields if they exist
    const taskData = formattedTask as any;
    
    if (taskData.dueDate) {
      taskData.dueDate = new Date(taskData.dueDate).toISOString().slice(0, 19);
    }
    if (taskData.estimatedStart) {
      taskData.estimatedStart = new Date(taskData.estimatedStart).toISOString().slice(0, 19);
    }
    if (taskData.estimatedEnd) {
      taskData.estimatedEnd = new Date(taskData.estimatedEnd).toISOString().slice(0, 19);
    }

    console.log('Sending formatted task data:', taskData);

    const response = await api.post('/production-tasks', taskData);
    return response.data;
  },

  update: async (id: number, task: Partial<ProductionTask>): Promise<ProductionTask> => {
    // Format the task data properly
    const formattedTask = { ...task };
    const taskData = formattedTask as any;

    // Format date fields if they exist
    if (taskData.dueDate) {
      taskData.dueDate = new Date(taskData.dueDate).toISOString().slice(0, 19);
    }
    if (taskData.estimatedStart) {
      taskData.estimatedStart = new Date(taskData.estimatedStart).toISOString().slice(0, 19);
    }
    if (taskData.estimatedEnd) {
      taskData.estimatedEnd = new Date(taskData.estimatedEnd).toISOString().slice(0, 19);
    }

    const response = await api.put(`/production-tasks/${id}`, taskData);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/production-tasks/${id}`);
  },

  getByStatus: async (status: string): Promise<ProductionTask[]> => {
    try {
      const response = await api.get(`/production-tasks?status=${status}`);
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return [];
      }
      throw error;
    }
  },

  getBySchedule: async (scheduleId: number): Promise<ProductionTask[]> => {
    try {
      const response = await api.get(`/production-tasks?scheduleId=${scheduleId}`);
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return [];
      }
      throw error;
    }
  },

  startTask: async (id: number): Promise<ProductionTask> => {
    const response = await api.put(`/production-tasks/${id}/start`);
    return response.data;
  },

  completeTask: async (id: number): Promise<ProductionTask> => {
    const response = await api.put(`/production-tasks/${id}/complete`);
    return response.data;
  },
};
