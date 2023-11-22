<script setup lang="ts">
import getApiClient from '@/http'
import type { OrdersDto } from '@/types/dto/OrdersDto'
import { onMounted, ref, type Ref } from 'vue'

const orders: Ref<OrdersDto[]> = ref([])

onMounted(async function () {
  getApiClient()
    .get('/orders')
    .then((res) => {
      console.log(res.data)
      orders.value.push(...res.data)
    })
    .catch((e) => console.error(e))
})
</script>

<template>
  <v-container>
    <v-col>
      <v-table>
        <thead>
          <tr>
            <th class="text-left">訂單編號</th>
            <th class="text-left">狀態</th>
            <th class="text-left">總價</th>
            <th class="text-left">訂單日期</th>
            <th class="text-left">明細</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in orders">
            <td>{{ order.orderId }}</td>
            <td>{{ order.status }}</td>
            <td>{{ order.totalPrice }}</td>
            <td>{{ order.orderDate }}</td>
            <td><v-btn>訂單明細</v-btn></td>
          </tr>
        </tbody>
      </v-table>
    </v-col>
  </v-container>
</template>
<style lang="scss" scoped></style>
