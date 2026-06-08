// Team Types

// Team Response
export type Team = {
  id: string
  name: string
  description?: string
  // members?: TeamMember[]
  createdAt: string
  color: string
  icon: string
  active: boolean
  createdBy: string
}

// Create Team

// Update Team Request

// Team Member Response
export type TeamMember = {
  id: string
  firstName: string
  lastName: string
  role: 'owner' | 'member'
}

// Manage Team Members Request
