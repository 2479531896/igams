/**
 * 绑定按钮事件
 */
function btnBind(){
	var sel_wjfl = $("#bulkimportsForm #wjfl");
	//文件分类下拉框改变事件
	sel_wjfl.unbind("change").change(function(){
		wjflEvent();
	});
	
	var sel_wjlb = $("#bulkimportsForm #wjlb");
	//文件类别下拉框改变事件
	sel_wjlb.unbind("change").change(function(){
		wjlbEvent();
	});
	//添加日期控件
	laydate.render({
	   elem: '#bulkimportsForm #sxrq'
	  ,theme: '#2381E9'
	});
}
/**
 * 文件分类下拉框事件
 */
function wjflEvent(){
	fl_addkzzd="";
	lb_addkzzd="";
	$("#bulkimportsForm #t_kzzd").empty();//清空扩展字段
	$("#bulkimportsForm #kzzd").empty();//清空扩展字段
	var wjfl = $("#bulkimportsForm #wjfl").val();
	if(wjfl == null || wjfl==""){
		var wjlbHtml = "";
		wjlbHtml += "<option value=''>--请选择--</option>";
		$("#bulkimportsForm #wjlb").empty();
    	$("#bulkimportsForm #wjlb").append(wjlbHtml);
		$("#bulkimportsForm #wjlb").trigger("chosen:updated");
		return;
	}
	var url="/systemmain/data/ansyGetJcsjList";
	if($("#bulkimportsForm #fwbj").val()=="/ws"){
		url=$("#bulkimportsForm #fwbj").val()+"/ws/data/ansyGetJcsjList";
	}
	$.ajax({ 
	    type:'post',  
	    url:$('#bulkimportsForm #urlPrefix').val() + url,
	    cache: false,
	    data: {"fcsid":wjfl,"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	if(data != null && data.length != 0){
	    		var wjlbHtml = "";
	    		wjlbHtml += "<option value=''>--请选择--</option>";
            	$.each(data,function(i){
            		wjlbHtml += "<option value='" + data[i].csid + "' + cskz2='" + data[i].cskz2 + "'+ cskz3='" + data[i].cskz3 + "'>" + data[i].csdm + "--" + data[i].csmc + "</option>";
            	});
            	$("#bulkimportsForm #wjlb").empty();
            	$("#bulkimportsForm #wjlb").append(wjlbHtml);
            	$("#bulkimportsForm #wjlb").trigger("chosen:updated");
	    	}else{
	    		var wjlbHtml = "";
	    		wjlbHtml += "<option value=''>--请选择--</option>";
	    		$("#bulkimportsForm #wjlb").empty();
            	$("#bulkimportsForm #wjlb").append(wjlbHtml);
            	$("#bulkimportsForm #wjlb").trigger("chosen:updated");
	    	}
	    	//获取扩展字段
	    	$.ajax({ 
	    	    type:'post',  
	    	    url:$('#bulkimportsForm #urlPrefix').val()+"/production/document/pagedataGetExtendField",
	    	    cache: false,
	    	    data: {"wjfl":wjfl,"access_token":$("#ac_tk").val()},  
	    	    dataType:'json', 
	    	    success:function(data){
	    	    	//返回值
	    	    	if(data.wjflkzlist!=null){
	    	    		if(data.wjflkzlist.length>0){
	    	    			for(var i=0;i<data.wjflkzlist.length;i++){
	    	    				var szlb=data.wjflkzlist[i].szlb;
	    	    				var str_szlb=szlb.split(".");
	    	    				fl_addkzzd=fl_addkzzd+","+str_szlb[str_szlb.length-1];
	    	    				var html="";
	    	    				html+="<div class='col-md-6 col-sm-6'>"+
	    	    				"<div class='form-group'>"+
	    	    				"<label for='' class='col-sm-4 col-md-4 control-label'>"+
	    	    				data.wjflkzlist[i].szmc+"<span style='color: red;'>*</span>"+
	    	    				"</label>"+
	    	    				"<div class='col-sm-7 col-xs-12 col-md-7'> "+
	    	    				"<input type='text' id='"+data.wjflkzlist[i].szz+"' name='"+data.wjflkzlist[i].szz+"' validate='{required:true,stringMaxLength:32}' class='form-control' />"+
	    	    				"</div>"+
	    	    				"</div>"+
	    	    				"</div>";
	    	    				$("#bulkimportsForm #kzzd").append(html);
	    	    			}
	    	    			fl_addkzzd=fl_addkzzd.substring(1);
	    	    		}
	    	    	}
	    	    }
	    	});
	    }
	});
}

function wjlbEvent(){
	var cskz2 = $("#bulkimportsForm #wjlb").find("option:selected").attr("cskz2");
	if (cskz2=="0"){
		$("#bulkimportsForm #glmc").text("关联计量")
	}else if (cskz2=="1"){
		$("#bulkimportsForm #glmc").text("关联验证")
	}else {
		$("#bulkimportsForm #glmc").text("关联设备")
	}
	$("#bulkimportsForm #sbmc").val("");
	$("#bulkimportsForm #sbysid").val("");
	$("#bulkimportsForm #sbglid").val("");
	$("#bulkimportsForm #wjlbcskz2").val(cskz2);
	lb_addkzzd=fl_addkzzd;
	$("#bulkimportsForm #t_kzzd").empty();//清空扩展字段
	var wjlb = $("#bulkimportsForm #wjlb").val();
	//获取扩展字段
	$.ajax({ 
	    type:'post',  
	    url:$('#bulkimportsForm #urlPrefix').val()+"/production/document/pagedataGetExtendField",
	    cache: false,
	    data: {"wjlb":wjlb,"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	//返回值
	    	if(data.wjlbkzlist!=null){
	    		if(data.wjlbkzlist.length>0){
	    			for(var i=0;i<data.wjlbkzlist.length;i++){
	    				var szlb=data.wjlbkzlist[i].szlb;
	    				var str_szlb=szlb.split(".");
	    				if(lb_addkzzd.indexOf(str_szlb[str_szlb.length-1])==-1){
	    					lb_addkzzd=lb_addkzzd+","+str_szlb[str_szlb.length-1]
		    				var html="";
		    				html+="<div class='col-md-6 col-sm-6'>"+
		    				"<div class='form-group'>"+
		    				"<label for='' class='col-sm-4 col-md-4 control-label'>"+
		    				data.wjlbkzlist[i].szmc+"<span style='color: red;'>*</span>"+
		    				"</label>"+
		    				"<div class='col-sm-7 col-xs-12 col-md-7'> "+
		    				"<input type='text' id='"+data.wjlbkzlist[i].szz+"' name='"+data.wjlbkzlist[i].szz+"' validate='{required:true,stringMaxLength:32}' class='form-control' />"+
		    				"</div>"+
		    				"</div>"+
		    				"</div>";
		    				$("#bulkimportsForm #t_kzzd").append(html);
	    				}
	    			}
	    			lb_addkzzd=lb_addkzzd.substring(1);
	    		}
	    	}
	    }
	});
}





function chooseBm() {
	var url = "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	if($("#fwbj").val()=='/ws'){
		url= "/ws/production/department/Departmentlist?fwbj="+$("#fwbj").val();
	}
	url = $('#bulkimportsForm #urlPrefix').val()+ url;
	$.showDialog(url, '选择机构', chooseBmConfig);
}
var chooseBmConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseBmModal",
	formName	: "bulkimportsForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#listDepartmentForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#bulkimportsForm #sqbm').val(sel_row[0].jgid);
					$('#bulkimportsForm #sqbmmc').val(sel_row[0].jgmc);
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
//打开文件上传界面
function editfile(divName,btnName){
	$("#bulkimportsForm"+"  #"+btnName).hide();
	$("#bulkimportsForm"+"  #"+divName).show();
}
//关闭文件上传界面
function cancelfile(divName,btnName){
	$("#bulkimportsForm"+"  #"+btnName).show();
	$("#bulkimportsForm"+"  #"+divName).hide();
}

function displayUpInfo(fjid){
    if(!$("#bulkimportsForm #fjids").val()){
        $("#bulkimportsForm #fjids").val(fjid);
    }else{
        $("#bulkimportsForm #fjids").val($("#bulkimportsForm #fjids").val()+","+fjid);
    }
}


$(document).ready(function(){
	//0.初始化fileinput
	var oFileInput = new FileInput();
	var sign_params=[];
	sign_params.prefix=$("#bulkimportsForm #urlPrefix").val();
	oFileInput.Init("bulkimportsForm","displayUpInfo",2,1,"doc_file",null,sign_params);
	btnBind();
	//所有下拉框添加choose样式
	jQuery('#bulkimportsForm .chosen-select').chosen({width: '100%'});
});