import React, { useState } from 'react';
import { Download, FileText, FileSpreadsheet, File } from 'lucide-react';
import { Button } from './Button';
import { exportService } from '../../services/exportService';

interface ExportButtonProps {
  data: any;
  filename: string;
  type: string;
  className?: string;
}

export const ExportButton: React.FC<ExportButtonProps> = ({
  data,
  filename,
  type,
  className = ''
}) => {
  const [isExporting, setIsExporting] = useState(false);
  const [showDropdown, setShowDropdown] = useState(false);

  const handleExport = async (format: 'pdf' | 'excel' | 'word') => {
    setIsExporting(true);
    setShowDropdown(false);
    
    try {
      let blob: Blob;
      
      switch (format) {
        case 'pdf':
          blob = await exportService.exportToPDF(type, data);
          break;
        case 'excel':
          blob = await exportService.exportToExcel(type, data);
          break;
        case 'word':
          blob = await exportService.exportToWord(type, data);
          break;
        default:
          throw new Error('Unsupported format');
      }
      
      exportService.downloadFile(blob, filename, format);
    } catch (error) {
      console.error('Export error:', error);
      alert('Export failed. Please try again.');
    } finally {
      setIsExporting(false);
    }
  };

  return (
    <div className={`relative ${className}`}>
      <Button
        variant="outline"
        size="sm"
        onClick={() => setShowDropdown(!showDropdown)}
        disabled={isExporting}
        icon={Download}
      >
        {isExporting ? 'Exporting...' : 'Export'}
      </Button>

      {showDropdown && (
        <div className="absolute right-0 mt-2 w-48 bg-white dark:bg-gray-800 rounded-md shadow-lg border border-gray-200 dark:border-gray-700 z-50">
          <div className="py-1">
            <button
              onClick={() => handleExport('pdf')}
              className="flex items-center w-full px-4 py-2 text-sm text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700"
            >
              <FileText className="h-4 w-4 mr-2 text-red-500" />
              Export as PDF
            </button>
            <button
              onClick={() => handleExport('excel')}
              className="flex items-center w-full px-4 py-2 text-sm text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700"
            >
              <FileSpreadsheet className="h-4 w-4 mr-2 text-green-500" />
              Export as Excel
            </button>
            <button
              onClick={() => handleExport('word')}
              className="flex items-center w-full px-4 py-2 text-sm text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700"
            >
              <File className="h-4 w-4 mr-2 text-blue-500" />
              Export as Word
            </button>
          </div>
        </div>
      )}

      {showDropdown && (
        <div
          className="fixed inset-0 z-40"
          onClick={() => setShowDropdown(false)}
        />
      )}
    </div>
  );
};