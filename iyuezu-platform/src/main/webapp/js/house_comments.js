var houseUuid = getUrlParams().houseUuid;

var curPage = 1;
var row = 10;

var visitor = {username : "游客"};

var thumbCommentUuids;

var curReplyCommentUuid;

var allCommentList;

$(function() {
	initCommonNav();
	allCommentList = [];
	getThumbCommentUuids(function(result) {
		thumbCommentUuids = result;
	});
	limitSize($("#reply-content"), $("#detail-count"), 300);
	enableSubmit();
	$("#load-more").click(function() {
		curPage++;
		getCommentList();
	});
	getCommentList();
});

function enableSubmit() {
	$("#reply-comment").unbind("click").bind("click", function() {
		replyComment();
	});
}

function disableSubmit() {
	$("#reply-comment").unbind("click").bind("click", function() {
		Zepto.toast("你的提交请求正在处理中，请稍候...");
	});
}

function getCommentList() {
	$("#load-more").hide();
	$("#loading-icon").show();
	get("/house/comment/list", {
		houseUuid : houseUuid,
		page : curPage,
		row : row
	}, function(result) {
		$("#loading-icon").hide();
		if (result.code == 0) {
			var pageCount = result.data.pages;
			if (curPage == pageCount || pageCount == 0) {
				$("#no-more-label").show();
			} else {
				$("#load-more").show();
			}
			var houseList = result.data.list;
			fillCommentList(houseList);
		} else {
			Zepto.toast(result.msg);
		}
	});
}


function fillCommentList(list) {
	sortCommentList(list);
	allCommentList = allCommentList.concat(list);
	var listHtml = "";
	for (var i = 0; i < list.length; i++) {
		var comment = list[i];
		var user = comment.user ? comment.user : visitor;
		var replys = comment.replys;
		var elementHtml = "<div class='card my-facebook-card'>"
			+ "<div class='card-header no-border'>"
			+ "<div class='facebook-avatar'>"
			+ "<img src='" + getHost() + (user.head ? user.head: "/images/yue_head.jpg") + "' width='34' height='34' alt=''>"
			+ "</div>"
			+ "<div class='facebook-name'>" + user.username + "</div>"
			+ "<div class='facebook-date' style='position: absolute;right:4%;top:12%;'>"
				+ (comment.compScore > 0 ? ("评分:" + comment.compScore) : "暂无评分") + "</div>"
			+ "<div class='facebook-date'>" + formatFullTime(comment.createTime) + "</div>"
			+ "</div>"
			+ "<div class='card-content'>"
			+ "<div class='card-content-inner' style='text-indent:36px;'>" + comment.content + "</div>"
			+ "</div>";
		if (replys && replys.length > 0) {
			var replysHtml = "<div class='card-footer'>"
				+ "<div id='reply-" + comment.uuid + "' class='card-content-inner' style='width:100%;'>";
			for (var j = 0; j < 2 && j < replys.length; j++) {
				var reply = replys[j];
				var replyHtml = "<div><span style='color:#0894ec'>" + (reply.user ? reply.user.username : "游客") + ": </span>" + reply.content + "</div>";
				replysHtml += replyHtml;
			}
			if (replys.length > 2) {
				replysHtml += ("<p><a href='javascript:void(0);' onclick='expandReply(\"" + comment.uuid
						+ "\");' class='button button-light'>查看全部回复<span class='icon icon-down'></span></a></p>");
			}
			replysHtml += "</div></div>";
			elementHtml += replysHtml;
		}
		var thmubHtml = "<img id='thumb-img-" + comment.uuid + "' src='./images/" + (isExist(thumbCommentUuids, comment.uuid) ? "thumb_on.png" : "thumb_off.png") + "' style='width:16px;height:16px;'>";
		elementHtml += ("<div class='card-footer'>"
			+ "<a href='javascript:void(0);' onclick='thumbComment(\"" + comment.uuid + "\");' class='link' style='color:#5f646e;'>" + thmubHtml
				+"(<span id='thumb-" + comment.uuid + "'>" + comment.thumb + "</span>)</a>"
			+ "<a href='javascript:void(0);' onclick='showReplyDialog(\"" + comment.uuid + "\");' class='link' style='color:#5f646e;'><img src='./images/comment.png' style='width:16px;height:16px;'>"
				+ "(<span id='reply-count-" + comment.uuid + "'>" + comment.replyCount + "</span>)</a>"
			+ "</div></div>");
		listHtml += elementHtml;
	}
	$("#comment-list").append($(listHtml));
}

function getComment(commentUuid) {
	for (var i = 0; i< allCommentList.length; i++) {
		var comment = allCommentList[i];
		if (comment.uuid == commentUuid) {
			return comment;
		}
	}
}

function expandReply(commentUuid) {
	var replys = getComment(commentUuid).replys;
	$("#reply-" + commentUuid).empty();
	var replysHtml = "";
	for (var i = 0; i < replys.length; i++) {
		var reply = replys[i];
		var replyHtml = "<div><span style='color:#0894ec'>" + (reply.user ? reply.user.username : "游客") + ": </span>" + reply.content + "</div>";
		replysHtml += replyHtml;
	}
	replysHtml += ("<p><a href='javascript:void(0);' onclick='collapseReply(\"" + commentUuid
			+ "\");' class='button button-light'>收起回复<span class='icon icon-up'></span></a></p>");
	$("#reply-" + commentUuid).html(replysHtml);
}

function collapseReply(commentUuid) {
	var replys = getComment(commentUuid).replys;
	$("#reply-" + commentUuid).empty();
	var replysHtml = "";
	for (var i = 0; i < 2 && i < replys.length; i++) {
		var reply = replys[i];
		var replyHtml = "<div><span style='color:#0894ec'>" + (reply.user ? reply.user.username : "游客") + ": </span>" + reply.content + "</div>";
		replysHtml += replyHtml;
	}
	replysHtml += ("<p><a href='javascript:void(0);' onclick='expandReply(\"" + commentUuid
			+ "\");' class='button button-light'>查看全部回复<span class='icon icon-down'></span></a></p>");
	$("#reply-" + commentUuid).html(replysHtml);
}

function thumbComment(commentUuid) {
	post("/house/comment/thumb", {
		commentUuid : commentUuid
	}, function(result) {
		Zepto.toast(result.msg);
		if (result.code == "0") {
			var originThumb = parseInt($("#thumb-" + commentUuid).text());
			var flag = parseInt(result.data);
			$("#thumb-" + commentUuid).text(originThumb + flag);
			if (flag == 1) {
				$("#thumb-img-" + commentUuid).attr("src", "./images/thumb_on.png")
			} else {
				$("#thumb-img-" + commentUuid).attr("src", "./images/thumb_off.png")
			}
		}
	});
}

function showReplyDialog(commentUuid) {
	curReplyCommentUuid = commentUuid;
	$("#reply-dialog").fadeIn(200);
}

function hideReplyDialog() {
	$("#reply-dialog").fadeOut(200);
}

function replyComment() {
	var params = {};
	params.replyUuid = curReplyCommentUuid;
	var content = $("#reply-content").val();
	if (!content) {
		Zepto.toast("请输入回复内容！");
		return;
	}
	params.content = content;
	var comment = getComment(curReplyCommentUuid);
	params["house.uuid"] = houseUuid;
	params.level = comment.level + 1;
	disableSubmit();
	post("/house/comment/save", params, function(result) {
		Zepto.toast(result.msg);
		enableSubmit();
		if (result.code == "0") {
			comment.replyCount++;
			comment.replys.unshift(result.data);
			$("#reply-count-" + curReplyCommentUuid).text(comment.replyCount);
			hideReplyDialog();
			collapseReply(curReplyCommentUuid); // 重新显示评论的回复
		}
	});
}
