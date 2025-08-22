//检测子项目
var detectsublist=[];
$(function(){
	laydate.render({
		elem: '#recheck_mod_Form #fkrq'
		,type: 'datetime'
	});
	$.ajax({
		url: '/inspection/inspection/pagedataSubDetect',
		type: 'post',
		data: {
			"access_token": $("#ac_tk").val(),
		},
		dataType: 'json',
		async:false,
		success: function(result) {
			detectsublist = result.subdetectlist;
		}
	});

	var jczxm = $("#recheck_mod_Form #jczxm").val();
	if(detectsublist){
		var html="";
		for (var i = 0; i < detectsublist.length; i++) {
			if($("input[name='jcxm']:checked").val()==detectsublist[i].fcsid){
				html+="<div class='col-xs-6 col-sm-12 col-md-6 col-lg-4' style='padding-left:0px;'>";
				html+="<label class='checkboxLabel checkbox-inline' style='padding-left:0px;'>";
				if(jczxm&&jczxm==detectsublist[i].csid){
					html+="<input type='radio' id='zxm' name='zxm' value='"+detectsublist[i].csid+"' checked onclick='changeZxm(\""+detectsublist[i].csid+"\")'/>";
				}else{
					html+="<input type='radio' id='zxm' name='zxm' value='"+detectsublist[i].csid+"' onclick='changeZxm(\""+detectsublist[i].csid+"\")'/>";
				}
				html+="<span>"+detectsublist[i].csmc+"</span>";
				html+="</label>";
				html+="</div>";
			}
		}
		if(html){
			$("#recheck_mod_Form #jczxmDiv").show();
			$("#recheck_mod_Form #jczxm_div").empty();
			$("#recheck_mod_Form  #jczxm_div").append(html);
		}else{
			$("#recheck_mod_Form #jczxm_div").empty();
			$("#recheck_mod_Form #jczxmDiv").hide();
		}
	}else{
		$("#recheck_mod_Form #jczxm_div").empty();
		$("#recheck_mod_Form #jczxmDiv").hide();
	}
	//所有下拉框添加choose样式
	jQuery('#recheck_mod_Form .chosen-select').chosen({width: '100%'});
	
	var lxdm=$("#recheck_mod_Form #fjlxdm").val();
	if(lxdm.indexOf("LAB_")!=-1){
		// $("#recheck_mod_Form #gbbj_bsf").prop("checked",true);
		// $("#recheck_mod_Form #sfff option[value='0']").attr("selected", true).prop('selected', true);
		// $("#recheck_mod_Form #sfff option[value='0']").trigger("chosen:updated");


		$("#recheck_mod_Form #gbbj_sf").attr("disabled", true);
		$("#recheck_mod_Form #sfff_chosen").attr("style","pointer-events: none;width: 100%;");
		$("#recheck_mod_Form #sfffDiv").attr("style","cursor:not-allowed;cursor:no-drop;");
		$("#recheck_mod_Form #sfffDiv .chosen-single").attr("style","background:#e5e5e5 !important");
	}
	//加测项目
	if(lxdm.indexOf("ADDDETECT") != -1){
		$("#recheck_mod_Form .jcxmDiv").each(function(){
			if($(this).attr("cskz3")!=null&&$(this).attr("cskz3").indexOf("_REM")!=-1){
				$(this).hide();
			}
		});
		$("#recheck_mod_Form #jcxm_span").text("加测项目");
	}else if(lxdm.indexOf("REM")!= -1){
		$("#recheck_mod_Form .jcxmDiv").each(function(){
			if($(this).attr("cskz3")==null||$(this).attr("cskz3").indexOf("_REM")==-1||this.id =="jcxm_R"||this.id =="jcxm_C"){
				$(this).hide();
			}
		});
		$("#recheck_mod_Form #jcxm_span").text("加测去人源");
	}else if(lxdm.indexOf("RECHECK")!= -1){
		var cskz1=$("#recheck_mod_Form #cskz1").val();
		if(cskz1=="D"){
			$("#recheck_mod_Form #jcxm_D").show();
			$("#recheck_mod_Form #jcxm_R").hide();
			$("#recheck_mod_Form #jcxm_C").hide();
			$("#recheck_mod_Form #jcxm_Z").hide();
			$("#recheck_mod_Form #jcxm_Z6").hide();
			// $("#recheck_mod_Form #jcxm_Z8").hide();
			// $("#recheck_mod_Form #jcxm_Z12").hide();
			// $("#recheck_mod_Form #jcxm_Z18").hide();
			$("#recheck_mod_Form #jcxm_F").hide();
			$("#recheck_mod_Form #jcxm_T3").hide();
			$("#recheck_mod_Form #jcxm_T6").hide();
			$("#recheck_mod_Form #jcxm_K").hide();
		}else if(cskz1=="C"){
			$("#recheck_mod_Form #jcxm_D").show();
			$("#recheck_mod_Form #jcxm_R").show();
			$("#recheck_mod_Form #jcxm_C").show();
			$("#recheck_mod_Form #jcxm_Z").hide();
			$("#recheck_mod_Form #jcxm_Z6").hide();
			// $("#recheck_mod_Form #jcxm_Z8").hide();
			// $("#recheck_mod_Form #jcxm_Z12").hide();
			// $("#recheck_mod_Form #jcxm_Z18").hide();
			$("#recheck_mod_Form #jcxm_F").hide();
			$("#recheck_mod_Form #jcxm_T3").hide();
			$("#recheck_mod_Form #jcxm_T6").hide();
			$("#recheck_mod_Form #jcxm_K").hide();
		}else if(cskz1=="R"){
			$("#recheck_mod_Form #jcxm_D").hide();
			$("#recheck_mod_Form #jcxm_R").show();
			$("#recheck_mod_Form #jcxm_C").hide();
			$("#recheck_mod_Form #jcxm_Z").hide();
			$("#recheck_mod_Form #jcxm_Z6").hide();
			// $("#recheck_mod_Form #jcxm_Z8").hide();
			// $("#recheck_mod_Form #jcxm_Z12").hide();
			// $("#recheck_mod_Form #jcxm_Z18").hide();
			$("#recheck_mod_Form #jcxm_F").hide();
			$("#recheck_mod_Form #jcxm_T3").hide();
			$("#recheck_mod_Form #jcxm_T6").hide();
			$("#recheck_mod_Form #jcxm_K").hide();
		// }else if(cskz1=="Z6"){
		}else if(cskz1=="Z" || cskz1=="Z6"){
			$("#recheck_mod_Form #jcxm_D").hide();
			$("#recheck_mod_Form #jcxm_R").hide();
			$("#recheck_mod_Form #jcxm_C").hide();
			$("#recheck_mod_Form #jcxm_Z").show();
			$("#recheck_mod_Form #jcxm_Z6").show();
			// $("#recheck_mod_Form #jcxm_Z8").hide();
			// $("#recheck_mod_Form #jcxm_Z12").hide();
			// $("#recheck_mod_Form #jcxm_Z18").hide();
			$("#recheck_mod_Form #jcxm_F").hide();
			$("#recheck_mod_Form #jcxm_T3").hide();
			$("#recheck_mod_Form #jcxm_T6").hide();
			$("#recheck_mod_Form #jcxm_K").hide();
		}else if(cskz1=="Z8"){
			$("#recheck_mod_Form #jcxm_D").hide();
			$("#recheck_mod_Form #jcxm_R").hide();
			$("#recheck_mod_Form #jcxm_C").hide();
			$("#recheck_mod_Form #jcxm_Z").hide();
			$("#recheck_mod_Form #jcxm_Z6").hide();
			// $("#recheck_mod_Form #jcxm_Z8").show();
			// $("#recheck_mod_Form #jcxm_Z12").hide();
			// $("#recheck_mod_Form #jcxm_Z18").hide();
			$("#recheck_mod_Form #jcxm_F").hide();
			$("#recheck_mod_Form #jcxm_T3").hide();
			$("#recheck_mod_Form #jcxm_T6").hide();
			$("#recheck_mod_Form #jcxm_K").hide();
		}else if(cskz1=="Z12"){
			$("#recheck_mod_Form #jcxm_D").hide();
			$("#recheck_mod_Form #jcxm_R").hide();
			$("#recheck_mod_Form #jcxm_C").hide();
			$("#recheck_mod_Form #jcxm_Z").hide();
			$("#recheck_mod_Form #jcxm_Z6").hide();
			// $("#recheck_mod_Form #jcxm_Z8").hide();
			// $("#recheck_mod_Form #jcxm_Z12").show();
			// $("#recheck_mod_Form #jcxm_Z18").hide();
			$("#recheck_mod_Form #jcxm_F").hide();
			$("#recheck_mod_Form #jcxm_T3").hide();
			$("#recheck_mod_Form #jcxm_T6").hide();
			$("#recheck_mod_Form #jcxm_K").hide();
		}else if(cskz1=="Z18"){
			$("#recheck_mod_Form #jcxm_D").hide();
			$("#recheck_mod_Form #jcxm_R").hide();
			$("#recheck_mod_Form #jcxm_C").hide();
			$("#recheck_mod_Form #jcxm_Z").hide();
			$("#recheck_mod_Form #jcxm_Z6").hide();
			// $("#recheck_mod_Form #jcxm_Z8").hide();
			// $("#recheck_mod_Form #jcxm_Z12").hide();
			// $("#recheck_mod_Form #jcxm_Z18").show();
			$("#recheck_mod_Form #jcxm_F").hide();
			$("#recheck_mod_Form #jcxm_T3").hide();
			$("#recheck_mod_Form #jcxm_T6").hide();
			$("#recheck_mod_Form #jcxm_K").hide();
		}else if(cskz1=="F"){
			$("#recheck_mod_Form #jcxm_D").hide();
			$("#recheck_mod_Form #jcxm_R").hide();
			$("#recheck_mod_Form #jcxm_C").hide();
			$("#recheck_mod_Form #jcxm_Z").hide();
			$("#recheck_mod_Form #jcxm_Z6").hide();
			// $("#recheck_mod_Form #jcxm_Z8").hide();
			// $("#recheck_mod_Form #jcxm_Z12").hide();
			// $("#recheck_mod_Form #jcxm_Z18").hide();
			$("#recheck_mod_Form #jcxm_F").show();
			$("#recheck_mod_Form #jcxm_T3").hide();
			$("#recheck_mod_Form #jcxm_T6").hide();
			$("#recheck_mod_Form #jcxm_K").hide();
		}else if(cskz1=="T3"){
			$("#recheck_mod_Form #jcxm_D").hide();
			$("#recheck_mod_Form #jcxm_R").hide();
			$("#recheck_mod_Form #jcxm_C").hide();
			$("#recheck_mod_Form #jcxm_Z").hide();
			$("#recheck_mod_Form #jcxm_Z6").hide();
			// $("#recheck_mod_Form #jcxm_Z8").hide();
			// $("#recheck_mod_Form #jcxm_Z12").hide();
			// $("#recheck_mod_Form #jcxm_Z18").hide();
			$("#recheck_mod_Form #jcxm_F").hide();
			$("#recheck_mod_Form #jcxm_T3").show();
			$("#recheck_mod_Form #jcxm_T6").hide();
			$("#recheck_mod_Form #jcxm_K").hide();
		}else if(cskz1=="T6"){
			$("#recheck_mod_Form #jcxm_D").hide();
			$("#recheck_mod_Form #jcxm_R").hide();
			$("#recheck_mod_Form #jcxm_C").hide();
			$("#recheck_mod_Form #jcxm_Z").hide();
			$("#recheck_mod_Form #jcxm_Z6").hide();
			// $("#recheck_mod_Form #jcxm_Z8").hide();
			// $("#recheck_mod_Form #jcxm_Z12").hide();
			// $("#recheck_mod_Form #jcxm_Z18").hide();
			$("#recheck_mod_Form #jcxm_F").hide();
			$("#recheck_mod_Form #jcxm_T3").hide();
			$("#recheck_mod_Form #jcxm_T6").show();
			$("#recheck_mod_Form #jcxm_K").hide();
		}else if(cskz1=="K"){
			$("#recheck_mod_Form #jcxm_D").hide();
			$("#recheck_mod_Form #jcxm_R").hide();
			$("#recheck_mod_Form #jcxm_C").hide();
			$("#recheck_mod_Form #jcxm_Z").hide();
			$("#recheck_mod_Form #jcxm_Z6").hide();
			// $("#recheck_mod_Form #jcxm_Z8").hide();
			// $("#recheck_mod_Form #jcxm_Z12").hide();
			// $("#recheck_mod_Form #jcxm_Z18").hide();
			$("#recheck_mod_Form #jcxm_F").hide();
			$("#recheck_mod_Form #jcxm_T3").hide();
			$("#recheck_mod_Form #jcxm_T6").hide();
			$("#recheck_mod_Form #jcxm_K").show();
		}
		
		$("#recheck_mod_Form .jcxmDiv").each(function(){
			if($(this).attr("cskz3")!=null&&$(this).attr("cskz3").indexOf("_REM")!=-1){
				$(this).hide();
			}
		});
		
		$("#recheck_mod_Form #jcxm_span").text("复测项目");
	}else{
		$("#recheck_mod_Form #jcxm_span").text("项目");
	}
	
	if($("#fixtop #lastStep").val()=="true"|| $("#auditAjaxForm #lastStep").val() =="true"){
		$("#recheck_mod_Form .jcxm_radio").each(function(index){
			if(this.checked==true){
				var cskz1=$(this).attr("cskz1");
				switchs(cskz1);
			}
		})
	}

	//添加日期控件
	laydate.render({
		elem: '#recheck_mod_Form #fkrq',
		theme: '#2381E9'
	});
	//添加日期控件
	laydate.render({
	   elem: '#recheck_mod_Form #syrq'
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
	laydate.render({
	   elem: '#recheck_mod_Form #dsyrq'
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
	laydate.render({
	   elem: '#recheck_mod_Form #qtsyrq'
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
	
	//判断是否显示检测单位修改按钮
	//判断审核类别是否存在，如果审核类别存在说明是什么操作，如果不存在说明是修改操作
	if($("#fixtop #shlb").val()){
		$("#recheck_mod_Form #modifBtn").show();
		$("#recheck_mod_Form #url").val("/recheck/recheck/pagedataUpdateJcdw");
	}else if($("#auditAjaxForm #shlb").val()){
		$("#recheck_mod_Form #modifBtn").show();
		$("#recheck_mod_Form #url").val("/ws/recheck/pagedataUpdateJcdw");
	}else{
		$("#recheck_mod_Form #modifBtn").hide();
	}
	showHistoryData();
})

function switchs(cskz1){
	if(cskz1=="D"){
		$("#recheck_mod_Form #D_div").show();
		$("#recheck_mod_Form #R_div").hide();
		$("#recheck_mod_Form #qt_div").hide();
		$("#recheck_mod_Form #dsyrq").removeAttr("disabled");
		$("#recheck_mod_Form #syrq").attr("disabled","disabled");
		$("#recheck_mod_Form #qtsyrq").attr("disabled","disabled");
	}else if(cskz1=="R"){
		$("#recheck_mod_Form #R_div").show();
		$("#recheck_mod_Form #D_div").hide();
		$("#recheck_mod_Form #qt_div").hide();
		$("#recheck_mod_Form #syrq").removeAttr("disabled");
		$("#recheck_mod_Form #dsyrq").attr("disabled","disabled");
		$("#recheck_mod_Form #qtsyrq").attr("disabled","disabled");
	}else if(cskz1=="C"){
		$("#recheck_mod_Form #D_div").show();
		$("#recheck_mod_Form #R_div").show();
		$("#recheck_mod_Form #qt_div").hide();
		$("#recheck_mod_Form #dsyrq").removeAttr("disabled");
		$("#recheck_mod_Form #syrq").removeAttr("disabled");
		$("#recheck_mod_Form #qtsyrq").attr("disabled","disabled");
	}else if(cskz1=="Z" || cskz1=="F"){
		$("#recheck_mod_Form #R_div").hide();
		$("#recheck_mod_Form #D_div").hide();
		$("#recheck_mod_Form #qt_div").show();
		$("#recheck_mod_Form #qtsyrq").removeAttr("disabled");
		$("#recheck_mod_Form #dsyrq").attr("disabled","disabled");
		$("#recheck_mod_Form #syrq").attr("disabled","disabled");
	}
}

$("#recheck_mod_Form .jcxm_radio").click(function(){
	if($("#fixtop #lastStep").val()=="true"|| $("#auditAjaxForm #lastStep").val() =="true"){
		if(this.checked==true){
			var cskz1=$(this).attr("cskz1");
			switchs(cskz1);
		}
	}
	var csid=$(this).val();
	$("#recheck_mod_Form #jczxm").val("");
	if(detectsublist){
		var html="";
		for (var i = 0; i < detectsublist.length; i++) {
			if(csid==detectsublist[i].fcsid){
				html+="<div class='col-xs-4 col-sm-4 col-md-4 col-lg-4' style='padding-left:0px;'>";
				html+="<label class='checkboxLabel checkbox-inline' style='padding-left:0px;'>";
				html+="<input type='radio' id='zxm' name='zxm' value='"+detectsublist[i].csid+"' onclick='changeZxm(\""+detectsublist[i].csid+"\")'/>";
				html+="<span>"+detectsublist[i].csmc+"</span>";
				html+="</label>";
				html+="</div>";
			}
		}
		if(html){
			$("#recheck_mod_Form #jczxmDiv").show();
			$("#recheck_mod_Form #jczxm_div").empty();
			$("#recheck_mod_Form  #jczxm_div").append(html);
		}else{
			$("#recheck_mod_Form #jczxm_div").empty();
			$("#recheck_mod_Form #jczxmDiv").hide();
		}
	}else{
		$("#recheck_mod_Form #jczxm_div").empty();
		$("#recheck_mod_Form #jczxmDiv").hide();
	}
	showHistoryData();
})

function changeZxm(csid){
	$("#recheck_mod_Form #jczxm").val(csid);
	showHistoryData();
}

function showHistoryData(){
	let jcxmid = $('#recheck_mod_Form input:radio[name="jcxm"]:checked').val();
	let jczxmid = $("#recheck_mod_Form #jczxm").val();

	if(jcxmid==null || jcxmid==""){
		let html = "<div class='form-group' ><label class='col-sm-2 col-xs-2  control-label' style='color: black'>历史记录</label><table class='table table-hover table-bordered' style='width: 80%;table-layout: fixed;margin: auto;'>";
		html += "<thead><tr><th width='25%'>类型</th><th width='25%'>录入人员</th><th width='25%'>录入时间</th><th width='25%'>状态</th></tr></thead><tbody>";
		html += "</tbody></table></div>";
		$("#recheck_mod_Form #historytable").empty();
		$("#recheck_mod_Form #historytable").append(html);
	}else{
		$.ajax({
			type : "post",
			url: "/recheck/recheck/pagedataRecheckHistory",
			data : {"sjid":$("#recheck_mod_Form #sjid").val(),"jcxm":jcxmid,"jczxm":jczxmid,"access_token":$("#ac_tk").val()},
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
				$("#recheck_mod_Form #historytable").empty();
				$("#recheck_mod_Form #historytable").append(html);
			}
		});
	}
}

function modifJcdw(){
	$.confirm('确定要修改当前检测单位?',function(result){
		if(result){
			var fjid=$("#recheck_mod_Form #fjid").val();
			var jcdw=$("#recheck_mod_Form #jcdw").val();
			var yjcdw=$("#recheck_mod_Form #yjcdw").val();
			var url=$("#recheck_mod_Form #url").val();
			$.ajax({
				url:url,
				type : "POST",
				data:{"fjid":fjid,"jcdw":jcdw,"yjcdw":yjcdw,"access_token":$("#ac_tk").val()},
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
