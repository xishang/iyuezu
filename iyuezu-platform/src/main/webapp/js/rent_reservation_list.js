var admin = getAdmin();
var allReservationList;
var curReservationUuid;
var curUpdateStatus;
var curPage = 1;
var ROW = 10;

$(function() {
	initCommonNav();
	if (admin == 1) {
		$("#reservation-statistics").click(function() {
			goToReservationRentList();
		});
	} else if (admin == 2) {
		$("#reservation-statistics").click(function() {
			goToReservationHouseList();
		});
	}
	allReservationList = [];
	enableSubmit();
	limitSize($("#remark-content"), $("#detail-count"), 300);
	$("#page-nav a:eq(2)").addClass("active");
	$("#load-more").click(function() {
		curPage++;
		getReservations();
	});
	getReservations();
});

function enableSubmit() {
	$("#submit-status").unbind("click").bind("click", function() {
		updateStatus();
	});
	$("#submit-status-remark").unbind("click").bind("click", function() {
		updateStatusWithRemark();
	});
}

function disableSubmit() {
	$("#submit-status, #submit-status-remark").unbind("click").bind("click", function() {
		Zepto.toast("你的提交请求正在处理中，请稍候...");
	});
}

function goToReservationHouseList() {
	var url = "/rent_reservation_house_list.html";
	redirect(url);
}

function goToReservationRentList() {
	var url = "/rent_reservation_rent_list.html";
	redirect(url);
}

function getReservations() {
	// 获取进行中的预约信息
	$("#load-more").hide();
	$("#loading-icon").show();
	get("/rent/reservation/ownList", {
		"page" : curPage,
		"row" : ROW
	}, function(result) {
		$("#loading-icon").hide();
		if (result.code == "0") {
			var pageCount = result.data.pages;
			if (curPage == pageCount || pageCount == 0) {
				$("#no-more-label").show();
			} else {
				$("#load-more").show();
			}
			var reservationList = result.data.list;
			fillReservationList(reservationList);
		} else {
			Zepto.toast(result.msg);
		}
	});
}

function fillReservationList(reservationList) {
	allReservationList = allReservationList.concat(reservationList);
	var listHtml = "";
	for (var i = 0; i < reservationList.length; i++) {
		var reservation = reservationList[i];
		listHtml += getReservationHtml(reservation);
	}
	$("#reservation-list").append($(listHtml));
}

function goToHouseDetail(houseUuid) {
	var url = "/house_detail.html?uuid=" + houseUuid;
	redirect(url);
}

function goToRentDetail(rentUuid) {
	var url = "/rent_detail.html?uuid=" + rentUuid;
	redirect(url);
}

function cancelReservation(reservationUuid) {
	curReservationUuid = reservationUuid;
	curUpdateStatus = 5;
	$("#remark-title").text("确定取消该求租信息的预约吗？");
	$("#remark-dialog").fadeIn(200);
}

function confirmReservation(reservationUuid) {
	curReservationUuid = reservationUuid;
	curUpdateStatus = 4;
	$("#confirm-title").text("如已与房东确定好看房相关事宜，请确认信息！");
	$("#confirm-dialog").fadeIn(200);
}

function refuseReservation(reservationUuid) {
	curReservationUuid = reservationUuid;
	curUpdateStatus = 3;
	$("#remark-title").text("确定拒绝该预约请求吗？");
	$("#remark-dialog").fadeIn(200);
}

function acceptReservation(reservationUuid) {
	curReservationUuid = reservationUuid;
	curUpdateStatus = 2;
	$("#confirm-title").text("确定接受该预约后，请与房东确认看房相关事宜！");
	$("#confirm-dialog").fadeIn(200);
}

function hideConfirmDialog() {
	$("#confirm-dialog").fadeOut(200);
}

function updateStatus() {
	updateReservationStatus(curUpdateStatus, null);
}

function hideRemarkDialog() {
	$("#remark-dialog").fadeOut(200);
}

function updateStatusWithRemark() {
	var remark = $("#remark-content").val();
	if (!remark) {
		Zepto.toast("请输入备注信息");
		return;
	}
	updateReservationStatus(curUpdateStatus, remark);
}

function updateReservationStatus(status, remark) {
	disableSubmit();
	post("/rent/reservation/updateStatus", {
		"reservationUuid" : curReservationUuid,
		"status" : status,
		"remark" : remark
	}, function(result) {
		Zepto.toast(result.msg);
		enableSubmit();
		if (result.code == "0") {
			hideConfirmDialog();
			hideRemarkDialog();
			refreshReservationItem(curReservationUuid, status);
		}
	});
}

function refreshReservationItem(reservationUuid, status) {
	var reservation = getReservationByUuid(reservationUuid);
	if (!reservation) {
		return;
	}
	reservation.status = status;
	$("#reservation-" + reservation.uuid).replaceWith(getReservationHtml(reservation)); // 定点更新状态
}

function getReservationByUuid(reservationUuid) {
	for (var i = 0; i < allReservationList.length; i++) {
		if (allReservationList[i].uuid == reservationUuid) {
			return allReservationList[i];
		}
	}
}

function getReservationHtml(reservation) {
	var house = reservation.house;
	var rentInfo = reservation.rentInfo;
	var houseRooms = (rentInfo.room ? (rentInfo.room + "室") : "") + (rentInfo.hall ? (rentInfo.hall + "厅") : "")
		+ (rentInfo.kitchen ? (rentInfo.kitchen + "厨") : "") + (rentInfo.defend ? (rentInfo.defend + "卫") : "");
	var reservationHtml = "<div class='card my-facebook-card' id='reservation-" + reservation.uuid + "'>"
		+ "<div class='card-header' onclick='goToHouseDetail(\"" + house.uuid + "\");'>"
		+ "<div class='facebook-avatar'>"
		+ "<img src='" + getHost() + (house.pictures ? house.pictures.split(",")[0] : "/images/yue_head.jpg") + "' width='34' height='34' alt=''>"
		+ "</div>"
		+ "<div class='facebook-name'>房源信息：" + house.district.name + "+" + getHouseType(house.type) + "+" + house.price + "元/月</div>"
		+ "<div class='facebook-date' style='position: absolute;right:4%;top:12%;color:red'>" + getStatusText(reservation.status) + "</div>"
		+ "<div class='facebook-date'>联系方式：" + reservation.contactPhone + "</div>"
		+ "<div class='facebook-date'>" + formatFullTime(reservation.createTime) + "</div>"
		+ "</div>"
		
		+ "<div class='card-content'>"
		+ "<div class='list-block media-list'>"
		+ "<ul>"
		+ "<li class='item-content'>"
		+ "<div class='item-inner'>"
		+ "<div class='item-title-row'>"
		+ "<div class='item-title' onclick='goToRentDetail(\"" + rentInfo.uuid + "\");'>" + rentInfo.district.name + " " + getHouseType(rentInfo.type) + "</div>"
		+ "</div>"
		+ "<div class='item-subtitle'>" + houseRooms + "|" + rentInfo.size + "平米</div>"
		+ "<div class='item-subtitle' style='position: absolute;right:2%;top:50%;'><span style='color:red;'>" + rentInfo.minPrice + "-" + rentInfo.maxPrice + "</span>元/月</div>"
		+ "</div>"
		+ "</li>"
		+ "</ul>"
		+ "</div>"
		+ "</div>"
		
		+ "<div class='card-content'>"
		+ "<div class='card-content-inner' style='text-indent:36px;'>备注信息：" + reservation.remark + "</div>"
		+ "</div>"
		
		+ getOperatorHtml(reservation.status, reservation.uuid)
		+ "</div>";
	return reservationHtml;
}

/*---- v2.1版[无租房功能] ----*/
function getOperatorHtml(status, reservationUuid) {
	if (status != 1 && status != 2) {
		return "";
	}
	var operatorHtml = "<div class='card-footer'>";
	if (admin == 1) {
		if (status == 1) {
			operatorHtml += ("<a href='javascript:void(0);' onclick='refuseReservation(\"" + reservationUuid + "\");' class='link'>拒绝预约</a>");
			operatorHtml += ("<a href='javascript:void(0);' onclick='acceptReservation(\"" + reservationUuid + "\");' class='link'>接受预约</a>");
		} else {
			operatorHtml += ("<a href='javascript:void(0);'></a>");
			operatorHtml += ("<a href='javascript:void(0);' onclick='confirmReservation(\"" + reservationUuid + "\");' class='link'>确认信息</a>");
		}
	} else if (admin == 2) {
		operatorHtml += ("<a href='javascript:void(0);'></a>");
		operatorHtml += ("<a href='javascript:void(0);' onclick='cancelReservation(\"" + reservationUuid + "\");' class='link'>取消预约</a>");
	}
	operatorHtml += "</div>";
	return operatorHtml;
}

function getStatusText(status) {
	// [1:已申请, 2:已接受, 3.已拒绝, 4:已确认, 5:已取消]
	switch(status) {
	case 1:
		return "已申请";
	case 2:
		return "已接受";
	case 3:
		return "已拒绝";
	case 4:
		return "已确认";
	case 5:
		return "已取消";
	case 0:
		return "无效预约";
	default:
		return "未知";
	}
}