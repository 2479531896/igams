
var sampApplyAudit_turnOff=true;

var sampApplyAuditing_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#sampApply_auditing #tb_list').bootstrapTable({
            url: '/sample/apply/pageGetListSampApplyAudit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#sampApply_auditing #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "shgc.sqsj",				//排序字段
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
            uniqueId: "sqid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'ybid',
                title: '标本ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'sqid',
                title: '申请ID',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'shxx_dqgwmc',
                title: '当前审核岗位名称',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'shxx_shid',
                title: '审核ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'yblxmc',
                title: '标本类型',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'ybbh',
                title: '标本编号',
                width: '14%',
                align: 'left',
                visible: true
            }, {
                field: 'ytmc',
                title: '申请用途',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'sqs',
                title: '申请数',
                width: '5%',
                align: 'left',
                visible: true
            }, {
                field: 'sqrxm',
                title: '申请人员',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'sqsj',
                title: '申请时间',
                width: '16%',
                align: 'left',
                visible: true
            },{
                field: 'dw',
                title: '单位',
                align: 'right',
                visible: true
            },{
                field: 'nd',
                title: '浓度',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'zt',
                title: '状态',
                width: '10%',
                align: 'left',
                formatter:ybsqztFormat,
                visible: true
            },{
                field: 'lrsj',
                align: 'center',
                title: '录入时间',
                visible: false
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	SampApplyAuditDealById(row.sqid, 'view',$("#sampApply_auditing #btn_view").attr("tourl"));
            },
        });
        $("#sampApply_auditing #tb_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true}        
        );
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
            sortLastName: "sq.sqid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

    	var cxtj = $("#sampApply_auditing #cxtj").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#sampApply_auditing #cxnr').val());
    	// '0':'来源编号','1':'标本编号','2':'冰箱号','3':'抽屉号','4':'盒子号','5':'备注'
    	if (cxtj == "0") {
    		map["sqs"] = cxnr;
    	}else if (cxtj == "1") {
    		map["ybbh"] = cxnr;
    	}else if (cxtj == "2") {
    		map["bxh"] = cxnr;
    	}else if (cxtj == "3") {
    		map["chth"] = cxnr;
    	}else if (cxtj == "4") {
    		map["hzh"] = cxnr;
    	}else if (cxtj == "5") {
    		map["bz"] = cxnr;
    	}
    	map["dqshzt"] = 'dsh';
    	// 标本类型
    	var yblx = jQuery('#sampApply_auditing #yblx_id_tj').val();
    	map["yblxs"] = yblx;
    	
    	// 用途
    	var yt = jQuery('#sampApply_auditing #yt_id_tj').val();
    	map["yts"] = yt;
    	
    	return map;
    };
    return oTableInit;
};

var sampApplyAudited_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#sampApply_audited #tb_list').bootstrapTable({
            url: '/sample/apply/pageGetListSampApplyAudit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#sampApply_audited #toolbar',                //工具按钮用哪个容器
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
            columns: [{
                checkbox: true
            }, {
                field: 'ybid',
                title: '标本ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'sqid',
                title: '申请ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'shxx_shid',
                title: '审核ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'yblxmc',
                title: '标本类型',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'ybbh',
                title: '标本编号',
                width: '14%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'ytmc',
                title: '申请用途',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'sqs',
                title: '申请数',
                width: '5%',
                align: 'left',
                visible: true
            }, {
                field: 'sqrxm',
                title: '申请人员',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                width: '16%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_lrryxm',
                title: '审核人',
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
            	SampApplyAuditDealById(row.sqid, 'view',$("#sampApply_audited #btn_view").attr("tourl"));
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
            sortLastName: "sq.sqid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

    	var cxtj_audited = $("#sampApply_audited #cxtj_audited").val();
    	// 查询条件
    	var cxnr_audited = $.trim(jQuery('#sampApply_audited #cxnr_audited').val());
    	// '0':'来源编号','1':'标本编号','2':'冰箱号','3':'抽屉号','4':'盒子号','5':'备注'
    	if (cxtj_audited == "0") {
    		map["sqs"] = cxnr_audited;
    	}else if (cxtj_audited == "1") {
    		map["ybbh"] = cxnr_audited;
    	}else if (cxtj_audited == "2") {
    		map["bxh"] = cxnr_audited;
    	}else if (cxtj_audited == "3") {
    		map["chth"] = cxnr_audited;
    	}else if (cxtj_audited == "4") {
    		map["hzh"] = cxnr_audited;
    	}else if (cxtj_audited == "5") {
    		map["bz"] = cxnr_audited;
    	}
    	map["dqshzt"] = 'ysh';
    	return map;
    };
    return oTableInit;
};

var auditModSampApplyConfig = {
	width		: "1200px",
	modalName	: "addSampApplyModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				if($("#ajaxForm #sqs").val() > ($("#sampApply_ajaxForm #ysl").val() - $("#sampApply_ajaxForm #yyds").val())){
					$.error("申请数量不允许超过库存数",function() {
					});
					return false;
				}
				
				if($("#ajaxForm #yt").val() == ""){
					$.error("请填写申请用途",function() {
					});
					return false;
				}

				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchSampApplyAudit();
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

var viewSampApplyAuditConfig = {
	width		: "800px",
	modalName	: "viewSampApplyModal",
	formName	: "viewsampApply_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var batchAuditSampApplyConfig = {
	width		: "800px",
	modalName	: "batchAuditModal",
	formName	: "batchAuditAjaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#batchAuditAjaxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#batchAuditAjaxForm input[name='access_token']").val($("#ac_tk").val());
				var loadYmCode = $("#batchAuditAjaxForm #batchaudit_loadYmCode").val();
				var sel_row = $('#sampApply_auditing #tb_list').bootstrapTable('getSelections');//获取选择行数据
				//创建objectNode
				var cardiv =document.createElement("div");
				cardiv.id="cardiv";
				var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span style="color:red;font-weight:600;">/' + sel_row.length + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
				cardiv.innerHTML=s_str;
				//将上面创建的P元素加入到BODY的尾部
				document.body.appendChild(cardiv);
				
				submitForm(opts["formName"]||"batchAuditAjaxForm",function(responseText,statusText){
					//1.启动进度条检测
					setTimeout("checkSampleApplyAuditCheckStatus(2000,'"+ loadYmCode + "')",1000);
					
					//绑定导出取消按钮事件
					$("#exportCancel").click(function(){
						//先移除导出提示，然后请求后台
						if($("#cardiv").length>0) $("#cardiv").remove();
						$.ajax({
							type : "POST",
							url : "/systemcheck/auditProcess/cancelAuditProcess",
							data : {"loadYmCode" : loadYmCode+"","access_token":$("#ac_tk").val()},
							dataType : "json",
							success:function(data){
								if(data != null && data.result==false){
									if(data.msg && data.msg!="")
										$.error(data.msg);
								}
							}
						});
					});
				});
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

//上传后自动提交服务器检查进度
var checkSampleApplyAuditCheckStatus = function(intervalTime,loadYmCode){
	$.ajax({
		type : "POST",
		url : "/systemcheck/auditProcess/commCheckAudit",
		data : {"loadYmCode":loadYmCode,"access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.cancel){//取消则直接return
				return;
			}
			if(data.result==false){
				if(data.status == "-2"){
					$.error(data.msg);
					if($("#cardiv")) $("#cardiv").remove();
				}
				else{
					if(intervalTime < 5000)
						intervalTime = intervalTime + 1000;
					if($("#exportCount")){
						$("#exportCount").html(data.currentCount)
					}
					setTimeout("checkSampleApplyAuditCheckStatus("+intervalTime+","+ loadYmCode +")",intervalTime)
				}
			}else{
				if(data.status == "2"){
					if($("#cardiv")) $("#cardiv").remove();
					$.success(data.msg,function(){
						$.closeModal("batchAuditModal");
						searchSampApplyAudit();
					});
				}else if(data.status == "0"){
					if($("#cardiv")) $("#cardiv").remove();
					$.alert(data.msg,function(){
						$.closeModal("batchAuditModal");
						searchSampApplyAudit();
					});
				}else{
					if($("#cardiv")) $("#cardiv").remove();
					$.error(data.msg,function(){
						$.closeModal("batchAuditModal");
						searchSampApplyAudit();
					});
				}
			}
		}
	});
}

//按钮动作函数
function SampApplyAuditDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?sqid=" +id;
		$.showDialog(url,'查看申请',viewSampApplyAuditConfig);
	}else if(action =='edit'){
		var url= tourl;
		$.showDialog(url,'修改申请',auditModSampApplyConfig);
	}else if(action =='audit'){
		var url= tourl;
		showAuditDialogWithLoading({
			id: id,
			type: 'AUDIT_SAMPAPPLY',
			url:$("#sampApply_auditing #btn_audit").attr("tourl"),
			data:{'ywzd':'sqid'},
			title:$("#sampApply_auditing #btn_audit").attr("gnm"),
			preSubmitCheck:'preSubmitApply',
			callback:function(){
				searchSampApplyAudit();//回调
			},
			dialogParam:{width:1000}
		});
	}else if(action =='batchaudit'){
		var url= tourl + "?ywids=" +id+"&shlb=AUDIT_SAMPAPPLY&ywzd=sqid&business_url="+tourl;
		$.showDialog(url,'批量审核',batchAuditSampApplyConfig);
	}
}

function preSubmitApply(){
	
	$("#sampApply_ajaxForm input[name='access_token']").val($("#ac_tk").val());
	
	if($("#sampApply_ajaxForm #sqs").val() > ($("#sampApply_ajaxForm #ysl").val())){
		$.error("申请数量不允许超过库存数",function() {
		});
		return false;
	}
	
	if($("#sampApply_ajaxForm #yt").val() == ""){
		$.error("请填写申请用途",function() {
		});
		return false;
	}
	return true;
}


var sampApplyAudit_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_audit = $("#sampApply_auditing #btn_audit");
    	var btn_mod = $("#sampApply_auditing #btn_mod");
    	var btn_del = $("#sampApply_auditing #btn_del");
    	var btn_view = $("#sampApply_auditing #btn_view");
    	var btn_batchaudit = $("#sampApply_auditing #btn_batchaudit");
    	
    	var btn_query = $("#sampApply_auditing #btn_query");
    	var btn_queryAudited = $("#sampApply_audited #btn_queryAudited");
    	var btn_cancelAudit = $("#sampApply_audited #btn_cancelAudit");


    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchSampApplyAudit(true);
    		});
    	}
    	//绑定搜索发送功能
    	if(btn_queryAudited != null){
    		btn_queryAudited.unbind("click").click(function(){
    			searchSampApplyAudited(true);
    		});
    	}
    	//取消审核
    	if(btn_cancelAudit!=null){
    		btn_cancelAudit.unbind("click");
    		btn_cancelAudit.click(function(){
    			cancelAudit($('#sampApply_audited #tb_list').bootstrapTable('getSelections'),function(){
    				searchSampApplyAudited();
    			});
    		})
    	}
    	btn_audit.unbind("click").click(function(){
    		var sel_row = $('#sampApply_auditing #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			SampApplyAuditDealById(sel_row[0].sqid,"audit",btn_audit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	//批量审核
    	btn_batchaudit.unbind("click").click(function(){
    		var sel_row = $('#sampApply_auditing #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var sqids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			sqids = sqids + ","+ sel_row[i].sqid;
    		}
    		sqids = sqids.substr(1);
    		SampApplyAuditDealById(sqids,"batchaudit",btn_batchaudit.attr("tourl"));
    	});
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#sampApply_auditing #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			SampApplyAuditDealById(sel_row[0].sqid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#sampApply_auditing #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			SampApplyAuditDealById(sel_row[0].sqid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#sampApply_auditing #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].sqid;
    		}
    		ids = ids.substr(1);
    		$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= btn_del.attr("tourl");
    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								searchSampApplyAudit();
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
    	});
    	
    	//选项卡切换事件回调
    	$('#sampApply_formAudit #audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
    		var _hash = e.target.hash.replace("#",'');
    		var _gridId = "#sampApply_"+ _hash;
    		if(!e.target.isLoaded){//只调用一次
    			if(_hash=='audited'){
	    			var oTable = new sampApplyAudited_TableInit();
	    		    oTable.Init();
    			}else{
    				var oTable = new sampApplyAuditing_TableInit();
	    		    oTable.Init();
    			}
    			e.target.isLoaded = true;
    		}else{//重新加载
    			//$(_gridId + ' #tb_list').bootstrapTable('refresh');
    		}
    	}).first().trigger('shown.bs.tab');//触发第一个选中事件
    	
    	/**显示隐藏**/      
    	$("#sampApply_auditing #sl_searchMore").on("click", function(ev){ 
    		var ev=ev||event;
    		if(sampApplyAudit_turnOff){
    			$("#sampApply_auditing #searchMore").slideDown("low");
    			sampApplyAudit_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#sampApply_auditing #searchMore").slideUp("low");
    			sampApplyAudit_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    		//showMore();
    	});
    };

    return oInit;
};

function searchSampApplyAudit(isTurnBack){
	//关闭高级搜索条件
	$("#sampApply_auditing #searchMore").slideUp("low");
	sampApplyAudit_turnOff =true;
	$("#sampApply_auditing #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#sampApply_auditing #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#sampApply_auditing #tb_list').bootstrapTable('refresh');
	}
}

function searchSampApplyAudited(isTurnBack){
	if(isTurnBack){
		$('#sampApply_audited #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#sampApply_audited #tb_list').bootstrapTable('refresh');
	}
}

/**
 * 标本申请列表的状态格式化函数
 * @returns
 */
function ybsqztFormat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
        return '审核通过';
    }
    else{
        return row.shxx_gwmc + '审核中';
    }
}

$(function(){

    //1.初始化Table
    /*var oTable = new sampApplyAuditing_TableInit();
    oTable.Init();*/

    //2.初始化Button的点击事件
    var oButtonInit = new sampApplyAudit_ButtonInit();
    oButtonInit.Init();
	//所有下拉框添加choose样式
	jQuery('#sampApply_auditing .chosen-select').chosen({width: '100%'});
	jQuery('#sampApply_audited .chosen-select').chosen({width: '100%'});
	
	// 初始绑定显示更多的事件
	//var showMore = function() {
	$("#sampApply_auditing [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	
});
