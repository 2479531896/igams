$(function(){
	if($("#verificationForm #isB").val()=="true"){
		$.error("当前样本类型为“血”,不可发起PCR验证新增!");
	}
	if($("#verificationForm #xsbj").val()=="1"){
		$("#verificationForm #sjphDiv").show();
		$("#verificationForm #ywbhDiv").show();
		$("#sjph").attr("validate","{required:true,stringMaxLength:64}");
	}else{
		$("#verificationForm #sjphDiv").hide();
		$("#verificationForm #ywbhDiv").hide();
	}
	if($("#verificationForm #yzlb").val()){
		// $("#verificationForm #yzjgtable").show();
		$("#verificationForm #historytable").show();
		showChildcsdmData();
		//历史记录
		showHistoryData();
	}else{
		$("#verificationForm #yzjgtable").hide();
		$("#verificationForm #historytable").hide();
	}
	if($("#fixtop #lastStep").val()=="true"){
		$("#verificationForm #yzjgSpan").show();
		$("#verificationForm #lastStep_flg").val("true");
	}else if($("#auditAjaxForm #lastStep").val()=="true"){
		$("#verificationForm #yzjgSpan").show();
		$("#verificationForm #lastStep_flg").val("true");
	}else{
		$("#verificationForm #yzjgSpan").hide();
		$("#verificationForm #lastStep_flg").val("false");
	}
	jQuery('#verificationForm .chosen-select').chosen({width: '100%'});
})

function yzjgtable(){
	if($("#verificationForm #yzlb").val()){
		// $("#verificationForm #yzjgtable").show();
		$("#verificationForm #historytable").show();
		showChildcsdmData();
		showHistoryData();
	}else{
		$("#verificationForm #yzjgtable").hide();
		$("#verificationForm #historytable").hide();
	}
}
function showChildcsdmData(){
	let pType = "VERIFY_RESULT";
	let fcsid = $("#verificationForm #yzlb").val();
	let yzid = $("#verificationForm #yzid").val();

	if(fcsid==null || fcsid==""){
		let html = "<div class='form-group' ><label class='col-xs-4 col-sm-2 col-md-2  col-lg-2 control-label'>验证结果</label><table class='table table-hover table-bordered' style='width: 80%;table-layout: fixed;margin: auto;'>";
		html += "<thead><tr><th width='50%'>名称</th><th width='50%'>结果</th></tr></thead><tbody>";
		html += "</tbody></table></div>";
		$("#verificationForm #yzjgtable").empty();
		$("#verificationForm #yzjgtable").append(html);
	}else{
		$.ajax({
			type : "post",
			url: "/ws/verification/pagedataAnsyGetJcsjListAndJl",
			data : {"jclb":pType,"fcsid":fcsid,"yzid":yzid,"access_token":$("#ac_tk").val()},
			dataType : "json",
			success : function(data){
				//父节点数据
				let html = "<div class='form-group' ><label class='col-xs-4 col-sm-2 col-md-2  col-lg-2 control-label'>验证结果</label><table class='table table-hover table-bordered radioChange' style='width: 90%;table-layout: fixed;margin: auto;'>";
				html += "<thead><tr><th width='50%'>名称</th><th width='50%'>结果</th></tr></thead><tbody>";
				let json = [];
				let lastStep_flg = $("#verificationForm #lastStep_flg").val();
				$.each(data.jcsjDtos,function(i){
					// html += "<option value='" + data.jcsjDtos[i].csid + "' selected='selected'>"+data.jcsjDtos[i].csdm + "--" + data.jcsjDtos[i].csmc + "</option>";
					html += "<tr><td style='padding-top: 12px;' value='" + data.jcsjDtos[i].csid + "'>"+data.jcsjDtos[i].csdm+"--"+data.jcsjDtos[i].csmc+"</td><td><div style='overflow-x: auto;padding-bottom: 8px;'>";
					if (data.jcsjDtos[i].yzjgjl){
						$.each(data.jcsjDtoList,function(j){
							html += "<label class='radio-inline'><input type='radio' onclick ='verificationFormSubmit()' name='" + data.jcsjDtos[i].csid + "' id='optionsRadio_"+data.jcsjDtos[i].csid+"' value='" + data.jcsjDtos[i].csmc +":"+data.jcsjDtoList[j].csmc+"'" ;
							if ( data.jcsjDtos[i].yzjgjl == data.jcsjDtoList[j].csid){
								html += "checked='true'";
							}
							if ( lastStep_flg == "true"){
								html += "validate='{required:true}'";
							}
							html += ">"+ data.jcsjDtoList[j].csmc+ "</label>";
						});
					}else{
						$.each(data.jcsjDtoList,function(j){
							html += "<label class='radio-inline'><input type='radio' onclick ='verificationFormSubmit()' name='" + data.jcsjDtos[i].csid + "' id='optionsRadio_"+data.jcsjDtos[i].csid+"' value='" + data.jcsjDtos[i].csmc +":"+data.jcsjDtoList[j].csmc+"'" ;
							// if ( j == "0"){
							// 	html += "checked";
							// }
							if ( lastStep_flg == "true"){
								html += "validate='{required:true}'";
							}
							html += ">"+ data.jcsjDtoList[j].csmc+ "</label>";
						});
					}
					let sz = {"csid":'',"csmc":''};
					sz.csid = data.jcsjDtos[i].csid;
					sz.csmc = data.jcsjDtos[i].csmc;
					json.push(sz);
				});
				html += "</div></td></tr></tbody></table></div>";
				$("#verificationForm #yzjgjson").val(JSON.stringify(json));
				$("#verificationForm #yzjgtable").empty();
				$("#verificationForm #yzjgtable").append(html);
				verificationFormSubmit()
			}
		});
	}
}

function showHistoryData(){

	let yzlb = $("#verificationForm #yzlb").val();
	let sjid = $("#verificationForm #sjid").val();

	if(yzlb==null || yzlb==""){
		let html = "<div class='form-group' ><label class='col-xs-4 col-sm-2 col-md-2  col-lg-2 control-label'>历史记录</label><table class='table table-hover table-bordered' style='width: 80%;table-layout: fixed;margin: auto;'>";
		html += "<thead><tr><th width='25%'>录入人员</th><th width='25%'>录入时间</th><th width='25%'>检测单位</th><th width='25%'>状态</th></tr></thead><tbody>";
		html += "</tbody></table></div>";
		$("#verificationForm #historytable").empty();
		$("#verificationForm #historytable").append(html);
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
				$("#verificationForm #historytable").empty();
				$("#verificationForm #historytable").append(html);
			}
		});
	}
}

function queryByYzid(yzid){
	var url= "/verification/verification/viewVerifi?yzid="+yzid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'验证历史信息',viewYzConfig);
}

/**
 * 查看送检验证页面
 */
var viewYzConfig={
	width		: "1000px",
	modalName	:"viewYzModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}

/**
 * 修改检测单位
 * @returns
 */
function modifJcdw(){
	$.confirm('确定要修改当前检测单位?',function(result){
		if(result){
			var yzid=$("#verificationForm #yzid").val();
			var jcdw=$("#verificationForm #jcdw").val();
			var url =$("#verificationForm #url").val();
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

function sjphlr(){
	$("#verificationForm #sjph").val($("#verificationForm #sj").val());
}

function ywbhlr(){
	$("#verificationForm #ywbh").val($("#verificationForm #yw").val());
}

function btnBind(){
	var sel_yzlb=$("#verificationForm #yzlb");
	//对账方式下拉事件
	sel_yzlb.unbind("change").change(function(){
		yzlbEvent();
	});
}

function yzlbEvent(){
	var yzlb=$("#verificationForm #yzlb").val();
	$.ajax({
		type:'post',
		url:"/verification/verification/pagedataMessageInfo",
		cache: false,
		data: {"yzlb":yzlb,"access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			//返回值
			if(data.grszxx!=null && data.grszxx!=null){
				$("#verificationForm #sjph").val(data.grszxx.szz);
			}else{
				$("#verificationForm #sjph").val("");
			}
		}
	});
}

function verificationFormSubmit(){
	let data = JSON.parse($("#verificationForm #yzjgjson").val());
	let yzjg = "";
	$.each(data,function(i){
		let jg = ''
		if (typeof($("#verificationForm input[name='"+data[i].csid+"']:checked").val()) != 'undefined'){
			jg = $("#verificationForm input[name='"+data[i].csid+"']:checked").val();
		}else{
			jg = $("#verificationForm input[name='"+data[i].csid+"']").val();
			let str = jg.split(":")
			jg = str[0]+":";
		}
		yzjg += jg;
		if (i != data.length-1){
			yzjg +=",";
		}
	});
	$("#verificationForm input[name='yzjg']").val(yzjg);
}

var csdm = "";
$("#verificationForm #qf").change(function(){
	if (csdm != $("#verificationForm #qf option:selected").attr("csdm")){
		csdm = $("#verificationForm #qf option:selected").attr("csdm");
		if(csdm&&csdm.indexOf("LAB_")!=-1){
			$("#verificationForm #khtz").find("option").each(function(){
				if($(this).attr("csdm")=="B"){
					$("#verificationForm #khtz").find("option[value='"+$(this).val()+"']").prop("selected","true");
					$("#verificationForm #khtz").trigger("chosen:updated");
				}
			})
			$("#verificationForm #khtz_chosen").attr("style","pointer-events: none;width: 100%;");
			$("#verificationForm #khtzDiv").attr("style","cursor:not-allowed;cursor:no-drop;");
			$("#verificationForm #khtzDiv .chosen-single").attr("style","background:#e5e5e5 !important");
		}else{
			$("#verificationForm #khtz").find("option").each(function(){
				if(!$("#verificationForm #khtz").val()){
					$("#verificationForm #khtz").find("option[value='"+$(this).val()+"']").prop("selected","true");
					$("#verificationForm #khtz").trigger("chosen:updated");
				}
			})
			$("#verificationForm #khtz_chosen").attr("style","width: 100%;");
			$("#verificationForm #khtzDiv").removeAttr("style");
			$("#verificationForm #khtzDiv .chosen-single").removeAttr("style");
		}
	}
})
$(document).ready(function(){
	btnBind();
});