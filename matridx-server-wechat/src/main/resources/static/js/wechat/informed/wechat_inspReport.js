//初始化
var isButtonActive =  true;
var loadMoreSjysFlag = true;//加载更多送检医生标记
var loadMoreSjdwFlag = true;//加载更多送检单位标记
var pageFlag = "hzxxInfo";
var pageNumber = 0;
var pageSize = 15;
var loadingFlag = false;
//年龄单位列表
var nldwList = [{label: '岁',value: '岁'}, {label: '个月',value: '个月'}, {label: '天',value: '天'}, {label: '无',value: '无'}];
//科室
var ksxxlist;
var xzksList;
var dqksid;
//关注病原
var pathogenylist;
//快递类型
var sdlist;
var qtkdlx;
//检测单位
var zmmddlist;
var xzjcdwList
//送检区分
var divisionlist;
//检测项目
var detectlist;
var dqdetectid = "";
//科研项目
var kylist;
var xzkylist;
//检测子项目
var detectsublist;
//可选择检测单位
var unitlist;
var xzunitList;
var localids=[];
var navigationIndex = 0;
var sjjcxmDtos=[];
/**
 * 年龄单位改变事件
 */
function chooseNldw(){
	weui.picker(
		nldwList, {
			defaultValue: [$("#nldw").val()],
			onChange: function (result) {
				console.log(result);
			},
			onConfirm: function (result) {
				$("#nldw").val(result[0].value)
			},
			title: '请选择年龄单位'
		});
}

/**
 * 送检医生改变事件
 */
function changeSjys(){
	pageFlag = "sjysInfo";
	$("#hzxxInfo").hide();
	$("#sjysInfo").show();
	searchSjysList();
}

/**
 * 送检医生查询事件
 */
function searchSjysList(){
	loadMoreSjysFlag=true;
	$("#loading").show()
	$("#noInfo").hide();
	$("#loadComplete").hide();
	$("#sjysList").html("")
	loadMoreSjys()
}

/**
 * 送检医生懒加载事件
 */
function loadMoreSjys(){
	if(loadMoreSjysFlag){
		$.ajax({
			url :"/wechat/sjysxx/getSjysxxListInfo",
			type : "POST",
			dataType : "json",
			data : {
				"wxid" : $("#wxid").val(),
				"sjys" : $("#sjysCxnr").val()?$("#sjysCxnr").val():"",
			},
			success : function(data) {
				loadMoreSjysFlag = false
				if(data.sjysList.length>0){
					sjysHtmlConcat(data.sjysList);
				}else {
					$("#loading").hide()
					$("#noInfo").show();
					$("#loadComplete").hide();
				}
			},
			error : function(data) {
				$.toptip(JSON.stringify(date), 'error');
			}
		})
	}
}

/**
 * 送检医生页面拼接
 * @param list
 */
function sjysHtmlConcat(list){
	var html = '';
	var firstSjdw = '';
	for (var j = 0; j < list.length; j++) {
		if (!list[j].ysid) {
			if(j==0){
				firstSjdw = list[j].sjdw;
			}
			html += '<div class="weui-panel weui-panel_access" style="border-radius: 15px;">' +
				'        <div class="weui-panel__hd" style="padding: 5px 16px;" onclick="openOrClose(\''+list[j].sjdw+'\')">' +
				'			<div class="weui-cell" style="padding: 0">' +
				'           	<div class="weui-cell__bd" style="padding-left: 0">'+(list[j].sjdwjc?list[j].sjdwjc:(list[j].dwmc?list[j].dwmc:""))+'</div>' +
				'           	<span class="weui-cell__ft weui-icon-doubleUp iconUp iconUp'+list[j].sjdw+'"></span>' +
				'           	<span class="weui-cell__ft weui-icon-doubleDown iconDown iconDown'+list[j].sjdw+'""></span>' +
				'          </div>' +
				'		</div>' +
				'       <div class="weui-panel__bd">'+
				'          <div class="weui-media-box weui-media-box_text" style="padding: 0;">';
			for (var i = j+1; i < list.length; i++) {
				if (list[i].ysid){
					var jcdwmc = "";
					for (var h = 0; h < zmmddlist.length; h++) {
						if (zmmddlist[h].csid == list[i].jcdw) {
							jcdwmc = zmmddlist[h].csmc
							break
						}
					}
					html += '<div class="weui-cell weui-cell_swiped sjysListInfo '+list[j].sjdw+'" style="padding: 0;border-top: 1px solid rgb(222 222 222);">' +
						'            <div class="weui-cell__bd" style="transform: translate3d(0px, 0px, 0px);padding-left: 0" onclick="chooseSjys(\''+list[i].ysid+'\',\''+list[i].sjys+'\',\''+(list[i].ysdh?list[i].ysdh:"")+'\',\''+list[i].ks+'\',\''+list[i].qtks+'\',\''+list[i].sjdw+'\',\''+list[i].dwmc+'\',\''+list[i].sjdwbj+'\',\''+list[i].dbm+'\',\''+list[i].jcdw+'\')">' +
						'              <div class="weui-cell">' +
						'                <div class="weui-cell__bd" style="font-size: 15px;">'+list[i].sjys+'</div>' +
						'                <div class="weui-cell__bd" style="font-size: 15px;">'+list[i].qtks+'</div>' +
						'              </div>' +
						'            </div>' +
						'            <div class="weui-cell__ft">' +
						'              <a class="weui-swiped-btn weui-swiped-btn_default close-swipeout" style="color: black" onclick="sjysCopy(\''+list[i].ysid+'\',\''+list[i].sjys+'\',\''+(list[i].ysdh?list[i].ysdh:"")+'\',\''+list[i].ks+'\',\''+list[i].qtks+'\',\''+list[i].sjdw+'\',\''+list[i].dwmc+'\',\''+list[i].jcdw+'\',\''+list[i].dbm+'\')">复制</a>' +
						'              <a class="weui-swiped-btn weui-swiped-btn_warn delete-swipeout" onclick="delSjys(\''+list[i].ysid+'\')">删除</a>' +
						'            </div>' +
						'          </div>';
				}else {
					break;
				}
			}
			html += '		</div>' +
				'		</div>' +
				'	</div>'
		}
	}
	$("#loading").hide()
	$("#noInfo").hide();
	$("#loadComplete").show();
	$("#sjysList").append(html)
	$('.weui-cell_swiped').swipeout()
	if (!$("#sjysCxnr").val()){
		$(".sjysListInfo").hide();
		$(".iconUp").hide();
		$(".iconDown").show();
		openOrClose(firstSjdw)
	}else {
		$(".iconUp").show();
		$(".iconDown").hide();
	}
}

function openOrClose(dwid){
	if($("."+dwid).is(":hidden")){
		$("."+dwid).show()
		$(".iconDown"+dwid).hide()
		$(".iconUp"+dwid).show()
	}else {
		$("."+dwid).hide()
		$(".iconDown"+dwid).show()
		$(".iconUp"+dwid).hide()
	}

}
/**
 * 送检医生删除事件
 * @param ysid
 */
function delSjys(ysid){
	$.confirm({
		title: '警告',
		text: '<span style="color: red;">确认删除？</span>',
		onOK: function () {
			showloading("正在删除...");
			$.ajax({
				url:'/wechat/sjysxx/deleteSjysxx',
				type:'post',
				dataType:'json',
				data:{"ysid":ysid},
				success:function(data){
					hideloading();
					if(data.state=="true"){
						$.toptip('删除成功！', 'success');
						searchSjysList();
					}else if(data.state=="false"){
						$.toptip("删除失败！", 'error');
					}
				},
				error: function (data) {
					hideloading();
					$.toptip(JSON.stringify(date), 'error');
				}
			})
		},
		onCancel: function () {
			$.closeModal();
		}
	})
}

/**
 * 送检医生新增方法
 */
function sjysAdd(){
	pageFlag = "sjysAdd";
	$("#hzxxInfo").hide();
	$("#sjysInfo").hide();
	$("#sjysAdd").show();
	for (var i = 0; i < ksxxlist.length; i++) {
		if(ksxxlist[i].dwid == $("#sjysadd_ks").val()){
			if (ksxxlist[i].kzcs == '1'){
				$("#qtksInput").show()
			} else {
				$("#qtksInput").hide()
				$("#qtksInput #qtks").val("")
			}
		}
	}
	refreshSjysDB();
}

/**
 * 送检医生复制
 * @param ysid
 * @param ysmc
 * @param ysdh
 * @param ksid
 * @param ksmc
 * @param sjdwid
 * @param sjdwmc
 * @param jcdw
 * @param dbm
 */
function sjysCopy(ysid,ysmc,ysdh,ksid,ksmc,sjdwid,sjdwmc,jcdw,dbm){
	pageFlag = "sjysAdd";
	$("#hzxxInfo").hide();
	$("#sjysInfo").hide();
	$("#sjysAdd").show();
	$("#sjysAdd #sjysadd_ysxm").val(ysmc)
	$("#sjysAdd #sjdwadd_sjdwid").val(sjdwid)
	$("#sjysAdd #sjdwadd_sjdwmc").val(sjdwmc)
	$("#sjysAdd #sjysadd_ks").val(ksid)
	$("#sjysAdd #sjysadd_qtks").val(ksmc)
	$("#sjysAdd #sjysadd_jcdwid").val(jcdw)
	$("#sjysAdd #sjysadd_ysdh").val(ysdh)
	$("#sjysAdd #sjysadd_hzhb").val(dbm)
	for (var i = 0; i < zmmddlist.length; i++) {
		if (jcdw == zmmddlist[i].csid){
			$("#sjysAdd #sjysadd_jcdwmc").val(zmmddlist[i].csmc)
			break;
		}
	}
	for (var i = 0; i < ksxxlist.length; i++) {
		if(ksxxlist[i].dwid == $("#sjysadd_ks").val()){
			$("#ajaxForm #ksmc").val(ksxxlist[i].dwmc)
			if (ksxxlist[i].kzcs == '1'){
				$("#sjysadd_qtksInput").show()
				$("#sjysAdd #sjysadd_ksmc").val(ksxxlist[i].dwmc)
				$("#sjysAdd #sjysadd_qtks").val(ksmc);
				break;
			} else {
				$("#qtksInput").hide()
				$("#ksmc").val(ksmc);
				$("#qtksInput #qtks").val("")
			}
		}
	}
	refreshSjysDB();
}

/**
 * 送检医生保存
 */
function sjysSave(){
	if(!$("#sjysadd_ysxm").val() ){
		$.toptip('请填写主治医师!', 'error');
	}else if(!$("#sjdwadd_sjdwid").val()){
		$.toptip('请选择医院名称!', 'error');
	}else if(!$("#sjysadd_ks").val()){
		$.toptip("请选择科室!", 'error');
	}else if(!$("#sjysadd_jcdwid").val()){
		$.toptip("请选择检测单位!", 'error');
	}else if(!$("#sjysadd_hzhb").val()){
		$.toptip("请填写负责人!", 'error');
	}else{
		$("#sjysSave").attr("disabled","true");
		showloading("正在保存...");
		$.ajax({
			url:'/wechat/sjysxx/saveSjysxx',
			type:'post',
			dataType:'json',
			data:{
				"wxid" : $("#wxid").val(),
				"sjdw" : $("#sjdwadd_sjdwid").val(),
				"dwmc" : $("#sjdwadd_sjdwmc").val(),
				"ks" : $("#sjysadd_ks").val(),
				"sjys" : $("#sjysadd_ysxm").val(),
				"ysdh": $("#sjysadd_ysdh").val(),
				"dbm": $("#sjysadd_hzhb").val(),
				"qtks": $("#sjysadd_qtks").val(),
				"jcdw": $("#sjysadd_jcdwid").val(),
			},
			success:function(data){
				hideloading();
				$("#sjysSave").removeAttr("disabled");
				if(data.status=="success"){
					$.toptip(data.message, 'success');
					backSjysList();
				}else if(data.status=="fail"){
					$.toptip(data.message, 'error');
				}
			},
			error : function(data) {
				hideloading();
				$("#sjysSave").removeAttr("disabled");
				$.toptip(JSON.stringify(date), 'error');
			},
		})
	}
}

/**
 * 送检医生新增信息情况
 */
function clearSjyxaddInfo(){
	$("#sjysadd_ysxm").val("");
	$("#sjdwadd_sjdwmc").val("");
	$("#sjdwadd_sjdwid").val("");
	$("#sjysadd_ksmc").val("");
	$("#sjysadd_ks").val("");
	$("#sjysadd_qtks").val("");
	$("#sjysadd_jcdwmc").val("");
	$("#sjysadd_jcdwid").val("");
	$("#sjysadd_ysdh").val("");
	$("#sjysadd_hzhb").val("");
}

/**
 * 送检医生取消保存
 */
function backSjysList(){
	$("#hzxxInfo").hide();
	$("#sjysInfo").show();
	$("#sjysAdd").hide();
	$("#sjysCxnr").val("");
	searchSjysList()
	clearSjyxaddInfo()
}

/**
 * 送检医生选择事件
 * @param ysid 医生id
 * @param ysmc 医生名称
 * @param ysdh 医生电话
 * @param ksid 科室id
 * @param ksmc 科室名称
 * @param sjdwid 送检单位id
 * @param sjdwmc 送检单位名称
 * @param sjdwbj 送检单位标记（1为其他）
 * @param dbm 代表名，送检伙伴
 */
function chooseSjys(ysid,ysmc,ysdh,ksid,ksmc,sjdwid,sjdwmc,sjdwbj,dbm,jcdw){
	$("#sjysCxnr").val("")
	$("#sjys").val(ysid)
	$("#sjysmc").val(ysmc)
	$("#sjdwid").val(sjdwid)
	$("#sjdwmc").val(sjdwmc)
	$("#yymc").val(sjdwmc)
	for (var i = 0; i < ksxxlist.length; i++) {
		if(ksxxlist[i].dwid == ksid){
			$("#ajaxForm #ksmc").val(ksxxlist[i].dwmc)
			if (ksxxlist[i].kzcs == '1'){
				$("#qtksInput").show()
				$("#qtks").val(ksmc);
				break;
			} else {
				$("#qtksInput").hide()
				$("#qtksInput #qtks").val("")
			}
		}
	}
	if(sjdwbj=='1'){
		$("#qtdwInput").show();
		$("#sjdwbj").val(sjdwbj);
	}else {
		$("#qtdwInput").hide();
		$("#sjdwbj").val("");
	}
	$("#ksid").val(ksid)
	$("#ysdh").val(ysdh?ysdh:"")
	$("#db").val(dbm)
	$("#hzxxInfo").show();
	$("#sjysInfo").hide();
	$("#sjysAdd").hide();
	$("#sjdwInfo").hide();
	refreshUnit(jcdw);
}

/**
 * 送检单位选择事件
 */
function changeSjdw(){
	pageFlag = "sjdwInfo";
	$("#hzxxInfo").hide();
	$("#sjysInfo").hide();
	$("#sjysAdd").hide();
	$("#sjdwInfo").show();
	$("#sjdwCxnr").val("")
	searchSjdwList()
}

/**
 * 选择送检单位页面返回事件
 */
function sjdwBack(){
	pageFlag = "sjysAdd";
	$("#hzxxInfo").hide();
	$("#sjysInfo").hide();
	$("#sjysAdd").show();
	$("#sjdwInfo").hide();
}

/**
 * 送检单位查询事件
 */
function searchSjdwList(){
	loadMoreSjdwFlag=true;
	$("#sjdw_loading").show()
	$("#sjdw_noInfo").hide();
	$("#sjdw_loadComplete").hide();
	pageNumber = 0;
	$("#sjdwList").html("")
	loadMoreSjdw()
}

/**
 * 送检单位懒加载事件
 */
function loadMoreSjdw(){
	if(loadMoreSjdwFlag){
		pageNumber = pageNumber+1;
		$.ajax({
			url :"/wechat/hospital/pagedataListHospital",
			type : "POST",
			dataType : "json",
			data : {
				"searchParam" : $("#sjdwCxnr").val()?$("#sjdwCxnr").val():"",
				"sortName" : "yyxx.lrsj",
				"sortOrder" : "desc",
				"sortLastName" : "yyxx.lrsj",
				"sortLastOrder" : "asc",
				"pageNumber": pageNumber,
				"pageSize": pageSize,
				"dwFlag":"1"
			},
			success : function(data) {
				if(data.rows.length>0){
					if (data.rows.length<pageSize){
						loadMoreSjdwFlag = false
					}
					sjdwHtmlConcat(data.rows);
				}else {
					loadMoreSjdwFlag = false
					$("#sjdw_loading").hide()
					$("#sjdw_noInfo").show();
					$("#sjdw_loadComplete").hide();
				}
				loadingFlag = false;
			},
			error : function(data) {
				loadingFlag = false;
				$.toptip(JSON.stringify(date), 'error');
			}
		})
	}
}

/**
 * 送检单位html拼接
 * @param list
 */
function sjdwHtmlConcat(list){
	var html = 	'';
	for (var i = 0; i < list.length; i++) {
		html += '  <a class="weui-cell" style="padding-bottom: 0;padding-top: 0;color: black" onclick="chooseSjdw(\''+list[i].dwid+'\',\''+list[i].dwmc+'\',\''+list[i].cskz1+'\')">' +
			'    <div class="weui-cell__bd">' +
			'      <p style="margin: 10px 0;">'+list[i].dwmc+'</p>' +
			'    </div>' +
			'  </a>'

	}
	$("#sjdw_loading").hide()
	$("#sjdw_noInfo").hide();
	$("#sjdw_loadComplete").show();
	$("#sjdwList").append(html)
	// $('.weui-cell_swiped').swipeout()
}

/**
 * 送检单位选择事件
 * @param dwid
 */
function chooseSjdw(dwid,dwmc,cskz1){
	sjdwBack();
	$("#sjdwadd_sjdwid").val(dwid);
	$("#sjdwadd_sjdwmc").val(dwmc);
}

/**
 * 判断是否获取到微信id
 * @returns {boolean}
 */
function getWxOrNot (){
	var wxid = $("#wxid").val();
	if (wxid==null || wxid ==''){
		$.alert("没有获取到您的微信信息，请重新至公众号打开！")
		return false;
	}
	return true;
}

/**
 * 获取基础数据
 */
function getBasicData(){
	$.ajax({
		url:'/wechat/getInspBasicData',
		type:'post',
		dataType:'json',
		data:{
			"dbm" : $("#dbm").val()
		},
		success:function(data){
			ksxxlist = data.ksxxlist;
			pathogenylist = data.pathogenylist;
			sdlist = data.sdlist;
			zmmddlist = data.zmmddlist;
			divisionlist = data.divisionlist;
			detectlist = data.detectlist;
			kylist = data.kylist;
			detectsublist = data.detectsublist;
			unitlist = data.unitlist;
			dealBasicData()
		},
		error : function(data) {
			$.toptip(JSON.stringify(date), 'error');
		}
	})
}

/**
 * 获取检测项目
 */
function getSjjcxmDtos(){
	$.ajax({
		url:'/wechat/getSjjcxmDtos',
		type:'post',
		dataType:'json',
		data:{
			"sjid" : $("#sjid").val()
		},
		success:function(data){
			sjjcxmDtos = data.sjjcxmDtos;
		},
		error : function(data) {
			$.toptip(JSON.stringify(date), 'error');
		}
	})
}

/**
 * 处理基础数据
 */
function dealBasicData(){
	//科室
	xzksList = [];
	for (var i = 0; i < ksxxlist.length; i++) {
		var lsks = {label:ksxxlist[i].dwmc,value:ksxxlist[i].dwid};
		xzksList.push(lsks)
	}
	//初始化报告显示科室输入框
	for (var i = 0; i < ksxxlist.length; i++) {
		if(ksxxlist[i].dwid == $("#ksid").val()){
			$("#ajaxForm #ksmc").val(ksxxlist[i].dwmc)
			if (ksxxlist[i].kzcs == '1'){
				$("#qtksInput").show()
			} else {
				$("#qtksInput").hide()
				$("#qtksInput #qtks").val("")
			}
		}
	}

	//检测项目、初始化检测子项目
	var html='';
	var dqdetectcskz = "";
	var oncoFlag=false;
	var tNGSFlag=false;
	$("#jcxmids").val($("#jcxmids").val().replace("[","").replace("]",""));
	for (var i = 0; i < detectlist.length; i++) {
		var xzFlag=false;
		for(var j = 0; j < detectlist.length; j++){
			if($("#jcxmids").val().indexOf(detectlist[j].csid)!=-1){
				var cskz3=detectlist[j].cskz3;
				var cskz4=detectlist[j].cskz4;
				if(cskz3==detectlist[i].cskz3&&cskz4==detectlist[i].cskz4){
					xzFlag=true;
				}
				if(cskz3=='IMP_REPORT_ONCO_QINDEX_TEMEPLATE'&&cskz4=='Q'){
					oncoFlag=true;
				}
				if(cskz3=='IMP_REPORT_TNGS_TEMEPLATE'&&cskz4=='T'){
					tNGSFlag=true;
				}else if(cskz3.indexOf('IMP_REPORT_SEQ_TNGS') > -1 && cskz4=='K'){
					tNGSFlag=true;
				}
			}
		}
		if(oncoFlag){
			if(detectlist[i].cskz3=='IMP_REPORT_ONCO_QINDEX_TEMEPLATE'&&detectlist[i].cskz4=='Q'){
				xzFlag=true;
			}
		}
		if(tNGSFlag){
			if(detectlist[i].cskz3=='IMP_REPORT_TNGS_TEMEPLATE'&&detectlist[i].cskz4=='T'){
				xzFlag=true;
			}else if(detectlist[i].cskz3.indexOf('IMP_REPORT_SEQ_TNGS')>-1&&detectlist[i].cskz4=='K'){
				xzFlag=true;
			}
		}
		if($("#jcxmids").val().indexOf(detectlist[i].csid)!=-1){
			dqdetectid += ","+detectlist[i].csid
			dqdetectcskz += ","+detectlist[i].cskz1
			html += '<span class="RadioStyle">';
			html+='<input type="checkbox" id="'+detectlist[i].csid+'" name="jcxm" value="'+detectlist[i].csid+'" csmc="'+detectlist[i].csmc+'" cskz1="'+detectlist[i].cskz1+'"  cskz3="'+detectlist[i].cskz3+'"  cskz4="'+detectlist[i].cskz4+'" checked onclick=\'changeJcxm("'+detectlist[i].csid+'");\'  validate="{required:true}"/>';
			html +='<label for="'+detectlist[i].csid+'" style="font-size: small;background-color: '+detectlist[i].cskz2+'">'+detectlist[i].csmc+'</label>';
			html +='</span>';
		}else{
			if(!xzFlag){
				html += '<span class="RadioStyle">';
				html+='<input type="checkbox" id="'+detectlist[i].csid+'" name="jcxm" value="'+detectlist[i].csid+'" csmc="'+detectlist[i].csmc+'"  cskz1="'+detectlist[i].cskz1+'"  cskz3="'+detectlist[i].cskz3+'"  cskz4="'+detectlist[i].cskz4+'" onclick=\'changeJcxm("'+detectlist[i].csid+'");\'  validate="{required:true}"/>';
				html +='<label for="'+detectlist[i].csid+'" style="font-size: small;background-color: '+detectlist[i].cskz2+'">'+detectlist[i].csmc+'</label>';
				html +='</span>';
			}
		}
	}
	$("#jcxm").empty();
	$("#jcxm").append(html);
	dqdetectid = dqdetectid.substring(1);
	dqdetectcskz = dqdetectcskz.substring(1);
	// 由于Res项目，将判断改为如下 if(dqdetectcskz.indexOf("F")!=-1 || dqdetectcskz.indexOf("Z")!=-1){
	getSubDetectByDetect()
	//报告显示单位
	if($("#sjdwbj").val()=="1"){
		$("#qtdwInput").show()
	}else {
		$("#qtdwInput").hide()
	}

	//检测单位
	xzunitList = [];
	if (unitlist.length > 0){
		for (var i = 0; i < unitlist.length; i++) {
			var lsuniy = {label:unitlist[i].csmc,value:unitlist[i].csid};
			xzunitList.push(lsuniy)
			if($("#jcdwid").val() && $("#jcdwid").val() == unitlist[i].csid){
				$("#jcdwmc").val(unitlist[i].csmc)
			}
		}
	}
	if (unitlist.length == 1) {
		$("#jcdwmc").val(unitlist[0].csmc);
		$("#jcdwid").val(unitlist[0].csid);
	}
	//检测单位
	xzjcdwList = [];
	if (zmmddlist.length > 0){
		for (var i = 0; i < zmmddlist.length; i++) {
			var lsjcdw = {label:zmmddlist[i].csmc,value:zmmddlist[i].csid};
			xzjcdwList.push(lsjcdw)
		}
	}

	//快递类型
	for (i = 0; i < sdlist.length; i++) {
		if (sdlist[i].cskz2 == "1") {
			qtkdlx = sdlist[i].csid;
			break
		}
	}
	initKdlx()

	//送检单位
	var dqzmmdd;
	if (zmmddlist.length > 0) {
		for (var j = 0; j < zmmddlist.length; j++) {
			if ($("#sjdw").val() == zmmddlist[j].dwid){
				dqzmmdd = zmmddlist[j]
			}
		}
	}
	if($("#sjdwbj").val()=="1"){
		$("#qtdwInput").show()
	}else {
		$("#qtdwInput").hide()
	}
	//送检区分、科研项目
	if ($("#ybbh").val()==null || $("#ybbh").val()=='' ){
		initSjqf()
	}else {
		if ($("#ybbh").val()==null || $("#ybbh").val()=='' ){
			initSjqf()
		}else {
			var kyxmid = $("#ajaxForm #kyxm").val();
			var sjqf = $("#ajaxForm input:radio[name='sjqf']:checked").val();
			xzkylist = [];
			if(sjqf){
				for (var i = 0; i < kylist.length; i++) {
					if(kylist[i].fcsid == sjqf){
						var lskyxm = {label:kylist[i].csmc,value:kylist[i].csid};
						xzkylist.push(lskyxm)
						if(kyxmid == kylist[i].csid){
							$("#ajaxForm #kyxmmc").val(kylist[i].csmc)
						}
					}
				}
			}
			if(xzkylist.length>0){
				$("#kyxmDiv").show();
			}else{
				$("#ajaxForm #kyxm").val("");
				$("#ajaxForm #kyxmmc").val("");
				$("#kyxmDiv").hide();
			}
		}
	}
}

function changeJcxm(jcxmid){
	var limitFlag=false;
	if(sjjcxmDtos!=null&&sjjcxmDtos.length>0){
		for(var i=0;i<sjjcxmDtos.length;i++){
			if(sjjcxmDtos[i].jcxmid==jcxmid){
				if(sjjcxmDtos[i].sfje&&parseFloat(sjjcxmDtos[i].sfje)>0){
					limitFlag=true;
				}
			}
		}
	}
	if(limitFlag){
		$("#ajaxForm input[name='jcxm'][id='"+jcxmid+"']").prop("checked",true);
		$.toptip('当前检测项目已付款，不允许修改!', 'error');
		return false;
	}
	var str='';
	var jcxmids='';
	$("#ajaxForm input[name='jcxm']").each(function(){
		var flag=$(this).prop("checked");
		if(flag){
			str += ","+$(this).attr("cskz1");
			jcxmids += ","+$(this).val();
		}
	});
	if(jcxmids){
		jcxmids = jcxmids.substring(1);
	}
	$("#jcxmids").val(jcxmids)
	var html=''
	var oncoFlag=false;
	var tNGSFlag=false;
	for (var i = 0; i < detectlist.length; i++) {
		var xzFlag=false;
		$("#ajaxForm input[name='jcxm']").each(function(){
			var flag=$(this).prop("checked");
			if(flag){
				var cskz3=$(this).attr("cskz3");
				var cskz4=$(this).attr("cskz4");
				if(cskz3==detectlist[i].cskz3&&cskz4==detectlist[i].cskz4){
					xzFlag=true;
				}
				if(cskz3=='IMP_REPORT_ONCO_QINDEX_TEMEPLATE'&&cskz4=='Q'){
					oncoFlag=true;
				}
				if(cskz3=='IMP_REPORT_TNGS_TEMEPLATE'&&cskz4=='T'){
					tNGSFlag=true;
				}else if(cskz3.indexOf('IMP_REPORT_SEQ_TNGS')>-1&&cskz4=='K'){
					tNGSFlag=true;
				}
			}
		});
		if(oncoFlag){
			if(detectlist[i].cskz3=='IMP_REPORT_ONCO_QINDEX_TEMEPLATE'&&detectlist[i].cskz4=='Q'){
				xzFlag=true;
			}
		}
		if(tNGSFlag){
			if(detectlist[i].cskz3=='IMP_REPORT_TNGS_TEMEPLATE'&&detectlist[i].cskz4=='T'){
				xzFlag=true;
			}else if(detectlist[i].cskz3.indexOf('IMP_REPORT_SEQ_TNGS')>-1&&detectlist[i].cskz4=='K'){
				xzFlag=true;
			}
		}
		if(jcxmids.indexOf(detectlist[i].csid)!=-1){
			html += '<span class="RadioStyle">';
			html+='<input type="checkbox" id="'+detectlist[i].csid+'" name="jcxm" value="'+detectlist[i].csid+'" csmc="'+detectlist[i].csmc+'" cskz1="'+detectlist[i].cskz1+'"  cskz3="'+detectlist[i].cskz3+'"  cskz4="'+detectlist[i].cskz4+'" checked onclick=\'changeJcxm("'+detectlist[i].csid+'");\' validate="{required:true}"/>';
			html +='<label for="'+detectlist[i].csid+'" style="font-size: small;background-color: '+detectlist[i].cskz2+'">'+detectlist[i].csmc+'</label>';
			html +='</span>';
		}else{
			if(!xzFlag){
				html += '<span class="RadioStyle">';
				html+='<input type="checkbox" id="'+detectlist[i].csid+'" name="jcxm" value="'+detectlist[i].csid+'" csmc="'+detectlist[i].csmc+'"  cskz1="'+detectlist[i].cskz1+'"  cskz3="'+detectlist[i].cskz3+'"  cskz4="'+detectlist[i].cskz4+'"onclick=\'changeJcxm("'+detectlist[i].csid+'");\'  validate="{required:true}"/>';
				html +='<label for="'+detectlist[i].csid+'" style="font-size: small;background-color: '+detectlist[i].cskz2+'">'+detectlist[i].csmc+'</label>';
				html +='</span>';
			}
		}
	}
	$("#jcxm").empty();
	$("#jcxm").append(html);
	getSubDetectByDetect()
}


function changeJczxm(jczxmid){
	var limitFlag=false;
	if(sjjcxmDtos!=null&&sjjcxmDtos.length>0){
		for(var i=0;i<sjjcxmDtos.length;i++){
			if(sjjcxmDtos[i].jczxmid==jczxmid){
				if(sjjcxmDtos[i].sfje&&parseFloat(sjjcxmDtos[i].sfje)>0){
					limitFlag=true;
				}
			}
		}
	}
	if(limitFlag){
		$("#ajaxForm input[name='jczxm'][id='"+jczxmid+"']").prop("checked",true);
		$.toptip('当前检测子项目已付款，不允许修改!', 'error');
		return false;
	}
	var jczxmids='';
	$("#ajaxForm input[name='jczxm']").each(function(){
		var flag=$(this).prop("checked");
		if(flag){
			jczxmids += ","+$(this).val();
		}
	});
	if(jczxmids){
		jczxmids = jczxmids.substring(1);
	}
	$("#jczxmids").val(jczxmids);
}

/**
 * 科室改变事件
 * @param flag
 */
function chooseKs(flag){
	var oldks = $("#"+(flag?"ksid":"sjysadd_ks")).val()?$("#"+(flag?"ksid":"sjysadd_ks")).val():xzksList[0].value
	weui.picker(
		xzksList, {
			defaultValue: [oldks],
			onChange: function (result) {
				console.log(result);
			},
			onConfirm: function (result) {
				$("#"+(flag?"ksmc":"sjysadd_ksmc")).val(result[0].label);
				$("#"+(flag?"ksid":"sjysadd_ks")).val(result[0].value);
				for (var i = 0; i < ksxxlist.length; i++) {
					if(ksxxlist[i].dwid == result[0].value){
						if (ksxxlist[i].kzcs == '1'){
							$("#"+(flag?"qtksInput":"sjysadd_qtksInput")).show();
							if (oldks != result[0].value){
								$("#"+(flag?"qtks":"sjysadd_qtks")).val(result[0].label)
							}
						} else {
							$("#"+(flag?"qtksInput":"sjysadd_qtksInput")).hide()
							$("#"+(flag?"qtks":"sjysadd_qtks")).val("")
						}
					}
				}
			},
			title: '请选择科室'
		});
}

/**
 * 检测单位改变事件
 * @param flag
 */
function chooseJcdw(flag){
	setTimeout(function(){
		weui.picker(
			flag?xzunitList:xzjcdwList, {
				defaultValue: [(flag?($("#jcdwid").val()?$("#jcdwid").val():xzunitList[0].value):($("#sjysadd_jcdwid").val()?$("#sjysadd_jcdwid").val():xzjcdwList[0].value))],
				onChange: function (result) {
					console.log(result);
				},
				onConfirm: function (result) {
					$("#"+(flag?"jcdwmc":"sjysadd_jcdwmc")).val(result[0].label);
					$("#"+(flag?"jcdwid":"sjysadd_jcdwid")).val(result[0].value);
				},
				title: '请选择检测单位'
			});
		}, 200);
}



/**
 * 科研项目改变事件
 */
function chooseKyxm(){
	weui.picker(
		xzkylist, {
			defaultValue: [$("#kyxm").val()?$("#kyxm").val():xzkylist[0].value],
			onChange: function (result) {
				console.log(result);
			},
			onConfirm: function (result) {
				$("#kyxmmc").val(result[0].label);
				$("#kyxm").val(result[0].value);
			},
			title: '请选择立项编号'
		});
}


/**
 * 根据检测项目筛选检测子项目
 * @param jcxmdm
 */
function getSubDetectByDetect(){
	var zxmHtml='';
	var jcxmids = $("#jcxmids").val();
	var jcxmlist = jcxmids.split(",");
	var zxmHtml='';
	for (var i = 0; i < detectsublist.length; i++) {
		for(var j = 0; j < jcxmlist.length; j++){
			if(detectsublist[i].fcsid==jcxmlist[j]){
				if($("#jczxmids").val().indexOf(detectsublist[i].csid)!=-1){
					zxmHtml += '<span class="RadioStyle">';
					zxmHtml +='<input type="checkbox" id="'+detectsublist[i].csid+'" name="jczxm" fcsid="'+detectsublist[i].fcsid+'" value="'+detectsublist[i].csid+'" csmc="'+detectsublist[i].csmc+'" cskz1="'+detectsublist[i].cskz1+'"  cskz3="'+detectsublist[i].cskz3+'"  cskz4="'+detectsublist[i].cskz4+'" checked onclick=\'changeJczxm("'+detectsublist[i].csid+'");\' validate="{required:true}"/>';
					zxmHtml +='<label fcsid="'+detectsublist[i].fcsid+'" for="'+detectsublist[i].csid+'" style="font-size: small;background-color: '+detectsublist[i].cskz2+'">'+detectsublist[i].csmc+'</label>';
					zxmHtml +='</span>';
				}else{
					zxmHtml += '<span class="RadioStyle">';
					zxmHtml +='<input type="checkbox" id="'+detectsublist[i].csid+'" name="jczxm" fcsid="'+detectsublist[i].fcsid+'" value="'+detectsublist[i].csid+'" csmc="'+detectsublist[i].csmc+'" cskz1="'+detectsublist[i].cskz1+'"  cskz3="'+detectsublist[i].cskz3+'"  cskz4="'+detectsublist[i].cskz4+'" onclick=\'changeJczxm("'+detectsublist[i].csid+'");\' validate="{required:true}"/>';
					zxmHtml +='<label fcsid="'+detectsublist[i].fcsid+'" for="'+detectsublist[i].csid+'" style="font-size: small;background-color: '+detectsublist[i].cskz2+'">'+detectsublist[i].csmc+'</label>';
					zxmHtml +='</span>';
				}
			}
		}
	}
	if (zxmHtml){
		$("#jczxmDiv").show();
		$("#jczxmDiv").attr("style","align-items: flex-start; display: flex;");
		$("#jczxm").empty();
		$("#jczxm").append(zxmHtml);
	} else {
		$("#jczxm").empty();
		$("#jczxmDiv").hide()
	}
}


/**
 * 初始化送检区分
 */
function initSjqf(){
	var db = $("#ajaxForm #db").val();
	if (db.length>=4){
		db = db.substring(db.length-2,db.length);
	}
	var length = $("#ajaxForm #sjqfGroup").length;
	if (length>0){
		for (var i = 0; i < length; i++) {
			var id = $("#ajaxForm #sjqfGroup input")[i].id;
			if ($("#ajaxForm #sjqfGroup")[i].innerText.replace(" ","")=="特检"){
				$("#ajaxForm #sjqfGroup").removeAttr("checked")
				$("#ajaxForm #sjqfGroup #"+id).prop("checked", true);
			}

			// if ($("#ajaxForm #sjqfGroup")[i].innerText.replace(" ","")=="特检"){
			// 	$("#ajaxForm #sjqfGroup").removeAttr("checked")
			// 	$("#ajaxForm #sjqfGroup #"+id).prop("checked", true);
			// }

			if ($("#ajaxForm #sjqfGroup")[i].innerText.replace(" ","")==db){
				$("#ajaxForm #sjqfGroup").removeAttr("checked")
				$("#ajaxForm #sjqfGroup #"+id).prop("checked", true);
				//根据送检区分初始化科研项目
				initKyxm();
				return
			}
		}
	}
	//根据送检区分初始化科研项目
	initKyxm();
}

/**
 * 初始化快递类型
 */
function initKdlx(){
	if ($("#ajaxForm #actionFlag").val() == '1'){
		$("#ajaxForm #kdhspan").text("*");
	}else {
		$("#ajaxForm #kdhspan").text(" ");
	}
    $("#ajaxForm #kdh").removeAttr("validate");
	$("#ajaxForm #kdh").removeAttr("placeholder");
	if ($('input:radio[name="kdlx"]:checked').attr("data-csdm") == "QYY"){
		$("#ajaxForm #kdhname").text("医检收样员")
		$("#ajaxForm #kdh").attr("placeholder","请填写医检收样员姓名");
		$("#ajaxForm #kdh").attr("validate","{stringMaxLength:32}");
		$("#ajaxForm #kddhQRCode").hide();
	}else {
		$("#ajaxForm #kdhname").text("快递单号")
		$("#ajaxForm #kdh").attr("placeholder","请填写快递单号");
		$("#ajaxForm #kdh").attr("validate","{required:true, stringMaxLength:32}");
		$("#ajaxForm #kddhQRCode").show();
	}
}

/**
 * 页面初始化
 */
function initPage(){
	//初始化基础数据
	getBasicData();
	if($("#sjid").val()){
		getSjjcxmDtos();
	}
}

//事件绑定
function btnBind() {
	//快递类型改变事件
	$("#ajaxForm input[type=radio][name=kdlx]").change(function(e) {
		initKdlx()
	});

	//暂存功能
	$('#ajaxForm #info_save').unbind("click").click(function(){
		if (!isButtonActive){
			return false;
		}
		buttonDisabled(true,false,"",true);
		if(!getWxOrNot()){
			return buttonDisabled(false,false,"",false);
		}
		if (!checkYbbh()){
			return false;
		}
		if(!$("#ajaxForm #hzxm").val()){
			return buttonDisabled(false,true,"请填写患者姓名!",false);
		}
		$("#ajaxForm #db").val($("#ajaxForm #db").val().replace(/\s+/g,""));
		$("#ajaxForm #db").val(DBC2SBC($("#ajaxForm #db").val()));
		$("#ajaxForm #sjys").val($("#ajaxForm #sjys").val().replace(/\s+/g,""));
		$("#ajaxForm #ybbh").val($("#ajaxForm #ybbh").val().replace(/\s+/g,""));

		//性别
		var lsxb = ($("#ajaxForm input:radio[name='xb']:checked").val()=="" || $("#ajaxForm input:radio[name='xb']:checked").val()==null)?"":$("#ajaxForm input:radio[name='xb']:checked").val();
		//年龄+年龄单位
		var lsnl = ($("#ajaxForm #nl").val()?$("#ajaxForm #nl").val():"");
		var lsnldw = ($("#ajaxForm #nldw").val()?$("#ajaxForm #nldw").val():"岁");
		//联系电话
		var lslxdh = ($("#ajaxForm #dh").val()?$("#ajaxForm #dh").val():"");
		//送检医生
		var lssjys = ($("#ajaxForm #sjys").val()?$("#ajaxForm #sjys").val():"");
		//医生电话
		var lsysdh = ($("#ajaxForm #ysdh").val()?$("#ajaxForm #ysdh").val():"");
		//送检单位
		var lssjdw = ($("#ajaxForm #sjdwid").val()?$("#ajaxForm #sjdwid").val():"");
		//送检单位名称
		var lssjdwmc = ($("#ajaxForm #sjdwmc").val()?$("#ajaxForm #sjdwmc").val():"");
		//科室
		var lsks = ($("#ajaxForm #ksid").val()?$("#ajaxForm #ksid").val():"");
		//其他科室
		var lsqtks = (!$("#ajaxForm #qtks").is(":hidden")?($("#qtksInput #qtks").val()?$("#qtksInput #qtks").val():""):"");
		//检测单位
		var lsjcdw = ($("#ajaxForm #jcdwid").val()?$("#ajaxForm #jcdwid").val():"");
		//科研项目
		var lskyxm =  (!$("#ajaxForm #kyxmDiv").is(":hidden"))?($("#ajaxForm #kyxm").val()?$("#ajaxForm #kyxm").val():""):"";
		//合作伙伴
		var lsdb = ($("#ajaxForm #db").val()?$("#ajaxForm #db").val():"");
		//快递类型
		var lskdlx = $("#ajaxForm input:radio[name='kdlx']:checked").val()?$("#ajaxForm input:radio[name='kdlx']:checked").val():"";
		//快递号
		var lskdh = $("#ajaxForm #kdh").val()?$("#ajaxForm #kdh").val():"";
		if(lskdh && lskdh.length>32){
            return buttonDisabled(false,true,"请填写正确的快递号!",false);
		}
		//检测项目
		var lsjcxmids = ($("#ajaxForm #jcxmids").val().replace("[","").replace("]",""))?($("#ajaxForm #jcxmids").val().replace("[","").replace("]","")):"";
		//检测子项目
		var lsjczxm = (!$("#ajaxForm #jczxmDiv").is(":hidden"))?($("#ajaxForm #jczxmids").val()?$("#ajaxForm #jczxmids").val():""):""
		var jcxmmc="";
		var json=[];
		$("#ajaxForm input[name='jcxm']").each(function(){
			var flag=$(this).prop("checked");
			if(flag){
				jcxmmc+=","+$(this).attr("csmc");
				var hasSub=false;
				for (var i = 0; i < detectsublist.length; i++) {
					if($(this).val()==detectsublist[i].fcsid){
						hasSub=true;
						break;
					}
				}
				if(!hasSub){
					var sz={"jcxmid":$(this).val(),"jczxmid":"","xmglid":""};
					json.push(sz);
				}
			}
		});
		$("#ajaxForm input[name='jczxm']").each(function(){
			var flag=$(this).prop("checked");
			if(flag){
				var sz={"jcxmid":$(this).attr("fcsid"),"jczxmid":$(this).val(),"xmglid":""};
				json.push(sz);
			}
		});

		if(sjjcxmDtos!=null&&sjjcxmDtos.length>0){
			for(var i=0;i<json.length;i++){
				for(var j=0;j<sjjcxmDtos.length;j++){
					if(json[i].jcxmid==sjjcxmDtos[j].jcxmid){
						if(json[i].jczxmid){
							if(json[i].jczxmid==sjjcxmDtos[j].jczxmid){
								json[i].xmglid=sjjcxmDtos[j].xmglid;
							}
						}else{
							json[i].xmglid=sjjcxmDtos[j].xmglid;
						}
					}
				}
			}
		}
		console.log(JSON.stringify(json));
		//送检区分
		var lssjqf = $("#ajaxForm input:radio[name='sjqf']:checked").val()?$("#ajaxForm input:radio[name='sjqf']:checked").val():"";
		//关注病原
		var lsbys = ""
		var bysArr = document.getElementsByName("bys");
		if(bysArr && bysArr.length>0){
			for (var i = 0; i < bysArr.length; i++) {
				if (bysArr[i].checked) {
					lsbys += ","+bysArr[i].value;
				}
			}
			lsbys = lsbys.substring(1)
		}
		dealSfwsFlag()
		showloading("正在保存...");
		$.ajax({
			type:'post',
			url:"/wechat/miniprogram/saveInspectFirst",
			cache: false,
			data: {
				"wxid":$("#wxid").val(),
				"sjid":($("#sjid").val()?$("#sjid").val():""),
				"ywlx":$("#ywlx").val(),
				"status":($("#status").val()?$("#status").val():""),
				"fjids":$("#fjids").val()?$("#fjids").val():"",
				"ybbh":$("#ajaxForm #ybbh").val(),
				"hzxm":$("#ajaxForm #hzxm").val(),
				"xb":lsxb,
				"nl":lsnl,
				"nldw":lsnldw,
				"dh":lslxdh,
				"sjys":$("#sjysmc").val()?$("#sjysmc").val():"",
				"ysdh":lsysdh,
				"sjdw":lssjdw,
				"yymc":lssjdwmc,
				"sjdwmc":lssjdwmc,
				"ks":lsks,
				"qtks":lsqtks,
				"jcdw":lsjcdw,
				"kyxm":lskyxm,
				"kdlx":lskdlx,
				"kdh":lskdh,
				"jcxmids":lsjcxmids,
				"jcxm"	:JSON.stringify(json),
				"db":lsdb,
				"sjqf":lssjqf,
				"bys":lsbys,
				"zyh":$("#zyh").val()?$("#zyh").val():"",
				"cwh":$("#cwh").val()?$("#cwh").val():"",
				"sfws":$("#ajaxForm #sfws").val(),//是否完善标记
				"xgsj":$("#ajaxForm #xgsj").val(),//页面打开时间
				"jcxmmc":jcxmmc?jcxmmc.substring(1):"",
			},
			dataType:'json',
			success:function(result){
				hideloading();
				buttonDisabled(false,false,"",true);
				if(result.status == "success"){
					if(result.sjxxDto){
						$("#ajaxForm #sjid").val(result.sjxxDto.sjid);
					}
					refreshFile();
					refreshFj(result.fjcfbDtos);
					$.alert("未完善数据请至“录入清单”中补充");
				}else{
					$.closeModal();
					$.toptip(result.message, "error");
				}
			},
			error: function (data) {
				hideloading();
				buttonDisabled(false,true,JSON.stringify(data),false);
			}

		})
		return true;
	});

	//初始化微信信息
	$.ajax({
		url: '/wechat/getJsApiInfo',
		type: 'post',
		data: {
			"url":location.href.split('#')[0],
			"wbcxdm":$("#wbcxdm").val()
		},
		dataType: 'json',
		success: function(result) {
			//注册信息
			wx.config({
				debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
				appId: result.appid, // 必填，公众号的唯一标识
				timestamp: result.timestamp, // 必填，生成签名的时间戳
				nonceStr: result.noncestr, // 必填，生成签名的随机串
				signature: result.sign, // 必填，签名，见附录1
				jsApiList: ['checkJsApi','scanQRCode','startRecord','stopRecord','onVoiceRecordEnd','playVoice','pauseVoice','stopVoice','onVoicePlayEnd','uploadVoice','downloadVoice','translateVoice','chooseImage','previewImage','uploadImage','downloadImage']
				// 必填，需要使用的JS接口列表，所有JS接口列表见附录2
			});
			wx.error(function(res) {
				console.log(res);
			});
			//config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作
			wx.ready(function(res) {
				wx.checkJsApi({
					jsApiList: ['scanQRCode','startRecord','stopRecord','onVoiceRecordEnd','playVoice','pauseVoice','stopVoice','onVoicePlayEnd','uploadVoice','downloadVoice','translateVoice','chooseImage','previewImage','uploadImage','downloadImage'],
					success: function(res) {
						// var btnElem=document.getElementById("getaudio");//获取ID
						var posStart = 0;//初始化起点坐标
						var posEnd = 0;//初始化终点坐标
						var posMove = 0;//初始化滑动坐标
						var localId;//返回音频的本地ID
						var serverId;//返回音频的服务器端ID
						/*btnElem.addEventListener("touchstart", function(event) {
							event.preventDefault();//阻止浏览器默认行为
							//开始录音
							wx.startRecord();
							$("#ajaxForm #audiopoint").text("录音中，上滑可取消");
							$("#ajaxForm #audiodiv").attr("style","position: fixed ! important;right: 25%; bottom: 30%; width:50%;height:40%;display:block");
							posStart = 0;
							posStart = event.touches[0].pageY;//获取起点坐标
							btnElem.value = '松开结束';
						});
						btnElem.addEventListener("touchmove", function(event) {
							event.preventDefault();//阻止浏览器默认行为
							posMove = 0;
							posMove = event.targetTouches[0].pageY;//获取滑动实时坐标
							if(posStart - posMove < 100){
								btnElem.value = '松开结束';
								$("#ajaxForm #audiopoint").text("录音中，上滑取消");
							}else{
								btnElem.value = '松开取消';
								$("#ajaxForm #audiopoint").text("录音中，松开取消");
							}
						});
						btnElem.addEventListener("touchend", function(event) {
							event.preventDefault();//阻止浏览器默认行为
							$("#ajaxForm #audiodiv").attr("style","display:none");
							posEnd = 0;
							posEnd = event.changedTouches[0].pageY;//获取终点坐标
							btnElem.value = '语音输入';
							//停止录音接口
							wx.stopRecord({
								success: function (res) {
									localId = res.localId;
									if(posStart - posMove < 100){
										//语音识别
										wx.translateVoice({
											localId: localId, // 需要识别的音频的本地Id，由录音相关接口获得
											isShowProgressTips: 1, // 默认为1，显示进度提示
											success: function (res) {
												var String = res.translateResult;
												if(String==null || String=="")
													return;
												var textlist = String.split(/[.]|,|。|，/);
												for (var i = 0; i < textlist.length; i++) {
													var text = textlist[i];
													if(text.indexOf("姓名是")>-1 || text.indexOf("姓名为")>-1){
														$("#ajaxForm #hzxm").val(text.substr(3));
													}else if(text.indexOf("性别是")>-1 || text.indexOf("性别为")>-1){
														var sex = text.substr(3);
														if(sex=="男"){
															$("input:radio[value='1']").attr('checked','true');
														}else if(sex=="女"){
															$("input:radio[value='2']").attr('checked','true');
														}else{
															$("input:radio[value='3']").attr('checked','true');
														}
													}else if(text.indexOf("年龄是")>-1 || text.indexOf("年龄为")>-1){
														var nl=zhDigitToArabic(text.substr(3));
														$("#ajaxForm #nl").val(nl);
													}else if(text.indexOf("医生电话是")>-1 || text.indexOf("医生电话为")>-1){
														var ysdh=phoneNumber(text.substr(5));
														$("#ajaxForm #ysdh").val(ysdh);
													}else if(text.indexOf("电话是")>-1 || text.indexOf("电话为")>-1){
														var dh=phoneNumber(text.substr(3));
														$("#ajaxForm #dh").val(dh);
													}else if(text.indexOf("送检医生是")>-1 || text.indexOf("送检医生为")>-1){
														$("#ajaxForm #sjys").val(text.substr(5));
													}else if(text.indexOf("送检单位是")>-1 || text.indexOf("送检单位为")>-1){
														$("#ajaxForm #sjdw").val(text.substr(5));
													}else if(text.indexOf("住院号是")>-1 || text.indexOf("住院号为")>-1){
														$("#ajaxForm #zyh").val(text.substr(4));
													}else if(text.indexOf("床位号是")>-1 || text.indexOf("床位号为")>-1){
														$("#ajaxForm #cwh").val(text.substr(4));
													}else if(text.indexOf("科室是")>-1 || text.indexOf("科室为")>-1){
														$("#ajaxForm input[name='ks']").each(function(){
															if($(this).attr("dwmc") == text.substr(3)){
																$(this).attr('checked','true');
															}
														});
													}else if(text.indexOf("年龄单位是")>-1 || text.indexOf("年龄单位为")>-1){
														$("#ajaxForm #nldw option").each(function(){
															if($(this).text() == text.substr(5)){
																$(this).attr("selected","selected");
															}
														});
													}else if(text.indexOf("检测单位是")>-1 || text.indexOf("检测单位为")>-1){
														$("#ajaxForm input[name='jcdw'][placeholder='"+text.substr(5)+"']").attr("checked", true);
														$("#ajaxForm input[name='jcdw']").each(function(){
															if($(this).attr("jcdwmc") == text.substr(5)){
																$(this).attr('checked','true');
															}
														});
													}else if(text.indexOf("合作伙伴是")>-1 || text.indexOf("合作伙伴为")>-1){
														$("#ajaxForm #db").val(text.substr(5));
													}else if(text.indexOf("快递类型是")>-1 || text.indexOf("快递类型为")>-1){
														$("#ajaxForm input[name='kdlx'][placeholder='"+text.substr(5)+"']").attr("checked", true);
														$("#ajaxForm input[name='kdlx']").each(function(){
															if($(this).attr("kdlxmc") == text.substr(5)){
																$(this).attr('checked','true');
															}
														});
													}else if(text.indexOf("快递号是")>-1 || text.indexOf("快递号为")>-1){
														$("#ajaxForm #kdh").val(text.substr(4));
													}else if(text.indexOf("检测项目是")>-1 || text.indexOf("检测项目为")>-1){
														$("#ajaxForm input[name='jcxmids'][placeholder='"+text.substr(5)+"']").attr("checked", true);
														$("#ajaxForm input[name='jcxmids']").each(function(){
															if($(this).attr("jcxmmc") == text.substr(5)){
																$(this).attr('checked','true');
															}
														});
													}else if(text.indexOf("区分是")>-1 || text.indexOf("区分为")>-1){
														$("#ajaxForm input[name='sjqf'][placeholder='"+text.substr(5)+"']").attr("checked", true);
														$("#ajaxForm input[name='sjqf']").each(function(){
															if($(this).attr("sjqfmc") == text.substr(3)){
																$(this).attr('checked','true');
															}
														});
													}else if(text.indexOf("关注病原是")>-1 || text.indexOf("关注病原为")>-1){
														$("#ajaxForm input[name='bys'][placeholder='"+text.substr(5)+"']").attr("checked", true);
													}
												}
											}
										});
									}
								}
							});
						});
*/
						/**
						 * 样本编号扫码事件
						 */
						$("#getQRCode").unbind("click").click(function(){
							//扫码事件
							wx.scanQRCode({
								needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果
								scanType: ["qrCode", "barCode"],
								success: function(res) {
									// 当needResult 为 1 时，扫码返回的结果
									var result = res.resultStr;
									// http://service.matridx.com/wechat/getUserReport?ybbh=1112
									var s_res = result.split('ybbh=')

									$("#ajaxForm #ybbh").val(s_res[s_res.length-1]);

									if(s_res[s_res.length-1]!=null && s_res[s_res.length-1]!= ""){
										getInspectionInfo();
									}
								},
								fail: function(res) {
									$.toptip(JSON.stringify(res), 'error');
								}
							});
						});
						/**
						 * 快递单号扫码事件
						 */
						$("#getKddhQRCode").unbind("click").click(function(){
							//扫码事件
							wx.scanQRCode({
								needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果
								scanType: ["qrCode", "barCode"],
								success: function(res) {
									// 当needResult 为 1 时，扫码返回的结果
									var result = res.resultStr;
									// https://ucmp.sf-express.com/service/weixin/activity/wx_b2sf_order?p1=SF1087793119325
									var s_res = result.split('p1=');
									$("#ajaxForm #kdh").val(s_res[s_res.length-1]);
								},
								fail: function(res) {
									$.toptip(JSON.stringify(res), 'error');
								}
							});
						});

						$("#uploader").unbind("click").click(function(e){
							try{
								wx.chooseImage({
									count: 4, // 默认9
									sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
									sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
									success: function (res) {
										var localIds = res.localIds;
										upload(localIds, 0);
									}
								});
							} catch (ex) {
								$("#chrom").show();
								$("#phone").hide();
							}
						});
					}
				});
			});
		}
	});
}

function upload(localIds, i){
	var ywlx = $("#ajaxForm #ywlx").val();
	wx.uploadImage({
		localId:localIds[i], //需要上传的图片的本地ID,由chooseImage接口获得
		isShowProgressTips:1,   //默认为1，显示进度提示
		success:function (res) {
			var serverId = res.serverId; //返回图片的服务器端ID
			$.ajax({
				url: '/wechat/file/saveTempFile',
				type: 'post',
				dataType: 'json',
				data : {"mediaid" : serverId,"localid" : localIds[i], "ywlx":ywlx,"wbcxdm":$("#wbcxdm").val()},
				success: function(result) {
					i = i + 1;
					var localid = result.fjcfbDto.localid;
					var fjid = result.fjcfbDto.fjid;
					displayUpInfo(fjid);
					var u = navigator.userAgent, app = navigator.appVersion;
					var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //安卓
					var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
					if (isAndroid) {
						localids.push(localid);
						var img =  '<div id="'+fjid+'" style="padding:0;width: 30%;height: 70px;margin: 0 5px;">'
							+'<div style="padding:0;border:1px solid grey;">'
							+'<img onclick="viewfile(event)" style="height: 50px;width: 100%;" src="'+localid+'"/>'
							+'<img onclick=\'delfile(event,"'+fjid+'");\' data-wjlj="'+localid+'" style="height: 20px;width: 100%;" src="/images/delete.png"/>'
							+'</div>'
							+'</div>'
						$("#uploader").before(img);
					}else if (isIOS) {
						wx.getLocalImgData({
							localId: localid, // 图片的localID
							success: function (res) {
								var localData = res.localData; // localData是图片的base64数据，可以用img标签显示
								localids.push(localData);
								var img =  '<div id="'+fjid+'" style="padding:0;width: 30%;height: 70px;margin: 0 5px;">'
									+'<div style="padding:0;border:1px solid grey;">'
									+'<img onclick="viewfile(event)" style="height: 50px;width: 100%;" src="'+localid+'"/>'
									+'<img onclick=\'delfile(event,"'+fjid+'");\' data-wjlj="'+localid+'" style="height: 20px;width: 100%;" src="/images/delete.png"/>'
									+'</div>'
									+'</div>'
								$("#uploader").before(img);
							}
						});
					}else{
						localids.push(localid);

						var img =  '<div id="'+fjid+'" style="padding:0;width: 30%;height: 70px;margin: 0 5px;">'
							+'<div style="padding:0;border:1px solid grey;">'
							+'<img onclick="viewfile(event)" style="height: 50px;width: 100%;" src="'+localid+'"/>'
							+'<img onclick=\'delfile(event,"'+fjid+'");\' data-wjlj="'+localid+'" style="height: 20px;width: 100%;" src="/images/delete.png"/>'
							+'</div>'
							+'</div>'
						$("#uploader").before(img);
					}
					if(i < localIds.length){
						upload(localIds, i);
					}
				}
			});
		}
	});
}

function viewfile(e){
	wx.previewImage({
		current: e.currentTarget.src, // 当前显示图片的http链接
		urls: localids // 需要预览的图片http链接列表
	});
}

function delfile(e,fjid){
	for(var i = 0; i < localids.length; i++){
		if(e.currentTarget.dataset.wjlj == localids[i]){
			localids.splice(i,1);
			break;
		}
	}
	var str = $("#ajaxForm #fjids").val();
	var arr=str.split(",");
	for(var i=0;i<arr.length;i++){
		if(fjid == arr[i]){
			arr.splice(i,1);
			break;
		}
	}
	var str = arr.join(",");
	$("#ajaxForm #fjids").val(str);
	$("#ajaxForm #"+fjid).remove();
}

/**
 * 扫码获取样本信息
 */
function getInspectionInfo(){
	showloading("正在获取...");
	$.ajax({
		url: '/wechat/getInspectionInfo',
		type: 'post',
		dataType: 'json',
		data : {"ybbh" : $("#ajaxForm #ybbh").val(),"wxid" : $("#wxid").val()},
		success: function(result) {
			if(result.sjxxDto && result.sjxxDto.zt != '80'){
				//患者姓名
				$("#ajaxForm #hzxm").val(result.sjxxDto.hzxm);
				//性别
				$("input:radio[value='"+result.sjxxDto.xb+"']").attr('checked','true');
				//年龄
				$("#ajaxForm #nl").val(result.sjxxDto.nl);
				//年龄单位
				$("#ajaxForm #nldw").val(result.sjxxDto.nldw);
				//电话
				$("#ajaxForm #dh").val(result.sjxxDto.dh);
				//送检单位
				$("#ajaxForm #sjdwid").val(result.sjxxDto.sjdw);
				$("#ajaxForm #sjdwmc").val(result.sjxxDto.sjdwmc);
				//科室
				$("#ajaxForm #ksid").val(result.sjxxDto.ks);
				for (var i = 0; i < ksxxlist.length; i++) {
					if(ksxxlist[i].dwid == result.sjxxDto.ks){
						$("#ajaxForm #ksmc").val(ksxxlist[i].dwmc)
						if (ksxxlist[i].kzcs == '1'){
							$("#qtksInput").show()
							$("#qtksInput #qtks").val(result.sjxxDto.qtks)
						} else {
							$("#qtksInput").hide()
							$("#qtksInput #qtks").val("")
						}
					}
				}
				$("#ajaxForm #sjys").val(result.sjxxDto.sjys);
				$("#ajaxForm #sjysmc").val(result.sjxxDto.sjys);
				$("#ajaxForm #ysdh").val(result.sjxxDto.ysdh);
				$("#ajaxForm #db").val(result.sjxxDto.db);
				$("#ajaxForm #kdh").val(result.sjxxDto.kdh);
				$("#ajaxForm #cwh").val(result.sjxxDto.cwh);
				$("#ajaxForm #zyh").val(result.sjxxDto.zyh);
				$("#ajaxForm #dbm").val(result.sjxxDto.db);
				//送检id
				$("#ajaxForm #sjid").val(result.sjxxDto.sjid);
				$("#ajaxForm #xgsj").val(result.sjxxDto.xgsj);
				$("#ajaxForm #jcxmids").val(result.sjxxDto.jcxmids);
				$("#ajaxForm #jczxmids").val(result.sjxxDto.jczxmids);
				$("input:radio[value='"+result.sjxxDto.kdlx+"']").attr('checked','true');
				$("input:radio[value='"+result.sjxxDto.sjqf+"']").attr('checked','true');

				// 检测项目限制
				detectlist = result.detectlist;
				var dqdetectcskz = "";
				for (var i = 0; i < detectlist.length; i++) {
					if($("#jcxmids").val().indexOf(detectlist[i].csid)!=-1){
						$("#ajaxForm input[name='jcxm'][id='"+detectlist[i].csid+"']").prop("checked",true);
						dqdetectid += ","+detectlist[i].csid
						dqdetectcskz += ","+detectlist[i].cskz1
					}
				}
				dqdetectid = dqdetectid.substring(1);
				dqdetectcskz = dqdetectcskz.substring(1);
				// 由于Res项目，将判断改为如下 if(dqdetectcskz.indexOf("F")!=-1 || dqdetectcskz.indexOf("Z")!=-1){
				getSubDetectByDetect()

				var sjgzbys = result.sjxxDto.sjgzbys;
				if(sjgzbys.length>0){
					for (var i = 0; i < sjgzbys.length; i++) {
						$("#ajaxForm [name='bys'][value='"+sjgzbys[i].by+"']").prop("checked",true);
					}
				}
				hideloading()
			}else if(result.sjxxDto && result.sjxxDto.zt == '80'){
				hideloading()
				$("#ajaxForm #ybbh").val("");
				$.toptip("条码重复不能使用！", 'error');
			}else {
				hideloading()
			}
		}
	});
}

/**
 * 下一步
 */
$("#ajaxForm #nextbtn").unbind("click").click(function(){
	if (!isButtonActive){
		return false;
	}
	buttonDisabled(true,false,"",true);
	if(!getWxOrNot()){
		return buttonDisabled(false,false,"",false);
	}
	var actionFlag = $("#ajaxForm #actionFlag").val();//动作标记，1：完善，其他：新增、修改
	//附件上传，录单必填项
	if(!$("#ajaxForm #fjids").val() && $("#ajaxForm #fjcfbDto").children().length == 0){
		return buttonDisabled(false,true,"请上传附件!",false);
	}

	$("#ajaxForm #db").val($("#ajaxForm #db").val().replace(/\s+/g,""));
	$("#ajaxForm #db").val(DBC2SBC($("#ajaxForm #db").val()));
	$("#ajaxForm #sjys").val($("#ajaxForm #sjys").val().replace(/\s+/g,""));
	$("#ajaxForm #ybbh").val($("#ajaxForm #ybbh").val().replace(/\s+/g,""));

	//若为新增、修改，检验录单必填项
	if (actionFlag != "1"){
		//校验样本编号，录单必填项
		if (!checkYbbh()){
			return false;
		}
		//校验患者姓名，录单必填项
		if(!$("#ajaxForm #hzxm").val()){
			return buttonDisabled(false,true,"请填写患者姓名!",false);
		}
		//校验性别，录单必填项
		if($("#ajaxForm input:radio[name='xb']:checked").val()=="" || $("#ajaxForm input:radio[name='xb']:checked").val()==null){
			return buttonDisabled(false,true,"请选择性别!",false);
		}
		//校验年龄，录单必填项
		if(!$("#ajaxForm #nl").val()){
			return buttonDisabled(false,true,"请填写年龄!",false);
		}
		//校验年龄单位，录单必填项
		if(!$("#ajaxForm #nldw").val()){
			return buttonDisabled(false,true,"请选择年龄单位!",false);
		}
		//校验检测单位，录单必填项
		if(!$("#ajaxForm #jcdwid").val()){
			return buttonDisabled(false,true,"请选择检测单位!",false);
		}
		//校验送检区分，录单必填项
		if(!$("#ajaxForm input:radio[name='sjqf']:checked").val()){
			return buttonDisabled(false,true,"请选择送检区分!",false);
		}
		//校验科研项目，若送检区分为科研项目，则必填
		if(!$("#ajaxForm #kyxm").val() && !$("#ajaxForm #kyxmDiv").is(":hidden")){
			return buttonDisabled(false,true,"请选择立项编号!",false);
		}
		//校验合作伙伴，录单必填项
		if(!$("#ajaxForm #db").val()){
			return buttonDisabled(false,true,"请填写负责人!",false);
		}
		//校验检测项目，录单必填项
		if(!$("#ajaxForm #jcxmids").val()){
			return buttonDisabled(false,true,"请选择检测项目!",false);
		}
		//校验检测子项目，录单必填项
		if(!$("#ajaxForm #jczxmDiv").is(":hidden")){
			if(!$("#ajaxForm #jczxmids").val()){
				return buttonDisabled(false,true,"请选择检测子项目!",false);
			}
		}
	}
	var jcxmmc="";
	var json=[];
	$("#ajaxForm input[name='jcxm']").each(function(){
		var flag=$(this).prop("checked");
		if(flag){
			jcxmmc+=","+$(this).attr("csmc");
			var hasSub=false;
			for (var i = 0; i < detectsublist.length; i++) {
				if($(this).val()==detectsublist[i].fcsid){
					hasSub=true;
					break;
				}
			}
			if(!hasSub){
				var sz={"jcxmid":$(this).val(),"jczxmid":"","xmglid":""};
				json.push(sz);
			}
		}
	});
	$("#ajaxForm input[name='jczxm']").each(function(){
		var flag=$(this).prop("checked");
		if(flag){
			var sz={"jcxmid":$(this).attr("fcsid"),"jczxmid":$(this).val(),"xmglid":""};
			json.push(sz);
		}
	});

	if(sjjcxmDtos!=null&&sjjcxmDtos.length>0){
		for(var i=0;i<json.length;i++){
			for(var j=0;j<sjjcxmDtos.length;j++){
				if(json[i].jcxmid==sjjcxmDtos[j].jcxmid){
					if(json[i].jczxmid){
						if(json[i].jczxmid==sjjcxmDtos[j].jczxmid){
							json[i].xmglid=sjjcxmDtos[j].xmglid;
						}
					}else{
						json[i].xmglid=sjjcxmDtos[j].xmglid;
					}
				}
			}
		}
	}
	console.log(JSON.stringify(json));
	//若为完善，检验完善必填项
	if (actionFlag == "1"){
		//校验送检医生，完善必填项
		if(!$("#ajaxForm #sjys").val()){
			return buttonDisabled(false,true,"请选择主治医师!",false);
		}
		if(!$("#ajaxForm #sjdwid").val()){
			return buttonDisabled(false,true,"请选择医院名称!",false);
		}
		//校验报告显示单位，完善必填项
		if(!$("#ajaxForm #sjdwmc").val()){
			return buttonDisabled(false,true,"请选择医院名称!",false);
		}
		//校验科室，完善必填项
		if(!$("#ajaxForm #ksid").val()){
			return buttonDisabled(false,true,"请选择科室!",false);
		}
		//校验报告显示科室，完善必填项
		for (let i = 0; i < ksxxlist.length; i++) {
			if(ksxxlist[i].dwid == $("#ajaxForm #ksid").val()){
				if (ksxxlist[i].kzcs == "1") {
					if(!$("#ajaxForm #qtks").val()){
						return buttonDisabled(false,true,"请填写报告显示科室!",false);
					}
				}
			}
		}
		//校验快递类型
		if(!$("#ajaxForm input:radio[name='kdlx']:checked").val()){
			return buttonDisabled(false,true,"请选择快递类型!",false);
		}
		//校验快递号,若快递类型不为无时，则必填
		if ($("#ajaxForm input:radio[name='kdlx']:checked").val() != qtkdlx){
			if(!$("#ajaxForm #kdh").val()){
				return buttonDisabled(false,true,"请填写快递号!",false);
			}
		}
	}
	if($("#ajaxForm #kdh").val().length>32){
        return buttonDisabled(false,true,"请填写正确的快递号!",false);
    }
	//处理关注病原
	var lsbys = ""
	var bysArr = document.getElementsByName("bys");
	for (var i = 0; i < bysArr.length; i++) {
		if (bysArr[i].checked) {
			lsbys += ","+bysArr[i].value;
		}
	}
	lsbys = lsbys.substring(1)
	dealSfwsFlag();
	showloading("正在保存...");
	$.ajax({
		type:'post',
		url:"/wechat/addSaveReportReturnMap",
		cache: false,
		data: {
			"wxid"	:$("#wxid").val(),
			"sjid"	:($("#sjid").val()?$("#sjid").val():""),
			"ywlx"	:$("#ywlx").val(),
			"status":($("#status").val()?$("#status").val():""),
			"fjids"	:$("#fjids").val(),//附件ids
			"ybbh"	:$("#ajaxForm #ybbh").val(),//样本编号，录单必填项
			"hzxm"	:$("#ajaxForm #hzxm").val(),//姓名，录单必填项
			"xb"	:$("#ajaxForm input:radio[name='xb']:checked").val(),//性别，录单必填项
			"nl"	:$("#ajaxForm #nl").val(),//年龄，录单必填项
			"nldw"	:$("#ajaxForm #nldw").val(),//年龄单位，录单必填项
			"dh"	:($("#ajaxForm #dh").val()?$("#ajaxForm #dh").val():''),//联系电话，非必填项
			"sjys"	:($("#sjysmc").val()?$("#sjysmc").val():''),//送检医生，完善必填项
			"ysdh"	:($("#ajaxForm #ysdh").val()?$("#ajaxForm #ysdh").val():''),//医生电话，非必填项
			"sjdw"	:$("#ajaxForm #sjdwid").val(),//送检单位，完善必填项
			"yymc"	:$("#ajaxForm #sjdwmc").val(),//报告显示单位，完善必填项
			"sjdwmc":$("#ajaxForm #sjdwmc").val(),//报告显示单位，完善必填项
			"ks"	:$("#ajaxForm #ksid").val(),//科室，完善必填项
			"qtks"	:$("#ajaxForm #qtks").val(),//报告显示科室，完善必填项
			"jcdw"	:$("#ajaxForm #jcdwid").val(),//检测单位，录单必填项
			"db"	:$("#ajaxForm #db").val(),//合作伙伴，录单必填项
			"sjqf"	:$("#ajaxForm input:radio[name='sjqf']:checked").val(),//送检区分，完善必填项
			"kyxm"	:($("#ajaxForm #kyxm").val()?$("#ajaxForm #kyxm").val():''),//科研项目，完善必填项
			"zyh"	:($("#ajaxForm #zyh").val()?$("#ajaxForm #zyh").val():''),//住院号，非必填项
			"cwh"	:($("#ajaxForm #cwh").val()?$("#ajaxForm #cwh").val():''),//床位号，非必填项
			"kdlx"	:$("#ajaxForm input:radio[name='kdlx']:checked").val(),//快递类型，完善必填项
			"kdh"	:($("#ajaxForm #kdh").val()?$("#ajaxForm #kdh").val():'无'),//快递号，完善必填项
			"jcxmids":$("#ajaxForm #jcxmids").val().replace("[","").replace("]",""),//检测项目，录单必填项
			"jcxm"	:JSON.stringify(json),
			"bys"	:lsbys,//关注病原，非必填项
			"sfws":$("#ajaxForm #sfws").val(),//是否完善标记
			"xgsj":$("#ajaxForm #xgsj").val(),//页面打开时间
			"jcxmmc":jcxmmc?jcxmmc.substring(1):"",
		},
		dataType:'json',
		success:function(data){
			hideloading();
			if (data.status=="success"){
				window.location.replace('/wechat/goToSecondPage?wxid=' + $("#wxid").val()+"&sjid="+data.sjxxDto.sjid+"&wbcxdm="+$("#wbcxdm").val()+"&actionFlag="+(actionFlag!=null?actionFlag:''));
			}else {
				buttonDisabled(false,true,data.message,false);
			}
		},
		error: function (data) {
			hideloading();
			buttonDisabled(false,true,data,false);
		}

	})
	return true;
});

/**
 * 按钮禁用事件
 * @param isError 是否提示错误信息
 * @param isDisabled	是否禁用按钮
 * @param errorInfo	错误信息内容
 * @param returnFlg	返回状态 true:成功 false:失败
 * @returns {*}
 */
function buttonDisabled(isDisabled,isError,errorInfo,returnFlg){
	if (isDisabled){
		isButtonActive = false;
		$("#ajaxForm #nextbtn").attr("disabled","true");
		$("#ajaxForm #info_save").attr("disabled","true");
	}else {
		isButtonActive = true;
		$("#ajaxForm #info_save").removeAttr("disabled");
		$("#ajaxForm #nextbtn").removeAttr("disabled");
	}
	if (isError){
		$.toptip(errorInfo, 'error');
	}
	return returnFlg;
}
/**
 * 提取患者信息
 */
$("#ajaxForm #getuser").unbind("click").click(function(){
	if(!getWxOrNot()){
		return false;
	}
	var ybbh = $("#ajaxForm #ybbh").val();
	var hzxm = $("#ajaxForm #hzxm").val();
	if(!ybbh && !hzxm){
		return buttonDisabled(false,true,"请填写标本编号或患者姓名！",false);
	}
	var wxid = $("#ajaxForm #wxid").val();
	showloading("正在提取...");
	$.ajax({
		type:'post',
		url:"/wechat/inspection/extractUserInfo",
		cache: false,
		data: {"wxid":wxid,"hzxm":hzxm,"ybbh":ybbh},
		dataType:'json',
		success:function(data){
			if(data.status == "success"){
				$("#ajaxForm #sjid").val(data.sjxxDto.sjid);
				$("#ajaxForm #ybbh").val(data.sjxxDto.ybbh);
				$("#ajaxForm #hzxm").val(data.sjxxDto.hzxm);
				$("input:radio[name='xb'][value='"+data.sjxxDto.xb+"']").attr('checked','true');
				$("#ajaxForm #nl").val(data.sjxxDto.nl);
				$("#ajaxForm #nldw").val(data.sjxxDto.nldw);
				$("#ajaxForm #dh").val(data.sjxxDto.dh);
				$("#ajaxForm #sjdw").val(data.sjxxDto.sjdw);
				$("#ajaxForm #yymc").val(data.sjxxDto.yymc);
				$("#ajaxForm #sjdwmc").val(data.sjxxDto.sjdwmc);
				sjjcxmDtos = data.sjjcxmDtos;
				if(data.sjxxDto.sjdwbj=='1'){
					$("#ajaxForm #qtdwInput").show();
				}else{
					$("#ajaxForm #qtdwInput").hide();
				}
				$("#ajaxForm #ksid").val(data.sjxxDto.ks);
				for (i = 0; i < xzksList.length; i++) {
					if(xzksList[i].value == data.sjxxDto.ks){
						$("#ajaxForm #ksmc").val(xzksList[i].label)
						break;
					}
				}
				for (var i = 0; i < ksxxlist.length; i++) {
					if(ksxxlist[i].dwid == data.sjxxDto.ks){
						$("#ajaxForm #ksmc").val(ksxxlist[i].dwmc)
						if (ksxxlist[i].kzcs == '1'){
							$("#qtksInput").show()
							$("#qtksInput #qtks").val(data.sjxxDto.qtks)
						} else {
							$("#qtksInput").hide()
							$("#qtksInput #qtks").val("")
						}
					}
				}
				$("#ajaxForm #sjys").val(data.sjxxDto.sjys);
				$("#ajaxForm #zyh").val(data.sjxxDto.zyh);
				$("#ajaxForm #cwh").val(data.sjxxDto.cwh);
				$("#ajaxForm #ysdh").val(data.sjxxDto.ysdh);
				$("#ajaxForm #db").val(data.sjxxDto.db);
				$("#ajaxForm #kdh").val(data.sjxxDto.kdh);
				$("#ajaxForm #xgsj").val(data.sjxxDto.xgsj);
				$("input:radio[value='"+data.sjxxDto.kdlx+"']").attr('checked','true');
				initKdlx()
				// 检测单位
				$("#hzxxInfo #jcdwid").val(data.sjxxDto.jcdw)
				$("#hzxxInfo #jcdwmc").val(data.sjxxDto.jcdwmc)
				refreshUnit(data.sjxxDto.jcdw)
				$("input:radio[value='"+data.sjxxDto.sjqf+"']").attr('checked','true');
				//科研项目
				var kyxmid = data.sjxxDto.kyxm;
				var sjqf = data.sjxxDto.sjqf;
				xzkylist = [];
				if(sjqf){
					for (var i = 0; i < kylist.length; i++) {
						if(kylist[i].fcsid == sjqf){
							var lskyxm = {label:kylist[i].csmc,value:kylist[i].csid};
							xzkylist.push(lskyxm)
							if(kyxmid == kylist[i].csid){
								$("#ajaxForm #kyxmmc").val(kylist[i].csmc)
							}
						}
					}
				}
				if(xzkylist.length>0){
					$("#kyxmDiv").show();
				}else{
					$("#ajaxForm #kyxm").val("");
					$("#ajaxForm #kyxmmc").val("");
					$("#kyxmDiv").hide();
				}
				//检测项目、初始化检测子项目
				var dqdetectcsid = data.sjxxDto.jcxmids;
				if(dqdetectcsid){
					var dqdetectcskz = ""
					$("#jcxmids").val(dqdetectcsid)
					$("#jczxmids").val(data.sjxxDto.jczxmids)
					var html='';
					var oncoFlag=false;
					var tNGSFlag=false;
					for (var i = 0; i < detectlist.length; i++) {
						var xzFlag=false;
						for(var j = 0; j < detectlist.length; j++){
							if($("#jcxmids").val().indexOf(detectlist[j].csid)!=-1){
								var cskz3=detectlist[j].cskz3;
								var cskz4=detectlist[j].cskz4;
								if(cskz3==detectlist[i].cskz3&&cskz4==detectlist[i].cskz4){
									xzFlag=true;
								}
								if(cskz3=='IMP_REPORT_ONCO_QINDEX_TEMEPLATE'&&cskz4=='Q'){
									oncoFlag=true;
								}
								if(cskz3=='IMP_REPORT_TNGS_TEMEPLATE'&&cskz4=='T'){
									tNGSFlag=true;
								}else if(cskz3.indexOf('IMP_REPORT_SEQ_TNGS')>-1&&cskz4=='K'){
									tNGSFlag=true;
								}
							}
						}
						if(oncoFlag){
							if(detectlist[i].cskz3=='IMP_REPORT_ONCO_QINDEX_TEMEPLATE'&&detectlist[i].cskz4=='Q'){
							    xzFlag=true;
							}
						}
						if(tNGSFlag){
							if(detectlist[i].cskz3=='IMP_REPORT_TNGS_TEMEPLATE'&&detectlist[i].cskz4=='T'){
								xzFlag=true;
							}else if(detectlist[i].cskz3.indexOf('IMP_REPORT_SEQ_TNGS')>-1&&detectlist[i].cskz4=='K'){
								xzFlag=true;
							}
						}
						if($("#jcxmids").val().indexOf(detectlist[i].csid)!=-1){
							dqdetectid += ","+detectlist[i].csid
							dqdetectcskz += ","+detectlist[i].cskz1
							html += '<span class="RadioStyle">';
							html+='<input type="checkbox" id="'+detectlist[i].csid+'" name="jcxm" value="'+detectlist[i].csid+'" csmc="'+detectlist[i].csmc+'" cskz1="'+detectlist[i].cskz1+'"  cskz3="'+detectlist[i].cskz3+'"  cskz4="'+detectlist[i].cskz4+'" checked onclick=\'changeJcxm("'+detectlist[i].csid+'");\'  validate="{required:true}"/>';
							html +='<label for="'+detectlist[i].csid+'" style="font-size: small;background-color: '+detectlist[i].cskz2+'">'+detectlist[i].csmc+'</label>';
							html +='</span>';
						}else{
							if(!xzFlag){
								html += '<span class="RadioStyle">';
								html+='<input type="checkbox" id="'+detectlist[i].csid+'" name="jcxm" value="'+detectlist[i].csid+'" csmc="'+detectlist[i].csmc+'"  cskz1="'+detectlist[i].cskz1+'"  cskz3="'+detectlist[i].cskz3+'"  cskz4="'+detectlist[i].cskz4+'" onclick=\'changeJcxm("'+detectlist[i].csid+'");\'  validate="{required:true}"/>';
								html +='<label for="'+detectlist[i].csid+'" style="font-size: small;background-color: '+detectlist[i].cskz2+'">'+detectlist[i].csmc+'</label>';
								html +='</span>';
							}
						}
					}
					$("#jcxm").empty();
					$("#jcxm").append(html);
					dqdetectcskz = dqdetectcskz.substring(1);
					// 由于Res项目，将判断改为如下 if(dqdetectcskz.indexOf("F")!=-1 || dqdetectcskz.indexOf("Z")!=-1){
					getSubDetectByDetect()
				}
				if(data.sjxxDto.sjgzbys){
					$.each(data.sjxxDto.sjgzbys,function(n,value) {
						$("#ajaxForm [name='bys'][value='"+value.by+"']").prop("checked",true);
					});
				}
				if(data.fjcfbDtos){
					refreshFj(data.fjcfbDtos);
				}
				hideloading();
				$.toptip("提取成功！", 'success');
			}else{
				hideloading();
				if(data.message){
					$.toptip(data.message, 'error');
					$("#ajaxForm #ybbh").val("");
				}else{
					$.toptip("未获取到数据!", 'error');
				}
			}
		},
		error:function (date) {
			hideloading();
			$.toptip(JSON.stringify(date), 'error');
		}
	});
});

/**
 * 中文数字转阿拉伯数字
 * @param digit
 */
function zhDigitToArabic(digit) {
	var result = 0,quotFlag;
	if(!isNaN(digit)){
		result = digit;
		return result;
	}
	const zh = ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九'];
	const unit = ['千', '百', '十'];
	const quot = ['万', '亿', '兆', '京', '垓', '秭', '穰', '沟', '涧', '正', '载', '极', '恒河沙', '阿僧祗', '那由他', '不可思议', '无量', '大数'];

	for (var i = digit.length - 1; i >= 0; i--) {
		if (zh.indexOf(digit[i]) > -1) { // 数字
			if (quotFlag) {
				result += quotFlag * getNumber(digit[i]);
			} else {
				result += getNumber(digit[i]);
			}
		} else if (unit.indexOf(digit[i]) > -1) { // 十分位
			if (quotFlag) {
				result += quotFlag * getUnit(digit[i]) * getNumber(digit[i - 1]);
			} else {
				if(i == 0){
					result += getUnit(digit[i]);
				}else{
					result += getUnit(digit[i]) * getNumber(digit[i - 1]);
				}
			}
			--i;
		} else if (quot.indexOf(digit[i]) > -1) { // 万分位
			if (unit.indexOf(digit[i - 1]) > -1) {
				if (getNumber(digit[i - 1])) {
					result += getQuot(digit[i]) * getNumber(digit[i - 1]);
				} else {
					result += getQuot(digit[i]) * getUnit(digit[i - 1]) * getNumber(digit[i - 2]);
					quotFlag = getQuot(digit[i]);
					--i;
				}
			} else {
				result += getQuot(digit[i]) * getNumber(digit[i - 1]);
				quotFlag = getQuot(digit[i]);
			}
			--i;
		}
	}
	return result;
	// 返回中文大写数字对应的阿拉伯数字
	function getNumber(num) {
		for (var i = 0; i < zh.length; i++) {
			if (zh[i] == num) {
				return i;
			}
		}
	}
	// 取单位
	function getUnit(num) {
		for (var i = unit.length; i > 0; i--) {
			if (num == unit[i - 1]) {
				return Math.pow(10, 4 - i);
			}
		}
	}
	// 取分段
	function getQuot(q) {
		for (var i = 0; i < quot.length; i++) {
			if (q == quot[i]) {
				return Math.pow(10, (i + 1) * 4);
			}
		}
	}
}

var chnNumChar = {
	零: 0,
	一: 1,
	二: 2,
	三: 3,
	四: 4,
	五: 5,
	六: 6,
	七: 7,
	八: 8,
	九: 9
};

/**
 * 手机号转换
 * @param digit
 * @returns {Number}
 */
function phoneNumber(digit) {
	var rtn = 0;
	if(!isNaN(digit)){
		rtn = digit;
		return rtn;
	}
	var str = digit.split('');
	for (var i = 0; i < str.length; i++) {
		var num = chnNumChar[str[i]];
		rtn = rtn * 10 + num;
	}
	return rtn;
}

/**
 * 全角数字或字母转换为半角
 * @param str
 * @param flag
 * @returns {String}
 */
function DBC2SBC(str, flag) {
	var i;
	var result = '';
	for (i = 0; i < str.length; i++) {
		str1 = str.charCodeAt(i);
		if (str1 < 65296) {
			result += String.fromCharCode(str.charCodeAt(i));
			continue;
		}
		if (str1 < 125 && !flag)
			result += String.fromCharCode(str.charCodeAt(i));

		else result += String.fromCharCode(str.charCodeAt(i) - 65248);
	}
	return result;
}

window.onload = function() {
	var list = new Array();
	for (var i = 50; i < document.all.length; i++) {
		if (document.all[i].type == "text" && document.all[i].style.display != "none")
			list.push(i);
	}

	for (var i = 0; i < list.length - 1; i++) {
		document.all[list[i]].setAttribute("nextFocusIndex", list[i + 1]);
		document.all[list[i]].onkeydown = JumpToNext;
	}
	for (var i = list.length - 1; i < document.all.length; i++) {
		if (document.all[i].type == "button") {
			document.all[list[list.length - 1]].setAttribute("nextFocusIndex", i);
			document.all[list[list.length - 1]].onkeydown = JumpToNext;
			break;
		}
	}
	document.all[list[0]].focus();
}

function JumpToNext() {
	if (event.keyCode == 13) {
		var nextFocusIndex = this.getAttribute("nextFocusIndex");
		document.all[nextFocusIndex].focus();
	}
}

/**
 * 附件删除
 * @param fjid
 * @param wjlj
 */
function del(fjid,wjlj){
	var url= "/wechat/inspection/delFile";
	jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
		setTimeout(function(){
			if(responseText["status"] == 'success'){
				$.toptip(responseText["message"], 'success');
				$("#"+fjid).remove();
			}else if(responseText["status"] == "fail"){
				$.toptip(responseText["message"], 'error');
			} else{
				$.toptip(responseText["message"], 'error');
			}
		},1);
	},'json');
}

var predb = null;
function refreshDB(){
	var jcdw = $("#ajaxForm #jcdwid").val();
	refreshUnit(jcdw)
}



function refreshSjysDB(){
	// 查询检测单位
	$.ajax({
		url: '/wechat/selectDetectionUnit',
		type: 'post',
		data: {
			"hbmc": $("#sjysadd_hzhb").val()
		},
		dataType: 'json',
		success: function(result) {
			unitlist = result.unitList;
			if(unitlist != null && unitlist.length > 0){
				xzjcdwList = [];
				for (var i = 0; i < unitlist.length; i++) {
					var lsuniy = {label:unitlist[i].csmc,value:unitlist[i].csid};
					xzjcdwList.push(lsuniy)
					if($("#ajaxForm #jcdwid").val()){
						if(lsuniy.value==$("#ajaxForm #jcdwid").val()){
							$("#jcdwmc").val(lsuniy.label);
							$("#jcdwid").val(lsuniy.value);
						}
					}
				}
				if(!$("#ajaxForm #jcdwid").val()){
					$("#jcdwmc").val("");
					$("#jcdwid").val("");
					if (unitlist.length == 1) {
						$("#jcdwmc").val(unitlist[0].csmc);
						$("#jcdwid").val(unitlist[0].csid);
					}
				}
			}
		}
	});
}
/**
 * 根据合作伙伴刷新检测单位
 * @returns {boolean}
 */
function refreshUnit(jcdw){
	//根据伙伴匹配送检区分
	initSjhb()
	//刷新检测单位
	var db = $("#ajaxForm #db").val();
	if(db == predb){
		if (!jcdw){
			return false;
		}else {
			let isCompare = false;
			if (xzunitList!=null && xzunitList.length>0){
				for (var i = 0; i < xzunitList.length; i++) {
					if(xzunitList[i].value==jcdw){
						$("#jcdwmc").val(xzunitList[i].label);
						$("#jcdwid").val(xzunitList[i].value);
						isCompare = true;
						break;
					}
				}
			}
			if (isCompare){
				return  false;
			}else {
				$("#jcdwmc").val(xzunitList[0].label);
				$("#jcdwid").val(xzunitList[0].value);
			}
		}
	}
	predb = db;
	// 查询检测单位
	$.ajax({
		url: '/wechat/selectDetectionUnit',
		type: 'post',
		data: {
			"hbmc": db
		},
		dataType: 'json',
		success: function(result) {
			let isCompare = false;
			unitlist = result.unitList;
			if(unitlist != null && unitlist.length > 0){
				xzunitList = [];
				for (var i = 0; i < unitlist.length; i++) {
					var lsuniy = {label:unitlist[i].csmc,value:unitlist[i].csid};
					xzunitList.push(lsuniy)
					if(jcdw){
						if(lsuniy.value==jcdw){
							$("#jcdwmc").val(lsuniy.label);
							$("#jcdwid").val(lsuniy.value);
							isCompare = true;
						}
					}
				}
				if(!jcdw || !isCompare){
					$("#jcdwmc").val("");
					$("#jcdwid").val("");
					if (unitlist.length == 1) {
						$("#jcdwmc").val(unitlist[0].csmc);
						$("#jcdwid").val(unitlist[0].csid);
					}
				}
			}
		}
	});

}

function initSjhb(){
	var sjqf = $("#ajaxForm input:radio[name='sjqf']:checked").val();
	var sjqfmc = $("#ajaxForm input:radio[name='sjqf']:checked").attr("sjqfmc");
	var sjhbmc = $("#ajaxForm #db").val();
	var sjhbmcPrefix = sjhbmc;//送检伙伴名称前缀
	var sjhbmcSuffix = "";//送检伙伴名称后缀
	if (sjhbmc == null || sjhbmc == "" || sjqfmc == null || sjqfmc == "") {
		return false;
	}
	//若送检区分是“科研”或“入院”
	if (sjqfmc.indexOf("科研") > -1 || sjqfmc.indexOf("入院") > -1){
		sjhbmcSuffix = sjqfmc;
	}
	//若送检伙伴名称的最后两位是“科研”
	if ((sjhbmc.indexOf("科研") > -1 && sjhbmc.substring(sjhbmc.length-2,sjhbmc.length) == "科研") || (sjhbmc.indexOf("入院") > -1 && sjhbmc.substring(sjhbmc.length-2,sjhbmc.length) == "入院") ) {
		sjhbmcPrefix = sjhbmc.substring(0,sjhbmc.length-2);
	}
	sjhbmc = sjhbmcPrefix + sjhbmcSuffix;
	$("#ajaxForm #db").val(sjhbmc);
}
//科研项目数据初始化
function initKyxm(){
	$("#ajaxForm #kyxm").val("");
	$("#ajaxForm #kyxmmc").val("");
	var sjqf = $("#ajaxForm input:radio[name='sjqf']:checked").val();
	xzkylist = [];
	if(sjqf){
		for (var i = 0; i < kylist.length; i++) {
			if(kylist[i].fcsid == sjqf){
				var lskyxm = {label:kylist[i].csmc,value:kylist[i].csid};
				xzkylist.push(lskyxm)
			}
		}
	}
	if(xzkylist.length>0){
		$("#kyxmDiv").show();
	}else{
		$("#ajaxForm #kyxm").val("");
		$("#ajaxForm #kyxmmc").val("");
		$("#kyxmDiv").hide();
	}
}

//初始化fileinput
var FileInput = function () {
	var oFile = new Object();
	//初始化fileinput控件（第一次初始化）
	//参数说明 singleFlg 好像是标明 是否单文件上传限制，有点忘记了
	//impflg 用于后台判断是否为导入。1为导入，则开启线程进行导入  2：代表附件保存
	oFile.Init = function(ctrlName,callback,impflg,singleFlg,fileName) {
		var control = $('#' + ctrlName + ' #' + fileName);
		var filecnt = 0;

		//初始化上传控件的样式
		control.fileinput({
			language: 'zh', //设置语言
			uploadUrl: "/wechat/file/saveImportFile?access_token="+$("#ac_tk").val(), //上传的地址
			showUpload: false, //是否显示上传按钮
			showPreview: true, //展前预览
			showCaption: true,//是否显示标题
			showRemove: true,
			showCancel: false,
			showClose: false,
			showUploadedThumbs: true,
			encodeUrl: false,
			purifyHtml: false,
			browseClass: "btn btn-primary", //按钮样式	 
			dropZoneEnabled: false,//是否显示拖拽区域
			//minImageWidth: 50, //图片的最小宽度
			//minImageHeight: 50,//图片的最小高度
			//maxImageWidth: 1000,//图片的最大宽度
			//maxImageHeight: 1000,//图片的最大高度
			maxFileSize: 0,//单位为kb，如果为0表示不限制文件大小
			//minFileCount: 0,
			maxFileCount: 5, //表示允许同时上传的最大文件个数
			enctype: 'multipart/form-data',
			validateInitialCount:true,
			previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
			msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
			layoutTemplates :{
				//actionDelete:'', //去除上传预览的缩略图中的删除图标
				actionUpload:'',//去除上传预览缩略图中的上传图片；
				//actionZoom:''   //去除上传预览缩略图中的查看详情预览的缩略图标。
			},

			uploadExtraData:function (previewId, index) {
				//向后台传递id作为额外参数，是后台可以根据id修改对应的图片地址。
				var obj = {};
				obj.access_token = $("#ac_tk").val();
				obj.ywlx = $("#"+ ctrlName + " #ywlx").val();
				if(impflg && impflg == 1){
					obj.impflg = 1;
				}
				obj.fileName = fileName;
				return obj;
			},
			initialPreviewAsData:false,
			initialPreview: [        //这里配置需要初始展示的图片连接数组，字符串类型的，通常是通过后台获取后然后组装成数组直接赋给initialPreview就行了
				//"http://localhost:8086/common/file/getFileInfo",
			],
			initialPreviewConfig: [ //配置预览中的一些参数
			]
		})
			//删除初始化存在文件时执行
			.on('filepredelete', function(event, key, jqXHR, data) {

			})
			//删除打开页面后新上传文件时执行
			.on('filesuccessremove', function(event, key, jqXHR, data) {
				if(fileName == 'wechat_file'){
					var frames = $("#"+ ctrlName +" .file-preview-frame.krajee-default.kv-preview-thumb.file-preview-success");
					var index = 0;
					for(var i=0;i<frames.length;i++){
						if(key == frames[i].id){
							index = i;
							break;
						}
					}
					var str = $("#"+ ctrlName + " #fjids").val();
					var arr=str.split(",");
					arr.splice(index,1);
					var str=arr.join(",");
					$("#"+ ctrlName + " #fjids").val(str);
				}
			})

			// 实现图片被选中后自动提交
			.on('filebatchselected', function(event, files) {
				// 选中事件
				$(event.target).fileinput('upload');
				filecnt = files.length;

			})
			// 异步上传错误结果处理
			.on('fileerror', function(event, data, msg) {  //一个文件上传失败
				// 清除当前的预览图 ，并隐藏 【移除】 按钮
				$(event.target)
					.fileinput('clear')
					.fileinput('unlock');
				if(singleFlg && singleFlg==1){
					$(event.target)
						.parent()
						.siblings('.fileinput-remove')
						.hide()
				}
				// 打开失败的信息弹窗
				$.toptip("请求失败，请稍后重试!", 'error');
			})
			// 异步上传成功结果处理
			.on('fileuploaded', function(event, data) {
				// 判断 code 是否为  0	0 代表成功
				status = data.response.status
				if (status === "success") {

					if(callback){
						eval(callback+"('"+ data.response.fjcfbDto.fjid+"')");
					}
				} else {
					// 失败回调
					// 清除当前的预览图 ，并隐藏 【移除】 按钮
					$(event.target)
						.fileinput('clear')
						.fileinput('unlock');
					if(singleFlg && singleFlg==1){
						$(event.target)
							.parent()
							.siblings('.fileinput-remove')
							.hide()
					}
					// 打开失败的信息弹窗
					$.toptip("请求失败，请稍后重试!", 'error');
				}
			})
	}
	return oFile;
};

function refreshFj(fjcfbDtos){
	$("#ajaxForm #fjcfbDto").html("");
	var fileHtml = "";
	if(fjcfbDtos != null && fjcfbDtos.length > 0){
		for (var i = 0; i < fjcfbDtos.length; i++) {
			fileHtml += '<div id="'+fjcfbDtos[i].fjid+'" style="font-size: small;">' +
				'	<button title="删除" class="weui-icon-delete" style="color: red;margin-top: -15px" type="button" onclick="del(\''+fjcfbDtos[i].fjid+'\',\''+fjcfbDtos[i].wjlj+'\');"></button>' +
				'	<span style="white-space: nowrap;display: inline-block;overflow: hidden;text-overflow: ellipsis;max-width: 150px">附件'+(i+1)+':'+fjcfbDtos[i].wjm+'</span>' +
				'</div>'
		}
	}
	$("#ajaxForm #fjcfbDto").append(fileHtml);
}

function refreshFile(){
	$("#ajaxForm #fjids").val("");
	$("#ajaxForm .upfile").remove();

	$("#ajaxForm #fileDiv").html("");
	var fileHtml="<input id='wechat_file' name='wechat_file' type='file'>";
	$("#ajaxForm #fileDiv").append(fileHtml);
	//初始化fileinput
	var oFileInput = new FileInput();
	oFileInput.Init("ajaxForm","displayUpInfo",2,1,"wechat_file");
}

function displayUpInfo(fjid){
	if(!$("#ajaxForm #fjids").val()){
		$("#ajaxForm #fjids").val(fjid);
	}else{
		$("#ajaxForm #fjids").val($("#ajaxForm #fjids").val()+","+fjid);
	}
}

//上传图片点击开始
$("#ajaxForm #uploader").on("touchstart",function(){
	$("#uploader img").eq(0).attr("src","/images/addon.png");
})
//上传图片点击结束
$("#ajaxForm #uploader").on("touchend",function(){
	$("#uploader img").eq(0).attr("src","/images/add.png");
})

/**
 * 判断是否为微信内置浏览器
 */
function isFromWeiXin() {
	var ua = window.navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i) == 'micromessenger'){
		var system = {
			win: false,
			mac: false
		};
		//检测平台
		var p = navigator.platform;
		system.win = p.indexOf("Win") == 0;
		system.mac = p.indexOf("Mac") == 0;
		// 非微信PC端上打开内置浏览器不做操作
		if (system.win || system.mac) {
			$("#chrom").show();
			$("#phone").hide();
		}
	}else{
		$("#chrom").show();
		$("#phone").hide();
	}

}

$(document).ready(function(){
	showloading("正在初始化...")
	$("#ajaxForm #hzxxInfo").show()
	$("#ajaxForm #sjysInfo").hide()
	$("#ajaxForm #sjysAdd").hide()
	$("#ajaxForm #sjdwInfo").hide()
	if(!getWxOrNot()){
		return false;
	}
	isFromWeiXin();
	//0.初始化fileinput
	var oFileInput = new FileInput();
	oFileInput.Init("ajaxForm","displayUpInfo",2,1,"wechat_file");
	//绑定事件
	btnBind();
	//初始化页面数据
	initPage();
	//监听送检区分改变事件
	$('input:radio[name="sjqf"]').change( function(){
		refreshDB();
		initKyxm();
	})
	hideloading();
});

$(document.body).infinite().on("infinite", function() {
	if (loadingFlag){
		return;
	}
	loadingFlag = true
	if (pageFlag == "sjdwInfo"){
		loadMoreSjdw()
	}
});
//设置上一页为空
function pushHistory() {
	var state = {
		title: "title",
		url: "#"
	};
	window.history.pushState(state, "title", "#");
}
function goBack(){
	if($("#ajaxForm #hzxxInfo").is(":hidden")){
		pushHistory();
		if($("#ajaxForm #sjysInfo").is(":hidden")){
			//若 送检医生选择页面 是隐藏的
			if($("#ajaxForm #sjysAdd").is(":hidden")){
				//若 送检医生新增页面 是隐藏的
				if($("#ajaxForm #sjdwInfo").is(":hidden")){
					//若 送检单位选择页面 是隐藏的
					$("#ajaxForm #hzxxInfo").show()
					$("#ajaxForm #sjysInfo").hide()
					$("#ajaxForm #sjysAdd").hide()
					$("#ajaxForm #sjdwInfo").hide()
				}else {
					//若 送检单位选择页面 是显示的
					$("#ajaxForm #hzxxInfo").hide()
					$("#ajaxForm #sjysInfo").hide()
					$("#ajaxForm #sjysAdd").show()
					$("#ajaxForm #sjdwInfo").hide()
				}
			}else {
				//若 送检医生新增页面 是显示的
				$("#ajaxForm #hzxxInfo").hide()
				$("#ajaxForm #sjysInfo").show()
				$("#ajaxForm #sjysAdd").hide()
				$("#ajaxForm #sjdwInfo").hide()
			}
		}else {
			//若 送检医生选择页面 是显示的
			$("#ajaxForm #hzxxInfo").show()
			$("#ajaxForm #sjysInfo").hide()
			$("#ajaxForm #sjysAdd").hide()
			$("#ajaxForm #sjdwInfo").hide()
		}
	}
	else {
		window.history.go(navigationIndex)
	}
}

$(function(){
	navigationIndex += -1
	pushHistory()
	window.addEventListener("popstate", function(e) {
		goBack()
	}, false);
});

/**
 * 校验样本编号
 * @returns {*}
 */
function checkYbbh(){
	let ybbh = $("#ajaxForm #ybbh").val();
	if(!ybbh){
		return buttonDisabled(false,true,"请填写样本编号!",false);
	}
	//若样本编号中包含除英文、数字以及-以外的字符，则提示错误
	if(!/^[a-zA-Z0-9-]+$/.test(ybbh)){
		return buttonDisabled(false,true,"样本编号只能包含英文、数字以及-",false);
	}
	return buttonDisabled(true,false,"",true);
}

/**
 * 处理是否完善
 */
function dealSfwsFlag(){
	var sfws = $("#ajaxForm #sfws").val() ? $("#ajaxForm #sfws").val() : '0';
	if (sfws == '0' || sfws == '1'){
		//若是否完善为0或1，则表示第二页未完善
		$("#ajaxForm #sfws").val(checkIsPerfect()?'1':'0');
	}else{
		//若是否完善为2或者3，则表示第二页已完善
		$("#ajaxForm #sfws").val(checkIsPerfect()?'3':'2');
	}
}
/**
 * 校验所有出报告必填项是否均已填写
 * @returns {boolean}
 */
function checkIsPerfect() {
	//校验录单必填项
	if (!checkIsRequired()){
		return false;
	}
	//校验送检医生，完善必填项
	if (!$("#ajaxForm #sjys").val()) {
		return false;
	}
	if (!$("#ajaxForm #sjdwid").val()) {
		return false;
	}
	//校验报告显示单位，完善必填项
	if (!$("#ajaxForm #sjdwmc").val()) {
		return false;
	}
	//校验科室，完善必填项
	if (!$("#ajaxForm #ksid").val()) {
		return false;
	}
	//校验报告显示科室，完善必填项
	for (let i = 0; i < ksxxlist.length; i++) {
		if (ksxxlist[i].dwid == $("#ajaxForm #ksid").val()) {
			if (ksxxlist[i].kzcs == "1") {
				if (!$("#ajaxForm #qtks").val()) {
					return false;
				}
			}
		}
	}
	//校验快递类型，完善必填项
	if (!$("#ajaxForm input:radio[name='kdlx']:checked").val()) {
		return false;
	}
	//校验快递号,若快递类型不为无时，则必填，完善必填项
	if ($("#ajaxForm input:radio[name='kdlx']:checked").val() != qtkdlx) {
		if (!$("#ajaxForm #kdh").val()) {
			return false;
		}else if($("#ajaxForm #kdh").val().length>32){
            return false;
        }
	}
	return true;
}
function checkIsRequired() {
	//校验样本编号，录单必填项
	if (!$("#ajaxForm #ybbh").val()) {
		return false;
	}
	//校验患者姓名，录单必填项
	if (!$("#ajaxForm #hzxm").val()) {
		return false;
	}
	//校验性别，录单必填项
	if ($("#ajaxForm input:radio[name='xb']:checked").val() == "" || $("#ajaxForm input:radio[name='xb']:checked").val() == null) {
		return false;
	}
	//校验年龄，录单必填项
	if (!$("#ajaxForm #nl").val()) {
		return false;
	}
	//校验年龄单位，录单必填项
	if (!$("#ajaxForm #nldw").val()) {
		return false;
	}
	//校验检测单位，录单必填项
	if (!$("#ajaxForm #jcdwid").val()) {
		return false;
	}
	//校验合作伙伴，录单必填项
	if (!$("#ajaxForm #db").val()) {
		return false;
	}
	//校验送检区分，完善必填项
	if (!$("#ajaxForm input:radio[name='sjqf']:checked").val()) {
		return false;
	}
	//校验科研项目，若送检区分为科研项目，则必填，完善必填项
	if (!$("#ajaxForm #kyxm").val() && !$("#ajaxForm #kyxmDiv").is(":hidden")) {
		return false;
	}
	//校验检测项目，录单必填项
	if (!$("#ajaxForm #jcxmids").val()) {
		return false;
	}
	//校验检测子项目，录单必填项
	if (!$("#ajaxForm #jczxmDiv").is(":hidden")) {
		if (!$("#ajaxForm #jczxmids").val()) {
			return false;
		}
	}
	return true;
}

function showloading(text){
	//loading显示
	$.showLoading(text);
}

function hideloading(){
	setTimeout(function() {
		//loading隐藏
		$.hideLoading();

	},200)
}
//检测输入框内是否有输入空格，若有则去除
function clearSpace(){
	var value = event.target.value;
	while (value && value.indexOf(" ") != -1 && value.indexOf("　") != -1) {
		value = value.replace(" ", "").replace("　", "");
	}
	event.target.value = value;
}

function showInfo(){
	var actionFlag = $("#ajaxForm #actionFlag").val()=='1'?true:false;//true:完善，false:录单
	$.alert({
		title: '必填项提示',
		text: '<span style="font-weight: bold">当前为“'+(actionFlag?'完善':'录单') + '”流程</span><br>' +
			'<span style="color:red;">*</span>　为当前流程必填项' + '<span style="font-size: 10px">' + (actionFlag?'（完善/出报告）':'（录单/接收）　') + '</span><br>' +
			'<span style="color:#00AFEC;">*</span>　为' + (actionFlag?'上一流程必填项':'下一流程必填项') + '<span style="font-size: 10px">' +(actionFlag?'（录单/接收）　':'（完善/出报告）') + '</span>',
		onOK: function () {
			//点击确认
		}
	})
}