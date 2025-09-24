'use client';

import { createContext, useContext, useState, ReactNode } from 'react';
import { Season } from '@/types/plant';

interface SeasonContextType {
  currentSeason: Season;
  setCurrentSeason: (season: Season) => void;
}

const SeasonContext = createContext<SeasonContextType | undefined>(undefined);

export function SeasonProvider({ children }: { children: ReactNode }) {
  const [currentSeason, setCurrentSeason] = useState<Season>('COMMON');

  return (
    <SeasonContext.Provider value={{ currentSeason, setCurrentSeason }}>
      {children}
    </SeasonContext.Provider>
  );
}

export function useSeason() {
  const context = useContext(SeasonContext);
  if (context === undefined) {
    throw new Error('useSeason must be used within a SeasonProvider');
  }
  return context;
}
