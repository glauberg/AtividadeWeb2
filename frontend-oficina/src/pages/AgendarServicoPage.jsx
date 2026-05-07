import { useState, useEffect } from 'react'
import api from '../services/api'

const moeda = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })

export default function AgendarServicoPage() {
  const [veiculos, setVeiculos] = useState([])
  const [servicos, setServicos] = useState([])
  const [mecanicos, setMecanicos] = useState([])
  const [produtos, setProdutos] = useState([])
  const [quantidades, setQuantidades] = useState({})
  const [modalProdutos, setModalProdutos] = useState(false)
  const [form, setForm] = useState({ veiculoId: '', dataHora: '', mecanicoId: '', servicoIds: [] })
  const [sucesso, setSucesso] = useState(false)
  const [erro, setErro] = useState('')
  const [salvando, setSalvando] = useState(false)

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

  function alterarQuantidade(produtoId, valor, estoque) {
    const quantidade = Math.max(0, Math.min(Number(valor) || 0, estoque || 0))
    setQuantidades(prev => ({ ...prev, [produtoId]: quantidade }))
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setErro('')
    if (form.servicoIds.length === 0) {
      setErro('Selecione ao menos um servico.')
      return
    }

    try {
      const query = form.servicoIds.map(id => `servicoIds=${id}`).join('&')
      const resposta = await api.get(`/produtos/sugeridos${query ? `?${query}` : ''}`)
      setProdutos(resposta.data)
      setQuantidades({})
      setModalProdutos(true)
    } catch (err) {
      setErro('Erro ao buscar produtos: ' + (err.response?.data || err.message))
    }
  }

  async function confirmarAgendamento() {
    setErro('')
    setSalvando(true)

    const produtosSelecionados = Object.entries(quantidades)
      .filter(([, quantidade]) => Number(quantidade) > 0)
      .map(([produtoId, quantidade]) => ({ produtoId: Number(produtoId), quantidade: Number(quantidade) }))

    try {
      await api.post('/agendamentos', { ...form, produtos: produtosSelecionados })
      setModalProdutos(false)
      setSucesso(true)
    } catch (err) {
      setErro('Erro ao agendar servico: ' + (err.response?.data || err.message))
    } finally {
      setSalvando(false)
    }
  }

  const totalServicos = servicos
    .filter(s => form.servicoIds.includes(s.id))
    .reduce((total, servico) => total + (servico.precoBase || 0), 0)

  const totalProdutos = produtos.reduce((total, produto) => {
    const quantidade = Number(quantidades[produto.id]) || 0
    return total + quantidade * (produto.precoVenda || 0)
  }, 0)

  if (sucesso) return (
    <main className="page-content">
      <div className="alert alert-success d-flex align-items-center gap-2 mt-4" role="alert">
        <i className="bi bi-check-circle-fill fs-4"></i>
        <div>
          <strong>Agendamento criado com sucesso!</strong><br />
          Voce recebera uma confirmacao em breve.
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
        <i className="bi bi-plus-circle me-2 text-primary"></i>Agendar Servico
      </h2>

      {erro && <div className="alert alert-danger">{erro}</div>}

      <form onSubmit={handleSubmit} id="form-agendamento">
        <div className="form-section">
          <h5><i className="bi bi-truck me-1"></i>Veiculo</h5>
          <div className="row g-3">
            <div className="col-md-6">
              <label htmlFor="veiculoId" className="form-label">Selecione o veiculo</label>
              <select id="veiculoId" name="veiculoId" className="form-select" value={form.veiculoId} onChange={handleChange} required>
                <option value="">-- selecione --</option>
                {veiculos.map(v => (
                  <option key={v.id} value={v.id}>{v.placa} - {v.modelo}</option>
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
          <h5><i className="bi bi-person-badge me-1"></i>Mecanico</h5>
          <div className="col-md-6">
            <label htmlFor="mecanicoId" className="form-label">Selecione o mecanico</label>
            <select id="mecanicoId" name="mecanicoId" className="form-select" value={form.mecanicoId} onChange={handleChange} required>
              <option value="">-- selecione --</option>
              {mecanicos.map(m => (
                <option key={m.id} value={m.id}>{m.nome}</option>
              ))}
            </select>
          </div>
        </div>

        <div className="form-section">
          <h5><i className="bi bi-wrench me-1"></i>Servicos</h5>
          <div className="row g-2">
            {servicos.map(s => (
              <div key={s.id} className="col-6 col-md-3">
                <div
                  className="border rounded p-2 text-center"
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
                  <div className="text-muted small">{moeda.format(s.precoBase || 0)}</div>
                </div>
              </div>
            ))}
          </div>
        </div>

        <button type="submit" id="btn-novo-agendamento">
          <i className="bi bi-check-lg"></i>Escolher Produtos
        </button>
      </form>

      {modalProdutos && (
        <div className="modal d-block" tabIndex="-1" style={{ background: 'rgba(0,0,0,0.45)' }}>
          <div className="modal-dialog modal-lg modal-dialog-centered">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Produtos para o servico</h5>
                <button type="button" className="btn-close" onClick={() => setModalProdutos(false)}></button>
              </div>
              <div className="modal-body">
                {produtos.length === 0 ? (
                  <div className="alert alert-info mb-0">Nenhum produto disponivel para os servicos selecionados.</div>
                ) : (
                  <div className="table-responsive">
                    <table>
                      <thead>
                        <tr>
                          <th>Produto</th><th>Preco</th><th>Estoque</th><th style={{ width: 140 }}>Quantidade</th><th>Subtotal</th>
                        </tr>
                      </thead>
                      <tbody>
                        {produtos.map(produto => {
                          const quantidade = Number(quantidades[produto.id]) || 0
                          return (
                            <tr key={produto.id}>
                              <td>{produto.nome}</td>
                              <td>{moeda.format(produto.precoVenda || 0)}</td>
                              <td>{produto.estoque}</td>
                              <td>
                                <input
                                  type="number"
                                  className="form-control form-control-sm"
                                  min="0"
                                  max={produto.estoque}
                                  value={quantidade}
                                  onChange={e => alterarQuantidade(produto.id, e.target.value, produto.estoque)}
                                />
                              </td>
                              <td>{moeda.format(quantidade * (produto.precoVenda || 0))}</td>
                            </tr>
                          )
                        })}
                      </tbody>
                    </table>
                  </div>
                )}
                <div className="d-flex justify-content-end gap-4 mt-3 fw-semibold">
                  <span>Servicos: {moeda.format(totalServicos)}</span>
                  <span>Produtos: {moeda.format(totalProdutos)}</span>
                  <span>Total: {moeda.format(totalServicos + totalProdutos)}</span>
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setModalProdutos(false)}>Voltar</button>
                <button type="button" className="btn btn-primary" onClick={confirmarAgendamento} disabled={salvando}>
                  {salvando ? 'Agendando...' : 'Confirmar Agendamento'}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </main>
  )
}
