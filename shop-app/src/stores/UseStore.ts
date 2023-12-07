import { defineStore } from 'pinia'

const useStore = defineStore('store', {
  state: () => {
    let isShowOverlay: boolean = false
    return { isShowOverlay }
  },
  getters: {
    getShowOverlay: (state) => state.isShowOverlay
  },
  actions: {
    showOverlay(): void {
      this.isShowOverlay = true
    },
    hideOverlay(): void {
      this.isShowOverlay = false
    }
  }
})

export default useStore
