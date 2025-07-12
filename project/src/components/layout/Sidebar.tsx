
import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import {
  BarChart3,
  Package,
  Truck,
  Users,
  ShieldCheck,
  FileText,
  Building2,
  Home,
  MessageCircle,
  Factory,
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

const navigation = [
  { name: 'Dashboard', href: '/dashboard', icon: Home, roles: ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_INVENTORY', 'ROLE_LOGISTICS', 'ROLE_QUALITY', 'ROLE_PRODUCTION'] },
  { name: 'Inventory', href: '/dashboard/inventory', icon: Package, roles: ['ROLE_ADMIN', 'ROLE_INVENTORY'] },
  { name: 'Production', href: '/dashboard/production', icon: Factory, roles: ['ROLE_ADMIN', 'ROLE_PRODUCTION'] },
  { name: 'Customers', href: '/dashboard/customers', icon: Users, roles: ['ROLE_ADMIN', 'ROLE_LOGISTICS'] },
  { name: 'Suppliers', href: '/dashboard/suppliers', icon: Building2, roles: ['ROLE_ADMIN', 'ROLE_INVENTORY'] },
  { name: 'Deliveries', href: '/dashboard/deliveries', icon: Truck, roles: ['ROLE_ADMIN', 'ROLE_LOGISTICS'] },
  { name: 'Quality', href: '/dashboard/quality', icon: ShieldCheck, roles: ['ROLE_ADMIN', 'ROLE_QUALITY'] },
  { name: 'Reports', href: '/dashboard/reports', icon: FileText, roles: ['ROLE_ADMIN', 'ROLE_INVENTORY', 'ROLE_LOGISTICS', 'ROLE_QUALITY'] },
  { name: 'Analytics', href: '/dashboard/analytics', icon: BarChart3, roles: ['ROLE_ADMIN'] },
  { name: 'Support Chat', href: '/dashboard/chat', icon: MessageCircle, roles: ['ROLE_ADMIN', 'ROLE_LOGISTICS', 'ROLE_USER'] },
];

export const Sidebar: React.FC = () => {
  const location = useLocation();
  const { hasRole } = useAuth();

  const filteredNavigation = navigation.filter(item =>
    item.roles.some(role => hasRole(role))
  );

  return (
    <div className="hidden md:flex md:w-64 md:flex-col md:fixed md:inset-y-0 pt-16 z-40">
      <div className="flex-1 flex flex-col min-h-0 bg-gradient-to-b from-purple-900 via-rose-900 to-rose-800 border-r border-rose-900/50 backdrop-blur-lg">
        <div className="flex-1 flex flex-col pt-5 pb-4 overflow-y-auto">
          <nav className="mt-5 flex-1 px-2 space-y-1">
            {filteredNavigation.map((item) => {
              const isActive = location.pathname === item.href;
              return (
                <Link
                  key={item.name}
                  to={item.href}
                  className={`
                    group flex items-center px-2 py-2 text-sm font-medium rounded-md transition-colors duration-200
                    ${isActive
                      ? 'bg-rose-800/60 text-rose-50 shadow-lg shadow-rose-900/30'
                      : 'text-rose-200/90 hover:bg-rose-800/40 hover:text-rose-50'
                    }
                  `}
                >
                  <item.icon
                    className={`
                      mr-3 flex-shrink-0 h-6 w-6 transition-colors duration-200
                      ${isActive
                        ? 'text-rose-50'
                        : 'text-rose-300/80 group-hover:text-rose-50'
                      }
                    `}
                  />
                  {item.name}
                </Link>
              );
            })}
          </nav>
        </div>
      </div>
    </div>
  );
};