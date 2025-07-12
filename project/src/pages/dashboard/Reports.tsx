// import React, { useState, useEffect } from 'react';
// import { Plus, Search, Edit, Trash2, FileText, Download, Eye } from 'lucide-react';
// import { Button } from '../../components/ui/Button';
// import { Input } from '../../components/ui/Input';
// import { Card } from '../../components/ui/Card';
// import { Table, TableHeader, TableBody, TableRow, TableHead, TableCell } from '../../components/ui/Table';
// import { Modal } from '../../components/ui/Modal';
// import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
// import { reportService } from '../../services/reportService';
// import { Report } from '../../types';

// export const Reports: React.FC = () => {
//   const [reports, setReports] = useState<Report[]>([]);
//   const [isLoading, setIsLoading] = useState(true);
//   const [isModalOpen, setIsModalOpen] = useState(false);
//   const [editingReport, setEditingReport] = useState<Report | null>(null);
//   const [searchQuery, setSearchQuery] = useState('');

//   const [formData, setFormData] = useState<Partial<Report>>({
//     title: '',
//     type: 'INVENTORY',
//     reportDate: new Date().toISOString().split('T')[0],
//     periodStart: '',
//     periodEnd: '',
//     summary: '',
//     findings: '',
//     recommendations: '',
//   });

//   useEffect(() => {
//     fetchReports();
//   }, []);

//   const fetchReports = async () => {
//     try {
//       setIsLoading(true);
//       const data = await reportService.getAll();
//       setReports(data);
//     } catch (error) {
//       console.error('Error fetching reports:', error);
//     } finally {
//       setIsLoading(false);
//     }
//   };

//   const handleSubmit = async (e: React.FormEvent) => {
//     e.preventDefault();
//     try {
//       if (editingReport) {
//         await reportService.update(editingReport.id!, formData as Omit<Report, 'id'>);
//       } else {
//         await reportService.create(formData as Omit<Report, 'id'>);
//       }
//       setIsModalOpen(false);
//       setEditingReport(null);
//       resetForm();
//       fetchReports();
//     } catch (error) {
//       console.error('Error saving report:', error);
//     }
//   };

//   const handleEdit = (report: Report) => {
//     setEditingReport(report);
//     setFormData({
//       ...report,
//       reportDate: report.reportDate.split('T')[0],
//       periodStart: report.periodStart.split('T')[0],
//       periodEnd: report.periodEnd.split('T')[0],
//     });
//     setIsModalOpen(true);
//   };

//   const handleDelete = async (id: number) => {
//     if (window.confirm('Are you sure you want to delete this report?')) {
//       try {
//         await reportService.delete(id);
//         fetchReports();
//       } catch (error) {
//         console.error('Error deleting report:', error);
//       }
//     }
//   };

//   const resetForm = () => {
//     setFormData({
//       title: '',
//       type: 'INVENTORY',
//       reportDate: new Date().toISOString().split('T')[0],
//       periodStart: '',
//       periodEnd: '',
//       summary: '',
//       findings: '',
//       recommendations: '',
//     });
//   };

//   const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
//     const { name, value } = e.target;
//     setFormData(prev => ({
//       ...prev,
//       [name]: value
//     }));
//   };

//   const getReportTypeColor = (type: string) => {
//     switch (type) {
//       case 'INVENTORY':
//         return 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200';
//       case 'SALES':
//         return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200';
//       case 'PRODUCTION':
//         return 'bg-purple-100 text-purple-800 dark:bg-purple-900 dark:text-purple-200';
//       case 'QUALITY':
//         return 'bg-orange-100 text-orange-800 dark:bg-orange-900 dark:text-orange-200';
//       case 'LOGISTICS':
//         return 'bg-indigo-100 text-indigo-800 dark:bg-indigo-900 dark:text-indigo-200';
//       case 'FINANCIAL':
//         return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200';
//       default:
//         return 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200';
//     }
//   };

//   const filteredReports = reports.filter(report =>
//     report.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
//     report.type.toLowerCase().includes(searchQuery.toLowerCase()) ||
//     report.summary.toLowerCase().includes(searchQuery.toLowerCase())
//   );

//   if (isLoading) {
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
//           <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Reports & Analytics</h1>
//           <p className="text-gray-600 dark:text-gray-400">Generate and manage business reports</p>
//         </div>
//         <Button
//           onClick={() => {
//             setEditingReport(null);
//             resetForm();
//             setIsModalOpen(true);
//           }}
//           icon={Plus}
//         >
//           Create Report
//         </Button>
//       </div>

//       <Card className="p-6">
//         <div className="flex justify-between items-center mb-6">
//           <div className="flex-1 max-w-md">
//             <Input
//               placeholder="Search reports..."
//               value={searchQuery}
//               onChange={(e) => setSearchQuery(e.target.value)}
//               icon={Search}
//             />
//           </div>
//         </div>

//         <Table>
//           <TableHeader>
//             <TableRow>
//               <TableHead>Title</TableHead>
//               <TableHead>Type</TableHead>
//               <TableHead>Report Date</TableHead>
//               <TableHead>Period</TableHead>
//               <TableHead>Generated By</TableHead>
//               <TableHead>Actions</TableHead>
//             </TableRow>
//           </TableHeader>
//           <TableBody>
//             {filteredReports.map((report) => (
//               <TableRow key={report.id}>
//                 <TableCell>
//                   <div className="flex items-center">
//                     <FileText className="h-4 w-4 text-gray-400 mr-2" />
//                     <div>
//                       <div className="font-medium">{report.title}</div>
//                       <div className="text-sm text-gray-500 truncate max-w-xs">
//                         {report.summary}
//                       </div>
//                     </div>
//                   </div>
//                 </TableCell>
//                 <TableCell>
//                   <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getReportTypeColor(report.type)}`}>
//                     {report.type}
//                   </span>
//                 </TableCell>
//                 <TableCell>
//                   {new Date(report.reportDate).toLocaleDateString()}
//                 </TableCell>
//                 <TableCell>
//                   <div className="text-sm">
//                     <div>{new Date(report.periodStart).toLocaleDateString()}</div>
//                     <div className="text-gray-500">to {new Date(report.periodEnd).toLocaleDateString()}</div>
//                   </div>
//                 </TableCell>
//                 <TableCell>
//                   <div>
//                     <div className="font-medium">{report.generatedByUsername || 'System'}</div>
//                     <div className="text-sm text-gray-500">
//                       {report.createdAt && new Date(report.createdAt).toLocaleDateString()}
//                     </div>
//                   </div>
//                 </TableCell>
//                 <TableCell>
//                   <div className="flex space-x-2">
//                     <Button
//                       variant="ghost"
//                       size="sm"
//                       onClick={() => handleEdit(report)}
//                       icon={Eye}
//                     >
//                       View
//                     </Button>
//                     <Button
//                       variant="ghost"
//                       size="sm"
//                       onClick={() => handleEdit(report)}
//                       icon={Edit}
//                     >
//                       Edit
//                     </Button>
//                     <Button
//                       variant="ghost"
//                       size="sm"
//                       icon={Download}
//                       className="text-blue-600 hover:text-blue-700"
//                     >
//                       Export
//                     </Button>
//                     <Button
//                       variant="ghost"
//                       size="sm"
//                       onClick={() => handleDelete(report.id!)}
//                       icon={Trash2}
//                       className="text-red-600 hover:text-red-700"
//                     >
//                       Delete
//                     </Button>
//                   </div>
//                 </TableCell>
//               </TableRow>
//             ))}
//           </TableBody>
//         </Table>
//       </Card>

//       <Modal
//         isOpen={isModalOpen}
//         onClose={() => {
//           setIsModalOpen(false);
//           setEditingReport(null);
//           resetForm();
//         }}
//         title={editingReport ? 'Edit Report' : 'Create New Report'}
//         size="xl"
//       >
//         <form onSubmit={handleSubmit} className="space-y-4">
//           <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
//             <Input
//               label="Report Title"
//               name="title"
//               value={formData.title}
//               onChange={handleInputChange}
//               required
//             />
//             <div>
//               <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//                 Report Type
//               </label>
//               <select
//                 name="type"
//                 value={formData.type}
//                 onChange={handleInputChange}
//                 className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
//                 required
//               >
//                 <option value="INVENTORY">Inventory</option>
//                 <option value="SALES">Sales</option>
//                 <option value="PRODUCTION">Production</option>
//                 <option value="QUALITY">Quality</option>
//                 <option value="LOGISTICS">Logistics</option>
//                 <option value="FINANCIAL">Financial</option>
//               </select>
//             </div>
//           </div>

//           <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
//             <Input
//               label="Report Date"
//               name="reportDate"
//               type="date"
//               value={formData.reportDate}
//               onChange={handleInputChange}
//               required
//             />
//             <Input
//               label="Period Start"
//               name="periodStart"
//               type="date"
//               value={formData.periodStart}
//               onChange={handleInputChange}
//               required
//             />
//             <Input
//               label="Period End"
//               name="periodEnd"
//               type="date"
//               value={formData.periodEnd}
//               onChange={handleInputChange}
//               required
//             />
//           </div>

//           <div>
//             <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//               Summary
//             </label>
//             <textarea
//               name="summary"
//               value={formData.summary}
//               onChange={handleInputChange}
//               rows={3}
//               className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
//               placeholder="Brief summary of the report..."
//               required
//             />
//           </div>

//           <div>
//             <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//               Findings
//             </label>
//             <textarea
//               name="findings"
//               value={formData.findings}
//               onChange={handleInputChange}
//               rows={4}
//               className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
//               placeholder="Detailed findings and observations..."
//               required
//             />
//           </div>

//           <div>
//             <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
//               Recommendations
//             </label>
//             <textarea
//               name="recommendations"
//               value={formData.recommendations}
//               onChange={handleInputChange}
//               rows={4}
//               className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
//               placeholder="Recommendations and action items..."
//               required
//             />
//           </div>

//           <div className="flex justify-end space-x-3 pt-4">
//             <Button
//               type="button"
//               variant="outline"
//               onClick={() => {
//                 setIsModalOpen(false);
//                 setEditingReport(null);
//                 resetForm();
//               }}
//             >
//               Cancel
//             </Button>
//             <Button type="submit">
//               {editingReport ? 'Update' : 'Generate'} Report
//             </Button>
//           </div>
//         </form>
//       </Modal>
//     </div>
//   );
// };

import React, { useState, useEffect } from 'react';
import { Plus, Search, Edit, Trash2, FileText, Download, Eye } from 'lucide-react';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Card } from '../../components/ui/Card';
import { Table, TableHeader, TableBody, TableRow, TableHead, TableCell } from '../../components/ui/Table';
import { Modal } from '../../components/ui/Modal';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { reportService } from '../../services/reportService';
import { Report } from '../../types';
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';
import { format } from 'date-fns';

export const Reports: React.FC = () => {
  const [reports, setReports] = useState<Report[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingReport, setEditingReport] = useState<Report | null>(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [isExporting, setIsExporting] = useState(false);
  const [formData, setFormData] = useState<Partial<Report>>({
    title: '',
    type: 'INVENTORY',
    reportDate: new Date().toISOString().split('T')[0],
    periodStart: '',
    periodEnd: '',
    summary: '',
    findings: '',
    recommendations: '',
  });

  useEffect(() => {
    fetchReports();
  }, []);

  const fetchReports = async () => {
    try {
      setIsLoading(true);
      const data = await reportService.getAll();
      setReports(data);
    } catch (error) {
      console.error('Error fetching reports:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const generateReportPDF = (report: Report) => {
    try {
      console.log('Starting PDF generation for report:', report.title);
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
      const pageHeight = doc.internal.pageSize.getHeight();
      
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
      doc.text(report.title.toUpperCase(), 14, 54);
      
      // Report metadata
      doc.setFontSize(12);
      doc.text(`Report Date: ${format(new Date(report.reportDate), 'MMM d, yyyy')}`, 14, 64);
      doc.text(`Period: ${format(new Date(report.periodStart), 'MMM d, yyyy')} - ${format(new Date(report.periodEnd), 'MMM d, yyyy')}`, 14, 72);
      doc.text(`Type: ${report.type}`, 14, 80);
      doc.text(`Generated By: ${report.generatedByUsername || 'System'}`, 14, 88);
      
      // Summary section
      doc.setFontSize(14);
      doc.text('Summary', 14, 100);
      doc.setFontSize(11);
      const summaryLines = doc.splitTextToSize(report.summary, pageWidth - 28);
      doc.text(summaryLines, 14, 108);
      
      // Calculate the Y position after the summary text
      let currentY = 108 + (summaryLines.length * 5);
      
      // Findings section
      doc.setFontSize(14);
      doc.text('Findings', 14, currentY + 10);
      doc.setFontSize(11);
      const findingsLines = doc.splitTextToSize(report.findings, pageWidth - 28);
      doc.text(findingsLines, 14, currentY + 18);
      
      // Update Y position after findings
      currentY = currentY + 18 + (findingsLines.length * 5);
      
      // Recommendations section
      doc.setFontSize(14);
      doc.text('Recommendations', 14, currentY + 10);
      doc.setFontSize(11);
      const recommendationLines = doc.splitTextToSize(report.recommendations, pageWidth - 28);
      doc.text(recommendationLines, 14, currentY + 18);
      
      // Footer
      const footerY = pageHeight - 25;
      doc.setLineWidth(0.5);
      doc.line(14, footerY - 2, pageWidth - 14, footerY - 2);
          
      doc.setFontSize(8);
      doc.setTextColor(80, 80, 80);
      doc.text(companyAddress, pageWidth / 2, footerY + 5, { align: 'center' });
      doc.text(`Generated on ${format(new Date(), 'MMM d, yyyy HH:mm')}`, pageWidth / 2, footerY + 10, { align: 'center' });
      
      // Save the PDF
      doc.save(`VT-${report.title.replace(/\s+/g, '-')}-${format(new Date(report.reportDate), 'yyyy-MM-dd')}.pdf`);
      
      console.log('PDF generated successfully for report:', report.title);
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
      if (editingReport) {
        await reportService.update(editingReport.id!, formData as Omit<Report, 'id'>);
      } else {
        await reportService.create(formData as Omit<Report, 'id'>);
      }
      setIsModalOpen(false);
      setEditingReport(null);
      resetForm();
      fetchReports();
    } catch (error) {
      console.error('Error saving report:', error);
    }
  };

  const handleEdit = (report: Report) => {
    setEditingReport(report);
    setFormData({
      ...report,
      reportDate: report.reportDate.split('T')[0],
      periodStart: report.periodStart.split('T')[0],
      periodEnd: report.periodEnd.split('T')[0],
    });
    setIsModalOpen(true);
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this report?')) {
      try {
        await reportService.delete(id);
        fetchReports();
      } catch (error) {
        console.error('Error deleting report:', error);
      }
    }
  };

  const resetForm = () => {
    setFormData({
      title: '',
      type: 'INVENTORY',
      reportDate: new Date().toISOString().split('T')[0],
      periodStart: '',
      periodEnd: '',
      summary: '',
      findings: '',
      recommendations: '',
    });
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const getReportTypeColor = (type: string) => {
    switch (type) {
      case 'INVENTORY':
        return 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200';
      case 'SALES':
        return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200';
      case 'PRODUCTION':
        return 'bg-purple-100 text-purple-800 dark:bg-purple-900 dark:text-purple-200';
      case 'QUALITY':
        return 'bg-orange-100 text-orange-800 dark:bg-orange-900 dark:text-orange-200';
      case 'LOGISTICS':
        return 'bg-indigo-100 text-indigo-800 dark:bg-indigo-900 dark:text-indigo-200';
      case 'FINANCIAL':
        return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200';
      default:
        return 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200';
    }
  };

  const filteredReports = reports.filter(report =>
    report.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
    report.type.toLowerCase().includes(searchQuery.toLowerCase()) ||
    report.summary.toLowerCase().includes(searchQuery.toLowerCase())
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
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Reports & Analytics</h1>
          <p className="text-gray-600 dark:text-gray-400">Generate and manage business reports</p>
        </div>
        <Button
          onClick={() => {
            setEditingReport(null);
            resetForm();
            setIsModalOpen(true);
          }}
          icon={Plus}
        >
          Create Report
        </Button>
      </div>
      <Card className="p-6">
        <div className="flex justify-between items-center mb-6">
          <div className="flex-1 max-w-md">
            <Input
              placeholder="Search reports..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              icon={Search}
            />
          </div>
        </div>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Title</TableHead>
              <TableHead>Type</TableHead>
              <TableHead>Report Date</TableHead>
              <TableHead>Period</TableHead>
              <TableHead>Generated By</TableHead>
              <TableHead>Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {filteredReports.map((report) => (
              <TableRow key={report.id}>
                <TableCell>
                  <div className="flex items-center">
                    <FileText className="h-4 w-4 text-gray-400 mr-2" />
                    <div>
                      <div className="font-medium">{report.title}</div>
                      <div className="text-sm text-gray-500 truncate max-w-xs">
                        {report.summary}
                      </div>
                    </div>
                  </div>
                </TableCell>
                <TableCell>
                  <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getReportTypeColor(report.type)}`}>
                    {report.type}
                  </span>
                </TableCell>
                <TableCell>
                  {new Date(report.reportDate).toLocaleDateString()}
                </TableCell>
                <TableCell>
                  <div className="text-sm">
                    <div>{new Date(report.periodStart).toLocaleDateString()}</div>
                    <div className="text-gray-500">to {new Date(report.periodEnd).toLocaleDateString()}</div>
                  </div>
                </TableCell>
                <TableCell>
                  <div>
                    <div className="font-medium">{report.generatedByUsername || 'System'}</div>
                    <div className="text-sm text-gray-500">
                      {report.createdAt && new Date(report.createdAt).toLocaleDateString()}
                    </div>
                  </div>
                </TableCell>
                <TableCell>
                  <div className="flex space-x-2">
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => handleEdit(report)}
                      icon={Eye}
                    >
                      View
                    </Button>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => handleEdit(report)}
                      icon={Edit}
                    >
                      Edit
                    </Button>
                    <Button
                      variant="ghost"
                      size="sm"
                      icon={Download}
                      className="text-blue-600 hover:text-blue-700"
                      onClick={() => generateReportPDF(report)}
                      disabled={isExporting}
                    >
                      {isExporting ? 'Exporting...' : 'Export'}
                    </Button>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => handleDelete(report.id!)}
                      icon={Trash2}
                      className="text-red-600 hover:text-red-700"
                    >
                      Delete
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Card>
      <Modal
        isOpen={isModalOpen}
        onClose={() => {
          setIsModalOpen(false);
          setEditingReport(null);
          resetForm();
        }}
        title={editingReport ? 'Edit Report' : 'Create New Report'}
        size="xl"
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Input
              label="Report Title"
              name="title"
              value={formData.title}
              onChange={handleInputChange}
              required
            />
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Report Type
              </label>
              <select
                name="type"
                value={formData.type}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
                required
              >
                <option value="INVENTORY">Inventory</option>
                <option value="SALES">Sales</option>
                <option value="PRODUCTION">Production</option>
                <option value="QUALITY">Quality</option>
                <option value="LOGISTICS">Logistics</option>
                <option value="FINANCIAL">Financial</option>
              </select>
            </div>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <Input
              label="Report Date"
              name="reportDate"
              type="date"
              value={formData.reportDate}
              onChange={handleInputChange}
              required
            />
            <Input
              label="Period Start"
              name="periodStart"
              type="date"
              value={formData.periodStart}
              onChange={handleInputChange}
              required
            />
            <Input
              label="Period End"
              name="periodEnd"
              type="date"
              value={formData.periodEnd}
              onChange={handleInputChange}
              required
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
              Summary
            </label>
            <textarea
              name="summary"
              value={formData.summary}
              onChange={handleInputChange}
              rows={3}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
              placeholder="Brief summary of the report..."
              required
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
              Findings
            </label>
            <textarea
              name="findings"
              value={formData.findings}
              onChange={handleInputChange}
              rows={4}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
              placeholder="Detailed findings and observations..."
              required
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
              Recommendations
            </label>
            <textarea
              name="recommendations"
              value={formData.recommendations}
              onChange={handleInputChange}
              rows={4}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
              placeholder="Recommendations and action items..."
              required
            />
          </div>
          <div className="flex justify-end space-x-3 pt-4">
            <Button
              type="button"
              variant="outline"
              onClick={() => {
                setIsModalOpen(false);
                setEditingReport(null);
                resetForm();
              }}
            >
              Cancel
            </Button>
            {editingReport && (
              <Button
                type="button"
                variant="outline"
                onClick={() => generateReportPDF(editingReport)}
                icon={Download}
                disabled={isExporting}
              >
                {isExporting ? 'Exporting...' : 'Export'}
              </Button>
            )}
            <Button type="submit">
              {editingReport ? 'Update' : 'Generate'} Report
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
