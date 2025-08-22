// 显示字段
var columnsArray = [
	{
    	title: '序号',
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
        width: '1%',
        align: 'left',
        visible: false
    }, {
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '6%',
        align: 'left',
        formatter:ajaxForm_wlbmformat,
        visible: true
    }, {
        field: 'wlmc',
        title: '物料名称',
        titleTooltip:'物料名称',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'gg',
        title: '规格',
        titleTooltip:'规格',
        width: '3%',
        align: 'left',
        visible: true
    }, {
        field: 'jldw',
        title: '单位',
        titleTooltip:'单位',
        width: '3%',
        align: 'left',
        visible: true
    },  {
        field: 'scph',
        title: '生产批号',
        width: '7%',
        align: 'left',
        titleTooltip:'生产批号',
        visible: true
    },{
        field: 'jydh',
        title: '检验单号',
        width: '9%',
        align: 'left',
        formatter:ajaxForm_jydhformat,
        visible: true
    },{
        field: 'dhdh',
        title: '货号',
        titleTooltip:'货号',
        width: '8%',
        align: 'left',
        formatter:ajaxForm_dhdhformat,
        visible: true
    }, {
        field: 'sl',
        title: '数量',
        titleTooltip:'数量',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'htnbbh',
        title: '合同号',
        titleTooltip:'合同号',
        width: '10%',
        align: 'left',
        visible: true
    }, {
        field: 'cpzch',
        title: '注册号',
        titleTooltip:'注册号',
        width: '6%',
        align: 'left',
        formatter:cpzchformat,
        visible: true,
    },{
        field: 'kwmc',
        title: '库位',
        titleTooltip:'库位',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'rkbz',
        title: '入库备注',
        titleTooltip:'入库备注',
        width: '10%',
        align: 'left',
        visible: true
    },{
        field: 'dhbz',
        title: '到货备注',
        titleTooltip:'到货备注',
        width: '10%',
        align: 'left',
        visible: true
    },{
        field: 'u8rkdh',
        title: 'U8入库单号',
        titleTooltip:'U8入库单号',
        width: '10%',
        align: 'left',
        visible: true
    }];
var putInStorage_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#ajaxForm #tb_list').bootstrapTable({
            url: $("#ajaxForm #urlPrefix").val()+'/warehouse/putInStorage/pagedataGetListByhwid',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#ajaxForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "rkrq",					//排序字段
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
            uniqueId: "hwid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
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
            sortLastName: "htnbbh", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            rkid: $("#ajaxForm #rkid").val(),
            //搜索框使用
            //search:params.search
        };
    	return map;
    };
    return oTableInit;
};

/**
 * 检验方式格式化
 * @returns
 */
function ajaxForm_jyformat(value,row,index) {
	if(row.lbcskz1=="1"){
		var html="<span>"+"试剂"+"</span>";
	}else if(row.lbcskz1=="2"){
		var html="<span>"+"仪器"+"</span>";
	}else{
		var html="<span>"+"未检验"+"</span>";
	}
	return html;
}

/**
 * 检验单号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function ajaxForm_jydhformat(value,row,index){
	var html = ""; 	
	if(row.jydh!=null && row.jydh!=""){
		html += "<a href='javascript:void(0);' onclick=\"ajaxForm_queryByJydh('"+row.dhjyid+"')\">"+row.jydh+"</a>";
	}		
	return html;
}
function ajaxForm_queryByJydh(dhjyid){
	var url=$("#ajaxForm #urlPrefix").val()+"/inspectionGoods/inspectionGoods/viewInspectionGoods?dhjyid="+dhjyid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'检验信息',viewJyConfig);	
}
/**
 * 产品注册号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function cpzchformat(value,row,index){
    if(row.cpzch == null){
        row.cpzch = "" ;
    }
    return row.cpzch;
}
var viewJyConfig = {
		width		: "1600px",
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
 * 到货单号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function ajaxForm_dhdhformat(value,row,index){
	var html = ""; 	
		html += "<a href='javascript:void(0);' onclick=\"ajaxForm_queryByDhdh('"+row.dhid+"')\">"+row.dhdh+"</a>";
	return html;
}
function ajaxForm_queryByDhdh(dhid){
	var url=$("#ajaxForm #urlPrefix").val()+"/storehouse/arrivalGoods/pagedataViewArrivalGoods?dhid="+dhid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'到货信息',viewDhConfig);	
}

var viewDhConfig = {
		width		: "1600px",
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
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function ajaxForm_wlbmformat(value,row,index){
	var html = ""; 	
		html += "<a href='javascript:void(0);' onclick=\"ajaxForm_queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";
	return html;
}
function ajaxForm_queryByWlbm(wlid){
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

}


$(document).ready(function(){
	//初始化列表
	var oTable=new putInStorage_TableInit();
	oTable.Init();
	// 初始化页面
	init();
	//所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
});


