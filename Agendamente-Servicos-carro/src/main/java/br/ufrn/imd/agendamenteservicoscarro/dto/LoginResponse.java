package br.ufrn.imd.agendamenteservicoscarro.dto;

/**
 * Resposta de POST /auth/login.
 * O frontend espera: { usuario: { id, email, nome, perfil }, token }
 * onde perfil é a string do papel (ex: "ROLE_GERENTE").
 */
public record LoginResponse(UsuarioInfo usuario, String token) {
    public record UsuarioInfo(Long id, String email, String nome, String perfil) {}
}
