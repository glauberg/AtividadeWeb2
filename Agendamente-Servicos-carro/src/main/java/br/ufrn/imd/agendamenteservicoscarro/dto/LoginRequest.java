package br.ufrn.imd.agendamenteservicoscarro.dto;

/**
 * Corpo da requisição POST /auth/login.
 */
public record LoginRequest(String email, String senha) {}
