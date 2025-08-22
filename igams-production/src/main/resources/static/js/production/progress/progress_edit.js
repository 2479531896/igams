
/**
 * 选择业务员
 * @returns
 */
function chooseSqr() {
	var url = $('#progress_edit_ajaxForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择业务员', choosesqrConfig);
}
var choosesqrConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseSqrModal",
	formName	: "allocationForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#taskListFzrForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#progress_edit_ajaxForm #sqr').val(sel_row[0].yhid);
					$('#progress_edit_ajaxForm #sqrmc').val(sel_row[0].zsxm);
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
 * 刷新需求单号
 * @returns
 */
function refreshXqdh(){
	$.ajax({ 
	    type:'post',  
	    url:$('#progress_edit_ajaxForm #urlPrefix').val() + "/progress/progress/pagedataRefreshNumber",
	    cache: false,
	    data: {"access_token":$("#ac_tk").val()},
	    dataType:'json',
	    success:function(result){
	    	$("#progress_edit_ajaxForm #xqdh").val(result.xqdh);
	    }
	});
}

/**
 * 选择申请部门
 * @returns
 */
function chooseSqbm(){
	var url=$('#progress_edit_ajaxForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择销售部门',addSqbmConfig);
}
var addSqbmConfig = {
	width		: "800px",
	modalName	:"addSqbmModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#progress_edit_ajaxForm #sqbm').val(sel_row[0].jgid);
					$('#progress_edit_ajaxForm #sqbmmc').val(sel_row[0].jgmc);
				}else{
					$.error("请选中一行");
					return false;
				}
			},
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};


var t_map = [];
var t_index="";
// 显示字段
var columnsArray = [
	{
	// 	checkbox: true,
	// 	width: '2%'
	// },{
		title: '序号',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序号',
		width: '3%',
		align: 'left',
		visible:true
	},{
		field: 'xqjhmxid',
		title: '需求计划明细id',
		width: '6%',
		align: 'left',
		visible: false
	}, {
		field: 'wlbm',
		title: '物料编码',
		width: '6%',
		align: 'left',
		visible: true
	},  {
		field: 'wlmc',
		title: '物料名称',
		width: '4%',
		align: 'left',
		visible: true
	},  {
		field: 'scs',
		title: '生产商',
		width: '4%',
		align: 'left',
		visible: true
	}, {
		field: 'gg',
		title: '规格',
		width: '4%',
		align: 'left',
		visible: true
	},{
		field: 'jldw',
		title: '单位',
		width: '4%',
		align: 'left',
		visible: true
	},{
		field: 'sl',
		title: '数量',
		width: '6%',
		align: 'left',
		formatter: progressForm_slformat,
		visible: true,
	},{
        field: 'kcl',
        title: '库存量',
        width: '6%',
        align: 'left',
        visible: true,
    },{
		field: 'yq',
		title: '批次和批量要求',
		width: '6%',
		align: 'left',
		formatter: progressForm_yqformat,
		visible: true,
	}, {
		field: 'cz',
		title: '操作',
		width: '6%',
		align: 'left',
		formatter:progressAjaxForm_czformat,
		visible: true
	}];
var progress_TableInit = function () {
	var oTableInit = new Object();
	//初始化Table
	oTableInit.Init = function () {
		$('#progress_edit_ajaxForm #progress_edit_tb_list').bootstrapTable({
			url: $('#progress_edit_ajaxForm #urlPrefix').val()+$('#progress_edit_ajaxForm #url').val(),         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#progress_edit_ajaxForm #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   //是否显示分页（*）
			sortable: true,                     //是否启用排序
			sortName: "xqjhmx.lrsj",					//排序字段
			sortOrder: "asc",                   //排序方式
			queryParams: oTableInit.queryParams,//传递参数（*）
			sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
			pageNumber:1,                       //初始化加载第一页，默认第一页
			pageSize: 15,                       //每页的记录行数（*）
			pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
			paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
			search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,
			isForceTable: true,
			showColumns: false,                  //是否显示所有的列
			showRefresh: false,                  //是否显示刷新按钮
			minimumCountColumns: 2,             //最少允许的列数
			clickToSelect: false,                //是否启用点击选中行
			//height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "xqjhmx.xqjhmxid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: columnsArray,
			onLoadSuccess: function (map) {
				t_map = map;
			},
			onDblClickRow: function (row, $element) {
				return;
			},
		});
	};

	//得到查询的参数
	oTableInit.queryParams = function(params){
		//请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
		//例如 toolbar 中的参数 
		//如果 queryParamsType = ‘limit’ ,返回参数必须包含 
		//limit, offset, search, sort, order 
		//否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder. 
		//返回false将会终止请求
		var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			pageSize: 15,   //页面大小
			pageNumber: 1,  //页码
			access_token:$("#ac_tk").val(),
			sortName: params.sort,      // 排序列名
			sortOrder: params.order, // 排位命令（desc，asc）
			sortLastName: "xqjhmx.xqjhmxid", // 防止同名排位用
			sortLastOrder: "asc", // 防止同名排位用
			xqjhid: $("#progress_edit_ajaxForm #xqjhid").val(), // 防止同名排位用
		};
		return map;
	};
	return oTableInit;
};

function checkTableValue(index, e, zdmc){
	if(zdmc=="sl"){
		t_map.rows[index].sl = e.value;
	}
	if(zdmc=="yq"){
		t_map.rows[index].yq = e.value;
	}
}
/**
 * 要求格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function progressForm_yqformat(value,row,index){
	if(row.yq == null){
		row.yq = "";
	}
	var html = "<input id='yq_"+index+"' autocomplete='off' value='"+row.yq+"' name='yq_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onchange=\"checkTableValue('"+index+"',this,\'yq\')\"></input>";
	return html;
}

/**
 *数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function progressForm_slformat(value,row,index){
	let sl = 0;
	if (row.sl == null){
		sl = ""
	}else {
		sl = row.sl
	}
	var html="";
	html ="<div id='sldiv_"+index+"' name='sldiv' style='width: 100%'>";
	html += "<input id='sl_"+index+"' autocomplete='off' value='"+sl+"' validate = '{slNumber:true}' name='sl_"+index+"' style='border: 1px solid #ccc;border-radius: 4px;width: 100%' ";
	html += "onblur=\"checkTableValue('"+index+"',this,\'sl\')\" ";

	html += "></input>";
	html += "</div>";
	return html;
}
/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("slNumber", function (value, element){
	if(!value){
		$("#progress_edit_ajaxForm #"+element.id).val("")
		return false;
	}else{
		if(!/^\d+(\.\d{1,2})?$/.test(value)){
			$.error("请填写正确数量格式，可保留两位小数!");
			$("#progress_edit_ajaxForm #"+element.id).val("")
		}
	}
	return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");
/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delWlInfo(index,event){
	t_map.rows.splice(index,1);
	$("#progress_edit_ajaxForm #progress_edit_tb_list").bootstrapTable('load',t_map);
}

/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function progressAjaxForm_czformat(value,row,index){
	var html="";
	html = html+"<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除物料信息' onclick=\"delWlInfo('" + index +  "',event)\" >删除</span>";
	return html;
}
$(document).ready(function(){
	//添加日期控件
	laydate.render({
		elem: '#progress_edit_ajaxForm #xqrq'
		,theme: '#2381E9'
	});
	//初始化列表
	var oTable=new progress_TableInit();
	oTable.Init();
	//0.初始化fileinput
	var oFileInput = new FileInput();
	var picking_params=[];
	picking_params.prefix=$("#progress_edit_ajaxForm #urlPrefix").val();
	oFileInput.Init("progress_edit_ajaxForm","displayUpInfo",2,1,"pro_file",null,picking_params);
	jQuery('#progress_edit_ajaxForm .chosen-select').chosen({width: '100%'});
});
/**
 * 根据物料名称模糊查询
 */
$('#progress_edit_ajaxForm #addnr').typeahead({
	source : function(query, process) {
		return $.ajax({
			url : $('#progress_edit_ajaxForm #urlPrefix').val()+'/progress/progress/pagedataQueryWlxx',
			type : 'post',
			data : {
				"entire" : query,
				"access_token" : $("#ac_tk").val()
			},
			dataType : 'json',
			success : function(result) {
				var resultList = result.dtoList
					.map(function(item) {
						var aItem = {
							id : item.wlid,
							name : item.wlbm+"-"+item.wlmc+"-"+item.scs+"-"+item.gg
						};
						return JSON.stringify(aItem);
					});
				return process(resultList);
			}
		});
	},
	matcher : function(obj) {
		var item = JSON.parse(obj);
		return ~item.name.toLowerCase().indexOf(
			this.query.toLowerCase())
	},
	sorter : function(items) {
		var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
		while (aItem = items.shift()) {
			var item = JSON.parse(aItem);
			if (!item.name.toLowerCase().indexOf(
				this.query.toLowerCase()))
				beginswith.push(JSON.stringify(item));
			else if (~item.name.indexOf(this.query))
				caseSensitive.push(JSON.stringify(item));
			else
				caseInsensitive.push(JSON.stringify(item));
		}
		return beginswith.concat(caseSensitive,
			caseInsensitive)
	},
	highlighter : function(obj) {
		var item = JSON.parse(obj);
		var query = this.query.replace(
			/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
		return item.name.replace(new RegExp('(' + query
			+ ')', 'ig'), function($1, match) {
			return '<strong>' + match + '</strong>'
		})
	},
	updater : function(obj) {
		var item = JSON.parse(obj);
		$('#progress_edit_ajaxForm #addwlid').attr('value', item.id);
		return item.name;
	}
});

/**
 * 回车键添加物料至请购车
 * @param event
 * @returns
 */
var addkeydown = $("#addnr").bind("keydown",function(event){
	var wlid=$('#progress_edit_ajaxForm #addwlid').val();
	if (event.keyCode == "13") {
		//判断新增输入框是否有内容
		if(!$('#progress_edit_ajaxForm #addnr').val()){
			return false;
		}
		setTimeout(function(){ if(wlid != null && wlid != ''){
			$.ajax({
				type:'post',
				url:$('#progress_edit_ajaxForm #urlPrefix').val()+"/progress/progress/pagedataQueryWlxx",
				cache: false,
				data: {"wlid":wlid,"access_token":$("#ac_tk").val()},
				dataType:'json',
				success:function(data){
					//返回值
					if(data.dtoList!=null && data.dtoList.length>0){

						if(t_map.rows==null || t_map.rows ==""){
							t_map.rows=[];
						}
						t_map.rows.push(data.dtoList[0]);
						$("#progress_edit_ajaxForm #progress_edit_tb_list").bootstrapTable('load',t_map);
						$('#progress_edit_ajaxForm #addwlid').val("");
						$('#progress_edit_ajaxForm #addnr').val("");

					}else{
						$.confirm("该物料不存在!");
					}
				}
			});
		}else{
			var addnr = $('#progress_edit_ajaxForm #addnr').val();
			if(addnr != null && addnr != ''){
				$.confirm("请选择物料信息!");
			}
		}}, '200')
	}
})


/**
 * 点击确定添加物料
 * @param
 * @param
 * @returns
 */
function punchCofirmaddwl(){
	var wlid=$('#progress_edit_ajaxForm #addwlid').val();
	//判断新增输入框是否有内容
	if(!$('#progress_edit_ajaxForm #addnr').val()){
		return false;
	}
	setTimeout(function(){ if(wlid != null && wlid != ''){
		$.ajax({
			type:'post',
			url:$('#progress_edit_ajaxForm #urlPrefix').val()+"/progress/progress/pagedataQueryWlxx",
			cache: false,
			data: {"wlid":wlid,"access_token":$("#ac_tk").val()},
			dataType:'json',
			success:function(data){
				//返回值
				if(data.dtoList!=null && data.dtoList.length>0){
					if(t_map.rows==null || t_map.rows ==""){
						t_map.rows=[];
					}
					t_map.rows.push(data.dtoList[0]);
					$("#progress_edit_ajaxForm #progress_edit_tb_list").bootstrapTable('load',t_map);
					$('#progress_edit_ajaxForm #addwlid').val("");
					$('#progress_edit_ajaxForm #addnr').val("");
				}else{
					$.confirm("该物料不存在!");
				}
			}
		});
	}else{
		var addnr = $('#progress_edit_ajaxForm #addnr').val();
		if(addnr != null && addnr != ''){
			$.confirm("请选择物料信息!");
		}
	}}, '200')
}


function choosewlxx() {
    var url = $("#progress_edit_ajaxForm #urlPrefix").val()+"/progress/progress/setmaterielProgress?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择物料', chooseWlxxConfig);
}

var chooseWlxxConfig = {
	width		: "1200px",
	modalName	:"chooseWlxxModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#setmateriel_formSearch #view_list').bootstrapTable('getSelections');//获取选择行数据

				if(sel_row.length>0){
				    if(t_map.rows!=null&&t_map.rows.length>0){
                        for(var j=0;j<t_map.rows.length;j++){
                            for (var i = 0; i < sel_row.length; i++) {
                                if(sel_row[i].wlid==t_map.rows[j].wlid){
                                    sel_row.splice(i,1);
                                    break;
                                }
                            }
                        }
				    }

                    for (var i = 0; i < sel_row.length; i++) {
                        var sz = {"sczlmxid":'',"wlbm":'',"wlmc":'',"sl":'',"wlid":'',"sl":'',"yq":'',"gg":'',"scs":'',"jldw":'',"kcl":''};
                        sz.wlbm = sel_row[i].wlbm;
                        sz.wlmc = sel_row[i].wlmc;
                        sz.wlid = sel_row[i].wlid;
                        sz.gg = sel_row[i].gg;
                        sz.scs = sel_row[i].scs;
                        sz.jldw = sel_row[i].jldw;
                        sz.sl = sel_row[i].cksl;
                        sz.kcl = sel_row[i].kcl;
                        t_map.rows.push(sz);
                        $("#progress_edit_ajaxForm #progress_edit_tb_list").bootstrapTable('load',t_map);
                    }
				}else{
					$.error("请选中一行");
					return false;
				}
			},
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};




/**
 * 点击显示文件上传
 * @returns
 */
function editfile(){
	$("#fileDiv").show();
	$("#file_btn").hide();
}
/**
 * 点击隐藏文件上传
 * @returns
 */
function cancelfile(){
	$("#fileDiv").hide();
	$("#file_btn").show();
}

/**
 * 点击图片预览
 * @param fjid
 * @param wjm
 * @returns
 */
function view(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url=$("#progress_edit_ajaxForm #urlPrefix").val()+"/ws/sjxxpripreview/?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= $("#progress_edit_ajaxForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
		$.showDialog(url,'文件预览',JPGMaterConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}
var JPGMaterConfig = {
	width		: "800px",
	offAtOnce	: true,
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

/**
 * 点击文件下载
 * @param fjid
 * @returns
 */
function xz(fjid){
	jQuery('<form action="'+$('#progress_edit_ajaxForm #urlPrefix').val() + '/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
		'<input type="text" name="fjid" value="'+fjid+'"/>' +
		'<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
		'</form>')
		.appendTo('body').submit().remove();
}
/**
 * 点击删除文件
 * @param fjid
 * @param wjlj
 * @returns
 */
function del(fjid,wjlj){
	$.confirm('您确定要删除所选择的记录吗？',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			var url= $("#progress_edit_ajaxForm #urlPrefix").val()+"/common/file/delFile";
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
/**
 * 附件上传回调
 * @param fjid
 * @returns
 */
function displayUpInfo(fjid){
	if(!$("#progress_edit_ajaxForm #fjids").val()){
		$("#progress_edit_ajaxForm #fjids").val(fjid);
	}else{
		$("#progress_edit_ajaxForm #fjids").val($("#progress_edit_ajaxForm #fjids").val()+","+fjid);
	}
}