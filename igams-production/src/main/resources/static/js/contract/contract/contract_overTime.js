var overTimeView_TableInit = function () {
    //初始化Table
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#contractOverTime_Form #contractOverTime_view").bootstrapTable({
			url: $("#contractOverTime_Form #urlPrefix").val()+'/contract/contract/pagedataGetOverTimeView?htid='+$("#contractOverTime_Form #htid").val()+'&qgid='+$("#contractOverTime_Form #qgid").val(),         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#contractOverTime_Form #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   //是否显示分页（*）
			paginationShowPageGo: false,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "wl.wlbm",				//排序字段
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
			showColumns: false,                  //是否显示所有的列
			showRefresh: false,                  //是否显示刷新按钮
			minimumCountColumns: 2,             //最少允许的列数
			clickToSelect: true,                //是否启用点击选中行
			//height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "htmxid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: [{
				title: '序',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '3%',
				align: 'left',
				visible:true
			},{
				field: 'wlbm',
				title: '物料编码',
				width: '5%',
				align: 'left',
				visible: true
			},{
				field: 'wlmc',
				title: '物料名称',
				titleTooltip:'物料名称',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'gg',
				title: '规格',
				width: '10%',
				align: 'left',
				visible: true
			},{
                field: 'qgsl',
                title: '请购数量',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'sl',
                title: '采购数量',
                width: '5%',
                align: 'left',
                visible: true
            },{
				field: 'hsdj',
				title: '含税单价',
				width: '5%',
				align: 'left',
				visible: true
			},{
				field: 'hjje',
				title: '合计金额',
				width: '5%',
				align: 'left',
				visible: true
			},{
                field: 'qwrq',
                title: '期望到货日期',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'jhdhrq',
                title: '计划到货日期',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'ccdg',
                title: '初次订购',
                width: '3%',
                align: 'left',
                formatter:ccFormat,
                visible: true
            },{
                field: 'wlflmc',
                title: '物料分类',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'cpzch',
                title: '产品注册号',
                width: '8%',
                align: 'left',
                visible: true
            }],
			onLoadSuccess:function(map){

			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
		});
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			pageSize: 1,   // 页面大小
			pageNumber: 1,  // 页码
			access_token:$("#ac_tk").val(),
			sortName: params.sort,      // 排序列名
			sortOrder: params.order, // 排位命令（desc，asc）
			sortLastName: "wl.wlmc", // 防止同名排位用
			sortLastOrder: "asc" // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		return map;
	};
	return oTableInit
}
function ccFormat(value,row,index) {
    var result="";
    if(row.ccdg){
        if("1"==row.ccdg){
            result = "是";
        }else{
            result = "否";
        }
    }
    return result;
}
$(function(){
	var oTable = new overTimeView_TableInit();
	oTable.Init();
})