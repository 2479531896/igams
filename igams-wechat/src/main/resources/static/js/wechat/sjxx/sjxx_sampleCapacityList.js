var sampleCapacity_turnOff=true;
    	
var sampleCapacity_TableInit = function () {
    var oTableInit = new Object();
    var syrq=$("#syrq").val();
    //初始化Table
    oTableInit.Init = function (){
        $('#sampleCapacity_formSearch #sampleCapacity_list').bootstrapTable({
            url: '/ws/inspection/sampleCapacityList?syrq='+syrq,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#sampleCapacity_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "yblxmc",				//排序字段
            sortOrder: "DESC",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 200,                       //每页的记录行数（*）
            pageList: [200],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: false,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "sjid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                field: 'sjid',
                title: '送检id',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'ybbh',
                title: '标本编号',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'nbbm',
                title: '内部编码',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'hzxm',
                title: '患者姓名',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'syrq',
                title: '实验日期',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'yblxmc',
                title: '标本类型',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'jcxmmc',
                title: '检测项目名称',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'jsrq',
                title: '接收日期',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'db',
                title: '合作伙伴',
                width: '10%',
                align: 'left',
                visible:false
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
           onDblClickRow: function (row, $element) {
            },
        });
    };
    return oTableInit;
}

$(function(){
	var oTable=new sampleCapacity_TableInit();
		oTable.Init();
	jQuery('#sampleCapacity_formSearch .chosen-select').chosen({width: '100%'});
	setTimeout(function(){
		var counnt=$('#sampleCapacity_formSearch #sampleCapacity_list').bootstrapTable('getData').length;
		$("#count").text("标本数:"+counnt);
	},200)
})