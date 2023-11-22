import axios, { type AxiosInstance } from 'axios'
import { ContentTypeEnum } from '../common/HttpEnum'
import * as UsersService from '@/services/UsersService'

const getApiClient = (options = {}): AxiosInstance => {
  return axios.create({
    baseURL: '/api',
    headers: {
      'Content-type': ContentTypeEnum.JSON,
      Authorization: 'Bearer ' + UsersService.getStoreUsers()?.token || '',
      ...options
    }
  })
}

export default getApiClient
