/**
 * 绑定按钮事件
 */
function btnBind(){
	
	var sel_bxid = $("#ajaxForm #sp_bxid");
	var sel_chtid = $("#ajaxForm #sp_chtid");
	var sel_hzid = $("#ajaxForm #sp_hzid");
	var sel_yblx = $("#ajaxForm #sp_yblx");
	var sel_qswz = $("#ajaxForm #sp_qswz");
	var sel_jswz = $("#ajaxForm #sp_jswz");
	var inp_sl = $("#ajaxForm #sp_sl");
	var add_sp  =$("#ajaxForm #btn-add-sp");
	
	//添加日期控件
	laydate.render({
	   elem: '#sp_hqsj'
	  ,theme: '#2381E9'
	});
	
	//冰箱下拉框改变事件
	sel_bxid.unbind("change").change(function(){
		bxEvent();
	});
	//抽屉下拉框改变事件
	sel_chtid.unbind("change").change(function(){
		ctEvent();
	});
	//盒子下拉框改变事件
	sel_hzid.unbind("change").change(function(){
		hzEvent();
	});
	//数量离开焦点事件
	inp_sl.unbind("onblur").blur(function(){
		var options = $("#sp_yblx option:selected").attr("cskz2");
		if(options=="1"){
			
		}else{
			slBlurEvent();
		}
	});
	//标本类型改变事件
	sel_yblx.unbind("change").change(function(){
		yblxEvent();
	});
	//起始位置改变事件
	sel_qswz.unbind("change").change(function(){
		reserveDisplay();
	});
	//结束位置改变事件
	sel_jswz.unbind("change").change(function(){
		reserveDisplay();
	});
	//增加标本点击事件
	add_sp.unbind("click").click(function(){
		addSampList();
	});
}
/**
 * 判断选择的标本是否为企参盘（如果为企参盘，单位改变为盒，不能修改）
 * @returns
 */
function yblxEvent(){
	var dw=$("#dw_span").text();
	var sl=$("#sl_span").text();
	var value=$("#sp_yblx option:selected").attr("cskz2");
	if(value&&value=="1"){
		//如果为企参盘，开始结束位置选定第一个和最后一个。不能修改.
		$("#sp_qswz").find("option[value = '1']").attr("selected","selected");
		$("#sp_qswz").trigger("chosen:updated");
		$("#sp_jswz option:last").attr("selected", 'selected');
		$("#sp_jswz").trigger("chosen:updated");
		//如果为企参盘，显示下方的类型，编号，批号
		$("#ybgg").show();
		$("#cpbh").show();
		$("#ybph").show();
		//企参盘的数量为1，且不能修改
		$("#sp_sl").val("1");
		$("#sp_sl").attr("readonly","readonly");
		$("#ajaxForm #sp_dw").attr("readonly","readonly");
		$("#sp_qswz").attr("disabled","disabled");
		$("#sp_qswz").trigger("chosen:updated");
		$("#sp_jswz").attr("disabled","disabled");
		$("#sp_jswz").trigger("chosen:updated");
		
	}else{
		//如果不为企参盘，不显示下方的类型，编号，批号
		$("#ybgg").hide();
		$("#cpbh").hide();
		$("#ybph").hide();
		$("#sp_sl").val(sl);
		$("#sp_sl").removeAttr("readonly");
		$("#ajaxForm #sp_dw").removeAttr("readonly");
		$("#sp_qswz").removeAttr("disabled");
		$("#sp_qswz").trigger("chosen:updated");
		$("#sp_jswz").removeAttr("disabled");
		$("#sp_jswz").trigger("chosen:updated");
		slBlurEvent();
	}
}
/**
 * 冰箱下拉框事件
 */
function bxEvent(){
	var bxid = $("#ajaxForm #sp_bxid").val();
	if(bxid == null || bxid==""){
		var sbHtml = "";
		sbHtml += "<option value=''>--请选择--</option>";
		$("#sp_chtid").empty();
    	$("#sp_chtid").append(sbHtml);
		$("#sp_chtid").trigger("chosen:updated");
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
            	$("#sp_chtid").empty();
            	$("#sp_chtid").append(sbHtml);
            	$("#sp_chtid").trigger("chosen:updated");
	    	}else{
	    		var sbHtml = "";
	    		sbHtml += "<option value=''>--请选择--</option>";
	    		$("#sp_chtid").empty();
            	$("#sp_chtid").append(sbHtml);
            	$("#sp_chtid").trigger("chosen:updated");
	    	}
	    }
	});
}

/**
 * 抽屉下拉框事件
 */
function ctEvent(){
	var chtid = $("#sp_chtid").val();
	if(chtid == null || chtid==""){
		var sbHtml = "";
		sbHtml += "<option value=''>--请选择--</option>";
		$("#sp_hzid").empty();
		$("#sp_hzid").append(sbHtml);
		$("#sp_hzid").trigger("chosen:updated");
		return;
	}
	$.ajax({ 
	    type:'post',  
	    url:"/sample/device/pagedataAnsyGetDeviceList", 
	    cache: false,
	    data: {"fsbid":chtid,"sblx":"hz","access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    async: false,
	    success:function(data){
	    		if(data != null && data.length != 0){
		    		var sbHtml = "";
		    		sbHtml += "<option value=''>--请选择--</option>";
	            	$.each(data,function(i){   		
	            		sbHtml += "<option value='" + data[i].sbid + "' cskz1='" + data[i].cfs + "'>" + data[i].sbh + "</option>";
	            	});
	            	$("#sp_hzid").empty();
	            	$("#sp_hzid").append(sbHtml);
	            	$("#sp_hzid").trigger("chosen:updated");
		    	}else{
		    		var sbHtml = "";
		    		sbHtml += "<option value=''>--请选择--</option>";
		    		$("#sp_hzid").empty();
	            	$("#sp_hzid").append(sbHtml);
	            	$("#sp_hzid").trigger("chosen:updated");
		    	}
	    }
	});
}

/**
 * 盒子下拉框事件
 */
function hzEvent(){
	var hzid = $("#sp_hzid").val();
	if(hzid == null || hzid==""){
		$("#sp_qswz").empty();
		$("#sp_qswz").trigger("chosen:updated");
		$("#sp_jswz").empty();
		$("#sp_jswz").trigger("chosen:updated");
		$("#sp_kxtp").empty();
		return;
	}
	$.ajax({ 
	    type:'post',  
	    url:"/sample/device/pagedataAnsyGetPosition", 
	    cache: false,
	    data: {"sbid":hzid,"cfs":$("#sp_hzid").find("option:selected").attr("cskz1"),"access_token":$("#ac_tk").val()},  
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
            	$("#sp_qswz").empty();
            	$("#sp_qswz").append(sbHtml);
            	$("#sp_qswz").trigger("chosen:updated");
            	$("#sp_jswz").empty();
            	$("#sp_jswz").append(sbHtml);
            	$("#sp_jswz").trigger("chosen:updated");
            	$("#sp_kxtp").empty().append( "<tr>"+tableHeadHtml +"</tr>" + tableHtml );
	    	}else{
	    		var sbHtml = "";
	    		sbHtml += "<option value=''>--请选择--</option>";
	    		$("#sp_qswz").empty();
            	$("#sp_qswz").append(sbHtml);
            	$("#sp_qswz").trigger("chosen:updated");
            	$("#sp_jswz").empty();
            	$("#sp_jswz").append(sbHtml);
            	$("#sp_jswz").trigger("chosen:updated");
	    	}
	    }
	});
}

/**
 * 数量离开焦点事件
 */
function slBlurEvent(){
	var sl = $("#sp_sl").val();
	if(sl == null || sl==""){
		return;
	}
	var yblx = $("#sp_yblx").val();
	if(yblx == null || yblx==""){
		return;
	}
	$.ajax({ 
	    type:'post',  
	    url:"/sample/device/pagedataAnsyGetRecommendPos",
	    cache: false,
	    data: {"yblx":yblx,"sl":sl,"ybid":$("#ajaxForm #sp_ybid").val(),"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	if(data && data.sbkxglDto){
	    		$("#sp_bxid").val(data.sbkxglDto.bxid);
            	$("#sp_bxid").trigger("chosen:updated");
            	var ctHtml = "";
            	ctHtml += "<option value=''>--请选择--</option>";
            	$.each(data.ctList,function(i){   		
            		ctHtml += "<option value='" + data.ctList[i].sbid + "'>" + data.ctList[i].sbh + "</option>";
            	});
            	$("#sp_chtid").append(ctHtml);
	    		$("#sp_chtid").val(data.sbkxglDto.fsbid);
            	$("#sp_chtid").trigger("chosen:updated");
            	
            	var hzHtml = "";
            	hzHtml += "<option value=''>--请选择--</option>";
            	$.each(data.hzList,function(i){   		
            		hzHtml += "<option value='" + data.hzList[i].sbid + "'>" + data.hzList[i].sbh + "</option>";
            	});
            	$("#sp_hzid").append(hzHtml);
	    		$("#sp_hzid").val(data.sbkxglDto.sbid);
            	$("#sp_hzid").trigger("chosen:updated");
            	
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
            	$("#sp_qswz").empty();
            	$("#sp_qswz").append(sbHtml);
            	$("#sp_qswz").val(data.sbkxglDto.cfqswz);
            	$("#sp_qswz").trigger("chosen:updated");
            	$("#sp_jswz").empty();
            	$("#sp_jswz").append(sbHtml);
            	$("#sp_jswz").val(data.sbkxglDto.cfjswz);
            	$("#sp_jswz").trigger("chosen:updated");
            	$("#sp_kxtp").empty().append( "<tr>"+tableHeadHtml +"</tr>" + tableHtml );
            	reserveDisplay();
	    	}
	    }
	});
}

/**
 * 在图标里显示预定信息
 */
function reserveDisplay(){
	var s_td = $("#sp_kxtp .td-class");
	var qswz = parseInt($("#sp_qswz").val());
	var jswz = parseInt($("#sp_jswz").val());
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
	var s_option = $("#sp_qswz .tab-sel");
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
	$("#sp_kxtp").empty().append( "<tr>"+tableHeadHtml +"</tr>" + tableHtml + "</tr>" );
}

//初始化时的表格共同处理
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

//增加标本按钮事件
function addSampList(){
	var tableHtml="";
	var size = $("#sample_reg_table").find("tr").length;
	var index = size + 1;
	if(checkValue()){
		tableHtml +="<tr>";
		tableHtml +="<input type='hidden' name='mxs[" + (index-1) + "].lyid' value='" + $("#ajaxForm #sp_lyid").val() + "'/>";
		tableHtml +="<input type='hidden' name='mxs[" + (index-1) + "].yblx' value='" + $("#ajaxForm #sp_yblx").val() + "'/>";
		tableHtml +="<input type='hidden' name='mxs[" + (index-1) + "].sl' value='" + $("#ajaxForm #sp_sl").val() + "'/>";
		tableHtml +="<input type='hidden' name='mxs[" + (index-1) + "].nd' value='" + $("#ajaxForm #sp_nd").val() + "'/>";
		tableHtml +="<input type='hidden' name='mxs[" + (index-1) + "].dw' value='" + $("#ajaxForm #sp_dw").val() + "'/>";
		tableHtml +="<input type='hidden' name='mxs[" + (index-1) + "].bxid' value='" + $("#ajaxForm #sp_bxid").val() + "'/>";
		tableHtml +="<input type='hidden' name='mxs[" + (index-1) + "].chtid' value='" + $("#ajaxForm #sp_chtid").val() + "'/>";
		tableHtml +="<input type='hidden' name='mxs[" + (index-1) + "].hzid' id='h_hz' value='" + $("#ajaxForm #sp_hzid").val() + "'/>";
		tableHtml +="<input type='hidden' name='mxs[" + (index-1) + "].qswz' value='" + $("#ajaxForm #sp_qswz").val() + "'/>";
		tableHtml +="<input type='hidden' name='mxs[" + (index-1) + "].jswz' value='" + $("#ajaxForm #sp_jswz").val() + "'/>";
		tableHtml +="<td id='row_" + index +"' class='td_row'>"+ index +"</td>";
		tableHtml +="<td>"+ $("#ajaxForm #sp_yblx").find("option:selected").text() +"</td>";
		tableHtml +="<td>"+ $("#ajaxForm #sp_bxid").find("option:selected").text() +"</td>";
		tableHtml +="<td>"+ $("#ajaxForm #sp_chtid").find("option:selected").text() +"</td>";
		tableHtml +="<td>"+ $("#ajaxForm #sp_hzid").find("option:selected").text() +"</td>";
		tableHtml +="<td class='td_sl'>"+ $("#ajaxForm #sp_sl").val() +"</td>";
		tableHtml +="<td>"+ $("#ajaxForm #sp_dw").val() +"</td>";
		tableHtml +="<td>"+ $("#ajaxForm #sp_nd").val() +"</td>";
		tableHtml +="<td><input type='button' name='row_del' value='删除' onclick='javascript:delsamprow(" + index + ")'></td>";
		tableHtml +="</tr>";
		$("#sample_reg_table").append(tableHtml);
	}
}
/**
 * 检查输入的值，是否存在未输入的情况
 */
function checkValue(){
	if($("#ajaxForm #sp_yblx").val()==''){
		$.alert("请选择标本类型!");
		return false;
	}
	if($("#ajaxForm #sp_sl").val()==''){
		$.alert("请输入数量!");
		return false;
	}
	if($("#ajaxForm #sp_bxid").val()==''){
		$.alert("请选择冰箱号!");
		return false;
	}
	if($("#ajaxForm #sp_chtid").val()==''){
		$.alert("请选择抽屉号!");
		return false;
	}
	if($("#ajaxForm #sp_hzid").val()==''){
		$.alert("请选择盒子号!");
		return false;
	}
	if($("#ajaxForm #sp_qswz").val()==''){
		$.alert("请选择起始位置!");
		return false;
	}
	if($("#ajaxForm #sp_jswz").val()==''){
		$.alert("请选择结束位置!");
		return false;
	}
	return true;
}

function delsamprow(index){
	$("#table_dragsort #row_"+index).parent().remove();
	refreshRow();
}

function refreshRow(){
	var td_row = $("#table_dragsort").find(".td_row");
	$.each(td_row,function(i){
		var id =td_row[i].getAttribute("id");
		var index =$(td_row[i]).text();
		td_row[i].setAttribute("id","row_"+(i+1));
		$(td_row[i]).text(i+1);
		$("#table_dragsort input[name='mxs[" + (index-1) + "].lyid']").attr("name","mxs[" + i + "].lyid");
		$("#table_dragsort input[name='mxs[" + (index-1) + "].yblx']").attr("name","mxs[" + i + "].yblx");
		$("#table_dragsort input[name='mxs[" + (index-1) + "].sl']").attr("name","mxs[" + i + "].sl");
		$("#table_dragsort input[name='mxs[" + (index-1) + "].nd']").attr("name","mxs[" + i + "].nd");
		$("#table_dragsort input[name='mxs[" + (index-1) + "].dw']").attr("name","mxs[" + i + "].dw");
		$("#table_dragsort input[name='mxs[" + (index-1) + "].bxid']").attr("name","mxs[" + i + "].bxid");
		$("#table_dragsort input[name='mxs[" + (index-1) + "].chtid']").attr("name","mxs[" + i + "].chtid");
		$("#table_dragsort input[name='mxs[" + (index-1) + "].hzid']").attr("name","mxs[" + i + "].hzid");
		$("#table_dragsort input[name='mxs[" + (index-1) + "].qswz']").attr("name","mxs[" + i + "].qswz");
		$("#table_dragsort input[name='mxs[" + (index-1) + "].jswz']").attr("name","mxs[" + i + "].jswz");
	});
}

$(document).ready(function(){
	//所有下拉框添加choose样式
	jQuery('.chosen-select').chosen({width: '100%'});
	btnBind();
	displayTable();
	$("#dw_span").text($("#sp_dw").val());
	$("#sl_span").text($("#sp_sl").val());
	yblxEvent();
});