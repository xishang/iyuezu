var curPage = 1;
var ROW = 10;

$(function() {
	initCommonNav();
	$("#page-nav a:eq(2)").addClass("active");
	$("#load-more").click(function() {
		curPage++;
		getRentList();
	});
	getRentList();
});

function getRentList() {
	// 获取进行中的预约信息
	$("#load-more").hide();
	$("#loading-icon").show();
	get("/rent/reservation/rentList", {
		"page" : curPage,
		"row" : ROW
	}, function(result) {
		$("#loading-icon").hide();
		if (result.code == "0") {
			if (result.data.length < ROW) {
				$("#no-more-label").show();
			} else {
				$("#load-more").show();
			}
			var reservationList = result.data;
			fillReservationList(reservationList);
		} else {
			Zepto.toast(result.msg);
		}
	});
}

function fillHouseList(list) {
	var listHtml = "";
	for (var i = 0; i < list.length; i++) {
		var rent = list[i];
		var liHtml = "<li>"
			+ "<a href='javascript:goToReservationListRent(\"" + rent.uuid + "\")' class='item-link item-content'>"
			+ "<div class='item-inner'>"
			+ "<div class='item-title-row'>"
			+ "<div class='item-title'>" + rent.title + "</div>"
			+ "</div>"
			+ "<div class='item-subtitle'>" + rent.district.name + "+" + getHouseType(rent.type) + "+" + rent.price + "元/月</div>"
			+ "</div>"
			+ "</a>"
			+ "</li>";
		listHtml += liHtml;
	}
	$("#rent-list").append($(listHtml));
}

function goToReservationListRent(rentUuid) {
	var url = "/rent_reservation_list_rent.html?rentUuid=" + rentUuid;
	redirect(url);
}
