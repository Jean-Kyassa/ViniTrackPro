import React, { useState, useEffect } from 'react';
import { Wifi, WifiOff, AlertCircle } from 'lucide-react';
import api from '../../utils/api';

export const ConnectionStatus: React.FC = () => {
  const [isConnected, setIsConnected] = useState(true);
  const [isChecking, setIsChecking] = useState(false);

  useEffect(() => {
    checkConnection();
    const interval = setInterval(checkConnection, 30000); // Check every 30 seconds
    return () => clearInterval(interval);
  }, []);

  const checkConnection = async () => {
    setIsChecking(true);
    try {
      // Try to make a simple request to check connectivity
      await api.get('/auth/users', { timeout: 5000 });
      setIsConnected(true);
    } catch (error) {
      console.error('Connection check failed:', error);
      setIsConnected(false);
    } finally {
      setIsChecking(false);
    }
  };

  if (isConnected) {
    return (
      <div className="flex items-center text-green-600 dark:text-green-400 text-sm">
        <Wifi className="h-4 w-4 mr-1" />
        <span>Connected</span>
      </div>
    );
  }

  return (
    <div className="flex items-center text-red-600 dark:text-red-400 text-sm">
      <WifiOff className="h-4 w-4 mr-1" />
      <span>Disconnected</span>
      {!isChecking && (
        <button
          onClick={checkConnection}
          className="ml-2 text-xs underline hover:no-underline"
        >
          Retry
        </button>
      )}
    </div>
  );
};