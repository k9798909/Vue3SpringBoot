<template>
  <v-app>
    <AppHeader></AppHeader>
    <v-main>
      <router-view class="py-5"></router-view>
    </v-main>
    <Footer></Footer>
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

<style lang="scss" scoped></style>
