var houseUuid = getUrlParams().houseUuid;

$(function() {
	initCommonNav();
	$('#input-comp').on('rating.change', function(event, value, caption) {
		$("#comp-score").text(value);
	});
	$('#input-auth').on('rating.change', function(event, value, caption) {
		$("#auth-score").text(value);
	});
	$('#input-satis').on('rating.change', function(event, value, caption) {
		$("#satis-score").text(value);
	});
	$('#input-serv').on('rating.change', function(event, value, caption) {
		$("#serv-score").text(value);
	});
	$(".rating").css("ime-mode", "disabled");
	limitSize($("#detail"), $("#detail-count"), 500);
	enableSubmit();
});

function enableSubmit() {
	$("#submit-comment").unbind("click").bind("click", function() {
		saveComment();
	});
}

function disableSubmit() {
	$("#submit-comment").unbind("click").bind("click", function() {
		Zepto.toast("你的提交请求正在处理中，请稍候...");
	});
}

function showCancelDialog() {
	$('#cancel-dialog').fadeIn(200);
}

function hideCancelDialog() {
	$('#cancel-dialog').fadeOut(200);
}

function saveComment() {
	var compScore = $("#comp-score").text();
	if (!compScore) {
		Zepto.toast("请输入房源综合评分");
		return;
	}
	var authScore = $("#auth-score").text();
	if (!authScore) {
		Zepto.toast("请输入房源真实度评分");
		return;
	}
	var satisScore = $("#satis-score").text();
	if (!satisScore) {
		Zepto.toast("请输入房源满意度评分");
		return;
	}
	var servScore = $("#serv-score").text();
	if (!servScore) {
		Zepto.toast("请输入服务满意度评分");
		return;
	}
	var content = $("#detail").val();
	if (!content) {
		Zepto.toast("请输入评论详情");
		return;
	}
	disableSubmit();
	post("/house/comment/save", {
		"house.uuid" : houseUuid,
		"level" : 1,
		"content" : content,
		"compScore" : compScore,
		"authScore" : authScore,
		"satisScore" : satisScore,
		"servScore" : servScore
	}, function(result) {
		Zepto.toast(result.msg);
		if (result.code == "0") {
			setTimeout(function() {
				window.history.go(-1);
			}, 1000);
		} else {
			enableSubmit();
		}
	});
}

