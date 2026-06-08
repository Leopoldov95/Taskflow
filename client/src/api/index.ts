/**
 * Base API file
 * Handles authentication and provides a helper function for making authenticated API calls
 */

export const API_BASE_URL = 'http://localhost:8080/api/v1'
export const TOKEN_STORAGE_KEY = 'taskflow_jwt'

// Helper function to handle JSON responses and errors
// Need a generic type parameter to properly type the response data
export async function apiFetch<T>(
  url: string,
  options: RequestInit = {},
): Promise<T> {
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
