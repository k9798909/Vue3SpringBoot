<template>
  <main>
    <v-form class="form" ref="form">
      <v-card class="mx-auto pa-4 pb-8" width="50%">
        <v-card-title><h2 class="text-center">個人資料</h2></v-card-title>
        <div class="d-flex justify-end" width="100%">
          <v-btn-toggle v-model="toggle" divided>
            <v-btn variant="text" @click="() => (isEdit = !isEdit)"
              ><v-icon icon="mdi-square-edit-outline" />編輯</v-btn
            >
          </v-btn-toggle>
        </div>

        <div class="text-subtitle-1 text-medium-emphasis">帳號</div>
        <v-text-field
          type="text"
          id="username"
          placeholder="輸入帳號"
          variant="outlined"
          density="compact"
          readonly
          :value="editForm.username"
        />
        <div class="text-subtitle-1 text-medium-emphasis">姓名</div>
        <v-text-field
          type="text"
          id="name"
          placeholder="輸入姓名"
          variant="outlined"
          density="compact"
          :readonly="!isEdit"
          v-model="editForm.name"
          :rules="UsersFormValidator.getNameRules()"
          :error-messages="editUsersValidMessage.name"
        />
        <div class="text-subtitle-1 text-medium-emphasis">出生日期</div>
        <v-text-field
          type="date"
          id="birthday"
          variant="outlined"
          density="compact"
          :readonly="!isEdit"
          v-model="editForm.birthday"
        />
        <div class="text-subtitle-1 text-medium-emphasis">電子信箱</div>
        <v-text-field
          type="email"
          id="email"
          variant="outlined"
          density="compact"
          placeholder="輸入電子信箱"
          :readonly="!isEdit"
          v-model="editForm.email"
          :rules="UsersFormValidator.getEmailRules()"
          :error-messages="editUsersValidMessage.email"
        />
        <div class="text-subtitle-1 text-medium-emphasis">地址</div>
        <v-text-field
          type="text"
          id="address"
          variant="outlined"
          density="compact"
          placeholder="輸入地址"
          :readonly="!isEdit"
          v-model="editForm.address"
          :rules="UsersFormValidator.getAddressRules()"
          :error-messages="editUsersValidMessage.address"
        />

        <v-btn
          v-if="isEdit"
          type="submit"
          @click="submit"
          class="w-100 btn btn-success btn-lg"
          color="indigo"
          variant="elevated"
          >修改</v-btn
        >
      </v-card>
    </v-form>
    <!--  成功視窗 [[ -->
    <SuccessDialog
      :dialogShow="dialogShow"
      title="修改成功"
      btnName="確認"
      content="個人資料修改成功"
      :btnFunction="() => (dialogShow = !dialogShow)"
    />
    <!--  ]] -->
  </main>
</template>

<script setup lang="ts">
import { onMounted, ref, type Ref } from 'vue'
import * as UsersFormValidator from '@/validators/UsersFormValidator'
import * as UsersService from '@/services/UsersService'
import type { EditUsersForm, EditUsersValidMessage } from '@/types/form/EditUsersForm'
import SuccessDialog from '@/components/SuccessDialog.vue'
import { getFieldErrors } from '@/http'
import * as NotificationUtils from '@/utils/NotificationUtils'
import { ViewMsg } from '@/common/MsgEnum'

const dialogShow = ref(false)
const isEdit: Ref<boolean> = ref(false)
const toggle: Ref<boolean> = ref(false)
const form: Ref<HTMLFormElement | null> = ref(null)
const editForm: Ref<EditUsersForm> = ref({
  id: '',
  name: '',
  birthday: new Date().toLocaleDateString('en-CA'),
  email: '',
  address: '',
  username: ''
})
const editUsersValidMessage: Ref<EditUsersValidMessage> = ref({
  name: [],
  birthday: [],
  email: [],
  address: []
})

async function submit(e: MouseEvent) {
  try {
    e.preventDefault()

    const valid = (await form.value!.validate()).valid
    if (!valid) {
      NotificationUtils.showErrorNotification(ViewMsg.FiledError)
      return
    }

    await UsersService.updateForEditPage(editForm.value)
    isEdit.value = false
    dialogShow.value = true
  } catch (error) {
    const fieldErrors = getFieldErrors<EditUsersValidMessage>(error)
    if (fieldErrors) {
      editUsersValidMessage.value = fieldErrors
      NotificationUtils.showErrorNotification(ViewMsg.FiledError)
      return
    }

    console.error('server error', error)
    NotificationUtils.showErrorNotification(ViewMsg.ServerError)
  }
}

async function initEditForm(): Promise<void> {
  try {
    const data = await UsersService.findEditUsers()
    editForm.value.id = data.id
    editForm.value.birthday = data.birthday
    editForm.value.email = data.email
    editForm.value.address = data.address
    editForm.value.username = data.username
    editForm.value.name = data.name
  } catch (error) {
    console.error('server error', error)
    NotificationUtils.showErrorNotification(ViewMsg.ServerError)
  }
}

onMounted(initEditForm)
</script>

<style lang="scss" scoped>
:deep(.v-field__input:read-only) {
  color: gray;
}
</style>
