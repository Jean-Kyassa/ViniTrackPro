// import React from 'react';
// import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
// import { AuthProvider } from './context/AuthContext';
// import { ThemeProvider } from './context/ThemeContext';
// import { ProtectedRoute } from './components/auth/ProtectedRoute';
// import { Layout } from './components/layout/Layout';

// // Pages
// import { Landing } from './pages/Landing';
// import { Login } from './pages/auth/Login';
// import { Signup } from './pages/auth/Signup';
// import { ForgotPassword } from './pages/auth/ForgotPassword';
// import { ResetPassword } from './pages/auth/ResetPassword';
// import { Dashboard } from './pages/dashboard/Dashboard';
// import { Inventory } from './pages/dashboard/Inventory';
// import { Customers } from './pages/dashboard/Customers';
// import { Suppliers } from './pages/dashboard/Suppliers';
// import { Deliveries } from './pages/dashboard/Deliveries';
// import { Quality } from './pages/dashboard/Quality';
// import { Reports } from './pages/dashboard/Reports';
// import { Analytics } from './pages/dashboard/Analytics';

// function App() {
//   return (
//     <ThemeProvider>
//       <AuthProvider>
//         <Router>
//           <Routes>
//             {/* Public routes */}
//             <Route path="/" element={<Landing />} />
//             <Route path="/login" element={<Login />} />
//             <Route path="/signup" element={<Signup />} />
//             <Route path="/forgot-password" element={<ForgotPassword />} />
//             <Route path="/reset-password/:token" element={<ResetPassword />} />

//             {/* Protected dashboard routes */}
//             <Route
//               path="/dashboard"
//               element={
//                 <ProtectedRoute>
//                   <Layout>
//                     <Dashboard />
//                   </Layout>
//                 </ProtectedRoute>
//               }
//             />
//             <Route
//               path="/dashboard/inventory"
//               element={
//                 <ProtectedRoute requiredRoles={['ROLE_ADMIN', 'ROLE_INVENTORY']}>
//                   <Layout>
//                     <Inventory />
//                   </Layout>
//                 </ProtectedRoute>
//               }
//             />
//             <Route
//               path="/dashboard/customers"
//               element={
//                 <ProtectedRoute requiredRoles={['ROLE_ADMIN', 'ROLE_LOGISTICS']}>
//                   <Layout>
//                     <Customers />
//                   </Layout>
//                 </ProtectedRoute>
//               }
//             />
//             <Route
//               path="/dashboard/suppliers"
//               element={
//                 <ProtectedRoute requiredRoles={['ROLE_ADMIN', 'ROLE_INVENTORY']}>
//                   <Layout>
//                     <Suppliers />
//                   </Layout>
//                 </ProtectedRoute>
//               }
//             />
//             <Route
//               path="/dashboard/deliveries"
//               element={
//                 <ProtectedRoute requiredRoles={['ROLE_ADMIN', 'ROLE_LOGISTICS']}>
//                   <Layout>
//                     <Deliveries />
//                   </Layout>
//                 </ProtectedRoute>
//               }
//             />
//             <Route
//               path="/dashboard/quality"
//               element={
//                 <ProtectedRoute requiredRoles={['ROLE_ADMIN', 'ROLE_QUALITY']}>
//                   <Layout>
//                     <Quality />
//                   </Layout>
//                 </ProtectedRoute>
//               }
//             />
//             <Route
//               path="/dashboard/reports"
//               element={
//                 <ProtectedRoute requiredRoles={['ROLE_ADMIN', 'ROLE_INVENTORY', 'ROLE_LOGISTICS', 'ROLE_QUALITY']}>
//                   <Layout>
//                     <Reports />
//                   </Layout>
//                 </ProtectedRoute>
//               }
//             />
//             <Route
//               path="/dashboard/analytics"
//               element={
//                 <ProtectedRoute requiredRoles={['ROLE_ADMIN']}>
//                   <Layout>
//                     <Analytics />
//                   </Layout>
//                 </ProtectedRoute>
//               }
//             />

//             {/* Catch all route */}
//             <Route path="*" element={<Navigate to="/\" replace />} />
//           </Routes>
//         </Router>
//       </AuthProvider>
//     </ThemeProvider>
//   );
// }

// export default App;



import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { ThemeProvider } from './context/ThemeContext';
import { ProtectedRoute } from './components/auth/ProtectedRoute';
import { Layout } from './components/layout/Layout';
import { ErrorBoundary } from './components/ui/ErrorBoundary';

// Pages
import { Landing } from './pages/Landing';
import { Login } from './pages/auth/Login';
import { Signup } from './pages/auth/Signup';
import { ForgotPassword } from './pages/auth/ForgotPassword';
import { ResetPassword } from './pages/auth/ResetPassword';
import { Dashboard } from './pages/dashboard/Dashboard';
import { Inventory } from './pages/dashboard/Inventory';
import { Customers } from './pages/dashboard/Customers';
import { Suppliers } from './pages/dashboard/Suppliers';
import { Deliveries } from './pages/dashboard/Deliveries';
import { Production } from './pages/dashboard/Production';
import { Quality } from './pages/dashboard/Quality';
import { Reports } from './pages/dashboard/Reports';
import { Analytics } from './pages/dashboard/Analytics';
import { Chatbot } from './pages/dashboard/Chatbot';

function App() {
  return (
    <ErrorBoundary>
      <ThemeProvider>
        <AuthProvider>
          <Router
            future={{
              v7_startTransition: true,
              v7_relativeSplatPath: true,
            }}
          >
            <Routes>
              {/* Public routes */}
              <Route path="/" element={<Landing />} />
              <Route path="/login" element={<Login />} />
              <Route path="/signup" element={<Signup />} />
              <Route path="/forgot-password" element={<ForgotPassword />} />
              <Route path="/reset-password/:token" element={<ResetPassword />} />

              {/* Protected dashboard routes */}
              <Route
                path="/dashboard"
                element={
                  <ProtectedRoute>
                    <Layout>
                      <Dashboard />
                    </Layout>
                  </ProtectedRoute>
                }
              />
              <Route
                path="/dashboard/inventory"
                element={
                  <ProtectedRoute requiredRoles={['ROLE_ADMIN', 'ROLE_INVENTORY']}>
                    <Layout>
                      <Inventory />
                    </Layout>
                  </ProtectedRoute>
                }
              />
              <Route
                path="/dashboard/customers"
                element={
                  <ProtectedRoute requiredRoles={['ROLE_ADMIN', 'ROLE_LOGISTICS']}>
                    <Layout>
                      <Customers />
                    </Layout>
                  </ProtectedRoute>
                }
              />
              <Route
                path="/dashboard/suppliers"
                element={
                  <ProtectedRoute requiredRoles={['ROLE_ADMIN', 'ROLE_INVENTORY']}>
                    <Layout>
                      <Suppliers />
                    </Layout>
                  </ProtectedRoute>
                }
              />
              <Route
                path="/dashboard/deliveries"
                element={
                  <ProtectedRoute requiredRoles={['ROLE_ADMIN', 'ROLE_LOGISTICS']}>
                    <Layout>
                      <Deliveries />
                    </Layout>
                  </ProtectedRoute>
                }
              />
              <Route
                path="/dashboard/production"
                element={
                  <ProtectedRoute requiredRoles={['ROLE_ADMIN', 'ROLE_PRODUCTION']}>
                    <Layout>
                      <Production />
                    </Layout>
                  </ProtectedRoute>
                }
              />
              <Route
                path="/dashboard/quality"
                element={
                  <ProtectedRoute requiredRoles={['ROLE_ADMIN', 'ROLE_QUALITY']}>
                    <Layout>
                      <Quality />
                    </Layout>
                  </ProtectedRoute>
                }
              />
              <Route
                path="/dashboard/reports"
                element={
                  <ProtectedRoute requiredRoles={['ROLE_ADMIN', 'ROLE_INVENTORY', 'ROLE_LOGISTICS', 'ROLE_QUALITY']}>
                    <Layout>
                      <Reports />
                    </Layout>
                  </ProtectedRoute>
                }
              />
              <Route
                path="/dashboard/analytics"
                element={
                  <ProtectedRoute requiredRoles={['ROLE_ADMIN']}>
                    <Layout>
                      <Analytics />
                    </Layout>
                  </ProtectedRoute>
                }
              />
              <Route
                path="/dashboard/chat"
                element={
                  <ProtectedRoute requiredRoles={['ROLE_ADMIN', 'ROLE_LOGISTICS', 'ROLE_USER']}>
                    <Layout>
                      <Chatbot />
                    </Layout>
                  </ProtectedRoute>
                }
              />

              {/* Catch all route */}
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </Router>
        </AuthProvider>
      </ThemeProvider>
    </ErrorBoundary>
  );
}

export default App;