import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useStore = defineStore('store', () => {
  const beforeLoginUrl = ref<string>()
  return {
    beforeLoginUrl
  }
})
