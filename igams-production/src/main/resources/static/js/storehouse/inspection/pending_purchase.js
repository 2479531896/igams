var t_map = [];
var t_index="";
var kwlist = [];
// 显示字段
var columnsArray = [
	{
		checkbox: true,
		width: '1%'
	},{
    	title: '序号',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序号',
		width: '3%',
        align: 'left',
        visible:true
    },{
		field: 'wlid',
		title: '物料ID',
		width: '8%',
		align: 'left',
		visible: false
	},{
		field: 'wlbm',
		title: '物料编码',
		width: '8%',
		align: 'left',
		visible: true
	}, {
		field: 'wlmc',
		title: '物料名称',
		width: '8%',
		align: 'left',
		visible: true
	},  {
		field: 'jldw',
		title: '单位',
		width: '4%',
		align: 'left',
		visible: true
	}, {
		field: 'scph',
		title: '生产批号',
		width: '6%',
		align: 'left',
		visible: true
	}, {
		field: 'kwmc',
		title: '库位',
		width: '5%',
		align: 'left',
		visible: true
	}, {
		field: 'dhsl',
		title: '到货数量',
		width: '5%',
		align: 'left',
		visible: true
	},{
		field: 'cythsl',
		title: '初检退回',
		width: '5%',
		align: 'left',
		visible: true
	},{
		field: 'dclsl',
		title: '质检退回',
		width: '5%',
		align: 'left',
		visible: true
	},{
		field: 'hcsl',
		title: '红冲数量',
		width: '8%',
		align: 'left',
		formatter:hcslformat,
		visible: true
	},{
		field: 'bz',
		title: '备注',
		width: '12%',
		align: 'left',
		formatter:bzPurchaseformat,
		visible: true
	},{
		field: 'cz',
		title: '操作',
		width: '10%',
		align: 'left',
		formatter:czPurchaseformat,
		visible: true

    }];
var pendingEdit_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#pendingpurchaseForm #pendingPurchase_list').bootstrapTable({
            url: $('#pendingpurchaseForm #urlPrefix').val()+$('#pendingpurchaseForm #url').val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#pendingpurchaseForm #toolbar',                //工具按钮用哪个容器
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
            uniqueId: "wlid",                     //每一行的唯一标识，一般为主键列
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
		var hwxxids =  $('#pendingpurchaseForm #hwxxids').val();
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
			ids: hwxxids,
			dhid: $('#pendingpurchaseForm #dhid').val(),
        };
    	return map;
    };
    return oTableInit;
};

/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czPurchaseformat(value,row,index){
	var html = "<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除物料信息' onclick=\"purchaesDelWlDetail('" + index + "','"+row.hwid+"',event)\" >删除</span>";
	return html;
}
/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function purchaesDelWlDetail(index,value,event){
	t_map.rows.splice(index,1);
	$("#pendingpurchaseForm #pendingPurchase_list").bootstrapTable('load',t_map);
}
/**
 * 红冲数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function hcslformat(value,row,index){
	if (!row.hcsl){
		row.hcsl= row.dclsl*-1;
	}
	var html= "<input style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' id='hcsl_"+index+"' validate='{hcslNumber:true}' autocomplete='off' value='"+row.hcsl+"' name='hcsl_"+index+"' max='0' min='"+row.dclsl*-1+"'  onblur=\"checkPurchaseSum('"+index+"',this,\'hcsl\')\" ></input>";
	return html;
}

/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("hcslNumber", function (value, element){
	if(!value) {
		$.error("请填写数量!");
		return false;
	}
	return this.optional(element) || /^(-)?\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");
/**
 * 备注格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function bzPurchaseformat(value,row,index){
	if(row.bz == null){
		row.bz = "" ;
	}
	var html="<input id='bz_"+index+"' autocomplete='off' name='bz_"+index+"' value='"+row.bz+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{stringMaxLength:1024}' onblur=\"checkPurchaseSum('"+index+"',this,\'bz\')\"></input>";
	return html;
}
function checkPurchaseSum(index, e, value) {
	if (value == "hcsl"){
		t_map.rows[index].hcsl = e.value;
		if (e.value < t_map.rows[index].dclsl*-1){
			$.alert("红冲数量不能大于质检退回数量！");
			$("#pendingpurchaseForm #hcsl_"+index).val("0");
			t_map.rows[index].hcsl = 0;
		}
	} else if (value == "bz"){
		t_map.rows[index].bz = e.value;
	}
}


/**
 * 初始化页面
 * @returns
 */
function init(){
  	//添加日期控件
	laydate.render({
	   elem: '#dhrq'
	  ,theme: '#2381E9'
	});
}
$(document).ready(function(){
	//初始化列表
	var oTable=new pendingEdit_TableInit();
	oTable.Init();
	// 初始化页面
	init();
	jQuery('#pendingpurchaseForm .chosen-select').chosen({width: '100%'});
	$("#pendingpurchaseForm #ck").attr("style","pointer-events: none;margin-top:5px");
	$("#pendingpurchaseForm #serk").attr("style","pointer-events: none;");
	$("#pendingpurchaseForm #secg").attr("style","pointer-events: none;");
});