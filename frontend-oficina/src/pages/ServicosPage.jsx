import { useState, useEffect } from 'react'
import api from '../services/api'

const MOCK = [
  { id: 1, nome: 'Troca de Óleo', precoBase: 150 },
  { id: 2, nome: 'Alinhamento',   precoBase: 100 },
  { id: 3, nome: 'Balanceamento', precoBase: 80  },
  { id: 4, nome: 'Revisão Geral', precoBase: 450 },
  { id: 5, nome: 'Troca de Pastilhas', precoBase: 200 },
  { id: 6, nome: 'Limpeza de Bicos',   precoBase: 120 },
]

export default function ServicosPage() {
  const [servicos, setServicos] = useState([])
  const [modal, setModal] = useState(false)
  const [form, setForm] = useState({ nome: '', precoBase: '' })

  useEffect(() => {
    api.get('/servicos').then(r => setServicos(r.data)).catch(() => {})
  }, [])

  function handleChange(e) {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }))
  }

  async function handleSave(e) {
    e.preventDefault()
    try {
      const r = await api.post('/servicos', form)
      setServicos(prev => [...prev, r.data])
    } catch (err) {
      console.error(err)
      alert("Erro ao salvar serviço.")
    }
    setModal(false)
    setForm({ nome: '', precoBase: '' })
  }

  function handleDelete(id) {
    if (!confirm('Remover serviço?')) return
    api.delete(`/servicos/${id}`).catch(() => {})
    setServicos(prev => prev.filter(s => s.id !== id))
  }

  return (
    <main className="page-content">
      <div className="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2">
        <h2 className="mb-0">
          <i className="bi bi-wrench me-2 text-primary"></i>Catálogo de Serviços
        </h2>
        <button id="btn-novo-agendamento" onClick={() => setModal(true)}>
          <i className="bi bi-plus-lg"></i>Novo Serviço
        </button>
      </div>

      <div className="table-responsive">
        <table>
          <thead>
            <tr><th>#</th><th>Serviço</th><th>Preço Base</th><th style={{ textAlign: 'center' }}>Ações</th></tr>
          </thead>
          <tbody>
            {servicos.map(s => (
              <tr key={s.id}>
                <td>{s.id}</td>
                <td><strong>{s.nome}</strong></td>
                <td>R$ {Number(s.precoBase).toFixed(2)}</td>
                <td style={{ textAlign: 'center' }}>
                  <button className="btn-cancelar" onClick={() => handleDelete(s.id)}>
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
                <h5 className="modal-title">Novo Serviço</h5>
                <button type="button" className="btn-close" onClick={() => setModal(false)}></button>
              </div>
              <form onSubmit={handleSave} id="form-servico">
                <div className="modal-body">
                  <div className="mb-3">
                    <label className="form-label">Nome *</label>
                    <input type="text" name="nome" className="form-control" value={form.nome} onChange={handleChange} required />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Preço Base (R$) *</label>
                    <input type="number" name="precoBase" step="0.01" min="0" className="form-control" value={form.precoBase} onChange={handleChange} required />
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
