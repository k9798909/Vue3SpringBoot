<script setup lang="ts">
import * as UsersService from '@/services/UsersService'
import { reactive, ref, type Ref } from 'vue'
import { ViewMsg } from '@/common/MsgEnum'
import { NetworkErrorCode } from '@/common/HttpEnum'
import type { AxiosError } from 'axios'
import { ConstantKey } from '@/common/ConstantKey'
import type LoginForm from '@/types/form/LoginForm'
import { useRouter, type Router } from 'vue-router'

const router: Router = useRouter()

//session訊息用完移除
let msg: Ref<string> = ref(sessionStorage.getItem(ConstantKey.LOGIN_SESSION_MSG) || '')
sessionStorage.removeItem(ConstantKey.LOGIN_SESSION_MSG)
//密碼能見度
let visible: Ref<boolean> = ref(false)

let loginForm: LoginForm = {
  username: '',
  password: ''
}
const state = reactive({ loginForm })

const loginEvent = async () => {
  msg.value = ''

  if (!(state.loginForm.username && state.loginForm.password)) {
    msg.value = '請輸入帳號及密碼'
    return
  }

  UsersService.login(state.loginForm)
    .then(() => {
      router.push('/index')
    })
    .catch((e) => {
      let axiosError: AxiosError = e as AxiosError
      if (NetworkErrorCode.Unauthorized == axiosError.response?.status) {
        msg.value = ViewMsg.InvalidUsernameOrPassword
        return
      }

      console.error('login error:', e)
      msg.value = ViewMsg.ServerError
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
        v-model="state.loginForm.username"
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
        v-model="state.loginForm.password"
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
