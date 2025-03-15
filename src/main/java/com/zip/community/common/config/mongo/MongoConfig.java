package com.zip.community.common.config.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@EnableMongoAuditing
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), databaseName);
    }

    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(this.connectionString);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }
}

