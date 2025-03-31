import axios, { AxiosError, type AxiosInstance, type AxiosRequestConfig } from 'axios'
import * as qs from 'qs'
import { type MaybeRef, toValue } from 'vue'
import useUsersStore from '@/stores/UseUsersStore.ts'
import { useOverlayStore } from '@/stores/UseOverlayStore.ts'
import { useStore } from '@/stores/UseStore.ts'
import * as NotificationUtils from '@/utils/NotificationUtils.ts'
import { ViewMsg } from '@/common/MsgEnum.ts'
import { useRouter } from 'vue-router'
import _, { upperCase } from 'lodash'

/* eslint-disable  @typescript-eslint/no-explicit-any */
export interface FieldError {
  [key: string]: any
}

export interface AxiosConfig extends AxiosRequestConfig {
  mask?: boolean //遮罩
}

export interface ResponseError {
  code: string
  message: string
  fieldErrors: FieldError
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
  const users = useUsersStore()
  const router = useRouter()

  instance.interceptors.response.use(
    function (response) {
      overlayStore.closeOverlay()
      return response
    },
    async function (error) {
      overlayStore.closeOverlay()
      const axiosError: AxiosError = error as AxiosError
      if (400 == axiosError.response?.status) {
        const errorData = axiosError.response?.data as ResponseError
        const fieldErrors = {}
        if (errorData.fieldErrors) {
          for (const [key, value] of Object.entries(errorData.fieldErrors)) {
            _.set(fieldErrors, key, value)
          }
        }
        errorData.fieldErrors = fieldErrors
      } else if (401 == axiosError.response?.status) {
        store.beforeLoginUrl = router.currentRoute.value.fullPath
        NotificationUtils.showWaringNotification(ViewMsg.NotLogin)
        users.logout()
        await router.push('/login')
      } else if (422 == axiosError.response?.status) {
        const data = axiosError.response?.data as ResponseError
        NotificationUtils.showErrorNotification(data.message)
      } else {
        console.error('server error', error)
        NotificationUtils.showErrorNotification(ViewMsg.ServerError)
      }
      return Promise.reject(error)
    }
  )
}

//建立axios實體
const createInstance = (): AxiosInstance => {
  const instance: AxiosInstance = axios.create({
    baseURL:'/api',
    paramsSerializer:(params) =>
      qs.stringify(params, {
        arrayFormat: 'brackets', //解決陣列問題
        allowDots: true //解決物件問題
      }),
    headers : {
      'Content-type': 'application/json'
    }
  })
  requestInterceptors(instance)
  responseInterceptors(instance)
  return instance
}

const axiosInstance = createInstance()
export const useAxios = () => {
  const usersStore = useUsersStore()
  const overlayStore = useOverlayStore()

  const httpGet = async <T = unknown>(
    url: string,
    params?: MaybeRef<unknown>,
    config: AxiosConfig = {}
  ) => {
    config.method = 'GET'
    return httpRequest<T>(url, params, config)
  }

  const httpPost = async <T = unknown>(
    url: string,
    data?: MaybeRef<unknown>,
    config: AxiosConfig = {}
  ) => {
    config.method = 'POST'
    return httpRequest<T>(url, data, config)
  }

  const httpPut = async <T = unknown>(
    url: string,
    data?: MaybeRef<unknown>,
    config: AxiosConfig = {}
  ) => {
    config.method = 'PUT'
    return httpRequest<T>(url, data, config)
  }

  const httpDelete = async <T = unknown>(
    url: string,
    params?: MaybeRef<unknown>,
    config: AxiosConfig = {}
  ) => {
    config.method = 'DELETE'
    return httpRequest<T>(url, params, config)
  }

  const httpRequest = async <T = unknown>(
    url: string,
    requestData?: MaybeRef<unknown>,
    config: AxiosConfig = {}
  ) => {
    if (config.mask == undefined || config.mask) {
      overlayStore.openOverlay()
    }

    if (!config.method) {
      config.method = 'GET'
    }

    if (!config.headers) {
      config.headers = {}
    }

    config.headers.Authorization = 'Bearer ' + usersStore.users?.token

    if (['GET', 'DELETE'].includes(upperCase(config.method!))) {
      config.params = toValue(requestData)
    }

    switch (upperCase(config.method!)) {
      case 'GET':
        return (await axiosInstance.get<T>(url, config)).data
      case 'POST':
        return (await axiosInstance.post<T>(url, toValue(requestData), config)).data
      case 'PUT':
        return (await axiosInstance.put<T>(url, toValue(requestData), config)).data
      case 'DELETE':
        return (await axiosInstance.delete<T>(url, config)).data
      default:
        throw new Error(`Unsupported HTTP method: ${config.method}`)
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
