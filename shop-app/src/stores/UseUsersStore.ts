import { defineStore } from 'pinia'
import type Users from '@/types/stores/Users'
import type LoginResDto from '@/types/dto/LoginResDto'
import { ConstantKey } from '@/common/ConstantKey'
import { useAxios } from '@/composables/UseAxios.ts'
import { type MaybeRef, ref, toValue } from 'vue'

const useUsersStore = defineStore('usersStore', () => {
  const { httpPost } = useAxios()
  const users = ref<Users>()
  const usersString = localStorage.getItem(ConstantKey.USERS_KEY)
  if (usersString) {
    users.value = JSON.parse(usersString)
  }

  const login = async (
    loginForm: MaybeRef<{
      username?: string
      password?: string
    }>
  ) => {
    const data: LoginResDto = await httpPost<LoginResDto>('/login', toValue(loginForm), {
      axiosErrorHandle: false
    })
    users.value = data
    localStorage.setItem(ConstantKey.USERS_KEY, JSON.stringify(toValue(users)))
  }

  const logout = () => {
    users.value = undefined
    localStorage.removeItem(ConstantKey.USERS_KEY)
  }

  return {
    users,
    login,
    logout
  }
})

export default useUsersStore
