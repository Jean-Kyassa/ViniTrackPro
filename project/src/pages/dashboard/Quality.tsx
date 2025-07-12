import React, { useState, useEffect } from 'react';
import { Plus, Search, Edit, Trash2, Shield, CheckCircle, X, AlertTriangle } from 'lucide-react';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Card } from '../../components/ui/Card';
import { Table, TableHeader, TableBody, TableRow, TableHead, TableCell } from '../../components/ui/Table';
import { Modal } from '../../components/ui/Modal';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { qualityService } from '../../services/qualityService';
import { QualityCheck } from '../../types';

export const Quality: React.FC = () => {
  const [qualityChecks, setQualityChecks] = useState<QualityCheck[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingCheck, setEditingCheck] = useState<QualityCheck | null>(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [error, setError] = useState<string>('');

  const [formData, setFormData] = useState<Partial<QualityCheck>>({
    name: '',
    type: 'VISUAL',
    batchId: 1,
    checkDate: new Date().toISOString().split('T')[0],
    status: 'PENDING',
    notes: '',
    checkedById: 1,
  });

  useEffect(() => {
    fetchQualityChecks();
  }, []);

  const fetchQualityChecks = async () => {
    try {
      setIsLoading(true);
      setError('');
      const data = await qualityService.getAll();
      setQualityChecks(data);
    } catch (error: any) {
      console.error('Error fetching quality checks:', error);
      setError('Failed to load quality checks. Please check if the backend is running.');
    } finally {
      setIsLoading(false);
    }
  };

  // const handleSubmit = async (e: React.FormEvent) => {
  //   e.preventDefault();
  //   try {
  //     setError('');
      
  //     // Validate required fields
  //     if (!formData.name || !formData.type || !formData.batchId || !formData.checkedById) {
  //       setError('Please fill in all required fields');
  //       return;
  //     }

  //     // Convert date string to ISO format for backend
  //     const submitData = {
  //       ...formData,
  //       checkDate: formData.checkDate ? new Date(formData.checkDate + 'T00:00:00').toISOString() : new Date().toISOString()
  //     };

  //     if (editingCheck) {
  //       await qualityService.update(editingCheck.id!, submitData as Omit<QualityCheck, 'id'>);
  //     } else {
  //       await qualityService.create(submitData as Omit<QualityCheck, 'id'>);
  //     }

  //     setIsModalOpen(false);
  //     setEditingCheck(null);
  //     resetForm();
  //     fetchQualityChecks();
  //   } catch (error: any) {
  //     console.error('Error saving quality check:', error);
      
  //     let errorMessage = 'An error occurred while saving the quality check.';
      
  //     if (error instanceof Error) {
  //       errorMessage = error.message;
  //     } else if (typeof error === 'object' && error !== null && 'message' in error) {
  //       errorMessage = String((error as any).message);
  //     }
      
  //     setError(errorMessage);
  //   }
  // };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setError('');
      
      // Validate required fields
      if (!formData.name?.trim()) {
        setError('Check name is required');
        return;
      }
      if (!formData.type) {
        setError('Check type is required');
        return;
      }
      if (!formData.batchId || formData.batchId <= 0) {
        setError('Valid batch ID is required');
        return;
      }
      if (!formData.checkedById || formData.checkedById <= 0) {
        setError('Valid user ID is required');
        return;
      }
  
      // Prepare submit data with proper formatting
      const submitData = {
        name: formData.name.trim(),
        type: formData.type,
        batchId: Number(formData.batchId),
        checkDate: formData.checkDate || new Date().toISOString().split('T')[0],
        status: formData.status || 'PENDING',
        notes: formData.notes?.trim() || '',
        checkedById: Number(formData.checkedById)
      };
  
      console.log('Submitting data:', submitData);
  
      if (editingCheck) {
        await qualityService.update(editingCheck.id!, submitData);
      } else {
        await qualityService.create(submitData as Omit<QualityCheck, 'id'>);
      }
      
      setIsModalOpen(false);
      setEditingCheck(null);
      resetForm();
      fetchQualityChecks();
    } catch (error: any) {
      console.error('Error saving quality check:', error);
      
      let errorMessage = 'An error occurred while saving the quality check.';
      
      if (error instanceof Error) {
        errorMessage = error.message;
      } else if (typeof error === 'object' && error !== null && 'message' in error) {
        errorMessage = String((error as any).message);
      }
      
      setError(errorMessage);
    }
  };  

  const handleEdit = (check: QualityCheck) => {
    setEditingCheck(check);
    setFormData({
      ...check,
      checkDate: check.checkDate.split('T')[0],
    });
    setIsModalOpen(true);
    setError('');
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this quality check?')) {
      try {
        await qualityService.delete(id);
        fetchQualityChecks();
      } catch (error) {
        console.error('Error deleting quality check:', error);
        setError('Failed to delete quality check');
      }
    }
  };

  const handleStatusUpdate = async (id: number, status: string) => {
    try {
      await qualityService.updateStatus(id, status);
      fetchQualityChecks();
    } catch (error) {
      console.error('Error updating quality check status:', error);
      setError('Failed to update status');
    }
  };

  const resetForm = () => {
    setFormData({
      name: '',
      type: 'VISUAL',
      batchId: 1,
      checkDate: new Date().toISOString().split('T')[0],
      status: 'PENDING',
      notes: '',
      checkedById: 1,
    });
    setError('');
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === 'batchId' || name === 'checkedById'
        ? parseInt(value) || 0
        : value
    }));
  };

  const getStatusIcon = (status: string) => {
    switch (status) {
      case 'PASSED':
        return CheckCircle;
      case 'FAILED':
        return X;
      case 'PENDING':
        return AlertTriangle;
      default:
        return Shield;
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'PASSED':
        return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200';
      case 'FAILED':
        return 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200';
      case 'PENDING':
        return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200';
      default:
        return 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200';
    }
  };

  const filteredChecks = qualityChecks.filter(check =>
    check.name?.toLowerCase().includes(searchQuery.toLowerCase()) ||
    check.type?.toLowerCase().includes(searchQuery.toLowerCase()) ||
    check.status?.toLowerCase().includes(searchQuery.toLowerCase())
  );

  if (isLoading) {
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
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Quality Control</h1>
          <p className="text-gray-600 dark:text-gray-400">Manage quality checks and testing procedures</p>
        </div>
        <Button
          onClick={() => {
            setEditingCheck(null);
            resetForm();
            setIsModalOpen(true);
          }}
          icon={Plus}
        >
          Add Quality Check
        </Button>
      </div>

      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
          {error}
        </div>
      )}

      <Card className="p-6">
        <div className="flex justify-between items-center mb-6">
          <div className="flex-1 max-w-md">
            <Input
              placeholder="Search quality checks..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              icon={Search}
            />
          </div>
        </div>

        {qualityChecks.length === 0 ? (
          <div className="text-center py-12">
            <Shield className="h-12 w-12 text-gray-400 mx-auto mb-4" />
            <p className="text-gray-500 dark:text-gray-400">No quality checks found</p>
            <p className="text-sm text-gray-400 dark:text-gray-500">
              {error ? 'Please check your backend connection' : 'Create your first quality check to get started'}
            </p>
          </div>
        ) : (
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Check Name</TableHead>
                <TableHead>Type</TableHead>
                <TableHead>Batch ID</TableHead>
                <TableHead>Check Date</TableHead>
                <TableHead>Status</TableHead>
                <TableHead>Checked By</TableHead>
                <TableHead>Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredChecks.map((check) => {
                const StatusIcon = getStatusIcon(check.status);
                return (
                  <TableRow key={check.id}>
                    <TableCell>
                      <div>
                        <div className="font-medium">{check.name}</div>
                        {check.notes && (
                          <div className="text-sm text-gray-500 truncate max-w-xs">
                            {check.notes}
                          </div>
                        )}
                      </div>
                    </TableCell>
                    <TableCell>
                      <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200">
                        {check.type}
                      </span>
                    </TableCell>
                    <TableCell>
                      <div className="font-medium">#{check.batchId}</div>
                      {check.batchNumber && (
                        <div className="text-sm text-gray-500">{check.batchNumber}</div>
                      )}
                    </TableCell>
                    <TableCell>
                      {new Date(check.checkDate).toLocaleDateString()}
                    </TableCell>
                    <TableCell>
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(check.status)}`}>
                        <StatusIcon className="h-3 w-3 mr-1" />
                        {check.status}
                      </span>
                    </TableCell>
                    <TableCell>
                      <div>
                        <div className="font-medium">{check.checkedByName || `User ${check.checkedById}`}</div>
                      </div>
                    </TableCell>
                    <TableCell>
                      <div className="flex space-x-2">
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleEdit(check)}
                          icon={Edit}
                        >
                          Edit
                        </Button>
                        {check.status === 'PENDING' && (
                          <>
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => handleStatusUpdate(check.id!, 'PASSED')}
                              className="text-green-600 hover:text-green-700"
                            >
                              Pass
                            </Button>
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => handleStatusUpdate(check.id!, 'FAILED')}
                              className="text-red-600 hover:text-red-700"
                            >
                              Fail
                            </Button>
                          </>
                        )}
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleDelete(check.id!)}
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
        )}
      </Card>

      <Modal
        isOpen={isModalOpen}
        onClose={() => {
          setIsModalOpen(false);
          setEditingCheck(null);
          resetForm();
        }}
        title={editingCheck ? 'Edit Quality Check' : 'Add New Quality Check'}
        size="lg"
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          {error && (
            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
              {error}
            </div>
          )}

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Input
              label="Check Name *"
              name="name"
              value={formData.name}
              onChange={handleInputChange}
              required
            />
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Type *
              </label>
              <select
                name="type"
                value={formData.type}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
                required
              >
                <option value="VISUAL">Visual</option>
                <option value="TASTE">Taste</option>
                <option value="CHEMICAL">Chemical</option>
                <option value="MICROBIOLOGICAL">Microbiological</option>
                <option value="PHYSICAL">Physical</option>
              </select>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Input
              label="Batch ID *"
              name="batchId"
              type="number"
              value={formData.batchId}
              onChange={handleInputChange}
              required
              min="1"
            />
            <Input
              label="Check Date *"
              name="checkDate"
              type="date"
              value={formData.checkDate}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Status *
              </label>
              <select
                name="status"
                value={formData.status}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
                required
              >
                <option value="PENDING">Pending</option>
                <option value="PASSED">Passed</option>
                <option value="FAILED">Failed</option>
              </select>
            </div>
            <Input
              label="Checked By (User ID) *"
              name="checkedById"
              type="number"
              value={formData.checkedById}
              onChange={handleInputChange}
              required
              min="1"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
              Notes
            </label>
            <textarea
              name="notes"
              value={formData.notes}
              onChange={handleInputChange}
              rows={4}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
              placeholder="Enter detailed notes about the quality check..."
              maxLength={2000}
            />
          </div>

          <div className="flex justify-end space-x-3 pt-4">
            <Button
              type="button"
              variant="outline"
              onClick={() => {
                setIsModalOpen(false);
                setEditingCheck(null);
                resetForm();
              }}
            >
              Cancel
            </Button>
            <Button type="submit">
              {editingCheck ? 'Update' : 'Create'} Quality Check
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};