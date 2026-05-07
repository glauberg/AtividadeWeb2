package br.ufrn.imd.agendamenteservicoscarro.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Configuração explícita do MongoDB Atlas (banco de auditoria).
 *
 * <p>
 * Ao usar {@link EnableMongoRepositories} com pacote base definido,
 * garantimos que os repositórios Mongo (pacote {@code audit}) são tratados
 * separadamente dos repositórios JPA (pacote {@code repository}),
 * evitando conflitos de auto-configuração entre os dois contextos de dados.
 * </p>
 */
@Configuration
@EnableMongoRepositories(basePackages = "br.ufrn.imd.agendamenteservicoscarro.audit")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }

    /**
     * Habilita suporte a transações MongoDB (replica set / Atlas).
     * Necessário para uso de @Transactional com MongoDB.
     */
    @Bean
    public MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
}
