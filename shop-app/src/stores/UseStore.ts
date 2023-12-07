import { defineStore } from 'pinia'

const useStore = defineStore('store', {
  state: () => {
    let isShowOverlay: boolean = false
    let beforeLoginUrl: string = ''
    return { isShowOverlay, beforeLoginUrl }
  },
  getters: {
    getShowOverlay: (state) => state.isShowOverlay,
    getBeforeLoginUrl: (state) => state.beforeLoginUrl
  },
  actions: {
    showOverlay(): void {
      this.isShowOverlay = true
    },
    hideOverlay(): void {
      this.isShowOverlay = false
    },
    setBeforeLoginUrl(url: string): void {
      this.beforeLoginUrl = url
    },
    clearBeforeLoginUrl(): void {
      this.beforeLoginUrl = ''
    }
  }
})

export default useStore
