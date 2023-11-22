const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/

export const require = (v: string, label: string): boolean | string => !!v || `${label}為必填`

export const isEmailValid = (v: string): boolean | string =>
  emailRegex.test(v) || '電子郵件格式錯誤'

export const maxLength = (v: string, len: number): boolean | string =>
  v.length < len || `長度需小於${len}個字`

export function isIdnValid(idn: string): boolean | string {
  return /^[A-Z][12]\d{8}$/.test(idn) || '身分證格式錯誤'
}
