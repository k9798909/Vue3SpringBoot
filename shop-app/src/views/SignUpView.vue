<template>
  <v-sheet class="mx-auto" width="600">
    <v-form class="d-flex flex-column ga-3">
      <h2 class="text-center">註冊</h2>
      <v-text-field
        label="帳號"
        v-model="formParams.username"
        type="text"
        id="username"
        placeholder="輸入帳號"
        variant="outlined"
        :rules="UsersFormValidator.getUsernameRules()"
        :errorMessages="validMessage.username"
      />
      <v-text-field
        label="密碼"
        v-model="formParams.password"
        type="password"
        id="password"
        placeholder="輸入密碼"
        variant="outlined"
        :rules="UsersFormValidator.getPasswordRules()"
        :errorMessages="validMessage.password"
      />
      <v-text-field
        label="密碼複驗"
        type="password"
        id="chkPassword"
        v-model="formParams.chkPassword"
        placeholder="輸入與上方相同的密碼"
        variant="outlined"
        :rules="[
          ...UsersFormValidator.getChkPasswordRules(),
          (v) => v === formParams.password || '密碼複驗需與上方密碼相同'
        ]"
        :errorMessages="validMessage.chkPassword"
      />
      <v-text-field
        label="姓名"
        v-model="formParams.name"
        type="text"
        id="name"
        placeholder="輸入姓名"
        variant="outlined"
        :rules="UsersFormValidator.getNameRules()"
        :errorMessages="validMessage.name"
      />
      <v-text-field
        label="出生日期"
        v-model="formParams.birthday"
        type="date"
        id="birthday"
        variant="outlined"
      />
      <v-text-field
        label="電子信箱"
        v-model="formParams.email"
        type="email"
        id="email"
        variant="outlined"
        placeholder="輸入電子信箱"
        :rules="UsersFormValidator.getEmailRules()"
        :errorMessages="validMessage.email"
      />
      <v-text-field
        label="地址"
        v-model="formParams.address"
        type="text"
        id="address"
        variant="outlined"
        placeholder="輸入地址"
        :rules="UsersFormValidator.getAddressRules()"
        :errorMessages="validMessage.address"
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
      :btnFunction="() => router.push('/index')"
    />
    <!--  ]] -->
  </v-sheet>
</template>
<script setup lang="ts">
import { ref, type Ref } from 'vue'
import * as UsersFormValidator from '@/validators/UsersFormValidator'
import type { SignUpForm, SignUpValidMessage } from '@/types/form/SignUpForm'
import { useRouter, type Router } from 'vue-router'
import SuccessDialog from '@/components/SuccessDialog.vue'
import * as NotificationUtils from '@/utils/NotificationUtils'
import { ViewMsg } from '@/common/MsgEnum'
import { useAxios } from '@/composables/UseAxios.ts'

const { httpPost } = useAxios()
const router: Router = useRouter()
const dialogShow = ref(false)
const form: Ref<HTMLFormElement | null> = ref(null)
const formParams: Ref<SignUpForm> = ref({
  name: '',
  birthday: new Date().toLocaleDateString('en-CA'),
  email: '',
  address: '',
  username: '',
  password: '',
  chkPassword: ''
})
const validMessage: Ref<SignUpValidMessage> = ref({
  name: [],
  birthday: [],
  email: [],
  address: [],
  username: [],
  password: [],
  chkPassword: []
})

async function submit(e: MouseEvent) {
  e.preventDefault()

  const valid = (await form.value!.validate()).valid
  if (!valid) {
    NotificationUtils.showErrorNotification(ViewMsg.FiledError)
    return
  }

  await httpPost('public/users/signUp', formParams.value)
  dialogShow.value = true
}
</script>
<style lang="scss" scoped></style>
