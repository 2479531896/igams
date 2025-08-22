//事件绑定
function btnBind() {
}


//下载模板
function xz(fjid){
	jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
		'<input type="text" name="fjid" value="'+fjid+'"/>' +
		'<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
		'</form>')
		.appendTo('body').submit().remove();
}
//删除模板
function del(fjid,wjlj,div){
	$.confirm('您确定要删除所选择的记录吗？',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			var url= "/common/file/delFile";
			jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
				setTimeout(function(){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							$("#"+fjid).remove();
						});
						$("#ajaxForm"+"  #"+div).remove();
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

function initPage() {
	if ($('#sfsd').is(':checked'))
		$('#sfsd').bootstrapSwitch('state', true);
	else
		$('#sfsd').bootstrapSwitch('state', false);
	var chk_jsid = $('#hzxx_ajaxForm input[name="jsids"]');
	$.each(chk_jsid, function(i) {
		if ($(chk_jsid[i]).is(':checked'))
			$(chk_jsid[i]).bootstrapSwitch('state', true);
	})
}
//添加日期控件
laydate.render({
	elem: '#nrsj'
	, theme: '#2381E9'
});
//添加日期控件
laydate.render({
	elem: '#hzxx_ajaxForm #bgsj'
	, theme: '#2381E9'
});
//添加日期控件
laydate.render({
	elem: '#rysj'
	, theme: '#2381E9'
});
//添加日期控件
laydate.render({
	elem: '#jlrqOne'
	, theme: '#2381E9'
});
//添加日期控件
laydate.render({
	elem: '#jlrqThree'
	, theme: '#2381E9'
});
//添加日期控件
laydate.render({
	elem: '#jlrqFive'
	, theme: '#2381E9'
});
//添加日期控件
laydate.render({
	elem: '#jlrqSeven'
	, theme: '#2381E9'
});

//添加日期控件
laydate.render({
	elem: '#cysj'
	, theme: '#2381E9'
	, type: 'datetime'
	, format: 'yyyy-MM-dd HH:mm'//保留时分
});
//添加日期控件
laydate.render({
	elem: '#cicusj'
	, theme: '#2381E9'
	, type: 'datetime'
	, format: 'yyyy-MM-dd HH:mm'//保留时分
});
//添加日期控件
laydate.render({
	elem: '#hxjtysj'
	, theme: '#2381E9'
	, type: 'datetime'
	, format: 'yyyy-MM-dd HH:mm'//保留时分
});
//添加日期控件
laydate.render({
	elem: '#crrttysj'
	, theme: '#2381E9'
	, type: 'datetime'
	, format: 'yyyy-MM-dd HH:mm'//保留时分
});
//添加日期控件
laydate.render({
	elem: '#xghxytysj'
	, theme: '#2381E9'
	, type: 'datetime'
	, format: 'yyyy-MM-dd HH:mm'//保留时分
});
function Addspgw(obj) {
	examine(obj);
	institutions(obj);
}
function institutions(obj) {
	var jsid = obj.id;
	var hzid = $("#hzxx_ajaxForm #hzid").val();
	if ($('#' + jsid).prop("checked")) {
		$.ajax({
			url: '/systemrole/user/pagedataDwxzbj',
			type: 'post',
			dataType: 'JSON',
			async: false,
			data: { "access_token": $("#ac_tk").val(), "jsid": jsid },
			success: function(data) {
				if (data != null && data.xtjsDto.dwxdbj == 1) {
					var divHtml = "<div class='col-md-12 col-sm-12' id='JG" + jsid + "' style='margin-bottom:20px;'>";
					divHtml += "<div class='col-md-6 col-sm-6' style='margin-bottom:10px;' >";
					divHtml += "<label for='' class='col-md-6 col-sm-6 control-label'>";
					divHtml += "<span>角色名称：</span>";
					divHtml += "</label>";
					divHtml += "<div class='col-sm-6 col-md-6  padding_t7'>";
					divHtml += "<input type='hidden' name='yhjgqxDtos[" + $("#jsjgDiv").children().size() + "].jsid'  value='" + data.xtjsDto.jsid + "'>";
					divHtml += "<span>" + data.xtjsDto.jsmc + "</span>";
					divHtml += "</div>";
					divHtml += "</div>";
					divHtml += "<div class='col-md-6 col-sm-6'>";
					divHtml += "<label for='' class='col-md-4 col-sm-4 control-label'>";
					divHtml += "<span>机构：</span>";
					divHtml += "</label>";
					divHtml += "<div class='col-sm-8 col-md-8' style='padding-right:0px;'>";
					divHtml += "<input type='text'  id='jgmcs'  name='noname' data-provide='typeahead' data-items='5' style='border:0px;border-bottom:1px solid #cccccc;width:130px;height:25px;outline:none;'>";
					divHtml += "</div>";
					divHtml += "</div>";
					divHtml += "<div class='col-md-12 col-sm-12'>";
					divHtml += "<label for='' class='col-md-3 col-sm-3 control-label' >";
					divHtml += "<span style='margin-right:7px;'>已选机构：</span>";
					divHtml += "</label>";
					divHtml += "<div class='col-md-9 col-sm-9' style='padding-right:0px;'>";
					divHtml += "<input   type='text' id='jgids' name='yhjgqxDtos[" + $("#jsjgDiv").children().size() + "].jgids'  class='form-control' >";
					divHtml += "</div>";
					divHtml += "</div>";
					divHtml += "</div>";
					$("#jsjgDiv").append(divHtml);
				}
				//修改时，已选机构添加显示
				if (hzid != null && hzid != "") {
					$.ajax({
						url: '/systemrole/user/pagedataListByjsid',
						type: 'post',
						dataType: 'JSON',
						async: false,
						data: { "access_token": $("#ac_tk").val(), "jsid": jsid, "hzid": hzid },
						success: function(_data) {
							if (_data.yhjgqxDtos != null) {
								for (var i = 0; i < _data.yhjgqxDtos.length; i++) {
									addtagsinput(jsid, _data.yhjgqxDtos[i].jgid, _data.yhjgqxDtos[i].jgmc);
								}
							}
						}
					})
				}
			}
		})
	} else {
		$("#jsjgDiv   " + "#JG" + jsid).remove();
	}
	jgysr(jsid);
}
//新增修改时 添加修改审批岗位
function examine(obj) {
	//定义一个全局变量
	var spgwDtos = {};
	var mod_flg;
	//ajxa查询出来所有的岗位
	$.ajax({
		url: '/systemrole/user/pagedataDtoList',
		type: 'post',
		dataType: 'JSON',
		async: false,
		data: { "access_token": $("#ac_tk").val() },
		success: function(data) {
			spgwDtos = data;//把岗位赋值给全局变量
		}
	})
	var jsid = obj.id; //获取当前的id即 jsid
	var jsmc = obj.title;//获取当前的title即 角色名称
	var hzid = $("#hzxx_ajaxForm #hzid").val();//获取到页面中的hzid；
	if ($('#' + jsid).prop("checked")) {
		if (hzid != null && hzid != "") {//如果用户id存在，说明当前执行的是修改方法，需要根据hzid和jsid查询出当前js下边的岗位信息
			var spgwcy = {} //定义一个空的map，用来接收当前的岗位信息
			$.ajax({
				url: '/systemrole/user/pagedataSpgwcyList',
				type: 'post',
				dataType: 'JSON',
				async: false,
				data: { "access_token": $("#ac_tk").val(), "hzid": hzid, "jsid": jsid },
				success: function(data) {
					spgwcy = data;//把岗位赋值给全局变量
				}
			})
		}
		var div = "<div class='col-md-12 col-sm-12 checknotnull' id='JG" + jsid + "'>";
		div += "<div class='form-group'>";
		div += "<label for='' class='col-md-12  control-label' style='text-align:left;'>";
		div += "<input type='hidden' name='xtjsDtos[" + $("#spgwDiv").children().size() + "].jsid'  value='" + jsid + "'>";
		div += "<span>" + jsmc + "</span>";
		div += "<span style='color: red;display:none;' id='alertJG" + jsid + "'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请选择审批岗位！</span>";
		div += "</label>";
		div += " <div class='col-md-12  col-sm-12'>";
		if (spgwDtos != null) {//判断如果审批岗位不为空 显示审批岗位
			for (var i = 0; i < spgwDtos.spgwDtos.length; i++) {
				div += "<div class='col-md-3'>";
				div += "<label class='checkboxLabel checkbox-inline'>";
				if (spgwcy != null && spgwcy.spgwcyDtos != "") {
					var cheeck_true = 0;
					$.each(spgwcy.spgwcyDtos, function(index, spgwcyDto) {
						if (spgwcyDto.gwid == spgwDtos.spgwDtos[i].gwid) {
							cheeck_true++;
						}
					})
					if (cheeck_true > 0) {
						div += "<input type='checkbox'   checked='checked' class='boxnotnull' name='xtjsDtos[" + $("#spgwDiv").children().size() + "].spgwDtos[" + i + "].gwid'   value='" + spgwDtos.spgwDtos[i].gwid + "'  />";
					} else {
						div += "<input type='checkbox'  class='boxnotnull' name='xtjsDtos[" + $("#spgwDiv").children().size() + "].spgwDtos[" + i + "].gwid'     value='" + spgwDtos.spgwDtos[i].gwid + "'   />";
					}
				} else {
					div += "<input type='checkbox'    class='boxnotnull' name='xtjsDtos[" + $("#spgwDiv").children().size() + "].spgwDtos[" + i + "].gwid'   value='" + spgwDtos.spgwDtos[i].gwid + "'   />";
				}
				div += "<span>" + spgwDtos.spgwDtos[i].gwmc + "</span>";
				div += "</label>";
				div += "</div>";
			}
		}
		div += "</div>";
		div += "</div>";
		div += "</div>";
		div += "</div>";
		$("#spgwDiv").append(div);
	} else {
		$("#spgwDiv   " + "#JG" + jsid).remove();
	}
}

/**
 * 机构名称模糊查询
 * @returns
 */
function jgysr(jsid) {
	$('#jsjgDiv #jgmcs').typeahead({
		source: function(query, process) {
			return $.ajax({
				url: '/systemrole/organ/pagedataJgxxByjgmc',
				type: 'post',
				data: {
					"jgmc": query,
					"access_token": $("#ac_tk").val()
				},
				dataType: 'json',
				success: function(result) {
					var resultList = result.jgxxList
						.map(function(item) {
							var aItem = {
								jgmc: item.jgmc,
								jgid: item.jgid,
							};
							return JSON.stringify(aItem);
						});
					return process(resultList);
				}
			});
		},
		matcher: function(obj) {
			var item = JSON.parse(obj);
			return ~item.jgmc.toLowerCase().indexOf(
				this.query.toLowerCase())
		},
		sorter: function(items) {
			var beginswith = [],
				caseSensitive = [],
				caseInsensitive = [],
				item;
			var aItem;
			while (aItem = items.shift()) {
				var item = JSON.parse(aItem);
				if (!item.jgmc.toLowerCase().indexOf(
					this.query.toLowerCase()))
					beginswith.push(JSON.stringify(item));
				else if (~item.jgmc.indexOf(this.query))
					caseSensitive.push(JSON.stringify(item));
				else
					caseInsensitive.push(JSON.stringify(item));
			}
			return beginswith.concat(caseSensitive,
				caseInsensitive)
		},
		highlighter: function(obj) {
			var item = JSON.parse(obj);
			var query = this.query.replace(
				/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
			return item.jgmc.replace(new RegExp('(' + query +
				')', 'ig'), function($1, match) {
					return '<strong>' + match + '</strong>'
				})
		},
		updater: function(obj) {
			var item = JSON.parse(obj);
			addtagsinput(jsid, item.jgid, item.jgmc);
			return item.jgmc;
		}
	});
}

function addtagsinput(jsid, jgid, jgmc) {
	$("#JG" + jsid + "  #jgids").tagsinput({
		itemValue: "value",
		itemText: "text",
	})
	$("#JG" + jsid + "  #jgids").tagsinput('add', { value: jgid, text: jgmc });
}

$('input[type=radio][name=yz]').change(function() {
	var myvalue = $(this).val();
	console.log(myvalue);
	var yzType = $("#" + myvalue).val();
	//由各种类型显示不同页面
	changeHide(yzType);

});
function changeHide(yzType) {
	if (yzType == 'ADULT') {
		$("#threeDay").show();
		$("#fiveDay").show();
		$("input[name='sfyc1']").each(function(j,item){
    		$("#"+item.value).hide();
  		});
		//隐藏
	}else if(yzType == 'IMMUNOSUPPRESSION'){
		$("#threeDay").show();
		$("#fiveDay").show();
		$("input[name='sfyc1']").each(function(j,item){
    		$("#"+item.value).show();
  		});
	}else if(yzType == 'CHILD'){
		$("#threeDay").hide();
		$("#fiveDay").hide();
		$("input[name='sfyc1']").each(function(j,item){
    		$("#"+item.value).hide();
  		});
	}else if(yzType == 'NON_INFECTION'){
		$("#threeDay").hide();
		$("#fiveDay").hide();
		$("input[name='sfyc1']").each(function(j,item){
    		$("#"+item.value).show();
  		});
	}
}

function displayUpInfo(fjid){
	if(!$("#hzxx_ajaxForm #fjids").val()){
		$("#hzxx_ajaxForm #fjids").val(fjid);
	}else{
		$("#hzxx_ajaxForm #fjids").val($("#hzxx_ajaxForm #fjids").val()+","+fjid);
	}
}

$(document).ready(function() {
	var sign_params=[];
	sign_params.prefix=$('#hzxx_ajaxForm #urlPrefix').val();
	var oFileInput = new FileInput();
	oFileInput.Init("hzxx_ajaxForm","displayUpInfo",2,1,"sign_file",null,sign_params);
	//绑定事件
	btnBind();
	//初始化页面数据
	initPage();
	//所有下拉框添加choose样式
		jQuery('#hzxx_ajaxForm .chosen-select').chosen({width: '100%'});
	console.log('report');
	
	var djtView = $("#djtView").val();
	jQuery('#hzxx_ajaxForm .chosen-select').chosen({ width: '100%' });
	if (djtView == '1') {
		$("#dyrTab").addClass("tab-pane fade active in");
		$("#oneDay").addClass("active");
	}
	if (djtView == '3') {
		$("#dsrTab").addClass("tab-pane fade active in");
		$("#threeDay").addClass("active");
	}
	if (djtView == '5') {
		$("#dwrTab").addClass("tab-pane fade active in");
		$("#fiveDay").addClass("active");
	}
	if (djtView == '7') {
		$("#dqrTab").addClass("tab-pane fade active in");
		$("#sevenDay").addClass("active");
	}
var cT = $('input[name="yz"]').filter(':checked').val();
 var yzType = $("#" + cT).val();
	//由各种类型显示不同页面
	changeHide(yzType);


});