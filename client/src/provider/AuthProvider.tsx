import { createContext, useContext, useEffect, useState } from 'react'
import { getMe, getToken, removeToken, setToken } from '../api/auth'
import type { UserResponse } from '../api/auth'

interface User extends UserResponse {}

interface AuthContextValue {
  user: User | null
  token: string | null
  isLoading: boolean
  login: (token: string) => Promise<void>
  logout: () => void
  updateUser: (user: User) => void
}

const AuthContext = createContext<AuthContextValue | null>(null)

const USER_STORAGE_KEY = 'taskflow_user'

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null)
  const [token, setTokenState] = useState<string | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  // Load user and token from storage on mount
  useEffect(() => {
    const savedToken = getToken()
    const savedUser = localStorage.getItem(USER_STORAGE_KEY)

    if (savedToken) {
      setTokenState(savedToken)
    }

    if (savedUser) {
      try {
        setUser(JSON.parse(savedUser))
      } catch {
        // Invalid JSON, clear it
        localStorage.removeItem(USER_STORAGE_KEY)
      }
    }

    setIsLoading(false)
  }, [])

  const handleLogin = async (newToken: string) => {
    setTokenState(newToken)
    setToken(newToken)

    try {
      // Fetch user details after login
      const userDetails = await getMe()
      setUser(userDetails)
      localStorage.setItem(USER_STORAGE_KEY, JSON.stringify(userDetails))
    } catch (error) {
      console.error('Failed to fetch user details:', error)
      // Token is set but user details failed to load
      // This is not ideal but lets user continue
    }
  }

  const handleLogout = () => {
    setUser(null)
    setTokenState(null)
    removeToken()
    localStorage.removeItem(USER_STORAGE_KEY)
  }

  const handleUpdateUser = (updatedUser: User) => {
    setUser(updatedUser)
    localStorage.setItem(USER_STORAGE_KEY, JSON.stringify(updatedUser))
  }

  const value: AuthContextValue = {
    user,
    token,
    isLoading,
    login: handleLogin,
    logout: handleLogout,
    updateUser: handleUpdateUser,
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (context === null) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}
