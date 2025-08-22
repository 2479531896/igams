var report_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#SjxxBgByysdhForm #sjbg_list').bootstrapTable({
            url: '/wechat/getPageListByysdh',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#SjxxBgByysdhForm  #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "sjxx.lrsj",				//排序字段
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
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "sjid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                field: 'sjid',
                title: '送检id',
                titleTooltip:'送检id',
                width: '25%',
                align: 'left',
                visible: false
            },{
                field: 'ybbh',
                title: '标本编号',
                titleTooltip:'标本编号',
                width: '25%',
                align: 'left',
                visible: false
            },{
                field: 'hzxm',
                title: '患者姓名',
                titleTooltip:'患者姓名',
                width: '25%',
                align: 'left',
                visible: true
            },{
                field: 'cyrq',
                title: '送检日期',
                titleTooltip:'送检日期',
                width: '25%',
                align: 'right',
                visible: true,
            },{
                field: 'bgrq',
                title: '报告日期',
                titleTooltip:'报告日期',
                width: '25%',
                align: 'right',
                visible: true,
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onClickRow: function (row, $element) {
            	reportDealById(row.sjid,row.ysdh,row.hzxm);
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
        	pageSize: params.limit,   //页面大小
        	pageNumber: (params.offset / params.limit) + 1,  //页码
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "sjxx.sjid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        return getViewReportDocSearchData(map);
    };
    return oTableInit;
};
function getViewReportDocSearchData(map){
	// 患者姓名
	var hzxm = jQuery('#SjxxBgByysdhForm #hzxm').val();
	map["hzxm"] =hzxm;
	// 电话
	var sj = jQuery('#SjxxBgByysdhForm #sj').val();
	map["ysdh"] = sj;
	
	var sign = jQuery('#SjxxBgByysdhForm #sign').val();
	map["sign"] = sign;
	return map;
}
function reportDealById(sjid,ysdh,hzxm){
	window.location.href="/wechat/getUserInfoView?sjid="+sjid+"&sflx="+$("#sflx").val()+"&sign="+encodeURIComponent($("#sign").val());
}

$("#confirm").click(function(){
	if($("#hzxm").val()!=null && $("#hzxm").val()!=""){
		searchReportResult();
	}else{
		$.alert("请输入患者姓名！")
	}
})

function searchReportResult(){
	$('#SjxxBgByysdhForm #sjbg_list').bootstrapTable('refresh');
}
$(function(){
	//0.界面初始化
	
    //1.初始化Table
    var oTable = new report_TableInit();
    oTable.Init();
});