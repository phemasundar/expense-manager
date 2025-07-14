import React from 'react';
import { SignedIn, SignedOut } from '@clerk/clerk-react';
import Header from './components/Header';
import Footer from './components/Footer';
import HealthCheck from './HealthCheck';

const App: React.FC = () => {
  return (
    <div>
      <Header />
      <main>
        <SignedIn>
          <HealthCheck />
        </SignedIn>
        <SignedOut>
          <p>Please sign in to see the health check.</p>
        </SignedOut>
      </main>
      <Footer />
    </div>
  );
};

export default App;
