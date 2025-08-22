var sbxx_audit_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#sbxx_audit_formSearch #sbxx_audit_list").bootstrapTable({
			url: '/deviceinfo/deviceinfo/ListSbxxAudit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#sbxx_audit_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "sbxx.sbid",				//排序字段
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
            uniqueId: "sbid",                     //每一行的唯一标识，一般为主键列
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
                field: 'sbmc',
                title: '设备名称',
                width: '7%',
                align: 'left',
                titleTooltip:'设备名称',
                visible:true
            },{
                field: 'bhlb',
                title: '编号类别',
                width: '7%',
                align: 'left',
                titleTooltip:'编号类别',
                visible:true
            },{
                field: 'gllb',
                title: '管理类别',
                width: '7%',
                align: 'left',
                titleTooltip:'管理类别',
                visible:true
            },{
                field: 'ggxh',
                title: '规格',
                width: '5%',
                align: 'left',
                titleTooltip:'规格',
                visible:true
            },{
                field: 'djjs',
                title: '单价(含税)',
                width: '7%',
                align: 'left',
                titleTooltip:'单价(含税)',
                visible:true
            },{
                field: 'sl',
                title: '数量',
                width: '5%',
                align: 'left',
                titleTooltip:'数量',
                visible:true
            },{
                field: 'jejs',
                title: '金额(含税)',
                width: '7%',
                align: 'left',
                titleTooltip:'金额(含税)',
                visible:true
            },{
                field: 'sqbm',
                title: '申请部门',
                width: '8%',
                align: 'left',
                titleTooltip:'申请部门',
                visible:true
            },{
                field: 'gys',
                title: '供货商',
                width: '10%',
                align: 'left',
                titleTooltip:'供应商',
                visible:true
            },{
                field: 'dhrq',
                title: '到货日期',
                width: '10%',
                align: 'left',
                titleTooltip:'到货日期',
                visible:true
            },{
                field: 'ysjg',
                title: '验收结果',
                width: '10%',
                formatter:ysjgformat,
                align: 'left',
                titleTooltip:'验收结果',
                visible:true
            },{
                field: 'sqrxm',
                title: '申请人员',
                width: '10%',
                align: 'left',
                titleTooltip:'申请人员',
                visible:true
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                width: '10%',
                align: 'left',
                titleTooltip:'申请时间',
                visible:true
            },{
                field: 'zt',
                title: '状态',
                width: '10%',
                align: 'left',
                formatter:ztFormat,
                titleTooltip:'状态',
                visible:true
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
		 $("#sbxx_audit_formSearch #sbxx_audit_list").colResizable({
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
            sortLastName: "sbxx.sbid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		var cxtj=$("#sbxx_audit_formSearch #cxtj").val();
		var cxnr=$.trim(jQuery('#sbxx_audit_formSearch #cxnr').val());
		if(cxtj=="0"){
			map["sbbh"]=cxnr
		}else if(cxtj=="1"){
			map["sbmc"]=cxnr
		}else if(cxtj=="2"){
			map["sqrmc"]=cxnr
		}else if(cxtj=="3"){
			map["sqbmmc"]=cxnr
		}else if(cxtj=="4"){
			map["sgbmmc"]=cxnr
		}else if(cxtj=="5"){
			map["ssxm"]=cxnr
		}else if(cxtj=="6"){
			map["ssgs"]=cxnr
		}else if(cxtj=="7"){
			map["gys"]=cxnr
		}else if(cxtj=="8"){
			map["sccj"]=cxnr
		}else if(cxtj=="9"){
			map["sybmmc"]=cxnr
		}else if(cxtj=="10"){
			map["zrbmmc"]=cxnr
		}else if(cxtj=="11"){
			map["zrrmc"]=cxnr
		}
		map["dqshzt"] = 'dsh';
		return map;
	}
	return oTableInit;
}

/**
 * 验收结果格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function ysjgformat(value,row,index){
	if(row.ysjg=='1'){
		return '合格';
	}else if(row.ysjg=='0'){
		var ysjg="<span style='color:red;font-weight: bold;'>不合格</span>";
		return ysjg;
		}
}

/**
 * 设备审核列表的状态格式化函数
 * @returns
 */
function ztFormat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
        return '审核通过';
    }
    else{
        return row.shxx_gwmc + '审核中';
    }
}
var sbxxAudited_TableInit=function(){
	var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
    	$("#sbxx_audit_audited #tb_list").bootstrapTable({
			url: '/deviceinfo/deviceinfo/ListSbxxAudit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#sbxx_audit_audited #toolbar',                //工具按钮用哪个容器
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
            }, {
                field: 'sbmc',
                title: '设备名称',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'shxx_shid',
                title: '审核ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'sqbm',
                title: '申请部门',
                titleTooltip:'申请部门',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'gys',
                title: '供应商',
                width: '12%',
                titleTooltip:'供应商',
                align: 'left',
                visible: true
            },{
                field: 'ggxh',
                title: '规格型号',
                titleTooltip:'规格型号',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'sl',
                title: '数量',
                titleTooltip:'数量',
                width: '4%',
                align: 'right',
                visible: true
            },{
                field: 'jejs',
                title: '金额(含税)',
                titleTooltip:'金额(含税)',
                width: '4%',
                align: 'right',
                visible: true
            },{
                field: 'sqrxm',
                title: '申请人员',
                titleTooltip:'申请人员',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                titleTooltip:'申请时间',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_lrryxm',
                title: '审核人',
                titleTooltip:'审核人',
                align: 'left',
                width: '12%',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shsj',
                title: '审核时间',
                titleTooltip:'审核时间',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sftgmc',
                title: '是否通过',
                titleTooltip:'是否通过',
                width: '12%',
                align: 'left',
                visible: true
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
            sortLastName: "sbxx.sbid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        var cxtj=$("#sbxx_audit_audited #cxtj").val();
		var cxnr=$.trim(jQuery('#sbxx_audit_audited #cxnr').val());
		if(cxtj=="0"){
			map["sbbh"]=cxnr
		}else if(cxtj=="1"){
			map["sbmc"]=cxnr
		}else if(cxtj=="2"){
			map["sqrmc"]=cxnr
		}else if(cxtj=="3"){
			map["sqbmmc"]=cxnr
		}else if(cxtj=="4"){
			map["sgbmmc"]=cxnr
		}else if(cxtj=="5"){
			map["ssxm"]=cxnr
		}else if(cxtj=="6"){
			map["ssgs"]=cxnr
		}else if(cxtj=="7"){
			map["gys"]=cxnr
		}else if(cxtj=="8"){
			map["sccj"]=cxnr
		}else if(cxtj=="9"){
			map["sybmmc"]=cxnr
		}else if(cxtj=="10"){
			map["zrbmmc"]=cxnr
		}else if(cxtj=="11"){
			map["zrrmc"]=cxnr
		}
		var sqsjstart = jQuery('#sbxx_audit_audited #sqsjstart').val();
    	map["sqsjstart"] = sqsjstart;
    	// 通过结束日期
    	var sqsjend = jQuery('#sbxx_audit_audited #sqsjend').val();
    	map["sqsjend"] = sqsjend;
    	map["dqshzt"] = 'ysh';
    	return map;
    };
    return oTableInit;
}
var sbxx_audit_oButtton=function(){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#sbxx_audit_formSearch #btn_query");//模糊查询
		var btn_queryAudited = $("#sbxx_audit_audited #btn_query");//审核记录列表模糊查询
    	var btn_cancelAudit = $("#sbxx_audit_audited #btn_cancelAudit");//取消审核
    	var btn_view = $("#sbxx_audit_formSearch #btn_view");//查看页面
		var btn_audit = $("#sbxx_audit_formSearch #btn_audit");//审核
    	
		/*-----------------------模糊查询(审核列表)------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchSbxx_audit_Result(true);
    		});
		}
		/*-----------------------模糊查询(审核记录)------------------------------------*/
		if(btn_queryAudited!=null){
			btn_queryAudited.unbind("click").click(function(){
				 searchSbxxAudited(true);
    		});
		}
		/*---------------------------查看页面--------------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row=$('#sbxx_audit_formSearch #sbxx_audit_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				sbxx_audit_DealById(sel_row[0].sbid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*---------------------------审核--------------------------------*/
    	btn_audit.unbind("click").click(function(){
    		var sel_row = $('#sbxx_audit_formSearch #sbxx_audit_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			sbxx_audit_DealById(sel_row[0].sbid,"audit",btn_audit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/*---------------------------查看审核记录--------------------------------*/
		//选项卡切换事件回调
    	$('#sbxx_formAudit #sbxx_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
    		var _hash = e.target.hash.replace("#",'');
    		var _gridId = "#sbxx"+ _hash;
    		if(!e.target.isLoaded){//只调用一次
    			if(_hash=='audited'){
	    			var oTable = new sbxxAudited_TableInit();
	    		    oTable.Init();
    			}else{
    				var oTable = new sbxxAudited_TableInit();
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
    			cancelAudit($('#sbxx_audit_audited #tb_list').bootstrapTable('getSelections'),function(){
    				searchSbxxAudited();
    			});
    		})
    	}
	}
	return oInit;
}
function sbxx_audit_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action=="view"){
		var url=tourl+"?sbid="+id;
		$.showDialog(url,'查看信息',viewDeviceInfo_audit_Config);
	}else if(action=="audit"){
		var url= tourl;
		showAuditDialogWithLoading({
			id: id,
			type: 'AUDIT_DEVICEINFO',
			url:url,
			data:{'ywzd':'sbid'},
			title:$("#sbxx_audit_formSearch #btn_audit").attr("gnm"),
			preSubmitCheck:'preSubmitDeviceInfo',
			callback:function(map){
				if(map){
					$.ajax({
						url:'/measurement/measurement/saveSbjlxx',
						type:'post',
						data:{"sbid":map.backInfo.sbid,"access_token":$("#ac_tk").val()},
						dataType:'JSON',
						success:function(data){
							if(data.state=="success"){
								$.success(data.msg);
							}else if(data.state=="error"){
								$.error(data.msg);
							}
							
						}
					})
				}
				searchSbxx_audit_Result();//回调
			},
			dialogParam:{width:1000}
		});
	}
}

function preSubmitDeviceInfo(){
	return true;
}

var viewDeviceInfo_audit_Config = {
		width		: "1000px",
		modalName	:"viewDeviceInfoConfig",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
function searchSbxx_audit_Result(isTurnBack){
	if(isTurnBack){
		$('#sbxx_audit_formSearch #sbxx_audit_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#sbxx_audit_formSearch #sbxx_audit_list').bootstrapTable('refresh');
	}
}

function searchSbxxAudited(isTurnBack){
	if(isTurnBack){
		$('#sbxx_audit_audited #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#sbxx_audit_audited #tb_list').bootstrapTable('refresh');
	}
}

$(function(){
	var oTable= new sbxx_audit_TableInit();
		oTable.Init();
    var oButtonInit = new sbxx_audit_oButtton();
		oButtonInit.Init();
	jQuery('#sbxx_audit_formSearch .chosen-select').chosen({width: '100%'});
	jQuery('#sbxx_audit_audited .chosen-select').chosen({width: '100%'});
	laydate.render({
	 	  elem: '#sbxx_audit_audited #sqsjstart'
	 });
	laydate.render({
	 	  elem: '#sbxx_audit_audited #sqsjend'
	 });
})