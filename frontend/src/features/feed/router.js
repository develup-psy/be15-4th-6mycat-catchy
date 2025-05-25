import FeedListPage from '@/features/feed/views/FeedListPage.vue';
import FeedDetailPage from '@/features/feed/views/FeedDetailPage.vue';

export const FeedRoutes = [
  {
    path: '/feed',
    component: FeedListPage,
    children: [
      {
        path: ':id',
        component: FeedDetailPage,
        props: true,
      },
    ],
  },
];
