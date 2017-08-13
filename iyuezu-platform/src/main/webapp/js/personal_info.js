$(function() {
	getPersonalInfo();
});

function getPersonalInfo() {
	ajax("/user/own", "GET", null, function(result) {
		initCommonNav();
		$("#page-nav a:eq(4)").addClass("active");
		if (result.code == "0") {
			var user = result.data;
			$("#personal-name").text(user.username);
			$("#name").text(user.name);
			$("#phone").text(user.phone);
			$("#email").text(user.email);
			$("#identity").text(user.identity);
			if (user.head) {
				$("#personal-head").attr("src", getHost() + user.head);
			}
		} else {
			Zepto.toast("获取个人信息失败");
		}
	}, false);
}

function editPersonalInfo() {
	redirect("/personal_edit.html");
}



