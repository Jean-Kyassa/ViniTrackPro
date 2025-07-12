import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { 
  ArrowRight, 
  Wine, 
  Shield, 
  BarChart3, 
  Truck,
  Package,
  Users,
  CheckCircle,
  Star,
  Moon,
  Sun
} from 'lucide-react';
import { Button } from '../components/ui/Button';
import { useTheme } from '../context/ThemeContext';

export const Landing: React.FC = () => {
  const [isVisible, setIsVisible] = useState(false);
  const { isDark, toggleTheme } = useTheme();

  useEffect(() => {
    setIsVisible(true);
  }, []);

  const features = [
    {
      icon: Package,
      title: 'Inventory Management',
      description: 'Track your wine inventory with precision and real-time updates.',
    },
    {
      icon: Shield,
      title: 'Quality Control',
      description: 'Ensure product quality with comprehensive testing and monitoring.',
    },
    {
      icon: Truck,
      title: 'Delivery Tracking',
      description: 'Monitor deliveries from warehouse to customer doorstep.',
    },
    {
      icon: BarChart3,
      title: 'Analytics & Reports',
      description: 'Gain insights with detailed analytics and custom reports.',
    },
    {
      icon: Users,
      title: 'Customer Management',
      description: 'Manage customer relationships and order history efficiently.',
    },
    {
      icon: Wine,
      title: 'Production Planning',
      description: 'Optimize your wine production with intelligent scheduling.',
    },
  ];

  const testimonials = [
    {
      name: 'Sarah Johnson',
      role: 'Winery Owner',
      content: 'ViniTrackPro has revolutionized how we manage our wine production and inventory.',
      rating: 5,
    },
    {
      name: 'Michael Chen',
      role: 'Operations Manager',
      content: 'The delivery tracking and quality control features are game-changers for our business.',
      rating: 5,
    },
    {
      name: 'Emily Rodriguez',
      role: 'Quality Director',
      content: 'Outstanding platform with incredible attention to detail and user experience.',
      rating: 5,
    },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 via-blue-900 to-black dark:from-black dark:via-gray-900 dark:to-blue-900 text-white relative overflow-hidden">
      {/* Theme Toggle */}
      <div className="absolute top-6 right-6 z-50">
        <Button
          variant="ghost"
          size="sm"
          onClick={toggleTheme}
          icon={isDark ? Sun : Moon}
          className="text-white hover:bg-white/10"
        >
          <span className="sr-only">Toggle theme</span>
        </Button>
      </div>

      {/* Background Video Effect */}
      <div className="absolute inset-0 bg-gradient-to-r from-black/50 to-transparent z-10" />
      
      {/* Animated Background Shapes */}
      <div className="absolute inset-0 overflow-hidden">
        <div className="absolute -top-40 -right-40 w-80 h-80 bg-blue-500/20 rounded-full blur-3xl animate-pulse" />
        <div className="absolute top-1/2 -left-40 w-96 h-96 bg-purple-500/20 rounded-full blur-3xl animate-pulse delay-1000" />
        <div className="absolute bottom-0 right-1/3 w-64 h-64 bg-indigo-500/20 rounded-full blur-3xl animate-pulse delay-500" />
      </div>

      {/* Hero Section */}
      <section className="relative z-20 min-h-screen flex items-center">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid lg:grid-cols-2 gap-12 items-center">
            <div className={`space-y-8 ${isVisible ? 'animate-fade-in-up' : 'opacity-0'}`}>
              <div className="flex items-center space-x-2 text-blue-400">
                <Wine className="h-8 w-8" />
                <span className="text-2xl font-bold">ViniTrackPro</span>
              </div>
              
              <h1 className="text-5xl lg:text-7xl font-bold leading-tight">
                Premium Wine
                <span className="bg-gradient-to-r from-blue-400 to-purple-400 bg-clip-text text-transparent">
                  {' '}Management
                </span>
              </h1>
              
              <p className="text-xl text-gray-300 leading-relaxed max-w-lg">
                Transform your wine business with our comprehensive management platform. 
                From production to delivery, we've got you covered.
              </p>
              
              <div className="flex flex-col sm:flex-row gap-4">
                <Link to="/signup">
                  <Button 
                    size="lg" 
                    icon={ArrowRight}
                    className="bg-blue-600 hover:bg-blue-700 text-white px-8 py-4 text-lg font-semibold shadow-xl hover:shadow-2xl transition-all duration-300 transform hover:scale-105"
                  >
                    Get Started Free
                  </Button>
                </Link>
                <Link to="/login">
                  <Button 
                    variant="outline" 
                    size="lg"
                    className="border-2 border-white/20 text-white hover:bg-white/10 px-8 py-4 text-lg font-semibold backdrop-blur-sm"
                  >
                    Sign In
                  </Button>
                </Link>
              </div>

              <div className="flex items-center space-x-6 text-sm text-gray-400">
                <div className="flex items-center space-x-2">
                  <CheckCircle className="h-5 w-5 text-green-400" />
                  <span>No credit card required</span>
                </div>
                <div className="flex items-center space-x-2">
                  <CheckCircle className="h-5 w-5 text-green-400" />
                  <span>14-day free trial</span>
                </div>
              </div>
            </div>

            <div className={`relative ${isVisible ? 'animate-fade-in-right' : 'opacity-0'}`}>
              <div className="relative">
                <div className="absolute inset-0 bg-gradient-to-r from-blue-600 to-purple-600 rounded-3xl blur-2xl opacity-30 transform rotate-6" />
                <div className="relative bg-white/10 backdrop-blur-lg rounded-3xl p-8 border border-white/20">
                  <div className="space-y-6">
                    <div className="flex items-center justify-between">
                      <h3 className="text-2xl font-bold">Production Overview</h3>
                      <div className="w-12 h-12 bg-green-500 rounded-full flex items-center justify-center">
                        <Wine className="h-6 w-6 text-white" />
                      </div>
                    </div>
                    
                    <div className="grid grid-cols-2 gap-4">
                      <div className="bg-white/5 rounded-xl p-4">
                        <div className="text-sm text-gray-400">Total Inventory</div>
                        <div className="text-2xl font-bold">12,458</div>
                        <div className="text-green-400 text-sm">+5.2%</div>
                      </div>
                      <div className="bg-white/5 rounded-xl p-4">
                        <div className="text-sm text-gray-400">Active Orders</div>
                        <div className="text-2xl font-bold">847</div>
                        <div className="text-blue-400 text-sm">+12%</div>
                      </div>
                    </div>

                    <div className="space-y-3">
                      <div className="flex justify-between text-sm">
                        <span>Quality Checks</span>
                        <span>98%</span>
                      </div>
                      <div className="w-full bg-gray-700 rounded-full h-2">
                        <div className="bg-green-500 h-2 rounded-full w-[98%]" />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="relative z-20 py-24 bg-white/5 backdrop-blur-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16">
            <h2 className="text-4xl font-bold mb-4">Everything You Need</h2>
            <p className="text-xl text-gray-300 max-w-3xl mx-auto">
              Comprehensive tools designed specifically for wine businesses
            </p>
          </div>

          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
            {features.map((feature, index) => (
              <div
                key={feature.title}
                className={`bg-white/10 backdrop-blur-lg rounded-2xl p-6 border border-white/20 hover:bg-white/15 transition-all duration-300 transform hover:scale-105 ${
                  isVisible ? 'animate-fade-in-up' : 'opacity-0'
                }`}
                style={{ animationDelay: `${index * 100}ms` }}
              >
                <feature.icon className="h-12 w-12 text-blue-400 mb-4" />
                <h3 className="text-xl font-semibold mb-2">{feature.title}</h3>
                <p className="text-gray-300">{feature.description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Testimonials Section */}
      <section className="relative z-20 py-24">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16">
            <h2 className="text-4xl font-bold mb-4">Trusted by Wine Professionals</h2>
            <p className="text-xl text-gray-300">See what our customers have to say</p>
          </div>

          <div className="grid md:grid-cols-3 gap-8">
            {testimonials.map((testimonial, index) => (
              <div
                key={testimonial.name}
                className={`bg-white/10 backdrop-blur-lg rounded-2xl p-6 border border-white/20 ${
                  isVisible ? 'animate-fade-in-up' : 'opacity-0'
                }`}
                style={{ animationDelay: `${index * 200}ms` }}
              >
                <div className="flex mb-4">
                  {[...Array(testimonial.rating)].map((_, i) => (
                    <Star key={i} className="h-5 w-5 text-yellow-400 fill-current" />
                  ))}
                </div>
                <p className="text-gray-300 mb-4">"{testimonial.content}"</p>
                <div>
                  <div className="font-semibold">{testimonial.name}</div>
                  <div className="text-sm text-gray-400">{testimonial.role}</div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="relative z-20 py-24 bg-gradient-to-r from-blue-600/20 to-purple-600/20">
        <div className="max-w-4xl mx-auto text-center px-4 sm:px-6 lg:px-8">
          <h2 className="text-4xl font-bold mb-6">Ready to Transform Your Business?</h2>
          <p className="text-xl text-gray-300 mb-8">
            Join thousands of wine professionals who trust ViniTrackPro
          </p>
          <Link to="/signup">
            <Button 
              size="lg" 
              icon={ArrowRight}
              className="bg-blue-600 hover:bg-blue-700 text-white px-12 py-4 text-lg font-semibold shadow-xl hover:shadow-2xl transition-all duration-300 transform hover:scale-105"
            >
              Start Your Free Trial
            </Button>
          </Link>
        </div>
      </section>

      <style jsx>{`
        @keyframes fade-in-up {
          from {
            opacity: 0;
            transform: translateY(30px);
          }
          to {
            opacity: 1;
            transform: translateY(0);
          }
        }

        @keyframes fade-in-right {
          from {
            opacity: 0;
            transform: translateX(30px);
          }
          to {
            opacity: 1;
            transform: translateX(0);
          }
        }

        .animate-fade-in-up {
          animation: fade-in-up 0.8s ease-out forwards;
        }

        .animate-fade-in-right {
          animation: fade-in-right 0.8s ease-out forwards;
        }
      `}</style>
    </div>
  );
};