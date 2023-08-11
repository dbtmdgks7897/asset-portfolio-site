import { createRouter, createWebHistory } from 'vue-router'
import { login } from '@/utils/login'
import PageMain from '@/views/PageMain'
import PageBoardList from '@/views/board/PageBoardList'
import PageBoardWrite from '@/views/board/PageBoardWrite'
import PageBoardDetail from '@/views/board/PageBoardDetail'
import PageBoardUpdate from '@/views/board/PageBoardUpdate'
import PageLogin from '@/views/auth/PageLogin'
import PageJoin from '@/views/auth/PageJoin'
import PageMyinfo from '@/views/mypage/PageMyinfo'
import PageMyactive from '@/views/mypage/PageMyactive'
import PageManageUser from '@/views/admin/PageManageUser'
import PageManageBoard from '@/views/admin/PageManageBoard'
import PageAssetMain from '@/views/asset/PageAssetMain'
import PageStockDomestic from '@/views/asset/stock/PageStockDomestic'
import PageStockOverseas from '@/views/asset/stock/PageStockOverseas'
import PageAssetTransaction from '@/views/asset/PageAssetTransaction'


const routes = [
    {
        path: '/',
        name: 'PageMain',
        component: PageMain
    },
    {
        path: '/board',
        name: 'PageBoardList',
        component: PageBoardList
    },
    {
        path: '/board/write',
        name: 'PageBoardWrite',
        component: PageBoardWrite
    },
    {
        path: '/board/:id',
        name: 'PageBoardDetail',
        component: PageBoardDetail
    },
    {
        path: '/board/:id/updatePage',
        name: 'PageBoardUpdate',
        component: PageBoardUpdate
    },
    {
        path: '/login',
        name: 'PageLogin',
        component: PageLogin
    },
    {
        path: '/join',
        name: 'PageJoin',
        component: PageJoin
    },
    {
        path: '/mypage/info',
        name: 'PageMyinfo',
        component: PageMyinfo
    },
    {
        path: '/mypage/active',
        name: 'PageMyactive',
        component: PageMyactive
    },
    {
        path: '/admin/user',
        name: 'PageManageUser',
        component: PageManageUser
    },
    {
        path: '/admin/board',
        name: 'PageManageBoard',
        component: PageManageBoard
    },
    {
        path: '/asset',
        name: 'PageAssetMain',
        component: PageAssetMain
    },
    {
        path: '/asset/stock/domestic',
        name: 'PageStockDomestic',
        component: PageStockDomestic
    },
    {
        path: '/asset/stock/overseas',
        name: 'PageStockOverseas',
        component: PageStockOverseas
    },
    {
        path: '/asset/transaction',
        name: 'PageAssetTransaction',
        component: PageAssetTransaction
    },
]

const authNotRequiredComponents = ['PageMain', 'PageBoardList', 'PageJoin', 'PageLogin'];


const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    scrollBehavior () {
        return { top: 0 }
    },
    routes
  })
  
  router.beforeEach((to, from, next) => {
    if (!authNotRequiredComponents.includes(to.name) && !login.isLogined) {
      // 인증이 필요한 페이지에 접근하려고 할 때
      alert('로그인이 필요한 서비스입니다.');
      next({ name: 'PageLogin' }); // 로그인 페이지로 이동
    } else {
      next(); // 다음 단계로 진행
    }
  });

  export default router