var Sale_turnOff=true;

var Sale_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#salelist_formSearch #tb_list').bootstrapTable({
            url: '/sale/query/pageGetListQuery',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#salelist_formSearch #toolbar', //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName:"cx.cxmc,cx.lrsj",					//排序字段
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
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "cxid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '4%'
            }, {
                field: 'cxid',
                title: '查询ID',
                width: '24%',
                align: 'left',
                visible: false
            }, {
                field: 'cxmc',
                title: '查询名称',
                titleTooltip:'查询名称',
                width: '24%',
                align: 'left',
                visible: true
            }, {
                field: 'bz',
                title: '备注',
                titleTooltip:'备注',
                width: '24%',
                align: 'left',
                visible: true
            }, {
                field: 'xsfsmc',
                title: '显示方式',
                titleTooltip:'显示方式',
                width: '24%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	SaleDealById(row.cxid, 'view',$("#salelist_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#salelist_formSearch #tb_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true}        
        );
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
        	pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "cx.cxid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

    	var cxbt = $("#salelist_formSearch #cxtj").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#salelist_formSearch #cxnr').val());
    	// '0':'审核名称','1':'描述',
    	if (cxbt == "0") {
    		map["cxmc"] = cxnr;
    	}
    	
    	return map;
    };
    return oTableInit;
};



//按钮动作函数
function SaleDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='execute'){
		var url=tourl + "?cxid=" +id;
		$.showDialog(url,'执行临时查询',executeSaleConfig);
	}
}

var executeSaleConfig = {
	width		: "1200px",
	modalName	: "addSaleModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
	buttons		: {
		execute : {
			label : "执 行",
			className : "btn-success",
			callback : function() {
                $(".shadow").css({'display':'block'});
                $('.addBox').show();
                $("#btn_selectexecute").attr("disabled","disabled");
                setTimeout(5000);
				executionSelect();
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var Sale_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_execute= $("#salelist_formSearch #btn_execute");
    	var btn_query = $("#salelist_formSearch #btn_query");

    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchSaleResult(true);
    		});
    	}
    	btn_execute.unbind("click").click(function(){
    		var sel_row = $('#salelist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			SaleDealById(sel_row[0].cxid,"execute",btn_execute.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    };

    return oInit;
};

function searchSaleResult(isTurnBack){
	if(isTurnBack){
		$('#salelist_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#salelist_formSearch #tb_list').bootstrapTable('refresh');
	}
}

$(function(){

    //1.初始化Table
    var oTable = new Sale_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new Sale_ButtonInit();
    oButtonInit.Init();
    
	//所有下拉框添加choose样式
	jQuery('#salelist_formSearch .chosen-select').chosen({width: '100%'});
});
