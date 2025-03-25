<template>
  <v-sheet class="mx-auto" width="600">
    <v-form class="d-flex flex-column ga-3">
      <h2 class="text-center">個人資料</h2>
      <div class="d-flex justify-end w-100">
        <v-btn-toggle v-model="toggle" divided>
          <v-btn variant="text" @click="() => (isEdit = !isEdit)">
            <v-icon icon="mdi-square-edit-outline" />
            編輯
          </v-btn>
        </v-btn-toggle>
      </div>
      <TextField
        type="text"
        id="username"
        placeholder="輸入帳號"
        label="帳號"
        variant="outlined"
        readonly
        v-model="formData.name"
      />
      <TextField
        type="text"
        id="name"
        placeholder="輸入姓名"
        label="姓名"
        variant="outlined"
        :readonly="!isEdit"
        v-model="formData.name"
        :customErrorMessage="formError?.name"
      />
      <TextField
        label="出生日期"
        type="date"
        id="birthday"
        variant="outlined"
        :readonly="!isEdit"
        v-model="formData.birthday"
      />
      <TextField
        type="email"
        id="email"
        variant="outlined"
        label="電子信箱"
        placeholder="輸入電子信箱"
        :readonly="!isEdit"
        v-model="formData.email"
        :customErrorMessage="formError?.email"
      />
      <TextField
        type="text"
        id="address"
        variant="outlined"
        label="地址"
        placeholder="輸入地址"
        :readonly="!isEdit"
        v-model="formData.address"
        :customErrorMessage="formError?.address"
      />

      <v-btn
        v-if="isEdit"
        type="submit"
        @click="submit"
        class="w-100 btn btn-success btn-lg"
        color="indigo"
        variant="elevated"
        >修改
      </v-btn>
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
  </v-sheet>
</template>
<script setup lang="ts">
import { onMounted, ref, type Ref } from 'vue'
import type { EditUsersForm } from '@/types/form/EditUsersForm'
import SuccessDialog from '@/components/SuccessDialog.vue'
import { useFormState } from '@/composables/UseFormState.ts'
import TextField from '@/components/TextField.vue'

const dialogShow = ref(false)
const isEdit: Ref<boolean> = ref(false)
const toggle: Ref<boolean> = ref(false)
const { formData, formError, fetchFormData, submitFormData } = useFormState<EditUsersForm>()

const submit = async (e: MouseEvent) => {
  e.preventDefault()
  await submitFormData('/users/edit', { method: 'POST' })
  isEdit.value = false
  dialogShow.value = true
}

const initFormData = async () => {
  await fetchFormData('/users/edit', {}, { method: 'GET' })
}

onMounted(() => initFormData())
</script>
<style lang="scss" scoped>
:deep(.v-field__input:read-only) {
  color: gray;
}
</style>
