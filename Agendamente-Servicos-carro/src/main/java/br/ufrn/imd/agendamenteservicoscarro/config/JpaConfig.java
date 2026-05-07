package br.ufrn.imd.agendamenteservicoscarro.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configuração explícita dos repositórios JPA.
 *
 * <p>Limita o scan do Spring Data JPA apenas ao pacote {@code repository},
 * impedindo que a auto-configuração tente registrar os repositórios MongoDB
 * do pacote {@code audit} como repositórios JPA — o que causaria erros
 * de inicialização ao ter dois provedores de dados ativos simultaneamente.</p>
 *
 * <p>Em conjunto com {@link MongoConfig} (que faz o mesmo para o MongoDB),
 * esta classe garante o isolamento completo entre os dois contextos de dados:
 * <ul>
 *   <li><b>JPA / Supabase PostgreSQL</b>: pacote {@code repository}</li>
 *   <li><b>Spring Data MongoDB / Atlas</b>: pacote {@code audit}</li>
 * </ul>
 * </p>
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "br.ufrn.imd.agendamenteservicoscarro.repository"
)
public class JpaConfig {
    // Configuração via anotação — nenhum bean adicional necessário.
}
