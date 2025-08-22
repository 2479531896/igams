var fl_addkzzd="";
var lb_addkzzd="";
var t_map=[];
var ck_map=[];
var xx_map=[];
var xz_map=[];
var fwbjFlag = $("#ajaxForm #fwbj").val()=='/ws';
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
	
	//判断是否隐藏权限信息
	if($("#auditAjaxForm #lastStep").val()){
		$("#ajaxForm #wj_wcsj").remove();
	}
	if($("#auditAjaxForm #lastStep").val() == "false"){
		$("#ajaxForm #wjqxb").remove();
	}else if($("#ajaxForm #formAction").val() != "mod"&&$("#ajaxForm #formAction").val() != "submit"&&$("#ajaxForm #formAction").val() != "audit"&&$("#ajaxForm #formAction").val() != "advancedmod"){
		$("#ajaxForm #wjqxb").remove();
	}
	//添加日期控件
	laydate.render({
	   elem: '#ajaxForm #sxrq'
	  ,theme: '#2381E9'
	});
	//添加日期控件
	laydate.render({
	   elem: '#ajaxForm #wj_qwwcsj'
	  ,theme: '#2381E9'
	});
}
/**
 * 选择关联信息
 * @returns
 */
function chooseGlxx() {
	var cskz2 = $("#ajaxForm #wjlb").find("option:selected").attr("cskz2");
	if (!cskz2){
		$.error("请先选择文件类别或维护对应基础数据！");
		return;
	}
	var url = $('#ajaxForm #urlPrefix').val()+"/inspectionGoods/inspectionGoods/pagedataChooseDevice?cskz2="+cskz2+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择关联信息', chooseGLConfig);
}
var chooseGLConfig = {
	width : "1200px",
	height : "500px",
	modalName	: "chooseGLModal",
	formName	: "printgrantForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#dh_glsb_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$("#ajaxForm #sbmc").val(sel_row[0].sbmc);
					$("#ajaxForm #sbysid").val(sel_row[0].sbysid);
					var cskz2 = $("#ajaxForm #wjlb").find("option:selected").attr("cskz2");
					if (cskz2=="0"){
						$("#ajaxForm #sbglid").val(sel_row[0].sbjlid);
					}else if (cskz2=="1"){
						$("#ajaxForm #sbglid").val(sel_row[0].sbyzid);
					}else {
						$("#ajaxForm #sbglid").val("")
					}

				}else{
					$.error("请选中一行");
					return false;
				}
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
	var url=$("#ajaxForm #fwbj").val()+"/systemmain/data/ansyGetJcsjList";
	if($("#ajaxForm #fwbj").val()=="/ws"){
		url=$("#ajaxForm #fwbj").val()+"/ws/data/ansyGetJcsjList";
	}
	$.ajax({ 
	    type:'post',  
	    url:$('#ajaxForm #urlPrefix').val() + url,
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
	    	    url:$('#ajaxForm #urlPrefix').val()+"/production/document/pagedataGetExtendField",
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
	var cskz2 = $("#ajaxForm #wjlb").find("option:selected").attr("cskz2");
	if (cskz2=="0"){
		$("#ajaxForm #glmc").text("关联计量")
	}else if (cskz2=="1"){
		$("#ajaxForm #glmc").text("关联验证")
	}else {
		$("#ajaxForm #glmc").text("关联设备")
	}
	$("#ajaxForm #sbmc").val("");
	$("#ajaxForm #sbysid").val("");
	$("#ajaxForm #sbglid").val("");
	$("#ajaxForm #wjlbcskz2").val(cskz2);
	lb_addkzzd=fl_addkzzd;
	$("#ajaxForm #t_kzzd").empty();//清空扩展字段
	var wjlb = $("#ajaxForm #wjlb").val();
	//获取扩展字段
	$.ajax({ 
	    type:'post',  
	    url:$('#ajaxForm #urlPrefix').val()+"/production/document/pagedataGetExtendField",
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
		    				$("#ajaxForm #t_kzzd").append(html);
	    				}
	    			}
	    			lb_addkzzd=lb_addkzzd.substring(1);
	    		}
	    	}
	    }
	});
}

var editGlwj_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		var url="/production/document/pagedataGetGlWj";
		if($("#ajaxForm #fwbj").val()=="/ws"){
			url=$("#ajaxForm #fwbj").val()+url;
		}
		$("#ajaxForm #glwj_list").bootstrapTable({
			url:   $('#ajaxForm #urlPrefix').val()+url,         //请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#ajaxForm #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   // 是否显示分页（*）
			paginationShowPageGo: false,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"wj.lrsj",					// 排序字段
			sortOrder: "desc",                   // 排序方式
			queryParams: oTableInit.queryParams,// 传递参数（*）
			sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
			pageNumber:1,                       // 初始化加载第一页，默认第一页
			pageSize: 15,                       // 每页的记录行数（*）
			pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
			paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
			search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,
			showColumns: false,                  // 是否显示所有的列
			showRefresh: false,                  // 是否显示刷新按钮
			minimumCountColumns: 2,             // 最少允许的列数
			clickToSelect: true,                // 是否启用点击选中行
			// height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "wjid",                     // 每一行的唯一标识，一般为主键列
			showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: fwbjFlag,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns:  [
				{
					title: '序号',
					formatter: function (value, row, index) {
						return index+1;
					},
					titleTooltip:'序号',
					width: '3%',
					align: 'left',
					visible:!fwbjFlag
				}, {
					field: 'wjbh',
					title: '文件编号',
					titleTooltip:'文件编号',
					width: '15%',
					align: 'left',
					visible: true,
					sortable: true
				}, {
					field: 'wjmc',
					title: '文件名称',
					titleTooltip:'文件名称',
					width: '30%',
					align: 'left',
					visible: true,
					formatter:viewwjxxformat
				}, {
					field: 'bbh',
					title: '版本',
					titleTooltip:'版本',
					width: '10%',
					align: 'left',
					visible: true
				}, {
					field: 'sxrq',
					title: '生效日期',
					titleTooltip:'生效日期',
					width: '10%',
					align: 'left',
					visible: true,
					sortable: true
				},{
					field: 'cz',
					title: '操作',
					width: '5%',
					align: 'left',
					formatter:czformat,
					visible: true
				}],
			onLoadSuccess:function(map){
				t_map=map;
				if (t_map){
					$("#ajaxForm #glwj_json").val(JSON.stringify(t_map.rows))
				}else {
					var json = [];
					$("#ajaxForm #glwj_json").val(JSON.stringify(json))
				}
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
			},
		});
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			access_token:$("#ac_tk").val(),
			wjid: $('#ajaxForm #wjid').val()
			// 搜索框使用
			// search:params.search
		};
		return map;
	};
	return oTableInit;
}
/**
 * 附件格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fjformat(value,row,index){
	var html = "";
	html = "<span style='margin-left:5px;' class='btn btn-default' title='查看附件' onclick='uploadShoppingFile(\"" + index + "\",\""+row.zwjid+"\")'>查看附件</span>";
	return html;
}
/**
 * 文件附件
 * @param index
 * @returns
 */
var currentUploadIndex = "";
function uploadShoppingFile(index,zwjid) {
	if(index){
		var url=$('#ajaxForm #urlPrefix').val()+"/ws/production/viewFj?ywid="+zwjid;
		currentUploadIndex = index;
	}
	$.showDialog(url, '附件内容', uploadFileConfig);
}
var uploadFileConfig = {
	width : "800px",
	height : "500px",
	modalName	: "uploadFileModal",
	formName	: "ajaxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
	var html="";
	html = "<span style='margin-left:5px;' class='btn btn-danger' title='删除' onclick=\"deleteDetail('" + index + "')\" >删除</span>";
	if (fwbjFlag){
		html += "<span style='margin-left:5px;' class='btn btn-default' title='查看附件' onclick='uploadShoppingFile(\"" + index + "\",\""+row.zwjid+"\")'>查看附件</span>";

	}
	return html;
}

/**
 * 文件查看
 * @param value
 * @param row
 * @param index
 * @returns
 */
function viewwjxxformat(value,row,index){
	var html = "";
	if($('#ac_tk').length > 0){
		html += "<a href='javascript:void(0);' onclick=\"viewwjxx('"+row.wjid+"')\">"+row.wjmc+"</a>"
	}else{
		html += row.wjmc;
	}
	return html;
}
function viewwjxx(wjid){
	var url=$("#ajaxForm #urlPrefix").val()+"/production/document/viewDocument?wjid="+wjid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'文件查看',viewWjxxConfig);
}
var viewWjxxConfig = {
	width		: "1000px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function deleteDetail(index){
	t_map.rows.splice(index,1);
	$("#ajaxForm #glwj_list").bootstrapTable('load',t_map);
	$("#ajaxForm #glwj_json").val(JSON.stringify(t_map.rows));
}

function addGlwj() {
	var url = $("#ajaxForm #urlPrefix").val()+"/production/document/pagedataListDocument?nwjid="+$("#ajaxForm #nwjid").val()+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择文件', chooseWjConfig);
}
var chooseWjConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseWjModal",
	formName	: "chooseDocumentForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#chooseDocumentForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var sel = $("#chooseDocumentForm #yxwj").tagsinput('items');
				var json = JSON.parse($("#ajaxForm #glwj_json").val());
				var ids="";
				for(var i = 0; i < sel.length; i++){
					var value = sel[i].value;
					ids+=","+value;
				}
				//如果有选中数据
				if(ids){
					ids=ids.substring(1);
					//先将原表数据中未选中的数据删除
					for (var i = t_map.rows.length-1; i >=0; i--) {
						if(t_map.rows[i].zwjid){
							if(ids.indexOf(t_map.rows[i].zwjid)==-1){
								t_map.rows.splice(i,1);
							}
						}
					}
					$.ajax({
						async:false,
						url: $('#ajaxForm #urlPrefix').val()+'/production/document/pagedataGetDocumentDetails',
						type: 'post',
						dataType: 'json',
						data : {"ids":ids, "access_token":$("#ac_tk").val()},
						success: function(data) {
							if(data.wjglDtos.length>0){
								for (var i = 0; i < data.wjglDtos.length; i++) {
									var isFind=false;
									for (var j = 0; j < t_map.rows.length; j++) {
										if(data.wjglDtos[i].wjid==t_map.rows[j].zwjid){
											isFind=true;
											break;
										}
									}
									if(!isFind){
										var sz = {"wjid":'',"zwjid":'',"wjbh":'',"wjmc":'',"bbh":'',"sxrq":'',"xh":json.length};
										sz.zwjid = data.wjglDtos[i].wjid;
										sz.wjbh = data.wjglDtos[i].wjbh;
										sz.wjmc = data.wjglDtos[i].wjmc;
										sz.bbh = data.wjglDtos[i].bbh;
										sz.sxrq = data.wjglDtos[i].sxrq;
										sz.wjid = data.wjglDtos[i].wjid;
										t_map.rows.push(sz);
										json.push(sz);
									}
								}
							}
						}
					});
				}else{//没有选中数据则将表中的清除
					for (var i = t_map.rows.length-1; i >=0; i--) {
						if(t_map.rows[i].zwjid){
							t_map.rows.splice(i,1);
						}
					}
				}
				$("#ajaxForm #glwj_list").bootstrapTable('load',t_map);
				$("#ajaxForm #glwj_json").val(JSON.stringify(json));
				$.closeModal(opts.modalName);
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
//查看文件
var editCkwj_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#ajaxForm #ckwj_list").bootstrapTable({
			url:   $('#ajaxForm #urlPrefix').val()+'/ws/production/document/pagedataGetWjqx?qxlx=VIEW',         //请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#ajaxForm #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   // 是否显示分页（*）
			paginationShowPageGo: false,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"wj.lrsj",					// 排序字段
			sortOrder: "desc",                   // 排序方式
			queryParams: oTableInit.queryParams,// 传递参数（*）
			sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
			pageNumber:1,                       // 初始化加载第一页，默认第一页
			pageSize: 15,                       // 每页的记录行数（*）
			pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
			paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
			search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,
			showColumns: false,                  // 是否显示所有的列
			showRefresh: false,                  // 是否显示刷新按钮
			minimumCountColumns: 2,             // 最少允许的列数
			clickToSelect: true,                // 是否启用点击选中行
			// height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "wjid",                     // 每一行的唯一标识，一般为主键列
			showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns:  [
				{
					title: '序号',
					formatter: function (value, row, index) {
						return index+1;
					},
					titleTooltip:'序号',
					width: '3%',
					align: 'left',
					visible:true
				}, {
					field: 'jsmc',
					title: '角色名称',
					titleTooltip:'角色名称',
					width: '15%',
					align: 'left',
					visible: true,
					sortable: true
				},{
					field: 'cz',
					title: '操作',
					width: '5%',
					align: 'left',
					formatter:ckczformat,
					visible: true
				}],
			onLoadSuccess:function(map){
				ck_map=map;
				if (ck_map){
					var jsids = "";
					for (var i = 0; i < ck_map.rows.length; i++) {
						jsids = jsids + "," + ck_map.rows[i].jsid;
					}
					if (jsids.length>0){
						jsids = jsids.substring(1)
					}
					$("#ajaxForm #jsids").val(jsids)
				}else {
					$("#ajaxForm #jsids").val("")
				}
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
			},
		});
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			access_token:$("#ac_tk").val(),
			wjid: $('#ajaxForm #wjid').val()
			// 搜索框使用
			// search:params.search
		};
		return map;
	};
	return oTableInit;
}
//学习文件
var editXxwj_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#ajaxForm #xxwj_list").bootstrapTable({
			url:   $('#ajaxForm #urlPrefix').val()+'/ws/production/document/pagedataGetWjqx?qxlx=STUDY',         //请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#ajaxForm #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   // 是否显示分页（*）
			paginationShowPageGo: false,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"wj.lrsj",					// 排序字段
			sortOrder: "desc",                   // 排序方式
			queryParams: oTableInit.queryParams,// 传递参数（*）
			sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
			pageNumber:1,                       // 初始化加载第一页，默认第一页
			pageSize: 15,                       // 每页的记录行数（*）
			pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
			paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
			search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,
			showColumns: false,                  // 是否显示所有的列
			showRefresh: false,                  // 是否显示刷新按钮
			minimumCountColumns: 2,             // 最少允许的列数
			clickToSelect: true,                // 是否启用点击选中行
			// height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "wjid",                     // 每一行的唯一标识，一般为主键列
			showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns:  [
				{
					title: '序号',
					formatter: function (value, row, index) {
						return index+1;
					},
					titleTooltip:'序号',
					width: '3%',
					align: 'left',
					visible:true
				}, {
					field: 'jsmc',
					title: '角色名称',
					titleTooltip:'角色名称',
					width: '15%',
					align: 'left',
					visible: true,
					sortable: true
				},{
					field: 'cz',
					title: '操作',
					width: '5%',
					align: 'left',
					formatter:xxczformat,
					visible: true
				}],
			onLoadSuccess:function(map){
				xx_map=map;
				if (xx_map){
					var t_jsids = "";
					for (var i = 0; i < xx_map.rows.length; i++) {
						t_jsids = t_jsids + "," + xx_map.rows[i].jsid;
					}
					if (t_jsids.length>0){
						t_jsids = t_jsids.substring(1)
					}
					$("#ajaxForm #t_jsids").val(t_jsids)
				}else {
					$("#ajaxForm #t_jsids").val("")
				}
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
			},
		});
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			access_token:$("#ac_tk").val(),
			wjid: $('#ajaxForm #wjid').val()
			// 搜索框使用
			// search:params.search
		};
		return map;
	};
	return oTableInit;
}
//下载文件
var editXzwj_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#ajaxForm #xzwj_list").bootstrapTable({
			url:   $('#ajaxForm #urlPrefix').val()+'/ws/production/document/pagedataGetWjqx?qxlx=DOWNLOAD',         //请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#ajaxForm #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   // 是否显示分页（*）
			paginationShowPageGo: false,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"wj.lrsj",					// 排序字段
			sortOrder: "desc",                   // 排序方式
			queryParams: oTableInit.queryParams,// 传递参数（*）
			sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
			pageNumber:1,                       // 初始化加载第一页，默认第一页
			pageSize: 15,                       // 每页的记录行数（*）
			pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
			paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
			search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,
			showColumns: false,                  // 是否显示所有的列
			showRefresh: false,                  // 是否显示刷新按钮
			minimumCountColumns: 2,             // 最少允许的列数
			clickToSelect: true,                // 是否启用点击选中行
			// height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "wjid",                     // 每一行的唯一标识，一般为主键列
			showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns:  [
				{
					title: '序号',
					formatter: function (value, row, index) {
						return index+1;
					},
					titleTooltip:'序号',
					width: '3%',
					align: 'left',
					visible:true
				}, {
					field: 'jsmc',
					title: '角色名称',
					titleTooltip:'角色名称',
					width: '15%',
					align: 'left',
					visible: true,
					sortable: true
				},{
					field: 'cz',
					title: '操作',
					width: '5%',
					align: 'left',
					formatter:xzczformat,
					visible: true
				}],
			onLoadSuccess:function(map){
				xz_map=map;
				if (xz_map){
					var d_jsids = "";
					for (var i = 0; i < xz_map.rows.length; i++) {
						d_jsids = d_jsids + "," + xz_map.rows[i].jsid;
					}
					if (d_jsids.length>0){
						d_jsids = d_jsids.substring(1)
					}
					$("#ajaxForm #d_jsids").val(d_jsids)
				}else {
					$("#ajaxForm #d_jsids").val("")
				}
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
			},
		});
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			access_token:$("#ac_tk").val(),
			wjid: $('#ajaxForm #wjid').val()
			// 搜索框使用
			// search:params.search
		};
		return map;
	};
	return oTableInit;
}
/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function ckczformat(value,row,index){
	var html="";
	html = "<span style='margin-left:5px;' class='btn btn-danger' title='删除' onclick=\"delCkjs('" + index + "')\" >删除</span>";
	return html;
}
/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function xxczformat(value,row,index){
	var html="";
	html = "<span style='margin-left:5px;' class='btn btn-danger' title='删除' onclick=\"delXxjs('" + index + "')\" >删除</span>";
	return html;
}
/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function xzczformat(value,row,index){
	var html="";
	html = "<span style='margin-left:5px;' class='btn btn-danger' title='删除' onclick=\"delXzjs('" + index + "')\" >删除</span>";
	return html;
}
/**
 * 删除操作(查看角色)
 * @param index
 * @param event
 * @returns
 */
function delCkjs(index,event){
	ck_map.rows.splice(index,1);
	var jsids = "";
	for (var i = 0; i < ck_map.rows.length; i++) {
		jsids = jsids + "," + ck_map.rows[i].jsid;
	}
	if (jsids.length>0){
		jsids = jsids.substring(1)
	}
	$("#ajaxForm #jsids").val(jsids)
	$("#ajaxForm #ckwj_list").bootstrapTable('load',ck_map);
}
/**
 * 删除操作(学习角色)
 * @param index
 * @param event
 * @returns
 */
function delXxjs(index,event){
	xx_map.rows.splice(index,1);
	var jsids = "";
	for (var i = 0; i < xx_map.rows.length; i++) {
		jsids = jsids + "," + xx_map.rows[i].jsid;
	}
	if (jsids.length>0){
		jsids = jsids.substring(1)
	}
	$("#ajaxForm #t_jsids").val(jsids)
	$("#ajaxForm #xxwj_list").bootstrapTable('load',xx_map);
}
/**
 * 删除操作(下载角色)
 * @param index
 * @param event
 * @returns
 */
function delXzjs(index,event){
	xz_map.rows.splice(index,1);
	var jsids = "";
	for (var i = 0; i < xz_map.rows.length; i++) {
		jsids = jsids + "," + xz_map.rows[i].jsid;
	}
	if (jsids.length>0){
		jsids = jsids.substring(1)
	}
	$("#ajaxForm #d_jsids").val(jsids)
	$("#ajaxForm #xzwj_list").bootstrapTable('load',xz_map);
}
function addJs(qxlx) {
	var njsids = "";
	if (qxlx=='VIEW'){
		njsids = $("#ajaxForm #jsids").val();
	}else if (qxlx=='STUDY'){
		njsids = $("#ajaxForm #t_jsids").val();
	}else if (qxlx=='DOWNLOAD'){
		njsids = $("#ajaxForm #d_jsids").val();
	}
	var url = $("#ajaxForm #urlPrefix").val()+"/ws/production/role/pagedataListRole?njsids="+njsids+"&qxlx="+qxlx;
	$.showDialog(url, '选择角色', chooseJsConfig);
}
var chooseJsConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseJsModal",
	formName	: "chooseRoleForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#chooseRoleForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var sel = $("#chooseRoleForm #yxjs").tagsinput('items');
				var szjsids = [];
				var qxlx = $("#chooseRoleForm #qxlx").val();
				if (qxlx=='VIEW'){
					szjsids = $("#ajaxForm #jsids").val().split(",");
				}else if (qxlx=='STUDY'){
					szjsids = $("#ajaxForm #t_jsids").val().split(",");
				}else if (qxlx=='DOWNLOAD'){
					szjsids = $("#ajaxForm #d_jsids").val().split(",");
				}
				for(var i = 0; i < sel.length; i++){
					var jsid = sel[i].value;
					var jsmc = sel[i].text;
					var sz = {"jsid":'',"jsmc":''};
					sz.jsid = jsid;
					sz.jsmc = jsmc;
					if (qxlx=='VIEW'){
						ck_map.rows.push(sz);
					}else if (qxlx=='STUDY'){
						xx_map.rows.push(sz);
					}else if (qxlx=='DOWNLOAD'){
						xz_map.rows.push(sz);
					}
					szjsids.push(jsid)
				}
				if (qxlx=='VIEW'){
					$("#ajaxForm #jsids").val(szjsids.join());
					$("#ajaxForm #ckwj_list").bootstrapTable('load',ck_map);
				}else if (qxlx=='STUDY'){
					$("#ajaxForm #t_jsids").val(szjsids.join());
					$("#ajaxForm #xxwj_list").bootstrapTable('load',xx_map);
				}else if (qxlx=='DOWNLOAD'){
					$("#ajaxForm #d_jsids").val(szjsids.join());
					$("#ajaxForm #xzwj_list").bootstrapTable('load',xz_map);
				}
				$.closeModal(opts.modalName);
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
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
	var d_jsids = "";
	for (var i = 0; i < $("#ajaxForm #d_sel_list optgroup option").length; i++) {//循环读取已选角色
		d_jsids = d_jsids + ","+ $("#ajaxForm #d_sel_list optgroup")[0].children[i].value;
	}
	d_jsids = d_jsids.substr(1);
	$("#ajaxForm #d_jsids").val(d_jsids);
	// 判断状态是否必填
	if($("#ajaxForm #scbjFlag").val() == "2"){
		$("#ajaxForm #scbjSpan").hide();
		$("#ajaxForm input[name='scbj']").removeAttr("validate");
	}
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
			if(document.forms[0].action.indexOf("/ws/")!= -1){
				var wjm = $("#ajaxForm #audit_wjm").val();
				var sign = $("#ajaxForm #audit_sign").val();
				$.showDialog($('#ajaxForm #urlPrefix').val() + "/common/view/viewerById?fjid=" + fjid + "&jmzd=" + wjm + "&sign=" + sign,'文件预览',viewPreModConfig);
			}else{
				var url= $('#ajaxForm #urlPrefix').val() + "/common/file/pdfPreview?fjid=" + fjid;
	            $.showDialog(url,'文件预览',viewPreModConfig);
			}
		},
		viewdoc(fjid){
            if(document.forms[0].action.indexOf("/ws/")!= -1){
				var wjm = $("#ajaxForm #audit_wjm").val();
				var sign = $("#ajaxForm #audit_wssign").val();
				$.showDialog($('#ajaxForm #urlPrefix').val() + "/common/view/viewerDocById?fjid=" + fjid + "&sign=" + sign,'文件预览',viewPreModConfig);
			}else{
				var url= $('#ajaxForm #urlPrefix').val() + "/common/file/pdfPreview?fjid=" + fjid + "&pdf_flg="+"1";
	            $.showDialog(url,'文件预览',viewPreModConfig);
			}
		},
		xz(fjid){
		    jQuery('<form action="'+$('#ajaxForm #urlPrefix').val() +'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
	                '<input type="text" name="fjid" value="'+fjid+'"/>' +
	                '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
	            '</form>')
	        .appendTo('body').submit().remove();
		},
		del(fjid,wjlj){
			$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= $('#ajaxForm #urlPrefix').val()+"/common/file/delFile";
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

function chooseBm() {
	var url = "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	if($("#fwbj").val()=='/ws'){
		url= "/ws/production/department/Departmentlist?fwbj="+$("#fwbj").val();
	}
	url = $('#ajaxForm #urlPrefix').val()+ url;
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
//打开文件上传界面
function editfile(divName,btnName){
	$("#ajaxForm"+"  #"+btnName).hide();
	$("#ajaxForm"+"  #"+divName).show();
}
//关闭文件上传界面
function cancelfile(divName,btnName){
	$("#ajaxForm"+"  #"+btnName).show();
	$("#ajaxForm"+"  #"+divName).hide();
}
$(document).ready(function(){
	//0.初始化fileinput
	var oFileInput = new FileInput();
	var sign_params=[];
	sign_params.prefix=$("#ajaxForm #urlPrefix").val();
	oFileInput.Init("ajaxForm","displayUpInfo",2,1,"doc_file",null,sign_params);
	btnBind();
	init();
	moveOption();
	addOption();
	var glwjOTable=new editGlwj_TableInit();
	glwjOTable.Init();
	if (fwbjFlag){
		var ckwjOTable=new editCkwj_TableInit();
		ckwjOTable.Init();
		var xxwjOTable=new editXxwj_TableInit();
		xxwjOTable.Init();
		var xzwjOTable=new editXzwj_TableInit();
		xzwjOTable.Init();
	}
	//所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
});