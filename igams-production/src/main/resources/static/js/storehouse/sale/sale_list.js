//主要限制金额信息
var zdxz = $("#sale_formSearch #zdxz").val();
var xzColumns = [{
	checkbox: true,
	width: '3%'
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
	field: 'xsid',
	title: '销售id',
	width: '8%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'u8xsdh',
	title: 'u8销售单号',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: true
},{
	field: 'oaxsdh',
	title: '销售单号',
	width: '12%',
	align: 'left',
	sortable: true,
	visible: true
},{
	field: 'yoaxsdh',
	title: '原销售单号',
	width: '12%',
	align: 'left',
	sortable: true,
	formatter:yoaxsdhformatter,
	visible: false
},{
	field: 'jydh',
	title: '借用单号',
	width: '12%',
	align: 'left',
	sortable: true,
	visible: false
}, {
	field: 'glid',
	title: '关联id',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'xslxmc',
	title: '销售类型',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'khjcmc',
	title: '客户简称',
	width: '15%',
	align: 'left',
	sortable: true,
	visible: true
},{
	field: 'khzd',
	title: '客户终端',
	width: '15%',
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
	field: 'ysyq',
	title: '运输要求',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'suil',
	title: '税率',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: false
}, {
	field: 'shdz',
	title: '收货地址',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: false
}, {
	field: 'ddrq',
	title: '订单日期',
	width: '8%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'ywlxmc',
	title: '业务类型',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'xsbmmc',
	title: '销售部门',
	width: '8%',
	align: 'left',
	sortable: true,
	visible: false
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
	field: 'zdqymc',
	title: '终端区域',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'kdxx',
	title: '快递信息',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'sfqy',
	title: '是否确认',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'shlxfs',
	title: '收货联系方式',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'shrrmc',
	title: '审核人',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'bz',
	title: '备注',
	width: '5%',
	align: 'left',
	visible: false
},{
	field: 'djlx',
	title: '单据类型',
	width: '13%',
	align: 'left',
	formatter:djlxFormat,
	visible: false
},{
	field: 'fhzt',
	title: '发货状态',
	width: '8%',
	align: 'left',
	formatter:fhztFormat,
	visible: true
},{
	field: 'zt',
	title: '状态',
	width: '8%',
	align: 'left',
	formatter:ztFormat,
	visible: true
},{
	field: 'cz',
	title: '操作',
	titleTooltip:'操作',
	width: '7%',
	align: 'left',
	formatter:czFormat,
	visible: true
}]
var columns = [{
	checkbox: true,
	width: '3%'
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
	field: 'xsid',
	title: '销售id',
	width: '8%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'u8xsdh',
	title: 'u8销售单号',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: true
},{
	field: 'oaxsdh',
	title: '销售单号',
	width: '12%',
	align: 'left',
	sortable: true,
	visible: true
},{
	field: 'yoaxsdh',
	title: '原销售单号',
	width: '12%',
	align: 'left',
	sortable: true,
	formatter:yoaxsdhformatter,
	visible: false
},{
	field: 'jydh',
	title: '借用单号',
	width: '12%',
	align: 'left',
	sortable: true,
	visible: false
}, {
	field: 'glid',
	title: '关联id',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'xslxmc',
	title: '销售类型',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'khjcmc',
	title: '客户简称',
	width: '15%',
	align: 'left',
	sortable: true,
	visible: true
},{
	field: 'khzd',
	title: '客户终端',
	width: '15%',
	align: 'left',
	sortable: true,
	visible: true
},{
	field: 'htdh',
	title: '合同单号',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: false,
	formatter:viewYxhtFormatter
},{
	field: 'ysfsmc',
	title: '运输方式',
	width: '5%',
	align: 'left',
	sortable: true,
	visible: true
},{
	field: 'ysyq',
	title: '运输要求',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'suil',
	title: '税率',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'xszje',
	title: '总金额',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: true
},{
	field: 'ysk',
	title: '应收款',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'shdz',
	title: '收货地址',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'zk',
	title: '折扣',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'ddrq',
	title: '订单日期',
	width: '8%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'ywlxmc',
	title: '业务类型',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'xsbmmc',
	title: '销售部门',
	width: '8%',
	align: 'left',
	sortable: true,
	visible: false
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
	field: 'zdqymc',
	title: '终端区域',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'dky',
	title: '到款月',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'dkje',
	title: '到款金额',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'xsfph',
	title: '发票号',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'kdxx',
	title: '快递信息',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'sfqy',
	title: '是否确认',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'shlxfs',
	title: '收货联系方式',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'shrrmc',
	title: '审核人',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'bz',
	title: '备注',
	width: '5%',
	align: 'left',
	visible: false
},{
	field: 'djlx',
	title: '单据类型',
	width: '13%',
	align: 'left',
	formatter:djlxFormat,
	visible: false
},{
	field: 'fhzt',
	title: '发货状态',
	width: '8%',
	align: 'left',
	formatter:fhztFormat,
	visible: true
},{
	field: 'zt',
	title: '状态',
	width: '8%',
	align: 'left',
	formatter:ztFormat,
	visible: true
},{
	field: 'cz',
	title: '操作',
	titleTooltip:'操作',
	width: '7%',
	align: 'left',
	formatter:czFormat,
	visible: true
}]
var sale_turnOff=true;
var sale_TableInit = function () { 
var oTableInit = new Object();	    
	// 初始化Table
	oTableInit.Init = function () {
		$('#sale_formSearch #sale_list').bootstrapTable({
			url: $("#sale_formSearch #urlPrefix").val()+'/storehouse/sale/pageGetListSale',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#sale_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"oaxsdh",					// 排序字段
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
			uniqueId: "xsid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮                 								
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: zdxz == '1'?xzColumns:columns,
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				saleById(row.xsid,'view',$("#sale_formSearch #btn_view").attr("tourl"),'',zdxz);
			},
		});
		$("#sale_formSearch #sale_list").colResizable({
			liveDrag:true, 
			gripInnerHtml:"<div class='grip'></div>", 
			draggingClass:"dragging", 
			resizeMode:'fit', 
			postbackSafe:true,
			partialRefresh:true
		})
	}
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "xsgl.xsid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return saleSearchData(map);
	};
	return oTableInit
};


/**
 * 状态格式化函数
 * @returns
 */
function ztFormat(value,row,index) {
	if (row.zt == '00') {
		return '未提交';
	}else if (row.zt == '80') {
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.xsid + "\",event,\"AUDIT_SALEORDER\",{prefix:\"" + $('#sale_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
	}else if (row.zt == '15') {
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.xsid + "\",event,\"AUDIT_SALEORDER\",{prefix:\"" + $('#sale_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
	}else if (row.zt == '10'){
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.xsid + "\",event,\"AUDIT_SALEORDER\",{prefix:\"" + $('#sale_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
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
	}else if (row.djlx == '2') {
		return '借出借用转销售单';
	}
}
/**
 * 发货状态格式化函数
 * @returns
 */
function fhztFormat(value,row,index) {
	var html ="";
	if(row.fhzt=="00"){
		html="<span style='color:red;'>"+"未发货"+"</span>";
	}else if(row.fhzt=="01"){
		html="<span style='color:#f0ad4e;'>"+"部分发货"+"</span>";
	}else if (row.fhzt=="02"){
		html="<span style='color:green;'>"+"全部发货"+"</span>";
	}else {
		html="<span style='color:red;'>"+"未发货"+"</span>";
	}
	return html;
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
	if (row.zt == '10') {
		return "<span class='btn btn-warning' onclick=\"recallDeviceCheck('" + row.xsid +"','" + row.shlx+ "',event)\" >撤回</span>";
	}else{
		return "";
	}
}
function yoaxsdhformatter(value,row,index) {
	var html = "";
	if(row.yoaxsdh==null){
		html = "";
	}else{
		html += "<a href='javascript:void(0);' onclick=\"showXsInfo('" + row.yxsid +"','" + row.jcjyid+ "')\">"+row.yoaxsdh+"</a>";

	}
	return html;
}
function showXsInfo(xsid,jcjyid) {
	$.showDialog($("#sale_formSearch #urlPrefix").val()+"/storehouse/sale/pagedataViewSale?xsid="+xsid+"&jcjyid="+jcjyid,'详细信息',viewXsConfig);
}
var viewXsConfig = {
	width		: "1600px",
	modalName	: "viewsaleModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
//撤回项目提交
function recallDeviceCheck(xsid,event){
	var auditType = $("#sale_formSearch #auditType").val();
	var msg = '您确定要撤回该条审核吗？';
	var purchase_params = [];
	purchase_params.prefix = $("#sale_formSearch #urlPrefix").val();
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,xsid,function(){
				saleResult();
			},purchase_params);
		}
	});
}

//提供给导出用的回调函数
function salesSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="xsgl.xgsj";
	map["sortLastOrder"]="desc";
	map["sortName"]="xsgl.lrsj";
	map["sortOrder"]="desc";
	return saleSearchData(map);
}
// 根据查询条件查询
function saleSearchData(map){
	var cxtj=$("#sale_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#sale_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["oaxsdh"]=cxnr;
	}else if(cxtj=="1"){
		map["xsbmmc"]=cxnr;
	}else if(cxtj=="2"){
		map["ywymc"]=cxnr;
	}else if(cxtj=="3"){
		map["khjcmc"]=cxnr;
	}
	else if(cxtj=="4"){
		map["htdh"]=cxnr;
	}
	else if(cxtj=="5"){
		map["khzd"]=cxnr;
	}
	else if(cxtj=="6"){
		map["ysqy"]=cxnr;
	}else if(cxtj=="7"){
		map["zdqy"]=cxnr;
	}else if(cxtj=="8"){
		map["shry"]=cxnr;
	}else if(cxtj=="9"){
		map["zk"]=cxnr;
	}else if(cxtj=="10"){
		map["dky"]=cxnr;
	}else if(cxtj=="11"){
		map["xsfph"]=cxnr;
	}else if(cxtj=="12"){
		map["entire"]=cxnr;
	}else if(cxtj=="13"){
		map["jydh"]=cxnr;
	}else if(cxtj=="14"){
		map["u8xsdh"]=cxnr;
	}

	// 订单开始日期
	var ddrqstart = jQuery('#sale_formSearch #ddrqstart').val();
	map["ddrqstart"] = ddrqstart;
	// 订单结束日期
	var ddrqend = jQuery('#sale_formSearch #ddrqend').val();
	map["ddrqend"] = ddrqend;
	//客户类别
	var khlbs = jQuery('#sale_formSearch #khlb_id_tj').val();
	map["khlbs"] = khlbs.replace(/'/g, "");;
	//是否签收
	var sfqss = jQuery('#sale_formSearch #sfqs_id_tj').val();
	map["sfqss"] = sfqss.replace(/'/g, "");;
	//是否确认
	var sfqrs = jQuery('#sale_formSearch #sfqr_id_tj').val();
	map["sfqrs"] = sfqrs.replace(/'/g, "");;
	//单据类型
	var djlxs = jQuery('#sale_formSearch #djlx_id_tj').val();
	map["djlxs"] = djlxs.replace(/'/g, "");
	// 到款金额小
	var dkjemin = jQuery('#sale_formSearch #dkjemin').val();
	map["dkjemin"] = dkjemin;
	// 到款金额大
	var dkjemax = jQuery('#sale_formSearch #dkjemax').val();
	map["dkjemax"] = dkjemax;
	return map;
}



function saleById(id,action,tourl,oaxsdh,zdxz){
	if(!tourl){
		return;
	}
	tourl = $("#sale_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?xsid=" +id+"&zdxz="+zdxz;
		$.showDialog(url,'销售详情',viewSaleConfig);
	} else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'新增',addSaleConfig);
	}else if(action =='mod'){
		var url= tourl+ "?xsid=" +id;
		$.showDialog(url,'修改',modSaleConfig);
	}else if(action =='submit'){
        var url= tourl+ "?xsid=" +id;
        $.showDialog(url,'提交',submitSaleConfig);
    }else if(action =='copy'){
		var url= tourl+ "?xsid=" +id;
		$.showDialog(url,"复制",copySaleConfig);
	}else if(action =='salereceipt'){
		var url= tourl+ "?xsid=" +id;
		$.showDialog(url,"销售发票维护",salereceiptConfig);
	}else if(action =='logisticsuphold'){
		var url= tourl+"?ywid="+id+"&ywlx=xs&ywdh="+oaxsdh;
		$.showDialog(url,'物流维护',addlogisticsUpholdFormConfig);
	}else if(action =='signfor'){
		var url= tourl+"?ywid="+id+"&ywlx=xs&ywdh="+oaxsdh;
		$.showDialog(url,'物流签收',addlogisticsUpholdFormConfig);
	}else if(action =='signforconfirm'){
		var url= tourl+"?ywid="+id+"&ywlx=xs&ywdh="+oaxsdh;
		$.showDialog(url,'物流签收确认',addlogisticsUpholdFormConfig);
	}else if(action =='xstransform'){
		var url= tourl+ "?xsid=" +id;
		$.showDialog(url,"销售换货",modSaleConfig);
	}else if(action =='paymentreceived'){
		var url= tourl+ "?xsid=" +id;
		$.showDialog(url,"销售到款",paymentreceivedFormConfig);
	}else if(action =='stewardset'){
		var url= tourl;
		$.showDialog(url,"负责人设置",stewardsetFormConfig);
	}
}
//物流维护
var stewardsetFormConfig = {
	width		: "800px",
	modalName	: "stewardsetFormModal",
	formName	: "stewardsetForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				$("#stewardsetForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"stewardsetForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							saleResult();
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

//负责人
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
							saleResult();
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

//销售到款
var paymentreceivedFormConfig = {
	width		: "1200px",
	modalName	: "paymentreceivedModal",
	formName	: "paymentreceivedForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default",
			callback : function() {
				saleResult();
			}
		},

	}
};

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
						}else if((xsmx[i].dkje!=""&&xsmx[i].dkje!=null)&&(xsmx[i].dky==""||xsmx[i].dky==null)){
							$.alert("物料编码为  "+xsmx[i].wlbm+"  的数据未选择到款月！");
							return false;
						}
						//取消销售订单的数量的检验
						// if(parseFloat(xsmx[i].sl).toFixed(2) - parseFloat(xsmx[i].kcl).toFixed(2) > 0){
						// 	$.alert("物料编码为  "+xsmx[i].wlbm+"  的数据所写数量大于库存量！");
						// 	return false;
						// }
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
								saleResult();
							},null,sale_params);
						}else{
							saleResult();
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
function viewYxhtFormatter(value,row,index) {
	var html = "";
	if(row.htdh==null){
		html = "";
	}else{
		html += "<a href='javascript:void(0);' onclick=\"viewYxht('"+row.htid+"')\">"+row.htdh+"</a>";

	}
	return html;
}
function viewYxht(htid){
	var url=$("#sale_formSearch #urlPrefix").val()+"/marketingContract/marketingContract/pagedataViewMarketingContract?htid="+htid+"&access_token=" + $("#ac_tk").val();
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
var modSaleConfig = {
	width		: "1550px",
	modalName	: "modSaleModal",
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
						}else if((xsmx[i].dkje!=""&&xsmx[i].dkje!=null)&&(xsmx[i].dky==""||xsmx[i].dky==null)){
							$.alert("物料编码为  "+xsmx[i].wlbm+"  的数据未选择到款月！");
							return false;
						}
						//取消销售订单的数量的检验
						// if(parseFloat(xsmx[i].sl).toFixed(2) - parseFloat(xsmx[i].kcl).toFixed(2) > 0){
						// 	$.alert("物料编码为  "+xsmx[i].wlbm+"  的数据所写数量大于库存量！");
						// 	return false;
						// }
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
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								saleResult();
							}
						});
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

var submitSaleConfig = {
	width		: "1550px",
	modalName	: "submitSaleModal",
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
						}else if((xsmx[i].dkje!=""&&xsmx[i].dkje!=null)&&(xsmx[i].dky==""||xsmx[i].dky==null)){
							$.alert("物料编码为  "+xsmx[i].wlbm+"  的数据未选择到款月！");
							return false;
						}
						// if(parseFloat(xsmx[i].sl).toFixed(2) - parseFloat(xsmx[i].kcl).toFixed(2) > 0){
						// 	$.alert("物料编码为  "+xsmx[i].wlbm+"  的数据所写数量大于库存量！");
						// 	return false;
						// }
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
								saleResult();
							},null,sale_params);
						}else{
							saleResult();
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
var copySaleConfig = {
	width		: "1550px",
	modalName	: "copySaleModal",
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
						}else if((xsmx[i].dkje!=""&&xsmx[i].dkje!=null)&&(xsmx[i].dky==""||xsmx[i].dky==null)){
							$.alert("物料编码为  "+xsmx[i].wlbm+"  的数据未选择到款月！");
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
								saleResult();
							},null,sale_params);
						}else{
							saleResult();
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
var salereceiptConfig = {
	width		: "600px",
	modalName	: "salereceiptModal",
	formName	: "salereceipt_Form",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "保 存",
			className : "btn-primary",
			callback : function() {
				if(!$("#salereceipt_Form").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#salereceipt_Form input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"salereceipt_Form",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								saleResult();
							}
						});
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
var viewSaleConfig = {
	width		: "1600px",
	modalName	: "viewsaleModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	} 
};




var sale_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_view = $("#sale_formSearch #btn_view");
		var btn_query = $("#sale_formSearch #btn_query");
		var btn_add = $("#sale_formSearch #btn_add");
		var btn_mod = $("#sale_formSearch #btn_mod");
        var btn_submit = $("#sale_formSearch #btn_submit");
		var btn_discard=$("#sale_formSearch #btn_discard");
		var btn_del=$("#sale_formSearch #btn_del");
		var btn_copy=$("#sale_formSearch #btn_copy");
		var btn_selectexport = $("#sale_formSearch #btn_selectexport");//选中导出
		var btn_searchexport = $("#sale_formSearch #btn_searchexport");//搜索导出
		var btn_salereceipt = $("#sale_formSearch #btn_salereceipt");//销售发票维护
		var btn_logisticsuphold =  $("#sale_formSearch #btn_logisticsuphold");//物料维护
		var btn_signfor =  $("#sale_formSearch #btn_signfor");//物流签收
		var btn_signforconfirm =  $("#sale_formSearch #btn_signforconfirm");//物流签收确认
		var btn_xstransform =  $("#sale_formSearch #btn_xstransform");//换货
		var btn_maintablesearchexport = $("#sale_formSearch #btn_maintablesearchexport");//主表搜索导出
		var btn_maintableselectexport = $("#sale_formSearch #btn_maintableselectexport");//主表选中导出
		var btn_paymentreceived = $("#sale_formSearch #btn_paymentreceived");//到款
		var btn_stewardset = $("#sale_formSearch #btn_stewardset");
		//添加日期控件
		laydate.render({
			elem: '#sale_formSearch #ddrqstart'
			,theme: '#2381E9'
		});
		//添加日期控件
		laydate.render({
			elem: '#sale_formSearch #ddrqend'
			,theme: '#2381E9'
		});
		/*--------------------------------模糊查询---------------------------*/    	
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			saleResult(true);
    		});
    	}
  
       /* ---------------------------销售列表查看-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#sale_formSearch #sale_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			saleById(sel_row[0].xsid,"view",btn_view.attr("tourl"),'',zdxz);
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/*-----------------------修改------------------------------------*/
		btn_mod.unbind("click").click(function(){
			var sel_row = $('#sale_formSearch #sale_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
			    if(sel_row[0].zt=='00'||sel_row[0].zt=='15'){
                    saleById(sel_row[0].xsid,"mod",btn_mod.attr("tourl"));
                }else {
			        $.error("此状态不允许修改！")
                    return;
                }
			}else{
				$.error("请选中一行");
			}
		});
		/*-----------------------修改------------------------------------*/
		btn_salereceipt.unbind("click").click(function(){
			var sel_row = $('#sale_formSearch #sale_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				saleById(sel_row[0].xsid,"salereceipt",btn_salereceipt.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*-----------------------换货------------------------------------*/
		btn_xstransform.unbind("click").click(function(){
			var sel_row = $('#sale_formSearch #sale_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				saleById(sel_row[0].xsid,"xstransform",btn_xstransform.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*-----------------------复制------------------------------------*/
		btn_copy.unbind("click").click(function(){
			var sel_row = $('#sale_formSearch #sale_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				saleById(sel_row[0].xsid,"copy",btn_copy.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
        /*-----------------------提交------------------------------------*/
        btn_submit.unbind("click").click(function(){
            var sel_row = $('#sale_formSearch #sale_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].zt=='00'||sel_row[0].zt=='15'){
                    saleById(sel_row[0].xsid,"submit",btn_submit.attr("tourl"));
                }else {
                    $.error("此状态不允许提交！")
                    return;
                }
            }else{
                $.error("请选中一行");
            }
        });

		/* ---------------------------销售列表新增-----------------------------------*/
		btn_add.unbind("click").click(function(){

			saleById(null,"add",btn_add.attr("tourl"));

		});
		/* ---------------------------销售列表负责人设置-----------------------------------*/
		btn_stewardset.unbind("click").click(function(){

			saleById(null,"stewardset",btn_stewardset.attr("tourl"));
		});
		//---------------------------------选中导出---------------------------------------
		btn_selectexport.unbind("click").click(function(){
			var sel_row = $('#sale_formSearch #sale_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].xsid;
				}
				ids = ids.substr(1);
				$.showDialog($('#sale_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=SALES_SELECT&expType=select&ids="+ids
					,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1200px"}));
			}else{
				$.error("请选择数据");
			}
		});
		//搜索导出
		btn_searchexport.unbind("click").click(function(){
			$.showDialog($('#sale_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=SALES_SEARCH&expType=search&callbackJs=salesSearchData"
				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1200px"}));
		});

		//---------------------------------主表选中导出---------------------------------------
		btn_maintableselectexport.unbind("click").click(function(){
			var sel_row = $('#sale_formSearch #sale_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].xsid;
				}
				ids = ids.substr(1);
				$.showDialog($('#sale_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=MAINTABLESALES_SELECT&expType=select&ids="+ids
					,btn_maintableselectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1200px"}));
			}else{
				$.error("请选择数据");
			}
		});
		//主表搜索导出
		btn_maintablesearchexport.unbind("click").click(function(){
			$.showDialog($('#sale_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=MAINTABLESALES_SEARCH&expType=search&callbackJs=salesSearchData"
				,btn_maintablesearchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1200px"}));
		});
		/* ------------------------------废弃-----------------------------*/
		btn_discard.unbind("click").click(function(){
			var sel_row = $('#sale_formSearch #sale_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==0){
				$.error("请至少选中一行");
				return;
			}else {
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					if(sel_row[i].zt=='00'||sel_row[i].zt=='15'){
						ids= ids + ","+ sel_row[i].xsid;
					}else{
						$.error("请选择未提交的数据！");
						return;
					}
				}
				ids=ids.substr(1);
				$.confirm('您确定要废弃所选择的信息吗？',function(result){
					if(result){
						jQuery.ajaxSetup({async:false});
						var url= $('#sale_formSearch #urlPrefix').val() + btn_discard.attr("tourl");
						jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
							setTimeout(function(){
								if(responseText["status"] == 'success'){
									$.success(responseText["message"],function() {
										saleResult();
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
		/* ------------------------------删除-----------------------------*/
		btn_del.unbind("click").click(function(){
			var sel_row = $('#sale_formSearch #sale_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==0){
				$.error("请至少选中一行");
				return;
			}else {
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
						ids= ids + ","+ sel_row[i].xsid;
				}
				ids=ids.substr(1);
				$.confirm('您确定要删除所选择的信息吗？',function(result){
					if(result){
						jQuery.ajaxSetup({async:false});
						var url= $('#sale_formSearch #urlPrefix').val() + btn_del.attr("tourl");
						jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
							setTimeout(function(){
								if(responseText["status"] == 'success'){
									$.success(responseText["message"],function() {
										saleResult();
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
		/* --------------------------- 物流维护 -----------------------------------*/
		btn_logisticsuphold.unbind("click").click(function(){
			var sel_row = $('#sale_formSearch #sale_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
				saleById(sel_row[0].xsid, "logisticsuphold", btn_logisticsuphold.attr("tourl"),sel_row[0].oaxsdh);
			}else{
				$.error("请选中一行");
			}
		});
		/* --------------------------- 到款 -----------------------------------*/
		btn_paymentreceived.unbind("click").click(function(){
			var sel_row = $('#sale_formSearch #sale_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
				if (sel_row[0].zt!='80'){
					$.error("该条数据未审核通过!");
				}else {
					saleById(sel_row[0].xsid, "paymentreceived", btn_paymentreceived.attr("tourl"),sel_row[0].oaxsdh);
				}
			}else{
				$.error("请选中一行");
			}
		});
		/* --------------------------- 物流签收 -----------------------------------*/
		btn_signfor.unbind("click").click(function(){
			var sel_row = $('#sale_formSearch #sale_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
				saleById(sel_row[0].xsid, "signfor", btn_signfor.attr("tourl"),sel_row[0].oaxsdh);
			}else{
				$.error("请选中一行");
			}
		});
		/* --------------------------- 物流签收确认 -----------------------------------*/
		btn_signforconfirm.unbind("click").click(function(){
			var sel_row = $('#sale_formSearch #sale_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
				saleById(sel_row[0].xsid, "signforconfirm", btn_signforconfirm.attr("tourl"),sel_row[0].oaxsdh);
			}else{
				$.error("请选中一行");
			}
		});
		/*-----------------------显示隐藏------------------------------------*/
		$("#sale_formSearch #sl_searchMore").on("click", function(ev){
			var ev=ev||event;
			if(sale_turnOff){
				$("#sale_formSearch #searchMore").slideDown("low");
				sale_turnOff=false;
				this.innerHTML="基本筛选";
			}else{
				$("#sale_formSearch #searchMore").slideUp("low");
				sale_turnOff=true;
				this.innerHTML="高级筛选";
			}
			ev.cancelBubble=true;
			//showMore();
		});
    };
    	return oInit;
};


function saleResult(isTurnBack){
	if(isTurnBack){
		$('#sale_formSearch #sale_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#sale_formSearch #sale_list').bootstrapTable('refresh');
	}
}

$(function(){
	// 1.初始化Table
	var oTable = new sale_TableInit();
	oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new sale_ButtonInit();
    oButtonInit.Init();
	$("body").addClass("modal-open");
	// 所有下拉框添加choose样式
	jQuery('#sale_formSearch .chosen-select').chosen({width: '100%'});
	
});