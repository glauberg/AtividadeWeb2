// Contexto de autenticação simples (mock para desenvolvimento)
import { createContext, useContext, useState } from 'react'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('oficina_user')
    return saved ? JSON.parse(saved) : null
  })

  function login(userData, token) {
    localStorage.setItem('oficina_user', JSON.stringify(userData))
    localStorage.setItem('oficina_token', token)
    setUser(userData)
  }

  function logout() {
    localStorage.removeItem('oficina_user')
    localStorage.removeItem('oficina_token')
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}
