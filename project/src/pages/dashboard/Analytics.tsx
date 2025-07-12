import React, { useState, useEffect } from 'react';
import { BarChart3, TrendingUp, Package, Users, Truck, DollarSign } from 'lucide-react';
import { Card } from '../../components/ui/Card';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { inventoryService } from '../../services/inventoryService';
import { deliveryService } from '../../services/deliveryService';
import { customerService } from '../../services/customerService';
import { supplierService } from '../../services/supplierService';

interface AnalyticsData {
  inventory: {
    totalItems: number;
    totalValue: number;
    lowStockItems: number;
  };
  delivery: {
    totalOrders: number;
    pendingOrders: number;
    deliveredOrders: number;
    totalRevenue: number;
  };
  customers: number;
  suppliers: number;
}

export const Analytics: React.FC = () => {
  const [data, setData] = useState<AnalyticsData | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    fetchAnalyticsData();
  }, []);

  const fetchAnalyticsData = async () => {
    try {
      setIsLoading(true);
      setError('');

      // Initialize default values
      let inventoryStats = { totalItems: 0, totalValue: 0, lowStockItems: 0 };
      let deliveryStats = { totalOrders: 0, pendingOrders: 0, deliveredOrders: 0 };
      let customersCount = 0;
      let suppliersCount = 0;

      // Fetch data with error handling
      try {
        inventoryStats = await inventoryService.getStats();
      } catch (error) {
        console.warn('Inventory stats not available:', error);
      }

      try {
        deliveryStats = await deliveryService.getStatistics();
      } catch (error) {
        console.warn('Delivery stats not available:', error);
      }

      try {
        const customersData = await customerService.getAll(0, 1);
        customersCount = customersData.totalElements || 0;
      } catch (error) {
        console.warn('Customers data not available:', error);
      }

      try {
        const suppliersData = await supplierService.getAll(0, 1);
        suppliersCount = suppliersData.totalElements || 0;
      } catch (error) {
        console.warn('Suppliers data not available:', error);
      }

      setData({
        inventory: inventoryStats,
        delivery: {
          ...deliveryStats,
          totalRevenue: 125000, // Mock data since this might not be available
        },
        customers: customersCount,
        suppliers: suppliersCount,
      });
    } catch (error) {
      console.error('Error fetching analytics data:', error);
      setError('Failed to load analytics data');
      // Set default data even on error
      setData({
        inventory: { totalItems: 0, totalValue: 0, lowStockItems: 0 },
        delivery: { totalOrders: 0, pendingOrders: 0, deliveredOrders: 0, totalRevenue: 0 },
        customers: 0,
        suppliers: 0,
      });
    } finally {
      setIsLoading(false);
    }
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  if (!data) {
    return (
      <div className="text-center py-12">
        <p className="text-gray-500 dark:text-gray-400">No analytics data available</p>
        {error && <p className="text-red-500 text-sm mt-2">{error}</p>}
      </div>
    );
  }

  const kpiCards = [
    {
      title: 'Total Revenue',
      value: `$${(data.delivery.totalRevenue || 0).toLocaleString()}`,
      change: '+12.5%',
      changeType: 'positive',
      icon: DollarSign,
      color: 'bg-green-500',
    },
    {
      title: 'Total Orders',
      value: (data.delivery.totalOrders || 0).toString(),
      change: '+8.2%',
      changeType: 'positive',
      icon: Truck,
      color: 'bg-blue-500',
    },
    {
      title: 'Inventory Value',
      value: `$${(data.inventory.totalValue || 0).toLocaleString()}`,
      change: '+3.1%',
      changeType: 'positive',
      icon: Package,
      color: 'bg-purple-500',
    },
    {
      title: 'Active Customers',
      value: (data.customers || 0).toString(),
      change: '+15.3%',
      changeType: 'positive',
      icon: Users,
      color: 'bg-orange-500',
    },
  ];

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Analytics Dashboard</h1>
          <p className="text-gray-600 dark:text-gray-400">Business insights and performance metrics</p>
        </div>
      </div>

      {error && (
        <div className="bg-yellow-100 border border-yellow-400 text-yellow-700 px-4 py-3 rounded">
          <p className="font-medium">Warning:</p>
          <p>{error}</p>
        </div>
      )}

      {/* KPI Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {kpiCards.map((kpi) => (
          <Card key={kpi.title} className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
                  {kpi.title}
                </p>
                <p className="text-2xl font-bold text-gray-900 dark:text-white">
                  {kpi.value}
                </p>
                <div className="mt-2 flex items-center">
                  <TrendingUp className="h-4 w-4 text-green-500 mr-1" />
                  <span className="text-green-600 dark:text-green-400 text-sm font-medium">
                    {kpi.change}
                  </span>
                  <span className="text-gray-500 dark:text-gray-400 text-sm ml-1">
                    vs last month
                  </span>
                </div>
              </div>
              <div className={`${kpi.color} p-3 rounded-lg`}>
                <kpi.icon className="h-6 w-6 text-white" />
              </div>
            </div>
          </Card>
        ))}
      </div>

      {/* Charts and Detailed Analytics */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Inventory Overview */}
        <Card className="p-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-lg font-semibold text-gray-900 dark:text-white">
              Inventory Overview
            </h2>
            <Package className="h-5 w-5 text-gray-400" />
          </div>
          <div className="space-y-4">
            <div className="flex justify-between items-center">
              <span className="text-gray-600 dark:text-gray-400">Total Items</span>
              <span className="font-semibold">{data.inventory.totalItems || 0}</span>
            </div>
            <div className="flex justify-between items-center">
              <span className="text-gray-600 dark:text-gray-400">Low Stock Items</span>
              <span className="font-semibold text-orange-600">{data.inventory.lowStockItems || 0}</span>
            </div>
            <div className="flex justify-between items-center">
              <span className="text-gray-600 dark:text-gray-400">Total Value</span>
              <span className="font-semibold">${(data.inventory.totalValue || 0).toLocaleString()}</span>
            </div>
            <div className="mt-4">
              <div className="flex justify-between text-sm mb-1">
                <span className="text-gray-600 dark:text-gray-400">Stock Health</span>
                <span className="text-gray-900 dark:text-white">
                  {data.inventory.totalItems > 0 
                    ? Math.round(((data.inventory.totalItems - data.inventory.lowStockItems) / data.inventory.totalItems) * 100)
                    : 100}%
                </span>
              </div>
              <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
                <div 
                  className="bg-green-500 h-2 rounded-full" 
                  style={{ 
                    width: `${data.inventory.totalItems > 0 
                      ? Math.round(((data.inventory.totalItems - data.inventory.lowStockItems) / data.inventory.totalItems) * 100)
                      : 100}%` 
                  }}
                />
              </div>
            </div>
          </div>
        </Card>

        {/* Order Status Distribution */}
        <Card className="p-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-lg font-semibold text-gray-900 dark:text-white">
              Order Status
            </h2>
            <Truck className="h-5 w-5 text-gray-400" />
          </div>
          <div className="space-y-4">
            <div className="flex justify-between items-center">
              <span className="text-gray-600 dark:text-gray-400">Total Orders</span>
              <span className="font-semibold">{data.delivery.totalOrders || 0}</span>
            </div>
            <div className="flex justify-between items-center">
              <span className="text-gray-600 dark:text-gray-400">Pending</span>
              <span className="font-semibold text-yellow-600">{data.delivery.pendingOrders || 0}</span>
            </div>
            <div className="flex justify-between items-center">
              <span className="text-gray-600 dark:text-gray-400">Delivered</span>
              <span className="font-semibold text-green-600">{data.delivery.deliveredOrders || 0}</span>
            </div>
            <div className="mt-4">
              <div className="flex justify-between text-sm mb-1">
                <span className="text-gray-600 dark:text-gray-400">Completion Rate</span>
                <span className="text-gray-900 dark:text-white">
                  {data.delivery.totalOrders > 0 
                    ? Math.round((data.delivery.deliveredOrders / data.delivery.totalOrders) * 100)
                    : 0}%
                </span>
              </div>
              <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
                <div 
                  className="bg-blue-500 h-2 rounded-full" 
                  style={{ 
                    width: `${data.delivery.totalOrders > 0 
                      ? Math.round((data.delivery.deliveredOrders / data.delivery.totalOrders) * 100)
                      : 0}%` 
                  }}
                />
              </div>
            </div>
          </div>
        </Card>

        {/* Business Partners */}
        <Card className="p-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-lg font-semibold text-gray-900 dark:text-white">
              Business Partners
            </h2>
            <Users className="h-5 w-5 text-gray-400" />
          </div>
          <div className="space-y-4">
            <div className="flex justify-between items-center">
              <span className="text-gray-600 dark:text-gray-400">Active Customers</span>
              <span className="font-semibold">{data.customers || 0}</span>
            </div>
            <div className="flex justify-between items-center">
              <span className="text-gray-600 dark:text-gray-400">Suppliers</span>
              <span className="font-semibold">{data.suppliers || 0}</span>
            </div>
            <div className="flex justify-between items-center">
              <span className="text-gray-600 dark:text-gray-400">Partner Ratio</span>
              <span className="font-semibold">
                {data.customers > 0 ? (data.suppliers / data.customers).toFixed(2) : '0.00'}
              </span>
            </div>
          </div>
        </Card>

        {/* Performance Metrics */}
        <Card className="p-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-lg font-semibold text-gray-900 dark:text-white">
              Performance Metrics
            </h2>
            <BarChart3 className="h-5 w-5 text-gray-400" />
          </div>
          <div className="space-y-4">
            <div>
              <div className="flex justify-between text-sm mb-1">
                <span className="text-gray-600 dark:text-gray-400">Order Fulfillment</span>
                <span className="text-gray-900 dark:text-white">94%</span>
              </div>
              <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
                <div className="bg-green-500 h-2 rounded-full w-[94%]" />
              </div>
            </div>
            <div>
              <div className="flex justify-between text-sm mb-1">
                <span className="text-gray-600 dark:text-gray-400">Customer Satisfaction</span>
                <span className="text-gray-900 dark:text-white">87%</span>
              </div>
              <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
                <div className="bg-blue-500 h-2 rounded-full w-[87%]" />
              </div>
            </div>
            <div>
              <div className="flex justify-between text-sm mb-1">
                <span className="text-gray-600 dark:text-gray-400">Inventory Turnover</span>
                <span className="text-gray-900 dark:text-white">76%</span>
              </div>
              <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
                <div className="bg-purple-500 h-2 rounded-full w-[76%]" />
              </div>
            </div>
          </div>
        </Card>
      </div>

      {/* Recent Activity Summary */}
      <Card className="p-6">
        <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">
          Recent Activity Summary
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="text-center">
            <div className="text-2xl font-bold text-blue-600 dark:text-blue-400">
              {data.delivery.pendingOrders || 0}
            </div>
            <div className="text-sm text-gray-600 dark:text-gray-400">Orders Pending</div>
          </div>
          <div className="text-center">
            <div className="text-2xl font-bold text-orange-600 dark:text-orange-400">
              {data.inventory.lowStockItems || 0}
            </div>
            <div className="text-sm text-gray-600 dark:text-gray-400">Items Low Stock</div>
          </div>
          <div className="text-center">
            <div className="text-2xl font-bold text-green-600 dark:text-green-400">
              ${(data.delivery.totalRevenue || 0).toLocaleString()}
            </div>
            <div className="text-sm text-gray-600 dark:text-gray-400">Monthly Revenue</div>
          </div>
        </div>
      </Card>
    </div>
  );
};


// "use client"

// import type React from "react"
// import { useState, useEffect } from "react"
// import { BarChart3, TrendingUp, Package, Users, Truck, Factory, CheckSquare } from "lucide-react"
// import { Card } from "../../components/ui/Card"
// import { LoadingSpinner } from "../../components/ui/LoadingSpinner"
// import { inventoryService } from "../../services/inventoryService"
// import { customerService } from "../../services/customerService"
// import { supplierService } from "../../services/supplierService"
// import { productionMetricsService, type ProductionMetrics } from "../../services/productionMetricsService"
// import { deliveryMetricsService, type DeliveryMetrics } from "../../services/deliveryMetricsService"
// import { useAuth } from "../../context/AuthContext"

// interface AnalyticsData {
//   inventory: {
//     totalItems: number
//     totalValue: number
//     lowStockItems: number
//   }
//   delivery: DeliveryMetrics
//   production: ProductionMetrics
//   customers: number
//   suppliers: number
// }

// export const Analytics: React.FC = () => {
//   const { hasRole } = useAuth()
//   const [data, setData] = useState<AnalyticsData | null>(null)
//   const [isLoading, setIsLoading] = useState(true)
//   const [error, setError] = useState<string>("")

//   useEffect(() => {
//     fetchAnalyticsData()
//   }, [])

//   const fetchAnalyticsData = async () => {
//     try {
//       setIsLoading(true)
//       setError("")

//       // Initialize default values
//       let inventoryStats = { totalItems: 0, totalValue: 0, lowStockItems: 0 }
//       let deliveryStats: DeliveryMetrics = {
//         totalOrders: 0,
//         pendingOrders: 0,
//         processingOrders: 0,
//         inTransitOrders: 0,
//         deliveredOrders: 0,
//         cancelledOrders: 0,
//         totalDrivers: 0,
//         activeDrivers: 0,
//         totalVehicles: 0,
//         totalRoutes: 0,
//         averageDeliveryTime: 0,
//         onTimeDeliveryRate: 0,
//       }
//       let productionStats: ProductionMetrics = {
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
//       let customersCount = 0
//       let suppliersCount = 0

//       // Fetch data with error handling
//       const promises = []

//       if (hasRole("ROLE_INVENTORY") || hasRole("ROLE_ADMIN")) {
//         promises.push(
//           inventoryService.getStats().catch(() => inventoryStats),
//           supplierService
//             .getAll(0, 1)
//             .then((data) => data.totalElements || 0)
//             .catch(() => 0),
//         )
//       }

//       if (hasRole("ROLE_LOGISTICS") || hasRole("ROLE_ADMIN")) {
//         promises.push(
//           deliveryMetricsService.getMetrics().catch(() => deliveryStats),
//           customerService
//             .getAll(0, 1)
//             .then((data) => data.totalElements || 0)
//             .catch(() => 0),
//         )
//       }

//       if (hasRole("ROLE_PRODUCTION") || hasRole("ROLE_ADMIN")) {
//         promises.push(productionMetricsService.getMetrics().catch(() => productionStats))
//       }

//       const results = await Promise.all(promises)

//       let resultIndex = 0
//       if (hasRole("ROLE_INVENTORY") || hasRole("ROLE_ADMIN")) {
//         const inventoryResult = results[resultIndex++];
//         if (typeof inventoryResult === 'object' && 'totalItems' in inventoryResult && 'totalValue' in inventoryResult && 'lowStockItems' in inventoryResult) {
//           inventoryStats = inventoryResult as { totalItems: number; totalValue: number; lowStockItems: number; };
//         }
//         const suppliersResult = results[resultIndex++];
//         suppliersCount = typeof suppliersResult === 'number' ? suppliersResult : 0;
//       }

//       if (hasRole("ROLE_LOGISTICS") || hasRole("ROLE_ADMIN")) {
//         const deliveryResult = results[resultIndex++];
//         if (typeof deliveryResult === 'object' && 'totalOrders' in deliveryResult) {
//           deliveryStats = deliveryResult as DeliveryMetrics;
//         }
//         const customersResult = results[resultIndex++];
//         customersCount = typeof customersResult === 'number' ? customersResult : 0;
//       }

//       if (hasRole("ROLE_PRODUCTION") || hasRole("ROLE_ADMIN")) {
//         const productionResult = results[resultIndex++];
//         if (typeof productionResult === 'object' && 'totalProducts' in productionResult) {
//           productionStats = productionResult as ProductionMetrics;
//         }
//       }

//       setData({
//         inventory: inventoryStats,
//         delivery: deliveryStats,
//         production: productionStats,
//         customers: customersCount,
//         suppliers: suppliersCount,
//       })
//     } catch (error) {
//       console.error("Error fetching analytics data:", error)
//       setError("Failed to load analytics data")
//       // Set default data even on error
//       setData({
//         inventory: { totalItems: 0, totalValue: 0, lowStockItems: 0 },
//         delivery: {
//           totalOrders: 0,
//           pendingOrders: 0,
//           processingOrders: 0,
//           inTransitOrders: 0,
//           deliveredOrders: 0,
//           cancelledOrders: 0,
//           totalDrivers: 0,
//           activeDrivers: 0,
//           totalVehicles: 0,
//           totalRoutes: 0,
//           averageDeliveryTime: 0,
//           onTimeDeliveryRate: 0,
//         },
//         production: {
//           totalProducts: 0,
//           totalBatches: 0,
//           activeBatches: 0,
//           totalProductionLines: 0,
//           operationalLines: 0,
//           totalTasks: 0,
//           completedTasks: 0,
//           pendingTasks: 0,
//           efficiency: 0,
//           completedTasksToday: 0,
//         },
//         customers: 0,
//         suppliers: 0,
//       })
//     } finally {
//       setIsLoading(false)
//     }
//   }

//   if (isLoading) {
//     return (
//       <div className="flex items-center justify-center h-64">
//         <LoadingSpinner size="lg" />
//       </div>
//     )
//   }

//   if (!data) {
//     return (
//       <div className="text-center py-12">
//         <p className="text-gray-500 dark:text-gray-400">No analytics data available</p>
//         {error && <p className="text-red-500 text-sm mt-2">{error}</p>}
//       </div>
//     )
//   }

//   const kpiCards = [
//     ...(hasRole("ROLE_LOGISTICS") || hasRole("ROLE_ADMIN")
//       ? [
//           {
//             title: "Total Orders",
//             value: (data.delivery.totalOrders || 0).toString(),
//             change: "+8.2%",
//             changeType: "positive" as const,
//             icon: Truck,
//             color: "bg-blue-500",
//           },
//         ]
//       : []),
//     ...(hasRole("ROLE_INVENTORY") || hasRole("ROLE_ADMIN")
//       ? [
//           {
//             title: "Inventory Value",
//             value: `$${(data.inventory.totalValue || 0).toLocaleString()}`,
//             change: "+3.1%",
//             changeType: "positive" as const,
//             icon: Package,
//             color: "bg-purple-500",
//           },
//         ]
//       : []),
//     ...(hasRole("ROLE_PRODUCTION") || hasRole("ROLE_ADMIN")
//       ? [
//           {
//             title: "Production Efficiency",
//             value: `${data.production.efficiency || 0}%`,
//             change: "+5.2%",
//             changeType: "positive" as const,
//             icon: Factory,
//             color: "bg-green-500",
//           },
//         ]
//       : []),
//     ...(hasRole("ROLE_LOGISTICS") || hasRole("ROLE_ADMIN")
//       ? [
//           {
//             title: "Active Customers",
//             value: (data.customers || 0).toString(),
//             change: "+15.3%",
//             changeType: "positive" as const,
//             icon: Users,
//             color: "bg-orange-500",
//           },
//         ]
//       : []),
//   ]

//   return (
//     <div className="space-y-6">
//       <div className="flex justify-between items-center">
//         <div>
//           <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Analytics Dashboard</h1>
//           <p className="text-gray-600 dark:text-gray-400">Business insights and performance metrics</p>
//         </div>
//       </div>

//       {error && (
//         <div className="bg-yellow-100 border border-yellow-400 text-yellow-700 px-4 py-3 rounded">
//           <p className="font-medium">Warning:</p>
//           <p>{error}</p>
//         </div>
//       )}

//       {/* KPI Cards */}
//       <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
//         {kpiCards.map((kpi) => (
//           <Card key={kpi.title} className="p-6">
//             <div className="flex items-center justify-between">
//               <div>
//                 <p className="text-sm font-medium text-gray-600 dark:text-gray-400">{kpi.title}</p>
//                 <p className="text-2xl font-bold text-gray-900 dark:text-white">{kpi.value}</p>
//                 <div className="mt-2 flex items-center">
//                   <TrendingUp className="h-4 w-4 text-green-500 mr-1" />
//                   <span className="text-green-600 dark:text-green-400 text-sm font-medium">{kpi.change}</span>
//                   <span className="text-gray-500 dark:text-gray-400 text-sm ml-1">vs last month</span>
//                 </div>
//               </div>
//               <div className={`${kpi.color} p-3 rounded-lg`}>
//                 <kpi.icon className="h-6 w-6 text-white" />
//               </div>
//             </div>
//           </Card>
//         ))}
//       </div>

//       {/* Charts and Detailed Analytics */}
//       <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-6">
//         {/* Inventory Overview */}
//         {(hasRole("ROLE_INVENTORY") || hasRole("ROLE_ADMIN")) && (
//           <Card className="p-6">
//             <div className="flex items-center justify-between mb-4">
//               <h2 className="text-lg font-semibold text-gray-900 dark:text-white">Inventory Overview</h2>
//               <Package className="h-5 w-5 text-gray-400" />
//             </div>
//             <div className="space-y-4">
//               <div className="flex justify-between items-center">
//                 <span className="text-gray-600 dark:text-gray-400">Total Items</span>
//                 <span className="font-semibold">{data.inventory.totalItems || 0}</span>
//               </div>
//               <div className="flex justify-between items-center">
//                 <span className="text-gray-600 dark:text-gray-400">Low Stock Items</span>
//                 <span className="font-semibold text-orange-600">{data.inventory.lowStockItems || 0}</span>
//               </div>
//               <div className="flex justify-between items-center">
//                 <span className="text-gray-600 dark:text-gray-400">Total Value</span>
//                 <span className="font-semibold">${(data.inventory.totalValue || 0).toLocaleString()}</span>
//               </div>
//               <div className="mt-4">
//                 <div className="flex justify-between text-sm mb-1">
//                   <span className="text-gray-600 dark:text-gray-400">Stock Health</span>
//                   <span className="text-gray-900 dark:text-white">
//                     {data.inventory.totalItems > 0
//                       ? Math.round(
//                           ((data.inventory.totalItems - data.inventory.lowStockItems) / data.inventory.totalItems) *
//                             100,
//                         )
//                       : 100}
//                     %
//                   </span>
//                 </div>
//                 <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
//                   <div
//                     className="bg-green-500 h-2 rounded-full"
//                     style={{
//                       width: `${
//                         data.inventory.totalItems > 0
//                           ? Math.round(
//                               ((data.inventory.totalItems - data.inventory.lowStockItems) / data.inventory.totalItems) *
//                                 100,
//                             )
//                           : 100
//                       }%`,
//                     }}
//                   />
//                 </div>
//               </div>
//             </div>
//           </Card>
//         )}

//         {/* Production Overview */}
//         {(hasRole("ROLE_PRODUCTION") || hasRole("ROLE_ADMIN")) && (
//           <Card className="p-6">
//             <div className="flex items-center justify-between mb-4">
//               <h2 className="text-lg font-semibold text-gray-900 dark:text-white">Production Overview</h2>
//               <Factory className="h-5 w-5 text-gray-400" />
//             </div>
//             <div className="space-y-4">
//               <div className="flex justify-between items-center">
//                 <span className="text-gray-600 dark:text-gray-400">Total Products</span>
//                 <span className="font-semibold">{data.production.totalProducts || 0}</span>
//               </div>
//               <div className="flex justify-between items-center">
//                 <span className="text-gray-600 dark:text-gray-400">Active Batches</span>
//                 <span className="font-semibold text-blue-600">{data.production.activeBatches || 0}</span>
//               </div>
//               <div className="flex justify-between items-center">
//                 <span className="text-gray-600 dark:text-gray-400">Operational Lines</span>
//                 <span className="font-semibold text-green-600">{data.production.operationalLines || 0}</span>
//               </div>
//               <div className="flex justify-between items-center">
//                 <span className="text-gray-600 dark:text-gray-400">Pending Tasks</span>
//                 <span className="font-semibold text-orange-600">{data.production.pendingTasks || 0}</span>
//               </div>
//               <div className="mt-4">
//                 <div className="flex justify-between text-sm mb-1">
//                   <span className="text-gray-600 dark:text-gray-400">Efficiency</span>
//                   <span className="text-gray-900 dark:text-white">{data.production.efficiency || 0}%</span>
//                 </div>
//                 <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
//                   <div
//                     className="bg-green-500 h-2 rounded-full"
//                     style={{ width: `${data.production.efficiency || 0}%` }}
//                   />
//                 </div>
//               </div>
//             </div>
//           </Card>
//         )}

//         {/* Delivery Status */}
//         {(hasRole("ROLE_LOGISTICS") || hasRole("ROLE_ADMIN")) && (
//           <Card className="p-6">
//             <div className="flex items-center justify-between mb-4">
//               <h2 className="text-lg font-semibold text-gray-900 dark:text-white">Delivery Status</h2>
//               <Truck className="h-5 w-5 text-gray-400" />
//             </div>
//             <div className="space-y-4">
//               <div className="flex justify-between items-center">
//                 <span className="text-gray-600 dark:text-gray-400">Total Orders</span>
//                 <span className="font-semibold">{data.delivery.totalOrders || 0}</span>
//               </div>
//               <div className="flex justify-between items-center">
//                 <span className="text-gray-600 dark:text-gray-400">Pending</span>
//                 <span className="font-semibold text-yellow-600">{data.delivery.pendingOrders || 0}</span>
//               </div>
//               <div className="flex justify-between items-center">
//                 <span className="text-gray-600 dark:text-gray-400">In Transit</span>
//                 <span className="font-semibold text-blue-600">{data.delivery.inTransitOrders || 0}</span>
//               </div>
//               <div className="flex justify-between items-center">
//                 <span className="text-gray-600 dark:text-gray-400">Delivered</span>
//                 <span className="font-semibold text-green-600">{data.delivery.deliveredOrders || 0}</span>
//               </div>
//               <div className="mt-4">
//                 <div className="flex justify-between text-sm mb-1">
//                   <span className="text-gray-600 dark:text-gray-400">On-Time Rate</span>
//                   <span className="text-gray-900 dark:text-white">{data.delivery.onTimeDeliveryRate || 0}%</span>
//                 </div>
//                 <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
//                   <div
//                     className="bg-blue-500 h-2 rounded-full"
//                     style={{ width: `${data.delivery.onTimeDeliveryRate || 0}%` }}
//                   />
//                 </div>
//               </div>
//             </div>
//           </Card>
//         )}
//       </div>

//       {/* Performance Metrics */}
//       <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
//         {(hasRole("ROLE_LOGISTICS") || hasRole("ROLE_ADMIN")) && (
//           <Card className="p-6">
//             <div className="flex items-center justify-between mb-4">
//               <h2 className="text-lg font-semibold text-gray-900 dark:text-white">Delivery Performance</h2>
//               <BarChart3 className="h-5 w-5 text-gray-400" />
//             </div>
//             <div className="space-y-4">
//               <div>
//                 <div className="flex justify-between text-sm mb-1">
//                   <span className="text-gray-600 dark:text-gray-400">Order Fulfillment</span>
//                   <span className="text-gray-900 dark:text-white">
//                     {data.delivery.totalOrders > 0
//                       ? Math.round((data.delivery.deliveredOrders / data.delivery.totalOrders) * 100)
//                       : 0}
//                     %
//                   </span>
//                 </div>
//                 <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
//                   <div
//                     className="bg-green-500 h-2 rounded-full"
//                     style={{
//                       width: `${
//                         data.delivery.totalOrders > 0
//                           ? Math.round((data.delivery.deliveredOrders / data.delivery.totalOrders) * 100)
//                           : 0
//                       }%`,
//                     }}
//                   />
//                 </div>
//               </div>
//               <div>
//                 <div className="flex justify-between text-sm mb-1">
//                   <span className="text-gray-600 dark:text-gray-400">Driver Utilization</span>
//                   <span className="text-gray-900 dark:text-white">
//                     {data.delivery.totalDrivers > 0
//                       ? Math.round((data.delivery.activeDrivers / data.delivery.totalDrivers) * 100)
//                       : 0}
//                     %
//                   </span>
//                 </div>
//                 <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
//                   <div
//                     className="bg-blue-500 h-2 rounded-full"
//                     style={{
//                       width: `${
//                         data.delivery.totalDrivers > 0
//                           ? Math.round((data.delivery.activeDrivers / data.delivery.totalDrivers) * 100)
//                           : 0
//                       }%`,
//                     }}
//                   />
//                 </div>
//               </div>
//               <div className="grid grid-cols-2 gap-4 mt-4">
//                 <div className="text-center">
//                   <div className="text-lg font-bold text-blue-600">{data.delivery.totalDrivers}</div>
//                   <div className="text-xs text-gray-500">Total Drivers</div>
//                 </div>
//                 <div className="text-center">
//                   <div className="text-lg font-bold text-green-600">{data.delivery.totalVehicles}</div>
//                   <div className="text-xs text-gray-500">Total Vehicles</div>
//                 </div>
//               </div>
//             </div>
//           </Card>
//         )}

//         {(hasRole("ROLE_PRODUCTION") || hasRole("ROLE_ADMIN")) && (
//           <Card className="p-6">
//             <div className="flex items-center justify-between mb-4">
//               <h2 className="text-lg font-semibold text-gray-900 dark:text-white">Production Performance</h2>
//               <CheckSquare className="h-5 w-5 text-gray-400" />
//             </div>
//             <div className="space-y-4">
//               <div>
//                 <div className="flex justify-between text-sm mb-1">
//                   <span className="text-gray-600 dark:text-gray-400">Task Completion</span>
//                   <span className="text-gray-900 dark:text-white">
//                     {data.production.totalTasks > 0
//                       ? Math.round((data.production.completedTasks / data.production.totalTasks) * 100)
//                       : 0}
//                     %
//                   </span>
//                 </div>
//                 <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
//                   <div
//                     className="bg-green-500 h-2 rounded-full"
//                     style={{
//                       width: `${
//                         data.production.totalTasks > 0
//                           ? Math.round((data.production.completedTasks / data.production.totalTasks) * 100)
//                           : 0
//                       }%`,
//                     }}
//                   />
//                 </div>
//               </div>
//               <div>
//                 <div className="flex justify-between text-sm mb-1">
//                   <span className="text-gray-600 dark:text-gray-400">Line Efficiency</span>
//                   <span className="text-gray-900 dark:text-white">{data.production.efficiency}%</span>
//                 </div>
//                 <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
//                   <div className="bg-purple-500 h-2 rounded-full" style={{ width: `${data.production.efficiency}%` }} />
//                 </div>
//               </div>
//               <div className="grid grid-cols-2 gap-4 mt-4">
//                 <div className="text-center">
//                   <div className="text-lg font-bold text-blue-600">{data.production.totalBatches}</div>
//                   <div className="text-xs text-gray-500">Total Batches</div>
//                 </div>
//                 <div className="text-center">
//                   <div className="text-lg font-bold text-green-600">{data.production.completedTasksToday}</div>
//                   <div className="text-xs text-gray-500">Tasks Today</div>
//                 </div>
//               </div>
//             </div>
//           </Card>
//         )}
//       </div>

//       {/* Recent Activity Summary */}
//       <Card className="p-6">
//         <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">Recent Activity Summary</h2>
//         <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
//           {(hasRole("ROLE_LOGISTICS") || hasRole("ROLE_ADMIN")) && (
//             <div className="text-center">
//               <div className="text-2xl font-bold text-blue-600 dark:text-blue-400">
//                 {data.delivery.pendingOrders || 0}
//               </div>
//               <div className="text-sm text-gray-600 dark:text-gray-400">Orders Pending</div>
//             </div>
//           )}
//           {(hasRole("ROLE_INVENTORY") || hasRole("ROLE_ADMIN")) && (
//             <div className="text-center">
//               <div className="text-2xl font-bold text-orange-600 dark:text-orange-400">
//                 {data.inventory.lowStockItems || 0}
//               </div>
//               <div className="text-sm text-gray-600 dark:text-gray-400">Items Low Stock</div>
//             </div>
//           )}
//           {(hasRole("ROLE_PRODUCTION") || hasRole("ROLE_ADMIN")) && (
//             <div className="text-center">
//               <div className="text-2xl font-bold text-green-600 dark:text-green-400">
//                 {data.production.activeBatches || 0}
//               </div>
//               <div className="text-sm text-gray-600 dark:text-gray-400">Active Batches</div>
//             </div>
//           )}
//           {(hasRole("ROLE_PRODUCTION") || hasRole("ROLE_ADMIN")) && (
//             <div className="text-center">
//               <div className="text-2xl font-bold text-purple-600 dark:text-purple-400">
//                 {data.production.pendingTasks || 0}
//               </div>
//               <div className="text-sm text-gray-600 dark:text-gray-400">Pending Tasks</div>
//             </div>
//           )}
//         </div>
//       </Card>
//     </div>
//   )
// }
// export default Analytics;


// import React, { useState, useEffect } from 'react';
// import { BarChart3, TrendingUp, Package, Users, Truck, DollarSign, Factory, Cog } from 'lucide-react';
// import { Card } from '../../components/ui/Card';
// import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
// import { inventoryService } from '../../services/inventoryService';
// import { deliveryService } from '../../services/deliveryService';
// import { customerService } from '../../services/customerService';
// import { supplierService } from '../../services/supplierService';
// import { productionMetricsService, ProductionMetrics, DeliveryMetrics } from '../../services/productionMetricsService';

// interface AnalyticsData {
//   inventory: {
//     totalItems: number;
//     totalValue: number;
//     lowStockItems: number;
//   };
//   delivery: {
//     totalOrders: number;
//     pendingOrders: number;
//     deliveredOrders: number;
//     totalRevenue: number;
//   };
//   production: ProductionMetrics;
//   deliveryMetrics: DeliveryMetrics;
//   customers: number;
//   suppliers: number;
// }

// export const Analytics: React.FC = () => {
//   const [data, setData] = useState<AnalyticsData | null>(null);
//   const [isLoading, setIsLoading] = useState(true);
//   const [error, setError] = useState<string>('');

//   useEffect(() => {
//     fetchAnalyticsData();
//   }, []);

//   const fetchAnalyticsData = async () => {
//     try {
//       setIsLoading(true);
//       setError('');

//       // Initialize default values
//       let inventoryStats = { totalItems: 0, totalValue: 0, lowStockItems: 0 };
//       let deliveryStats = { totalOrders: 0, pendingOrders: 0, deliveredOrders: 0 };
//       let productionMetrics: ProductionMetrics;
//       let deliveryMetrics: DeliveryMetrics;
//       let customersCount = 0;
//       let suppliersCount = 0;

//       // Fetch data with error handling
//       try {
//         inventoryStats = await inventoryService.getStats();
//       } catch (error) {
//         console.warn('Inventory stats not available:', error);
//       }

//       try {
//         deliveryStats = await deliveryService.getStatistics();
//       } catch (error) {
//         console.warn('Delivery stats not available:', error);
//       }

//       try {
//         productionMetrics = await productionMetricsService.getProductionMetrics();
//       } catch (error) {
//         console.warn('Production metrics not available:', error);
//         productionMetrics = {
//           totalProducts: 0,
//           totalBatches: 0,
//           activeBatches: 0,
//           totalProductionLines: 0,
//           operationalLines: 0,
//           totalTasks: 0,
//           completedTasks: 0,
//           pendingTasks: 0,
//           averageEfficiency: 0
//         };
//       }

//       try {
//         deliveryMetrics = await productionMetricsService.getDeliveryMetrics();
//       } catch (error) {
//         console.warn('Delivery metrics not available:', error);
//         deliveryMetrics = {
//           totalOrders: 0,
//           pendingOrders: 0,
//           processingOrders: 0,
//           inTransitOrders: 0,
//           deliveredOrders: 0,
//           cancelledOrders: 0,
//           totalDrivers: 0,
//           activeDrivers: 0,
//           totalVehicles: 0,
//           totalRoutes: 0
//         };
//       }

//       try {
//         const customersData = await customerService.getAll(0, 1);
//         customersCount = customersData.totalElements || 0;
//       } catch (error) {
//         console.warn('Customers data not available:', error);
//       }

//       try {
//         const suppliersData = await supplierService.getAll(0, 1);
//         suppliersCount = suppliersData.totalElements || 0;
//       } catch (error) {
//         console.warn('Suppliers data not available:', error);
//       }

//       setData({
//         inventory: inventoryStats,
//         delivery: {
//           ...deliveryStats,
//           totalRevenue: 125000, // Mock data since this might not be available
//         },
//         production: productionMetrics,
//         deliveryMetrics,
//         customers: customersCount,
//         suppliers: suppliersCount,
//       });
//     } catch (error) {
//       console.error('Error fetching analytics data:', error);
//       setError('Failed to load analytics data');
//       // Set default data even on error
//       setData({
//         inventory: { totalItems: 0, totalValue: 0, lowStockItems: 0 },
//         delivery: { totalOrders: 0, pendingOrders: 0, deliveredOrders: 0, totalRevenue: 0 },
//         production: {
//           totalProducts: 0,
//           totalBatches: 0,
//           activeBatches: 0,
//           totalProductionLines: 0,
//           operationalLines: 0,
//           totalTasks: 0,
//           completedTasks: 0,
//           pendingTasks: 0,
//           averageEfficiency: 0
//         },
//         deliveryMetrics: {
//           totalOrders: 0,
//           pendingOrders: 0,
//           processingOrders: 0,
//           inTransitOrders: 0,
//           deliveredOrders: 0,
//           cancelledOrders: 0,
//           totalDrivers: 0,
//           activeDrivers: 0,
//           totalVehicles: 0,
//           totalRoutes: 0
//         },
//         customers: 0,
//         suppliers: 0,
//       });
//     } finally {
//       setIsLoading(false);
//     }
//   };

//   if (isLoading) {
//     return (
//       <div className="flex items-center justify-center h-64">
//         <LoadingSpinner size="lg" />
//       </div>
//     );
//   }

//   if (!data) {
//     return (
//       <div className="text-center py-12">
//         <p className="text-gray-500 dark:text-gray-400">No analytics data available</p>
//         {error && <p className="text-red-500 text-sm mt-2">{error}</p>}
//       </div>
//     );
//   }

//   const kpiCards = [
//     {
//       title: 'Total Revenue',
//       value: `$${(data.delivery.totalRevenue || 0).toLocaleString()}`,
//       change: '+12.5%',
//       changeType: 'positive',
//       icon: DollarSign,
//       color: 'bg-green-500',
//     },
//     {
//       title: 'Total Orders',
//       value: (data.deliveryMetrics.totalOrders || 0).toString(),
//       change: '+8.2%',
//       changeType: 'positive',
//       icon: Truck,
//       color: 'bg-blue-500',
//     },
//     {
//       title: 'Production Lines',
//       value: (data.production.totalProductionLines || 0).toString(),
//       change: `${data.production.operationalLines}/${data.production.totalProductionLines} operational`,
//       changeType: 'neutral',
//       icon: Factory,
//       color: 'bg-purple-500',
//     },
//     {
//       title: 'Active Drivers',
//       value: (data.deliveryMetrics.activeDrivers || 0).toString(),
//       change: `${data.deliveryMetrics.totalDrivers} total`,
//       changeType: 'neutral',
//       icon: Users,
//       color: 'bg-orange-500',
//     },
//   ];

//   return (
//     <div className="space-y-6">
//       <div className="flex justify-between items-center">
//         <div>
//           <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Analytics Dashboard</h1>
//           <p className="text-gray-600 dark:text-gray-400">Business insights and performance metrics</p>
//         </div>
//       </div>

//       {error && (
//         <div className="bg-yellow-100 border border-yellow-400 text-yellow-700 px-4 py-3 rounded">
//           <p className="font-medium">Warning:</p>
//           <p>{error}</p>
//         </div>
//       )}

//       {/* KPI Cards */}
//       <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
//         {kpiCards.map((kpi) => (
//           <Card key={kpi.title} className="p-6">
//             <div className="flex items-center justify-between">
//               <div>
//                 <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
//                   {kpi.title}
//                 </p>
//                 <p className="text-2xl font-bold text-gray-900 dark:text-white">
//                   {kpi.value}
//                 </p>
//                 <div className="mt-2 flex items-center">
//                   {kpi.changeType === 'positive' && <TrendingUp className="h-4 w-4 text-green-500 mr-1" />}
//                   <span className={`text-sm font-medium ${
//                     kpi.changeType === 'positive' ? 'text-green-600 dark:text-green-400' : 'text-gray-600 dark:text-gray-400'
//                   }`}>
//                     {kpi.change}
//                   </span>
//                 </div>
//               </div>
//               <div className={`${kpi.color} p-3 rounded-lg`}>
//                 <kpi.icon className="h-6 w-6 text-white" />
//               </div>
//             </div>
//           </Card>
//         ))}
//       </div>

//       {/* Charts and Detailed Analytics */}
//       <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
//         {/* Production Overview */}
//         <Card className="p-6">
//           <div className="flex items-center justify-between mb-4">
//             <h2 className="text-lg font-semibold text-gray-900 dark:text-white">
//               Production Overview
//             </h2>
//             <Factory className="h-5 w-5 text-gray-400" />
//           </div>
//           <div className="space-y-4">
//             <div className="flex justify-between items-center">
//               <span className="text-gray-600 dark:text-gray-400">Total Products</span>
//               <span className="font-semibold">{data.production.totalProducts}</span>
//             </div>
//             <div className="flex justify-between items-center">
//               <span className="text-gray-600 dark:text-gray-400">Active Batches</span>
//               <span className="font-semibold text-blue-600">{data.production.activeBatches}</span>
//             </div>
//             <div className="flex justify-between items-center">
//               <span className="text-gray-600 dark:text-gray-400">Pending Tasks</span>
//               <span className="font-semibold text-orange-600">{data.production.pendingTasks}</span>
//             </div>
//             <div className="flex justify-between items-center">
//               <span className="text-gray-600 dark:text-gray-400">Avg Efficiency</span>
//               <span className="font-semibold">{data.production.averageEfficiency}%</span>
//             </div>
//             <div className="mt-4">
//               <div className="flex justify-between text-sm mb-1">
//                 <span className="text-gray-600 dark:text-gray-400">Task Completion</span>
//                 <span className="text-gray-900 dark:text-white">
//                   {data.production.totalTasks > 0 
//                     ? Math.round((data.production.completedTasks / data.production.totalTasks) * 100)
//                     : 0}%
//                 </span>
//               </div>
//               <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
//                 <div 
//                   className="bg-purple-500 h-2 rounded-full" 
//                   style={{ 
//                     width: `${data.production.totalTasks > 0 
//                       ? Math.round((data.production.completedTasks / data.production.totalTasks) * 100)
//                       : 0}%` 
//                   }}
//                 />
//               </div>
//             </div>
//           </div>
//         </Card>

//         {/* Delivery Overview */}
//         <Card className="p-6">
//           <div className="flex items-center justify-between mb-4">
//             <h2 className="text-lg font-semibold text-gray-900 dark:text-white">
//               Delivery Overview
//             </h2>
//             <Truck className="h-5 w-5 text-gray-400" />
//           </div>
//           <div className="space-y-4">
//             <div className="flex justify-between items-center">
//               <span className="text-gray-600 dark:text-gray-400">Total Orders</span>
//               <span className="font-semibold">{data.deliveryMetrics.totalOrders}</span>
//             </div>
//             <div className="flex justify-between items-center">
//               <span className="text-gray-600 dark:text-gray-400">In Transit</span>
//               <span className="font-semibold text-blue-600">{data.deliveryMetrics.inTransitOrders}</span>
//             </div>
//             <div className="flex justify-between items-center">
//               <span className="text-gray-600 dark:text-gray-400">Active Drivers</span>
//               <span className="font-semibold text-green-600">{data.deliveryMetrics.activeDrivers}</span>
//             </div>
//             <div className="flex justify-between items-center">
//               <span className="text-gray-600 dark:text-gray-400">Total Vehicles</span>
//               <span className="font-semibold">{data.deliveryMetrics.totalVehicles}</span>
//             </div>
//             <div className="mt-4">
//               <div className="flex justify-between text-sm mb-1">
//                 <span className="text-gray-600 dark:text-gray-400">Delivery Rate</span>
//                 <span className="text-gray-900 dark:text-white">
//                   {data.deliveryMetrics.totalOrders > 0 
//                     ? Math.round((data.deliveryMetrics.deliveredOrders / data.deliveryMetrics.totalOrders) * 100)
//                     : 0}%
//                 </span>
//               </div>
//               <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
//                 <div 
//                   className="bg-green-500 h-2 rounded-full" 
//                   style={{ 
//                     width: `${data.deliveryMetrics.totalOrders > 0 
//                       ? Math.round((data.deliveryMetrics.deliveredOrders / data.deliveryMetrics.totalOrders) * 100)
//                       : 0}%` 
//                   }}
//                 />
//               </div>
//             </div>
//           </div>
//         </Card>

//         {/* Inventory Overview */}
//         <Card className="p-6">
//           <div className="flex items-center justify-between mb-4">
//             <h2 className="text-lg font-semibold text-gray-900 dark:text-white">
//               Inventory Overview
//             </h2>
//             <Package className="h-5 w-5 text-gray-400" />
//           </div>
//           <div className="space-y-4">
//             <div className="flex justify-between items-center">
//               <span className="text-gray-600 dark:text-gray-400">Total Items</span>
//               <span className="font-semibold">{data.inventory.totalItems || 0}</span>
//             </div>
//             <div className="flex justify-between items-center">
//               <span className="text-gray-600 dark:text-gray-400">Low Stock Items</span>
//               <span className="font-semibold text-orange-600">{data.inventory.lowStockItems || 0}</span>
//             </div>
//             <div className="flex justify-between items-center">
//               <span className="text-gray-600 dark:text-gray-400">Total Value</span>
//               <span className="font-semibold">${(data.inventory.totalValue || 0).toLocaleString()}</span>
//             </div>
//             <div className="mt-4">
//               <div className="flex justify-between text-sm mb-1">
//                 <span className="text-gray-600 dark:text-gray-400">Stock Health</span>
//                 <span className="text-gray-900 dark:text-white">
//                   {data.inventory.totalItems > 0 
//                     ? Math.round(((data.inventory.totalItems - data.inventory.lowStockItems) / data.inventory.totalItems) * 100)
//                     : 100}%
//                 </span>
//               </div>
//               <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
//                 <div 
//                   className="bg-green-500 h-2 rounded-full" 
//                   style={{ 
//                     width: `${data.inventory.totalItems > 0 
//                       ? Math.round(((data.inventory.totalItems - data.inventory.lowStockItems) / data.inventory.totalItems) * 100)
//                       : 100}%` 
//                   }}
//                 />
//               </div>
//             </div>
//           </div>
//         </Card>

//         {/* Performance Metrics */}
//         <Card className="p-6">
//           <div className="flex items-center justify-between mb-4">
//             <h2 className="text-lg font-semibold text-gray-900 dark:text-white">
//               Performance Metrics
//             </h2>
//             <BarChart3 className="h-5 w-5 text-gray-400" />
//           </div>
//           <div className="space-y-4">
//             <div>
//               <div className="flex justify-between text-sm mb-1">
//                 <span className="text-gray-600 dark:text-gray-400">Production Efficiency</span>
//                 <span className="text-gray-900 dark:text-white">{data.production.averageEfficiency}%</span>
//               </div>
//               <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
//                 <div 
//                   className="bg-purple-500 h-2 rounded-full" 
//                   style={{ width: `${data.production.averageEfficiency}%` }}
//                 />
//               </div>
//             </div>
//             <div>
//               <div className="flex justify-between text-sm mb-1">
//                 <span className="text-gray-600 dark:text-gray-400">Order Fulfillment</span>
//                 <span className="text-gray-900 dark:text-white">
//                   {data.deliveryMetrics.totalOrders > 0 
//                     ? Math.round((data.deliveryMetrics.deliveredOrders / data.deliveryMetrics.totalOrders) * 100)
//                     : 0}%
//                 </span>
//               </div>
//               <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
//                 <div 
//                   className="bg-green-500 h-2 rounded-full" 
//                   style={{ 
//                     width: `${data.deliveryMetrics.totalOrders > 0 
//                       ? Math.round((data.deliveryMetrics.deliveredOrders / data.deliveryMetrics.totalOrders) * 100)
//                       : 0}%` 
//                   }}
//                 />
//               </div>
//             </div>
//             <div>
//               <div className="flex justify-between text-sm mb-1">
//                 <span className="text-gray-600 dark:text-gray-400">Inventory Turnover</span>
//                 <span className="text-gray-900 dark:text-white">76%</span>
//               </div>
//               <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
//                 <div className="bg-blue-500 h-2 rounded-full w-[76%]" />
//               </div>
//             </div>
//           </div>
//         </Card>
//       </div>

//       {/* Recent Activity Summary */}
//       <Card className="p-6">
//         <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">
//           Recent Activity Summary
//         </h2>
//         <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
//           <div className="text-center">
//             <div className="text-2xl font-bold text-blue-600 dark:text-blue-400">
//               {data.deliveryMetrics.pendingOrders}
//             </div>
//             <div className="text-sm text-gray-600 dark:text-gray-400">Orders Pending</div>
//           </div>
//           <div className="text-center">
//             <div className="text-2xl font-bold text-orange-600 dark:text-orange-400">
//               {data.production.pendingTasks}
//             </div>
//             <div className="text-sm text-gray-600 dark:text-gray-400">Tasks Pending</div>
//           </div>
//           <div className="text-center">
//             <div className="text-2xl font-bold text-purple-600 dark:text-purple-400">
//               {data.production.activeBatches}
//             </div>
//             <div className="text-sm text-gray-600 dark:text-gray-400">Active Batches</div>
//           </div>
//           <div className="text-center">
//             <div className="text-2xl font-bold text-green-600 dark:text-green-400">
//               ${(data.delivery.totalRevenue || 0).toLocaleString()}
//             </div>
//             <div className="text-sm text-gray-600 dark:text-gray-400">Monthly Revenue</div>
//           </div>
//         </div>
//       </Card>
//     </div>
//   );
// };