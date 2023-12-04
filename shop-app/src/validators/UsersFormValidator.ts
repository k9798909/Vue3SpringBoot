import { isEmailValid, isIdnValid, maxLength, require } from '@/validators/Rules'
import * as UsersService from '@/services/UsersService'

// 檢核身分證字號
export function getIdnRules() {
  return [(v: string) => require(v, '身分證字號'), isIdnValid]
}

// 檢核姓名
export function getNameRules() {
  return [(v: string) => require(v, '姓名'), (v: string) => maxLength(v, 10)]
}

// 檢核電子郵件
export function getEmailRules() {
  return [(v: string) => require(v, '電子郵件'), isEmailValid]
}

// 檢核地址
export function getAddressRules() {
  return [(v: string) => require(v, '地址'), (v: string) => maxLength(v, 100)]
}

// 檢核帳號
export function getUsernameRules() {
  return [(v: string) => require(v, '帳號'), checkUsername, (v: string) => maxLength(v, 20)]
}

// 檢核密碼
export function getPasswordRules() {
  return [(v: string) => require(v, '密碼'), (v: string) => maxLength(v, 10)]
}

// 檢核密碼複驗
export function getChkPasswordRules() {
  return [(v: string) => require(v, '密碼複驗'), (v: string) => maxLength(v, 10)]
}

// 檢核帳號是否存在
async function checkUsername(username: string) {
  return UsersService.checkUsername(username)
    .then((data) => {
      return data || '帳號已存在'
    })
    .catch((error) => {
      console.log(`checkUsername:${checkUsername}`, error)
      return '帳號異常'
    })
}
