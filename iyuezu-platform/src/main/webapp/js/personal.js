$(function() {
	getPersonalInfo();
});

function initPage() {
	initCommonNav();
	$("#page-nav a:eq(4)").attr("href", "").addClass("active");
	enableSubmit();
}

function enableSubmit() {
	$("#submit-password").unbind("click").bind("click", function() {
		changePassword();
	});
}

function disableSubmit() {
	$("#submit-password").unbind("click").bind("click", function() {
		Zepto.toast("你的提交请求正在处理中，请稍候...");
	});
}

function goToPersonalInfo() {
	var url = "/personal_info.html";
	redirect(url);
}

function getPersonalInfo() {
	ajax("/user/own", "GET", null, function(result) {
		if (result.code == "0") {
			var user = result.data;
			setExpiresCookie("admin", user.type, 10 * 60 * 1000);
			initPage();
			fillPersonalInfo(user);
		} else {
			setExpiresCookie("admin", "-1", 10 * 60 * 1000);
			initPage();
			$("#login-module").show();
			$("#search-bar").show();
			initSearchBar();
		}
	}, false);
}

function fillPersonalInfo(user) {
	$("#personal-name").text(user.username).show();
	if (user.head) {
		$("#personal-head").attr("src", getHost() + user.head);
	}
	$("#personal-head").click(function() {
		goToPersonalInfo();
	});
	$("#function-module").show();
	$("#change-password").click(function() {
		$(".need-check").val("");
		$('#password-dialog').fadeIn(200);
	});
	$(".need-check").blur(function() {
		if ($(this).val() && !checkPassword($(this).val())) {
			Zepto.toast("输入不合法，请输入3-25位数字，英文字母或 \"-\" \"_\"");
		}
	});
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
		var phone = $("#search-input").val();
		if (!phone) {
			Zepto.toast("请先输入手机号码");
			return;
		} else if (!checkPhone(phone)) {
			Zepto.toast("手机号格式错误，请输入正确的手机号");
			return;
		}
		var url = "/house_reservation_list_search.html?phone=" + phone;
		redirect(url);
	});
}

function changePassword() {
	var password = $("#password").val();
	if (!password) {
		Zepto.toast("原密码为空，请重新输入");
		return;
	} else if (!checkPassword(password)) {
		Zepto.toast("原密码格式错误，请输入3-25位数字，英文字母或 \"-\" \"_\"");
		return;
	}
	var newPassword = $("#new-password").val();
	if (!newPassword) {
		Zepto.toast("新密码为空，请重新输入");
		return;
	} else if (!checkPassword(newPassword)) {
		Zepto.toast("新密码格式错误，请输入3-25位数字，英文字母或 \"-\" \"_\"");
		return;
	}
	var repeatPassword = $("#repeat-password").val();
	if (!repeatPassword) {
		Zepto.toast("确认密码为空，请重新输入");
		return;
	} else if (!checkPassword(repeatPassword)) {
		Zepto.toast("确认密码格式错误，请输入3-25位数字，英文字母或 \"-\" \"_\"");
		return;
	} else if (newPassword != repeatPassword) {
		Zepto.toast("确认密码与新密码不一致，请重新输入");
		return;
	}
	disableSubmit();
	post("/user/changePassword", {
		"password" : password,
		"newPassword" : newPassword
	}, function(result) {
		Zepto.toast(result.msg);
		enableSubmit();
		if (result.code == "0") {
			hideDialog();
		} else {
			$(".need-check").val("");
		}
	});
}

function hidePasswordDialog() {
	$('#password-dialog').fadeOut(200);
}

function showLogoutDialog() {
	$('#logout-dialog').fadeIn(200);
}

function hideLogoutDialog() {
	$('#logout-dialog').fadeOut(200);
}

function logout() {
	post("/user/logout", null, function(result) {
		if (result.code == "0") {
			redirect("/house_list.html");
			setExpiresCookie("admin", "-1", 10 * 60 * 1000);
		} else {
			Zepto.toast(result.msg);
		}
	});
}