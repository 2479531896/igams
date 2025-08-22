var fl_addkzzd="";
var lb_addkzzd="";

/**
 * 绑定按钮事件
 */
function btnBind(){
	var sel_wjfl = $("#ajaxForm #wjfl");
	//文件分类下拉框改变事件
	sel_wjfl.unbind("change").change(function(){
		wjflEvent();
	});
	
	var sel_wjlb = $("#ajaxForm #wjlb");
	//文件类别下拉框改变事件
	sel_wjlb.unbind("change").change(function(){
		wjlbEvent();
	});
	
	//添加日期控件
	laydate.render({
	   elem: '#ajaxForm #sxrq'
	  ,theme: '#2381E9'
	});
	//添加日期控件
//	laydate.render({
//	   elem: '#ajaxForm #wj_qwwcsj'
//	  ,theme: '#2381E9'
//	});
}

function chooseBm() {
	var url = $('#ajaxForm #urlPrefix')+"/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择机构', chooseBmConfig);
}
var chooseBmConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseBmModal",
	formName	: "ajaxForm",
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
					$('#ajaxForm #sqbm').val(sel_row[0].jgid);
					$('#ajaxForm #sqbmmc').val(sel_row[0].jgmc);
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
/**
 * 文件分类下拉框事件
 */
function wjflEvent(){
	fl_addkzzd="";
	lb_addkzzd="";
	$("#ajaxForm #t_kzzd").empty();//清空扩展字段
	$("#ajaxForm #kzzd").empty();//清空扩展字段
	var wjfl = $("#ajaxForm #wjfl").val();
	if(wjfl == null || wjfl==""){
		var wjlbHtml = "";
		wjlbHtml += "<option value=''>--请选择--</option>";
		$("#ajaxForm #wjlb").empty();
    	$("#ajaxForm #wjlb").append(wjlbHtml);
		$("#ajaxForm #wjlb").trigger("chosen:updated");
		return;
	}
	$.ajax({ 
	    type:'post',  
	    url:$('#ajaxForm #urlPrefix').val() + "/systemmain/data/ansyGetJcsjList",
	    cache: false,
	    data: {"fcsid":wjfl,"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	if(data != null && data.length != 0){
	    		var wjlbHtml = "";
	    		wjlbHtml += "<option value=''>--请选择--</option>";
            	$.each(data,function(i){   		
            		wjlbHtml += "<option value='" + data[i].csid + "'>" + data[i].csdm + "--" +  data[i].csmc + "</option>";
            	});
            	$("#ajaxForm #wjlb").empty();
            	$("#ajaxForm #wjlb").append(wjlbHtml);
            	$("#ajaxForm #wjlb").trigger("chosen:updated");
	    	}else{
	    		var wjlbHtml = "";
	    		wjlbHtml += "<option value=''>--请选择--</option>";
	    		$("#ajaxForm #wjlb").empty();
            	$("#ajaxForm #wjlb").append(wjlbHtml);
            	$("#ajaxForm #wjlb").trigger("chosen:updated");
	    	}
	    	
	    	//获取扩展字段
	    	$.ajax({ 
	    	    type:'post',  
	    	    url:"/production/document/pagedataGetExtendField",
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
	    	    				$("#ajaxForm #kzzd").append(html);
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
	lb_addkzzd=fl_addkzzd;
	$("#ajaxForm #t_kzzd").empty();//清空扩展字段
	var wjlb = $("#ajaxForm #wjlb").val();
	//获取扩展字段
	$.ajax({ 
	    type:'post',  
	    url:"/production/document/pagedataGetExtendField",
	    cache: false,
	    data: {"wjlb":wjlb,"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	//返回值
	    	if(data.wjflkzlist!=null){
	    		if(data.wjflkzlist.length>0){
	    			for(var i=0;i<data.wjlbkzlist.length;i++){
	    				var szlb=data.wjflkzlist[i].szlb;
	    				var str_szlb=szlb.split(".");
	    				if(lb_addkzzd.indexOf(str_szlb[str_szlb.length-1])=-1){
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
		    				$("#ajaxForm #t_kzzd").append(html);
	    				}
	    			}
	    			lb_addkzzd=lb_addkzzd.substring(1);
	    		}
	    	}
	    }
	});
}

function init(){
	var jsids = "";
	for (var i = 0; i < $("#ajaxForm #sel_list optgroup option").length; i++) {//循环读取已选角色
		jsids = jsids + ","+ $("#ajaxForm #sel_list optgroup")[0].children[i].value;
	}
	jsids = jsids.substr(1);
	$("#ajaxForm #jsids").val(jsids);
	var t_jsids = "";
	for (var i = 0; i < $("#ajaxForm #t_sel_list optgroup option").length; i++) {//循环读取已选角色
		t_jsids = t_jsids + ","+ $("#ajaxForm #t_sel_list optgroup")[0].children[i].value;
	}
	t_jsids = t_jsids.substr(1);
	$("#ajaxForm #t_jsids").val(t_jsids);
}

var viewPreModConfig = {
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

var vm = new Vue({
	el:'#document_edit',
	data: {
	},
	methods:{
		addOption(){
			var unselected = $("#ajaxForm #sel_unlist optgroup");
			var t_unselected = $("#ajaxForm #t_sel_unlist optgroup");
			var selected = $("#ajaxForm #sel_list optgroup");
			for (var i = $("#ajaxForm #sel_unlist optgroup option").length-1; i >= 0; i--) {
				 if(unselected[0].children[i].selected){
					 selected.append("<option value='"+unselected[0].children[i].value+"'ondblclick='moveOption()'>"+unselected[0].children[i].text+"</option>");
					 unselected[0].children[i].remove();
					 t_unselected[0].children[i].remove();
				 }
			}
			var jsids = "";
			for (var i = 0; i < $("#ajaxForm #sel_list optgroup option").length; i++) {//循环读取已选角色
				jsids = jsids + ","+ $("#ajaxForm #sel_list optgroup")[0].children[i].value;
			}
			jsids = jsids.substr(1);
			$("#ajaxForm #jsids").val(jsids);
		},
		moveOption(){
			var unselected = $("#ajaxForm #sel_unlist optgroup");
			var t_unselected = $("#ajaxForm #t_sel_unlist optgroup");
			var selected = $("#ajaxForm #sel_list optgroup");
			for (var i = $("#ajaxForm #sel_list optgroup option").length-1; i >= 0; i--) {
				 if(selected[0].children[i].selected){
					 unselected.append("<option value='"+selected[0].children[i].value+"'ondblclick='addOption()'>"+selected[0].children[i].text+"</option>");
					 t_unselected.append("<option value='"+selected[0].children[i].value+"'ondblclick='t_addOption()'>"+selected[0].children[i].text+"</option>");
					 selected[0].children[i].remove();
				 }
			}
			var jsids = "";
			for (var i = 0; i < $("#ajaxForm #sel_list optgroup option").length; i++) {//循环读取已选角色
				jsids = jsids + ","+ $("#ajaxForm #sel_list optgroup")[0].children[i].value;
			}
			jsids = jsids.substr(1);
			$("#ajaxForm #jsids").val(jsids);
		},
		t_addOption(){
			var t_unselected = $("#ajaxForm #t_sel_unlist optgroup");
			var t_selected = $("#ajaxForm #t_sel_list optgroup");
			var unselected = $("#ajaxForm #sel_unlist optgroup");
			for (var i = $("#ajaxForm #t_sel_unlist optgroup option").length-1; i >= 0; i--) {
				 if(t_unselected[0].children[i].selected){
					 t_selected.append("<option value='"+t_unselected[0].children[i].value+"'ondblclick='t_moveOption()'>"+t_unselected[0].children[i].text+"</option>");
					 t_unselected[0].children[i].remove();
					 unselected[0].children[i].remove();
				 }
			}
			var jsids = "";
			for (var i = 0; i < $("#ajaxForm #t_sel_list optgroup option").length; i++) {//循环读取已选角色
				jsids = jsids + ","+ $("#ajaxForm #t_sel_list optgroup")[0].children[i].value;
			}
			jsids = jsids.substr(1);
			$("#ajaxForm #t_jsids").val(jsids);
		},
		t_moveOption(){
			var t_unselected = $("#ajaxForm #t_sel_unlist optgroup");
			var t_selected = $("#ajaxForm #t_sel_list optgroup");
			var unselected = $("#ajaxForm #sel_unlist optgroup");
			for (var i = $("#ajaxForm #t_sel_list optgroup option").length-1; i >= 0; i--) {
				 if(t_selected[0].children[i].selected){
					 t_unselected.append("<option value='"+t_selected[0].children[i].value+"'ondblclick='t_addOption()'>"+t_selected[0].children[i].text+"</option>"); 
					 unselected.append("<option value='"+t_selected[0].children[i].value+"'ondblclick='addOption()'>"+t_selected[0].children[i].text+"</option>");
					 t_selected[0].children[i].remove();
				 }
			}
			var jsids = "";
			for (var i = 0; i < $("#ajaxForm #t_sel_list optgroup option").length; i++) {//循环读取已选角色
				jsids = jsids + ","+ $("#ajaxForm #t_sel_list optgroup")[0].children[i].value;
			}
			jsids = jsids.substr(1);
			$("#ajaxForm #t_jsids").val(jsids);
		},
		d_addOption(){
			var d_unselected = $("#ajaxForm #d_sel_unlist optgroup");
			var d_selected = $("#ajaxForm #d_sel_list optgroup");
			for (var i = $("#ajaxForm #d_sel_unlist optgroup option").length-1; i >= 0; i--) {
				 if(d_unselected[0].children[i].selected){
					 d_selected.append("<option value='"+d_unselected[0].children[i].value+"'ondblclick='d_moveOption()'>"+d_unselected[0].children[i].text+"</option>");
					 d_unselected[0].children[i].remove();
				 }
			}
			var jsids = "";
			for (var i = 0; i < $("#ajaxForm #d_sel_list optgroup option").length; i++) {//循环读取已选角色
				jsids = jsids + ","+ $("#ajaxForm #d_sel_list optgroup")[0].children[i].value;
			}
			jsids = jsids.substr(1);
			$("#ajaxForm #d_jsids").val(jsids);
		},
		d_moveOption(){
			var d_unselected = $("#ajaxForm #d_sel_unlist optgroup");
			var d_selected = $("#ajaxForm #d_sel_list optgroup");
			for (var i = $("#ajaxForm #d_sel_list optgroup option").length-1; i >= 0; i--) {
				 if(d_selected[0].children[i].selected){
					 d_unselected.append("<option value='"+d_selected[0].children[i].value+"'ondblclick='d_addOption()'>"+d_selected[0].children[i].text+"</option>");
					 d_selected[0].children[i].remove();
				 }
			}
			var jsids = "";
			for (var i = 0; i < $("#ajaxForm #d_sel_list optgroup option").length; i++) {//循环读取已选角色
				jsids = jsids + ","+ $("#ajaxForm #d_sel_list optgroup")[0].children[i].value;
			}
			jsids = jsids.substr(1);
			$("#ajaxForm #d_jsids").val(jsids);
		},
		view(fjid){
			var url= $('#ajaxForm #urlPrefix').val() + "/common/file/pdfPreview?fjid=" + fjid;
            $.showDialog(url,'文件预览',viewPreModConfig);
            
		},
		xz(fjid){
		    jQuery('<form action="'+$('#ajaxForm #urlPrefix').val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
	                '<input type="text" name="fjid" value="'+fjid+'"/>' + 
	                '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
	            '</form>')
	        .appendTo('body').submit().remove();
		},
		del(fjid,wjlj){
			$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= $('#ajaxForm #urlPrefix').val() + "/common/file/delFile";
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
	}
})
//双击事件
function d_moveOption(){
	var d_unselected = $("#ajaxForm #d_sel_unlist optgroup");
	var d_selected = $("#ajaxForm #d_sel_list optgroup");
	for (var i = $("#ajaxForm #d_sel_list optgroup option").length-1; i >= 0; i--) {
		 if(d_selected[0].children[i].selected){
			 d_unselected.append("<option value='"+d_selected[0].children[i].value+"'ondblclick='d_addOption()'>"+d_selected[0].children[i].text+"</option>");
			 d_selected[0].children[i].remove();
		 }
	}
	var jsids = "";
	for (var i = 0; i < $("#ajaxForm #d_sel_list optgroup option").length; i++) {//循环读取已选角色
		jsids = jsids + ","+ $("#ajaxForm #d_sel_list optgroup")[0].children[i].value;
	}
	jsids = jsids.substr(1);
	$("#ajaxForm #d_jsids").val(jsids);
}

function d_addOption(){
	var d_unselected = $("#ajaxForm #d_sel_unlist optgroup");
	var d_selected = $("#ajaxForm #d_sel_list optgroup");
	for (var i = $("#ajaxForm #d_sel_unlist optgroup option").length-1; i >= 0; i--) {
		 if(d_unselected[0].children[i].selected){
			 d_selected.append("<option value='"+d_unselected[0].children[i].value+"'ondblclick='d_moveOption()'>"+d_unselected[0].children[i].text+"</option>");
			 d_unselected[0].children[i].remove();
		 }
	}
	var jsids = "";
	for (var i = 0; i < $("#ajaxForm #d_sel_list optgroup option").length; i++) {//循环读取已选角色
		jsids = jsids + ","+ $("#ajaxForm #d_sel_list optgroup")[0].children[i].value;
	}
	jsids = jsids.substr(1);
	$("#ajaxForm #d_jsids").val(jsids);
}

//双击事件
function t_moveOption(){
	var t_unselected = $("#ajaxForm #t_sel_unlist optgroup");
	var t_selected = $("#ajaxForm #t_sel_list optgroup");
	var unselected = $("#ajaxForm #sel_unlist optgroup");
	for (var i = $("#ajaxForm #t_sel_list optgroup option").length-1; i >= 0; i--) {
		 if(t_selected[0].children[i].selected){
			 t_unselected.append("<option value='"+t_selected[0].children[i].value+"'ondblclick='t_addOption()'>"+t_selected[0].children[i].text+"</option>"); 
			 unselected.append("<option value='"+t_selected[0].children[i].value+"'ondblclick='addOption()'>"+t_selected[0].children[i].text+"</option>");
			 t_selected[0].children[i].remove();
		 }
	}
	var jsids = "";
	for (var i = 0; i < $("#ajaxForm #t_sel_list optgroup option").length; i++) {//循环读取已选角色
		jsids = jsids + ","+ $("#ajaxForm #t_sel_list optgroup")[0].children[i].value;
	}
	jsids = jsids.substr(1);
	$("#ajaxForm #t_jsids").val(jsids);
}

function t_addOption(){
	var t_unselected = $("#ajaxForm #t_sel_unlist optgroup");
	var t_selected = $("#ajaxForm #t_sel_list optgroup");
	var unselected = $("#ajaxForm #sel_unlist optgroup");
	for (var i = $("#ajaxForm #t_sel_unlist optgroup option").length-1; i >= 0; i--) {
		 if(t_unselected[0].children[i].selected){
			 t_selected.append("<option value='"+t_unselected[0].children[i].value+"'ondblclick='t_moveOption()'>"+t_unselected[0].children[i].text+"</option>");
			 t_unselected[0].children[i].remove();
			 unselected[0].children[i].remove();
		 }
	}
	var jsids = "";
	for (var i = 0; i < $("#ajaxForm #t_sel_list optgroup option").length; i++) {//循环读取已选角色
		jsids = jsids + ","+ $("#ajaxForm #t_sel_list optgroup")[0].children[i].value;
	}
	jsids = jsids.substr(1);
	$("#ajaxForm #t_jsids").val(jsids);
}

//双击事件
function moveOption(){
	var unselected = $("#ajaxForm #sel_unlist optgroup");
	var selected = $("#ajaxForm #sel_list optgroup");
	var t_unselected = $("#ajaxForm #t_sel_unlist optgroup");
	for (var i = $("#ajaxForm #sel_list optgroup option").length-1; i >= 0; i--) {
		 if(selected[0].children[i].selected){
			 unselected.append("<option value='"+selected[0].children[i].value+"'ondblclick='addOption()'>"+selected[0].children[i].text+"</option>"); 
			 t_unselected.append("<option value='"+selected[0].children[i].value+"'ondblclick='t_addOption()'>"+selected[0].children[i].text+"</option>");
			 selected[0].children[i].remove();
		 }
	}
	var jsids = "";
	for (var i = 0; i < $("#ajaxForm #sel_list optgroup option").length; i++) {//循环读取已选角色
		jsids = jsids + ","+ $("#ajaxForm #sel_list optgroup")[0].children[i].value;
	}
	jsids = jsids.substr(1);
	$("#ajaxForm #jsids").val(jsids);
}

function addOption(){
	var unselected = $("#ajaxForm #sel_unlist optgroup");
	var t_unselected = $("#ajaxForm #t_sel_unlist optgroup");
	var selected = $("#ajaxForm #sel_list optgroup");
	for (var i = $("#ajaxForm #sel_unlist optgroup option").length-1; i >= 0; i--) {
		 if(unselected[0].children[i].selected){
			 selected.append("<option value='"+unselected[0].children[i].value+"'ondblclick='moveOption()'>"+unselected[0].children[i].text+"</option>");
			 unselected[0].children[i].remove();
			 t_unselected[0].children[i].remove();
		 }
	}
	var jsids = "";
	for (var i = 0; i < $("#ajaxForm #sel_list optgroup option").length; i++) {//循环读取已选角色
		jsids = jsids + ","+ $("#ajaxForm #sel_list optgroup")[0].children[i].value;
	}
	jsids = jsids.substr(1);
	$("#ajaxForm #jsids").val(jsids);
}

function displayUpInfo(fjid){
	if(!$("#ajaxForm #fjids").val()){
		$("#ajaxForm #fjids").val(fjid);
	}else{
		$("#ajaxForm #fjids").val($("#ajaxForm #fjids").val()+","+fjid);
	}
	var fileName = $("#ajaxForm .file-caption-info").html();
	//获取去掉扩展名的文件名
	var fileStrName = splitFileName(fileName);
	//获取扩展名
	var fileextname = fileName.substring(fileName.lastIndexOf("."), fileName.length);
	//分割文件名
	arr=fileStrName.split("+");
	if(arr.length > 1){
		for (var int = 0; int < arr.length; int++) {
			if(int == 0){
				if($("#ajaxForm #wjbh").val() == null||$("#ajaxForm #wjbh").val() == ""){
					$("#ajaxForm #wjbh").val(arr[0]);
				}
			}else if(int == 1){
				if($("#ajaxForm #wjmc").val() == null||$("#ajaxForm #wjmc").val() == ""){
					$("#ajaxForm #wjmc").val(arr[1]);
				}
			}else if(int == 2){
				if($("#ajaxForm #sxrq").val() == null||$("#ajaxForm #sxrq").val() == ""){
					if(!isNaN(arr[2])){
						var pattern = /(\d{4})(\d{2})(\d{2})/;
						var formatedDate = arr[2].replace(pattern, '$1-$2-$3');
						if(checkDateValid(formatedDate)){
							$("#ajaxForm #sxrq").val(formatedDate);
						}
					}
				}
			}
		}
	}
}

function splitFileName(text) {
    var pattern = /\.{1}[A-z]{1,}$/;
    if (pattern.exec(text) !== null) {
        return (text.slice(0, pattern.exec(text).index));
    } else {
        return text;
    }
}
/*
 *参数
 *      dateStr：客户端输入的日期字符串
 *功能
 *      检查dateStr日期是否有效（年、月、日是否有效），分闰年、平年、大月、小月考虑
 *返回值
 *      true或者false
 */
function checkDateValid(dateStr) {
	var sepa1 = dateStr.indexOf("-");
	var sepa2 = dateStr.indexOf("/");
	var sepa3 = dateStr.indexOf(".");
	var sepa = "";
	if (sepa1 > -1) {
		sepa = "-";
	}else if (sepa2 > -1) {
		sepa = "/";
	}else if (sepa3 > -1) {
		sepa = ".";
	}
	var arrDate = dateStr.split(sepa);
	var year = parseInt(arrDate[0]);
	var month = parseInt(arrDate[1]);
	var day = parseInt(arrDate[2]);
	/*
	 *  datetime数据类型在MS SQL Server 取值范围是1753-1-1到9999-12-31
	 *  if 西元年份是400的倍数 : 闰年
	 * 	else if 西元年份是4的倍数 and 西元年份不是100的倍数 : 闰年
	 * 	else : 平年
	 *  2月：平年为28天，闰年为29天
	 * 	30天的月份：4、6、9、11
	 *  31天的月份：1、3、5、7、8、10、12
	*/
	if (year < 1753 || year > 9999 || month < 1 || month > 12 || day < 1 || day > 31) {
		return false;
	}else {
		if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {    // 闰年
			if ((month == 2 && day > 29) || ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) || ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && day > 31)) {
				return false;
			}else {
				return true;
			}
		}else {  // 平年
			if ((month == 2 && day > 28) || ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) || ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && day > 31)) {
				return false;
			}else {
				return true;
			}
		}
	}
}

$(document).ready(function(){
	//0.初始化fileinput
	var oFileInput = new FileInput();
	var sign_params=[];
	sign_params.prefix=$('#ajaxForm #urlPrefix').val();
	oFileInput.Init("ajaxForm","displayUpInfo",2,1,"doc_file",null,sign_params);
	btnBind();
	init();
	moveOption();
	addOption();
	//所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
});