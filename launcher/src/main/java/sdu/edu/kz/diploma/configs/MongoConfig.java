package sdu.edu.kz.diploma.configs;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri:mongodb://localhost:27017/MongoLocal_2_uri}")
    private String mongoUri;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        final var connectionString = new ConnectionString(mongoUri);
        final var database = connectionString.getDatabase() != null
                ? connectionString.getDatabase()
                : "MongoLocal_2_uri";
        return new MongoTemplate(mongoClient, database);
    }
}