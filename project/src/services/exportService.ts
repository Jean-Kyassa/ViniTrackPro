import api from '../utils/api';

export const exportService = {
  exportToPDF: async (type: string, data: any): Promise<Blob> => {
    try {
      const response = await api.post(`/export/pdf/${type}`, data, {
        responseType: 'blob',
        headers: {
          'Accept': 'application/pdf'
        }
      });
      return response.data;
    } catch (error) {
      console.error('PDF export error:', error);
      throw new Error('Failed to export PDF');
    }
  },

  exportToExcel: async (type: string, data: any): Promise<Blob> => {
    try {
      const response = await api.post(`/export/excel/${type}`, data, {
        responseType: 'blob',
        headers: {
          'Accept': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        }
      });
      return response.data;
    } catch (error) {
      console.error('Excel export error:', error);
      throw new Error('Failed to export Excel');
    }
  },

  exportToWord: async (type: string, data: any): Promise<Blob> => {
    try {
      const response = await api.post(`/export/word/${type}`, data, {
        responseType: 'blob',
        headers: {
          'Accept': 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
        }
      });
      return response.data;
    } catch (error) {
      console.error('Word export error:', error);
      throw new Error('Failed to export Word document');
    }
  },

  downloadFile: (blob: Blob, filename: string, type: 'pdf' | 'excel' | 'word') => {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    
    const extensions = {
      pdf: '.pdf',
      excel: '.xlsx',
      word: '.docx'
    };
    
    link.download = `${filename}${extensions[type]}`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  }
};