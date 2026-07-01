import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/LoginView.vue'),
    meta: { title: '用户登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/register/RegisterView.vue'),
    meta: { title: '用户注册' }
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('@/views/home/HomeView.vue'),
    meta: { title: '首页', requiresAuth: true },
    children: [
      {
        path: '',
        redirect: '/home/dashboard'
      },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/home/DashboardView.vue'),
        meta: { title: '首页' }
      }
    ]
  },
  {
    path: '/tokens',
    name: 'TokenList',
    component: () => import('@/views/home/HomeView.vue'),
    meta: { title: 'Token市场', requiresAuth: true },
    children: [
      {
        path: '',
        name: 'TokenListView',
        component: () => import('@/views/token/TokenListView.vue'),
        meta: { title: 'Token市场' }
      }
    ]
  },
  {
    path: '/tokens/:id',
    name: 'TokenDetail',
    component: () => import('@/views/home/HomeView.vue'),
    meta: { title: 'Token详情', requiresAuth: true },
    children: [
      {
        path: '',
        component: () => import('@/views/token/TokenDetailView.vue'),
        meta: { title: 'Token详情' }
      }
    ]
  },
  {
    path: '/tokens/create',
    name: 'TokenCreate',
    component: () => import('@/views/home/HomeView.vue'),
    meta: { title: '发布Token', requiresAuth: true },
    children: [
      {
        path: '',
        component: () => import('@/views/token/TokenCreateView.vue'),
        meta: { title: '发布Token' }
      }
    ]
  },
  {
    path: '/orders',
    name: 'OrderList',
    component: () => import('@/views/home/HomeView.vue'),
    meta: { title: '订单列表', requiresAuth: true },
    children: [
      {
        path: '',
        component: () => import('@/views/order/OrderListView.vue'),
        meta: { title: '订单列表' }
      }
    ]
  },
  {
    path: '/orders/:id',
    name: 'OrderDetail',
    component: () => import('@/views/home/HomeView.vue'),
    meta: { title: '订单详情', requiresAuth: true },
    children: [
      {
        path: '',
        component: () => import('@/views/order/OrderDetailView.vue'),
        meta: { title: '订单详情' }
      }
    ]
  },
  {
    path: '/wallet',
    name: 'Wallet',
    component: () => import('@/views/home/HomeView.vue'),
    meta: { title: '我的钱包', requiresAuth: true },
    children: [
      {
        path: '',
        component: () => import('@/views/wallet/WalletView.vue'),
        meta: { title: '我的钱包' }
      }
    ]
  },
  {
    path: '/favorites',
    name: 'Favorites',
    component: () => import('@/views/home/HomeView.vue'),
    meta: { title: '我的收藏', requiresAuth: true },
    children: [
      {
        path: '',
        component: () => import('@/views/favorite/FavoriteView.vue'),
        meta: { title: '我的收藏' }
      }
    ]
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/home/HomeView.vue'),
    meta: { title: '个人中心', requiresAuth: true },
    children: [
      {
        path: '',
        component: () => import('@/views/profile/ProfileView.vue'),
        meta: { title: '个人中心' }
      }
    ]
  },
  {
    path: '/stats',
    name: 'Stats',
    component: () => import('@/views/home/HomeView.vue'),
    meta: { title: '数据看板', requiresAuth: true },
    children: [
      {
        path: '',
        component: () => import('@/views/stats/StatsView.vue'),
        meta: { title: '数据看板' }
      }
    ]
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('@/views/home/HomeView.vue'),
    meta: { title: '管理后台', requiresAuth: true, requiresAdmin: true },
    children: [
      {
        path: '',
        component: () => import('@/views/admin/AdminView.vue'),
        meta: { title: '管理后台' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.meta.requiresAdmin && token) {
    next()
  } else {
    next()
  }
})

export default router
