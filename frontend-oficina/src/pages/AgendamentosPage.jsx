import { useState, useEffect } from 'react'
import StatusBadge from '../components/StatusBadge'
import api from '../services/api'



function fmt(iso) {
  const d = new Date(iso)
  return d.toLocaleDateString('pt-BR') + ' ' + d.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' })
}

export default function AgendamentosPage() {
  const [lista, setLista] = useState([])
  const [filtroCliente, setFiltroCliente] = useState('')
  const [filtroVeiculo, setFiltroVeiculo] = useState('')
  const [filtroStatus, setFiltroStatus] = useState('')

  useEffect(() => {
    api.get('/agendamentos').then(r => setLista(r.data)).catch(() => {})
  }, [])

  const dados = lista.filter(a => {
    const okC = !filtroCliente || a.cliente?.nome.toLowerCase().includes(filtroCliente.toLowerCase())
    const okV = !filtroVeiculo || a.veiculo?.placa.toLowerCase().includes(filtroVeiculo.toLowerCase()) || a.veiculo?.modelo.toLowerCase().includes(filtroVeiculo.toLowerCase())
    const okS = !filtroStatus  || a.status === filtroStatus
    return okC && okV && okS
  })

  function changeStatus(id, novoStatus) {
    api.patch(`/agendamentos/${id}/status`, { status: novoStatus })
      .then(() => setLista(prev => prev.map(a => a.id === id ? { ...a, status: novoStatus } : a)))
      .catch(err => {
        console.error(err)
        alert('Erro ao atualizar status do agendamento.')
      })
  }

  return (
    <main className="page-content">
      <h2 className="mb-3">
        <i className="bi bi-calendar2-week me-2 text-primary"></i>Agendamentos
      </h2>

      <div className="filtros-container">
        <div>
          <label htmlFor="ag-cliente">Cliente</label>
          <input id="ag-cliente" type="text" placeholder="Nome do cliente…" value={filtroCliente} onChange={e => setFiltroCliente(e.target.value)} />
        </div>
        <div>
          <label htmlFor="ag-veiculo">Veículo / Placa</label>
          <input id="ag-veiculo" type="text" placeholder="Placa ou modelo…" value={filtroVeiculo} onChange={e => setFiltroVeiculo(e.target.value)} />
        </div>
        <div>
          <label htmlFor="ag-status">Status</label>
          <select id="ag-status" value={filtroStatus} onChange={e => setFiltroStatus(e.target.value)}>
            <option value="">Todos</option>
            <option value="AGENDADO">Agendado</option>
            <option value="EM_MANUTENCAO">Em Manutenção</option>
            <option value="CONCLUIDO">Concluído</option>
            <option value="CANCELADO">Cancelado</option>
          </select>
        </div>
        <button className="btn btn-outline-secondary btn-sm" onClick={() => { setFiltroCliente(''); setFiltroVeiculo(''); setFiltroStatus('') }}>
          <i className="bi bi-x-lg me-1"></i>Limpar
        </button>
      </div>

      <div className="table-responsive">
        <table>
          <thead>
            <tr>
              <th>#</th><th>Data / Hora</th><th>Cliente</th><th>Veículo</th><th>Placa</th>
              <th>Funcionário</th><th>Serviço(s)</th><th>Valor</th><th>Status</th><th style={{ textAlign: 'center' }}>Ações</th>
            </tr>
          </thead>
          <tbody>
            {dados.map(a => (
              <tr key={a.id}>
                <td>{a.id}</td>
                <td>{fmt(a.dataHora)}</td>
                <td>{a.cliente?.nome}</td>
                <td>{a.veiculo?.modelo}</td>
                <td><span className="badge bg-secondary">{a.veiculo?.placa}</span></td>
                <td>{a.funcionario?.nome}</td>
                <td>{a.servicos?.join(', ')}</td>
                <td>R$ {a.valorTotal?.toFixed(2)}</td>
                <td><StatusBadge status={a.status} /></td>
                <td style={{ textAlign: 'center', whiteSpace: 'nowrap' }}>
                  {a.status === 'AGENDADO' && (
                    <button className="btn-finalizar me-1" title="Iniciar manutenção" onClick={() => changeStatus(a.id, 'EM_MANUTENCAO')}>
                      <i className="bi bi-tools"></i>
                    </button>
                  )}
                  {a.status === 'EM_MANUTENCAO' && (
                    <button className="btn-finalizar me-1" title="Finalizar" onClick={() => changeStatus(a.id, 'CONCLUIDO')}>
                      <i className="bi bi-check-lg"></i>
                    </button>
                  )}
                  {(a.status === 'AGENDADO' || a.status === 'EM_MANUTENCAO') && (
                    <button className="btn-cancelar" title="Cancelar" onClick={() => changeStatus(a.id, 'CANCELADO')}>
                      <i className="bi bi-x-lg"></i>
                    </button>
                  )}
                  {(a.status === 'CONCLUIDO' || a.status === 'CANCELADO') && (
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
