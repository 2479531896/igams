var qglbdm=$("#purchaseAuditViewForm #qglbdm").val();
var PurchaseViewInfo_TableInit=function(){
	var qgid=$("#purchaseAuditViewForm #qgid").val();
	var url=$("#purchaseAuditViewForm #urlPrefix").val()+'/purchase/purchase/pagedataListQgmx?qgid='+qgid+'&zt=80';
	if($("#purchaseAuditViewForm #fwbj").val()=="/ws"){
		url=$("#purchaseAuditViewForm #urlPrefix").val()+$("#purchaseAuditViewForm #fwbj").val()+'/production/purchase/pagedataQgmxList?qgid='+qgid+'&zt=80';
	}
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#purchaseAuditViewForm #tb_list").bootstrapTable({
			url:  url  ,     //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#purchaseAuditViewForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            paginationDetailHAlign:' hidden',//隐藏分页
            sortable: false,                     //是否启用排序
            sortName: "qgmx.xh",				//排序字段
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 100,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: false,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "qgmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [/*{
            	width: '3%',
                checkbox: true
            },*/{
                field: 'xh',
                title: '序号',
                width: '10%',
                align: 'right',
                formatter:xhformat,
                sortable: true,
                visible: true
            },{
                field: wlmcfield(),
                title: wlmctitle(),
                width: '30%',
                align: 'right',
                formatter:purchaseAuditView_wlmcformat,
                sortable: true,
                visible: true
            },{
                field: 'sl',
                title: '数量',
                width: '30%',
                align: 'right',
                sortable: true,
                visible: true
            },{
                field: 'jg',
                title: '价格',
                width: '30%',
                align: 'right',
                formatter:jgformat,
                sortable: true,
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
		})
	};
	$("#purchaseAuditViewForm #tb_list").colResizable({
        liveDrag:true, 
        gripInnerHtml:"<div class='grip'></div>", 
        draggingClass:"dragging", 
        resizeMode:'fit', 
        postbackSafe:true,
        partialRefresh:true        
  });
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
	    return getShoppingViewSearchData(map);
	};
	return oTableInit;
}

function purchaseAuditView_wlmcformat(value,row,index){
	if(qglbdm=="MATERIAL" || qglbdm=="" || qglbdm==null){
		return row.wlmc;
	}else{
		return row.hwmc;
	}
}

function wlmctitle(){
	if(qglbdm=="DEVICE"){
		return "设备名称";
	}else if(qglbdm=="SERVICE"){
		return "服务名称";
	}else{
		return "物料名称";
	}
}

function wlmcfield(value,row,index){
	if(qglbdm=="DEVICE"){
		return "hwmc";
	}else if(qglbdm=="SERVICE"){
		return "hwmc";
	}else{
		return "wlmc";
	}
}

function jgformat(value,row,index){
	if(row.jg != null && row.jg != ""){
		var numpart=String(row.jg).split(".");//将数字通过jq split用小数点分隔为数组对象
		numpart[0]=numpart[0].replace(new RegExp('(\\d)(?=(\\d{3})+$)','ig'),"$1,");
		return numpart.join(".");
	}else{
		return "0.00";
	}
}

function getShoppingViewSearchData(map){
	var cxtj=$("#purchaseAuditViewForm #cxtj").val();
	var cxnr=$.trim(jQuery('#purchaseAuditViewForm #cxnr').val());
	if(cxtj=="0"){
		map["wlbm"]=cxnr;
	}else if(cxtj=="1"){
		map["wlmc"]=cxnr;
	}else if(cxtj=="2"){
		map["scs"]=cxnr;
	}else if(cxtj=="3"){
		map["ychh"]=cxnr;
	}
	return map;
}

function searchShoppingViewResult(isTurnBack){
	if(isTurnBack){
		$('#purchaseAuditViewForm #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#purchaseAuditViewForm #tb_list').bootstrapTable('refresh');
	}
}

function xhformat(value,row,index){
	return index+1;
}

function ztformat(value,row,index){
	var html="";
	if(row.zt=='15'){
		html="<span style='color:red;'>拒绝审批</span>";
	}else if(row.zt=='30'){
		html="<span style='color:orange;'>取消采购</span>";
	}else if(row.zt=='80'){
		html="<span style='color:green;'>正常采购</span>";
	}
	return html;
}

$(function(){
	//添加日期控件
	laydate.render({
	   elem: '#purchaseAuditViewForm #sqrq'
	  ,theme: '#2381E9'
	});
	var oTable=new PurchaseViewInfo_TableInit();
	oTable.Init();

	
	//2.初始化Button的点击事件
//    var oButtonInit = new Partner_oButtton();
//    	oButtonInit.Init();
	jQuery('#purchaseAuditViewForm .chosen-select').chosen({width: '100%'});
})