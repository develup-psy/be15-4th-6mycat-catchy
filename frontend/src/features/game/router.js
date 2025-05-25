export default [
  {
    path: '/game',
    children: [
      { path: '', redirect: '/game/start' }, // 기본 리다이렉트,
      {
        path: 'start',
        name: 'GameStart',
        component: () => import('@/features/game/views/GameStart.vue'),
        meta: { requiresAuth: true, title: '게임 시작' },
      },
      {
        path: 'play',
        name: 'GamePlay',
        component: () => import('@/features/game/views/GamePlay.vue'),
        meta: { requiresAuth: true, title: '게임 중' },
      },
      {
        path: 'ranking',
        name: 'GameRanking',
        component: () => import('@/features/game/views/GameRanking.vue'),
        meta: { requiresAuth: false, title: '랭킹 조회' },
      },
    ],
  },
];
