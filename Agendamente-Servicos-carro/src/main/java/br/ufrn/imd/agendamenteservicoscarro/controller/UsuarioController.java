package br.ufrn.imd.agendamenteservicoscarro.controller;

import br.ufrn.imd.agendamenteservicoscarro.dto.UsuarioResponse;
import br.ufrn.imd.agendamenteservicoscarro.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * GET /usuarios
     * Lista todos os usuários (shape: { id, email, nome, ativo, perfil: { nome } }).
     */
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    /**
     * POST /usuarios
     * Cadastra um novo acesso de usuário.
     * Body: { email, senha, perfilId }
     */
    @PostMapping
    public ResponseEntity<Object> cadastrar(@RequestBody Map<String, Object> body) {
        try {
            String email   = (String) body.get("email");
            String senha   = (String) body.get("senha");
            Long perfilId  = Long.valueOf(body.get("perfilId").toString());
            UsuarioResponse salvo = usuarioService.cadastrar(email, senha, perfilId);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * PATCH /usuarios/{id}/toggle-ativo
     * Alterna o estado ativo/inativo do usuário.
     */
    @PatchMapping("/{id}/toggle-ativo")
    public ResponseEntity<Object> toggleAtivo(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(usuarioService.toggleAtivo(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
