import { useState, useEffect } from 'react'
import api from '../services/api'



const LABEL_PERFIL = {
  ROLE_CLIENTE:  { label: 'Cliente',  cls: 'bg-info text-dark'    },
  ROLE_GERENTE:  { label: 'Gerente',  cls: 'bg-primary'           },
  ROLE_MECANICO: { label: 'Mecânico', cls: 'bg-warning text-dark' },
}

export default function UsuariosPage() {
  const [usuarios, setUsuarios] = useState([])
  const [modal, setModal] = useState(false)
  const [form, setForm] = useState({ email: '', senha: '', perfilId: '' })

  useEffect(() => {
    api.get('/usuarios').then(r => setUsuarios(r.data)).catch(() => {})
  }, [])

  function handleChange(e) {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }))
  }

  async function handleSave(e) {
    e.preventDefault()
    try {
      const r = await api.post('/usuarios', form)
      setUsuarios(prev => [...prev, r.data])
    } catch (err) {
      console.error(err)
      alert("Erro ao salvar usuário.")
    }
    setModal(false)
    setForm({ email: '', senha: '', perfilId: '' })
  }

  function toggleAtivo(id) {
    api.patch(`/usuarios/${id}/toggle-ativo`).catch(() => {})
    setUsuarios(prev => prev.map(u => u.id === id ? { ...u, ativo: !u.ativo } : u))
  }

  return (
    <main className="page-content">
      <div className="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2">
        <h2 className="mb-0">
          <i className="bi bi-person-gear me-2 text-primary"></i>Usuários
        </h2>
        <button id="btn-novo-agendamento" onClick={() => setModal(true)}>
          <i className="bi bi-plus-lg"></i>Novo Usuário
        </button>
      </div>

      <div className="table-responsive">
        <table>
          <thead>
            <tr><th>#</th><th>Nome</th><th>E-mail</th><th>Perfil</th><th>Ativo</th><th style={{ textAlign: 'center' }}>Ações</th></tr>
          </thead>
          <tbody>
            {usuarios.map(u => {
              const p = LABEL_PERFIL[u.perfil?.nome] || { label: u.perfil?.nome, cls: 'bg-secondary' }
              return (
                <tr key={u.id}>
                  <td>{u.id}</td>
                  <td>{u.nome}</td>
                  <td>{u.email}</td>
                  <td><span className={`badge ${p.cls}`}>{p.label}</span></td>
                  <td>
                    <span className={`badge ${u.ativo ? 'bg-success' : 'bg-secondary'}`}>
                      {u.ativo ? 'Ativo' : 'Inativo'}
                    </span>
                  </td>
                  <td style={{ textAlign: 'center' }}>
                    <button
                      className={u.ativo ? 'btn-cancelar' : 'btn-finalizar'}
                      title={u.ativo ? 'Desativar' : 'Ativar'}
                      onClick={() => toggleAtivo(u.id)}
                    >
                      <i className={`bi ${u.ativo ? 'bi-toggle-on' : 'bi-toggle-off'}`}></i>
                    </button>
                  </td>
                </tr>
              )
            })}
          </tbody>
        </table>
      </div>

      {modal && (
        <div className="modal d-block" tabIndex="-1" style={{ background: 'rgba(0,0,0,0.4)' }}>
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Novo Usuário</h5>
                <button type="button" className="btn-close" onClick={() => setModal(false)}></button>
              </div>
              <form onSubmit={handleSave} id="form-usuario">
                <div className="modal-body">
                  <div className="mb-3">
                    <label className="form-label">E-mail *</label>
                    <input type="email" name="email" className="form-control" value={form.email} onChange={handleChange} required />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Senha *</label>
                    <input type="password" name="senha" className="form-control" value={form.senha} onChange={handleChange} required />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Perfil *</label>
                    <select name="perfilId" className="form-select" value={form.perfilId} onChange={handleChange} required>
                      <option value="">-- selecione --</option>
                      <option value="1">Cliente</option>
                      <option value="2">Mecânico</option>
                      <option value="3">Gerente</option>
                    </select>
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
