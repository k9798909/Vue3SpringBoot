import { defineStore } from 'pinia'
import type Users from '@/types/stores/Users'
import type LoginResDto from '@/types/dto/LoginResDto'
import { inject } from 'vue'
import type { VueCookies } from 'vue-cookies'
import { ConstantKey } from '@/common/ConstantKey'

const useUsersStore = defineStore('usersStore', {
  state: () => {
    const store: Storage = localStorage
    let users: Users | null = null
    const usersString = store.getItem(ConstantKey.USERS_KEY)
    if (usersString) {
      users = JSON.parse(usersString)
    }
    return { users, store }
  },
  getters: {
    getUsers: (state) => state.users
  },
  actions: {
    login(loginRes: LoginResDto, token: string) {
      this.users = { ...loginRes, token }
      this.store.setItem(ConstantKey.USERS_KEY, JSON.stringify(this.users))
    },
    logout() {
      this.users = null
      this.store.removeItem(ConstantKey.USERS_KEY)
    }
  }
})

export default useUsersStore
