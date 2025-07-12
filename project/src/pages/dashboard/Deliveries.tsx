// import React, { useState, useEffect } from 'react';
// import { Plus, Search, Edit, Truck, Clock, CheckCircle, X, Car, Users, Route } from 'lucide-react';
// import { Button } from '../../components/ui/Button';
// import { Input } from '../../components/ui/Input';
// import { Card } from '../../components/ui/Card';
// import { Table, TableHeader, TableBody, TableRow, TableHead, TableCell } from '../../components/ui/Table';
// import { Modal } from '../../components/ui/Modal';
// import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
// import { deliveryService } from '../../services/deliveryService';
// import { driverService } from '../../services/driverService';
// import { vehicleService } from '../../services/vehicleService';
// import { deliveryRouteService } from '../../services/deliveryRouteService';
// import { customerService } from '../../services/customerService';
// import { DeliveryOrder, Customer, Driver, Vehicle, DeliveryRoute } from '../../types';
// import { useAuth } from '../../context/AuthContext';
// import jsPDF from 'jspdf';
// import 'jspdf-autotable';
// import { format } from 'date-fns';

// type ExtendedVehicle = Vehicle & { driverId?: number };

// export const Deliveries: React.FC = () => {
//   const { hasRole } = useAuth();
//   const [orders, setOrders] = useState<DeliveryOrder[]>([]);
//   const [drivers, setDrivers] = useState<Driver[]>([]);
//   const [vehicles, setVehicles] = useState<ExtendedVehicle[]>([]);
//   const [routes, setRoutes] = useState<DeliveryRoute[]>([]);
//   const [customers, setCustomers] = useState<Customer[]>([]);
//   const [isLoading, setIsLoading] = useState(true);
//   const [isExporting, setIsExporting] = useState(false);
//   const [searchQuery, setSearchQuery] = useState('');
//   const [currentPage, setCurrentPage] = useState(0);
//   const [totalPages, setTotalPages] = useState(0);

//   // Modal states
//   const [isOrderModalOpen, setIsOrderModalOpen] = useState(false);
//   const [isDriverModalOpen, setIsDriverModalOpen] = useState(false);
//   const [isVehicleModalOpen, setIsVehicleModalOpen] = useState(false);
//   const [isRouteModalOpen, setIsRouteModalOpen] = useState(false);

//   const [editingOrder, setEditingOrder] = useState<DeliveryOrder | null>(null);
//   const [editingDriver, setEditingDriver] = useState<Driver | null>(null);
//   const [editingVehicle, setEditingVehicle] = useState<Vehicle | null>(null);
//   const [editingRoute, setEditingRoute] = useState<DeliveryRoute | null>(null);

//   // Form data states
//   const [orderFormData, setOrderFormData] = useState<Partial<DeliveryOrder>>({
//     orderNumber: '',
//     customerId: 0,
//     deliveryAddress: '',
//     status: 'PENDING',
//     deliveryInstructions: '',
//     deliveryFee: 0,
//     totalAmount: 0,
//   });

//   const [driverFormData, setDriverFormData] = useState<Partial<Driver>>({
//     name: '',
//     licenseNumber: '',
//     phone: '',
//     email: '',
//     status: 'ACTIVE',
//     rating: 5,
//     vehicleId: undefined,
//   });

//   const [vehicleFormData, setVehicleFormData] = useState<Partial<Vehicle>>({
//     registrationNumber: '',
//     make: '',
//     model: '',
//     type: 'VAN',
//     capacity: 0,
//     lastMaintenanceDate: '',
//   });

//   const [routeFormData, setRouteFormData] = useState<Partial<DeliveryRoute>>({
//     name: '',
//     driverId: 0,
//     vehicleId: 0,
//     date: new Date().toISOString().split('T')[0],
//     totalDistance: 0,
//     estimatedDuration: 0,
//     totalStops: 0,
//     totalLoad: 0,
//   });

//   useEffect(() => {
//     fetchAllData();
//   }, [currentPage, searchQuery]);

//   const fetchAllData = async () => {
//     try {
//       setIsLoading(true);
//       await Promise.all([
//         fetchOrders(),
//         fetchDrivers(),
//         fetchVehicles(),
//         fetchRoutes(),
//         fetchCustomers(),
//       ]);
//     } catch (error) {
//       console.error('Error fetching data:', error);
//     } finally {
//       setIsLoading(false);
//     }
//   };

//   const fetchOrders = async () => {
//     try {
//       let response;
//       if (searchQuery) {
//         response = await deliveryService.search(searchQuery, currentPage, 10);
//       } else {
//         response = await deliveryService.getAll(currentPage, 10);
//       }
//       setOrders(response.content);
//       setTotalPages(response.totalPages);
//     } catch (error) {
//       console.error('Error fetching delivery orders:', error);
//     }
//   };

//   const fetchDrivers = async () => {
//     try {
//       const data = await driverService.getAll();
//       setDrivers(data);
//     } catch (error) {
//       console.error('Error fetching drivers:', error);
//     }
//   };

//   const fetchVehicles = async () => {
//     try {
//       const data = await vehicleService.getAll();
//       setVehicles(data);
//     } catch (error) {
//       console.error('Error fetching vehicles:', error);
//     }
//   };

//   const fetchRoutes = async () => {
//     try {
//       const data = await deliveryRouteService.getAll();
//       setRoutes(data);
//     } catch (error) {
//       console.error('Error fetching routes:', error);
//     }
//   };

//   const fetchCustomers = async () => {
//     try {
//       const response = await customerService.getAll(0, 100);
//       setCustomers(response.content);
//     } catch (error) {
//       console.error('Error fetching customers:', error);
//     }
//   };

//   // Order handlers
//   const handleOrderSubmit = async (e: React.FormEvent) => {
//     e.preventDefault();
//     try {
//       if (editingOrder) {
//         await deliveryService.update(editingOrder.id!, orderFormData as DeliveryOrder);
//       } else {
//         const orderData = {
//           ...orderFormData,
//           orderNumber: orderFormData.orderNumber || `ORD-${Date.now()}`,
//           orderDate: new Date().toISOString(),
//         };
//         await deliveryService.create(orderData as DeliveryOrder);
//       }
//       setIsOrderModalOpen(false);
//       setEditingOrder(null);
//       resetOrderForm();
//       fetchOrders();
//     } catch (error) {
//       console.error('Error saving delivery order:', error);
//     }
//   };

//   // Driver handlers
//   const handleDriverSubmit = async (e: React.FormEvent) => {
//     e.preventDefault();
//     try {
//       if (editingDriver) {
//         await driverService.update(editingDriver.id!, driverFormData as Driver);
//       } else {
//         await driverService.create(driverFormData as Driver);
//       }
//       setIsDriverModalOpen(false);
//       setEditingDriver(null);
//       resetDriverForm();
//       fetchDrivers();
//     } catch (error) {
//       console.error('Error saving driver:', error);
//     }
//   };

//   // Vehicle handlers
//   const handleVehicleSubmit = async (e: React.FormEvent) => {
//     e.preventDefault();
//     try {
//       if (editingVehicle) {
//         await vehicleService.update(editingVehicle.id!, vehicleFormData as Vehicle);
//       } else {
//         await vehicleService.create(vehicleFormData as Vehicle);
//       }
//       setIsVehicleModalOpen(false);
//       setEditingVehicle(null);
//       resetVehicleForm();
//       fetchVehicles();
//     } catch (error) {
//       console.error('Error saving vehicle:', error);
//     }
//   };

//   // Route handlers
//   const handleRouteSubmit = async (e: React.FormEvent) => {
//     e.preventDefault();
//     try {
//       if (editingRoute) {
//         await deliveryRouteService.update(editingRoute.id!, routeFormData as DeliveryRoute);
//       } else {
//         await deliveryRouteService.create(routeFormData as DeliveryRoute);
//       }
//       setIsRouteModalOpen(false);
//       setEditingRoute(null);
//       resetRouteForm();
//       fetchRoutes();
//     } catch (error) {
//       console.error('Error saving route:', error);
//     }
//   };

//   const handleStatusUpdate = async (id: number, status: string) => {
//     try {
//       await deliveryService.updateStatus(id, status);
//       fetchOrders();
//     } catch (error) {
//       console.error('Error updating order status:', error);
//     }
//   };

//   const resetOrderForm = () => {
//     setOrderFormData({
//       orderNumber: '',
//       customerId: 0,
//       deliveryAddress: '',
//       status: 'PENDING',
//       deliveryInstructions: '',
//       deliveryFee: 0,
//       totalAmount: 0,
//     });
//   };

//   const resetDriverForm = () => {
//     setDriverFormData({
//       name: '',
//       licenseNumber: '',
//       phone: '',
//       email: '',
//       status: 'ACTIVE',
//       rating: 5,
//       vehicleId: undefined,
//     });
//   };

//   const resetVehicleForm = () => {
//     setVehicleFormData({
//       registrationNumber: '',
//       make: '',
//       model: '',
//       type: 'VAN',
//       capacity: 0,
//       lastMaintenanceDate: '',
//     });
//   };

//   const resetRouteForm = () => {
//     setRouteFormData({
//       name: '',
//       driverId: 0,
//       vehicleId: 0,
//       date: new Date().toISOString().split('T')[0],
//       totalDistance: 0,
//       estimatedDuration: 0,
//       totalStops: 0,
//       totalLoad: 0,
//     });
//   };

//   const getStatusIcon = (status: string) => {
//     switch (status) {
//       case 'PENDING':
//         return Clock;
//       case 'PROCESSING':
//         return Edit;
//       case 'IN_TRANSIT':
//         return Truck;
//       case 'DELIVERED':
//         return CheckCircle;
//       case 'CANCELLED':
//         return X;
//       default:
//         return Clock;
//     }
//   };

//   const getStatusColor = (status: string) => {
//     switch (status) {
//       case 'PENDING':
//         return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200';
//       case 'PROCESSING':
//         return 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200';
//       case 'IN_TRANSIT':
//         return 'bg-purple-100 text-purple-800 dark:bg-purple-900 dark:text-purple-200';
//       case 'DELIVERED':
//         return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200';
//       case 'CANCELLED':
//         return 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200';
//       default:
//         return 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200';
//     }
//   };

 
//   //   try {
//   //     setIsExporting(true);
      
//   //     const doc = new jsPDF();
//   //     // Company information
//   //     const companyName = "VINI TRACK";
//   //     const companyAbbr = "(VT)";
//   //     const companyEmail = "vinittrack@gmail.com";
//   //     const companyPhone = "+243 123 456 789";
//   //     const companyAddress = "123 Innovation Avenue, Commune de Kinshasa, Ville de Kinshasa";
      
//   //     // Page dimensions
//   //     const pageWidth = doc.internal.pageSize.getWidth();
      
//   //     // Header section
//   //     doc.setFontSize(18);
//   //     doc.setTextColor(66, 139, 202);
//   //     doc.text(companyName, 14, 20);
          
//   //     doc.setFontSize(14);
//   //     doc.setTextColor(100, 100, 100);
//   //     doc.text(companyAbbr, 14, 28);
      
//   //     // Contact info
//   //     doc.setFontSize(10);
//   //     doc.setTextColor(0, 0, 0);
//   //     doc.text(`Email: ${companyEmail} | Phone: ${companyPhone}`, 14, 36);
          
//   //     // Decorative line
//   //     doc.setLineWidth(1);
//   //     doc.setDrawColor(66, 139, 202);
//   //     doc.line(14, 42, pageWidth - 14, 42);
      
//   //     // Report title
//   //     doc.setFontSize(20);
//   //     doc.setTextColor(0, 0, 0);
//   //     doc.text('DELIVERY MANAGEMENT REPORT', pageWidth / 2, 54, { align: 'center' });
      
//   //     // Report metadata
//   //     doc.setFontSize(12);
//   //     doc.text(`Generated on: ${format(new Date(), 'MMM d, yyyy')}`, 14, 64);
//   //     doc.text(`Total Orders: ${orders.length}`, 14, 72);
//   //     doc.text(`Total Drivers: ${drivers.length}`, 14, 80);
//   //     doc.text(`Total Vehicles: ${vehicles.length}`, 14, 88);
//   //     doc.text(`Total Routes: ${routes.length}`, 14, 96);
      
//   //     // Orders table
//   //     doc.setFontSize(16);
//   //     doc.text('DELIVERY ORDERS', 14, 110);
      
//   //     const orderTableData = orders.map(order => [
//   //       order.orderNumber ?? '',
//   //       order.companyName ?? '',
//   //       order.deliveryAddress ?? '',
//   //       order.status ?? '',
//   //       format(new Date(order.orderDate), 'MMM d, yyyy'),
//   //       `$${(order.totalAmount ?? 0).toFixed(2)}`
//   //     ]);
      
//   //     (doc as any).autoTable({
//   //       startY: 115,
//   //       head: [['Order #', 'Customer', 'Address', 'Status', 'Date', 'Amount']],
//   //       body: orderTableData,
//   //       styles: {
//   //         fontSize: 9,
//   //         cellPadding: 2,
//   //         valign: 'middle'
//   //       },
//   //       headStyles: {
//   //         fillColor: [66, 139, 202],
//   //         textColor: 255,
//   //         fontSize: 10
//   //       },
//   //       alternateRowStyles: {
//   //         fillColor: [240, 240, 240]
//   //       }
//   //     });
      
//   //     // Drivers table
//   //     doc.setFontSize(16);
//   //     doc.text('DRIVERS', 14, (doc as any).lastAutoTable.finalY + 15);
      
//   //     const driverTableData = drivers.map(driver => [
//   //       driver.name ?? '',
//   //       driver.licenseNumber ?? '',
//   //       driver.phone ?? '',
//   //       driver.email ?? '',
//   //       driver.status ?? '',
//   //       driver.rating?.toString() ?? ''
//   //     ]);
      
//   //     (doc as any).autoTable({
//   //       startY: (doc as any).lastAutoTable.finalY + 20,
//   //       head: [['Name', 'License', 'Phone', 'Email', 'Status', 'Rating']],
//   //       body: driverTableData,
//   //       styles: {
//   //         fontSize: 9,
//   //         cellPadding: 2,
//   //         valign: 'middle'
//   //       },
//   //       headStyles: {
//   //         fillColor: [66, 139, 202],
//   //         textColor: 255,
//   //         fontSize: 10
//   //       }
//   //     });
      
//   //     // Footer
//   //     const footerY = doc.internal.pageSize.getHeight() - 25;
//   //     doc.setLineWidth(0.5);
//   //     doc.line(14, footerY - 2, pageWidth - 14, footerY - 2);
          
//   //     doc.setFontSize(8);
//   //     doc.setTextColor(80, 80, 80);
//   //     doc.text(companyAddress, pageWidth / 2, footerY + 5, { align: 'center' });
//   //     doc.text(`Page ${doc.getNumberOfPages()}`, pageWidth / 2, footerY + 10, { align: 'center' });
      
//   //     // Save the PDF
//   //     doc.save(`VT-Delivery-Report-${format(new Date(), 'yyyy-MM-dd')}.pdf`);
      
//   //   } catch (error) {
//   //     console.error('Error generating PDF:', error);
//   //     alert('Failed to generate PDF. Please check the console for details.');
//   //   } finally {
//   //     setIsExporting(false);
//   //   }
//   // };

//   const generateDeliveryPDF = () => {
//     try {
//       setIsExporting(true);
      
//       const doc = new jsPDF();
//       // Company information
//       const companyName = "VINI TRACK";
//       const companyAbbr = "(VT)";
//       const companyEmail = "vinittrack@gmail.com";
//       const companyPhone = "+243 123 456 789";
//       const companyAddress = "123 Innovation Avenue, Commune de Kinshasa, Ville de Kinshasa";
      
//       // Page dimensions
//       const pageWidth = doc.internal.pageSize.getWidth();
      
//       // Header section
//       doc.setFontSize(18);
//       doc.setTextColor(66, 139, 202);
//       doc.text(companyName, 14, 20);
          
//       doc.setFontSize(14);
//       doc.setTextColor(100, 100, 100);
//       doc.text(companyAbbr, 14, 28);
      
//       // Contact info
//       doc.setFontSize(10);
//       doc.setTextColor(0, 0, 0);
//       doc.text(`Email: ${companyEmail} | Phone: ${companyPhone}`, 14, 36);
          
//       // Decorative line
//       doc.setLineWidth(1);
//       doc.setDrawColor(66, 139, 202);
//       doc.line(14, 42, pageWidth - 14, 42);
      
//       // Report title
//       doc.setFontSize(20);
//       doc.setTextColor(0, 0, 0);
//       doc.text('DELIVERY MANAGEMENT REPORT', pageWidth / 2, 54, { align: 'center' });
      
//       // Report metadata
//       doc.setFontSize(12);
//       doc.text(`Generated on: ${format(new Date(), 'MMM d, yyyy')}`, 14, 64);
//       doc.text(`Total Orders: ${orders.length}`, 14, 72);
//       doc.text(`Total Drivers: ${drivers.length}`, 14, 80);
//       doc.text(`Total Vehicles: ${vehicles.length}`, 14, 88);
//       doc.text(`Total Routes: ${routes.length}`, 14, 96);
      
//       // Orders table
//       doc.setFontSize(16);
//       doc.text('DELIVERY ORDERS', 14, 110);
      
//       const orderTableData = orders.map(order => [
//         order.orderNumber ?? '',
//         order.companyName ?? '',
//         order.deliveryAddress ?? '',
//         order.status ?? '',
//         format(new Date(order.orderDate), 'MMM d, yyyy'),
//         `$${(order.totalAmount ?? 0).toFixed(2)}`
//       ]);
      
//       doc.autoTable({
//         startY: 115,
//         head: [['Order #', 'Customer', 'Address', 'Status', 'Date', 'Amount']],
//         body: orderTableData,
//         styles: {
//           fontSize: 9,
//           cellPadding: 2,
//           valign: 'middle'
//         },
//         headStyles: {
//           fillColor: [66, 139, 202],
//           textColor: 255,
//           fontSize: 10
//         },
//         alternateRowStyles: {
//           fillColor: [240, 240, 240]
//         }
//       });
      
//       // Drivers table
//       const finalY = (doc as any).lastAutoTable.finalY;
//       doc.setFontSize(16);
//       doc.text('DRIVERS', 14, finalY + 15);
      
//       const driverTableData = drivers.map(driver => [
//         driver.name ?? '',
//         driver.licenseNumber ?? '',
//         driver.phone ?? '',
//         driver.email ?? '',
//         driver.status ?? '',
//         driver.rating?.toString() ?? ''
//       ]);
      
//       doc.autoTable({
//         startY: finalY + 20,
//         head: [['Name', 'License', 'Phone', 'Email', 'Status', 'Rating']],
//         body: driverTableData,
//         styles: {
//           fontSize: 9,
//           cellPadding: 2,
//           valign: 'middle'
//         },
//         headStyles: {
//           fillColor: [66, 139, 202],
//           textColor: 255,
//           fontSize: 10
//         }
//       });
      
//       // Footer
//       const footerY = doc.internal.pageSize.getHeight() - 25;
//       doc.setLineWidth(0.5);
//       doc.line(14, footerY - 2, pageWidth - 14, footerY - 2);
          
//       doc.setFontSize(8);
//       doc.setTextColor(80, 80, 80);
//       doc.text(companyAddress, pageWidth / 2, footerY + 5, { align: 'center' });
//       doc.text(`Page ${doc.getNumberOfPages()}`, pageWidth / 2, footerY + 10, { align: 'center' });
      
//       // Save the PDF
//       doc.save(`VT-Delivery-Report-${format(new Date(), 'yyyy-MM-dd')}.pdf`);
      
//     } catch (error) {
//       console.error('Error generating PDF:', error);
//       alert('Failed to generate PDF. Please check the console for details.');
//     } finally {
//       setIsExporting(false);
//     }
//   };
  

//   if (isLoading && currentPage === 0) {
//     return (
//       <div className="flex items-center justify-center h-64">
//         <LoadingSpinner size="lg" />
//       </div>
//     );
//   }

//   return (
//     <div className="space-y-6">
//       <div>
//         <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Delivery Management</h1>
//         <p className="text-gray-600 dark:text-gray-400">Track and manage delivery orders, drivers, vehicles, and routes</p>
//       </div>

//       {/* Summary Cards */}
//       <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
//         <Card className="bg-gradient-to-r from-blue-500 to-blue-600 text-white p-6">
//           <div className="flex items-center justify-between">
//             <div>
//               <h3 className="text-lg font-semibold">Total Drivers</h3>
//               <p className="text-3xl font-bold">{drivers.length}</p>
//               <p className="text-blue-100 mt-2">Active: {drivers.filter(d => d.status === 'ACTIVE').length}</p>
//             </div>
//             <div className="bg-blue-400/20 p-4 rounded-full">
//               <Users className="h-8 w-8" />
//             </div>
//           </div>
//         </Card>

//         <Card className="bg-gradient-to-r from-purple-500 to-purple-600 text-white p-6">
//           <div className="flex items-center justify-between">
//             <div>
//               <h3 className="text-lg font-semibold">Total Vehicles</h3>
//               <p className="text-3xl font-bold">{vehicles.length}</p>
//               <p className="text-purple-100 mt-2">Available: {vehicles.filter(v => !v.driverId).length}</p>
//             </div>
//             <div className="bg-purple-400/20 p-4 rounded-full">
//               <Car className="h-8 w-8" />
//             </div>
//           </div>
//         </Card>

//         <Card className="bg-gradient-to-r from-green-500 to-green-600 text-white p-6">
//           <div className="flex items-center justify-between">
//             <div>
//               <h3 className="text-lg font-semibold">Total Routes</h3>
//               <p className="text-3xl font-bold">{routes.length}</p>
//               <p className="text-green-100 mt-2">Today: {routes.filter(r => new Date(r.date).toDateString() === new Date().toDateString()).length}</p>
//             </div>
//             <div className="bg-green-400/20 p-4 rounded-full">
//               <Route className="h-8 w-8" />
//             </div>
//           </div>
//         </Card>
//       </div>

//       <div className="flex justify-between items-center">
//         <div className="flex-1 max-w-md">
//           <Input
//             placeholder="Search orders..."
//             value={searchQuery}
//             onChange={(e) => setSearchQuery(e.target.value)}
//             icon={Search}
//           />
//         </div>
//         <div className="flex space-x-2">
//         <Button
//   onClick={generateDeliveryPDF}
//   variant="outline"
//   disabled={isExporting}
// >
//   {isExporting ? (
//     <>
//       <LoadingSpinner size="sm" className="mr-2" />
//       Exporting...
//     </>
//   ) : (
//     'Export PDF'
//   )}
// </Button>
//           <Button
//             onClick={() => {
//               setEditingOrder(null);
//               resetOrderForm();
//               setIsOrderModalOpen(true);
//             }}
//             icon={Plus}
//           >
//             Create Order
//           </Button>
//         </div>
//       </div>

//       {/* Management Buttons for Admin */}
//       {hasRole('ROLE_ADMIN') && (
//         <Card className="p-4">
//           <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">
//             Delivery Management Tools
//           </h2>
//           <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
//             <Button
//               onClick={() => {
//                 setEditingDriver(null);
//                 resetDriverForm();
//                 setIsDriverModalOpen(true);
//               }}
//               icon={Users}
//               variant="outline"
//               className="w-full"
//             >
//               Manage Drivers
//             </Button>
//             <Button
//               onClick={() => {
//                 setEditingVehicle(null);
//                 resetVehicleForm();
//                 setIsVehicleModalOpen(true);
//               }}
//               icon={Car}
//               variant="outline"
//               className="w-full"
//             >
//               Manage Vehicles
//             </Button>
//             <Button
//               onClick={() => {
//                 setEditingRoute(null);
//                 resetRouteForm();
//                 setIsRouteModalOpen(true);
//               }}
//               icon={Route}
//               variant="outline"
//               className="w-full"
//             >
//               Manage Routes
//             </Button>
//           </div>
//         </Card>
//       )}

//       <Card className="p-6">
//         <Table>
//           <TableHeader>
//             <TableRow>
//               <TableHead>Order Number</TableHead>
//               <TableHead>Customer</TableHead>
//               <TableHead>Delivery Address</TableHead>
//               <TableHead>Status</TableHead>
//               <TableHead>Order Date</TableHead>
//               <TableHead>Total Amount</TableHead>
//               <TableHead>Actions</TableHead>
//             </TableRow>
//           </TableHeader>
//           <TableBody>
//             {orders.map((order) => {
//               const StatusIcon = getStatusIcon(order.status);
//               return (
//                 <TableRow key={order.id}>
//                   <TableCell>
//                     <div className="font-medium">{order.orderNumber}</div>
//                   </TableCell>
//                   <TableCell>
//                     <div>
//                       <div className="font-medium">{order.companyName}</div>
//                     </div>
//                   </TableCell>
//                   <TableCell>
//                     <div className="max-w-xs truncate">{order.deliveryAddress}</div>
//                   </TableCell>
//                   <TableCell>
//                     <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(order.status)}`}>
//                       <StatusIcon className="h-3 w-3 mr-1" />
//                       {order.status}
//                     </span>
//                   </TableCell>
//                   <TableCell>
//                     {new Date(order.orderDate).toLocaleDateString()}
//                   </TableCell>
//                   <TableCell>
//                     ${order.totalAmount.toFixed(2)}
//                   </TableCell>
//                   <TableCell>
//                     <div className="flex space-x-2">
//                       <Button
//                         variant="ghost"
//                         size="sm"
//                         onClick={() => {
//                           setEditingOrder(order);
//                           setOrderFormData(order);
//                           setIsOrderModalOpen(true);
//                         }}
//                         icon={Edit}
//                       >
//                         Edit
//                       </Button>
//                       {order.status === 'PENDING' && (
//                         <Button
//                           variant="ghost"
//                           size="sm"
//                           onClick={() => handleStatusUpdate(order.id!, 'PROCESSING')}
//                           className="text-blue-600 hover:text-blue-700"
//                         >
//                           Process
//                         </Button>
//                       )}
//                       {order.status === 'PROCESSING' && (
//                         <Button
//                           variant="ghost"
//                           size="sm"
//                           onClick={() => handleStatusUpdate(order.id!, 'IN_TRANSIT')}
//                           className="text-purple-600 hover:text-purple-700"
//                         >
//                           Ship
//                         </Button>
//                       )}
//                       {order.status === 'IN_TRANSIT' && (
//                         <Button
//                           variant="ghost"
//                           size="sm"
//                           onClick={() => handleStatusUpdate(order.id!, 'DELIVERED')}
//                           className="text-green-600 hover:text-green-700"
//                         >
//                           Deliver
//                         </Button>
//                       )}
//                     </div>
//                   </TableCell>
//                 </TableRow>
//               );
//             })}
//           </TableBody>
//         </Table>

//         {totalPages > 1 && (
//           <div className="flex justify-between items-center mt-6">
//             <Button
//               variant="outline"
//               onClick={() => setCurrentPage(prev => Math.max(0, prev - 1))}
//               disabled={currentPage === 0}
//             >
//               Previous
//             </Button>
//             <span className="text-sm text-gray-600 dark:text-gray-400">
//               Page {currentPage + 1} of {totalPages}
//             </span>
//             <Button
//               variant="outline"
//               onClick={() => setCurrentPage(prev => Math.min(totalPages - 1, prev + 1))}
//               disabled={currentPage === totalPages - 1}
//             >
//               Next
//             </Button>
//           </div>
//         )}
//       </Card>

//       {/* Order Modal */}
//       <Modal
//         isOpen={isOrderModalOpen}
//         onClose={() => {
//           setIsOrderModalOpen(false);
//           setEditingOrder(null);
//           resetOrderForm();
//         }}
//         title={editingOrder ? 'Edit Delivery Order' : 'Create New Delivery Order'}
//         size="lg"
//       >
//         <form onSubmit={handleOrderSubmit} className="space-y-4">
//           <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
//             <div>
//               <label htmlFor="orderNumber" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Order Number
//               </label>
//               <Input
//                 id="orderNumber"
//                 name="orderNumber"
//                 value={orderFormData.orderNumber}
//                 onChange={(e) => setOrderFormData(prev => ({ ...prev, orderNumber: e.target.value }))}
//                 placeholder="Auto-generated if empty"
//               />
//             </div>
//             <div>
//               <label htmlFor="customerId" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Customer
//               </label>
//               <select
//                 id="customerId"
//                 name="customerId"
//                 value={orderFormData.customerId}
//                 onChange={(e) => setOrderFormData(prev => ({ ...prev, customerId: parseInt(e.target.value) }))}
//                 className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
//                 required
//               >
//                 <option value="">Select Customer</option>
//                 {customers.map((customer) => (
//                   <option key={customer.id} value={customer.id}>
//                     {customer.companyName}
//                   </option>
//                 ))}
//               </select>
//             </div>
//           </div>

//           <div>
//             <label htmlFor="deliveryAddress" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//               Delivery Address
//             </label>
//             <textarea
//               id="deliveryAddress"
//               name="deliveryAddress"
//               value={orderFormData.deliveryAddress}
//               onChange={(e) => setOrderFormData(prev => ({ ...prev, deliveryAddress: e.target.value }))}
//               rows={3}
//               className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
//               required
//             />
//           </div>

//           <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
//             <div>
//               <label htmlFor="status" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Status
//               </label>
//               <select
//                 id="status"
//                 name="status"
//                 value={orderFormData.status}
//                 onChange={(e) => setOrderFormData(prev => ({ ...prev, status: e.target.value }))}
//                 className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
//                 required
//               >
//                 <option value="PENDING">Pending</option>
//                 <option value="PROCESSING">Processing</option>
//                 <option value="IN_TRANSIT">In Transit</option>
//                 <option value="DELIVERED">Delivered</option>
//                 <option value="CANCELLED">Cancelled</option>
//               </select>
//             </div>
//             <div>
//               <label htmlFor="deliveryFee" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Delivery Fee
//               </label>
//               <Input
//                 id="deliveryFee"
//                 name="deliveryFee"
//                 type="number"
//                 step="0.01"
//                 value={orderFormData.deliveryFee}
//                 onChange={(e) => setOrderFormData(prev => ({ ...prev, deliveryFee: parseFloat(e.target.value) || 0 }))}
//                 required
//               />
//             </div>
//           </div>

//           <div>
//             <label htmlFor="totalAmount" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//               Total Amount
//             </label>
//             <Input
//               id="totalAmount"
//               name="totalAmount"
//               type="number"
//               step="0.01"
//               value={orderFormData.totalAmount}
//               onChange={(e) => setOrderFormData(prev => ({ ...prev, totalAmount: parseFloat(e.target.value) || 0 }))}
//               required
//             />
//           </div>

//           <div>
//             <label htmlFor="deliveryInstructions" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//               Delivery Instructions
//             </label>
//             <textarea
//               id="deliveryInstructions"
//               name="deliveryInstructions"
//               value={orderFormData.deliveryInstructions}
//               onChange={(e) => setOrderFormData(prev => ({ ...prev, deliveryInstructions: e.target.value }))}
//               rows={3}
//               className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
//             />
//           </div>

//           <div className="flex justify-end space-x-3 pt-4">
//             <Button
//               type="button"
//               variant="outline"
//               onClick={() => {
//                 setIsOrderModalOpen(false);
//                 setEditingOrder(null);
//                 resetOrderForm();
//               }}
//             >
//               Cancel
//             </Button>
//             <Button type="submit">
//               {editingOrder ? 'Update' : 'Create'} Order
//             </Button>
//           </div>
//         </form>
//       </Modal>

//       {/* Driver Modal */}
//       <Modal
//         isOpen={isDriverModalOpen}
//         onClose={() => {
//           setIsDriverModalOpen(false);
//           setEditingDriver(null);
//           resetDriverForm();
//         }}
//         title={editingDriver ? 'Edit Driver' : 'Add New Driver'}
//         size="lg"
//       >
//         <form onSubmit={handleDriverSubmit} className="space-y-4">
//           <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
//             <div>
//               <label htmlFor="driverName" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Driver Name
//               </label>
//               <Input
//                 id="driverName"
//                 name="name"
//                 value={driverFormData.name}
//                 onChange={(e) => setDriverFormData(prev => ({ ...prev, name: e.target.value }))}
//                 required
//               />
//             </div>
//             <div>
//               <label htmlFor="licenseNumber" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 License Number
//               </label>
//               <Input
//                 id="licenseNumber"
//                 name="licenseNumber"
//                 value={driverFormData.licenseNumber}
//                 onChange={(e) => setDriverFormData(prev => ({ ...prev, licenseNumber: e.target.value }))}
//                 required
//               />
//             </div>
//           </div>

//           <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
//             <div>
//               <label htmlFor="phone" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Phone
//               </label>
//               <Input
//                 id="phone"
//                 name="phone"
//                 value={driverFormData.phone}
//                 onChange={(e) => setDriverFormData(prev => ({ ...prev, phone: e.target.value }))}
//                 required
//               />
//             </div>
//             <div>
//               <label htmlFor="email" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Email
//               </label>
//               <Input
//                 id="email"
//                 name="email"
//                 type="email"
//                 value={driverFormData.email}
//                 onChange={(e) => setDriverFormData(prev => ({ ...prev, email: e.target.value }))}
//                 required
//               />
//             </div>
//           </div>

//           <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
//             <div>
//               <label htmlFor="status" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Status
//               </label>
//               <select
//                 id="status"
//                 name="status"
//                 value={driverFormData.status}
//                 onChange={(e) => setDriverFormData(prev => ({ ...prev, status: e.target.value as any }))}
//                 className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
//                 required
//               >
//                 <option value="ACTIVE">Active</option>
//                 <option value="ON_BREAK">On Break</option>
//                 <option value="OFF_DUTY">Off Duty</option>
//                 <option value="ON_LEAVE">On Leave</option>
//               </select>
//             </div>
//             <div>
//               <label htmlFor="rating" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Rating
//               </label>
//               <Input
//                 id="rating"
//                 name="rating"
//                 type="number"
//                 min="1"
//                 max="5"
//                 step="0.1"
//                 value={driverFormData.rating}
//                 onChange={(e) => setDriverFormData(prev => ({ ...prev, rating: parseFloat(e.target.value) || 5 }))}
//                 required
//               />
//             </div>
//             <div>
//               <label htmlFor="vehicleId" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Assigned Vehicle
//               </label>
//               <select
//                 id="vehicleId"
//                 name="vehicleId"
//                 value={driverFormData.vehicleId || ''}
//                 onChange={(e) => setDriverFormData(prev => ({ ...prev, vehicleId: e.target.value ? parseInt(e.target.value) : undefined }))}
//                 className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
//               >
//                 <option value="">No Vehicle</option>
//                 {vehicles.map((vehicle) => (
//                   <option key={vehicle.id} value={vehicle.id}>
//                     {vehicle.registrationNumber} - {vehicle.make} {vehicle.model}
//                   </option>
//                 ))}
//               </select>
//             </div>
//           </div>

//           <div className="flex justify-end space-x-3 pt-4">
//             <Button
//               type="button"
//               variant="outline"
//               onClick={() => {
//                 setIsDriverModalOpen(false);
//                 setEditingDriver(null);
//                 resetDriverForm();
//               }}
//             >
//               Cancel
//             </Button>
//             <Button type="submit">
//               {editingDriver ? 'Update' : 'Create'} Driver
//             </Button>
//           </div>
//         </form>
//       </Modal>

//       {/* Vehicle Modal */}
//       <Modal
//         isOpen={isVehicleModalOpen}
//         onClose={() => {
//           setIsVehicleModalOpen(false);
//           setEditingVehicle(null);
//           resetVehicleForm();
//         }}
//         title={editingVehicle ? 'Edit Vehicle' : 'Add New Vehicle'}
//         size="lg"
//       >
//         <form onSubmit={handleVehicleSubmit} className="space-y-4">
//           <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
//             <div>
//               <label htmlFor="registrationNumber" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Registration Number
//               </label>
//               <Input
//                 id="registrationNumber"
//                 name="registrationNumber"
//                 value={vehicleFormData.registrationNumber}
//                 onChange={(e) => setVehicleFormData(prev => ({ ...prev, registrationNumber: e.target.value }))}
//                 required
//               />
//             </div>
//             <div>
//               <label htmlFor="type" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Vehicle Type
//               </label>
//               <select
//                 id="type"
//                 name="type"
//                 value={vehicleFormData.type}
//                 onChange={(e) => setVehicleFormData(prev => ({ ...prev, type: e.target.value as any }))}
//                 className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
//                 required
//               >
//                 <option value="VAN">Van</option>
//                 <option value="TRUCK">Truck</option>
//                 <option value="CAR">Car</option>
//                 <option value="MOTORBIKE">Motorbike</option>
//               </select>
//             </div>
//           </div>

//           <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
//             <div>
//               <label htmlFor="make" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Make
//               </label>
//               <Input
//                 id="make"
//                 name="make"
//                 value={vehicleFormData.make}
//                 onChange={(e) => setVehicleFormData(prev => ({ ...prev, make: e.target.value }))}
//                 required
//               />
//             </div>
//             <div>
//               <label htmlFor="model" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Model
//               </label>
//               <Input
//                 id="model"
//                 name="model"
//                 value={vehicleFormData.model}
//                 onChange={(e) => setVehicleFormData(prev => ({ ...prev, model: e.target.value }))}
//                 required
//               />
//             </div>
//           </div>

//           <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
//             <div>
//               <label htmlFor="capacity" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Capacity (kg)
//               </label>
//               <Input
//                 id="capacity"
//                 name="capacity"
//                 type="number"
//                 step="0.01"
//                 value={vehicleFormData.capacity}
//                 onChange={(e) => setVehicleFormData(prev => ({ ...prev, capacity: parseFloat(e.target.value) || 0 }))}
//                 required
//               />
//             </div>
//             <div>
//               <label htmlFor="lastMaintenanceDate" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Last Maintenance Date
//               </label>
//               <Input
//                 id="lastMaintenanceDate"
//                 name="lastMaintenanceDate"
//                 type="date"
//                 value={vehicleFormData.lastMaintenanceDate}
//                 onChange={(e) => setVehicleFormData(prev => ({ ...prev, lastMaintenanceDate: e.target.value }))}
//               />
//             </div>
//           </div>

//           <div className="flex justify-end space-x-3 pt-4">
//             <Button
//               type="button"
//               variant="outline"
//               onClick={() => {
//                 setIsVehicleModalOpen(false);
//                 setEditingVehicle(null);
//                 resetVehicleForm();
//               }}
//             >
//               Cancel
//             </Button>
//             <Button type="submit">
//               {editingVehicle ? 'Update' : 'Create'} Vehicle
//             </Button>
//           </div>
//         </form>
//       </Modal>

//       {/* Route Modal */}
//       <Modal
//         isOpen={isRouteModalOpen}
//         onClose={() => {
//           setIsRouteModalOpen(false);
//           setEditingRoute(null);
//           resetRouteForm();
//         }}
//         title={editingRoute ? 'Edit Delivery Route' : 'Create New Delivery Route'}
//         size="lg"
//       >
//         <form onSubmit={handleRouteSubmit} className="space-y-4">
//           <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
//             <div>
//               <label htmlFor="routeName" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Route Name
//               </label>
//               <Input
//                 id="routeName"
//                 name="name"
//                 value={routeFormData.name}
//                 onChange={(e) => setRouteFormData(prev => ({ ...prev, name: e.target.value }))}
//                 required
//               />
//             </div>
//             <div>
//               <label htmlFor="date" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Date
//               </label>
//               <Input
//                 id="date"
//                 name="date"
//                 type="date"
//                 value={routeFormData.date}
//                 onChange={(e) => setRouteFormData(prev => ({ ...prev, date: e.target.value }))}
//                 required
//               />
//             </div>
//           </div>

//           <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
//             <div>
//               <label htmlFor="driverId" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Driver
//               </label>
//               <select
//                 id="driverId"
//                 name="driverId"
//                 value={routeFormData.driverId}
//                 onChange={(e) => setRouteFormData(prev => ({ ...prev, driverId: parseInt(e.target.value) }))}
//                 className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
//                 required
//               >
//                 <option value="">Select Driver</option>
//                 {drivers.map((driver) => (
//                   <option key={driver.id} value={driver.id}>
//                     {driver.name} - {driver.licenseNumber}
//                   </option>
//                 ))}
//               </select>
//             </div>
//             <div>
//               <label htmlFor="routeVehicleId" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Vehicle
//               </label>
//               <select
//                 id="routeVehicleId"
//                 name="vehicleId"
//                 value={routeFormData.vehicleId}
//                 onChange={(e) => setRouteFormData(prev => ({ ...prev, vehicleId: parseInt(e.target.value) }))}
//                 className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
//                 required
//               >
//                 <option value="">Select Vehicle</option>
//                 {vehicles.map((vehicle) => (
//                   <option key={vehicle.id} value={vehicle.id}>
//                     {vehicle.registrationNumber} - {vehicle.make} {vehicle.model}
//                   </option>
//                 ))}
//               </select>
//             </div>
//           </div>

//           <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
//             <div>
//               <label htmlFor="totalDistance" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Total Distance (km)
//               </label>
//               <Input
//                 id="totalDistance"
//                 name="totalDistance"
//                 type="number"
//                 step="0.01"
//                 value={routeFormData.totalDistance}
//                 onChange={(e) => setRouteFormData(prev => ({ ...prev, totalDistance: parseFloat(e.target.value) || 0 }))}
//                 required
//               />
//             </div>
//             <div>
//               <label htmlFor="estimatedDuration" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Estimated Duration (hours)
//               </label>
//               <Input
//                 id="estimatedDuration"
//                 name="estimatedDuration"
//                 type="number"
//                 step="0.1"
//                 value={routeFormData.estimatedDuration}
//                 onChange={(e) => setRouteFormData(prev => ({ ...prev, estimatedDuration: parseFloat(e.target.value) || 0 }))}
//                 required
//               />
//             </div>
//           </div>

//           <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
//             <div>
//               <label htmlFor="totalStops" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Total Stops
//               </label>
//               <Input
//                 id="totalStops"
//                 name="totalStops"
//                 type="number"
//                 value={routeFormData.totalStops}
//                 onChange={(e) => setRouteFormData(prev => ({ ...prev, totalStops: parseInt(e.target.value) || 0 }))}
//                 required
//               />
//             </div>
//             <div>
//               <label htmlFor="totalLoad" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Total Load (kg)
//               </label>
//               <Input
//                 id="totalLoad"
//                 name="totalLoad"
//                 type="number"
//                 step="0.01"
//                 value={routeFormData.totalLoad}
//                 onChange={(e) => setRouteFormData(prev => ({ ...prev, totalLoad: parseFloat(e.target.value) || 0 }))}
//                 required
//               />
//             </div>
//           </div>

//           <div className="flex justify-end space-x-3 pt-4">
//             <Button
//               type="button"
//               variant="outline"
//               onClick={() => {
//                 setIsRouteModalOpen(false);
//                 setEditingRoute(null);
//                 resetRouteForm();
//               }}
//             >
//               Cancel
//             </Button>
//             <Button type="submit">
//               {editingRoute ? 'Update' : 'Create'} Route
//             </Button>
//           </div>
//         </form>
//       </Modal>
//     </div>
//   );
// };


import React, { useState, useEffect } from 'react';
import { Plus, Search, Edit, Truck, Clock, CheckCircle, X, Car, Users, Route, Download } from 'lucide-react';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Card } from '../../components/ui/Card';
import { Table, TableHeader, TableBody, TableRow, TableHead, TableCell } from '../../components/ui/Table';
import { Modal } from '../../components/ui/Modal';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { deliveryService } from '../../services/deliveryService';
import { driverService } from '../../services/driverService';
import { vehicleService } from '../../services/vehicleService';
import { deliveryRouteService } from '../../services/deliveryRouteService';
import { customerService } from '../../services/customerService';
import { DeliveryOrder, Customer, Driver, Vehicle, DeliveryRoute } from '../../types';
import { useAuth } from '../../context/AuthContext';
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';
import { format } from 'date-fns';

type ExtendedVehicle = Vehicle & { driverId?: number };

export const Deliveries: React.FC = () => {
  const { hasRole } = useAuth();
  const [orders, setOrders] = useState<DeliveryOrder[]>([]);
  const [drivers, setDrivers] = useState<Driver[]>([]);
  const [vehicles, setVehicles] = useState<ExtendedVehicle[]>([]);
  const [routes, setRoutes] = useState<DeliveryRoute[]>([]);
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isExporting, setIsExporting] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  // Modal states
  const [isOrderModalOpen, setIsOrderModalOpen] = useState(false);
  const [isDriverModalOpen, setIsDriverModalOpen] = useState(false);
  const [isVehicleModalOpen, setIsVehicleModalOpen] = useState(false);
  const [isRouteModalOpen, setIsRouteModalOpen] = useState(false);

  const [editingOrder, setEditingOrder] = useState<DeliveryOrder | null>(null);
  const [editingDriver, setEditingDriver] = useState<Driver | null>(null);
  const [editingVehicle, setEditingVehicle] = useState<Vehicle | null>(null);
  const [editingRoute, setEditingRoute] = useState<DeliveryRoute | null>(null);

  // Form data states
  const [orderFormData, setOrderFormData] = useState<Partial<DeliveryOrder>>({
    orderNumber: '',
    customerId: 0,
    deliveryAddress: '',
    status: 'PENDING',
    deliveryInstructions: '',
    deliveryFee: 0,
    totalAmount: 0,
  });

  const [driverFormData, setDriverFormData] = useState<Partial<Driver>>({
    name: '',
    licenseNumber: '',
    phone: '',
    email: '',
    status: 'ACTIVE',
    rating: 5,
    vehicleId: undefined,
  });

  const [vehicleFormData, setVehicleFormData] = useState<Partial<Vehicle>>({
    registrationNumber: '',
    make: '',
    model: '',
    type: 'VAN',
    capacity: 0,
    lastMaintenanceDate: '',
  });

  const [routeFormData, setRouteFormData] = useState<Partial<DeliveryRoute>>({
    name: '',
    driverId: 0,
    vehicleId: 0,
    date: new Date().toISOString().split('T')[0],
    totalDistance: 0,
    estimatedDuration: 0,
    totalStops: 0,
    totalLoad: 0,
  });

  useEffect(() => {
    fetchAllData();
  }, [currentPage, searchQuery]);

  const fetchAllData = async () => {
    try {
      setIsLoading(true);
      await Promise.all([
        fetchOrders(),
        fetchDrivers(),
        fetchVehicles(),
        fetchRoutes(),
        fetchCustomers(),
      ]);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const fetchOrders = async () => {
    try {
      let response;
      if (searchQuery) {
        response = await deliveryService.search(searchQuery, currentPage, 10);
      } else {
        response = await deliveryService.getAll(currentPage, 10);
      }
      setOrders(response.content);
      setTotalPages(response.totalPages);
    } catch (error) {
      console.error('Error fetching delivery orders:', error);
    }
  };

  const fetchDrivers = async () => {
    try {
      const data = await driverService.getAll();
      setDrivers(data);
    } catch (error) {
      console.error('Error fetching drivers:', error);
    }
  };

  const fetchVehicles = async () => {
    try {
      const data = await vehicleService.getAll();
      setVehicles(data);
    } catch (error) {
      console.error('Error fetching vehicles:', error);
    }
  };

  const fetchRoutes = async () => {
    try {
      const data = await deliveryRouteService.getAll();
      setRoutes(data);
    } catch (error) {
      console.error('Error fetching routes:', error);
    }
  };

  const fetchCustomers = async () => {
    try {
      const response = await customerService.getAll(0, 100);
      setCustomers(response.content);
    } catch (error) {
      console.error('Error fetching customers:', error);
    }
  };

  const generateDeliveryPDF = () => {
    try {
      setIsExporting(true);
      
      const doc = new jsPDF();
      // Company information
      const companyName = "VINI TRACK";
      const companyAbbr = "(VT)";
      const companyEmail = "vinittrack@gmail.com";
      const companyPhone = "+243 123 456 789";
      const companyAddress = "123 Innovation Avenue, Commune de Kinshasa, Ville de Kinshasa";
      
      // Page dimensions
      const pageWidth = doc.internal.pageSize.getWidth();
      
      // Header section
      doc.setFontSize(18);
      doc.setTextColor(66, 139, 202);
      doc.text(companyName, 14, 20);
          
      doc.setFontSize(14);
      doc.setTextColor(100, 100, 100);
      doc.text(companyAbbr, 14, 28);
      
      // Contact info
      doc.setFontSize(10);
      doc.setTextColor(0, 0, 0);
      doc.text(`Email: ${companyEmail} | Phone: ${companyPhone}`, 14, 36);
          
      // Decorative line
      doc.setLineWidth(1);
      doc.setDrawColor(66, 139, 202);
      doc.line(14, 42, pageWidth - 14, 42);
      
      // Report title
      doc.setFontSize(20);
      doc.setTextColor(0, 0, 0);
      doc.text('DELIVERY MANAGEMENT REPORT', pageWidth / 2, 54, { align: 'center' });
      
      // Report metadata
      doc.setFontSize(12);
      doc.text(`Generated on: ${format(new Date(), 'MMM d, yyyy')}`, 14, 64);
      doc.text(`Total Orders: ${orders.length}`, 14, 72);
      doc.text(`Total Drivers: ${drivers.length}`, 14, 80);
      doc.text(`Total Vehicles: ${vehicles.length}`, 14, 88);
      doc.text(`Total Routes: ${routes.length}`, 14, 96);
      
      // Orders table
      doc.setFontSize(16);
      doc.text('DELIVERY ORDERS', 14, 110);
      
      const orderTableData = orders.map(order => [
        order.orderNumber ?? '',
        order.companyName ?? '',
        order.deliveryAddress ?? '',
        order.status ?? '',
        format(new Date(order.orderDate), 'MMM d, yyyy'),
        `$${(order.totalAmount ?? 0).toFixed(2)}`
      ]);
      
      autoTable(doc, {
        startY: 115,
        head: [['Order #', 'Customer', 'Address', 'Status', 'Date', 'Amount']],
        body: orderTableData,
        styles: {
          fontSize: 9,
          cellPadding: 2,
          valign: 'middle'
        },
        headStyles: {
          fillColor: [66, 139, 202],
          textColor: 255,
          fontSize: 10
        },
        alternateRowStyles: {
          fillColor: [240, 240, 240]
        },
        columnStyles: {
          0: { cellWidth: 20 },
          1: { cellWidth: 30 },
          2: { cellWidth: 40 },
          3: { cellWidth: 20 },
          4: { cellWidth: 20 },
          5: { cellWidth: 15 }
        }
      });
      
      // Drivers table
      const finalY = (doc as any).lastAutoTable.finalY;
      doc.setFontSize(16);
      doc.text('DRIVERS', 14, finalY + 15);
      
      const driverTableData = drivers.map(driver => [
        driver.name ?? '',
        driver.licenseNumber ?? '',
        driver.phone ?? '',
        driver.email ?? '',
        driver.status ?? '',
        driver.rating?.toString() ?? ''
      ]);
      
      autoTable(doc, {
        startY: finalY + 20,
        head: [['Name', 'License', 'Phone', 'Email', 'Status', 'Rating']],
        body: driverTableData,
        styles: {
          fontSize: 9,
          cellPadding: 2,
          valign: 'middle'
        },
        headStyles: {
          fillColor: [66, 139, 202],
          textColor: 255,
          fontSize: 10
        },
        columnStyles: {
          0: { cellWidth: 25 },
          1: { cellWidth: 20 },
          2: { cellWidth: 20 },
          3: { cellWidth: 30 },
          4: { cellWidth: 15 },
          5: { cellWidth: 10 }
        }
      });
      
      // Vehicles table
      const vehiclesFinalY = (doc as any).lastAutoTable.finalY;
      doc.setFontSize(16);
      doc.text('VEHICLES', 14, vehiclesFinalY + 15);
      
      const vehicleTableData = vehicles.map(vehicle => [
        vehicle.registrationNumber ?? '',
        `${vehicle.make} ${vehicle.model}` ?? '',
        vehicle.type ?? '',
        vehicle.capacity?.toString() ?? '',
        vehicle.lastMaintenanceDate ? format(new Date(vehicle.lastMaintenanceDate), 'MMM d, yyyy') : 'N/A'
      ]);
      
      autoTable(doc, {
        startY: vehiclesFinalY + 20,
        head: [['Reg #', 'Make/Model', 'Type', 'Capacity', 'Last Maintenance']],
        body: vehicleTableData,
        styles: {
          fontSize: 9,
          cellPadding: 2,
          valign: 'middle'
        },
        headStyles: {
          fillColor: [66, 139, 202],
          textColor: 255,
          fontSize: 10
        },
        columnStyles: {
          0: { cellWidth: 20 },
          1: { cellWidth: 25 },
          2: { cellWidth: 15 },
          3: { cellWidth: 15 },
          4: { cellWidth: 25 }
        }
      });
      
      // Footer
      const footerY = doc.internal.pageSize.getHeight() - 25;
      doc.setLineWidth(0.5);
      doc.line(14, footerY - 2, pageWidth - 14, footerY - 2);
          
      doc.setFontSize(8);
      doc.setTextColor(80, 80, 80);
      doc.text(companyAddress, pageWidth / 2, footerY + 5, { align: 'center' });
      doc.text(`Page ${doc.getNumberOfPages()}`, pageWidth / 2, footerY + 10, { align: 'center' });
      
      // Save the PDF
      doc.save(`VT-Delivery-Report-${format(new Date(), 'yyyy-MM-dd')}.pdf`);
      
    } catch (error) {
      console.error('Error generating PDF:', error);
      alert('Failed to generate PDF. Please check the console for details.');
    } finally {
      setIsExporting(false);
    }
  };

  // Order handlers
  const handleOrderSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingOrder) {
        await deliveryService.update(editingOrder.id!, orderFormData as DeliveryOrder);
      } else {
        const orderData = {
          ...orderFormData,
          orderNumber: orderFormData.orderNumber || `ORD-${Date.now()}`,
          orderDate: new Date().toISOString(),
        };
        await deliveryService.create(orderData as DeliveryOrder);
      }
      setIsOrderModalOpen(false);
      setEditingOrder(null);
      resetOrderForm();
      fetchOrders();
    } catch (error) {
      console.error('Error saving delivery order:', error);
    }
  };

  // Driver handlers
  const handleDriverSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingDriver) {
        await driverService.update(editingDriver.id!, driverFormData as Driver);
      } else {
        await driverService.create(driverFormData as Driver);
      }
      setIsDriverModalOpen(false);
      setEditingDriver(null);
      resetDriverForm();
      fetchDrivers();
    } catch (error) {
      console.error('Error saving driver:', error);
    }
  };

  // Vehicle handlers
  const handleVehicleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingVehicle) {
        await vehicleService.update(editingVehicle.id!, vehicleFormData as Vehicle);
      } else {
        await vehicleService.create(vehicleFormData as Vehicle);
      }
      setIsVehicleModalOpen(false);
      setEditingVehicle(null);
      resetVehicleForm();
      fetchVehicles();
    } catch (error) {
      console.error('Error saving vehicle:', error);
    }
  };

  // Route handlers
  const handleRouteSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingRoute) {
        await deliveryRouteService.update(editingRoute.id!, routeFormData as DeliveryRoute);
      } else {
        await deliveryRouteService.create(routeFormData as DeliveryRoute);
      }
      setIsRouteModalOpen(false);
      setEditingRoute(null);
      resetRouteForm();
      fetchRoutes();
    } catch (error) {
      console.error('Error saving route:', error);
    }
  };

  const handleStatusUpdate = async (id: number, status: string) => {
    try {
      await deliveryService.updateStatus(id, status);
      fetchOrders();
    } catch (error) {
      console.error('Error updating order status:', error);
    }
  };

  const resetOrderForm = () => {
    setOrderFormData({
      orderNumber: '',
      customerId: 0,
      deliveryAddress: '',
      status: 'PENDING',
      deliveryInstructions: '',
      deliveryFee: 0,
      totalAmount: 0,
    });
  };

  const resetDriverForm = () => {
    setDriverFormData({
      name: '',
      licenseNumber: '',
      phone: '',
      email: '',
      status: 'ACTIVE',
      rating: 5,
      vehicleId: undefined,
    });
  };

  const resetVehicleForm = () => {
    setVehicleFormData({
      registrationNumber: '',
      make: '',
      model: '',
      type: 'VAN',
      capacity: 0,
      lastMaintenanceDate: '',
    });
  };

  const resetRouteForm = () => {
    setRouteFormData({
      name: '',
      driverId: 0,
      vehicleId: 0,
      date: new Date().toISOString().split('T')[0],
      totalDistance: 0,
      estimatedDuration: 0,
      totalStops: 0,
      totalLoad: 0,
    });
  };

  const getStatusIcon = (status: string) => {
    switch (status) {
      case 'PENDING':
        return Clock;
      case 'PROCESSING':
        return Edit;
      case 'IN_TRANSIT':
        return Truck;
      case 'DELIVERED':
        return CheckCircle;
      case 'CANCELLED':
        return X;
      default:
        return Clock;
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'PENDING':
        return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200';
      case 'PROCESSING':
        return 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200';
      case 'IN_TRANSIT':
        return 'bg-purple-100 text-purple-800 dark:bg-purple-900 dark:text-purple-200';
      case 'DELIVERED':
        return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200';
      case 'CANCELLED':
        return 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200';
      default:
        return 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200';
    }
  };

  if (isLoading && currentPage === 0) {
    return (
      <div className="flex items-center justify-center h-64">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Delivery Management</h1>
          <p className="text-gray-600 dark:text-gray-400">Track and manage delivery orders, drivers, vehicles, and routes</p>
        </div>
        <div className="flex space-x-2">
          <Button
            onClick={generateDeliveryPDF}
            icon={Download}
            disabled={isExporting}
          >
            {isExporting ? 'Exporting...' : 'Export PDF'}
          </Button>
          <Button
            onClick={() => {
              setEditingOrder(null);
              resetOrderForm();
              setIsOrderModalOpen(true);
            }}
            icon={Plus}
          >
            Create Order
          </Button>
        </div>
      </div>

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card className="bg-gradient-to-r from-blue-500 to-blue-600 text-white p-6">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-lg font-semibold">Total Drivers</h3>
              <p className="text-3xl font-bold">{drivers.length}</p>
              <p className="text-blue-100 mt-2">Active: {drivers.filter(d => d.status === 'ACTIVE').length}</p>
            </div>
            <div className="bg-blue-400/20 p-4 rounded-full">
              <Users className="h-8 w-8" />
            </div>
          </div>
        </Card>

        <Card className="bg-gradient-to-r from-purple-500 to-purple-600 text-white p-6">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-lg font-semibold">Total Vehicles</h3>
              <p className="text-3xl font-bold">{vehicles.length}</p>
              <p className="text-purple-100 mt-2">Available: {vehicles.filter(v => !v.driverId).length}</p>
            </div>
            <div className="bg-purple-400/20 p-4 rounded-full">
              <Car className="h-8 w-8" />
            </div>
          </div>
        </Card>

        <Card className="bg-gradient-to-r from-green-500 to-green-600 text-white p-6">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-lg font-semibold">Total Routes</h3>
              <p className="text-3xl font-bold">{routes.length}</p>
              <p className="text-green-100 mt-2">Today: {routes.filter(r => new Date(r.date).toDateString() === new Date().toDateString()).length}</p>
            </div>
            <div className="bg-green-400/20 p-4 rounded-full">
              <Route className="h-8 w-8" />
            </div>
          </div>
        </Card>
      </div>

      <div className="flex justify-between items-center">
        <div className="flex-1 max-w-md">
          <Input
            placeholder="Search orders..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            icon={Search}
          />
        </div>
      </div>

      {/* Management Buttons for Admin */}
      {hasRole('ROLE_ADMIN') && (
        <Card className="p-4">
          <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">
            Delivery Management Tools
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <Button
              onClick={() => {
                setEditingDriver(null);
                resetDriverForm();
                setIsDriverModalOpen(true);
              }}
              icon={Users}
              variant="outline"
              className="w-full"
            >
              Manage Drivers
            </Button>
            <Button
              onClick={() => {
                setEditingVehicle(null);
                resetVehicleForm();
                setIsVehicleModalOpen(true);
              }}
              icon={Car}
              variant="outline"
              className="w-full"
            >
              Manage Vehicles
            </Button>
            <Button
              onClick={() => {
                setEditingRoute(null);
                resetRouteForm();
                setIsRouteModalOpen(true);
              }}
              icon={Route}
              variant="outline"
              className="w-full"
            >
              Manage Routes
            </Button>
          </div>
        </Card>
      )}

      <Card className="p-6">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Order Number</TableHead>
              <TableHead>Customer</TableHead>
              <TableHead>Delivery Address</TableHead>
              <TableHead>Status</TableHead>
              <TableHead>Order Date</TableHead>
              <TableHead>Total Amount</TableHead>
              <TableHead>Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {orders.map((order) => {
              const StatusIcon = getStatusIcon(order.status);
              return (
                <TableRow key={order.id}>
                  <TableCell>
                    <div className="font-medium">{order.orderNumber}</div>
                  </TableCell>
                  <TableCell>
                    <div>
                      <div className="font-medium">{order.companyName}</div>
                    </div>
                  </TableCell>
                  <TableCell>
                    <div className="max-w-xs truncate">{order.deliveryAddress}</div>
                  </TableCell>
                  <TableCell>
                    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(order.status)}`}>
                      <StatusIcon className="h-3 w-3 mr-1" />
                      {order.status}
                    </span>
                  </TableCell>
                  <TableCell>
                    {new Date(order.orderDate).toLocaleDateString()}
                  </TableCell>
                  <TableCell>
                    ${order.totalAmount.toFixed(2)}
                  </TableCell>
                  <TableCell>
                    <div className="flex space-x-2">
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => {
                          setEditingOrder(order);
                          setOrderFormData(order);
                          setIsOrderModalOpen(true);
                        }}
                        icon={Edit}
                      >
                        Edit
                      </Button>
                      {order.status === 'PENDING' && (
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleStatusUpdate(order.id!, 'PROCESSING')}
                          className="text-blue-600 hover:text-blue-700"
                        >
                          Process
                        </Button>
                      )}
                      {order.status === 'PROCESSING' && (
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleStatusUpdate(order.id!, 'IN_TRANSIT')}
                          className="text-purple-600 hover:text-purple-700"
                        >
                          Ship
                        </Button>
                      )}
                      {order.status === 'IN_TRANSIT' && (
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleStatusUpdate(order.id!, 'DELIVERED')}
                          className="text-green-600 hover:text-green-700"
                        >
                          Deliver
                        </Button>
                      )}
                    </div>
                  </TableCell>
                </TableRow>
              );
            })}
          </TableBody>
        </Table>

        {totalPages > 1 && (
          <div className="flex justify-between items-center mt-6">
            <Button
              variant="outline"
              onClick={() => setCurrentPage(prev => Math.max(0, prev - 1))}
              disabled={currentPage === 0}
            >
              Previous
            </Button>
            <span className="text-sm text-gray-600 dark:text-gray-400">
              Page {currentPage + 1} of {totalPages}
            </span>
            <Button
              variant="outline"
              onClick={() => setCurrentPage(prev => Math.min(totalPages - 1, prev + 1))}
              disabled={currentPage === totalPages - 1}
            >
              Next
            </Button>
          </div>
        )}
      </Card>

      {/* Order Modal */}
      <Modal
        isOpen={isOrderModalOpen}
        onClose={() => {
          setIsOrderModalOpen(false);
          setEditingOrder(null);
          resetOrderForm();
        }}
        title={editingOrder ? 'Edit Delivery Order' : 'Create New Delivery Order'}
        size="lg"
      >
        <form onSubmit={handleOrderSubmit} className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label htmlFor="orderNumber" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Order Number
              </label>
              <Input
                id="orderNumber"
                name="orderNumber"
                value={orderFormData.orderNumber}
                onChange={(e) => setOrderFormData(prev => ({ ...prev, orderNumber: e.target.value }))}
                placeholder="Auto-generated if empty"
              />
            </div>
            <div>
              <label htmlFor="customerId" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Customer
              </label>
              <select
                id="customerId"
                name="customerId"
                value={orderFormData.customerId}
                onChange={(e) => setOrderFormData(prev => ({ ...prev, customerId: parseInt(e.target.value) }))}
                className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
                required
              >
                <option value="">Select Customer</option>
                {customers.map((customer) => (
                  <option key={customer.id} value={customer.id}>
                    {customer.companyName}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div>
            <label htmlFor="deliveryAddress" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
              Delivery Address
            </label>
            <textarea
              id="deliveryAddress"
              name="deliveryAddress"
              value={orderFormData.deliveryAddress}
              onChange={(e) => setOrderFormData(prev => ({ ...prev, deliveryAddress: e.target.value }))}
              rows={3}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
              required
            />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label htmlFor="status" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Status
              </label>
              <select
                id="status"
                name="status"
                value={orderFormData.status}
                onChange={(e) => setOrderFormData(prev => ({ ...prev, status: e.target.value }))}
                className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
                required
              >
                <option value="PENDING">Pending</option>
                <option value="PROCESSING">Processing</option>
                <option value="IN_TRANSIT">In Transit</option>
                <option value="DELIVERED">Delivered</option>
                <option value="CANCELLED">Cancelled</option>
              </select>
            </div>
            <div>
              <label htmlFor="deliveryFee" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Delivery Fee
              </label>
              <Input
                id="deliveryFee"
                name="deliveryFee"
                type="number"
                step="0.01"
                value={orderFormData.deliveryFee}
                onChange={(e) => setOrderFormData(prev => ({ ...prev, deliveryFee: parseFloat(e.target.value) || 0 }))}
                required
              />
            </div>
          </div>

          <div>
            <label htmlFor="totalAmount" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
              Total Amount
            </label>
            <Input
              id="totalAmount"
              name="totalAmount"
              type="number"
              step="0.01"
              value={orderFormData.totalAmount}
              onChange={(e) => setOrderFormData(prev => ({ ...prev, totalAmount: parseFloat(e.target.value) || 0 }))}
              required
            />
          </div>

          <div>
            <label htmlFor="deliveryInstructions" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
              Delivery Instructions
            </label>
            <textarea
              id="deliveryInstructions"
              name="deliveryInstructions"
              value={orderFormData.deliveryInstructions}
              onChange={(e) => setOrderFormData(prev => ({ ...prev, deliveryInstructions: e.target.value }))}
              rows={3}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
            />
          </div>

          <div className="flex justify-end space-x-3 pt-4">
            <Button
              type="button"
              variant="outline"
              onClick={() => {
                setIsOrderModalOpen(false);
                setEditingOrder(null);
                resetOrderForm();
              }}
            >
              Cancel
            </Button>
            <Button type="submit">
              {editingOrder ? 'Update' : 'Create'} Order
            </Button>
          </div>
        </form>
      </Modal>

      {/* Driver Modal */}
      <Modal
        isOpen={isDriverModalOpen}
        onClose={() => {
          setIsDriverModalOpen(false);
          setEditingDriver(null);
          resetDriverForm();
        }}
        title={editingDriver ? 'Edit Driver' : 'Add New Driver'}
        size="lg"
      >
        <form onSubmit={handleDriverSubmit} className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label htmlFor="driverName" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Driver Name
              </label>
              <Input
                id="driverName"
                name="name"
                value={driverFormData.name}
                onChange={(e) => setDriverFormData(prev => ({ ...prev, name: e.target.value }))}
                required
              />
            </div>
            <div>
              <label htmlFor="licenseNumber" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                License Number
              </label>
              <Input
                id="licenseNumber"
                name="licenseNumber"
                value={driverFormData.licenseNumber}
                onChange={(e) => setDriverFormData(prev => ({ ...prev, licenseNumber: e.target.value }))}
                required
              />
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label htmlFor="phone" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Phone
              </label>
              <Input
                id="phone"
                name="phone"
                value={driverFormData.phone}
                onChange={(e) => setDriverFormData(prev => ({ ...prev, phone: e.target.value }))}
                required
              />
            </div>
            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Email
              </label>
              <Input
                id="email"
                name="email"
                type="email"
                value={driverFormData.email}
                onChange={(e) => setDriverFormData(prev => ({ ...prev, email: e.target.value }))}
                required
              />
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label htmlFor="status" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Status
              </label>
              <select
                id="status"
                name="status"
                value={driverFormData.status}
                onChange={(e) => setDriverFormData(prev => ({ ...prev, status: e.target.value as any }))}
                className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
                required
              >
                <option value="ACTIVE">Active</option>
                <option value="ON_BREAK">On Break</option>
                <option value="OFF_DUTY">Off Duty</option>
                <option value="ON_LEAVE">On Leave</option>
              </select>
            </div>
            <div>
              <label htmlFor="rating" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Rating
              </label>
              <Input
                id="rating"
                name="rating"
                type="number"
                min="1"
                max="5"
                step="0.1"
                value={driverFormData.rating}
                onChange={(e) => setDriverFormData(prev => ({ ...prev, rating: parseFloat(e.target.value) || 5 }))}
                required
              />
            </div>
            <div>
              <label htmlFor="vehicleId" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Assigned Vehicle
              </label>
              <select
                id="vehicleId"
                name="vehicleId"
                value={driverFormData.vehicleId || ''}
                onChange={(e) => setDriverFormData(prev => ({ ...prev, vehicleId: e.target.value ? parseInt(e.target.value) : undefined }))}
                className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
              >
                <option value="">No Vehicle</option>
                {vehicles.map((vehicle) => (
                  <option key={vehicle.id} value={vehicle.id}>
                    {vehicle.registrationNumber} - {vehicle.make} {vehicle.model}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="flex justify-end space-x-3 pt-4">
            <Button
              type="button"
              variant="outline"
              onClick={() => {
                setIsDriverModalOpen(false);
                setEditingDriver(null);
                resetDriverForm();
              }}
            >
              Cancel
            </Button>
            <Button type="submit">
              {editingDriver ? 'Update' : 'Create'} Driver
            </Button>
          </div>
        </form>
      </Modal>

      {/* Vehicle Modal */}
      <Modal
        isOpen={isVehicleModalOpen}
        onClose={() => {
          setIsVehicleModalOpen(false);
          setEditingVehicle(null);
          resetVehicleForm();
        }}
        title={editingVehicle ? 'Edit Vehicle' : 'Add New Vehicle'}
        size="lg"
      >
        <form onSubmit={handleVehicleSubmit} className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label htmlFor="registrationNumber" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Registration Number
              </label>
              <Input
                id="registrationNumber"
                name="registrationNumber"
                value={vehicleFormData.registrationNumber}
                onChange={(e) => setVehicleFormData(prev => ({ ...prev, registrationNumber: e.target.value }))}
                required
              />
            </div>
            <div>
              <label htmlFor="type" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Vehicle Type
              </label>
              <select
                id="type"
                name="type"
                value={vehicleFormData.type}
                onChange={(e) => setVehicleFormData(prev => ({ ...prev, type: e.target.value as any }))}
                className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
                required
              >
                <option value="VAN">Van</option>
                <option value="TRUCK">Truck</option>
                <option value="CAR">Car</option>
                <option value="MOTORBIKE">Motorbike</option>
              </select>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label htmlFor="make" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Make
              </label>
              <Input
                id="make"
                name="make"
                value={vehicleFormData.make}
                onChange={(e) => setVehicleFormData(prev => ({ ...prev, make: e.target.value }))}
                required
              />
            </div>
            <div>
              <label htmlFor="model" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Model
              </label>
              <Input
                id="model"
                name="model"
                value={vehicleFormData.model}
                onChange={(e) => setVehicleFormData(prev => ({ ...prev, model: e.target.value }))}
                required
              />
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label htmlFor="capacity" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Capacity (kg)
              </label>
              <Input
                id="capacity"
                name="capacity"
                type="number"
                step="0.01"
                value={vehicleFormData.capacity}
                onChange={(e) => setVehicleFormData(prev => ({ ...prev, capacity: parseFloat(e.target.value) || 0 }))}
                required
              />
            </div>
            <div>
              <label htmlFor="lastMaintenanceDate" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Last Maintenance Date
              </label>
              <Input
                id="lastMaintenanceDate"
                name="lastMaintenanceDate"
                type="date"
                value={vehicleFormData.lastMaintenanceDate}
                onChange={(e) => setVehicleFormData(prev => ({ ...prev, lastMaintenanceDate: e.target.value }))}
              />
            </div>
          </div>

          <div className="flex justify-end space-x-3 pt-4">
            <Button
              type="button"
              variant="outline"
              onClick={() => {
                setIsVehicleModalOpen(false);
                setEditingVehicle(null);
                resetVehicleForm();
              }}
            >
              Cancel
            </Button>
            <Button type="submit">
              {editingVehicle ? 'Update' : 'Create'} Vehicle
            </Button>
          </div>
        </form>
      </Modal>

      {/* Route Modal */}
      <Modal
        isOpen={isRouteModalOpen}
        onClose={() => {
          setIsRouteModalOpen(false);
          setEditingRoute(null);
          resetRouteForm();
        }}
        title={editingRoute ? 'Edit Delivery Route' : 'Create New Delivery Route'}
        size="lg"
      >
        <form onSubmit={handleRouteSubmit} className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label htmlFor="routeName" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Route Name
              </label>
              <Input
                id="routeName"
                name="name"
                value={routeFormData.name}
                onChange={(e) => setRouteFormData(prev => ({ ...prev, name: e.target.value }))}
                required
              />
            </div>
            <div>
              <label htmlFor="date" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Date
              </label>
              <Input
                id="date"
                name="date"
                type="date"
                value={routeFormData.date}
                onChange={(e) => setRouteFormData(prev => ({ ...prev, date: e.target.value }))}
                required
              />
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label htmlFor="driverId" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Driver
              </label>
              <select
                id="driverId"
                name="driverId"
                value={routeFormData.driverId}
                onChange={(e) => setRouteFormData(prev => ({ ...prev, driverId: parseInt(e.target.value) }))}
                className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
                required
              >
                <option value="">Select Driver</option>
                {drivers.map((driver) => (
                  <option key={driver.id} value={driver.id}>
                    {driver.name} - {driver.licenseNumber}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label htmlFor="routeVehicleId" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Vehicle
              </label>
              <select
                id="routeVehicleId"
                name="vehicleId"
                value={routeFormData.vehicleId}
                onChange={(e) => setRouteFormData(prev => ({ ...prev, vehicleId: parseInt(e.target.value) }))}
                className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
                required
              >
                <option value="">Select Vehicle</option>
                {vehicles.map((vehicle) => (
                  <option key={vehicle.id} value={vehicle.id}>
                    {vehicle.registrationNumber} - {vehicle.make} {vehicle.model}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label htmlFor="totalDistance" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Total Distance (km)
              </label>
              <Input
                id="totalDistance"
                name="totalDistance"
                type="number"
                step="0.01"
                value={routeFormData.totalDistance}
                onChange={(e) => setRouteFormData(prev => ({ ...prev, totalDistance: parseFloat(e.target.value) || 0 }))}
                required
              />
            </div>
            <div>
              <label htmlFor="estimatedDuration" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Estimated Duration (hours)
              </label>
              <Input
                id="estimatedDuration"
                name="estimatedDuration"
                type="number"
                step="0.1"
                value={routeFormData.estimatedDuration}
                onChange={(e) => setRouteFormData(prev => ({ ...prev, estimatedDuration: parseFloat(e.target.value) || 0 }))}
                required
              />
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label htmlFor="totalStops" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Total Stops
              </label>
              <Input
                id="totalStops"
                name="totalStops"
                type="number"
                value={routeFormData.totalStops}
                onChange={(e) => setRouteFormData(prev => ({ ...prev, totalStops: parseInt(e.target.value) || 0 }))}
                required
              />
            </div>
            <div>
              <label htmlFor="totalLoad" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Total Load (kg)
              </label>
              <Input
                id="totalLoad"
                name="totalLoad"
                type="number"
                step="0.01"
                value={routeFormData.totalLoad}
                onChange={(e) => setRouteFormData(prev => ({ ...prev, totalLoad: parseFloat(e.target.value) || 0 }))}
                required
              />
            </div>
          </div>

          <div className="flex justify-end space-x-3 pt-4">
            <Button
              type="button"
              variant="outline"
              onClick={() => {
                setIsRouteModalOpen(false);
                setEditingRoute(null);
                resetRouteForm();
              }}
            >
              Cancel
            </Button>
            <Button type="submit">
              {editingRoute ? 'Update' : 'Create'} Route
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};