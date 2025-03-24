<template>
  <v-dialog v-model="show" width="50%">
    <v-sheet
      elevation="12"
      max-width="600"
      rounded="lg"
      width="100%"
      class="pa-4 text-center mx-auto"
    >
      <v-icon class="mb-5" color="success" icon="mdi-check-circle" size="112"></v-icon>
      <h2 class="text-h5 mb-6">{{ title }}</h2>
      <p class="mb-4 text-medium-emphasis text-body-2">
        {{ content }}
      </p>
      <v-divider class="mb-4"></v-divider>
      <div class="text-end">
        <v-btn
          @click="$emit('btnFunction')"
          class="text-none"
          color="success"
          rounded
          variant="flat"
          width="90"
        >
          {{ btnName }}
        </v-btn>
      </div>
    </v-sheet>
  </v-dialog>
</template>
<script setup lang="ts">
defineProps<{
  title: string
  content: string
  btnName: string
}>()

defineEmits(['btnFunction'])

const show = defineModel('show', { default: false })
</script>

<style lang="scss" scoped></style>
