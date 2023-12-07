import axios, { AxiosError, type AxiosInstance } from 'axios'
import { ContentTypeEnum, NetworkErrorCode } from '../common/HttpEnum'
import * as UsersService from '@/services/UsersService'
import type ResponseError from '@/types/http/ResponseError'
import useStore from '@/stores/UseStore'

const getApiClient = (options = {}): AxiosInstance => {
  const instance = axios.create({
    baseURL: '/api',
    headers: {
      'Content-type': ContentTypeEnum.JSON,
      Authorization: 'Bearer ' + UsersService.getStoreUsers()?.token || '',
      ...options
    }
  })

  instance.interceptors.request.use(
    function (config) {
      if (config.method === 'post' || config.method === 'put' || config.method === 'delete') {
        useStore().showOverlay()
      }
      return config
    },
    function (error) {
      return Promise.reject(error)
    }
  )

  instance.interceptors.response.use(
    function (response) {
      setTimeout(() => {
        useStore().hideOverlay()
      }, 500)
      return response
    },
    function (error) {
      return Promise.reject(error)
    }
  )

  return instance
}

//如果回傳400而且fieldErrors有值，就回傳錯誤訊息。
export function getFieldErrors<T>(error: any): T | undefined {
  if (!axios.isAxiosError(error)) {
    return
  }

  const axiosError: AxiosError<ResponseError<T>> = error
  if (axiosError.response?.status !== NetworkErrorCode.BadRequest) {
    return
  }

  if (!axiosError.response?.data.fieldErrors) {
    return
  }

  return axiosError.response.data.fieldErrors
}

//是否為未授權或輸入的帳號密碼錯誤。
export function isUnauthorized(error: any): boolean {
  let axiosError: AxiosError = error as AxiosError
  return NetworkErrorCode.Unauthorized == axiosError.response?.status
}

export default getApiClient
