var curPage = 1;
var ROW = 10;

$(function() {
	initCommonNav();
	$("#page-nav a:eq(2)").addClass("active");
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
	get("/house/reservation/houseList", {
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
			var houseList = result.data;
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
			+ "<a href='javascript:goToReservationListHouse(\"" + house.uuid + "\")' class='item-link item-content'>"
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

function goToReservationListHouse(houseUuid) {
	var url = "/house_reservation_list_house.html?houseUuid=" + houseUuid;
	redirect(url);
}
