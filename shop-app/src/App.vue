<script setup lang="ts">
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
</script>

<template>
  <v-layout>
    <Header></Header>
    <v-main class="w-100">
      <router-view class="main"></router-view>
    </v-main>
    <Footer></Footer>
  </v-layout>
</template>

<style lang="scss" scoped>
.main {
  min-height: 80vh;
  padding: 1rem;
}
</style>
