<template>
  <v-card width="230px">
    <v-img :src="imgUrl" height="200px" cover></v-img>
    <v-card-title>
      <b>商品名稱：{{ product.name }}</b>
    </v-card-title>
    <v-card-subtitle> 售價:{{ product.price }}</v-card-subtitle>
    <v-card-text>
      <div class="card-text-content">
        {{ product.description }}
      </div>
    </v-card-text>
    <v-card-actions>
      <v-btn color="indigo" variant="elevated" @click="addCardProduct"> 加入購物車</v-btn>
      <v-btn color="indigo" variant="elevated" @click="productDetail"> 明細</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script setup lang="ts">
import type ProductDto from '@/types/dto/ProductDto'
import * as NotificationUtils from '@/utils/NotificationUtils'
import { useAxios } from '@/composables/UseAxios.ts'

const { httpPost } = useAxios()
const props = defineProps<{ product: ProductDto }>()
const imgUrl = `/api/public/product/img/${props.product.id}`

async function addCardProduct(): Promise<void> {
  httpPost('/cart', { productId: props.product.id, quantity: 1 }).then(() => {
    NotificationUtils.showSuccessNotification('加入購物車成功')
  }).catch()
}

async function productDetail(): Promise<void> {
  alert('未實作')
}
</script>

<style lang="scss" scoped>
.card-text-content {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 4;
  text-overflow: ellipsis;
  overflow: hidden;
}
</style>
