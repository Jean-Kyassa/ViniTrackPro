import React, { useState, useEffect } from 'react';

export default function Dashboard() {
  const [stats, setStats] = useState({
    totalWines: 0,
    pendingOrders: 0,
    activeShipments: 0,
    lowStock: 0
  });

  useEffect(() => {
    // TODO: Fetch actual stats from backend
    setStats({
      totalWines: 150,
      pendingOrders: 12,
      activeShipments: 8,
      lowStock: 5
    });
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-semibold text-gray-900">Dashboard</h1>
      
      <div className="mt-6 grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
        <div className="overflow-hidden rounded-lg bg-white px-4 py-5 shadow sm:p-6">
          <dt className="truncate text-sm font-medium text-gray-500">Total Wines</dt>
          <dd className="mt-1 text-3xl font-semibold tracking-tight text-gray-900">{stats.totalWines}</dd>
        </div>
        <div className="overflow-hidden rounded-lg bg-white px-4 py-5 shadow sm:p-6">
          <dt className="truncate text-sm font-medium text-gray-500">Pending Orders</dt>
          <dd className="mt-1 text-3xl font-semibold tracking-tight text-gray-900">{stats.pendingOrders}</dd>
        </div>
        <div className="overflow-hidden rounded-lg bg-white px-4 py-5 shadow sm:p-6">
          <dt className="truncate text-sm font-medium text-gray-500">Active Shipments</dt>
          <dd className="mt-1 text-3xl font-semibold tracking-tight text-gray-900">{stats.activeShipments}</dd>
        </div>
        <div className="overflow-hidden rounded-lg bg-white px-4 py-5 shadow sm:p-6">
          <dt className="truncate text-sm font-medium text-gray-500">Low Stock Items</dt>
          <dd className="mt-1 text-3xl font-semibold tracking-tight text-gray-900">{stats.lowStock}</dd>
        </div>
      </div>
    </div>
  );
}