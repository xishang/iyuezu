var rentUuid = getUrlParams().uuid;
var districts;

$(function() {
	initPage();
	bindEvent();
	getDistricts();
	if (rentUuid) {
		getRentInfo();
	} else {
		getPersonalInfo();
	}
});

function getDistricts() {
	ajax("/district/list", "GET", {
		"page" : 1,
		"row" : 1000
	}, function(result) {
		if (result.code == "0") {
			districts = result.data.list;
		} else {
			districts = [];
		}
	}, false);
}

function getDistrictById(id) {
	for (var i = 0; i < districts.length; i++) {
		if (districts[i].id == id) {
			return districts[i];
		}
	}
}

function fillPopupDistrictList() {
	var firstListHtml = "";
	var firstLi = "<li><a class='item-link list-button close-popup' onclick='closePopupByDistrict(1);'>全深圳市</a></li>";
	firstListHtml += firstLi;
	for (var i = 0; i < districts.length; i++) {
		var district = districts[i];
		if (district.level == 2) {
			var liHtml = "<li><a class='item-link list-button' onclick='fillSecondDistrictList(" + district.id + ");'>"
				+ district.name + "</a></li>";
			firstListHtml += liHtml;
		}
	}
	$("#p-first-list").html(firstListHtml);
	var curDistrictId = $("#district").attr("district-id");
	if (curDistrictId) {
		var curDistrict = getDistrictById(curDistrictId);
		if (curDistrict.level == 3) {
			fillSecondDistrictList(curDistrict.parent);
		}
	}
}

function closePopupByDistrict(id) {
	var district = getDistrictById(id);
	$("#district").attr("district-id", id);
	$("#district").text(district.name);
}

function fillSecondDistrictList(id) {
	var secondListHtml = "";
	var parentDistrict = getDistrictById(id);
	secondListHtml += "<li><a class='item-link list-button close-popup' onclick='closePopupByDistrict(" + parentDistrict.id + ");'>全"
		+ parentDistrict.name + "</a></li>";
	for (var i = 0; i < districts.length; i++) {
		var district = districts[i];
		if (district.parent == id) {
			var liHtml = "<li><a class='item-link list-button close-popup' onclick='closePopupByDistrict(" + district.id + ");'>"
				+ district.name + "</a></li>";
			secondListHtml += liHtml;
		}
	}
	$("#p-second-list").html(secondListHtml);
}

function initPage() {
	initCommonNav();
	$("#page-nav a:eq(1)").addClass("active");
	$(".only-number").keydown(function() {
		if (event.keyCode == 13) {
			event.keyCode = 9;
		}
	}).keypress(function() {
		if (event.keyCode < 48 || event.keyCode > 57) {
			event.returnValue = false;
		}
	});
}

function bindEvent() {
	$("#contact-phone").blur(function() {
		if ($(this).val() && !checkPhone($(this).val())) {
			Zepto.toast("联系人手机号格式错误，请输入正确的手机号");
		}
	});
	$("#hide-info").change(function() {
		var isHide = $(this).is(":checked");
		if (isHide) {
			$(".contact-info").hide();
		} else {
			$(".contact-info").show();
		}
	});
	$("#district").click(function() {
		fillPopupDistrictList();
	});
	$("#cancel").click(function() {
		$('#cancel-dialog').fadeIn(200);
	});
	limitSize($("#detail"), $("#detail-count"), 500);
	enableSubmit();
}

function enableSubmit() {
	$("#submit-rent").unbind("click").bind("click", function() {
		saveRentInfo();
	});
}

function disableSubmit() {
	$("#submit-rent").unbind("click").bind("click", function() {
		Zepto.toast("你的提交请求正在处理中，请稍候...");
	});
}

function hideCancelDialog() {
	$('#cancel-dialog').fadeOut(200);
}

function getRentInfo() {
	get("/rent/" + rentUuid, null, function(result) {
		if (result.code == "0") {
			var rentInfo = result.data;
			$("#district").attr("district-id", rentInfo.district ? rentInfo.district.id : "");
			$("#district").text(rentInfo.district ? rentInfo.district.name : "");
			$("#min-price").val(rentInfo.minPrice);
			$("#max-price").val(rentInfo.maxPrice);
			$("#size").val(rentInfo.size ? rentInfo.size : "");
			$("#room").val(rentInfo.room == null ? "" : rentInfo.room);
			$("#hall").val(rentInfo.hall == null ? "" : rentInfo.hall);
			$("#kitchen").val(rentInfo.kitchen == null ? "" : rentInfo.kitchen);
			$("#defend").val(rentInfo.defend == null ? "" : rentInfo.defend);
			var selectHtml = "<option value='1'" + (rentInfo.type == "1" ? " selected" : "") + ">整租</option>"
				+ "<option value='2'" + (rentInfo.type == "2" ? " selected" : "") + ">单间</option>"
				+ "<option value='3'" + (rentInfo.type == "3" ? " selected" : "") + ">床位</option>";
			$("#type").html(selectHtml);
			if (rentInfo.contactName || rentInfo.contactPhone || rentInfo.contactWechat) {
				$("#hide-info").prop("checked", false);
				$("#contact-name").val(rentInfo.contactName ? rentInfo.contactName : "");
				$("#contact-phone").val(rentInfo.contactPhone ? rentInfo.contactPhone : "");
				$("#contact-wechat").val(rentInfo.contactWechat ? rentInfo.contactWechat : "");
			} else {
				$("#hide-info").prop("checked", true);
				$(".contact-info").hide();
			}
			$("#detail").text(rentInfo.detail ? rentInfo.detail : "");
		} else {
			Zepto.toast("获取求租信息失败");
		}
	});
}

function getPersonalInfo() {
	get("/user/own", null, function(result) {
		if (result.code == "0") {
			var user = result.data;
			$("#contact-name").val(user.username);
			$("#contact-phone").val(user.phone);
		} else {
			Zepto.toast("获取个人信息失败");
		}
	});
}

function saveRentInfo() {
	var params = {};
	var districtId = $("#district").attr("district-id");
	if (!districtId) {
		Zepto.toast("请输入希望租房的地区");
		return;
	} else {
		params["district.id"] = districtId;
	}
	var minPrice = parseFloat($("#min-price").val());
	if (!minPrice) {
		Zepto.toast("请输入可以接受的最低租金");
		return;
	}
	var maxPrice = parseFloat($("#max-price").val());
	if (!maxPrice) {
		Zepto.toast("请输入可以接受的最高租金");
		return;
	}
	if (minPrice > maxPrice) {
		Zepto.toast("最低租金不能大于最高租金");
		return;
	}
	params.minPrice = minPrice;
	params.maxPrice = maxPrice;
	var size = $("#size").val();
	if (!size) {
		Zepto.toast("请输入期望租房面积");
		return;
	} else {
		params.size = size;
	}
	var room = $("#room").val();
	var hall = $("#hall").val();
	var kitchen = $("#kitchen").val();
	var defend = $("#defend").val();
	if (room == "" || hall == "" || kitchen == "" || defend == "") {
		Zepto.toast("请输入期望房源的厅室数");
		return;
	} else {
		params.room = room;
		params.hall = hall;
		params.kitchen = kitchen;
		params.defend = defend;
	}
	var type = $("#type").val();
	if (!type) {
		Zepto.toast("请输入期望房源出租方式");
		return;
	} else {
		params.type = type;
	}
	var isHide = $("#hide-info").is(":checked");
	if (isHide) {
		params.contactName = "";
		params.contactPhone = "";
		params.contactWechat = "";
	} else {
		var contactName = $("#contact-name").val();
		if (!contactName) {
			Zepto.toast("请输入联系人姓名");
			return;
		} else {
			params.contactName = contactName;
		}
		var contactPhone = $("#contact-phone").val();
		if (contactPhone && !checkPhone(contactPhone)) {
			Zepto.toast("联系人手机号格式错误，请输入正确的手机号");
			return;
		} else {
			params.contactPhone = contactPhone;
		}
		var contactWechat = $("#contact-wechat").val();
		params.contactWechat = contactWechat;
	}
	var detail = $("#detail").val();
	params.detail = detail; // detail不强制要求
	if (rentUuid) {
		params.uuid = rentUuid;
	}
	disableSubmit();
	post("/rent/save", params, function(result) {
		Zepto.toast(result.msg);
		if (result.code == "0") {
			setTimeout(function() {
				var url = "/rent_detail.html?uuid=" + result.data.uuid;
				redirect(url);
			}, 1000);
		} else {
			enableSubmit();
		}
	});
}