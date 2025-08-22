var sbjlxx_audit_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#sbjlxx_audit_formSearch #sbjlxx_audit_list").bootstrapTable({
			url: '/measurement/measurement_audit/getListSbjxx_audit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#sbxx_audit_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "sbjlxx.lrsj",				//排序字段
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
            uniqueId: "jlid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            },{title: '序号',
				formatter: function (value, row, index) {
					return index+1;
				},
				width: '5%',
                align: 'left',
                titleTooltip:'序号',
                visible:true
             },{
                 field: 'sbbh',
                 title: '设备编号',
                 width: '7%',
                 align: 'left',
                 visible: true
             },{
                 field: 'sbmc',
                 title: '设备名称',
                 width: '7%',
                 align: 'left',
                 visible: true
             },{
                 field: 'bhlbmc',
                 title: '编号类别',
                 width: '7%',
                 align: 'left',
                 visible: true
             },{
                 field: 'gllbmc',
                 title: '管理类别',
                 width: '7%',
                 align: 'left',
                 visible: true
             },{
                 field: 'ssxm',
                 title: '所属项目',
                 width: '7%',
                 align: 'left',
                 visible: false
             },{
                 field: 'ssgs',
                 title: '所属公司',
                 width: '7%',
                 align: 'left',
                 visible: false
             },{
                 field: 'sqrmc',
                 title: '申请人',
                 width: '7%',
                 align: 'left',
                 visible: true
             },{
                 field: 'sqbmmc',
                 title: '申请部门',
                 width: '7%',
                 align: 'left',
                 visible: true
             },{
                 field: 'sgbmmc',
                 title: '申购部门',
                 width: '7%',
                 align: 'left',
                 visible: true
             },{
                 field: 'ggxh',
                 title: '规格型号',
                 width: '5%',
                 align: 'left',
                 visible: false
             },{
                 field: 'djjs',
                 title: '单价(含税)',
                 width: '6%',
                 align: 'left',
                 visible: false
             },{
                 field: 'sl',
                 title: '数量',
                 width: '4%',
                 align: 'left',
                 visible: true
             },{
                 field: 'jejs',
                 title: '金额(含税)',
                 width: '6%',
                 align: 'left',
                 visible: false
             },{
                 field: 'dhrq',
                 title: '到货日期',
                 width: '7%',
                 align: 'left',
                 visible: false
             },{
                 field: 'sccj',
                 title: '生产厂家',
                 width: '7%',
                 align: 'left',
                 visible: false
             },{
                 field: 'cjdh',
                 title: '厂家电话',
                 width: '7%',
                 align: 'left',
                 visible: false
             },{
                 field: 'gys',
                 title: '供应商',
                 width: '7%',
                 align: 'left',
                 visible: false
             },{
                 field: 'gysdh',
                 title: '供应商电话',
                 width: '7%',
                 align: 'left',
                 visible: false
             }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！"); 
            },
            onDblClickRow: function (row, $element) {
            	sbxx_audit_DealById(row.sbid,"view",$("#sbxx_audit_formSearch #btn_view").attr("tourl"));
             },
		});
		$("#sbjlxx_audit_formSearch #sbjlxx_audit_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true        
	  });
	}
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "sbjlxx.jlid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		var cxtj=$("#sbjlxx_audit_formSearch #cxtj").val();
		var cxnr=$.trim(jQuery('#sbjlxx_audit_formSearch #cxnr').val());
		if(cxtj=="0"){
			map["sbbh"]=cxnr
		}else if(cxtj=="1"){
			map["sbmc"]=cxnr
		}
		map["dqshzt"] = 'dsh';
		return map;
	}
	return oTableInit
}
var sbjlxxAudited_TableInit=function(){
	var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
    	$("#sbjlxx_audit_audited #tb_list").bootstrapTable({
			url: '/measurement/measurement_audit/getListSbjxx_audit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#sbjlxx_audit_audited #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "shxx.shsj",				//排序字段
            sortOrder: "desc",                  //排序方式
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
            //height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "shxx_shxxid",            //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                  //是否显示父子表
            isForceTable:true,
            columns: [{
                checkbox: true
            },{
                field: 'sbbh',
                title: '设备编号',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'sbmc',
                title: '设备名称',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'bhlbmc',
                title: '编号类别',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'gllbmc',
                title: '管理类别',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'ssxm',
                title: '所属项目',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'ssgs',
                title: '所属公司',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'sqrmc',
                title: '申请人',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'sqbmmc',
                title: '申请部门',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'sgbmmc',
                title: '申购部门',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'ggxh',
                title: '规格型号',
                width: '5%',
                align: 'left',
                visible: false
            },{
                field: 'djjs',
                title: '单价(含税)',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'sl',
                title: '数量',
                width: '4%',
                align: 'left',
                visible: true
            },{
                field: 'jejs',
                title: '金额(含税)',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'dhrq',
                title: '到货日期',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'sccj',
                title: '生产厂家',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'cjdh',
                title: '厂家电话',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'gys',
                title: '供应商',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'gysdh',
                title: '供应商电话',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'shxx_shid',
                title: '审核ID',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_lrryxm',
                title: '审核人',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shsj',
                title: '审核时间',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sftgmc',
                title: '是否通过',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'lrsj',
                align: 'center',
                title: '录入时间',
                visible: false
            },{
                field: 'shxx_shxxid',
                align: 'center',
                title: '审核信息ID',
                visible: false
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	sbxx_audit_DealById(row.sbid,"view",$("#sbxx_audit_formSearch #btn_view").attr("tourl"));
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
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "sbjlxx.jlid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        var cxtj=$("#sbjlxx_audit_audited #cxtj").val();
		var cxnr=$.trim(jQuery('#sbjlxx_audit_audited #cxnr').val());
		if(cxtj=="0"){
			map["sbbh"]=cxnr
		}else if(cxtj=="1"){
			map["sbmc"]=cxnr
		}
		
		/*var sqsjstart = jQuery('#sbjlxx_audit_audited #sqsjstart').val();
		var sqsjend = jQuery('#sbjlxx_audit_audited #sqsjend').val();
    	map["sqsjstart"] = sqsjstart;
    	map["sqsjend"] = sqsjend;*/
    	map["dqshzt"] = 'ysh';
    	return map;
    };
    return oTableInit;
}

var sbjlxx_audit_oButtton=function(){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#sbjlxx_audit_formSearch #btn_query");//模糊查询
		var btn_queryAudited = $("#sbjlxx_audit_audited #btn_query");//审核记录列表模糊查询
    	var btn_cancelAudit = $("#sbjlxx_audit_audited #btn_cancelAudit");//取消审核
    	var btn_view = $("#sbjlxx_audit_formSearch #btn_view");//查看页面
    	var btn_audit = $("#sbjlxx_audit_formSearch #btn_audit");//审核
    	
		/*-----------------------模糊查询(审核列表)------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchSbjlxx_audit_Result(true);
    		});
		}
		
		/*-----------------------模糊查询(审核记录)------------------------------------*/
		if(btn_queryAudited!=null){
			btn_queryAudited.unbind("click").click(function(){
				 searchSbjlxxAudited(true);
    		});
		}
		
		/*---------------------------查看页面--------------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row=$('#sbjlxx_audit_formSearch #sbjlxx_audit_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				sbjlxx_audit_DealById(sel_row[0].jlid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		
		/*---------------------------审核--------------------------------*/
    	btn_audit.unbind("click").click(function(){
    		var sel_row = $('#sbjlxx_audit_formSearch #sbjlxx_audit_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			sbjlxx_audit_DealById(sel_row[0].jlid,"audit",btn_audit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
		/*---------------------------查看审核记录--------------------------------*/
    	//选项卡切换事件回调
    	$('#sbjlxx_formAudit #sbjlxx_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
    		var _hash = e.target.hash.replace("#",'');
    		if(!e.target.isLoaded){//只调用一次
    			if(_hash=='sbjlxx_auditing'){
    				var oTable= new sbjlxx_audit_TableInit();
    				oTable.Init();
    				
    			}else{
    				var oTable= new sbjlxxAudited_TableInit();
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
    			cancelAudit($('#sbjlxx_audit_audited #tb_list').bootstrapTable('getSelections'),function(){
    				searchSbjlxxAudited();
    			});
    		})
    	}
	}
	return oInit;
}

function sbjlxx_audit_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action=="view"){
		var url=tourl+"?jlid="+id;
		$.showDialog(url,'查看信息',viewMeasurement_audit_Config);
	}else if(action=="audit"){
		showAuditDialogWithLoading({
			id: id,
			type: 'AUDIT_MEASURE',
			url:$("#sbjlxx_audit_formSearch #btn_audit").attr("tourl"),
			data:{'ywzd':'jlid'},
			title:"计量校准审核",
			preSubmitCheck:'preSubmitMEASUREInfo',
			callback:function(){
				searchSbjlxx_audit_Result();//回调
			},
			dialogParam:{width:1000}
		});
	}
}

function preSubmitMEASUREInfo(){
	return true;
}

function searchSbjlxx_audit_Result(isTurnBack){
	if(isTurnBack){
		$('#sbjlxx_audit_formSearch #sbjlxx_audit_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#sbjlxx_audit_formSearch #sbjlxx_audit_list').bootstrapTable('refresh');
	}
}

function searchSbjlxxAudited(isTurnBack){
	if(isTurnBack){
		$('#sbjlxx_audit_audited #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#sbjlxx_audit_audited #tb_list').bootstrapTable('refresh');
	}
}

var viewMeasurement_audit_Config = {
	width		: "1000px",
	modalName	:"viewMeasurementConfig",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
$(function(){
	var oTable= new sbjlxx_audit_TableInit();
	oTable.Init();
	
	var oButtonInit = new sbjlxx_audit_oButtton();
	oButtonInit.Init();
	
	jQuery('#sbjlxx_audit_formSearch .chosen-select').chosen({width: '100%'});
	jQuery('#sbjlxx_audit_audited .chosen-select').chosen({width: '100%'});
	
	laydate.render({
	 	  elem: '#sbjlxx_audit_audited #sqsjstart'
	 });
	laydate.render({
	 	  elem: '#sbjlxx_audit_audited #sqsjend'
	});
})