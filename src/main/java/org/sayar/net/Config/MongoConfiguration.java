//package org.sayar.net.Config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientOptions;
//import com.mongodb.MongoCredential;
//import com.mongodb.ServerAddress;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//public class MongoConfiguration extends AbstractMongoConfiguration {
//    @Bean
//    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        MappingJackson2HttpMessageConverter converter =
//                new MappingJackson2HttpMessageConverter(mapper);
//        return converter;
//    }
//
//    private String host = "localhost";
//    private int port = 27017;
//    private String authenticationDB = "";
//    private String username = "";
//    private String password = "";
//    private int socketTimeout = 1000;
//    private int connectionTimeout = 1000;
//    private int connectionsPerHost = 100;
//
//    @Override
//    public MongoMappingContext mongoMappingContext() throws ClassNotFoundException {
//        return super.mongoMappingContext();
//    }
//
//    @Bean
//    public Class aClass() {
//        return null;
//    }
//
//
//    @Override
//    public MongoClient mongoClient() {
//        List<ServerAddress> servers = new ArrayList<>();
//        servers.add(new ServerAddress(host, port));
//
//        MongoClientOptions builder = MongoClientOptions.builder()
//                .socketTimeout(socketTimeout)
//                .connectTimeout(connectionTimeout)
//                .connectionsPerHost(connectionsPerHost)
//                .build();
//        return new MongoClient(servers, builder);
//    }
//
//    @Bean
//    public MongoTemplate mongoTemplate() throws Exception {
//        List<ServerAddress> servers = new ArrayList<>();
//        servers.add(new ServerAddress(host, port));
//
//        List<MongoCredential> credentials = new ArrayList<>();
//        credentials.add(MongoCredential.createCredential(username, authenticationDB, password.toCharArray()));
//
//        MongoClientOptions builder = MongoClientOptions.builder()
//                .socketTimeout(socketTimeout)
//                .connectTimeout(connectionTimeout)
//                .connectionsPerHost(connectionsPerHost)
//                .build();
//
//        return new MongoTemplate(new MongoClient(servers, builder)
//                , MongoConfiguration.getMongoDB());
//    }
//
//    @Override
//    protected String getDatabaseName() {
//        return mongoDB;
//    }
//
//    public static String getMongoDB() {
//        return mongoDB;
//    }
//
//}