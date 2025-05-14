import React, { useEffect, useState } from 'react';
import api from '../api/api';

export default function PaymentFailures() {
  const [failures, setFailures] = useState([]);

  useEffect(() => {
    api.get('/payment-failures').then(res => setFailures(res.data)).catch(console.error);
  }, []);

  return (
    <div>
      <h2 className="text-2xl font-bold mb-4">Payment Failures</h2>
      <div className="overflow-auto rounded-lg shadow">
        <table className="w-full table-auto">
          <thead className="bg-red-100">
            <tr>
              <th className="p-2">User</th>
              <th className="p-2">Reason</th>
              <th className="p-2">Date</th>
            </tr>
          </thead>
          <tbody>
            {failures.map((f, i) => (
              <tr key={i} className="border-t">
                <td className="p-2">{f.user}</td>
                <td className="p-2">{f.reason}</td>
                <td className="p-2">{f.date}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}