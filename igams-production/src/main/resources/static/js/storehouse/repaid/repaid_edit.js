var t_map = [];
var t_index="";
// 显示字段
var columnsArray = [
	{
		title: '序',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序',
		width: '2%',
		align: 'left',
		visible:true
	}, {
		field: 'hwid',
		title: '货物id',
		titleTooltip:'货物id',
		width: '4%',
		align: 'left',
		visible: false
	},{
        field: 'sbysid',
        title: '设备验收id',
        titleTooltip:'设备验收id',
        width: '4%',
        align: 'left',
        visible: false
    }, {
		field: 'jyxxid',
		title: '借用信息id',
		titleTooltip:'借用信息id',
		width: '4%',
		align: 'left',
		visible: false
	}, {
		field: 'wlbm',
		title: '物料编码',
		titleTooltip:'物料编码',
		width: '4%',
		align: 'left',
		visible: true
	}, {
		field: 'wlmc',
		title: '物料名称',
		titleTooltip:'物料名称',
		width: '15%',
		align: 'left',
		visible: true
	},{
		field: 'gg',
		title: '规格',
		titleTooltip:'规格',
		width: '4%',
		align: 'left',
		visible: true
	}, {
		field: 'jldw',
		title: '单位',
		titleTooltip:'单位',
		width: '3%',
		align: 'left',
		visible: true
	}, {
		field: 'scph',
		title: '生产批号',
		titleTooltip:'生产批号',
		width: '5%',
		align: 'left',
		visible: true
	}, {
		field: 'scrq',
		title: '生产日期',
		titleTooltip:'生产日期',
		width: '5%',
		align: 'left',
		visible: true
	}, {
		field: 'yxq',
		title: '失效日期',
		titleTooltip:'失效日期',
		width: '5%',
		align: 'left',
		visible: true
	}, {
		field: 'zsh',
		title: '追溯号',
		titleTooltip:'追溯号',
		width: '10%',
		align: 'left',
		visible: true
	}, {
		field: 'ckid',
		title: '仓库',
		titleTooltip:'仓库',
		width: '10%',
		align: 'left',
		formatter:ckformat,
		visible: true
	}, {
		field: 'kwbh',
		title: '库位',
		titleTooltip:'库位',
		width: '10%',
		align: 'left',
		formatter:kwformat,
		visible: true
	}, {
		field: 'khsl',
		title: '可还数量',
		titleTooltip:'可还数量',
		width: '5%',
		align: 'left',
		visible: true
	},{
		field: 'ghsl',
		title: '归还数量',
		titleTooltip:'归还数量',
		width: '10%',
		align: 'left',
		formatter:ghslformat,
		visible: true
	},{
		field: 'sfyzgh',
		title: '是否异种归还',
		titleTooltip:'是否异种归还',
		width: '6%',
		align: 'left',
		formatter:sfyzghformat,
		visible: true
	}, {
		field: 'bz',
		title: '备注',
		titleTooltip:'备注',
		width: '10%',
		align: 'left',
		formatter:bzformat,
		visible: true
	},
	{
		field: 'cz',
		title: '操作',
		titleTooltip:'操作',
		width: '4%',
		align: 'left',
		formatter:czformat,
		visible: true
	}];
var repaid_TableInit = function () {
	var oTableInit = new Object();
	//初始化Table
	oTableInit.Init = function () {
		$('#repaidForm #jcghglDto_list').bootstrapTable({
			url: $('#repaidForm #urlPrefix').val()+$('#repaidForm #url').val(),         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#repaidForm #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   //是否显示分页（*）
			sortable: true,                     //是否启用排序
			sortName: "wlmc",					//排序字段
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
			uniqueId: "ckhwid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: columnsArray,
			onLoadSuccess: function (map) {
				t_map = map;
				if(t_map.length>0){
				}
			},
			onLoadError: function () {
				alert("数据加载失败！");
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
			pageSize: 1,   //页面大小
			pageNumber: 1,  //页码
			access_token:$("#ac_tk").val(),
			sortName: params.sort,      //排序列名
			sortOrder: params.order, //排位命令（desc，asc）
			sortLastName: "wlid", //防止同名排位用
			sortLastOrder: "asc", //防止同名排位用
			jcjyid: $("#repaidForm #jcjyid").val(),
			jcghid: $("#repaidForm #jcghid").val()
			//搜索框使用
			//search:params.search
		};
		return map;
	};
	return oTableInit;
};

/**
 * 仓库格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function ckformat(value,row,index){
	var xmdls = $("#repaidForm #cklist").val();
	var cklist = JSON.parse(xmdls);
	var ckid=row.ckid;
	var html="";
	html += "<select id='ckid_"+index+"' name='ckid_"+index+"' class='form-control chosen-select'  onchange=\"checkSum('"+index+"',this,\'ckid\')\">";
	html += "<option value=''>--请选择--</option>";
	for(var i = 0; i < cklist.length; i++) {
		html += "<option id='"+cklist[i].ckid+"' value='"+cklist[i].ckid+"'";
		if(ckid!=null && ckid!=""){
			if(ckid==cklist[i].ckid){
				html += "selected"
			}
		}
		html += ">"+cklist[i].ckmc+"</option>";
	}
	html +="</select>";
	return html;
}

/**
 * 库位格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function kwformat(value,row,index){
	var html="";
	html += "<select id='kwbh_"+index+"' name='kwbh_"+index+"' class='form-control chosen-select'  onchange=\"checkSum('"+index+"',this,\'kwbh\')\">";
	html +="</select>";
	var ckid ="";
	if(row.ckid){
		ckid = row.ckid;
		var url="/storehouse/arrivalGoods/pagedataGetkwlist";
		url = $('#repaidForm #urlPrefix').val()+url;
		$.ajax({
			type:'post',
			url:url,
			cache: false,
			data: {"ckid":ckid,"access_token":$("#ac_tk").val()},
			dataType:'json',
			success:function(data){
				if(data != null && data.length != 0){

					var zlbHtml = "";
					zlbHtml += "<option value=''>--请选择--</option>";
					for(var i = 0; i < data.kwlist.length; i++) {
						zlbHtml += "<option id='" + data.kwlist[i].ckid + "' value='" + data.kwlist[i].ckid + "'";
						if (row.kwbh != null && row.kwbh != "") {
							if (row.kwbh == data.kwlist[i].ckid) {
								zlbHtml += "selected"
							}
						}
						zlbHtml += ">" + data.kwlist[i].ckmc + "</option>";
					}
					$("#repaidForm #kwbh_"+index).empty();
					$("#repaidForm #kwbh_"+index).append(zlbHtml);
					$("#repaidForm #kwbh_"+index).trigger("chosen:updated");
				}else{
					var zlbHtml = "";
					zlbHtml += "<option value=''>--请选择--</option>";
					$("#inStoreForm #kwbh_"+index).empty();
					$("#inStoreForm #kwbh_"+index).append(zlbHtml);
					$("#inStoreForm #kwbh_"+index).trigger("chosen:updated");
				}
			}
		});
	}else{
		var zlbHtml = "";
		zlbHtml += "<option value=''>--请选择--</option>";
		$("#inStoreForm #kwbh_"+index).empty();
		$("#inStoreForm #kwbh_"+index).append(zlbHtml);
		$("#inStoreForm #kwbh_"+index).trigger("chosen:updated");
	}
	return html;
}

/**
 * 异种归还格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function sfyzghformat(value,row,index){
	var html="";
	html +=  "<select id='sfyzgh_"+index+"' name='sfyzgh_"+index+"' class='form-control chosen-select'  onchange=\"checkSum('"+index+"',this,\'sfyzgh\')\">";
	if(row.sfyzgh=='1'){
		html += "<option value=''>--请选择--</option>";
		html += "<option value='1' selected>是</option>";
		html += "<option value='0'>否</option>";
	}else if(row.sfyzgh=='0'){
		html += "<option value=''>--请选择--</option>";
		html += "<option value='1' >是</option>";
		html += "<option value='0' selected>否</option>";
	}else{
		html += "<option value=''>--请选择--</option>";
		html += "<option value='1' >是</option>";
		html += "<option value='0' >否</option>";
	}
	html += "</select>";
	return html;
}
/**
 * 归还数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function ghslformat(value,row,index){
	var khsl="";
	if(row.khsl==null){
		khsl='';
	}else{
		khsl=row.khsl;
	}
	var html="";
	html ="<div id='ghsldiv_"+index+"' name='ghsldiv_"+index+"' isBeyond='false' style='background:darkcyan;'>";
	html += "<input id='ghsl_"+index+"' type='number' autocomplete='off' value='"+(row.ghsl==null?"0":row.ghsl)+"' name='ghsl_"+index+"'  min='0' style='width:55%;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'ghsl\')\"></input>";
	html += "<span id='khsl_"+index+"' style='font-size:13px;width:45%;margin-left:3px;'>/"+khsl+"</span>";
	html += "</div>";
	return html;
}

/**
 * 备注格式化
 */
function bzformat(value,row,index){
	var html="";
	if (!row.bz){
		row.bz="";
	}
	if (row.bz){
		row.bz=row.bz;
	}
	html ="<div id='bzdiv_"+index+"' name='bzdiv'>";
	html += "<input id='bz_"+index+"' value='"+row.bz+"' name='bz_"+index+"' onblur=\"checkSum('"+index+"',this,\'bz\')\"  validate='{stringMaxLength:64}'></input>";
	html += "</div>";
	return html;
}

/**
 * 刷新归还单号
 */

function sxghdh(){
	$.ajax({
		type:'post',
		url: $('#repaidForm #urlPrefix').val()+'/repaid/repaid/pagedataRefreshGhdh',
		cache: false,
		data: {"access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			if(data != null && data.length != 0){
				if (data.ghdh){
					$("#repaidForm #ghdh").val(data.ghdh);
				}
			}
		}
	});
}


/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
	var html = "<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除物料信息' onclick=\"delWlDetail('" + index + "',event)\" >删除</span>";
	return html;
}
/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delWlDetail(index,event){
	t_map.rows.splice(index,1);
	$("#repaidForm #jcghglDto_list").bootstrapTable('load',t_map);
}

/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkSum(index, e, zdmc) {
	//当前物料可请领数
	if (zdmc == "sfyzgh"){
		t_map.rows[index].sfyzgh = e.value;
	}else if(zdmc == "ghsl"){
		t_map.rows[index].ghsl = e.value
	}else if(zdmc == "bz"){
		t_map.rows[index].bz = e.value;
	}else if(zdmc == "ckid"){
		t_map.rows[index].ckid = e.value;
		var url="/storehouse/arrivalGoods/pagedataGetkwlist";
		url = $('#repaidForm #urlPrefix').val()+url;
		$.ajax({
			type:'post',
			url:url,
			cache: false,
			data: {"ckid":e.value,"access_token":$("#ac_tk").val()},
			dataType:'json',
			success:function(data){
				if(data != null && data.length != 0){

					var zlbHtml = "";
					zlbHtml += "<option value=''>--请选择--</option>";
					$.each(data.kwlist,function(i){
						zlbHtml += "<option value='" + data.kwlist[i].ckid + "' id='"+data.kwlist[i].ckid+"' csdm='"+data.kwlist[i].ckdm+"' csmc='"+data.kwlist[i].ckmc+"'>" + data.kwlist[i].ckmc + "</option>";
					});
					$("#repaidForm #kwbh_"+index).empty();
					$("#repaidForm #kwbh_"+index).append(zlbHtml);
					$("#repaidForm #kwbh_"+index).trigger("chosen:updated");
				}else{
					var zlbHtml = "";
					zlbHtml += "<option value=''>--请选择--</option>";
					$("#inStoreForm #kwbh_"+index).empty();
					$("#inStoreForm #kwbh_"+index).append(zlbHtml);
					$("#inStoreForm #kwbh_"+index).trigger("chosen:updated");
				}
			}
		});
	}else if(zdmc == "kwbh"){
		t_map.rows[index].kwbh = e.value;
	}
}




/**
 * 初始化页面
 * @returns
 */
function init(){
	//添加日期控件
	laydate.render({
		elem: '#repaidForm #ghrq'
		,theme: '#2381E9'
	});
}


/**
 * 更改单位类别
 * @returns
 */
function choseDwlx(){
	$('#repaidForm #dwdm').val("");
	$('#repaidForm #dwid').val("");
	$('#repaidForm #dwmc').val("");
}
/**
 * 选择申请部门列表
 * @returns
 */
function choosedw(){
	var dwdm =$('#repaidForm #dwlx').find("option:selected").attr("csdm");
	if(dwdm=='GYS'){
		var url=$('#repaidForm #urlPrefix').val() + "/warehouse/supplier/pagedataSupplier?access_token=" + $("#ac_tk").val();
		$.showDialog(url,'选择供应商',addGysConfig);
	}else if(dwdm=='KH'){
		var url=$('#repaidForm #urlPrefix').val() + "/systemmain/client/pagedataListClient?access_token=" + $("#ac_tk").val();
		$.showDialog(url,'选择客户',addKhConfig);
	}else if(dwdm=='BM'){
		var url = $("#repaidForm #urlPrefix").val()+"/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
		$.showDialog(url, '选择部门', chooseBmConfig);
	}else{
		$.alert("请先选择单位类别！");
		return false;
	}

}

var addGysConfig = {
	width		: "1200px",
	modalName	:"addGysModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row=$('#choosesupplier_formSearch #tb_list').bootstrapTable('getSelections');
				if(sel_row.length == 1){
					var gysid = sel_row[0].gysid;
					var gysmc = sel_row[0].gysmc;
					var gysdm = sel_row[0].gysdm;
					$('#repaidForm #dwdm').val(gysdm);
					$('#repaidForm #dwid').val(gysid);
					$('#repaidForm #dwmc').val(gysmc);
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

var addKhConfig = {
	width		: "800px",
	modalName	:"addKhModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#client_list_ajaxForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#repaidForm #dwdm').val(sel_row[0].khdm);
					$('#repaidForm #dwid').val(sel_row[0].khid);
					$('#repaidForm #dwmc').val(sel_row[0].khmc);
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

var chooseBmConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseBmModal",
	formName	: "purchaseCancelForm",
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
					$('#repaidForm #dwid').val(sel_row[0].jgid);
					$('#repaidForm #dwmc').val(sel_row[0].jgmc);
					$('#repaidForm #dwdm').val(sel_row[0].jgdm);
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
 * 选择申请部门列表
 * @returns
 */
function chooseSqbm(){
	var url=$('#repaidForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择部门',addSqbmConfig);
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
					if (sel_row[0].jgdm){
						$('#repaidForm #bmdm').val(sel_row[0].jgdm);
					}else{
						$('#repaidForm #bmdm').val("");
						$.alert("U8不存在此部门！");
						return false;
					}
					$('#repaidForm #bm').val(sel_row[0].jgid);
					$('#repaidForm #bmmc').val(sel_row[0].jgmc);
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


$(document).ready(function(){
	//初始化列表
	var oTable=new repaid_TableInit();
	oTable.Init();
	// 初始化页面
	init();
	// //0.初始化fileinput
	// var oFileInput = new FileInput();
	// var picking_params=[];
	// picking_params.prefix=$("#repaidForm #urlPrefix").val();
	// oFileInput.Init("repaidForm","displayUpInfo",2,1,"pro_file",null,picking_params);
	//所有下拉框添加choose样式
	jQuery('#repaidForm .chosen-select').chosen({width: '100%'});
});