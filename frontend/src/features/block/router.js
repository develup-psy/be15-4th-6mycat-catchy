export default [
  {
    path: '/profile/block',
    name: 'BlockedList',
    component: () => import('./views/BlockedListView.vue'),
    meta: { requiresAuth: true },
  },
];
