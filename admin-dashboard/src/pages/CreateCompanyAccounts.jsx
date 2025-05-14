import React, { useState } from 'react';
import api from '../api/api';

export default function CreateCompanyAccounts() {
  const [names, setNames] = useState(['']);

  const handleChange = (index, value) => {
    const newNames = [...names];
    newNames[index] = value;
    setNames(newNames);
  };

  const handleAdd = () => setNames([...names, '']);
  const handleSubmit = async () => {
    try {
      await api.post('/create-company-accounts', {
        companyNames: names.filter(name => name.trim())
      });
      alert('Companies created successfully!');
    } catch (err) {
      console.error(err);
      alert('Error creating companies');
    }
  };

  return (
    <div className="space-y-4">
      <h2 className="text-2xl font-bold">Create Company Accounts</h2>
      {names.map((name, idx) => (
        <input
          key={idx}
          value={name}
          onChange={e => handleChange(idx, e.target.value)}
          className="w-full p-2 border rounded shadow"
          placeholder={`Company ${idx + 1}`}
        />
      ))}
      <button onClick={handleAdd} className="bg-green-500 text-white px-4 py-2 rounded">Add Company</button>
      <button onClick={handleSubmit} className="bg-blue-600 text-white px-4 py-2 rounded">Submit</button>
    </div>
  );
}