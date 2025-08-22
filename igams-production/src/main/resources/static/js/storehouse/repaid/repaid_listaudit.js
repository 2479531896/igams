var repaidAudit_audit_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#repaidAudit_audit_formSearch #repaidAudit_auditing_list").bootstrapTable({
			url: $("#repaidAudit_formAudit #urlPrefix").val()+'/repaid/repaid/pageGetListAudit',
            method: 'get',                      // 请求方式（*）
            toolbar: '#repaidAudit_audit_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "jcghgl.ghrq",				// 排序字段
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
            uniqueId: "jcghid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width:'3%'
            },{
                field: 'jcghid',
                title: '借出归还ID',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'jcjyid',
                title: '借出借用id',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'ghdh',
                title: '归还单号',
                width: '20%',
                align: 'left',
                sortable:true,
                visible: true
            },{
                field: 'bmmc',
                title: '部门',
                width: '15%',
                align: 'left',
                visible: true
            },{
                field: 'dwlxmc',
                title: '单位类型',
                width: '15%',
                align: 'left',
                visible: true
            },{
                field: 'dwmc',
                title: '单位',
                width: '15%',
                align: 'left',
                visible: true
            },{
                field: 'ghrq',
                title: '归还日期',
                width: '15%',
                align: 'left',
                sortable:true,
                visible:true
            },{
                field: 'sfzfyf',
                title: '支付运费',
                width: '10%',
                align: 'left',
                sortable:true,
                formatter:sfzfyfFormat,
                visible:true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '24%',
                align: 'left',
                visible:true,
            },{
                field: 'bz',
                title: '备注',
                width: '20%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	repaid_audit_DealById(row.jcghid,'view',$("#repaidAudit_audit_formSearch #btn_view").attr("tourl"));
             },
		});
        $("#repaidAudit_audit_formSearch #repaidAudit_auditing_list").colResizable({
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
            sortLastName: "jcghgl.jcghid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
        };
		var repaidAudit_select=$("#repaidAudit_audit_formSearch #repaidAudit_select").val();
		var repaidAudit_input=$.trim(jQuery('#repaidAudit_audit_formSearch #repaidAudit_input').val());
        if(repaidAudit_select=="0"){
            map["entire"]=repaidAudit_input
        }else if(repaidAudit_select=="1"){
            map["ghdh"]=repaidAudit_input
        }else if(repaidAudit_select=="2"){
            map["bmmc"]=repaidAudit_input
        }
		map["dqshzt"] = 'dsh';
		return map;
	};
	return oTableInit;
}

//标记格式化
function sfzfyfFormat(value,row,index){
    var html="";
    if(row.sfzfyf=='1'){
        html="<span style='color:green;'>"+"是"+"</span>";
    }else{
        html="<span style='color:red;'>"+"否"+"</span>";
    }
    return html;
}

//是否通过格式化
function repaidAudit_wcbjformat(value,row,index){
	   if (row.shxx_sftg == '1') {
	        return '通过';
	    }else{
	    	return '未通过';
	    }
}

var repaidAudit_audited_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#repaidAudit_audited_formSearch #repaidAudit_audited_list").bootstrapTable({
			url: $("#repaidAudit_formAudit #urlPrefix").val()+'/repaid/repaid/pageGetListAudit',
            method: 'get',                      // 请求方式（*）
            toolbar: '#repaidAudit_audited_formSearch #toolbar',                // 工具按钮用哪个容器
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
                checkbox: true,
                width:'3%'
            },{
                field: 'jcghid',
                title: '借出归还ID',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'jcjyid',
                title: '借出借用id',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'ghdh',
                title: '归还单号',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            },{
                field: 'bmmc',
                title: '部门',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'dwlxmc',
                title: '单位类型',
                width: '15%',
                align: 'left',
                visible: true
            },{
                field: 'dwmc',
                title: '单位',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'ghrq',
                title: '归还日期',
                width: '10%',
                align: 'left',
                sortable:true,
                visible:true
            },{
                field: 'sfzfyf',
                title: '支付运费',
                width: '8%',
                align: 'left',
                sortable:true,
                formatter:sfzfyfFormat,
                visible:true
            },{
                field: 'bz',
                title: '备注',
                width: '20%',
                align: 'left',
                visible: true
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
                formatter:repaidAudit_wcbjformat,
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	repaid_audit_DealById(row.jcghid,'view',$("#repaidAudit_audit_formSearch #btn_view").attr("tourl"));
             },
		});
        $("#repaidAudit_audited_formSearch #repaidAudit_audited_list").colResizable({
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
            sortLastName: "jcghgl.jcghid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
//		return getrepaidAuditSearchData(map);
		var repaidAudit_select=$("#repaidAudit_audited_formSearch #repaidAudit_select").val();
		var repaidAudit_input=$.trim(jQuery('#repaidAudit_audited_formSearch #repaidAudit_input').val());
        if(repaidAudit_select=="0"){
            map["entire"]=repaidAudit_input
        }else if(repaidAudit_select=="1"){
            map["ghdh"]=repaidAudit_input
        }else if(repaidAudit_select=="2"){
            map["bmmc"]=repaidAudit_input
        }
		map["dqshzt"] = 'ysh';
		return map;
	};
	return oTableInit;
}

var viewRepaidAuditConfig = {
		width		: "1200px",
		modalName	:"viewRepaidAuditModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

//待审核列表的按钮路径
function repaid_audit_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $('#repaidAudit_formAudit #urlPrefix').val() + tourl; 
	if(action=="view"){
	var url=tourl+"?jcghid="+id;
		$.showDialog(url,'查看领料信息',viewRepaidAuditConfig);
	}else if(action=="audit"){
		showAuditDialogWithLoading({
			id: id,
			type: 'AUDIT_REPAID',
			url:tourl,
			data:{'ywzd':'jcghid'},
			title:"借出归还审核",
			preSubmitCheck:'preSubmitRepaid',
			prefix:$('#repaidAudit_formAudit #urlPrefix').val(),
			callback:function(){
				searchrepaid_audit_Result(true);//回调
		},
			dialogParam:{width:1600}
		});
	}
}

function preSubmitRepaid(){
        let jgdm= $("#repaidForm #bmdm").val()
        if(!jgdm){
            $.alert("所选部门存在异常！");
            return false;
        }
        var $this = this;
        var opts = $this["options"]||{};
        var json = [];
        if(t_map.rows != null && t_map.rows.length > 0){
            for(var i=0;i<t_map.rows.length;i++){
                if(!t_map.rows[i].ghsl){
                    $.alert("归还数量不能为空！");
                    return false;
                }
                if(parseFloat(t_map.rows[i].ghsl)==0) {
                    $.alert("归还数量不能为0！");
                    return false;
                }
                if(parseFloat(t_map.rows[i].ghsl)>parseFloat(t_map.rows[i].khsl)) {
                    $.alert("归还数量不能大于可还数量！");
                    return false;
                }
                var sz = {"jymxid":'',"jyxxid":'',"hwid":'',"ghsl":'',"bz":'',"sfyzgh":'',"ckid":'',"kwbh":'',"wlid":'',"ghmxid":'',"zsh":''};
                sz.jymxid = t_map.rows[i].jymxid;
                sz.jyxxid = t_map.rows[i].jyxxid;
                sz.hwid = t_map.rows[i].hwid;
                sz.ghsl = t_map.rows[i].ghsl;
                sz.bz = t_map.rows[i].bz;
                sz.sfyzgh = t_map.rows[i].sfyzgh;
                sz.ckid = t_map.rows[i].ckid;
                sz.kwbh = t_map.rows[i].kwbh;
                sz.wlid = t_map.rows[i].wlid;
                sz.ghmxid = t_map.rows[i].ghmxid;
                sz.zsh = t_map.rows[i].zsh;
                json.push(sz);
            }
            $("#repaidForm #ghmx_json").val(JSON.stringify(json));
            $("#repaidForm #cklist").val("");
            $("#repaidForm #kwlist").val("");
        }else{
            $.alert("归还信息不能为空！");
            return false;
        }
	return true;
}

var repaidAudit_audit_oButtton=function(){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#repaidAudit_audit_formSearch #btn_query");//模糊查询（待审核）
		var btn_queryAudited = $("#repaidAudit_audited_formSearch #btn_query");//模糊查询（审核历史）
    	var btn_cancelAudit = $("#repaidAudit_audited_formSearch #btn_cancelAudit");//取消审核（审核历史）
    	var btn_view = $("#repaidAudit_audit_formSearch #btn_view");//查看页面（待审核）
    	var btn_audit = $("#repaidAudit_audit_formSearch #btn_audit");//审核（待审核）
    	
    	/*-----------------------模糊查询(审核列表)------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchrepaid_audit_Result(true);
    		});
		}
		
		/*-----------------------模糊查询(审核记录)------------------------------------*/
		if(btn_queryAudited!=null){
			btn_queryAudited.unbind("click").click(function(){
				searchrepaid_audited_Result(true);
    		});
		}
		
		/*---------------------------查看页面--------------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row=$('#repaidAudit_audit_formSearch #repaidAudit_auditing_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				repaid_audit_DealById(sel_row[0].jcghid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		
		/*---------------------------审核--------------------------------*/
    	btn_audit.unbind("click").click(function(){
    		var sel_row = $('#repaidAudit_audit_formSearch #repaidAudit_auditing_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			repaid_audit_DealById(sel_row[0].jcghid,"audit",btn_audit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*---------------------------查看审核记录--------------------------------*/
    	//选项卡切换事件回调
    	$('#repaidAudit_formAudit #repaidAudit_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
    		var _hash = e.target.hash.replace("#",'');
    		if(!e.target.isLoaded){//只调用一次
    			if(_hash=='repaidAudit_auditing'){
    				var oTable= new repaidAudit_audit_TableInit();
    				oTable.Init();
    				
    			}else{
    				var oTable= new repaidAudit_audited_TableInit();
    				oTable.Init();
    			}
    			e.target.isLoaded = true;
    		}else{//重新加载
    			//$(_gridId + ' #repaidAudit_audited_list').bootstrapTable('refresh');
    		}
    	}).first().trigger('shown.bs.tab');//触发第一个选中事件
    	
    	/*-------------------------------------取消审核-----------------------------------*/
    	if(btn_cancelAudit!=null){
    		btn_cancelAudit.unbind("click");
    		btn_cancelAudit.click(function(){
    			var repaidAudit_params=[];
    			repaidAudit_params.prefix=$('#repaidAudit_formAudit #urlPrefix').val();
    			cancelAudit($('#repaidAudit_audited_formSearch #repaidAudit_audited_list').bootstrapTable('getSelections'),function(){
    				searchrepaid_audited_Result();
    			},null,repaidAudit_params);
    		})
    	}
    	/*-----------------------------------------------------------------------------*/
	}
	return oInit;
}


function searchrepaid_audit_Result(isTurnBack){
	if(isTurnBack){
		$('#repaidAudit_audit_formSearch #repaidAudit_auditing_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#repaidAudit_audit_formSearch #repaidAudit_auditing_list').bootstrapTable('refresh');
	}
}

function searchrepaid_audited_Result(isTurnBack){
	if(isTurnBack){
		$('#repaidAudit_audited_formSearch #repaidAudit_audited_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#repaidAudit_audited_formSearch #repaidAudit_audited_list').bootstrapTable('refresh');
	}
}

$(function(){
	var oTable= new repaidAudit_audit_TableInit();
	oTable.Init();
	
	var oButtonInit = new repaidAudit_audit_oButtton();
	oButtonInit.Init();
	
	// 所有下拉框添加choose样式
	jQuery('#repaidAudit_audit_formSearch .chosen-select').chosen({width: '100%'});
	jQuery('#repaidAudit_audited_formSearch .chosen-select').chosen({width: '100%'});
})