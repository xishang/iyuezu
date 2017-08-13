$(function() {
	initCommonNav();
	$("#page-nav a:eq(2)").attr("href", "").addClass("active");
	$("#create-house").click(function() {
		$("#dialog-title").text("只有房东才能发布房源，现在登录？");
		$("#dialog").fadeIn(200);
	});
	$("#create-rent").click(function() {
		$("#dialog-title").text("只有租客才能发布求租信息，现在登录？");
		$("#dialog").fadeIn(200);
	});
});

function toLogin() {
	var url = "/login.html";
	redirect(url);
}

function hideDialog() {
	$("#dialog").fadeOut(200);
}