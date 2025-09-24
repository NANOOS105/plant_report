'use client';

import { useSeason } from '@/contexts/SeasonContext';
import { Season } from '@/types/plant';

const seasonLabels: Record<Season, string> = {
  COMMON: '공통',
  SUMMER: '여름',
  WINTER: '겨울',
};

export default function SeasonSelector() {
  const { currentSeason, setCurrentSeason } = useSeason();

  return (
    <div className="flex items-center gap-2">
      <span className="text-sm text-gray-600">계절:</span>
      <select
        value={currentSeason}
        onChange={(e) => setCurrentSeason(e.target.value as Season)}
        className="px-3 py-1 border rounded text-sm bg-white text-gray-900"
      >
        {Object.entries(seasonLabels).map(([value, label]) => (
          <option key={value} value={value}>
            {label}
          </option>
        ))}
      </select>
    </div>
  );
}
