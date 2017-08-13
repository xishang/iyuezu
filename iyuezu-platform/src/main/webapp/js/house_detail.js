var houseUuid = getUrlParams().uuid;
var admin = getAdmin();
var replyUuid;

var visitor = {username : "游客"};

var thumbCommentUuids;

var commentsList;

$(function() {
	initPage();
	getThumbCommentUuids(function (result) {
		thumbCommentUuids = result;
	});
	getHouseInfo();
	refreshCommentList();
});

function getHouseInfo() {
	var url = "/house/" + houseUuid;
	get(url, null, function(result) {
		if (result.code == "0") {
			var house = result.data;
			fillHouseInfo(house);
		}
	});
//	drawEcharts();
}

function initPage() {
	initCommonNav(admin);
	$("#page-nav a:eq(0)").addClass("active");
	if (admin != 2) {
		$("#edit-house").hide(); // 隐藏编辑房源按钮
	}
	if (admin != 1) {
		$("#reservation-btn").hide();
	}
	limitSize($("#reservation-content"), $("#reservation-detail-count"), 300);
	limitSize($("#comment-content"), $("#detail-count"), 300);
	$("#publish-comment").click(function() {
		replyUuid = null;
		$("#dialog-title").text("发表评论");
		$("#comment-content").val("");
		$('#comment-dialog').fadeIn(200);
	});
	enableSubmitComment();
	enableSubmitReservation();
	if (!admin || admin == -1) {
		$("#phone-module").show();
	}
	$("#gallery").click(function(){
    	$("#gallery").fadeOut(200);
    });
}

function enableSubmitComment() {
	$("#submit-comment").unbind("click").bind("click", function() {
		publishComment();
	});
}

function disableSubmitComment() {
	$("#submit-comment").unbind("click").bind("click", function() {
		Zepto.toast("你的提交请求正在处理中，请稍候...");
	});
}

function enableSubmitReservation() {
	$("#submit-reservation").unbind("click").bind("click", function() {
		createReservation();
	});
}

function disableSubmitReservation() {
	$("#submit-reservation").unbind("click").bind("click", function() {
		Zepto.toast("你的提交请求正在处理中，请稍候...");
	});
}

function showReservationDialog() {
	$("#reservation-dialog").fadeIn(200);
}

function hideReservationDialog() {
	$("#reservation-dialog").fadeOut(200);
}

function createReservation() {
	disableSubmitReservation();
	post("/house/reservation/save", {
		"house.uuid" : houseUuid,
		"contactPhone" : $("#reservation-contact-phone").val(),
		"remark" : $("#reservation-content").val()
	}, function(result) {
		Zepto.toast(result.msg);
		enableSubmitReservation();
		if (result.code == "0") {
			hideReservationDialog();
		}
	});
}

function goToHouseComments() {
	var url = "/house_comments.html?houseUuid=" + houseUuid;
	redirect(url);
}

function refreshCommentList() {
	commentsList = [];
	get("/house/comment/list", {
		houseUuid : houseUuid,
		page : 1,
		row : 2
	}, function(result) {
		if (result.code == "0") {
			commentsList = result.data.list;
			sortCommentList(commentsList);
			fillCommentList();
		} else {
			Zepto.toast(result.msg);
		}
	});
}

function fillCommentList() {
	var listHtml = "";
	for (var i = 0; i < commentsList.length; i++) {
		var comment = commentsList[i];
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
	$("#comment-list").html(listHtml);
}

function getComment(commentUuid) {
	for (var i = 0; i< commentsList.length; i++) {
		var comment = commentsList[i];
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
	replyUuid = commentUuid;
	$("#dialog-title").text("回复评论");
	$("#comment-content").val("");
	$('#comment-dialog').fadeIn(200);
}

function hideCommentDialog() {
	$('#comment-dialog').fadeOut(200);
}

function publishComment() {
	var params = {};
	params["house.uuid"] = houseUuid;
	if (replyUuid) {
		params.replyUuid = replyUuid;
		params.level = 2;
	} else {
		params.level = 1;
	}
	var content = $("#comment-content").val();
	if (!content) {
		Zepto.toast("请输入评论内容");
		return;
	}
	params.content = content;
	disableSubmitComment();
	post("/house/comment/save", params, function(result) {
		Zepto.toast(result.msg);
		enableSubmitComment();
		if (result.code == "0") {
			hideCommentDialog();
			refreshCommentList();
		}
	});
}

function fillHouseInfo(house) {
	if (house.pictures) {
		var pictureArr = house.pictures.split(",");
		var count = 0;
		for (var i = 0; i < pictureArr.length; i++) {
			var picture = pictureArr[i];
			var indicator = "<li data-target='#picture-carousel' data-slide-to='" + count + "'" + (count == 0 ? " class='active'" : "") + "></li>";
			$(".carousel-indicators").append($(indicator));
			var carousel = "<div class='item" + (count == 0 ? " active" : "") + "'>"
				+ "<img src='" + getHost() + picture + "' alt='' style='height:100%;width:100%'>"
				+ "</div>";
			$(".carousel-inner").append($(carousel));
			count++;
		}
		$('.carousel').carousel({
			interval : 2000
		});
		$(".carousel-inner .item img").click(function() {
			var image = $(this).attr("src");
			$("#gallery-image").css("background-image", "url(" + image + ")");
		    $("#gallery").fadeIn(200);
		});
	} else {
		$("#picture-carousel").hide();
	}
	var rooms = (house.room ? (house.room + "室") : "") + (house.hall ? (house.hall + "厅") : "") + (house.kitchen ? (house.kitchen + "厨") : "")
		+ (house.defend ? (house.defend + "卫") : "");
	if (house.reservationCount) {
		$("#reservation-count span").text(house.reservationCount);
	} else {
		$("#reservation-count").hide();
	}
	$("#reservation-count").text( )
	$("#title").text(house.title ? house.title : "");
	$("#district").text(house.district ? house.district.name : "");
	$("#community").text(house.community ? house.community : "");
	$("#price").text(house.price ? (house.price + "元/月") : "");
	$("#size").text(house.size ? (house.size + "平米") : "");
	$("#rooms").text(rooms);
	$("#type").text(getHouseType(house.type));
	$("#create-time").text(formatTime(house.createTime));
	$("#detail").text(house.detail ? house.detail : "暂无说明");
	if (house.contactName || house.contactPhone || house.contactWechat) {
		$("#contact-name").text(house.contactName);
		$("#contact-phone").text(house.contactPhone);
		$("#contact-wechat").text(house.contactWechat);
	} else {
		$("#contact").text("该房东设置了隐私保护");
		$("#contact-info").hide();
	}
	$("#comment-count").text(house.commentCount);
}

function editHouse() {
	var url = "/house_edit.html?uuid=" + houseUuid;
	redirect(url);
}


function createOrder() {
	
}

function drawEcharts() {
	option = {
		title : {
			text : '未来一周气温变化',
			subtext : '纯属虚构'
		},
		tooltip : {
			trigger : 'axis'
		},
		legend : {
			data : [ '最高气温', '最低气温' ]
		},
		toolbox : {
			show : true,
			feature : {
				dataZoom : {
					yAxisIndex : 'none'
				},
				dataView : {
					readOnly : false
				},
				magicType : {
					type : [ 'line', 'bar' ]
				},
				restore : {},
				saveAsImage : {}
			}
		},
		xAxis : {
			type : 'category',
			boundaryGap : false,
			data : [ '周一', '周二', '周三', '周四', '周五', '周六', '周日' ]
		},
		yAxis : {
			type : 'value',
			axisLabel : {
				formatter : '{value} °C'
			}
		},
		series : [ {
			name : '最高气温',
			type : 'line',
			data : [ 11, 11, 15, 13, 12, 13, 10 ],
			markPoint : {
				data : [ {
					type : 'max',
					name : '最大值'
				}, {
					type : 'min',
					name : '最小值'
				} ]
			},
			markLine : {
				data : [ {
					type : 'average',
					name : '平均值'
				} ]
			}
		}, {
			name : '最低气温',
			type : 'line',
			data : [ 1, -2, 2, 5, 3, 2, 0 ],
			markPoint : {
				data : [ {
					name : '周最低',
					value : -2,
					xAxis : 1,
					yAxis : -1.5
				} ]
			},
			markLine : {
				data : [ {
					type : 'average',
					name : '平均值'
				}, [ {
					symbol : 'none',
					x : '90%',
					yAxis : 'max'
				}, {
					symbol : 'circle',
					label : {
						normal : {
							position : 'start',
							formatter : '最大值'
						}
					},
					type : 'max',
					name : '最高点'
				} ] ]
			}
		} ]
	};
	debugger;
	var myChart = echarts.init(document.getElementById('main'));
	myChart.setOption(option);
}

