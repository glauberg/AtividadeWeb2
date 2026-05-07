import { useEffect, useState } from 'react'
import api from '../services/api'

const formInicial = { nome: '', precoVenda: '', estoque: '' }

export default function ProdutosPage() {
  const [produtos, setProdutos] = useState([])
  const [busca, setBusca] = useState('')
  const [modal, setModal] = useState(false)
  const [editando, setEditando] = useState(null)
  const [form, setForm] = useState(formInicial)

  useEffect(() => {
    carregar()
  }, [])

  function carregar() {
    api.get('/produtos').then(r => setProdutos(r.data)).catch(() => {})
  }

  function abrirNovo() {
    setEditando(null)
    setForm(formInicial)
    setModal(true)
  }

  function abrirEdicao(produto) {
    setEditando(produto.id)
    setForm({
      nome: produto.nome || '',
      precoVenda: produto.precoVenda ?? '',
      estoque: produto.estoque ?? '',
    })
    setModal(true)
  }

  function handleChange(e) {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }))
  }

  async function handleSave(e) {
    e.preventDefault()
    const payload = {
      nome: form.nome,
      precoVenda: Number(form.precoVenda),
      estoque: Number(form.estoque),
    }

    try {
      if (editando) {
        const r = await api.put(`/produtos/${editando}`, payload)
        setProdutos(prev => prev.map(p => p.id === editando ? r.data : p))
      } else {
        const r = await api.post('/produtos', payload)
        setProdutos(prev => [...prev, r.data])
      }
      setModal(false)
    } catch (err) {
      alert('Erro ao salvar produto: ' + (err.response?.data || err.message))
    }
  }

  async function handleDelete(id) {
    if (!confirm('Remover produto do estoque?')) return
    try {
      await api.delete(`/produtos/${id}`)
      setProdutos(prev => prev.filter(p => p.id !== id))
    } catch (err) {
      alert('Erro ao remover produto: ' + (err.response?.data || err.message))
    }
  }

  const filtrados = produtos.filter(p =>
    p.nome?.toLowerCase().includes(busca.toLowerCase())
  )

  return (
    <main className="page-content">
      <div className="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2">
        <h2 className="mb-0">
          <i className="bi bi-box-seam me-2 text-primary"></i>Produtos
        </h2>
        <button id="btn-novo-agendamento" onClick={abrirNovo}>
          <i className="bi bi-plus-lg"></i>Novo Produto
        </button>
      </div>

      <div className="filtros-container mb-3">
        <div>
          <label htmlFor="busca-produto">Buscar</label>
          <input id="busca-produto" type="text" placeholder="Nome do produto" value={busca} onChange={e => setBusca(e.target.value)} />
        </div>
      </div>

      <div className="table-responsive">
        <table>
          <thead>
            <tr>
              <th>#</th><th>Produto</th><th>Preco</th><th>Estoque</th><th style={{ textAlign: 'center' }}>Acoes</th>
            </tr>
          </thead>
          <tbody>
            {filtrados.map(p => (
              <tr key={p.id}>
                <td>{p.id}</td>
                <td>{p.nome}</td>
                <td>R$ {p.precoVenda?.toFixed(2)}</td>
                <td>{p.estoque}</td>
                <td style={{ textAlign: 'center', whiteSpace: 'nowrap' }}>
                  <button className="btn-finalizar me-1" title="Editar" onClick={() => abrirEdicao(p)}>
                    <i className="bi bi-pencil"></i>
                  </button>
                  <button className="btn-cancelar" title="Remover" onClick={() => handleDelete(p.id)}>
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
                <h5 className="modal-title">{editando ? 'Editar Produto' : 'Novo Produto'}</h5>
                <button type="button" className="btn-close" onClick={() => setModal(false)}></button>
              </div>
              <form onSubmit={handleSave}>
                <div className="modal-body">
                  <div className="mb-3">
                    <label className="form-label">Nome *</label>
                    <input name="nome" className="form-control" value={form.nome} onChange={handleChange} required />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Preco de venda *</label>
                    <input type="number" step="0.01" min="0" name="precoVenda" className="form-control" value={form.precoVenda} onChange={handleChange} required />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Estoque *</label>
                    <input type="number" min="0" name="estoque" className="form-control" value={form.estoque} onChange={handleChange} required />
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
