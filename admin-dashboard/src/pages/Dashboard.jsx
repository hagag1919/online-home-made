import React from 'react';

export default function Dashboard() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-100 via-blue-50 to-indigo-100 py-10 px-4">
      <h2 className="text-4xl font-extrabold text-gray-800 mb-8 tracking-tight">Admin Dashboard</h2>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
        <div className="bg-white rounded-2xl shadow-xl p-8 flex flex-col items-center border-t-4 border-blue-500 hover:scale-105 transition-transform">
          <div className="bg-blue-100 text-blue-600 rounded-full p-4 mb-4">
            <svg className="w-8 h-8" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
              <path d="M17 20h5v-2a4 4 0 00-3-3.87M9 20H4v-2a4 4 0 013-3.87m9-4a4 4 0 10-8 0 4 4 0 008 0zm6 4v6M3 16v6" />
            </svg>
          </div>
          <h3 className="text-lg font-semibold text-gray-700">Users</h3>
          <p className="text-3xl font-bold text-blue-700 mt-2">123</p>
        </div>
        <div className="bg-white rounded-2xl shadow-xl p-8 flex flex-col items-center border-t-4 border-red-500 hover:scale-105 transition-transform">
          <div className="bg-red-100 text-red-600 rounded-full p-4 mb-4">
            <svg className="w-8 h-8" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
              <path d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <h3 className="text-lg font-semibold text-gray-700">Failures</h3>
          <p className="text-3xl font-bold text-red-700 mt-2">5</p>
        </div>
        <div className="bg-white rounded-2xl shadow-xl p-8 flex flex-col items-center border-t-4 border-green-500 hover:scale-105 transition-transform">
          <div className="bg-green-100 text-green-600 rounded-full p-4 mb-4">
            <svg className="w-8 h-8" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
              <path d="M16 7a4 4 0 01.88 7.9M8 7a4 4 0 00-.88 7.9M12 17v4m0 0h-4m4 0h4" />
            </svg>
          </div>
          <h3 className="text-lg font-semibold text-gray-700">Companies</h3>
          <p className="text-3xl font-bold text-green-700 mt-2">8</p>
        </div>
      </div>
    </div>
  );
}