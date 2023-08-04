import { createRouter, createWebHistory } from 'vue-router'
import PageMain from '@/views/PageMain'
import PageBoardList from '@/views/boards/PageBoardList'
import PageBoardWrite from '@/views/boards/PageBoardWrite'
import PageBoardDetail from '@/views/boards/PageBoardDetail'
import PageBoardUpdate from '@/views/boards/PageBoardUpdate'
import PageLogin from '@/views/auth/PageLogin'
import PageJoin from '@/views/auth/PageJoin'
import PageMyinfo from '@/views/mypages/PageMyinfo'
import PageMyactive from '@/views/mypages/PageMyactive'
import PageManageUsers from '@/views/admin/PageManageUsers'
import PageManageBoards from '@/views/admin/PageManageBoards'
import PageAssetsMain from '@/views/assets/PageAssetsMain'
import PageStocksDomestic from '@/views/assets/stocks/PageStocksDomestic'

import Test from '@/views/test'


const routes = [
    {
        path: '/',
        name: 'PageMain',
        component: PageMain
    },
    {
        path: '/boards',
        name: 'PageBoardList',
        component: PageBoardList
    },
    {
        path: '/boards/write',
        name: 'PageBoardWrite',
        component: PageBoardWrite
    },
    {
        path: '/boards/:id',
        name: 'PageBoardDetail',
        component: PageBoardDetail
    },
    {
        path: '/boards/:id/update',
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
        path: '/admin/users',
        name: 'PageManageUsers',
        component: PageManageUsers
    },
    {
        path: '/admin/boards',
        name: 'PageManageBoards',
        component: PageManageBoards
    },
    {
        path: '/assets',
        name: 'PageAssetsMain',
        component: PageAssetsMain
    },
    {
        path: '/assets/stocks/domestic',
        name: 'PageStocksDomestic',
        component: PageStocksDomestic
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