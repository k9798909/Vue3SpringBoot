<template>
  <main>
    <div class="mx-auto w-50 d-flex">
      <v-text-field
        density="compact"
        variant="solo"
        label="搜尋要找的商品"
        append-inner-icon="mdi-magnify"
        single-line
        hide-details
        v-model="searchInput"
        @click:append-inner="() => searchEvent()"
      ></v-text-field>

      <router-link to="/cart" custom v-slot="{ navigate }">
        <v-btn class="mx-2 my-auto" @click="navigate">購物車</v-btn>
      </router-link>
    </div>

    <v-parallax
      src="https://cdn.vuetifyjs.com/images/backgrounds/vbanner.jpg"
      class="my-3 mx-auto"
      max-height="250"
      min-width="900"
      max-width="1300"
    >
      <div class="d-flex flex-column fill-height justify-center align-center text-white">
        <h1 class="text-h4 font-weight-thin mb-4">Vuetify</h1>
        <h4 class="subheading">Build your application today!</h4>
      </div>
    </v-parallax>

    <v-container class="d-flex flex-wrap ga-10" v-if="products.length != 0" min-width="900" max-width="1200">
      <div :key="index" v-for="(product, index) in products">
        <Product :product="product"></Product>
      </div>
    </v-container>
    <v-pagination :length="1"></v-pagination>
  </main>
</template>
<script setup lang="ts">
import Product from '../components/Product.vue'
import { onMounted, ref } from 'vue'
import type ProductDto from '@/types/dto/ProductDto'
import { useAxios } from '@/composables/UseAxios.ts'

const searchInput = ref<string>('')
const products = ref<ProductDto[]>([])
const { httpGet } = useAxios()

const initProductList = async () => {
  products.value = await httpGet<ProductDto[]>('/public/product')
}

const searchEvent = async () => {
  if (!searchInput.value) {
    products.value = await httpGet<ProductDto[]>('/public/product')
    return
  }
  products.value = await httpGet<ProductDto[]>(`/public/product/name/${searchInput.value}`)
}

onMounted(() => initProductList())
</script>
<style lang="scss" scoped></style>
