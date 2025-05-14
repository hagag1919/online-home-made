import { Link, useLocation } from 'react-router-dom';
const links = [
  { path: '/', label: 'Dashboard' },
  { path: '/users', label: 'Users' },
  { path: '/failures', label: 'Payment Failures' },
  { path: '/logs', label: 'Service Logs' },
  { path: '/company', label: 'Add Companies' },
  { path: '/dishes', label: 'List Dishes' },
];

export default function Sidebar() {
  const { pathname } = useLocation();
  return (
    <aside className="w-64 bg-white shadow-md p-4 fixed h-full">
      <h1 className="text-2xl font-bold text-blue-600 mb-6">Admin Panel</h1>
      <nav className="space-y-3">
        {links.map(link => (
          <Link
            key={link.path}
            to={link.path}
            className={`block py-2 px-4 rounded hover:bg-blue-100 transition ${
              pathname === link.path ? 'bg-blue-200 text-blue-900 font-semibold' : ''
            }`}
          >
            {link.label}
          </Link>
        ))}
      </nav>
    </aside>
  );
}
