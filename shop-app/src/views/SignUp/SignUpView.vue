<script setup lang="ts">
import { ref, type Ref } from 'vue'
import * as UsersService from '@/services/UsersService'
import type SignUpForm from '@/types/form/SignUpForm'
import { useRouter, type Router } from 'vue-router'
import * as SignUpHelper from './SignUpHelper'
import SuccessDialog from '@/components/SuccessDialog.vue'

const router: Router = useRouter()
let dialogShow: Ref<boolean> = ref(false)
const form: Ref<HTMLFormElement | null> = ref(null)
const formParams: Ref<SignUpForm> = ref(SignUpHelper.buildSignUpForm())

async function submit(e: MouseEvent) {
  try {
    e.preventDefault()

    const valid = (await form.value!.validate()).valid
    if (!valid) {
      return
    }

    await UsersService.signUp(formParams.value)

    dialogShow.value = true
  } catch (error) {
    console.error('註冊失敗', error)
    alert('伺服器異常')
  }
}
</script>

<template>
  <main>
    <v-form ref="form">
      <v-card class="mx-auto pa-12 pb-8" width="50%">
        <v-card-title><h2 class="text-center">註冊</h2></v-card-title>
        <div class="text-subtitle-1 text-medium-emphasis">帳號</div>
        <v-text-field
          v-model="formParams.username"
          type="text"
          id="username"
          placeholder="輸入帳號"
          variant="outlined"
          density="compact"
          :rules="SignUpHelper.getUsernameRules()"
        />
        <div class="text-subtitle-1 text-medium-emphasis">密碼</div>
        <v-text-field
          v-model="formParams.password"
          type="password"
          id="password"
          placeholder="輸入密碼"
          variant="outlined"
          density="compact"
          :rules="SignUpHelper.getPasswordRules()"
        />
        <div class="text-subtitle-1 text-medium-emphasis">密碼複驗</div>
        <v-text-field
          type="password"
          id="chkPassword"
          v-model="formParams.chkPassword"
          placeholder="輸入與上方相同的密碼"
          variant="outlined"
          density="compact"
          :rules="[
            ...SignUpHelper.getChkPasswordRules(),
            (v) => v === formParams.password || '密碼複驗需與上方密碼相同'
          ]"
        />
        <div class="text-subtitle-1 text-medium-emphasis">姓名</div>
        <v-text-field
          v-model="formParams.name"
          type="text"
          id="name"
          placeholder="輸入姓名"
          variant="outlined"
          density="compact"
          :rules="SignUpHelper.getNameRules()"
        />
        <div class="text-subtitle-1 text-medium-emphasis">出生日期</div>
        <v-text-field
          v-model="formParams.birthday"
          type="date"
          id="birthday"
          variant="outlined"
          density="compact"
        />
        <div class="text-subtitle-1 text-medium-emphasis">電子信箱</div>
        <v-text-field
          v-model="formParams.email"
          type="email"
          id="email"
          variant="outlined"
          density="compact"
          placeholder="輸入電子信箱"
          :rules="SignUpHelper.getEmailRules()"
        />
        <div class="text-subtitle-1 text-medium-emphasis">地址</div>
        <v-text-field
          v-model="formParams.address"
          type="text"
          id="address"
          variant="outlined"
          density="compact"
          placeholder="輸入地址"
          :rules="SignUpHelper.getAddressRules()"
        />

        <v-btn
          type="submit"
          @click="submit"
          class="w-100 btn btn-success btn-lg"
          color="indigo"
          variant="elevated"
          >確認</v-btn
        >
      </v-card>
    </v-form>

    <!--  成功視窗 [[ -->
    <SuccessDialog
      :dialogShow="dialogShow"
      title="註冊成功"
      btnName="回到首頁"
      content="註冊成功點選回到首頁。"
      :btnFunction="() => router.push('/index')"
    />
    <!--  ]] -->
  </main>
</template>
<style lang="scss" scoped></style>
