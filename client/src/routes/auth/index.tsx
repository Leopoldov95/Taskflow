import { createFileRoute, useNavigate } from '@tanstack/react-router'
import { useState } from 'react'
import AuthForm from '#/components/auth/AuthForm'
import { useAuth } from '#/hooks/useAuth'
import type { AuthMode, FormValues } from '@/types/auth'

export const Route = createFileRoute('/auth/')({
  component: Auth,
})

function Auth() {
  const [mode, setMode] = useState<AuthMode>('signIn')
  const navigate = useNavigate()
  const { login, register, isLoading } = useAuth()

  const handleModeChange = (newMode: AuthMode) => {
    setMode(newMode)
  }

  const handleSubmit = async (data: FormValues) => {
    try {
      if (mode === 'signIn') {
        await login({ email: data.email, password: data.password })
      } else if (data.firstName && data.lastName) {
        await register({
          firstName: data.firstName,
          lastName: data.lastName,
          email: data.email,
          password: data.password,
        })
      }

      // on login or registration success, navigate to home page
      navigate({ to: '/', replace: true })
    } catch (error) {
      // errors handled by useAuth toast callbacks
      console.log('Authentication error:', error)
    }
  }

  return (
    <main
      className="w-full h-screen flex items-center justify-center"
      id="auth"
    >
      <AuthForm
        mode={mode}
        onModeChange={handleModeChange}
        onSubmit={handleSubmit}
      />
    </main>
  )
}
