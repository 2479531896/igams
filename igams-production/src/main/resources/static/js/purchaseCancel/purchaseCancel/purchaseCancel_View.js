var t_map=[];
var qglbdm=$("#PurchaseCancel_Form #qglbdm").val();
var cgmx_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    var qgqxid=$("#PurchaseCancel_Form #qgqxid").val();
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#PurchaseCancel_Form #cgqxmx_view").bootstrapTable({
			url: $("#PurchaseCancel_Form #urlPrefix").val()+'/purchaseCancel/purchaseCancel/pagedataGetListpurchaseCancelInfo?qgqxid='+qgqxid,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#PurchaseCancel_Form #toolbar',                //工具按钮用哪个容器
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
                visible: PurchaseCancelViewWlbmVisible(),
            },{
                field: 'wlmc',
                title: '物料名称',
                width: '10%',
                align: 'left',
                formatter:purchaseCancelViewwlmcformat,
                visible: true
            },{
        	    field: 'qglbmc',
        	    title: '类别',
        	    width: '3%',
        	    align: 'left',
        	    sortable: true,
        	    visible: true
        	},{
                field: 'wllbmc',
                title: '物料类别',
                width: '8%',
                align: 'left',
                visible: purchaseCancelViewwllbVisible()
            },{
        	    field: 'hwbz',
        	    title: purchaseCancelViewhwbztitle(),
        	    width: '5%',
        	    align: 'left',
        	    sortable: true,
        	    visible: purchaseCancelViewhwbzVisible()
        	},{
        	    field: 'hwyt',
        	    title: purchaseCancelViewhwyttitle(),
        	    width: '5%',
        	    align: 'left',
        	    sortable: true,
        	    visible: purchaseCancelViewhwytVisible()
        	},{
                field: 'wlzlbmc',
                title: '物料子类别',
                width: '8%',
                align: 'left',
                visible: purchaseCancelViewwlzlbVisible()
            },{
                field: 'wlzlbtcmc',
                title: '子类别统称',
                width: '8%',
                align: 'left',
                visible: purchaseCancelViewwlzlbtcVisible()
            },{
                field: 'gg',
                title: '规格',
                width: '5%',
                align: 'left',
                visible: purchaseCancelViewggVisible()
            },{
                field: 'ychh',
                title: '货号',
                width: '8%',
                align: 'left',
                visible: purchaseCancelViewychhVisible()
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
                visible: false
            },{
                field: 'ydsl',
                title: '预定数量',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'jg',
                title: '价格',
                width: '5%',
                align: 'left',
                visible: false
            },{
                field: 'qwrq',
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
	$("#PurchaseCancel_Form #cgqxmx_view").colResizable({
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

function purchaseCancelViewhwytVisible(){
	if(qglbdm!='MATERIAL' && qglbdm!='ADMINISTRATION' && qglbdm!=null && qglbdm!=''){
		return true;
	}
	return false;
}

//货物用途标题格式化
function purchaseCancelViewhwyttitle(){
	if(qglbdm=='DEVICE'){
		return "设备用途";
	}else if(qglbdm=='SERVICE'){
		return "服务用途";
	}
}

function purchaseCancelViewhwbzVisible(){
	if(qglbdm!='MATERIAL' && qglbdm!=null && qglbdm!=''){
		return true;
	}
	return false;
}

//货物标准标题格式化
function purchaseCancelViewhwbztitle(){
	if(qglbdm=='DEVICE'){
		return "规格型号";
	}else if(qglbdm=='SERVICE'){
		return "服务期限";
	}else if(qglbdm=='ADMINISTRATION'){
		return "规格型号";
	}
}

function purchaseCancelViewwlmcformat(value,row,index){
	if(qglbdm=='MATERIAL' || qglbdm==null || qglbdm==''){
		return row.wlmc;
	}
	return row.hwmc;
}

function purchaseCancelViewychhVisible(){
	if(qglbdm=='MATERIAL' || qglbdm==null || qglbdm==''){
		return true;
	}
	return false;
}

function purchaseCancelViewggVisible(){
	if(qglbdm=='MATERIAL' || qglbdm==null || qglbdm==''){
		return true;
	}
	return false;
}

function purchaseCancelViewwlzlbtcVisible(){
	if(qglbdm=='MATERIAL' || qglbdm==null || qglbdm==''){
		return true;
	}
	return false;
}

function purchaseCancelViewwlzlbVisible(){
	if(qglbdm=='MATERIAL' || qglbdm==null || qglbdm==''){
		return true;
	}
	return false;
}

function purchaseCancelViewwllbVisible(){
	if(qglbdm=='MATERIAL' || qglbdm==null || qglbdm==''){
		return true;
	}
	return false;
}

function PurchaseCancelViewWlbmVisible(){
    if(qglbdm=='MATERIAL' || qglbdm==null || qglbdm==''){
        return true;
    }
    return false;
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
	var url=$("#PurchaseCancel_Form #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
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