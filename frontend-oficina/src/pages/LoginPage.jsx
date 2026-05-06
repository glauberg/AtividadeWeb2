import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import api from '../services/api'

// Usuários mock para desenvolvimento (remover quando JWT estiver implementado)
const MOCK_USERS = [
  { id: 1, email: 'admin@oficina.com', senha: 'senha123', nome: 'Administrador', perfil: 'ROLE_GERENTE' },
  { id: 2, email: 'joao@email.com', senha: 'senha123', nome: 'João', perfil: 'ROLE_CLIENTE' },
  { id: 5, email: 'pedro.mec@oficina.com', senha: 'senha123', nome: 'Pedro', perfil: 'ROLE_MECANICO' },
]

export default function LoginPage() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({ email: '', senha: '' })
  const [erro, setErro] = useState('')
  const [loading, setLoading] = useState(false)

  function handleChange(e) {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }))
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setErro('')
    setLoading(true)

    try {
      // Tentativa de autenticação real via API
      const res = await api.post('/auth/login', form)
      login(res.data.usuario, res.data.token)
      navigate('/')
    } catch {
      // Fallback mock para desenvolvimento sem backend
      const mockUser = MOCK_USERS.find(
        u => u.email === form.email && u.senha === form.senha
      )
      if (mockUser) {
        login(mockUser, 'mock-token-' + mockUser.perfil)
        navigate('/')
      } else {
        setErro('E-mail ou senha inválidos.')
      }
    } finally {
      setLoading(false)
    }
  }

  return (
    <div
      className="d-flex justify-content-center align-items-center"
      style={{ minHeight: '100vh', background: 'linear-gradient(135deg, #0d6efd22 0%, #f8f9fa 100%)' }}
    >
      <div className="card shadow-sm" style={{ width: '100%', maxWidth: 420 }}>
        <div className="card-body p-4">
          {/* Logo + título */}
          <div className="text-center mb-4">
            <i className="bi bi-car-front-fill text-primary" style={{ fontSize: '2.8rem' }}></i>
            <h4 className="mt-2 mb-0 fw-bold">AutoAgenda</h4>
            <p className="text-muted small">Sistema de Agendamento de Oficina</p>
          </div>

          {erro && (
            <div className="alert alert-danger py-2 d-flex align-items-center gap-2" role="alert">
              <i className="bi bi-exclamation-triangle-fill"></i> {erro}
            </div>
          )}

          <form onSubmit={handleSubmit} id="form-login" noValidate>
            <div className="mb-3">
              <label htmlFor="email" className="form-label fw-semibold">E-mail</label>
              <div className="input-group">
                <span className="input-group-text"><i className="bi bi-envelope"></i></span>
                <input
                  id="email"
                  type="email"
                  name="email"
                  className="form-control"
                  placeholder="seu@email.com"
                  value={form.email}
                  onChange={handleChange}
                  required
                  autoComplete="username"
                />
              </div>
            </div>

            <div className="mb-4">
              <label htmlFor="senha" className="form-label fw-semibold">Senha</label>
              <div className="input-group">
                <span className="input-group-text"><i className="bi bi-lock"></i></span>
                <input
                  id="senha"
                  type="password"
                  name="senha"
                  className="form-control"
                  placeholder="••••••••"
                  value={form.senha}
                  onChange={handleChange}
                  required
                  autoComplete="current-password"
                />
              </div>
            </div>

            <button
              type="submit"
              className="btn btn-primary w-100 fw-semibold"
              id="btn-entrar"
              disabled={loading}
            >
              {loading
                ? <><span className="spinner-border spinner-border-sm me-2"></span>Entrando…</>
                : <><i className="bi bi-box-arrow-in-right me-2"></i>Entrar</>
              }
            </button>
          </form>

          <hr />
          <p className="text-center text-muted small mb-0">
            <i className="bi bi-shield-lock me-1"></i>
            Acesso restrito a usuários cadastrados
          </p>
          <div className="text-center mt-2">
            <details className="text-muted" style={{ fontSize: '0.78rem' }}>
              <summary style={{ cursor: 'pointer' }}>Credenciais de teste</summary>
              <table className="table table-sm mt-1 mb-0" style={{ fontSize: '0.75rem' }}>
                <thead><tr><th>E-mail</th><th>Perfil</th><th>Senha</th></tr></thead>
                <tbody>
                  <tr><td>admin@oficina.com</td><td>Gerente</td><td>senha123</td></tr>
                  <tr><td>joao@email.com</td><td>Cliente</td><td>senha123</td></tr>
                  <tr><td>pedro.mec@oficina.com</td><td>Mecânico</td><td>senha123</td></tr>
                </tbody>
              </table>
            </details>
          </div>
        </div>
      </div>
    </div>
  )
}
