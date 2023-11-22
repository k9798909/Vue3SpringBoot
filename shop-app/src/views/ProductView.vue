<script setup lang="ts">
import Product from '../components/Product.vue'
import { onMounted, watch, ref, type Ref } from 'vue'
import * as ProductService from '@/services/ProductService'
import type ProductDto from '@/types/dto/ProductDto'
import SuccessDialog from '@/components/SuccessDialog.vue'

const allProduct: ProductDto[] = []
let searchResult: Ref<boolean> = ref(true)
let searchInput: Ref<string> = ref('')
let products: Ref<ProductDto[]> = ref([])
let successAlert: Ref<boolean> = ref(false)

async function loadProductList(): Promise<void> {
  try {
    allProduct.push(...(await ProductService.findAll()).data)
    products.value.push(...allProduct)
  } catch (e) {
    console.error('掛載所有商品時發生錯誤', e)
  }
}

function searchEvent(e: MouseEvent): void {
  products.value = allProduct.filter((t) => t.name.includes(searchInput.value))
}

onMounted(loadProductList)

watch(
  () => products.value,
  () => {
    searchResult.value = products.value.length != 0
  },
  { deep: true }
)
</script>

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
          <Product class="mx-auto" :product="product" v-model:successAlert="successAlert"></Product>
        </v-col>
      </v-row>
      <v-pagination :length="1"></v-pagination>
    </v-container>

    <!--  成功視窗 [[ -->
    <SuccessDialog
      :dialogShow="successAlert"
      title="加入購物車成功"
      btnName="確定"
      content="加入購物車成功，請至購物車頁面查看。"
      :btnFunction="() => (successAlert = !successAlert)"
    />
    <!--  ]] -->
  </main>
</template>
<style lang="scss" scoped></style>
