//检测子项目
var detectsublist=[];

$("#recheck_Form .jclb").bind("click",function(){
	var csid=$(this).val();
	if($("#recheck_Form .yy")!=null && $("#recheck_Form .yy").length>0){
		for(var i=0;i<$("#recheck_Form .yy").length;i++){
			var yycsid=$("#recheck_Form .yy")[i].value;
			var fcsid=$("#recheck_Form #yy_"+yycsid+" input").attr("fcsid");
			if(csid==fcsid){
				$("#recheck_Form #yy_"+yycsid).show();
			}else{
				$("#recheck_Form #yy_"+yycsid).hide();
			}
		}
	}
	//把当前检测项目的选中状态都置为false；
	$("#recheck_Form .jcxm").each(function(){
		this.checked = false;
	});
	$("#recheck_Form #jczxm").val("");
	$("#recheck_Form #jczxm_div").empty();
	$("#recheck_Form #jczxmDiv").hide();
	var csdm=$(this).attr("csdm");

	if(csdm.indexOf("LAB_")!=-1){//实验室复测
		$("#recheck_Form #gbbj_bsf").prop("checked",true);
		$("#recheck_Form #sfff option[value='0']").attr("selected", true).prop('selected', true);
		$("#recheck_Form #sfff option[value='0']").trigger("chosen:updated");


		$("#recheck_Form #gbbj_sf").attr("disabled", true);
		$("#recheck_Form #sfff_chosen").attr("style","pointer-events: none;width: 100%;");
		$("#recheck_Form #sfffDiv").attr("style","cursor:not-allowed;cursor:no-drop;");
		$("#recheck_Form #sfffDiv .chosen-single").attr("style","background:#e5e5e5 !important");
	}else if(csdm == 'RECHECK'){//复测
		$("#recheck_Form #gbbj_bsf").prop("checked",true);
		$("#recheck_Form #sfff option[value='0']").attr("selected", true).prop('selected', true);
		$("#recheck_Form #sfff option[value='0']").trigger("chosen:updated");

		$("#recheck_Form #gbbj_sf").removeAttr("disabled");
		$("#recheck_Form #sfff_chosen").attr("style","width: 100%;");
		$("#recheck_Form #sfffDiv").removeAttr("style");
		$("#recheck_Form #sfffDiv .chosen-single").removeAttr("style");
	}else{
		$("#recheck_Form #gbbj_bsf").prop("checked",true);
		$("#recheck_Form #sfff option[value='1']").attr("selected", true).prop('selected', true);
		$("#recheck_Form #sfff option[value='1']").trigger("chosen:updated");
		$("#recheck_Form #gbbj_sf").removeAttr("disabled");
		$("#recheck_Form #sfff_chosen").attr("style","width: 100%;");
		$("#recheck_Form #sfffDiv").removeAttr("style");
		$("#recheck_Form #sfffDiv .chosen-single").removeAttr("style");
	}
	if(csdm.indexOf("ADDDETECT")!=-1){
		//设置其他DOM的状态(复检&加测&去人源  显示的DOM不同)
		$("#recheck_Form #jcxm_D").show();
		$("#recheck_Form #jcxm_R").show();
		$("#recheck_Form #jcxm_C").show();
		$("#recheck_Form #jcxm_F").show();
		$("#recheck_Form #jcxm_Z").show();
		// $("#recheck_Form #jcxm_Z6").show();
		// $("#recheck_Form #jcxm_Z8").show();
		// $("#recheck_Form #jcxm_Z12").show();
		// $("#recheck_Form #jcxm_Z18").show();
		$("#recheck_Form #jcxm_T3").show();
		$("#recheck_Form #jcxm_T6").show();
		$("#recheck_Form #jcxm_K").show();
		//加测时不显示去人源项目(通过REM判断)
		$("#recheck_Form .jcxmDiv").each(function(){
			if($(this).attr("cskz3")!=null&&$(this).attr("cskz3").indexOf("_REM")!=-1){
				$(this).hide();
			}
		})
		$("#recheck_Form #jcxm_span").text("加测项目");
	}else if(csdm.indexOf("RECHECK")!=-1){
		//复检加测时根据检测项目只能选中当前的检测项目
		var cskz1=$("#recheck_Form #cskz1").val();
		if(cskz1=="D"){
			$("#recheck_Form #jcxm_D").show();
			$("#recheck_Form #jcxm_R").hide();
			$("#recheck_Form #jcxm_C").hide();
			$("#recheck_Form #jcxm_F").hide();
			$("#recheck_Form #jcxm_Z").hide();
			$("#recheck_Form #jcxm_Z6").hide();
			// $("#recheck_Form #jcxm_Z8").hide();
			// $("#recheck_Form #jcxm_Z12").hide();
			// $("#recheck_Form #jcxm_Z18").hide();
			$("#recheck_Form #jcxm_T3").hide();
			$("#recheck_Form #jcxm_T6").hide();
			$("#recheck_Form #jcxm_K").hide();
		}else if(cskz1=="C"){
			$("#recheck_Form #jcxm_D").show();
			$("#recheck_Form #jcxm_R").show();
			$("#recheck_Form #jcxm_C").show();
			$("#recheck_Form #jcxm_F").hide();
			$("#recheck_Form #jcxm_Z").hide();
			$("#recheck_Form #jcxm_Z6").hide();
			// $("#recheck_Form #jcxm_Z8").hide();
			// $("#recheck_Form #jcxm_Z12").hide();
			// $("#recheck_Form #jcxm_Z18").hide();
			$("#recheck_Form #jcxm_T3").hide();
			$("#recheck_Form #jcxm_T6").hide();
			$("#recheck_Form #jcxm_K").hide();
		}else if(cskz1=="R"){
			$("#recheck_Form #jcxm_D").hide();
			$("#recheck_Form #jcxm_R").show();
			$("#recheck_Form #jcxm_C").hide();
			$("#recheck_Form #jcxm_F").hide();
			$("#recheck_Form #jcxm_Z").hide();
			$("#recheck_Form #jcxm_Z6").hide();
			// $("#recheck_Form #jcxm_Z8").hide();
			// $("#recheck_Form #jcxm_Z12").hide();
			// $("#recheck_Form #jcxm_Z18").hide();
			$("#recheck_Form #jcxm_T3").hide();
			$("#recheck_Form #jcxm_T6").hide();
			$("#recheck_Form #jcxm_K").hide();
		}else if(cskz1=="F"){
			$("#recheck_Form #jcxm_D").hide();
			$("#recheck_Form #jcxm_R").hide();
			$("#recheck_Form #jcxm_C").hide();
			$("#recheck_Form #jcxm_F").show();
			$("#recheck_Form #jcxm_Z").hide();
			$("#recheck_Form #jcxm_Z6").hide();
			// $("#recheck_Form #jcxm_Z8").hide();
			// $("#recheck_Form #jcxm_Z12").hide();
			// $("#recheck_Form #jcxm_Z18").hide();
			$("#recheck_Form #jcxm_T3").hide();
			$("#recheck_Form #jcxm_T6").hide();
			$("#recheck_Form #jcxm_K").hide();
		}else if(cskz1=="Z"||cskz1=="Z6"){
			$("#recheck_Form #jcxm_D").hide();
			$("#recheck_Form #jcxm_R").hide();
			$("#recheck_Form #jcxm_C").hide();
			$("#recheck_Form #jcxm_F").hide();
			$("#recheck_Form #jcxm_Z").show();
			$("#recheck_Form #jcxm_Z6").show();
			// $("#recheck_Form #jcxm_Z8").hide();
			// $("#recheck_Form #jcxm_Z12").hide();
			// $("#recheck_Form #jcxm_Z18").hide();
			$("#recheck_Form #jcxm_T3").hide();
			$("#recheck_Form #jcxm_T6").hide();
			$("#recheck_Form #jcxm_K").hide();
		}else if(cskz1=="Z8"){
			$("#recheck_Form #jcxm_D").hide();
			$("#recheck_Form #jcxm_R").hide();
			$("#recheck_Form #jcxm_C").hide();
			$("#recheck_Form #jcxm_F").hide();
			$("#recheck_Form #jcxm_Z").hide();
			$("#recheck_Form #jcxm_Z6").hide();
			// $("#recheck_Form #jcxm_Z8").show();
			// $("#recheck_Form #jcxm_Z12").hide();
			// $("#recheck_Form #jcxm_Z18").hide();
			$("#recheck_Form #jcxm_T3").hide();
			$("#recheck_Form #jcxm_T6").hide();
			$("#recheck_Form #jcxm_K").hide();
		}else if(cskz1=="Z12"){
			$("#recheck_Form #jcxm_D").hide();
			$("#recheck_Form #jcxm_R").hide();
			$("#recheck_Form #jcxm_C").hide();
			$("#recheck_Form #jcxm_F").hide();
			$("#recheck_Form #jcxm_Z").hide();
			$("#recheck_Form #jcxm_Z6").hide();
			// $("#recheck_Form #jcxm_Z8").hide();
			// $("#recheck_Form #jcxm_Z12").show();
			// $("#recheck_Form #jcxm_Z18").hide();
			$("#recheck_Form #jcxm_T3").hide();
			$("#recheck_Form #jcxm_T6").hide();
			$("#recheck_Form #jcxm_K").hide();
		}else if(cskz1=="Z18"){
			$("#recheck_Form #jcxm_D").hide();
			$("#recheck_Form #jcxm_R").hide();
			$("#recheck_Form #jcxm_C").hide();
			$("#recheck_Form #jcxm_F").hide();
			$("#recheck_Form #jcxm_Z").hide();
			$("#recheck_Form #jcxm_Z6").hide();
			// $("#recheck_Form #jcxm_Z8").hide();
			// $("#recheck_Form #jcxm_Z12").hide();
			// $("#recheck_Form #jcxm_Z18").show();
			$("#recheck_Form #jcxm_T3").hide();
			$("#recheck_Form #jcxm_T6").hide();
			$("#recheck_Form #jcxm_K").hide();
		}else if(cskz1=="T3"){
			$("#recheck_Form #jcxm_D").hide();
			$("#recheck_Form #jcxm_R").hide();
			$("#recheck_Form #jcxm_C").hide();
			$("#recheck_Form #jcxm_F").hide();
			$("#recheck_Form #jcxm_Z").hide();
			$("#recheck_Form #jcxm_Z6").hide();
			// $("#recheck_Form #jcxm_Z8").hide();
			// $("#recheck_Form #jcxm_Z12").hide();
			// $("#recheck_Form #jcxm_Z18").hide();
			$("#recheck_Form #jcxm_T3").show();
			$("#recheck_Form #jcxm_T6").hide();
			$("#recheck_Form #jcxm_K").hide();
		}else if(cskz1=="T6"){
			$("#recheck_Form #jcxm_D").hide();
			$("#recheck_Form #jcxm_R").hide();
			$("#recheck_Form #jcxm_C").hide();
			$("#recheck_Form #jcxm_F").hide();
			$("#recheck_Form #jcxm_Z").hide();
			$("#recheck_Form #jcxm_Z6").hide();
			// $("#recheck_Form #jcxm_Z8").hide();
			// $("#recheck_Form #jcxm_Z12").hide();
			// $("#recheck_Form #jcxm_Z18").hide();
			$("#recheck_Form #jcxm_T3").hide();
			$("#recheck_Form #jcxm_T6").show();
			$("#recheck_Form #jcxm_K").hide();
		}else if(cskz1=="K"){
			$("#recheck_Form #jcxm_D").hide();
			$("#recheck_Form #jcxm_R").hide();
			$("#recheck_Form #jcxm_C").hide();
			$("#recheck_Form #jcxm_F").hide();
			$("#recheck_Form #jcxm_Z").hide();
			$("#recheck_Form #jcxm_Z6").hide();
			// $("#recheck_Form #jcxm_Z8").hide();
			// $("#recheck_Form #jcxm_Z12").hide();
			// $("#recheck_Form #jcxm_Z18").hide();
			$("#recheck_Form #jcxm_T3").hide();
			$("#recheck_Form #jcxm_T6").hide();
			$("#recheck_Form #jcxm_K").show();
		}else {
			$("#recheck_Form #jcxm_F").hide();
		}
		
		//复检加测时不显示去人源项目(通过REM判断)
		$("#recheck_Form .jcxmDiv").each(function(){
			if($(this).attr("cskz3")!=null&&$(this).attr("cskz3").indexOf("_REM")!=-1){
				$(this).hide();
			}
		})
		$("#recheck_Form #jcxm_span").text("复测项目");
	}else if(csdm.indexOf("REM")!=-1){
		//去人源的时候只能选择去人源项目 &&  不能选择RNA 项目   2020/11/27 张晗
		$("#recheck_Form #jcxm_D").hide();
		$("#recheck_Form #jcxm_R").hide();
		$("#recheck_Form #jcxm_C").hide();
		$("#recheck_Form #jcxm_F").hide();
		$("#recheck_Form #jcxm_Z").hide();
		// $("#recheck_Form #jcxm_Z6").hide();
		// $("#recheck_Form #jcxm_Z8").hide();
		// $("#recheck_Form #jcxm_Z12").hide();
		// $("#recheck_Form #jcxm_Z18").hide();
		$("#recheck_Form #jcxm_T3").hide();
		$("#recheck_Form #jcxm_T6").hide();
		$("#recheck_Form #jcxm_K").hide();
		
		$("#recheck_Form .jcxmDiv").each(function(){
		if($(this).attr("cskz3")!=null&&$(this).attr("cskz3").indexOf("_REM")!=-1&& this.id =="jcxm_D"){
			$(this).show();
		}
	})
		$("#recheck_Form #jcxm_span").text("去人源项目");
	}else{
		//显示所有检测项目
		$("#recheck_Form .jcxmDiv").each(function(){
			$(this).show();
		})
		$("#recheck_Form #jcxm_F").hide();//F项目隐藏
		$("#recheck_Form #jcxm_span").text("项目");
	}
})

$("#recheck_Form .jcxm").bind("click",function(){
	var csdm=$("#recheck_Form .jclb:checked").attr("csdm");
	var cskz3=$(this).attr("cskz3");
	var csid=$(this).val();
	if(csdm=="REM"){
		if(cskz3=="IMP_REPORT_C_TEMEPLATE_REM" || csdm=="IMP_REPORT_C_TEMEPLATE"){
			$("#recheck_Form #gbbj_bsf").prop("checked",true);
		}
	}
	$("#recheck_Form #jczxm").val("");
	if(detectsublist){
		var html="";
		for (var i = 0; i < detectsublist.length; i++) {
			if(csid==detectsublist[i].fcsid){
				html+="<div class='col-xs-4 col-sm-4 col-md-4 col-lg-4' style='padding-left:0px;'>";
				html+="<label class='checkboxLabel checkbox-inline' style='padding-left:16px;'>";
				html+="<input type='radio' id='zxm' name='zxm' value='"+detectsublist[i].csid+"' onclick='changeZxm(\""+detectsublist[i].csid+"\")'/>";
				html+="<span>"+detectsublist[i].csmc+"</span>";
				html+="</label>";
				html+="</div>";
			}
		}
		if(html){
			$("#recheck_Form #jczxmDiv").show();
			$("#recheck_Form #jczxm_div").empty();
			$("#recheck_Form  #jczxm_div").append(html);
		}else{
			$("#recheck_Form #jczxm_div").empty();
			$("#recheck_Form #jczxmDiv").hide();
		}
	}else{
		$("#recheck_Form #jczxm_div").empty();
		$("#recheck_Form #jczxmDiv").hide();
	}
	showHistoryData();
})

function changeZxm(csid){
	$("#recheck_Form #jczxm").val(csid);
	showHistoryData();
}

function initSubDetect(){
	$.ajax({
		url: '/inspection/inspection/pagedataSubDetect',
		type: 'post',
		data: {
			"access_token": $("#ac_tk").val(),
		},
		dataType: 'json',
		success: function(result) {
			detectsublist = result.subdetectlist;
		}
	});
}

function showHistoryData(){
	let jcxmid = $('#recheck_Form input:radio[name="jcxm"]:checked').val();
	let jczxmid = $("#recheck_Form #jczxm").val();

	if(jcxmid==null || jcxmid==""){
		let html = "<div class='form-group' ><label class='col-sm-2 col-xs-2  control-label' style='color: black'>历史记录</label><table class='table table-hover table-bordered' style='width: 80%;table-layout: fixed;margin: auto;'>";
		html += "<thead><tr><th width='25%'>类型</th><th width='25%'>录入人员</th><th width='25%'>录入时间</th><th width='25%'>状态</th></tr></thead><tbody>";
		html += "</tbody></table></div>";
		$("#recheck_Form #historytable").empty();
		$("#recheck_Form #historytable").append(html);
	}else{
		$.ajax({
			type : "post",
			url: "/recheck/recheck/pagedataRecheckHistory",
			data : {"sjid":$("#recheck_Form #sjid").val(),"jcxm":jcxmid,"jczxm":jczxmid,"access_token":$("#ac_tk").val()},
			dataType : "json",
			success : function(data){

				let html = "<div class='form-group' ><label class='col-sm-2 col-xs-2  control-label' style='color: black'>历史记录</label><table class='table table-hover table-bordered' style='width: 80%;table-layout: fixed;margin: auto;'>";
				html += "<thead><tr><th width='25%'>类型</th><th width='25%'>录入人员</th><th width='25%'>录入时间</th><th width='25%'>状态</th></tr></thead><tbody>";
				let json = [];
				let zthtml='';
				$.each(data.list,function(i){
					if ('00'==(data.list[i].zt)){
						zthtml = '<td>未提交</td>' ;
					}else if ('80'==(data.list[i].zt)){
						if(data.list[i].shlx){
							zthtml = "<td><a href='javascript:void(0);' onclick='showAuditInfo(\"" + data.list[i].fjid + "\",event,\"" + data.list[i].shlx + "\")' >审核通过</a></td>";
						}else{
							zthtml = "<td><a href='javascript:void(0);' onclick='showAuditInfo(\"" + data.list[i].fjid + "\",event,\"AUDIT_RECHECK\")' >审核通过</a></td>";
						}
					}else if ('15'==(data.list[i].zt)){
						if(data.list[i].shlx){
							zthtml = "<td><a href='javascript:void(0);' onclick='showAuditInfo(\"" + data.list[i].fjid + "\",event,\"" + data.list[i].shlx + "\")' >审核未通过</a></td>";
						}else{
							zthtml = "<td><a href='javascript:void(0);' onclick='showAuditInfo(\"" + data.list[i].fjid + "\",event,\"AUDIT_RECHECK\")' >审核未通过</a></td>";
						}
					} else {
						if(data.list[i].shlx){
							zthtml = "<td><a href='javascript:void(0);' onclick='showAuditInfo(\"" + data.list[i].fjid + "\",event,\"" + data.list[i].shlx + "\")' >"+data.list[i].shxx_dqgwmc+"审核中</a></td>";
						}else{
							zthtml = "<td><a href='javascript:void(0);' onclick='showAuditInfo(\"" + data.list[i].fjid + "\",event,\"AUDIT_RECHECK\")' >"+data.list[i].shxx_dqgwmc+"审核中</a></td>";
						}
					}
					html += '<tr>' +
						'<td value="' + data.list[i].lxmc + '">' +data.list[i].lxmc +'</td>'+
						'<td value="'+data.list[i].lrrymc+'">' +data.list[i].lrrymc+'</td>'+
						'<td value="'+data.list[i].lrsj+'">' +data.list[i].lrsj+'</td>'+
						zthtml +
						'</tr>'
				});
				html += "</tbody></table></div>";
				$("#recheck_Form #historytable").empty();
				$("#recheck_Form #historytable").append(html);
			}
		});
	}
}

$(function(){
	//所有下拉框添加choose样式
	jQuery('#recheck_Form .chosen-select').chosen({width: '100%'});
	initSubDetect();
	$("#recheck_Form #jczxmDiv").hide();
	$("#recheck_Form .yyDiv").hide();
})