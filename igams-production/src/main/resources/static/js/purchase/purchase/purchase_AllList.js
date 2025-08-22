var Purchase_turnOff=true;

var PurchaseAll_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#purchaseAll_formSearch #purchase_list").bootstrapTable({
			url: $("#purchaseAll_formSearch #urlPrefix").val()+'/purchase/purchase/pageGetListAllPurchase',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#purchaseAll_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "qggl.lrsj",				//排序字段
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
            uniqueId: "qgid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            },{
            	title: '序号',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'djh',
                title: '请购单号',
                width: '10%',
                align: 'left',
                sortable: true,   
                visible: true
            },{
                field: 'qglx',
                title: '请购类型',
                width: '4%',
                align: 'left',
                formatter:qglxAllFormat,
                sortable: true,   
                visible: false
            },{
                field: 'qglbmc',
                title: '类别',
                width: '4%',
                align: 'left',
                formatter:qglbAllformat,
                sortable: true,   
                visible: true
            },{
                field: 'sqrmc',
                title: '申请人',
                width: '5%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'sqbmmc',
                title: '申请部门',
                width: '10%',
                align: 'left',
                sortable: true,   
                visible: true
            },{
                field: 'jlbh',
                title: '记录编号',
                width: '6%',
                align: 'left',
                sortable: true,   
                visible: false
            },{
                field: 'sqrq',
                title: '申请日期',
                width: '7%',
                align: 'left',
                sortable: true,   
                visible: false
            },{
                field: 'shsj',
                title: '审核时间',
                width: '7%',
                align: 'left',
                sortable: true,   
                visible: true
            },{
                field: 'xmbmmc',
                title: '项目编码',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xmdlmc',
                title: '项目大类',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqly',
                title: '申请理由',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'zffsmc',
                title: '支付方式',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'fkfmc',
                title: '付款方',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'bz',
                title: '备注',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'lrsj',
                title: '录入时间',
                width: '7%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'lrrymc',
                title: '录入人员',
                width: '7%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'zt',
                title: '状态',
                width: '10%',
                align: 'left',
                formatter:qgztAllFormat,
                sortable: true,
                visible: true
            },{
				field: 'rkzt',
				title: '入库状态',
				width: '6%',
				align: 'left',
				formatter:qgrkztFormat,
				visible: true
			},{
				field: 'wcbj',
				title: '是否完成',
				width: '8%',
				align: 'left',
				formatter:wbztAllformat,
				sortable: true,
				visible: true
			},{
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '7%',
				align: 'left',
				formatter:czAllFormat,
				visible: true
			}],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	PurchaseAllDealById(row.qgid,'view',$("#purchaseAll_formSearch #btn_view").attr("tourl"));
             },
		});
		$("#purchaseAll_formSearch #purchase_list").colResizable({
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
            sortLastName: "qggl.qgid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return purchaseAllSearchData(map);
	}
	return oTableInit
}

function qglbAllformat(value,row,index){
    if(row.qglbmc==null || row.qglbmc==''){
        return "物料";
    }
    return row.qglbmc;
}
function qglxAllFormat(value,row,index){
    if(row.qglx=='1'){
        return "OA请购";
    }else{
        return "普通请购";
    }
}

function wbztAllformat(value,row,index){
	if(row.wcbj==1){
		var html="<span style='color:green;'>"+"已完成"+"</span>";
	}else{
		var html="<span style='color:red;'>"+"未完成"+"</span>";
	}
	return html;
}

/**
 * 物料列表的状态格式化函数
 * @returns
 */
function qgztAllFormat(value,row,index) {
	if(row.qglx=='1'){
		if (row.zt == '00') {
	        return '未提交';
	    }else if (row.zt == '80') {		
	    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONSTWO\",{prefix:\"" + $('#purchaseAll_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
	    }else if (row.zt == '15') {
	    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONSTWO\",{prefix:\"" + $('#purchaseAll_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
	    }else{
	    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONSTWO\" ,{prefix:\"" + $('#purchaseAll_formSearch #urlPrefix').val() + "\"})'>" + row.shxx_dqgwmc + "审核中</a>";
	    }
	}else{
		if(row.qglbdm=='MATERIAL' || row.qglbdm==null || row.qglbdm==''){
			if (row.zt == '00') {
		        return '未提交';
		    }else if (row.zt == '80') {		
		    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS\",{prefix:\"" + $('#purchaseAll_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
		    }else if (row.zt == '15') {
		    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS\",{prefix:\"" + $('#purchaseAll_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
		    }else{
		    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS\" ,{prefix:\"" + $('#purchaseAll_formSearch #urlPrefix').val() + "\"})'>" + row.shxx_dqgwmc + "审核中</a>";
		    }
		}else if(row.qglbdm=='ADMINISTRATION'){
			if (row.zt == '00') {
		        return '未提交';
		    }else if (row.zt == '80') {		
		    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS_ADMINISTRATION\",{prefix:\"" + $('#purchaseAll_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
		    }else if (row.zt == '15') {
		    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS_ADMINISTRATION\",{prefix:\"" + $('#purchaseAll_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
		    }else{
		    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS_ADMINISTRATION\" ,{prefix:\"" + $('#purchaseAll_formSearch #urlPrefix').val() + "\"})'>" + row.shxx_dqgwmc + "审核中</a>";
		    }
		}else if(row.qglbdm=='SERVICE'){
			if (row.zt == '00') {
		        return '未提交';
		    }else if (row.zt == '80') {		
		    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS_SERVICE\",{prefix:\"" + $('#purchaseAll_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
		    }else if (row.zt == '15') {
		    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS_SERVICE\",{prefix:\"" + $('#purchaseAll_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
		    }else{
		    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS_SERVICE\" ,{prefix:\"" + $('#purchaseAll_formSearch #urlPrefix').val() + "\"})'>" + row.shxx_dqgwmc + "审核中</a>";
		    }
		}else if(row.qglbdm=='DEVICE'){
			if (row.zt == '00') {
		        return '未提交';
		    }else if (row.zt == '80') {		
		    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS_DEVICE\",{prefix:\"" + $('#purchaseAll_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
		    }else if (row.zt == '15') {
		    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS_DEVICE\",{prefix:\"" + $('#purchaseAll_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
		    }else{
		    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS_DEVICE\" ,{prefix:\"" + $('#purchaseAll_formSearch #urlPrefix').val() + "\"})'>" + row.shxx_dqgwmc + "审核中</a>";
		    }
		}else if(row.qglbdm=='OUTSOURCE'){
            if (row.zt == '00') {
                return '未提交';
            }else if (row.zt == '80') {
                return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS\",{prefix:\"" + $('#purchaseAll_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
            }else if (row.zt == '15') {
                return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS\",{prefix:\"" + $('#purchaseAll_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
            }else{
                return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS\" ,{prefix:\"" + $('#purchaseAll_formSearch #urlPrefix').val() + "\"})'>" + row.shxx_dqgwmc + "审核中</a>";
            }
        }
	}   
}
/**
 * 请购入库状态初始化
 */
function qgrkztFormat(value,row,index) {
	if(row.rkzt=="01"){
		var html="<span style='color:red;'>"+"未入库"+"</span>";
	}else if(row.rkzt=="02"){
		var html="<span style='color:#f0ad4e;'>"+"部分入库"+"</span>";
	}else if (row.rkzt=="03"){
		var html="<span style='color:green;'>"+"全部入库"+"</span>";
	}
	return html;

}
/**
 * 操作按钮格式化函数
 * @returns
 */
function czAllFormat(value,row,index) {
	if (row.zt == '10' && row.scbj =='0' ) {
        return "<span class='btn btn-warning' onclick=\"recallRequisitions('" + row.qgid +"','" + row.shlx+ "',event)\" >撤回</span>";
	}else{
		return "";
	}
}

//撤回请购单提交
function recallRequisitions(qgid,shlx,event){
    var auditType = null;
    if(shlx){
        auditType = shlx;
    }else{
        auditType = $("#purchaseAll_formSearch #auditType").val();
    }
	var msg = '您确定要撤回该请购单吗？';
	var purchase_params = [];
	purchase_params.prefix = $("#purchaseAll_formSearch #urlPrefix").val();
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,qgid,function(){
				searchPurchaseAllResult();
			},purchase_params);
		}
	});
}


function purchaseAllSearchData(map){
	var cxtj=$("#purchaseAll_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#purchaseAll_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["djh"]=cxnr
	}else if(cxtj=="1"){
		map["sqrmc"]=cxnr
	}else if(cxtj=="2"){
		map["sqbmmc"]=cxnr
	}else if(cxtj=="3"){
		map["jlbh"]=cxnr
	}else if(cxtj=="4"){
		map["xmbmmc"]=cxnr
	}else if(cxtj=="5"){
		map["xmdlmc"]=cxnr
	}else if(cxtj=="6"){
		map["sqly"]=cxnr
	}else if(cxtj=="7"){
		map["gdzcbh"]=cxnr
	}else if(cxtj=="8"){
		map["xlh"]=cxnr
	}else if(cxtj=="9"){
		map["sbbh"]=cxnr
	}else if(cxtj=="10"){
		map["sbccbh"]=cxnr
	}
	
	// 支付方式
	var zffss = jQuery('#purchaseAll_formSearch #zffs_id_tj').val();
	map["zffss"] = zffss;
	// 付款方
	var fkfs = jQuery('#purchaseAll_formSearch #fkf_id_tj').val();
	map["fkfs"] = fkfs;
	// 项目编码
	var xmbms = jQuery('#purchaseAll_formSearch #xmbm_id_tj').val();
	map["xmbms"] = xmbms;
	// 项目大类
	var xmdls = jQuery('#purchaseAll_formSearch #xmdl_id_tj').val();
	map["xmdls"] = xmdls;
	// 状态
	var zts = jQuery('#purchaseAll_formSearch #zt_id_tj').val();
	map["zts"] = zts;
	// 申请开始日期
	var sqrqstart = jQuery('#purchaseAll_formSearch #sqrqstart').val();
	map["sqrqstart"] = sqrqstart;
	// 申请结束日期
	var sqrqend = jQuery('#purchaseAll_formSearch #sqrqend').val();
	map["sqrqend"] = sqrqend;
	
	// 完成标记
	var wcbj = jQuery('#purchaseAll_formSearch #wcbj_id_tj').val();
	map["wcbj"] = wcbj;
    
	//请购类别
	var qglbs = jQuery('#purchaseAll_formSearch #qglb_id_tj').val();
	map["qglbs"] = qglbs;

	// 请购类型
	var qglx = jQuery('#purchaseAll_formSearch #qglx_id_tj').val();
	map["qglx"] = qglx;
	// 入库状态多
	var rkzts = jQuery('#purchaseAll_formSearch #rkzt_id_tj').val();
	map["rkzts"] = rkzts;
	map["flg"]="purchase";
	return map;
}
//提供给导出用的回调函数
function PurSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="qggl.qgid";
	map["sortLastOrder"]="desc";
	map["sortName"]="qggl.lrsj";
	map["sortOrder"]="desc";
	return purchaseAllSearchData(map);
}

var PurchaseAll_ButtonInit= function (){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#purchaseAll_formSearch #btn_query");
		var btn_add=$("#purchaseAll_formSearch #btn_add");
		var btn_submit=$("#purchaseAll_formSearch #btn_submit");
		var btn_advancedmod=$("#purchaseAll_formSearch #btn_advancedmod");
		var btn_view=$("#purchaseAll_formSearch #btn_view");
		var btn_del = $("#purchaseAll_formSearch #btn_del");
		var btn_copy = $("#purchaseAll_formSearch #btn_copy");
		var btn_download= $("#purchaseAll_formSearch #btn_download");
		var xmdlBind = $("#purchaseAll_formSearch #xmdl_id ul li a");
		var btn_discard = $("#purchaseAll_formSearch #btn_discard");
	 	var btn_selectexport = $("#purchaseAll_formSearch #btn_selectexport");
		var btn_searchexport = $("#purchaseAll_formSearch #btn_searchexport");
		var btn_cancelrequisition = $("#purchaseAll_formSearch #btn_cancelrequisition");
        var btn_generatepurchase=$("#purchaseAll_formSearch #btn_generatepurchase");
    	var btn_viewCommon = $("#purchaseAll_formSearch #btn_viewCommon");
    	var btn_print = $("#purchaseAll_formSearch #btn_print");
		var btn_auditingsearchexport = $("#purchaseAll_formSearch #btn_auditingsearchexport");
		var btn_auditingselectexport = $("#purchaseAll_formSearch #btn_auditingselectexport");
		var btn_cgauditsearchexport = $("#purchaseAll_formSearch #btn_cgauditsearchexport");
		var btn_cgauditselectexport = $("#purchaseAll_formSearch #btn_cgauditselectexport");
        //添加日期控件
    	laydate.render({
    	   elem: '#purchaseAll_formSearch #sqrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#purchaseAll_formSearch #sqrqend'
    	  ,theme: '#2381E9'
    	});
		/*--------------------------------模糊查询---------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchPurchaseAllResult(true); 
    		});
		}
        /*-----------------------------------生成合同----------------------------------*/
        btn_generatepurchase.unbind().click(function(){
            var sel_row = $('#purchaseAll_formSearch #purchase_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
            	PurchaseAllDealById(sel_row[0].qgid, "generatepurchase", btn_generatepurchase.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        })
	   	   // 	  ---------------------------导出-----------------------------------
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#purchaseAll_formSearch #purchase_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].qgid;
        		}
        		ids = ids.substr(1);
        		$.showDialog($("#purchaseAll_formSearch #urlPrefix").val()+exportPrepareUrl+"?ywid=QGGL_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});

		btn_searchexport.unbind("click").click(function(){
			$.showDialog($('#purchaseAll_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=QGGL_SEARCH&expType=search&callbackJs=PurSearchData"
				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
		});
    	
		/*--------------------------------新增请购信息---------------------------*/
		btn_add.unbind("click").click(function(){
			PurchaseAllDealById(null,"add",btn_add.attr("tourl"));
    	});
		/*--------------------------------修改请购信息---------------------------*/
		btn_submit.unbind("click").click(function(){
			var sel_row=$('#purchaseAll_formSearch #purchase_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
    				PurchaseAllDealById(sel_row[0].qgid,"submit",btn_submit.attr("tourl"));
    			}else{
    				$.alert("该状态不允许提交!");
    			}
    		}else{
    			$.error("请选中一行");
    		}
    	});
		// 	  ---------------------------审核导出-----------------------------------
		btn_auditingselectexport.unbind("click").click(function(){
			var sel_row = $('#purchaseAll_formSearch #purchase_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].qgid;
				}
				ids = ids.substr(1);
				$.showDialog($('#purchaseAll_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=QGGL_AUDITINGSELECT&expType=select&ids="+ids
					,btn_auditingselectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
			}else{
				$.error("请选择数据");
			}
		});

		btn_auditingsearchexport.unbind("click").click(function(){
			$.showDialog($('#purchaseAll_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=QGGL_AUDITINGSEARCH&expType=search&callbackJs=PurSearchData"
				,btn_auditingsearchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
		});
		// 	  ---------------------------采购审核导出-----------------------------------
		btn_cgauditselectexport.unbind("click").click(function(){
			var sel_row = $('#purchaseAll_formSearch #purchase_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].qgid;
				}
				ids = ids.substr(1);
				$.showDialog($('#purchaseAll_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=QGGL_CGAUDITINGSELECT&expType=select&ids="+ids
					,btn_cgauditselectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
			}else{
				$.error("请选择数据");
			}
		});

		btn_cgauditsearchexport.unbind("click").click(function(){
			$.showDialog($('#purchaseAll_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=QGGL_CGAUDITINGSEARCH&expType=search&callbackJs=PurSearchData"
				,btn_cgauditsearchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
		});
		/*--------------------------------高级修改请购信息(审核通过调用) ---------------------------*/
		btn_advancedmod.unbind("click").click(function(){
			var sel_row=$('#purchaseAll_formSearch #purchase_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			PurchaseAllDealById(sel_row[0].qgid,"advancedmod",btn_advancedmod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/*--------------------------------复制请购信息---------------------------*/
		btn_copy.unbind("click").click(function(){
			var sel_row=$('#purchaseAll_formSearch #purchase_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			PurchaseAllDealById(sel_row[0].qgid,"copy",btn_copy.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/*--------------------------------下载请购附件---------------------------*/
		btn_download.unbind("click").click(function(){
			var sel_row=$('#purchaseAll_formSearch #purchase_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			PurchaseAllDealById(sel_row[0].qgid,"download",btn_download.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		 /* ------------------------------查看请购信息-----------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row=$('#purchaseAll_formSearch #purchase_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			PurchaseAllDealById(sel_row[0].qgid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/* ------------------------------删除请购信息-----------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#purchaseAll_formSearch #purchase_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else {
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].qgid;
        		}
    			ids=ids.substr(1);
    			$.confirm('您确定要删除所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= $("#purchaseAll_formSearch #urlPrefix").val()+btn_del.attr("tourl");
        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								searchPurchaseAllResult();
        							});
        						}else if(responseText["status"] == "fail"){
        							$.error(responseText["message"],function() {
        							});
        						} else{
        							$.alert(responseText["message"],function() {
        							});
        						}
        					},1);
        					
        				},'json');
        				jQuery.ajaxSetup({async:true});
        			}
        		});
    		}
    	});
        /*-------------------------------废弃------------------------------*/
    	btn_discard.unbind("click").click(function(){
    		var sel_row = $('#purchaseAll_formSearch #purchase_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else {
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].qgid;
	    			if(sel_row[i].zt !='00' && sel_row[i].zt !='15'){
	    				$.error("该物料正在审核中或已审核，不允许删除");
	    				return;
	    			}
        		}
    			ids=ids.substr(1);
    			$.confirm('您确定要删除所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= $("#purchaseAll_formSearch #urlPrefix").val()+btn_discard.attr("tourl");
        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								searchPurchaseAllResult();
        							});
        						}else if(responseText["status"] == "fail"){
        							$.error(responseText["message"],function() {
        							});
        						} else{
        							$.alert(responseText["message"],function() {
        							});
        						}
        					},1);
        					
        				},'json');
        				jQuery.ajaxSetup({async:true});
        			}
        		});
    		}
    	});
    	
    	/*--------------------------------请购取消---------------------------*/
		btn_cancelrequisition.unbind("click").click(function(){
			var sel_row=$('#purchaseAll_formSearch #purchase_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			if(sel_row[0].zt=="80"){
					if (sel_row[0].qglbdm=="MATERIAL"||sel_row[0].qglbdm=="DEVICE") {
						PurchaseAllDealById(sel_row[0].qgid,"cancelrequisition",btn_cancelrequisition.attr("tourl"));
					}
				else {
						$.alert("只有请购类型为物料或设备允许取消请购!");
					}
    			}else{
    				$.alert("该状态不允许取消请购!");
    			}
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/* ---------------------------共通 查看列表-----------------------------------*/
    	btn_viewCommon.unbind("click").click(function(){
    		var sel_row = $('#purchaseAll_formSearch #purchase_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			PurchaseAllDealById(sel_row[0].qgid,"viewCommon",btn_viewCommon.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/*-----------------------显示隐藏------------------------------------*/
    	$("#purchaseAll_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(Purchase_turnOff){
    			$("#purchaseAll_formSearch #searchMore").slideDown("low");
    			Purchase_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#purchaseAll_formSearch #searchMore").slideUp("low");
    			Purchase_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    		//showMore();
    	});
		/* --------------------------- 打印 -----------------------------------*/
		btn_print.unbind("click").click(function(){
			var sel_row = $('#purchaseAll_formSearch #purchase_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length ==0){
				$.error("请选中一行");
				return;
			}
			var ids="";
			var qglbdm = sel_row[0].qglbdm=='OUTSOURCE'?'MATERIAL':sel_row[0].qglbdm;
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
				if (sel_row[i].zt!='80'){
					$.error("只允许打印审核通过的单子！");
					return;
				}
				ids= ids + ","+ sel_row[i].qgid;
				if (qglbdm!=(sel_row[i].qglbdm=='OUTSOURCE'?'MATERIAL':sel_row[i].qglbdm)){
					$.error("请选择请购类别一致的数据！");
					return;
				}
			}
			ids=ids.substr(1);
			var url=$('#purchaseAll_formSearch #urlPrefix').val()+btn_print.attr("tourl")+"?ids="+ids+"&qglbdm="+qglbdm+"&access_token="+$("#ac_tk").val();
			window.open(url);
		});
    	/*-------------------------项目编码的显示----------------------------------*/
    	if(xmdlBind!=null){
    		xmdlBind.on("click",function(){
    			setTimeout(function(){
    				getAllxmbm();
    			},10);
    		});
    	}
	}
	return oInit;
}
/**
 * 根据项目大类获取项目编码
 * @returns
 */
function getAllxmbm(){
	var xmdl=$("#purchaseAll_formSearch #xmdl_id_tj").val();
	if(!isEmpty(xmdl)){
		$("#purchaseAll_formSearch #xmbm_id").removeClass("hidden");
	}else{
		$("#purchaseAll_formSearch #xmbm_id").addClass("hidden");
	}
	if(!isEmpty(xmdl)){
		var url = $("#purchaseAll_formSearch #urlPrefix").val()+"/systemmain/data/ansyGetJcsjListInStop";
		$.ajax({
			type:'post',
			url:url,
			data:{"fcsids":xmdl,"access_token":$("#ac_tk").val()},
			dataType:'JSON',
			success:function(data){
				if(data.length>0){
					var html="";
					$.each(data,function(i){
						html +="<li>";
						html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('xmbm','" + data[i].csid + "','purchaseAll_formSearch');\" id=\"xmbm_id_" + data[i].csid + "\"><span>" + data[i].csmc + "</span></a>";
						html +="</li>"
					});
					$("#purchaseAll_formSearch #ul_xmbm").html(html);
				}else{
					$("#purchaseAll_formSearch #ul_xmbm").empty();
				}
				$("#purchaseAll_formSearch #xmbm_id_tj").val("");
			}
		});
	}else{
		$("#purchaseAll_formSearch [id^='xmbm_li_']").remove();
		$("#purchaseAll_formSearch #xmbm_id_tj").val("");
	}
}
function PurchaseAllDealById(id,action,tourl){
	var url= tourl;
	if(!tourl){
		return;
	}
	tourl = $("#purchaseAll_formSearch #urlPrefix").val()+tourl;
	if(action =='add'){
		var url=tourl;
        $.showDialog(url,'选择请购类型',chancePurchaseTypeConfig);
	}else if(action =='submit'){
		var url= tourl+"?qgid="+id;
		$.showDialog(url,'提交采购信息',submitPurchaseAllConfig);
	}else if(action =='view'){
		var url= tourl+"?qgid="+id;
		$.showDialog(url,'查看请购信息',viewPurchaseAllConfig);
	}else if(action =='copy'){
		var url= tourl+"?qgid="+id;
		$.showDialog(url,'复制请购信息',copyPurchaseAllConfig);
	}else if(action=="advancedmod"){
		var url=tourl+"?qgid="+id;
		$.showDialog(url,'高级修改',advanceModPurchaseAllConfig);
	}else if(action=="download"){
		var url=tourl+"?qgid="+id;
		$.showDialog(url,'附件下载',downloadPurchaseAllConfig);
	}else if(action=="cancelrequisition"){
		var url=tourl+"?qgid="+id;
		$.showDialog(url,'取消请购',cancelRequisitionAllConfig);
	}else if(action =='generatepurchase'){
        var url= tourl + "?qgid=" +id;
        $.showDialog(url,'生成申购单',generatePurchaseAllConfig);
	}else if(action=="viewCommon"){
		var url=tourl+"?qgid="+id;
		$.showDialog(url,'请购详细信息 共通',viewPurchaseAllConfig);
	}
}
var generatePurchaseAllConfig = {
        width        : "800px",
        offAtOnce    : true,  //当数据提交成功，立刻关闭窗口
        buttons        : {
            cancel : {
                label : "关 闭",
                className : "btn-default"
            }
        } 
    };

var chancePurchaseTypeConfig={
        width        : "600px",
        offAtOnce    : true,  //当数据提交成功，立刻关闭窗口
        modalName    : "chancePurchaseTypeModal",
        buttons        : {
            cancel : {
                label : "关 闭",
                className : "btn-default"
            }
        }     
};

var cancelRequisitionAllConfig = {
		width		: "1500px",
		modalName	: "purchaseCancelModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "提交",
				className : "btn-primary",
				callback : function() {
					if(!$("#purchaseCancelForm").valid()){
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#purchaseCancelForm input[name='access_token']").val($("#ac_tk").val());

					submitForm(opts["formName"]||"purchaseCancelForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								var purchaseCancel_params=[];
								purchaseCancel_params.prefix=$('#purchaseAll_formSearch #urlPrefix').val();
								var auditType = $("#purchaseCancelForm #auditType").val();
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								//提交审核
								showAuditFlowDialog(auditType,responseText["ywid"],function(){
									searchPurchaseAllResult();
								},null,purchaseCancel_params);
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
							preventResubmitForm(".modal-footer > button", false);
							$.alert(responseText["message"],function() {
							});
						}
					},".modal-footer > button");
					return false;
				}
			},
			successtwo : {
				label : "保存",
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
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
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

var addPurchaseAllConfig = {
		width		: "1500px",
		modalName	: "addPurchaseModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "提交",
				className : "btn-primary",
				callback : function() {
					if(!$("#shoppingCarForm").valid()){
						$.alert("所选信息有误！");
						return false;
					}
					if($("#shoppingCarForm #qglbdm").val() == "MATERIAL"){
						let jgdm= $("#shoppingCarForm #sqbmdm").val()
						if(!jgdm){
							$.alert("所选部门存在异常！");
							return false;
						}
					}

					var nowDate=new Date();//当前系统时间
					var $this = this;
					var opts = $this["options"]||{};
					var json=[];
					var data=$('#shoppingCarForm #tb_list').bootstrapTable('getData');//获取选择行数据
					var xmbmdm=$("#shoppingCarForm #xmbm option:selected").attr("csdm");
					var xmdldm=$("#shoppingCarForm #xmdl option:selected").attr("csdm");
					var xmmc=$("#shoppingCarForm #xmbm option:selected").attr("csmc");
					$("#shoppingCarForm #xmbmdm").val(xmbmdm);
					$("#shoppingCarForm #xmdldm").val(xmdldm);
					$("#shoppingCarForm #xmmc").val(xmmc);
					for(var i=0;i<data.length;i++){
                        var sz={"index":'',"wlid":'',"sl":'',"qwrq":'',"bz":'',"wlbm":'',"fjids":'',"wlzlbtc":'',"hwmc":'',"hwjldw":'',"jg":'',"cpzch":''};
						sz.index=i;
						sz.wlid=data[i].wlid;
						if($("#sl_"+i).val()==null || $("#sl_"+i).val()==''){
							$.confirm("第"+(i+1)+"行货物数量不能为空!");
							return false;
						}else{
							sz.sl=$("#sl_"+i).val();
						}
						if($("#shoppingCarForm #qglbdm").val()=='ADMINISTRATION'){
							if($("#hwmc_"+i).val()==null || $("#hwmc_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物名称不能为空!");
								return false;
							}else{
								sz.hwmc=$("#hwmc_"+i).val();
							}
							if($("#hwbz_"+i).val()==null || $("#hwbz_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物规格型号不能为空!");
								return false;
							}else{
								sz.hwbz=$("#hwbz_"+i).val();
							}
							if($("#hwjldw_"+i).val()==null || $("#hwjldw_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物计量单位不能为空!");
								return false;
							}else{
								sz.hwjldw=$("#hwjldw_"+i).val();
							}
							if(nowDate<new Date($("#qwrq_"+i).val())){
								sz.qwrq=$("#qwrq_"+i).val();
							}
						}else if($("#shoppingCarForm #qglbdm").val()=='SERVICE'){
							sz.hwmc=$("#hwmc_"+i).val();
							sz.hwbz=$("#hwbz_"+i).val();
							if($("#gys_"+i).val()==null || $("#gys_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物供应商不能为空!");
								return false;
							}else{
								sz.gys=$("#gys_"+i).val();
							}
						}else{
							if(nowDate<new Date($("#qwrq_"+i).val())){
								sz.qwrq=$("#qwrq_"+i).val();
							}
						}
                        sz.fjbj=data[i].fjbj;
                        sz.cskz1=data[i].cskz1;
                        sz.hwyt=$("#hwyt_"+i).val();    
                        sz.yq=$("#yq_"+i).val();
                        sz.wbyq=$("#wbyq_"+i).val();
                        sz.pzyq=$("#pzyq_"+i).val();
						sz.wlbm=data[i].wlbm;
						sz.bz=$("#bz_"+i).val();
						sz.fjids=$("#fj_"+i+" input").val();
						sz.wlzlbtc=data[i].wlzlbtc;
						sz.cpzch=data[i].cpzch;
						json.push(sz);
					}
					$("#shoppingCarForm #qgmx_json").val(JSON.stringify(json));
					
					$("#shoppingCarForm input[name='access_token']").val($("#ac_tk").val());

					submitForm(opts["formName"]||"shoppingCarForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							var auditType = $("#shoppingCarForm #auditType").val();
							var purchase_params=[];
							purchase_params.prefix=$('#shoppingCarForm #urlPrefix').val();
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								//提交审核
								showAuditFlowDialog(auditType,responseText["ywid"],function(){
									searchPurchaseAllResult();
								},null,purchase_params);
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
			successtwo : {
				label : "保 存",
				className : "btn-success",
				callback : function() {
					if(!$("#shoppingCarForm").valid()){
						$.alert("所选信息有误！");
						return false;
					}
					if($("#shoppingCarForm #qglbdm").val() == "MATERIAL"){
						let jgdm= $("#shoppingCarForm #sqbmdm").val()
						if(!jgdm){
							$.alert("所选部门存在异常！");
							return false;
						}
					}
					var nowDate=new Date();//当前系统时间
					var $this = this;
					var opts = $this["options"]||{};
					var json=[];
					var data=$('#shoppingCarForm #tb_list').bootstrapTable('getData');//获取选择行数据
					var xmbmdm=$("#shoppingCarForm #xmbm option:selected").attr("csdm");
					var xmdldm=$("#shoppingCarForm #xmdl option:selected").attr("csdm");
					var xmmc=$("#shoppingCarForm #xmbm option:selected").attr("csmc");
					$("#shoppingCarForm #xmbmdm").val(xmbmdm);
					$("#shoppingCarForm #xmdldm").val(xmdldm);
					$("#shoppingCarForm #xmmc").val(xmmc);
					$("#shoppingCarForm #bcbj").val("1");//保存标记，用于跳过后台验证
					for(var i=0;i<data.length;i++){
                        var sz={"index":'',"wlid":'',"sl":'',"qwrq":'',"bz":'',"wlbm":'',"fjids":'',"hwmc":'',"hwjldw":'',"jg":'',"fwqx":'',"hwyt":'',"fwyq":'',"cpzch":''};
						sz.index=i;
						sz.wlid=data[i].wlid;
						if($("#sl_"+i).val()==null || $("#sl_"+i).val()==''){
                            $.confirm("第"+(i+1)+"行货物数量不能为空!");
							return false;
						}else{
							sz.sl=$("#sl_"+i).val();
						}
                        if($("#shoppingCarForm #qglbdm").val()=='ADMINISTRATION'){
                            if($("#hwmc_"+i).val()==null || $("#hwmc_"+i).val()==''){
                                $.confirm("第"+(i+1)+"行货物名称不能为空!");
                                return false;
                            }else{
                                sz.hwmc=$("#hwmc_"+i).val();
                            }
                            if($("#hwbz_"+i).val()==null || $("#hwbz_"+i).val()==''){
                                $.confirm("第"+(i+1)+"行货物规格型号不能为空!");
                                return false;
                            }else{
                                sz.hwbz=$("#hwbz_"+i).val();
                            }
                            if($("#hwjldw_"+i).val()==null || $("#hwjldw_"+i).val()==''){
                                $.confirm("第"+(i+1)+"行货物计量单位不能为空!");
                                return false;
                            }else{
                                sz.hwjldw=$("#hwjldw_"+i).val();
                            }
                            if($("#jg_"+i).val()==null || $("#jg_"+i).val()==''){
                                $.confirm("第"+(i+1)+"行货物价格不能为空!");
                                return false;
                            }else{
                                sz.jg=$("#jg_"+i).val();
                            }
                        }else if($("#shoppingCarForm #qglbdm").val()=='SERVICE'){
                            sz.hwmc=$("#hwmc_"+i).val();
                            sz.fwqx=$("#hwbz_"+i).val();
                            sz.hwyt=$("#hwyt_"+i).val();    
                            sz.fwyq=$("#fwyq_"+i).val();
                            if($("#gys_"+i).val()==null || $("#gys_"+i).val()==''){
                                $.confirm("第"+(i+1)+"行货物供应商不能为空!");
                                return false;
                            }else{
                                sz.gys=$("#gys_"+i).val();
                            }
                        }else {
                        	if(nowDate<new Date($("#qwrq_"+i).val())){
  				              sz.qwrq=$("#qwrq_"+i).val();
  				            }
  						}
                        sz.fjbj=data[i].fjbj;
                        sz.hwyt=$("#hwyt_"+i).val();    
                        sz.yq=$("#yq_"+i).val();
                        sz.wbyq=$("#wbyq_"+i).val();
                        sz.pzyq=$("#pzyq_"+i).val();
						sz.wlbm=data[i].wlbm;
						sz.cpzch=data[i].cpzch;
						sz.bz=$("#bz_"+i).val();
						sz.fjids=$("#fj_"+i+" input").val();
						json.push(sz);
					}
					$("#shoppingCarForm #qgmx_json").val(JSON.stringify(json));
					
					$("#shoppingCarForm input[name='access_token']").val($("#ac_tk").val());

					submitForm(opts["formName"]||"shoppingCarForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchPurchaseAllResult();
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
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

var submitPurchaseAllConfig = {
		width		: "1500px",
		modalName	: "submitPurchaseModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "提交",
				className : "btn-success",
				callback : function() {
					if(!$("#shoppingCarForm").valid()){
						$.alert("所选信息有误！");
						return false;
					}
					if($("#shoppingCarForm #qglbdm").val() == "MATERIAL"){
						let jgdm= $("#shoppingCarForm #sqbmdm").val()
						if(!jgdm){
							$.alert("所选部门存在异常！");
							return false;
						}
					}
					var $this = this;
					var opts = $this["options"]||{};
					var json=[];
					var data=$('#shoppingCarForm #tb_list').bootstrapTable('getData');//获取选择行数据
					var xmbmdm=$("#shoppingCarForm #xmbm option:selected").attr("csdm");
					var xmdldm=$("#shoppingCarForm #xmdl option:selected").attr("csdm");
					var xmmc=$("#shoppingCarForm #xmbm option:selected").attr("csmc");
					$("#shoppingCarForm #xmbmdm").val(xmbmdm);
					$("#shoppingCarForm #xmdldm").val(xmdldm);
					$("#shoppingCarForm #xmmc").val(xmmc);
					for(var i=0;i<data.length;i++){
						var sz={"index":'',"qgmxid":'',"wlid":'',"sl":'',"qwrq":'',"bz":'',"wlbm":'',"qxqg":'',"glid":'',"wlzlbtc":'',"fjids":'',"zt":'',"cpzch":''};
						sz.index=i;
						sz.qgmxid=data[i].qgmxid;
						sz.wlid=data[i].wlid;
						if($("#sl_"+i).val()==null || $("#sl_"+i).val()==''){
							$.confirm("第"+(i+1)+"行货物数量不能为空!");
							return false;
						}else{
							sz.sl=$("#sl_"+i).val();
						}
						if($("#shoppingCarForm #qglbdm").val()=='ADMINISTRATION'){
							if($("#hwmc_"+i).val()==null || $("#hwmc_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物名称不能为空!");
								return false;
							}else{
								sz.hwmc=$("#hwmc_"+i).val();
							}
							if($("#hwbz_"+i).val()==null || $("#hwbz_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物规格型号不能为空!");
								return false;
							}else{
								sz.hwbz=$("#hwbz_"+i).val();
							}
							if($("#hwjldw_"+i).val()==null || $("#hwjldw_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物计量单位不能为空!");
								return false;
							}else{
								sz.hwjldw=$("#hwjldw_"+i).val();
							}
						}else if ($("#shoppingCarForm #qglbdm").val()=='SERVICE'){
							sz.hwmc=$("#hwmc_"+i).val();
							sz.hwbz=$("#hwbz_"+i).val();
							if($("#gys_"+i).val()==null || $("#gys_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物供应商不能为空!");
								return false;
							}else{
								sz.gys=$("#gys_"+i).val();
							}
						}
                        sz.fjbj=data[i].fjbj;
                        sz.cskz1=data[i].cskz1;
                        sz.hwyt=$("#hwyt_"+i).val();	
						sz.yq=$("#yq_"+i).val();
						sz.wbyq=$("#wbyq_"+i).val();
						sz.pzyq=$("#pzyq_"+i).val();
						sz.wlbm=data[i].wlbm;
			            sz.qwrq=$("#qwrq_"+i).val();
						sz.bz=$("#bz_"+i).val();
						sz.qxqg=$("#qxqg_"+i).val();
						sz.fjids=$("#fj_"+i+" input").val();
						sz.glid=data[i].glid;
						sz.wlzlbtc=data[i].wlzlbtc;
						sz.cpzch=data[i].cpzch;
						sz.zt=$("#mxzt_"+i).attr("cskz");
						json.push(sz);
					}
					$("#shoppingCarForm #qgmx_json").val(JSON.stringify(json));
					
					$("#shoppingCarForm input[name='access_token']").val($("#ac_tk").val());
					
					submitForm(opts["formName"]||"shoppingCarForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							//提交审核
							var purchase_params=[];
							purchase_params.prefix=$('#shoppingCarForm #urlPrefix').val();
							var auditType = $("#shoppingCarForm #auditType").val();
							var ywid = $("#shoppingCarForm #qgid").val();
							showAuditFlowDialog(auditType,ywid,function(){
								$.closeModal(opts.modalName);
								searchPurchaseAllResult();
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
	
var advanceModPurchaseAllConfig={
		width		: "1500px",
		modalName	: "advanceModPurchaseModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "保存",
				className : "btn-success",
				callback : function() {
					if(!$("#shoppingCarForm").valid()){
						$.alert("所选信息有误！");
						return false;
					}
					if($("#shoppingCarForm #qglbdm").val() == "MATERIAL"){
						let jgdm= $("#shoppingCarForm #sqbmdm").val()
						if(!jgdm){
							$.alert("所选部门存在异常！");
							return false;
						}
					}
					var $this = this;
					var opts = $this["options"]||{};
					var json=[];
					var data=$('#shoppingCarForm #tb_list').bootstrapTable('getData');//获取选择行数据
					var xmbmdm=$("#shoppingCarForm #xmbm option:selected").attr("csdm");
					var xmdldm=$("#shoppingCarForm #xmdl option:selected").attr("csdm");
					var xmmc=$("#shoppingCarForm #xmbm option:selected").attr("csmc");
					$("#shoppingCarForm #xmbmdm").val(xmbmdm);
					$("#shoppingCarForm #xmdldm").val(xmdldm);
					$("#shoppingCarForm #xmmc").val(xmmc);
					for(var i=0;i<data.length;i++){
						var sz={"index":'',"qgmxid":'',"wlid":'',"sl":'',"qwrq":'',"bz":'',"wlbm":'',"qxqg":'',"glid":'',"wlzlbtc":'',"cpzch":''};
						sz.index=i;
						sz.qgmxid=data[i].qgmxid;
						sz.wlid=data[i].wlid;
						if($("#sl_"+i).val()==null || $("#sl_"+i).val()==''){
							$.confirm("第"+(i+1)+"行货物数量不能为空!");
							return false;
						}else{
							sz.sl=$("#sl_"+i).val();
						}
						if($("#shoppingCarForm #qglbdm").val()=='ADMINISTRATION'){
							if($("#hwmc_"+i).val()==null || $("#hwmc_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物名称不能为空!");
								return false;
							}else{
								sz.hwmc=$("#hwmc_"+i).val();
							}
							if($("#hwbz_"+i).val()==null || $("#hwbz_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物规格型号不能为空!");
								return false;
							}else{
								sz.hwbz=$("#hwbz_"+i).val();
							}
							if($("#hwjldw_"+i).val()==null || $("#hwjldw_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物计量单位不能为空!");
								return false;
							}else{
								sz.hwjldw=$("#hwjldw_"+i).val();
							}
						}else if($("#shoppingCarForm #qglbdm").val()=='SERVICE'){
							sz.hwmc=$("#hwmc_"+i).val();
							sz.hwbz=$("#hwbz_"+i).val();
							if($("#gys_"+i).val()==null || $("#gys_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物供应商不能为空!");
								return false;
							}else{
								sz.gys=$("#gys_"+i).val();
							}
						}
						sz.fjbj=data[i].fjbj;
						sz.hwyt=$("#hwyt_"+i).val();
						sz.yq=$("#yq_"+i).val();
						sz.wbyq=$("#wbyq_"+i).val();
						sz.pzyq=$("#pzyq_"+i).val();
						sz.sjjl=$("#sjjl_"+i).val();
						sz.wlbm=data[i].wlbm;
						sz.cskz1=data[i].cskz1;
						sz.qwrq=$("#qwrq_"+i).val();
						sz.bz=$("#bz_"+i).val();
						sz.qxqg=$("#qxqg_"+i).val();
						sz.jg=$("#jg_"+i).val();
						sz.fjids=$("#fj_"+i+" input").val();
						sz.glid=data[i].glid;
						sz.wlzlbtc=data[i].wlzlbtc;
						sz.cpzch=data[i].cpzch;
						json.push(sz);
					}

					$("#shoppingCarForm #qgmx_json").val(JSON.stringify(json));
					
					$("#shoppingCarForm input[name='access_token']").val($("#ac_tk").val());

					submitForm(opts["formName"]||"shoppingCarForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchPurchaseAllResult();
							});
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
	
var copyPurchaseAllConfig = {
		width		: "1500px",
		modalName	: "copyPurchaseModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "提交",
				className : "btn-primary",
				callback : function() {
					if(!$("#shoppingCarForm").valid()){
						return false;
					}
					var nowDate=new Date();//当前系统时间
					var $this = this;
					var opts = $this["options"]||{};
					var json=[];
					var data=$('#shoppingCarForm #tb_list').bootstrapTable('getData');//获取选择行数据
					var xmbmdm=$("#shoppingCarForm #xmbm option:selected").attr("csdm");
					var xmdldm=$("#shoppingCarForm #xmdl option:selected").attr("csdm");
					var xmmc=$("#shoppingCarForm #xmbm option:selected").attr("csmc");
					$("#shoppingCarForm #xmbmdm").val(xmbmdm);
					$("#shoppingCarForm #xmdldm").val(xmdldm);
					$("#shoppingCarForm #xmmc").val(xmmc);
					for(var i=0;i<data.length;i++){
						var sz={"index":'',"wlid":'',"sl":'',"qwrq":'',"bz":'',"wlbm":'',"fjids":'',"wlzlbtc":'',"qxqg":'',"zt":'',"cpzch":''};
						sz.index=i;
						sz.wlid=data[i].wlid;
						if($("#sl_"+i).val()==null || $("#sl_"+i).val()==''){
							$.confirm("第"+(i+1)+"行货物数量不能为空!");
							return false;
						}else{
							sz.sl=$("#sl_"+i).val();
						}
						if($("#shoppingCarForm #qglbdm").val()=='ADMINISTRATION'){
							if($("#hwmc_"+i).val()==null || $("#hwmc_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物名称不能为空!");
								return false;
							}else{
								sz.hwmc=$("#hwmc_"+i).val();
							}
							if($("#hwbz_"+i).val()==null || $("#hwbz_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物规格型号不能为空!");
								return false;
							}else{
								sz.hwbz=$("#hwbz_"+i).val();
							}
							if($("#hwjldw_"+i).val()==null || $("#hwjldw_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物计量单位不能为空!");
								return false;
							}else{
								sz.hwjldw=$("#hwjldw_"+i).val();
							}
						}else if($("#shoppingCarForm #qglbdm").val()=='SERVICE'){
							sz.hwmc=$("#hwmc_"+i).val();
							sz.hwbz=$("#hwbz_"+i).val();
							if($("#gys_"+i).val()==null || $("#gys_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物供应商不能为空!");
								return false;
							}else{
								sz.gys=$("#gys_"+i).val();
							}
						}else{
							if(nowDate<new Date($("#qwrq_"+i).val())){
								sz.qwrq=$("#qwrq_"+i).val();
							}
						}
						sz.fjbj=data[i].fjbj;
						sz.wlbm=data[i].wlbm;
						sz.cskz1=data[i].cskz1;
						sz.hwyt=$("#hwyt_"+i).val();
						sz.yq=$("#yq_"+i).val();
						sz.wbyq=$("#wbyq_"+i).val();
						sz.pzyq=$("#pzyq_"+i).val();
						sz.sjjl=$("#sjjl_"+i).val();
						sz.bz=$("#bz_"+i).val();
						sz.fjids=$("#fj_"+i+" input").val();
						sz.wlzlbtc=data[i].wlzlbtc;
						sz.cpzch=data[i].cpzch;
						sz.qxqg=$("#qxqg_"+i).val();
						sz.zt=$("#mxzt_"+i).attr("cskz");
						json.push(sz);
					}
					$("#shoppingCarForm #qgmx_json").val(JSON.stringify(json));
					
					$("#shoppingCarForm input[name='access_token']").val($("#ac_tk").val());

					submitForm(opts["formName"]||"shoppingCarForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							var purchase_params=[];
							purchase_params.prefix=$('#shoppingCarForm #urlPrefix').val();
							var auditType = $("#shoppingCarForm #auditType").val();
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								//提交审核
								showAuditFlowDialog(auditType,responseText["ywid"],function(){
									searchPurchaseAllResult();
								},null,purchase_params);
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
			successtwo : {
				label : "保 存",
				className : "btn-success",
				callback : function() {
					if(!$("#shoppingCarForm").valid()){
						return false;
					}
					var nowDate=new Date();//当前系统时间
					var $this = this;
					var opts = $this["options"]||{};
					var json=[];
					var data=$('#shoppingCarForm #tb_list').bootstrapTable('getData');//获取选择行数据
					var xmbmdm=$("#shoppingCarForm #xmbm option:selected").attr("csdm");
					var xmdldm=$("#shoppingCarForm #xmdl option:selected").attr("csdm");
					var xmmc=$("#shoppingCarForm #xmbm option:selected").attr("csmc");
					$("#shoppingCarForm #xmbmdm").val(xmbmdm);
					$("#shoppingCarForm #xmdldm").val(xmdldm);
					$("#shoppingCarForm #xmmc").val(xmmc);
					$("#shoppingCarForm #bcbj").val("1");//保存标记，用于跳过后台验证
					for(var i=0;i<data.length;i++){
						var sz={"index":'',"wlid":'',"sl":'',"qwrq":'',"bz":'',"wlbm":'',"fjids":'',"qxqg":'',"zt":'',"cpzch":''};
						sz.index=i;
						sz.wlid=data[i].wlid;
						if($("#sl_"+i).val()==null || $("#sl_"+i).val()==''){
							$.confirm("第"+(i+1)+"行货物数量不能为空!");
							return false;
						}else{
							sz.sl=$("#sl_"+i).val();
						}
						if($("#shoppingCarForm #qglbdm").val()=='ADMINISTRATION'){
							if($("#hwmc_"+i).val()==null || $("#hwmc_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物名称不能为空!");
								return false;
							}else{
								sz.hwmc=$("#hwmc_"+i).val();
							}
							if($("#hwbz_"+i).val()==null || $("#hwbz_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物规格型号不能为空!");
								return false;
							}else{
								sz.hwbz=$("#hwbz_"+i).val();
							}
							if($("#hwjldw_"+i).val()==null || $("#hwjldw_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物计量单位不能为空!");
								return false;
							}else{
								sz.hwjldw=$("#hwjldw_"+i).val();
							}
						}else if($("#shoppingCarForm #qglbdm").val()=='SERVICE'){
							sz.hwmc=$("#hwmc_"+i).val();
							sz.hwbz=$("#hwbz_"+i).val();
							if($("#gys_"+i).val()==null || $("#gys_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物供应商不能为空!");
								return false;
							}else{
								sz.gys=$("#gys_"+i).val();
							}
						}else{
							if(nowDate<new Date($("#qwrq_"+i).val())){
								sz.qwrq=$("#qwrq_"+i).val();
							}
						}
						sz.fjbj=data[i].fjbj;
						sz.wlbm=data[i].wlbm;
						sz.cpzch=data[i].cpzch;
						sz.hwyt=$("#hwyt_"+i).val();
						sz.yq=$("#yq_"+i).val();
						sz.wbyq=$("#wbyq_"+i).val();
						sz.pzyq=$("#pzyq_"+i).val();
						sz.sjjl=$("#sjjl_"+i).val();
						sz.bz=$("#bz_"+i).val();
						sz.fjids=$("#fj_"+i+" input").val();
						sz.qxqg=$("#qxqg_"+i).val();
						sz.zt=$("#mxzt_"+i).attr("cskz");
						json.push(sz);
					}
					$("#shoppingCarForm #qgmx_json").val(JSON.stringify(json));
					
					$("#shoppingCarForm input[name='access_token']").val($("#ac_tk").val());

					submitForm(opts["formName"]||"shoppingCarForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchPurchaseAllResult();
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
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

var downloadPurchaseAllConfig = {
		width		: "1500px",
		modalName	: "downloadPurchaseModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "下 载",
				className : "btn-primary",
				callback : function() {
					if(!$("#downloadpurchase_formSearch").valid()){
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					var wlzlbtcs=$("#downloadpurchase_formSearch #wlzlbtc_id_tj").val();
					var ywlx=$("#downloadpurchase_formSearch #ywlx").val();
					var qgid=$("#downloadpurchase_formSearch #qgid").val();
					var sel_row = $('#downloadpurchase_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
					if(sel_row.length>0){
		    			var ids="";
		    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
		        			ids= ids + ","+ sel_row[i].qgmxid;
		        		}
		    		}
					var map = {
			    			access_token:$("#ac_tk").val(),
			    			wlzlbtcs:wlzlbtcs,
			    			ywlx:ywlx,
			    			qgid:qgid,
			    			ids:ids,
			    		}; 
					var url= $("#downloadpurchase_formSearch #urlPrefix").val() + $("#downloadpurchase_formSearch #action").val();
					$.post(url,map,function(data){
						if(data){
							if(data.status == 'success'){
								if(data.count==0 || !data.count){
									$.confirm("未查询到附件!");
									return false;
								}
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								//创建objectNode
								var cardiv =document.createElement("div");
								cardiv.id="cardiv";
								var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span id="totalCount" style="color:red;font-weight:600;">/' + data.count + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
								cardiv.innerHTML=s_str;
								//将上面创建的P元素加入到BODY的尾部
								document.body.appendChild(cardiv);
								setTimeout("checkReportAllZipStatus(2000,'"+ data.redisKey + "')",1000);
								//绑定导出取消按钮事件
								$("#exportCancel").click(function(){
									//先移除导出提示，然后请求后台
									if($("#cardiv").length>0) $("#cardiv").remove();
									$.ajax({
										type : "POST",
										url : $('#purchase_formAudit #urlPrefix').val()+"/common/export/commCancelExport",
										data : {"wjid" : data.redisKey+"","access_token":$("#ac_tk").val()},
										dataType : "json",
										success:function(res){
											if(res != null && res.result==false){
												if(res.msg && res.msg!="")
													$.error(res.msg);
											}

										}
									});
								});
							}else if(data.status == 'fail'){
								$.error(data.error,function() {
								});
							}else{
								if(data.error){
									$.alert(data.error,function() {
									});
								}
							}
						}
					},'json');
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
var viewPurchaseAllConfig={
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

//自动检查报告压缩进度
var checkReportAllZipStatus = function(intervalTime,redisKey){
	$.ajax({
		type : "POST",
		url : $('#purchaseAll_formSearch #urlPrefix').val()+"/common/export/commCheckExport",
		data : {"wjid" : redisKey +"","access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.cancel){//取消导出则直接return
				return;
			}
			if(data.result == false){
				if(data.msg && data.msg!=""){
					$.error(data.msg);
					if($("#cardiv")) $("#cardiv").remove();
				}else{
					if(intervalTime < 5000)
						intervalTime = intervalTime + 1000;
					if($("#exportCount")){
						$("#exportCount").html(data.currentCount)
						if(("/"+data.currentCount) == $("#totalCount").text()){
							$("#totalCount").html($("#totalCount").text()+" 压缩中....")
						}
					}
					setTimeout("checkReportAllZipStatus("+intervalTime+",'"+ redisKey +"')",intervalTime);
				}
			}else{
				if(data.msg && data.msg!=""){
					$.error(data.msg);
					if($("#cardiv")) $("#cardiv").remove();
				}
				else{
					if($("#cardiv")) $("#cardiv").remove();
					window.open($('#purchaseAll_formSearch #urlPrefix').val()+"/common/export/commDownloadExport?wjid="+redisKey + "&access_token="+$("#ac_tk").val());
				}
			}
		}
	});
}

/**
 * 列表刷新
 * @param isTurnBack
 * @returns
 */
function searchPurchaseAllResult(isTurnBack){
	//关闭高级搜索条件
	$("#purchaseAll_formSearch #searchMore").slideUp("low");
	Purchase_turnOff=true;
	$("#purchaseAll_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#purchaseAll_formSearch #purchase_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#purchaseAll_formSearch #purchase_list').bootstrapTable('refresh');
	}
}

runEvery = function () {
    $.ajax({
        "dataType" : 'json',
        "type" : "POST",
        "url" : $("#purchaseAll_formSearch #urlPrefix").val()+"/purchase/purchase/pagedataQueryOverTime",
        "data" : { "access_token" : $("#ac_tk").val()},
        "success" : function(data) {
            $("#jjNum").text(data.overdueString);
            $("#csNum").text(data.overString);
            let timeoutID = setTimeout( runEvery, 1000 * 600 );
        }
    });
}

window.onbeforeunload = function(){
    try{
        clearTimeout(timeoutID);
    }catch(error){
    };
}

function queryCs(flag){
    var title = "即将超时的请购单";
    if("cs"==flag){
        title = "已超时的请购单";
    }
    $.showDialog($("#purchaseAll_formSearch #urlPrefix").val()+"/purchase/purchase/pagedataQueryOverTable?flg="+flag+"&access_token="+$("#ac_tk").val(),title,viewOverTimeConfig);
}
var viewOverTimeConfig = {
	width		: "1500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

$(function(){
	 // 1.初始化Table
	var oTable = new PurchaseAll_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new PurchaseAll_ButtonInit();
    oButtonInit.Init();
    runEvery();
    jQuery('#purchaseAll_formSearch .chosen-select').chosen({width: '100%'});
})