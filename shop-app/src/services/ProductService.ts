import type Product from '@/types/dto/ProductDto'
import getHttp from '@/http'

export async function findAll(): Promise<Product[]> {
  return (await getHttp().get('/public/product')).data
}

export async function findBy(id: any): Promise<Product> {
  return (await getHttp().get(`/public/product/${id}`)).data
}

export async function findByName(name: string): Promise<Product[]> {
  return (await getHttp().get(`/public/product/name/${name}`)).data
}
