package org.sayar.net.General.db.configuration;

import org.sayar.net.General.db.util.CommonQuery;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

public class CustomMongoTemplate<T> extends MongoTemplate {

    private final MongoDatabaseFactory mongoDbFactory;

    public CustomMongoTemplate(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
        this.mongoDbFactory = mongoDbFactory;
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory);
    }

    protected <T> CommonQuery<T> mongoQuery(Class<T> entityClass) {
        CommonQuery<T> queryDetails = new CommonQuery<T>(mongoDbFactory);
        queryDetails.setEntityClass(entityClass);
        return queryDetails;
    }
}
