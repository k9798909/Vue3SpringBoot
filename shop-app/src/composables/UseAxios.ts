import axios, {
  type AxiosInstance,
  type AxiosRequestConfig,
  type AxiosResponse,
  mergeConfig
} from 'axios'
import * as qs from 'qs'
import { type MaybeRef, toValue } from 'vue'
import useUsersStore from '@/stores/UseUsersStore.ts'

/* eslint-disable  @typescript-eslint/no-explicit-any */
export interface FieldError {
  [key: string]: any
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
  instance.interceptors.response.use(
    function (response) {
      return response
    },
    function (error) {
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
  const httpGet = async <T = unknown, R = AxiosResponse<T>>(
    url: string,
    params?: MaybeRef<unknown>,
    config: AxiosRequestConfig = {}
  ): Promise<R> => {
    return httpRequest(url, params, 'GET', config)
  }

  const httpPost = async <T = unknown, R = AxiosResponse<T>>(
    url: string,
    data?: MaybeRef<unknown>,
    config: AxiosRequestConfig = {}
  ): Promise<R> => {
    return httpRequest(url, data, 'POST', config)
  }

  const httpPut = async <T = unknown, R = AxiosResponse<T>>(
    url: string,
    data?: MaybeRef<unknown>,
    config: AxiosRequestConfig = {}
  ): Promise<R> => {
    return httpRequest(url, data, 'PUT', config)
  }

  const httpDelete = async <T = unknown, R = AxiosResponse<T>>(
    url: string,
    params?: MaybeRef<unknown>,
    config: AxiosRequestConfig = {}
  ): Promise<R> => {
    return httpRequest(url, params, 'DELETE', config)
  }

  const httpRequest = async <T = unknown, R = AxiosResponse<T>>(
    url: string,
    requestData?: MaybeRef<unknown>,
    method: 'GET' | 'POST' | 'PUT' | 'DELETE' = 'POST',
    config: AxiosRequestConfig = {}
  ): Promise<R> => {
    mergeConfig(config, { headers: { Authorization: 'Bearer ' + usersStore.users?.token } })
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
