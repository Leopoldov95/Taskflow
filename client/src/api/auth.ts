export type LoginRequest = {
  email: string
  password: string
}

export type RegisterRequest = {
  firstName: string
  lastName: string
  email: string
  password: string
}

// Expected to receive a JWT token on successful login or registration
export type AuthResponse = {
  token: string
}

const API_BASE_URL = 'http://localhost:8080/api'
const TOKEN_STORAGE_KEY = 'taskflow_jwt'

// Helper function to handle JSON responses and errors
// Need a generic type parameter to properly type the response data
async function apiFetch<T>(url: string, options: RequestInit = {}): Promise<T> {
  const response = await fetch(url, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers ?? {}),
    },
  })

  if (!response.ok) {
    const text = await response.text()
    throw new Error(text || response.statusText)
  }

  return response.json()
}

// Helper functions to manage tokens, using localStorage for ease

export function setToken(token: string) {
  if (typeof window === 'undefined') return
  window.localStorage.setItem(TOKEN_STORAGE_KEY, token)
}

export function getToken(): string | null {
  if (typeof window === 'undefined') return null
  return window.localStorage.getItem(TOKEN_STORAGE_KEY)
}

export function removeToken() {
  if (typeof window === 'undefined') return
  window.localStorage.removeItem(TOKEN_STORAGE_KEY)
}

// Utility function to get auth headers for API calls
export function getAuthHeaders(): Record<string, string> {
  const token = getToken()
  return token ? { Authorization: `Bearer ${token}` } : {}
}

export async function login(data: LoginRequest): Promise<AuthResponse> {
  console.log('Attempting to login....')
  console.log('Data:', data)

  const response = await apiFetch<AuthResponse>(`${API_BASE_URL}/auth/login`, {
    method: 'POST',
    body: JSON.stringify(data),
  })

  setToken(response.token)
  return response
}

export async function register(data: RegisterRequest): Promise<AuthResponse> {
  const response = await apiFetch<AuthResponse>(
    `${API_BASE_URL}/auth/register`,
    {
      method: 'POST',
      body: JSON.stringify(data),
    },
  )

  setToken(response.token)
  return response
}

// Function to validate the current token by making a request to a protected endpoint
export async function validateToken(): Promise<boolean> {
  const token = getToken()
  if (!token) return false

  try {
    const response = await fetch(`${API_BASE_URL}/me`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    return response.ok
  } catch {
    return false
  }
}

// Utility function to make authenticated API calls with the token automatically included
export async function fetchWithAuth<T>(
  path: string,
  options: RequestInit = {},
): Promise<T> {
  // If the path is a full URL, use it directly; otherwise, prepend the API base URL
  const url = path.startsWith('http') ? path : `${API_BASE_URL}${path}`
  // using existing apiFetch function to handle response parsing and error handling, just adding auth headers
  return apiFetch<T>(url, {
    ...options,
    headers: {
      ...(options.headers ?? {}),
      ...getAuthHeaders(),
    },
  })
}
