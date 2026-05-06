// Componente reutilizável de badge de status
export default function StatusBadge({ status }) {
  const map = {
    AGENDADO:      { cls: 'badge-agendado',   label: 'Agendado'       },
    EM_MANUTENCAO: { cls: 'badge-manutencao', label: 'Em Manutenção'  },
    CONCLUIDO:     { cls: 'badge-concluido',  label: 'Concluído'      },
    CANCELADO:     { cls: 'badge-cancelado',  label: 'Cancelado'      },
  }
  const entry = map[status] || { cls: '', label: status }
  return (
    <span className={`status-badge ${entry.cls}`}>{entry.label}</span>
  )
}
