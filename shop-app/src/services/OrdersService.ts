import getApiClient from '@/http'
import type { OrdersDto } from '@/types/dto/OrdersDto'
import type ResponseData from '@/types/http/ResponseData'

export async function checkout() {
  await getApiClient().post('/orders')
}

export async function getOrders(): Promise<ResponseData<OrdersDto>> {
  return await getApiClient().get('/orders')
}
