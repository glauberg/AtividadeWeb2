import { useState, useEffect } from 'react'
import api from '../services/api'

const MOCK = [
  { id: 2, nome: 'Cliente João', cpf: '11111111111', telefone: '84911111111' },
  { id: 3, nome: 'Cliente Maria', cpf: '22222222222', telefone: '84922222222' },
  { id: 4, nome: 'Cliente Carlos', cpf: '33333333333', telefone: '84933333333' },
]

export default function ClientesPage() {
  const [clientes, setClientes] = useState([])
  const [busca, setBusca] = useState('')
  const [modal, setModal] = useState(false)
  const [form, setForm] = useState({ nome: '', cpf: '', telefone: '' })

  useEffect(() => {
    api.get('/clientes').then(r => setClientes(r.data)).catch(() => {})
  }, [])

  const filtrados = clientes.filter(c =>
    c.nome.toLowerCase().includes(busca.toLowerCase()) ||
    c.cpf.includes(busca) ||
    c.telefone.includes(busca)
  )

  function handleChange(e) {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }))
  }

  async function handleSave(e) {
    e.preventDefault()
    try {
      const r = await api.post('/clientes', form)
      setClientes(prev => [...prev, r.data])
    } catch (err) {
      console.error("Erro ao salvar", err)
      alert("Erro ao salvar cliente.")
    }
    setModal(false)
    setForm({ nome: '', cpf: '', telefone: '' })
  }

  function handleDelete(id) {
    if (!confirm('Remover cliente?')) return
    api.delete(`/clientes/${id}`).catch(() => {})
    setClientes(prev => prev.filter(c => c.id !== id))
  }

  return (
    <main className="page-content">
      <div className="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2">
        <h2 className="mb-0">
          <i className="bi bi-people me-2 text-primary"></i>Clientes
        </h2>
        <button id="btn-novo-agendamento" onClick={() => setModal(true)}>
          <i className="bi bi-plus-lg"></i>Novo Cliente
        </button>
      </div>

      {/* Busca */}
      <div className="filtros-container mb-3">
        <div>
          <label htmlFor="busca-cliente">Buscar</label>
          <input id="busca-cliente" type="text" placeholder="Nome, CPF ou telefone…" value={busca} onChange={e => setBusca(e.target.value)} />
        </div>
      </div>

      <div className="table-responsive">
        <table>
          <thead>
            <tr><th>#</th><th>Nome</th><th>CPF</th><th>Telefone</th><th style={{ textAlign: 'center' }}>Ações</th></tr>
          </thead>
          <tbody>
            {filtrados.map(c => (
              <tr key={c.id}>
                <td>{c.id}</td>
                <td>{c.nome}</td>
                <td>{c.cpf}</td>
                <td>{c.telefone}</td>
                <td style={{ textAlign: 'center' }}>
                  <button className="btn-cancelar" onClick={() => handleDelete(c.id)}>
                    <i className="bi bi-trash"></i>
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Modal novo cliente */}
      {modal && (
        <div className="modal d-block" tabIndex="-1" style={{ background: 'rgba(0,0,0,0.4)' }}>
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Novo Cliente</h5>
                <button type="button" className="btn-close" onClick={() => setModal(false)}></button>
              </div>
              <form onSubmit={handleSave} id="form-cliente">
                <div className="modal-body">
                  <div className="mb-3">
                    <label className="form-label">Nome *</label>
                    <input type="text" name="nome" className="form-control" value={form.nome} onChange={handleChange} required />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">CPF *</label>
                    <input type="text" name="cpf" className="form-control" maxLength={14} value={form.cpf} onChange={handleChange} required />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Telefone</label>
                    <input type="tel" name="telefone" className="form-control" value={form.telefone} onChange={handleChange} />
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
