import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import StatusBadge from '../components/StatusBadge'
import api from '../services/api'



function formatarData(iso) {
  const d = new Date(iso)
  return d.toLocaleDateString('pt-BR') + ' ' + d.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' })
}

function SummaryCard({ icon, value, label, colorClass }) {
  return (
    <div className={`summary-card ${colorClass}`}>
      <div className="card-icon"><i className={`bi ${icon}`}></i></div>
      <div>
        <div className="card-value">{value}</div>
        <div className="card-label">{label}</div>
      </div>
    </div>
  )
}

export default function HomePage() {
  const { user } = useAuth()
  const [agendamentos, setAgendamentos] = useState([])
  const [filtroCliente, setFiltroCliente] = useState('')
  const [filtroVeiculo, setFiltroVeiculo] = useState('')
  const [filtroStatus, setFiltroStatus] = useState('')

  useEffect(() => {
    api.get('/agendamentos')
      .then(r => setAgendamentos(r.data))
      .catch(err => console.error("Erro ao carregar agendamentos", err))
  }, [])

  const dados = agendamentos.filter(a => {
    const okCliente  = !filtroCliente || a.cliente?.nome.toLowerCase().includes(filtroCliente.toLowerCase())
    const okVeiculo  = !filtroVeiculo || a.veiculo?.placa.toLowerCase().includes(filtroVeiculo.toLowerCase()) || a.veiculo?.modelo.toLowerCase().includes(filtroVeiculo.toLowerCase())
    const okStatus   = !filtroStatus  || a.status === filtroStatus
    return okCliente && okVeiculo && okStatus
  })

  const contadores = {
    agendados:   agendamentos.filter(a => a.status === 'AGENDADO').length,
    manutencao:  agendamentos.filter(a => a.status === 'EM_MANUTENCAO').length,
    concluidos:  agendamentos.filter(a => a.status === 'CONCLUIDO').length,
    cancelados:  agendamentos.filter(a => a.status === 'CANCELADO').length,
  }

  return (
    <main className="page-content">
      {/* Boas vindas */}
      <div className="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2">
        <div>
          <h2 className="mb-0">
            <i className="bi bi-speedometer2 me-2 text-primary"></i>
            Dashboard
          </h2>
          <p className="text-muted mb-0 small">
            Bem-vindo, <strong>{user?.nome || user?.email}</strong>! Aqui estão os agendamentos da semana.
          </p>
        </div>
        {(user?.perfil === 'ROLE_GERENTE' || user?.perfil === 'ROLE_CLIENTE') && (
          <Link to="/agendar" id="btn-novo-agendamento">
            <i className="bi bi-plus-lg"></i>Novo Agendamento
          </Link>
        )}
      </div>

      {/* Cards de resumo */}
      <div className="row g-3 mb-4">
        <div className="col-6 col-md-3">
          <SummaryCard icon="bi-calendar2-check" value={contadores.agendados}  label="Agendados"     colorClass="card-primary"  />
        </div>
        <div className="col-6 col-md-3">
          <SummaryCard icon="bi-tools"            value={contadores.manutencao} label="Em Manutenção" colorClass="card-warning"  />
        </div>
        <div className="col-6 col-md-3">
          <SummaryCard icon="bi-check-circle"     value={contadores.concluidos} label="Concluídos"    colorClass="card-success"  />
        </div>
        <div className="col-6 col-md-3">
          <SummaryCard icon="bi-x-circle"         value={contadores.cancelados} label="Cancelados"    colorClass="card-danger"   />
        </div>
      </div>

      {/* Filtros */}
      <div className="filtros-container">
        <div>
          <label htmlFor="filtro-cliente">Cliente</label>
          <input
            id="filtro-cliente"
            type="text"
            placeholder="Nome do cliente…"
            value={filtroCliente}
            onChange={e => setFiltroCliente(e.target.value)}
          />
        </div>
        <div>
          <label htmlFor="filtro-veiculo">Veículo / Placa</label>
          <input
            id="filtro-veiculo"
            type="text"
            placeholder="Placa ou modelo…"
            value={filtroVeiculo}
            onChange={e => setFiltroVeiculo(e.target.value)}
          />
        </div>
        <div>
          <label htmlFor="filtro-status">Status</label>
          <select id="filtro-status" value={filtroStatus} onChange={e => setFiltroStatus(e.target.value)}>
            <option value="">Todos</option>
            <option value="AGENDADO">Agendado</option>
            <option value="EM_MANUTENCAO">Em Manutenção</option>
            <option value="CONCLUIDO">Concluído</option>
            <option value="CANCELADO">Cancelado</option>
          </select>
        </div>
        <button
          className="btn btn-outline-secondary btn-sm"
          onClick={() => { setFiltroCliente(''); setFiltroVeiculo(''); setFiltroStatus('') }}
        >
          <i className="bi bi-x-lg me-1"></i>Limpar
        </button>
      </div>

      {/* Tabela */}
      <div className="table-responsive">
        <table>
          <thead>
            <tr>
              <th>#</th>
              <th>Data / Hora</th>
              <th>Cliente</th>
              <th>Telefone</th>
              <th>Veículo</th>
              <th>Placa</th>
              <th>Funcionário</th>
              <th>Serviço(s)</th>
              <th>Status</th>
              <th style={{ textAlign: 'center' }}>Ações</th>
            </tr>
          </thead>
          <tbody>
            {dados.length === 0 && (
              <tr>
                <td colSpan={10} style={{ textAlign: 'center', color: '#6c757d', padding: '1.5rem' }}>
                  <i className="bi bi-calendar-x me-2"></i>Nenhum agendamento encontrado.
                </td>
              </tr>
            )}
            {dados.map(a => (
              <tr key={a.id}>
                <td>{a.id}</td>
                <td>{formatarData(a.dataHora)}</td>
                <td>{a.cliente?.nome}</td>
                <td>{a.cliente?.telefone}</td>
                <td>{a.veiculo?.modelo}</td>
                <td><span className="badge bg-secondary">{a.veiculo?.placa}</span></td>
                <td>{a.funcionario?.nome}</td>
                <td>{a.servicos?.join(', ')}</td>
                <td><StatusBadge status={a.status} /></td>
                <td style={{ textAlign: 'center', whiteSpace: 'nowrap' }}>
                  {a.status === 'AGENDADO' || a.status === 'EM_MANUTENCAO' ? (
                    <>
                      <button
                        className="btn-finalizar me-1"
                        title="Finalizar"
                        onClick={() => alert(`Finalizar agendamento #${a.id}`)}
                      >
                        <i className="bi bi-check-lg"></i>
                      </button>
                      <button
                        className="btn-cancelar"
                        title="Cancelar"
                        onClick={() => alert(`Cancelar agendamento #${a.id}`)}
                      >
                        <i className="bi bi-x-lg"></i>
                      </button>
                    </>
                  ) : (
                    <span className="text-muted small">—</span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </main>
  )
}
