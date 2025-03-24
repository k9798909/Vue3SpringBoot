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
          <tr v-for="(order, index) in orders" :key="index">
            <td>{{ order.orderId }}</td>
            <td>{{ order.status }}</td>
            <td>{{ order.totalPrice }}</td>
            <td>{{ order.orderDate }}</td>
            <td>
              <v-btn @click="dtlEvent(order)">訂單明細</v-btn>
            </td>
          </tr>
        </tbody>
      </v-table>
    </v-col>
    <!--  訂單明細視窗 [[ -->
    <v-dialog v-model="dialogShow" :fullscreen="true" content-class="pt-5">
      <OrderDetails class="w-50" :order-details="orderDetails" @dialog-false="dialogFalse" />
    </v-dialog>
    <!--  ]] -->
  </v-container>
</template>
<script setup lang="ts">
import type { OrderDetailDto, OrdersDto } from '@/types/dto/OrdersDto'
import { onMounted, ref } from 'vue'
import OrderDetails from '@/components/OrderDetails.vue'
import { useAxios } from '@/composables/UseAxios'

const { httpGet } = useAxios()
const orders = ref<OrdersDto[]>([])
const dialogShow = ref<boolean>(false)
const orderDetails = ref<OrderDetailDto[]>([])

const initOrders = async () => {
  orders.value = await httpGet<OrdersDto[]>('/orders')
}

const dtlEvent = (order: OrdersDto) => {
  dialogShow.value = true
  orderDetails.value = order.orderDetails
}

const dialogFalse = () => {
  dialogShow.value = false
}

onMounted(() => initOrders())
</script>
<style lang="scss" scoped></style>
