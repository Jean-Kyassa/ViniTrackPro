import api from "../utils/api"

export interface DeliveryMetrics {
  totalOrders: number
  pendingOrders: number
  processingOrders: number
  inTransitOrders: number
  deliveredOrders: number
  cancelledOrders: number
  totalDrivers: number
  activeDrivers: number
  totalVehicles: number
  totalRoutes: number
  averageDeliveryTime: number
  onTimeDeliveryRate: number
}

export const deliveryMetricsService = {
  getMetrics: async (): Promise<DeliveryMetrics> => {
    try {
      const [orders, drivers, vehicles, routes] = await Promise.all([
        api.get("/delivery-orders").catch(() => ({ data: { content: [] } })),
        api.get("/drivers").catch(() => ({ data: [] })),
        api.get("/vehicles").catch(() => ({ data: [] })),
        api.get("/delivery-routes").catch(() => ({ data: [] })),
      ])

      const ordersData = orders.data.content || orders.data || []
      const driversData = drivers.data || []
      const vehiclesData = vehicles.data || []
      const routesData = routes.data || []

      const pendingOrders = ordersData.filter((order: any) => order.status === "PENDING").length
      const processingOrders = ordersData.filter((order: any) => order.status === "PROCESSING").length
      const inTransitOrders = ordersData.filter((order: any) => order.status === "IN_TRANSIT").length
      const deliveredOrders = ordersData.filter((order: any) => order.status === "DELIVERED").length
      const cancelledOrders = ordersData.filter((order: any) => order.status === "CANCELLED").length
      const activeDrivers = driversData.filter((driver: any) => driver.status === "ACTIVE").length

      return {
        totalOrders: ordersData.length,
        pendingOrders,
        processingOrders,
        inTransitOrders,
        deliveredOrders,
        cancelledOrders,
        totalDrivers: driversData.length,
        activeDrivers,
        totalVehicles: vehiclesData.length,
        totalRoutes: routesData.length,
        averageDeliveryTime: 2.5, // Mock data - you can calculate this from actual delivery times
        onTimeDeliveryRate: 94, // Mock data - you can calculate this from delivery dates
      }
    } catch (error) {
      console.error("Error fetching delivery metrics:", error)
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
        totalRoutes: 0,
        averageDeliveryTime: 0,
        onTimeDeliveryRate: 0,
      }
    }
  },
}
