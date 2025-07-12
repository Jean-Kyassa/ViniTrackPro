// import React from 'react';
// import { DivideIcon as LucideIcon } from 'lucide-react';

// interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
//   variant?: 'primary' | 'secondary' | 'outline' | 'ghost' | 'danger';
//   size?: 'sm' | 'md' | 'lg';
//   icon?: LucideIcon;
//   isLoading?: boolean;
//   children: React.ReactNode;
// }

// export const Button: React.FC<ButtonProps> = ({
//   variant = 'primary',
//   size = 'md',
//   icon: Icon,
//   isLoading = false,
//   children,
//   className = '',
//   disabled,
//   ...props
// }) => {
//   const baseClasses = 'inline-flex items-center justify-center rounded-lg font-medium transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2';
  
//   const variants = {
//     primary: 'bg-blue-600 hover:bg-blue-700 text-white focus:ring-blue-500',
//     secondary: 'bg-gray-600 hover:bg-gray-700 text-white focus:ring-gray-500',
//     outline: 'border border-gray-300 hover:bg-gray-50 text-gray-700 dark:border-gray-600 dark:hover:bg-gray-800 dark:text-gray-200',
//     ghost: 'hover:bg-gray-100 text-gray-600 dark:hover:bg-gray-800 dark:text-gray-300',
//     danger: 'bg-red-600 hover:bg-red-700 text-white focus:ring-red-500',
//   };

//   const sizes = {
//     sm: 'px-3 py-1.5 text-sm',
//     md: 'px-4 py-2 text-sm',
//     lg: 'px-6 py-3 text-base',
//   };

//   const classes = `${baseClasses} ${variants[variant]} ${sizes[size]} ${className}`;

//   return (
//     <button
//       className={classes}
//       disabled={disabled || isLoading}
//       {...props}
//     >
//       {isLoading ? (
//         <div className="w-4 h-4 border-2 border-current border-t-transparent rounded-full animate-spin mr-2" />
//       ) : Icon ? (
//         <Icon className="w-4 h-4 mr-2" />
//       ) : null}
//       {children}
//     </button>
//   );
// };



import React from 'react';
import { DivideIcon as LucideIcon } from 'lucide-react';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'outline' | 'ghost' | 'danger';
  size?: 'sm' | 'md' | 'lg';
  icon?: typeof LucideIcon;
  isLoading?: boolean;
  children: React.ReactNode;
}

export const Button: React.FC<ButtonProps> = ({
  variant = 'primary',
  size = 'md',
  icon: Icon,
  isLoading = false,
  children,
  className = '',
  disabled,
  ...props
}) => {
  const baseClasses = 'inline-flex items-center justify-center rounded-lg font-medium transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2';
  
  const variants = {
    primary: 'bg-blue-600 hover:bg-blue-700 text-white focus:ring-blue-500',
    secondary: 'bg-gray-600 hover:bg-gray-700 text-white focus:ring-gray-500',
    outline: 'border border-gray-300 hover:bg-gray-50 text-gray-700 dark:border-gray-600 dark:hover:bg-gray-800 dark:text-gray-200',
    ghost: 'hover:bg-gray-100 text-gray-600 dark:hover:bg-gray-800 dark:text-gray-300',
    danger: 'bg-red-600 hover:bg-red-700 text-white focus:ring-red-500',
  };

  const sizes = {
    sm: 'px-3 py-1.5 text-sm',
    md: 'px-4 py-2 text-sm',
    lg: 'px-6 py-3 text-base',
  };

  const classes = `${baseClasses} ${variants[variant]} ${sizes[size]} ${className}`;

  return (
    <button
      className={classes}
      disabled={disabled || isLoading}
      {...props}
    >
      {isLoading ? (
        <div className="w-4 h-4 border-2 border-current border-t-transparent rounded-full animate-spin mr-2" />
      ) : Icon ? (
        <Icon className="w-4 h-4 mr-2" />
      ) : null}
      {children}
    </button>
  );
};