<template>
  <v-card class="mx-auto px-5 pb-5 mt-15" elevation="8" max-width="448" rounded="lg">
    <v-card-text>
      <p class="text-h5 font-weight-black mb-5 text-center">登入</p>
      <v-form>
        <TextField
          v-model="formData.username"
          label="使用者名稱"
          placeholder="輸入使用者名稱"
          prepend-inner-icon="mdi-email-outline"
          variant="outlined"
          color="primary"
          required
          :customErrorMessage="formError?.username"
          class="mb-2"
        ></TextField>
        <TextField
          v-model="formData.password"
          :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
          :type="visible ? 'text' : 'password'"
          placeholder="輸入密碼"
          prepend-inner-icon="mdi-lock-outline"
          required
          variant="outlined"
          label="密碼"
          color="primary"
          @click:append-inner="visible = !visible"
          :customErrorMessage="formError?.password"
          class="mb-2"
        >
        </TextField>
        <v-btn block color="indigo" size="large" variant="elevated" @click="onLogin">登入</v-btn>
      </v-form>
    </v-card-text>
  </v-card>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from '@/stores/UseStore'
import useUsersStore from '@/stores/UseUsersStore.ts'
import TextField from '@/components/TextField.vue'
import { useFormState } from '@/composables/UseFormState.ts'
import type Users from '@/types/stores/Users.ts'

export interface LoginForm {
  username: string
  password: string
}

const { formData, formError, submitFormData } = useFormState<LoginForm>()
const store = useStore()
const usersStore = useUsersStore()
const router = useRouter()
const visible = ref(false)

const onLogin = async () => {
  const users = await submitFormData<Users>('/login', { method: 'POST' })
  usersStore.login(users)
  const toUrl = store.beforeLoginUrl
  store.beforeLoginUrl = ''
  await router.push(toUrl || '/index')
}
</script>
