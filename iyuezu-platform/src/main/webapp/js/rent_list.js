var admin;
var page;
var districtId;
var price;
var type;
var order;
var isDesc;
var rentListUrl;
var row = 10;

var orderList = [{"order":"update_time", "desc":1, "text":"时间从后往前"},
                 {"order":"update_time", "desc":0, "text":"时间从前到后"},
                 {"order":"min_price", "desc":1, "text":"租金从高到低"},
                 {"order":"min_price", "desc":0, "text":"租金从低到高"}];

$(function() {
	initParams();
	initPage();
	initSelector(districtId, price, type, order, isDesc, orderList, goToFilterPage);
	getRentList();
});

function initParams() {
	var urlParams = getUrlParams();
	admin = getAdmin();
	page = urlParams.page ? parseInt(urlParams.page) : 1;
	districtId = urlParams.districtId;
	price = urlParams.price;
	type = urlParams.type;
	order = urlParams.order;
	isDesc = urlParams.isDesc;
}

function initPage() {
	initCommonNav();
	$("#page-nav a:eq(1)").attr("href", "").addClass("active");
	if (admin == "1") {
		rentListUrl = "/rent/ownList";
		$("#page-title").text("我的求租");
	} else {
		rentListUrl = "/rent/list";
		$("#page-title").text("求租列表");
		$("#create-rent").hide(); // 隐藏创建求租信息按钮
	}
	$("#pre-page").click(function() {
		if ($(this).hasClass("useless")) {
			Zepto.toast("已经是第一页");
		}
	});
	$("#next-page").click(function() {
		if ($(this).hasClass("useless")) {
			Zepto.toast("已经是最后一页");
		}
	});
}

function getRentList() {
	var params = {};
	if (districtId) {
		params.districtId = parseInt(districtId);
	}
	if (price) {
		var priceArr = price.split("-");
		var minPrice = parseFloat(priceArr[0]);
		if (minPrice) {
			params.minPrice = minPrice;
		}
		var maxPrice = parseFloat(priceArr[1]);
		if (maxPrice) {
			params.maxPrice = maxPrice;
		}
	}
	if (type) {
		params.type = type;
	}
	if (order && isDesc != null) {
		params.order = order;
		params.isDesc = parseInt(isDesc);
	}
	params.page = page;
	params.row = row;
	get(rentListUrl, params, function(result) {
		if (result.code == 0) {
			var rentList = result.data.list;
			fillRentList(rentList);
			var pageCount = result.data.pages;
			setPageBtn(pageCount);
		}
	});
}

function fillRentList(rentList) {
	$("#rent-list").empty();
	var listHtml = "";
	for (var i = 0; i < rentList.length; i++) {
		var rent = rentList[i];
		var rooms = (rent.room ? (rent.room + "室") : "") + (rent.hall ? (rent.hall + "厅") : "") + (rent.kitchen ? (rent.kitchen + "厨") : "")
			+ (rent.defend ? (rent.defend + "卫") : "");
		var element = "<div class='card' onclick='goToRentDetail(\"" + rent.uuid + "\");'>"
			+ "<div class='card-header'>" + rent.district.name + " " + getHouseType(rent.type) + "</div>"
			+ "<div class='card-content'>"
			+ "<div class='card-content-inner'>"
			+ "<p>期望厅室：" + rooms + "</p>"
			+ "<p>期望面积：" + rent.size + "平米</p>"
			+ "<span style='position: absolute;right:10;bottom:5'>已有<span style='color: red;'>" + rent.reservationCount + "</span>位房东回应</span>"
			+ "</div>"
			+ "</div>"
			+ "<div class='card-footer'>"
			+ "<a href='javascript:' class='link' style='color: grey'><span style='color: red'>" + rent.minPrice + "-" + rent.maxPrice + "</span>元/月</a>"
			+ "<a href='javascript:' class='link'>" + formatPushlishTime(rent.createTime) + "</a>"
			+ "</div>"
			+ "</div>";
		listHtml += element;
	}
	if (rentList.length < row) {
		listHtml += "<div class='content-block-title'>没有更多求租信息了</div>";
	}
	$("#rent-list").html(listHtml);
}

function setPageBtn(pageCount) {
	if (page == 1) {
		$("#pre-page").addClass("useless").attr("href", "");
	} else {
		$("#pre-page").removeClass("useless").attr("href", "javascript:goToPage(" + (page - 1) + ");");
	}
	if (pageCount == 0 || page == pageCount) {
		$("#next-page").addClass("useless").attr("href", "");
	} else {
		$("#next-page").removeClass("useless").attr("href", "javascript:goToPage(" + (page + 1) + ");");
	}
	$("#page-index").text("第" + page + "页");
}

function goToPage(page) {
	var filterParams = "";
	if (districtId) {
		filterParams += ("&districtId=" + districtId);
	}
	if (price) {
		filterParams += ("&price=" + price);
	}
	if (type) {
		filterParams += ("&type=" + type);
	}
	if (order && isDesc != null) {
		filterParams += ("&order=" + order + "&isDesc=" + isDesc);
	}
	var url = "/rent_list.html?page=" + page + filterParams;
	redirect(url);
}

function goToFilterPage(params) {
	var url = "/rent_list.html?page=1" + params;
	redirect(url);
}

function goToRentDetail(uuid) {
	var url = "/rent_detail.html?uuid=" + uuid;
	redirect(url);
}

function createRent() {
	var url = "/rent_edit.html";
	redirect(url);
}
