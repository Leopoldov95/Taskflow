/**
 * API file for Teams-related endpoints
 */

import { fetchWithAuth } from './auth'
import type { Team } from '@/types/team'

const BASE_PATH = '/teams'

// GET Teams
export async function getTeams(): Promise<Team[]> {
  return fetchWithAuth<Team[]>(BASE_PATH)
}

// GET single Team by ID
export async function getTeamById(id: string): Promise<Team> {
  return fetchWithAuth<Team>(`${BASE_PATH}/${id}`)
}

// POST Create new Team

// PATCH Update Team

// DELETE Team
