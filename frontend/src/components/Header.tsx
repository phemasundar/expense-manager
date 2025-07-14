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
        <UserButton />
      </SignedIn>
    </header>
  );
};

export default Header;
