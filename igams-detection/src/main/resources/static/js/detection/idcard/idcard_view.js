var sfcs=0;//是否为页面初始化
$("#hzxxSave_ajaxForm .jcxm").click(function(e){
	limitYblx();
})
//限制标本类型
function limitYblx(){
	$("#hzxxSave_ajaxForm #yblx").val(-1)

	var yblxlength=$("#hzxxSave_ajaxForm .t_yblx").length;
	var jcxmid=$("input[name='fzxmids']:checked").attr("id");
	var csdm=$("#hzxxSave_ajaxForm #"+jcxmid ).attr("csdm");
	if(yblxlength>0){
		for(var i=0;i<yblxlength;i++){
			var yblxid=$("#hzxxSave_ajaxForm .t_yblx")[i].id;
			if(yblxid!=null && yblxid!=""){
				if(jcxmid==null || jcxmid ==""){
					$("#hzxxSave_ajaxForm #"+yblxid).attr("style","display:block;");
				}else{
					var jcxmdm=$("#hzxxSave_ajaxForm #"+yblxid).attr("cskz2");
					var jcxmcsdm=$("#hzxxSave_ajaxForm #"+yblxid).attr("csdm");
					if(jcxmdm!=null && jcxmdm!=""){
						if(jcxmdm.indexOf(csdm)>=0){
							$("#hzxxSave_ajaxForm #"+yblxid).attr("style","display:block;");
							if(jcxmcsdm=="SH" && sfcs==0){
								$("#hzxxSave_ajaxForm #"+yblxid).prop("selected","selected");
								sfcs=1;
							}
						}else{
							$("#hzxxSave_ajaxForm #"+yblxid).attr("style","display:none;");
						}
					}else{
						$("#hzxxSave_ajaxForm #"+yblxid).attr("style","display:none;");
					}
				}
			}
		}
	}
	$('#hzxxSave_ajaxForm #yblx').trigger("chosen:updated"); //更新下拉框
}

//默认证件类型代码为1，即居民身份证
var zjlxdm = '1';
var jcdwhz = $("#jcdwhz").val();
//居民身份证验证方法
var idCardNoUtil = {
	/*省,直辖市代码表*/
	provinceAndCitys: {
		11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古", 21: "辽宁", 22: "吉林", 23: "黑龙江",
		31: "上海", 32: "江苏", 33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东", 41: "河南", 42: "湖北", 43: "湖南", 44: "广东",
		45: "广西", 46: "海南", 50: "重庆", 51: "四川", 52: "贵州", 53: "云南", 54: "西藏", 61: "陕西", 62: "甘肃", 63: "青海", 64: "宁夏",
		65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外"
	},

	/*每位加权因子*/
	powers: ["7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"],

	/*第18位校检码*/
	parityBit: ["1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"],

	/*性别*/
	genders: {male: "1", female: "2"},

	/*校验地址码*/
	checkAddressCode: function (addressCode) {
		var check = /^[1-9]\d{5}$/.test(addressCode);
		if (!check) return false;
		if (idCardNoUtil.provinceAndCitys[parseInt(addressCode.substring(0, 2))]) {
			return true;
		} else {
			return false;
		}
	},

	/*校验日期码*/
	checkBirthDayCode: function (birDayCode) {
		var check = /^[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))$/.test(birDayCode);
		if (!check) return false;
		var yyyy = parseInt(birDayCode.substring(0, 4), 10);
		var mm = parseInt(birDayCode.substring(4, 6), 10);
		var dd = parseInt(birDayCode.substring(6), 10);
		var xdata = new Date(yyyy, mm - 1, dd);
		if (xdata > new Date()) {
			return false;//生日不能大于当前日期
		} else if ((xdata.getFullYear() == yyyy) && (xdata.getMonth() == mm - 1) && (xdata.getDate() == dd)) {
			return true;
		} else {
			return false;
		}
	},

	/*计算校检码*/
	getParityBit: function (idCardNo) {
		var id17 = idCardNo.substring(0, 17);
		/*加权 */
		var power = 0;
		for (var i = 0; i < 17; i++) {
			power += parseInt(id17.charAt(i), 10) * parseInt(idCardNoUtil.powers[i]);
		}
		/*取模*/
		var mod = power % 11;
		return idCardNoUtil.parityBit[mod];
	},

	/*验证校检码*/
	checkParityBit: function (idCardNo) {
		var parityBit = idCardNo.charAt(17).toUpperCase();
		if (idCardNoUtil.getParityBit(idCardNo) == parityBit) {
			return true;
		} else {
			return false;
		}
	},

	/*校验15位或18位的身份证号码*/
	checkIdCardNo: function (idCardNo) {
		//15位和18位身份证号码的基本校验
		var check = /^\d{15}|(\d{17}(\d|x|X))$/.test(idCardNo);
		if (!check) return false;
		//判断长度为15位或18位
		if (idCardNo.length == 15) {
			return idCardNoUtil.check15IdCardNo(idCardNo);
		} else if (idCardNo.length == 18) {
			return idCardNoUtil.check18IdCardNo(idCardNo);
		} else {
			return false;
		}
	},

	//校验15位的身份证号码
	check15IdCardNo: function (idCardNo) {
		//15位身份证号码的基本校验
		var check = /^[1-9]\d{7}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}$/.test(idCardNo);
		if (!check) return false;
		//校验地址码
		var addressCode = idCardNo.substring(0, 6);
		check = idCardNoUtil.checkAddressCode(addressCode);
		if (!check) return false;
		var birDayCode = '19' + idCardNo.substring(6, 12);
		//校验日期码
		check = idCardNoUtil.checkBirthDayCode(birDayCode);
		if (!check) return false;
		//验证校检码
		return idCardNoUtil.checkParityBit(idCardNo);
	},

	//校验18位的身份证号码
	check18IdCardNo: function (idCardNo) {
		//18位身份证号码的基本格式校验
		var check = /^[1-9]\d{5}[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}(\d|x|X)$/.test(idCardNo);
		if (!check) return false;
		//校验地址码
		var addressCode = idCardNo.substring(0, 6);
		check = idCardNoUtil.checkAddressCode(addressCode);
		if (!check) return false;
		//校验日期码
		var birDayCode = idCardNo.substring(6, 14);
		check = idCardNoUtil.checkBirthDayCode(birDayCode);
		if (!check) return false;
		//验证校检码
		return idCardNoUtil.checkParityBit(idCardNo);
	},

	formateDateCN: function (day) {
		var yyyy = day.substring(0, 4);
		var mm = day.substring(4, 6);
		var dd = day.substring(6);
		return yyyy + '-' + mm + '-' + dd;
	},

	//获取信息
	getIdCardInfo: function (idCardNo) {
		var idCardInfo = {
			gender: "",  //性别
			birthday: "" // 出生日期(yyyy-mm-dd)
		};
		if (idCardNo.length == 15) {
			var aday = '19' + idCardNo.substring(6, 12);
			idCardInfo.birthday = idCardNoUtil.formateDateCN(aday);
			if (parseInt(idCardNo.charAt(14)) % 2 == 0) {
				idCardInfo.gender = idCardNoUtil.genders.female;
			} else {
				idCardInfo.gender = idCardNoUtil.genders.male;
			}
		} else if (idCardNo.length == 18) {
			var aday = idCardNo.substring(6, 14);
			idCardInfo.birthday = idCardNoUtil.formateDateCN(aday);
			if (parseInt(idCardNo.charAt(16)) % 2 == 0) {
				idCardInfo.gender = idCardNoUtil.genders.female;
			} else {
				idCardInfo.gender = idCardNoUtil.genders.male;
			}

		}
		return idCardInfo;
	},

	/*18位转15位*/
	getId15: function (idCardNo) {
		if (idCardNo.length == 15) {
			return idCardNo;
		} else if (idCardNo.length == 18) {
			return idCardNo.substring(0, 6) + idCardNo.substring(8, 17);
		} else {
			return null;
		}
	},

	/*15位转18位*/
	getId18: function (idCardNo) {
		if (idCardNo.length == 15) {
			var id17 = idCardNo.substring(0, 6) + '19' + idCardNo.substring(6);
			var parityBit = idCardNoUtil.getParityBit(id17);
			return id17 + parityBit;
		} else if (idCardNo.length == 18) {
			return idCardNo;
		} else {
			return null;
		}
	}
};

//通过省份级联城市
$("#sf").on('change',function(){
	var csid=$("#sf").val();

	$.ajax({
		url : "/partner/pagedataJscjcity",
		type : "post",
		data : {fcsid:csid,"access_token":$("#ac_tk").val()},
		dataType : "json",
		async:false,
		success:function(data){
			if(data != null && data.length != 0){
				var csbHtml = "";
				csHtml += "<option value=''>--请选择--</option>";
				$.each(data,function(i){
					csHtml += "<option value='" + data[i].csid + "'>" + data[i].csmc + "</option>";
				});
				$("#hzxxSave_ajaxForm #cs").empty();
				$("#hzxxSave_ajaxForm #cs").append(csHtml);
				$("#hzxxSave_ajaxForm #cs").trigger("chosen:updated");
			}else{
				var csHtml = "";
				csHtml += "<option value=''>--请选择--</option>";
				$("#hzxxSave_ajaxForm #cs").empty();
				$("#hzxxSave_ajaxForm #cs").append(csHtml);
				$("#hzxxSave_ajaxForm #cs").trigger("chosen:updated");
			}
		}
	});
})

//创建读卡控件
var CertCtl = new IDCertCtl();
var xgxmid = $("#hzxxSave_ajaxForm #xgxmid").val();
//身份证读卡控件创建
function IDCertCtl() {
	//创建用于与服务交换数据的对象
	this.xhr = createXmlHttp();
	this.type = "CertCtl";
	this.height = 0;
	this.width = 0;
	//连接
	this.connect = CertCtl_connect;
	//断开
	this.disconnect = CertCtl_disconnect;
	//获取状态
	this.getStatus = CertCtl_getStatus;
	//读卡
	this.readCert = CertCtl_readCert;
	//读IC卡序列号
	this.readICCardSN = CertCtl_readICCardSN;
	//读身份证物理卡号
	this.readIDCardSN = CertCtl_readIDCardSN;
	//读安全模块接口
	this.getSAMID = CertCtl_getSAMID;
}

//创建XMLHttpRequest 对象，用于在后台与服务器交换数据
function createXmlHttp() {
	var xmlHttp = null;
	//根据window.XMLHttpRequest对象是否存在使用不同的创建方式
	if (window.XMLHttpRequest) {
		xmlHttp = new XMLHttpRequest();                  //FireFox、Opera等浏览器支持的创建方式
	} else {
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");//IE浏览器支持的创建方式
	}
	return xmlHttp;
}

//连接方法
function CertCtl_connect() {
	var result = "";
	//创建请求 第一个参数是代表以post方式发送；第二个是请求端口和地址；第三个表示是否异步
	CertCtl.xhr.open("POST", "http://127.0.0.1:18889/api/connect", false);
	//发送请求
	try {
		CertCtl.xhr.send();
	} catch (e) {
	}
	//返回值readyState   0: 请求未初始化
	//				    1: 服务器连接已建立
	//				    2：请求已接收
	//				    3: 请求处理中
	//				    4: 请求已完成，且响应已就绪
	//返回值status      200: "OK"
	//					404: 未找到页面
	//当返回值readyState为4且status为200时,为查询成功
	if (CertCtl.xhr.readyState == 4 && CertCtl.xhr.status == 200) {
		result = CertCtl.xhr.responseText;
		CertCtl.xhr.readyState = 1;
	}
	return result;
}

//断开方法
function CertCtl_disconnect() {
	var result = "";
	//创建请求 第一个参数是代表以post方式发送；第二个是请求端口和地址；第三个表示是否异步
	CertCtl.xhr.open("POST", "http://127.0.0.1:18889/api/disconnect", false);
	//发送请求
	try {
		CertCtl.xhr.send();
	} catch (e) {
	}
	if (CertCtl.xhr.readyState == 4 && CertCtl.xhr.status == 200) {
		result = CertCtl.xhr.responseText;
		CertCtl.xhr.readyState = 1;
	}
	return result;
}

//获取状态方法
function CertCtl_getStatus() {
	var result = "";
	//创建请求 第一个参数是代表以post方式发送；第二个是请求端口和地址；第三个表示是否异步
	CertCtl.xhr.open("POST", "http://127.0.0.1:18889/api/getStatus", false);
	//发送请求
	try {
		CertCtl.xhr.send();
	} catch (e) {
	}
	if (CertCtl.xhr.readyState == 4 && CertCtl.xhr.status == 200) {
		result = CertCtl.xhr.responseText;
		CertCtl.xhr.readyState = 1;
	}
	return result;
}

//执行读卡操作
function CertCtl_readCert() {
	var result = "";
	try {
		//创建请求 第一个参数是代表以post方式发送；第二个是请求端口和地址；第三个表示是否异步
		//CertCtl.xhr.open("POST", "http://127.0.0.1:18889/api/readCard", false); //readCard读卡，不生成正反面仿复印件
		CertCtl.xhr.open("POST", "http://127.0.0.1:18889/api/readCert", false);	  //readCert读卡，生成正反面仿复印件	
		//发送请求
		CertCtl.xhr.send();
		if (CertCtl.xhr.readyState == 4 && CertCtl.xhr.status == 200) {
			result = CertCtl.xhr.responseText;
			CertCtl.xhr.readyState = 1;
		}
	} catch (e) {

	}
	return result;
}

//获取IC卡序列号
function CertCtl_readICCardSN() {
	var result = "";
	//创建请求 第一个参数是代表以post方式发送；第二个是请求端口和地址；第三个表示是否异步
	CertCtl.xhr.open("POST", "http://127.0.0.1:18889/api/readICCardSN", false);
	//发送请求
	try {
		CertCtl.xhr.send();
	} catch (e) {
	}
	if (CertCtl.xhr.readyState == 4 && CertCtl.xhr.status == 200) {
		result = CertCtl.xhr.responseText;
		CertCtl.xhr.readyState = 1;
	}
	return result;
}

//获取身份证物理卡号
function CertCtl_readIDCardSN() {
	var result = "";
	//创建请求 第一个参数是代表以post方式发送；第二个是请求端口和地址；第三个表示是否异步
	CertCtl.xhr.open("POST", "http://127.0.0.1:18889/api/readIDCardSN", false);
	//发送请求
	try {
		CertCtl.xhr.send();
	} catch (e) {
	}
	if (CertCtl.xhr.readyState == 4 && CertCtl.xhr.status == 200) {
		result = CertCtl.xhr.responseText;
		CertCtl.xhr.readyState = 1;
	}
	return result;
}

//获取身份证物理卡号
function CertCtl_getSAMID() {
	var result = "";
	//创建请求 第一个参数是代表以post方式发送；第二个是请求端口和地址；第三个表示是否异步
	CertCtl.xhr.open("POST", "http://127.0.0.1:18889/api/getSAMID", false);
	//发送请求
	try {
		CertCtl.xhr.send();
	} catch (e) {
	}
	if (CertCtl.xhr.readyState == 4 && CertCtl.xhr.status == 200) {
		result = CertCtl.xhr.responseText;
		CertCtl.xhr.readyState = 1;
	}
	return result;
}

//转为Json格式
function toJson(str) {
	//var obj = JSON.parse(str);
	//return obj;
	return eval('(' + str + ')');
}

//清空页面上显示的内容
function clearForm() {
	//对应控件的值全部清空
	$("#hzxxSave_ajaxForm #xmdsfz").html('');
	$("#hzxxSave_ajaxForm #xbdsfz").html('');
	$("#hzxxSave_ajaxForm #csrqdsfz").html('');
	$("#hzxxSave_ajaxForm #nldsfz").html('');
	$("#idcardReadForm #certNumber").val('');
	$("#hzxxSave_ajaxForm #sfbjsp").html('-1');
	$("#hzxxSave_ajaxForm #csbjsp").html('-1');
	$("#hzxxSave_ajaxForm #xxdzbjsp").html('');

}

//连接方法
function connect() {
	//清空页面
	clearForm();

	try {
		//调用对应的连接方法,并赋值给result
		var result = CertCtl.connect();
		//如果result为空,代表读卡插件未启动
		if (result == "") {
			alert("未启动读卡插件!")
		} else {
			//result页面回显
			document.getElementById("result").value = result;
		}
	} catch (e) {
	}
}

//断开连接方法
function disconnect() {
	//清空页面
	clearForm();

	try {
		//调用对应的断开连接方法,并赋值给result
		var result = CertCtl.disconnect();
		//如果result为空,代表读卡插件未启动
		if (result == "") {
			alert("未启动读卡插件!")
		} else {
			//result页面回显
			document.getElementById("result").value = result;
		}
	} catch (e) {
	}
}

//获取状态方法
function getStatus() {
	//清空页面
	clearForm();

	try {
		//调用对应的获取状态方法,并赋值给result
		var result = CertCtl.getStatus();
		//如果result为空,代表读卡插件未启动
		if (result == "") {
			alert("未启动读卡插件!")
		} else {
			//result页面回显
			document.getElementById("result").value = result;
		}
	} catch (e) {
	}
}

//读卡方法
function readCert() {
	connect();//链接
	//清空页面
	clearForm();
	//开始时间
	var startDt = new Date();
	//调用对应的读卡方法
	var result = CertCtl.readCert();
	//如果result为空,代表读卡插件未启动
	if (result == "") {
		alert("未启动读卡插件!")
	} else {
		// =======================查询或者点击身份证需要给序号赋值==============================
		// xhDeal()
		if ($("#hzxxSave_ajaxForm #xh").val() == null || $("#hzxxSave_ajaxForm #xh").val() == undefined || $("#hzxxSave_ajaxForm #xh").val() == '') {
			$("#hzxxSave_ajaxForm #xh").val( '01' );
		}
		// =====================================查询/读身份证赋值序号结束=====================
		//结束时间
		var endDt = new Date();
		//读卡时间回显
		document.getElementById("timeElapsed").value = (endDt.getTime() - startDt.getTime()) + "毫秒";
		document.getElementById("result").value = result;
		//var resultObj = toJson(result);
		//result = result.replace("\"resultFlag\":","\"resultFlag\":\"true\"");
		//格式化result
		//var resultObj = $.parseJSON(result);//windows10上面无法解析
		var resultObj = eval('(' + result + ')');
		//resultFlag为0代表读卡成功
		if (resultObj.resultFlag == "0") {
			//回显相关数据
			$("#hzxxSave_ajaxForm #xmdsfz").html(resultObj.resultContent.partyName);
			if(resultObj.resultContent.gender == '1'){
				$("#hzxxSave_ajaxForm #xbdsfz").html("男");
			}else{
				$("#hzxxSave_ajaxForm #xbdsfz").html("女");
			}
			// $("#hzxxSave_ajaxForm #xbdsfz").html(resultObj.resultContent.gender);
			$("#hzxxSave_ajaxForm #csrqdsfz").html(resultObj.resultContent.bornDay);
			$("#hzxxSave_ajaxForm #nldsfz").html(   (2021 - parseInt((resultObj.resultContent.bornDay).substr(0, 4)))  );
			$("#idcardReadForm #certNumber").val(resultObj.resultContent.certNumber);
			//省份
			var cernum = resultObj.resultContent.certNumber;
			if(cernum!=null&&cernum!=""){
				var sfnam = getcsandsfname(cernum.substring(0,2));
				var sfnm = "PROVINCE_"+cernum.substring(0,2)+"0000";
				var count = $("#hzxxSave_ajaxForm #sf option").length;
				for ( var i = 0; i < count; i++) {
					if ($("#hzxxSave_ajaxForm #sf ").get(0).options[i].text == sfnam) {
						$("#hzxxSave_ajaxForm #sf").get(0).options[i].selected = true;
						sfnm = $("#hzxxSave_ajaxForm #sf ").get(0).options[i].value;
						$("#hzxxSave_ajaxForm #sf").change();
						break;
					}
				}
				//获取城市
				var csnm ="CITY_"+cernum.substring(0,4)+"00";
				var csnam = getcsandsfname(cernum.substring(0,4));
				if(sfnam.slice(sfnam.length - 1)=="市"){
					csnm = "CITY_"+cernum.substring(0,6);
					csnam = getcsandsfname(cernum.substring(0,6));
				}
				var count1 = $("#hzxxSave_ajaxForm #cs option").length;
				for ( var i = 0; i < count1; i++) {
					if ($("#hzxxSave_ajaxForm #cs ").get(0).options[i].text == csnam) {
						csnm = $("#hzxxSave_ajaxForm #cs").get(0).options[i].value ;
						break;
					}
				}

				$("#hzxxSave_ajaxForm #sfbjsp").html(sfnm);
				$("#hzxxSave_ajaxForm #csbjsp").html(csnm);
			}else{
				$("#hzxxSave_ajaxForm #sfbjsp").html("-1");
				$("#hzxxSave_ajaxForm #csbjsp").html("-1");
			}
			$("#hzxxSave_ajaxForm #xxdzbjsp").html(resultObj.resultContent.certAddress);
			var map = {};
			map['xm'] = resultObj.resultContent.partyName;
			map['zjh'] = resultObj.resultContent.certNumber;
			//读卡成功以后去查找后台数据库
			readCardQ();
		} else if (resultObj.resultFlag == "-1") {
			if (resultObj.errorMsg == "端口打开失败") {
				alert("读卡器未连接");
			} else {
				alert(resultObj.errorMsg);
			}
		} else if (resultObj.resultFlag == "-2") {
			alert(resultObj.errorMsg);
		}
	}
}

function sendData(map) {
	//初始化列表
	$('#idcardReadForm #tb_list').bootstrapTable('refresh');

}

function readICCardSN() {
	//清空页面
	clearForm();

	try {
		//调用对应的获取状态方法,并赋值给result
		var result = CertCtl.readICCardSN();
		//如果result为空,代表读卡插件未启动
		if (result == "") {
			alert("未启动读卡插件!")
		} else {
			//result页面回显
			document.getElementById("result").value = result;
		}
	} catch (e) {
	}
}

function readIDCardSN() {
	//清空页面
	clearForm();

	try {
		//调用对应的获取状态方法,并赋值给result
		var result = CertCtl.readIDCardSN();
		//如果result为空,代表读卡插件未启动
		if (result == "") {
			alert("未启动读卡插件!")
		} else {
			//result页面回显
			document.getElementById("result").value = result;
		}
	} catch (e) {
	}
}

function getSAMID() {
	//清空页面
	clearForm();

	try {
		//调用对应的获取状态方法,并赋值给result
		var result = CertCtl.getSAMID();
		//如果result为空,代表读卡插件未启动
		if (result == "") {
			alert("未启动读卡插件!")
		} else {
			//result页面回显
			document.getElementById("result").value = result;
		}
	} catch (e) {
	}
}

var Appointment_TableInit = function(map) {
	var zjh = map['zjh'];
	var oTableInit = new Object();
	oTableInit.Init = function() {
		$("#idcardReadForm #tb_list").bootstrapTable({
			url: "/detection/detection/pagedataOverdueAppoint",         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#idcardReadForm #toolbar',                // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         // 增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName: "hzxx.xm",				// 排序字段
			sortOrder: "asc",                   // 排序方式
			queryParams: oTableInit.queryParams,// 传递参数（*）
			sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
			pageNumber: 1,                       // 初始化加载第一页，默认第一页
			pageSize: 15,                       // 每页的记录行数（*）
			pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
			paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
			search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,
			showColumns: true,                  // 是否显示所有的列
			showRefresh: true,                  // 是否显示刷新按钮
			minimumCountColumns: 2,             // 最少允许的列数
			clickToSelect: true,                // 是否启用点击选中行
			// height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "xm",                     // 每一行的唯一标识，一般为主键列
			showToggle: true,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true,
				width: '1%'
			}, {
				field: 'fzjcid',
				title: '项目名称',
				width: '10%',
				align: 'left',
				visible: false
			}, {
				field: 'jcxmmc',
				title: '项目名称',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'xm',
				title: '收检人姓名',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'ybbh',
				title: '标本编号',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'yyjcrq',
				title: '预约检测日期',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'fkbj',
				title: '支付状态',
				width: '10%',
				formatter: idcardfkbjformat,
				align: 'left',
				visible: true
			}, {
				field: 'cyd',
				title: '采样点',
				width: '10%',
				align: 'left',
				visible: false
			}, {
				field: 'fzxmids',
				title: '项目id',
				width: '10%',
				align: 'left',
				visible: false
			}],
			onLoadSuccess: function() {
			},
			onLoadError: function() {
				alert("数据加载失败！");
			},
			onLoadSuccess: function(map) {
				//默认选中查出来的那条检测数据
				var sel_row = $('#idcardReadForm #tb_list').bootstrapTable('getData');
				for (var i=0; i < sel_row.length; i++){
					if ( sel_row[i].fzjcid == $("#hzxxSave_ajaxForm #fzjcid").val() ){
						$('#idcardReadForm #tb_list').bootstrapTable("checkBy",{field:'fzjcid',values:[sel_row[i].fzjcid]});
					}
				}
			},
			onLoadError: function() {
				alert("数据加载失败！");
			},
			onDblClickRow: function(row, $element) {
			},
			onCheck: function(row, $element) {
				var sel_row = $('#idcardReadForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if (sel_row.length == 1) {
					//将此行的信息赋值到文本框种
					setFzjcxx(sel_row[0]);
				} else if (sel_row.length == 0) {//清空信息
					$("input:[name='fzxmids']").each(function() {
						$(this).attr('checked', false);
					});
					$(":checkbox[name='fzxmids'][value='" + xgxmid + "']").prop("checked", "checked");
					$("#hzxxSave_ajaxForm #tw").val('');
					// $("#hzxxSave_ajaxForm #yyjcrq").val('');
					$("#hzxxSave_ajaxForm #fzjcid").val('');
					//$("#hzxxSave_ajaxForm #cyd").val('');
					//$("#hzxxSave_ajaxForm #jcdw").val('');
					//清空医院
					//$("#hzxxSave_ajaxForm #sjdwmc").val(null);
					//$("#hzxxSave_ajaxForm #sjdw").val('');
					//$("#hzxxSave_ajaxForm #hospitalname").val('');
					//$("#hzxxSave_ajaxForm #sjdwmc").attr("disabled", "disabled");
					//$("#hzxxSave_ajaxForm #qtyycheck").hide();
					//$("#hzxxSave_ajaxForm  #glxx").val('')
					//$("#hzxxSave_ajaxForm #ks").val('--请选择--');
					//$("#hzxxSave_ajaxForm #qtks").val('');
					//$("#hzxxSave_ajaxForm #qtks").attr("disabled", "disabled");
				    //$("#hzxxSave_ajaxForm #qtkscheck").hide();

					$("#hzxxSave_ajaxForm #fzjcid").val('');
					//$("input[name='fzxmids']").prop("checked", false);
				} else {
					$.error("请选中一行");
				}
			},
		});
		$("#idcardReadForm #tb_list").colResizable({
			liveDrag: true,
			gripInnerHtml: "<div class='grip'></div>",
			draggingClass: "dragging",
			resizeMode: 'fit',
			postbackSafe: true,
			partialRefresh: true
		}
		);
	};
	oTableInit.queryParams = function(params) {
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			pageSize: params.limit,   // 页面大小
			pageNumber: (params.offset / params.limit) + 1,  // 页码
			access_token: $("#hzxxSave_ajaxForm #access_token").val(),
			sortName: params.sort,      // 排序列名
			sortOrder: params.order, // 排位命令（desc，asc）
			sortLastName: "hzxx.xm", // 防止同名排位用

			sortLastOrder: "asc" // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		map["zjh"] = $("#idcardReadForm #certNumber").val();
		map["zjlx"] = $('#idcardReadForm #zjlx').val();
		return map;
	};
	return oTableInit;
}

//标记格式化
function idcardfkbjformat(value,row,index){
	var html="";
	if(row.fkbj=='1'){
		html="<span style='color:green;'>"+"已付款"+"</span>";
	}else if(row.fkbj=='0'){
		html="<button type='button' class='btn btn-danger' onclick=\"payOrder('"+row.fzjcid+"\',\'"+row.wxid+"')\">扫码支付</button><button type=\"button\" class=\"btn btn-primary\" onclick=\"zfwc('"+row.fzjcid+"','"+row.fkje+"')\">现金支付</button>";
		$("#idcardReadForm #fk").attr("style","background:#ff6162;");
	}else if(row.fkbj=='2'){
		html="<span style='color: rgba(0,0,0,0.64);'>"+"已退款"+"</span>";
	}
	return html;
}
var xh='01';
function xhDeal(){
	var mrjclxcsid = $("#hzxxSave_ajaxForm #jclxmr_csid").val();
	var jclxcsid = $("#hzxxSave_ajaxForm #fzjczxmid").val();
	var jclxcsdm ;
	if ( jclxcsid == null || jclxcsid === 'undefined' || jclxcsid === '') {
		$("#hzxxSave_ajaxForm #fzjczxmid").val(   $("#hzxxSave_ajaxForm #jclxmr_csid").val()  );
		jclxcsdm = $("#hzxxSave_ajaxForm #"+mrjclxcsid).attr('csdm');
	}else {
		jclxcsdm = $("#hzxxSave_ajaxForm #"+jclxcsid).attr('csdm');
	}
	if (jclxcsdm=='D'){
		$("#hzxxSave_ajaxForm #xh").val(xh);
	}else if (jclxcsdm=='H10'){
		if (xh<=10){
			$("#hzxxSave_ajaxForm #xh").val(      $("#hzxxSave_ajaxForm #xh").val()+1       );
		}
		xh='01';
	}else if (jclxcsdm=='H05'){
		if (xh<=5){
			$("#hzxxSave_ajaxForm #xh").val(      $("#hzxxSave_ajaxForm #xh").val()+1       );
		}
		xh='01';
	}else if (jclxcsdm=='H03'){
		if (xh<=3){
			$("#hzxxSave_ajaxForm #xh").val(      $("#hzxxSave_ajaxForm #xh").val()+1       );
		}
		xh='01';
	}
}

// //keydown事件
// $("#idcardReadForm #btn_hzxx_query").on('keydown',function(e){
// 	if ($("#certNumber").val().length==15 || $("#certNumber").val().length==18){
// 		if (getZjh()){
// 			readCardQ();//查询患者新冠信息
// 		}
// 	}else if ( $("#certNumber").val().length==32 ){
// 		readCardQ();//查询患者新冠信息
// 	}
// });
// //keyup事件
// $("#idcardReadForm #btn_hzxx_query").keyup(function () {
// 	if ($("#certNumber").val().length==15 || $("#certNumber").val().length==18){
// 		if (getZjh()){
// 			readCardQ();//查询患者新冠信息
// 		}
// 	}else if ( $("#certNumber").val().length==32 ){
// 		readCardQ();//查询患者新冠信息
// 	}
// });

//点击查询按钮
$("#idcardReadForm #btn_hzxx_query").unbind("click").click(function(e) {
	// =======================查询或者点击身份证需要给序号赋值==============================
	// xhDeal()
	if ($("#hzxxSave_ajaxForm #xh").val() == null || $("#hzxxSave_ajaxForm #xh").val() == undefined || $("#hzxxSave_ajaxForm #xh").val() == '') {
		$("#hzxxSave_ajaxForm #xh").val( '01' );
	}
	// =====================================查询/读身份证赋值序号结束=====================
	$("#hzxxSave_ajaxForm #xmdsfz").html('');//姓名
	$("#hzxxSave_ajaxForm #xbdsfz").html('');//性别
	$("#hzxxSave_ajaxForm #csrqdsfz").html('');//出生日期
	$("#hzxxSave_ajaxForm #nldsfz").html('');//年龄
	$("#hzxxSave_ajaxForm #sfbjsp").html('-1');
	$("#hzxxSave_ajaxForm #csbjsp").html('-1');
	$("#hzxxSave_ajaxForm #xxdzbjsp").html('');
	
	// if (getZjh()){
		readCardQ();//查询患者新冠信息
	// }else{
	//
	// }
});

function getHzxxQuery(){
	if ($("#hzxxSave_ajaxForm #xh").val() == null || $("#hzxxSave_ajaxForm #xh").val() == undefined || $("#hzxxSave_ajaxForm #xh").val() == '') {
		$("#hzxxSave_ajaxForm #xh").val( '01' );
	}
	$("#hzxxSave_ajaxForm #xmdsfz").html('');
	$("#hzxxSave_ajaxForm #xbdsfz").html('');
	$("#hzxxSave_ajaxForm #csrqdsfz").html('');
	$("#hzxxSave_ajaxForm #nldsfz").html('');
	$("#hzxxSave_ajaxForm #sfbjsp").html('-1');
	$("#hzxxSave_ajaxForm #csbjsp").html('-1');
	$("#hzxxSave_ajaxForm #xxdzbjsp").html('');
	// if (getZjh()){
		readCardQ();
	// }else{
	//
	// }
}

function zfwc(fzjcid,sfje){
	$.confirm('确定已完成支付',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			var url= "/detection/detection/pagedataNuonuoPay";
			jQuery.post(url,{fzjcid:fzjcid,sfje:sfje,zffs:"现金支付","access_token":$("#ac_tk").val()},function(responseText){
				setTimeout(function(){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							readCardQ();
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				},1);
			},'json');
			jQuery.ajaxSetup({async:true});
		}
	});
}

function payOrder(fzjcid,wxid) {
	let amount = $("#fkje").val();
	$.ajax({
		url: $("#idcardReadForm #menuurl").val()+'/wechat/pay/createOrderQRCode',
		type: 'post',
		dataType: 'json',
		data : {"ywid" : fzjcid, "ybbh" : "XGBDJC","fkje": amount,"wxid": wxid,"ywlx": "XG"},
		success: function(data) {
			if(data.status == 'success'){
				// 二维码路径 data.qrCode
				$.showDialog('/detection/detection/pagedataGoPay?payUtl='+data.qrCode+'&fzjcid='+fzjcid,'扫码支付',payDetectionConfig);
				// $("#fastPay_ajaxForm #path").val(data.filePath);
				// var url= $("#idcardReadForm #menuurl").val()+"/wechat/pay/picturePreview?path="+ data.filePath +"&fkje="+amount +"&ywid="+fzjcid +"&ybbh=XGBDJC&ywlx=XG";
				// $.showDialog(url,'图片预览',JPGMaterConfig);
			}else{
				$.alert(data.message);
			}
			// $("#fastPay_ajaxForm .preBtn").attr("disabled", false);
		}
	});
	//诺诺支付
	// $.ajax({
	// 	type: "post",
	// 	url: "/detection/detection/detectionAppointmentSave",
	// 	dataType: 'json',
	// 	data: { "fzjcid": fzjcid ,"subject": "新冠病毒检测","amount": amount, "access_token": $("#ac_tk").val() },
	// 	success: function (result) {
	// 		if ("success" == result.status) {
	// 			var pay = JSON.parse(result.payUrl);
	// 			if ("JH200" == pay.code) {
	// 				let payUtl = pay.result.payUtl;
	// 				let temps = payUtl.split("?");
	// 				let pays = "";
	// 				let utl = "";
	// 				if (temps.length > 1){
	// 					pays = temps[0]+"%3F";
	// 					utl = temps[1].split("&");
	// 					let str = "";
	// 					for (let i=0;i<utl.length;i++){
	// 						str = str + utl[i] +"%26";
	// 					}
	// 					let s = "";
	// 					if (str){
	// 						let strs = str.split("=")
	// 						for (let i=0;i<strs.length;i++){
	// 							s = s + strs[i] +"%3D";
	// 						}
	// 					}
	// 					pays += s;
	// 				}
	// 				$.showDialog('/detection/detection/goPay?payUtl='+pays+'&fzjcid='+fzjcid,'扫码支付',payDetectionConfig);
	// 			}
	// 		} else {
	// 			$.alert("支付信息获取失败！");
	// 		}
	// 	}
	// })
}

function closePayModal(modalName) {
	readCardQ();
	$.closeModal(modalName);
}
/*查看详情信息模态框*/
var payDetectionConfig = {
	width		: "600px",
	height		: "300px",
	modalName	: "payModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "现金支付",
			className : "btn-primary",
			callback : function() {
				clearTimeout(timer);
				let fzjcid = $("#payOrderInfo #fzjcid").val();
				if (!fzjcid){
					$.error("更新失败!");
					return;
				}
				let sfje =$("#fkje").val();
				zfwc(fzjcid,sfje);
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default",
			callback : function() {
				clearTimeout(timer);
			}
		}
	}
};

//查询患者新冠信息
function readCardQ() {
	if (!$("#idcardReadForm").valid()) {
		return false;
	}
	/* --------------------------- 修改 -----------------------------------*/
	var zjh = $('#idcardReadForm #certNumber').val().toUpperCase();
	var zjlx = $('#idcardReadForm #zjlx').val();
	// console.log("查询");
	if (zjh != null && zjh != "") {
		cleanValue('');
		$.ajax({
			type: 'post',
			url: "/detection/detection/pagedataAfterAppointOa", //查询最新的一条预约数据
			cache: false,
			data: { "zjh":zjh, "zjlx":zjlx, "access_token":$("#access_token").val() },
			dataType: 'json',
			success: function(data) {
				$("#hzxxSave_ajaxForm #btn_hzxx_mod").show();  //编辑人员按钮
				$("#hzxxSave_ajaxForm #btn_hzxx_mod_qr").hide();  //人员身份信息保存按钮
				$("#hzxxSave_ajaxForm #btn_hzxx_mod_qx").hide(); //人员身份信息取消按钮
				showAndHideRy("2");
				//查看查询出的用户信息是否为空
				var hzxxDto = data.hzxxDto;
				//后台有患者信息，则复制给前端
				if (hzxxDto != null && hzxxDto != undefined && hzxxDto != '') {
					if (hzxxDto.hzid != null && hzxxDto.hzid != undefined && hzxxDto.hzid != '') {
						if (hzxxDto.sf == null || hzxxDto.sf == undefined || hzxxDto.sf == ''){
							hzxxDto.sf  = $("#hzxxSave_ajaxForm #sfbjsp").text();
						}
						if (hzxxDto.cs == null || hzxxDto.cs == undefined || hzxxDto.cs == ''){
							hzxxDto.cs = $("#hzxxSave_ajaxForm #csbjsp").text()
						}
						if (hzxxDto.xxdz == null || hzxxDto.xxdz == undefined || hzxxDto.xxdz == ''){
							hzxxDto.xxdz = $("#hzxxSave_ajaxForm #xxdzbjsp").text();
							// $("#hzxxSave_ajaxForm #xxdzbjsp").html(resultObj.resultContent.certAddress);
						}

						setHzxx(hzxxDto);
						//验证和读出身份证的数据是否相同
						valxm();
						var fzjcxxDto = data.fzjcxxDto;
						if (fzjcxxDto != null && fzjcxxDto != undefined && fzjcxxDto != '') {
							if (fzjcxxDto.fzjcid !== null || fzjcxxDto.fzjcid !== undefined || fzjcxxDto.fzjcid !== ''){
								$("#hzxxSave_ajaxForm #yyjcxx").attr("class","panel panel-success");
							}
							setFzjcxx(fzjcxxDto);
							if (fzjcxxDto.fkbj == "1"){
								$("#idcardReadForm #fk").removeAttr("style","background:#ff6162;");
								$("#idcardReadForm #fkbj").text("已支付！");
								$("#idcardReadForm #fkbj").attr("style","background:#88ffa1;font-size: 24px;cursor:default");
								// $("#idcardReadForm #pay").attr("style","display:none");
								// $("#idcardReadForm #pay").removeAttr("onclick","zfwc(\'"+fzjcxxDto.fzjcid+"\')");
								$("#idcardReadForm #fkbj").removeAttr("onclick","payOrder(\'"+fzjcxxDto.fzjcid+"\',\'"+fzjcxxDto.wxid+"\')");
							}else{
								$("#idcardReadForm #fk").attr("style","background:#ff6162;");
								$("#idcardReadForm #fkbj").text("未支付(扫码支付)！");
								$("#idcardReadForm #fkbj").attr("style","background:#red;font-size: 24px;cursor:pointer");
								$("#idcardReadForm #fkbj").attr("onclick","payOrder(\'"+fzjcxxDto.fzjcid+"\',\'"+fzjcxxDto.wxid+"\')");
								// $("#idcardReadForm #fk").removeAttr("style","background:#ff6162;");
								// $("#idcardReadForm #pay").attr("style","display:block");
								// $("#idcardReadForm #pay").attr("onclick","zfwc(\'"+fzjcxxDto.fzjcid+"\')");
							}
							$("#idcardReadForm #tb_listdiv").show();
							var map = {};
							map['zjh'] = hzxxDto.zjh;
							sendData(map);
						} else {//未查到未过期的预约信息，则查询过期的信息
							$("#idcardReadForm #tb_listdiv").show();
							var map = {};
							map['zjh'] = hzxxDto.zjh;
							sendData(map);
							$("#idcardReadForm #fk").attr("style","background:#ff6162;");
							$("#idcardReadForm #fkbj").text("未预约！");
							$("#idcardReadForm #fkbj").attr("style","background:#red;font-size: 24px;;cursor:default");
						}

					}else{
						$("#hzxxSave_ajaxForm #ryxx").attr("class","panel panel-warning");
					}
				}else{
					if(zjh.length > 18){
						$.error("系统中未查询到该预约码，或者该预约码已经被使用，请重新预约。");
					}else{
						
						//未查询到患者信息弹出消息框并将读取到的身份信息赋值
						$.error("系统中未查询到身份信息，可编辑人员。");
						//保存到后台人员信息
						var hzxxDto = {};
						hzxxDto.xm = $("#hzxxSave_ajaxForm #xmdsfz").text();
						var xbmc = $("#hzxxSave_ajaxForm #xbdsfz").text();
						if (xbmc == '未知') {
							hzxxDto.xb = '0'
						} else if (xbmc == '男') {
							hzxxDto.xb = '1'
						} else if (xbmc == '女') {
							hzxxDto.xb = '2'
						}
						hzxxDto.nl = $("#hzxxSave_ajaxForm #nldsfz").text();
						hzxxDto.xxdz = $("#hzxxSave_ajaxForm #xxdzbjsp").text();
						hzxxDto.sf  = $("#hzxxSave_ajaxForm #sfbjsp").text();
						hzxxDto.cs = $("#hzxxSave_ajaxForm #csbjsp").text()
						setHzxx(hzxxDto)
						$("#hzxxSave_ajaxForm #ryxx").attr("class","panel panel-warning");
						
					}
				}
			}, error: function(XMLHttpRequest, textStatus, errorThrown) {
				// 401一般都为两台电脑登录报错
				if (     "401"==(XMLHttpRequest.status)      ){
					$.error("系统账号被他人重复登录!请重登录");
				}else if(  "500"==(XMLHttpRequest.status)  ){
					$.error(XMLHttpRequest.status+" 内部服务器错误。");
				}else {
					$.error("状态码："+XMLHttpRequest.status+", readyState: "+XMLHttpRequest.readyState+", textStatus: "+textStatus+" 查询身份信息报错。");
				}
			}

		});

	} else {
		$("#idcardReadForm #fk").removeAttr("style");
		$("#idcardReadForm #fkbj").text("");
		$("#idcardReadForm #fkbj").removeAttr("style");
		$.error("证件信息不能为空!");
	}
	/* ----------------------------------------------------------------------*/
}

//标记（勾、叉）
function valxm() {
	var xm = $("#hzxxSave_ajaxForm #xmry").val();
	var xmsp = $("#hzxxSave_ajaxForm #xmdsfz").text();
	if (xm != null && xm !=undefined && xm !='' && xmsp != null && xmsp!= undefined && xmsp != ''){
		if (xm == xmsp) {
			$("#hzxxSave_ajaxForm #xmpdd").show();
			$("#hzxxSave_ajaxForm #xmpdc").hide();
			$("#hzxxSave_ajaxForm #xmpddsf").show();
			$("#hzxxSave_ajaxForm #xmpdcsf").hide();
		}else{
			$("#hzxxSave_ajaxForm #xmpddsf").hide();
			$("#hzxxSave_ajaxForm #xmpdcsf").show();
			$("#hzxxSave_ajaxForm #xmpdd").hide();
			$("#hzxxSave_ajaxForm #xmpdc").show();
		}
	}
}

//姓名改变时间
$("#hzxxSave_ajaxForm #xmry").on('change', function(){
	valxm();
});

/*人员身份信息编辑按钮*/
$("#btn_hzxx_mod").unbind("click").click(function(e) {
	/*按钮变动*/
	$("#hzxxSave_ajaxForm #btn_hzxx_mod").hide();
	$("#hzxxSave_ajaxForm #btn_hzxx_mod_qr").show();
	$("#hzxxSave_ajaxForm #btn_hzxx_mod_qx").show();
	showAndHideRy("1");
});

/*人员身份信息取消按钮*/
$("#hzxxSave_ajaxForm #btn_hzxx_mod_qx").on("click", function(e) {
	/*按钮变动*/
	$("#hzxxSave_ajaxForm #btn_hzxx_mod").show();
	$("#hzxxSave_ajaxForm #btn_hzxx_mod_qr").hide();
	$("#hzxxSave_ajaxForm #btn_hzxx_mod_qx").hide();
	//取消的时候将input框中的值还原
	$("#hzxxSave_ajaxForm #xmry").val($("#hzxxSave_ajaxForm #xmrysp").text());
	$("#hzxxSave_ajaxForm #sjry").val($("#hzxxSave_ajaxForm #sjrysp").text());
	$("#hzxxSave_ajaxForm #xxdz").val($("#hzxxSave_ajaxForm #xxdzsp").text());
	$("#hzxxSave_ajaxForm #nlry").val($("#hzxxSave_ajaxForm #nlrysp").text());
	$("#hzxxSave_ajaxForm #sf").val($("#hzxxSave_ajaxForm #sfsp").text());
	$("#sf").change();
	$("#hzxxSave_ajaxForm #cs").val($("#hzxxSave_ajaxForm #cssp").text());
	var xb = $("#hzxxSave_ajaxForm #xbrysp").text();
	var xbv = '0';
	if(xb == '男'){
		xbv='1';
	}else if(xb=='女'){
		xbv='2';
	}
	$(":radio[name='xb'][value='" + xbv + "']").prop("checked", "checked");
	showAndHideRy("2");
	valxm();
});

/*人员身份信息保存按钮*/
$("#hzxxSave_ajaxForm #btn_hzxx_mod_qr").on("click", function(e) {
	//保存到后台人员信息
	var hzid = $("#hzxxSave_ajaxForm #hzid").val();
	var xmry = $("#hzxxSave_ajaxForm #xmry").val();
	var xb = $("input[name='xb']:checked").val();
	var sjry = $("#hzxxSave_ajaxForm #sjry").val();
	// var xjzdry = $("#hzxxSave_ajaxForm #xjzdry").val();
	var xxdz = $("#hzxxSave_ajaxForm #xxdz").val();
	var nlry = $("#hzxxSave_ajaxForm #nlry").val();
	var certNo = $('#idcardReadForm #certNumber').val();
	var sf = $("#hzxxSave_ajaxForm #sf").val();
	var cs = $("#hzxxSave_ajaxForm #cs").val();
	var zjlx = $("#idcardReadForm #zjlx").val();
	if (certNo == null || certNo == '') {
		$.alert("证件号不能为空");
		return;
	}
	if (zjlxdm == '1' && certNo.length>18) {
		$.alert("预约码为检测ID，请填写身份证号后点保存");
		return;
	}if (zjlxdm == '1' && certNo.length<15) {
		$.alert("输入身份证号位数不正确");
		return;
	}

	if (xmry == null || xmry == '') {
		$.alert("请输入姓名。");
		return;
	}
	if (sjry == null || sjry == '') {
		$.alert("请输入手机号。");
		return;
	}
	// if (xjzdry == null || xjzdry == '') {
	// 	$.alert("请输入居住地址。");
	// 	return;
	// }
	if (nlry == null || nlry == '') {
		$.alert("请输入年龄。");
		return;
	}
	// if (sf == null || sf == '') {
	// 	$.alert("省份不能为空");
	// 	return;
	// }
	// if (cs == null || cs == '') {
	// 	$.alert("城市不能为空");
	// 	return;
	// }
	// if (xxdz == null || xxdz == '') {
	// 	$.alert("请输入详细地址。");
	// 	return;
	// }

	if (!$("#idcardReadForm").valid()) {
		return false;
	}

	$.ajax({
		type: 'post',
		url: "/detection/detection/pagedataEditorHzxx",
		cache: false,
		data: {
			"hzid": hzid,
			"xb": xb,
			"sj": sjry,
			"nl": nlry,
			"xm": xmry,
			"zjh": certNo,
			"sf": sf,
			"cs": cs,
			"xxdz": xxdz,
			"zjlx":zjlx,
			"access_token": $("#access_token").val()
		},
		dataType: 'json',
		success: function(data) {
			//查看查询出的用户信息是否为空
			if (data.status == 'success') {
				setHzxx(data.hzxxDto);
				//保存成功后要隐藏按钮显示值
				$("#hzxxSave_ajaxForm #btn_hzxx_mod").show();
				$("#hzxxSave_ajaxForm #btn_hzxx_mod_qr").hide();
				$("#hzxxSave_ajaxForm #btn_hzxx_mod_qx").hide();
				showAndHideRy("2");
			} else {
				$.error(data.message);
			}
		}, error: function() {
			$.error("保存人员信息出错");
		}

	});
    valxm();
});

//管理身份信息模块输入框只读和可编辑状态，编辑/保存/取消的只读部分的区别
function showAndHideRy(type) {
	if (type == '1') {
		$("#hzxxSave_ajaxForm #xxdz").removeAttr("readonly","true"); //详细地址
		$("#hzxxSave_ajaxForm #sjry").removeAttr("readonly","true"); //手机号码
		$("#hzxxSave_ajaxForm #nlry").removeAttr("readonly","true"); //年龄
		$("#hzxxSave_ajaxForm #csnyry").removeAttr("readonly","true");
		$("#hzxxSave_ajaxForm #xbry label input").removeAttr("disabled","true"); //性别
		$("#hzxxSave_ajaxForm #xmry").removeAttr("readonly","true"); //姓名
		$("#hzxxSave_ajaxForm #sf").removeAttr("disabled","true");  //省份
		$("#hzxxSave_ajaxForm #cs").removeAttr("disabled","true"); //城市
	} else if (type == '2') {
		$("#hzxxSave_ajaxForm #xxdz").attr("readonly","true")
		$("#hzxxSave_ajaxForm #sjry").attr("readonly","true")
		$("#hzxxSave_ajaxForm #nlry").attr("readonly","true")
		$("#hzxxSave_ajaxForm #csnyry").attr("readonly","true")
		$("#hzxxSave_ajaxForm #xbry label input").attr("disabled","true")
		$("#hzxxSave_ajaxForm #xmry").attr("readonly","true")
		$("#hzxxSave_ajaxForm #sf").attr("disabled","true")
		$("#hzxxSave_ajaxForm #cs").attr("disabled","true")
	}
};

//查询到的患者信息赋值
function setHzxx(hzxxDto) {
	$("#hzxxSave_ajaxForm #hzid").val(hzxxDto.hzid);
	$("#hzxxSave_ajaxForm #xmrysp").html(hzxxDto.xm);
	var xb = "";
	if (hzxxDto.xb == '0') {
		xb = '未知'
	} else if (hzxxDto.xb == '1') {
		xb = '男'
	} else if (hzxxDto.xb == '2') {
		xb = '女'
	}
	$("#hzxxSave_ajaxForm #xbrysp").html(xb);
	$("#hzxxSave_ajaxForm #sjrysp").html(hzxxDto.sj);
	// $("#hzxxSave_ajaxForm #xjzdrysp").html(hzxxDto.xjzd);
	$("#hzxxSave_ajaxForm #xxdzsp").html(hzxxDto.xxdz);
	$("#hzxxSave_ajaxForm #nlrysp").html(hzxxDto.nl);
	$("#hzxxSave_ajaxForm #sfsp").html(hzxxDto.sf);
	$("#hzxxSave_ajaxForm #cssp").html(hzxxDto.cs);
	//同时赋值给input框
	$("#hzxxSave_ajaxForm #xmry").val(hzxxDto.xm);
	$("#hzxxSave_ajaxForm #sjry").val(hzxxDto.sj);
	// $("#hzxxSave_ajaxForm #xjzdry").val(hzxxDto.xjzd);
	$("#hzxxSave_ajaxForm #nlry").val(hzxxDto.nl);
	$(":radio[name='xb'][value='" + hzxxDto.xb + "']").prop("checked", "checked");
	$("#hzxxSave_ajaxForm #ryxx").attr("class","panel panel-success");
	$("#hzxxSave_ajaxForm #ryxx").attr("class","panel panel-success");
	//查找出的人员信息没有地址信息时候，填写身份证读取到的数据

	$("#hzxxSave_ajaxForm #sf").val(hzxxDto.sf);
	$("#sf").change();
	$("#hzxxSave_ajaxForm #cs").val(hzxxDto.cs);
	$("#hzxxSave_ajaxForm #xxdz").val(hzxxDto.xxdz);
}

//查询到的新冠分子信息赋值
function setFzjcxx(fzjcxxDto) {
	$("#hzxxSave_ajaxForm #tw").val(fzjcxxDto.tw);
	$("#hzxxSave_ajaxForm #pt").val(fzjcxxDto.pt);
	$("#hzxxSave_ajaxForm #yyjcrq").val(fzjcxxDto.yyjcrq);
	$("#hzxxSave_ajaxForm #fzjcid").val(fzjcxxDto.fzjcid);
	//赋值给送检单位
	var yyxxcskz1 = fzjcxxDto.yyxxcskz1; //为1时候代表选择的是其他单位
	if (yyxxcskz1 == '1') {
		$("#hzxxSave_ajaxForm #sjdwmc").val(fzjcxxDto.sjdwmc); //其他单位
		// $("#hzxxSave_ajaxForm #qtyycheck").removeAttr("disabled");
		// $("#hzxxSave_ajaxForm #qtyycheck").show();
	} else {
		$("#hzxxSave_ajaxForm #sjdwmc").val(fzjcxxDto.sjdwmc);
		$("#hzxxSave_ajaxForm #sjdwmc").attr("disabled", "disabled");
		// $("#hzxxSave_ajaxForm #qtyycheck").hide();

	}
	//先判断是否锁定，锁定状态下才更改送检单位的值为查找出的值
	if ( !$("#hzxxSave_ajaxForm #sjdwsd").is(':checked') ){//所有版本:true/false
		//非选中状态更改送检单位
		//送检单位
		if (   fzjcxxDto.sjdw != null && fzjcxxDto.sjdw != ""){
			$("#hzxxSave_ajaxForm #hospitalname").val(fzjcxxDto.dwmc);
			$("#hzxxSave_ajaxForm #sjdw").val(fzjcxxDto.sjdw);
		}
	}
	// console.log($("#hzxxSave_ajaxForm #hospitalname").val());
	// console.log($("#hzxxSave_ajaxForm #sjdw").val());

	//赋值给科室
	$("#hzxxSave_ajaxForm #ks").val(fzjcxxDto.ks);
	//样本类型
	if (fzjcxxDto.yblx != null && fzjcxxDto.yblx != ""){
		$("#hzxxSave_ajaxForm #yblx").val(fzjcxxDto.yblx);
	}
	if (fzjcxxDto.qtks != null && fzjcxxDto.qtks != "") {
		$("#hzxxSave_ajaxForm #qtkscheck").show();
		$("#hzxxSave_ajaxForm #qtks").removeAttr("disabled");
		$("#hzxxSave_ajaxForm #qtks").val(fzjcxxDto.qtks);
	} else {
		$("#hzxxSave_ajaxForm #qtkscheck").hide();
		$("#hzxxSave_ajaxForm #qtks").attr("disabled", "disabled");
	}
	//采样点
	// if (   fzjcxxDto.cyd != null && fzjcxxDto.cyd != ""){
	// 	$("#hzxxSave_ajaxForm #cyd").val(fzjcxxDto.cyd);
	// }
	if(fzjcxxDto.jcdw!=null&&fzjcxxDto.jcdw!=''){
		$("#hzxxSave_ajaxForm #jcdw").val(fzjcxxDto.jcdw);
	}else{
		$("#hzxxSave_ajaxForm #jcdw").val(jcdwhz);
	}

	if (fzjcxxDto.jcxmid != null && fzjcxxDto.jcxmid != '') {
		$("input[name='fzxmids']").prop("checked", false);
		var fzjcxs = fzjcxxDto.jcxmid.split(",");
		if (fzjcxs.length > 0) {
			for (var i = 0; i < fzjcxs.length; i++) {
				$(":checkbox[name='fzxmids'][value='" + fzjcxs[i] + "']").prop("checked", "checked");
			}
		}
	}
	$("#hzxxSave_ajaxForm #yyjcxx").attr("class","panel panel-success");
}

//单机选中表格事件
function getRowsetFzjcxx(fzxmids, tw, yyjcrq, fzjcid, cyd) {
	//将值赋到文本框种
	$("#hzxxSave_ajaxForm #tw").val(tw);
	$("#hzxxSave_ajaxForm #yyjcrq").val(yyjcrq);
	$("#hzxxSave_ajaxForm #fzjcid").val(fzjcid);
	$("#hzxxSave_ajaxForm #cyd").val(cyd);
	if(jcdw!=null&&jcdw!=''){
		$("#hzxxSave_ajaxForm #jcdw").val(jcdw);
	}else{
		$("#hzxxSave_ajaxForm #jcdw").val(jcdwhz);
	}
	$("#hzxxSave_ajaxForm #yblx").val(yblx);

	if (fzxmids != null && fzxmids != '') {
		var fzjcxs = fzxmids.split(",");
		$("input[name='fzxmids']").prop("checked", false);
		if (fzjcxs.length > 0) {
			for (var i = 0; i < fzjcxs.length; i++) {
				$(":checkbox[name='fzxmids'][value='" + fzjcxs[i] + "']").prop("checked", "checked");
			}
		}
	}
}

//清空所有的值
function cleanValue(bj) {
	if (bj == '1') {
		$("#idcardReadForm #certNumber").val('');
		$("#hzxxSave_ajaxForm #xmdsfz").html('');
		$("#hzxxSave_ajaxForm #xbdsfz").html('');
		$("#hzxxSave_ajaxForm #csrqdsfz").html('');
		$("#hzxxSave_ajaxForm #nldsfz").html('');
		$("#hzxxSave_ajaxForm #sfbjsp").html('-1');
		$("#hzxxSave_ajaxForm #csbjsp").html('-1');
		$("#hzxxSave_ajaxForm #xxdzbjsp").html('');
	}
	//清空医院
	//$("#hzxxSave_ajaxForm #sjdwmc").val(null);
	//$("#hzxxSave_ajaxForm #sjdw").val('');
	//$("#hzxxSave_ajaxForm #hospitalname").val('');
	//$("#hzxxSave_ajaxForm #sjdwmc").attr("disabled", "disabled");
	//$("#hzxxSave_ajaxForm #qtyycheck").hide();
	//$("#hzxxSave_ajaxForm  #glxx").val('')
	$("#hzxxSave_ajaxForm #hzid").val('');
	$("#hzxxSave_ajaxForm #xmrysp").html('');
	$("#hzxxSave_ajaxForm #xbrysp").html('');
	$("#hzxxSave_ajaxForm #sjrysp").html('');
	// $("#hzxxSave_ajaxForm #xjzdrysp").html('');
	$("#hzxxSave_ajaxForm #xxdzsp").html('');
	$("#hzxxSave_ajaxForm #nlrysp").html('');
	$("#hzxxSave_ajaxForm #sfsp").html('');
	$("#hzxxSave_ajaxForm #cssp").html('');
	$("#hzxxSave_ajaxForm #xmry").val('');
	$("#hzxxSave_ajaxForm #sjry").val('');
	// $("#hzxxSave_ajaxForm #xjzdry").val('');
	$("#hzxxSave_ajaxForm #xxdz").val('');
	$("#hzxxSave_ajaxForm #sf").val('-1');
	$("#hzxxSave_ajaxForm #cs").val('-1');
	$("#hzxxSave_ajaxForm #nlry").val('');
	$(":radio[name='xb'][value='" + '0' + "']").prop("checked", "checked");
	$("#hzxxSave_ajaxForm #tw").val('');
	$("#hzxxSave_ajaxForm #pt").val('');
	$("#hzxxSave_ajaxForm #yyjcrq").val($("#hzxxSave_ajaxForm #yyjcrq_default").val());
	//$("#hzxxSave_ajaxForm #cyd").val('');
	//$("#hzxxSave_ajaxForm #jcdw").val('-1');
	$("#hzxxSave_ajaxForm #fzjcid").val('');
	$("input[name='fzxmids']").prop("checked", false);
	$(":checkbox[name='fzxmids'][value='" + xgxmid + "']").prop("checked", "checked");
	//清空表格,隐藏表头
	$("#idcardReadForm #tb_list  tr:not(:first)").empty();
	$("#idcardReadForm #tb_listdiv").hide();
	//$("#hzxxSave_ajaxForm #ks").val('-1');
	//$("#hzxxSave_ajaxForm #qtks").val('');
	//$("#hzxxSave_ajaxForm #qtks").attr("disabled", "disabled");
   // $("#hzxxSave_ajaxForm #qtkscheck").hide();

	//将身份真读和读取数据对比的表示隐藏掉
	$("#hzxxSave_ajaxForm #xmpdd").hide();
	$("#hzxxSave_ajaxForm #xmpdc").hide();
	$("#hzxxSave_ajaxForm #xmpddsf").hide();
	$("#hzxxSave_ajaxForm #xmpdcsf").hide();

	$("#hzxxSave_ajaxForm #xh").attr("readonly","readonly");
	$("#hzxxSave_ajaxForm #ybbh_ym").attr("readonly","readonly");
	$("#hzxxSave_ajaxForm #yyjcxx").attr("class","panel panel-default");
	$("#hzxxSave_ajaxForm #ryxx").attr("class","panel panel-default");
}

//保存按钮点击事件
$("#hzxxSave_ajaxForm #btn_hzxx_save").on("click", function(ev) {
	$("#hzxxSave_ajaxForm #btn_hzxx_save").attr("disabled", "disabled");//点击保存以后按钮不可再次点击，防止重复操作
	//取消省份城市的disabled
	$("#hzxxSave_ajaxForm #sf").removeAttr("disabled","true");
	$("#hzxxSave_ajaxForm #cs").removeAttr("disabled","true");
	$("#hzxxSave_ajaxForm #xbry label input").removeAttr("disabled","true")
	 saveFzjcxm();
});

//保存页面信息
function saveFzjcxm() {
	var hzid = $('#hzxxSave_ajaxForm #hzid').val();
	if (hzid == null || hzid == '') {
		$("#hzxxSave_ajaxForm #btn_hzxx_save").removeAttr("disabled");
		$.alert("人员信息不存在，请先编辑人员信息。");
		return;
	}
	// var ks = $('#hzxxSave_ajaxForm #ks').val();  //去掉科室必填项
	// if (ks == null || ks == '' || ks == '-1') {
	// 	$.alert("请选择科室。");
	// 	return;
	// }
	var sz_flg = $("input[name='szz']:checked").val();
	if (sz_flg == "1") {
		var glxx = $("#hzxxSave_ajaxForm  #glxx").val();
		if (glxx == null || glxx == '') {
			$("#hzxxSave_ajaxForm #btn_hzxx_save").removeAttr("disabled");
			$.alert("请填写外机地址。");
			return;
		}
	}
	if (!$("#hzxxSave_ajaxForm").valid()) {
		$("#hzxxSave_ajaxForm #btn_hzxx_save").removeAttr("disabled");
		$.alert("请填写完整信息！");
		return false;
	}
	var yblx = $('#hzxxSave_ajaxForm #yblx').val();
	if (yblx == null || yblx == '' || yblx == '-1') {
		$("#hzxxSave_ajaxForm #btn_hzxx_save").removeAttr("disabled");
		$.alert("请选择样本类型。");
		return;
	}
	//判断患者信息是否填写完整
	var hzid = $("#hzxxSave_ajaxForm #hzid").val();
	var xmry = $("#hzxxSave_ajaxForm #xmry").val();
	var xb = $("input[name='xb']:checked").val();
	var sjry = $("#hzxxSave_ajaxForm #sjry").val();
	// var xjzdry = $("#hzxxSave_ajaxForm #xjzdry").val();
	var xxdz = $("#hzxxSave_ajaxForm #xxdz").val();
	var nlry = $("#hzxxSave_ajaxForm #nlry").val();
	var certNo = $('#idcardReadForm #certNumber').val();
	var sf = $("#hzxxSave_ajaxForm #sf").val();
	var cs = $("#hzxxSave_ajaxForm #cs").val();
	if (certNo == null || certNo == '') {
		$("#hzxxSave_ajaxForm #btn_hzxx_save").removeAttr("disabled");
		$.alert("证件号不能为空");
		return;
	}
	if (xmry == null || xmry == '') {
		$("#hzxxSave_ajaxForm #btn_hzxx_save").removeAttr("disabled");
		$.alert("请输入姓名。");
		return;
	}
	if (sjry == null || sjry == '') {
		$("#hzxxSave_ajaxForm #btn_hzxx_save").removeAttr("disabled");
		$.alert("请输入手机号。");
		return;
	}
	if (nlry == null || nlry == '') {
		$("#hzxxSave_ajaxForm #btn_hzxx_save").removeAttr("disabled");
		$.alert("请输入年龄。");
		return;
	}
	// if (sf == null || sf == '') {
	// 	$("#hzxxSave_ajaxForm #btn_hzxx_save").removeAttr("disabled");
	// 	$.alert("省份不能为空");
	// 	return;
	// }
	// if (cs == null || cs == '') {
	// 	$("#hzxxSave_ajaxForm #btn_hzxx_save").removeAttr("disabled");
	// 	$.alert("城市不能为空");
	// 	return;
	// }
	// if (xxdz == null || xxdz == '') {
	// 	$("#hzxxSave_ajaxForm #btn_hzxx_save").removeAttr("disabled");
	// 	$.alert("请输入详细地址。");
	// 	return;
	// }
	if (!$("#idcardReadForm").valid()) {
		$("#hzxxSave_ajaxForm #btn_hzxx_save").removeAttr("disabled");
		return false;
	}

	$('#hzxxSave_ajaxForm').ajaxSubmit({
		url: "/detection/detection/pagedataSubmitAppoint",
		dataType: "text",
		success: function(data) {
			//打印
			var dataJson = $.parseJSON(data);
			var xh =  $("#hzxxSave_ajaxForm #xh").val();
			if (xh == "01" || !xh){
				printQrjg(dataJson);
			}
			// ===================
			$("#hzxxSave_ajaxForm #ybbh_ym").val(      dataJson.ybbh_ym       );
			// ===================
			// $.success("保存成功。",function () {//去除保存成功的弹窗点击，
			// 更改为消息框显示三秒结束，toastr，部分默认设置值https://www.jq22.com/jquery-info15566

			$("#idcardReadForm #fk").removeAttr("style");
			$("#idcardReadForm #fkbj").text("");
			$("#idcardReadForm #fkbj").removeAttr("style");
			$("#hzxxSave_ajaxForm #xh").val(      dataJson.xh       );
			//清空页面信息
			cleanValue('1');
			$.Toast("success", "提交数据成功！", "success", {
					stack: true,
					has_icon:true,
					has_close_btn:true,
					fullscreen:false,
					timeout:2000,
					sticky:false,
					has_progress:true,
					rtl:false,
				}
			);

			$("#hzxxSave_ajaxForm #btn_hzxx_save").removeAttr("disabled");
			$("#hzxxSave_ajaxForm #btn_hzxx_mod").show();
			$("#hzxxSave_ajaxForm #btn_hzxx_mod_qr").hide();
			$("#hzxxSave_ajaxForm #btn_hzxx_mod_qx").hide();
			showAndHideRy("2");
			//保存后增加证件号输入框的光标锁定
			$("#idcardReadForm #certNumber").focus(); // 文本框获得焦点
			// });
		},
		error: function() {
			$("#hzxxSave_ajaxForm #btn_hzxx_save").removeAttr("disabled");
			$.alert("保存失败。");
			$("#idcardReadForm #certNumber").focus(); // 文本框获得焦点
		}
	});
}

laydate.render({
	elem: ' #hzxxSave_ajaxForm #yyjcrq'
	,type: 'datetime'
	,ready: function(date){
		if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
			var myDate = new Date(); //实例一个时间对象；
			this.dateTime.hours=myDate.getHours();
			this.dateTime.minutes=myDate.getMinutes();
			this.dateTime.seconds=myDate.getSeconds();
		}
	}
});

//打印机改变事件
$("input[name=szz]").on("click", function(ev) {
	var sz_flg = $("input[name='szz']:checked").val();
	if (sz_flg == "0") {
		$("#hzxxSave_ajaxForm #glxx").attr("disabled", "disabled");
		$("#hzxxSave_ajaxForm #glxx").val("");
	} else if (sz_flg == "1") {
		$("#hzxxSave_ajaxForm #glxx").removeAttr("disabled");
	}
});

//打印方法
function printQrjg(json) {
	var glxx = $("#hzxxSave_ajaxForm #glxx").val();
	var sz_flg = $("input[name='szz']:checked").val();
	var print_url = null
	if (sz_flg == "0") {
		print_url = "http://localhost:8081/XGLRPrint";
	} else if (sz_flg == "1") {
		print_url = "http://" + glxx + ":8082/XGLRPrint";
	}
	var url = print_url+"?callBackUrl="+json.openUrl+"&ResponseNum=3&RequestLocalCode="+json.RequestLocalCode+"&ResponseSign="+json.ResponseSign+"&access_token="+ $("#ac_tk").val();
	openWindow = window.open(url);
	setTimeout(function(){
		openWindow.close();
	}, 800);
	if (json.sfxz){
		payOrder(json.fzjcid)
	}
}

//选择送检单位
function selectHospital() {
	url = "/wechat/hospital/pagedataCheckUnitView?access_token=" + $("#access_token").val();
	$.showDialog(url, '医院名称', SelectFzjcHospitalConfig);
};

//送检单位模板框
//医院列表弹出框
var SelectFzjcHospitalConfig = {
	width: "1000px",
	modalName: "SelectFzjcHospitalConfig",
	offAtOnce: false,  //当数据提交成功，立刻关闭窗口
	buttons: {
		success: {
			label: "确 定",
			className: "btn-primary",
			callback: function() {
				var sel_row = $('#hospital_formSearch #hospital_list').bootstrapTable('getSelections');
				if (sel_row.length == 1) {
					var dwid = sel_row[0].dwid;
					var dwmc = sel_row[0].dwmc;
					var cskz1 = sel_row[0].cskz1;
					$("#hzxxSave_ajaxForm #sjdw").val(dwid);
					$("#hzxxSave_ajaxForm #hospitalname").val(dwmc);
					if (cskz1 == '1') {
						$("#hzxxSave_ajaxForm #sjdwmc").val(null);
						$("#hzxxSave_ajaxForm #sjdwmc").removeAttr("disabled");
						// $("#hzxxSave_ajaxForm #qtyycheck").show();
						
					} else {
						$("#hzxxSave_ajaxForm #sjdwmc").val("");
						$("#hzxxSave_ajaxForm #sjdwmc").attr("disabled", "disabled");
						// $("#hzxxSave_ajaxForm #qtyycheck").hide();

					}
				} else {
					$.error("请选中一行!");
					return false;
				}
			},
		},
		cancel: {
			label: "关 闭",
			className: "btn-default"
		}
	}
};

//科室改变时间
$("#hzxxSave_ajaxForm #ks").change(function() {
	var kzcs = $("#hzxxSave_ajaxForm #ks option:selected");
	if (kzcs.attr("kzcs") == "1") {
		$("#hzxxSave_ajaxForm #qtkscheck").show();
		$("#hzxxSave_ajaxForm #qtks").removeAttr("disabled");
	} else {
		$("#hzxxSave_ajaxForm #qtkscheck").hide();
		$("#hzxxSave_ajaxForm #qtks").attr("disabled", "disabled");
		$("#hzxxSave_ajaxForm #qtks").val("");
	}
});

//资料打开事件
$("#showzl").on("click", function(){
	$("#syzl").show();
	$("#showzl").hide();
	$("#hidezl").show();
})

//资料关闭事件
$("#hidezl").on("click", function(){
	$("#syzl").hide();
	$("#hidezl").hide();
	$("#showzl").show();
})

function cancleRed(){
	$("#checkZjh").removeClass("has-error")
	$("#btn_hzxx_query").removeAttr("disabled","true")
};

//证件类别改变事件
function changeZjlx(){
	//获取证件类型代码，并设置
	zjlxdm = $("#"+$("#zjlx").val()).attr("csdm");
	$("#checkZjh").removeClass("has-error")
	$("#btn_hzxx_query").removeAttr("disabled","true")
}

//证件号验证
function getZjh(){
	// var idCard = $("#certNumber").val().toUpperCase();
	if ($("#certNumber").val()  !=null && $("#certNumber").val() !=''){
		if (zjlxdm == '1') {
			/*居民身份证*/
			/*使用 * 验证身份证的正确性*/
			// true 验证通过，身份证号码正确
			// false 身份证号码错误
			if ($("#certNumber").val().length<20){
				if (!idCardNoUtil.checkIdCardNo(   $("#certNumber").val().toUpperCase())  ) {
					$("#checkZjh").addClass("has-error")
					$("#btn_hzxx_query").attr("disabled","true")
					return false
				}
			}
			$("#checkZjh").removeClass("has-error")
			$("#btn_hzxx_query").removeAttr("disabled","true")
			return true
		}
/*		else if (zjlxdm == '2') {
			/!*护照*!/
			// if (!idCard || !/^((1[45]\d{7})|(G\d{8})|(P\d{7})|(S\d{7,8}))?$/.test(idCard)) {
			// 	$("#checkZjh").addClass("has-error")
			// 	return false
			// }
			$("#checkZjh").removeClass("has-error")
			$("#btn_hzxx_query").removeAttr("disabled","true")
			return true
		} else if (zjlxdm == '55') {
			/!*港澳居住证*!/
			// if (!idCard || !/^[HMhm]{1}([0-9]{10}|[0-9]{8})$/.test(idCard)) {
			// 	$("#checkZjh").addClass("has-error")
			// 	return false
			// }
			$("#checkZjh").removeClass("has-error")
			$("#btn_hzxx_query").removeAttr("disabled","true")
			return true
		}else if (zjlxdm == '54'){
			/!*台湾居住证*!/
			// if (!idCard || !/^830000(?:19|20)\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])\d{3}[\dX]$/.test(idCard)){
			// 	$("#checkZjh").addClass("has-error")
			// 	return false
			// }
			$("#checkZjh").removeClass("has-error")
			$("#btn_hzxx_query").removeAttr("disabled","true")
			return true
		}*/
		$("#checkZjh").removeClass("has-error")
		$("#btn_hzxx_query").removeAttr("disabled","true")
		return true
	}
	return true
}

$("#hzxxSave_ajaxForm #xmdsfz").bind('DOMNodeInserted', function () {
	judegeIsSame();
});

$("#hzxxSave_ajaxForm #xmry").change(function () {
	judegeIsSame();
});
function judegeIsSame(){
	var xmread = $("#hzxxSave_ajaxForm #xmdsfz").text();
	var xmedit = $("#hzxxSave_ajaxForm #xmry").val();
	if (xmread != null && xmread != '' && xmread != undefined && xmedit != null && xmedit != '' && xmedit != undefined) {

			if (xmread == xmedit){
				$("#hzxxSave_ajaxForm #xmpdcsf").show();
				$("#hzxxSave_ajaxForm #xmpddsf").show();
				$("#hzxxSave_ajaxForm #xmpdc").show();
				$("#hzxxSave_ajaxForm #xmpdd").show();
			}
		}
}

//序号点击编辑事件
function modXhBj(){
	$("#hzxxSave_ajaxForm #xh").removeAttr("readonly");
}

function modYbbhBj(){
	$("#hzxxSave_ajaxForm #ybbh_ym").removeAttr("readonly");
}

function Btnbind(){
	//检测类别下拉框事件
	var sel_fzjczxm=$("#hzxxSave_ajaxForm #fzjczxmid");
	sel_fzjczxm.unbind("change").change(function(){
		fzjczxmEvent();
	});
}
function fzjczxmEvent(){
	$("#hzxxSave_ajaxForm #ybbh_ym").val("");
	$("#hzxxSave_ajaxForm #xh").val("01");
}

$("#ybbh_ym").blur(function(){
	$("#ybbh_ym").val($("#ybbh_ym").val().replaceAll("\t","").replaceAll(" ","").replaceAll("　",""))
});

$("#xh").blur(function(){
	$("#xh").val($("#xh").val().replaceAll("\t","").replaceAll(" ","").replaceAll("　",""))
});

$(function() {
	//增加证件号输入框的光标锁定
    $("#idcardReadForm #certNumber").focus(); // 文本框获得焦点
	Btnbind();
	//根据基础数据分子检测类型的默认值页面显示默认数据
	var jclxcsid = $("#hzxxSave_ajaxForm #fzjczxmid").val();
	if ( jclxcsid == null || jclxcsid === 'undefined' || jclxcsid === '') {
		$("#hzxxSave_ajaxForm #fzjczxmid").val(   $("#hzxxSave_ajaxForm #jclxmr_csid").val()  );
	}
	$(":checkbox[name='fzxmids'][value='" + xgxmid + "']").prop("checked", "checked");
	//初始化列表
	var map = {};
	map['zjh'] = '';
	var oTable = new Appointment_TableInit(map);
	oTable.Init();
	$('#idcardReadForm').validateForm({
		beforeValidated: function() {
			return true
		}, beforeSubmit: function(h, g, f) {
			return true
		}
	});
	$('#hzxxSave_ajaxForm').validateForm({
		beforeValidated: function() {
			return true
		}, beforeSubmit: function(h, g, f) {
			return true
		}
	})
	//限制样本类型
	limitYblx();
})