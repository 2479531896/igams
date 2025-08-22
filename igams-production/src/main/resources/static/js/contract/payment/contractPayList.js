var contract_turnOff=true;
var contractPay_TableInit = function () {
	 var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#contractPay_formSearch #contractPay_list').bootstrapTable({
			url: $("#contractPay_formSearch #urlPrefix").val()+'/contract/payInfo/pagedataGetPayInfoList',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#contractPay_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"fkrq",					// 排序字段
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
			uniqueId: "htfkid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮                 								
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true,
				width: '3%',
			},{
				'field': '',
				'title': '序号',
				'align': 'center',
				'width': '3%',
				'formatter': function (value, row, index) {
					var options = $('#contractPay_formSearch #contractPay_list').bootstrapTable('getOptions');
					return options.pageSize * (options.pageNumber - 1) + index + 1;
				}
			},{
				field: 'htfkid',
				title: '合同付款ID',
				width: '12%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'fkdh',
				title: '付款单号',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'fkzje',
				title: '付款总金额',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'fkrq',
				title: '付款日期',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'zfdxmc',
				title: '支付对象',
				width: '12%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'zwzfrq',
				title: '最晚支付日期',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'fksy',
				title: '付款事由',
				width: '12%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'sqrmc',
				title: '申请人',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'sqbmmc',
				title: '申请部门',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'zt',
				title: '状态',
				width: '10%',
				align: 'left',
				formatter:fkztformat,
				sortable: true,
				visible: true
			}, {
				field: 'shtgsj',
				title: '审核通过时间',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: false
			}, {
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '5%',
				align: 'left',
				formatter:fkqk_czFormat,
				visible: true
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				contractPayById(row.htfkid,'view',$("#contractPay_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#contractPay_formSearch #contractPay_list").colResizable({
			liveDrag:true, 
			gripInnerHtml:"<div class='grip'></div>", 
			draggingClass:"dragging", 
			resizeMode:'fit', 
			postbackSafe:true,
			partialRefresh:true
		})
	};
	// 得到查询的参数
	oTableInit.queryParams = function(params){
	// 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
	// 例如 toolbar 中的参数
	// 如果 queryParamsType = ‘limit’ ,返回参数必须包含
	// limit, offset, search, sort, order
	// 否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
	// 返回false将会终止请求
    var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
    	pageSize: params.limit,   // 页面大小
    	pageNumber: (params.offset / params.limit) + 1,  // 页码
        access_token:$("#ac_tk").val(),
        sortName: params.sort,      // 排序列名
        sortOrder: params.order, // 排位命令（desc，asc）
        sortLastName: "htfkid", // 防止同名排位用
        sortLastOrder: "desc" // 防止同名排位用
        // 搜索框使用
        // search:params.search
    };
    return contractPaySearchData(map);
	};
	return oTableInit;
};

function contractPaySearchData(map){
	var cxtj=$("#contractPay_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#contractPay_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["fkdh"]=cxnr;
	}else if(cxtj=="1"){
		map["zfdxmc"]=cxnr;
	}else if(cxtj=="2"){
		map["fksy"]=cxnr;
	}else if(cxtj=="3"){
		map["sqrmc"]=cxnr;
	}else if(cxtj=="4"){
		map["sqbmmc"]=cxnr;
	}else if (cxtj=="5"){
		map["entire"]=cxnr;
	}
	// // 删除标记
	// var scbjs = jQuery('#contractPay_formSearch #scbj_id_tj').val();
	// map["scbjs"] = scbjs;
	//
	// // 完成标记
	// var wcbj = jQuery('#contractPay_formSearch #wcbj_id_tj').val();
	// map["wcbj"] = wcbj;

	//审核通过时间
	var shtgsjstart = jQuery('#contractPay_formSearch #shtgsjstart').val();
	map["shtgsjstart"] = shtgsjstart;
	var shtgsjend = jQuery('#contractPay_formSearch #shtgsjend').val();
	map["shtgsjend"] = shtgsjend;

	// 创建开始日期
	var fksjstart = jQuery('#contractPay_formSearch #fksjstart').val();
	map["fksjstart"] = fksjstart;

	// 创建结束日期
	var fksjend = jQuery('#contractPay_formSearch #fksjend').val();
	map["fksjend"] = fksjend;

	// 状态
	var zts = jQuery('#contractPay_formSearch #zt_id_tj').val();
	map["zts"] = zts;
	return map;
}
//提供给导出用的回调函数
function ConFkSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="fk.fkzje";
	map["sortLastOrder"]="desc";
	map["sortName"]="fk.lrsj";
	map["sortOrder"]="desc";
	return contractPaySearchData(map);
}
/**
 * 操作按钮格式化函数
 * @returns
 */
function fkqk_czFormat(value,row,index) {
	if (row.zt == '10' && row.scbj =='0' ) {
		return "<span class='btn btn-warning' onclick=\"recallContractPayment('" + row.htfkid + "',event)\" >撤回</span>";
	}else if(row.zt == '10' && row.scbj ==null ){
		return "<span class='btn btn-warning' onclick=\"recallContractPayment('" + row.htfkid + "',event)\" >撤回</span>";
	}else{
		return "";
	}
}

//撤回项目提交
function recallContractPayment(htfkid,event){
	var auditType = $("#contractPay_formSearch #auditType").val();
	var msg = '您确定要撤回该合同吗？';
	var contractpay_params = [];
	contractpay_params.prefix = $("#contractPay_formSearch #urlPrefix").val();
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,htfkid,function(){
				contractPayResult();
			},contractpay_params);
		}
	});
}

function fkztformat(value,row,index){
	if (row.zt == '00') {
		return '未提交';
	}else if (row.zt == '80'){
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htfkid + "\",event,\"AUDIT_CONTRACT_PAYMENT\",{prefix:\"" + $('#contractPay_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
	}else if (row.zt == '15') {

		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htfkid + "\",event,\"AUDIT_CONTRACT_PAYMENT\",{prefix:\"" + $('#contractPay_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
	}else{
        var shr = row.shxx_dqgwmc;
        if(row.dqshr!=null && row.dqshr!=""){
            shr = row.dqshr;
        }
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htfkid + "\",event,\"AUDIT_CONTRACT_PAYMENT\",{prefix:\"" + $('#contractPay_formSearch #urlPrefix').val() + "\"})' >" + shr + "审核中</a>";
	}
}

// function fkbfbformat(value,row,index){
// 	return row.fkbfb+"%";
// }
//
// function htidurl(value,row,index){
// 	return "<a href='javascript:void(0);'  onclick='url(\"" + row.htid + "\" )'>"+row.htnbbh+"</a>";
// }
// function url(htid){
// 	contractPayById(htid,'url',"/contract/contract/viewContract");
// }

var contractPay_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_view = $("#contractPay_formSearch #btn_view");
    	var btn_mod = $("#contractPay_formSearch #btn_mod");
    	var btn_submit = $("#contractPay_formSearch #btn_submit");
		var btn_query = $("#contractPay_formSearch #btn_query");
    	var btn_del=$("#contractPay_formSearch #btn_del");
		var btn_searchexport = $("#contractPay_formSearch #btn_searchexport");
		var btn_selectexport = $("#contractPay_formSearch #btn_selectexport");
		//添加日期控件
    	laydate.render({
    	   elem: '#fksjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#fksjend'
    	  ,theme: '#2381E9'
    	});
		//添加日期控件
		laydate.render({
			elem: '#shtgsjstart'
			,theme: '#2381E9'
		});
		//添加日期控件
		laydate.render({
			elem: '#shtgsjend'
			,theme: '#2381E9'
		});
    	
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			contractPayResult(true);
    		});
    	}

		/* ---------------------------查看列表-----------------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row = $('#contractPay_formSearch #contractPay_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				contractPayById(sel_row[0].htfkid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});

		/* --------------------------- 修改 -----------------------------------*/
		btn_mod.unbind("click").click(function(){
			var sel_row = $('#contractPay_formSearch #contractPay_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
				if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
					contractPayById(sel_row[0].htfkid, "mod", btn_mod.attr("tourl"));
				}else{
					$.alert("该记录在审核中或已审核通过，不允许修改!");
				}
			}else{
				$.error("请选中一行");
			}
		});

		/* ---------------------------提交-----------------------------------*/
		btn_submit.unbind("click").click(function(){
			var sel_row = $('#contractPay_formSearch #contractPay_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				if(sel_row[0].zt !='00' && sel_row[0].zt !='15' && sel_row[0].zt !=null){
					$.error("您好！该条记录已提交或者已通过，不允许重复提交！");
					return false;
				}
				contractPayById(sel_row[0].htfkid,"submit",btn_submit.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		// 	  ---------------------------导出-----------------------------------
		btn_selectexport.unbind("click").click(function(){
			var sel_row = $('#contractPay_formSearch #contractPay_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].htfkid;
				}
				ids = ids.substr(1);
				$.showDialog($('#contractPay_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=HTFK_SELECT&expType=select&ids="+ids
					,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
			}else{
				$.error("请选择数据");
			}
		});

		btn_searchexport.unbind("click").click(function(){
			$.showDialog($('#contractPay_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=HTFK_SEARCH&expType=search&callbackJs=ConFkSearchData"
				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
		});

		/* ------------------------------删除列表-----------------------------*/
		btn_del.unbind("click").click(function(){
			var sel_row = $('#contractPay_formSearch #contractPay_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==0){
				$.error("请至少选中一行");
				return;
			}else {
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids= ids + ","+ sel_row[i].htfkid;
				}
				ids=ids.substr(1);
				$.confirm('您确定要删除所选择的信息吗？',function(result){
					if(result){
						jQuery.ajaxSetup({async:false});
						var url= $('#contractPay_formSearch #urlPrefix').val()+btn_del.attr("tourl");
						jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
							setTimeout(function(){
								if(responseText["status"] == 'success'){
									$.success(responseText["message"],function() {
										contractPayResult();
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
		/*-----------------------显示隐藏------------------------------------*/
		$("#contractPay_formSearch #sl_searchMore").on("click", function(ev){
			var ev=ev||event;
			if(contract_turnOff){
				$("#contractPay_formSearch #searchMore").slideDown("low");
				contract_turnOff=false;
				this.innerHTML="基本筛选";
			}else{
				$("#contractPay_formSearch #searchMore").slideUp("low");
				contract_turnOff=true;
				this.innerHTML="高级筛选";
			}
			ev.cancelBubble=true;
			//showMore();
		});
    };
    	return oInit;
};

function contractPayById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#contractPay_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?htfkid=" +id;
		$.showDialog(url,'付款详细信息',viewPayContractConfig);
	}else if(action =='mod'){
		var url=tourl + "?htfkid=" +id;
		$.showDialog(url,'修改付款信息',addPayContractConfig);
	}else if(action =='submit'){
		var url=tourl + "?htfkid=" +id+"&open_flg=submit";
		$.showDialog(url,'提交付款信息',submitPayContractConfig);
	}else if(action =='audit'){
		var url= tourl;
		showAuditDialogWithLoading({
			id: id,
			type: 'AUDIT_CONTRACT_PAYMENT',
			url:url,
			data:{'ywzd':'htfkid'},
			title:"合同审核",
			preSubmitCheck:'preSubmitRecheck',
			callback:function(){
				contractPayResult();//回调
			},
			dialogParam:{width:1200}
		});
	}else if(action =='url'){
		var url= tourl + "?htid=" +id;
		$.showDialog(url,'合同详细信息',viewContractConfig);
	}
}

var submitPayContractConfig = {
	width		: "1565px",
	modalName	: "submitContractModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "提交",
			className : "btn-primary",
			callback : function() {
				if(!$("#payMod_ajaxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				$("#payMod_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				// $("#payMod_ajaxForm #fkmxJson").val(JSON.stringify($("#payMod_ajaxForm #tb_list").bootstrapTable("getData")));
				submitForm(opts["formName"]||"payMod_ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						if(opts.offAtOnce){
							//新增提交审
							var contractPay_params=[];
							contractPay_params.prefix=$('#payMod_ajaxForm #urlPrefix').val();
							var auditType = $("#payMod_ajaxForm #auditType").val();
							var ywid = $("#payMod_ajaxForm #htfkid").val();
							showAuditFlowDialog(auditType,ywid,function(){
								$.closeModal(opts.modalName);
								contractPayResult();
							},null,contractPay_params);
							$.closeModal(opts.modalName);
						}
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
							preventResubmitForm(".modal-footer > button", false);
						});
					} else{
						$.alert(responseText["message"],function() {
							preventResubmitForm(".modal-footer > button", false);
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

var viewPayContractConfig = {
	width		: "1200px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var viewContractConfig = {
	width		: "1600px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var addPayContractConfig = {
	width		: "1565px",
	modalName	: "addPayModModel",
	formName	: "payMod_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function (){
				if(!$("#payMod_ajaxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				$("#payMod_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				$("#payMod_ajaxForm #fkmxJson").val(JSON.stringify($("#payMod_ajaxForm #tb_list").bootstrapTable("getData")));
				submitForm(opts["formName"]||"payMod_ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								contractPayResult();
							}
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
							preventResubmitForm(".modal-footer > button", false);
						});
					} else{
						$.alert(responseText["message"],function() {
							preventResubmitForm(".modal-footer > button", false);
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

function contractPayResult(isTurnBack){
	//关闭高级搜索条件
	$("#contractPay_formSearch #searchMore").slideUp("low");
	contract_turnOff=true;
	$("#contractPay_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#contractPay_formSearch #contractPay_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#contractPay_formSearch #contractPay_list').bootstrapTable('refresh');
	}
}


$(function() {


	// 1.初始化Table
	var oTable = new contractPay_TableInit();
	oTable.Init();

	//2.初始化Button的点击事件
	var oButtonInit = new contractPay_ButtonInit();
	oButtonInit.Init();

	// 所有下拉框添加choose样式
	jQuery('#contractPay_formSearch .chosen-select').chosen({width: '100%'});

	$("#contractPay_formSearch [name='more']").each(function () {
		$(this).on("click", s_showMoreFn);
	});
});
