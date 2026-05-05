import { useMutation } from '@tanstack/react-query'
import { toast } from 'sonner'
import { login as loginApi, register as registerApi } from '@/api/auth'

type LoginData = {
  email: string
  password: string
}

type RegisterData = {
  firstName: string
  lastName: string
  email: string
  password: string
}

export function useAuth() {
  const loginMutation = useMutation({
    mutationFn: async (data: LoginData) => {
      return loginApi(data)
    },
    onSuccess: () => {
      toast.success('Successfully signed in!')
    },
    onError: (error) => {
      const errorMessage = JSON.parse(error.message)
      errorMessage?.message
        ? toast.error(`Sign in failed: ${errorMessage.message}`)
        : toast.error('Failed to sign in. Please try again.')

      console.error('Login error:', error)
    },
  })

  const registerMutation = useMutation({
    mutationFn: async (data: RegisterData) => {
      return registerApi(data)
    },
    onSuccess: () => {
      toast.success('Account created successfully!')
    },
    onError: (error) => {
      toast.error('Failed to create account. Please try again.')
      console.error('Register error:', error)
    },
  })

  return {
    login: loginMutation.mutateAsync,
    register: registerMutation.mutateAsync,
    isLoading: loginMutation.isPending || registerMutation.isPending,
  }
}
