import { defineStore } from 'pinia'
import type Users from '@/types/stores/Users'
import { ConstantKey } from '@/common/ConstantKey'
import { ref, toValue } from 'vue'

const useUsersStore = defineStore('usersStore', () => {
  const users = ref<Users>()
  const usersString = localStorage.getItem(ConstantKey.USERS_KEY)
  if (usersString) {
    users.value = JSON.parse(usersString)
  }

  const login = (loginUsers: Users) => {
    users.value = loginUsers
    localStorage.setItem(ConstantKey.USERS_KEY, JSON.stringify(toValue(loginUsers)))
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
