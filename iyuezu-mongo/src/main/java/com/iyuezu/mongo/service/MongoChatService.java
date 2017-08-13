package com.iyuezu.mongo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.iyuezu.mongo.entity.ChatContent;
import com.iyuezu.mongo.params.ChatContentParams;

@Repository
public class MongoChatService {

	@Autowired
	private MongoTemplate mongoTemplate;

	public List<ChatContent> findContentByChatId(Integer chatId) {
		return mongoTemplate.find(new Query(Criteria.where("chatId").is(chatId)), ChatContent.class);
	}
	
	public List<ChatContent> findContents(ChatContentParams params) {
		Query query = params.createQuery(true);
		return mongoTemplate.find(query, ChatContent.class);
	}
	
	public void insertContent(ChatContent content) {
		mongoTemplate.insert(content);
	}
	
	public void removeContent(ChatContentParams params) {
		Query query = params.createQuery(false);
		mongoTemplate.remove(query, ChatContent.class);
	}
	
	public void updateContentStatus(String id, int status) {
		Query query = Query.query(Criteria.where("id").is(id));
		Update update = Update.update("status", status);
		mongoTemplate.updateMulti(query, update, ChatContent.class);
	}

}
