<script setup lang="ts">
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
import Overlay from '@/components/Overlay.vue'
</script>

<template>
  <v-layout class="rounded rounded-md">
    <Header></Header>
    <v-main class="main">
      <router-view class="w-100 py-5"></router-view>
    </v-main>
    <Footer></Footer>
    <notifications :classes="['custom-vue-notification']" position="bottom right" max="1" />
    <Overlay></Overlay>
  </v-layout>
</template>

<style lang="scss" scoped>
.main {
  min-height: 100vh;
}
</style>
