var oldtwrlx="";
//点击文件上传
function editfile(){
	$("#fileDiv").show();
	$("#file_btn").hide();
}
//点击隐藏文件上传
function cancelfile(){
	$("#fileDiv").hide();
	$("#file_btn").show();
}

function changeYcqf(csdm,csid){
//	if(csdm=='OTHER_EXCEPTION'||csdm=='PRODUCT_EXCEPTION'){
	if(csdm!='NORMAL_EXCEPTION'&&csdm!='WECHAT_EXCEPTION'){
		$("#editExceptionForm #ybbh").text("");
		$("#editExceptionForm #hzxm").text("");
		$("#editExceptionForm #yblxmc").text("");
		$("#editExceptionForm #db").text("");
		$("#editExceptionForm #ycbt").val("");
		$("#editExceptionForm #ywid").val("");
	}
	$("#editExceptionForm #khmc").val("");
	$("#editExceptionForm #wlmc").text("");
	$("#editExceptionForm #gg").text("");
	$("#editExceptionForm #sl").text("");
	$("#editExceptionForm #scph").text("");
	$("#editExceptionForm #shrq").text("");
	$("#editExceptionForm #twr").val("");
	$("#editExceptionForm #twrmc").val("");
	$("#editExceptionForm #twrlx").val("");
	initYcqf(csdm,csid);
	console.log(csdm);
}
var yclxList = [];
function initYcqf(csdm,csid,yczqf,lx){
	if("NORMAL_EXCEPTION"==csdm){
		if("add"==$("#editExceptionForm #formAction").val()){
			$("#editExceptionForm #twrmc").val($("#editExceptionForm #mrtwrmc").val());
			$("#editExceptionForm #twr").val($("#editExceptionForm #mrtwr").val());
		}
		$("#editExceptionForm #twrDiv").show();
		$("#editExceptionForm #twrDiv").attr("disabled",false)
		$("#editExceptionForm #twr").attr("validate","{required:true}")
		if($("#editExceptionForm #sjids").val()){
            $("#editExceptionForm #jcdwDiv").show();
		}else{
            $("#editExceptionForm #row1").show();
            $("#editExceptionForm #row2").show();
            $("#editExceptionForm #row3").hide();
            $("#editExceptionForm #row4").hide();
            $("#editExceptionForm #row5").hide();
            $("#editExceptionForm #jcdwDiv").show();
		}
		$("#editExceptionForm #twrlxDiv").hide();
		$("#editExceptionForm #twrlxDiv").attr("disabled",true)
		$("#editExceptionForm #twrlx").attr("validate","{required:false}")
	}else if("WECHAT_EXCEPTION"==csdm){
		$("#editExceptionForm #twrDiv").show();
		$("#editExceptionForm #twrDiv").attr("disabled",false)
		$("#editExceptionForm #twr").attr("validate","{required:true}")
		if($("#editExceptionForm #sjids").val()){
            $("#editExceptionForm #jcdwDiv").show();
        }else{
            $("#editExceptionForm #row1").show();
            $("#editExceptionForm #row2").show();
            $("#editExceptionForm #row3").hide();
            $("#editExceptionForm #row4").hide();
            $("#editExceptionForm #row5").hide();
            $("#editExceptionForm #jcdwDiv").show();
		}
		if("add"==$("#editExceptionForm #formAction").val()){
			$("#editExceptionForm #twr").val("");
			$("#editExceptionForm #twrmc").val("");
		}
		$("#editExceptionForm #twrlxDiv").show();
		$("#editExceptionForm #twrlxDiv").attr("disabled",false)
		$("#editExceptionForm #twrlx").attr("validate","{required:true}")
	}else if("PRODUCT_EXCEPTION"==csdm){
		$("#editExceptionForm #twrDiv").hide();
		$("#editExceptionForm #twrDiv").attr("disabled",true)
		$("#editExceptionForm #twr").attr("validate","{required:false}")
		if($("#editExceptionForm #sjids").val()){
            $("#editExceptionForm #jcdwDiv").hide();
        }else{
            $("#editExceptionForm #row1").hide();
            $("#editExceptionForm #row2").hide();
            $("#editExceptionForm #row3").show();
            $("#editExceptionForm #row4").show();
            $("#editExceptionForm #row5").show();
            $("#editExceptionForm #jcdwDiv").hide();
		}
		$("#editExceptionForm #twrlxDiv").hide();
		$("#editExceptionForm #twrlxDiv").attr("disabled",true)
		$("#editExceptionForm #twrlx").attr("validate","{required:false}")
	}else{
		//OTHER_EXCEPTION
		$("#editExceptionForm #twrDiv").hide();
		$("#editExceptionForm #twrDiv").attr("disabled",true)
		$("#editExceptionForm #twr").attr("validate","{required:false}")
		if($("#editExceptionForm #sjids").val()){
            $("#editExceptionForm #jcdwDiv").hide();
        }else{
            $("#editExceptionForm #row1").hide();
            $("#editExceptionForm #row2").hide();
            $("#editExceptionForm #row3").hide();
            $("#editExceptionForm #row4").hide();
            $("#editExceptionForm #row5").hide();
            $("#editExceptionForm #jcdwDiv").hide();
		}
		$("#editExceptionForm #twrlxDiv").hide();
		$("#editExceptionForm #twrlxDiv").attr("disabled",true)
		$("#editExceptionForm #twrlx").attr("validate","{required:false}")
	}

	var formAction = $("#editExceptionForm #formAction").val();

	$.ajax({
		type:'post',
		url:$("#editExceptionForm #urlPrefix").val()+"/systemmain/data/ansyGetJcsjList",
		cache: false,
		async: false,
		data: {"fcsid":csid,"jclb":"EXCEPTION_SUBDISTINGUISH","access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			if(data != null && data.length != 0){
				$("#editExceptionForm #yczqfDiv").attr("style","display:open;");
				var html = "";
				$.each(data,function(i){
					html += "<label class='radio-inline'>";
					if(yczqf&&yczqf==data[i].csid){
						if("mod"==formAction){
							html +="<input type='radio' value="+data[i].csid+" csdm="+data[i].csdm+" checked name='yczqf' onchange='changeYczqf()' disabled validate='{required:true}'>"
						}else{
							html +="<input type='radio' value="+data[i].csid+" csdm="+data[i].csdm+" checked name='yczqf' onchange='changeYczqf()' validate='{required:true}'>"
						}
					}else{
						if("mod"==formAction){
							html +="<input type='radio' value="+data[i].csid+" csdm="+data[i].csdm+"  name='yczqf' onchange='changeYczqf()' disabled validate='{required:true}'>"
						}else{
							html +="<input type='radio' value="+data[i].csid+" csdm="+data[i].csdm+"  name='yczqf' onchange='changeYczqf()' validate='{required:true}'>"
						}
					}
					html +="<span>"+data[i].csmc+"</span>";
					html+="</label>";
				});
				$("#editExceptionForm #yczqf").empty();
				$("#editExceptionForm #yczqf").append(html);
			}else{
				$("#editExceptionForm #yczqf").empty();
				$("#editExceptionForm #yczqfDiv").attr("style","display:none;");
			}
		}
	});

	$.ajax({
		type:'post',
		async: false,
		url:$("#editExceptionForm #urlPrefix").val()+"/systemmain/data/ansyGetJcsjList",
		cache: false,
		data: {"fcsid":csid,"jclb":"EXCEPTION_TYPE","access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			if(data != null && data.length != 0){
			    yclxList = data;
				var lxHtml = "";
				lxHtml += "<option value=''>--请选择--</option>";
				$.each(data,function(i){
					if(lx&&lx==data[i].csid){
						lxHtml +="<option value='" + data[i].csid + "' cskz1='" + data[i].cskz1 + "' csdm='" + data[i].csdm + "'  selected>" + data[i].csmc + "</option>"
					}else{
						lxHtml +="<option value='" + data[i].csid + "' cskz1='" + data[i].cskz1 + "' csdm='" + data[i].csdm + "'>" + data[i].csmc + "</option>"
					}
					lxHtml += "";
				});
				$("#editExceptionForm #lx").empty();
				$("#editExceptionForm #lx").append(lxHtml);
				$("#editExceptionForm #lx").trigger("chosen:updated");
			}else{
				var lxHtml = "";
				lxHtml += "<option value=''>--请选择--</option>";
				$("#editExceptionForm #lx").empty();
				$("#editExceptionForm #lx").append(lxHtml);
				$("#editExceptionForm #lx").trigger("chosen:updated");
			}
		}
	});

	$.ajax({
		type:'post',
		url:$("#editExceptionForm #urlPrefix").val()+"/systemmain/data/ansyGetJcsjList",
		cache: false,
		async: false,
		data: {"fcsid":csid,"jclb":"EXCEPTION_STATISTICS","access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			if(data != null && data.length != 0){
				$("#editExceptionForm #yctjlxDiv").attr("style","display:open;");
				var html = "";
				$.each(data,function(i){
					html += "<label class='checkboxLabel checkbox-inline'>";
					var statisticsids=$("#statisticsids").val();
					if(statisticsids&&statisticsids.indexOf(data[i].csid)!=-1){
						html +="<input class='sjycstatistics' type='checkbox' value='"+data[i].csid+"' csdm='"+data[i].csdm+"' cskz1='"+data[i].cskz1+"' checked name='sjycstatisticsids'   validate='{required:true}'>"
					}else{
						html +="<input class='sjycstatistics' type='checkbox' value='"+data[i].csid+"' csdm='"+data[i].csdm+"' cskz1='"+data[i].cskz1+"'  name='sjycstatisticsids'   validate='{required:true}'>"
					}
					html +="<span>"+data[i].csmc+"</span>";
					html+="</label>";
				});
				$("#editExceptionForm #yctjlx").empty();
				$("#editExceptionForm #yctjlx").append(html);
			}else{
				$("#editExceptionForm #yctjlx").empty();
				$("#editExceptionForm #yctjlxDiv").attr("style","display:none;");
			}
			$("#editExceptionForm .sjycstatistics").click(function(e){
                if(!$("#lx").val()){
                    var cskz1s = [];
                    $("input[name='sjycstatisticsids']:checked").each(function(){
                        var cskz1 = $(this).attr("cskz1");
                        if(cskz1 != null && cskz1){
                            var options = document.getElementById('lx').options;
                            $.each(options,function(i){
                                if(options[i].getAttribute("csdm") && cskz1.indexOf(options[i].getAttribute("csdm"))>-1 ){
                                    options[i].selected = true;
				                    $("#editExceptionForm #lx").trigger("chosen:updated");
                                }
                            })
                        }
                    })
                }
            })
		}
	});
}

function yl(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url=$("#editExceptionForm #urlPrefix").val()+"/ws/sjxxpripreview/?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= $("#editExceptionForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',JPGMaterConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}

var JPGMaterConfig = {
		width		: "800px",
		offAtOnce	: true,  
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

function xz(fjid){
    jQuery('<form action="'+$("#editExceptionForm #urlPrefix").val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="fjid" value="'+fjid+'"/>' + 
            '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
}

function del(fjid,wjlj){
	$.confirm('您确定要删除所选择的记录吗？',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			var url= $("#editExceptionForm #urlPrefix").val()+"/common/file/delFile";
			jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
				setTimeout(function(){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							$("#"+fjid).remove();
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

function displayUpInfo(fjid){
	if(!$("#editExceptionForm #fjids").val()){
		$("#editExceptionForm #fjids").val(fjid);
	}else{
		$("#editExceptionForm #fjids").val($("#editExceptionForm #fjids").val()+","+fjid);
	}
}

/**
 * 绑定按钮事件
 */
function btnBind(){
	
	var sel_yclx = $("#editExceptionForm #lx");
	var sel_twrlx = $("#editExceptionForm #twrlx");
	//异常类别下拉框改变事件
	sel_yclx.unbind("change").change(function(){
		yclbEvent();
	});
	sel_twrlx.unbind("change").change(function(){
		twrlxEvent();
	});
	if($("#editExceptionForm #twrmc").val()==null || $("#editExceptionForm #twrmc").val()==''){
		$("#editExceptionForm #twrmc").val($("#editExceptionForm #mrtwrmc").val());
		$("#editExceptionForm #twr").val($("#editExceptionForm #mrtwr").val());
	}
	if($("#editExceptionForm #zlxmc").val()!=null && $("#editExceptionForm #zlxmc").val()!=''){
		$("#editExceptionForm #yczlx").attr("style","display:open;");
	}
}


/**
 * 提问人类型下拉事件
 */
function twrlxEvent(){
	var twrlx = $("#editExceptionForm #twrlx").val();
	if(twrlx != null &&twrlx!=""){
		if(twrlx!=oldtwrlx){
			oldtwrlx=twrlx;
			$("#editExceptionForm #twrmc").val("");
			$("#editExceptionForm #twr").val("");
		}

	}else{
		$("#editExceptionForm #twrmc").val("");
		$("#editExceptionForm #twr").val("");
	}
}
/**
 * 异常类别下拉框事件
 */
function yclbEvent(){
	var yclx = $("#editExceptionForm #lx").val();
	if(yclx == null || yclx==""){
		var zlbHtml = "";
		zlbHtml += "<option value=''>--请选择--</option>";
		$("#editExceptionForm #zlx").empty();
    	$("#editExceptionForm #zlx").append(zlbHtml);
		$("#editExceptionForm #zlx").trigger("chosen:updated");
		return;
	}
	$.ajax({ 
	    type:'post',  
	    url:$("#editExceptionForm #urlPrefix").val()+"/systemmain/data/ansyGetJcsjList",
	    cache: false,
	    data: {"fcsid":yclx,"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	if(data != null && data.length != 0){
	    		$("#editExceptionForm #yczlx").attr("style","display:open;");
	    		$("#editExceptionForm #zlx").attr("disabled",false);
	    		var zlbHtml = "";
	    		zlbHtml += "<option value=''>--请选择--</option>";
            	$.each(data,function(i){   		
            		zlbHtml += "<option value='" + data[i].csid + "'>" + data[i].csmc + "</option>";
            	});
            	$("#editExceptionForm #zlx").empty();
            	$("#editExceptionForm #zlx").append(zlbHtml);
            	$("#editExceptionForm #zlx").trigger("chosen:updated");
	    	}else{
	    		var zlbHtml = "";
	    		zlbHtml += "<option value=''>--请选择--</option>";
	    		$("#editExceptionForm #yczlx").attr("style","display:none;");
	    		$("#editExceptionForm #zlx").attr("disabled","disabled");
	    		$("#editExceptionForm #zlx").empty();
            	$("#editExceptionForm #zlx").append(zlbHtml);
            	$("#editExceptionForm #zlx").trigger("chosen:updated");
	    	}
	    }
	});
}

// 确认人员、角色 选择方法
function chooseQrr() {
	var qrlx = $("#editExceptionForm #qrlx").val();
	if(!qrlx){
		$.error("请选择确认类型！");
		return false;
	}
	if (qrlx == "ROLE_TYPE") {
		url = $("#editExceptionForm #urlPrefix").val()+"/role/role/pagedataRoleList?access_token=" + $("#ac_tk").val();
		$.showDialog(url, '选择确认角色', chooseQrjsConfig);
	} else if (qrlx == "USER_TYPE") {
		var url = $("#editExceptionForm #urlPrefix").val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
		$.showDialog(url, '选择确认人员', chooseQrryConfig);
	} else {
		$("#qrryjs").addClass("hidden");
	}
}

// 通知角色选择框
var chooseQrjsConfig = {
	width: "800px",
	height: "500px",
	modalName: "chooseQrjsModal",
	formName: "exceptionList_Form",
	offAtOnce: true, // 当数据提交成功，立刻关闭窗口
	buttons: {
		success: {
			label: "确 定",
			className: "btn-primary",
			callback: function () {
				if (!$("#RoleForm").valid()) {
					return false;
				}
				var $this = this;
				var opts = $this["options"] || {};
				var sel_row = $('#RoleForm #tb_list').bootstrapTable('getSelections');// 获取选择行数据
				if (sel_row.length == 1) {
					$('#editExceptionForm #qrr').val(sel_row[0].jsid);
					$('#editExceptionForm #qrrmc').val(sel_row[0].jsmc);
					$.closeModal(opts.modalName);
				} else {
					$.error("请选中一行");
					return false;
				}
				return false;
			}
		},
		cancel: {
			label: "关 闭",
			className: "btn-default"
		}
	}
};

// 确认人员选择框
var chooseQrryConfig = {
	width: "800px",
	height: "500px",
	modalName: "chooseFzrModal",
	formName: "exceptionList_Form",
	offAtOnce: true, // 当数据提交成功，立刻关闭窗口
	buttons: {
		success: {
			label: "确 定",
			className: "btn-primary",
			callback: function () {
				if (!$("#taskListFzrForm").valid()) {
					return false;
				}
				var $this = this;
				var opts = $this["options"] || {};
				var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');// 获取选择行数据
				if (sel_row.length == 1) {
					$('#editExceptionForm #qrr').val(sel_row[0].yhid);
					$('#editExceptionForm #qrrmc').val(sel_row[0].zsxm);
					$.closeModal(opts.modalName);
				} else {
					$.error("请选中一行");
					return;
				}
				return false;
			}
		},
		cancel: {
			label: "关 闭",
			className: "btn-default"
		}
	}
};

function chooseTwr() {
	$('input:radio[name="ycqf"]').each( function(){
		var flag=$(this).prop("checked");
		if(flag){
			if("WECHAT_EXCEPTION"==$(this).attr("csdm")){
				var twrlx = $("#editExceptionForm #twrlx").val();
				if(twrlx){
					if("WECHAT"==twrlx){
						var url = $("#editExceptionForm #urlPrefix").val()+"/wechat/user/pagedataListUser?access_token=" + $("#ac_tk").val();
						$.showDialog(url, '选择提问人', chooseWxyhConfig);
					}else if("WEB"==twrlx){
						var url = $("#editExceptionForm #urlPrefix").val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
						$.showDialog(url, '选择提问人', chooseTwrConfig);
					}
				}else{
					$.error("请选择提问人类型！");
					return;
				}
			}else{
				var url = $("#editExceptionForm #urlPrefix").val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
				$.showDialog(url, '选择提问人', chooseTwrConfig);
			}
		}
	})
}
var chooseWxyhConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseWxyhModal",
	formName	: "editExceptionForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#userListWxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var selWx = $("#userListWxForm #yxyh").tagsinput('items');
				$("#editExceptionForm  #cskz1").tagsinput({
					itemValue: "value",
					itemText: "text",
				})
				if(selWx.length>1){
					$.error("请选择一个微信用户！");
					return false;
				}else if(selWx.length==0||selWx.length==undefined){
					$.error("请选择一个微信用户！");
					return false;
				}
				$('#editExceptionForm #twr').val(selWx[0].value);
				$('#editExceptionForm #twrmc').val(selWx[0].text);
				$.closeModal(opts.modalName);
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function chooseTzry() {
	var url = $("#editExceptionForm #urlPrefix").val()+"/exception/exception/pagedataChooseTzry?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择通知人员', chooseTzryConfig);
}


$("#editExceptionForm #qrlx").change(function(){
	$("#editExceptionForm  #qrr").val("");
	$("#editExceptionForm  #qrrmc").val("");
})

function changeYczqf(){
	$("#editExceptionForm #ywid").val("");
	$("#editExceptionForm #ybbh").text("");
	$("#editExceptionForm #hzxm").text("");
	$("#editExceptionForm #yblxmc").text("");
	$("#editExceptionForm #db").text("");
	$("#editExceptionForm #khmc").val("");
	$("#editExceptionForm #wlmc").text("");
	$("#editExceptionForm #gg").text("");
	$("#editExceptionForm #sl").text("");
	$("#editExceptionForm #scph").text("");
	$("#editExceptionForm #shrq").text("");
	$("#editExceptionForm #ycbt").val("");
	$("#editExceptionForm #twr").val("");
	$("#editExceptionForm #twrmc").val("");
}

function change(){
	var url =$("#editExceptionForm #urlPrefix").val()+ "/exception/exception/pagedataSjxxChance?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择送检信息', chooseSjxxConfig);
}

var chooseTwrConfig = {
		width : "800px",
		height : "500px",
		modalName	: "chooseFzrModal",
		formName	: "editExceptionForm",
		offAtOnce : true, // 当数据提交成功，立刻关闭窗口
		buttons : {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#taskListFzrForm").valid()){
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
					if(sel_row.length==1){
						$('#editExceptionForm #twr').val(sel_row[0].yhid);
						$('#editExceptionForm #twrmc').val(sel_row[0].zsxm);
						$.closeModal(opts.modalName);
		    		}else{
		    			$.error("请选中一行");
		    			return;
		    		}
					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};


var chooseTzryConfig = {
		width : "800px",
		height : "500px",
		modalName	: "chooseTzryModal",
		formName	: "editExceptionForm",
		offAtOnce : true, // 当数据提交成功，立刻关闭窗口
		buttons : {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					var tzrys = $("#chooseTzryForm #tzrys").tagsinput('items');
					$("#editExceptionForm #tzrys").tagsinput({
						itemValue: "value",
						itemText: "text",
					})
					$("#editExceptionForm  #tzrys").tagsinput('removeAll');
					for(var i = 0; i < tzrys.length; i++){
						var value = tzrys[i].value;
						var text = tzrys[i].text;
						$("#editExceptionForm #tzrys").tagsinput('add', {"value":value,"text":text});
					}
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

var chooseSjxxConfig = {
		width : "800px",
		height : "500px",
		modalName	: "chooseSjxxModal",
		formName	: "editExceptionForm",
		offAtOnce : true, // 当数据提交成功，立刻关闭窗口
		buttons : {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#chanceSjxxForm").valid()){
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					var sel_row = $('#chanceSjxxForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
					if(sel_row.length==1){
						$('#editExceptionForm #ybbh').text(sel_row[0].ybbh);
						$('#editExceptionForm #hzxm').text(sel_row[0].hzxm);
						$('#editExceptionForm #yblxmc').text(sel_row[0].yblxmc);
						$('#editExceptionForm #db').text(sel_row[0].db);
						$('#editExceptionForm #ywid').val(sel_row[0].sjid);
						$("#editExceptionForm #"+sel_row[0].jcdw).prop("selected","selected");
						$("#editExceptionForm #"+sel_row[0].jcdw).trigger("chosen:updated");
						$('#editExceptionForm #ycbt').val(sel_row[0].hzxm+'-'+sel_row[0].yblxmc+'-'+sel_row[0].db);
						$.closeModal(opts.modalName);
		    		}else{
		    			$.error("请选中一行");
		    			return;
		    		}
					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
var switchoverflagE = 2;
function switchoverE(){
	if (switchoverflagE == 1){
		$("#editExceptionForm #text").show();
		$("#editExceptionForm #textD").hide();
		switchoverflagE= 2;
		$("#editExceptionForm #code").show();
		$("#editExceptionForm #qrcode").hide();
	}else{
		$("#editExceptionForm #textD").show();
		$("#editExceptionForm #text").hide();
		switchoverflagE= 1;
		$("#editExceptionForm #qrcode").show();
		$("#editExceptionForm #code").hide();
	}
}

function connectExceptionMessage(key) {
	var source = null;
	// 用时间戳模拟登录用户
	if (!!window.EventSource) {
		// 建立连接
		source = new EventSource($("#editExceptionForm #urlPrefix").val()+"/sseEmit/connect/pagedataOA?access_token=" + $("#ac_tk").val()+"&key="+key);
		/**
		 * 连接一旦建立，就会触发open事件
		 * 另一种写法：source.onopen = function (event) {}
		 */
		source.addEventListener('open', function (e) {
			console.log("建立连接。。。");
		}, false);
		/**
		 * 客户端收到服务器发来的数据
		 * 另一种写法：source.onmessage = function (event) {}
		 */
		source.addEventListener('message', function (e) {
			setExceptionMessageInnerHTML(e.data);
		});
		/**
		 * 如果发生通信错误（比如连接中断），就会触发error事件
		 * 或者：
		 * 另一种写法：source.onerror = function (event) {}
		 */
		source.addEventListener('error', function (e) {
			if (e.readyState === EventSource.CLOSED) {
				console.log("连接关闭");
			} else {
				closeSse();
			}
		}, false);
	} else {
		console.log("你的浏览器不支持SSE");
	}
	// 监听窗口关闭事件，主动去关闭sse连接，如果服务端设置永不过期，浏览器关闭后手动清理服务端数据
	window.onbeforeunload = function () {
		closeSse();
		if ($("#editExceptionForm #key").val()){
			$.ajax({
				url: $("#editExceptionForm #urlPrefix").val()+"/sseEmit/connect/pagedataCloseOA",
				data: {
					"access_token": $("#ac_tk").val(),
					"key": $("#editExceptionForm #key").val(),
				}
			});
		}
	};
	window.addEventListener('message', (event) => {
		$.ajax({
			url: $("#editExceptionForm #urlPrefix").val()+"/sseEmit/connect/pagedataCloseOA",
			data: {
				"access_token": $("#ac_tk").val(),
				"key": $("#editExceptionForm #key").val(),
			}
		});
		closeSse();
	})
	// 关闭Sse连接
	function closeSse() {
		source.close();
		// $.ajax({
		// 	url: "/sseEmit/connect/pagedataCloseOA",
		// 	data: {
		// 		"access_token": $("#ac_tk").val(),
		// 		"userId": $("#editExceptionForm #yhid").val(),
		// 	},
		// 	success: function (data) {},
		// 	error: function () {
		// 		$.error("连接手机失败，请联系管理员解决原因！");
		// 	}
		// });

		console.log("close");
	}

	// 将消息显示在网页上
	function setExceptionMessageInnerHTML( res) {
		//处理读到的数据

		var obj = JSON.parse(res);
		//显示到页面
		if (obj) {
			if (obj.key == $("#editExceptionForm #key").val()){
				if (obj.fjids){
					if ( $("#editExceptionForm #fjids").val()){
						$("#editExceptionForm #fjids").val($("#editExceptionForm #fjids").val()+","+obj.fjids)
					}else{
						$("#editExceptionForm #fjids").val(obj.fjids);
					}
					if (obj.fjcfbDtoList && obj.fjcfbDtoList.length>0){
						var html = "";
						for (let i = 0; i < obj.fjcfbDtoList.length; i++) {
							if ($("#editExceptionForm #bs").val() != "1"){
								html +="<label  class=\"col-md-2 col-sm-4 col-xs-12 control-label \"></label>";
							}else{
								html +="<label class=\"col-md-2 col-sm-4 col-xs-12 control-label \">附件：</label>";
							}
							html += "<div class=\"col-md-10 col-sm-8 col-xs-12\" id=\""+obj.fjcfbDtoList[i].fjid+"\" >";
							html += "<label>"+($("#editExceptionForm #bs").val())+".</label>";
							$("#editExceptionForm #bs").val($("#editExceptionForm #bs").val()*1+1);
							html += " <button style=\"outline:none;margin-bottom:5px;padding:0px;\" onclick=\"xzExceptionTemporary(\'"+obj.fjcfbDtoList[i].wjlj+"\',\'"+obj.fjcfbDtoList[i].wjm+"\')\" class=\"btn btn-link\" type=\"button\" title=\"下载\">";
							html += " <span>"+obj.fjcfbDtoList[i].wjm+"</span>";
							html += " <span class=\"glyphicon glyphicon glyphicon-save\"></span>";
							html += " </button>";
							html += " <button title=\"删除\" class=\"f_button\" type=\"button\" onclick=\"delExceptionTemporary(\'"+obj.fjcfbDtoList[i].fjid+"\')\">";
							html += " <span class=\"glyphicon glyphicon-remove\"></span>";
							html += " </button>";
							html += " <button title=\"预览\" class=\"f_button\" type=\"button\" onclick=\"ylExceptionTemporary(\'"+obj.fjcfbDtoList[i].fjid+"\',\'"+obj.fjcfbDtoList[i].wjm+"\')\">";
							html += " <span  class=\"glyphicon glyphicon-eye-open\"></span>";
							html += " </button>";
							html += "</div>";
						}
						$("#editExceptionForm #redisFj").append(html);
					}
				}
			}
		}

	}

}

function xzExceptionTemporary(wjlj,wjm){
	jQuery('<form action="'+$("#editExceptionForm #urlPrefix").val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
		'<input type="text" name="wjlj" value="'+wjlj+'"/>' +
		'<input type="text" name="wjm" value="'+wjm+'"/>' +
		'<input type="text" name="temporary" value="temporary"/>' +
		'<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
		'</form>')
		.appendTo('body').submit().remove();
}

function delExceptionTemporary(fjid){
	$.confirm('您确定要删除所选择的记录吗？',function(result){
		if(result){
			$("#editExceptionForm #redisFj #"+fjid).remove();
			var strings = $("#editExceptionForm  #fjids").val().split(",");
			if (null!= strings && strings.length>0){
				var string = "";
				var xh=0;
				for (let i = 0; i < strings.length; i++) {
					if (strings[i] != fjid){
						string += ","+strings[i];
						xh+=1;
					}
				}
				$("#editExceptionForm  #fjids").val(string.substring(1));
				$("#editExceptionForm  #bs").val(xh+1);
				if(!string){
					$("#editExceptionForm #redisFj").empty();
				}
			}
		}
	});
}

function ylExceptionTemporary(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url=$("#editExceptionForm #urlPrefix").val()+"/ws/sjxxpripreview/?fjid="+fjid+"&temporary=temporary"
		$.showDialog(url,'图片预览',JPGExceptionMaterConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}

var JPGExceptionMaterConfig = {
	width		: "800px",
	offAtOnce	: true,
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function chooseProduction(){
	var csdm="";
	$('input:radio[name="yczqf"]').each( function(){
		var flag=$(this).prop("checked");
		if(flag){
			csdm=$(this).attr("csdm");
		}
	})
	if(!csdm){
		$.error("请选择异常子区分！");
		return;
	}else{
		if(csdm=="XS"){
			var url=$("#editExceptionForm #urlPrefix").val()+"/exception/exception/pagedataSalesDetails?access_token=" + $("#ac_tk").val();
			$.showDialog(url, '选择销售', chooseXsConfig);
		}else if(csdm=="JY"){
			var url=$("#editExceptionForm #urlPrefix").val()+"/exception/exception/pagedataBorrowingDetails?access_token=" + $("#ac_tk").val();
			$.showDialog(url, '选择借用', chooseJyConfig);
		}else if(csdm=="LL"){
			var url=$("#editExceptionForm #urlPrefix").val()+"/exception/exception/pagedataPickingDetails?access_token=" + $("#ac_tk").val();
			$.showDialog(url, '选择领料', chooseLlConfig);
		}
		console.log(csdm);
	}
}

var chooseXsConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseXsModal",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				var sel_row = $('#chooseSale_formSearch #saleDetails_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length!=1){
					$.error("请选择一行数据！");
					return;
				}
				$('#editExceptionForm #khmc').val(sel_row[0].khmc);
				$('#editExceptionForm #wlmc').text(sel_row[0].wlmc);
				$('#editExceptionForm #gg').text(sel_row[0].gg);
				$('#editExceptionForm #sl').text(sel_row[0].fhsl);
				$('#editExceptionForm #scph').text(sel_row[0].scph);
				$('#editExceptionForm #ywid').val(sel_row[0].fhmxid);
				$('#editExceptionForm #ycbt').val(sel_row[0].khmc+'-'+sel_row[0].wlmc);
				$('#editExceptionForm #shrq').text(sel_row[0].djrq);
				$.closeModal(opts.modalName);
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var chooseJyConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseJyModal",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				var sel_row = $('#chooseBorrow_formSearch #borrowing_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length!=1){
					$.error("请选择一行数据！");
					return;
				}
				$('#editExceptionForm #khmc').val(sel_row[0].khmc);
				$('#editExceptionForm #wlmc').text(sel_row[0].wlmc);
				$('#editExceptionForm #gg').text(sel_row[0].gg);
				$('#editExceptionForm #sl').text(sel_row[0].jysl);
				$('#editExceptionForm #scph').text(sel_row[0].scph);
				$('#editExceptionForm #ywid').val(sel_row[0].jymxid);
				$('#editExceptionForm #ycbt').val(sel_row[0].khmc+'-'+sel_row[0].wlmc);
				$('#editExceptionForm #shrq').text(sel_row[0].jyrq);
				$.closeModal(opts.modalName);
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var chooseLlConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseLlModal",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				var sel_row = $('#choosePick_formSearch #picking_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length!=1){
					$.error("请选择一行数据！");
					return;
				}
				$('#editExceptionForm #khmc').val(sel_row[0].khmc);
				$('#editExceptionForm #wlmc').text(sel_row[0].wlmc);
				$('#editExceptionForm #gg').text(sel_row[0].gg);
				$('#editExceptionForm #sl').text(sel_row[0].cksl);
				$('#editExceptionForm #scph').text(sel_row[0].scph);
				$('#editExceptionForm #ywid').val(sel_row[0].ckmxid);
				$('#editExceptionForm #ycbt').val(sel_row[0].khmc+'-'+sel_row[0].wlmc);
				$('#editExceptionForm #shrq').text(sel_row[0].ckrq);
				$.closeModal(opts.modalName);
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
$('#addExceptionModal').on('hide.bs.modal', function (e) {
	// 模态框右上角叉叉关闭模态框时关闭长链接
	$.ajax({
		url: "/sseEmit/connect/pagedataCloseOA",
		data: {
			"access_token": $("#ac_tk").val(),
			"key": $("#editExceptionForm #key").val(),
		}
	});
});
$('#modExceptionModal').on('hide.bs.modal', function (e) {
	// 模态框右上角叉叉关闭模态框时关闭长链接
	$.ajax({
		url: "/sseEmit/connect/pagedataCloseOA",
		data: {
			"access_token": $("#ac_tk").val(),
			"key": $("#editExceptionForm #key").val(),
		}
	});
});
$(function(){
	if($("#editExceptionForm #zlx").val()!=null && $("#editExceptionForm #zlx").val()!=''){
		$("#editExceptionForm #zlx").attr("disabled","disabled");
	}
	var ids=$("#tzrys").val();
	if(ids){
		var jsids="";
		var yhids="";
		var split = ids.split(",");
		for(var i=0;i<split.length;i++){
			if(split[i].indexOf("ROLE_TYPE")!=-1){
				jsids=jsids+","+split[i].split("-")[1];
			}else{
				yhids=yhids+","+split[i].split("-")[1];
			}
		}
		if(jsids){
			jsids=jsids.substring(1);
			$.ajax({
				type:'post',
				url:$("#editExceptionForm #urlPrefix").val()+"/systemrole/user/pagedataGetXtjs",
				cache: false,
				data: {"ids":jsids,"access_token":$("#ac_tk").val()},
				dataType:'json',
				success:function(data){
					if(data.xtjslist!=null && data.xtjslist!=''){
						var reg = new RegExp("jsid","g");
						var reg_t= new RegExp("jsmc","g");
						var json=JSON.stringify(data.xtjslist);
						json=json.replace(reg,"value");
						json=json.replace(reg_t,"text");
						$("#editExceptionForm  #tzrys").tagsinput({
							itemValue: "value",
							itemText: "text",
						})
						var jsonStr=eval('('+json+')');
						for (var i = 0; i < jsonStr.length; i++) {
							jsonStr[i].value="ROLE_TYPE-"+jsonStr[i].value;
							$("#editExceptionForm  #tzrys").tagsinput('add',jsonStr[i]);
						}
					}
				}
			});
		}
		if(yhids){
			yhids=yhids.substring(1);
			$.ajax({
				type:'post',
				url:$("#editExceptionForm #urlPrefix").val()+"/systemrole/common/pagedataUserListByIds",
				cache: false,
				data: {"ids":yhids,"sfjs":"1","access_token":$("#ac_tk").val()},
				dataType:'json',
				success:function(data){
					if(data.users!=null && data.users!=''){
						var reg = new RegExp("yhid","g");
						var reg_t= new RegExp("zsxm","g");
						var json=JSON.stringify(data.users);
						json=json.replace(reg,"value");
						json=json.replace(reg_t,"text");
						$("#editExceptionForm  #tzrys").tagsinput({
							itemValue: "value",
							itemText: "text",
						})
						var jsonStr=eval('('+json+')');
						for (var i = 0; i < jsonStr.length; i++) {
							jsonStr[i].value="USER_TYPE-"+jsonStr[i].value;
							$("#editExceptionForm  #tzrys").tagsinput('add',jsonStr[i]);
						}
					}
				}
			});
		}
	}

	if ($("#editExceptionForm #key").val()){
		connectExceptionMessage($("#editExceptionForm #key").val());
		if($("#editExceptionForm #dingtalkurl").val()) {
			$("#editExceptionForm #qrcode").qrcode({
				render: 'div',
				size: 200,
				text: $("#editExceptionForm #dingtalkurl").val()
			})
		}
		if($("#editExceptionForm #switchoverurl").val()) {
			$("#editExceptionForm #code").qrcode({
				render: 'div',
				size: 200,
				text: $("#editExceptionForm #switchoverurl").val()
			})
		}
	}
	var formAction = $("#editExceptionForm #formAction").val();
	$('input:radio[name="ycqf"]').each( function(){
		var flag=$(this).prop("checked");
		if(flag){
			if("mod"==formAction){
				initYcqf($(this).attr("csdm"),$(this).val(),$("#editExceptionForm #yyczqf").val(),$("#editExceptionForm #ylx").val());
			}else{
				initYcqf($(this).attr("csdm"),$(this).val());
			}
		}
	})

	var oFileInput = new FileInput();
	var sign_params=[];
	sign_params.prefix=$('#editExceptionForm #urlPrefix').val();
	oFileInput.Init("editExceptionForm","displayUpInfo",2,1,"pro_file",null,sign_params);
	btnBind();
	if($("#editExceptionForm #sjids").val()){
        $("#editExceptionForm #row1").hide();
        $("#editExceptionForm #row2").hide();
        $("#editExceptionForm #row3").hide();
        $("#editExceptionForm #row4").hide();
        $("#editExceptionForm #row5").hide();
    }
	// 所有下拉框添加choose样式
    jQuery('#editExceptionForm .chosen-select').chosen({width: '100%'});
})


