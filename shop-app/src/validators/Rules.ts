export function require(v: string, label: string): boolean | string {
  return !!v || `${label}為必填`
}

const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/
export function isEmailValid(v: string): boolean | string {
  return emailRegex.test(v) || '電子郵件格式錯誤'
}

export function maxLength(v: string, len: number): boolean | string {
  return v.length < len || `長度需小於${len}個字`
}

export function isIdnValid(idn: string): boolean | string {
  return /^[A-Z][12]\d{8}$/.test(idn) || '身分證格式錯誤'
}
