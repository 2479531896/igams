function showTable(){
	$("#verifyModAuditForm #fjcfbDto2").attr("style","")
	$("#verifyModAuditForm #show").attr("style","display: none")
	$("#verifyModAuditForm #hide").attr("style","display: block")
}
function hideTable(){
	$("#verifyModAuditForm #fjcfbDto2").attr("style","display: none;")
	$("#verifyModAuditForm #show").attr("style","display: block")
	$("#verifyModAuditForm #hide").attr("style","display: none")
}

/**
 * 修改备注
 */
function modBz() {
	$.confirm('确定要修改备注?',function(result){
		if(result){
			var yzid=$("#verifyModAuditForm #yzid").val();
			var bz=$("#verifyModAuditForm #bz").val();
			var url =$("#verifyModAuditForm #url").val();
			$.ajax({
				url:url,
				type : "POST",
				data:{"yzid":yzid,"bz":bz,"access_token":$("#ac_tk").val()},
				dataType:"json",
				success:function(data){
					if(data.status){
						$.success("修改成功！");
					}else{
						$.error("修改失败！");
					}
				}
			})
		}
	})

}

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

function view(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif" || type.toLowerCase()==".png"){
		var url="/ws/sjxxpripreview/?fjid="+fjid
		$.showDialog(url,'图片预览',viewPreViewConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= "/common/file/pdfPreview?fjid=" + fjid;
		 $.showDialog(url,'文件预览',viewPreViewConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}

function xz(fjid){
    jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="fjid" value="'+fjid+'"/>' + 
            '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
}

function del(fjid,wjlj){
	$.confirm('您确定要删除所选择的记录吗？',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			var url= "/common/file/delFileOnlyFjcfb";
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

var viewPreViewConfig = {
	    width        : "900px",
	    height        : "800px",
	    offAtOnce    : true,  //当数据提交成功，立刻关闭窗口
	    buttons        : {
	        cancel : {
	            label : "关 闭",
	            className : "btn-default"
	        }
	    }
	};

var showConfig = function(confirmmsg){
	
}
//上传后自动提交服务器检查进度
var checkImpCheckStatus = function(intervalTime,fjid){
	var formname = "#verifyModAuditForm";
	$.ajax({
		type : "POST",
		url : "/common/file/checkImpInfo",
		data : {"fjid":fjid,"access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.endflg==1&&data.confirmmsg){
				$("#verifyModAuditForm #confirmdiv").show()
				$("#verifyModAuditForm #confirmmsg").html("");
				$("#verifyModAuditForm #confirmmsg").html(data.confirmmsg);	
			}/*else{
				$("#verifyModAuditForm #confirmdiv").show()
				$("#verifyModAuditForm #confirmmsg").html("");
				$("#verifyModAuditForm #confirmmsg").html("<span style='color:red;font-size:18px;'>上传文件无法解析!</span>");
			}*/
		}
	});
};
function displayUpInfo(fjid){
	if(!$("#verifyModAuditForm #fjids").val()){
		$("#verifyModAuditForm #fjids").val(fjid);
	}else{
		$("#verifyModAuditForm #fjids").val($("#verifyModAuditForm #fjids").val()+","+fjid);
	}
	setTimeout("checkImpCheckStatus(2000,'"+ fjid + "')",1000);
}

$(function(){
	var oFileInput = new FileInput();
	oFileInput.Init("verifyModAuditForm","displayUpInfo",3,1,"pro_file");
	// 所有下拉框添加choose样式
    jQuery('#verifyModAuditForm .chosen-select').chosen({width: '100%'});
	if($("#verifyModAuditForm #yzlb").val()){
		$("#verifyModAuditForm #yzjgtable").show();
		$("#verifyModAuditForm #historytable").show();
		showyzjg();
		showHistoryDataMod();
	}else{
		$("#verifyModAuditForm #yzjgtable").hide();
		$("#verifyModAuditForm #historytable").hide();
	}
	hideTable()
})

function modYzjgtable(){
	if($("#verifyModAuditForm #yzlb").val()){
		$("#verifyModAuditForm #yzjgtable").show();
		$("#verifyModAuditForm #historytable").show();
		showyzjg();
		showHistoryDataMod();
	}else{
		$("#verifyModAuditForm #yzjgtable").hide();
		$("#verifyModAuditForm #historytable").hide();
	}
}

function showHistoryDataMod(){
	let yzlb = $("#verifyModAuditForm #yzlb").val();
	let sjid = $("#verifyModAuditForm #sjid").val();

	if(yzlb==null || yzlb==""){
		let html = "<div class='form-group' ><label class='col-xs-4 col-sm-2 col-md-2  col-lg-2 control-label'>历史记录</label><table class='table table-hover table-bordered' style='width: 80%;table-layout: fixed;margin: auto;'>";
		html += "<thead><tr><th width='25%'>录入人员</th><th width='25%'>录入时间</th><th width='25%'>检测单位</th><th width='25%'>状态</th></tr></thead><tbody>";
		html += "</tbody></table></div>";
		$("#verifyModAuditForm #historytable").empty();
		$("#verifyModAuditForm #historytable").append(html);
	}else{
		$.ajax({
			type : "post",
			url: "/ws/sjyzHistory/pagedataSjyzHistory",
			data : {"sjid":sjid,"yzlb":yzlb,"access_token":$("#ac_tk").val()},
			dataType : "json",
			success : function(data){

				let html = "<div class='form-group' ><label class='col-xs-4 col-sm-2 col-md-2  col-lg-2 control-label'>历史记录</label><table class='table table-hover table-bordered' style='width: 80%;table-layout: fixed;margin: auto;'>";
				html += "<thead><tr><th width='25%'>录入人员</th><th width='25%'>录入时间</th><th width='25%'>检测单位</th><th width='25%'>状态</th></tr></thead><tbody>";
				let json = [];
				$.each(data.sjyzlist,function(i){
					if ('00'==(data.sjyzlist[i].zt)){
						zthtml = '<td>未提交</td>' ;
					}else if ('80'==(data.sjyzlist[i].zt)){
						zthtml = '<td><a href="javascript:void(0);" onclick="showAuditInfo(\''+data.sjyzlist[i].yzid+'\',event,\'AUDIT_VERIFICATION\')">审核通过</a></td>' ;
					}else if ('15'==(data.sjyzlist[i].zt)){
						zthtml = '<td><a href="javascript:void(0);" onclick="showAuditInfo(\''+data.sjyzlist[i].yzid+'\',event,\'AUDIT_VERIFICATION\')">审核未通过</a></td>' ;
					}
					else if ('20'==(data.sjyzlist[i].zt)){
						zthtml = '<td><a href="javascript:void(0);" onclick="showAuditInfo(\''+data.sjyzlist[i].yzid+'\',event,\'AUDIT_VERIFICATION\')">'+data.sjyzlist[i].shxx_dqgwmc+'审核中</a></td>' ;
					}
					else {
						zthtml = '<td><a href="javascript:void(0);" onclick="showAuditInfo(\''+data.sjyzlist[i].yzid+'\',event,\'AUDIT_VERIFICATION\')">'+data.sjyzlist[i].shxx_dqgwmc+'审核中</a></td>' ;
					}
					html += '<tr>' +
						'<td value="' + data.sjyzlist[i].hzxm + '">' +data.sjyzlist[i].hzxm +'</td>'+
						'<td value="'+data.sjyzlist[i].lrsj+'"><a href="javascript:void(0);" onclick=\'queryByYzid("'+ data.sjyzlist[i].yzid +'")\' >' +data.sjyzlist[i].lrsj+ '</a></td>'+
						'<td value="'+data.sjyzlist[i].jcdwmc+'">' +data.sjyzlist[i].jcdwmc+'</td>'+
						// '<td value="'+data.sjyzlist[i].zt+'">' +data.sjyzlist[i].zt+'</td>'
						zthtml +
						'</tr>'
				});
				html += "</tbody></table></div>";
				// $("#verificationForm #yzjg").val(JSON.stringify(json));
				$("#verifyModAuditForm #historytable").empty();
				$("#verifyModAuditForm #historytable").append(html);
			}
		});
	}
}

function showyzjg(){
	let pType = "VERIFY_RESULT";
	let fcsid = $("#verifyModAuditForm #yzlb").val();
	let yzid = $("#verifyModAuditForm #yzid").val();
	if(fcsid==null || fcsid==""){
		let html = "<div class='form-group' ><label class='col-xs-4 col-sm-2 col-md-2  col-lg-2 control-label'>验证结果</label><table class='table table-hover table-bordered' style='width: 80%;table-layout: fixed;margin: auto;'>";
		html += "<thead><tr><th width='50%'>名称</th><th width='50%'>结果</th></tr></thead><tbody>";
		html += "</tbody></table></div>";
		$("#verifyModAuditForm #yzjgtable").empty();
		$("#verifyModAuditForm #yzjgtable").append(html);
	}else{
		$.ajax({
			type : "post",
			url: "/ws/verification/pagedataAnsyGetJcsjListAndJl",
			data: {"jclb": pType, "yzid": yzid, "fcsid": fcsid, "access_token": $("#ac_tk").val()},
			dataType : "json",
			success : function(data){
				//父节点数据
				if (data){

				}
				let html = "<div class='form-group' ><label class='col-xs-4 col-sm-2 col-md-2  col-lg-2 control-label'>验证结果</label><table class='table table-hover table-bordered' style='width: 80%;table-layout: fixed;margin: auto;'>";
				html += "<thead><tr><th width='50%'>名称</th><th width='50%'>结果</th></tr></thead><tbody>";
				let json = [];
				$.each(data.jcsjDtos,function(i){
					// html += "<option value='" + data.jcsjDtos[i].csid + "' selected='selected'>"+data.jcsjDtos[i].csdm + "--" + data.jcsjDtos[i].csmc + "</option>";
					html += "<tr><td style='padding-top: 12px;' value='" + data.jcsjDtos[i].csid + "'>"+data.jcsjDtos[i].csdm+"--"+data.jcsjDtos[i].csmc+"</td><td>";
					if (data.jcsjDtos[i].yzjgjl){
						$.each(data.jcsjDtoList,function(j){
							html += "<label class='radio-inline'><input class='yzjgxxs' style='margin-top: 2px' type='radio' name='" + data.jcsjDtos[i].csid + "' id='optionsRadio"+i+"' value='" + data.jcsjDtos[i].csmc +":"+data.jcsjDtoList[j].csmc+"'" ;
							if ( data.jcsjDtos[i].yzjgjl == data.jcsjDtoList[j].csid){
								html += "checked='true'";
							}
							html += "validate='{required:true}'";
							html += ">"+ data.jcsjDtoList[j].csmc+ "</label>";
						});
					}else{
						$.each(data.jcsjDtoList,function(j){
							html += "<label class='radio-inline'><input class='yzjgxxs' type='radio' name='" + data.jcsjDtos[i].csid + "' id='optionsRadio"+i+"' value='" + data.jcsjDtos[i].csmc +":"+data.jcsjDtoList[j].csmc+"'" ;
							// if ( j == "0"){
							// 	html += "checked";
							// }
							html += "validate='{required:true}'";
							html += ">"+ data.jcsjDtoList[j].csmc+ "</label>";
						});
					}
					let sz = {"csid":'',"csmc":''};
					sz.csid = data.jcsjDtos[i].csid;
					sz.csmc = data.jcsjDtos[i].csmc;
					json.push(sz);
				});
				html += "</td></tr></tbody></table></div>";
				$("#verifyModAuditForm #yzjg").val(JSON.stringify(json));
				$("#verifyModAuditForm #yzjgtable").empty();
				$("#verifyModAuditForm #yzjgtable").append(html);
			}
		});
	}
}
/**
 * 修改检测单位
 * @returns
 */
function modifJcdw(){
	$.confirm('确定要修改当前检测单位?',function(result){
		if(result){
			var yzid=$("#verifyModAuditForm #yzid").val();
			var jcdw=$("#verifyModAuditForm #jcdw").val();
			var url = $("#verifyModAuditForm #url").val();
			$.ajax({
				url:url,
				type : "POST",
				data:{"yzid":yzid,"jcdw":jcdw,"access_token":$("#ac_tk").val()},
				dataType:"json",
				success:function(data){
					if(data.status){
						$.success("修改成功！");
					}else{
						$.error("修改失败！");
					}
				}
			})
		}
	})
}

$("#verifyModAuditForm #qf").change(function(){
	var csdm = $("#verifyModAuditForm #qf option:selected").attr("csdm");
	if(csdm&&csdm.indexOf("LAB_")!=-1){
		$("#verifyModAuditForm #khtz").find("option").each(function(){
			if($(this).attr("csdm")=="B"){
				$("#verifyModAuditForm #khtz").find("option[value='"+$(this).val()+"']").prop("selected","true");
				$("#verifyModAuditForm #khtz").trigger("chosen:updated");
			}
		})
		$("#verifyModAuditForm #khtz_chosen").attr("style","pointer-events: none;width: 100%;");
		$("#verifyModAuditForm #khtzDiv").attr("style","cursor:not-allowed;cursor:no-drop;");
		$("#verifyModAuditForm #khtzDiv .chosen-single").attr("style","background:#e5e5e5 !important");
	}else{
		$("#verifyModAuditForm #khtz").find("option").each(function(){
			if(!$(this).val()){
				$("#verifyModAuditForm #khtz").find("option[value='"+$(this).val()+"']").prop("selected","true");
				$("#verifyModAuditForm #khtz").trigger("chosen:updated");
			}
		})
		$("#verifyModAuditForm #khtz_chosen").attr("style","width: 100%;");
		$("#verifyModAuditForm #khtzDiv").removeAttr("style");
		$("#verifyModAuditForm #khtzDiv .chosen-single").removeAttr("style");
	}


})

function sjphlr(){
	$("#verifyModAuditForm #sjph").val($("#verifyModAuditForm #sj").val());
}

function ywbhlr(){
	$("#verifyModAuditForm #ywbh").val($("#verifyModAuditForm #yw").val());
}