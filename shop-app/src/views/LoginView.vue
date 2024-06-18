<template>
  <v-form ref="form">
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
          :rules="[() => loginForm.username.length > 0 || '請輸入使用者名稱']"
          :error-messages="loginValidMessage.username"
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
          :rules="[() => loginForm.password.length > 0 || '請輸入密碼']"
          :error-messages="loginValidMessage.password"
        ></v-text-field>
        <v-alert class="mb-5" v-if="msg" density="compact" type="error" variant="tonal">{{
          msg
        }}</v-alert>
        <v-btn block class="mb-5" color="indigo" size="large" variant="elevated" @click="loginEvent"
          >登入</v-btn
        >
      </v-card>
    </div>
  </v-form>
</template>

<script setup lang="ts">
import * as UsersService from '@/services/UsersService'
import { ref, type Ref } from 'vue'
import type { LoginForm, LoginValidationMessages } from '@/types/form/LoginForm'
import { useRouter, type Router } from 'vue-router'
import { getFieldErrors, isUnauthorized } from '@/http'
import * as NotificationUtils from '@/utils/NotificationUtils'
import useStore from '@/stores/UseStore'
import { ViewMsg } from '@/common/MsgEnum'

const router: Router = useRouter()
//密碼能見度
const visible: Ref<boolean> = ref(false)
//session訊息用完移除
const msg: Ref<string> = ref('')
const form: Ref<HTMLFormElement | null> = ref(null)

const loginForm: Ref<LoginForm> = ref({
  username: '',
  password: ''
})

const loginValidMessage: Ref<LoginValidationMessages> = ref({
  username: [],
  password: []
})

const loginEvent = async () => {
  msg.value = ''

  const valid = (await form.value!.validate()).valid
  if (!valid) {
    return
  }

  UsersService.login(loginForm.value)
    .then(() => {
      const toUrl = useStore().getBeforeLoginUrl
      useStore().clearBeforeLoginUrl()
      router.push(toUrl || '/index')
    })
    .catch((error) => {
      const fieldErrors = getFieldErrors<LoginValidationMessages>(error)
      if (fieldErrors) {
        loginValidMessage.value = fieldErrors
        return
      }

      if (isUnauthorized(error)) {
        msg.value = '帳號或密碼錯誤'
        return
      }

      console.error('server error', error)
      NotificationUtils.showErrorNotification(ViewMsg.ServerError)
    })
}
</script>

<style lang="scss" scoped></style>
