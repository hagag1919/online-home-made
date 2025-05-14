import React, { useState } from 'react';
import api from '../api/api';

export default function ListDishes() {
  const [companyIds, setCompanyIds] = useState('');
  const [dishes, setDishes] = useState([]);

  const fetchDishes = async () => {
    try {
      const ids = companyIds.split(',').map(id => parseInt(id.trim())).filter(id => !isNaN(id));
      const res = await api.post('/list-dishes', ids);
      setDishes(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="space-y-4">
      <h2 className="text-2xl font-bold">List Dishes by Company IDs</h2>
      <input
        type="text"
        value={companyIds}
        onChange={e => setCompanyIds(e.target.value)}
        className="w-full p-2 border rounded shadow"
        placeholder="Enter company IDs (e.g. 1,2,3)"
      />
      <button onClick={fetchDishes} className="bg-indigo-500 text-white px-4 py-2 rounded">Fetch Dishes</button>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {dishes.map((dish, idx) => (
          <div key={idx} className="bg-white p-4 border rounded shadow">
            <h4 className="text-lg font-bold">{dish.name}</h4>
            <p className="text-sm text-gray-600">{dish.description}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
