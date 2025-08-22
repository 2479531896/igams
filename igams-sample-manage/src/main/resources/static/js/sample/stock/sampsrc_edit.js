/**
 * 绑定按钮事件
 */
function btnBind(){
	var formname;
	if($("#ss_formAction").val()=="add")
		formname = "addSampSrc_ajaxForm";
	else
		formname = "modSampSrc_ajaxForm";
	
	var btn_lx = $("input[name='rklx']");
	var btn_save = $("#btn-sampsrc-save");
	var sel_bxid = $("#ss_bxid");
	var sel_chtid = $("#ss_chtid");
	var sel_hzid = $("#ss_hzid");
	var sel_yblx = $("#ss_yblx");
	var sel_qswz = $("#ss_qswz");
	var sel_jswz = $("#ss_jswz");
	var inp_sl = $("#ss_sl");
	btn_lx.unbind("click").click(function(){
		displaySampleInfo();
	});
	
	btn_save.unbind("click").click(function(){
		submitSampSourceForm();
	});
	
	sel_bxid.unbind("change").change(function(){
		bxEvent();
	});
	
	sel_chtid.unbind("change").change(function(){
		ctEvent();
	});
	
	sel_hzid.unbind("change").change(function(){
		hzEvent();
	});
	
	inp_sl.unbind("onblur").blur(function(){
		slBlurEvent();
	});
	
	sel_yblx.unbind("change").change(function(){
		slBlurEvent();
		var rklx;
		if($("#ss_formAction").val()=="add"){
			rklx = $("input[name='rklx']:checked").val();
		}else{
			rklx = $("#rklx").val();
		}
		var cskz2=$("#ss_yblx option:selected").attr("cskz2");
		if(cskz2=='1' && rklx=='00'){
			$("#ybgg").attr("style","display:block;");
			$("#cpbh").attr("style","display:block;");
			$("#ybph").attr("style","display:block;");
			$("#ybbh").attr("style","display:block;");
			$("#ss_dw").val("盒");
		}else{
			$("#ybgg").attr("style","display:none;");
			$("#cpbh").attr("style","display:none;");
			$("#ybph").attr("style","display:none;");
			$("#ybbh").attr("style","display:none;");
			$("#hqhz").empty();
			$("#xlhz").attr("style","display:block;");
			$("#ss_dw").val("");
    		var bxxzoptionval=$("#ss_bxid").find("option:selected").val();
    		$("#ss_bxid").find("option[value='"+bxxzoptionval+"']").prop("selected",false);
    		$("#ss_bxid").find("option[value='mr']").prop("selected",true);
    		$("#ss_bxid").trigger("chosen:updated");
    		var sbHtml = "<option value='mr'>--请先选择冰箱号--</option>";
    		$("#ss_chtid").empty();
    		$("#ss_chtid").append(sbHtml);
    		$("#ss_chtid").trigger("chosen:updated");
		}
	});
	
	sel_qswz.unbind("change").change(function(){
		reserveDisplay();
	});
	sel_jswz.unbind("change").change(function(){
		reserveDisplay();
	});
	
	$("#"+ formname).validateForm({
                beforeValidated: function() {
                    return true
                },
                beforeSubmit: function(h, g, f) {
                    return true
                },
                afterSuccess: function(f, g) {
                    afterSuccessFunc.call(d, f, g)
                }
            })
}

/**
 * 提交表单信息
 */
function submitSampSourceForm(){
	
	var formname;
	if(!$("#addSampSrc_ajaxForm").valid()){
		return false;
	}
	
	if($("#ss_formAction").val()=="add")
		formname = "addSampSrc_ajaxForm";
	else
		formname = "modSampSrc_ajaxForm";
	$("input[name='access_token']").val($("#ac_tk").val());
	
	if($("#ss_yblx").val()==""){
		$.alert("请输入标本类型！")
		return;
	}
	if($("#ss_lyd").val()==""){
		$.alert("请输入来源地！")
		return;
	}
	if($("#ss_yblx").find("option:selected").attr("cskz1")){
		$("#ss_yblxdm").val($("#ss_yblx").find("option:selected").attr("cskz1"));
	}else{
		$("#ss_yblxdm").val($("#ss_yblx").find("option:selected").attr("csdm"));
	}
	if($("#ss_yblx").find("option:selected").attr("cskz2")=='1'){
		var ids=[];
		var yxhzs=$("#hqhz input[type='checkbox']").length;
		if($("#ss_sl").val()!=yxhzs){
			$.alert("标本类型为企参盘时,数量需与选择的盒子数一致!");
			return;
		}else{
			for(var i=0;i<yxhzs;i++){
				var id=$("#hqhz input[type='checkbox']")[i].value;
				ids=ids+","+id;
			}
			ids=ids.substr(1);
			$("#"+$("#ss_formAction").val()+"SampSrc_ajaxForm #ids").val(ids);
		}
		$("#sfqcp").val("1");
	}else{
		$("#sfqcp").val("0");
	}
	
	$('#'+formname).ajaxSubmit(function(date) {
		if(date.status= 'success'){
			$.success(date.message,function() {
				clearFrom();
			});
		}else if(date.status == "fail"){
			$.error(date.message,function() {
			});
		} else{
			$.alert(date.message,function() {
			});
		}
   });
}

function clearFrom(){
//	$("#ss_yblx").val("");
//	$("#ss_wbbh").val("");
//	$("#ss_cysj").val("");
//	$("#ss_jyjg").val("");
//	$("#ss_lyd").val("");
//	$("#xb").val("");
//	$("#yz").val("");
//	$("#tz").val("");
//	$("#ss_xm").val("");
//	$("#ss_sl").val("");
//	$("#ss_dw").val("");
//	$("#ss_nd").val("");
//	$("#ss_bxid").val("");
//	$("#ss_chtid").val("");
//	$("#ss_hzid").val("");
//	$("#ss_qswz").val("");
//	$("#ss_jswz").val("");
//	$("#ss_bz").val("");
//	$("#ss_bxid").trigger("chosen:updated");
//	$("#ss_chtid").trigger("chosen:updated");
//	$("#ss_hzid").trigger("chosen:updated");
//	$("#ss_qswz").trigger("chosen:updated");
//	$("#ss_jswz").trigger("chosen:updated");
//	$("#ss_yblx").trigger("chosen:updated");
	$("#"+$("#ss_formAction").val()+"SampSrc_ajaxForm").load("/sample/stock/addSampSrc?access_token="+$("#ac_tk").val());
}

/**
 * 是否显示正式标本信息
 */
function displaySampleInfo(){
	var rklx;
	if($("#ss_formAction").val()=="add"){
		rklx = $("input[name='rklx']:checked").val();
	}else{
		rklx = $("#rklx").val();
	}
	if(rklx == "00"){
		$(".sample-formal").removeClass("hidden");
		$("#ss_sl").attr("validate","{required:true}");
		$("#ss_dw").attr("validate","{required:true}");
		$("#ss_bxid").attr("validate","{required:true}");
		$("#ss_chtid").attr("validate","{required:true}");
		$("#ss_hzid").attr("validate","{required:true}");
		$("#ss_qswz").attr("validate","{required:true}");
		$("#ss_jswz").attr("validate","{required:true}");
	}else{
		$(".sample-formal").addClass("hidden");
		$("#ss_sl").removeAttr("validate");
		$("#ss_dw").removeAttr("validate");
		$("#ss_nd").removeAttr("validate");
		$("#ss_bxid").removeAttr("validate");
		$("#ss_chtid").removeAttr("validate");
		$("#ss_hzid").removeAttr("validate");
		$("#ss_qswz").removeAttr("validate");
		$("#ss_jswz").removeAttr("validate");
	}
}

/**
 * 冰箱下拉框事件
 */
function bxEvent(){
	var bxid = $("#ss_bxid").val();
	if(bxid == null || bxid==""){
		var sbHtml = "";
		sbHtml += "<option value=''>--请选择--</option>";
		$("#ss_chtid").empty();
    	$("#ss_chtid").append(sbHtml);
		$("#ss_chtid").trigger("chosen:updated");
		return;
	}
	$.ajax({ 
	    type:'post',  
	    url:"/sample/device/pagedataAnsyGetDeviceList", 
	    cache: false,
	    data: {"fsbid":bxid,"sblx":"ct","access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	if(data != null && data.length != 0){
	    		var sbHtml = "";
	    		sbHtml += "<option value=''>--请选择--</option>";
            	$.each(data,function(i){   		
            		sbHtml += "<option value='" + data[i].sbid + "'>" + data[i].sbh + "</option>";
            	});
            	$("#ss_chtid").empty();
            	$("#ss_chtid").append(sbHtml);
            	$("#ss_chtid").trigger("chosen:updated");
	    	}else{
	    		var sbHtml = "";
	    		sbHtml += "<option value=''>--请选择--</option>";
	    		$("#ss_chtid").empty();
            	$("#ss_chtid").append(sbHtml);
            	$("#ss_chtid").trigger("chosen:updated");
	    	}
	    }
	});
}

/**
 * 抽屉下拉框事件
 */
function ctEvent(){
	var chtid = $("#ss_chtid").val();
	var sfqcp = $("#ss_yblx option:selected").attr("cskz2");
	if(chtid == null || chtid==""){
		var sbHtml = "";
		sbHtml += "<option value=''>--请选择--</option>";
		$("#ss_hzid").empty();
		$("#ss_hzid").append(sbHtml);
		$("#ss_hzid").trigger("chosen:updated");
		return;
	}
	$.ajax({ 
	    type:'post',  
	    url:"/sample/device/pagedataAnsyGetDeviceList", 
	    cache: false,
	    data: {"fsbid":chtid,"sfqcp":sfqcp,"sblx":"hz","access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	if(sfqcp=='1'){
	    		$("#ss_hzid").empty();
	    		$("#xlhz").attr("style","display:none;");
	    		if(data != null && data.length != 0){
	    			var sbHtml = "";
	    			$.each(data,function(i){
	    				sbHtml += "<div class='col-sm-6'>" +
	    						"<label class='checkboxLabel checkbox-inline' >" +
	    						"<input type='checkbox' name='hzid' id='dx"+data[i].sbid+"' value='"+data[i].sbid+"'/>"+
	    						"<span id='" + data[i].sbid + "' cskz1='" + data[i].cfs +"'>"+data[i].sbh+"</span>" +
	    						"</label>" +
	    						"</div>";
	    			});
	    			$("#hqhz").empty();
	            	$("#hqhz").append(sbHtml);
	            	$("#hqhz").trigger("chosen:updated");
	    		}else{
	    			$("#hqhz").empty();
	    		}	
	    		//更改位置，固定为A1到I9
	    		var qswzHtml = "<option value='1'>A1</option>";
	    		$("#ss_qswz").empty();
            	$("#ss_qswz").append(qswzHtml);
            	$("#ss_qswz").trigger("chosen:updated");
            	var jswzHtml = "<option value='81'>I9</option>";
	    		$("#ss_jswz").empty();
            	$("#ss_jswz").append(jswzHtml);
            	$("#ss_jswz").trigger("chosen:updated");
	    	}else{
	    		$("#hqhz").empty();
	    		$("#xlhz").attr("style","display:block;");
	    		if(data != null && data.length != 0){
	    			var sbHtml = "";
		    		sbHtml += "<option value=''>--请选择--</option>";
	            	$.each(data,function(i){   		
	            		sbHtml += "<option value='" + data[i].sbid + "' cskz1='" + data[i].cfs + "'>" + data[i].sbh + "</option>";
	            	});
	            	$("#ss_hzid").empty();
	            	$("#ss_hzid").append(sbHtml);
	            	$("#ss_hzid").trigger("chosen:updated");
	    		}else{
	    			var sbHtml = "";
		    		sbHtml += "<option value=''>--请选择--</option>";
		    		$("#ss_hzid").empty();
	            	$("#ss_hzid").append(sbHtml);
	            	$("#ss_hzid").trigger("chosen:updated");
	    		}
	    	}
	    }
	});
}

/**
 * 盒子下拉框事件
 */
function hzEvent(){
	var hzid = $("#ss_hzid").val();
	if(hzid == null || hzid==""){
		$("#ss_qswz").empty();
		$("#ss_qswz").trigger("chosen:updated");
		$("#ss_jswz").empty();
		$("#ss_jswz").trigger("chosen:updated");
		$("#kxtp").empty();
		return;
	}
	$.ajax({ 
	    type:'post',  
	    url:"/sample/device/pagedataAnsyGetPosition", 
	    cache: false,
	    data: {"sbid":hzid,"cfs":$("#ss_hzid").find("option:selected").attr("cskz1"),"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	if(data != null && data.wzlist){
	    		var sbHtml = "";
	    		var tableHtml = "";
	    		var tableHeadHtml = "";
            	$.each(data.wzlist,function(i){
            		tableHtml +="<tr>";
            		$.each(data.wzlist[i],function(j){
            			sbHtml += "<option value='" + data.wzlist[i][j].csid + "'>" + data.wzlist[i][j].csmc + "</option>";
            			if(i==0 && j==0)
            				tableHeadHtml +="<td>&nbsp;</td>";
            			if(i==0)
            				tableHeadHtml +="<td>"+(j+1)+"</td>";
            			if(j==0)
            				tableHtml +="<td>"+data.wzlist[i][j].csdm+"</td>";
            			if(data.wzlist[i][j].cskz1 =="kx"){
            				tableHtml +="<td style='background-color:#c2ff68' class='td-class' td-mc='"+data.wzlist[i][j].csmc +"' td-wz='"+data.wzlist[i][j].csid +"' td-kzcs='"+data.wzlist[i][j].cskz1 +"'>&nbsp;</td>";
            			}else if(data.wzlist[i][j].cskz1 =="yd"){
            				tableHtml +="<td style='background-color:#666633' class='td-class' td-mc='"+data.wzlist[i][j].csmc +"' td-wz='"+data.wzlist[i][j].csid +"' td-kzcs='"+data.wzlist[i][j].cskz1 +"'>&nbsp;</td>";
            			}else
            				tableHtml +="<td style='background-color:#c48888' class='td-class' td-mc='"+data.wzlist[i][j].csmc +"' td-wz='"+data.wzlist[i][j].csid +"' td-kzcs='"+data.wzlist[i][j].cskz1 +"'>&nbsp;</td>";
            		});
            		tableHtml +="</tr>";
            	});
            	$("#ss_qswz").empty();
            	$("#ss_qswz").append(sbHtml);
            	$("#ss_qswz").trigger("chosen:updated");
            	$("#ss_jswz").empty();
            	$("#ss_jswz").append(sbHtml);
            	$("#ss_jswz").trigger("chosen:updated");
            	$("#kxtp").empty().append( "<tr>"+tableHeadHtml +"</tr>" + tableHtml );
	    	}else{
	    		var sbHtml = "";
	    		sbHtml += "<option value=''>--请选择--</option>";
	    		$("#ss_qswz").empty();
            	$("#ss_qswz").append(sbHtml);
            	$("#ss_qswz").trigger("chosen:updated");
            	$("#ss_jswz").empty();
            	$("#ss_jswz").append(sbHtml);
            	$("#ss_jswz").trigger("chosen:updated");
	    	}
	    }
	});
}

/**
 * 数量离开焦点事件
 */
function slBlurEvent(){
	var sl = $("#ss_sl").val();
	if(sl == null || sl==""){
		return;
	}
	var yblx = $("#ss_yblx").val();
	if(yblx == null || yblx==""){
		return;
	}
	var cskz2 = $("#ss_yblx option:selected").attr("cskz2");
	if(cskz2!='1'){
		$.ajax({ 
		    type:'post',  
		    url:"/sample/device/pagedataAnsyGetRecommendPos",
		    cache: false,
		    data: {"yblx":yblx,"sl":sl,"ybid":$("#ybid").val(),"access_token":$("#ac_tk").val()},  
		    dataType:'json', 
		    success:function(data){
		    	if(data && data.sbkxglDto){
		    		$("#ss_bxid").val(data.sbkxglDto.bxid);
	            	$("#ss_bxid").trigger("chosen:updated");
	            	var ctHtml = "";
	            	ctHtml += "<option value=''>--请选择--</option>";
	            	$.each(data.ctList,function(i){   		
	            		ctHtml += "<option value='" + data.ctList[i].sbid + "'>" + data.ctList[i].sbh + "</option>";
	            	});
	            	$("#ss_chtid").empty();
	            	$("#ss_chtid").append(ctHtml);
		    		$("#ss_chtid").val(data.sbkxglDto.fsbid);
	            	$("#ss_chtid").trigger("chosen:updated");
	            	
	            	var hzHtml = "";
	            	hzHtml += "<option value=''>--请选择--</option>";
	            	$.each(data.hzList,function(i){   		
	            		hzHtml += "<option value='" + data.hzList[i].sbid + "'>" + data.hzList[i].sbh + "</option>";
	            	});
	            	$("#ss_hzid").empty();
	            	$("#ss_hzid").append(hzHtml);
		    		$("#ss_hzid").val(data.sbkxglDto.sbid);
	            	$("#ss_hzid").trigger("chosen:updated");
	            	
	            	var sbHtml = "";
		    		var tableHtml = "";
		    		var tableHeadHtml = "";
	            	$.each(data.wzlist,function(i){
	            		tableHtml +="<tr>";
	            		$.each(data.wzlist[i],function(j){
	            			sbHtml += "<option value='" + data.wzlist[i][j].csid + "'>" + data.wzlist[i][j].csmc + "</option>";
	            			if(i==0 && j==0)
	            				tableHeadHtml +="<td>&nbsp;</td>";
	            			if(i==0)
	            				tableHeadHtml +="<td>"+(j+1)+"</td>";
	            			if(j==0)
	            				tableHtml +="<td>"+data.wzlist[i][j].csdm+"</td>";
	            			if(data.wzlist[i][j].cskz1 =="kx"){
	            				tableHtml +="<td style='background-color:#c2ff68' class='td-class' td-mc='"+data.wzlist[i][j].csmc +"' td-wz='"+data.wzlist[i][j].csid +"' td-kzcs='"+data.wzlist[i][j].cskz1 +"'>&nbsp;</td>";
	            			}else if(data.wzlist[i][j].cskz1 =="yy"){
	            				tableHtml +="<td style='background-color:#666633' class='td-class' td-mc='"+data.wzlist[i][j].csmc +"' td-wz='"+data.wzlist[i][j].csid +"' td-kzcs='"+data.wzlist[i][j].cskz1 +"'>&nbsp;</td>";
	            			}else
	            				tableHtml +="<td style='background-color:#c48888' class='td-class' td-mc='"+data.wzlist[i][j].csmc +"' td-wz='"+data.wzlist[i][j].csid +"' td-kzcs='"+data.wzlist[i][j].cskz1 +"'>&nbsp;</td>";
	            		});
	            		tableHtml +="</tr>";
	            	});
	            	$("#ss_qswz").empty();
	            	$("#ss_qswz").append(sbHtml);
	            	$("#ss_qswz").val(data.sbkxglDto.cfqswz);
	            	$("#ss_qswz").trigger("chosen:updated");
	            	$("#ss_jswz").empty();
	            	$("#ss_jswz").append(sbHtml);
	            	$("#ss_jswz").val(data.sbkxglDto.cfjswz);
	            	$("#ss_jswz").trigger("chosen:updated");
	            	$("#kxtp").empty().append( "<tr>"+tableHeadHtml +"</tr>" + tableHtml );
	            	reserveDisplay();
		    	}
		    }
		});
	}
}

/**
 * 在图标里显示预定信息
 */
function reserveDisplay(){
	var s_td = $("#kxtp .td-class");
	var qswz = parseInt($("#ss_qswz").val());
	var jswz = parseInt($("#ss_jswz").val());
	$.each(s_td,function(i){
		var kzcs =s_td[i].getAttribute("td-kzcs");
		var wz = parseInt(s_td[i].getAttribute("td-wz"));
		if( wz <= jswz && wz>= qswz){
			if( kzcs =="kx")
				s_td[i].setAttribute("style","background-color:#BFEFFF");
			else if( kzcs =="yy")
				s_td[i].setAttribute("style","background-color:#BFEFFF");
			else
				s_td[i].setAttribute("style","background-color:#DC143C");
		}
		else if( kzcs =="kx")
			s_td[i].setAttribute("style","background-color:#c2ff68");
		else if( kzcs =="yy")
			s_td[i].setAttribute("style","background-color:#666633");
		else
			s_td[i].setAttribute("style","background-color:#c48888");
	})
}

//初始化时显示空闲情况
function displayTable(){
	var s_option = $("#ss_qswz .tab-sel");
	var preHead ="";
	var isFirst = true;
	var tableHeadHtml="";
	var tableHtml ="<tr>";
	$.each(s_option,function(i){
		var kzcs =s_option[i].getAttribute("cskz1");
		var head = s_option[i].getAttribute("csdm");
		if(preHead!="" && preHead == head){
			if(isFirst)
				tableHeadHtml +="<td>"+(i+1)+"</td>";
			tableHtml +=getTableHtml(s_option[i]);
		}else if(preHead==""){
			preHead = head;
			if(isFirst){
				tableHeadHtml +="<td>&nbsp;</td>";
				tableHeadHtml +="<td>"+(i+1)+"</td>";
			}
			tableHtml +="<td>"+s_option[i].getAttribute("csdm")+"</td>";
			tableHtml +=getTableHtml(s_option[i]);
		}else{
			isFirst= false;
			tableHtml +="</tr><tr>";
			tableHtml +="<td>"+s_option[i].getAttribute("csdm")+"</td>";
			tableHtml +=getTableHtml(s_option[i]);
			preHead = head;
		}
	});
	$("#kxtp").empty().append( "<tr>"+tableHeadHtml +"</tr>" + tableHtml + "</tr>" );
}

function getTableHtml(s_option){
	var tableHtml;
	if(s_option.getAttribute("cskz1") =="kx"){
		tableHtml +="<td style='background-color:#c2ff68' class='td-class' td-mc='"+s_option.text +"' td-wz='"+s_option.getAttribute("vlaue") +"' td-kzcs='"+s_option.getAttribute("cskz1") +"'>&nbsp;</td>";
	}else if(s_option.getAttribute("cskz1") =="yy"){
		tableHtml +="<td style='background-color:#666633' class='td-class' td-mc='"+s_option.text +"' td-wz='"+s_option.getAttribute("vlaue") +"' td-kzcs='"+s_option.getAttribute("cskz1") +"'>&nbsp;</td>";
	}else
		tableHtml +="<td style='background-color:#c48888' class='td-class' td-mc='"+s_option.text +"' td-wz='"+s_option.getAttribute("vlaue") +"' td-kzcs='"+s_option.getAttribute("cskz1") +"'>&nbsp;</td>";
	return tableHtml;
}

$(document).ready(function(){
	//所有下拉框添加choose样式
	jQuery('.chosen-select').chosen({width: '100%'});
	
	//添加日期控件
	laydate.render({
	   elem: '#ss_cysj'
	  ,theme: '#2381E9'
	}),
	laydate.render({
	   elem: '#ss_hqsj'
	  ,theme: '#2381E9'
	});
	laydate.render({
	   elem: '#ss_ybbhrq'
	  ,theme: '#2381E9'
	});
	
	btnBind();
	//根据入库类型进行显示区分
	displaySampleInfo();
	
	displayTable();
});