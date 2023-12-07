import getApiClient from '@/http'
import * as UsersService from './UsersService'
import type { CartDto } from '@/types/dto/CartDto'
import type CartProduct from '@/types/dto/CartProductDto'
import type ProductDto from '@/types/dto/ProductDto'
import NotLoginError from '@/common/NotLoginError'
import * as ProductService from './ProductService'
import type ResponseData from '@/types/http/ResponseData'

export async function getCartProductList(): Promise<CartProduct[]> {
  const users = UsersService.getStoreUsers()
  if (!users) {
    throw new NotLoginError('未登入')
  }

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
  const users = UsersService.getStoreUsers()
  if (!users) {
    throw new NotLoginError('未登入')
  }

  const isVerify = await UsersService.verifyToken(users.token)
  if (!isVerify) {
    UsersService.logout()
    throw new NotLoginError('登入已逾期')
  }

  const updCart = {
    productId,
    quantity
  }
  await getApiClient().post('/cart', updCart)
}

export async function deleteCartProduct(productId: String): Promise<CartProduct[]> {
  const users = UsersService.getStoreUsers()
  if (!users) {
    throw new NotLoginError('未登入')
  }

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
