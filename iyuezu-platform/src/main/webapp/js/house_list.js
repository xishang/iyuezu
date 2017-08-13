var admin;
var page;
var districtId;
var price;
var type;
var order;
var isDesc;
var houseListUrl;
var row = 10;

var orderList = [{"order":"update_time", "desc":1, "text":"时间从后往前"},
                 {"order":"update_time", "desc":0, "text":"时间从前到后"},
                 {"order":"price", "desc":1, "text":"租金从高到低"},
                 {"order":"price", "desc":0, "text":"租金从低到高"},
                 {"order":"comment_count", "desc":1, "text":"评论数从高到低"},
                 {"order":"comment_count", "desc":0, "text":"评论数从低到高"}];

$(function() {
	initParams();
	initPage();
	initSelector(districtId, price, type, order, isDesc, orderList, goToFilterPage);
	getHouseList();
});

function initParams() {
	admin = getAdmin();
	var urlParams = getUrlParams();
	page = urlParams.page ? parseInt(urlParams.page) : 1;
	districtId = urlParams.districtId;
	price = urlParams.price;
	type = urlParams.type;
	order = urlParams.order;
	isDesc = urlParams.isDesc;
}

function initPage() {
	initCommonNav();
	$("#page-nav a:eq(0)").attr("href", "").addClass("active");
	if (admin == 2) {
		houseListUrl = "/house/ownList";
		$("#page-title").text("我的房源");
	} else {
		houseListUrl = "/house/list";
		$("#create-house").hide(); // 隐藏创建房源按钮
		$("#page-title").text("房源列表");
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

function getHouseList() {
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
	get(houseListUrl, params, function(result) {
		if (result.code == 0) {
			var houseList = result.data.list;
			fillHouseList(houseList);
			var pageCount = result.data.pages;
			setPageBtn(pageCount);
		}
	});
}

function fillHouseList(houseList) {
	$("#house-list").empty();
	var listHtml = "";
	for (var i = 0; i < houseList.length; i++) {
		var house = houseList[i];
		var image = "/images/yue_bg.png";
		var pictures = house.pictures;
		if (pictures) {
			image = pictures.split(",")[0];
		}
		var rooms = (house.room ? (house.room + "室") : "") + (house.hall ? (house.hall + "厅") : "") + (house.kitchen ? (house.kitchen + "厨") : "")
			+ (house.defend ? (house.defend + "卫") : "");
		var element = "<div class='card demo-card-header-pic'>"
			+ "<div valign='bottom' class='card-header color-white no-border no-padding'>"
			+ "<img style='height:40%' onclick='goToHouseDetail(\"" + house.uuid + "\");' class='card-cover' src='" + getHost() + image + "' alt=''>"
			+ "</div>"
			+ "<div class='card-content'>"
			+ "<div class='card-content-inner'>"
			+ "<h4 onclick='goToHouseDetail(\"" + house.uuid + "\");'>" + house.title + "</h4>"
			+ "<p class='color-gray'>" + house.district.name + "&nbsp;|&nbsp;" + rooms + "&nbsp;|&nbsp;" + getHouseType(house.type) + "</p>"
			+ "<span style='position: absolute;right:10;bottom:5'>已有<span style='color: red;'>" + house.reservationCount + "</span>位租客预约过该房源</span>"
			+ "</div>"
			+ "</div>"
			+ "<div class='card-footer'>"
			+ "<a href='javascript:' class='link'  style='color: grey'><span style='color: red'>" + house.price + "</span>元/月</a>"
			+ "<span>评论(" + house.commentCount + ")</span>"
			+ "<a href='javascript:' class='link'>" + formatPushlishTime(house.createTime) + "</a>"
			+ "</div>"
			+ "</div>";
		listHtml += element;
	}
	if (houseList.length < row) {
		listHtml += "<div class='content-block-title'>没有更多房源了</div>";
	}
	$("#house-list").html(listHtml);
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
	var url = "/house_list.html?page=" + page + filterParams;
	redirect(url);
}

function goToFilterPage(params) {
	var url = "/house_list.html?page=1" + params;
	redirect(url);
}

function goToHouseDetail(uuid) {
	var url = "/house_detail.html?uuid=" + uuid;
	redirect(url);
}

function createHouse() {
	var url = "/house_edit.html";
	redirect(url);
}
