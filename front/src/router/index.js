import { createRouter, createWebHistory } from 'vue-router'
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

import Test from '@/views/test'


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
        path: '/myinfo',
        name: 'PageMyinfo',
        component: PageMyinfo
    },
    {
        path: '/myactive',
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
    
    {
        path: '/test',
        name: 'Test',
        component: Test
    },

]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    scrollBehavior () {
        return { top: 0 }
    },
    routes
  })
  
  export default router