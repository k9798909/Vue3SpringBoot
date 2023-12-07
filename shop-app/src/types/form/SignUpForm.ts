export interface SignUpForm {
  name: string
  birthday: string
  email: string
  address: string
  username: string
  password: string
  chkPassword: string
}

export interface SignUpValidMessage {
  name: string[]
  email: string[]
  address: string[]
  username: string[]
  password: string[]
  chkPassword: string[]
}
