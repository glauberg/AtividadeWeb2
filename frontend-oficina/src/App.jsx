import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider, useAuth } from './context/AuthContext'

import Navbar from './components/Navbar'
import Footer from './components/Footer'

import LoginPage           from './pages/LoginPage'
import HomePage            from './pages/HomePage'
import MeusAgendamentosPage from './pages/MeusAgendamentosPage'
import AgendarServicoPage  from './pages/AgendarServicoPage'
import ClientesPage        from './pages/ClientesPage'
import VeiculosPage        from './pages/VeiculosPage'
import ServicosPage        from './pages/ServicosPage'
import AgendamentosPage    from './pages/AgendamentosPage'
import UsuariosPage        from './pages/UsuariosPage'

// Rota protegida — redireciona para /login se não autenticado
function PrivateRoute({ children, roles }) {
  const { user } = useAuth()
  if (!user) return <Navigate to="/login" replace />
  if (roles && !roles.includes(user.perfil)) return <Navigate to="/" replace />
  return children
}

function Layout({ children }) {
  return (
    <>
      <Navbar />
      {children}
      <Footer />
    </>
  )
}

function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />

      <Route path="/" element={
        <PrivateRoute>
          <Layout><HomePage /></Layout>
        </PrivateRoute>
      } />

      <Route path="/meus-agendamentos" element={
        <PrivateRoute roles={['ROLE_CLIENTE']}>
          <Layout><MeusAgendamentosPage /></Layout>
        </PrivateRoute>
      } />

      <Route path="/agendar" element={
        <PrivateRoute roles={['ROLE_CLIENTE', 'ROLE_GERENTE']}>
          <Layout><AgendarServicoPage /></Layout>
        </PrivateRoute>
      } />

      <Route path="/clientes" element={
        <PrivateRoute roles={['ROLE_GERENTE']}>
          <Layout><ClientesPage /></Layout>
        </PrivateRoute>
      } />

      <Route path="/veiculos" element={
        <PrivateRoute roles={['ROLE_GERENTE']}>
          <Layout><VeiculosPage /></Layout>
        </PrivateRoute>
      } />

      <Route path="/servicos" element={
        <PrivateRoute roles={['ROLE_GERENTE']}>
          <Layout><ServicosPage /></Layout>
        </PrivateRoute>
      } />

      <Route path="/agendamentos" element={
        <PrivateRoute roles={['ROLE_GERENTE', 'ROLE_MECANICO']}>
          <Layout><AgendamentosPage /></Layout>
        </PrivateRoute>
      } />

      <Route path="/usuarios" element={
        <PrivateRoute roles={['ROLE_GERENTE']}>
          <Layout><UsuariosPage /></Layout>
        </PrivateRoute>
      } />

      {/* Fallback */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </BrowserRouter>
  )
}
