import { Plant, Season } from '@/types/plant';

// 계절에 따른 물주기 간격 계산
export const getIntervalForSeason = (plant: Plant, currentSeason: Season) => {
  switch (currentSeason) {
    case 'SUMMER':
      return plant.summerInterval ?? plant.commonInterval ?? 7; // 기본값 7일
    case 'WINTER':
      return plant.winterInterval ?? plant.commonInterval ?? 7; // 기본값 7일
    case 'COMMON':
    default:
      return plant.commonInterval ?? 7; // 기본값 7일
  }
};

// 다음 물주기 날짜 계산 (계절 반영)
export const calculateNextWateringDate = (plant: Plant, currentSeason: Season) => {
  if (!plant.lastWateringDate) return null;
  
  const interval = getIntervalForSeason(plant, currentSeason);
  const lastWatering = new Date(plant.lastWateringDate);
  const nextWatering = new Date(lastWatering.getTime() + interval * 24 * 60 * 60 * 1000);
  
  return nextWatering.toISOString().split('T')[0];
};

// 물주기 필요 여부 계산 (계절 반영)
export const isWateringRequired = (plant: Plant, currentSeason: Season) => {
  const nextWateringDate = calculateNextWateringDate(plant, currentSeason);
  if (!nextWateringDate) return false;
  
  const today = new Date().toISOString().split('T')[0];
  return nextWateringDate <= today;
};

// 물주기 지연 일수 계산
export const getWateringDelayDays = (plant: Plant, currentSeason: Season) => {
  const nextWateringDate = calculateNextWateringDate(plant, currentSeason);
  if (!nextWateringDate) return 0;
  
  const today = new Date();
  const nextWatering = new Date(nextWateringDate);
  const diffTime = today.getTime() - nextWatering.getTime();
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  
  return Math.max(0, diffDays);
};