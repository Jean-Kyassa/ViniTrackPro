import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import Dashboard from './pages/Dashboard';
import Inventory from './pages/Inventory';
import Logistics from './pages/Logistics';
import Orders from './pages/Orders';

function App() {
  return (
    <Router>
      <Layout>
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/inventory" element={<Inventory />} />
          <Route path="/logistics" element={<Logistics />} />
          <Route path="/orders" element={<Orders />} />
        </Routes>
      </Layout>
    </Router>
  );
}

export default App;