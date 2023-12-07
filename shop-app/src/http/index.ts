import axios, { AxiosError, type AxiosInstance } from 'axios'
import { ContentTypeEnum, NetworkErrorCode } from '../common/HttpEnum'
import * as UsersService from '@/services/UsersService'
import type ResponseError from '@/types/http/ResponseError'
import useStore from '@/stores/UseStore'
import router from '@/router'

// 建立一個axios實體，並設定預設的baseURL、header、interceptors。
const getApiClient = (options = {}): AxiosInstance => {
  // 建立axios實體
  const instance: AxiosInstance = createInstance(options)
  // 設定req攔截器啟動遮罩
  requestInterceptors(instance)
  // 設定res攔截器關閉遮罩
  responseInterceptors(instance)
  return instance
}

//建立axios實體
function createInstance(options = {}): AxiosInstance {
  return axios.create({
    baseURL: '/api',
    headers: {
      'Content-type': ContentTypeEnum.JSON,
      Authorization: 'Bearer ' + UsersService.getStoreUsers()?.token || '',
      ...options
    }
  })
}

// 設定req攔截器啟動遮罩
function requestInterceptors(instance: AxiosInstance) {
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
}

// 設定res攔截器關閉遮罩
function responseInterceptors(instance: AxiosInstance) {
  const hideOverlay = () => {
    setTimeout(() => {
      useStore().hideOverlay()
    }, 100)
  }

  instance.interceptors.response.use(
    function (response) {
      hideOverlay()
      return response
    },
    function (error) {
      hideOverlay()
      console.error(error)
      return Promise.reject(error)
    }
  )
}

//如果回傳400而且fieldErrors有值，就回傳錯誤欄位物件。
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

// 處理未授權或未登入統一跳轉到登入頁面並且記錄當前頁面，登入之後跳轉到紀錄頁面。
export function handleUnauthorized(error: any) {
  const handle = isUnauthorized(error)
  if (handle) {
    useStore().setBeforeLoginUrl(router.currentRoute.value.fullPath)
    router.push('/login')
  }
  return handle
}

//是否為未授權或輸入的帳號密碼錯誤。
export function isUnauthorized(error: any): boolean {
  let axiosError: AxiosError = error as AxiosError
  return NetworkErrorCode.Unauthorized == axiosError.response?.status
}

export default getApiClient
