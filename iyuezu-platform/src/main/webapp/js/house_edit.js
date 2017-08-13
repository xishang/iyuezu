var houseUuid = getUrlParams().uuid;
var districts;
var $curImage;

//允许上传的图片类型
var allowTypes = [ "image/jpg", "image/jpeg", "image/png", "image/gif" ];
// 图片最大内存
var maxSize = 1024 * 1024 * 5;
// 图片最大宽度
var maxWidth = 300;
// 最大上传图片数量
var maxCount = 9;

$(function() {
	
	initPage();
	bindEvent();
	getDistricts();
	if (houseUuid) {
		getHouseInfo();
	} else {
		getPersonalInfo();
	}
	
	$("#picture-upload").on("change", function(event) {
		var files = event.target.files;
		// 如果没有选中文件，直接返回
		if (files.length === 0) {
			return;
		}
		for (var i = 0, len = files.length; i < len; i++) {
			var file = files[i];
			var reader = new FileReader();
			// 如果类型不在允许的类型范围内
			if (allowTypes.indexOf(file.type) === -1) {
				Zepto.toast("该类型不允许上传");
				continue;
			}
			if (file.size > maxSize) {
				Zepto.toast("图片太大，不允许上传");
				continue;
			}
			if ($(".weui-uploader__file").length >= maxCount) {
				Zepto.toast("最多只能上传" + maxCount + "张图片");
				return;
			}
			reader.onload = function(e) {
				var img = new Image();
				img.onload = function() {
					// 不要超出最大宽度
					var w = Math.min(maxWidth, img.width);
					// 高度按比例计算
					var h = img.height * (w / img.width);
					var canvas = document.createElement('canvas');
					var ctx = canvas.getContext('2d');
					// 设置 canvas 的宽度和高度
					canvas.width = w;
					canvas.height = h;
					ctx.drawImage(img, 0, 0, w, h);
					var base64 = canvas.toDataURL('image/png');
					// 插入到预览区
					var $element = $("<li class='weui-uploader__file weui-uploader__file_status' style='background-image:url(" + base64 + ")'>"
							+ "<div class='weui-uploader__file-content'>上传中...</div>"
							+ "</li>");
					$(".weui-uploader__files").append($element);
					setImageCount();
					post("/uploadBase64", {
						base64 : base64
					}, function(result) {
						Zepto.toast(result.msg);
						if (result.code == "0") {
							$element.attr("url-data", result.data).removeClass("weui-uploader__file_status").html("");
							$element.click(function() {
								showImageDialog();
//								$("#gallery-image").attr("style", this.getAttribute("style"));
//							    $("#gallery").fadeIn(200);
								$curImage = $(this);
							});
						} else {
							$element.remove();
							setImageCount();
						}
					});
				};
				img.src = e.target.result;
			};
			reader.readAsDataURL(file);
		}
	});
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
	$("#page-nav a:eq(0)").addClass("active");
	$(".only-number").keydown(function() {
		if (event.keyCode == 13) {
			event.keyCode = 9;
		}
	}).keypress(function() {
		if (event.keyCode < 48 || event.keyCode > 57) {
			event.returnValue = false;
		}
	});
	limitSize($("#detail"), $("#detail-count"), 500);
	enableSubmit();
}

function enableSubmit() {
	$("#submit-house").unbind("click").bind("click", function() {
		saveHouseInfo();
	});
}

function disableSubmit() {
	$("#submit-house").unbind("click").bind("click", function() {
		Zepto.toast("你的提交请求正在处理中，请稍候...");
	});
}

function removeImage() {
	$curImage.remove();
	hideImageDialog();
	$("#gallery").fadeOut(200);
	setImageCount();
}

function showImageDialog() {
	$('#remove-image-dialog').fadeIn(200);
}

function hideImageDialog() {
	$('#remove-image-dialog').fadeOut(200);
}

function hideCancelDialog() {
	$('#cancel-dialog').fadeOut(200);
}

function setImageCount() {
	var num = $(".weui-uploader__file").length;
	$("#picture-count").text(num + "/" + maxCount);
	if (num == maxCount) {
		$("#upload-icon").hide();
	} else {
		$("#upload-icon").show();
	}
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
    $("#gallery").click(function(){
    	$("#gallery").fadeOut(200);
    });
}

function getHouseInfo() {
	get("/house/" + houseUuid, null, function(result) {
		if (result.code == "0") {
			var house = result.data;
			$("#title").val(house.title ? house.title : "");
			$("#district").attr("district-id", house.district ? house.district.id : "");
			$("#district").text(house.district ? house.district.name : "");
			$("#community").val(house.community ? house.community : "");
			$("#price").val(house.price ? house.price : "");
			$("#size").val(house.size ? house.size : "");
			$("#room").val(house.room == null ? "" : house.room);
			$("#hall").val(house.hall == null ? "" : house.hall);
			$("#kitchen").val(house.kitchen == null ? "" : house.kitchen);
			$("#defend").val(house.defend == null ? "" : house.defend);
//			$("#type option[value='" + (house.type ? house.type : "") + "']").attr("selected", true); // 手机设置无效
			var selectHtml = "<option value='1'" + (house.type == "1" ? " selected" : "") + ">整租</option>"
				+ "<option value='2'" + (house.type == "2" ? " selected" : "") + ">单间</option>"
				+ "<option value='3'" + (house.type == "3" ? " selected" : "") + ">床位</option>";
			$("#type").html(selectHtml);
			/*-----------*/
			if (house.contactName || house.contactPhone) {
				$("#hide-info").prop("checked", false);
				$("#contact-name").val(house.contactName ? house.contactName : "");
				$("#contact-phone").val(house.contactPhone ? house.contactPhone : "");
			} else {
				$("#hide-info").prop("checked", true);
				$(".contact-info").hide();
			}
			$("#detail").text(house.detail ? house.detail : "");
			if (house.pictures) {
				var pictureArray = house.pictures.split(",");
				var picturesHtml = "";
				for (var i = 0; i < pictureArray.length; i++) {
					var picture = pictureArray[i];
					var liElement = "<li url-data='" + picture + "' class='weui-uploader__file' style='background-image:url(" + getHost() + picture + ")'></li>";
					picturesHtml += liElement;
				}
				$(".weui-uploader__files").html(picturesHtml);
				$(".weui-uploader__file").click(function() {
					showImageDialog();
//					$("#gallery-image").attr("style", this.getAttribute("style"));
//				    $("#gallery").fadeIn(200);
					$curImage = $(this);
				});
				setImageCount();
			}
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

function saveHouseInfo() {
	var params = {};
	if (houseUuid) {
		params.uuid = houseUuid;
	}
	var pictures = "";
	$(".weui-uploader__file").each(function() {
		pictures += $(this).attr("url-data") + ",";
	});
	if (pictures.length > 0) {
		pictures = pictures.substring(0, pictures.length - 1);
	}
	params.pictures = pictures;
	var districtId = $("#district").attr("district-id");
	if (!districtId) {
		Zepto.toast("请输入房源所在地区");
		return;
	} else {
		params["district.id"] = districtId; // 对象传参使用"obj.val"当key, 不能对象中嵌套对象
	}
	var title = $("#title").val();
	if (!title) {
		Zepto.toast("请输入房源信息标题");
		return;
	} else {
		params.title = title;
	}
	var community = $("#community").val();
	if (!community) {
		Zepto.toast("请输入房源所属小区");
		return;
	} else {
		params.community = community;
	}
	var price = $("#price").val();
	if (!price) {
		Zepto.toast("请输入房源每月租金");
		return;
	} else {
		params.price = price;
	}
	var size = $("#size").val();
	if (!size) {
		Zepto.toast("请输入房源面积");
		return;
	} else {
		params.size = size;
	}
	var room = $("#room").val();
	var hall = $("#hall").val();
	var kitchen = $("#kitchen").val();
	var defend = $("#defend").val();
	if (room == "" || hall == "" || kitchen == "" || defend == "") {
		Zepto.toast("请输入房源的厅室数");
		return;
	} else {
		params.room = room;
		params.hall = hall;
		params.kitchen = kitchen;
		params.defend = defend;
	}
	var type = $("#type").val();
	if (!type) {
		Zepto.toast("请输入房源出租方式");
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
		if (contactPhone && !checkPhone(contactPhone)) { // 不强制输入手机号，但若输入则要求格式正确
			Zepto.toast("联系人手机号格式错误，请输入正确的手机号");
			return;
		} else {
			params.contactPhone = contactPhone;
		}
		var contactWechat = $("#contact-wechat").val();
		params.contactWechat = contactWechat; // detail不校验
	}
	params.detail = $("#detail").val(); // detail不强制要求
	disableSubmit();
	post("/house/save", params, function(result) {
		Zepto.toast(result.msg);
		if (result.code == "0") {
			setTimeout(function() {
				var url = "/house_detail.html?uuid=" + result.data.uuid;
				redirect(url);
			}, 1000);
		} else {
			enableSubmit();
		}
	});
}
