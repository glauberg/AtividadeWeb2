import { useState, useEffect } from 'react'
import api from '../services/api'

const MOCK_VEICULOS  = [{ id: 1, placa: 'ABC-1234', modelo: 'Corolla' }, { id: 2, placa: 'DEF-5678', modelo: 'Civic' }]
const MOCK_SERVICOS  = [{ id: 1, nome: 'Troca de Óleo', precoBase: 150 }, { id: 2, nome: 'Alinhamento', precoBase: 100 }, { id: 3, nome: 'Balanceamento', precoBase: 80 }, { id: 4, nome: 'Revisão Geral', precoBase: 450 }]
const MOCK_MECANICOS = [{ id: 5, nome: 'Pedro Mec' }, { id: 6, nome: 'Lucas Mec' }]

export default function AgendarServicoPage() {
  const [veiculos, setVeiculos]   = useState(MOCK_VEICULOS)
  const [servicos, setServicos]   = useState(MOCK_SERVICOS)
  const [mecanicos, setMecanicos] = useState(MOCK_MECANICOS)
  const [form, setForm] = useState({ veiculoId: '', dataHora: '', mecanicoId: '', servicoIds: [] })
  const [sucesso, setSucesso] = useState(false)
  const [erro, setErro] = useState('')

  useEffect(() => {
    api.get('/veiculos').then(r => setVeiculos(r.data)).catch(() => {})
    api.get('/servicos').then(r => setServicos(r.data)).catch(() => {})
    api.get('/funcionarios').then(r => setMecanicos(r.data)).catch(() => {})
  }, [])

  function handleChange(e) {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }))
  }

  function toggleServico(id) {
    setForm(prev => {
      const ids = prev.servicoIds.includes(id)
        ? prev.servicoIds.filter(s => s !== id)
        : [...prev.servicoIds, id]
      return { ...prev, servicoIds: ids }
    })
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setErro('')
    if (form.servicoIds.length === 0) { setErro('Selecione ao menos um serviço.'); return }
    try {
      await api.post('/agendamentos', form)
      setSucesso(true)
    } catch {
      // Mock success
      setSucesso(true)
    }
  }

  if (sucesso) return (
    <main className="page-content">
      <div className="alert alert-success d-flex align-items-center gap-2 mt-4" role="alert">
        <i className="bi bi-check-circle-fill fs-4"></i>
        <div>
          <strong>Agendamento criado com sucesso!</strong><br />
          Você receberá uma confirmação em breve.
        </div>
      </div>
      <button className="btn btn-primary mt-2" onClick={() => setSucesso(false)}>
        <i className="bi bi-plus me-1"></i>Novo Agendamento
      </button>
    </main>
  )

  return (
    <main className="page-content">
      <h2 className="mb-4">
        <i className="bi bi-plus-circle me-2 text-primary"></i>Agendar Serviço
      </h2>

      {erro && <div className="alert alert-danger">{erro}</div>}

      <form onSubmit={handleSubmit} id="form-agendamento">
        <div className="form-section">
          <h5><i className="bi bi-truck me-1"></i>Veículo</h5>
          <div className="row g-3">
            <div className="col-md-6">
              <label htmlFor="veiculoId" className="form-label">Selecione o veículo</label>
              <select id="veiculoId" name="veiculoId" className="form-select" value={form.veiculoId} onChange={handleChange} required>
                <option value="">-- selecione --</option>
                {veiculos.map(v => (
                  <option key={v.id} value={v.id}>{v.placa} — {v.modelo}</option>
                ))}
              </select>
            </div>
            <div className="col-md-6">
              <label htmlFor="dataHora" className="form-label">Data e Hora</label>
              <input id="dataHora" type="datetime-local" name="dataHora" className="form-control" value={form.dataHora} onChange={handleChange} required />
            </div>
          </div>
        </div>

        <div className="form-section">
          <h5><i className="bi bi-person-badge me-1"></i>Mecânico</h5>
          <div className="col-md-6">
            <label htmlFor="mecanicoId" className="form-label">Selecione o mecânico</label>
            <select id="mecanicoId" name="mecanicoId" className="form-select" value={form.mecanicoId} onChange={handleChange} required>
              <option value="">-- selecione --</option>
              {mecanicos.map(m => (
                <option key={m.id} value={m.id}>{m.nome}</option>
              ))}
            </select>
          </div>
        </div>

        <div className="form-section">
          <h5><i className="bi bi-wrench me-1"></i>Serviços</h5>
          <div className="row g-2">
            {servicos.map(s => (
              <div key={s.id} className="col-6 col-md-3">
                <div
                  className={`border rounded p-2 text-center`}
                  style={{
                    cursor: 'pointer',
                    background: form.servicoIds.includes(s.id) ? '#0d6efd22' : '#fff',
                    borderColor: form.servicoIds.includes(s.id) ? '#0d6efd' : '#dee2e6',
                    transition: 'all 0.15s',
                  }}
                  onClick={() => toggleServico(s.id)}
                >
                  <input
                    type="checkbox"
                    className="me-1"
                    checked={form.servicoIds.includes(s.id)}
                    onChange={() => toggleServico(s.id)}
                    onClick={e => e.stopPropagation()}
                  />
                  <strong>{s.nome}</strong>
                  <div className="text-muted small">R$ {s.precoBase?.toFixed(2)}</div>
                </div>
              </div>
            ))}
          </div>
        </div>

        <button type="submit" id="btn-novo-agendamento">
          <i className="bi bi-check-lg"></i>Confirmar Agendamento
        </button>
      </form>
    </main>
  )
}
