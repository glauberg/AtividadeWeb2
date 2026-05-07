package br.ufrn.imd.agendamenteservicoscarro.controller;

import br.ufrn.imd.agendamenteservicoscarro.dto.LoginRequest;
import br.ufrn.imd.agendamenteservicoscarro.dto.LoginResponse;
import br.ufrn.imd.agendamenteservicoscarro.model.Cliente;
import br.ufrn.imd.agendamenteservicoscarro.model.Pessoa;
import br.ufrn.imd.agendamenteservicoscarro.model.Usuario;
import br.ufrn.imd.agendamenteservicoscarro.security.JwtUtil;
import br.ufrn.imd.agendamenteservicoscarro.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest req) {
        try {
            Usuario usuario = usuarioService.buscarPorEmail(req.email());

            if (!usuario.getAtivo()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuario inativo.");
            }

            if (!senhaValida(usuario, req.senha())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha invalidos.");
            }

            String perfilNome = usuario.getPerfil().getNome().name();
            Pessoa pessoa = usuario.getPessoa();
            String nome = pessoa != null ? pessoa.getNome() : usuario.getEmail();
            Long pessoaId = pessoa != null ? pessoa.getId() : null;
            Long clienteId = pessoa instanceof Cliente cliente ? cliente.getId() : null;

            String token = jwtUtil.gerarToken(usuario.getId(), usuario.getEmail(), perfilNome);

            LoginResponse response = new LoginResponse(
                    new LoginResponse.UsuarioInfo(usuario.getId(), pessoaId, clienteId, usuario.getEmail(), nome, perfilNome),
                    token
            );
            return ResponseEntity.ok(response);

        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha invalidos.");
        }
    }

    private boolean senhaValida(Usuario usuario, String senhaInformada) {
        String senhaSalva = usuario.getSenha();
        if (senhaSalva == null || senhaInformada == null) {
            return false;
        }

        if (senhaSalva.startsWith("$2") && passwordEncoder.matches(senhaInformada, senhaSalva)) {
            return true;
        }

        if (senhaSalva.equals(senhaInformada)) {
            usuarioService.atualizarSenhaHash(usuario, senhaInformada);
            return true;
        }

        return false;
    }
}
