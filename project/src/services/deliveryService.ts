// import api from '../utils/api';
// import { DeliveryOrder, PaginatedResponse, DeliveryStats } from '../types';

// export const deliveryService = {
//   getAll: async (page = 0, size = 10): Promise<PaginatedResponse<DeliveryOrder>> => {
//     const response = await api.get('/deliveries', {
//       params: { page, size }
//     });
//     return response.data;
//   },

//   getById: async (id: number): Promise<DeliveryOrder> => {
//     const response = await api.get(`/deliveries/${id}`);
//     return response.data;
//   },

//   create: async (order: Omit<DeliveryOrder, 'id'>): Promise<DeliveryOrder> => {
//     try {
//       // Validate required fields before sending
//       if (!order.customerId) {
//         throw new Error('Customer is required');
//       }
//       if (!order.deliveryAddress?.trim()) {
//         throw new Error('Delivery address is required');
//       }
//       if (!order.totalAmount || order.totalAmount <= 0) {
//         throw new Error('Total amount must be greater than 0');
//       }

//       // Prepare order data - let backend handle orderDate
//       const orderData = {
//         orderNumber: order.orderNumber || `ORD-${Date.now()}`,
//         customerId: Number(order.customerId),
//         deliveryAddress: order.deliveryAddress.trim(),
//         status: order.status || 'PENDING',
//         deliveryInstructions: order.deliveryInstructions || '',
//         deliveryFee: Number(order.deliveryFee) || 0,
//         totalAmount: Number(order.totalAmount),
//         items: order.items || []
//       };

//       console.log('Sending order data:', orderData);
            
//       const response = await api.post('/deliveries', orderData);
//       return response.data;
//     } catch (error: any) {
//       console.error('Error creating delivery order:', error);
            
//       if (error.response?.data) {
//         // If backend returns a string error message
//         if (typeof error.response.data === 'string') {
//           throw new Error(error.response.data);
//         }
//         // If backend returns an object with message
//         if (error.response.data.message) {
//           throw new Error(error.response.data.message);
//         }
//         // If backend returns validation errors
//         if (error.response.data.errors) {
//           throw new Error(Object.values(error.response.data.errors).join(', '));
//         }
//       }
            
//       // If it's our custom validation error
//       if (error.message && !error.response) {
//         throw error;
//       }
            
//       // Generic error
//       throw new Error('Failed to create delivery order. Please check your input and try again.');
//     }
//   },

//   update: async (id: number, order: Partial<DeliveryOrder>): Promise<DeliveryOrder> => {
//     try {
//       // Prepare update data without orderDate
//       const updateData = {
//         ...order,
//         customerId: order.customerId ? Number(order.customerId) : undefined,
//         deliveryFee: order.deliveryFee ? Number(order.deliveryFee) : undefined,
//         totalAmount: order.totalAmount ? Number(order.totalAmount) : undefined
//       };
      
//       // Remove undefined values
//       // Object.keys(updateData).forEach(key => 
//       //   updateData[key] === undefined && delete updateData[key]
//       // );

//       const response = await api.put(`/deliveries/${id}`, updateData);
//       return response.data;
//     } catch (error: any) {
//       console.error('Error updating delivery order:', error);
//       if (error.response?.data?.message) {
//         throw new Error(error.response.data.message);
//       }
//       throw new Error('Failed to update delivery order');
//     }
//   },

//   delete: async (id: number): Promise<void> => {
//     await api.delete(`/deliveries/${id}`);
//   },

//   updateStatus: async (id: number, status: string): Promise<DeliveryOrder> => {
//     const response = await api.patch(`/deliveries/${id}/status`, null, {
//       params: { status }
//     });
//     return response.data;
//   },

//   search: async (keyword: string, page = 0, size = 10): Promise<PaginatedResponse<DeliveryOrder>> => {
//     const response = await api.get('/deliveries/search', {
//       params: { keyword, page, size }
//     });
//     return response.data;
//   },

//   getStatistics: async (): Promise<DeliveryStats> => {
//     try {
//       const response = await api.get('/deliveries/statistics');
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         console.warn('Delivery statistics endpoint not available, returning default stats');
//         return {
//           totalOrders: 0,
//           pendingOrders: 0,
//           processingOrders: 0,
//           inTransitOrders: 0,
//           deliveredOrders: 0,
//           cancelledOrders: 0
//         };
//       }
//       throw error;
//     }
//   },

//   getPending: async (): Promise<DeliveryOrder[]> => {
//     try {
//       const response = await api.get('/deliveries/pending');
//       return response.data;
//     } catch (error: any) {
//       if (error.response?.status === 404) {
//         return [];
//       }
//       throw error;
//     }
//   },

//   getByStatus: async (status: string): Promise<DeliveryOrder[]> => {
//     const response = await api.get(`/deliveries/status/${status}`);
//     return response.data;
//   },

//   markAsDelivered: async (id: number): Promise<DeliveryOrder> => {
//     const response = await api.patch(`/deliveries/${id}/deliver`);
//     return response.data;
//   },

//   cancelOrder: async (id: number): Promise<DeliveryOrder> => {
//     const response = await api.patch(`/deliveries/${id}/cancel`);
//     return response.data;
//   },

//   startProcessing: async (id: number): Promise<DeliveryOrder> => {
//     const response = await api.patch(`/deliveries/${id}/process`);
//     return response.data;
//   },

//   markInTransit: async (id: number): Promise<DeliveryOrder> => {
//     const response = await api.patch(`/deliveries/${id}/transit`);
//     return response.data;
//   },
// };


import api from '../utils/api';
import { DeliveryOrder, PaginatedResponse, DeliveryStats } from '../types';

export const deliveryService = {
  getAll: async (page = 0, size = 10): Promise<PaginatedResponse<DeliveryOrder>> => {
    const response = await api.get('/deliveries', {
      params: { page, size }
    });
    return response.data;
  },

  getById: async (id: number): Promise<DeliveryOrder> => {
    const response = await api.get(`/deliveries/${id}`);
    return response.data;
  },

  create: async (order: Omit<DeliveryOrder, 'id'>): Promise<DeliveryOrder> => {
    try {
      // Validate required fields before sending
      if (!order.customerId) {
        throw new Error('Customer is required');
      }
      if (!order.deliveryAddress?.trim()) {
        throw new Error('Delivery address is required');
      }
      if (!order.totalAmount || order.totalAmount <= 0) {
        throw new Error('Total amount must be greater than 0');
      }

      // Prepare order data - let backend handle orderDate
      const orderData = {
        orderNumber: order.orderNumber || `ORD-${Date.now()}`,
        customerId: Number(order.customerId),
        deliveryAddress: order.deliveryAddress.trim(),
        status: order.status || 'PENDING',
        deliveryInstructions: order.deliveryInstructions || '',
        deliveryFee: Number(order.deliveryFee) || 0,
        totalAmount: Number(order.totalAmount),
        items: order.items || []
      };

      console.log('Sending order data:', orderData);
            
      const response = await api.post('/deliveries', orderData);
      return response.data;
    } catch (error: any) {
      console.error('Error creating delivery order:', error);
            
      if (error.response?.data) {
        // If backend returns a string error message
        if (typeof error.response.data === 'string') {
          throw new Error(error.response.data);
        }
        // If backend returns an object with message
        if (error.response.data.message) {
          throw new Error(error.response.data.message);
        }
        // If backend returns validation errors
        if (error.response.data.errors) {
          throw new Error(Object.values(error.response.data.errors).join(', '));
        }
      }
            
      // If it's our custom validation error
      if (error.message && !error.response) {
        throw error;
      }
            
      // Generic error
      throw new Error('Failed to create delivery order. Please check your input and try again.');
    }
  },

  update: async (id: number, order: Partial<DeliveryOrder>): Promise<DeliveryOrder> => {
    try {
      // Prepare update data without orderDate
      const updateData = {
        ...order,
        customerId: order.customerId ? Number(order.customerId) : undefined,
        deliveryFee: order.deliveryFee ? Number(order.deliveryFee) : undefined,
        totalAmount: order.totalAmount ? Number(order.totalAmount) : undefined
      };
      
      // Remove undefined values
      // Object.keys(updateData).forEach(key => 
      //   updateData[key] === undefined && delete updateData[key]
      // );

      const response = await api.put(`/deliveries/${id}`, updateData);
      return response.data;
    } catch (error: any) {
      console.error('Error updating delivery order:', error);
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to update delivery order');
    }
  },

  delete: async (id: number): Promise<void> => {
    try {
      await api.delete(`/deliveries/${id}`);
    } catch (error: any) {
      console.error('Error deleting delivery order:', error);
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      if (error.response?.status === 404) {
        throw new Error('Delivery order not found');
      }
      if (error.response?.status === 409) {
        throw new Error('Cannot delete delivery order. It may be referenced by other records.');
      }
      throw new Error('Failed to delete delivery order');
    }
  },

  updateStatus: async (id: number, status: string): Promise<DeliveryOrder> => {
    const response = await api.patch(`/deliveries/${id}/status`, null, {
      params: { status }
    });
    return response.data;
  },

  search: async (keyword: string, page = 0, size = 10): Promise<PaginatedResponse<DeliveryOrder>> => {
    const response = await api.get('/deliveries/search', {
      params: { keyword, page, size }
    });
    return response.data;
  },

  getStatistics: async (): Promise<DeliveryStats> => {
    try {
      const response = await api.get('/deliveries/statistics');
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        console.warn('Delivery statistics endpoint not available, returning default stats');
        return {
          totalOrders: 0,
          pendingOrders: 0,
          processingOrders: 0,
          inTransitOrders: 0,
          deliveredOrders: 0,
          cancelledOrders: 0
        };
      }
      throw error;
    }
  },

  getPending: async (): Promise<DeliveryOrder[]> => {
    try {
      const response = await api.get('/deliveries/pending');
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        return [];
      }
      throw error;
    }
  },

  getByStatus: async (status: string): Promise<DeliveryOrder[]> => {
    const response = await api.get(`/deliveries/status/${status}`);
    return response.data;
  },

  markAsDelivered: async (id: number): Promise<DeliveryOrder> => {
    const response = await api.patch(`/deliveries/${id}/deliver`);
    return response.data;
  },

  cancelOrder: async (id: number): Promise<DeliveryOrder> => {
    const response = await api.patch(`/deliveries/${id}/cancel`);
    return response.data;
  },

  startProcessing: async (id: number): Promise<DeliveryOrder> => {
    const response = await api.patch(`/deliveries/${id}/process`);
    return response.data;
  },

  markInTransit: async (id: number): Promise<DeliveryOrder> => {
    const response = await api.patch(`/deliveries/${id}/transit`);
    return response.data;
  },
};
