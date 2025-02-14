import {createRouter, createWebHistory} from 'vue-router'


const routes = [
    {
        path: '/backend',
        name: 'backend-home',
        meta: {name: '首页', roles: ['0', '1'], icon: 'House', hidden: false},
        redirect: '/dashboard',
        component: () => import('@/views/backend/index.vue'),
        children: [
            {
                path: '/dashboard',
                name: 'dashboard',
                meta: {name: '仪表盘', roles: ['0', '1'], icon: 'Odometer', hidden: false},
                component: () => import('@/views/backend/dashboard/index.vue')
            },
            {
                path: '/blog',
                name: 'backend-blog',
                meta: {name: '博客', roles: ['0', '1'], icon: 'Stopwatch', hidden: false},
                component: () => import('@/views/backend/blog/index.vue')
            },
            {
                path: '/tag',
                name: 'tag',
                meta: {name: '标签', roles: ['0', '1'], icon: 'PriceTag', hidden: false},
                component: () => import('@/views/backend/tag/index.vue')
            },
            {
                path: '/blogTag',
                name: 'blogTag',
                meta: {name: '博客标签关系', roles: ['0', '1'], icon: 'Stopwatch', hidden: false},
                component: () => import('@/views/backend/blogTag/index.vue')
            },
            {
                path: '/chat',
                name: 'chat',
                meta: {name: '聊天', roles: ['0', '1'], icon: 'ChatLineRound', hidden: false},
                component: () => import('@/views/backend/chat/index.vue')
            },
            {
                path: '/comment',
                name: 'comment',
                meta: {name: '评论', roles: ['0', '1'], icon: 'ChatDotSquare', hidden: false},
                component: () => import('@/views/backend/comment/index.vue')
            },
            {
                path: '/follow',
                name: 'follow',
                meta: {name: '关注信息', roles: ['0'], icon: 'Stopwatch', hidden: false},
                component: () => import('@/views/backend/follow/index.vue')
            },
            {
                path: '/user',
                name: 'user',
                meta: {name: '用户信息', roles: ['0', '1'], icon: 'User', hidden: false},
                component: () => import('@/views/backend/user/index.vue')
            },
            {
                path: '/center',
                name: 'center',
                meta: {name: '个人中心', roles: [], icon: 'Stopwatch', hidden: false},
                component: () => import('@/views/backend/user/center/index.vue')
            },
            {
                path: '/statistics',
                name: 'statistics',
                meta: {name: '统计信息', roles: ['0'], icon: 'Stopwatch', hidden: false},
                component: () => import('@/views/backend/statistics/index.vue')
            }
        ]
    },
    {
        path: '/',
        name: 'home',
        meta: {name: '首页', roles: ['0', '1'], icon: 'Home', hidden: true},
        redirect: '/index',
        component: () => import('@/views/frontend/index.vue'),
        children: [
            {
                path: '/index',
                name: 'index',
                meta: {name: '首页', roles: [], icon: 'Stopwatch', hidden: false},
                component: () => import('@/views/frontend/index/index.vue')
            },
            {
                path: '/detail/:id',
                name: 'blog-detail',
                meta: {name: '博客详情', roles: [], icon: 'Stopwatch', hidden: false},
                component: () => import('@/views/frontend/index/detail.vue')
            },
            {
                path: '/main/:id',
                name: 'main',
                meta: {name: '个人主页', roles: [], icon: 'Stopwatch', hidden: false},
                component: () => import('@/views/frontend/main/index.vue')
            },
            {
                path: '/blog/:id',
                name: 'blog',
                meta: {name: '发布文章', roles: [], icon: 'Stopwatch', hidden: false},
                component: () => import('@/views/frontend/blog/index.vue')
            },
            {
                path: '/center',
                name: 'center',
                meta: {name: '个人中心', roles: [], icon: 'Stopwatch', hidden: false},
                component: () => import('@/views/backend/user/center/index.vue')
            }
        ]
    },
    {
        path: '/login',
        name: 'Login',
        meta: {name: '登录', roles: [], icon: 'Stopwatch', hidden: true},
        component: () => import('@/views/LoginView.vue')
    },
    {
        path: '/register',
        name: 'Register',
        meta: {name: '注册', roles: [], icon: 'Stopwatch', hidden: true},
        component: () => import('@/views/RegisterView.vue')
    },
    {
        path: '/:pathMatch(.*)*',
        meta: {name: '404', roles: [], icon: 'Stopwatch', hidden: true},
        component: () => import('@/views/404.vue')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

/**
 * 路由守卫
 */
const whiteList = ['/login', '/register', '/index']
router.beforeEach((to, from, next) => {
    if (whiteList.indexOf(to.path) !== -1) {
        next()
        return
    }
    if (to.path.includes('/detail/') || to.path.includes('/main/')) {
        next()
        return
    }
    const token = JSON.parse(localStorage.getItem('user') || '{}').token
    if (!token) {
        next('/login')
        return
    }
    next()
})

export default router
