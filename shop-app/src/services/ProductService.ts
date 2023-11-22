import type Product from '@/types/dto/ProductDto'
import getHttp from '@/http'
import type ResponseData from '@/types/http/ResponseData'

export async function findAll(): Promise<ResponseData<Product[]>> {
  return getHttp().get('/public/product')
}

export async function findBy(id: any): Promise<ResponseData<Product>> {
  return getHttp().get(`/public/product/${id}`)
}
