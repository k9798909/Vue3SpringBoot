<script setup lang="ts">
import { onMounted, ref, watch, type Ref } from 'vue'
import * as UsersService from '@/services/UsersService'
import headerItems from '@/common/HeaderItems'
import type Users from '@/types/stores/Users'
import router from '@/router'
import useUsersStore from '@/stores/UseUsersStore'

const userStore = useUsersStore()
let isLogin: Ref<boolean> = ref(false)
let name: Ref<string> = ref('')

async function loadUserStatus(): Promise<void> {
  let users: Users | null = UsersService.getStoreUsers()
  name.value = users?.name || ''
  isLogin.value = !!users
}

function logoutEvent(): void {
  UsersService.logout()
  router.push('/login')
}

onMounted(loadUserStatus)

//監聽store有更動觸發init
watch(
  () => userStore.users,
  () => loadUserStatus()
)
</script>

<template>
  <v-app-bar :elevation="1">
    <v-toolbar-title>
      <router-link class="d-flex" to="/index">
        <img alt="Vue logo" class="logo" src="@/assets/logo.svg" />
        <span class="ml-2">Shop-App</span>
      </router-link>
    </v-toolbar-title>

    <router-link v-for="item in headerItems" :to="item.href" custom v-slot="{ navigate }">
      <v-btn variant="text" @click="navigate">{{ item.name }}</v-btn>
    </router-link>

    <div v-if="!isLogin" class="px-5">
      <router-link to="/login" custom v-slot="{ navigate }">
        <v-btn class="px-0" variant="text" @click="navigate"
          ><v-icon icon="mdi-account" />登入</v-btn
        >
      </router-link>
      |
      <router-link to="/signUp" custom v-slot="{ navigate }">
        <v-btn class="px-0" variant="text" @click="navigate"
          ><v-icon icon="mdi-account-plus" />註冊</v-btn
        >
      </router-link>
    </div>

    <v-menu open-on-hover v-if="isLogin">
      <template v-slot:activator="{ props }">
        <v-btn v-bind="props"><v-icon icon="mdi-account" /></v-btn>
      </template>

      <v-list>
        <v-list-item>
          <router-link to="/users" custom v-slot="{ navigate }">
            <v-btn variant="text" @click="navigate">個人資料</v-btn>
          </router-link>
        </v-list-item>
        <v-list-item>
          <v-btn variant="text" @click="logoutEvent">登出</v-btn>
        </v-list-item>
      </v-list>
    </v-menu>
  </v-app-bar>
</template>

<style lang="scss" scoped>
.logo {
  display: block;
  width: 35px;
  height: 35px;
}

a {
  color: inherit;
  text-decoration: none;
}

a:hover {
  color: inherit;
  text-decoration: none;
}
</style>
