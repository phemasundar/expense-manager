import React from 'react';
import { SignedIn, SignedOut, SignInButton, UserButton } from '@clerk/clerk-react';

const Header: React.FC = () => {
  return (
    <header>
      <h1>Personal Expense Manager</h1>
      <SignedOut>
        <SignInButton />
      </SignedOut>
      <SignedIn>
        <a href="/dashboard">Dashboard</a>
        <a href="/upload">Upload Receipt</a>
        <UserButton />
      </SignedIn>
    </header>
  );
};

export default Header;
