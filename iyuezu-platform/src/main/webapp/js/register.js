$(function() {
	$("input").focus(function() {
		$("#tips").hide();
	});
	enableSubmit();
});

function enableSubmit() {
	$("#submit-register").unbind("click").bind("click", function() {
		register();
	});
}

function disableSubmit() {
	$("#submit-register").unbind("click").bind("click", function() {
		$("#tips").text("正在注册中，请稍候...").show();
	});
}

function register() {
	var account = $("#account").val();
	if (!account) {
		$("#tips").text("账号不能为空").show();
		return;
	} else if (!checkAccount(account)) {
		$("#tips").text("账号格式错误，请输入3-25位非中文字符").show();
		return;
	}
	var username = $("#username").val();
	if (!username) {
		$("#tips").text("昵称不能为空").show();
		return;
	}
	var password = $("#password").val();
	if (!password) {
		$("#tips").text("密码不能为空").show();
		return;
	} else if (!checkPassword(password)) {
		$("#tips").text("密码格式错误，请输入3-25位非中文字符").show();
		return;
	}
	var repeatPassword = $("#repeat-password").val();
	if (!repeatPassword) {
		$("#tips").text("请输入确认密码").show();
		return;
	} else if (!checkPassword(repeatPassword)) {
		$("#tips").text("确认密码格式错误，请输入3-25位非中文字符").show();
		return;
	} else if (password != repeatPassword) {
		$("#password").val("");
		$("#repeat-password").val("");
		$("#tips").text("两次密码输入不一致，请重新输入").show();
		return;
	}
	var phone = $("#phone").val();
	if (!phone) {
		$("#tips").text("手机号不能为空").show();
		return;
	} else if (!checkPhone(phone)) {
		$("#tips").text("手机号格式错误，请输入正确的手机号").show();
		return;
	}
	var type = $("#type").val();
	if (!type) {
		$("#tips").text("请选择用户类型").show();
		return;
	}
	disableSubmit();
	post("/user/register", {
		account : account,
		username : username,
		password : password,
		phone : phone,
		type : type
	}, function(result) {
		if (result.code == "0") {
			var user = result.data;
			$.cookie("access_token", user.token);
			$.cookie("account", account);
			$.cookie("password", password);
			setExpiresCookie("admin", user.type, 10 * 60 * 1000);
			setTimeout(function() {
				redirect("/house_list.html");
			}, 1000);
		} else {
			$("#tips").text(result.msg).show();
			enableSubmit();
		}
	});
}