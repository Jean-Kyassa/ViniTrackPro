import React, { useState, useEffect, useRef } from 'react';
import { Send, MessageCircle, User, Bot, Clock } from 'lucide-react';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Card } from '../../components/ui/Card';
import { useAuth } from '../../context/AuthContext';

interface Message {
  id: string;
  content: string;
  sender: 'user' | 'logistics' | 'admin';
  timestamp: Date;
  senderName: string;
}

export const Chatbot: React.FC = () => {
  const { user, hasRole } = useAuth();
  const [messages, setMessages] = useState<Message[]>([]);
  const [newMessage, setNewMessage] = useState('');
  const [isTyping, setIsTyping] = useState(false);
  const messagesEndRef = useRef<HTMLDivElement>(null);

  // Mock initial messages
  useEffect(() => {
    const initialMessages: Message[] = [
      {
        id: '1',
        content: 'Welcome to ViniTrackPro Support Chat! How can we help you today?',
        sender: 'logistics',
        timestamp: new Date(Date.now() - 300000),
        senderName: 'Logistics Team'
      }
    ];
    setMessages(initialMessages);
  }, []);

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  const handleSendMessage = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newMessage.trim()) return;

    const userMessage: Message = {
      id: Date.now().toString(),
      content: newMessage,
      sender: hasRole('ROLE_ADMIN') ? 'admin' : hasRole('ROLE_LOGISTICS') ? 'logistics' : 'user',
      timestamp: new Date(),
      senderName: user?.username || 'Unknown User'
    };

    setMessages(prev => [...prev, userMessage]);
    setNewMessage('');
    setIsTyping(true);

    // Simulate response delay
    setTimeout(() => {
      const responses = [
        "Thank you for your message. A logistics team member will respond shortly.",
        "I've received your request. Let me check on that for you.",
        "Your inquiry has been logged. We'll get back to you within 24 hours.",
        "I understand your concern. Let me escalate this to the appropriate team.",
        "Thanks for reaching out! Is there anything specific I can help you with?"
      ];

      const responseMessage: Message = {
        id: (Date.now() + 1).toString(),
        content: responses[Math.floor(Math.random() * responses.length)],
        sender: hasRole('ROLE_USER') ? 'logistics' : 'admin',
        timestamp: new Date(),
        senderName: hasRole('ROLE_USER') ? 'Logistics Support' : 'Admin Support'
      };

      setMessages(prev => [...prev, responseMessage]);
      setIsTyping(false);
    }, 1500);
  };

  const getMessageIcon = (sender: string) => {
    switch (sender) {
      case 'admin':
        return <Bot className="h-4 w-4" />;
      case 'logistics':
        return <MessageCircle className="h-4 w-4" />;
      default:
        return <User className="h-4 w-4" />;
    }
  };

  const getMessageColor = (sender: string) => {
    switch (sender) {
      case 'admin':
        return 'bg-purple-100 text-purple-800 dark:bg-purple-900 dark:text-purple-200';
      case 'logistics':
        return 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200';
      default:
        return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200';
    }
  };

  const canAccessChat = hasRole('ROLE_ADMIN') || hasRole('ROLE_LOGISTICS') || hasRole('ROLE_USER');

  if (!canAccessChat) {
    return (
      <div className="flex items-center justify-center h-64">
        <Card className="p-8 text-center">
          <MessageCircle className="h-12 w-12 text-gray-400 mx-auto mb-4" />
          <h2 className="text-xl font-semibold text-gray-900 dark:text-white mb-2">
            Access Denied
          </h2>
          <p className="text-gray-600 dark:text-gray-400">
            You don't have permission to access the chat system.
          </p>
        </Card>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Support Chat</h1>
          <p className="text-gray-600 dark:text-gray-400">
            {hasRole('ROLE_USER') 
              ? 'Chat with our logistics team for support'
              : hasRole('ROLE_LOGISTICS')
              ? 'Support chat - Help users and escalate to admin when needed'
              : 'Admin chat - Monitor all conversations and provide support'
            }
          </p>
        </div>
        <div className="flex items-center space-x-2 text-sm text-gray-500">
          <div className="w-2 h-2 bg-green-500 rounded-full"></div>
          <span>Online</span>
        </div>
      </div>

      <Card className="h-[600px] flex flex-col">
        {/* Chat Header */}
        <div className="p-4 border-b border-gray-200 dark:border-gray-700">
          <div className="flex items-center space-x-3">
            <div className="w-10 h-10 bg-blue-500 rounded-full flex items-center justify-center">
              <MessageCircle className="h-5 w-5 text-white" />
            </div>
            <div>
              <h3 className="font-semibold text-gray-900 dark:text-white">
                ViniTrackPro Support
              </h3>
              <p className="text-sm text-gray-500">
                {hasRole('ROLE_ADMIN') ? 'Admin Channel' : 'Logistics Support'}
              </p>
            </div>
          </div>
        </div>

        {/* Messages Area */}
        <div className="flex-1 overflow-y-auto p-4 space-y-4">
          {messages.map((message) => (
            <div
              key={message.id}
              className={`flex ${
                message.sender === (hasRole('ROLE_ADMIN') ? 'admin' : hasRole('ROLE_LOGISTICS') ? 'logistics' : 'user')
                  ? 'justify-end' 
                  : 'justify-start'
              }`}
            >
              <div className={`max-w-xs lg:max-w-md px-4 py-2 rounded-lg ${
                message.sender === (hasRole('ROLE_ADMIN') ? 'admin' : hasRole('ROLE_LOGISTICS') ? 'logistics' : 'user')
                  ? 'bg-blue-500 text-white' 
                  : 'bg-gray-100 dark:bg-gray-700 text-gray-900 dark:text-white'
              }`}>
                <div className="flex items-center space-x-2 mb-1">
                  {getMessageIcon(message.sender)}
                  <span className="text-xs font-medium">{message.senderName}</span>
                  <span className={`inline-flex items-center px-1.5 py-0.5 rounded-full text-xs font-medium ${getMessageColor(message.sender)}`}>
                    {message.sender.toUpperCase()}
                  </span>
                </div>
                <p className="text-sm">{message.content}</p>
                <div className="flex items-center space-x-1 mt-1">
                  <Clock className="h-3 w-3 opacity-70" />
                  <span className="text-xs opacity-70">
                    {message.timestamp.toLocaleTimeString()}
                  </span>
                </div>
              </div>
            </div>
          ))}

          {isTyping && (
            <div className="flex justify-start">
              <div className="bg-gray-100 dark:bg-gray-700 px-4 py-2 rounded-lg">
                <div className="flex space-x-1">
                  <div className="w-2 h-2 bg-gray-400 rounded-full animate-bounce"></div>
                  <div className="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style={{ animationDelay: '0.1s' }}></div>
                  <div className="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style={{ animationDelay: '0.2s' }}></div>
                </div>
              </div>
            </div>
          )}
          <div ref={messagesEndRef} />
        </div>

        {/* Message Input */}
        <div className="p-4 border-t border-gray-200 dark:border-gray-700">
          <form onSubmit={handleSendMessage} className="flex space-x-2">
            <Input
              value={newMessage}
              onChange={(e) => setNewMessage(e.target.value)}
              placeholder="Type your message..."
              className="flex-1"
              disabled={isTyping}
            />
            <Button
              type="submit"
              disabled={!newMessage.trim() || isTyping}
              icon={Send}
            >
              Send
            </Button>
          </form>
        </div>
      </Card>

      {/* Chat Guidelines */}
      <Card className="p-4">
        <h3 className="font-semibold text-gray-900 dark:text-white mb-2">Chat Guidelines</h3>
        <div className="text-sm text-gray-600 dark:text-gray-400 space-y-1">
          {hasRole('ROLE_USER') && (
            <>
              <p>• Use this chat to get help with orders, inventory, and system issues</p>
              <p>• Logistics team will respond during business hours (9 AM - 6 PM)</p>
              <p>• For urgent issues, please contact your logistics coordinator directly</p>
            </>
          )}
          {hasRole('ROLE_LOGISTICS') && (
            <>
              <p>• Respond to user inquiries promptly and professionally</p>
              <p>• Escalate complex issues to admin when necessary</p>
              <p>• Use @admin to notify administrators in urgent cases</p>
            </>
          )}
          {hasRole('ROLE_ADMIN') && (
            <>
              <p>• Monitor all chat conversations for quality assurance</p>
              <p>• Step in when logistics team needs assistance</p>
              <p>• Handle escalated issues and system-wide problems</p>
            </>
          )}
        </div>
      </Card>
    </div>
  );
};