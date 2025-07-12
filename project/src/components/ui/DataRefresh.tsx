import React, { useState, useEffect } from 'react';
import { RefreshCw } from 'lucide-react';
import { Button } from './Button';

interface DataRefreshProps {
  onRefresh: () => Promise<void>;
  interval?: number; // Auto-refresh interval in milliseconds
  className?: string;
}

export const DataRefresh: React.FC<DataRefreshProps> = ({
  onRefresh,
  interval = 30000, // 30 seconds default
  className = ''
}) => {
  const [isRefreshing, setIsRefreshing] = useState(false);
  const [lastRefresh, setLastRefresh] = useState<Date>(new Date());

  useEffect(() => {
    if (interval > 0) {
      const intervalId = setInterval(async () => {
        await handleRefresh();
      }, interval);

      return () => clearInterval(intervalId);
    }
  }, [interval]);

  const handleRefresh = async () => {
    setIsRefreshing(true);
    try {
      await onRefresh();
      setLastRefresh(new Date());
    } catch (error) {
      console.error('Refresh error:', error);
    } finally {
      setIsRefreshing(false);
    }
  };

  return (
    <div className={`flex items-center space-x-2 ${className}`}>
      <Button
        variant="ghost"
        size="sm"
        onClick={handleRefresh}
        disabled={isRefreshing}
        icon={RefreshCw}
        className={isRefreshing ? 'animate-spin' : ''}
      >
        Refresh
      </Button>
      <span className="text-xs text-gray-500 dark:text-gray-400">
        Last updated: {lastRefresh.toLocaleTimeString()}
      </span>
    </div>
  );
};