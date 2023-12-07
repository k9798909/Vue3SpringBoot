<script setup lang="ts">
import * as CartService from '@/services/CartService'
import { onMounted, ref, type Ref } from 'vue'
import type CartProduct from '@/types/dto/CartProductDto'
import * as OrdersService from '@/services/OrdersService'
import { useRouter, type Router } from 'vue-router'
import * as NotificationUtils from '@/utils/NotificationUtils'
import { handleUnauthorized } from '@/http'
import { ViewMsg } from '@/common/MsgEnum'

const cart: Ref<CartProduct[]> = ref([])
const message = ref('讀取中...')
const router: Router = useRouter()

async function initProducts() {
  CartService.getCartProductList()
    .then((cartList) => {
      cart.value.push(...cartList)
      if (cart.value.length == 0) {
        message.value = '購物車無商品'
      }
    })
    .catch((error) => {
      if (handleUnauthorized(error)) {
        NotificationUtils.showWaringNotification(ViewMsg.NotLogin)
        return
      }
      console.error('cartService getCartList error:', error)
    })
}

async function deleteCartProduct(e: MouseEvent, productId: string) {
  CartService.deleteCartProduct(productId)
    .then((res) => {
      console.log('deleteCartProduct res:', res)
      cart.value = res
      if (cart.value.length == 0) {
        message.value = '購物車無商品'
      }
    })
    .catch((error) => {
      if (handleUnauthorized(error)) {
        NotificationUtils.showWaringNotification(ViewMsg.NotLogin)
        return
      }
      console.error('cartService deleteCartProduct error:', e)
      NotificationUtils.showErrorNotification(ViewMsg.ServerError)
    })
}

async function checkout() {
  OrdersService.checkout()
    .then((res) => {
      NotificationUtils.showSuccessNotification('結帳成功')
      router.push('/orders')
    })
    .catch((e) => {
      NotificationUtils.showErrorNotification('結帳失敗')
      console.error(e)
    })
}

onMounted(initProducts)
</script>

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

        <v-row v-for="dt in cart">
          <v-col cols="12">
            <v-card border>
              <div class="d-flex flex-no-wrap justify-space-between">
                <div>
                  <v-card-title class="text-h5"> {{ dt.productName }} </v-card-title>
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
                  <img :src="dt.imgUrl" class="h-100 w-100" style="border-radius: 5px" />
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
              >{{ message }}</v-alert
            >
          </v-col>
        </v-row>
      </v-container>
    </v-card>
  </div>
</template>

<style lang="scss" scoped></style>
