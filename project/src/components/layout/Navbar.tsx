
import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { LogOut, Moon, Sun, User, Wine } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { useTheme } from '../../context/ThemeContext';
import { Button } from '../ui/Button';
import { ConnectionStatus } from '../ui/ConnectionStatus';

export const Navbar: React.FC = () => {
  const { user, logout } = useAuth();
  const { isDark, toggleTheme } = useTheme();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="bg-gradient-to-r from-rose-800 via-[#811D1D] to-rose-900 shadow-sm border-b border-rose-900/50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex items-center">
            <Link to="/dashboard" className="flex items-center space-x-2">
              <Wine className="h-8 w-8 text-rose-100" />
              <span className="font-bold text-xl text-rose-50">
                ViniTrackPro
              </span>
            </Link>
          </div>

          <div className="flex items-center space-x-4">
            <ConnectionStatus />
            
            <Button
              variant="ghost"
              size="sm"
              onClick={toggleTheme}
              icon={isDark ? Sun : Moon}
              className="text-rose-100 hover:bg-rose-900/40"
            >
              <span className="sr-only">Toggle theme</span>
            </Button>

            {user && (
              <div className="flex items-center space-x-2">
                <div className="flex items-center space-x-2 px-3 py-2 rounded-lg bg-rose-900/30">
                  <User className="h-4 w-4 text-rose-100" />
                  <span className="text-sm font-medium text-rose-50">{user.username}</span>
                  <span className="text-xs text-rose-100/80">
                    {Array.isArray(user.username) ? user.username.join(', ') : ''}
                  </span>
                </div>

                <Button
                  variant="ghost"
                  size="sm"
                  onClick={handleLogout}
                  icon={LogOut}
                  className="text-rose-100 hover:bg-rose-900/40"
                >
                  Logout
                </Button>
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};