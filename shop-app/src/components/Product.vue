<script setup lang="ts">
import * as CartService from '@/services/CartService'
import type ProductDto from '@/types/dto/ProductDto'
import * as NotificationUtils from '@/utils/NotificationUtils'
import { handleUnauthorized } from '@/http'
import { ViewMsg } from '@/common/MsgEnum'
export interface Prop {
  product: ProductDto
}

const props = defineProps<Prop>()
const imgUrl = `/api/public/product/img/${props.product.id}`

async function addCardProduct(): Promise<void> {
  CartService.updateCartProduct(props.product.id, 1)
    .then(() => {
      NotificationUtils.showSuccessNotification('加入購物車成功')
    })
    .catch((error) => {
      if (handleUnauthorized(error)) {
        NotificationUtils.showWaringNotification(ViewMsg.NotLogin)
        return
      }
      console.error('addCardProduct error', error)
    })
}

async function productDetill(): Promise<void> {
  alert('未實作')
}
</script>
<template>
  <v-card width="230px">
    <v-img :src="imgUrl" height="200px" cover></v-img>
    <v-card-title>
      <b>商品名稱：{{ props.product.name }}</b>
    </v-card-title>
    <v-card-subtitle> 售價:{{ props.product.price }} </v-card-subtitle>
    <v-card-text>
      <div class="card-text-content">
        {{ props.product.description }}
      </div>
    </v-card-text>
    <v-card-actions>
      <v-btn color="indigo" variant="elevated" @click="addCardProduct"> 加入購物車 </v-btn>
      <v-btn color="indigo" variant="elevated" @click="productDetill"> 明細 </v-btn>
    </v-card-actions>
  </v-card>
</template>

<style lang="scss" scoped>
.card-text-content {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 4;
  text-overflow: ellipsis;
  overflow: hidden;
}
</style>
