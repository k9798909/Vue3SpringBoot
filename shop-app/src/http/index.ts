import axios, { type AxiosInstance } from 'axios'
import { ContentTypeEnum } from '../common/HttpEnum'
import * as UsersService from '@/services/UsersService'

const getApiClient = (options = {}): AxiosInstance => {
  const instance = axios.create({
    baseURL: '/api',
    headers: {
      'Content-type': ContentTypeEnum.JSON,
      Authorization: 'Bearer ' + UsersService.getStoreUsers()?.token || '',
      ...options
    }
  })

  return instance
}

export default getApiClient
