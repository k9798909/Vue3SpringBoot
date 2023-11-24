<script setup lang="ts">
import getApiClient from '@/http'
import type { OrderDetailDto, OrdersDto } from '@/types/dto/OrdersDto'
import { onMounted, ref, type Ref } from 'vue'
import OrderDetails from '@/components/OrderDetails.vue'

const orders: Ref<OrdersDto[]> = ref([])
const dialogShow: Ref<boolean> = ref(false)
const orderDetails: Ref<OrderDetailDto[]> = ref([])

onMounted(async function () {
  getApiClient()
    .get('/orders')
    .then((res) => {
      orders.value.push(...res.data)
    })
    .catch((e) => console.error(e))
})

const dtlEvent = (order: OrdersDto) => {
  dialogShow.value = true
  orderDetails.value = order.orderDetails
}

const dialogFalse = () => {
  dialogShow.value = false
}
</script>

<template>
  <div>
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
              <td><v-btn @click="dtlEvent(order)">訂單明細</v-btn></td>
            </tr>
          </tbody>
        </v-table>
      </v-col>
    </v-container>
    <!--  訂單明細視窗 [[ -->
    <v-dialog v-model="dialogShow" width="50%" :absolute="true">
      <OrderDetails :order-details="orderDetails" @dialog-false="dialogFalse" />
    </v-dialog>
    <!--  ]] -->
  </div>
</template>
<style lang="scss" scoped></style>
