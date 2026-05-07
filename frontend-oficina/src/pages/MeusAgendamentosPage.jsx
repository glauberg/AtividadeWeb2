import { useState, useEffect } from 'react'
import StatusBadge from '../components/StatusBadge'
import { useAuth } from '../context/AuthContext'
import api from '../services/api'

function fmt(iso) {
  const d = new Date(iso)
  return d.toLocaleDateString('pt-BR') + ' ' + d.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' })
}

export default function MeusAgendamentosPage() {
  const { user } = useAuth()
  const [lista, setLista] = useState([])

  useEffect(() => {
    if (!user?.clienteId) return
    api.get(`/agendamentos?clienteId=${user.clienteId}`)
      .then(r => setLista(r.data))
      .catch(err => console.error(err))
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
              <th>#</th><th>Data / Hora</th><th>Veiculo</th><th>Placa</th>
              <th>Servico(s)</th><th>Produtos</th><th>Funcionario</th><th>Valor</th><th>Status</th>
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
                <td>{a.produtos?.length ? a.produtos.map(p => `${p.quantidade}x ${p.nome}`).join(', ') : '-'}</td>
                <td>{a.funcionario?.nome}</td>
                <td>R$ {a.valorTotal?.toFixed(2)}</td>
                <td><StatusBadge status={a.status} /></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </main>
  )
}
