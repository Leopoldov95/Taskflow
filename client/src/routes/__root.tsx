import { Outlet, createRootRoute, useLocation, useNavigate } from '@tanstack/react-router'
import { TanStackRouterDevtoolsPanel } from '@tanstack/react-router-devtools'
import { TanStackDevtools } from '@tanstack/react-devtools'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { useEffect } from 'react'

import '../styles.css'
import { Toaster } from 'sonner'
import { getToken, removeToken, validateToken } from '@/api/auth'

const queryClient = new QueryClient()

export const Route = createRootRoute({
  component: RootComponent,
})

function RootComponent() {
  const navigate = useNavigate()
  const location = useLocation()

  useEffect(() => {
    async function checkAuth() {
      const token = getToken()
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

  return (
    <QueryClientProvider client={queryClient}>
      <Outlet />
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
    </QueryClientProvider>
  )
}
