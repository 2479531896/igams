var t_map = [];
var dwList = null;
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
		field: 'syglid',
		title: '实验管理id',
		titleTooltip:'实验管理id',
		width: '4%',
		align: 'left',
		visible: false
	}, {
		field: 'jcxmmc',
		title: '检测项目',
		titleTooltip:'检测项目',
		width: '10%',
		align: 'left',
		visible: true
	}, {
		field: 'jczxmmc',
		title: '检测子项目',
		titleTooltip:'检测子项目',
		width: '10%',
		align: 'left',
		visible: true
	}, {
		field: 'jclxmc',
		title: '检测类型',
		titleTooltip:'检测类型',
		width: '10%',
		align: 'left',
		visible: true
	}, {
		field: 'jcdwmc',
		title: '检测单位',
		titleTooltip:'检测单位',
		width: '16%',
		align: 'left',
		formatter:jcdwformat,
		visible: true
	}, {
		field: 'sfjs',
		title: '接收',
		titleTooltip:'接收',
		width: '6%',
		align: 'left',
		formatter:sfjsformat,
		visible: true
	}, {
		field: 'nbzbm',
		title: '子编码',
		titleTooltip:'子编码',
		width: '10%',
		align: 'left',
		visible: true
	}, {
		field: 'jsrq',
		title: '接收日期',
		titleTooltip:'接收日期',
		width: '10%',
		align: 'left',
		visible: true
	}, {
		field: 'jsrymc',
		title: '接收人员',
		titleTooltip:'接收人员',
		width: '10%',
		align: 'left',
		visible: true
	}, {
		field: 'jcbj',
		title: '检测',
		titleTooltip:'检测',
		width: '6%',
		align: 'left',
		formatter:jcbjformat,
		visible: true
	}, {
		field: 'syrq',
		title: '实验日期',
		titleTooltip:'实验日期',
		width: '10%',
		align: 'left',
		visible: true
	}, {
		field: 'jt',
		title: '接头',
		titleTooltip:'接头',
		width: '6%',
		align: 'left',
		visible: true
	}, {
		field: 'zt',
		title: '状态',
		titleTooltip:'状态',
		width: '6%',
		align: 'left',
		visible: true
	}, {
		field: 'bz',
		title: '备注',
		titleTooltip:'备注',
		width: '16%',
		align: 'left',
		visible: true
	}, {
		field: 'lxmc',
		title: '类型',
		titleTooltip:'类型',
		width: '16%',
		align: 'left',
		visible: true
	}];
var adjust_TableInit = function () {
	var oTableInit = new Object();
	//初始化Table
	oTableInit.Init = function () {
		$('#ajaxFormAdjust #sygl_list').bootstrapTable({
			url: $('#ajaxFormAdjust #url').val(),         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#ajaxFormAdjust #toolbar',    //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   //是否显示分页（*）
			sortable: true,                     //是否启用排序
			sortName: "jcxmid",					//排序字段
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
			uniqueId: "syglid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: columnsArray,
			onLoadSuccess: function (map) {
				t_map = map;
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
			sortLastName: "lrsj", //防止同名排位用
			sortLastOrder: "asc", //防止同名排位用
		};
		return map;
	};
	return oTableInit;
};

function checkJcdw(index){
	t_map.rows[index].jcdw = $("#ajaxFormAdjust #jc_" + index).find("option:selected").val();
}
//格式化
function sfjsformat(value,row,index){
	if(row.sfjs=='1'){
		return '是';
	}else{
		return '否';
	}
}
function jcbjformat(value,row,index){
	if(row.jcbj=='1'){
		return '是';
	}else{
		return '否';
	}
}
/**
 * 检测单位格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function jcdwformat(value,row,index){
	var html="";
	if (dwList && dwList.length >0) {
		html +="<div id='jcdwdiv_"+index+"' name='jcdwdiv' style='width:100%;display:inline-block'>";
		html +=  "<select id='jc_"+index+"' name='jc_"+index+"' class='form-control'  onchange=\"checkJcdw('"+index+"')\">";
		html += "<option value=''>请选择--</option>";
		for(var i=0;i<dwList.length;i++){

			if(row.jcdw==dwList[i].csid){
				html += "<option value='"+dwList[i].csid+"' selected >"+dwList[i].csmc+"</option>";
			}else{
				html += "<option value='"+dwList[i].csid+"' >"+dwList[i].csmc+"</option>";
			}
		}
		html += "</select></div>";
	}else{
		html += "<span  >"+value+"</span>";
	}
	return html;
}



$(document).ready(function(){
	//初始化列表
	var oTable=new adjust_TableInit();
	oTable.Init();
	var list = $('#ajaxFormAdjust #dwList').val()
	if (list){
		dwList = JSON.parse(list);
	}

	//所有下拉框添加choose样式
	jQuery('#ajaxFormAdjust .chosen-select').chosen({width: '100%'});
});