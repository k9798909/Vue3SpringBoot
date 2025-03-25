import { defineStore } from 'pinia'
import type Users from '@/types/stores/Users'
import { ConstantKey } from '@/common/ConstantKey'
import { ref, toValue } from 'vue'
import { isNil, isUndefined } from 'lodash'
import { useAxios } from '@/composables/UseAxios.ts'
import { AxiosError } from 'axios'

const useUsersStore = defineStore('usersStore', () => {
  const users = ref<Users>()
  const verifyTokenDone = ref<boolean>(false)
  const usersString = localStorage.getItem(ConstantKey.USERS_KEY)

  const login = (loginUsers: Users) => {
    users.value = loginUsers
    localStorage.setItem(ConstantKey.USERS_KEY, JSON.stringify(toValue(loginUsers)))
  }

  const logout = () => {
    users.value = undefined
    localStorage.removeItem(ConstantKey.USERS_KEY)
  }

  const isLogin = () => {
    return (
      !isNil(users.value) &&
      !isUndefined(users.value) &&
      !isNil(users.value?.token) &&
      !isUndefined(users.value?.token)
    )
  }

  const checkToken = async () => {
    try {
      const { httpPost } = useAxios()
      const isValid = await httpPost<boolean>('/public/tokenVerify', { token: users.value?.token })
      if (!isValid) {
        logout()
      }
      verifyTokenDone.value = true
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
    } catch (error) {
      if (!(error instanceof AxiosError)) {
        console.error(error)
      }
      logout()
    }
  }

  if (!isNil(usersString) && !isUndefined(usersString) && usersString !== 'undefined') {
    users.value = JSON.parse(usersString)
  } else {
    logout()
  }

  return {
    users,
    login,
    logout,
    isLogin,
    checkToken,
    verifyTokenDone
  }
})

export default useUsersStore
