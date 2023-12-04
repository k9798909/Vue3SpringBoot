<script setup lang="ts">
import * as UsersService from '@/services/UsersService'
import { ref, type Ref } from 'vue'
import { ConstantKey } from '@/common/ConstantKey'
import type LoginForm from '@/types/form/LoginForm'
import { useRouter, type Router } from 'vue-router'

const router: Router = useRouter()
//密碼能見度
const visible: Ref<boolean> = ref(false)
//session訊息用完移除
const msg: Ref<string> = ref(sessionStorage.getItem(ConstantKey.LOGIN_SESSION_MSG) || '')
sessionStorage.removeItem(ConstantKey.LOGIN_SESSION_MSG)

const loginForm: Ref<LoginForm> = ref({
  username: '',
  password: ''
})

const loginEvent = async () => {
  msg.value = ''

  if (!(loginForm.value.username && loginForm.value.password)) {
    msg.value = '請輸入帳號及密碼'
    return
  }

  UsersService.login(loginForm.value)
    .then(() => {
      router.push('/index')
    })
    .catch((e) => {
      msg.value = e.message
    })
}
</script>

<template>
  <div>
    <v-img
      class="mx-auto my-6"
      max-width="228"
      src="https://cdn.vuetifyjs.com/docs/images/logos/vuetify-logo-v3-slim-text-light.svg"
    ></v-img>

    <v-card class="mx-auto pa-12 pb-8" elevation="8" max-width="448" rounded="lg">
      <div class="text-subtitle-1 text-medium-emphasis">使用者名稱</div>

      <v-text-field
        density="compact"
        placeholder="輸入使用者名稱"
        prepend-inner-icon="mdi-email-outline"
        variant="outlined"
        v-model="loginForm.username"
      ></v-text-field>

      <div class="text-subtitle-1 text-medium-emphasis d-flex align-center justify-space-between">
        密碼

        <!-- <a
          class="text-caption text-decoration-none text-blue"
          href="#"
          rel="noopener noreferrer"
          target="_blank"
        >
          Forgot login password?</a
        > -->
      </div>

      <v-text-field
        :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
        :type="visible ? 'text' : 'password'"
        density="compact"
        placeholder="輸入密碼"
        prepend-inner-icon="mdi-lock-outline"
        variant="outlined"
        v-model="loginForm.password"
        @click:append-inner="visible = !visible"
      ></v-text-field>
      <v-alert class="mb-5" v-if="msg" density="compact" type="error" variant="tonal">{{
        msg
      }}</v-alert>
      <v-btn block class="mb-5" color="indigo" size="large" variant="elevated" @click="loginEvent"
        >登入</v-btn
      >
    </v-card>
  </div>
</template>
<style lang="scss" scoped></style>
