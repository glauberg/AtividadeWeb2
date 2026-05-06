import { useState, useEffect } from 'react'
import StatusBadge from '../components/StatusBadge'
import { useAuth } from '../context/AuthContext'
import api from '../services/api'

const MOCK = [
  { id: 1, dataHora: '2026-05-10T08:00', status: 'AGENDADO',      valorTotal: 220, veiculo: { modelo: 'Corolla', placa: 'ABC-1234' }, funcionario: { nome: 'Pedro Mec' }, servicos: ['Troca de Óleo'] },
  { id: 5, dataHora: '2026-05-12T09:00', status: 'AGENDADO',      valorTotal: 180, veiculo: { modelo: 'Civic',   placa: 'DEF-5678' }, funcionario: { nome: 'Pedro Mec' }, servicos: ['Alinhamento', 'Balanceamento'] },
]

function fmt(iso) {
  const d = new Date(iso)
  return d.toLocaleDateString('pt-BR') + ' ' + d.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' })
}

export default function MeusAgendamentosPage() {
  const { user } = useAuth()
  const [lista, setLista] = useState(MOCK)

  useEffect(() => {
    api.get(`/agendamentos?clienteId=${user?.id}`)
      .then(r => setLista(r.data))
      .catch(() => {})
  }, [user])

  return (
    <main className="page-content">
      <h2 className="mb-3">
        <i className="bi bi-calendar2-check me-2 text-primary"></i>Meus Agendamentos
      </h2>

      <div className="table-responsive">
        <table>
          <thead>
            <tr>
              <th>#</th><th>Data / Hora</th><th>Veículo</th><th>Placa</th>
              <th>Serviço(s)</th><th>Funcionário</th><th>Valor</th><th>Status</th><th>Ação</th>
            </tr>
          </thead>
          <tbody>
            {lista.map(a => (
              <tr key={a.id}>
                <td>{a.id}</td>
                <td>{fmt(a.dataHora)}</td>
                <td>{a.veiculo?.modelo}</td>
                <td><span className="badge bg-secondary">{a.veiculo?.placa}</span></td>
                <td>{a.servicos?.join(', ')}</td>
                <td>{a.funcionario?.nome}</td>
                <td>R$ {a.valorTotal?.toFixed(2)}</td>
                <td><StatusBadge status={a.status} /></td>
                <td>
                  {a.status === 'AGENDADO' && (
                    <button className="btn-cancelar" onClick={() => alert('Cancelar #' + a.id)}>
                      <i className="bi bi-x-lg me-1"></i>Cancelar
                    </button>
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
