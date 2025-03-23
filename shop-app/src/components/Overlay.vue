<template>
  <v-overlay :model-value="overlayStore.isShowOverlay" class="align-center justify-center">
    <v-progress-circular color="primary" indeterminate size="64"></v-progress-circular>
  </v-overlay>
</template>
<script setup lang="ts">
import { useOverlayStore } from '@/stores/UseOverlayStore.ts'

const overlayStore = useOverlayStore()
</script>

<style lang="scss" scoped></style>
