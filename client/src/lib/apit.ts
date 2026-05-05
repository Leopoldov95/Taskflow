// Base API call wrapper to handle JWT token in all API calls

const BASE_URL = 'http://localhost:8080/api' // <-- Will need to be replaced in Prod

// retrives the token from the header
const getHeaders = (): HeadersInit => {
  const token = localStorage.getItem('token')
  return {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  }
}

export const apiFetch = async (endPoint: string, options?: RequestInit) => {
  const response = await fetch(`${BASE_URL}${endPoint}`, {
    ...options,
    headers: getHeaders(),
  })

  if (response.status === 401) {
    localStorage.getItem('token')
    //! Might need to be handled different in Tanstack Router
    window.location.href = '/auth'
  }

  if (!response.ok) {
    const error = await response.json()
    throw new Error(error.message || 'Something went wrong')
  }

  // handle 204 no content
  if (response.status === 204) return null

  return response.json()
}
