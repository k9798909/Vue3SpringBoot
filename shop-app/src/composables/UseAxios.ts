import axios, { AxiosError, type AxiosInstance, type AxiosRequestConfig, mergeConfig } from 'axios'
import * as qs from 'qs'
import { type MaybeRef, toValue } from 'vue'
import useUsersStore from '@/stores/UseUsersStore.ts'
import { useOverlayStore } from '@/stores/UseOverlayStore.ts'
import { useStore } from '@/stores/UseStore.ts'
import * as NotificationUtils from '@/utils/NotificationUtils.ts'
import { ViewMsg } from '@/common/MsgEnum.ts'
import { useRouter } from 'vue-router'

/* eslint-disable  @typescript-eslint/no-explicit-any */
export interface FieldError {
  [key: string]: any
}

export interface AxiosConfig extends AxiosRequestConfig {
  mask?: boolean //遮罩
}

const requestInterceptors = (instance: AxiosInstance) => {
  instance.interceptors.request.use(
    function (config) {
      return config
    },
    function (error) {
      return Promise.reject(error)
    }
  )
}

const responseInterceptors = (instance: AxiosInstance) => {
  const overlayStore = useOverlayStore()
  const store = useStore()
  const router = useRouter()
  const users = useUsersStore()

  instance.interceptors.response.use(
    function (response) {
      overlayStore.closeOverlay()
      return response.data
    },
    async function(error) {
      const axiosError: AxiosError = error as AxiosError
      if (400 == axiosError.response?.status) {
        NotificationUtils.showErrorNotification(ViewMsg.FiledError)
      } else if (401 == axiosError.response?.status) {
        store.beforeLoginUrl = router.currentRoute.value.fullPath
        NotificationUtils.showWaringNotification(ViewMsg.NotLogin)
        users.logout()
        await router.push('/login')
      } else if (422 == axiosError.response?.status) {
        const data = axiosError.response?.data as { message: string }
        NotificationUtils.showErrorNotification(data.message)
      } else {
        console.error('server error', error)
        NotificationUtils.showErrorNotification(ViewMsg.ServerError)
      }
      overlayStore.closeOverlay()
      return Promise.reject(error)
    }
  )
}

//建立axios實體
const createInstance = (config: AxiosRequestConfig = {}): AxiosInstance => {
  config.baseURL = '/api'
  config.paramsSerializer = (params) =>
    qs.stringify(params, {
      arrayFormat: 'brackets', //解決陣列問題
      allowDots: true //解決物件問題
    })
  if (!config.headers) {
    config.headers = {
      'Content-type': 'application/json'
    }
  }
  const instance: AxiosInstance = axios.create(config)
  requestInterceptors(instance)
  responseInterceptors(instance)
  return instance
}

export const useAxios = () => {
  const usersStore = useUsersStore()
  const axiosInstance = createInstance()
  const overlayStore = useOverlayStore()

  const httpGet = async <T = unknown, R = T>(
    url: string,
    params?: MaybeRef<unknown>,
    config: AxiosConfig = {}
  ): Promise<R> => {
    return httpRequest(url, params, 'GET', config)
  }

  const httpPost = async <T = unknown, R = T>(
    url: string,
    data?: MaybeRef<unknown>,
    config: AxiosConfig = {}
  ): Promise<R> => {
    return httpRequest(url, data, 'POST', config)
  }

  const httpPut = async <T = unknown, R = T>(
    url: string,
    data?: MaybeRef<unknown>,
    config: AxiosConfig = {}
  ): Promise<R> => {
    return httpRequest(url, data, 'PUT', config)
  }

  const httpDelete = async <T = unknown, R = T>(
    url: string,
    params?: MaybeRef<unknown>,
    config: AxiosConfig = {}
  ): Promise<R> => {
    return httpRequest(url, params, 'DELETE', config)
  }

  const httpRequest = async <T = unknown, R = T>(
    url: string,
    requestData?: MaybeRef<unknown>,
    method: 'GET' | 'POST' | 'PUT' | 'DELETE' = 'POST',
    config: AxiosConfig = {}
  ): Promise<R> => {
    if (config.mask == undefined || config.mask) {
      overlayStore.openOverlay()
    }

    config = mergeConfig(config, { headers: { Authorization: 'Bearer ' + usersStore.users?.token } })
    if (['GET', 'DELETE'].includes(method)) {
      config.params = toValue(requestData)
    }

    switch (method) {
      case 'GET':
        return axiosInstance.get<T, R>(url, config)
      case 'POST':
        return axiosInstance.post<T, R>(url, toValue(requestData), config)
      case 'PUT':
        return axiosInstance.put<T, R>(url, toValue(requestData), config)
      case 'DELETE':
        return axiosInstance.delete<T, R>(url, config)
      default:
        throw new Error(`Unsupported HTTP method: ${method}`)
    }
  }

  return {
    httpGet,
    httpPost,
    httpPut,
    httpDelete,
    httpRequest
  }
}
