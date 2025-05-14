import { Routes, Route } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import UsersPage from './pages/UsersPage';
import PaymentFailures from './pages/PaymentFailures';
import ServiceLogs from './pages/ServiceLogs';
import CreateCompanyAccounts from './pages/CreateCompanyAccounts';
import ListDishes from './pages/ListDishes';

export default function App() {
  return (
    <div className="flex min-h-screen bg-gray-100 text-gray-900">
      <Sidebar />
      <div className="flex-1 p-6 overflow-auto">
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/users" element={<UsersPage />} />
          <Route path="/failures" element={<PaymentFailures />} />
          <Route path="/logs" element={<ServiceLogs />} />
          <Route path="/company" element={<CreateCompanyAccounts />} />
          <Route path="/dishes" element={<ListDishes />} />
        </Routes>
      </div>
    </div>
  );
}
