import { createRouter, createWebHistory, type RouteLocationNormalized } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import * as NotificationUtils from '@/utils/NotificationUtils'
import { ViewMsg } from '@/common/MsgEnum'
import useUsersStore from '@/stores/UseUsersStore.ts'
import { useStore } from '@/stores/UseStore.ts'
import { useAxios } from '@/composables/UseAxios.ts'

const productViewName = 'product'
const loginViewName = 'login'
const indexViewName = 'index'
const homeViewName = 'home'
const signUpViewName = 'signUp'
const ordersViewName = 'ordersView'
const notCheckLogin: string[] = [
  productViewName,
  loginViewName,
  indexViewName,
  homeViewName,
  signUpViewName
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: homeViewName,
      component: HomeView
    },
    {
      path: '/index',
      name: indexViewName,
      component: HomeView
    },
    {
      path: '/product',
      name: productViewName,
      component: () => import('../views/ProductView.vue')
    },
    {
      path: '/login',
      name: loginViewName,
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/users',
      name: 'users',
      component: () => import('../views/EditUsersView.vue')
    },
    {
      path: '/cart',
      name: 'cart',
      component: () => import('../views/CartView.vue')
    },
    {
      path: '/signUp',
      name: signUpViewName,
      component: () => import('../views/SignUpView.vue')
    },
    {
      path: '/orders',
      name: ordersViewName,
      component: () => import('../views/OrdersView.vue')
    }
  ]
})

router.beforeEach(loginCheck)

//檢查是否有登入或逾期
async function loginCheck(to: RouteLocationNormalized) {
  try {
    const usersStore = useUsersStore()
    const store = useStore()
    const { httpPost } = useAxios()

    //頁面是否需要驗證
    if (notCheckLogin.includes(to.name!.toString())) {
      return
    }

    const token = usersStore.users?.token
    const isVerify = (await httpPost<boolean>('/public/tokenVerify', { token })).data

    //確定token是否過期如果過期將token刪除
    if (!isVerify) {
      usersStore.logout()
    }

    //檢查token是否有效
    if (token && isVerify) {
      return
    }

    store.beforeLoginUrl = to.fullPath
    NotificationUtils.showWaringNotification(ViewMsg.NotLogin)
    return '/login'
  } catch (error) {
    console.error('loginCheck error', error)
    NotificationUtils.showErrorNotification('請重新進行使用者登入')
    return '/login'
  }
}

export default router
