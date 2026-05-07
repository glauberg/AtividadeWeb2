package br.ufrn.imd.agendamenteservicoscarro.service;

import br.ufrn.imd.agendamenteservicoscarro.model.Usuario;
import br.ufrn.imd.agendamenteservicoscarro.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SenhaMigrationService implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        for (Usuario usuario : usuarioRepository.findAll()) {
            String senha = usuario.getSenha();
            if (senha != null && !senha.startsWith("$2")) {
                usuario.setSenha(passwordEncoder.encode(senha));
                usuarioRepository.save(usuario);
            }
        }
    }
}
