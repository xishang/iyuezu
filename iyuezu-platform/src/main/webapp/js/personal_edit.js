var userUuid;
var headImage;

//允许上传的图片类型
var allowTypes = [ "image/jpg", "image/jpeg", "image/png", "image/gif" ];
// 图片最大内存
var maxSize = 1024 * 1024 * 5;
// 图片最大宽度
var maxWidth = 300;

$(function() {
	enableSubmit();
	getPersonalInfo();
	initUpload();
	$("#phone").blur(function() {
		if ($(this).val() && !checkPhone($(this).val())) {
			Zepto.toast("手机号格式错误，请输入正确的手机号");
		}
	});
	$("#email").blur(function() {
		if ($(this).val() && !checkEmail($(this).val())) {
			Zepto.toast("邮箱格式错误，请输入正确的邮箱地址");
		}
	});
	$("#identity").blur(function() {
		if ($(this).val() && !checkIdentity($(this).val())) {
			Zepto.toast("身份证号格式错误，请输入正确的身份证号");
		}
	});
	$("#head-image").click(function() {
		$("#gallery-image").attr("style", this.getAttribute("style"));
	    $("#gallery").fadeIn(200);
	});
	$("#gallery").click(function(){
    	$("#gallery").fadeOut(200);
    });
});

function enableSubmit() {
	$("#submit-personal").unbind("click").bind("click", function() {
		savePersonalInfo();
	});
}

function disableSubmit() {
	$("#submit-personal").unbind("click").bind("click", function() {
		Zepto.toast("你的提交请求正在处理中，请稍候...");
	});
}

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
				$("#head-image").css("background-image", "url(" + base64 + ")");
				$("#head-image").addClass("weui-uploader__file_status").html("<div class='weui-uploader__file-content'>上传中...</div>");
				post("/uploadBase64", {
					base64 : base64
				}, function(result) {
					Zepto.toast(result.msg);
					if (result.code == "0") {
						headImage = result.data;
						$("#head-image").removeClass("weui-uploader__file_status").html("");
					} else {
						$("#head-image").html("<div class='weui-uploader__file-content'><i class='weui-icon-warn'></i></div>");
					}
				});
			};
			img.src = e.target.result;
		};
		reader.readAsDataURL(file);
	});
}

function showCancelDialog() {
	$('#cancel-dialog').fadeIn(200);
}

function hideCancelDialog() {
	$('#cancel-dialog').fadeOut(200);
}

function getPersonalInfo() {
	ajax("/user/own", "GET", null, function(result) {
		initCommonNav();
		$("#page-nav a:eq(4)").addClass("active");
		if (result.code == "0") {
			var user = result.data;
			$("#username").val(user.username);
			$("#name").val(user.name);
			$("#phone").val(user.phone);
			$("#email").val(user.email);
			$("#identity").val(user.identity);
			$("#head-image").css("background-image", "url(" + getHost() + user.head + ")");
			userUuid = user.uuid;
			headImage = user.head;
		} else {
			Zepto.toast("获取个人信息失败");
		}
	}, false);
}

function savePersonalInfo() {
	// 不强制要求输入所有内容
	var username = $("#username").val();
	if (!username) {
//		Zepto.toast("昵称为空，请输入昵称");
//		return;
	}
	var name = $("#name").val();
	if (!name) {
//		Zepto.toast("姓名为空，请输入姓名");
//		return;
	}
	var phone = $("#phone").val();
	if (!phone) {
//		Zepto.toast("手机号为空，请输入手机号");
//		return;
	} else if (!checkPhone(phone)) {
		Zepto.toast("手机号格式错误，请输入正确的手机号");
		return;
	}
	var email = $("#email").val();
	if (!email) {
//		Zepto.toast("邮箱地址为空，请输入邮箱地址");
//		return;
	} else if (!checkEmail(email)) {
		Zepto.toast("邮箱格式错误，请输入正确的邮箱地址");
		return;
	}
	var identity = $("#identity").val();
	if (!identity) {
//		Zepto.toast("身份证号为空，请输入身份证号");
//		return;
	} else if (!checkIdentity(identity)) {
		Zepto.toast("身份证号格式错误，请输入正确的身份证号");
		return;
	}
	disableSubmit();
	post("/user/edit", {
		"uuid" : userUuid,
		"username" : username,
		"name" : name,
		"phone" : phone,
		"email" : email,
		"identity" : identity,
		"head" : headImage
	}, function(result) {
		Zepto.toast(result.msg);
		if (result.code == "0") {
			setTimeout(function() {
				var url = "/personal_info.html";
				redirect(url);
			}, 1000);
		} else {
			enableSubmit();
		}
	});
}