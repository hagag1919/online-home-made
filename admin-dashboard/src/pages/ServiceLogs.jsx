import React, { useEffect, useState } from 'react';
import api from '../api/api';

export default function ServiceLogs() {
  const [logs, setLogs] = useState([]);

  useEffect(() => {
    api.get('/service-logs').then(res => setLogs(res.data)).catch(console.error);
  }, []);

  return (
    <div>
      <h2 className="text-2xl font-bold mb-4">Service Logs</h2>
      <div className="bg-gray-100 p-4 rounded shadow space-y-2 h-[60vh] overflow-y-scroll">
        {logs.map((log, idx) => (
          <div key={idx} className="bg-white p-3 rounded shadow border border-gray-200">
            <pre className="text-sm whitespace-pre-wrap">{log}</pre>
          </div>
        ))}
      </div>
    </div>
  );
}