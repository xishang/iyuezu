/**
 * 格式化时间
 * @param format
 * @returns
 */
Date.prototype.format = function(format) {
	var date = {
		"M+" : this.getMonth() + 1,
		"d+" : this.getDate(),
		"h+" : this.getHours(),
		"m+" : this.getMinutes(),
		"s+" : this.getSeconds(),
		"q+" : Math.floor((this.getMonth() + 3) / 3),
		"S+" : this.getMilliseconds()
	};
	if (/(y+)/i.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
	}
	for ( var k in date) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? date[k]
					: ("00" + date[k]).substr(("" + date[k]).length));
		}
	}
	return format;
}

function getHost() {
	return "http://localhost:8082";
}

function getDomain() {
	return "localhost:8082";
}

function redirect(url) {
	window.location.href = getHost() + url;
}

function newPage(url) {
	window.open(getHost() + url, "_blank");
}

function get(url, data, callback) {
	ajax(url, "GET", data, callback);
}

function post(url, data, callback) {
	ajax(url, "POST", data, callback);
}

function ajax(url, type, data, callback, isAsync) {
	var access_token = $.cookie("access_token");
	if (access_token) {
		if (!data) {
			data = {};
		}
		data.access_token = access_token;
	}
	$.ajax({
		url : getHost() + url,
		type : type,
		data : data,
		async : (isAsync == null ? true : isAsync),
		success : function(result) {
			if (result.code == "9") { // token无效，跳转到登录页
				redirect("/login.html");
			} else {
				callback(result);
			}
		}
	});
}

function logout() {
	get("/user/logout", null, function(result) {
		redirect("/login.html");
	});
}

/**
 * 显示toast信息[要加固定html片段]
 */
function showToast(text) {
	$("#toast-content").text(text);
	$('#toast').show();
	setTimeout(function() {
		$('#toast').hide();
	}, 2000);
}

function getUrlParams() {
	var info = {};
	var url = decodeURI(window.location.href.split("?")[1]);
	var params = url.split("&");
	for ( var i in params) {
		var param = params[i].split("=");
		info[$.trim(param[0])] = $.trim(param[1]);
	}
	return info;
}

function formatDate(ts) {
	var date = new Date(ts);
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	month = month < 10 ? ("0" + month) : month;
	var day = date.getDate();
	day = day < 10 ? ("0" + day) : day;
	return year + "-" + month + "-" + day;
}

function formatTime(ts) {
	var date = new Date(ts);
	var hours = date.getHours();
	var minutes = date.getMinutes();
	return (hours < 10 ? ("0" + hours) : hours) + ":" + (minutes < 10 ? ("0" + minutes) : minutes);
}

function formatPushlishTime(ts) {
	var now = new Date();
	var nowTs = now.getTime();
	var disTs = nowTs - ts;
	if (disTs < 1000 * 60 * 60 * 6) { // 6小时之内显示[Xxx前]
		if (disTs < 1000 * 60 * 60) { // 1小时之内
			var minutes = Math.round(disTs / (1000 * 60));
			if (minutes == 0) {
				return "刚刚";
			} else {
				return minutes + "分钟前";
			}
		} else {
			var hours = Math.round(disTs / (1000 * 60 * 60));
			return hours + "小时前";
		}
	}
	var date = new Date(ts);
	if (now.getFullYear() == date.getFullYear() && now.getMonth() == date.getMonth() && now.getDate() == date.getDate()) {
		return formatTime(ts);
	} else {
		var yesterday = new Date(now.setDate(now.getDate() - 1));
		if (yesterday.getFullYear() == date.getFullYear() && yesterday.getMonth() == date.getMonth() && yesterday.getDate() == date.getDate()) {
			return "昨天";
		}
	}
	return formatDate(ts);
}

function formatFullTime(ts) {
	var date = new Date(ts);
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	var hours = date.getHours();
	hours = hours < 10 ? ("0" + hours) : hours;
	var minutes = date.getMinutes();
	minutes = minutes < 10 ? ("0" + minutes) : minutes;
	return year + "年" + month + "月" + day + "日  " + hours + ":" + minutes;
}

var accountRegex = /^[a-zA-Z]\w{4,19}$/; // 字母开头[5-20位]
var myAccountRegex = /^\w{3,25}$/; // 3-25个非中文字符
var passwordRegex = /^\w{3,25}$/; // 3-25个非中文字符
var phoneRegex = /^1[3,5,8]\d{9}$/;
var emailRegex = /^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+(\.[a-zA-Z]{2,3})+$/;
var identityRegex = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;

function checkAccount(account) {
	return myAccountRegex.exec(account) ? true : false;
}

function checkPassword(password) {
	return passwordRegex.exec(password) ? true : false;
}

function checkPhone(phone) {
	return phoneRegex.exec(phone) ? true : false;
}

function checkEmail(email) {
	return emailRegex.exec(email) ? true : false;
}

function checkIdentity(identity) {
	return identityRegex.exec(identity) ? true : false;
}

function getAdmin() {
	var admin = $.cookie("admin");
	if (admin) {
		return admin;
	}
	ajax("/user/own", "GET", null, function(result) {
		if (result.code == "0") {
			admin = result.data.type;
		} else {
			admin = "-1";
		}
		setExpiresCookie("admin", admin, 10 * 60 * 1000); // 过期时间10分钟
	}, false);
	return admin;
}

// 刷新cookie中的admin
function refreshAdmin() {
	ajax("/user/own", "GET", null, function(result) {
		if (result.code == "0") {
			setExpiresCookie("admin", result.data.type, 10 * 60 * 1000);
		} else {
			setExpiresCookie("admin", "-1", 10 * 60 * 1000);
		}
	}, false);
}

/**
 * 设置cookie及超时时间
 * @param key			cookie名
 * @param value			cookie值
 * @param expireTime	cookie过期时间[毫秒]
 */
function setExpiresCookie(key, value, expireTime) {
	var expireDate = new Date();
	expireDate.setTime(expireDate.getTime() + expireTime);
	$.cookie(key, value, {
		expires : expireDate
	});
}

function initCommonNav() {
	var admin = getAdmin();
	$("#page-nav a:eq(0)").attr("href", "house_list.html");
	$("#page-nav a:eq(0)").find(".icon").addClass("icon-home");
	$("#page-nav a:eq(0)").find(".page-title").text(admin == "2" ? "我的房源" : "房源列表");
	$("#page-nav a:eq(1)").attr("href", "rent_list.html");
	$("#page-nav a:eq(1)").find(".icon").addClass("icon-phone");
	$("#page-nav a:eq(1)").find(".page-title").text(admin == "1" ? "我的求租" : "求租信息");
	$("#page-nav a:eq(3)").attr("href", "chat.html");
	$("#page-nav a:eq(3)").find(".icon").addClass("icon-message");
	$("#page-nav a:eq(3)").find(".page-title").text("聊天信息");
	$("#page-nav a:eq(4)").attr("href", "personal.html");
	$("#page-nav a:eq(4)").find(".icon").addClass("icon-me");
	$("#page-nav a:eq(4)").find(".page-title").text("我的");
	if (admin == "1") {
		$("#page-nav a:eq(2)").attr("href", "house_reservation_list_underway.html");
		$("#page-nav a:eq(2)").find(".icon").addClass("icon-edit");
		$("#page-nav a:eq(2)").find(".page-title").text("预约管理");
	} else if (admin == "2") {
		$("#page-nav a:eq(2)").attr("href", "rent_reservation_list_underway.html");
		$("#page-nav a:eq(2)").find(".icon").addClass("icon-edit");
		$("#page-nav a:eq(2)").find(".page-title").text("预约管理");
	} else {
		$("#page-nav a:eq(2)").attr("href", "publish.html");
		$("#page-nav a:eq(2)").find(".icon").addClass("icon-edit");
		$("#page-nav a:eq(2)").find(".page-title").text("发布");
	}
}

function getHouseType(type) {
	switch (type) {
	case 1:
		return "整租";
	case 2:
		return "单间";
	case 3:
		return "床位";
	default:
		return "";
	}
}

/**
 * @base	需要引入sm.min.js[Zepto.toast]
 * 
 * @param $input	需要限制长度的input或textarea
 * @param $count	显示当前字符数的元素
 * @param maxSize	限制的最大字符数
 */
function limitSize($input, $count, maxSize) {
	$input.bind("input propertychange", function(){
		var content = $(this).val();
		var size = content.length;
		if (size > maxSize) {
			$(this).val(content.substr(0, maxSize));
			$count.text(maxSize);
			Zepto.toast("长度超过限制");
		} else {
			$count.text(size);
		}
	});
}

/**
 * 将评论及其回复按时间倒序排列
 * @param list	评论列表
 */
function sortCommentList(list) {
	list.sort(function(a, b) {
		return b.createTime - a.createTime;
	});
	for (var i = 0; i < list.length; i++) {
		var replys = list[i].replys;
		replys.sort(function(a, b) {
			return b.createTime - a.createTime;
		});
	}
}

/**
 * 获取用户待处理的信息
 */
function refreshNavBar() {
	get("/user/handleInfo", null, function(result) {
		if (result.code == "0") {
			var handleInfo = result.data;
			$("#page-nav a:eq(2)").find(".reddot-badge").remove();
			$("#page-nav a:eq(3)").find(".reddot-badge").remove();
			var reservationCount = admin == 1 ? handleInfo.houseReservationCount : handleInfo.rentReservationCount;
			var totalCount = (handleInfo.houseReservationCount ? handleInfo.houseReservationCount : 0)
				+ (handleInfo.rentReservationCount ? handleInfo.rentReservationCount : 0);
			if (reservationCount) {
				$("#page-nav a:eq(2)").append($("<span class='reddot-badge'></span>"));
			}
			if (totalCount) {
				$("#page-nav a:eq(3)").append($("<span class='reddot-badge'></span>"));
			}
		}
	});
}

function initInterval(admin) {
	setInterval(function() {
		refreshNavBar(admin);
	}, 20 * 1000); // 每20s刷新一次
	refreshNavBar(admin);
}

/**
 * 获取本人所有点赞的评论uuid
 */
function getThumbCommentUuids(callback) {
	ajax("/house/comment/thumb/commentUuidList", "GET", null, function(result) {
		if (result.code == "0") {
			callback(result.data);
		}
	}, false);
}

/**
 * 判断数组array中是否存在数据item
 * @param array	需要判断的简单数组
 * @param item	需要判断是否存在的数据
 */
function isExist(array, item) {
	var isArray = (Object.prototype.toString.call(array) === "[object Array]"); // 判断array是否是数组
	if (!isArray) {
		return false;
	}
	for (var i = 0; i < array.length; i++) {
		if (array[i] == item) {
			return true;
		}
	}
	return false;
}

/**
 * 在数组array中添加item
 */
function insertItem(array, item) {
	if (isExist(array, item)) {
		return;
	} else {
		array.push(item);
	}
}


/**
 * 在数组array中移除item
 */
function removeItem(array, item) {
	var tempArray = [];
	for (var i = 0; i < array.length; i++) {
		if (array[i] != item) {
			tempArray.push(array[i]);
		}
	}
	array = tempArray;
}
