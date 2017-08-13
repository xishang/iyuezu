$(function() {
	$("input").focus(function() {
		$("#tips").hide();
	});
	enableSubmit();
	var account = $.cookie("account");
	var password = $.cookie("password");
	if (account) {
		$("#account").val(account);
	}
	if (password) {
		$("#password").val(password);
	}
});

function enableSubmit() {
	$("#submit-login").unbind("click").bind("click", function() {
		login();
	});
}

function disableSubmit() {
	$("#submit-login").unbind("click").bind("click", function() {
		$("#tips").text("正在登录中，请稍候...").show();
	});
}

function login() {
	var account = $("#account").val();
	if (!account) {
		$("#tips").text("账号不能为空").show();
		return;
	}
	var password = $("#password").val();
	if (!password) {
		$("#tips").text("密码不能为空").show();
		return;
	}
	disableSubmit();
	post("/user/login", {
		account : account,
		password : password
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

function forgetPassword() {

}

function register() {
	redirect("/register.html");
}

