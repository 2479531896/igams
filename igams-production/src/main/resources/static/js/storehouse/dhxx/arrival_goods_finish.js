 	
var mater_RK_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#dh_finish_formSearch #tb_list').bootstrapTable({
            url: $('#dh_finish_formSearch #urlPrefix').val() + '/storehouse/arrivalGoods/pagedataGetMaterial',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#dh_finish_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            //paginationShowPageGo: false,         //增加跳转页码的显示
//            paginationDetailHAlign: " hidden", //不显示底部文字
            sortable: true,                     //是否启用排序
            sortName: "wl.lrsj",				//排序字段
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
            showColumns: true,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "wlid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            }, {
                field: 'wlid',
                title: '物料ID',
                width: '8%',
                align: 'left',
                visible: false
            }, 
            {
                field: 'wllbmc',
                title: '物料类别',
                titleTooltip:'物料类别',
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'wlzlbmc',
                title: '物料子类别',
                titleTooltip:'物料子类别',
                width: '10%',
                align: 'left',
                visible: false
            }, 
            {
                field: 'wlbm',
                title: '物料编码',
                titleTooltip:'物料编码',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true,
            }, {
                field: 'wlmc',
                title: '物料名称',
                titleTooltip:'物料名称',
                width: '18%',
                align: 'left',
                visible: true
            }, {
                field: 'lbmc',
                title: '类别',
                titleTooltip:'类别',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'scs',
                title: '生产商',
                titleTooltip:'生产商',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'ychh',
                title: '货号',
                titleTooltip:'货号',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'gg',
                title: '规格',
                titleTooltip:'规格',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'jldw',
                title: '单位',
                titleTooltip:'单位',
                width: '4%',
                align: 'right',
                visible: true
            },{
                field: 'bctj',
                title: '保存条件',
                titleTooltip:'保存条件',
                width: '10%',
                align: 'left',
                visible: false
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            },
        });
		$("#dh_finish_formSearch #tb_list").colResizable({
			liveDrag:true, 
			gripInnerHtml:"<div class='grip'></div>", 
			draggingClass:"dragging", 
			resizeMode:'fit', 
			postbackSafe:true,
			partialRefresh:true
		})
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
        	pageSize: params.limit,   //页面大小
        	//pageNumber: 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "wl.wlid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            zt: $("#dh_finish_formSearch #zt").val()
            //搜索框使用
            //search:params.search
        };

        return getMaterSearchData(map);
    };
    return oTableInit;
};

function getMaterSearchData(map){
	var cxtj = $("#dh_finish_formSearch #cxtj").val();
	// 查询条件
	var cxnr = $.trim(jQuery('#dh_finish_formSearch #cxnr').val());
	if (cxtj == "0") {
		map["wlbm"] = cxnr;
	}else if (cxtj == "1") {
		map["wlmc"] = cxnr;
	}else if (cxtj == "2") {
		map["scs"] = cxnr;
	}else if (cxtj == "3") {
		map["ychh"] = cxnr;
	}else if (cxtj == "4") {
		map["lrryxm"] = cxnr;
    }else if (cxtj == "5") {
    	map["jwlbm"] = cxnr;
	}else if (cxtj == "6") {
    	map["entire"] = cxnr;
	}else if (cxtj == "7") {
        map["gg"] = cxnr;
    }
	return map;
}


var mater_RK_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_query = $("#dh_finish_formSearch #btn_query");

    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchRkResult(true);
    		});
    	}
    };

    return oInit;
};





function searchRkResult(isTurnBack){
	if(isTurnBack){
		$('#dh_finish_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#dh_finish_formSearch #tb_list').bootstrapTable('refresh');
	}
}


$(function(){
	//0.界面初始化
	
    //1.初始化Table
    var oTable = new mater_RK_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new mater_RK_ButtonInit();
    oButtonInit.Init();
});
