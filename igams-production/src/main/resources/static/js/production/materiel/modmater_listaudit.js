
var modMaterAudit_turnOff=true;

var modMaterAuditing_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#modMater_auditing #tb_list').bootstrapTable({
            url: $('#modMater_formAudit #urlPrefix').val()+'/production/materiel/pageGetListModMaterAudit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#modMater_auditing #toolbar',                //工具按钮用哪个容器
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
            uniqueId: "lsid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'lsid',
                title: '临时ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'wlid',
                title: '物料ID',
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
                field: 'wlbm',
                title: '物料编码',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'wllbmc',
                title: '物料类别',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'wlzlbmc',
                title: '物料子类别',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'wlmc',
                title: '物料名称',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'scs',
                title: '生产商',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'ychh',
                title: '原厂货号',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'gg',
                title: '规格',
                titleTooltip:'规格',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'jldw',
                title: '单位',
                titleTooltip:'单位',
                width: '4%',
                align: 'right',
                visible: false
            },{
                field: 'sfwxp',
                title: '危险品',
                width: '6%',
                align: 'left',
                visible: true,
                formatter:adwxpFormat
            },{
                field: 'bzq',
                title: '保质期',
                width: '6%',
                align: 'left',
                visible: true,
                formatter:bzqFormat
            },{
                field: 'bzqflg',
                title: '保质期标记',
                titleTooltip:'保质期标记',
                width: '6%',
                align: 'right',
                visible: false,
            },{
                field: 'bctj',
                title: '保存条件',
                align: 'left',
                width: '10%',
                visible: true
            },{
                field: 'jwlbm',
                title: '旧物料编码',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'sqrxm',
                title: '申请人员',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '16%',
                align: 'left',
                sortable: true,
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
            	modMaterAuditDealById(row.lsid, 'view',$("#modMater_auditing #btn_view").attr("tourl"));
            },
        });
        $("#modMater_auditing #tb_list").colResizable({
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
            sortLastName: "wllsb.lsid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

    	var cxtj = $("#modMater_auditing #cxtj").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#modMater_auditing #cxnr').val());
    	// '0':'物料名称','1':'生产商'
    	if (cxtj == "0") {
    		map["wlmc"] = cxnr;
    	}else if (cxtj == "1") {
    		map["scs"] = cxnr;
    	}
    	map["dqshzt"] = 'dsh';
    	// 物料类别
//    	var wllb = jQuery('#modMater_auditing #wllb_id_tj').val();
//    	map["wllbs"] = wllb;
    	// 物料子类别
//    	var wlzlb = jQuery('#modMater_auditing #wlzlb_id_tj').val();
//    	map["wlzlbs"] = wlzlb;

    	// 是否危险品
//    	var sfwxp = jQuery('#modMater_auditing #sfwxp_id_tj').val();
//    	map["sfwxps"] = sfwxp;
    	
    	return map;
    };
    return oTableInit;
};

var modMaterAudited_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#modMater_audited #tb_list').bootstrapTable({
            url: $('#modMater_formAudit #urlPrefix').val()+'/production/materiel/pageGetListModMaterAudit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#modMater_audited #toolbar',                //工具按钮用哪个容器
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
                checkbox: true
            }, {
                field: 'wlid',
                title: '物料ID',
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
                field: 'wllbmc',
                title: '物料类别',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'wlzlbmc',
                title: '物料子类别',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'wlmc',
                title: '物料名称',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'scs',
                title: '生产商',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'gg',
                title: '规格',
                titleTooltip:'规格',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'jldw',
                title: '单位',
                titleTooltip:'单位',
                width: '4%',
                align: 'right',
                visible: false
            },{
                field: 'bzq',
                title: '保质期',
                width: '10%',
                align: 'left',
                visible: false,
                formatter:bzqFormat
            },{
                field: 'bzqflg',
                title: '保质期标记',
                titleTooltip:'保质期标记',
                width: '6%',
                align: 'right',
                visible: false,
            },{
                field: 'bctj',
                title: '保存条件',
                align: 'left',
                visible: false
            },{
                field: 'jwlbm',
                title: '旧物料编码',
                width: '10%',
                align: 'left',
                visible: false
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
                field: 'shxx_shyj',
                title: '审核意见',
                width: '10%',
                align: 'left',
                sortable: true,
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
            	modMaterAuditDealById(row.lsid, 'view',$("#modMater_auditing #btn_view").attr("tourl"));
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
            sortLastName: "wllsb.lsid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

    	var cxtj_audited = $("#modMater_audited #cxtj_audited").val();
    	// 查询条件
    	var cxnr_audited = $.trim(jQuery('#modMater_audited #cxnr_audited').val());
    	// '0':'来源编号','1':'标本编号','2':'冰箱号','3':'抽屉号','4':'盒子号','5':'备注'
    	if (cxtj == "0") {
    		map["wlmc"] = cxnr;
    	}else if (cxtj == "1") {
    		map["scs"] = cxnr;
    	}else if (cxtj == "2") {
    		map["sqrxm"] = cxnr;
    	}
    	
    	// 通过开始日期
    	var sqsjstart = jQuery('#modMater_audited #sqsjstart').val();
    	map["sqsjstart"] = sqsjstart;
    	
    	// 通过结束日期
    	var sqsjend = jQuery('#modMater_audited #sqsjend').val();
    	map["sqsjend"] = sqsjend;
    	map["dqshzt"] = 'ysh';
    	return map;
    };
    return oTableInit;
};

var viewModMaterAuditConfig = {
	width		: "800px",
	modalName	: "viewmodmaterModal",
	formName	: "viewmodmater_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

//危险品格式化
function adwxpFormat(value,row,index) {
    if (row.sfwxp == '0') {
        return '否';
    }
    else{
        return '是';
    }
}
//保质期格式化
function bzqFormat(value,row,index) {
    if (row.bzqflg == '1') {
    	return row.bzq;
    }else {
    	return  row.bzq+'个月';
    }
}
var batchAuditModMaterConfig = {
	width		: "800px",
	modalName	: "batchAuditModModal",
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
				var sel_row = $('#modMater_auditing #tb_list').bootstrapTable('getSelections');//获取选择行数据
				//创建objectNode
				var cardiv =document.createElement("div");
				cardiv.id="cardiv";
				var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span style="color:red;font-weight:600;">/' + sel_row.length + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
				cardiv.innerHTML=s_str;
				//将上面创建的P元素加入到BODY的尾部
				document.body.appendChild(cardiv);
				
				submitForm(opts["formName"]||"batchAuditAjaxForm",function(responseText,statusText){
					//1.启动进度条检测
					setTimeout("checkModAuditCheckStatus(2000,'"+ loadYmCode + "')",1000);
					
					//绑定导出取消按钮事件
					$("#exportCancel").click(function(){
						//先移除导出提示，然后请求后台
						if($("#cardiv").length>0) $("#cardiv").remove();
						$.ajax({
							type : "POST",
							url : $('#modMater_formAudit #urlPrefix').val()+"/systemcheck/auditProcess/cancelAuditProcess",
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
var checkModAuditCheckStatus = function(intervalTime,loadYmCode){
	$.ajax({
		type : "POST",
		url : $('#modMater_formAudit #urlPrefix').val()+"/systemcheck/auditProcess/commCheckAudit",
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
					setTimeout("checkModAuditCheckStatus('"+intervalTime+"',"+ loadYmCode +")",intervalTime)
				}
			}else{
				if(data.status == "2"){
					if($("#cardiv")) $("#cardiv").remove();
					$.success(data.msg,function(){
						$.closeModal("batchAuditModModal");
						searchModMaterAudit();
					});
				}else if(data.status == "0"){
					if($("#cardiv")) $("#cardiv").remove();
					$.alert(data.msg,function(){
						$.closeModal("batchAuditModModal");
						searchModMaterAudit();
					});
				}else{
					if($("#cardiv")) $("#cardiv").remove();
					$.error(data.msg,function(){
						$.closeModal("batchAuditModModal");
						searchModMaterAudit();
					});
				}
			}
		}
	});
}
//按钮动作函数
function modMaterAuditDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl=$('#modMater_formAudit #urlPrefix').val()+tourl;
	if(action =='view'){
		var url= tourl + "?lsid=" +id;
		$.showDialog(url,'查看申请',viewModMaterAuditConfig);
	}else if(action =='audit'){
		var url= tourl;
		showAuditDialogWithLoading({
			id: id,
			type: 'AUDIT_METRIELMOD',
			url:tourl,
			data:{'ywzd':'lsid'},
			title:$("#modMater_auditing #btn_audit").attr("gnm"),
			preSubmitCheck:'preSubmitMateriel',
			prefix:$('#modMater_formAudit #urlPrefix').val(),
			callback:function(){
				searchModMaterAudit();//回调
			},
			dialogParam:{width:1000}
		});
	}else if(action =='batchaudit'){
		var url= tourl + "?ywids=" +id+"&shlb=AUDIT_METRIELMOD&ywzd=lsid&business_url="+tourl;
		$.showDialog(url,'批量审核',batchAuditModMaterConfig);
	}
}

function preSubmitMateriel(){
	return true;
}


var modMaterAudit_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_batchaudit = $("#modMater_auditing #btn_batchaudit");
    	var btn_audit = $("#modMater_auditing #btn_audit");
    	var btn_mod = $("#modMater_auditing #btn_mod");
    	var btn_del = $("#modMater_auditing #btn_del");
    	var btn_view = $("#modMater_auditing #btn_view");
    	
    	var btn_query = $("#modMater_auditing #btn_query");
    	var btn_queryAudited = $("#modMater_audited #btn_query");
    	var btn_cancelAudit = $("#modMater_audited #btn_cancelAudit");
    	
    	var wllbBind = $("#modMater_auditing #wllb_id ul li a");

    	//添加日期控件
    	laydate.render({
    	   elem: '#modMater_audited #sqsjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#modMater_audited #sqsjend'
    	  ,theme: '#2381E9'
    	});
    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchModMaterAudit();
    		});
    	}
    	//绑定搜索发送功能
    	if(btn_queryAudited != null){
    		btn_queryAudited.unbind("click").click(function(){
    			searchModMaterAudited();
    		});
    	}
    	//取消审核
    	if(btn_cancelAudit!=null){
    		btn_cancelAudit.unbind("click");
    		btn_cancelAudit.click(function(){
    			var mater_params=[];
				mater_params.prefix=$('#modMater_audited #urlPrefix').val();
    			cancelAudit($('#modMater_audited #tb_list').bootstrapTable('getSelections'),function(){
    				searchModMaterAudited();
    			},null,mater_params);
    		})
    	}
    	btn_audit.unbind("click").click(function(){
    		var sel_row = $('#modMater_auditing #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			modMaterAuditDealById(sel_row[0].lsid,"audit",btn_audit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	//批量审核
    	btn_batchaudit.unbind("click").click(function(){
    		var sel_row = $('#modMater_auditing #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var lsids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			lsids = lsids + ","+ sel_row[i].lsid;
    		}
    		lsids = lsids.substr(1);
    		modMaterAuditDealById(lsids,"batchaudit",btn_batchaudit.attr("tourl"));
    	});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#modMater_auditing #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			modMaterAuditDealById(sel_row[0].lsid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
    	//选项卡切换事件回调
    	$('#modMater_formAudit #audit_mod a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
    		var _hash = e.target.hash.replace("#",'');
    		var _gridId = "#modMater_"+ _hash;
    		if(!e.target.isLoaded){//只调用一次
    			if(_hash=='auditedmod'){
	    			var oTable = new modMaterAudited_TableInit();
	    		    oTable.Init();
    			}else{
    				var oTable = new modMaterAuditing_TableInit();
	    		    oTable.Init();
    			}
    			e.target.isLoaded = true;
    		}else{//重新加载
    			//$(_gridId + ' #tb_list').bootstrapTable('refresh');
    		}
    	}).first().trigger('shown.bs.tab');//触发第一个选中事件
    	
    	/**显示隐藏**/      
//    	$("#modMater_auditing #sl_searchMore").on("click", function(ev){ 
//    		var ev=ev||event;
//    		if(modMaterAudit_turnOff){
//    			$("#modMater_auditing #searchMore").slideDown(200);
//    			modMaterAudit_turnOff=false;
//    			this.innerHTML="基本筛选";
//    		}else{
//    			$("#modMater_auditing #searchMore").slideUp(200);
//    			modMaterAudit_turnOff=true;
//    			this.innerHTML="高级筛选";
//    		}
//    		ev.cancelBubble=true;
//    		//showMore();
//    	});
    	
    	//绑定物料类别的单击事件
    	if(wllbBind!=null){
    		wllbBind.on("click", function(){
    			setTimeout(function(){
    				getAuditWlzlbs();
    			}, 10);
    		});
    	}
    };

    return oInit;
};


//物料子类别的取得
function getAuditWlzlbs() {
	// 物料类别
	var wllb = jQuery('#modMater_auditing #wllb_id_tj').val();
	if (!isEmpty(wllb)) {
		wllb = "'" + wllb + "'";
		jQuery("#modMater_auditing #wlzlb_id").removeClass("hidden");
	}else{
		jQuery("#modMater_auditing #wlzlb_id").addClass("hidden");
	}
	// 项目类别不为空
	if (!isEmpty(wllb)) {
		var url = $('#modMater_formAudit #urlPrefix').val()+"/systemmain/data/ansyGetJcsjList";
		$.ajax({
			type : "POST",
			url : url,
			data : {"fcsids":wllb,"access_token":$("#ac_tk").val()},
			dataType : "json",
			success : function(data){
				if(data.length > 0) {
					var html = "";
					$.each(data,function(i){
						html += "<li>";
						html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('wlzlb','" + data[i].csid + "','modMater_auditing');\" id=\"wlzlb_id_" + data[i].csid + "\"><span>" + data[i].csmc + "</span></a>";
						html += "</li>";
					});
					jQuery("#modMater_auditing #ul_wlzlb").html(html);
					jQuery("#modMater_auditing #ul_wlzlb").find("[name='more']").each(function(){
						$(this).on("click", s_showMoreFn);
					});
				} else {
					jQuery("#modMater_auditing #ul_wlzlb").empty();
					
				}
				jQuery("#modMater_auditing [id^='wlzlb_li_']").remove();
				$("#modMater_auditing #wlzlb_id_tj").val("");
			}
		});
	} else {
		jQuery("#modMater_auditing #div_wlzlb").empty();
		jQuery("#modMater_auditing [id^='wlzlb_li_']").remove();
		$("#modMater_auditing #wlzlb_id_tj").val("");
	}
}

function searchModMaterAudit(){
	//关闭高级搜索条件
//	$("#modMater_auditing #searchMore").slideUp("low");
//	modMaterAudit_turnOff =true;
//	$("#modMater_auditing #sl_searchMore").html("高级筛选");
	$('#modMater_auditing #tb_list').bootstrapTable('refresh');
}

function searchModMaterAudited(){
	$('#modMater_audited #tb_list').bootstrapTable('refresh');
}

/**
 * 物料申请列表的状态格式化函数
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
    var oTable = new modMaterAuditing_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new modMaterAudit_ButtonInit();
    oButtonInit.Init();
	//所有下拉框添加choose样式
	jQuery('#modMater_auditing .chosen-select').chosen({width: '100%'});
	jQuery('#modMater_audited .chosen-select').chosen({width: '100%'});
	
	// 初始绑定显示更多的事件
	//var showMore = function() {
	$("#modMater_auditing [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	
});
