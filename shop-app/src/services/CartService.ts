import getApiClient from '@/http'
import type { CartDto } from '@/types/dto/CartDto'
import type CartProduct from '@/types/dto/CartProductDto'
import type ProductDto from '@/types/dto/ProductDto'
import * as ProductService from './ProductService'

export async function getCartProductList(): Promise<CartProduct[]> {
  let cartProduct: CartProduct[] = []
  let cartDto: CartDto[] = (await getApiClient().get(`/cart`)).data
  for (let dto of cartDto) {
    try {
      let product: ProductDto = await ProductService.findBy(dto.productId)
      cartProduct.push({
        productId: dto.productId,
        productName: product.name,
        price: product.price,
        quantity: 1,
        imgUrl: `/api/public/product/img/${dto.productId}`
      })
    } catch (error) {
      console.error(error)
    }
  }
  return cartProduct
}

export async function updateCartProduct(productId: string, quantity: number): Promise<void> {
  const updCart = {
    productId,
    quantity
  }
  await getApiClient().post('/cart', updCart)
}

export async function deleteCartProduct(productId: String): Promise<CartProduct[]> {
  let cartProduct: CartProduct[] = []
  let cartDto: CartDto[] = (await getApiClient().delete(`/cart/${productId}`)).data
  for (let dto of cartDto) {
    try {
      let product: ProductDto = await ProductService.findBy(dto.productId)
      cartProduct.push({
        productId: dto.productId,
        productName: product.name,
        price: product.price,
        quantity: 1,
        imgUrl: `/api/public/product/img/${dto.productId}`
      })
    } catch (error) {
      console.error(error)
    }
  }

  return cartProduct
}
