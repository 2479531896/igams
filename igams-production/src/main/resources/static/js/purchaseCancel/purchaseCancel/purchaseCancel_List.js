var RemovePurchase_turnOff=true;

var RemovePurchase_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#removePurchase_formSearch #removePurchase_list").bootstrapTable({
			url: $("#removePurchase_formSearch #urlPrefix").val()+'/purchaseCancel/purchaseCancel/pageGetListPurchaseCancel',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#removePurchase_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "qgmx.lrsj",				//排序字段
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
            uniqueId: "qgqxmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            },{
            	title: '序',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序',
				width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'qgqxid',
                title: '请购取消id',
                width: '8%',
                align: 'left',
                visible:false
            },{
                field: 'djh',
                title: '请购单号',
                width: '8%',
                align: 'left',
                visible:true
            },{
                field: 'wlbm',
                title: '物料编码',
                width: '8%',
                align: 'left',
                sortable: true,   
                visible: true
            },{
                field: 'wlmc',
                title: '物料名称',
                width: '10%',
                align: 'left',
                formatter:removePurchaseListformat,
                sortable: true,   
                visible: true
            },{
                field: 'qglbmc',
                title: '类别',
                width: '5%',
                align: 'left',
                formatter:removePurchaseListqglbmcformat,
                sortable: true,   
                visible: true
            },{
                field: 'wllbmc',
                title: '物料类别',
                width: '10%',
                align: 'left',
                sortable: true,   
                visible: false
            },{
                field: 'wlzlbmc',
                title: '物料子类别',
                width: '10%',
                align: 'left',
                sortable: true, 
                visible: false
            },{
                field: 'wlzlbmc',
                title: '物料子类别名称',
                width: '10%',
                align: 'left',
                sortable: true, 
                visible: false
            },{
                field: 'gg',
                title: '规格',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'ychh',
                title: '原厂货号',
                width: '6%',
                align: 'left',
                visible: true
            },{
				field: 'qxsl',
				title: '取消数量',
				width: '6%',
				align: 'left',
				visible: true
			},{
				field: 'bz',
				title: '备注',
				width: '12%',
				align: 'left',
                sortable: true, 
                visible: true
			},{
				field: 'qgqxsj',
				title: '取消日期',
				width: '12%',
				align: 'left',
                sortable: true, 
                visible: true
			},{
				field: 'qgqxry',
				title: '取消人员',
				width: '12%',
				align: 'left',
                sortable: true, 
                visible: true
			},{
                field: 'zt',
                title: '状态',
                width: '10%',
                align: 'left',
                formatter:qxqgztFormat,
                visible: true
            },{
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '7%',
				align: 'left',
				formatter:czFormat,
				visible: true
			}],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	RemovePurchaseDealById(row.qgqxid,'view',$("#removePurchase_formSearch #btn_view").attr("tourl"));
             },
		});
		$("#removePurchase_formSearch #removePurchase_list").colResizable({
			liveDrag:true, 
			gripInnerHtml:"<div class='grip'></div>", 
			draggingClass:"dragging", 
			resizeMode:'fit', 
			postbackSafe:true,
			partialRefresh:true
		})
	}
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "qgqxmx.qgqxmxid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return removePurchaseSearchData(map);
	}
	return oTableInit
}

function removePurchaseListqglbmcformat(value,row,index){
	if(row.qglbdm=='MATERIAL' || row.qglbdm==null || row.qglbdm==''){
		return "物料";
	}else{
		return row.qglbmc;
	}
}

function removePurchaseListformat(value,row,index){
	if(row.qglbdm=='MATERIAL' || row.qglbdm==null || row.qglbdm==''){
		return row.wlmc;
	}
	return row.hwmc;
}
/**
 * 取消请购列表的状态格式化函数
 * @returns
 */
function qxqgztFormat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgqxid + "\",event,\"AUDIT_REQUISITIONS_CANCEL\",{prefix:\"" + $('#removePurchase_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {
    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgqxid + "\",event,\"AUDIT_REQUISITIONS_CANCEL\",{prefix:\"" + $('#removePurchase_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
    }else{
    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgqxid + "\",event,\"AUDIT_REQUISITIONS_CANCEL\",{prefix:\"" + $('#removePurchase_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
	if (row.zt == '10' && row.scbj =='0' ) {
		return "<span class='btn btn-warning' onclick=\"recallRequisitions('" + row.qgqxid + "',event)\" >撤回</span>";
	}else{
		return "";
	}
}

//撤回请购单提交
function recallRequisitions(qgqxid,event){
	var auditType = $("#removePurchase_formSearch #auditType").val();
	var msg = '您确定要撤回该取消请购单吗？';
	var purchase_params = [];
	purchase_params.prefix = $("#removePurchase_formSearch #urlPrefix").val();
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,qgqxid,function(){
				searchRemovePurchaseResult(true);
			},purchase_params);
		}
	});
}

function removePurchaseSearchData(map){
	var cxtj=$("#removePurchase_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#removePurchase_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr;
	}else if(cxtj=="1"){
		map["djh"]=cxnr;
	}else if(cxtj=="2"){
		map["wlbm"]=cxnr;
	}else if(cxtj=="3"){
		map["wlmc"]=cxnr;
	}else if(cxtj=="4"){
		map["wllbmc"]=cxnr;
	}else if (cxtj == "5") {
    	map["wlzlbmc"] = cxnr;
	}else if (cxtj == "6") {
    	map["sqbmmc"] = cxnr;
	}
	return map;
}


//提供给导出用的回调函数
function removePurchaseDate(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="qgmx.qxqgsj";
	map["sortLastOrder"]="desc";
	map["sortName"]="qgqxmx.lrsj";
	map["sortOrder"]="desc";
	return removePurchaseSearchData(map);
}

var RemovePurchase_ButtonInit= function (){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#removePurchase_formSearch #btn_query");
		var btn_view=$("#removePurchase_formSearch #btn_view");
		var btn_del=$("#removePurchase_formSearch #btn_del");
		var btn_searchexport = $("#removePurchase_formSearch #btn_searchexport");
    	var btn_selectexport = $("#removePurchase_formSearch #btn_selectexport");
    	var btn_submit=$("#removePurchase_formSearch #btn_submit");
		
    	/*--------------------------------提交取消请购信息---------------------------*/
		btn_submit.unbind("click").click(function(){
			var sel_row=$('#removePurchase_formSearch #removePurchase_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
    				RemovePurchaseDealById(sel_row[0].qgqxid,"submit",btn_submit.attr("tourl"));
    			}else{
    				$.alert("该状态不允许提交!");
    			}
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	 /* ------------------------------删除-----------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#removePurchase_formSearch #removePurchase_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length == 1){
    			if(sel_row[0].zt!="80"){
    				$.alert("审核未通过不允许删除!");
    			}else{
    				$.confirm('您确定要删除所选择的记录吗？',function(result){
    	    			if(result){
    	    				RemovePurchaseDealById(sel_row[0].qgqxid, "del", btn_del.attr("tourl"));
    		    			}
    					});
    				}		
		    	}else{
		    		$.error("请选中一行");
	    		}
	    	});
		/*--------------------------------模糊查询---------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchRemovePurchaseResult(true); 
    		});
		}
	   	   

		 /* ------------------------------查看取消请购信息-----------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row=$('#removePurchase_formSearch #removePurchase_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			RemovePurchaseDealById(sel_row[0].qgqxid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	 // 	  ---------------------------导出-----------------------------------
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#removePurchase_formSearch #removePurchase_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].qgqxmxid;
        		}
        		ids = ids.substr(1);
        		$.showDialog($('#removePurchase_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=PURCHASECANCEL_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});

    	btn_searchexport.unbind("click").click(function(){
    		$.showDialog($('#removePurchase_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=PURCHASECANCEL_SEARCH&expType=search&callbackJs=removePurchaseDate"
    				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});	

	}
	return oInit;
}

function RemovePurchaseDealById(id,action,tourl){
	var url= tourl;
	if(!tourl){
		return;
	}
	tourl = $("#removePurchase_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl+"?qgqxid="+id;
		$.showDialog(url,'查看请购取消信息',viewPurchaseConfig);
	}else if(action =='del'){
		var url= tourl+"?qgqxid="+id;
		$.showDialog(url,'删除请购取消信息',delPurchaseConfig);
	}else if(action =='submit'){
		var url= tourl+"?qgqxid="+id;
		$.showDialog(url,'提交请购取消信息',submitPurchaseConfig);
	}
}

var submitPurchaseConfig = {
		width		: "1500px",
		modalName	: "submitPurchaseModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "提交",
				className : "btn-success",
				callback : function() {
					if(!$("#purchaseCancelForm").valid()){
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#purchaseCancelForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"purchaseCancelForm",function(responseText,statusText){
		
						if(responseText["status"] == 'success'){
							//提交审核
							var auditType = $("#purchaseCancelForm #auditType").val();
							var purchase_params=[];
							purchase_params.prefix=$('#purchaseCancelForm #urlPrefix').val();
							var ywid = responseText["ywid"];
							showAuditFlowDialog(auditType,ywid,function(){
								$.closeModal(opts.modalName);
								searchRemovePurchaseResult(true);
							},null,purchase_params);
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						}else{
							preventResubmitForm(".modal-footer > button", false);
							$.alert(responseText["message"],function() {
							});
						}
					},".modal-footer > button");
					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};


/**
 * 查看页面模态框
 */
var viewPurchaseConfig={
	width		: "1600px",
	modalName	:"viewPurchaseModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}

var delPurchaseConfig = {
	width		: "1600px",
	modalName	: "modPurchaseModal",
	formName	: "PurchaseCancel_del_Form",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#PurchaseCancel_del_Form input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"PurchaseCancel_del_Form",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchRemovePurchaseResult();
							}
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				},".modal-footer > button");
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};    
/**
 * 列表刷新
 * @param isTurnBack
 * @returns
 */
function searchRemovePurchaseResult(isTurnBack){
	if(isTurnBack){
		$('#removePurchase_formSearch #removePurchase_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#removePurchase_formSearch #removePurchase_list').bootstrapTable('refresh');
	}
}

$(function(){
	 // 1.初始化Table
	var oTable = new RemovePurchase_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new RemovePurchase_ButtonInit();
    oButtonInit.Init();
    jQuery('#removePurchase_formSearch .chosen-select').chosen({width: '100%'});
})