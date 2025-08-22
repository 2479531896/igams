//信息显示配置
var showConfig = function(confirmmsg){
	var formname = "#countingImportForm";
	$(formname + " #errordiv").addClass("hidden");
	$(formname + " #infodiv").addClass("hidden");
	$(formname + " #confirmdiv").addClass("hidden");
	$(formname + " #warndiv").addClass("hidden");
	$(formname + " #btn-cancel").addClass("hidden");
	$(formname + " #btn-continue").addClass("hidden");
	$(formname + " #btn-complete").addClass("hidden");
	if(confirmmsg){
		$(formname + " #confirmmsg").html(confirmmsg);	
		$(formname + " #confirmdiv").removeClass("hidden");
		$(formname + " #btn-cancel").removeClass("hidden");
		$(formname + " #btn-continue").removeClass("hidden");
		existsMsg = true;
	}
	if (!existsMsg) {
		$(formname + " #infomsg").html("数据检查没有问题，请点击继续按钮继续或点击取消按钮取消上传。");	
		$(formname + " #infodiv").removeClass("hidden");
		$(formname + " #btn-cancel").removeClass("hidden");
		$(formname + " #btn-continue").removeClass("hidden");
	}
}
//上传后自动提交服务器检查进度
var checkImpCheckStatus = function(intervalTime,fjid){
	var formname = "#countingImportForm";
	$.ajax({
		type : "POST",
		url : "/common/file/checkImpInfo",
		data : {"fjid":fjid,"access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.endflg==1){
				if(data.errormsg && data.errormsg!=""){
					$(formname +" #errormsg").html(data.errormsg);
					showConfig(data.errormsg);
				}else if(data.warnmsg && data.warnmsg!=""){
					$(formname +" #warnmsg").html(data.warnmsg);
					showConfig(data.warnmsg);
				}else if(data.confirmmsg && data.confirmmsg!=""){
					$(formname +" #confirmmsg").html(data.confirmmsg);
					showConfig(data.confirmmsg);
					$(formname +" #tb_list").bootstrapTable('refresh');
				}else if(data.infomsg && data.infomsg!=""){
					$(formname +"#infomsg").html(data.infomsg);
					showConfig(data.infomsg);
					//1.初始化Table
					var oTable = new impSample_TableInit();
					oTable.Init();
				}else{
					showConfig(data.infomsg);
					//1.初始化Table
					var oTable = new impSample_TableInit(fjid);
					oTable.Init();
				}
			}else{
				if(data.errormsg && data.errormsg!=""){
					$(formname +" #errormsg").html(data.errormsg);
					showConfig(data.errormsg);
				}
				else{
					if(intervalTime < 2000)
						intervalTime = intervalTime + 1000;
					if($(formname +" #infomsg")){$(formname +" #infomsg").html(data.msg)}
					setTimeout("checkImpCheckStatus("+ intervalTime +",'"+fjid+"')",intervalTime)
				}
			}
		}
	});
};
//自动检查服务器插入数据库进度
var checkImpInsertStatus = function(intervalTime,fjid){
	var formname = "#countingImportForm";
	$.ajax({
		type : "POST",
		url : "/common/file/checkImpSave",
		data : {"fjid":fjid,"access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.insertEndflg==1){
				if($(formname +" #infomsg")){$(formname +" #infomsg").html(data.insertMsg)}
				$(formname + " #infodiv").removeClass("hidden");
				$(formname + " #btn-complete").removeClass("hidden");
			}else{
				if(intervalTime < 2000)
					intervalTime = intervalTime + 1000;
				if($(formname +" #infomsg")){$(formname +" #infomsg").html(data.insertMsg)}
				$(formname + " #infodiv").removeClass("hidden");
				setTimeout("checkImpInsertStatus("+ intervalTime +",'"+fjid+"')",intervalTime)
			}
		}
	});
};
var countingImp_ButtonInit = function(){
    var oInits = new Object();
    var formname ="#countingImportForm";
    oInits.Init = function () {
    	//取消按钮
    	$(formname + " #btn-cancel").unbind("click").click(function(){
    		$(formname + " #counting_file")
				.fileinput('clear')
				.fileinput('unlock');
			$(formname + " #counting_file")
				.parent()
				.siblings('.fileinput-remove')
				.hide();
			/*$('#countingImportForm #update_table').html('<table id="tb_list" class="table table-hover table-bordered"></table>');*/
			$(formname + " #errordiv").addClass("hidden");
			$(formname + " #infodiv").addClass("hidden");
			$(formname + " #confirmdiv").addClass("hidden");
			$(formname + " #warndiv").addClass("hidden");
			$(formname + " #btn-cancel").addClass("hidden");
			$(formname + " #btn-continue").addClass("hidden");
			$(formname + " #btn-complete").addClass("hidden");
    	});
    	//继续
    	$(formname + " #btn-continue").unbind("click").click(function(){
			$(formname + " #errordiv").addClass("hidden");
			$(formname + " #warndiv").addClass("hidden");
			$(formname + " #confirmdiv").addClass("hidden");
			$(formname + " #infodiv").addClass("hidden");
			$(formname + " #btn-cancel").addClass("hidden");
			$(formname + " #btn-continue").addClass("hidden");
			$(formname + " #btn-complete").addClass("hidden");
			var s_fjid = $("#countingImportForm #fjid").val();
    		$.ajax({
    			type : "POST",
    			url : "/common/file/saveImportRecord",
    			data : {"fjid":s_fjid,"access_token":$("#ac_tk").val(),"ywlx": $("#countingImportForm #ywlx").val(),"impflg":"3","kzcs4":$("#countingImportForm #jcdw").val()},
    			dataType : "json",
    			success:function(data){
    				if(data.status == "fail")
    				{
    					$.alert("服务器异常,请稍后再次尝试。错误信息：" + data.msg);
    					$(formname + " #btn-cancel").removeClass("hidden");
    					$(formname + " #btn-continue").removeClass("hidden");
    				}else{
        				setTimeout("checkImpInsertStatus(2000,'"+ s_fjid + "')",1000);
    				}
    			}
    		});
    	});
    	
    	//完成
    	$(formname + " #btn-complete").unbind("click").click(function(){
    		$(formname + " #counting_file")
			.fileinput('clear')
			.fileinput('unlock');
			$(formname + " #counting_file")
				.parent()
				.siblings('.fileinput-remove')
				.hide();
			/*$('#countingImportForm #update_table').html('<table id="tb_list" class="table table-hover table-bordered"></table>');*/
			$(formname + " #errordiv").addClass("hidden");
			$(formname + " #infodiv").addClass("hidden");
			$(formname + " #confirmdiv").addClass("hidden");
			$(formname + " #warndiv").addClass("hidden");
			$(formname + " #btn-cancel").addClass("hidden");
			$(formname + " #btn-continue").addClass("hidden");
			$(formname + " #btn-complete").addClass("hidden");
			
    	});
    	
    	
    	//下载模板按钮
    	$(formname + " #template_dw").unbind("click").click(function(){
    		$("#updateCountForm #access_token").val($("#ac_tk").val());
    		$("#updateCountForm").submit();
    	});
    }
    return oInits;
}
function displayUpInfo(fjid){
	$("#countingImportForm #fjid").val(fjid);
	//1.初始化Table
	setTimeout("checkImpCheckStatus(2000,'"+ fjid + "')",1000);
}
function checkjcdw(){
	var jcdw=$("#countingImportForm #jcdw").val();
	if(jcdw){
		$("#countingImportForm #jcdwDiv").hide();
	}else{
		$("#countingImportForm #jcdwDiv").show();
	}
}
$("#countingImportForm #jcdw").on('change',function(){
	checkjcdw();
})
$(function () {
	//0.初始化fileinput,这是公用方法，调用comdefine.js里的方法
	var oFileInput = new FileInput();
	oFileInput.Init("countingImportForm","displayUpInfo",3,1,"counting_file");
	//2.初始化Button的点击事件
    var oimpButtonInit = new countingImp_ButtonInit();
	oimpButtonInit.Init();
	checkjcdw();
	jQuery('#countingImportForm .chosen-select').chosen({width: '100%'});
});