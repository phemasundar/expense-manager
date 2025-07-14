import React, { useEffect } from 'react';
import { SignIn, useAuth } from '@clerk/clerk-react';
import { useNavigate } from 'react-router-dom';

const SignInPage: React.FC = () => {
  const navigate = useNavigate();
  const { isSignedIn } = useAuth();

  useEffect(() => {
    if (isSignedIn) {
      navigate('/dashboard');
    }
  }, [isSignedIn, navigate]);

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '80vh' }}>
      <SignIn path="/sign-in" routing="path" signUpUrl="/sign-up" />
    </div>
  );
};

export default SignInPage;
