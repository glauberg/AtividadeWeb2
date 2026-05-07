import { useAuth } from '../context/AuthContext'
import { Link, useNavigate } from 'react-router-dom'

export default function Navbar() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const perfil = user?.perfil

  function handleLogout() {
    logout()
    navigate('/login')
  }

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
      <div className="container-fluid">
        <Link className="navbar-brand d-flex align-items-center" to="/">
          <i className="bi bi-car-front-fill fs-4 me-2"></i>
          <span>Oficina AutoAgenda</span>
        </Link>

        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navMenu"
          aria-controls="navMenu"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="navMenu">
          <ul className="navbar-nav me-auto mb-2 mb-lg-0">
            <li className="nav-item">
              <Link className="nav-link" to="/"><i className="bi bi-house me-1"></i>Home</Link>
            </li>

            {perfil === 'ROLE_CLIENTE' && (
              <>
                <li className="nav-item">
                  <Link className="nav-link" to="/meus-agendamentos">
                    <i className="bi bi-calendar2-check me-1"></i>Meus Agendamentos
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/veiculos">
                    <i className="bi bi-truck me-1"></i>Meus Veiculos
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/agendar">
                    <i className="bi bi-plus-circle me-1"></i>Agendar Servico
                  </Link>
                </li>
              </>
            )}

            {perfil === 'ROLE_GERENTE' && (
              <>
                <li className="nav-item">
                  <Link className="nav-link" to="/clientes">
                    <i className="bi bi-people me-1"></i>Clientes
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/veiculos">
                    <i className="bi bi-truck me-1"></i>Veiculos
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/servicos">
                    <i className="bi bi-wrench me-1"></i>Servicos
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/produtos">
                    <i className="bi bi-box-seam me-1"></i>Produtos
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/agendamentos">
                    <i className="bi bi-calendar2-week me-1"></i>Agendamentos
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/usuarios">
                    <i className="bi bi-person-gear me-1"></i>Usuarios
                  </Link>
                </li>
              </>
            )}

            {perfil === 'ROLE_MECANICO' && (
              <li className="nav-item">
                <Link className="nav-link" to="/agendamentos">
                  <i className="bi bi-calendar2-week me-1"></i>Agendamentos
                </Link>
              </li>
            )}
          </ul>

          {user && (
            <div className="d-flex align-items-center gap-2">
              <span className="navbar-user-info">
                <i className="bi bi-person-circle me-1"></i>
                {user.nome || user.email}
                {' '}
                <span className="badge bg-light text-dark" style={{ fontSize: '0.72rem' }}>
                  {perfil?.replace('ROLE_', '')}
                </span>
              </span>
              <button className="btn-logout" onClick={handleLogout} title="Sair">
                <i className="bi bi-box-arrow-right me-1"></i>Sair
              </button>
            </div>
          )}
        </div>
      </div>
    </nav>
  )
}
