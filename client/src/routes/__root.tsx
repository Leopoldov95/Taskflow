import {
  Outlet,
  createRootRoute,
  useLocation,
  useNavigate,
} from '@tanstack/react-router'
import { TanStackRouterDevtoolsPanel } from '@tanstack/react-router-devtools'
import { TanStackDevtools } from '@tanstack/react-devtools'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { useEffect } from 'react'

import '../styles.css'
import { Toaster } from 'sonner'
import { getToken, removeToken, validateToken } from '@/api/auth'
import { AuthProvider } from '#/provider/AuthProvider'
import AppLayout from '#/components/AppLayout'

const queryClient = new QueryClient()
console.log('Root component loaded')

export const Route = createRootRoute({
  component: RootComponent,
})

function RootComponent() {
  const navigate = useNavigate()
  const location = useLocation()

  // On initial load, check if user is authenticated and redirect accordingly
  useEffect(() => {
    async function checkAuth() {
      const token = getToken()
      // Check if the current route is an auth route (e.g., /auth, /auth/login, /auth/register)
      const isAuthRoute = location.pathname.startsWith('/auth')

      if (!token) {
        if (!isAuthRoute) {
          navigate({ to: '/auth', replace: true })
        }
        return
      }

      const valid = await validateToken()
      if (!valid) {
        removeToken()
        if (!isAuthRoute) {
          navigate({ to: '/auth', replace: true })
        }
        return
      }

      if (isAuthRoute) {
        navigate({ to: '/', replace: true })
      }
    }

    checkAuth()
  }, [location.pathname, navigate])

  const isAuthRoute = location.pathname.startsWith('/auth')

  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        {isAuthRoute ? (
          <Outlet />
        ) : (
          <AppLayout>
            <Outlet />
          </AppLayout>
        )}
        <TanStackDevtools
          config={{
            position: 'bottom-right',
          }}
          plugins={[
            {
              name: 'TanStack Router',
              render: <TanStackRouterDevtoolsPanel />,
            },
          ]}
        />
        <Toaster />
      </AuthProvider>
    </QueryClientProvider>
  )
}
