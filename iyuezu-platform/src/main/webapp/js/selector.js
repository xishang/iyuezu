var districts;
var callback;

var curDistrictId, curPrice, curType, curOrder, curIsDesc, curOrderList;

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
	if (!districts) {
		getDistricts();
	}
	for (var i = 0; i < districts.length; i++) {
		if (districts[i].id == id) {
			return districts[i];
		}
	}
}

function fillPopupList(tab) {
	$("#p-tab a").removeClass("active");
	$("#p-" + tab).addClass("active");
	$("#p-first-list").empty();
	$("#p-second-list").empty();
	switch (tab) {
	case "district":
		fillPopupDistrictList();
		break;
	case "price":
		fillPopupPriceList();
		break;
	case "order":
		fillPopupOrderList();
		break;
	case "type":
		fillPopupTypeList();
	}
}

function openPopup(tab) {
	$("#p-district").html($("#h-district").html());
	$("#p-price").html($("#h-price").html());
	$("#p-type").html($("#h-type").html());
	$("#p-order").html($("#h-order").html());
	fillPopupList(tab);
}

function fillPopupDistrictList() {
	if (!districts) {
		getDistricts();
	}
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
	if (curDistrictId) {
		var curDistrict = getDistrictById(curDistrictId);
		if (curDistrict.level == 3) {
			fillSecondDistrictList(curDistrict.parent);
		} else if (curDistrict.level == 2) {
			fillSecondDistrictList(curDistrict.id);
		}
	}
}

function closePopupByDistrict(id) {
	curDistrictId = id;
	executeCallback();
}

function fillSecondDistrictList(id) {
	if (!districts) {
		getDistricts();
	}
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

function fillPopupPriceList() {
	var listHtml = "<li><a class='item-link list-button close-popup' onclick='closePopupByPrice(\"\");'>不限价格</a></li>"
		+ "<li><a class='item-link list-button close-popup' onclick='closePopupByPrice(\"-1000\");'>1000元以下</a></li>"
		+ "<li><a class='item-link list-button close-popup' onclick='closePopupByPrice(\"1000-1500\");'>1000-1500元</a></li>"
		+ "<li><a class='item-link list-button close-popup' onclick='closePopupByPrice(\"1500-2000\");'>1500-2000元</a></li>"
		+ "<li><a class='item-link list-button close-popup' onclick='closePopupByPrice(\"2000-3000\");'>2000-3000元</a></li>"
		+ "<li><a class='item-link list-button close-popup' onclick='closePopupByPrice(\"3000-4500\");'>3000-4500元</a></li>"
		+ "<li><a class='item-link list-button close-popup' onclick='closePopupByPrice(\"4500-\");'>4500元以上</a></li>";
	$("#p-first-list").html(listHtml);
}

function closePopupByPrice(price) {
	curPrice = price;
	executeCallback();
}

function fillPopupOrderList() {
	var listHtml = "";
	for (var i = 0; i < curOrderList.length; i++) {
		var orderInfo = curOrderList[i];
		var liHtml = "<li><a class='item-link list-button close-popup' onclick='closePopupByOrder(\"" + orderInfo.order 
			+ "\"," + orderInfo.desc + ");'>" + orderInfo.text + "</a></li>";
		listHtml += liHtml;
	}
	$("#p-first-list").html(listHtml);
}

function closePopupByOrder(order, isDesc) {
	curOrder = order;
	curIsDesc = isDesc;
	executeCallback();
}

function fillPopupTypeList() {
	var listHtml = "<li><a class='item-link list-button close-popup' onclick='closePopupByType();'>不限类型</a></li>"
		+ "<li><a class='item-link list-button close-popup' onclick='closePopupByType(1);'>整租</a></li>"
		+ "<li><a class='item-link list-button close-popup' onclick='closePopupByType(2);'>单间</a></li>"
		+ "<li><a class='item-link list-button close-popup' onclick='closePopupByType(3);'>床位</a></li>";
	$("#p-first-list").html(listHtml);
}

function closePopupByType(type) {
	curType = type;
	executeCallback();
}

function executeCallback() {
	var filterParams = "";
	if (curDistrictId) {
		filterParams += ("&districtId=" + curDistrictId);
	}
	if (curPrice) {
		filterParams += ("&price=" + curPrice);
	}
	if (curType) {
		filterParams += ("&type=" + curType);
	}
	if (curOrder && curIsDesc != null) {
		filterParams += ("&order=" + curOrder + "&isDesc=" + curIsDesc);
	}
	callback(filterParams);
}


function initSelector(districtId, price, type, order, isDesc, orderList, cb) {
	$("#h-tab a").unbind("click").bind("click", function() {
		var tab = $(this).attr("id").split("-")[1];
		openPopup(tab);
	});
	$("#p-tab a").unbind("click").bind("click", function() {
		var tab = $(this).attr("id").split("-")[1];
		fillPopupList(tab);
	});
	curDistrictId = districtId;
	curPrice = price;
	curType = type;
	curOrder = order;
	curIsDesc = isDesc;
	curOrderList = orderList;
	callback = cb;
	if (districtId) {
		var district = getDistrictById(districtId);
		$("#h-district").html(district.name + "<span class='arrow-down'></span>");
	}
	if (price) {
		$("#h-price").html(getPriceText(price) + "<span class='arrow-down'></span>");
	}
	if (type) {
		$("#h-type").html(getHouseType(parseInt(type)) + "<span class='arrow-down'></span>");
	}
	if (order && isDesc != null) {
		$("#h-order").html(getOrderText(order, isDesc) + "<span class='arrow-down'></span>");
	}
}

function getPriceText(price) {
	if (price == "-1000") {
		return "1000元以下";
	} else if (price == "1000-1500") {
		return "1000-1500元";
	} else if (price == "1500-2000") {
		return "1500-2000元";
	} else if (price == "2000-3000") {
		return "2000-3000元";
	} else if (price == "3000-4500") {
		return "3000-4500元";
	} else if (price == "4500-") {
		return "4500元以上";
	}
}

function getOrderText(order, isDesc) {
	var orderText = "";
	if (order == "update_time") {
		orderText = "时间";
	} else if (order == "price" || order == "min_price") {
		orderText = "租金";
	} else if (order == "comment_count") {
		orderText = "评论数";
	}
	return orderText + (isDesc == 1 ? "&darr;" : "&uarr;");
}
