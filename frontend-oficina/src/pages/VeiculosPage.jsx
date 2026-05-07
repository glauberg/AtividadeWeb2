import { useState, useEffect } from 'react'
import api from '../services/api'

const MOCK = [
  { id: 1, placa: 'ABC-1234', modelo: 'Corolla', marca: 'Toyota', cliente: { nome: 'João Silva' } },
  { id: 2, placa: 'DEF-5678', modelo: 'Civic',   marca: 'Honda',  cliente: { nome: 'João Silva' } },
  { id: 3, placa: 'GHI-9012', modelo: 'Fiesta',  marca: 'Ford',   cliente: { nome: 'Maria Santos' } },
  { id: 4, placa: 'JKL-3456', modelo: 'Onix',    marca: 'Chevrolet', cliente: { nome: 'Carlos Lima' } },
]

export default function VeiculosPage() {
  const [veiculos, setVeiculos] = useState(MOCK)
  const [busca, setBusca] = useState('')
  const [modal, setModal] = useState(false)
  const [form, setForm] = useState({ placa: '', modelo: '', marca: '', clienteId: '' })

  useEffect(() => {
    api.get('/veiculos').then(r => setVeiculos(r.data)).catch(() => {})
  }, [])

  const filtrados = veiculos.filter(v =>
    v.placa.toLowerCase().includes(busca.toLowerCase()) ||
    v.modelo.toLowerCase().includes(busca.toLowerCase()) ||
    v.marca.toLowerCase().includes(busca.toLowerCase()) ||
    v.cliente?.nome?.toLowerCase().includes(busca.toLowerCase())
  )

  function handleChange(e) {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }))
  }

  async function handleSave(e) {
    e.preventDefault()
    try {
      const { clienteId, ...dadosVeiculo } = form
      const r = await api.post(`/veiculos?clienteId=${clienteId}`, dadosVeiculo)
      setVeiculos(prev => [...prev, r.data])
    } catch (err) {
      console.error("Falha ao salvar veículo", err)
      alert("Erro ao salvar veículo no banco de dados.")
    }
    setModal(false)
    setForm({ placa: '', modelo: '', marca: '', clienteId: '' })
  }

  function handleDelete(id) {
    if (!confirm('Remover veículo?')) return
    api.delete(`/veiculos/${id}`).catch(() => {})
    setVeiculos(prev => prev.filter(v => v.id !== id))
  }

  return (
    <main className="page-content">
      <div className="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2">
        <h2 className="mb-0">
          <i className="bi bi-truck me-2 text-primary"></i>Veículos
        </h2>
        <button id="btn-novo-agendamento" onClick={() => setModal(true)}>
          <i className="bi bi-plus-lg"></i>Novo Veículo
        </button>
      </div>

      <div className="filtros-container mb-3">
        <div>
          <label htmlFor="busca-veiculo">Buscar</label>
          <input id="busca-veiculo" type="text" placeholder="Placa, modelo, marca ou cliente…" value={busca} onChange={e => setBusca(e.target.value)} />
        </div>
      </div>

      <div className="table-responsive">
        <table>
          <thead>
            <tr><th>#</th><th>Placa</th><th>Modelo</th><th>Marca</th><th>Cliente</th><th style={{ textAlign: 'center' }}>Ações</th></tr>
          </thead>
          <tbody>
            {filtrados.map(v => (
              <tr key={v.id}>
                <td>{v.id}</td>
                <td><span className="badge bg-secondary">{v.placa}</span></td>
                <td>{v.modelo}</td>
                <td>{v.marca}</td>
                <td>{v.cliente?.nome}</td>
                <td style={{ textAlign: 'center' }}>
                  <button className="btn-cancelar" onClick={() => handleDelete(v.id)}>
                    <i className="bi bi-trash"></i>
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {modal && (
        <div className="modal d-block" tabIndex="-1" style={{ background: 'rgba(0,0,0,0.4)' }}>
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Novo Veículo</h5>
                <button type="button" className="btn-close" onClick={() => setModal(false)}></button>
              </div>
              <form onSubmit={handleSave} id="form-veiculo">
                <div className="modal-body">
                  <div className="mb-3">
                    <label className="form-label">Placa *</label>
                    <input type="text" name="placa" className="form-control" maxLength={10} value={form.placa} onChange={handleChange} required />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Modelo *</label>
                    <input type="text" name="modelo" className="form-control" value={form.modelo} onChange={handleChange} required />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Marca *</label>
                    <input type="text" name="marca" className="form-control" value={form.marca} onChange={handleChange} required />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">ID do Cliente *</label>
                    <input type="number" name="clienteId" className="form-control" value={form.clienteId} onChange={handleChange} required />
                  </div>
                </div>
                <div className="modal-footer">
                  <button type="button" className="btn btn-secondary" onClick={() => setModal(false)}>Cancelar</button>
                  <button type="submit" className="btn btn-primary">Salvar</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </main>
  )
}
