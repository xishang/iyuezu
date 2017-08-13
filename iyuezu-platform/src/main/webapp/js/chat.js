var access_token = $.cookie("access_token");
var webSocket;
var chatList;
var friendList;
var curChat; // 当前聊天信息
var curChatUser; // 当前用户群聊关系
var ownUuid; // 当前用户uuid
var popupFlag = 0; // 弹窗内容[0:添加好友, 1:编辑对话]

//允许上传的图片类型
var allowTypes = [ "image/jpg", "image/jpeg", "image/png", "image/gif" ];
// 图片最大内存
var maxSize = 1024 * 1024 * 5;
// 图片最大宽度
var maxWidth = 300;

$(function() {
	getOwnUuid();
	initCommonNav();
	bindEvent();
	initSearchBar();
	initWebSocket();
	initUpload();
	initData();
});

// 得到用户uuid
function getOwnUuid() {
	ajax("/user/own", "GET", null, function(result) {
		if (result.code == "0") {
			ownUuid = result.data.uuid;
		}
	}, false);
}

function bindEvent() {
	$("#page-nav a:eq(3)").attr("href", "").addClass("active");
	$("#chat-tab-nav, #friend-tab-nav").click(function() {
		$("#chat-edit").attr("data-popup", ".popup-friend");
		$("#chat-edit").text("添加好友");
		$("#chat-title").text("聊天信息");
		popupFlag = 0;
	});
	$("#talk-tab-nav").click(function() {
		$("#chat-edit").attr("data-popup", ".popup-chat");
		$("#chat-edit").text("编辑对话");
		getTalkData();
		popupFlag = 1;
	});
	$("#chat-edit").click(function() {
		if (popupFlag == 0) { // 添加好友弹窗, 不需要预处理
			return;
		}
		curChatUser = getChatUser(curChat.id); // 获取用户当前聊天关系设置
		fillPopupChat();
	});
	$("#add-chat-user").click(function() {
		fillPopupChatUser();
	});
	// 修改群聊名称
	$("#popup-chat-name").click(function() {
		var chatName = $(this).text();
		$("#edit-chat-name").val(chatName);
		$("#edit-chat-name-dialog").fadeIn(200);
	});
	// 修改用户群聊昵称
	$("#popup-chat-userNick").click(function() {
		var userNick = $(this).text();
		$("#edit-user-nick").val(userNick);
		$("#edit-user-nick-dialog").fadeIn(200);
	});
}

function getChatUser(chatId) {
	var chatUser = null;
	ajax("/chat/ownChatUser", "GET", {
		chatId : chatId
	}, function(result) {
		if (result.code == "0") {
			chatUser = result.data;
		}
	}, false);
	return chatUser;
}

// 填充用户聊天信息编辑页面
function fillPopupChat() {
	var users = curChat.users;
	var userListHtml = "";
	for (var i = 0; i < users.length; i++) {
		var userHtml = "<li class='weui-uploader__file' style='background-image:url(" + (users[i].head ? users[i].head : "./images/unknown.png") + ")'></li>";
		userListHtml += userHtml;
	}
	$("#popup-chat-users").html(userListHtml);
	$("#popup-chat-name").text(curChat.name);
	$("#popup-chat-userNick").text(curChatUser.userNick);
	$("#popup-chat-disturb").prop("checked", curChatUser.disturb == 1);
}

// 填充添加聊天用户界面
function fillPopupChatUser() {
	var userListHtml = "";
	var users = curChat.users;
	for (var i = 0; i < friendList.length; i++) {
		var item = friendList[i];
		if (!isUserExist(item, users)) { // 好友不在聊天成员中, 可以加入
			var userHtml = "<div class='card facebook-card list-item' uuid='" + item.uuid + "'>"
				+ "<div class='card-header'>"
				+ "<div class='facebook-avatar'>"
				+ "<img src='" + (item.head ? item.head : "./images/unknown.png") + "' width='34' height='34'>"
				+ "</div>"
				+ "<div class='facebook-name'>" + item.userNick + "</div>"
				+ "<div class='facebook-date'>" + item.username + "</div>"
				+ "</div>"
				+ "</div>";
			userListHtml += userHtml;
		}
	}
	$("#chat-filter-users").html(userListHtml);
	// 聊天信息添加好友列表项
	$(".list-item").unbind("click").bind("click", function() {
		if ($(this).hasClass("item-selected")) {
			$(this).removeClass("item-selected");
		} else {
			$(this).addClass("item-selected");
		}
	});
}

// 添加聊天用户界面点击添加
function addChatUsers() {
	var userUuids = "";
	$(".item-selected").each(function() {
		var uuid = $(this).attr("uuid");
		userUuids += uuid + ",";
	});
	if (userUuids.length == 0) {
		Zepto.toast("请选择要添加的好友");
		return;
	}
	userUuids = userUuids.substr(0, userUuids.length - 1);
	post("/chat/addUser", {
		chatId : curChat.id,
		userUuids : userUuids
	}, function(data) {
		if (data.code == "0") {
			// 添加用户成功, 重新加载聊天用户
			curChat.users = getChatUsers(curChat.id);
		}
	});
}

// 修改聊天信息名称
function editChatName() {
	var chatName = $("#edit-chat-name").val();
	if (!chatName) {
		Zepto.toast("请输入群聊名称");
		return;
	}
	post("/chat/edit", {
		chatId : curChat.id,
		name : chatName
	}, function(data) {
		Zepto.toast(data.msg);
		if (data.code == "0") {
			curChat.name = chatName;
			$("#chat-title").text(chatName);
			$("#edit-chat-name-dialog").fadeOut(200);
		}
	});
}

// 修改用户群聊昵称
function editUserNick() {
	var userNick = $("#edit-user-nick").val();
	if (!userNick) {
		Zepto.toast("请输入群聊昵称");
		return;
	}
	post("/chat/setUserNick", {
		chatUserId : curChatUser.id,
		userNick : userNick
	}, function(data) {
		Zepto.toast(data.msg);
		if (data.code == "0") {
			curChatUser.userNick = userNick;
			$("#edit-user-nick-dialog").fadeOut(200);
		}
	});
}

function isUserExist(user, list) {
	for (var i = 0; i < list.length; i++) {
		var item = list[i];
		if (item.uuid == user.uuid) {
			return true;
		}
	}
	return false;
}

function initSearchBar() {
	function hideSearchResult() {
		$("#search-input").val("");
	}
	$("#search-text").click(function() {
		$("#search-bar").addClass("weui-search-bar_focusing");
		$("#search-input").focus();
	});
	$("#search-clear").click(function() {
		hideSearchResult();
		$("#search-input").focus();
	});
	$("#search-cancel").click(function() {
		var keyword = $("#search-input").val();
		if (!keyword) {
			Zepto.toast("请先输入关键字");
			return;
		}
		getUsersByKeyword(keyword);
	});
}

//添加好友时按关键字搜索
function getUsersByKeyword(keyword) {
	get("/user/listByKeyword", {
		keyword : keyword
	}, function(data) {
		if (data == null) {
			Zepto.toast("按关键字获取用户信息失败");
			return;
		}
		fillFilterUserList(data);
	});
}

function fillFilterUserList(userList) {
	if (userList.length == 0) {
		$("#filter-users").text("未搜索到用户");
		return;
	}
	var userListHtml = "";
	for (var i = 0; i < userList.length; i++) {
		var item = userList[i];
		var userHtml = "<div class='card facebook-card close-popup' onclick='openAddFriendDialog(\"" + item.uuid + "\", \"" + item.account + "\", \"" + item.username + "\");'>"
			+ "<div class='card-header'>"
			+ "<div class='facebook-avatar'>"
			+ "<img src='" + (item.head ? item.head : "./images/unknown.png") + "' width='34' height='34'>"
			+ "</div>"
			+ "<div class='facebook-name'>" + item.account + "</div>"
			+ "<div class='facebook-date'>" + item.username + "</div>"
			+ "</div>"
			+ "</div>";
		userListHtml += userHtml;
	}
	$("#filter-users").html(userListHtml);
}

function openAddFriendDialog(userUuid, account, username) {
	$("#add-friend-uuid").val(userUuid);
	$("#add-friend-account").val(account);
	$("#add-friend-username").val(username);
	$("#add-friend-nick").val("");
	$("#add-friend-dialog").fadeIn(200);
}

function addFriend() {
	var friendUuid = $("#add-friend-uuid").val();
	var friendNick = $("#add-friend-nick").val();
	var params = {};
	params.friendUuid = friendUuid;
	if (friendNick) {
		params.friendNick = friendNick;
	}
	post("/user/addFriend", params, function(data) {
		if (data && data.code == "0") {
			Zepto.toast(data.msg);
			$("#add-friend-dialog").fadeOut(200);
			getFriends(); // 重新填充好友列表
			
			$(".tab-link").removeClass("active");
			$("#friend-tab-nav").addClass("active");
			$(".tab").removeClass("active");
			$("#friend-tab").addClass("active");
			
			$("#friend-tab-nav").click();
		}
	});
	
}

function initWebSocket() {
	if ('WebSocket' in window) {
		websocket = new WebSocket("ws://" + getDomain() + "/ws?access_token="
				+ access_token);
	} else if ('MozWebSocket' in window) {
		websocket = new MozWebSocket("ws://" + getDomain()
				+ "/ws?access_token=" + access_token);
	} else {
		websocket = new SockJS("http://" + getDomain()
				+ "/ws/sockjs?access_token=" + access_token);
	}
	websocket.onopen = function(event) {
		console.log("WebSocket:已连接");
		console.log(event);
	};
	websocket.onmessage = function(event) {
		var data = JSON.parse(event.data); // 接收到消息
		var content = new ChatContent(data.id, data.chatId, data.userUuid, data.type, data.content, data.status, data.createTime, data.updateTime);
		addContent(content); // 加入chat
	};
	websocket.onerror = function(event) {
		console.log("WebSocket:发生错误 ");
		console.log(event);
	};
	websocket.onclose = function(event) {
		console.log("WebSocket:已关闭");
		console.log(event);
	}
}

/**
 * 初始化图片上传插件
 */
function initUpload() {
	
	$("#picture-upload").on("change", function(event) {
		var files = event.target.files;
		// 如果没有选中文件，直接返回
		if (files.length === 0) {
			return;
		}
		var file = files[0];
		var reader = new FileReader();
		// 如果类型不在允许的类型范围内
		if (allowTypes.indexOf(file.type) === -1) {
			Zepto.toast("该类型不允许上传");
			return;
		}
		if (file.size > maxSize) {
			Zepto.toast("图片太大，不允许上传");
			return;
		}
		reader.onload = function(e) {
			var img = new Image();
			img.onload = function() {
				// 不要超出最大宽度
				var w = Math.min(maxWidth, img.width);
				// 高度按比例计算
				var h = img.height * (w / img.width);
				var canvas = document.createElement('canvas');
				var ctx = canvas.getContext('2d');
				// 设置 canvas 的宽度和高度
				canvas.width = w;
				canvas.height = h;
				ctx.drawImage(img, 0, 0, w, h);
				var base64 = canvas.toDataURL('image/png');
				post("/uploadBase64", {
					base64 : base64
				}, function(result) {
					if (result.code == "0") {
						headImage = result.data;
						sendImage(headImage);
					} else {
						Zepto.toast("图片发送失败");
					}
				});
			};
			img.src = e.target.result;
		};
		reader.readAsDataURL(file);
	});
}

// 收到websocket消息, 加入chat
function addContent(content) {
	var chatId = content.chatId;
	var chat = getChatInList(chatId);
	if (chat == null) { // 新的chat
		chat = getChat(chatId);
	}
	var contents = chat.contents;
	for (var i = 0; i < contents.length; i++) {
		var item = contents[i];
		if (item.createTs == content.createTs && item.userUuid == content.userUuid) { // 重复的消息
			return;
		}
	}
	chat.msgTs = content.createTs;
	chat.msgCount += 1;
	chat.contents.push(content);
	chatList.push(chat);
	if (curChat.id == chatId) { // 当前对话
		getTalkData();
	}
}

function getChatInList(chatId) {
	for (var i = 0; i < chatList.length; i++) {
		var chat = chatList[i];
		if (chat.id == chatId) {
			return chat;
		}
	}
	return null;
}

function getChat(chatId) {
	// 网络请求获取chat
	var chat = null;
	ajax("/chat/" + chatId, "GET", null, function(result) {
		if (result.code == "0") {
			var data = result.data;
			chat = new Chat(data.id, data.name, data.remark, data.createTime, data.updateTime, data.activeTime);
		}
	}, false);
	return chat;
}

function initData() {
	getActiveChats();
	getFriends();
}

// 获取最近活跃的聊天信息
function getActiveChats() {
	chatList = [];
	get("/chat/activeChats", null, function(data) {
		if (data == null) {
			Zepto.toast("获取最近聊天信息失败");
			return;
		}
		for (var i = 0; i < data.length; i++) {
			var item = data[i];
			var chat = new Chat(item.id, item.name, item.remark, item.createTime, item.updateTime, item.activeTime);
			chatList.push(chat);
		}
		fillChatList();
	});
}

function fillChatList() {
	if (chatList.length == 0) {
		$("#chat-list").text("暂无聊天信息");
		return;
	}
	chatList.sort(function(a, b) {
		return b.msgTs - a.msgTs;
	});
	var chatListHtml = "";
	for (var i = 0; i < chatList.length; i++) {
		var item = chatList[i];
		var chatHtml = "<div class='card facebook-card' onclick='openChat(\"" + item.id + "\");'>"
			+ "<div class='card-header'>"
			+ "<div class='facebook-avatar'>"
			+ "<img src='./images/chat01.jpg' width='34' height='34'>"
			+ "</div>"
			+ "<div class='facebook-name'>" + item.name + "</div>"
			+ "<div class='facebook-date'>" + item.activeTime + "</div>"
			+ "</div>"
			+ "</div>";
		chatListHtml += chatHtml;
	}
	$("#chat-list").html(chatListHtml);
}

// 打开聊天信息
function openChat(chatId) {
	curChat = getChatInList(chatId);
	
	$(".tab-link").removeClass("active");
	$("#talk-tab-nav").addClass("active");
	$(".tab").removeClass("active");
	$("#talk-tab").addClass("active");
	
	$("#talk-tab-nav").click();
}

// 获取好友列表
function getFriends() {
	friendList = [];
	get("/user/friends", {
		page : 1,
		row : 100
	}, function(data) {
		if (data == null) {
			Zepto.toast("获取好友列表失败");
			return;
		}
		var list = data.list;
		for (var i = 0; i < list.length; i++) {
			var item = list[i];
			var friend = new UserMember(item.uuid, item.username, item.userNick, item.name, item.phone, item.email, item.head, item.type, item.createTime);
			friendList.push(friend);
		}
		fillFriendList();
	});
}

function fillFriendList() {
	if (friendList.length == 0) {
		$("#friend-list").text("暂无好友");
		return;
	}
	friendList.sort(function(a, b) {
		return b.createTs - a.createTs;
	});
	var friendListHtml = "";
	for (var i = 0; i < friendList.length; i++) {
		var item = friendList[i];
		var friendHtml = "<div class='card facebook-card' onclick='createChatWithFriend(\"" + item.uuid + "\");'>"
			+ "<div class='card-header'>"
			+ "<div class='facebook-avatar'>"
			+ "<img src='" + (item.head ? item.head : "./images/unknown.png") + "' width='34' height='34'>"
			+ "</div>"
			+ "<div class='facebook-name'>" + item.userNick + "</div>"
			+ "<div class='facebook-date'>" + item.username + "</div>"
			+ "</div>"
			+ "</div>";
		friendListHtml += friendHtml;
	}
	$("#friend-list").html(friendListHtml);
}

//点击朋友创建聊天
function createChatWithFriend(friendUuid) {
	post("/chat/createWithFriend", {
		friendUuid : friendUuid
	}, function(data) {
		if (data != null && data.code == "0") {
			var item = data.data;
			curChat = new Chat(item.id, item.name, item.remark, item.createTime, item.updateTime, item.activeTime);
			chatList.push(curChat);
			fillChatList();
			
			$(".tab-link").removeClass("active");
			$("#talk-tab-nav").addClass("active");
			$(".tab").removeClass("active");
			$("#talk-tab").addClass("active");
			
			$("#talk-tab-nav").click();
		}
	});
}

// 聊天面板
function getTalkData() {
	// 修改标题栏
	$("#chat-title").text(curChat.name);
	
	var users = curChat.users;
	if (users.length == 0) { // 尚未获取聊天用户
		users = getChatUsers(curChat.id);
	}
	var contents = curChat.contents;
	if (contents.length == 0) { // 尚未获取聊天历史信息
		contents = getChatContents(curChat.id, 10, null);
	}
	contents.sort(function(a, b) {
		return a.createTs - b.createTs;
	});
	// contentList设置userNick和userHead
	for (var i = 0; i < contents.length; i++) {
		var content = contents[i];
		for (var j = 0; j < users.length; j++) {
			var user = users[j];
			if (content.userUuid == user.uuid) {
				content.userNick = user.userNick ? user.userNick : user.username;
				content.userHead = user.head;
				break;
			}
		}
	}
	curChat.users = users;
	curChat.contents = contents;
	// 填充html
	fillChatHtml(curChat);
	scrollToBottom();
}

// 获取聊天信息的成员
function getChatUsers(chatId) {
	var users = [];
	ajax("/chat/userMember", "GET", {
		chatId : chatId
	}, function(data) {
		if (data != null) {
			for (var i = 0; i < data.length; i++) {
				var item = data[i];
				var user = new UserMember(item.uuid, item.username, item.userNick, item.name, item.phone, item.email, item.head, item.type, item.createTime);
				users.push(user);
			}
		}
	}, false);
	return users;
}

/**
 * 获取聊天历史信息
 * 
 * @param chatId	群聊ID
 * @param count		聊天内容条数
 * @param endTime	截止时间
 * @returns
 */
function getChatContents(chatId, count, endTime) {
	var contents = [];
	ajax("/chat/contentList", "GET", {
		chatId : chatId,
		endTime : endTime,
		page : 1,
		row : count
	}, function(data) {
		if (data != null) {
			for (var i = 0; i < data.length; i++) {
				var item = data[i];
				var content = new ChatContent(item.id, item.chatId, item.userUuid, item.type, item.content, item.status, item.createTime, item.updateTime);
				contents.push(content);
			}
		}
	}, false);
	return contents;
}

function fillChatHtml(chat) {
	var chatListHtml = "";
	var contents = chat.contents;
	for (var i = 0; i < contents.length; i++) {
		var item = contents[i];
		var chatHtml = "<div class='card facebook-card' style='background-color:"+(item.userUuid == ownUuid ? "lightgreen" : "lightyellow")+";'>";
		chatHtml += "<div class='card-header'>";
		chatHtml += "<div class='facebook-avatar'>";
		chatHtml += "<img src='" + (item.userHead ? item.userHead : "./images/unknown.png") + "' width='34' height='34'>";
		chatHtml += "</div>";
		chatHtml += "<div class='facebook-name'>" + item.userNick + "</div>";
		chatHtml += "<div class='facebook-date'>" + item.createTime + "</div>";
		chatHtml += "</div>";
		chatHtml += "<div class='card-content'>";
		if (item.type == 1) {
			chatHtml += "<div class='card-content-inner'>" + item.content + "</div>";
		} else if (item.type == 2) {
			chatHtml += "<img src='" + item.content + "' width='100%'>";
		}
		chatHtml += "</div>";
		chatHtml += "</div>";
		chatListHtml += chatHtml;
	}
	$("#content-list").html(chatListHtml);
	$("#msg-content").val("");
}

// 下移到底部
function scrollToBottom(){
	var div = document.getElementById('content-panel');
	div.scrollTop = div.scrollHeight;
}

// 发送消息
function sendMsg() {
	var content = $("#msg-content").val();
	if (!content) {
		Zepto.toast("请输入要发送的消息");
		return;
	}
	var data = {};
	data.chatId = curChat.id;
	data.userUuid = ownUuid;
	data.type = 1;
	data.content = content;
	websocket.send(JSON.stringify(data));
	$("#msg-content").val("");		
}

/**
 * 发送图片消息
 * 
 * @param imageUrl	图片url
 * @returns
 */
function sendImage(imageUrl) {
	var data = {};
	data.chatId = curChat.id;
	data.userUuid = ownUuid;
	data.type = 2;
	data.content = imageUrl;
	websocket.send(JSON.stringify(data));
}

function Chat(id, name, remark, createTime, updateTime, activeTime) {
	this.id = id;
	this.name = name;
	this.remark = remark;
	this.createTime = new Date(createTime).format("yyyy-MM-dd hh:mm:ss");
	this.updateTime = new Date(updateTime).format("yyyy-MM-dd hh:mm:ss");
	this.msgTs = activeTime; // 最后一条消息时间
	this.activeTime = new Date(activeTime).format("yyyy-MM-dd hh:mm:ss");
	this.msgCount = 0; // 未读信息条数
	this.users = [];
	this.contents = [];
}

// 仅作为当前用户获取对话设置信息
function ChatUser(id, chatId, userUuid, userNick, disturb, status, createTime, updateTime) {
	this.id = id;
	this.chatId = chatId;
	this.userUuid = userUuid;
	this.userNick = userNick;
	this.disturb = disturb;
	this.status = status;
	this.createTime = new Date(createTime).format("yyyy-MM-dd hh:mm:ss");
	this.updateTime = new Date(updateTime).format("yyyy-MM-dd hh:mm:ss");
}

function ChatContent(id, chatId, userUuid, type, content, status, createTime, updateTime) {
	this.id = id;
	this.chatId = chatId;
	this.userUuid = userUuid;
	this.userNick = "";
	this.userHead = "";
	this.type = type; // 1:text, 2:picture, 3:audio, 4:video
	this.content = content;
	this.status = status;
	this.createTs = createTime; // 用来排序
	this.createTime = new Date(createTime).format("yyyy-MM-dd hh:mm:ss");
	this.updateTime = new Date(updateTime).format("yyyy-MM-dd hh:mm:ss");
}

function UserMember(uuid, username, userNick, name, phone, email, head, type, createTime) {
	this.uuid = uuid;
	this.username = username;
	this.userNick = userNick;
	this.name = name;
	this.phone = phone;
	this.email = email;
	this.head = head;
	this.type = type;
	this.createTs = createTime;
	this.createTime = new Date(createTime).format("yyyy-MM-dd hh:mm:ss");
}

