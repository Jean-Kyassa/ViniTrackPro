// import React, { useState, useEffect } from 'react';
// import { Plus, Search, Edit, Trash2, Package, AlertTriangle } from 'lucide-react';
// import { Button } from '../../components/ui/Button';
// import { Input } from '../../components/ui/Input';
// import { Card } from '../../components/ui/Card';
// import { Table, TableHeader, TableBody, TableRow, TableHead, TableCell } from '../../components/ui/Table';
// import { Modal } from '../../components/ui/Modal';
// import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
// import { inventoryService } from '../../services/inventoryService';
// import { InventoryItem } from '../../types';

// export const Inventory: React.FC = () => {
//   const [items, setItems] = useState<InventoryItem[]>([]);
//   const [isLoading, setIsLoading] = useState(true);
//   const [isModalOpen, setIsModalOpen] = useState(false);
//   const [editingItem, setEditingItem] = useState<InventoryItem | null>(null);
//   const [searchQuery, setSearchQuery] = useState('');
//   const [currentPage, setCurrentPage] = useState(0);
//   const [totalPages, setTotalPages] = useState(0);

//   const [formData, setFormData] = useState<Partial<InventoryItem>>({
//     itemName: '',
//     sku: '',
//     category: 'RAW_MATERIALS',
//     unitType: 'PIECES',
//     initialQuantity: 0,
//     currentQuantity: 0,
//     minimumStockLevel: 0,
//     manufacturingDate: '',
//     expiryDate: '',
//     costPrice: 0,
//     sellingPrice: 0,
//     description: '',
//     storageLocation: '',
//   });

//   useEffect(() => {
//     fetchItems();
//   }, [currentPage, searchQuery]);

//   const fetchItems = async () => {
//     try {
//       setIsLoading(true);
//       const response = await inventoryService.getAll(currentPage, 10, searchQuery);
//       setItems(response.content);
//       setTotalPages(response.totalPages);
//     } catch (error) {
//       console.error('Error fetching inventory items:', error);
//     } finally {
//       setIsLoading(false);
//     }
//   };

//   const handleSubmit = async (e: React.FormEvent) => {
//     e.preventDefault();
//     try {
//       if (editingItem) {
//         await inventoryService.update(editingItem.id!, formData as InventoryItem);
//       } else {
//         await inventoryService.create(formData as InventoryItem);
//       }
//       setIsModalOpen(false);
//       setEditingItem(null);
//       resetForm();
//       fetchItems();
//     } catch (error) {
//       console.error('Error saving inventory item:', error);
//     }
//   };

//   const handleEdit = (item: InventoryItem) => {
//     setEditingItem(item);
//     setFormData(item);
//     setIsModalOpen(true);
//   };

//   const handleDelete = async (id: number) => {
//     if (window.confirm('Are you sure you want to delete this item?')) {
//       try {
//         await inventoryService.delete(id);
//         fetchItems();
//       } catch (error) {
//         console.error('Error deleting inventory item:', error);
//       }
//     }
//   };

//   const resetForm = () => {
//     setFormData({
//       itemName: '',
//       sku: '',
//       category: 'RAW_MATERIALS',
//       unitType: 'PIECES',
//       initialQuantity: 0,
//       currentQuantity: 0,
//       minimumStockLevel: 0,
//       manufacturingDate: '',
//       expiryDate: '',
//       costPrice: 0,
//       sellingPrice: 0,
//       description: '',
//       storageLocation: '',
//     });
//   };

//   const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
//     const { name, value } = e.target;
//     setFormData(prev => ({
//       ...prev,
//       [name]: name.includes('Quantity') || name.includes('Price') || name.includes('Level') 
//         ? parseFloat(value) || 0 
//         : value
//     }));
//   };

//   const getStockStatus = (item: InventoryItem) => {
//     if (item.currentQuantity <= item.minimumStockLevel) {
//       return { status: 'Low Stock', color: 'text-red-600', icon: AlertTriangle };
//     }
//     return { status: 'In Stock', color: 'text-green-600', icon: Package };
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
//       <div className="flex justify-between items-center">
//         <div>
//           <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Inventory Management</h1>
//           <p className="text-gray-600 dark:text-gray-400">Manage your wine inventory and stock levels</p>
//         </div>
//         <Button
//           onClick={() => {
//             setEditingItem(null);
//             resetForm();
//             setIsModalOpen(true);
//           }}
//           icon={Plus}
//         >
//           Add Item
//         </Button>
//       </div>

//       <Card className="p-6">
//         <div className="flex justify-between items-center mb-6">
//           <div className="flex-1 max-w-md">
//             <Input
//               placeholder="Search items..."
//               value={searchQuery}
//               onChange={(e) => setSearchQuery(e.target.value)}
//               icon={Search}
//             />
//           </div>
//         </div>

//         <Table>
//           <TableHeader>
//             <TableRow>
//               <TableHead>Item Name</TableHead>
//               <TableHead>SKU</TableHead>
//               <TableHead>Category</TableHead>
//               <TableHead>Current Stock</TableHead>
//               <TableHead>Status</TableHead>
//               <TableHead>Cost Price</TableHead>
//               <TableHead>Selling Price</TableHead>
//               <TableHead>Actions</TableHead>
//             </TableRow>
//           </TableHeader>
//           <TableBody>
//             {items.map((item) => {
//               const stockStatus = getStockStatus(item);
//               return (
//                 <TableRow key={item.id}>
//                   <TableCell>
//                     <div>
//                       <div className="font-medium">{item.itemName}</div>
//                       <div className="text-sm text-gray-500">{item.description}</div>
//                     </div>
//                   </TableCell>
//                   <TableCell>{item.sku}</TableCell>
//                   <TableCell>
//                     <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200">
//                       {item.category}
//                     </span>
//                   </TableCell>
//                   <TableCell>
//                     <div>
//                       <div className="font-medium">{item.currentQuantity} {item.unitType}</div>
//                       <div className="text-sm text-gray-500">Min: {item.minimumStockLevel}</div>
//                     </div>
//                   </TableCell>
//                   <TableCell>
//                     <div className={`flex items-center ${stockStatus.color}`}>
//                       <stockStatus.icon className="h-4 w-4 mr-1" />
//                       {stockStatus.status}
//                     </div>
//                   </TableCell>
//                   <TableCell>${item.costPrice.toFixed(2)}</TableCell>
//                   <TableCell>${item.sellingPrice.toFixed(2)}</TableCell>
//                   <TableCell>
//                     <div className="flex space-x-2">
//                       <Button
//                         variant="ghost"
//                         size="sm"
//                         onClick={() => handleEdit(item)}
//                         icon={Edit}
//                       >
//                         Edit
//                       </Button>
//                       <Button
//                         variant="ghost"
//                         size="sm"
//                         onClick={() => handleDelete(item.id!)}
//                         icon={Trash2}
//                         className="text-red-600 hover:text-red-700"
//                       >
//                         Delete
//                       </Button>
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

//       <Modal
//         isOpen={isModalOpen}
//         onClose={() => {
//           setIsModalOpen(false);
//           setEditingItem(null);
//           resetForm();
//         }}
//         title={editingItem ? 'Edit Inventory Item' : 'Add New Inventory Item'}
//         size="lg"
//       >
//         <form onSubmit={handleSubmit} className="space-y-4">
//           <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
//             <Input
//               label="Item Name"
//               name="itemName"
//               value={formData.itemName}
//               onChange={handleInputChange}
//               required
//             />
//             <Input
//               label="SKU"
//               name="sku"
//               value={formData.sku}
//               onChange={handleInputChange}
//               required
//             />
//           </div>

//           <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
//             <div>
//               <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Category
//               </label>
//               <select
//                 name="category"
//                 value={formData.category}
//                 onChange={handleInputChange}
//                 className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
//                 required
//               >
//                 <option value="RAW_MATERIALS">Raw Materials</option>
//                 <option value="WINE">Wine</option>
//                 <option value="SUPPLIES">Supplies</option>
//                 <option value="EQUIPMENT">Equipment</option>
//               </select>
//             </div>
//             <div>
//               <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Unit Type
//               </label>
//               <select
//                 name="unitType"
//                 value={formData.unitType}
//                 onChange={handleInputChange}
//                 className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
//                 required
//               >
//                 <option value="PIECES">Pieces</option>
//                 <option value="BOTTLES">Bottles</option>
//                 <option value="CASES">Cases</option>
//                 <option value="BARRELS">Barrels</option>
//                 <option value="LITERS">Liters</option>
//                 <option value="KILOGRAMS">Kilograms</option>
//               </select>
//             </div>
//           </div>

//           <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
//             <Input
//               label="Initial Quantity"
//               name="initialQuantity"
//               type="number"
//               value={formData.initialQuantity}
//               onChange={handleInputChange}
//               required
//             />
//             <Input
//               label="Current Quantity"
//               name="currentQuantity"
//               type="number"
//               value={formData.currentQuantity}
//               onChange={handleInputChange}
//               required
//             />
//             <Input
//               label="Minimum Stock Level"
//               name="minimumStockLevel"
//               type="number"
//               value={formData.minimumStockLevel}
//               onChange={handleInputChange}
//               required
//             />
//           </div>

//           <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
//             <Input
//               label="Manufacturing Date"
//               name="manufacturingDate"
//               type="date"
//               value={formData.manufacturingDate}
//               onChange={handleInputChange}
//             />
//             <Input
//               label="Expiry Date"
//               name="expiryDate"
//               type="date"
//               value={formData.expiryDate}
//               onChange={handleInputChange}
//             />
//           </div>

//           <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
//             <Input
//               label="Cost Price"
//               name="costPrice"
//               type="number"
//               step="0.01"
//               value={formData.costPrice}
//               onChange={handleInputChange}
//               required
//             />
//             <Input
//               label="Selling Price"
//               name="sellingPrice"
//               type="number"
//               step="0.01"
//               value={formData.sellingPrice}
//               onChange={handleInputChange}
//               required
//             />
//           </div>

//           <Input
//             label="Storage Location"
//             name="storageLocation"
//             value={formData.storageLocation}
//             onChange={handleInputChange}
//           />

//           <div>
//             <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//               Description
//             </label>
//             <textarea
//               name="description"
//               value={formData.description}
//               onChange={(e) => handleInputChange(e as any)}
//               rows={3}
//               className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
//             />
//           </div>

//           <div className="flex justify-end space-x-3 pt-4">
//             <Button
//               type="button"
//               variant="outline"
//               onClick={() => {
//                 setIsModalOpen(false);
//                 setEditingItem(null);
//                 resetForm();
//               }}
//             >
//               Cancel
//             </Button>
//             <Button type="submit">
//               {editingItem ? 'Update' : 'Create'} Item
//             </Button>
//           </div>
//         </form>
//       </Modal>
//     </div>
//   );
// };


import React, { useState, useEffect } from 'react';
import { Plus, Search, Edit, Trash2, Package, AlertTriangle, Download } from 'lucide-react';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Card } from '../../components/ui/Card';
import { Table, TableHeader, TableBody, TableRow, TableHead, TableCell } from '../../components/ui/Table';
import { Modal } from '../../components/ui/Modal';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { inventoryService } from '../../services/inventoryService';
import { InventoryItem } from '../../types';
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';
import { format } from 'date-fns';

export const Inventory: React.FC = () => {
  const [items, setItems] = useState<InventoryItem[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<InventoryItem | null>(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [isExporting, setIsExporting] = useState(false);

  const [formData, setFormData] = useState<Partial<InventoryItem>>({
    itemName: '',
    sku: '',
    category: 'RAW_MATERIALS',
    unitType: 'PIECES',
    initialQuantity: 0,
    currentQuantity: 0,
    minimumStockLevel: 0,
    manufacturingDate: '',
    expiryDate: '',
    costPrice: 0,
    sellingPrice: 0,
    description: '',
    storageLocation: '',
  });

  useEffect(() => {
    fetchItems();
  }, [currentPage, searchQuery]);

  const fetchItems = async () => {
    try {
      setIsLoading(true);
      const response = await inventoryService.getAll(currentPage, 10, searchQuery);
      setItems(response.content);
      setTotalPages(response.totalPages);
    } catch (error) {
      console.error('Error fetching inventory items:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const generateInventoryPDF = () => {
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
      doc.text('INVENTORY REPORT', pageWidth / 2, 54, { align: 'center' });
      
      // Report metadata
      doc.setFontSize(12);
      doc.text(`Generated on: ${format(new Date(), 'MMM d, yyyy')}`, 14, 64);
      doc.text(`Total Items: ${items.length}`, 14, 72);
      
      // Inventory table
      const tableData = items.map(item => [
        item.itemName,
        item.sku,
        item.category,
        `${item.currentQuantity} ${item.unitType}`,
        item.currentQuantity <= item.minimumStockLevel ? 'Low Stock' : 'In Stock',
        `$${item.costPrice.toFixed(2)}`,
        `$${item.sellingPrice.toFixed(2)}`,
        item.storageLocation
      ]);
      
      autoTable(doc, {
        startY: 80,
        head: [['Item Name', 'SKU', 'Category', 'Quantity', 'Status', 'Cost', 'Price', 'Location']],
        body: tableData,
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
          0: { cellWidth: 30 },
          1: { cellWidth: 20 },
          2: { cellWidth: 25 },
          3: { cellWidth: 20 },
          4: { cellWidth: 15 },
          5: { cellWidth: 15 },
          6: { cellWidth: 15 },
          7: { cellWidth: 20 }
        }
      });
      
      // Footer
      const footerY = doc.internal.pageSize.getHeight() - 25;
      doc.setLineWidth(0.5);
      doc.line(14, footerY - 2, pageWidth - 14, footerY - 2);
          
      doc.setFontSize(8);
      doc.setTextColor(80, 80, 80);
      doc.text(companyAddress, pageWidth / 2, footerY + 5, { align: 'center' });
      doc.text(`Page ${doc.internal.getNumberOfPages()}`, pageWidth / 2, footerY + 10, { align: 'center' });
      
      // Save the PDF
      doc.save(`VT-Inventory-Report-${format(new Date(), 'yyyy-MM-dd')}.pdf`);
      
    } catch (error) {
      console.error('Error generating PDF:', error);
      alert('Failed to generate PDF. Please check the console for details.');
    } finally {
      setIsExporting(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingItem) {
        await inventoryService.update(editingItem.id!, formData as InventoryItem);
      } else {
        await inventoryService.create(formData as InventoryItem);
      }
      setIsModalOpen(false);
      setEditingItem(null);
      resetForm();
      fetchItems();
    } catch (error) {
      console.error('Error saving inventory item:', error);
    }
  };

  const handleEdit = (item: InventoryItem) => {
    setEditingItem(item);
    setFormData(item);
    setIsModalOpen(true);
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this item?')) {
      try {
        await inventoryService.delete(id);
        fetchItems();
      } catch (error) {
        console.error('Error deleting inventory item:', error);
      }
    }
  };

  const resetForm = () => {
    setFormData({
      itemName: '',
      sku: '',
      category: 'RAW_MATERIALS',
      unitType: 'PIECES',
      initialQuantity: 0,
      currentQuantity: 0,
      minimumStockLevel: 0,
      manufacturingDate: '',
      expiryDate: '',
      costPrice: 0,
      sellingPrice: 0,
      description: '',
      storageLocation: '',
    });
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name.includes('Quantity') || name.includes('Price') || name.includes('Level') 
        ? parseFloat(value) || 0 
        : value
    }));
  };

  const getStockStatus = (item: InventoryItem) => {
    if (item.currentQuantity <= item.minimumStockLevel) {
      return { status: 'Low Stock', color: 'text-red-600', icon: AlertTriangle };
    }
    return { status: 'In Stock', color: 'text-green-600', icon: Package };
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
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Inventory Management</h1>
          <p className="text-gray-600 dark:text-gray-400">Manage your wine inventory and stock levels</p>
        </div>
        <div className="flex space-x-2">
          <Button
            onClick={generateInventoryPDF}
            icon={Download}
            disabled={isExporting}
          >
            {isExporting ? 'Exporting...' : 'Export PDF'}
          </Button>
          <Button
            onClick={() => {
              setEditingItem(null);
              resetForm();
              setIsModalOpen(true);
            }}
            icon={Plus}
          >
            Add Item
          </Button>
        </div>
      </div>

      <Card className="p-6">
        <div className="flex justify-between items-center mb-6">
          <div className="flex-1 max-w-md">
            <Input
              placeholder="Search items..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              icon={Search}
            />
          </div>
        </div>

        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Item Name</TableHead>
              <TableHead>SKU</TableHead>
              <TableHead>Category</TableHead>
              <TableHead>Current Stock</TableHead>
              <TableHead>Status</TableHead>
              <TableHead>Cost Price</TableHead>
              <TableHead>Selling Price</TableHead>
              <TableHead>Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {items.map((item) => {
              const stockStatus = getStockStatus(item);
              return (
                <TableRow key={item.id}>
                  <TableCell>
                    <div>
                      <div className="font-medium">{item.itemName}</div>
                      <div className="text-sm text-gray-500">{item.description}</div>
                    </div>
                  </TableCell>
                  <TableCell>{item.sku}</TableCell>
                  <TableCell>
                    <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200">
                      {item.category}
                    </span>
                  </TableCell>
                  <TableCell>
                    <div>
                      <div className="font-medium">{item.currentQuantity} {item.unitType}</div>
                      <div className="text-sm text-gray-500">Min: {item.minimumStockLevel}</div>
                    </div>
                  </TableCell>
                  <TableCell>
                    <div className={`flex items-center ${stockStatus.color}`}>
                      <stockStatus.icon className="h-4 w-4 mr-1" />
                      {stockStatus.status}
                    </div>
                  </TableCell>
                  <TableCell>${item.costPrice.toFixed(2)}</TableCell>
                  <TableCell>${item.sellingPrice.toFixed(2)}</TableCell>
                  <TableCell>
                    <div className="flex space-x-2">
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => handleEdit(item)}
                        icon={Edit}
                      >
                        Edit
                      </Button>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => handleDelete(item.id!)}
                        icon={Trash2}
                        className="text-red-600 hover:text-red-700"
                      >
                        Delete
                      </Button>
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

      <Modal
        isOpen={isModalOpen}
        onClose={() => {
          setIsModalOpen(false);
          setEditingItem(null);
          resetForm();
        }}
        title={editingItem ? 'Edit Inventory Item' : 'Add New Inventory Item'}
        size="lg"
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Input
              label="Item Name"
              name="itemName"
              value={formData.itemName}
              onChange={handleInputChange}
              required
            />
            <Input
              label="SKU"
              name="sku"
              value={formData.sku}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Category
              </label>
              <select
                name="category"
                value={formData.category}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
                required
              >
                <option value="RAW_MATERIALS">Raw Materials</option>
                <option value="WINE">Wine</option>
                <option value="SUPPLIES">Supplies</option>
                <option value="EQUIPMENT">Equipment</option>
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Unit Type
              </label>
              <select
                name="unitType"
                value={formData.unitType}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
                required
              >
                <option value="PIECES">Pieces</option>
                <option value="BOTTLES">Bottles</option>
                <option value="CASES">Cases</option>
                <option value="BARRELS">Barrels</option>
                <option value="LITERS">Liters</option>
                <option value="KILOGRAMS">Kilograms</option>
              </select>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <Input
              label="Initial Quantity"
              name="initialQuantity"
              type="number"
              value={formData.initialQuantity}
              onChange={handleInputChange}
              required
            />
            <Input
              label="Current Quantity"
              name="currentQuantity"
              type="number"
              value={formData.currentQuantity}
              onChange={handleInputChange}
              required
            />
            <Input
              label="Minimum Stock Level"
              name="minimumStockLevel"
              type="number"
              value={formData.minimumStockLevel}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Input
              label="Manufacturing Date"
              name="manufacturingDate"
              type="date"
              value={formData.manufacturingDate}
              onChange={handleInputChange}
            />
            <Input
              label="Expiry Date"
              name="expiryDate"
              type="date"
              value={formData.expiryDate}
              onChange={handleInputChange}
            />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Input
              label="Cost Price"
              name="costPrice"
              type="number"
              step="0.01"
              value={formData.costPrice}
              onChange={handleInputChange}
              required
            />
            <Input
              label="Selling Price"
              name="sellingPrice"
              type="number"
              step="0.01"
              value={formData.sellingPrice}
              onChange={handleInputChange}
              required
            />
          </div>

          <Input
            label="Storage Location"
            name="storageLocation"
            value={formData.storageLocation}
            onChange={handleInputChange}
          />

          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
              Description
            </label>
            <textarea
              name="description"
              value={formData.description}
              onChange={(e) => handleInputChange(e as any)}
              rows={3}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
            />
          </div>

          <div className="flex justify-end space-x-3 pt-4">
            <Button
              type="button"
              variant="outline"
              onClick={() => {
                setIsModalOpen(false);
                setEditingItem(null);
                resetForm();
              }}
            >
              Cancel
            </Button>
            <Button type="submit">
              {editingItem ? 'Update' : 'Create'} Item
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};