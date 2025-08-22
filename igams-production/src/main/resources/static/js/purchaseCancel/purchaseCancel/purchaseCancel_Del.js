var t_map=[];
var cgmx_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    var qgqxid=$("#PurchaseCancel_del_Form #qgqxid").val();
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#PurchaseCancel_del_Form #qgqxmx_view").bootstrapTable({
			url: $("#PurchaseCancel_del_Form #urlPrefix").val()+'/purchaseCancel/purchaseCancel/pagedataGetListpurchaseCancelInfo?qgqxid='+qgqxid,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#PurchaseCancel_del_Form #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "qgmx.qxqgsj",				//排序字段
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
            uniqueId: "qgqxmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                field: 'wlbm',
                title: '物料编码',
                titleTooltip:'物料编码',
                width: '8%',
                formatter:wlbmformat,
                align: 'left',
                visible: true,
            },{
                field: 'wlmc',
                title: '物料名称',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'wllbmc',
                title: '物料类别',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'wlzlbmc',
                title: '物料子类别',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'wlzlbtcmc',
                title: '子类别统称',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'gg',
                title: '规格',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'ychh',
                title: '货号',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'qxsl',
                title: '取消数量',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'sysl',
                title: '剩余数量',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'ydsl',
                title: '预定数量',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'jg',
                title: '价格',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'qwdhrq',
                title: '使用日期',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '20%',
                align: 'left',
                visible: true
            },{
                field: 'zt',
                title: '审核',
                width: '3%',
                formatter:shformat,
                align: 'left',
                visible: true
            }],
            onLoadSuccess:function(map){
            	t_map=map;
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
		})
	$("#PurchaseCancel_del_Form #qgqxmx_view").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true}        
        );
	};
	oTableInit.queryParams=function(params){
	    var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            	pageSize: params.limit,   // 页面大小
            	pageNumber: (params.offset / params.limit) + 1,  // 页码
            	access_token:$("#ac_tk").val(),
            	sortName: params.sort,      // 排序列名
            	sortOrder: params.order, // 排位命令（desc，asc）
           	sortLastName: "qgmx.qgmxid", // 防止同名排位用
           	 sortLastOrder: "asc" // 防止同名排位用
           	 // 搜索框使用
           	 // search:params.search
            };
	    return map;
	};
    return oTableInit
}


//审核格式化zt
function shformat(value,row,index){
	var html="";
		if(row.zt=='00'||row.zt=='15'){
			html="<span style='color:red;'>取消</span>";
		}else{			
			html="<span style='color:green;'>正常</span>";
	}

	return html;	
}

function wlbmformat(value,row,index){
	var html = ""; 	
		html += "<a href='javascript:void(0);' onclick=\"queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";
	return html;
}
function queryByWlbm(wlid){
	var url=$("#PurchaseCancel_del_Form #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
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

$(function(){
	//0.界面初始化
	//1.初始化Table
    var oTable = new cgmx_TableInit();
    oTable.Init();
})