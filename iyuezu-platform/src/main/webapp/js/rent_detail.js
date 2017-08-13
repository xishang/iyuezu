var admin = getAdmin();
var rentUuid = getUrlParams().uuid;

$(function() {
	initPage();
	getRentInfo();
});

function getRentInfo() {
	var url = "/rent/" + rentUuid;
	get(url, null, function(result) {
		if (result.code == "0") {
			var rent = result.data;
			var rooms = (rent.room ? (rent.room + "室") : "") + (rent.hall ? (rent.hall + "厅") : "") + (rent.kitchen ? (rent.kitchen + "厨") : "")
				+ (rent.defend ? (rent.defend + "卫") : "");
			if (rent.reservationCount) {
				$("#reservation-count span").text(rent.reservationCount);
			} else {
				$("#reservation-count").hide();
			}
			$("#district").text(rent.district ? rent.district.name : "");
			$("#price").text(rent.minPrice + "-" + rent.maxPrice + "元/月");
			$("#size").text(rent.size ? (rent.size + "平米") : "");
			$("#rooms").text(rooms);
			$("#type").text(getHouseType(rent.type));
			$("#create-time").text(formatTime(rent.createTime));
			$("#detail").text(rent.detail ? rent.detail : "暂无说明");
			if (rent.contactName || rent.contactPhone || rent.contactWechat) {
				$("#contact-name").text(rent.contactName);
				$("#contact-phone").text(rent.contactPhone);
				$("#contact-wechat").text(rent.contactWechat);
			} else {
				$("#contact").text("该租客设置了隐私保护");
				$("#contact-info").hide();
			}
		}
	});
}

function initPage() {
	initCommonNav();
	$("#page-nav a:eq(1)").addClass("active");
	if (admin != 1) {
		$("#edit-rent").hide(); // 隐藏编辑求租信息按钮
	}
	if (admin == 2) {
		$("#create-reservation").click(function() {
			var url = "/house_list_for_reservation.html?rentUuid=" + rentUuid;
			redirect(url);
		});
	} else {
		$("#reservation-btn").hide();
	}
}

function editRent() {
	var url = "/rent_edit.html?uuid=" + rentUuid;
	redirect(url);
}

function createOrder() {
	
}
