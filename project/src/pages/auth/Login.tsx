// import React, { useState } from 'react';
// import { Link, useNavigate, useLocation } from 'react-router-dom';
// import { LogIn, Mail, Lock, Wine, ArrowLeft } from 'lucide-react';
// import { useAuth } from '../../context/AuthContext';
// import { Button } from '../../components/ui/Button';
// import { Input } from '../../components/ui/Input';
// import { Card } from '../../components/ui/Card';

// export const Login: React.FC = () => {
//   const [username, setUsername] = useState('');
//   const [password, setPassword] = useState('');
//   const [isLoading, setIsLoading] = useState(false);
//   const [error, setError] = useState('');

//   const { login } = useAuth();
//   const navigate = useNavigate();
//   const location = useLocation();

//   const from = location.state?.from?.pathname || '/dashboard';

//   const handleSubmit = async (e: React.FormEvent) => {
//     e.preventDefault();
//     setIsLoading(true);
//     setError('');

//     try {
//       await login({ username, password });
//       navigate(from, { replace: true });
//     } catch (err: any) {
//       console.error('Login error:', err);
      
//       // Handle different types of errors
//       if (err.code === 'ERR_NETWORK') {
//         setError('Unable to connect to server. Please check if the backend is running on port 8083.');
//       } else if (err.response?.status === 401) {
//         setError('Invalid username or password');
//       } else if (err.response?.status === 403) {
//         setError('Access denied. Please contact administrator.');
//       } else {
//         setError(err.response?.data?.error || err.response?.data?.message || 'Login failed. Please try again.');
//       }
//     } finally {
//       setIsLoading(false);
//     }
//   };

//   return (
//     <div className="min-h-screen bg-gradient-to-br from-blue-900 via-purple-900 to-indigo-900 flex items-center justify-center p-4">
//       <div className="absolute inset-0 bg-black/20" />
      
//       {/* Animated background elements */}
//       <div className="absolute inset-0 overflow-hidden">
//         <div className="absolute -top-40 -right-40 w-80 h-80 bg-blue-500/10 rounded-full blur-3xl animate-pulse" />
//         <div className="absolute bottom-0 -left-40 w-96 h-96 bg-purple-500/10 rounded-full blur-3xl animate-pulse delay-1000" />
//       </div>

//       <div className="relative z-10 w-full max-w-md">
//         <Link 
//           to="/" 
//           className="inline-flex items-center text-white/80 hover:text-white mb-8 transition-colors"
//         >
//           <ArrowLeft className="w-4 h-4 mr-2" />
//           Back to Home
//         </Link>

//         <Card className="bg-white/10 backdrop-blur-lg border-white/20">
//           <div className="text-center mb-8">
//             <div className="flex justify-center mb-4">
//               <div className="bg-blue-600 p-3 rounded-full">
//                 <Wine className="h-8 w-8 text-white" />
//               </div>
//             </div>
//             <h2 className="text-2xl font-bold text-white mb-2">Welcome Back</h2>
//             <p className="text-white/70">Sign in to your ViniTrackPro account</p>
//           </div>

//           <form onSubmit={handleSubmit} className="space-y-6">
//             {error && (
//               <div className="bg-red-500/20 border border-red-500/50 text-red-200 px-4 py-3 rounded-lg">
//                 {error}
//               </div>
//             )}

//             <Input
//               label="Username"
//               type="text"
//               value={username}
//               onChange={(e) => setUsername(e.target.value)}
//               required
//               icon={Mail}
//               className="bg-white/10 border-white/20 text-white placeholder-white/50"
//             />

//             <Input
//               label="Password"
//               type="password"
//               value={password}
//               onChange={(e) => setPassword(e.target.value)}
//               required
//               icon={Lock}
//               className="bg-white/10 border-white/20 text-white placeholder-white/50"
//             />

//             <div className="flex justify-between items-center">
//               <Link to="/forgot-password" className="text-blue-400 hover:text-blue-300 text-sm">
//                 Forgot password?
//               </Link>
//             </div>

//             <Button
//               type="submit"
//               className="w-full bg-blue-600 hover:bg-blue-700 text-white py-3"
//               isLoading={isLoading}
//               icon={LogIn}
//             >
//               Sign In
//             </Button>
//           </form>

//           <div className="mt-6 text-center">
//             <p className="text-white/70">
//               Don't have an account?{' '}
//               <Link to="/signup" className="text-blue-400 hover:text-blue-300 font-medium">
//                 Sign up
//               </Link>
//             </p>
//           </div>

//           <div className="mt-4 pt-4 border-t border-white/20">
//             <p className="text-center text-sm text-white/60">
//               Demo accounts available:
//             </p>
//             <div className="mt-2 text-xs text-white/50 space-y-1">
//               <div>Admin: admin / admin123</div>
//               <div>Inventory: inventory / inventory123</div>
//               <div>Logistics: logistics / logistics123</div>
//               <div>Quality: quality / quality123</div>
//             </div>
//           </div>
//         </Card>
//       </div>
//     </div>
//   );
// };

import React, { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { LogIn, Mail, Lock, Wine, ArrowLeft, AlertCircle } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Card } from '../../components/ui/Card';

export const Login: React.FC = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const from = location.state?.from?.pathname || '/dashboard';

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setError('');

    try {
      await login({ username, password });
      navigate(from, { replace: true });
    } catch (err: any) {
      console.error('Login error:', err);
      
      // Handle different types of errors
      if (err.code === 'ERR_NETWORK' || !err.response) {
        setError('Unable to connect to server. Please ensure the backend is running on port 8083 and CORS is properly configured.');
      } else if (err.response?.status === 401) {
        setError('Invalid username or password');
      } else if (err.response?.status === 403) {
        setError('Access denied. Please contact administrator.');
      } else {
        setError(err.response?.data?.error || err.response?.data?.message || 'Login failed. Please try again.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-900 via-purple-900 to-indigo-900 flex items-center justify-center p-4">
      <div className="absolute inset-0 bg-black/20" />
      
      {/* Animated background elements */}
      <div className="absolute inset-0 overflow-hidden">
        <div className="absolute -top-40 -right-40 w-80 h-80 bg-blue-500/10 rounded-full blur-3xl animate-pulse" />
        <div className="absolute bottom-0 -left-40 w-96 h-96 bg-purple-500/10 rounded-full blur-3xl animate-pulse delay-1000" />
      </div>

      <div className="relative z-10 w-full max-w-md">
        <Link 
          to="/" 
          className="inline-flex items-center text-white/80 hover:text-white mb-8 transition-colors"
        >
          <ArrowLeft className="w-4 h-4 mr-2" />
          Back to Home
        </Link>

        <Card className="bg-white/10 backdrop-blur-lg border-white/20">
          <div className="text-center mb-8">
            <div className="flex justify-center mb-4">
              <div className="bg-blue-600 p-3 rounded-full">
                <Wine className="h-8 w-8 text-white" />
              </div>
            </div>
            <h2 className="text-2xl font-bold text-white mb-2">Welcome Back</h2>
            <p className="text-white/70">Sign in to your ViniTrackPro account</p>
          </div>

          <form onSubmit={handleSubmit} className="space-y-6">
            {error && (
              <div className="bg-red-500/20 border border-red-500/50 text-red-200 px-4 py-3 rounded-lg flex items-start space-x-2">
                <AlertCircle className="h-5 w-5 mt-0.5 flex-shrink-0" />
                <span className="text-sm">{error}</span>
              </div>
            )}

            <Input
              label="Username"
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              icon={Mail}
              className="bg-white/10 border-white/20 text-white placeholder-white/50"
            />

            <Input
              label="Password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              icon={Lock}
              className="bg-white/10 border-white/20 text-white placeholder-white/50"
            />

            <div className="flex justify-between items-center">
              <Link to="/forgot-password" className="text-blue-400 hover:text-blue-300 text-sm">
                Forgot password?
              </Link>
            </div>

            <Button
              type="submit"
              className="w-full bg-blue-600 hover:bg-blue-700 text-white py-3"
              isLoading={isLoading}
              icon={LogIn}
            >
              Sign In
            </Button>
          </form>

          <div className="mt-6 text-center">
            <p className="text-white/70">
              Don't have an account?{' '}
              <Link to="/signup" className="text-blue-400 hover:text-blue-300 font-medium">
                Sign up
              </Link>
            </p>
          </div>

          {/* <div className="mt-4 pt-4 border-t border-white/20">
            <p className="text-center text-sm text-white/60">
              Demo accounts available:
            </p>
            <div className="mt-2 text-xs text-white/50 space-y-1">
              <div>Admin: admin / admin123</div>
              <div>Inventory: inventory / inventory123</div>
              <div>Logistics: logistics / logistics123</div>
              <div>Quality: quality / quality123</div>
            </div>
          </div> */}
        </Card>
      </div>
    </div>
  );
};