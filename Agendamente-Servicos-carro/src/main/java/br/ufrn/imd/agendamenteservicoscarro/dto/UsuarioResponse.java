package br.ufrn.imd.agendamenteservicoscarro.dto;

import br.ufrn.imd.agendamenteservicoscarro.model.Usuario;

/**
 * Resposta de GET /usuarios.
 * Achata o campo pessoa.nome para o nível raiz do JSON,
 * conforme esperado pelo frontend (UsuariosPage).
 */
public record UsuarioResponse(
        Long id,
        String email,
        String nome,
        Boolean ativo,
        PerfilInfo perfil
) {
    public record PerfilInfo(String nome) {}

    public static UsuarioResponse from(Usuario u) {
        return new UsuarioResponse(
                u.getId(),
                u.getEmail(),
                u.getPessoa() != null ? u.getPessoa().getNome() : null,
                u.getAtivo(),
                u.getPerfil() != null
                        ? new PerfilInfo(u.getPerfil().getNome().name())
                        : null
        );
    }
}
