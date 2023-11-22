import { isEmailValid, isIdnValid, maxLength, require } from '@/validate/Rules'
import * as UsersService from '@/services/UsersService'
import type EditUsersForm from '@/types/form/EditUsersForm'

export function buildFormData(): EditUsersForm {
  return {
    name: '',
    birthday: new Date().toLocaleDateString('en-CA'),
    email: '',
    address: '',
    username: '',
    id: ''
  }
}

export function getIdnRules() {
  return [(v: string) => require(v, '身分證字號'), isIdnValid]
}

export function getNameRules() {
  return [(v: string) => require(v, '姓名'), (v: string) => maxLength(v, 10)]
}

export function getEmailRules() {
  return [(v: string) => require(v, '電子郵件'), isEmailValid]
}

export function getAddressRules() {
  return [(v: string) => require(v, '地址'), (v: string) => maxLength(v, 100)]
}

export function getUsernameRules() {
  return [(v: string) => require(v, '帳號'), checkUsername, (v: string) => maxLength(v, 20)]
}

export function getPasswordRules() {
  return [(v: string) => require(v, '密碼'), (v: string) => maxLength(v, 10)]
}

export function getChkPasswordRules() {
  return [(v: string) => require(v, '密碼複驗'), (v: string) => maxLength(v, 10)]
}

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
