var t_map = [];
// 显示字段
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
        field: 'htmxid',
        title: '合同明细ID',
        titleTooltip:'合同明细ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'mxid',
        title: '明细ID',
        titleTooltip:'明细ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'qgid',
        title: '请购ID',
        titleTooltip:'请购ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'wlid',
        title: '物料ID',
        titleTooltip:'物料ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'djh',
        title: '请购单号',
        titleTooltip:'请购单号',
        width: '8%',
        align: 'left',
        visible: true,
    }, {
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '8%',
        align: 'left',
        formatter:wlbmformat,
        visible: true
    }, {
        field: 'wlmc',
        title: '物料名称',
        titleTooltip:'物料名称',
        width: '8%',
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
        field: 'sl',
        title: '数量',
        titleTooltip:'数量',
        width: '6%',
        align: 'left',
        visible: true
    }, {
        field: 'hsdj',
        title: '含税单价',
        titleTooltip:'含税单价',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'hjje',
        title: '合计金额',
        titleTooltip:'合计金额',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'jhdhrq',
        title: '计划到货日期',
        titleTooltip:'计划到货日期',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'bz',
        title: '备注',
        titleTooltip:'备注',
        width: '8%',
        align: 'left',
        visible: true
    }];
var contractEdit_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#ajaxForm #tb_list').bootstrapTable({
            url: $("#ajaxForm #urlPrefix").val()+'/contract/contract/pagedataGetContractDetailList',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#ajaxForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "qgid",				//排序字段
            sortOrder: "desc",                   //排序方式
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
            uniqueId: "htmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
            	t_map = map;
            	if(t_map.rows){
            		// 初始化htmx_json
            		var json = [];
            		for (var i = 0; i < t_map.rows.length; i++) {
            			var sz = {"qgid":'',"qgmxid":''};
						sz.qgid = t_map.rows[i].qgid;
						sz.qgmxid = t_map.rows[i].qgmxid;
						json.push(sz);
    				}
            		$("#ajaxForm #htmx_json").val(JSON.stringify(json));
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
            sortLastName: "qgmxid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            htid: $("#ajaxForm #htid").val(),
            //搜索框使用
            //search:params.search
        };
    	return map;
    };
    return oTableInit;
};

/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wlbmformat(value,row,index){
	var html = ""; 	
		html += "<a href='javascript:void(0);' onclick=\"queryByFirstWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";
	return html;
}
function queryByFirstWlbm(wlid){
	var url=$("#ajaxForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
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

/**
 * 初始化页面
 * @returns
 */
function init(){
	//添加日期控件
	laydate.render({
	   elem: '#ajaxForm #cjrq'
	  ,theme: '#2381E9'
	});
	// 初始化请购单号
	initDjh();
	//币种下拉框改变事件
	var sel_biz = $("#ajaxForm #biz");
	sel_biz.unbind("change").change(function(){
		var csid = $("#ajaxForm #biz").val();
		var cskz1 = $("#ajaxForm #"+csid).attr("cskz1");
		if(cskz1){
			$("#ajaxForm #sl").val(cskz1);
		}
	});
}
/**
 * 初始化请购单号
 * @returns
 */
function initDjh(){
	var qgids = $("#ajaxForm #qgids").val();
	if(!qgids || qgids.length < 0){
		return;
	}
	$.ajax({ 
	    type:'post',  
	    url:$("#ajaxForm #urlPrefix").val()+"/purchase/purchase/pagedataGetPurchaseList",
	    cache: false,
	    data: {"ids":qgids,"access_token":$("#ac_tk").val()},  
	    dataType:'json',
	    success:function(result){
	    	//返回值
	    	var qgglDtos = result.qgglDtos;
	    	if(qgglDtos){
	    		$("#ajaxForm #djhs").tagsinput({
	    			itemValue: "qgid",
	    			itemText: "djh",
	    		})
	    		for(var i = 0; i < qgglDtos.length; i++){
		    		$("#ajaxForm #djhs").tagsinput('add',{"qgid": qgglDtos[i].qgid, "djh": qgglDtos[i].djh});
		    	}
	    	}
	    }
	});
}

// function queryByWlbm(wlid){
// 	var url=$("#ajaxForm #urlPrefix").val()+"/production/materiel/viewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
// 	$.showDialog(url,'物料信息',viewWlConfig);
// }
//
// var viewWlConfig = {
// 		width		: "800px",
// 		height		: "500px",
// 		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
// 		buttons		: {
// 			cancel : {
// 				label : "关 闭",
// 				className : "btn-default"
// 			}
// 		}
// 	};

function queryByQgid(qgid){
	var url=$("#ajaxForm #urlPrefix").val()+"/purchase/purchase/viewPurchase?qgid="+qgid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'请购信息',viewQgConfig);	
}

var viewQgConfig={
	width		: "1500px",
	modalName	:"viewPurchaseModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}
function queryByHtid(htid){
	var url=$("#ajaxForm #urlPrefix").val()+"/contract/contract/pagedataViewContract?htid="+htid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'合同信息',viewHtConfig);	
}

var viewHtConfig={
		width		: "1500px",
		modalName	:"viewHtModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	}

function queryByHwid(hwid){
	var url=$("#ajaxForm #urlPrefix").val()+"/warehouse/goods/hwxxgetView?hwid="+hwid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'货物信息',viewHwConfig);	
}
var viewHwConfig={
		width		: "1000px",
		modalName	:"viewHwModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	}

$(document).ready(function(){
	//初始化列表
	var oTable=new contractEdit_TableInit();
	oTable.Init();
	// 初始化页面
	init();
	//所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
});