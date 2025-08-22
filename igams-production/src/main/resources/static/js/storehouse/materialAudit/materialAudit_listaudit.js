var MaterialAudit_audit_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#materialAudit_audit_formSearch #materialAudit_auditing_list").bootstrapTable({
			url: $("#materialAudit_formAudit #urlPrefix").val()+'/storehouse/materialAudit/pageGetListMaterialAudit',
            method: 'get',                      // 请求方式（*）
            toolbar: '#materialAudit_audit_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "llgl.lrsj",				// 排序字段
            sortOrder: "desc",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
            pageSize: 15,                       // 每页的记录行数（*）
            pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  // 是否显示所有的列
            showRefresh: true,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "llid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'lldh',
                title: '领料单号',
                width: '24%',
                align: 'left',
                visible: true,
            },{
                field: 'lllx',
                title: '领料类型',
                width: '24%',
                align: 'left',
                formatter:lllxformat,
                visible:false,
            },{
                field: 'jlbh',
                title: '记录编号',
                width: '24%',
                align: 'left',
                visible:true,
            },{
                field: 'sqbmmc',
                title: '申请部门',
                width: '18%',
                align: 'left',
                visible:true,
            },{
                field: 'sqrmc',
                title: '申请人员',
                width: '16%',
                align: 'left',
                visible:true,
            },{
                field: 'sqrq',
                title: '申请日期',
                width: '18%',
                align: 'left',
                visible:true,
            },{
                field: 'wcbj',
                title: '订单完成',
                width: '14%',
                align: 'left',
                visible:true,
                formatter:materialAudit_wcbjformat,
            },{
                field: 'bz',
                title: '备注',
                width: '26%',
                align: 'left',
                visible:true,
            },{
                field: 'zt',
                title: '状态',
                width: '20%',
                align: 'left',
                visible:false,
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                width: '24%',
                align: 'left',
                visible:true,
            },{
                field: 'shxx_shsj',
                title: '审核时间',
                width: '24%',
                align: 'left',
                visible:true,
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '24%',
                align: 'left',
                visible:true,
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	material_audit_DealById(row.llid,'view',$("#materialAudit_audit_formSearch #btn_view").attr("tourl"));
             },
		});
        $("#materialAudit_audit_formSearch #materialAudit_auditing_list").colResizable({
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
            sortLastName: "llgl.llid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
//		return getMaterialAuditSearchData(map);
		var materialAudit_select=$("#materialAudit_audit_formSearch #materialAudit_select").val();
		var materialAudit_input=$.trim(jQuery('#materialAudit_audit_formSearch #materialAudit_input').val());
		if(materialAudit_select=="0"){
			map["lldh"]=materialAudit_input
		}else if(materialAudit_select=="1"){
			map["sqbmmc"]=materialAudit_input
		}else if(materialAudit_select=="2"){
			map["sqrmc"]=materialAudit_input
		}
		map["dqshzt"] = 'dsh';
		return map;
	};
	return oTableInit;
}

//完成标记格式化
function materialAudit_wcbjformat(value,row,index){
	   if (row.wcbj == '1') {
	        return '是';
	    }else{
	    	return '否';
	    }
}
//是否通过格式化
function materialAudit_wcbjformat(value,row,index){
	   if (row.shxx_sftg == '1') {
	        return '通过';
	    }else{
	    	return '未通过';
	    }
}

function lllxformat(value,row,index){
    if (row.lllx == '1') {
        return "OA领料";
    }else if (row.lllx == '2') {
        return "留样领料";
    }else if (row.lllx == '3') {
        return "销售领料";
    }else if (row.lllx == '4') {
        return "OA留样领料";
    }else if (row.lllx == '5') {
        return "生产领料";
    }else {
        return "普通领料";
    }
	
}
var MaterialAudit_audited_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#materialAudit_audited_formSearch #materialAudit_audited_list").bootstrapTable({
			url: $("#materialAudit_formAudit #urlPrefix").val()+'/storehouse/materialAudit/pageGetListMaterialAudit',
            method: 'get',                      // 请求方式（*）
            toolbar: '#materialAudit_audited_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "shxx.shsj",				// 排序字段
            sortOrder: "desc",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
            pageSize: 15,                       // 每页的记录行数（*）
            pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  // 是否显示所有的列
            showRefresh: true,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "shxx_shxxid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'lldh',
                title: '领料单号',
                width: '24%',
                align: 'left',
                visible: true,
            },{
                field: 'lllx',
                title: '领料类型',
                width: '24%',
                align: 'left',
                formatter:lllxformat,
                visible:false,
            },{
                field: 'jlbh',
                title: '记录编号',
                width: '18%',
                align: 'left',
                visible:true,
            },{
                field: 'sqbmmc',
                title: '申请部门',
                width: '18%',
                align: 'left',
                visible:true,
            },{
                field: 'sqrmc',
                title: '申请人员',
                width: '16%',
                align: 'left',
                visible:true,
            },{
                field: 'sqrq',
                title: '申请日期',
                width: '24%',
                align: 'left',
                visible:true,
            },{
                field: 'wcbj',
                title: '订单完成',
                width: '14%',
                align: 'left',
                visible:true,
                formatter:materialAudit_wcbjformat,
            },{
                field: 'bz',
                title: '备注',
                width: '26%',
                align: 'left',
                visible:true,
            },{
                field: 'zt',
                title: '状态',
                width: '14%',
                align: 'left',
                visible:false,
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                width: '24%',
                align: 'left',
                visible:true,
            },{
                field: 'shxx_lrryxm',
                title: '审核人',
                width: '18%',
                align: 'left',
                visible:true,
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '24%',
                align: 'left',
                visible:true,
            },{
                field: 'shxx_shsj',
                title: '审核时间',
                width: '24%',
                align: 'left',
                visible:true,
            },{
                field: 'shxx_sftg',
                title: '是否通过',
                width: '12%',
                align: 'left',
                visible:true,
                formatter:materialAudit_wcbjformat,
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	material_audit_DealById(row.llid,'view',$("#materialAudit_audit_formSearch #btn_view").attr("tourl"));
             },
		});
        $("#materialAudit_audited_formSearch #materialAudit_audited_list").colResizable({
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
            sortLastName: "llgl.llid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
//		return getMaterialAuditSearchData(map);
		var materialAudit_select=$("#materialAudit_audited_formSearch #materialAudit_select").val();
		var materialAudit_input=$.trim(jQuery('#materialAudit_audited_formSearch #materialAudit_input').val());
		if(materialAudit_select=="0"){
			map["lldh"]=materialAudit_input
		}else if(materialAudit_select=="1"){
			map["sqbmmc"]=materialAudit_input
		}else if(materialAudit_select=="2"){
			map["sqrmc"]=materialAudit_input
		}
		map["dqshzt"] = 'ysh';
		return map;
	};
	return oTableInit;
}

var viewMaterialAuditConfig = {
		width		: "1600px",
		modalName	:"viewMaterialAuditModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

//待审核列表的按钮路径
function material_audit_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $('#materialAudit_formAudit #urlPrefix').val() + tourl; 
	if(action=="view"){
	var url=tourl+"?llid="+id;
		$.showDialog(url,'查看领料信息',viewMaterialAuditConfig);
	}else if(action=="audit"){
		showAuditDialogWithLoading({
			id: id,
			type: 'AUDIT_GOODS_APPLY',
			url:tourl,
			data:{'ywzd':'llid'},
			title:"领料审核",
			preSubmitCheck:'preSubmitputmaterial',
			prefix:$('#materialAudit_formAudit #urlPrefix').val(),
			callback:function(){
				searchMaterial_audit_Result(true);//回调
		},
			dialogParam:{width:1500}
		});
	}
}

function preSubmitputmaterial(){
	if($("#editPickingForm #xsbj").val()=="1"){
		var json = [];
		var message = $("#editPickingForm #message").val();
		if(message!=null && message!=""){
			$.alert(message);
			return false;
		}
        let jgdm= $("#editPickingForm #jgdm").val()
        if(!jgdm){
            $.alert("所选部门存在异常！");
            return false;
        }
		if(t_map.rows != null && t_map.rows.length > 0){
			for(var i=0;i<t_map.rows.length;i++){
				if(t_map.rows[i].qlsl==null || t_map.rows[i].qlsl==''){
					$.alert("领料数量不能为空！");
					return false;
				}
				if(parseFloat(t_map.rows[i].qlsl)>parseFloat(t_map.rows[i].kqls)){
					$.alert("请领数不能大于可请领数！");
					return false;
				}
				if(t_map.rows[i].xmdl==null || t_map.rows[i].xmdl==''){
					$.alert("项目大类不能为空！");
					return false;
				}
				if(t_map.rows[i].xmbm==null || t_map.rows[i].xmbm==''){
					$.alert("项目编码不能为空！");
					return false;
				}
				if(t_map.rows[i].qlsl==0){
					$.alert("请领数量不能为0！");
					return false;
				}
				var sz = {"hwllid":'',"ckhwid":'',"ckid":'',"wlid":'',"qlsl":'',"bz":'',"xmdl":'',"xmbm":'',"qlyds":'',"cplx":'',"hwllmx_json":''};
				sz.hwllid = t_map.rows[i].hwllid;
				sz.ckhwid = t_map.rows[i].ckhwid;
				sz.ckid = t_map.rows[i].ckid;
				sz.wlid = t_map.rows[i].wlid;
				sz.qlsl = t_map.rows[i].qlsl;
				sz.bz = t_map.rows[i].hwllxxbz;
				sz.xmdl = t_map.rows[i].xmdl;
				sz.xmbm = t_map.rows[i].xmbm;
				sz.qlyds = t_map.rows[i].qlyds;
                sz.cplx = t_map.rows[i].cplx;
				sz.hwllmx_json=t_map.rows[i].hwllmx_json;
				json.push(sz);				
			}
			$("#editPickingForm #llmx_json").val(JSON.stringify(json));
		}else{
			$.alert("领料信息不能为空！");
			return false;
		}
        $("#editPickingForm #xmdllist").val('')
        $("#editPickingForm #xmbmlist").val('')
	}
	if($("#senioreditPickingForm #xsbj").val()=="2"){
		var jy_json = $("#senioreditPickingForm #jy_json").val();
        let jgdm= $("#senioreditPickingForm #jgdm").val()
        if(!jgdm){
            $.alert("所选部门存在异常！");
            return false;
        }
		if(jy_json!=null && jy_json!=""){
			var json = JSON.parse(jy_json);
			for(i = 0; i < json.length; i++) {
				if(json[i].id!=null && json[i].id!=""){
					$.alert(json[i].message);
					return false;
				}
			}
		}
	}	
	return true;
}

var materialAudit_audit_oButtton=function(){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#materialAudit_audit_formSearch #btn_query");//模糊查询（待审核）
		var btn_queryAudited = $("#materialAudit_audited_formSearch #btn_query");//模糊查询（审核历史）
    	var btn_cancelAudit = $("#materialAudit_audited_formSearch #btn_cancelAudit");//取消审核（审核历史）
    	var btn_view = $("#materialAudit_audit_formSearch #btn_view");//查看页面（待审核）
    	var btn_audit = $("#materialAudit_audit_formSearch #btn_audit");//审核（待审核）
    	
    	/*-----------------------模糊查询(审核列表)------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchMaterial_audit_Result(true);
    		});
		}
		
		/*-----------------------模糊查询(审核记录)------------------------------------*/
		if(btn_queryAudited!=null){
			btn_queryAudited.unbind("click").click(function(){
				searchMaterial_audited_Result(true);
    		});
		}
		
		/*---------------------------查看页面--------------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row=$('#materialAudit_audit_formSearch #materialAudit_auditing_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				material_audit_DealById(sel_row[0].llid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		
		/*---------------------------审核--------------------------------*/
    	btn_audit.unbind("click").click(function(){
    		var sel_row = $('#materialAudit_audit_formSearch #materialAudit_auditing_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			material_audit_DealById(sel_row[0].llid,"audit",btn_audit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*---------------------------查看审核记录--------------------------------*/
    	//选项卡切换事件回调
    	$('#materialAudit_formAudit #materialAudit_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
    		var _hash = e.target.hash.replace("#",'');
    		if(!e.target.isLoaded){//只调用一次
    			if(_hash=='materialAudit_auditing'){
    				var oTable= new MaterialAudit_audit_TableInit();
    				oTable.Init();
    				
    			}else{
    				var oTable= new MaterialAudit_audited_TableInit();
    				oTable.Init();
    			}
    			e.target.isLoaded = true;
    		}else{//重新加载
    			//$(_gridId + ' #materialAudit_audited_list').bootstrapTable('refresh');
    		}
    	}).first().trigger('shown.bs.tab');//触发第一个选中事件
    	
    	/*-------------------------------------取消审核-----------------------------------*/
    	if(btn_cancelAudit!=null){
    		btn_cancelAudit.unbind("click");
    		btn_cancelAudit.click(function(){
    			var materialAudit_params=[];
    			materialAudit_params.prefix=$('#materialAudit_formAudit #urlPrefix').val();
    			cancelAudit($('#materialAudit_audited_formSearch #materialAudit_audited_list').bootstrapTable('getSelections'),function(){
    				searchMaterial_audited_Result();
    			},null,materialAudit_params);
    		})
    	}
    	/*-----------------------------------------------------------------------------*/
	}
	return oInit;
}


function searchMaterial_audit_Result(isTurnBack){
	if(isTurnBack){
		$('#materialAudit_audit_formSearch #materialAudit_auditing_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#materialAudit_audit_formSearch #materialAudit_auditing_list').bootstrapTable('refresh');
	}
}

function searchMaterial_audited_Result(isTurnBack){
	if(isTurnBack){
		$('#materialAudit_audited_formSearch #materialAudit_audited_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#materialAudit_audited_formSearch #materialAudit_audited_list').bootstrapTable('refresh');
	}
}

$(function(){
	var oTable= new MaterialAudit_audit_TableInit();
	oTable.Init();
	
	var oButtonInit = new materialAudit_audit_oButtton();
	oButtonInit.Init();
	
	// 所有下拉框添加choose样式
	jQuery('#materialAudit_audit_formSearch .chosen-select').chosen({width: '100%'});
	jQuery('#materialAudit_audited_formSearch .chosen-select').chosen({width: '100%'});
})