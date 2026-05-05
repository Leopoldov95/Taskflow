export type AuthMode = 'signIn' | 'register'

export type FormValues = {
  email: string
  password: string
  firstName?: string
  lastName?: string
  confirmPassword?: string
}
