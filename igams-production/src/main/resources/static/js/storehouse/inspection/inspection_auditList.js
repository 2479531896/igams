var inspection_AuditListInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#inspection_audit_formSearch #inspection_audit_list').bootstrapTable({
            url: $('#inspection_formAudit #urlPrefix').val()+'/inspectionGoods/inspectionGoods/pageGetListInspectionAudit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#inspection_formAudit #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "jyrq",				//排序字段
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
			sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
			pageSize: 15,                       // 每页的记录行数（*）
			pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
			paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "dhjyid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
            	
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
				inspectionById(row.dhjyid,'view',$("#inspection_audit_formSearch #btn_view").attr("tourl"));
            },
        });
    };
    // 得到查询的参数
	oTableInit.queryParams = function(params){
		var map = {
				pageSize: params.limit,   // 页面大小
		    	pageNumber: (params.offset / params.limit) + 1,  // 页码
				access_token:$("#ac_tk").val(),
				sortName: params.sort,      // 排序列名
		        sortOrder: params.order, // 排位命令（desc，asc）
		        sortLastName: "dhjyid", // 防止同名排位用
		        sortLastOrder: "desc", // 防止同名排位用
				zt:'10',
				dqshzt:'dsh'
		};
		var cxtj=$("#inspection_audit_formSearch #cxtj").val();
		var cxnr=$.trim(jQuery('#inspection_audit_formSearch #cxnr').val());
		if(cxtj=="0"){
			map["jydh"]=cxnr
		}
		return map;
	};
    return oTableInit;
};
var inspectionAuditedHistory_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#inspection_audit_audited #tb_list').bootstrapTable({
            url: $('#inspection_formAudit #urlPrefix').val()+'/inspectionGoods/inspectionGoods/pageGetListInspectionAudit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#inspection_audit_audited #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "jyrq",				//排序字段
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
			sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
			pageSize: 15,                       // 每页的记录行数（*）
			pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
			paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "shxx_shxxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsAuditedArray,
            onLoadSuccess: function (map) {
            	
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	return;
            }
        });
        $("#inspection_audit_audited #tb_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true        
	  });
    };
    // 得到查询的参数
	oTableInit.queryParams = function(params){
		var map = {
				pageSize: params.limit,   // 页面大小
		    	pageNumber: (params.offset / params.limit) + 1,  // 页码
				access_token:$("#ac_tk").val(),
				sortName: params.sort,      // 排序列名
		        sortOrder: params.order, // 排位命令（desc，asc）
		        sortLastName: "dhjyid", // 防止同名排位用
		        sortLastOrder: "desc", // 防止同名排位用
		};
		var cxtj=$("#inspection_audit_audited #cxtj").val();
		var cxnr=$.trim(jQuery('#inspection_audit_audited #cxnr').val());
		if(cxtj=="0"){
			map["jydh"]=cxnr
		}
		map["dqshzt"] = 'ysh';
		return map;
	};
    return oTableInit;
};

var columnsArray = [{
	checkbox: true,
	width: '1%'
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
	field: 'jydh',
	title: '检验单号',
	width: '8%',
	align: 'left',
	sortable: true,
	visible: true
}, {				
	field: 'jyrq',
	title: '检验日期',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: true
}, {				
	field: 'jyfzrmc',
	title: '检验负责人',
	width: '8%',
	align: 'left',
	sortable: true,
	visible: true
}, {				
	field: 'bz',
	title: '备注',
	width: '15%',
	align: 'left',
	visible: true
},{				
	field: 'sqrxm',
	title: '申请人员',
	width: '6%',
	align: 'left',
	visible: true
}, {				
	field: 'shxx_sqsj',
	title: '申请时间',
	width: '10%',
	align: 'left',
	visible: true,
	sortable: true
}, {
	field: 'shxx_shyj',
	title: '审核意见',
	width: '15%',
	align: 'left',
	visible: true
},{
	field: 'dhjyid',
	title: '到货检验id',
	width: '10%',
	align: 'left',
	visible: false
},{
	field: 'shlb',
	title: '审核类别',
	width: '10%',
	align: 'left',
	visible: false
}];
var columnsAuditedArray = [{
	checkbox: true,
	width: '1%'
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
	field: 'jydh',
	title: '检验单号',
	width: '8%',
	align: 'left',
	sortable: true,
	visible: true
}, {				
	field: 'jyrq',
	title: '检验日期',
	width: '8%',
	align: 'left',
	sortable: true,
	visible: true
}, {				
	field: 'jyfzrmc',
	title: '检验负责人',
	width: '8%',
	align: 'left',
	sortable: true,
	visible: true
}, {				
	field: 'bz',
	title: '备注',
	width: '8%',
	align: 'left',
	visible: true
},{				
	field: 'sqrxm',
	title: '申请人员',
	width: '8%',
	align: 'left',
	visible: true
}, {				
	field: 'shxx_sqsj',
	title: '申请时间',
	width: '8%',
	align: 'left',
	visible: true
},{				
	field: 'dhjyid',
	title: '申请时间',
	width: '8%',
	align: 'left',
	visible: false
},{
    field: 'shxx_lrryxm',
    title: '审核人',
    width: '8%',
    align: 'left',
    sortable: true,
    visible: true
},{
    field: 'shxx_shyj',
    title: '审核意见',
    width: '8%',
    align: 'left',
    sortable: true,
    visible: true
},{
    field: 'shxx_shsj',
    title: '审核时间',
    width: '8%',
    align: 'left',
    visible: true,
	sortable: true
},{				
	field: 'shxx_sftgmc',
	title: '是否通过',
	width: '8%',
	align: 'left',
	visible: true
},{
    field: 'shxx_shxxid',
    align: 'center',
    title: '审核信息ID',
    visible: false
}];
var inspectionAudit_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_view = $("#inspection_audit_formSearch #btn_view");
		var btn_query = $("#inspection_audit_formSearch #btn_query");//查询
		var btn_queryAudited = $("#inspection_audit_audited #btn_query");//审核记录查询
		var btn_audit = $("#inspection_audit_formSearch #btn_audit");
		var btn_cancelAudit = $("#inspection_audit_audited #btn_cancelAudit");//取消审核
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			inspectionAuditingResult(true);
    		});
    	};
    	if(btn_queryAudited != null){
    		btn_queryAudited.unbind("click").click(function(){
    			inspectionAuditedResult(true);
    		});
    	};
    	/* ---------------------------查看列表-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#inspection_audit_formSearch #inspection_audit_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			inspection_audit_DealById(sel_row[0].dhjyid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_audit.unbind("click").click(function(){
    		var sel_row = $('#inspection_audit_formSearch #inspection_audit_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			inspection_audit_DealById(sel_row[0].dhjyid,"audit",btn_audit.attr("tourl"),sel_row[0].shlb);
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*---------------------------查看审核记录--------------------------------*/
    	//选项卡切换事件回调
    	$('#inspection_formAudit #inspection_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
    		var _hash = e.target.hash.replace("#",'');
    		if(!e.target.isLoaded){//只调用一次
    			if(_hash=='inspection_auditing'){
    				var oTable= new inspection_AuditListInit();
    				oTable.Init();
    				
    			}else{
    				var oTable= new inspectionAuditedHistory_TableInit();
    				oTable.Init();
    			}
    			e.target.isLoaded = true;
    		}else{//重新加载
    			//$(_gridId + ' #tb_list').bootstrapTable('refresh');
    		}
    	}).first().trigger('shown.bs.tab');//触发第一个选中事件
    	/*-------------------------------------取消审核-----------------------------------*/
    	if(btn_cancelAudit!=null){
    		btn_cancelAudit.unbind("click");
    		btn_cancelAudit.click(function(){
    			var purchase_params=[];
    			purchase_params.prefix=$('#inspection_formAudit #urlPrefix').val();
    			cancelAudit($('#inspection_audit_audited #tb_list').bootstrapTable('getSelections'),function(){
    				inspectionAuditedResult();
    			},null,purchase_params);
    		})
    	}
    }
    return oInit;
}
function inspection_audit_DealById(id,action,tourl,auditType){
	if(!tourl){
		return;
	}
	tourl = $('#inspection_formAudit #urlPrefix').val() + tourl; 
	if(action=="view"){
		var url=tourl+"?dhjyid="+id;
		$.showDialog(url,'查看信息',viewInspection_Config);
	}else if(action=="audit"){
		tourl=tourl+"?auditType="+auditType;
		showAuditDialogWithLoading({
			id: id,
			type: auditType,
			url:tourl,
			data:{'ywzd':'dhjyid'},
			title:"到货检验审核",
			preSubmitCheck:'preSubmitInspection',
			prefix:$('#inspection_formAudit #urlPrefix').val(),
			callback:function(){
				inspectionAuditingResult(true);//回调
			},
			dialogParam:{width:1500}
		});
	}
}
//审核提交前校验
function preSubmitInspection(){
	//拼接hwxx列表提交的数据
	var allTableData = $('#inspectionModForm #dhjyhwList').bootstrapTable('getData'); 
	if(!allTableData.length>0){
		$.error("不存在待检验的货物信息，无法提交！");
		return;
	}
	var hwxxlist = [];
	var sffqlld = $("#inspectionModForm #sffqlld").val();
	var wlbms = "";
    $.each(allTableData,function(i,v){
    	 var wlbm = v.wlbm;
    	 var sl = v.sl;
         var hwid = v.hwid;
         var qyl = $("#inspectionModForm #qyl"+i).val();
         var qyrq =$("#inspectionModForm #qyrq"+i).val();
         var zjthsl =$("#inspectionModForm #zjthsl"+i).val();
         var bgdh = $("#bgdh_"+i).val();
         var hwfj = $("#hwfj_"+i).val().split(",");
         //判断是否需要废弃领料单
         if("1"==sffqlld){
        	 //校验不合格数量
        	 if(parseFloat(sl)<parseFloat(zjthsl)){
        		 wlbms = wlbms + "," + wlbm;
        	 }
         }else{
        	//校验不合格数量
        	 if(parseFloat(sl)<parseFloat(zjthsl)+parseFloat(qyl)){
        		 wlbms = wlbms + "," + wlbm;
        	 }
         }
         var hwxx = {
        		 hwid:v.hwid,
        		 wlid:v.wlid,
        		 qyl:qyl,
        		 qyrq:qyrq,
        		 zjthsl:zjthsl,
        		 bgdh:bgdh,
        		 fjids:hwfj
         }
         hwxxlist.push(hwxx);
    });
    if(wlbms!=null && wlbms!=""){
    	if(sffqlld=="1"){
    		$.error("不合格数量不能大于数量"+wlbms);
        	return false;
    	}else{
    		$.error("不合格数量和取样量的和不能大于数量"+wlbms);
        	return false;
    	}
    }
    $("#inspectionModForm #hwxxlist").val(JSON.stringify(hwxxlist));
    $("#inspectionModForm #delids").val(JSON.stringify(delhwids));
	return true;
}
var auditInspection_Config = {
	width		: "1080px",
	modalName	:"auditInspection_Config",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}	
}
var viewInspection_Config = {
	width		: "1080px",
	modalName	:"viewInspection_Config",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}
function inspectionAuditingResult(isTurnBack){
	if(isTurnBack){
		$('#inspection_audit_formSearch #inspection_audit_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#inspection_audit_formSearch #inspection_audit_list').bootstrapTable('refresh');
	}
}
function inspectionAuditedResult(isTurnBack){
	if(isTurnBack){
		$('#inspection_audit_audited #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#inspection_audit_audited #tb_list').bootstrapTable('refresh');
	}
}
$(function () {
	//2.初始化Button的点击事件
    var oButtonInit = new inspectionAudit_ButtonInit();
    oButtonInit.Init();
	var tableInit = new inspection_AuditListInit();
	tableInit.Init();
});