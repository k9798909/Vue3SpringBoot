<template>
  <div>
    <v-card border class="mx-auto my-5" width="60%" min-width="400px" min-height="200px">
      <v-container>
        <v-row>
          <v-col cols="12">
            <router-link to="/product" custom v-slot="{ navigate }">
              <v-btn @click="navigate" prepend-icon="mdi-arrow-left-bold">繼續購物</v-btn>
            </router-link>
          </v-col>
        </v-row>

        <v-row v-for="(dt, index) in cart" :key="index">
          <v-col cols="12">
            <v-card border>
              <div class="d-flex flex-no-wrap justify-space-between">
                <div>
                  <v-card-title class="text-h5"> {{ dt.productName }}</v-card-title>
                  <v-card-subtitle>${{ dt.price }} 數量:{{ dt.quantity }}</v-card-subtitle>
                  <v-card-actions>
                    <v-btn
                      class="ms-2"
                      color="red"
                      variant="tonal"
                      prepend-icon="$close"
                      size="small"
                      @click="deleteCartProduct($event, dt.productId)"
                    >
                      刪除
                    </v-btn>
                  </v-card-actions>
                </div>

                <div style="width: 100px; height: 100px" class="my-auto mr-5">
                  <img :src="dt.imgUrl" class="h-100 w-100" style="border-radius: 5px" alt="" />
                </div>
              </div>
            </v-card>
          </v-col>
        </v-row>

        <v-row v-if="cart.length > 0">
          <v-col cols="12" class="d-flex flex-row-reverse">
            <v-btn class="mx-0" @click="checkout" append-icon="mdi-arrow-right-bold">結帳</v-btn>
          </v-col>
        </v-row>

        <v-row>
          <v-col cols="12">
            <v-alert
              v-if="cart.length == 0"
              class="mb-5"
              density="compact"
              type="warning"
              variant="tonal"
              >{{ message }}
            </v-alert>
          </v-col>
        </v-row>
      </v-container>
    </v-card>
  </div>
</template>
<script setup lang="ts">
import { onMounted, ref, type Ref } from 'vue'
import type CartProduct from '@/types/dto/CartProductDto'
import { useRouter, type Router } from 'vue-router'
import * as NotificationUtils from '@/utils/NotificationUtils'
import { useAxios } from '@/composables/UseAxios.ts'

const cart: Ref<CartProduct[]> = ref([])
const message = ref('讀取中...')
const router: Router = useRouter()
const { httpGet, httpDelete, httpPost } = useAxios()

const deleteCartProduct = async (e: MouseEvent, productId: string) => {
  const data = await httpDelete<CartProduct[]>(`/cart/${productId}`)
  cart.value = data.map((t) => {
    return { ...t, imgUrl: `/api/public/product/img/${t.productId}` }
  })
  if (cart.value.length == 0) {
    message.value = '購物車無商品'
    return
  }
}

const checkout = async () => {
  await httpPost('/orders')
  NotificationUtils.showSuccessNotification('結帳成功')
  await router.push('/orders')
}

const initProducts = async () => {
  const data = await httpGet<CartProduct[]>(`/cart`)
  cart.value = data.map((t: CartProduct) => {
    return { ...t, imgUrl: `/api/public/product/img/${t.productId}` }
  })

  if (cart.value.length == 0) {
    message.value = '購物車無商品'
    return
  }
}

onMounted(() => initProducts())
</script>
<style lang="scss" scoped></style>
