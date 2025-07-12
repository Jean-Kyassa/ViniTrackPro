import React, { useState, useEffect } from 'react';
import { Plus, Search, Edit, Trash2, Package, Factory, Calendar, CheckSquare, Play } from 'lucide-react';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Card } from '../../components/ui/Card';
import { Table, TableHeader, TableBody, TableRow, TableHead, TableCell } from '../../components/ui/Table';
import { Modal } from '../../components/ui/Modal';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { ExportButton } from '../../components/ui/ExportButton';
import { productService } from '../../services/productService';
import { productionBatchService } from '../../services/productionBatchService';
import { productionLineService } from '../../services/productionLineService';
import { productionScheduleService } from '../../services/productionScheduleService';
import { productionTaskService } from '../../services/productionTaskService';
import { Product, ProductionBatch, ProductionLine, ProductionSchedule, ProductionTask } from '../../types';
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';
import { format } from 'date-fns';

export const Production: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'products' | 'batches' | 'lines' | 'schedules' | 'tasks'>('products');
  
  // Data states
  const [products, setProducts] = useState<Product[]>([]);
  const [batches, setBatches] = useState<ProductionBatch[]>([]);
  const [lines, setLines] = useState<ProductionLine[]>([]);
  const [schedules, setSchedules] = useState<ProductionSchedule[]>([]);
  const [tasks, setTasks] = useState<ProductionTask[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  
  // Modal states
  const [isProductModalOpen, setIsProductModalOpen] = useState(false);
  const [isBatchModalOpen, setIsBatchModalOpen] = useState(false);
  const [isLineModalOpen, setIsLineModalOpen] = useState(false);
  const [isScheduleModalOpen, setIsScheduleModalOpen] = useState(false);
  const [isTaskModalOpen, setIsTaskModalOpen] = useState(false);
  
  // Editing states
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);
  const [editingBatch, setEditingBatch] = useState<ProductionBatch | null>(null);
  const [editingLine, setEditingLine] = useState<ProductionLine | null>(null);
  const [editingSchedule, setEditingSchedule] = useState<ProductionSchedule | null>(null);
  const [editingTask, setEditingTask] = useState<ProductionTask | null>(null);
  
  // Form data states
  const [productFormData, setProductFormData] = useState<Partial<Product>>({
    name: '',
    code: '',
    description: '',
    category: '',
    price: 0,
    unit: '',
  });
  const [batchFormData, setBatchFormData] = useState<Partial<ProductionBatch>>({
    batchNumber: '',
    productId: 0,
    quantity: 0,
    productionDate: new Date().toISOString().split('T')[0],
    expiryDate: '',
  });
  const [lineFormData, setLineFormData] = useState<Partial<ProductionLine>>({
    name: '',
    type: 'BOTTLING',
    efficiency: 100,
    lastMaintenance: '',
    status: 'OPERATIONAL',
  });
  const [scheduleFormData, setScheduleFormData] = useState<Partial<ProductionSchedule>>({
    name: '',
    productionLineId: 0,
    startTime: '',
    endTime: '',
    status: 'DRAFT',
  });
  const [taskFormData, setTaskFormData] = useState<Partial<ProductionTask & { scheduleId?: number; scheduleName?: string }>>({
    name: '',
    description: '',
    productId: 0,
    quantity: 0,
    priority: 1,
    status: 'PENDING',
    estimatedStart: '',
    estimatedEnd: '',
    dueDate: '',
    scheduleId: 0,
    scheduleName: '',
  });

  useEffect(() => {
    fetchAllData();
  }, [activeTab]);

  const fetchAllData = async () => {
    try {
      setIsLoading(true);
      await Promise.all([
        fetchProducts(),
        fetchBatches(),
        fetchLines(),
        fetchSchedules(),
        fetchTasks(),
      ]);
    } catch (error) {
      console.error('Error fetching production data:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const fetchProducts = async () => {
    try {
      const data = await productService.getAll();
      setProducts(data);
    } catch (error) {
      console.error('Error fetching products:', error);
    }
  };

  const fetchBatches = async () => {
    try {
      const data = await productionBatchService.getAll();
      setBatches(data);
    } catch (error) {
      console.error('Error fetching batches:', error);
    }
  };

  const fetchLines = async () => {
    try {
      const data = await productionLineService.getAll();
      setLines(data);
    } catch (error) {
      console.error('Error fetching lines:', error);
    }
  };

  const fetchSchedules = async () => {
    try {
      const data = await productionScheduleService.getAll();
      setSchedules(data);
    } catch (error) {
      console.error('Error fetching schedules:', error);
    }
  };

  const fetchTasks = async () => {
    try {
      const data = await productionTaskService.getAll();
      setTasks(data);
    } catch (error) {
      console.error('Error fetching tasks:', error);
    }
  };

  const generateProductionPDF = (dataType: 'products' | 'batches' | 'lines' | 'schedules' | 'tasks') => {
    try {
      let data: any[] = [];
      let columns: string[] = [];
      let title = '';

      switch (dataType) {
        case 'products':
          data = products;
          columns = ['Name', 'Code', 'Category', 'Description', 'Price', 'Unit'];
          title = 'PRODUCTS REPORT';
          break;
        case 'batches':
          data = batches;
          columns = ['Batch Number', 'Product', 'Quantity', 'Production Date', 'Expiry Date'];
          title = 'PRODUCTION BATCHES REPORT';
          break;
        case 'lines':
          data = lines;
          columns = ['Name', 'Type', 'Efficiency', 'Status', 'Last Maintenance'];
          title = 'PRODUCTION LINES REPORT';
          break;
        case 'schedules':
          data = schedules;
          columns = ['Name', 'Production Line', 'Start Time', 'End Time', 'Status'];
          title = 'PRODUCTION SCHEDULES REPORT';
          break;
        case 'tasks':
          data = tasks;
          columns = ['Name', 'Schedule', 'Product', 'Quantity', 'Priority', 'Status', 'Due Date'];
          title = 'PRODUCTION TASKS REPORT';
          break;
      }

      const doc = new jsPDF();
      const pageWidth = doc.internal.pageSize.width;
      
      // Header section
      doc.setFontSize(18);
      doc.setTextColor(66, 139, 202);
      doc.text("VINI TRACK", 14, 20);
          
      doc.setFontSize(14);
      doc.setTextColor(100, 100, 100);
      doc.text("(VT)", 14, 28);
      
      // Contact info
      doc.setFontSize(10);
      doc.setTextColor(0, 0, 0);
      doc.text(`Email: vinittrack@gmail.com | Phone: +243 123 456 789`, 14, 36);
          
      // Decorative line
      doc.setLineWidth(1);
      doc.setDrawColor(66, 139, 202);
      doc.line(14, 42, pageWidth - 14, 42);
      
      // Report title
      doc.setFontSize(20);
      doc.setTextColor(0, 0, 0);
      doc.text(title, pageWidth / 2, 54, { align: 'center' });
      
      // Report metadata
      doc.setFontSize(12);
      doc.text(`Generated on: ${format(new Date(), 'MMM d, yyyy')}`, 14, 64);
      doc.text(`Total Records: ${data.length}`, 14, 72);
      
      // Prepare table data
      const tableData = data.map(item => {
        switch (dataType) {
          case 'products':
            return [
              item.name,
              item.code,
              item.category,
              item.description || 'N/A',
              `$${item.price?.toFixed(2) || '0.00'}`,
              item.unit
            ];
          case 'batches':
            return [
              item.batchNumber,
              item.productName,
              item.quantity,
              new Date(item.productionDate).toLocaleDateString(),
              new Date(item.expiryDate).toLocaleDateString()
            ];
          case 'lines':
            return [
              item.name,
              item.type,
              `${item.efficiency}%`,
              item.status,
              item.lastMaintenance ? new Date(item.lastMaintenance).toLocaleDateString() : 'Never'
            ];
          case 'schedules':
            return [
              item.name,
              item.productionLineName,
              new Date(item.startTime).toLocaleString(),
              new Date(item.endTime).toLocaleString(),
              item.status
            ];
          case 'tasks':
          default:
            return [
              item.name,
              (item as any).scheduleName || 'No Schedule',
              item.productName,
              item.quantity,
              item.priority >= 4 ? 'High' : item.priority >= 3 ? 'Medium' : 'Low',
              item.status,
              item.dueDate ? new Date(item.dueDate).toLocaleDateString() : 'N/A'
            ];
        }
      });

      // Generate the table
      autoTable(doc, {
        startY: 80,
        head: [columns],
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
          0: { cellWidth: dataType === 'products' ? 30 : 25 },
          1: { cellWidth: 20 },
          2: { cellWidth: dataType === 'tasks' ? 25 : 20 },
          3: { cellWidth: 15 },
          ...(dataType === 'products' && { 4: { cellWidth: 15 } }),
          ...(dataType === 'products' && { 5: { cellWidth: 15 } }),
          ...(dataType === 'tasks' && { 4: { cellWidth: 15 } }),
          ...(dataType === 'tasks' && { 5: { cellWidth: 15 } }),
          ...(dataType === 'tasks' && { 6: { cellWidth: 20 } })
        }
      });
      
      // Footer
      const footerY = doc.internal.pageSize.height - 25;
      doc.setLineWidth(0.5);
      doc.line(14, footerY - 2, pageWidth - 14, footerY - 2);
          
      doc.setFontSize(8);
      doc.setTextColor(80, 80, 80);
      doc.text("123 Innovation Avenue, Commune de Kinshasa, Ville de Kinshasa", pageWidth / 2, footerY + 5, { align: 'center' });
      doc.text(`Page ${doc.getNumberOfPages()}`, pageWidth / 2, footerY + 10, { align: 'center' });
      
      // Save the PDF
      doc.save(`VT-Production-${title.replace(' ', '-')}-${format(new Date(), 'yyyy-MM-dd')}.pdf`);
      
    } catch (error) {
      console.error('Error generating PDF:', error);
      alert('Failed to generate PDF. Please check the console for details.');
    }
  };

  const handleProductSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setIsSubmitting(true);
      if (editingProduct) {
        await productService.update(editingProduct.productId!, productFormData as Product);
      } else {
        await productService.create(productFormData as Product);
      }
      setIsProductModalOpen(false);
      setEditingProduct(null);
      resetProductForm();
      fetchProducts();
    } catch (error) {
      console.error('Error saving product:', error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleBatchSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setIsSubmitting(true);
      if (editingBatch) {
        await productionBatchService.update(editingBatch.id!, batchFormData as ProductionBatch);
      } else {
        await productionBatchService.create(batchFormData as ProductionBatch);
      }
      setIsBatchModalOpen(false);
      setEditingBatch(null);
      resetBatchForm();
      fetchBatches();
    } catch (error) {
      console.error('Error saving batch:', error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleLineSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setIsSubmitting(true);
      if (editingLine) {
        await productionLineService.update(editingLine.id!, lineFormData as ProductionLine);
      } else {
        await productionLineService.create(lineFormData as ProductionLine);
      }
      setIsLineModalOpen(false);
      setEditingLine(null);
      resetLineForm();
      fetchLines();
    } catch (error) {
      console.error('Error saving line:', error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleScheduleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setIsSubmitting(true);
      if (editingSchedule) {
        await productionScheduleService.update(editingSchedule.id!, scheduleFormData as ProductionSchedule);
      } else {
        await productionScheduleService.create(scheduleFormData as ProductionSchedule);
      }
      setIsScheduleModalOpen(false);
      setEditingSchedule(null);
      resetScheduleForm();
      fetchSchedules();
    } catch (error) {
      console.error('Error saving schedule:', error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleTaskSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setIsSubmitting(true);
      const taskData = { ...taskFormData };
      
      if (editingTask) {
        await productionTaskService.update(editingTask.id!, taskData as ProductionTask);
      } else {
        await productionTaskService.create(taskData as ProductionTask);
      }
      setIsTaskModalOpen(false);
      setEditingTask(null);
      resetTaskForm();
      fetchTasks();
    } catch (error) {
      console.error('Error saving task:', error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDeleteProduct = async (id: number) => {
    try {
      await productService.delete(id);
      fetchProducts();
    } catch (error) {
      console.error('Error deleting product:', error);
    }
  };

  const handleDeleteBatch = async (id: number) => {
    try {
      await productionBatchService.delete(id);
      fetchBatches();
    } catch (error) {
      console.error('Error deleting batch:', error);
    }
  };

  const handleDeleteLine = async (id: number) => {
    try {
      await productionLineService.delete(id);
      fetchLines();
    } catch (error) {
      console.error('Error deleting line:', error);
    }
  };

  const handleDeleteSchedule = async (id: number) => {
    try {
      await productionScheduleService.delete(id);
      fetchSchedules();
    } catch (error) {
      console.error('Error deleting schedule:', error);
    }
  };

  const handleDeleteTask = async (id: number) => {
    try {
      await productionTaskService.delete(id);
      fetchTasks();
    } catch (error) {
      console.error('Error deleting task:', error);
    }
  };

  const handleTaskStatus = async (id: number, status: string) => {
    try {
      if (status === 'IN_PROGRESS') {
        await productionTaskService.startTask(id);
      } else {
        await productionTaskService.completeTask(id);
      }
      fetchTasks();
    } catch (error) {
      console.error('Error updating task status:', error);
    }
  };

  const handleScheduleStatus = async (id: number, status: string) => {
    try {
      await productionScheduleService.updateStatus(id, status);
      fetchSchedules();
    } catch (error) {
      console.error('Error updating schedule status:', error);
    }
  };

  const resetProductForm = () => {
    setProductFormData({
      name: '',
      code: '',
      description: '',
      category: '',
      price: 0,
      unit: '',
    });
  };

  const resetBatchForm = () => {
    setBatchFormData({
      batchNumber: '',
      productId: 0,
      quantity: 0,
      productionDate: new Date().toISOString().split('T')[0],
      expiryDate: '',
    });
  };

  const resetLineForm = () => {
    setLineFormData({
      name: '',
      type: 'BOTTLING',
      efficiency: 100,
      lastMaintenance: '',
      status: 'OPERATIONAL',
    });
  };

  const resetScheduleForm = () => {
    setScheduleFormData({
      name: '',
      productionLineId: 0,
      startTime: '',
      endTime: '',
      status: 'DRAFT',
    });
  };

  const resetTaskForm = () => {
    setTaskFormData({
      name: '',
      description: '',
      productId: 0,
      quantity: 0,
      priority: 1,
      status: 'PENDING',
      estimatedStart: '',
      estimatedEnd: '',
      dueDate: '',
      scheduleId: 0,
      scheduleName: '',
    });
  };

  const getStatusColor = (status: string, type: 'line' | 'schedule' | 'task') => {
    if (type === 'line') {
      switch (status) {
        case 'OPERATIONAL':
          return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200';
        case 'MAINTENANCE':
          return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200';
        case 'SHUTDOWN':
          return 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200';
        default:
          return 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200';
      }
    } else if (type === 'schedule') {
      switch (status) {
        case 'DRAFT':
          return 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200';
        case 'SCHEDULED':
          return 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200';
        case 'IN_PROGRESS':
          return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200';
        case 'COMPLETED':
          return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200';
        case 'CANCELLED':
          return 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200';
        default:
          return 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200';
      }
    } else {
      switch (status) {
        case 'PENDING':
          return 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200';
        case 'IN_PROGRESS':
          return 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200';
        case 'COMPLETED':
          return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200';
        case 'CANCELLED':
          return 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200';
        case 'ON_HOLD':
          return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200';
        default:
          return 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200';
      }
    }
  };

  const filteredData = () => {
    switch (activeTab) {
      case 'products':
        return products.filter(item =>
          (item.name || '').toLowerCase().includes(searchQuery.toLowerCase()) ||
          (item.code || '').toLowerCase().includes(searchQuery.toLowerCase())
        );
      case 'batches':
        return batches.filter(item =>
          (item.batchNumber || '').toLowerCase().includes(searchQuery.toLowerCase()) ||
          (item.productName || '').toLowerCase().includes(searchQuery.toLowerCase())
        );
      case 'lines':
        return lines.filter(item =>
          (item.name || '').toLowerCase().includes(searchQuery.toLowerCase())
        );
      case 'schedules':
        return schedules.filter(item =>
          (item.name || '').toLowerCase().includes(searchQuery.toLowerCase())
        );
      case 'tasks':
        return tasks.filter(item =>
          (item.name || '').toLowerCase().includes(searchQuery.toLowerCase())
        );
      default:
        return [];
    }
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Title and Description */}
      <div>
        <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Production Management</h1>
        <p className="text-gray-600 dark:text-gray-400">Manage products, batches, lines, schedules, and tasks</p>
      </div>

      {/* Cards Section */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4">
        {/* Products Card */}
        <Card className="p-4 bg-gradient-to-br from-blue-50 to-blue-100 dark:from-blue-900 dark:to-blue-800">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-sm font-medium text-blue-800 dark:text-blue-200">Products</h3>
              <p className="text-2xl font-bold text-blue-600 dark:text-blue-100">{products.length}</p>
            </div>
            <div className="p-3 rounded-lg bg-blue-100 dark:bg-blue-700">
              <Package className="h-6 w-6 text-blue-600 dark:text-blue-200" />
            </div>
          </div>
          <div className="mt-2 text-xs text-blue-600 dark:text-blue-300">
            {products.length > 0 ? `${products.filter(p => !p.description).length} without description` : 'No products'}
          </div>
        </Card>

        {/* Batches Card */}
        <Card className="p-4 bg-gradient-to-br from-green-50 to-green-100 dark:from-green-900 dark:to-green-800">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-sm font-medium text-green-800 dark:text-green-200">Batches</h3>
              <p className="text-2xl font-bold text-green-600 dark:text-green-100">{batches.length}</p>
            </div>
            <div className="p-3 rounded-lg bg-green-100 dark:bg-green-700">
              <Factory className="h-6 w-6 text-green-600 dark:text-green-200" />
            </div>
          </div>
          <div className="mt-2 text-xs text-green-600 dark:text-green-300">
            {batches.length > 0 ? `${new Set(batches.map(b => b.productId)).size} unique products` : 'No batches'}
          </div>
        </Card>

        {/* Lines Card */}
        <Card className="p-4 bg-gradient-to-br from-purple-50 to-purple-100 dark:from-purple-900 dark:to-purple-800">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-sm font-medium text-purple-800 dark:text-purple-200">Lines</h3>
              <p className="text-2xl font-bold text-purple-600 dark:text-purple-100">{lines.length}</p>
            </div>
            <div className="p-3 rounded-lg bg-purple-100 dark:bg-purple-700">
              <Factory className="h-6 w-6 text-purple-600 dark:text-purple-200" />
            </div>
          </div>
          <div className="mt-2 text-xs text-purple-600 dark:text-purple-300">
            {lines.length > 0 ? `${lines.filter(l => l.status === 'OPERATIONAL').length} operational` : 'No lines'}
          </div>
        </Card>

        {/* Schedules Card */}
        <Card className="p-4 bg-gradient-to-br from-yellow-50 to-yellow-100 dark:from-yellow-900 dark:to-yellow-800">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-sm font-medium text-yellow-800 dark:text-yellow-200">Schedules</h3>
              <p className="text-2xl font-bold text-yellow-600 dark:text-yellow-100">{schedules.length}</p>
            </div>
            <div className="p-3 rounded-lg bg-yellow-100 dark:bg-yellow-700">
              <Calendar className="h-6 w-6 text-yellow-600 dark:text-yellow-200" />
            </div>
          </div>
          <div className="mt-2 text-xs text-yellow-600 dark:text-yellow-300">
            {schedules.length > 0 ? (
              <>
                <span className="inline-block w-2 h-2 rounded-full bg-green-500 mr-1"></span>
                {schedules.filter(s => s.status === 'IN_PROGRESS').length} in progress
              </>
            ) : 'No schedules'}
          </div>
        </Card>

        {/* Tasks Card */}
        <Card className="p-4 bg-gradient-to-br from-red-50 to-red-100 dark:from-red-900 dark:to-red-800">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-sm font-medium text-red-800 dark:text-red-200">Tasks</h3>
              <p className="text-2xl font-bold text-red-600 dark:text-red-100">{tasks.length}</p>
            </div>
            <div className="p-3 rounded-lg bg-red-100 dark:bg-red-700">
              <CheckSquare className="h-6 w-6 text-red-600 dark:text-red-200" />
            </div>
          </div>
          <div className="mt-2 text-xs text-red-600 dark:text-red-300">
            {tasks.length > 0 ? (
              <>
                <span className="inline-block w-2 h-2 rounded-full bg-blue-500 mr-1"></span>
                {tasks.filter(t => t.status === 'PENDING').length} pending
              </>
            ) : 'No tasks'}
          </div>
        </Card>
      </div>

      {/* Export Button */}
      <div className="flex justify-end">
        <Button
          onClick={() => generateProductionPDF(activeTab)}
          disabled={filteredData().length === 0}
          variant="outline"
        >
          Export to PDF
        </Button>
        <Button
          onClick={async () => {
            switch (activeTab) {
              case 'products':
                setEditingProduct(null);
                resetProductForm();
                setIsProductModalOpen(true);
                break;
              case 'batches':
                setEditingBatch(null);
                resetBatchForm();
                setIsBatchModalOpen(true);
                break;
              case 'lines':
                setEditingLine(null);
                resetLineForm();
                setIsLineModalOpen(true);
                break;
              case 'schedules':
                setEditingSchedule(null);
                resetScheduleForm();
                setIsScheduleModalOpen(true);
                break;
              case 'tasks':
                if (schedules.length === 0) {
                  await fetchSchedules();
                }
                setEditingTask(null);
                resetTaskForm();
                setIsTaskModalOpen(true);
                break;
            }
          }}
          icon={Plus}
          className="ml-2"
        >
          Add {activeTab.slice(0, -1).charAt(0).toUpperCase() + activeTab.slice(1, -1)}
        </Button>
      </div>
  
      {/* Tab Navigation */}
      <Card className="p-4">
        <div className="flex space-x-1 bg-gray-100 dark:bg-gray-800 rounded-lg p-1">
          {[
            { key: 'products', label: 'Products', icon: Package },
            { key: 'batches', label: 'Batches', icon: Factory },
            { key: 'lines', label: 'Lines', icon: Factory },
            { key: 'schedules', label: 'Schedules', icon: Calendar },
            { key: 'tasks', label: 'Tasks', icon: CheckSquare },
          ].map(({ key, label, icon: Icon }) => (
            <button
              key={key}
              onClick={() => setActiveTab(key as any)}
              className={`flex items-center space-x-2 px-4 py-2 rounded-md text-sm font-medium transition-colors ${
                activeTab === key
                  ? 'bg-white dark:bg-gray-700 text-gray-900 dark:text-white shadow-sm'
                  : 'text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white'
              }`}
            >
              <Icon className="h-4 w-4" />
              <span>{label}</span>
            </button>
          ))}
        </div>
      </Card>
  
      <Card className="p-6">
        <div className="flex justify-between items-center mb-6">
          <div className="flex-1 max-w-md">
            <Input
              placeholder={`Search ${activeTab}...`}
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              icon={Search}
            />
          </div>
        </div>
  
        {/* Products Table */}
        {activeTab === 'products' && (
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Name</TableHead>
                <TableHead>Code</TableHead>
                <TableHead>Category</TableHead>
                <TableHead>Price</TableHead>
                <TableHead>Unit</TableHead>
                <TableHead>Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredData().map((item) => {
                if (activeTab === 'products' && 'productId' in item) {
                  const product = item as Product;
                  return (
                    <TableRow key={product.productId}>
                      <TableCell>
                        <div>
                          <div className="font-medium">{product.name}</div>
                          <div className="text-sm text-gray-500">{product.description}</div>
                        </div>
                      </TableCell>
                      <TableCell>{product.code}</TableCell>
                      <TableCell>
                        <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200">
                          {product.category}
                        </span>
                      </TableCell>
                      <TableCell>${product.price?.toFixed(2)}</TableCell>
                      <TableCell>{product.unit}</TableCell>
                      <TableCell>
                        <div className="flex space-x-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => {
                              setEditingProduct(product);
                              setProductFormData(product);
                              setIsProductModalOpen(true);
                            }}
                            icon={Edit}
                          >
                            Edit
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleDeleteProduct(product.productId!)}
                            icon={Trash2}
                            className="text-red-600 hover:text-red-700"
                          >
                            Delete
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  );
                }
                return null;
              })}
            </TableBody>
          </Table>
        )}
        
        {/* Batches Table */}
        {activeTab === 'batches' && (
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Batch Number</TableHead>
                <TableHead>Product</TableHead>
                <TableHead>Quantity</TableHead>
                <TableHead>Production Date</TableHead>
                <TableHead>Expiry Date</TableHead>
                <TableHead>Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredData().map((item) => {
                if (activeTab === 'batches' && 'batchNumber' in item) {
                  const batch = item as ProductionBatch;
                  return (
                    <TableRow key={batch.id}>
                      <TableCell>
                        <div className="font-medium">{batch.batchNumber}</div>
                      </TableCell>
                      <TableCell>
                        <div>
                          <div className="font-medium">{batch.productName}</div>
                          <div className="text-sm text-gray-500">{batch.productCode}</div>
                        </div>
                      </TableCell>
                      <TableCell>{batch.quantity}</TableCell>
                      <TableCell>{new Date(batch.productionDate).toLocaleDateString()}</TableCell>
                      <TableCell>{new Date(batch.expiryDate).toLocaleDateString()}</TableCell>
                      <TableCell>
                        <div className="flex space-x-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => {
                              setEditingBatch(batch);
                              setBatchFormData(batch);
                              setIsBatchModalOpen(true);
                            }}
                            icon={Edit}
                          >
                            Edit
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleDeleteBatch(batch.id!)}
                            icon={Trash2}
                            className="text-red-600 hover:text-red-700"
                          >
                            Delete
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  );
                }
                return null;
              })}
            </TableBody>
          </Table>
        )}
  
        {/* Lines Table */}
        {activeTab === 'lines' && (
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Name</TableHead>
                <TableHead>Type</TableHead>
                <TableHead>Efficiency</TableHead>
                <TableHead>Status</TableHead>
                <TableHead>Last Maintenance</TableHead>
                <TableHead>Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredData().map((item) => {
                if (activeTab === 'lines' && 'type' in item) {
                  const line = item as ProductionLine;
                  return (
                    <TableRow key={line.id}>
                      <TableCell>
                        <div className="font-medium">{line.name}</div>
                      </TableCell>
                      <TableCell>
                        <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-purple-100 text-purple-800 dark:bg-purple-900 dark:text-purple-200">
                          {line.type}
                        </span>
                      </TableCell>
                      <TableCell>{line.efficiency}%</TableCell>
                      <TableCell>
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(line.status, 'line')}`}>
                          {line.status}
                        </span>
                      </TableCell>
                      <TableCell>
                        {line.lastMaintenance ? new Date(line.lastMaintenance).toLocaleDateString() : 'Never'}
                      </TableCell>
                      <TableCell>
                        <div className="flex space-x-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => {
                              setEditingLine(line);
                              setLineFormData(line);
                              setIsLineModalOpen(true);
                            }}
                            icon={Edit}
                          >
                            Edit
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleDeleteLine(line.id!)}
                            icon={Trash2}
                            className="text-red-600 hover:text-red-700"
                          >
                            Delete
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  );
                }
                return null;
              })}
            </TableBody>
          </Table>
        )}
        
        {/* Schedules Table */}
        {activeTab === 'schedules' && (
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Name</TableHead>
                <TableHead>Production Line</TableHead>
                <TableHead>Start Time</TableHead>
                <TableHead>End Time</TableHead>
                <TableHead>Status</TableHead>
                <TableHead>Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredData().map((item) => {
                if (activeTab === 'schedules' && 'productionLineName' in item) {
                  const schedule = item as ProductionSchedule;
                  return (
                    <TableRow key={schedule.id}>
                      <TableCell>
                        <div className="font-medium">{schedule.name}</div>
                      </TableCell>
                      <TableCell>{schedule.productionLineName}</TableCell>
                      <TableCell>{new Date(schedule.startTime).toLocaleString()}</TableCell>
                      <TableCell>{new Date(schedule.endTime).toLocaleString()}</TableCell>
                      <TableCell>
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(schedule.status, 'schedule')}`}>
                          {schedule.status}
                        </span>
                      </TableCell>
                      <TableCell>
                        <div className="flex space-x-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => {
                              setEditingSchedule(schedule);
                              setScheduleFormData(schedule);
                              setIsScheduleModalOpen(true);
                            }}
                            icon={Edit}
                          >
                            Edit
                          </Button>
                          {schedule.status === 'DRAFT' && (
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => handleScheduleStatus(schedule.id!, 'SCHEDULED')}
                              icon={Play}
                              className="text-green-600 hover:text-green-700"
                            >
                              Schedule
                            </Button>
                          )}
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleDeleteSchedule(schedule.id!)}
                            icon={Trash2}
                            className="text-red-600 hover:text-red-700"
                          >
                            Delete
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  );
                }
                return null;
              })}
            </TableBody>
          </Table>
        )}
        
        {/* Tasks Table */}
        {activeTab === 'tasks' && (
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Name</TableHead>
                <TableHead>Schedule</TableHead>
                <TableHead>Product</TableHead>
                <TableHead>Quantity</TableHead>
                <TableHead>Priority</TableHead>
                <TableHead>Status</TableHead>
                <TableHead>Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredData().map((item) => {
                if (activeTab === 'tasks' && 'productName' in item) {
                  const task = item as ProductionTask;
                  return (
                    <TableRow key={task.id}>
                      <TableCell>
                        <div>
                          <div className="font-medium">{task.name}</div>
                          <div className="text-sm text-gray-500">{task.description}</div>
                        </div>
                      </TableCell>
                      <TableCell>
                        <div className="text-sm">
                          {(task as any).scheduleName || 'No Schedule'}
                        </div>
                      </TableCell>
                      <TableCell>{task.productName}</TableCell>
                      <TableCell>{task.quantity}</TableCell>
                      <TableCell>
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                          task.priority >= 4 ? 'bg-red-100 text-red-800' :
                          task.priority >= 3 ? 'bg-orange-100 text-orange-800' :
                          'bg-green-100 text-green-800'
                        }`}>
                          {task.priority >= 4 ? 'High' : task.priority >= 3 ? 'Medium' : 'Low'}
                        </span>
                      </TableCell>
                      <TableCell>
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(task.status, 'task')}`}>
                          {task.status}
                        </span>
                      </TableCell>
                      <TableCell>
                        <div className="flex space-x-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={async () => {
                              if (schedules.length === 0) {
                                await fetchSchedules();
                              }
                              setEditingTask(task);
                              setTaskFormData({
                                ...task,
                                scheduleId: (task as any).scheduleId || 0,
                                scheduleName: (task as any).scheduleName || ''
                              });
                              setIsTaskModalOpen(true);
                            }}
                            icon={Edit}
                          >
                            Edit
                          </Button>
                          {task.status === 'PENDING' && (
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => handleTaskStatus(task.id!, 'IN_PROGRESS')}
                              icon={Play}
                              className="text-blue-600 hover:text-blue-700"
                            >
                              Start
                            </Button>
                          )}
                          {task.status === 'IN_PROGRESS' && (
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => handleTaskStatus(task.id!, 'COMPLETED')}
                              icon={CheckSquare}
                              className="text-green-600 hover:text-green-700"
                            >
                              Complete
                            </Button>
                          )}
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleDeleteTask(task.id!)}
                            icon={Trash2}
                            className="text-red-600 hover:text-red-700"
                          >
                            Delete
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  );
                }
                return null;
              })}
            </TableBody>
          </Table>
        )}

        {filteredData().length === 0 && (
          <div className="text-center py-12">
            <div className="text-gray-500 dark:text-gray-400">
              No {activeTab} found. {searchQuery && `Try adjusting your search terms.`}
            </div>
          </div>
        )}
      </Card>
  
      {/* Product Modal */}
      <Modal
        isOpen={isProductModalOpen}
        onClose={() => setIsProductModalOpen(false)}
        title={editingProduct ? 'Edit Product' : 'Add Product'}
      >
        <form onSubmit={handleProductSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <Input
              label="Product Name"
              value={productFormData.name || ''}
              onChange={(e) => setProductFormData({ ...productFormData, name: e.target.value })}
              required
            />
            <Input
              label="Product Code"
              value={productFormData.code || ''}
              onChange={(e) => setProductFormData({ ...productFormData, code: e.target.value })}
              required
            />
          </div>
          <Input
            label="Description"
            value={productFormData.description || ''}
            onChange={(e) => setProductFormData({ ...productFormData, description: e.target.value })}
          />
          <div className="grid grid-cols-3 gap-4">
            <Input
              label="Category"
              value={productFormData.category || ''}
              onChange={(e) => setProductFormData({ ...productFormData, category: e.target.value })}
              required
            />
            <Input
              label="Price"
              type="number"
              step="0.01"
              value={productFormData.price || 0}
              onChange={(e) => setProductFormData({ ...productFormData, price: parseFloat(e.target.value) || 0 })}
              required
            />
            <Input
              label="Unit"
              value={productFormData.unit || ''}
              onChange={(e) => setProductFormData({ ...productFormData, unit: e.target.value })}
              required
            />
          </div>
          <div className="flex justify-end space-x-2 pt-4">
            <Button
              type="button"
              variant="outline"
              onClick={() => setIsProductModalOpen(false)}
            >
              Cancel
            </Button>
            <Button type="submit" disabled={isSubmitting}>
              {isSubmitting ? 'Saving...' : editingProduct ? 'Update' : 'Create'}
            </Button>
          </div>
        </form>
      </Modal>
  
      {/* Batch Modal */}
      <Modal
        isOpen={isBatchModalOpen}
        onClose={() => setIsBatchModalOpen(false)}
        title={editingBatch ? 'Edit Batch' : 'Add Batch'}
      >
        <form onSubmit={handleBatchSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <Input
              label="Batch Number"
              value={batchFormData.batchNumber || ''}
              onChange={(e) => setBatchFormData({ ...batchFormData, batchNumber: e.target.value })}
              required
            />
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Product
              </label>
              <select
                className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
                value={batchFormData.productId || ''}
                onChange={(e) => {
                  const product = products.find(p => p.productId === parseInt(e.target.value));
                  setBatchFormData({
                    ...batchFormData,
                    productId: parseInt(e.target.value),
                    productName: product?.name || '',
                    productCode: product?.code || ''
                  });
                }}
                required
              >
                <option value="">Select Product</option>
                {products.map((product) => (
                  <option key={product.productId} value={product.productId}>
                    {product.name} ({product.code})
                  </option>
                ))}
              </select>
            </div>
          </div>
          <Input
            label="Quantity"
            type="number"
            value={batchFormData.quantity || 0}
            onChange={(e) => setBatchFormData({ ...batchFormData, quantity: parseInt(e.target.value) || 0 })}
            required
          />
          <div className="grid grid-cols-2 gap-4">
            <Input
              label="Production Date"
              type="date"
              value={batchFormData.productionDate || ''}
              onChange={(e) => setBatchFormData({ ...batchFormData, productionDate: e.target.value })}
              required
            />
            <Input
              label="Expiry Date"
              type="date"
              value={batchFormData.expiryDate || ''}
              onChange={(e) => setBatchFormData({ ...batchFormData, expiryDate: e.target.value })}
              required
            />
          </div>
          <div className="flex justify-end space-x-2 pt-4">
            <Button
              type="button"
              variant="outline"
              onClick={() => setIsBatchModalOpen(false)}
            >
              Cancel
            </Button>
            <Button type="submit" disabled={isSubmitting}>
              {isSubmitting ? 'Saving...' : editingBatch ? 'Update' : 'Create'}
            </Button>
          </div>
        </form>
      </Modal>
  
      {/* Line Modal */}
      <Modal
        isOpen={isLineModalOpen}
        onClose={() => setIsLineModalOpen(false)}
        title={editingLine ? 'Edit Production Line' : 'Add Production Line'}
      >
        <form onSubmit={handleLineSubmit} className="space-y-4">
          <Input
            label="Line Name"
            value={lineFormData.name || ''}
            onChange={(e) => setLineFormData({ ...lineFormData, name: e.target.value })}
            required
          />
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Type
              </label>
              <select
                className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
                value={lineFormData.type || ''}
                onChange={(e) => setLineFormData({ ...lineFormData, type: e.target.value as "BOTTLING" | "LABELING" | "PACKAGING" | "MIXING" | "FERMENTATION" })}
                required
              >
                <option value="BOTTLING">Bottling</option>
                <option value="LABELING">Labeling</option>
                <option value="PACKAGING">Packaging</option>
                <option value="MIXING">Mixing</option>
                <option value="FERMENTATION">Fermentation</option>
              </select>
            </div>
            <Input
              label="Efficiency (%)"
              type="number"
              min="0"
              max="100"
              value={lineFormData.efficiency || 0}
              onChange={(e) => setLineFormData({ ...lineFormData, efficiency: parseInt(e.target.value) || 0 })}
              required
            />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Status
              </label>
              <select
                className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
                value={lineFormData.status || ''}
                onChange={(e) => setLineFormData({ ...lineFormData, status: e.target.value as any })}
                required
              >
                <option value="OPERATIONAL">Operational</option>
                <option value="MAINTENANCE">Maintenance</option>
                <option value="SHUTDOWN">Shutdown</option>
              </select>
            </div>
            <Input
              label="Last Maintenance"
              type="date"
              value={lineFormData.lastMaintenance || ''}
              onChange={(e) => setLineFormData({ ...lineFormData, lastMaintenance: e.target.value })}
            />
          </div>
          <div className="flex justify-end space-x-2 pt-4">
            <Button
              type="button"
              variant="outline"
              onClick={() => setIsLineModalOpen(false)}
            >
              Cancel
            </Button>
            <Button type="submit" disabled={isSubmitting}>
              {isSubmitting ? 'Saving...' : editingLine ? 'Update' : 'Create'}
            </Button>
          </div>
        </form>
      </Modal>
  
      {/* Schedule Modal */}
      <Modal
        isOpen={isScheduleModalOpen}
        onClose={() => setIsScheduleModalOpen(false)}
        title={editingSchedule ? 'Edit Schedule' : 'Add Schedule'}
      >
        <form onSubmit={handleScheduleSubmit} className="space-y-4">
          <Input
            label="Schedule Name"
            value={scheduleFormData.name || ''}
            onChange={(e) => setScheduleFormData({ ...scheduleFormData, name: e.target.value })}
            required
          />
          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
              Production Line
            </label>
            <select
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
              value={scheduleFormData.productionLineId || ''}
              onChange={(e) => {
                const line = lines.find(l => l.id === parseInt(e.target.value));
                setScheduleFormData({
                  ...scheduleFormData,
                  productionLineId: parseInt(e.target.value),
                  productionLineName: line?.name || ''
                });
              }}
              required
            >
              <option value="">Select Production Line</option>
              {lines.filter(line => line.status === 'OPERATIONAL').map((line) => (
                <option key={line.id} value={line.id}>
                  {line.name} ({line.type})
                </option>
              ))}
            </select>
          </div>
          <div className="grid grid-cols-2 gap-4">
            <Input
              label="Start Time"
              type="datetime-local"
              value={scheduleFormData.startTime || ''}
              onChange={(e) => setScheduleFormData({ ...scheduleFormData, startTime: e.target.value })}
              required
            />
            <Input
              label="End Time"
              type="datetime-local"
              value={scheduleFormData.endTime || ''}
              onChange={(e) => setScheduleFormData({ ...scheduleFormData, endTime: e.target.value })}
              required
            />
          </div>
          <div className="flex justify-end space-x-2 pt-4">
            <Button
              type="button"
              variant="outline"
              onClick={() => setIsScheduleModalOpen(false)}
            >
              Cancel
            </Button>
            <Button type="submit" disabled={isSubmitting}>
              {isSubmitting ? 'Saving...' : editingSchedule ? 'Update' : 'Create'}
            </Button>
          </div>
        </form>
      </Modal>
  
      {/* Task Modal */}
      <Modal
        isOpen={isTaskModalOpen}
        onClose={() => setIsTaskModalOpen(false)}
        title={editingTask ? 'Edit Task' : 'Add Task'}
      >
        <form onSubmit={handleTaskSubmit} className="space-y-4">
          <Input
            label="Task Name"
            value={taskFormData.name || ''}
            onChange={(e) => setTaskFormData({ ...taskFormData, name: e.target.value })}
            required
          />
          <Input
            label="Description"
            value={taskFormData.description || ''}
            onChange={(e) => setTaskFormData({ ...taskFormData, description: e.target.value })}
          />
          
          {/* Schedule Dropdown */}
          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
              Schedule *
            </label>
            <select
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
              value={taskFormData.scheduleId || ''}
              onChange={(e) => {
                const scheduleId = parseInt(e.target.value);
                const schedule = schedules.find(s => s.id === scheduleId);
                setTaskFormData({
                  ...taskFormData,
                  scheduleId: scheduleId,
                  scheduleName: schedule?.name || ''
                });
              }}
              required
            >
              <option value="">Select Schedule</option>
              {schedules.length > 0 ? (
                schedules
                  .filter(schedule => schedule.status !== 'CANCELLED')
                  .map((schedule) => (
                    <option key={schedule.id} value={schedule.id}>
                      {schedule.name} - {schedule.productionLineName} ({schedule.status})
                    </option>
                  ))
              ) : (
                <option value="" disabled>No schedules available</option>
              )}
            </select>
            {schedules.length === 0 && (
              <p className="text-sm text-gray-500 mt-1">
                No schedules found. Please create a schedule first.
              </p>
            )}
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Product
              </label>
              <select
                className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
                value={taskFormData.productId || ''}
                onChange={(e) => {
                  const product = products.find(p => p.productId === parseInt(e.target.value));
                  setTaskFormData({
                    ...taskFormData,
                    productId: parseInt(e.target.value),
                    productName: product?.name || ''
                  });
                }}
                required
              >
                <option value="">Select Product</option>
                {products.map((product) => (
                  <option key={product.productId} value={product.productId}>
                    {product.name} ({product.code})
                  </option>
                ))}
              </select>
            </div>
            <Input
              label="Quantity"
              type="number"
              value={taskFormData.quantity || 0}
              onChange={(e) => setTaskFormData({ ...taskFormData, quantity: parseInt(e.target.value) || 0 })}
              required
            />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Priority
              </label>
              <select
                className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
                value={taskFormData.priority || 1}
                onChange={(e) => setTaskFormData({ ...taskFormData, priority: parseInt(e.target.value) })}
                required
              >
                <option value={1}>Low Priority</option>
                <option value={2}>Low-Medium Priority</option>
                <option value={3}>Medium Priority</option>
                <option value={4}>High Priority</option>
                <option value={5}>Critical Priority</option>
              </select>
            </div>
            <Input
              label="Due Date"
              type="date"
              value={taskFormData.dueDate || ''}
              onChange={(e) => setTaskFormData({ ...taskFormData, dueDate: e.target.value })}
              required
            />
          </div>
          <div className="flex justify-end space-x-2 pt-4">
            <Button
              type="button"
              variant="outline"
              onClick={() => setIsTaskModalOpen(false)}
            >
              Cancel
            </Button>
            <Button type="submit" disabled={isSubmitting}>
              {isSubmitting ? 'Saving...' : editingTask ? 'Update' : 'Create'}
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default Production;