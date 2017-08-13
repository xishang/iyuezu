var rentUuid = getUrlParams().rentUuid;

var curHouseUuid;
var curPage = 1;
var ROW = 10;

$(function() {
	initCommonNav();
	enableSubmit();
	limitSize($("#reservation-content"), $("#reservation-detail-count"), 500);
	$("#load-more").click(function() {
		curPage++;
		getHouseList();
	});
	getHouseList();
});

function getHouseList() {
	// 获取进行中的预约信息
	$("#load-more").hide();
	$("#loading-icon").show();
	get("/house/ownList", {
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
			var houseList = result.data.list;
			fillHouseList(houseList);
		} else {
			Zepto.toast(result.msg);
		}
	});
}

function fillHouseList(list) {
	var listHtml = "";
	for (var i = 0; i < list.length; i++) {
		var house = list[i];
		var liHtml = "<li>"
			+ "<a href='javascript:showReservationDialog(\"" + house.uuid + "\", \"" + house.title + "\")' class='item-link item-content'>"
			+ "<div class='item-media'>"
			+ "<img src='" + getHost() + (house.pictures ? house.pictures.split(",")[0] : "/images/yue_head.jpg") + "' width='44' height='44' alt=''>"
			+ "</div>"
			+ "<div class='item-inner'>"
			+ "<div class='item-title-row'>"
			+ "<div class='item-title'>" + house.title + "</div>"
			+ "</div>"
			+ "<div class='item-subtitle'>" + house.district.name + "+" + getHouseType(house.type) + "+" + house.price + "元/月</div>"
			+ "</div>"
			+ "</a>"
			+ "</li>";
		listHtml += liHtml;
	}
	$("#house-list").append($(listHtml));
}

function enableSubmit() {
	$("#submit-reservation").unbind("click").bind("click", function() {
		createReservation();
	});
}

function disableSubmit() {
	$("#submit-reservation").unbind("click").bind("click", function() {
		Zepto.toast("你的提交请求正在处理中，请稍候...");
	});
}

function showReservationDialog(houseUuid, houseTitle) {
	curHouseUuid = houseUuid;
	$("#select-house").text(houseTitle);
	$("#reservation-dialog").fadeIn(200);
}

function hideReservationDialog() {
	$("#reservation-dialog").fadeOut(200);
}

function createReservation() {
	disableSubmit();
	post("/rent/reservation/save", {
		"rentInfo.uuid" : rentUuid,
		"house.uuid" : curHouseUuid,
		"remark" : $("#reservation-content").val()
	}, function(result) {
		Zepto.toast(result.msg);
		enableSubmit();
		if (result.code == "0") {
			hideReservationDialog();
			setTimeout(function() {
				window.history.go(-1);
			}, 1500);
		}
	});
}
