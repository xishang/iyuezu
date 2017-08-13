var houseUuid = getUrlParams().houseUuid;
var allReservationList;
var curReservationUuid;
var curUpdateStatus;
var curPage = 1;
var ROW = 10;

$(function() {
	initCommonNav();
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

function getReservations() {
	$("#load-more").hide();
	$("#loading-icon").show();
	get("/rent/reservation/listByHouse", {
		"houseUuid" : houseUuid,
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

function goToRentDetail(rentUuid) {
	var url = "/rent_detail.html?uuid=" + rentUuid;
	redirect(url);
}

function contactRenter(renterUuid) {
	
}

function cancelReservation(reservationUuid) {
	curReservationUuid = reservationUuid;
	curUpdateStatus = 5;
	$("#remark-title").text("确定取消该求租信息的预约吗？");
	$("#remark-dialog").fadeIn(200);
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
	var rent = reservation.rentInfo;
	var reservationHtml = "<div class='card my-facebook-card' id='reservation-" + reservation.uuid + "'>"
		+ "<div class='card-header' onclick='goToRentDetail(\"" + rent.uuid + "\");'>"
		+ "<div class='facebook-name' style='margin-left:0'>求租信息：" + rent.district.name + " " + getHouseType(rent.type) + "</div>"
		+ "<div class='facebook-date' style='position: absolute;right:4%;top:12%;color:red'>" + getStatusText(reservation.status) + "</div>"
		+ "<div class='facebook-date' style='margin-left:0'>期望租金：" + rent.minPrice + "-" + rent.maxPrice + "元/月</div>"
		+ "<div class='facebook-date' style='margin-left:0'>联系方式：" + reservation.contactPhone + "</div>"
		+ "<div class='facebook-date' style='margin-left:0'>" + formatFullTime(reservation.createTime) + "</div>"
		+ "</div>"
		+ "<div class='card-content'>"
		+ "<div class='card-content-inner'><span style='color:red;'>备注：</span>" + reservation.remark + "</div>"
		+ "</div>"
		+ getOperatorHtml(reservation.status, reservation.uuid)
		+ "</div>";
	return reservationHtml;
}

function getOperatorHtml(status, reservationUuid) {
	if (status != 1 && status != 2) {
		return "";
	}
	var operatorHtml = "<div class='card-footer'>"
		+ "<a href='javascript:void(0);'></a>"
		+ "<a href='javascript:void(0);' onclick='cancelReservation(\"" + reservationUuid + "\");' class='link'>取消预约</a>"
		+ "</div>";
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