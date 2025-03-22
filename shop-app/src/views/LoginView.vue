<template>
  <v-card class="mx-auto px-5 pb-5 mt-15" elevation="8" max-width="448" rounded="lg">
    <v-card-text>
      <p class="text-h5 font-weight-black mb-5 text-center">登入</p>
      <v-form>
        <TextField
          v-model="loginForm.username"
          label="使用者名稱"
          placeholder="輸入使用者名稱"
          prepend-inner-icon="mdi-email-outline"
          variant="outlined"
          color="primary"
          required
          :custonErrorMessage="errorMessage?.username"
          class="mb-2"
        ></TextField>
        <TextField
          v-model="loginForm.password"
          :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
          :type="visible ? 'text' : 'password'"
          placeholder="輸入密碼"
          prepend-inner-icon="mdi-lock-outline"
          required
          variant="outlined"
          label="密碼"
          color="primary"
          @click:append-inner="visible = !visible"
          :custonErrorMessage="errorMessage?.password"
          class="mb-2"
        >
        </TextField>
        <v-alert v-if="msg" class="mb-5" density="compact" type="error" variant="tonal"
          >{{ msg }}
        </v-alert>
        <v-btn block color="indigo" size="large" variant="elevated" @click="onLogin">登入</v-btn>
      </v-form>
    </v-card-text>
  </v-card>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { getFieldErrors, isUnauthorized } from '@/http'
import * as NotificationUtils from '@/utils/NotificationUtils'
import useStore from '@/stores/UseStore'
import { ViewMsg } from '@/common/MsgEnum'
import useUsersStore from '@/stores/UseUsersStore.ts'
import TextField from '@/components/TextField.vue'
import type { FieldError } from '@/composables/UseAxios.ts'

export interface LoginForm {
  username: string
  password: string
}

const usersStore = useUsersStore()
const router = useRouter()
const visible = ref(false)
const msg = ref('')
const loginForm = ref<Partial<LoginForm>>({})
const errorMessage = ref<FieldError>()

const onLogin = async () => {
  msg.value = ''
  errorMessage.value = undefined
  await usersStore
    .login(loginForm.value)
    .then(() => {
      const toUrl = useStore().getBeforeLoginUrl
      useStore().clearBeforeLoginUrl()
      router.push(toUrl || '/index')
    })
    .catch((error) => {
      const fieldErrors = getFieldErrors<FieldError>(error)
      if (fieldErrors) {
        errorMessage.value = fieldErrors
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
