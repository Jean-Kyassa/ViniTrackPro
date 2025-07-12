// export interface User {
//   id: number;
//   username: string;
//   email: string;
//   roles: string[];
// }

// export interface AuthResponse {
//   message: string;
//   user: User;
//   token: string;
//   type: string;
// }

// export interface LoginRequest {
//   username: string;
//   password: string;
// }

// export interface SignupRequest {
//   username: string;
//   email: string;
//   password: string;
//   role?: string[];
// }

// export interface ForgotPasswordRequest {
//   email: string;
// }

// export interface ResetPasswordRequest {
//   token: string;
//   newPassword: string;
// }

// export interface Customer {
//   id?: number;
//   companyName: string;
//   contactPerson: string;
//   email: string;
//   phone: string;
//   billingAddress: string;
//   shippingAddress: string;
//   paymentTerms: string;
//   taxId: string;
// }

// export interface InventoryItem {
//   id?: number;
//   itemName: string;
//   sku: string;
//   category: string;
//   unitType: string;
//   initialQuantity: number;
//   currentQuantity: number;
//   minimumStockLevel: number;
//   manufacturingDate: string;
//   expiryDate: string;
//   costPrice: number;
//   sellingPrice: number;
//   description: string;
//   storageLocation: string;
// }

// export interface DeliveryOrder {
//   id?: number;
//   orderNumber: string;
//   customerId: number;
//   companyName?: string;
//   deliveryAddress: string;
//   status: string;
//   orderDate: string;
//   deliveryDate?: string;
//   routeId?: number;
//   routeName?: string;
//   items?: OrderItem[];
//   deliveryInstructions: string;
//   deliveryFee: number;
//   totalAmount: number;
//   createdBy?: number;
//   createdAt?: string;
// }

// export interface OrderItem {
//   id?: number;
//   itemId: number;
//   itemName?: string;
//   quantity: number;
//   unitPrice: number;
//   totalPrice?: number;
// }

// export interface QualityCheck {
//   id?: number;
//   name: string;
//   type: string;
//   batchId: number;
//   batchNumber?: string;
//   checkDate: string;
//   status: string;
//   notes: string;
//   checkedById: number;
//   checkedByName?: string;
//   createdAt?: string;
// }

// export interface Supplier {
//   id?: number;
//   companyName: string;
//   contactPerson: string;
//   email: string;
//   phone: string;
//   businessAddress: string;
//   productCategories: string;
//   leadTime: number;
//   minimumOrderQuantity: number;
//   taxId: string;
//   paymentTerms: string;
//   currency: string;
// }

// export interface Report {
//   id?: number;
//   title: string;
//   type: string;
//   reportDate: string;
//   periodStart: string;
//   periodEnd: string;
//   summary: string;
//   findings: string;
//   recommendations: string;
//   generatedById?: number;
//   generatedByUsername?: string;
//   createdAt?: string;
// }

// export interface ApiResponse<T> {
//   data: T;
//   message?: string;
//   error?: string;
// }

// export interface PaginatedResponse<T> {
//   content: T[];
//   totalElements: number;
//   totalPages: number;
//   size: number;
//   number: number;
//   first: boolean;
//   last: boolean;
// }

// // Statistics interfaces
// export interface InventoryStats {
//   totalItems: number;
//   lowStockItems: number;
//   totalValue: number;
// }

// export interface DeliveryStats {
//   totalOrders: number;
//   pendingOrders: number;
//   processingOrders: number;
//   inTransitOrders: number;
//   deliveredOrders: number;
//   cancelledOrders: number;
// }

// export interface QualityStats {
//   PASSED: number;
//   FAILED: number;
//   PENDING: number;
// }


export interface User {
  id: number;
  username: string;
  email: string;
  roles: string[];
}

export interface AuthResponse {
  message: string;
  user: User;
  token: string;
  type: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface SignupRequest {
  username: string;
  email: string;
  password: string;
  role?: string[];
}

export interface ForgotPasswordRequest {
  email: string;
}

export interface ResetPasswordRequest {
  token: string;
  newPassword: string;
}

export interface Customer {
  id?: number;
  companyName: string;
  contactPerson: string;
  email: string;
  phone: string;
  billingAddress: string;
  shippingAddress: string;
  paymentTerms: string;
  taxId: string;
}

export interface InventoryItem {
  id?: number;
  itemName: string;
  sku: string;
  category: string;
  unitType: string;
  initialQuantity: number;
  currentQuantity: number;
  minimumStockLevel: number;
  manufacturingDate: string;
  expiryDate: string;
  costPrice: number;
  sellingPrice: number;
  description: string;
  storageLocation: string;
}

export interface DeliveryOrder {
  id?: number;
  orderNumber: string;
  customerId: number;
  companyName?: string;
  deliveryAddress: string;
  status: string;
  orderDate: string;
  deliveryDate?: string;
  routeId?: number;
  routeName?: string;
  items?: OrderItem[];
  deliveryInstructions: string;
  deliveryFee: number;
  totalAmount: number;
  createdBy?: number;
  createdAt?: string;
}

export interface OrderItem {
  id?: number;
  itemId: number;
  itemName?: string;
  quantity: number;
  unitPrice: number;
  totalPrice?: number;
}

export interface QualityCheck {
  id?: number;
  name: string;
  type: string;
  batchId: number;
  batchNumber?: string;
  checkDate: string;
  status: string;
  notes: string;
  checkedById: number;
  checkedByName?: string;
  createdAt?: string;
}

export interface Supplier {
  id?: number;
  companyName: string;
  contactPerson: string;
  email: string;
  phone: string;
  businessAddress: string;
  productCategories: string;
  leadTime: number;
  minimumOrderQuantity: number;
  taxId: string;
  paymentTerms: string;
  currency: string;
}

export interface Report {
  id?: number;
  title: string;
  type: string;
  reportDate: string;
  periodStart: string;
  periodEnd: string;
  summary: string;
  findings: string;
  recommendations: string;
  generatedById?: number;
  generatedByUsername?: string;
  createdAt?: string;
}

// New interfaces for Production and Delivery features
export interface Driver {
  id?: number;
  name: string;
  licenseNumber: string;
  phone: string;
  email: string;
  status: 'ACTIVE' | 'ON_BREAK' | 'OFF_DUTY' | 'ON_LEAVE';
  rating: number;
  vehicleId?: number;
  vehicleRegistrationNumber?: string;
  currentRouteId?: number;
  currentRouteName?: string;
  createdById?: number;
  createdByName?: string;
  createdAt?: string;
}

export interface Vehicle {
  id?: number;
  registrationNumber: string;
  make: string;
  model: string;
  type: 'VAN' | 'TRUCK' | 'CAR' | 'MOTORBIKE';
  capacity: number;
  lastMaintenanceDate?: string;
  createdById?: number;
  createdByName?: string;
  createdAt?: string;
}

export interface DeliveryRoute {
  id?: number;
  name: string;
  driverId: number;
  driverName?: string;
  vehicleId: number;
  vehicleRegistrationNumber?: string;
  date: string;
  orders?: DeliveryOrder[];
  totalDistance: number;
  estimatedDuration: number;
  totalStops: number;
  totalLoad: number;
  createdById?: number;
  createdByName?: string;
  createdAt?: string;
}

export interface Product {
  productId?: number;
  name: string;
  code: string;
  description?: string;
  category: string;
  price: number;
  unit: string;
  createdAt?: string;
}

export interface ProductionBatch {
  id?: number;
  batchNumber: string;
  productId: number;
  productName?: string;
  productCode?: string;
  quantity: number;
  productionDate: string;
  expiryDate: string;
  createdById?: number;
  createdByName?: string;
  createdAt?: string;
  qualityChecks?: QualityCheck[];
}

export interface ProductionLine {
  id?: number;
  name: string;
  type: 'BOTTLING' | 'LABELING' | 'PACKAGING' | 'MIXING' | 'FERMENTATION';
  efficiency: number;
  lastMaintenance?: string;
  status: 'OPERATIONAL' | 'MAINTENANCE' | 'SHUTDOWN';
}

export interface ProductionSchedule {
  id?: number;
  name: string;
  productionLineId: number;
  productionLineName?: string;
  startTime: string;
  endTime: string;
  status: 'DRAFT' | 'SCHEDULED' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';
  createdById?: number;
  createdByUsername?: string;
  createdAt?: string;
  tasks?: ProductionTask[];
}

export interface ProductionTask {
  id?: number;
  name: string;
  description?: string;
  productId: number;
  productName?: string;
  quantity: number;
  priority: number;
  status: 'PENDING' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED' | 'ON_HOLD';
  estimatedStart?: string;
  estimatedEnd?: string;
  dueDate?: string; // Added dueDate property
  scheduleId?: number; // Add this line
  scheduleName?: string;
}

export interface ProductionMetrics {
  totalProducts: number;
  activeBatches: number;
  activeLines: number;
  pendingTasks: number;
  efficiency: number;
  completedTasksToday: number;
}

export interface ApiResponse<T> {
  data: T;
  message?: string;
  error?: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

// Statistics interfaces
export interface InventoryStats {
  totalItems: number;
  lowStockItems: number;
  totalValue: number;
}

export interface DeliveryStats {
  totalOrders: number;
  pendingOrders: number;
  processingOrders: number;
  inTransitOrders: number;
  deliveredOrders: number;
  cancelledOrders: number;
}

export interface QualityStats {
  PASSED: number;
  FAILED: number;
  PENDING: number;
}

export interface ProductionStats {
  totalProducts: number;
  totalBatches: number;
  activeBatches: number;
  totalProductionLines: number;
  operationalLines: number;
  totalTasks: number;
  completedTasks: number;
  pendingTasks: number;
}

export interface Message {
  id: string;
  content: string;
  sender: 'user' | 'logistics' | 'admin';
  timestamp: Date;
  senderName: string;
}