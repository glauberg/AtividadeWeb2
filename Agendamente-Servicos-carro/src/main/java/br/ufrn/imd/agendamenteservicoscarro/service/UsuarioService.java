package br.ufrn.imd.agendamenteservicoscarro.service;

import br.ufrn.imd.agendamenteservicoscarro.dto.UsuarioResponse;
import br.ufrn.imd.agendamenteservicoscarro.model.Perfil;
import br.ufrn.imd.agendamenteservicoscarro.model.Usuario;
import br.ufrn.imd.agendamenteservicoscarro.repository.PerfilRepository;
import br.ufrn.imd.agendamenteservicoscarro.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository  perfilRepository;
    private final PasswordEncoder passwordEncoder;

    /** Lista todos os usuários como DTO. */
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponse::from)
                .toList();
    }

    /** Busca um usuário pelo ID. */
    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado: id=" + id));
    }

    /** Busca um usuário pelo e-mail. */
    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado: email=" + email));
    }

    /**
     * Cadastra um novo usuário.
     * Corpo esperado: { email, senha, perfilId }
     * A senha é armazenada em texto plano por ora (TODO: adicionar hash BCrypt).
     */
    @Transactional
    public UsuarioResponse cadastrar(String email, String senha, Long perfilId) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Já existe um usuário com esse e-mail.");
        }
        Perfil perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new NoSuchElementException("Perfil não encontrado: id=" + perfilId));

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setAtivo(true);
        usuario.setPerfil(perfil);
        // O vínculo com Pessoa (Cliente ou Funcionario) deve ser feito
        // pelos endpoints /clientes ou /funcionarios após o cadastro de acesso.
        usuario.setPessoa(null);

        return UsuarioResponse.from(usuarioRepository.save(usuario));
    }

    /** Alterna o estado ativo/inativo de um usuário. */
    @Transactional
    public UsuarioResponse toggleAtivo(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(!usuario.getAtivo());
        return UsuarioResponse.from(usuarioRepository.save(usuario));
    }

    @Transactional
    public void atualizarSenhaHash(Usuario usuario, String senhaPura) {
        usuario.setSenha(passwordEncoder.encode(senhaPura));
        usuarioRepository.save(usuario);
    }
}
