export interface DeliveryOrder {
    id?: number;
    orderNumber: string;
    customerId: number;
    companyName?: string;
    deliveryAddress: string;
    status: OrderStatus;
    orderDate: string; // ISO string format
    deliveryDate?: string; // ISO string format
    routeId?: number;
    routeName?: string;
    items?: OrderItem[];
    deliveryInstructions?: string;
    deliveryFee: number;
    totalAmount: number;
    createdBy?: number;
    createdAt?: string; // ISO string format
  }
  
  export interface OrderItem {
    id?: number;
    itemId?: number;
    itemName?: string;
    quantity: number;
    unitPrice: number;
    totalPrice: number;
  }
  
  export enum OrderStatus {
    PENDING = 'PENDING',
    PROCESSING = 'PROCESSING',
    IN_TRANSIT = 'IN_TRANSIT',
    DELIVERED = 'DELIVERED',
    CANCELLED = 'CANCELLED'
  }
  