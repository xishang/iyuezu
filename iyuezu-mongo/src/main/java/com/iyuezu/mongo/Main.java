package com.iyuezu.mongo;

import java.util.List;

import com.iyuezu.mongo.entity.ChatContent;
import com.iyuezu.mongo.service.MongoChatService;
import com.iyuezu.mongo.utils.SpringContextHolder;

public class Main {

	public static void main(String[] args) {
		SpringContextHolder.init();
		MongoChatService service = SpringContextHolder.getBean(MongoChatService.class);
//		ChatContent content = new ChatContent();
//		content.setChatId(1);
//		content.setUsername("sam");
//		content.setType(2);
//		content.setStatus(1);
//		content.setContent("http://localhost:80/pics/a.jpg");
//		content.setCreateTime(System.currentTimeMillis());
//		content.setUpdateTime(System.currentTimeMillis());
//		service.insertContent(content);
		List<ChatContent> list = service.findContentByChatId(1);
//		service.updateContentStatus("590d99dd5b01e8487884ad91", 3);
		System.out.println(list.toString());
	}

}
