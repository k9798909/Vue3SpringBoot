import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useOverlayStore = defineStore('overlayStore', () => {
  const isShowOverlay = ref<boolean>(false)
  const cnt = ref(0)
  const closeOverlay = () => {
    if (cnt.value > 0) {
      cnt.value = cnt.value - 1
    }
    if (cnt.value == 0) {
      isShowOverlay.value = false
    }
  }
  const openOverlay = () => {
    cnt.value = cnt.value + 1
    isShowOverlay.value = true
  }
  return {
    isShowOverlay,
    cnt,
    closeOverlay,
    openOverlay
  }
})
