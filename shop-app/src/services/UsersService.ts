import type { LoginForm } from '@/types/form/LoginForm'
import type ResponseData from '@/types/http/ResponseData'
import getHttp from '@/http'
import useUsersStore from '@/stores/UseUsersStore'
import type Users from '@/types/stores/Users'
import type { SignUpForm } from '@/types/form/SignUpForm'
import type { EditUsersForm } from '@/types/form/EditUsersForm'
import type LoginResDto from '@/types/dto/LoginResDto'

export async function login(loginDto: LoginForm): Promise<void> {
  const res: ResponseData<LoginResDto> = await getHttp().post('/login', loginDto)
  useUsersStore().login(res.data, res.headers['authorization'])
}

export function logout(): void {
  useUsersStore().logout()
}

export function getStoreUsers(): Users | null {
  return useUsersStore().getUsers
}

//檢查token是否過期。
export async function verifyToken(token: string): Promise<boolean> {
  return (await getHttp().post('/public/tokenVerify', { token })).data
}

export async function signUp(form: SignUpForm): Promise<void> {
  await getHttp().post('public/users/signUp', form)
}

export async function checkUsername(username: string): Promise<boolean> {
  return (await getHttp().get('public/users/checkUsername', { params: { username } })).data
}

export async function findEditUsers(): Promise<EditUsersForm> {
  return (await getHttp().get(`/users/edit`)).data
}

export async function updateForEditPage(form: EditUsersForm): Promise<void> {
  await getHttp().post(`/users/edit`, form)
}
