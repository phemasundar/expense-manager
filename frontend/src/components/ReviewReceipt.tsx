import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useAuth } from '@clerk/clerk-react';

const ReviewReceipt: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [receipt, setReceipt] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const { getToken } = useAuth();

  useEffect(() => {
    // Fetch receipt data from the backend
    const fetchReceipt = async () => {
      try {
        const token = await getToken();
        const response = await fetch(`/api/v1/receipts/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        const data = await response.json();
        setReceipt(data);
      } catch (error) {
        console.error('Error fetching receipt:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchReceipt();
  }, [id, getToken]);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!receipt) {
    return <div>Receipt not found</div>;
  }

  return (
    <div>
      <h2>Review Receipt</h2>
      <p>Extracted Text:</p>
      <pre>{receipt.extractedText}</pre>
    </div>
  );
};

export default ReviewReceipt;
