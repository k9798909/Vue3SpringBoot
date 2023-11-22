import { defineStore } from 'pinia'
import type Users from '@/types/stores/Users'
import type LoginResDto from '@/types/dto/LoginResDto'
import { inject } from 'vue'
import type { VueCookies } from 'vue-cookies'
import { ConstantKey } from '@/common/ConstantKey'

const useUsersStore = defineStore('usersStore', {
  state: () => {
    const $cookies: VueCookies = inject<VueCookies>('$cookies')!
    let users: Users | null = $cookies.get(ConstantKey.USERS_KEY)
    return { users, $cookies }
  },
  getters: {
    getUsers: (state) => state.users
  },
  actions: {
    login(loginRes: LoginResDto, token: string) {
      this.users = { ...loginRes, token }
      this.$cookies.set(ConstantKey.USERS_KEY, JSON.stringify(this.users))
    },
    logout() {
      this.users = null
      this.$cookies.remove(ConstantKey.USERS_KEY)
    }
  }
})

export default useUsersStore
