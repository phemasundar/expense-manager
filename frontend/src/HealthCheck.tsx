import React, { useState, useEffect } from 'react';

const HealthCheck: React.FC = () => {
  const [status, setStatus] = useState<string>('Loading...');

  useEffect(() => {
    fetch('/api/health')
      .then((response) => response.json())
      .then((data) => setStatus(data.status))
      .catch(() => setStatus('Error'));
  }, []);

  return (
    <div>
      <h2>Backend Health Check</h2>
      <p>Status: {status}</p>
    </div>
  );
};

export default HealthCheck;
