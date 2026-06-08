import { createFileRoute } from '@tanstack/react-router'
import { Button } from '@/components/ui/button'
import { useTeams } from '#/hooks/useTeams'
import { useAuth } from '@/provider/AuthProvider'

export const Route = createFileRoute('/')({ component: App })

function App() {
  const { data: teams, isPending, isError } = useTeams()

  if (isPending) return <p>Loading teams...</p>
  if (isError) return <p>Something went wrong.</p>

  console.log('Teams data:', teams)

  console.log('User data from AuthContext:', useAuth().user)

  return (
    <main className="page-wrap px-4 pb-8 pt-14">
      <h1>Welcome to Home Page (TEAM)</h1>
      {/* Left side, user info */}

      {/* Teams list */}
    </main>
  )
}
