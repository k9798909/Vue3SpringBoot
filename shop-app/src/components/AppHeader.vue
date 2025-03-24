<template>
  <v-app-bar :elevation="1">
    <v-toolbar-title>
      <router-link class="d-flex align-center" to="/index">
        <img alt="Vue logo" class="logo" src="@/assets/logo.svg" />
        <h1 class="text-h5 ml-2">Shop-App</h1>
      </router-link>
    </v-toolbar-title>

    <router-link
      v-for="(item, index) in headerItems"
      v-slot="{ navigate }"
      :key="index"
      :to="item.href"
      custom
    >
      <v-btn variant="text" size="large" @click="navigate">{{ item.name }}</v-btn>
    </router-link>

    <div v-if="!isLogin" class="px-5">
      <router-link v-slot="{ navigate }" to="/login" custom>
        <v-btn class="px-0" size="large" variant="text" @click="navigate">
          <v-icon icon="mdi-account" />
          登入
        </v-btn>
      </router-link>
      |
      <router-link v-slot="{ navigate }" to="/signUp" custom>
        <v-btn class="px-0" size="large" variant="text" @click="navigate">
          <v-icon icon="mdi-account-plus" />
          註冊
        </v-btn>
      </router-link>
    </div>

    <v-menu v-if="isLogin" open-on-hover>
      <template #activator="{ props }">
        <v-btn v-bind="props">
          <v-icon icon="mdi-account" />
        </v-btn>
      </template>

      <v-list>
        <v-list-item>
          <router-link v-slot="{ navigate }" to="/users" custom>
            <v-btn variant="text" size="large" @click="navigate">個人資料</v-btn>
          </router-link>
        </v-list-item>
        <v-list-item>
          <v-btn variant="text" size="large" @click="logoutEvent">登出</v-btn>
        </v-list-item>
      </v-list>
    </v-menu>
  </v-app-bar>
</template>

<script setup lang="ts">
import { onMounted, ref, watch, type Ref } from 'vue'
import headerItems from '@/common/HeaderItems'
import useUsersStore from '@/stores/UseUsersStore'
import { useRouter } from 'vue-router'

const router = useRouter()
const usersStore = useUsersStore()
const isLogin: Ref<boolean> = ref(false)
const name: Ref<string> = ref('')

const loadUserStatus = async () => {
  const users = usersStore.users
  name.value = users?.name || ''
  isLogin.value = !!users
}

const logoutEvent = () => {
  usersStore.logout()
  router.push('/login')
}

onMounted(() => loadUserStatus())

//監聽store有更動觸發init
watch(
  () => usersStore.users,
  () => loadUserStatus()
)
</script>

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
