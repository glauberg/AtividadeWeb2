export default function Footer() {
  const ano = new Date().getFullYear()

  return (
    <footer id="site-footer">
      <div className="container">
        <div className="row g-4">
          {/* Sobre */}
          <div className="col-12 col-md-4">
            <h6><i className="bi bi-car-front-fill me-1"></i>Oficina AutoAgenda</h6>
            <p style={{ lineHeight: 1.7 }}>
              Sistema de agendamento de serviços automotivos desenvolvido como projeto
              acadêmico na UFRN — IMD.
            </p>
          </div>

          {/* Contato */}
          <div className="col-12 col-md-4">
            <h6>Contato</h6>
            <ul className="list-unstyled" style={{ lineHeight: 2 }}>
              <li>
                <i className="bi bi-envelope me-2"></i>
                <a href="mailto:contato@autoagenda.com.br" target="_blank" rel="noreferrer">
                  contato@autoagenda.com.br
                </a>
              </li>
              <li>
                <i className="bi bi-telephone me-2"></i>
                <a href="tel:+558432009999" target="_blank" rel="noreferrer">
                  (84) 3200-9999
                </a>
              </li>
              <li>
                <i className="bi bi-geo-alt me-2"></i>
                Av. Sen. Salgado Filho, 3000 — Natal/RN
              </li>
            </ul>
          </div>

          {/* Redes sociais */}
          <div className="col-12 col-md-4">
            <h6>Redes Sociais</h6>
            <div className="footer-social">
              <a href="https://instagram.com" target="_blank" rel="noreferrer" title="Instagram">
                <i className="bi bi-instagram"></i>
              </a>
              <a href="https://facebook.com" target="_blank" rel="noreferrer" title="Facebook">
                <i className="bi bi-facebook"></i>
              </a>
              <a href="https://wa.me/5584999990000" target="_blank" rel="noreferrer" title="WhatsApp">
                <i className="bi bi-whatsapp"></i>
              </a>
              <a href="https://linkedin.com" target="_blank" rel="noreferrer" title="LinkedIn">
                <i className="bi bi-linkedin"></i>
              </a>
            </div>
          </div>
        </div>

        <div className="footer-copy">
          &copy; {ano} Oficina AutoAgenda — Todos os direitos reservados. UFRN / IMD.
        </div>
      </div>
    </footer>
  )
}
