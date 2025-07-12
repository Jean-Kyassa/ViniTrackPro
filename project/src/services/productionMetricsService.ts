// import api from "../utils/api"

// export interface ProductionMetrics {
//   totalProducts: number
//   totalBatches: number
//   activeBatches: number
//   totalProductionLines: number
//   operationalLines: number
//   totalTasks: number
//   completedTasks: number
//   pendingTasks: number
//   efficiency: number
//   completedTasksToday: number
// }

// export const productionMetricsService = {
//   getMetrics: async (): Promise<ProductionMetrics> => {
//     try {
//       // Since there's no single metrics endpoint, we'll aggregate from multiple sources
//       const [products, batches, lines, tasks] = await Promise.all([
//         api.get("/products").catch(() => ({ data: [] })),
//         api.get("/production-batches").catch(() => ({ data: [] })),
//         api.get("/production-lines").catch(() => ({ data: [] })),
//         api.get("/production-tasks").catch(() => ({ data: [] })),
//       ])

//       const productsData = products.data || []
//       const batchesData = batches.data || []
//       const linesData = lines.data || []
//       const tasksData = tasks.data || []

//       const operationalLines = linesData.filter((line: any) => line.status === "OPERATIONAL").length
//       const completedTasks = tasksData.filter((task: any) => task.status === "COMPLETED").length
//       const pendingTasks = tasksData.filter((task: any) => task.status === "PENDING").length

//       // Calculate efficiency based on operational lines
//       const efficiency = linesData.length > 0 ? Math.round((operationalLines / linesData.length) * 100) : 100

//       // Mock completed tasks today (you can enhance this with actual date filtering)
//       const completedTasksToday = Math.floor(completedTasks * 0.1)

//       return {
//         totalProducts: productsData.length,
//         totalBatches: batchesData.length,
//         activeBatches: batchesData.filter((batch: any) => new Date(batch.expiryDate) > new Date()).length,
//         totalProductionLines: linesData.length,
//         operationalLines,
//         totalTasks: tasksData.length,
//         completedTasks,
//         pendingTasks,
//         efficiency,
//         completedTasksToday,
//       }
//     } catch (error) {
//       console.error("Error fetching production metrics:", error)
//       // Return default values on error
//       return {
//         totalProducts: 0,
//         totalBatches: 0,
//         activeBatches: 0,
//         totalProductionLines: 0,
//         operationalLines: 0,
//         totalTasks: 0,
//         completedTasks: 0,
//         pendingTasks: 0,
//         efficiency: 0,
//         completedTasksToday: 0,
//       }
//     }
//   },
// }


// import api from '../utils/api';

// export interface ProductionMetrics {
//   totalProducts: number;
//   totalBatches: number;
//   activeBatches: number;
//   totalProductionLines: number;
//   operationalLines: number;
//   totalTasks: number;
//   completedTasks: number;
//   pendingTasks: number;
//   //efficiency: number; // Added efficiency property
//   averageEfficiency: number; // Added averageEfficiency property
// }

// export interface DeliveryMetrics {
//   totalOrders: number;
//   pendingOrders: number;
//   processingOrders: number;
//   inTransitOrders: number;
//   deliveredOrders: number;
//   cancelledOrders: number;
//   totalDrivers: number;
//   activeDrivers: number;
//   totalVehicles: number;
//   totalRoutes: number;
// }

// export const productionMetricsService = {
//   getProductionMetrics: async (): Promise<ProductionMetrics> => {
//     try {
//       // Get all the data we need
//       const [products, batches, lines, tasks] = await Promise.all([
//         api.get('/products').catch(() => ({ data: [] })),
//         api.get('/production-batches').catch(() => ({ data: [] })),
//         api.get('/production-lines').catch(() => ({ data: [] })),
//         api.get('/production-tasks').catch(() => ({ data: [] }))
//       ]);

//       const productList = products.data || [];
//       const batchList = batches.data || [];
//       const lineList = lines.data || [];
//       const taskList = tasks.data || [];

//       // Calculate metrics
//       const activeBatches = batchList.filter((batch: any) => 
//         new Date(batch.expiryDate) > new Date()
//       ).length;

//       const operationalLines = lineList.filter((line: any) => 
//         line.status === 'OPERATIONAL'
//       ).length;

//       const completedTasks = taskList.filter((task: any) => 
//         task.status === 'COMPLETED'
//       ).length;

//       const pendingTasks = taskList.filter((task: any) => 
//         task.status === 'PENDING'
//       ).length;

//       const averageEfficiency = lineList.length > 0 
//         ? lineList.reduce((sum: number, line: any) => sum + (line.efficiency || 0), 0) / lineList.length
//         : 0;

//       return {
//         totalProducts: productList.length,
//         totalBatches: batchList.length,
//         activeBatches,
//         totalProductionLines: lineList.length,
//         operationalLines,
//         totalTasks: taskList.length,
//         completedTasks,
//         pendingTasks,
//         averageEfficiency: Math.round(averageEfficiency * 100) / 100
//       };
//     } catch (error) {
//       console.error('Error fetching production metrics:', error);
//       // Return default values
//       return {
//         totalProducts: 0,
//         totalBatches: 0,
//         activeBatches: 0,
//         totalProductionLines: 0,
//         operationalLines: 0,
//         totalTasks: 0,
//         completedTasks: 0,
//         pendingTasks: 0,
//         averageEfficiency: 0
//       };
//     }
//   },

//   getDeliveryMetrics: async (): Promise<DeliveryMetrics> => {
//     try {
//       // Get all the data we need
//       const [orders, drivers, vehicles, routes] = await Promise.all([
//         api.get('/delivery-orders').catch(() => ({ data: [] })),
//         api.get('/drivers').catch(() => ({ data: [] })),
//         api.get('/vehicles').catch(() => ({ data: [] })),
//         api.get('/delivery-routes').catch(() => ({ data: [] }))
//       ]);

//       const orderList = orders.data || [];
//       const driverList = drivers.data || [];
//       const vehicleList = vehicles.data || [];
//       const routeList = routes.data || [];

//       // Calculate order metrics
//       const pendingOrders = orderList.filter((order: any) => order.status === 'PENDING').length;
//       const processingOrders = orderList.filter((order: any) => order.status === 'PROCESSING').length;
//       const inTransitOrders = orderList.filter((order: any) => order.status === 'IN_TRANSIT').length;
//       const deliveredOrders = orderList.filter((order: any) => order.status === 'DELIVERED').length;
//       const cancelledOrders = orderList.filter((order: any) => order.status === 'CANCELLED').length;

//       const activeDrivers = driverList.filter((driver: any) => driver.status === 'ACTIVE').length;

//       return {
//         totalOrders: orderList.length,
//         pendingOrders,
//         processingOrders,
//         inTransitOrders,
//         deliveredOrders,
//         cancelledOrders,
//         totalDrivers: driverList.length,
//         activeDrivers,
//         totalVehicles: vehicleList.length,
//         totalRoutes: routeList.length
//       };
//     } catch (error) {
//       console.error('Error fetching delivery metrics:', error);
//       // Return default values
//       return {
//         totalOrders: 0,
//         pendingOrders: 0,
//         processingOrders: 0,
//         inTransitOrders: 0,
//         deliveredOrders: 0,
//         cancelledOrders: 0,
//         totalDrivers: 0,
//         activeDrivers: 0,
//         totalVehicles: 0,
//         totalRoutes: 0
//       };
//     }
//   }
// };



import api from '../utils/api';

export interface ProductionMetrics {
  totalProducts: number;
  totalBatches: number;
  activeBatches: number;
  totalProductionLines: number;
  operationalLines: number;
  totalTasks: number;
  completedTasks: number;
  pendingTasks: number;
  averageEfficiency: number;
}

export interface DeliveryMetrics {
  totalOrders: number;
  pendingOrders: number;
  processingOrders: number;
  inTransitOrders: number;
  deliveredOrders: number;
  cancelledOrders: number;
  totalDrivers: number;
  activeDrivers: number;
  totalVehicles: number;
  totalRoutes: number;
}

export const productionMetricsService = {
  getProductionMetrics: async (): Promise<ProductionMetrics> => {
    try {
      // Try to get metrics from dedicated endpoint first
      const response = await api.get('/metrics/production');
      return response.data;
    } catch (error) {
      console.warn('Production metrics endpoint not available, calculating from individual services');
      
      // Fallback: calculate metrics from individual endpoints
      try {
        const [products, batches, lines, tasks] = await Promise.all([
          api.get('/products').catch(() => ({ data: [] })),
          api.get('/production-batches').catch(() => ({ data: [] })),
          api.get('/production-lines').catch(() => ({ data: [] })),
          api.get('/production-tasks').catch(() => ({ data: [] }))
        ]);

        const productList = products.data || [];
        const batchList = batches.data || [];
        const lineList = lines.data || [];
        const taskList = tasks.data || [];

        // Calculate metrics
        const activeBatches = batchList.filter((batch: any) => 
          new Date(batch.expiryDate) > new Date()
        ).length;

        const operationalLines = lineList.filter((line: any) => 
          line.status === 'OPERATIONAL'
        ).length;

        const completedTasks = taskList.filter((task: any) => 
          task.status === 'COMPLETED'
        ).length;

        const pendingTasks = taskList.filter((task: any) => 
          task.status === 'PENDING'
        ).length;

        const averageEfficiency = lineList.length > 0 
          ? lineList.reduce((sum: number, line: any) => sum + (line.efficiency || 0), 0) / lineList.length
          : 0;

        return {
          totalProducts: productList.length,
          totalBatches: batchList.length,
          activeBatches,
          totalProductionLines: lineList.length,
          operationalLines,
          totalTasks: taskList.length,
          completedTasks,
          pendingTasks,
          averageEfficiency: Math.round(averageEfficiency * 100) / 100
        };
      } catch (fallbackError) {
        console.error('Error calculating production metrics:', fallbackError);
        // Return default values
        return {
          totalProducts: 0,
          totalBatches: 0,
          activeBatches: 0,
          totalProductionLines: 0,
          operationalLines: 0,
          totalTasks: 0,
          completedTasks: 0,
          pendingTasks: 0,
          averageEfficiency: 0
        };
      }
    }
  },

  getDeliveryMetrics: async (): Promise<DeliveryMetrics> => {
    try {
      // Try to get metrics from dedicated endpoint first
      const response = await api.get('/metrics/delivery');
      return response.data;
    } catch (error) {
      console.warn('Delivery metrics endpoint not available, calculating from individual services');
      
      // Fallback: calculate metrics from individual endpoints
      try {
        const [orders, drivers, vehicles, routes] = await Promise.all([
          api.get('/delivery-orders').catch(() => ({ data: [] })),
          api.get('/drivers').catch(() => ({ data: [] })),
          api.get('/vehicles').catch(() => ({ data: [] })),
          api.get('/delivery-routes').catch(() => ({ data: [] }))
        ]);

        const orderList = orders.data || [];
        const driverList = drivers.data || [];
        const vehicleList = vehicles.data || [];
        const routeList = routes.data || [];

        // Calculate order metrics
        const pendingOrders = orderList.filter((order: any) => order.status === 'PENDING').length;
        const processingOrders = orderList.filter((order: any) => order.status === 'PROCESSING').length;
        const inTransitOrders = orderList.filter((order: any) => order.status === 'IN_TRANSIT').length;
        const deliveredOrders = orderList.filter((order: any) => order.status === 'DELIVERED').length;
        const cancelledOrders = orderList.filter((order: any) => order.status === 'CANCELLED').length;

        const activeDrivers = driverList.filter((driver: any) => driver.status === 'ACTIVE').length;

        return {
          totalOrders: orderList.length,
          pendingOrders,
          processingOrders,
          inTransitOrders,
          deliveredOrders,
          cancelledOrders,
          totalDrivers: driverList.length,
          activeDrivers,
          totalVehicles: vehicleList.length,
          totalRoutes: routeList.length
        };
      } catch (fallbackError) {
        console.error('Error calculating delivery metrics:', fallbackError);
        // Return default values
        return {
          totalOrders: 0,
          pendingOrders: 0,
          processingOrders: 0,
          inTransitOrders: 0,
          deliveredOrders: 0,
          cancelledOrders: 0,
          totalDrivers: 0,
          activeDrivers: 0,
          totalVehicles: 0,
          totalRoutes: 0
        };
      }
    }
  }
};