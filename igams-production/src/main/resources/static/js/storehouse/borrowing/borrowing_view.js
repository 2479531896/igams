var columnsArray = [
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
		field: 'kdgs',
		title: '快递公司',
		titleTooltip:'快递公司',
		width: '15%',
		align: 'left',
		visible: true
	}, {
		field: 'wldh',
		title: '快递单号',
		titleTooltip:'快递单号',
		width: '15%',
		align: 'left',
		visible: true
	}, {
		field: 'qsrq',
		title: '签收时间',
		titleTooltip:'签收时间',
		width: '10%',
		align: 'left',
		visible: true
	}, {
		field: 'yf',
		title: '运费(元)',
		titleTooltip:'运费(元)',
		width: '5%',
		align: 'left',
		visible: true
	}, {
		field: 'sfqr',
		title: '是否确认',
		titleTooltip:'是否确认',
		width: '5%',
		align: 'left',
		formatter:sfqrformat,
		visible: true
	},{
		field: 'fjids',
		title: '附件',
		width: '3%',
		align: 'left',
		formatter:fjformat,
	}];
var logisticsUpholdView_TableInit = function () {
	var oTableInit = new Object();
	//初始化Table
	oTableInit.Init = function () {
		$('#viewBorrowAjaxForm #tb_list').bootstrapTable({
			url:$('#viewBorrowAjaxForm #urlPrefix').val()+'/storehouse/requisition/pagedataGetWlxxByYwid?ywid='+$('#viewBorrowAjaxForm #ywid').val(),         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#logisticsUpholdForm #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   //是否显示分页（*）
			sortable: true,                     //是否启用排序
			sortName: "wlxx.lrsj",				        //排序字段
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
			uniqueId: "wlxxid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: columnsArray,
			onLoadSuccess: function (map) {
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
			sortLastName: "wldh", //防止同名排位用
			sortLastOrder: "asc" //防止同名排位用
			//搜索框使用
			//search:params.search
		};
		return map;
	};
	return oTableInit;
};
function sfqrformat(value,row,index) {
	if (row.sfqr == '1') {
		return "是";
	}else if (row.sfqr == '0') {
		return "否";
	}else {
		return "";
	}
}
/**
 * 附件格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fjformat(value,row,index){
	var html = "<div id='fj_"+index+"'>";
	html += "<input type='hidden' id='wlfj_"+index+"' name='wlfj_"+index+"'/>";
	if(row.fjbj == "0"){
		html += "<a href='javascript:void(0);' onclick='ViewLogisFile(\"" + index + "\",\""+row.wlxxid+"\")' >是</a>";
	}else{
		html += "<a href='javascript:void(0);' onclick='ViewLogisFile(\"" + index + "\",\""+row.wlxxid+"\")' >否</a>";
	}
	html += "</div>";
	return html;
}
function ViewLogisFile(index,zywid) {
	url="/storehouse/requisition/pagedataViewFj?access_token=" + $("#ac_tk").val()+"&zywid="+zywid+"&ywid="+$("#viewBorrowAjaxForm #ywid").val();
	$.showDialog($("#viewBorrowAjaxForm #urlPrefix").val()+url, '查看附件', viewFileConfig);
}

var viewFileConfig = {
	width : "800px",
	height : "500px",
	modalName	: "uploadFileModal",
	formName	: "view_ajaxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
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
//事件绑定
function btnBind(){
}


function initPage(){
}
function queryByWlbm(wlid){
	var url=$("#viewBorrowAjaxForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'物料信息',viewWlConfig);
}
var viewWlConfig = {
	width		: "800px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
function viewYxht(htid){
	var url=$("#borrowing_formSearch #urlPrefix").val()+"/marketingContract/marketingContract/pagedataViewMarketingContract?htid="+htid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'营销合同信息',viewMarketingContractConfig);
}
var viewMarketingContractConfig = {
	width		: "1600px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
function showXsInfo(xsid,jcjyid) {
	$.showDialog($("#viewBorrowAjaxForm #urlPrefix").val()+"/storehouse/sale/pagedataViewSale?xsid="+xsid+"&jcjyid="+jcjyid,'详细信息',viewXsConfig);
}
var viewXsConfig = {
	width		: "1600px",
	modalName	: "viewsaleModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
$(document).ready(function(){
	//初始化列表
	var oTable=new logisticsUpholdView_TableInit();
	oTable.Init();
    //所有下拉框添加choose样式
	jQuery('#viewBorrowAjaxForm .chosen-select').chosen({width: '100%'});
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
});
