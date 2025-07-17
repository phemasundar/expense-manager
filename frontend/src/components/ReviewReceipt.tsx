import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

const ReviewReceipt = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const { parsedReceipt } = location.state || {};

    const [storeName, setStoreName] = useState(parsedReceipt?.storeName || '');
    const [purchaseDate, setPurchaseDate] = useState(parsedReceipt?.purchaseDate || '');
    const [items, setItems] = useState(parsedReceipt?.items || []);

    const handleItemChange = (index, field, value) => {
        const newItems = [...items];
        newItems[index][field] = value;
        setItems(newItems);
    };

    const handleSave = async () => {
        const receiptData = { storeName, purchaseDate, items };

        try {
            const response = await fetch('/api/v1/receipts', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    // Include auth token if needed
                },
                body: JSON.stringify(receiptData),
            });

            if (response.ok) {
                const result = await response.json();
                console.log('Receipt saved:', result);
                navigate(`/receipts/${result.receipt_id}`);
            } else {
                console.error('Failed to save receipt');
            }
        } catch (error) {
            console.error('Error saving receipt:', error);
        }
    };

    if (!parsedReceipt) {
        return <div>No receipt data to review. Please upload a receipt first.</div>;
    }

    return (
        <div className="container mx-auto p-4">
            <h2 className="text-2xl font-bold mb-4">Review Receipt</h2>
            <div className="mb-4">
                <label className="block text-gray-700">Store Name</label>
                <input
                    type="text"
                    value={storeName}
                    onChange={(e) => setStoreName(e.target.value)}
                    className="w-full p-2 border rounded"
                />
            </div>
            <div className="mb-4">
                <label className="block text-gray-700">Purchase Date</label>
                <input
                    type="date"
                    value={purchaseDate}
                    onChange={(e) => setPurchaseDate(e.target.value)}
                    className="w-full p-2 border rounded"
                />
            </div>

            <h3 className="text-xl font-bold mb-2">Items</h3>
            <table className="w-full table-auto">
                <thead>
                    <tr>
                        <th className="px-4 py-2">Product Name</th>
                        <th className="px-4 py-2">Price</th>
                    </tr>
                </thead>
                <tbody>
                    {items.map((item, index) => (
                        <tr key={index}>
                            <td className="border px-4 py-2">
                                <input
                                    type="text"
                                    value={item.productName}
                                    onChange={(e) => handleItemChange(index, 'productName', e.target.value)}
                                    className="w-full p-1 border rounded"
                                />
                            </td>
                            <td className="border px-4 py-2">
                                <input
                                    type="number"
                                    value={item.price}
                                    onChange={(e) => handleItemChange(index, 'price', e.target.value)}
                                    className="w-full p-1 border rounded"
                                />
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            <button
                onClick={handleSave}
                className="mt-4 bg-blue-500 text-white p-2 rounded"
            >
                Save Receipt
            </button>
        </div>
    );
};

export default ReviewReceipt;