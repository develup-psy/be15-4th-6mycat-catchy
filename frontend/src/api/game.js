import api from '@/api/axios.js';

/* 1. 게임 베스트 기록 저장 */
export function saveGameScore(score) {
  return api.post('/games/scores', { score });
}

/* 2. 게임 랭킹 조회 */
export function getGameRanking(memberId, limit = 10) {
  return api.get('/games/scores/ranking', {
    params: { memberId, limit },
  });
}
