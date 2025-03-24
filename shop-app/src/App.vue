<template>
  <v-app>
    <AppHeader class="header"></AppHeader>
    <v-main scrollable>
      <v-container class="main-container">
        <router-view></router-view>
      </v-container>
      <Footer class="footer"></Footer>
    </v-main>
    <Notifications :classes="['custom-vue-notification']" position="bottom right" :max="1" />
    <Overlay></Overlay>
  </v-app>
</template>

<script setup lang="ts">
import AppHeader from '@/components/AppHeader.vue'
import Footer from '@/components/Footer.vue'
import Overlay from '@/components/Overlay.vue'
import { Notifications } from '@kyvg/vue3-notification'
</script>

<style lang="scss" scoped>
.header {
  height: 70px;
}

.main-container {
  min-height: calc(100vh - 70px - 70px);
}
</style>
