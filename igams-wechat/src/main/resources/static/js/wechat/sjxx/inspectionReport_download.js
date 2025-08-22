

//上传后自动解析下载报告
var checkImpCheckStatus = function(intervalTime,fjid){
    var formname = "#inspectionReportDownload";
    $.ajax({
        type : "POST",
        url :"/inspection/inspection/pagedataZipDownload",
        data : {"fjid":fjid,"access_token":$("#ac_tk").val()},
        dataType : "json",
        success:function(data){
            if(data.status=="fail"){
                $(formname +" #errormsg").html(data.message);
                $(formname + " #errordiv").removeClass("hidden");
            }else{
                $(formname+"#confirmmsg").html(" 开始处理....");
                $("#inspectionReportDownload #totalCount").val(data.count);
                $(formname + " #confirmdiv").removeClass("hidden");
                setTimeout("checkReportZipStatus(2000,'"+ data.redisKey + "')",1000);
            }
        }
    });

};

//自动检查报告压缩进度
var checkReportZipStatus = function(intervalTime,redisKey){
    var formname = "#inspectionReportDownload";
	$.ajax({
		type : "POST",
		url : "/common/export/commCheckExport",
		data : {"wjid" : redisKey +"","access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.cancel){//取消导出则直接return
				return;
			}
			if(data.result == false){
				if(data.msg && data.msg!=""){
					$.error(data.msg);
					if($("#cardiv")) $("#cardiv").remove();
				}else{
					if(intervalTime < 5000)
						intervalTime = intervalTime + 1000;
					if($("#exportCount")){
						$("#confirmmsg").html($("#totalCount").text()+" 压缩中,第"+data.currentCount+"份,共"+$("#inspectionReportDownload #totalCount").val()+"份....")
						$(formname + " #confirmdiv").removeClass("hidden");
					}
					setTimeout("checkReportZipStatus("+intervalTime+",'"+ redisKey +"')",intervalTime);
				}
			}else{
				if(data.msg && data.msg!=""){
					$.error(data.msg);
					if($("#cardiv")) $("#cardiv").remove();
				}
				else{
					if($("#cardiv")) $("#cardiv").remove();
					window.open("/common/export/commDownloadExport?wjid="+redisKey + "&access_token="+$("#ac_tk").val());
					$(formname + " #confirmdiv").addClass("hidden");
				}
			}
		}
	});
}



var reportImp_ButtonInit = function(){
    var oInits = new Object();
    var formname ="#inspectionReportDownload";
    oInits.Init = function () {
        //取消按钮
        $(formname + " #btn-cancel").unbind("click").click(function(){
            $(formname + " #imp_file")
                .fileinput('clear')
                .fileinput('unlock');
            $(formname + " #imp_file")
                .parent()
                .siblings('.fileinput-remove')
                .hide();

        });

        //下载模板按钮
        $("#inspectionReportDownload #upload_inspection").unbind("click").click(function(){
            $("#reportInspectionForm #access_token").val($("#ac_tk").val());
            $("#reportInspectionForm").submit();
        });
    }
    return oInits;
}

function displayUpInfo(fjid){
    $("#inspectionReportDownload #fjid").val(fjid);
    //1.初始化Table
    setTimeout("checkImpCheckStatus(2000,'"+ fjid + "')",1000);
}

$(function () {
    //0.初始化fileinput,这是公用方法，调用comdefine.js里的方法
    var oFileInput = new FileInput();
    //ctrlName,callback,impflg,singleFlg,fileName,importType
    //参数说明 singleFlg 好像是标明 是否单文件上传限制，有点忘记了
    //impflg 用于后台判断是否为导入。1为导入，则开启线程进行导入  2：代表附件保存 3固定表头

    var document_params=[];
    document_params.prefix=$('#inspectionReportDownload #urlPrefix').val();
    oFileInput.Init("inspectionReportDownload","displayUpInfo",1,1,"imp_file","",document_params);
    //2.初始化Button的点击事件
    var oimpButtonInit = new reportImp_ButtonInit();
    oimpButtonInit.Init();
});

