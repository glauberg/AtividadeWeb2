package br.ufrn.imd.agendamenteservicoscarro.service;

import br.ufrn.imd.agendamenteservicoscarro.model.Cliente;
import br.ufrn.imd.agendamenteservicoscarro.model.Funcionario;
import br.ufrn.imd.agendamenteservicoscarro.model.Pessoa;
import br.ufrn.imd.agendamenteservicoscarro.model.Usuario;
import br.ufrn.imd.agendamenteservicoscarro.model.enums.PerfilNome;
import br.ufrn.imd.agendamenteservicoscarro.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioLogadoService {

    private final UsuarioRepository usuarioRepository;

    public Usuario atual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new AccessDeniedException("Usuario nao autenticado.");
        }
        return usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new AccessDeniedException("Usuario autenticado nao encontrado."));
    }

    public boolean temPerfil(PerfilNome perfil) {
        return atual().getPerfil().getNome() == perfil;
    }

    public boolean ehCliente() {
        return temPerfil(PerfilNome.ROLE_CLIENTE);
    }

    public boolean ehMecanico() {
        return temPerfil(PerfilNome.ROLE_MECANICO);
    }

    public boolean ehGerente() {
        return temPerfil(PerfilNome.ROLE_GERENTE);
    }

    public Long clienteIdObrigatorio() {
        Pessoa pessoa = atual().getPessoa();
        if (pessoa instanceof Cliente cliente) {
            return cliente.getId();
        }
        throw new AccessDeniedException("Usuario logado nao e cliente.");
    }

    public Long funcionarioIdObrigatorio() {
        Pessoa pessoa = atual().getPessoa();
        if (pessoa instanceof Funcionario funcionario) {
            return funcionario.getId();
        }
        throw new AccessDeniedException("Usuario logado nao e funcionario.");
    }

    public void negarCliente(String mensagem) {
        if (ehCliente()) {
            throw new AccessDeniedException(mensagem);
        }
    }

    public void exigirGerente(String mensagem) {
        if (!ehGerente()) {
            throw new AccessDeniedException(mensagem);
        }
    }
}
