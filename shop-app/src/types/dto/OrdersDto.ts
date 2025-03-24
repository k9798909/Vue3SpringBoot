export interface OrdersDto {
  orderId: number
  orderDate: string
  totalPrice: number
  status: string
  orderDetails: OrderDetailDto[]
}

export interface OrderDetailDto {
  productDto: ProductDto
  quantity: number
  price: number
}

export interface ProductDto {
  id: string
  name: string
  description: string
  price: number
  quantity: number
}
