<template>
  <v-sheet class="mx-auto" width="600">
    <v-form class="d-flex flex-column ga-3">
      <h2 class="text-center">註冊</h2>
      <TextField
        label="帳號"
        v-model="formData.username"
        type="text"
        id="username"
        placeholder="輸入帳號"
        variant="outlined"
        @change="checkUsername"
        :customErrorMessage="formError?.username"
      />
      <TextField
        label="密碼"
        v-model="formData.password"
        type="password"
        id="password"
        placeholder="輸入密碼"
        variant="outlined"
        :customErrorMessage="formError?.password"
      />
      <TextField
        label="密碼複驗"
        type="password"
        id="chkPassword"
        v-model="formData.chkPassword"
        placeholder="輸入與上方相同的密碼"
        variant="outlined"
        :customErrorMessage="formError?.chkPassword"
      />
      <TextField
        label="姓名"
        v-model="formData.name"
        type="text"
        id="name"
        placeholder="輸入姓名"
        variant="outlined"
        :customErrorMessage="formError?.name"
      />
      <TextField
        label="出生日期"
        v-model="formData.birthday"
        type="date"
        id="birthday"
        variant="outlined"
      />
      <TextField
        label="電子信箱"
        v-model="formData.email"
        type="email"
        id="email"
        variant="outlined"
        placeholder="輸入電子信箱"
        :customErrorMessage="formError?.email"
      />
      <TextField
        label="地址"
        v-model="formData.address"
        type="text"
        id="address"
        variant="outlined"
        placeholder="輸入地址"
        :customErrorMessage="formError?.address"
      />
      <v-btn
        type="submit"
        @click="submit"
        class="w-100 btn btn-success btn-lg"
        color="indigo"
        variant="elevated"
        >確認
      </v-btn>
    </v-form>

    <!--  成功視窗 [[ -->
    <SuccessDialog
      v-model:show="dialogShow"
      title="註冊成功"
      btnName="回到首頁"
      content="註冊成功點選回到首頁。"
      @btnFunction="() => router.push('/index')"
    />
    <!--  ]] -->
  </v-sheet>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import type { SignUpForm } from '@/types/form/SignUpForm'
import { useRouter, type Router } from 'vue-router'
import SuccessDialog from '@/components/SuccessDialog.vue'
import { useFormState } from '@/composables/UseFormState.ts'
import { useAxios } from '@/composables/UseAxios.ts'
import TextField from '@/components/TextField.vue'

const dialogShow = ref(false)
const router: Router = useRouter()
const { httpPost } = useAxios()
const { formData, formError, submitFormData } = useFormState<SignUpForm>({
  name: '',
  birthday: new Date().toLocaleDateString('en-CA'),
  email: '',
  address: '',
  username: '',
  password: '',
  chkPassword: ''
})

// 檢核帳號是否存在
const checkUsername = async () => {
  const isValid = await httpPost<boolean>('public/users/checkUsername', {
    username: formData.value.username
  })
  if (!isValid) {
    formError.value.username = '帳號已存在'
  }
}

const submit = async (e: MouseEvent) => {
  e.preventDefault()
  await submitFormData('public/users/signUp', { method: 'POST' })
  dialogShow.value = true
}
</script>
<style lang="scss" scoped></style>
