import type { AxiosHeaders } from 'axios'

export default interface ResponseData<T> {
  headers: AxiosHeaders
  data: T
}
