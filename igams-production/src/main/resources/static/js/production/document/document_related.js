 /*---------------------------用户列表-----------------------------------*/
var RelDoc_turnOff=true;
    	
var RelDoc_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function (){
        $('#reldoc_list').bootstrapTable({
            url: $("#reldocformSearch #urlPrefix").val()+'/production/document/pagedataGetListDocumentRE',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#reldocformSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "wjid",				//排序字段
            sortOrder: "desc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh:false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "wjid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
				field: 'wjid',
				title: '文件ID',
				width: '10%',
				align: 'left',
				visible: false
			}, {
				field: 'glwjid',
				title: '关联文件ID',
				width: '10%',
				align: 'left',
				visible: false
			}, {
				field: 'ywjid',
				title: '原文件ID',
				titleTooltip:'原文件ID',
				width: '7%',
				align: 'left',
				visible: false
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
				visible: true
			}, {
				field: 'wjflmc',
				title: '文件分类',
				titleTooltip:'文件分类',
				width: '20%',
				align: 'left',
				visible: true
			}, {
				field: 'wjlbmc',
				title: '文件类别',
				titleTooltip:'文件类别',
				width: '20%',
				align: 'left',
				visible: true
			}, {
				field: 'bbh',
				title: '版本',
				titleTooltip:'版本',
				width: '15%',
				align: 'left',
				visible: true
			}, {
				field: 'jgmc',
				title: '所属单位',
				titleTooltip:'所属单位',
				width: '7%',
				align: 'left',
				visible: false
			}, {
				field: 'bz',
				title: '备注',
				titleTooltip:'备注',
				width: '12%',
				align: 'left',
				visible: false
			}, {
				field: 'scbj',
				title: '状态',
				titleTooltip:'状态',
				width: '15%',
				align: 'left',
				formatter:scbjFormat,
				visible: true
			}],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
        });
    };
    	// 得到查询的参数
    	oTableInit.queryParams = function(params){
    	// 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
    	// 例如 toolbar 中的参数
    	// 如果 queryParamsType = ‘limit’ ,返回参数必须包含
    	// limit, offset, search, sort, order
    	// 否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
    	// 返回false将会终止请求
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "wjid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getSearchDataReldoc(map);
    	};
    return oTableInit;
}

//使用状态（删除标记）格式化
function scbjFormat(value,row,index) {
    if (row.scbj == '2') {
        return '停用';
    }else if (row.scbj == '3') {
        return '作废';
    }else{
        return '正常';
    }
}

function searchRelDocResult(isTurnBack){
	if(isTurnBack){
		$('#reldocformSearch #reldoc_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#reldocformSearch #reldoc_list').bootstrapTable('refresh');
	}
}

function getSearchDataReldoc(map){
	var cxtj=$("#reldocformSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#reldocformSearch #cxnr').val());
	// '0':'文件编码','1':'文件名称','2':'版本号'
	if (cxtj == "0") {
		map["wjbh"] = cxnr;
	}else if (cxtj == "1") {
		map["wjmc"] = cxnr;
	}else if (cxtj == "2") {
		map["bbh"] = cxnr;
	}
	map["wjid"] = $("#reldocformSearch #prewjid").val();	
	return map;
}

var RelDoc_ButtonInit = function(){
	var oInit = new Object();
	var postdata = {};
	oInit.Init = function () {
	    //初始化页面上面的按钮事件
		var btn_query =$("#reldocformSearch #btn_query");//模糊查询
		
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchRelDocResult(true); 
			});
		};
	};
	return oInit;
};

$(function(){
    var oTable = new RelDoc_TableInit();
    oTable.Init();
    var oButtonInit = new RelDoc_ButtonInit();
    oButtonInit.Init();
    jQuery('#reldocformSearch .chosen-select').chosen({width: '100%'});
});
