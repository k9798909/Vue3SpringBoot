<template>
  <!--  成功視窗 [[ -->
  <v-dialog v-model="props.dialogShow" width="50%">
    <v-sheet
      elevation="12"
      max-width="600"
      rounded="lg"
      width="100%"
      class="pa-4 text-center mx-auto"
    >
      <v-icon class="mb-5" color="success" icon="mdi-check-circle" size="112"></v-icon>
      <h2 class="text-h5 mb-6">{{ props.title }}</h2>
      <p class="mb-4 text-medium-emphasis text-body-2">
        {{ props.content }}
      </p>
      <v-divider class="mb-4"></v-divider>
      <div class="text-end">
        <v-btn
          @click="props.btnFunction"
          class="text-none"
          color="success"
          rounded
          variant="flat"
          width="90"
        >
          {{ props.btnName }}
        </v-btn>
      </div>
    </v-sheet>
  </v-dialog>
  <!--  ]] -->
</template>

<script setup lang="ts">
export interface Prop {
  dialogShow: boolean
  title: string
  content: string
  btnName: string
  btnFunction: () => void
}

const props = defineProps<Prop>()
</script>

<style lang="scss" scoped></style>
