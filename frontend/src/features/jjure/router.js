import JjureListPage from '@/features/jjure/views/JjureListPage.vue';
import JjureDetailPage from '@/features/jjure/views/JjureDetailPage.vue';

export const JjureRoutes = [
  {
    path: '/jjure',
    component: JjureListPage,
    children: [
      {
        path: ':id',
        component: JjureDetailPage,
        props: true,
      },
    ],
  },
];
