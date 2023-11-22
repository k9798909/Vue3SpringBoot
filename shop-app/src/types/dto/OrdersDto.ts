export interface OrdersDto {
  orderId: number
  orderDate: String
  totalPrice: number
  status: String
  orderDetails: OrderDetailDto[]
}

export interface OrderDetailDto {
  productDto: ProductDto
  quantity: number
  price: number
}

export interface ProductDto {
  id: String
  name: String
  description: String
  price: number
  quantity: number
}
