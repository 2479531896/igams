
var cwAudit_turnOff=true;

var cwAuditing_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#cw_auditing #tb_list').bootstrapTable({
            url: '/inspection/samplenum/pageGetListAuditSampleNum',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#cw_auditing #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
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
            uniqueId: "cwid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '4%'
            }, {
                field: 'cwid',
                title: '错误ID',
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
                field: 'cwlx',
                title: '错误类型',
                width: '26%',
                align: 'left',
                visible: true
            },{
                field: 'cwsj',
                title: '错误时间',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'ysnr',
                title: '原始内容',
                width: '22%',
                align: 'left',
                sortable: true,   
                visible: true
            },{
                field: 'sfcl',
                title: '是否处理',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'clsj',
                title: '处理时间',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'zt',
                title: '状态',
                width: '12%',
                align: 'left',
                formatter:yblztFormat,
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	cwAuditDealById(row.wlid, 'view',$("#cw_auditing #btn_view").attr("tourl"));
            },
        });
        $("#cw_auditing #tb_list").colResizable({
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
            sortLastName: "cw.cwid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            zt: "10"
            //搜索框使用
            //search:params.search
        };

    	var cxtj = $("#cw_auditing #cxtj").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#cw_auditing #cxnr').val());
    	// '0':'物料名称','1':'生产商'
    	if (cxtj == "0") {
    		map["cwlx"] = cxnr;
    	}else if(cxtj == "1"){
    		map["ysnr"] = cxnr;
    	}
    	map["dqshzt"] = 'dsh';
    	
    	return map;
    };
    return oTableInit;
};

var cwAudited_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#cw_audited #tb_list').bootstrapTable({
            url: '/inspection/samplenum/pageGetListAuditSampleNum',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#cw_audited #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
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
                checkbox: true,
                width: '2%'
            }, {
                field: 'cwid',
                title: '错误ID',
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
                field: 'cwlx',
                title: '错误类型',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'cwsj',
                title: '错误时间',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'ysnr',
                title: '原始内容',
                width: '6%',
                align: 'left',
                sortable: true,   
                visible: true
            },{
                field: 'sfcl',
                title: '是否处理',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'clsj',
                title: '处理时间',
                width: '8%',
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
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shsj',
                title: '审核时间',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sftgmc',
                title: '是否通过',
                width: '6%',
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
            	cwAuditDealById(row.wlid, 'view',$("#cw_audited #btn_view").attr("tourl"));
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
            sortLastName: "cw.cwid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

    	var cxtj = $("#cw_audited #cxtj").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#cw_audited #cxnr').val());
    	if (cxtj == "0") {
    		map["cwlx"] = cxnr;
    	}else if(cxtj == "1"){
    		map["ysnr"] = cxnr;
    	}
    	map["dqshzt"] = 'ysh';
    	// 通过开始日期
    	var sqsjstart = jQuery('#cw_audited #sqsjstart').val();
    	map["sqsjstart"] = sqsjstart;
    	
    	// 通过结束日期
    	var sqsjend = jQuery('#cw_audited #sqsjend').val();
    	map["sqsjend"] = sqsjend;
    	return map;
    };
    return oTableInit;
};


var viewCwAuditConfig = {
	width		: "800px",
	modalName	: "viewcwModal",
	formName	: "viewcw_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

//上传后自动提交服务器检查进度
var checkSampleAuditCheckStatus = function(intervalTime,loadYmCode){
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
				    setTimeout("checkSampleAuditCheckStatus("+intervalTime+","+ loadYmCode +")",intervalTime)
				}
			}else{
				if(data.status == "2"){
					if($("#cardiv")) $("#cardiv").remove();
					$.success(data.msg,function(){
						$.closeModal("batchAuditModal");
						searchCwAudited();
					});
				}else if(data.status == "0"){
					if($("#cardiv")) $("#cardiv").remove();
					$.alert(data.msg,function(){
						$.closeModal("batchAuditModal");
						searchCwAudited();
					});
				}else{
					if($("#cardiv")) $("#cardiv").remove();
					$.error(data.msg,function(){
						$.closeModal("batchAuditModal");
						searchCwAudited();
					});
				}
			}
		}
	});
}

var batchAuditCwConfig = {
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
				var sel_row = $('#mater_auditing #tb_list').bootstrapTable('getSelections');//获取选择行数据
				//创建objectNode
				var cardiv =document.createElement("div");
				cardiv.id="cardiv";
				var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span style="color:red;font-weight:600;">/' + sel_row.length + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
				cardiv.innerHTML=s_str;
				//将上面创建的P元素加入到BODY的尾部
				document.body.appendChild(cardiv);
				
				submitForm(opts["formName"]||"batchAuditAjaxForm",function(responseText,statusText){
					//1.启动进度条检测
					setTimeout("checkSampleAuditCheckStatus(2000,'"+ loadYmCode + "')",1000);
					
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



//按钮动作函数
function cwAuditDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?cwid=" +id;
		$.showDialog(url,'查看申请',viewCwAuditConfig);
	}else if(action =='audit'){
		var url= tourl;
		showAuditDialogWithLoading({
			id: id,
			type: 'AUDIT_SAMPNUM',
			url:$("#cw_auditing #btn_audit").attr("tourl"),
			data:{'ywzd':'cwid'},
			title:$("#cw_auditing #btn_audit").attr("gnm"),
			preSubmitCheck:'preSubmitSampleNum',
			callback:function(){
				searchCwAudit();//回调
			},
			dialogParam:{width:1000}
		});
	}
}

function preSubmitSampleNum(){
	return true;
}


var CwAudit_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_audit = $("#cw_auditing #btn_audit");
    	var btn_mod = $("#cw_auditing #btn_mod");
    	var btn_del = $("#cw_auditing #btn_del");
    	var btn_view = $("#cw_auditing #btn_view");
    	
    	var btn_query = $("#cw_auditing #btn_query");
    	var btn_queryAudited = $("#cw_audited #btn_query");
    	var btn_cancelAudit = $("#cw_audited #btn_cancelAudit");
    	
    	var wllbBind = $("#cw_auditing #wllb_id ul li a");

    	//添加日期控件
    	laydate.render({
    	   elem: '#cw_audited #sqsjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#cw_audited #sqsjend'
    	  ,theme: '#2381E9'
    	});
    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchCwAudit();
    		});
    	}
    	//绑定搜索发送功能
    	if(btn_queryAudited != null){
    		btn_queryAudited.unbind("click").click(function(){
    			searchCwAudited();
    		});
    	}
    	//审核
    	btn_audit.unbind("click").click(function(){
    		var sel_row = $('#cw_auditing #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			cwAuditDealById(sel_row[0].cwid,"audit",btn_audit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	//取消审核
    	if(btn_cancelAudit!=null){
    		btn_cancelAudit.unbind("click");
    		btn_cancelAudit.click(function(){
    			cancelAudit($('#cw_audited #tb_list').bootstrapTable('getSelections'),function(){
    				searchCwAudited();
    			});
    		})
    	}
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#cw_auditing #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			cwAuditDealById(sel_row[0].cwid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#cw_auditing #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			cwAuditDealById(sel_row[0].cwid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#cw_auditing #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].cwid;
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
    								searchCwAudit();
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
    	$('#cw_formAudit #audit_cw a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
    		var _hash = e.target.hash.replace("#",'');
    		var _gridId = "#cw_"+ _hash;
    		if(!e.target.isLoaded){//只调用一次
    			if(_hash=='auditedcw'){
	    			var oTable = new cwAudited_TableInit();
	    		    oTable.Init();
    			}else{
    				var oTable = new cwAuditing_TableInit();
	    		    oTable.Init();
    			}
    			e.target.isLoaded = true;
    		}else{//重新加载
    			//$(_gridId + ' #tb_list').bootstrapTable('refresh');
    		}
    	}).first().trigger('shown.bs.tab');//触发第一个选中事件
    	
    	/**显示隐藏**/      
    	$("#cw_formAudit #sl_searchMore").on("click", function(ev){ 
    		var ev=ev||event;
    		if(cwAudit_turnOff){
    			$("#cw_formAudit #searchMore").slideDown(200);
    			cwAudit_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#cw_formAudit #searchMore").slideUp(200);
    			cwAudit_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    		//showMore();
    	});
    };

    return oInit;
};


//物料子类别的取得


function searchCwAudit(){
//	//关闭高级搜索条件
//	$("#cw_auditing #searchMore").slideUp("low");
//	materAudit_turnOff =true;
//	$("#cw_auditing #sl_searchMore").html("高级筛选");
	$('#cw_auditing #tb_list').bootstrapTable('refresh',{pageNumber:1});
}

function searchCwAudited(){
	$('#cw_audited #tb_list').bootstrapTable('refresh',{pageNumber:1});
}

/**
 * 标本量列表的状态格式化函数
 * @returns
 */
function yblztFormat(value,row,index) {
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
    var oTable = new cwAuditing_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new CwAudit_ButtonInit();
    oButtonInit.Init();
	//所有下拉框添加choose样式
	jQuery('#cw_formAudit .chosen-select').chosen({width: '100%'});
	jQuery('#cw_formAudit .chosen-select').chosen({width: '100%'});
	
	// 初始绑定显示更多的事件
	//var showMore = function() {
	$("#cw_formAudit [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	
});
