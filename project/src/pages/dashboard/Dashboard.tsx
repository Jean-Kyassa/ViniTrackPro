import React, { useState, useEffect } from 'react';
import { 
  BarChart3, 
  Package, 
  Truck, 
  Users, 
  TrendingUp, 
  AlertTriangle,
  CheckCircle,
  Clock,
  Wine
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { Card } from '../../components/ui/Card';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { inventoryService } from '../../services/inventoryService';
import { deliveryService } from '../../services/deliveryService';
import { customerService } from '../../services/customerService';
import { supplierService } from '../../services/supplierService';

interface DashboardStats {
  inventory: {
    totalItems: number;
    lowStockItems: number;
    totalValue: number;
  };
  delivery: {
    totalOrders: number;
    pendingOrders: number;
    deliveredOrders: number;
  };
  customers: number;
  suppliers: number;
}

export const Dashboard: React.FC = () => {
  const { user, hasRole } = useAuth();
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [lowStockItems, setLowStockItems] = useState<any[]>([]);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const [inventoryStats, deliveryStats, customersData, suppliersData, lowStock] = await Promise.all([
          hasRole('ROLE_INVENTORY') || hasRole('ROLE_ADMIN') ? inventoryService.getStats() : null,
          hasRole('ROLE_LOGISTICS') || hasRole('ROLE_ADMIN') ? deliveryService.getStatistics() : null,
          hasRole('ROLE_LOGISTICS') || hasRole('ROLE_ADMIN') ? customerService.getAll(0, 1) : null,
          hasRole('ROLE_INVENTORY') || hasRole('ROLE_ADMIN') ? supplierService.getAll(0, 1) : null,
          hasRole('ROLE_INVENTORY') || hasRole('ROLE_ADMIN') ? inventoryService.getLowStock() : [],
        ]);

        setStats({
          inventory: inventoryStats || { totalItems: 0, lowStockItems: 0, totalValue: 0 },
          delivery: deliveryStats || { totalOrders: 0, pendingOrders: 0, deliveredOrders: 0 },
          customers: customersData?.totalElements || 0,
          suppliers: suppliersData?.totalElements || 0,
        });

        setLowStockItems(lowStock || []);
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchDashboardData();
  }, [hasRole]);

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  const quickActions = [
    { 
      title: 'Add Inventory Item', 
      href: '/dashboard/inventory', 
      icon: Package, 
      color: 'bg-blue-500',
      roles: ['ROLE_INVENTORY', 'ROLE_ADMIN']
    },
    { 
      title: 'Create Delivery', 
      href: '/dashboard/deliveries', 
      icon: Truck, 
      color: 'bg-green-500',
      roles: ['ROLE_LOGISTICS', 'ROLE_ADMIN']
    },
    { 
      title: 'Add Customer', 
      href: '/dashboard/customers', 
      icon: Users, 
      color: 'bg-purple-500',
      roles: ['ROLE_LOGISTICS', 'ROLE_ADMIN']
    },
    { 
      title: 'View Reports', 
      href: '/dashboard/reports', 
      icon: BarChart3, 
      color: 'bg-orange-500',
      roles: ['ROLE_ADMIN', 'ROLE_INVENTORY', 'ROLE_LOGISTICS', 'ROLE_QUALITY']
    },
  ];

  return (
    <div className="space-y-6">
      {/* Welcome Header */}
      <div className="bg-gradient-to-r from-blue-600 to-purple-600 rounded-lg p-6 text-white">
        <div className="flex items-center space-x-4">
          <div className="bg-white/20 p-3 rounded-full">
            <Wine className="h-8 w-8" />
          </div>
          <div>
            <h1 className="text-2xl font-bold">Welcome back, {user?.username}!</h1>
            <p className="text-blue-100">
              Username: {user?.username} | Here's your dashboard overview
            </p>
          </div>
        </div>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {(hasRole('ROLE_INVENTORY') || hasRole('ROLE_ADMIN')) && (
          <>
            <Card className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
                    Total Inventory
                  </p>
                  <p className="text-2xl font-bold text-gray-900 dark:text-white">
                    {stats?.inventory.totalItems || 0}
                  </p>
                </div>
                <Package className="h-8 w-8 text-blue-500" />
              </div>
              <div className="mt-2 flex items-center text-sm">
                <TrendingUp className="h-4 w-4 text-green-500 mr-1" />
                <span className="text-green-600 dark:text-green-400">Items tracked</span>
              </div>
            </Card>

            <Card className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
                    Low Stock Alerts
                  </p>
                  <p className="text-2xl font-bold text-gray-900 dark:text-white">
                    {stats?.inventory.lowStockItems || 0}
                  </p>
                </div>
                <AlertTriangle className="h-8 w-8 text-orange-500" />
              </div>
              <div className="mt-2 flex items-center text-sm">
                <span className="text-orange-600 dark:text-orange-400">Items need restock</span>
              </div>
            </Card>
          </>
        )}

        {(hasRole('ROLE_LOGISTICS') || hasRole('ROLE_ADMIN')) && (
          <>
            <Card className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
                    Active Orders
                  </p>
                  <p className="text-2xl font-bold text-gray-900 dark:text-white">
                    {stats?.delivery.pendingOrders || 0}
                  </p>
                </div>
                <Clock className="h-8 w-8 text-yellow-500" />
              </div>
              <div className="mt-2 flex items-center text-sm">
                <span className="text-yellow-600 dark:text-yellow-400">Pending delivery</span>
              </div>
            </Card>

            <Card className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
                    Customers
                  </p>
                  <p className="text-2xl font-bold text-gray-900 dark:text-white">
                    {stats?.customers || 0}
                  </p>
                </div>
                <Users className="h-8 w-8 text-green-500" />
              </div>
              <div className="mt-2 flex items-center text-sm">
                <CheckCircle className="h-4 w-4 text-green-500 mr-1" />
                <span className="text-green-600 dark:text-green-400">Total registered</span>
              </div>
            </Card>
          </>
        )}
      </div>

      {/* Quick Actions */}
      <Card className="p-6">
        <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">
          Quick Actions
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          {quickActions
            .filter(action => action.roles.some(role => hasRole(role)))
            .map((action) => (
              <a
                key={action.title}
                href={action.href}
                className="flex items-center p-4 rounded-lg border border-gray-200 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-800 transition-colors"
              >
                <div className={`${action.color} p-2 rounded-lg mr-3`}>
                  <action.icon className="h-5 w-5 text-white" />
                </div>
                <span className="font-medium text-gray-900 dark:text-white">
                  {action.title}
                </span>
              </a>
            ))}
        </div>
      </Card>

      {/* Low Stock Alert */}
      {lowStockItems.length > 0 && (
        <Card className="p-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-lg font-semibold text-gray-900 dark:text-white">
              Low Stock Alert
            </h2>
            <AlertTriangle className="h-5 w-5 text-orange-500" />
          </div>
          <div className="space-y-3">
            {lowStockItems.slice(0, 5).map((item) => (
              <div
                key={item.id}
                className="flex items-center justify-between p-3 bg-orange-50 dark:bg-orange-900/20 rounded-lg"
              >
                <div>
                  <p className="font-medium text-gray-900 dark:text-white">
                    {item.itemName}
                  </p>
                  <p className="text-sm text-gray-600 dark:text-gray-400">
                    SKU: {item.sku}
                  </p>
                </div>
                <div className="text-right">
                  <p className="text-sm font-medium text-orange-600 dark:text-orange-400">
                    {item.currentQuantity} left
                  </p>
                  <p className="text-xs text-gray-500 dark:text-gray-400">
                    Min: {item.minimumStockLevel}
                  </p>
                </div>
              </div>
            ))}
          </div>
        </Card>
      )}

      {/* Recent Activity */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card className="p-6">
          <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">
            System Status
          </h2>
          <div className="space-y-3">
            <div className="flex items-center justify-between">
              <span className="text-gray-600 dark:text-gray-400">API Status</span>
              <div className="flex items-center">
                <CheckCircle className="h-4 w-4 text-green-500 mr-1" />
                <span className="text-green-600 dark:text-green-400">Operational</span>
              </div>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-gray-600 dark:text-gray-400">Database</span>
              <div className="flex items-center">
                <CheckCircle className="h-4 w-4 text-green-500 mr-1" />
                <span className="text-green-600 dark:text-green-400">Connected</span>
              </div>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-gray-600 dark:text-gray-400">Last Backup</span>
              <span className="text-gray-600 dark:text-gray-400">2 hours ago</span>
            </div>
          </div>
        </Card>

        <Card className="p-6">
          <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">
            Performance
          </h2>
          <div className="space-y-4">
            <div>
              <div className="flex justify-between text-sm mb-1">
                <span className="text-gray-600 dark:text-gray-400">System Load</span>
                <span className="text-gray-900 dark:text-white">23%</span>
              </div>
              <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
                <div className="bg-green-500 h-2 rounded-full w-[23%]" />
              </div>
            </div>
            <div>
              <div className="flex justify-between text-sm mb-1">
                <span className="text-gray-600 dark:text-gray-400">Storage Used</span>
                <span className="text-gray-900 dark:text-white">67%</span>
              </div>
              <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
                <div className="bg-blue-500 h-2 rounded-full w-[67%]" />
              </div>
            </div>
            <div>
              <div className="flex justify-between text-sm mb-1">
                <span className="text-gray-600 dark:text-gray-400">Memory</span>
                <span className="text-gray-900 dark:text-white">45%</span>
              </div>
              <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
                <div className="bg-yellow-500 h-2 rounded-full w-[45%]" />
              </div>
            </div>
          </div>
        </Card>
      </div>
    </div>
  );
};


// "use client"

// import type React from "react"
// import { useState, useEffect } from "react"
// import {
//   BarChart3,
//   Package,
//   Truck,
//   Users,
//   TrendingUp,
//   AlertTriangle,
//   CheckCircle,
//   Clock,
//   Wine,
//   Factory,
//   CheckSquare,
//   Car,
// } from "lucide-react"
// import { useAuth } from "../../context/AuthContext"
// import { Card } from "../../components/ui/Card"
// import { LoadingSpinner } from "../../components/ui/LoadingSpinner"
// import { inventoryService } from "../../services/inventoryService"
// import { customerService } from "../../services/customerService"
// import { supplierService } from "../../services/supplierService"
// import { productionMetricsService, type ProductionMetrics } from "../../services/productionMetricsService"
// import { deliveryMetricsService, type DeliveryMetrics } from "../../services/deliveryMetricsService"

// interface DashboardStats {
//   inventory: {
//     totalItems: number
//     lowStockItems: number
//     totalValue: number
//   }
//   delivery: DeliveryMetrics
//   production: ProductionMetrics
//   customers: number
//   suppliers: number
// }

// export const Dashboard: React.FC = () => {
//   const { user, hasRole } = useAuth()
//   const [stats, setStats] = useState<DashboardStats | null>(null)
//   const [isLoading, setIsLoading] = useState(true)
//   const [lowStockItems, setLowStockItems] = useState<any[]>([])

//   useEffect(() => {
//     const fetchDashboardData = async () => {
//       try {
//         const promises = []
//         let inventoryStats = { totalItems: 0, lowStockItems: 0, totalValue: 0 }
//         let deliveryStats: DeliveryMetrics = {
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
//         }
//         let productionStats: ProductionMetrics = {
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
//         }
//         let customersCount = 0
//         let suppliersCount = 0
//         let lowStock: any[] = []

//         if (hasRole("ROLE_INVENTORY") || hasRole("ROLE_ADMIN")) {
//           promises.push(
//             inventoryService.getStats().catch(() => inventoryStats),
//             supplierService
//               .getAll(0, 1)
//               .then((data) => data.totalElements || 0)
//               .catch(() => 0),
//             inventoryService.getLowStock().catch(() => []),
//           )
//         }

//         if (hasRole("ROLE_LOGISTICS") || hasRole("ROLE_ADMIN")) {
//           promises.push(
//             deliveryMetricsService.getMetrics().catch(() => deliveryStats),
//             customerService
//               .getAll(0, 1)
//               .then((data) => data.totalElements || 0)
//               .catch(() => 0),
//           )
//         }

//         if (hasRole("ROLE_PRODUCTION") || hasRole("ROLE_ADMIN")) {
//           promises.push(productionMetricsService.getMetrics().catch(() => productionStats))
//         }

//         const results = await Promise.all(promises)

//         let resultIndex = 0
//         if (hasRole("ROLE_INVENTORY") || hasRole("ROLE_ADMIN")) {
//           const inventoryResult = results[resultIndex++];
//           if (inventoryResult && typeof inventoryResult === 'object' && 'totalItems' in inventoryResult) {
//             inventoryStats = inventoryResult as { totalItems: number; lowStockItems: number; totalValue: number; };
//           }
//           suppliersCount = Number.isFinite(results[resultIndex]) ? (results[resultIndex++] as number) : 0
//           const potentialLowStock = results[resultIndex++];
//           lowStock = Array.isArray(potentialLowStock) ? potentialLowStock : [];
//         }

//         if (hasRole("ROLE_LOGISTICS") || hasRole("ROLE_ADMIN")) {
//           const potentialDeliveryStats = results[resultIndex++];
//           if (potentialDeliveryStats && typeof potentialDeliveryStats === 'object' && 'totalOrders' in potentialDeliveryStats) {
//             deliveryStats = potentialDeliveryStats as DeliveryMetrics;
//           }
//           customersCount = Number.isFinite(results[resultIndex]) ? (results[resultIndex++] as number) : 0
//         }

//         if (hasRole("ROLE_PRODUCTION") || hasRole("ROLE_ADMIN")) {
//           const potentialProductionStats = results[resultIndex++];
//           if (potentialProductionStats && typeof potentialProductionStats === 'object' && 'totalProducts' in potentialProductionStats) {
//             productionStats = potentialProductionStats as ProductionMetrics;
//           }
//         }

//         setStats({
//           inventory: inventoryStats,
//           delivery: deliveryStats,
//           production: productionStats,
//           customers: customersCount,
//           suppliers: suppliersCount,
//         })

//         setLowStockItems(lowStock)
//       } catch (error) {
//         console.error("Error fetching dashboard data:", error)
//       } finally {
//         setIsLoading(false)
//       }
//     }

//     fetchDashboardData()
//   }, [hasRole])

//   if (isLoading) {
//     return (
//       <div className="flex items-center justify-center h-64">
//         <LoadingSpinner size="lg" />
//       </div>
//     )
//   }

//   const quickActions = [
//     ...(hasRole("ROLE_INVENTORY") || hasRole("ROLE_ADMIN")
//       ? [
//           {
//             title: "Add Inventory Item",
//             href: "/dashboard/inventory",
//             icon: Package,
//             color: "bg-blue-500",
//           },
//         ]
//       : []),
//     ...(hasRole("ROLE_LOGISTICS") || hasRole("ROLE_ADMIN")
//       ? [
//           {
//             title: "Create Delivery",
//             href: "/dashboard/deliveries",
//             icon: Truck,
//             color: "bg-green-500",
//           },
//         ]
//       : []),
//     ...(hasRole("ROLE_PRODUCTION") || hasRole("ROLE_ADMIN")
//       ? [
//           {
//             title: "Manage Production",
//             href: "/dashboard/production",
//             icon: Factory,
//             color: "bg-purple-500",
//           },
//         ]
//       : []),
//     ...(hasRole("ROLE_LOGISTICS") || hasRole("ROLE_ADMIN")
//       ? [
//           {
//             title: "Add Customer",
//             href: "/dashboard/customers",
//             icon: Users,
//             color: "bg-orange-500",
//           },
//         ]
//       : []),
//     {
//       title: "View Reports",
//       href: "/dashboard/reports",
//       icon: BarChart3,
//       color: "bg-indigo-500",
//     },
//   ]

//   return (
//     <div className="space-y-6">
//       {/* Welcome Header */}
//       <div className="bg-gradient-to-r from-blue-600 to-purple-600 rounded-lg p-6 text-white">
//         <div className="flex items-center space-x-4">
//           <div className="bg-white/20 p-3 rounded-full">
//             <Wine className="h-8 w-8" />
//           </div>
//           <div>
//             <h1 className="text-2xl font-bold">Welcome back, {user?.username}!</h1>
//             <p className="text-blue-100">Role: {user?.roles.join(", ")} | Here's your dashboard overview</p>
//           </div>
//         </div>
//       </div>

//       {/* Stats Grid */}
//       <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
//         {(hasRole("ROLE_INVENTORY") || hasRole("ROLE_ADMIN")) && (
//           <>
//             <Card className="p-6">
//               <div className="flex items-center justify-between">
//                 <div>
//                   <p className="text-sm font-medium text-gray-600 dark:text-gray-400">Total Inventory</p>
//                   <p className="text-2xl font-bold text-gray-900 dark:text-white">{stats?.inventory.totalItems || 0}</p>
//                 </div>
//                 <Package className="h-8 w-8 text-blue-500" />
//               </div>
//               <div className="mt-2 flex items-center text-sm">
//                 <TrendingUp className="h-4 w-4 text-green-500 mr-1" />
//                 <span className="text-green-600 dark:text-green-400">Items tracked</span>
//               </div>
//             </Card>

//             <Card className="p-6">
//               <div className="flex items-center justify-between">
//                 <div>
//                   <p className="text-sm font-medium text-gray-600 dark:text-gray-400">Low Stock Alerts</p>
//                   <p className="text-2xl font-bold text-gray-900 dark:text-white">
//                     {stats?.inventory.lowStockItems || 0}
//                   </p>
//                 </div>
//                 <AlertTriangle className="h-8 w-8 text-orange-500" />
//               </div>
//               <div className="mt-2 flex items-center text-sm">
//                 <span className="text-orange-600 dark:text-orange-400">Items need restock</span>
//               </div>
//             </Card>
//           </>
//         )}

//         {(hasRole("ROLE_LOGISTICS") || hasRole("ROLE_ADMIN")) && (
//           <>
//             <Card className="p-6">
//               <div className="flex items-center justify-between">
//                 <div>
//                   <p className="text-sm font-medium text-gray-600 dark:text-gray-400">Active Orders</p>
//                   <p className="text-2xl font-bold text-gray-900 dark:text-white">
//                     {stats?.delivery.pendingOrders || 0}
//                   </p>
//                 </div>
//                 <Clock className="h-8 w-8 text-yellow-500" />
//               </div>
//               <div className="mt-2 flex items-center text-sm">
//                 <span className="text-yellow-600 dark:text-yellow-400">Pending delivery</span>
//               </div>
//             </Card>

//             <Card className="p-6">
//               <div className="flex items-center justify-between">
//                 <div>
//                   <p className="text-sm font-medium text-gray-600 dark:text-gray-400">Total Vehicles</p>
//                   <p className="text-2xl font-bold text-gray-900 dark:text-white">
//                     {stats?.delivery.totalVehicles || 0}
//                   </p>
//                 </div>
//                 <Car className="h-8 w-8 text-blue-500" />
//               </div>
//               <div className="mt-2 flex items-center text-sm">
//                 <CheckCircle className="h-4 w-4 text-green-500 mr-1" />
//                 <span className="text-green-600 dark:text-green-400">Fleet available</span>
//               </div>
//             </Card>
//           </>
//         )}

//         {(hasRole("ROLE_PRODUCTION") || hasRole("ROLE_ADMIN")) && (
//           <>
//             <Card className="p-6">
//               <div className="flex items-center justify-between">
//                 <div>
//                   <p className="text-sm font-medium text-gray-600 dark:text-gray-400">Production Efficiency</p>
//                   <p className="text-2xl font-bold text-gray-900 dark:text-white">
//                     {stats?.production.efficiency || 0}%
//                   </p>
//                 </div>
//                 <Factory className="h-8 w-8 text-green-500" />
//               </div>
//               <div className="mt-2 flex items-center text-sm">
//                 <TrendingUp className="h-4 w-4 text-green-500 mr-1" />
//                 <span className="text-green-600 dark:text-green-400">Lines operational</span>
//               </div>
//             </Card>

//             <Card className="p-6">
//               <div className="flex items-center justify-between">
//                 <div>
//                   <p className="text-sm font-medium text-gray-600 dark:text-gray-400">Pending Tasks</p>
//                   <p className="text-2xl font-bold text-gray-900 dark:text-white">
//                     {stats?.production.pendingTasks || 0}
//                   </p>
//                 </div>
//                 <CheckSquare className="h-8 w-8 text-orange-500" />
//               </div>
//               <div className="mt-2 flex items-center text-sm">
//                 <span className="text-orange-600 dark:text-orange-400">Tasks to complete</span>
//               </div>
//             </Card>
//           </>
//         )}

//         {(hasRole("ROLE_LOGISTICS") || hasRole("ROLE_ADMIN")) && !hasRole("ROLE_PRODUCTION") && (
//           <Card className="p-6">
//             <div className="flex items-center justify-between">
//               <div>
//                 <p className="text-sm font-medium text-gray-600 dark:text-gray-400">Customers</p>
//                 <p className="text-2xl font-bold text-gray-900 dark:text-white">{stats?.customers || 0}</p>
//               </div>
//               <Users className="h-8 w-8 text-green-500" />
//             </div>
//             <div className="mt-2 flex items-center text-sm">
//               <CheckCircle className="h-4 w-4 text-green-500 mr-1" />
//               <span className="text-green-600 dark:text-green-400">Total registered</span>
//             </div>
//           </Card>
//         )}
//       </div>

//       {/* Quick Actions */}
//       <Card className="p-6">
//         <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">Quick Actions</h2>
//         <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4">
//           {quickActions.map((action) => (
//             <a
//               key={action.title}
//               href={action.href}
//               className="flex items-center p-4 rounded-lg border border-gray-200 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-800 transition-colors"
//             >
//               <div className={`${action.color} p-2 rounded-lg mr-3`}>
//                 <action.icon className="h-5 w-5 text-white" />
//               </div>
//               <span className="font-medium text-gray-900 dark:text-white text-sm">{action.title}</span>
//             </a>
//           ))}
//         </div>
//       </Card>

//       {/* Role-specific sections */}
//       <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
//         {/* Low Stock Alert */}
//         {lowStockItems.length > 0 && (hasRole("ROLE_INVENTORY") || hasRole("ROLE_ADMIN")) && (
//           <Card className="p-6">
//             <div className="flex items-center justify-between mb-4">
//               <h2 className="text-lg font-semibold text-gray-900 dark:text-white">Low Stock Alert</h2>
//               <AlertTriangle className="h-5 w-5 text-orange-500" />
//             </div>
//             <div className="space-y-3">
//               {lowStockItems.slice(0, 5).map((item) => (
//                 <div
//                   key={item.id}
//                   className="flex items-center justify-between p-3 bg-orange-50 dark:bg-orange-900/20 rounded-lg"
//                 >
//                   <div>
//                     <p className="font-medium text-gray-900 dark:text-white">{item.itemName}</p>
//                     <p className="text-sm text-gray-600 dark:text-gray-400">SKU: {item.sku}</p>
//                   </div>
//                   <div className="text-right">
//                     <p className="text-sm font-medium text-orange-600 dark:text-orange-400">
//                       {item.currentQuantity} left
//                     </p>
//                     <p className="text-xs text-gray-500 dark:text-gray-400">Min: {item.minimumStockLevel}</p>
//                   </div>
//                 </div>
//               ))}
//             </div>
//           </Card>
//         )}

//         {/* Production Status */}
//         {(hasRole("ROLE_PRODUCTION") || hasRole("ROLE_ADMIN")) && (
//           <Card className="p-6">
//             <div className="flex items-center justify-between mb-4">
//               <h2 className="text-lg font-semibold text-gray-900 dark:text-white">Production Status</h2>
//               <Factory className="h-5 w-5 text-gray-400" />
//             </div>
//             <div className="space-y-3">
//               <div className="flex items-center justify-between">
//                 <span className="text-gray-600 dark:text-gray-400">Active Batches</span>
//                 <div className="flex items-center">
//                   <span className="text-blue-600 dark:text-blue-400 font-semibold">
//                     {stats?.production.activeBatches || 0}
//                   </span>
//                 </div>
//               </div>
//               <div className="flex items-center justify-between">
//                 <span className="text-gray-600 dark:text-gray-400">Operational Lines</span>
//                 <div className="flex items-center">
//                   <CheckCircle className="h-4 w-4 text-green-500 mr-1" />
//                   <span className="text-green-600 dark:text-green-400 font-semibold">
//                     {stats?.production.operationalLines || 0}
//                   </span>
//                 </div>
//               </div>
//               <div className="flex items-center justify-between">
//                 <span className="text-gray-600 dark:text-gray-400">Tasks Completed Today</span>
//                 <span className="text-green-600 dark:text-green-400 font-semibold">
//                   {stats?.production.completedTasksToday || 0}
//                 </span>
//               </div>
//             </div>
//           </Card>
//         )}

//         {/* Delivery Overview */}
//         {(hasRole("ROLE_LOGISTICS") || hasRole("ROLE_ADMIN")) && (
//           <Card className="p-6">
//             <div className="flex items-center justify-between mb-4">
//               <h2 className="text-lg font-semibold text-gray-900 dark:text-white">Delivery Overview</h2>
//               <Truck className="h-5 w-5 text-gray-400" />
//             </div>
//             <div className="space-y-3">
//               <div className="flex items-center justify-between">
//                 <span className="text-gray-600 dark:text-gray-400">Active Drivers</span>
//                 <div className="flex items-center">
//                   <CheckCircle className="h-4 w-4 text-green-500 mr-1" />
//                   <span className="text-green-600 dark:text-green-400 font-semibold">
//                     {stats?.delivery.activeDrivers || 0}
//                   </span>
//                 </div>
//               </div>
//               <div className="flex items-center justify-between">
//                 <span className="text-gray-600 dark:text-gray-400">In Transit</span>
//                 <span className="text-blue-600 dark:text-blue-400 font-semibold">
//                   {stats?.delivery.inTransitOrders || 0}
//                 </span>
//               </div>
//               <div className="flex items-center justify-between">
//                 <span className="text-gray-600 dark:text-gray-400">On-Time Rate</span>
//                 <span className="text-green-600 dark:text-green-400 font-semibold">
//                   {stats?.delivery.onTimeDeliveryRate || 0}%
//                 </span>
//               </div>
//             </div>
//           </Card>
//         )}

//         {/* System Status */}
//         <Card className="p-6">
//           <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">System Status</h2>
//           <div className="space-y-3">
//             <div className="flex items-center justify-between">
//               <span className="text-gray-600 dark:text-gray-400">API Status</span>
//               <div className="flex items-center">
//                 <CheckCircle className="h-4 w-4 text-green-500 mr-1" />
//                 <span className="text-green-600 dark:text-green-400">Operational</span>
//               </div>
//             </div>
//             <div className="flex items-center justify-between">
//               <span className="text-gray-600 dark:text-gray-400">Database</span>
//               <div className="flex items-center">
//                 <CheckCircle className="h-4 w-4 text-green-500 mr-1" />
//                 <span className="text-green-600 dark:text-green-400">Connected</span>
//               </div>
//             </div>
//             <div className="flex items-center justify-between">
//               <span className="text-gray-600 dark:text-gray-400">Last Backup</span>
//               <span className="text-gray-600 dark:text-gray-400">2 hours ago</span>
//             </div>
//           </div>
//         </Card>
//       </div>
//     </div>
//   )
// }
// export default Dashboard;


// import React, { useState, useEffect } from 'react';
// import { 
//   BarChart3, 
//   Package, 
//   Truck, 
//   Users, 
//   TrendingUp, 
//   AlertTriangle,
//   CheckCircle,
//   Clock,
//   Wine,
//   Factory,
//   Cog
// } from 'lucide-react';
// import { useAuth } from '../../context/AuthContext';
// import { Card } from '../../components/ui/Card';
// import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
// import { inventoryService } from '../../services/inventoryService';
// import { deliveryService } from '../../services/deliveryService';
// import { customerService } from '../../services/customerService';
// import { supplierService } from '../../services/supplierService';
// import { productionMetricsService, ProductionMetrics, DeliveryMetrics } from '../../services/productionMetricsService';

// interface DashboardStats {
//   inventory: {
//     totalItems: number;
//     lowStockItems: number;
//     totalValue: number;
//   };
//   delivery: {
//     totalOrders: number;
//     pendingOrders: number;
//     deliveredOrders: number;
//   };
//   production: ProductionMetrics;
//   deliveryMetrics: DeliveryMetrics;
//   customers: number;
//   suppliers: number;
// }

// export const Dashboard: React.FC = () => {
//   const { user, hasRole } = useAuth();
//   const [stats, setStats] = useState<DashboardStats | null>(null);
//   const [isLoading, setIsLoading] = useState(true);
//   const [lowStockItems, setLowStockItems] = useState<any[]>([]);

//   useEffect(() => {
//     const fetchDashboardData = async () => {
//       try {
//         const promises = [];

//         // Fetch inventory data if user has permission
//         if (hasRole('ROLE_INVENTORY') || hasRole('ROLE_ADMIN')) {
//           promises.push(
//             inventoryService.getStats().catch(() => ({ totalItems: 0, lowStockItems: 0, totalValue: 0 })),
//             inventoryService.getLowStock().catch(() => [])
//           );
//         } else {
//           promises.push(
//             Promise.resolve({ totalItems: 0, lowStockItems: 0, totalValue: 0 }),
//             Promise.resolve([])
//           );
//         }

//         // Fetch delivery data if user has permission
//         if (hasRole('ROLE_LOGISTICS') || hasRole('ROLE_ADMIN')) {
//           promises.push(
//             deliveryService.getStatistics().catch(() => ({ totalOrders: 0, pendingOrders: 0, deliveredOrders: 0 })),
//             customerService.getAll(0, 1).catch(() => ({ totalElements: 0 }))
//           );
//         } else {
//           promises.push(
//             Promise.resolve({ totalOrders: 0, pendingOrders: 0, deliveredOrders: 0 }),
//             Promise.resolve({ totalElements: 0 })
//           );
//         }

//         // Fetch supplier data if user has permission
//         if (hasRole('ROLE_INVENTORY') || hasRole('ROLE_ADMIN')) {
//           promises.push(supplierService.getAll(0, 1).catch(() => ({ totalElements: 0 })));
//         } else {
//           promises.push(Promise.resolve({ totalElements: 0 }));
//         }

//         // Fetch production metrics
//         promises.push(productionMetricsService.getProductionMetrics());
//         promises.push(productionMetricsService.getDeliveryMetrics());

//         const [
//           inventoryStats, 
//           lowStock, 
//           deliveryStats, 
//           customersData, 
//           suppliersData,
//           productionMetrics,
//           deliveryMetrics
//         ] = await Promise.all(promises);

//         setStats({
//           inventory: (inventoryStats && 'totalItems' in inventoryStats && 'lowStockItems' in inventoryStats && 'totalValue' in inventoryStats)
//             ? inventoryStats
//             : { totalItems: 0, lowStockItems: 0, totalValue: 0 },
//           delivery: (deliveryStats && typeof deliveryStats === 'object' && 'totalOrders' in deliveryStats)
//             ? deliveryStats as { totalOrders: number; pendingOrders: number; deliveredOrders: number; }
//             : { totalOrders: 0, pendingOrders: 0, deliveredOrders: 0 },
//           production: productionMetrics && typeof productionMetrics === 'object' && 'totalProducts' in productionMetrics
//             ? (productionMetrics as ProductionMetrics)
//             : {
//                 totalProducts: 0,
//                 totalBatches: 0,
//                 activeBatches: 0,
//                 totalProductionLines: 0,
//                 operationalLines: 0,
//                 totalTasks: 0,
//                 completedTasks: 0,
//                 pendingTasks: 0,
//                 averageEfficiency: 0,
//               },
//           deliveryMetrics: deliveryMetrics as DeliveryMetrics,
//           customers: 'totalElements' in customersData ? customersData.totalElements : 0,
//           suppliers: 'totalElements' in suppliersData ? suppliersData.totalElements : 0,
//         });

//         setLowStockItems(Array.isArray(lowStock) ? lowStock : []);
//       } catch (error) {
//         console.error('Error fetching dashboard data:', error);
//       } finally {
//         setIsLoading(false);
//       }
//     };

//     fetchDashboardData();
//   }, [hasRole]);

//   if (isLoading) {
//     return (
//       <div className="flex items-center justify-center h-64">
//         <LoadingSpinner size="lg" />
//       </div>
//     );
//   }

//   const quickActions = [
//     { 
//       title: 'Add Inventory Item', 
//       href: '/dashboard/inventory', 
//       icon: Package, 
//       color: 'bg-blue-500',
//       roles: ['ROLE_INVENTORY', 'ROLE_ADMIN']
//     },
//     { 
//       title: 'Create Delivery', 
//       href: '/dashboard/deliveries', 
//       icon: Truck, 
//       color: 'bg-green-500',
//       roles: ['ROLE_LOGISTICS', 'ROLE_ADMIN']
//     },
//     { 
//       title: 'Production Management', 
//       href: '/dashboard/production', 
//       icon: Factory, 
//       color: 'bg-purple-500',
//       roles: ['ROLE_ADMIN', 'ROLE_PRODUCTION']
//     },
//     { 
//       title: 'View Reports', 
//       href: '/dashboard/reports', 
//       icon: BarChart3, 
//       color: 'bg-orange-500',
//       roles: ['ROLE_ADMIN', 'ROLE_INVENTORY', 'ROLE_LOGISTICS', 'ROLE_QUALITY']
//     },
//   ];

//   return (
//     <div className="space-y-6">
//       {/* Welcome Header */}
//       <div className="bg-gradient-to-r from-blue-600 to-purple-600 rounded-lg p-6 text-white">
//         <div className="flex items-center space-x-4">
//           <div className="bg-white/20 p-3 rounded-full">
//             <Wine className="h-8 w-8" />
//           </div>
//           <div>
//             <h1 className="text-2xl font-bold">Welcome back, {user?.username}!</h1>
//             <p className="text-blue-100">
//               Role: {user?.roles.join(', ')} | Here's your dashboard overview
//             </p>
//           </div>
//         </div>
//       </div>

//       {/* Stats Grid */}
//       <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
//         {(hasRole('ROLE_INVENTORY') || hasRole('ROLE_ADMIN')) && (
//           <>
//             <Card className="p-6">
//               <div className="flex items-center justify-between">
//                 <div>
//                   <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
//                     Total Inventory
//                   </p>
//                   <p className="text-2xl font-bold text-gray-900 dark:text-white">
//                     {stats?.inventory.totalItems || 0}
//                   </p>
//                 </div>
//                 <Package className="h-8 w-8 text-blue-500" />
//               </div>
//               <div className="mt-2 flex items-center text-sm">
//                 <TrendingUp className="h-4 w-4 text-green-500 mr-1" />
//                 <span className="text-green-600 dark:text-green-400">Items tracked</span>
//               </div>
//             </Card>

//             <Card className="p-6">
//               <div className="flex items-center justify-between">
//                 <div>
//                   <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
//                     Low Stock Alerts
//                   </p>
//                   <p className="text-2xl font-bold text-gray-900 dark:text-white">
//                     {stats?.inventory.lowStockItems || 0}
//                   </p>
//                 </div>
//                 <AlertTriangle className="h-8 w-8 text-orange-500" />
//               </div>
//               <div className="mt-2 flex items-center text-sm">
//                 <span className="text-orange-600 dark:text-orange-400">Items need restock</span>
//               </div>
//             </Card>
//           </>
//         )}

//         {(hasRole('ROLE_LOGISTICS') || hasRole('ROLE_ADMIN')) && (
//           <>
//             <Card className="p-6">
//               <div className="flex items-center justify-between">
//                 <div>
//                   <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
//                     Active Orders
//                   </p>
//                   <p className="text-2xl font-bold text-gray-900 dark:text-white">
//                     {stats?.deliveryMetrics.pendingOrders || 0}
//                   </p>
//                 </div>
//                 <Clock className="h-8 w-8 text-yellow-500" />
//               </div>
//               <div className="mt-2 flex items-center text-sm">
//                 <span className="text-yellow-600 dark:text-yellow-400">Pending delivery</span>
//               </div>
//             </Card>

//             <Card className="p-6">
//               <div className="flex items-center justify-between">
//                 <div>
//                   <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
//                     Active Drivers
//                   </p>
//                   <p className="text-2xl font-bold text-gray-900 dark:text-white">
//                     {stats?.deliveryMetrics.activeDrivers || 0}
//                   </p>
//                 </div>
//                 <Users className="h-8 w-8 text-green-500" />
//               </div>
//               <div className="mt-2 flex items-center text-sm">
//                 <CheckCircle className="h-4 w-4 text-green-500 mr-1" />
//                 <span className="text-green-600 dark:text-green-400">
//                   {stats?.deliveryMetrics.totalDrivers || 0} total
//                 </span>
//               </div>
//             </Card>
//           </>
//         )}

//         {(hasRole('ROLE_ADMIN') || hasRole('ROLE_PRODUCTION')) && (
//           <>
//             <Card className="p-6">
//               <div className="flex items-center justify-between">
//                 <div>
//                   <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
//                     Production Lines
//                   </p>
//                   <p className="text-2xl font-bold text-gray-900 dark:text-white">
//                     {stats?.production.operationalLines || 0}
//                   </p>
//                 </div>
//                 <Factory className="h-8 w-8 text-purple-500" />
//               </div>
//               <div className="mt-2 flex items-center text-sm">
//                 <span className="text-purple-600 dark:text-purple-400">
//                   {stats?.production.totalProductionLines || 0} total lines
//                 </span>
//               </div>
//             </Card>

//             <Card className="p-6">
//               <div className="flex items-center justify-between">
//                 <div>
//                   <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
//                     Pending Tasks
//                   </p>
//                   <p className="text-2xl font-bold text-gray-900 dark:text-white">
//                     {stats?.production.pendingTasks || 0}
//                   </p>
//                 </div>
//                 <Cog className="h-8 w-8 text-orange-500" />
//               </div>
//               <div className="mt-2 flex items-center text-sm">
//                 <span className="text-orange-600 dark:text-orange-400">
//                   {stats?.production.totalTasks || 0} total tasks
//                 </span>
//               </div>
//             </Card>
//           </>
//         )}
//       </div>

//       {/* Quick Actions */}
//       <Card className="p-6">
//         <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">
//           Quick Actions
//         </h2>
//         <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
//           {quickActions
//             .filter(action => action.roles.some(role => hasRole(role)))
//             .map((action) => (
//               <a
//                 key={action.title}
//                 href={action.href}
//                 className="flex items-center p-4 rounded-lg border border-gray-200 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-800 transition-colors"
//               >
//                 <div className={`${action.color} p-2 rounded-lg mr-3`}>
//                   <action.icon className="h-5 w-5 text-white" />
//                 </div>
//                 <span className="font-medium text-gray-900 dark:text-white">
//                   {action.title}
//                 </span>
//               </a>
//             ))}
//         </div>
//       </Card>

//       {/* Production Overview for Admins and Production Users */}
//       {(hasRole('ROLE_ADMIN') || hasRole('ROLE_PRODUCTION')) && stats?.production && (
//         <Card className="p-6">
//           <div className="flex items-center justify-between mb-4">
//             <h2 className="text-lg font-semibold text-gray-900 dark:text-white">
//               Production Overview
//             </h2>
//             <Factory className="h-5 w-5 text-purple-500" />
//           </div>
//           <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
//             <div className="text-center">
//               <div className="text-2xl font-bold text-purple-600 dark:text-purple-400">
//                 {stats.production.totalProducts}
//               </div>
//               <div className="text-sm text-gray-600 dark:text-gray-400">Total Products</div>
//             </div>
//             <div className="text-center">
//               <div className="text-2xl font-bold text-blue-600 dark:text-blue-400">
//                 {stats.production.activeBatches}
//               </div>
//               <div className="text-sm text-gray-600 dark:text-gray-400">Active Batches</div>
//             </div>
//             <div className="text-center">
//               <div className="text-2xl font-bold text-green-600 dark:text-green-400">
//                 {stats.production.completedTasks}
//               </div>
//               <div className="text-sm text-gray-600 dark:text-gray-400">Completed Tasks</div>
//             </div>
//             <div className="text-center">
//               <div className="text-2xl font-bold text-orange-600 dark:text-orange-400">
//                 {Math.round(stats.production.averageEfficiency)}%
//               </div>
//               <div className="text-sm text-gray-600 dark:text-gray-400">Avg Efficiency</div>
//             </div>
//           </div>
//         </Card>
//       )}

//       {/* Low Stock Alert */}
//       {lowStockItems.length > 0 && (
//         <Card className="p-6">
//           <div className="flex items-center justify-between mb-4">
//             <h2 className="text-lg font-semibold text-gray-900 dark:text-white">
//               Low Stock Alert
//             </h2>
//             <AlertTriangle className="h-5 w-5 text-orange-500" />
//           </div>
//           <div className="space-y-3">
//             {lowStockItems.slice(0, 5).map((item) => (
//               <div
//                 key={item.id}
//                 className="flex items-center justify-between p-3 bg-orange-50 dark:bg-orange-900/20 rounded-lg"
//               >
//                 <div>
//                   <p className="font-medium text-gray-900 dark:text-white">
//                     {item.itemName}
//                   </p>
//                   <p className="text-sm text-gray-600 dark:text-gray-400">
//                     SKU: {item.sku}
//                   </p>
//                 </div>
//                 <div className="text-right">
//                   <p className="text-sm font-medium text-orange-600 dark:text-orange-400">
//                     {item.currentQuantity} left
//                   </p>
//                   <p className="text-xs text-gray-500 dark:text-gray-400">
//                     Min: {item.minimumStockLevel}
//                   </p>
//                 </div>
//               </div>
//             ))}
//           </div>
//         </Card>
//       )}

//       {/* System Status and Performance */}
//       <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
//         <Card className="p-6">
//           <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">
//             System Status
//           </h2>
//           <div className="space-y-3">
//             <div className="flex items-center justify-between">
//               <span className="text-gray-600 dark:text-gray-400">API Status</span>
//               <div className="flex items-center">
//                 <CheckCircle className="h-4 w-4 text-green-500 mr-1" />
//                 <span className="text-green-600 dark:text-green-400">Operational</span>
//               </div>
//             </div>
//             <div className="flex items-center justify-between">
//               <span className="text-gray-600 dark:text-gray-400">Database</span>
//               <div className="flex items-center">
//                 <CheckCircle className="h-4 w-4 text-green-500 mr-1" />
//                 <span className="text-green-600 dark:text-green-400">Connected</span>
//               </div>
//             </div>
//             <div className="flex items-center justify-between">
//               <span className="text-gray-600 dark:text-gray-400">Last Backup</span>
//               <span className="text-gray-600 dark:text-gray-400">2 hours ago</span>
//             </div>
//           </div>
//         </Card>

//         <Card className="p-6">
//           <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">
//             Performance Metrics
//           </h2>
//           <div className="space-y-4">
//             <div>
//               <div className="flex justify-between text-sm mb-1">
//                 <span className="text-gray-600 dark:text-gray-400">System Load</span>
//                 <span className="text-gray-900 dark:text-white">23%</span>
//               </div>
//               <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
//                 <div className="bg-green-500 h-2 rounded-full w-[23%]" />
//               </div>
//             </div>
//             <div>
//               <div className="flex justify-between text-sm mb-1">
//                 <span className="text-gray-600 dark:text-gray-400">Storage Used</span>
//                 <span className="text-gray-900 dark:text-white">67%</span>
//               </div>
//               <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
//                 <div className="bg-blue-500 h-2 rounded-full w-[67%]" />
//               </div>
//             </div>
//             <div>
//               <div className="flex justify-between text-sm mb-1">
//                 <span className="text-gray-600 dark:text-gray-400">Memory</span>
//                 <span className="text-gray-900 dark:text-white">45%</span>
//               </div>
//               <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
//                 <div className="bg-yellow-500 h-2 rounded-full w-[45%]" />
//               </div>
//             </div>
//           </div>
//         </Card>
//       </div>
//     </div>
//   );
// };