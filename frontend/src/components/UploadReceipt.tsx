import React, { useState } from 'react';
import { useAuth } from '@clerk/clerk-react';

const UploadReceipt: React.FC = () => {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [preview, setPreview] = useState<string | null>(null);
  const [extractedText, setExtractedText] = useState<string | null>(null);
  const { getToken } = useAuth();

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (file) {
      setSelectedFile(file);
      setPreview(URL.createObjectURL(file));
      setExtractedText(null);
    }
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    if (!selectedFile) {
      alert('Please select a file first.');
      return;
    }

    const formData = new FormData();
    formData.append('file', selectedFile);

    try {
      const token = await getToken();
      const response = await fetch('/api/v1/receipts/upload', {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      });

      if (response.ok) {
        const text = await response.text();
        setExtractedText(text);
      } else {
        alert('Upload failed. Please try again.');
      }
    } catch (error) {
      console.error('Error uploading file:', error);
      alert('An error occurred during upload.');
    }
  };

  return (
    <div>
      <h2>Upload Receipt</h2>
      <form onSubmit={handleSubmit}>
        <input type="file" accept="image/*" onChange={handleFileChange} />
        <button type="submit">Upload</button>
      </form>
      {preview && (
        <div>
          <h3>Image Preview:</h3>
          <img src={preview} alt="Selected receipt" style={{ maxWidth: '500px', maxHeight: '500px' }} />
        </div>
      )}
      {extractedText && (
        <div>
          <h3>Extracted Text:</h3>
          <pre>{extractedText}</pre>
        </div>
      )}
    </div>
  );
};

export default UploadReceipt;
