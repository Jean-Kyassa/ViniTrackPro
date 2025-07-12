// import api from '../utils/api';
// import { ProductionLine } from '../types';

// export const productionLineService = {
//   getAll: async (): Promise<ProductionLine[]> => {
//     try {
//       const response = await api.get('/production-lines');
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         console.warn('Production lines endpoint not available, returning empty array');
//         return [];
//       }
//       throw error;
//     }
//   },

//   // getAll: async (): Promise<ProductionLine[]> => {
//   //   const response = await api.get('/production-lines');
//   //   return response.data;
//   // },

//   getById: async (id: number): Promise<ProductionLine> => {
//     const response = await api.get(`/production-lines/${id}`);
//     return response.data;
//   },

//   create: async (line: Omit<ProductionLine, 'id'>): Promise<ProductionLine> => {
//     const response = await api.post('/production-lines', line);
//     return response.data;
//   },

//   update: async (id: number, line: Partial<ProductionLine>): Promise<ProductionLine> => {
//     const response = await api.put(`/production-lines/${id}`, line);
//     return response.data;
//   },

//   delete: async (id: number): Promise<void> => {
//     await api.delete(`/production-lines/${id}`);
//   },

//   getByType: async (type: string): Promise<ProductionLine[]> => {
//     try {
//       const response = await api.get(`/production-lines?type=${type}`);
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return [];
//       }
//       throw error;
//     }
//   },

//   getByStatus: async (status: string): Promise<ProductionLine[]> => {
//     try {
//       const response = await api.get(`/production-lines?status=${status}`);
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return [];
//       }
//       throw error;
//     }
//   },

//   getNeedingMaintenance: async (threshold: number): Promise<ProductionLine[]> => {
//     try {
//       const response = await api.get(`/production-lines?efficiencyThreshold=${threshold}`);
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
import { ProductionLine } from '../types';

export const productionLineService = {
  getAll: async (): Promise<ProductionLine[]> => {
    try {
      const response = await api.get('/production-lines');
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        console.warn('Production lines endpoint not available, returning empty array');
        return [];
      }
      throw error;
    }
  },

  getById: async (id: number): Promise<ProductionLine> => {
    const response = await api.get(`/production-lines/${id}`);
    return response.data;
  },

  create: async (line: Omit<ProductionLine, 'id'>): Promise<ProductionLine> => {
    // Format dates properly for LocalDateTime (YYYY-MM-DDTHH:mm:ss)
    const formattedLine = {
      ...line,
      installationDate: line.installationDate ? 
        new Date(line.installationDate).toISOString().slice(0, 19) : null,
      lastMaintenanceDate: line.lastMaintenanceDate ? 
        new Date(line.lastMaintenanceDate).toISOString().slice(0, 19) : null,
      lastMaintenance: line.lastMaintenance ? 
        new Date(line.lastMaintenance).toISOString().slice(0, 19) : null,
    };

    console.log('Sending production line data:', formattedLine);

    const response = await api.post('/production-lines', formattedLine);
    return response.data;
  },

  update: async (id: number, line: Partial<ProductionLine>): Promise<ProductionLine> => {
    // Format dates if they exist
    const formattedLine = {
      ...line,
      installationDate: line.installationDate ? 
        new Date(line.installationDate).toISOString().slice(0, 19) : line.installationDate,
      lastMaintenanceDate: line.lastMaintenanceDate ? 
        new Date(line.lastMaintenanceDate).toISOString().slice(0, 19) : line.lastMaintenanceDate,
      lastMaintenance: line.lastMaintenance ? 
        new Date(line.lastMaintenance).toISOString().slice(0, 19) : line.lastMaintenance,
    };

    const response = await api.put(`/production-lines/${id}`, formattedLine);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/production-lines/${id}`);
  },

  getByType: async (type: string): Promise<ProductionLine[]> => {
    try {
      const response = await api.get(`/production-lines?type=${type}`);
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return [];
      }
      throw error;
    }
  },

  getByStatus: async (status: string): Promise<ProductionLine[]> => {
    try {
      const response = await api.get(`/production-lines?status=${status}`);
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return [];
      }
      throw error;
    }
  },

  getNeedingMaintenance: async (threshold: number): Promise<ProductionLine[]> => {
    try {
      const response = await api.get(`/production-lines?efficiencyThreshold=${threshold}`);
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return [];
      }
      throw error;
    }
  },
};
