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
        @click:append-inner="(e: MouseEvent) => searchEvent(e)"
      ></v-text-field>

      <router-link to="/cart" custom v-slot="{ navigate }"
        ><v-btn class="mx-2 my-auto" @click="navigate">購物車</v-btn></router-link
      >
    </div>

    <v-parallax
      src="https://cdn.vuetifyjs.com/images/backgrounds/vbanner.jpg"
      class="my-3 mx-auto"
      max-height="200px"
      width="80%"
    >
      <div class="d-flex flex-column fill-height justify-center align-center text-white">
        <h1 class="text-h4 font-weight-thin mb-4">Vuetify</h1>
        <h4 class="subheading">Build your application today!</h4>
      </div>
    </v-parallax>

    <v-container v-if="products.length != 0" min-width="500px">
      <v-row>
        <v-col cols="12" lg="3" md="4" sm="6" v-for="product in products">
          <Product class="mx-auto" :product="product"></Product>
        </v-col>
      </v-row>
      <v-pagination :length="1"></v-pagination>
    </v-container>
  </main>
</template>

<script setup lang="ts">
import Product from '../components/Product.vue'
import { onMounted, ref, type Ref } from 'vue'
import * as ProductService from '@/services/ProductService'
import type ProductDto from '@/types/dto/ProductDto'

const searchInput: Ref<string> = ref('')
const products: Ref<ProductDto[]> = ref([])

async function initProductList(): Promise<void> {
  ProductService.findAll()
    .then((data) => {
      products.value = data
    })
    .catch((e) => {
      console.error('ProductService findByName', e)
    })
}

async function searchEvent(e: MouseEvent): Promise<void> {
  try {
    if (!searchInput.value) {
      products.value = await ProductService.findAll()
    }

    products.value = await ProductService.findByName(searchInput.value)
  } catch (error) {
    console.error('ProductService searchEvent', e)
  }
}

onMounted(initProductList)
</script>

<style lang="scss" scoped></style>
