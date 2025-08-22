var borrowing_turnOff=true;
var borrowing_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#borrowing_formSearch #borrowing_list").bootstrapTable({
			url: $("#borrowing_formSearch #urlPrefix").val()+'/borrowing/borrowing/pageGetListBorrowing',         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#borrowing_formSearch #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   //是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "jydh",				//排序字段
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
			uniqueId: "jcjyid",                     //每一行的唯一标识，一般为主键列
			showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: [{
				checkbox: true,
				width: '4%'
			},{
				field: 'jydh',
				title: '借用单号',
				width: '10%',
				align: 'left',
				sortable: true,
				visible:true
			},{
				field: 'yjydh',
				title: '原借用单号',
				width: '10%',
				align: 'left',
				sortable: true,
				formatter:yjydhformatter,
				visible:false
			},{
				field: 'u8jydh',
				title: 'U8借用单号',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'dwlxmc',
				title: '单位类型',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'xslxmc',
				title: '销售类型',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'dwmc',
				title: '单位',
				width: '14%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'ysfsmc',
				title: '运输方式',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'zd',
				title: '终端',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'zdqymc',
				title: '终端区域',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'ywymc',
				title: '业务员',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'fzrmc',
				title: '负责人',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'bmmc',
				title: '部门',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'fzdq',
				title: '负责大区',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'jyrq',
				title: '借用日期',
				width: '10%',
				align: 'left',
				sortable: true,
				visible:true
			},{
				field: 'sfzfyf',
				title: '运费',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'lxr',
				title: '联系人',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'lxfs',
				title: '联系方式',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'htbh',
				title: '合同编号',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true,
				formatter:viewYxhtFormatter
			},{
				field: 'shdz',
				title: '收货地址',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'shlxfs',
				title: '收货联系方式',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'kdxx',
				title: '快递信息',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'sfqr',
				title: '是否确认',
				width: '3%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'bz',
				title: '备注',
				width: '12%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'djlx',
				title: '单据类型',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true,
				formatter: djlxFormat,
			},{
				field: 'zt',
				title: '状态',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true,
				formatter: ztformat,
			}, {
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '6%',
				align: 'left',
				formatter:czFormat,
				sortable: true,
				visible: true
			}
			],
			onLoadSuccess:function(map){
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				borrowing__DealById(row.jcjyid,"view",$("#borrowing_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#borrowing_formSearch #borrowing_list").colResizable({
			liveDrag:true,
			gripInnerHtml:"<div class='grip'></div>",
			draggingClass:"dragging",
			resizeMode:'fit',
			postbackSafe:true,
			partialRefresh:true
		})
			  
	};
		oTableInit.queryParams=function(params){
			var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
	        	pageSize: params.limit,   // 页面大小
	        	pageNumber: (params.offset / params.limit) + 1,  // 页码
	            access_token:$("#ac_tk").val(),
	            sortName: params.sort,      // 排序列名
	            sortOrder: params.order, // 排位命令（desc，asc）
	            sortLastName: "jcjyid", // 防止同名排位用
	            sortLastOrder: "asc" // 防止同名排位用
	        };
			return getBorrowingSearchData(map);
		}
	return oTableInit;
}
function yjydhformatter(value,row,index) {
	var html = "";
	if(row.yjydh==null){
		html = "";
	}else{
		html += "<a href='javascript:void(0);' onclick=\"queryByJydh('"+row.yjcjyid+"')\">"+row.yjydh+"</a>";

	}
	return html;
}
function queryByJydh(jcjyid ){
	var url=$("#borrowing_formSearch #urlPrefix").val()+"/borrowing/borrowing/pagedataViewBorrowing?jcjyid="+jcjyid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'借用信息',ViewBorrowConfig);
}
var ViewBorrowConfig = {
	width		: "1600px",
	modalName	:"ViewBorrowConfig",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
function viewYxhtFormatter(value,row,index) {
	var html = "";
	if(row.htbh==null){
		html = "";
	}else{
		html += "<a href='javascript:void(0);' onclick=\"viewYxht('"+row.htid+"')\">"+row.htbh+"</a>";

	}
	return html;
}
function viewYxht(htid){
	var url=$("#borrowing_formSearch #urlPrefix").val()+"/marketingContract/marketingContract/pagedataViewMarketingContract?htid="+htid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'营销合同信息',viewMarketingContractConfig);
}
var viewMarketingContractConfig = {
	width		: "1600px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
//提供给导出用的回调函数
function BorrowingSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="jcjygl.jcjyid";
	map["sortLastOrder"]="desc";
	map["sortName"]="jcjygl.lrsj";
	map["sortOrder"]="desc";
	return getBorrowingSearchData(map);
}
function getBorrowingSearchData(map){
	var cxtj=$("#borrowing_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#borrowing_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr
	}else if(cxtj=="1"){
		map["jydh"]=cxnr
	}else if(cxtj=="2"){
		map["lxr"]=cxnr
	}else if(cxtj=="3"){
		map["bmmc"]=cxnr
	}else if(cxtj=="4"){
		map["dwmc"]=cxnr
	}else if(cxtj=="5"){
		map["khmc"]=cxnr
	}else if(cxtj=="6"){
		map["htbh"]=cxnr
	}else if(cxtj=="7"){
		map["fzdq"]=cxnr
	}else if(cxtj=="8"){
		map["zdqy"]=cxnr
	}else if(cxtj=="9"){
		map["zd"]=cxnr
	}else if(cxtj=="10"){
		map["shdz"]=cxnr
	}else if(cxtj=="11"){
		map["shlxfs"]=cxnr
	}
	// 到货开始日期
	var jysjstart = jQuery('#borrowing_formSearch #jysjstart').val();
	map["jysjstart"] = jysjstart;

	// 到货结束日期
	var jysjend = jQuery('#borrowing_formSearch #jysjend').val();
	map["jysjend"] = jysjend;
	//客户类别
	var khlbs = jQuery('#borrowing_formSearch #khlb_id_tj').val();
	map["khlbs"] = khlbs.replace(/'/g, "");;
	//是否签收
	var sfqss = jQuery('#borrowing_formSearch #sfqs_id_tj').val();
	map["sfqss"] = sfqss.replace(/'/g, "");;
	//是否确认
	var sfqrs = jQuery('#borrowing_formSearch #sfqr_id_tj').val();
	map["sfqrs"] = sfqrs.replace(/'/g, "");;
	//单据类型
	var djlxs = jQuery('#borrowing_formSearch #djlx_id_tj').val();
	map["djlxs"] = djlxs.replace(/'/g, "");
	return map;
}

//借出列表的提交状态格式化函数
function ztformat(value,row,index) {
	if (row.zt == '00') {
		return '未提交';
	}else if (row.zt == '80'){
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.jcjyid + "\",event,\"AUDIT_BORROWING\",{prefix:\"" + $('#borrowing_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
	}else if (row.zt == '15') {
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.jcjyid + "\",event,\"AUDIT_BORROWING\",{prefix:\"" + $('#borrowing_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
	}else{
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.jcjyid + "\",event,\"AUDIT_BORROWING\",{prefix:\"" + $('#borrowing_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
	}
}

//操作按钮格式化函数
function czFormat(value,row,index) {
	//var param = {prefix:$('#receiveMaterielList_formSearch #urlPrefix').val()};
	if (row.zt == '10' && row.scbj =='0' ) {
		return "<span class='btn btn-warning' onclick=\"borrowing_recallReceiveMateriel('" + row.jcjyid + "',event)\" >撤回</span>";
	}else if(row.zt == '10' && row.scbj ==null ){
		return "<span class='btn btn-warning' onclick=\"borrowing_recallReceiveMateriel('" + row.jcjyid + "',event)\" >撤回</span>";
	}else{
		return "";
	}
}
/**
 * 单据类型格式化函数
 * @returns
 */
function djlxFormat(value,row,index) {
	if (row.djlx == '0'||row.djlx == null) {
		return '普通单';
	}else if (row.djlx == '1') {
		return '换货单';
	}
}
//撤回项目提交
function borrowing_recallReceiveMateriel(jcjyid,event){
	var auditType = $("#borrowing_formSearch #auditType").val();
	var msg = '您确定要撤回该借出借用单吗？';
	var receiveMateriel_params = [];
	receiveMateriel_params.prefix = $("#borrowing_formSearch #urlPrefix").val();
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,jcjyid,function(){
				searchBorrowingResult();
			},receiveMateriel_params);
		}
	});
}
		
var borrowing_oButtton=function(){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#borrowing_formSearch #btn_query");
		var btn_view = $("#borrowing_formSearch #btn_view");
		var btn_add = $("#borrowing_formSearch #btn_add");
		var btn_resubmit=$("#borrowing_formSearch #btn_resubmit");// 重新提交
		var btn_repaid=$("#borrowing_formSearch #btn_repaid");// 归还
		var btn_del = $("#borrowing_formSearch #btn_del");// 删除
		var btn_discard = $("#borrowing_formSearch #btn_discard");// 废弃
		var btn_ordertransform = $("#borrowing_formSearch #btn_ordertransform");// 借出借用单转销售订单
		var btn_searchexport = $("#borrowing_formSearch #btn_searchexport");
		var btn_selectexport = $("#borrowing_formSearch #btn_selectexport");
		var btn_logisticsuphold = $("#borrowing_formSearch #btn_logisticsuphold");
		var btn_signfor =  $("#borrowing_formSearch #btn_signfor");//物流签收
		var btn_signforconfirm =  $("#borrowing_formSearch #btn_signforconfirm");//物流签收确认
		var btn_jytransform =  $("#borrowing_formSearch #btn_jytransform");//换货
		var btn_copy =  $("#borrowing_formSearch #btn_copy");//复制
		//添加日期控件
		laydate.render({
			elem: '#jysjstart'
			,theme: '#2381E9'
		});
		//添加日期控件
		laydate.render({
			elem: '#jysjend'
			,theme: '#2381E9'
		});
		/*-----------------------模糊查询------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchBorrowingResult(); 
    		});
		}
		/* ------------------------------删除借出借用信息-----------------------------*/
		btn_del.unbind("click").click(function(){
			var sel_row = $('#borrowing_formSearch #borrowing_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==0){
				$.error("请至少选中一行");
				return;
			}else {
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids= ids + ","+ sel_row[i].jcjyid;
					// if(sel_row[i].zt!= "00") {
					// 	$.error("存在数据不允许删除！");
					// 	return;
					// }
				}
				ids=ids.substr(1);
				$.confirm('您确定要删除所选择的信息吗？',function(result){
					if(result){
						jQuery.ajaxSetup({async:false});
						var url= $("#borrowing_formSearch #urlPrefix").val()+btn_del.attr("tourl");
						jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
							setTimeout(function(){
								if(responseText["status"] == 'success'){
									$.success(responseText["message"],function() {
										searchBorrowingResult();
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
		/* ------------------------------废弃-----------------------------*/
		btn_discard.unbind("click").click(function(){
			var sel_row = $('#borrowing_formSearch #borrowing_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length ==0){
				$.error("请至少选中一行");
				return;
			}
			var ids="";
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
				ids = ids + ","+ sel_row[i].jcjyid;
				if (sel_row[i].zt!='00'){
					$.error("请选择未提交的数据");
					return;
				}
			}
			ids = ids.substr(1);
			$.confirm('您确定要废弃所选择的记录吗？',function(result){
				if(result){
					jQuery.ajaxSetup({async:false});
					var url=  $('#borrowing_formSearch #urlPrefix').val() + btn_discard.attr("tourl");
					jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
						setTimeout(function(){
							if(responseText["status"] == 'success'){
								$.success(responseText["message"],function() {
									searchBorrowingResult(true);
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
		/*--------------------------------重新提交领料信息---------------------------*/
		btn_resubmit.unbind("click").click(function(){
			var sel_row=$('#borrowing_formSearch #borrowing_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
					borrowing__DealById(sel_row[0].jcjyid,"resubmit",btn_resubmit.attr("tourl"));
				}else{
					$.alert("该状态不允许提交!");
				}
			}else{
				$.error("请选中一行");
			}
		});
		/*--------------------------------换货---------------------------*/
		btn_jytransform.unbind("click").click(function(){
			var sel_row=$('#borrowing_formSearch #borrowing_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				borrowing__DealById(sel_row[0].jcjyid,"jytransform",btn_jytransform.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*--------------------------------复制---------------------------*/
		btn_copy.unbind("click").click(function(){
			var sel_row=$('#borrowing_formSearch #borrowing_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				borrowing__DealById(sel_row[0].jcjyid,"copy",btn_copy.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*-------------------新增------------------*/
		btn_add.unbind("click").click(function(){
			borrowing__DealById(null,"add",btn_add.attr("tourl"));
		});
		/*----------------------查看----------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row=$('#borrowing_formSearch #borrowing_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				borrowing__DealById(sel_row[0].jcjyid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*----------------------借出借用单转销售订单----------------------*/
		btn_ordertransform.unbind("click").click(function(){
			var sel_row=$('#borrowing_formSearch #borrowing_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				borrowing__DealById(sel_row[0].jcjyid,"ordertransform",btn_ordertransform.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*----------------------归还----------------------*/
		btn_repaid.unbind("click").click(function(){
			var sel_row=$('#borrowing_formSearch #borrowing_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				if(sel_row[0].zt!='80'){
					$.error("审核未通过无法执行归还操作!");
				}else{
					borrowing__DealById(sel_row[0].jcjyid,"repaid",btn_repaid.attr("tourl"));
				}
				 	// borrowing__DealById(sel_row[0].jcjyid,"repaid",btn_repaid.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		// 	  ---------------------------导出-----------------------------------
		btn_selectexport.unbind("click").click(function(){
			var sel_row = $('#borrowing_formSearch #borrowing_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].jcjyid;
				}
				ids = ids.substr(1);
				$.showDialog($('#borrowing_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=JCJYGL_SELECT&expType=select&ids="+ids
					,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
			}else{
				$.error("请选择数据");
			}
		});

		btn_searchexport.unbind("click").click(function(){
			$.showDialog($('#borrowing_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=JCJYGL_SEARCH&expType=search&callbackJs=BorrowingSearchData"
				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
		});
		/* --------------------------- 物流维护 -----------------------------------*/
		btn_logisticsuphold.unbind("click").click(function(){
			var sel_row = $('#borrowing_formSearch #borrowing_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
				borrowing__DealById(sel_row[0].jcjyid, "logisticsuphold", btn_logisticsuphold.attr("tourl"),sel_row[0].jydh);
			}else{
				$.error("请选中一行");
			}
		});
		/* --------------------------- 物流签收 -----------------------------------*/
		btn_signfor.unbind("click").click(function(){
			var sel_row = $('#borrowing_formSearch #borrowing_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
				borrowing__DealById(sel_row[0].jcjyid, "signfor", btn_signfor.attr("tourl"),sel_row[0].jydh);
			}else{
				$.error("请选中一行");
			}
		});
		/* --------------------------- 物流签收确认 -----------------------------------*/
		btn_signforconfirm.unbind("click").click(function(){
			var sel_row = $('#borrowing_formSearch #borrowing_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
				borrowing__DealById(sel_row[0].jcjyid, "signforconfirm", btn_signforconfirm.attr("tourl"),sel_row[0].jydh);
			}else{
				$.error("请选中一行");
			}
		});
		/*-----------------------显示隐藏------------------------------------*/
		$("#borrowing_formSearch #sl_searchMore").on("click", function(ev){
			var ev=ev||event;
			if(borrowing_turnOff){
				$("#borrowing_formSearch #searchMore").slideDown("low");
				borrowing_turnOff=false;
				this.innerHTML="基本筛选";
			}else{
				$("#borrowing_formSearch #searchMore").slideUp("low");
				borrowing_turnOff=true;
				this.innerHTML="高级筛选";
			}
			ev.cancelBubble=true;
			//showMore();
		});
	}
	return oInit;
}
function borrowing__DealById(id,action,tourl,jydh){
	if(!tourl){
		return;
	}
	tourl=$("#borrowing_formSearch #urlPrefix").val()+tourl;
	if(action=="add"){
		var url=tourl;
		$.showDialog(url,'新增',addBorrowConfig);
	}else if(action=="view"){
		var url=tourl+"?jcjyid="+id;
		$.showDialog(url,'查看信息',ViewBorrowConfig);
	}else if(action=="resubmit"){
		var url=tourl+"?jcjyid="+id;
		$.showDialog(url,'重新提交领料信息',resubmitBorrowConfig);
	}else if(action=="repaid"){
		var url=tourl+"?jcjyid="+id;
		$.showDialog(url,'归还',repaidBorrowConfig);
	}else if(action=="logisticsuphold"){
		var url=tourl+"?ywid="+id+"&ywlx=jcjy&ywdh="+jydh;
		$.showDialog(url,'物流维护',addlogisticsUpholdFormConfig);
	}else if(action =='signfor'){
		var url= tourl+"?ywid="+id+"&ywlx=jcjy&ywdh="+jydh;
		$.showDialog(url,'物流签收',addlogisticsUpholdFormConfig);
	}else if(action =='signforconfirm'){
		var url= tourl+"?ywid="+id+"&ywlx=jcjy&ywdh="+jydh;
		$.showDialog(url,'物流签收确认',addlogisticsUpholdFormConfig);
	}else if(action=="ordertransform"){
		var url=tourl+"?jcjyid="+id;
		$.showDialog(url,'借出借用单转销售单',addSaleConfig);
	}else if(action=="jytransform"){
		var url=tourl+"?jcjyid="+id;
		$.showDialog(url,'借出借用单换货',resubmitBorrowConfig);
	}else if(action=="copy"){
		var url=tourl+"?jcjyid="+id;
		$.showDialog(url,'借出借用单复制',addBorrowConfig);
	}
}
var addSaleConfig = {
	width		: "1550px",
	modalName	: "addSaleModal",
	formName	: "sale_edit_Form",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "保 存",
			className : "btn-primary",
			callback : function() {
				if(!$("#sale_edit_Form").valid()){
					return false;
				}
				var xsmx = JSON.parse($("#sale_edit_Form #xsmx_json").val());
				if(xsmx.length!=0) {
					for (var i = 0; i < xsmx.length; i++) {
						if(xsmx[i].sl==""||xsmx[i].sl==null){
							$.alert("物料编码为  "+xsmx[i].wlbm+"  的数据未填写数量！");
							return false;
						}else if(xsmx[i].hsdj==""||xsmx[i].hsdj==null){
							$.alert("物料编码为  "+xsmx[i].wlbm+"  的数据未填写含税单价！");
							return false;
						}else if(xsmx[i].wsdj==""||xsmx[i].wsdj==null){
							$.alert("物料编码为  "+xsmx[i].wlbm+"  的数据未填写无税单价！");
							return false;
						}else if(xsmx[i].wszje==""||xsmx[i].wszje==null){
							$.alert("物料编码为  "+xsmx[i].wlbm+"  的数据未填写无税总金额！");
							return false;
						}else if(xsmx[i].jsze==""||xsmx[i].jsze==null){
							$.alert("物料编码为  "+xsmx[i].wlbm+"  的数据未填写价税总额！");
							return false;
						}else if(xsmx[i].cplx==""||xsmx[i].cplx==null){
							$.alert("物料编码为  "+xsmx[i].wlbm+"  的数据未选择产品类型！");
							return false;
						}
					}
				}else{
					$.alert("明细不允许为空！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#sale_edit_Form input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"sale_edit_Form",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						if(opts.offAtOnce){
							$.closeModal(opts.modalName);
						}
						//提交审核
						if(responseText["auditType"]!=null){
							var sale_params=[];
							sale_params.prefix=responseText["urlPrefix"];
							showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
								searchBorrowingResult();
							},null,sale_params);
						}else{
							searchBorrowingResult();
						}
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
//物流维护
var addlogisticsUpholdFormConfig = {
	width		: "1000px",
	modalName	: "logisticsUpholdFormModal",
	formName	: "logisticsUpholdForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				var json = [];
				if(t_map.rows != null && t_map.rows.length > 0){
					for(var i=0;i<t_map.rows.length;i++){
						if(t_map.rows[i].kdgs==null || t_map.rows[i].kdgs==''){
							$.alert("快递公司不能为空！");
							return false;
						}
						if(t_map.rows[i].wldh==null || t_map.rows[i].wldh==''){
							$.alert("快递单号不能为空！");
							return false;
						}
						var fjids = $("#wlfj_"+i).val().split(",");
						var sz = {"kdgs":'',"wldh":'',"ywid":'',"wlxxid":'',"qsrq":'',"sfqr":'',"lrsj":'',"fjids":'',"fhrq":'',"yf":''};
						sz.kdgs = t_map.rows[i].kdgs;
						sz.wldh = t_map.rows[i].wldh;
						sz.ywid = t_map.rows[i].ywid;
						sz.wlxxid = t_map.rows[i].wlxxid;
						sz.qsrq = t_map.rows[i].qsrq;
						sz.sfqr = t_map.rows[i].sfqr;
						sz.lrsj = t_map.rows[i].lrsj;
						sz.fhrq = t_map.rows[i].fhrq;
						sz.yf = t_map.rows[i].yf;
						sz.fjids = fjids;
						json.push(sz);
					}
					$("#logisticsUpholdForm #wlmx_json").val(JSON.stringify(json));
				}
				$("#logisticsUpholdForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"logisticsUpholdForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							searchBorrowingResult();
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
var resubmitBorrowConfig = {
	width		: "1600px",
	modalName	: "borrowFormFormModal",
	formName	: "borrowForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#borrowForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				let jgdm= $("#borrowForm #bmdm").val()
				if(!jgdm){
					$.alert("所选部门存在异常！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var json = [];
				if(t_map.rows != null && t_map.rows.length > 0){
					for(var i=0;i<t_map.rows.length;i++){
						if(t_map.rows[i].jcsl==null || t_map.rows[i].jcsl==''){
							$.alert("借用数量不能为0！");
							return false;
						}
						if(t_map.rows[i].jcsl==0){
							$.alert("借用数量不能为0！");
							return false;
						}
						var sz = {"ckhwid":'',"ckqxlx":'',"wlid":'',"jcsl":'',"bz":'',"yjghrq":'',"jyxxid":'',"cplx":''};
						sz.ckhwid = t_map.rows[i].ckhwid;
						sz.jyxxid = t_map.rows[i].jyxxid;
						sz.ckqxlx = t_map.rows[i].ckqxlx;
						sz.wlid = t_map.rows[i].wlid;
						sz.jcsl = t_map.rows[i].jcsl;
						sz.bz = t_map.rows[i].hwllxxbz;
						sz.yjghrq = t_map.rows[i].yjghrq;
						sz.cplx = t_map.rows[i].cplx;
						json.push(sz);
					}
					$("#borrowForm #jymx_json").val(JSON.stringify(json));
				}else{
					$.alert("借用信息不能为空！");
					return false;
				}
				$("#borrowForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"borrowForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						var auditType = $("#borrowForm #auditType").val();
						var receiveMateriel_params=[];
						receiveMateriel_params.prefix=$('#borrowForm #urlPrefix').val();
						if(opts.offAtOnce){
							$.closeModal(opts.modalName);
						}
						//提交审核
						showAuditFlowDialog(auditType, responseText["ywid"],function(){
							searchBorrowingResult();
						},null,receiveMateriel_params);
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

var addBorrowConfig = {
		width		: "1600px",
		modalName	:"addBorrowConfig",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#borrowForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					let jgdm= $("#borrowForm #bmdm").val()
					if(!jgdm){
						$.alert("所选部门存在异常！");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					var json = [];
					if(t_map.rows != null && t_map.rows.length > 0){
						for(var i=0;i<t_map.rows.length;i++){
							if(t_map.rows[i].jcsl==null || t_map.rows[i].jcsl==''){
								$.alert("借用数量不能为0！");
								return false;
							}
							if(t_map.rows[i].jcsl==0){
								$.alert("借用数量不能为0！");
								return false;
							}
							var sz = {"ckhwid":'',"ckqxlx":'',"wlid":'',"jcsl":'',"bz":'',"yjghrq":'',"cplx":''};
							sz.ckhwid = t_map.rows[i].ckhwid;
							sz.ckqxlx = t_map.rows[i].ckqxlx;
							sz.wlid = t_map.rows[i].wlid;
							sz.jcsl = t_map.rows[i].jcsl;
							sz.bz = t_map.rows[i].hwllxxbz;
							sz.yjghrq = t_map.rows[i].yjghrq;
							sz.cplx = t_map.rows[i].cplx;
							json.push(sz);
						}
						$("#borrowForm #jymx_json").val(JSON.stringify(json));
					}else{
						$.alert("借用信息不能为空！");
						return false;
					}
					$("#borrowForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"borrowForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							var auditType = $("#borrowForm #auditType").val();
							var receiveMateriel_params=[];
							receiveMateriel_params.prefix=$('#borrowForm #urlPrefix').val();
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							//提交审核
							showAuditFlowDialog(auditType, responseText["ywid"],function(){
								searchBorrowingResult();
							},null,receiveMateriel_params);
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

var repaidBorrowConfig = {
	width		: "1600px",
	modalName	:"repaidBorrowConfig",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#repaidForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}
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
						var sz = {"jymxid":'',"jyxxid":'',"hwid":'',"ghsl":'',"bz":'',"sfyzgh":'',"ckid":'',"kwbh":'',"wlid":'',"cplx":'',"zsh":'',"sbysid":''};
						sz.jymxid = t_map.rows[i].jymxid;
						sz.jyxxid = t_map.rows[i].jyxxid;
						sz.hwid = t_map.rows[i].hwid;
						sz.ghsl = t_map.rows[i].ghsl;
						sz.bz = t_map.rows[i].bz;
						sz.sfyzgh = t_map.rows[i].sfyzgh;
						sz.ckid = t_map.rows[i].ckid;
						sz.kwbh = t_map.rows[i].kwbh;
						sz.wlid = t_map.rows[i].wlid;
						sz.cplx = t_map.rows[i].cplx;
						sz.zsh = t_map.rows[i].zsh;
						sz.sbysid = t_map.rows[i].sbysid;
						json.push(sz);
					}
					$("#repaidForm #ghmx_json").val(JSON.stringify(json));
					$("#repaidForm #cklist").val("");
					$("#repaidForm #kwlist").val("");
				}else{
					$.alert("归还信息不能为空！");
					return false;
				}
				$("#repaidForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"repaidForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						var auditType = $("#repaidForm #auditType").val();
						var receiveMateriel_params=[];
						receiveMateriel_params.prefix=$('#repaidForm #urlPrefix').val();
						if(opts.offAtOnce){
							$.closeModal(opts.modalName);
						}
						//提交审核
						showAuditFlowDialog(auditType, responseText["ywid"],function(){
							searchBorrowingResult();
						},null,receiveMateriel_params);
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

var ViewBorrowConfig = {
		width		: "1600px",
		modalName	:"ViewBorrowConfig",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
function searchBorrowingResult(isTurnBack){
	if(isTurnBack){
		$('#borrowing_formSearch #borrowing_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#borrowing_formSearch #borrowing_list').bootstrapTable('refresh');
	}
}
$(function(){
	var oTable= new borrowing_TableInit();
		oTable.Init();
	var oButtonInit = new borrowing_oButtton();
		oButtonInit.Init();
		jQuery('#borrowing_formSearch .chosen-select').chosen({width: '100%'});
})