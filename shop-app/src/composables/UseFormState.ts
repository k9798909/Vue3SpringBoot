import { type MaybeRef, ref, toValue } from 'vue'
import type { FieldError, ResponseError } from '@/composables/UseAxios.ts'
import type { AxiosRequestConfig } from 'axios'
import { useAxios } from '@/composables/UseAxios.ts'
import { AxiosError } from 'axios'

export const useFormState = <T>(initFormData?: T) => {
  const { httpRequest } = useAxios()
  const formData = ref<Partial<T>>(initFormData ?? {})
  const formError = ref<FieldError>({})

  const submit = async <T>(
    url: string,
    requestData: MaybeRef<unknown>,
    config: AxiosRequestConfig = {}
  ) => {
    try {
      formError.value = {}
      return await httpRequest<T>(url, toValue(requestData), config)
    } catch (error) {
      if (error instanceof AxiosError) {
        const errorData = error.response?.data as ResponseError
        formError.value = errorData.fieldErrors
      }
      throw error
    }
  }

  const submitFormData = async <T>(url: string, config: AxiosRequestConfig = {}) => {
    return await submit<T>(url, toValue(formData), config)
  }

  const fetchFormData = async (
    url: string,
    requestData: MaybeRef<unknown>,
    config: AxiosRequestConfig = {}
  ) => {
    formData.value = await httpRequest<T>(url, toValue(requestData), config)
  }

  return {
    formData,
    formError,
    submit,
    submitFormData,
    fetchFormData
  }
}
