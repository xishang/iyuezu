package com.iyuezu.mongo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import com.mongodb.Mongo;

@Configuration
@ComponentScan("com.iyuezu.mongo.service")
public class SpringMongoConfig extends AbstractMongoConfiguration {

	@Override
	protected String getDatabaseName() {
		return "iyuezu";
	}

	@Override
	protected UserCredentials getUserCredentials() {
		return new UserCredentials("dev", "Dev235");
	}

	@SuppressWarnings("deprecation")
	@Override
	public @Bean Mongo mongo() throws Exception {
		return new Mongo("39.108.9.208", 27017);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public MongoDbFactory mongoDbFactory() throws Exception {
		return new SimpleMongoDbFactory(mongo(), getDatabaseName(), getUserCredentials());
	}

	@Override
	public @Bean MappingMongoConverter mappingMongoConverter() throws Exception {
		MappingMongoConverter mmc = super.mappingMongoConverter();
		mmc.setTypeMapper(new DefaultMongoTypeMapper(null));
		return mmc;
	}
	
	@Override
	public @Bean MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongoDbFactory(), mappingMongoConverter());
	}

}
