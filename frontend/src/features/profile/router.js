import FeedDetailPage from '@/features/feed/views/FeedDetailPage.vue';

export default [
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('./views/ProfileView.vue'),
    meta: { requiresAuth: true }, // 로그인된 사용자만 접근 가능
    children: [
      {
        path: 'feed/:id',
        component: FeedDetailPage,
        props: true,
      },
    ],
  },
  {
    path: '/profile/edit',
    name: 'ProfileEdit',
    component: () => import('./views/ProfileEditView.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/members/:mid',
    name: 'MemberProfile',
    component: () => import('./views/MemberProfileView.vue'),
    children: [
      {
        path: 'feed/:id',
        component: FeedDetailPage,
        props: true,
      },
    ],
  },
];
