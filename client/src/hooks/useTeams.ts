import { useMutation, useQuery } from '@tanstack/react-query'
import { toast } from 'sonner'
import { getTeams as getTeamsApi } from '@/api/teams'

export function useTeams() {
  return useQuery({
    queryKey: ['teams'],
    queryFn: getTeamsApi,
    throwOnError: (error) => {
      const errorMessage = JSON.parse(error.message)
      toast.error(errorMessage.message ?? 'Failed to fetch teams')
      return false
    },
  })
}
