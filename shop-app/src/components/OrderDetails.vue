<template>
  <v-container class="bg-white" style="border-radius: 10px">
    <v-col>
      <h2 class="text-center border-1">訂單明細</h2>
    </v-col>
    <v-col cols="12" v-for="dt in props.orderDetails">
      <v-card border>
        <div class="d-flex flex-no-wrap justify-space-between p-5">
          <div>
            <v-card-title class="text-h5"> {{ dt.productDto.name }} </v-card-title>
            <v-card-subtitle>
              <div class="py-1">價格:{{ dt.price }} 數量:{{ dt.quantity }}</div>
            </v-card-subtitle>
          </div>

          <div style="width: 100px; height: 120px" class="my-auto mr-5 py-3">
            <img
              :src="'/api/public/product/img/' + dt.productDto.id"
              class="h-100 w-100"
              style="border-radius: 5px"
            />
          </div>
        </div>
      </v-card>
    </v-col>
    <v-btn variant="plain" @click="$emit('dialogFalse')" block>返回</v-btn>
  </v-container>
</template>

<script setup lang="ts">
import type { OrderDetailDto } from '@/types/dto/OrdersDto'

const props = defineProps<{
  orderDetails: OrderDetailDto[]
}>()
</script>

<style lang="scss" scoped></style>
