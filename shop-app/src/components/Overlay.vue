<template>
  <v-overlay :model-value="isShowOverlay" class="align-center justify-center">
    <v-progress-circular color="primary" indeterminate size="64"></v-progress-circular>
  </v-overlay>
</template>

<script setup lang="ts">
import useStore from '@/stores/UseStore'
import { ref, watch, type Ref } from 'vue'
const store = useStore()
const isShowOverlay: Ref<boolean> = ref(store.getShowOverlay)

watch(
  () => store.isShowOverlay,
  () => {
    isShowOverlay.value = store.getShowOverlay
  }
)
</script>

<style lang="scss" scoped></style>
