var contract_turnOff=true;
var wlfls = [];
var kjht_json = [];
var htlx = $("#contract_formSearch #htlx").val();
var defaultColumnsArray = [
	{
	checkbox: true
},{
	field: 'htnbbh',
	title: '合同内部编号',
	width: '12%',
	align: 'left',
	sortable: true,
	visible: true
},{
	field: 'sfkj',
	title: '是否框架',
	width: '5%',
	align: 'left',
	sortable: true,
	visible: true,
	formatter:sfkjformat,
},{
	field: 'kjhtbh',
	title: '框架合同编号',
	width: '12%',
	align: 'left',
	sortable: true,
	visible: true,
	formatter:kjhtbhformat
}, {
	field: 'htwbbh',
	title: '合同外部编号',
	width: '12%',
	align: 'left',
	sortable: true,
	visible: false
}, {
	field: 'htlx',
	title: '采购类型',
	width: '6%',
	align: 'left',
	formatter:htlxformat,
	sortable: true,
	visible: false
},{
	field: 'ywlxmc',
	title: '合同类型',
	width: '8%',
	align: 'left',
	sortable: true,
	visible: false
}, {
	field: 'ddrq',
	title: '双章日期',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: true
},{
	field: 'ddrq',
	title: '订单日期',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'fzrmc',
	title: '负责人',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'sqbmmc',
	title: '申请部门',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'fpje',
	title: '发票金额',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: false
}, {
	field: 'bizmc',
	title: '币种',
	width: '4%',
	align: 'left',
	sortable: true,
	visible: false
}, {
	field: 'sl',
	title: '税率',
	width: '4%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'hl',
	title: '汇率',
	width: '4%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'fkfsmc',
	title: '付款方式',
	width: '8%',
	align: 'left',
	sortable: true,
	formatter: fkfsformat,
	visible: true
},{
	field: 'fpfsmc',
	title: '发票方式',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: true
},{
	field: 'gysmc',
	title: '供应商',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: true,
	formatter: gysformat
}, {
	field: 'zje',
	title: '总金额',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'htfxmc',
	title: '合同风险程度',
	width: '5%',
	align: 'left',
	sortable: true,
	visible: true
},{
	field: 'yfje',
	title: '已付金额',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: false
},  {
	field: 'cjrq',
	title: '创建日期',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'htwfrq',
	title: '外发日期',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'tjrq',
	title: '提交日期',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'szbj',
	title: '双章标记',
	width: '6%',
	align: 'left',
	formatter:szbjformat,
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
	field: 'scbj',
	title: '状态',
	width: '4%',
	align: 'left',
	formatter:htztformat,
	sortable: true,
	visible: true
}, {
	field: 'zt',
	title: '审核状态',
	width: '8%',
	align: 'left',
	formatter:ztformat,
	sortable: true,
	visible: true
}, {
	field: 'wcbj',
	title: '入库完成',
	width: '8%',
	align: 'left',
	formatter:wbztformat,
	sortable: true,
	visible: true
}, {
	field: 'cz',
	title: '操作',
	titleTooltip:'操作',
	width: '5%',
	align: 'left',
	formatter:czFormat,
	visible: true
}, {
	field: 'yfje',
	title: '已付金额',
	titleTooltip:'已付金额',
	width: '5%',
	align: 'left',
	visible: false
}, {
	field: 'wfje',
	title: '未付金额',
	titleTooltip:'未付金额',
	width: '5%',
	align: 'left',
	visible: false
}, {
	field: 'fkzje',
	title: '付款中金额',
	titleTooltip:'付款中金额',
	width: '5%',
	align: 'left',
	visible: false
}, {
	field: 'ksrq',
	title: '开始日期',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: false
}, {
	field: 'dqrq',
	title: '到期日期',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: false
}];
var frameworkColumnsArray = [
	{
	checkbox: true
},{
	field: 'htnbbh',
	title: '合同内部编号',
	width: '12%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'htwbbh',
	title: '合同外部编号',
	width: '12%',
	align: 'left',
	sortable: true,
	visible: false
}, {
	field: 'htlx',
	title: '采购类型',
	width: '6%',
	align: 'left',
	formatter:htlxformat,
	sortable: true,
	visible: true
},{
	field: 'kjlx',
	title: '框架类型',
	width: '8%',
	align: 'left',
	sortable: true,
	visible: true,
	formatter:kjlxformat,
},{
	field: 'ddrq',
	title: '双章日期',
	width: '8%',
	align: 'left',
	sortable: true,
	visible: true
},{
	field: 'fzrmc',
	title: '负责人',
	width: '8%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'bizmc',
	title: '币种',
	width: '4%',
	align: 'left',
	sortable: true,
	visible: false
}, {
	field: 'sl',
	title: '税率',
	width: '4%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'hl',
	title: '汇率',
	width: '4%',
	align: 'left',
	sortable: true,
	visible: false
},{
	field: 'fkfsmc',
	title: '付款方式',
	width: '8%',
	align: 'left',
	sortable: true,
	formatter: fkfsformat,
	visible: true
},{
	field: 'fpfsmc',
	title: '发票方式',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: true
},{
	field: 'gysmc',
	title: '供应商',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: true,
	formatter: gysformat
},{
	field: 'ywlxmc',
	title: '合同类型',
	width: '8%',
	align: 'left',
	sortable: true,
	visible: true
}
,  {
	field: 'cjrq',
	title: '创建日期',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'tjrq',
	title: '提交日期',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'ksrq',
	title: '开始日期',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'dqrq',
	title: '到期日期',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'sfgq',
	title: '是否过期',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'szbj',
	title: '双章标记',
	width: '6%',
	align: 'left',
	formatter:szbjformat,
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
	field: 'scbj',
	title: '状态',
	width: '4%',
	align: 'left',
	formatter:htztformat,
	sortable: true,
	visible: true
}, {
	field: 'zt',
	title: '审核状态',
	width: '8%',
	align: 'left',
	formatter:ztformat,
	sortable: true,
	visible: true
}, {
	field: 'cz',
	title: '操作',
	titleTooltip:'操作',
	width: '5%',
	align: 'left',
	formatter:czFormat,
	visible: true
}];
var contract_TableInit = function () {
	 var oTableInit = new Object();
	    
	// 初始化Table
	oTableInit.Init = function () {
		$('#contract_formSearch #contract_list').bootstrapTable({
			url: $("#contract_formSearch #urlPrefix").val()+'/contract/contract/pageGetListContract',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#contract_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"cjrq",					// 排序字段
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
			uniqueId: "htid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮                 								
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: htlx=='3'?frameworkColumnsArray:defaultColumnsArray,
			onLoadSuccess: function () {
			},
			onLoadError: function () {
			},
			onDblClickRow: function (row, $element) {
				contractById(row.htid,'view',$("#contract_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#contract_formSearch #contract_list").colResizable({
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
        sortLastName: "htid", // 防止同名排位用
        sortLastOrder: "desc", // 防止同名排位用
		htlx: $("#contract_formSearch #htlx").val()
        // 搜索框使用
        // search:params.search
    };
    return contractSearchData(map);
	};
	return oTableInit;
};

function gysformat(value,row,index){
	var html = "<a href='javascript:void(0);' onclick='queryByGysid(\""+row.gys+"\")'>"+row.gysmc+"</a>"
	return html;
}
function fkfsformat(value,row,index){
	var html ="";
	if (row.fkfsmc){
		html = row.fkfsmc;
		if (row.fkbz){
			html+=','+row.fkbz;
		}
	}
	return html;
}
//双章标记格式化
function queryByGysid(gys){
	$.showDialog($("#contract_formSearch #urlPrefix").val()+"/warehouse/supplier/viewSupplier?gysid="+gys+"&access_token="+$("#ac_tk").val(),'供应商详细信息',viewSupplierConfig);
}
var viewSupplierConfig = {
	width		: "800px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
function kjhtbhformat(value,row,index){
	var html = "";
	if (row.kjhtbh){
		html = "<a href='javascript:void(0);' onclick='queryByKjhtid(\""+row.kjhtid+"\")'>"+row.kjhtbh+"</a>"
	}
	return html;
}
function queryByKjhtid(kjhtid){
	$.showDialog($("#contract_formSearch #urlPrefix").val()+"/contract/contract/viewContract?htid="+kjhtid+"&access_token="+$("#ac_tk").val(),'框架合同查看',viewContractConfig);
}
//双章标记格式化
function szbjformat(value,row,index){
	if(row.szbj==1){
		var szbj="<span style='color:green;'>"+"是"+"</span>";
	}else if(row.szbj==0){
		var szbj="<span style='color:red;'>"+"否"+"</span>";
	}
	return szbj;
}

//采购类型格式化
function htlxformat(value,row,index){
	if(row.htlx==1){
		return "OA采购";
	}else if(row.htlx==2){
		return "委外采购";
	}else if(row.htlx==3){
		return "框架合同";
	}else {
		return "普通采购"
	}
}
function kjlxformat(value,row,index){
	if(row.kjlx==1){
		return "补充框架合同";
	}else if(row.kjlx==0){
		return "主框架合同";
	}
}
function sfkjformat(value,row,index){
	if(row.sfkj==1){
		return "是";
	}else if(row.sfkj==0){
		return "否";
	}
}

function wbztformat(value,row,index){
	if(row.wcbj==1){
		var html="<span style='color:green;'>"+"已完成"+"</span>";
	}else{
		var html="<span style='color:red;'>"+"未完成"+"</span>";
	}
	return html;
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
	if (row.zt == '10' && row.scbj =='0' ) {
		return "<span class='btn btn-warning' onclick=\"recallContract('" + row.htid + "',event)\" >撤回</span>";
	}else if(row.zt == '10' && row.scbj ==null ){
		return "<span class='btn btn-warning' onclick=\"recallContract('" + row.htid + "',event)\" >撤回</span>";
	}else{
		return "";
	}		
}

//撤回项目提交
function recallContract(htid,event){
	var auditType = $("#contract_formSearch #auditType").val();
	var msg = '您确定要撤回该合同吗？';
	var purchase_params = [];
	purchase_params.prefix = $("#contract_formSearch #urlPrefix").val();
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,htid,function(){
				contractResult();
			},purchase_params);
		}
	});
}

//合同状态格式化
function htztformat(value,row,index){
	var html="";
	if(row.scbj=='0'){
		html="<span style='color:green;'>正常</span>";
	}else{	
		html="<span style='color:red;'>废弃</span>";
	}
	return html;
}


//状态格式化
function ztformat(value,row,index){
	if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80'){
		return row.htlx=='1'?"<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htid + "\",event,\"AUDIT_CONTRACT_OA\",{prefix:\"" + $('#contract_formSearch #urlPrefix').val() + "\"})' >审核通过</a>":
			"<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htid + "\",event,\"AUDIT_CONTRACT\",{prefix:\"" + $('#contract_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {
		return row.htlx=='1'?"<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htid + "\",event,\"AUDIT_CONTRACT_OA\",{prefix:\"" + $('#contract_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>":
			"<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htid + "\",event,\"AUDIT_CONTRACT\",{prefix:\"" + $('#contract_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
    }else{
        var shr = row.shxx_dqgwmc;
        if(row.dqshr!=null && row.dqshr!=""){
            shr = row.dqshr;
        }
		return row.htlx=='1'?"<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htid + "\",event,\"AUDIT_CONTRACT_OA\",{prefix:\"" + $('#contract_formSearch #urlPrefix').val() + "\"})' >" + shr + "审核中</a>":
			"<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htid + "\",event,\"AUDIT_CONTRACT\",{prefix:\"" + $('#contract_formSearch #urlPrefix').val() + "\"})' >" + shr + "审核中</a>";
    }  
}


function contractSearchData(map){
	var cxtj=$("#contract_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#contract_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr;
	}else if(cxtj=="1"){
		map["htnbbh"]=cxnr;
	}else if(cxtj=="2"){
		map["htwbbh"]=cxnr;
	}else if (cxtj == "3") {
    	map["gysmc"] = cxnr;
	}else if (cxtj == "4") {
    	map["fzrmc"] = cxnr;
	}else if (cxtj == "5") {
    	map["sqbmmc"] = cxnr;
	}else if (cxtj == "6") {
		map["wlbm"] = cxnr;
	}else if (cxtj == "7") {
		map["wlmc"] = cxnr;
	}else if (cxtj == "8") {
		map["kjhtbh"] = cxnr;
	}
	
	// 合同类型
	var htlx = jQuery('#contract_formSearch #htlx_id_tj').val();
	map["htlx"] = htlx;
	var cshtlx = $("#contract_formSearch #htlx").val()
	if (cshtlx!=null&&cshtlx!=''){
		map["htlx"] = cshtlx;
	}
	// 双章标记
	var szbj = jQuery('#contract_formSearch #szbj_id_tj').val();
	map["szbj"] = szbj;

	// 发票方式
	var fpfs = jQuery('#contract_formSearch #fpfs_id_tj').val();
	map["fpfss"] = fpfs;
	
	// 付款方式
	var fkfs = jQuery('#contract_formSearch #fkfs_id_tj').val();
	map["fkfss"] = fkfs;
	
	// 创建开始日期
	var shsjstart = jQuery('#contract_formSearch #shsjstart').val();
	map["shsjstart"] = shsjstart;
	
	// 创建结束日期
	var shsjend = jQuery('#contract_formSearch #shsjend').val();
	map["shsjend"] = shsjend;
	
	// 总金额最小值
	var zjemin = jQuery('#contract_formSearch #zjemin').val();
	map["zjemin"] = zjemin;
	
	// 总金额最大值
	var zjemax = jQuery('#contract_formSearch #zjemax').val();
	map["zjemax"] = zjemax;
	
	// 删除标记
	var scbjs = jQuery('#contract_formSearch #scbj_id_tj').val();
	map["scbjs"] = scbjs;
	
	// 完成标记
	var wcbj = jQuery('#contract_formSearch #wcbj_id_tj').val();
	map["wcbj"] = wcbj;

	//合同风险程度
	var htfxcds=jQuery('#contract_formSearch #htfxcd_id_tj').val();
	map["htfxcds"] = htfxcds.replace(/'/g, "");
	//业务类型
	var htywlxs=jQuery('#contract_formSearch #htywlx_id_tj').val();
	map["htywlxs"] = htywlxs.replace(/'/g, "");
	//是否过期
	var sfgq=jQuery('#contract_formSearch #sfgq_id_tj').val();
	map["sfgq"] = sfgq;
	//是否框架
	var sfkj =jQuery('#contract_formSearch #sfkj_id_tj').val();
	map["sfkj"] = sfkj;
	return map;
}


//提供给导出用的回调函数
function ConSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="htgl.ddrq";
	map["sortLastOrder"]="desc";
	map["sortName"]="htgl.cjrq";
	map["sortOrder"]="desc";
	return contractSearchData(map);
}



function contractById(id,action,tourl,gysid,gysmc,htlx){
	if(!tourl){
		return;
	}
	tourl = $("#contract_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?htid=" +id;
		$.showDialog(url,'合同详细信息',viewContractConfig);
	}else if(action =='add'){
		var url=tourl + "?htaddflag=0";
		$.showDialog(url,'新增合同信息',addContractConfig);
	}else if(action =='mod'){
		var url=tourl + "?htid=" +id;
		if (htlx=='3'){
			$.showDialog(url,'修改框架合同信息',modFrameworkContractConfig);
		}else {
			$.showDialog(url,'修改合同信息',modContractConfig);
		}
	}else if(action =='advancedmod'){
		var url=tourl + "?htid=" +id;
		if (htlx=='3'){
			$.showDialog(url,'高级修改框架合同信息',modFrameworkContractConfig);
		}else {
			$.showDialog(url,'合同信息高级修改',modContractConfig);
		}
	}else if(action =='payment'){
		var url=tourl + "?htid=" +id;
		$.showDialog(url,'付款维护',paymentContractConfig);
	}else if(action =='receipt'){
		var url=tourl + "?ids=" +id+"&gys="+gysid+"&gysmc="+gysmc+"&dddw="+gysid+"&dddwmc="+gysmc;
		$.showDialog(url,'发票维护',receiptContractConfig);
	}else if(action =='submit'){
		var url=tourl + "?htid=" +id;
		$.showDialog(url,'提交审核',submitContractConfig);
	}else if(action =='audit'){
		var url= tourl;
		showAuditDialogWithLoading({
			id: id,
			type: 'AUDIT_CONTRACT',
			url:url,
			data:{'ywzd':'htid'},
			title:"合同审核",
			preSubmitCheck:'preSubmitRecheck',
			callback:function(){
				contractResult();//回调
			},
			dialogParam:{width:1200}
		});
	}else if(action =='contract'){
		var url= tourl + "?htid=" +id;
		$.showDialog(url,'生成合同',generateContractConfig);
	}else if(action =='formal'){
		var url=tourl + "?htid=" +id;
		$.showDialog(url,'正式上传合同',uploadContractConfig);
	}else if(action =='modaudit'){
		var url=tourl + "?ywid=" +id+"&shlb=AUDIT_CONTRACT";
		$.showDialog(url,'审核修改',modauditContractConfig);
	}else if(action=="viewCommon"){
		var url=tourl+"?htid="+id;
		$.showDialog(url,'合同详细信息 共通',viewContractConfig);
	}else if(action=="paymentremind"){
		var url=tourl+"?htid="+id;
		$.showDialog(url,'付款提醒',remindContractConfig);
	}else if(action =='system'){
		var url=tourl+"?htlx=1";
		$.showDialog(url,'OA新增合同信息',addContractConfig);
	}else if(action =='wwadd'){
        var url=tourl+"?htlx=2&qglbdm=OUTSOURCE";
        $.showDialog(url,'委外新增合同信息',addContractConfig);
    }else if(action =='framework'){
        var url=tourl+"?htlx=3";
        $.showDialog(url,'框架新增合同信息',addFrameworkContractConfig);
    }else if(action =='openclose'){
		var url=tourl+"?htid="+id;
        $.showDialog(url,'合同行关闭',opencloseContractConfig);
    }else if(action =='supplement'){
		var url=tourl+"?htid="+id;
		if (htlx=='3'){
			$.showDialog(url,'补充框架合同',addFrameworkContractConfig);
		}else {
			$.showDialog(url,'补充合同',addContractConfig);
		}
    }
}
function preSubmitRecheck(){
	return true;
}
var opencloseContractConfig = {
	width		: "1600px",
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

var modauditContractConfig = {
		width		: "800px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		} 
	};

var paymentContractConfig = {
		width		: "1000px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default",
					callback : function() {
						contractResult();
					},
			}
		} 
	};

//发票维护
var receiptContractConfig = {
	width		: "1550px",
	modalName	: "receiptContractModel",
	formName	: "editInvoiceForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if (!$("#editInvoiceForm").valid()) {
					$.alert("请填写完整信息");
					return false;
				}
				var fpmxJson = JSON.parse($("#editInvoiceForm #fpmx_json").val());
				if(fpmxJson.length!=0) {
					for (var i = 0; i < fpmxJson.length; i++) {
						if(parseFloat(fpmxJson[i].sl).toFixed(2) - parseFloat(fpmxJson[i].wwhsl).toFixed(2) > 0){
							$.alert("第"+(i+1)+"行数量超过未维护数量！");
							return false;
						}
					}
				}  else{
					$.alert("明细不允许为空！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				$("#editInvoiceForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"editInvoiceForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							contractResult();
							// //提交审核
							// if(responseText["auditType"]!=null){
							// 	var invoice_params=[];
							// 	invoice_params.prefix=responseText["urlPrefix"];
							// 	showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
							// 		$.closeModal(opts.modalName);
							// 		contractResult();
							// 	},null,invoice_params);
							// }else{
							// 	contractResult();
							// }
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

var generateContractConfig = {
		width		: "800px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		} 
	};

var submitContractConfig = {
		width		: "700px",
		modalName	: "submitContractModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "提交",
				className : "btn-primary",
				callback : function() {
					if(!$("#submitContractForm").valid()){
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					
					$("#submitContractForm input[name='access_token']").val($("#ac_tk").val());

					submitForm(opts["formName"]||"submitContractForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							if(opts.offAtOnce){
								//新增提交审
								var document_params=[];
								document_params.prefix=$('#submitContractForm #urlPrefix').val();
								var auditType = $("#submitContractForm #auditType").val();
								var ywid = $("#submitContractForm #htid").val();
								showAuditFlowDialog(auditType,ywid,function(){
									$.closeModal(opts.modalName);
									contractResult();
								},null,document_params);
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
		
var addContractConfig = {
	width		: "1500px",
	modalName	: "addContractModel",
	formName	: "editContractForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#editContractForm").valid()){
					return false;
				}
				//处理付款提醒数据
				var json=[];
				var yfjes=$("#editContractForm .yfjesz");
				var yfje_l=0;
				var yfrq_l=0;
				if(yfjes!=null && yfjes.length>0){
					for(var i=0;i<yfjes.length;i++){
						var sz={"yfje":"","yfrq":""};
						var yfjeval=$("#editContractForm #yfje"+i).val();
						var yfrqval=$("#editContractForm #yfrq"+i).val();
						if((yfjeval!=null && yfjeval!="") && (yfrqval!=null && yfrqval!="")){
							sz.yfje=yfjeval;
							sz.yfrq=yfrqval;
							json.push(sz);
						}else{
							if(((yfjeval!=null && yfjeval!="") && (yfrqval==null || yfrqval=="")) ||
								((yfjeval==null || yfjeval=="") && (yfrqval!=null && yfrqval!=""))){
								$.confirm("请合理设置预付日期和预付金额！");
								return false;
							}
						}
					}
					$("#editContractForm #fktxJson").val(JSON.stringify(json));
				}
				// 判断合同明细数量
				var isBeyond = "true";
				$("#editContractForm div[name='sldiv']").each(function(){
					if(this.getAttribute("isBeyond") == "false"){
						isBeyond = "false";
						return false;
					}
				});
				if(isBeyond == "false"){
					$.error("合同明细数量超出，请重新填写！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				if(t_map.rows != null && t_map.rows.length > 0){
					// 初始化htmxJson
            		var json = [];
					for (var i = 0; i < t_map.rows.length; i++) {
            			var sz = {"htmxid":'',"htid":'',"qgid":'',"qgmxid":'',"wlid":'',"sl":'',"hsdj":'',"hjje":'',"jhdhrq":'',"bz":'',"qgsl":'',"sysl":'',"yds":'',"presl":'',"sfgdzc":'',"bxsj":'',"wlmc" : '',"hwmc" : '',"wlfl" : '',"ccdg" : '',"cpzch" : '',"kjhtmxid" : '',"sfty" : '',"bchtmxid" : '',"xh" : ''};
            			sz.htmxid = t_map.rows[i].htmxid;
						sz.htid = t_map.rows[i].htid;
						sz.qgid = t_map.rows[i].qgid;
						sz.qgmxid = t_map.rows[i].qgmxid;
						sz.wlid = t_map.rows[i].wlid;
						sz.sl = t_map.rows[i].sl;
						sz.hsdj = t_map.rows[i].hsdj;
						sz.hjje = t_map.rows[i].hjje;
						sz.jhdhrq = t_map.rows[i].jhdhrq;
						sz.bz = t_map.rows[i].bz;
						sz.qgsl = t_map.rows[i].qgsl;						
						sz.sysl = t_map.rows[i].sysl;
						sz.yds = t_map.rows[i].yds;
						sz.presl = t_map.rows[i].presl;
						sz.sfgdzc = t_map.rows[i].sfgdzc;
						sz.bxsj = t_map.rows[i].bxsj;
						sz.wlmc = t_map.rows[i].wlmc;
						sz.hwmc = t_map.rows[i].hwmc;
						sz.wlfl = t_map.rows[i].wlfl;
						sz.ccdg = t_map.rows[i].ccdg;
						sz.cpzch = t_map.rows[i].cpzch;
						sz.kjhtmxid = t_map.rows[i].kjhtmxid;
						sz.sfty = t_map.rows[i].sfty;
						sz.bchtmxid = t_map.rows[i].bchtmxid;
						sz.xh = t_map.rows[i].xh;
						json.push(sz);
    				}
					$("#editContractForm #wlfls").val('');
            		$("#editContractForm #htmxJson").val(JSON.stringify(json));
				}else{
					$.error("合同明细不能为空！");
					return false;
				}
				//无关json设为null
				$("#editContractForm #htmx_json").val("");
				$("#editContractForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"editContractForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								contractResult();
							}
						});
					}else if(responseText["status"] == "failtwo"){
                        $.error(responseText["message"],function() {
                            preventResubmitForm(".modal-footer > button", false);
                        });
                    }else if(responseText["status"] == "fail"){
						// 加载页面提示div
						var htmxList_wlgl = responseText["htmxList_wlgl"];
						var htmxList_qgmx = responseText["htmxList_qgmx"];
						if(htmxList_wlgl && htmxList_wlgl.length > 0){
							$("#checkwlgl").attr("style","display:block;");
							$("#checkwlgl").empty();
							var html = '<table border="1" cellspacing="0" cellpadding="0" class="table table-hover table-bordered table-hidden" style="text-align: center;">';
							html += '<tr style="background-color: #63a3ff;">';
							html += '<th style="width:100px;"><span />物料编码</th>';
							html += '<th style="width:100px;"><span />起订量</th>';
							html += '<th style="width:100px;"><span />当前数量</th>';
							html += '</tr>';
							for (var i = 0; i < htmxList_wlgl.length; i++) {
								html += '<tr>';
								html += '<td><span title="' + htmxList_wlgl[i].wlbm + '"/>' + htmxList_wlgl[i].wlbm + '</td>';
								html += '<td><span title="' + htmxList_wlgl[i].qdl + '"/>' + htmxList_wlgl[i].qdl + '</td>';
								html += '<td><span title="' + htmxList_wlgl[i].sl + '"/>' + htmxList_wlgl[i].sl + '</td>';
								html += '</tr>';
							}
							html += '</table>';
							$("#checkwlgl").append(html);
							preventResubmitForm(".modal-footer > button", false);
							$.confirm(responseText["message"] + ' 是否继续提交！',function(result){
				    			if(result){
				    				$("#checkqdl").val("0"); // 跳过起订量校验
				    				submitForm(opts["formName"]||"editContractForm",function(responseText,statusText){
				    					if(responseText["status"] == 'success'){
				    						$.success(responseText["message"],function() {
				    							if(opts.offAtOnce){
				    								$.closeModal(opts.modalName);
													contractResult();
				    							}
				    						});
				    					}else if(responseText["status"] == "fail"){
				    						$.error(responseText["message"],function() {
			    								preventResubmitForm(".modal-footer > button", false);
			    							});
				    						// 加载页面提示div
				    						var htmxList_qgmx = responseText["htmxList_qgmx"];
				    						if(htmxList_qgmx && htmxList_qgmx.length > 0){
				    							$("#checkqgmx").attr("style","display:block;");
				    							$("#checkqgmx").empty();
				    							var html = '<table border="1" cellspacing="0" cellpadding="0" class="table table-hover table-bordered table-hidden" style="text-align: center;">';
				    							html += '<tr style="background-color: #63a3ff;">';
				    							html += '<th style="width:100px;"><span />请购单号</th>';
				    							html += '<th style="width:100px;"><span />物料编码</th>';
				    							html += '<th style="width:100px;"><span />起订量</th>';
				    							html += '<th style="width:100px;"><span />未完成总数</th>';
				    							html += '<th style="width:100px;"><span />采购中数</th>';
				    							html += '<th style="width:100px;"><span />当前数量</th>';
				    							html += '</tr>';
				    							for (var i = 0; i < htmxList_qgmx.length; i++) {
				    								html += '<tr>';
				    								html += '<td><span title="' + htmxList_qgmx[i].djh + '"/>' + htmxList_qgmx[i].djh + '</td>';
				    								html += '<td><span title="' + htmxList_qgmx[i].wlbm + '"/>' + htmxList_qgmx[i].wlbm + '</td>';
				    								html += '<td><span title="' + htmxList_qgmx[i].qdl + '"/>' + htmxList_qgmx[i].qdl + '</td>';
				    								html += '<td><span title="' + htmxList_qgmx[i].sysl + '"/>' + htmxList_qgmx[i].sysl + '</td>';
				    								html += '<td><span title="' + htmxList_qgmx[i].ydsl + '"/>' + htmxList_qgmx[i].ydsl + '</td>';
				    								html += '<td><span title="' + htmxList_qgmx[i].sl + '"/>' + htmxList_qgmx[i].sl + '</td>';
				    								html += '</tr>';
				    							}
				    							html += '</table>';
				    							$("#checkqgmx").append(html);
				    						}
				    					} else{
				    						$.alert(responseText["message"],function() {
				    						});
				    					}
				    				},".modal-footer > button");
				    				
				    			}
				    		});
				    	}else if(htmxList_qgmx && htmxList_qgmx.length > 0){
							$("#checkqgmx").attr("style","display:block;");
							$("#checkqgmx").empty();
							var html = '<table border="1" cellspacing="0" cellpadding="0" class="table table-hover table-bordered table-hidden" style="text-align: center;">';
							html += '<tr style="background-color: #63a3ff;">';
							html += '<th style="width:100px;"><span />请购单号</th>';
							html += '<th style="width:100px;"><span />物料编码</th>';
							html += '<th style="width:100px;"><span />起订量</th>';
							html += '<th style="width:100px;"><span />未完成总数</th>';
							html += '<th style="width:100px;"><span />采购中数</th>';
							html += '<th style="width:100px;"><span />当前数量</th>';
							html += '</tr>';
							for (var i = 0; i < htmxList_qgmx.length; i++) {
								html += '<tr>';
								html += '<td><span title="' + htmxList_qgmx[i].djh + '"/>' + htmxList_qgmx[i].djh + '</td>';
								html += '<td><span title="' + htmxList_qgmx[i].wlbm + '"/>' + htmxList_qgmx[i].wlbm + '</td>';
								html += '<td><span title="' + htmxList_qgmx[i].qdl + '"/>' + htmxList_qgmx[i].qdl + '</td>';
								html += '<td><span title="' + htmxList_qgmx[i].sysl + '"/>' + htmxList_qgmx[i].sysl + '</td>';
								html += '<td><span title="' + htmxList_qgmx[i].ydsl + '"/>' + htmxList_qgmx[i].ydsl + '</td>';
								html += '<td><span title="' + htmxList_qgmx[i].sl + '"/>' + htmxList_qgmx[i].sl + '</td>';
								html += '</tr>';
							}
							html += '</table>';
							$("#checkqgmx").append(html);
							$.error(responseText["message"],function() {
								preventResubmitForm(".modal-footer > button", false);
							});
						}else{
							$.error(responseText["message"],function() {
								preventResubmitForm(".modal-footer > button", false);
							});
						}
					}else{
						$.alert(responseText["message"],function() {
							preventResubmitForm(".modal-footer > button", false);
						});
					}
				},".modal-footer > button");
				return false;
			}
		},
		successtwo : {
			label : "保 存",
			className : "btn-success",
			callback : function() {
				//代表点的是保存按钮，跳过数量，价格，期望到货日期校验
				$("#editContractForm #bcbj").val("2");
				if(!$("#editContractForm").valid()){
					return false;
				}
				// 判断合同明细数量
				var isBeyond = "true";
				$("#editContractForm div[name='sldiv']").each(function(){
					if(this.getAttribute("isBeyond") == "false"){
						isBeyond = "false";
						return false;
					}
				});
				if(isBeyond == "false"){
					$.error("合同明细数量超出，请重新填写！");
					return false;
				}

				var $this = this;
				var opts = $this["options"]||{};
				if(t_map.rows != null && t_map.rows.length > 0){
					// 初始化htmxJson
            		var json = [];
					for (var i = 0; i < t_map.rows.length; i++) {
            			var sz = {"htmxid":'',"htid":'',"qgid":'',"qgmxid":'',"wlid":'',"sl":'',"hsdj":'',"hjje":'',"jhdhrq":'',"bz":'',"qgsl":'',"sysl":'',"yds":'',"presl":'',"sfgdzc":'',"bxsj":'',"wlmc":'',"hwmc":'',"wlfl":'',"ccdg":'',"cpzch":'',"kjhtmxid":'',"sfty" : '',"bchtmxid" : '',"xh" : ''};
            			sz.htmxid = t_map.rows[i].htmxid;
						sz.htid = t_map.rows[i].htid;
						sz.qgid = t_map.rows[i].qgid;
						sz.qgmxid = t_map.rows[i].qgmxid;
						sz.wlid = t_map.rows[i].wlid;
						sz.sl = t_map.rows[i].sl;
						sz.hsdj = t_map.rows[i].hsdj;
						sz.hjje = t_map.rows[i].hjje;
						sz.jhdhrq = t_map.rows[i].jhdhrq;
						sz.bz = t_map.rows[i].bz;
						sz.qgsl = t_map.rows[i].qgsl;						
						sz.sysl = t_map.rows[i].sysl;
						sz.yds = t_map.rows[i].yds;
						sz.presl = t_map.rows[i].presl;
						sz.sfgdzc = t_map.rows[i].sfgdzc;
						sz.bxsj = t_map.rows[i].bxsj;
                        sz.wlmc = t_map.rows[i].wlmc;
                        sz.hwmc = t_map.rows[i].hwmc;
						sz.wlfl = t_map.rows[i].wlfl;
						sz.ccdg = t_map.rows[i].ccdg;
						sz.cpzch = t_map.rows[i].cpzch;
						sz.kjhtmxid = t_map.rows[i].kjhtmxid;
						sz.sfty = t_map.rows[i].sfty;
						sz.bchtmxid = t_map.rows[i].bchtmxid;
						sz.xh = t_map.rows[i].xh;
						json.push(sz);
    				}
					$("#editContractForm #wlfls").val('');
            		$("#editContractForm #htmxJson").val(JSON.stringify(json));
				}else{
					$.error("合同明细不能为空！");
					return false;
				}
				//无关json设为null
				$("#editContractForm #htmx_json").val("");
				
				$("#editContractForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"editContractForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								contractResult();
							}
						});
					}else if(responseText["status"] == "failtwo"){
                        $.error(responseText["message"],function() {
                            preventResubmitForm(".modal-footer > button", false);
                        });
                    }else if(responseText["status"] == "fail"){
						// 加载页面提示div
						var htmxList_wlgl = responseText["htmxList_wlgl"];
						var htmxList_qgmx = responseText["htmxList_qgmx"];
						if(htmxList_wlgl && htmxList_wlgl.length > 0){
							$("#checkwlgl").attr("style","display:block;");
							$("#checkwlgl").empty();
							var html = '<table border="1" cellspacing="0" cellpadding="0" class="table table-hover table-bordered table-hidden" style="text-align: center;">';
							html += '<tr style="background-color: #63a3ff;">';
							html += '<th style="width:100px;"><span />物料编码</th>';
							html += '<th style="width:100px;"><span />起订量</th>';
							html += '<th style="width:100px;"><span />当前数量</th>';
							html += '</tr>';
							for (var i = 0; i < htmxList_wlgl.length; i++) {
								html += '<tr>';
								html += '<td><span title="' + htmxList_wlgl[i].wlbm + '"/>' + htmxList_wlgl[i].wlbm + '</td>';
								html += '<td><span title="' + htmxList_wlgl[i].qdl + '"/>' + htmxList_wlgl[i].qdl + '</td>';
								html += '<td><span title="' + htmxList_wlgl[i].sl + '"/>' + htmxList_wlgl[i].sl + '</td>';
								html += '</tr>';
							}
							html += '</table>';
							$("#checkwlgl").append(html);
							preventResubmitForm(".modal-footer > button", false);
							$.confirm(responseText["message"] + ' 是否继续提交！',function(result){
								if(result){
									$("#checkqdl").val("0"); // 跳过起订量校验
									submitForm(opts["formName"]||"editContractForm",function(responseText,statusText){
										if(responseText["status"] == 'success'){
											$.success(responseText["message"],function() {
												if(opts.offAtOnce){
													$.closeModal(opts.modalName);
													contractResult();
												}
											});
										}else if(responseText["status"] == "fail"){
											$.error(responseText["message"],function() {
												preventResubmitForm(".modal-footer > button", false);
											});
											// 加载页面提示div
											var htmxList_qgmx = responseText["htmxList_qgmx"];
											if(htmxList_qgmx && htmxList_qgmx.length > 0){
												$("#checkqgmx").attr("style","display:block;");
												$("#checkqgmx").empty();
												var html = '<table border="1" cellspacing="0" cellpadding="0" class="table table-hover table-bordered table-hidden" style="text-align: center;">';
												html += '<tr style="background-color: #63a3ff;">';
												html += '<th style="width:100px;"><span />请购单号</th>';
												html += '<th style="width:100px;"><span />物料编码</th>';
												html += '<th style="width:100px;"><span />起订量</th>';
												html += '<th style="width:100px;"><span />未完成总数</th>';
												html += '<th style="width:100px;"><span />采购中数</th>';
												html += '<th style="width:100px;"><span />当前数量</th>';
												html += '</tr>';
												for (var i = 0; i < htmxList_qgmx.length; i++) {
													html += '<tr>';
													html += '<td><span title="' + htmxList_qgmx[i].djh + '"/>' + htmxList_qgmx[i].djh + '</td>';
													html += '<td><span title="' + htmxList_qgmx[i].wlbm + '"/>' + htmxList_qgmx[i].wlbm + '</td>';
													html += '<td><span title="' + htmxList_qgmx[i].qdl + '"/>' + htmxList_qgmx[i].qdl + '</td>';
													html += '<td><span title="' + htmxList_qgmx[i].sysl + '"/>' + htmxList_qgmx[i].sysl + '</td>';
													html += '<td><span title="' + htmxList_qgmx[i].ydsl + '"/>' + htmxList_qgmx[i].ydsl + '</td>';
													html += '<td><span title="' + htmxList_qgmx[i].sl + '"/>' + htmxList_qgmx[i].sl + '</td>';
													html += '</tr>';
												}
												html += '</table>';
												$("#checkqgmx").append(html);
											}
										} else{
											$.alert(responseText["message"],function() {
											});
										}
									},".modal-footer > button");

								}
							});
						}else if(htmxList_qgmx && htmxList_qgmx.length > 0){
							$("#checkqgmx").attr("style","display:block;");
							$("#checkqgmx").empty();
							var html = '<table border="1" cellspacing="0" cellpadding="0" class="table table-hover table-bordered table-hidden" style="text-align: center;">';
							html += '<tr style="background-color: #63a3ff;">';
							html += '<th style="width:100px;"><span />请购单号</th>';
							html += '<th style="width:100px;"><span />物料编码</th>';
							html += '<th style="width:100px;"><span />起订量</th>';
							html += '<th style="width:100px;"><span />未完成总数</th>';
							html += '<th style="width:100px;"><span />采购中数</th>';
							html += '<th style="width:100px;"><span />当前数量</th>';
							html += '</tr>';
							for (var i = 0; i < htmxList_qgmx.length; i++) {
								html += '<tr>';
								html += '<td><span title="' + htmxList_qgmx[i].djh + '"/>' + htmxList_qgmx[i].djh + '</td>';
								html += '<td><span title="' + htmxList_qgmx[i].wlbm + '"/>' + htmxList_qgmx[i].wlbm + '</td>';
								html += '<td><span title="' + htmxList_qgmx[i].qdl + '"/>' + htmxList_qgmx[i].qdl + '</td>';
								html += '<td><span title="' + htmxList_qgmx[i].sysl + '"/>' + htmxList_qgmx[i].sysl + '</td>';
								html += '<td><span title="' + htmxList_qgmx[i].ydsl + '"/>' + htmxList_qgmx[i].ydsl + '</td>';
								html += '<td><span title="' + htmxList_qgmx[i].sl + '"/>' + htmxList_qgmx[i].sl + '</td>';
								html += '</tr>';
							}
							html += '</table>';
							$("#checkqgmx").append(html);
							$.error(responseText["message"],function() {
								preventResubmitForm(".modal-footer > button", false);
							});
						}else{
							$.error(responseText["message"],function() {
								preventResubmitForm(".modal-footer > button", false);
							});
						}
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
var addFrameworkContractConfig = {
	width		: "1500px",
	modalName	: "addContractModel",
	formName	: "editContractForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#editContractForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				if(t_map.rows != null && t_map.rows.length > 0){
					// 初始化htmxJson
            		var json = [];
					for (var i = 0; i < t_map.rows.length; i++) {
            			var sz = {"wlid":'',"hwmc":'',"hsdj":'',"dhzq":'',"ccdg" : '',"wlfl" : '',"cpzch" : '',"bxsj":'',"sfgdzc":'',"bz" : '',"scbj" : ''};
						sz.wlid = t_map.rows[i].wlid;
						sz.hwmc = t_map.rows[i].hwmc;
						sz.hsdj = t_map.rows[i].hsdj;
						sz.dhzq = t_map.rows[i].dhzq;
						sz.ccdg = t_map.rows[i].ccdg;
						sz.wlfl = t_map.rows[i].wlfl;
						sz.cpzch = t_map.rows[i].cpzch;
						sz.bxsj = t_map.rows[i].bxsj;
						sz.sfgdzc = t_map.rows[i].sfgdzc;
						sz.bz = t_map.rows[i].bz;
						sz.scbj = t_map.rows[i].scbj;
						json.push(sz);
    				}
            		$("#editContractForm #htmxJson").val(JSON.stringify(json));
				}else{
					$.error("合同明细不能为空！");
					return false;
				}
				wlfls = $("#editContractForm #wlfls").val([]);
				kjht_json =  $("#editContractForm #kjht_json").val([]);
				$("#editContractForm #wlfls").val([]);
				$("#editContractForm #kjht_json").val([]);
				if ("3"==$("#editContractForm #htlx").val()&&$("#editContractForm #bchtid").val()){
					$("#editContractForm #kjlx").attr("disabled", false);
					$("#editContractForm #kjlx").trigger("chosen:updated");
				}
				$("#editContractForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"editContractForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								contractResult();
							}
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$("#editContractForm #wlfls").val(wlfls);
						$("#editContractForm #kjht_json").val(kjht_json);
						if ("3"==$("#editContractForm #htlx").val()&&$("#editContractForm #bchtid").val()){
							$("#editContractForm #kjlx").attr("disabled", "disabled");
							$("#editContractForm #kjlx").trigger("chosen:updated");
						}
                        $.error(responseText["message"],function() {
                        });
                    }else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						if ("3"==$("#editContractForm #htlx").val()&&$("#editContractForm #bchtid").val()){
							$("#editContractForm #kjlx").attr("disabled", "disabled");
							$("#editContractForm #kjlx").trigger("chosen:updated");
						}
						$("#editContractForm #wlfls").val(wlfls);
						$("#editContractForm #kjht_json").val(kjht_json);
						$.error(responseText["message"],function() {

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
var modFrameworkContractConfig = {
	width		: "1500px",
	modalName	: "addContractModel",
	formName	: "editContractForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#editContractForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				if(t_map.rows != null && t_map.rows.length > 0){
					// 初始化htmxJson
					var json = [];
					for (var i = 0; i < t_map.rows.length; i++) {
						var sz = {"htmxid":'',"wlid":'',"hwmc":'',"hsdj":'',"dhzq":'',"ccdg" : '',"wlfl" : '',"cpzch" : '',"bxsj":'',"sfgdzc":'',"bz" : '',"scbj" : ''};
						sz.htmxid = t_map.rows[i].htmxid;
						sz.wlid = t_map.rows[i].wlid;
						sz.hwmc = t_map.rows[i].hwmc;
						sz.hsdj = t_map.rows[i].hsdj;
						sz.dhzq = t_map.rows[i].dhzq;
						sz.ccdg = t_map.rows[i].ccdg;
						sz.wlfl = t_map.rows[i].wlfl;
						sz.cpzch = t_map.rows[i].cpzch;
						sz.bxsj = t_map.rows[i].bxsj;
						sz.sfgdzc = t_map.rows[i].sfgdzc;
						sz.bz = t_map.rows[i].bz;
						sz.scbj = t_map.rows[i].scbj;
						json.push(sz);
					}
					$("#editContractForm #htmxJson").val(JSON.stringify(json));
				}else{
					$.error("合同明细不能为空！");
					return false;
				}
				wlfls = $("#editContractForm #wlfls").val([]);
				kjht_json =  $("#editContractForm #kjht_json").val([]);
				$("#editContractForm #wlfls").val([]);
				$("#editContractForm #kjht_json").val([]);
				$("#editContractForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"editContractForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								contractResult();
							}
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$("#editContractForm #wlfls").val(wlfls);
						$("#editContractForm #kjht_json").val(kjht_json);
						$.error(responseText["message"],function() {
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$("#editContractForm #wlfls").val(wlfls);
						$("#editContractForm #kjht_json").val(kjht_json);
						$.error(responseText["message"],function() {

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
var modContractConfig = {
	width		: "1500px",
	modalName	: "modContractModel",
	formName	: "editContractForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#editContractForm").valid()){
					return false;
				}
				var json=[];
				var yfjes=$("#editContractForm .yfjesz");
				var yfje_l=0;
				var yfrq_l=0;
				if(yfjes!=null && yfjes.length>0){
					for(var i=0;i<yfjes.length;i++){
						var sz={"yfje":"","yfrq":""};
						var yfjeval=$("#editContractForm #yfje"+i).val();
						var yfrqval=$("#editContractForm #yfrq"+i).val();
						if((yfjeval!=null && yfjeval!="") && (yfrqval!=null && yfrqval!="")){
							sz.yfje=yfjeval;
							sz.yfrq=yfrqval;
							json.push(sz);
						}else{
							if(((yfjeval!=null && yfjeval!="") && (yfrqval==null || yfrqval=="")) ||
								((yfjeval==null || yfjeval=="") && (yfrqval!=null && yfrqval!=""))){
								$.confirm("请合理设置预付日期和预付金额！");
								return false;
							}
						}
					}
					$("#editContractForm #fktxJson").val(JSON.stringify(json));
				}
				// 判断合同明细数量
				var isBeyond = "true";
				$("#editContractForm div[name='sldiv']").each(function(){
					if(this.getAttribute("isBeyond") == "false"){
						isBeyond = "false";
						return false;
					}
				});
				if(isBeyond == "false"){
					$.error("合同明细数量超出，请重新填写！");
					return false;
				}

				var $this = this;
				var opts = $this["options"]||{};

				if(t_map.rows != null && t_map.rows.length > 0){
					// 初始化htmxJson
            		var json = [];
					for (var i = 0; i < t_map.rows.length; i++) {
            			var sz = {"htmxid":'',"htid":'',"qgid":'',"qgmxid":'',"wlid":'',"sl":'',"hsdj":'',"hjje":'',"jhdhrq":'',"bz":'',"qgsl":'',"sysl":'',"yds":'',"presl":'','sfgdzc':'',"bxsj":'',"wlfl":'',"ccdg":'',"cpzch":'',"hwmc":'',"kjhtmxid":'',"sfty":'',"bchtmxid":'',"xh":''};
            			sz.htmxid = t_map.rows[i].htmxid;
						sz.htid = t_map.rows[i].htid;
						sz.qgid = t_map.rows[i].qgid;
						sz.qgmxid = t_map.rows[i].qgmxid;
						sz.wlid = t_map.rows[i].wlid;
						sz.sl = t_map.rows[i].sl;
						sz.hsdj = t_map.rows[i].hsdj;
						sz.hjje = t_map.rows[i].hjje;
						sz.jhdhrq = t_map.rows[i].jhdhrq;
						sz.bz = t_map.rows[i].bz;
						sz.qgsl = t_map.rows[i].qgsl;						
						sz.sysl = t_map.rows[i].sysl;
						sz.yds = t_map.rows[i].yds;
						sz.presl = t_map.rows[i].presl;
						sz.sfgdzc = t_map.rows[i].sfgdzc;
						sz.bxsj = t_map.rows[i].bxsj;
						sz.wlfl = t_map.rows[i].wlfl;
						sz.ccdg = t_map.rows[i].ccdg;
						sz.cpzch = t_map.rows[i].cpzch;
						sz.hwmc = t_map.rows[i].hwmc;
						sz.kjhtmxid = t_map.rows[i].kjhtmxid;
						sz.sfty = t_map.rows[i].sfty;
						sz.bchtmxid = t_map.rows[i].bchtmxid;
						sz.xh = t_map.rows[i].xh;
						json.push(sz);
    				}
					$("#editContractForm #wlfls").val('');
            		$("#editContractForm #htmxJson").val(JSON.stringify(json));
				}else{
					$.error("合同明细不能为空！");
					return false;
				}
				//无关json设为null
				$("#editContractForm #htmx_json").val("");
				
				$("#editContractForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"editContractForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								contractResult();
							}
						});
					}else if(responseText["status"] == "fail"){
						// 加载页面提示div
						var htmxList_wlgl = responseText["htmxList_wlgl"];
						var htmxList_qgmx = responseText["htmxList_qgmx"];
						if(htmxList_wlgl && htmxList_wlgl.length > 0){
							$("#checkwlgl").attr("style","display:block;");
							$("#checkwlgl").empty();
							var html = '<table border="1" cellspacing="0" cellpadding="0" class="table table-hover table-bordered table-hidden" style="text-align: center;">';
							html += '<tr style="background-color: #63a3ff;">';
							html += '<th style="width:100px;"><span />物料编码</th>';
							html += '<th style="width:100px;"><span />起订量</th>';
							html += '<th style="width:100px;"><span />当前数量</th>';
							html += '</tr>';
							for (var i = 0; i < htmxList_wlgl.length; i++) {
								html += '<tr>';
								html += '<td><span title="' + htmxList_wlgl[i].wlbm + '"/>' + htmxList_wlgl[i].wlbm + '</td>';
								html += '<td><span title="' + htmxList_wlgl[i].qdl + '"/>' + htmxList_wlgl[i].qdl + '</td>';
								html += '<td><span title="' + htmxList_wlgl[i].sl + '"/>' + htmxList_wlgl[i].sl + '</td>';
								html += '</tr>';
							}
							html += '</table>';
							$("#checkwlgl").append(html);
							preventResubmitForm(".modal-footer > button", false);
							$.confirm(responseText["message"] + ' 是否继续提交！',function(result){
								if(result){
									$("#checkqdl").val("0"); // 跳过起订量校验
									submitForm(opts["formName"]||"editContractForm",function(responseText,statusText){
										if(responseText["status"] == 'success'){
											$.success(responseText["message"],function() {
												if(opts.offAtOnce){
													$.closeModal(opts.modalName);
													contractResult();
												}
											});
										}else if(responseText["status"] == "fail"){
											$.error(responseText["message"],function() {
												preventResubmitForm(".modal-footer > button", false);
											});
											// 加载页面提示div
											var htmxList_qgmx = responseText["htmxList_qgmx"];
											if(htmxList_qgmx && htmxList_qgmx.length > 0){
												$("#checkqgmx").attr("style","display:block;");
												$("#checkqgmx").empty();
												var html = '<table border="1" cellspacing="0" cellpadding="0" class="table table-hover table-bordered table-hidden" style="text-align: center;">';
												html += '<tr style="background-color: #63a3ff;">';
												html += '<th style="width:100px;"><span />请购单号</th>';
												html += '<th style="width:100px;"><span />物料编码</th>';
												html += '<th style="width:100px;"><span />起订量</th>';
												html += '<th style="width:100px;"><span />未完成总数</th>';
												html += '<th style="width:100px;"><span />采购中数</th>';
												html += '<th style="width:100px;"><span />当前数量</th>';
												html += '</tr>';
												for (var i = 0; i < htmxList_qgmx.length; i++) {
													html += '<tr>';
													html += '<td><span title="' + htmxList_qgmx[i].djh + '"/>' + htmxList_qgmx[i].djh + '</td>';
													html += '<td><span title="' + htmxList_qgmx[i].wlbm + '"/>' + htmxList_qgmx[i].wlbm + '</td>';
													html += '<td><span title="' + htmxList_qgmx[i].qdl + '"/>' + htmxList_qgmx[i].qdl + '</td>';
													html += '<td><span title="' + htmxList_qgmx[i].sysl + '"/>' + htmxList_qgmx[i].sysl + '</td>';
													html += '<td><span title="' + htmxList_qgmx[i].ydsl + '"/>' + htmxList_qgmx[i].ydsl + '</td>';
													html += '<td><span title="' + htmxList_qgmx[i].sl + '"/>' + htmxList_qgmx[i].sl + '</td>';
													html += '</tr>';
												}
												html += '</table>';
												$("#checkqgmx").append(html);
											}
										} else{
											$.alert(responseText["message"],function() {
											});
										}
									},".modal-footer > button");

								}
							});
						}else if(htmxList_qgmx && htmxList_qgmx.length > 0){
							$("#checkqgmx").attr("style","display:block;");
							$("#checkqgmx").empty();
							var html = '<table border="1" cellspacing="0" cellpadding="0" class="table table-hover table-bordered table-hidden" style="text-align: center;">';
							html += '<tr style="background-color: #63a3ff;">';
							html += '<th style="width:100px;"><span />请购单号</th>';
							html += '<th style="width:100px;"><span />物料编码</th>';
							html += '<th style="width:100px;"><span />起订量</th>';
							html += '<th style="width:100px;"><span />未完成总数</th>';
							html += '<th style="width:100px;"><span />采购中数</th>';
							html += '<th style="width:100px;"><span />当前数量</th>';
							html += '</tr>';
							for (var i = 0; i < htmxList_qgmx.length; i++) {
								html += '<tr>';
								html += '<td><span title="' + htmxList_qgmx[i].djh + '"/>' + htmxList_qgmx[i].djh + '</td>';
								html += '<td><span title="' + htmxList_qgmx[i].wlbm + '"/>' + htmxList_qgmx[i].wlbm + '</td>';
								html += '<td><span title="' + htmxList_qgmx[i].qdl + '"/>' + htmxList_qgmx[i].qdl + '</td>';
								html += '<td><span title="' + htmxList_qgmx[i].sysl + '"/>' + htmxList_qgmx[i].sysl + '</td>';
								html += '<td><span title="' + htmxList_qgmx[i].ydsl + '"/>' + htmxList_qgmx[i].ydsl + '</td>';
								html += '<td><span title="' + htmxList_qgmx[i].sl + '"/>' + htmxList_qgmx[i].sl + '</td>';
								html += '</tr>';
							}
							html += '</table>';
							$("#checkqgmx").append(html);
							$.error(responseText["message"],function() {
								preventResubmitForm(".modal-footer > button", false);
							});
						}else{
							$.error(responseText["message"],function() {
								preventResubmitForm(".modal-footer > button", false);
							});
						}
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

var uploadContractConfig = {
	width		: "600px",
	modalName	: "uploadContractModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "上传",
			className : "btn-primary",
			callback : function() {
				if(!$("#uploadContractForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var fjids = $("#uploadContractForm #fjids").val();

				if (fjids.length<=0){
					$.error("未上传附件不允许保存！");
					return false;
				}
				$("#uploadContractForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"uploadContractForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								contractResult();
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

var remindContractConfig = {
	width		: "1600px",
	modalName	: "remindContractModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "保存",
			className : "btn-primary",
			callback : function() {
				if(!$("#remindPaymentForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				$("#remindPaymentForm input[name='access_token']").val($("#ac_tk").val());
				var data=JSON.parse($("#remindPaymentForm #fktxJson").val());
				//校验
				if(data!=null && data.length>0){
					for(var i=0;i<data.length;i++){
						if(data[i].yfbl==null || data[i].yfbl==''){
							$.error("第"+(i+1)+"行预付比例不能为空！");
							return false;
						}
						if(data[i].yfje==null || data[i].yfje==''){
							$.error("第"+(i+1)+"行预付金额不能为空！");
							return false;
						}
						if(data[i].yfrq==null || data[i].yfrq==''){
							$.error("第"+(i+1)+"行预付日期不能为空！");
							return false;
						}
					}
				}

				submitForm(opts["formName"]||"remindPaymentForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
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
var contract_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_view = $("#contract_formSearch #btn_view");
		var btn_query = $("#contract_formSearch #btn_query");
		var btn_auditingsearchexport = $("#contract_formSearch #btn_auditingsearchexport");
    	var btn_auditingselectexport = $("#contract_formSearch #btn_auditingselectexport");
		var btn_searchexport = $("#contract_formSearch #btn_searchexport");
		var btn_selectexport = $("#contract_formSearch #btn_selectexport");
    	var btn_add = $("#contract_formSearch #btn_add");
    	var btn_mod = $("#contract_formSearch #btn_mod");
    	var btn_advancedmod = $("#contract_formSearch #btn_advancedmod");
    	var btn_audit = $("#contract_formSearch #btn_audit");
    	var btn_submit = $("#contract_formSearch #btn_submit");
    	var btn_contract=$("#contract_formSearch #btn_contract");
    	var btn_payment=$("#contract_formSearch #btn_payment");
    	var btn_receipt=$("#contract_formSearch #btn_receipt");
    	var btn_del=$("#contract_formSearch #btn_del");
    	var btn_discard=$("#contract_formSearch #btn_discard");
    	var btn_outgrowth=$("#contract_formSearch #btn_outgrowth");
    	var btn_formal=$("#contract_formSearch #btn_formal");
    	var btn_modaudit=$("#contract_formSearch #btn_modaudit");
    	var btn_viewCommon = $("#contract_formSearch #btn_viewCommon");
    	var btn_paymentremind=$("#contract_formSearch #btn_paymentremind");
    	var btn_system = $("#contract_formSearch #btn_system");
    	var btn_wwadd = $("#contract_formSearch #btn_wwadd");
    	var btn_openclose = $("#contract_formSearch #btn_openclose");
    	var btn_framework = $("#contract_formSearch #btn_framework");
    	var btn_supplement = $("#contract_formSearch #btn_supplement");
    	//添加日期控件
    	laydate.render({
    	   elem: '#shsjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#shsjend'
    	  ,theme: '#2381E9'
    	});
    	
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			contractResult(true);
    		});
    	}
  
		  
       /* ---------------------------合同付款维护-----------------------------------*/
		btn_payment.unbind("click").click(function(){
    		var sel_row = $('#contract_formSearch #contract_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			contractById(sel_row[0].htid,"payment",btn_payment.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});		
		
		/* ---------------------------审核修改-----------------------------------*/
		btn_modaudit.unbind("click").click(function(){
    		var sel_row = $('#contract_formSearch #contract_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			contractById(sel_row[0].htid,"modaudit",btn_modaudit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});		
		
		/* ---------------------------合同付款维护-----------------------------------*/
		btn_receipt.unbind("click").click(function(){
    		var sel_row = $('#contract_formSearch #contract_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==0){
				var ids="";
				contractById(ids,"receipt",btn_receipt.attr("tourl"),"","");
			}else{
				var ids="";
				var gysid="";
				var gysmc="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					if(i==0){
						gysid=gysid+sel_row[i].gys;
						gysmc=gysmc+sel_row[i].gysmc;
					}
					if(sel_row[i].zt=='80'){
						if(gysid.indexOf(sel_row[i].gys)!=-1){
							ids= ids + ","+ sel_row[i].htid;
						}else{
							$.error("请选择供应商相同的信息！")
							return;
						}
					}else{
						$.error("审核未通过不允许发票维护！")
						return;
					}
				}
				ids=ids.substr(1);
				contractById(ids,"receipt",btn_receipt.attr("tourl"),gysid,gysmc);
			}
    	});	
		
       /* ---------------------------查看列表-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#contract_formSearch #contract_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			contractById(sel_row[0].htid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
        /* ---------------------------提交-----------------------------------*/
    	btn_submit.unbind("click").click(function(){
    		var sel_row = $('#contract_formSearch #contract_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			if(sel_row[0].zt !='00' && sel_row[0].zt !='15' && sel_row[0].zt !=null){
    				$.error("您好！该条记录已提交或者已通过，不允许重复提交！");
    				return false;
    			}
    			contractById(sel_row[0].htid,"submit",btn_submit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});

		/* ---------------------------付款维护-----------------------------------*/
		btn_paymentremind.unbind("click").click(function(){
			var sel_row = $('#contract_formSearch #contract_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				if(sel_row[0].zt !='80'){
					$.error("您好！该条记录未审核通过，不允许设置付款提醒！");
					return false;
				}
                if(sel_row[0].bchtid != '' && sel_row[0].bchtid!=null){
                    $.error("补充合同不允许付款！");
                    return false;
                }
                var htid = sel_row[0].htid;
                if(sel_row[0].fkfsdm =='001'){
                    $.ajax({
                        type:'post',
                        url:$('#contract_formSearch #urlPrefix').val() + "/contract/contract/pagedataQueryBcht",
                        cache: false,
                        data: {"htid":htid,"access_token":$("#ac_tk").val()},
                        dataType:'json',
                        success:function(result){
                            if (result.fkbj=='1'){
                                $.alert("您好！预付全款不允许设置付款提醒！");
                            }else {
                                contractById(sel_row[0].htid,"paymentremind",btn_paymentremind.attr("tourl"));
                            }
                        }
                    });
                }else{
                    contractById(sel_row[0].htid,"paymentremind",btn_paymentremind.attr("tourl"));
                }
			}else{
				$.error("请选中一行");
			}
		});
    	
    	 /* ---------------------------正式合同上传-----------------------------------*/
    	btn_formal.unbind("click").click(function(){
    		var sel_row = $('#contract_formSearch #contract_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			if(sel_row[0].zt !='80'){
    				$.error("您好！该条记录还未通过审核，不允许上传正式合同！");
    				return false;
    			}
    			contractById(sel_row[0].htid,"formal",btn_formal.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
    	   // 	  ---------------------------导出-----------------------------------
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#contract_formSearch #contract_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].htid;
        		}
        		ids = ids.substr(1);
        		$.showDialog($('#contract_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=HTGL_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});

    	btn_searchexport.unbind("click").click(function(){
    		$.showDialog($('#contract_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=HTGL_SEARCH&expType=search&callbackJs=ConSearchData"
    				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});
		// 	  ---------------------------审核导出-----------------------------------
		btn_auditingselectexport.unbind("click").click(function(){
			var sel_row = $('#contract_formSearch #contract_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].htid;
				}
				ids = ids.substr(1);
				$.showDialog($('#contract_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=HTGL_AUDITINGSELECT&expType=select&ids="+ids
					,btn_auditingselectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
			}else{
				$.error("请选择数据");
			}
		});

		btn_auditingsearchexport.unbind("click").click(function(){
			$.showDialog($('#contract_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=HTGL_AUDITINGSEARCH&expType=search&callbackJs=ConSearchData"
				,btn_auditingsearchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
		});

		//---------------------------审核--------------------------------
		btn_audit.unbind("click").click(function(){
    		var sel_row = $('#contract_formSearch #contract_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			contractById(sel_row[0].htid,"audit",btn_audit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/* --------------------------- 新增合同 -----------------------------------*/
    	btn_add.unbind("click").click(function(){
    		contractById(null, "add", btn_add.attr("tourl"));
    	});
    	/* --------------------------- OA新增合同 -----------------------------------*/
    	btn_system.unbind("click").click(function(){
    		contractById(null, "system", btn_system.attr("tourl"));
    	});
        /* --------------------------- 委外新增合同 -----------------------------------*/
        btn_wwadd.unbind("click").click(function(){
            contractById(null, "wwadd", btn_wwadd.attr("tourl"));
        });
		/* --------------------------- 框架新增合同 -----------------------------------*/
		btn_framework.unbind("click").click(function(){
            contractById(null, "framework", btn_framework.attr("tourl"));
        });

    	/* --------------------------- 修改合同 -----------------------------------*/
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#contract_formSearch #contract_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length == 1){
    			if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
    				contractById(sel_row[0].htid, "mod", btn_mod.attr("tourl"),'','',sel_row[0].htlx);
    			}else{
    				$.alert("该记录在审核中或已审核，不允许修改!");
    			}
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/* --------------------------- 补充合同 -----------------------------------*/
		btn_supplement.unbind("click").click(function(){
			var sel_row = $('#contract_formSearch #contract_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
				if(sel_row[0].zt=='80'){
					contractById(sel_row[0].htid, "supplement", btn_supplement.attr("tourl"),'','',sel_row[0].htlx);
				}else{
					$.alert("该记录未审核通过，不能补充合同!");
				}
			}else{
				$.error("请选中一行");
			}
		});
    	/* --------------------------- 高级修改合同 -----------------------------------*/
    	btn_advancedmod.unbind("click").click(function(){
    		var sel_row = $('#contract_formSearch #contract_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length == 1){
    			contractById(sel_row[0].htid, "advancedmod", btn_advancedmod.attr("tourl"),'','',sel_row[0].htlx);
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
    	/*-----------------------------------生成合同----------------------------------*/
    	btn_contract.unbind().click(function(){
    		var sel_row = $('#contract_formSearch #contract_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length == 1){
    			contractById(sel_row[0].htid, "contract", btn_contract.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	})
    	
    	/* ------------------------------删除合同信息-----------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#contract_formSearch #contract_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else {		
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].htid;
        		}
    			ids=ids.substr(1);
    			$.confirm('您确定要删除所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= $('#contract_formSearch #urlPrefix').val()+btn_del.attr("tourl");
        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								contractResult();
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
    	
    	/* ------------------------------废弃合同信息-----------------------------*/
    	btn_discard.unbind("click").click(function(){
    		var sel_row = $('#contract_formSearch #contract_list').bootstrapTable('getSelections');//获取选择行数据
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			if(sel_row[i].zt !='00' && sel_row[i].zt !='15'){
    				$.error("该记录在审核中或已审核，不允许废弃!");
        			return;
    			}
    		}
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else{
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].htid;
        		}
    			ids=ids.substr(1);
    			$.confirm('您确定要废弃所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url=$('#contract_formSearch #urlPrefix').val() + btn_discard.attr("tourl");
        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								contractResult();
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
    	/* ------------------------------合同外发盖章-----------------------------*/
    	btn_outgrowth.unbind("click").click(function(){
    		var sel_row = $('#contract_formSearch #contract_list').bootstrapTable('getSelections');//获取选择行数据
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			if(sel_row[i].zt!="00"){
        			$.error("有记录已提交过，不允许再次外发盖章！");
        			return;
    			}    				
    		}
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else{
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].htid;
        		}
    			ids=ids.substr(1);
    			$.confirm('您确定合同已经发送给供应商？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= $('#contract_formSearch #urlPrefix').val()+btn_outgrowth.attr("tourl");
        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								contractResult();
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
    	/* ---------------------------共通 查看列表-----------------------------------*/
    	btn_viewCommon.unbind("click").click(function(){
    		var sel_row = $('#contract_formSearch #contract_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			contractById(sel_row[0].htid,"viewCommon",btn_viewCommon.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/* ---------------------------合同行关闭-----------------------------------*/
		btn_openclose.unbind("click").click(function(){
    		var sel_row = $('#contract_formSearch #contract_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
				if (sel_row[0].zt != '80'){
					$.error("合同未审核通过不允许关闭");
				}
				contractById(sel_row[0].htid,"openclose",btn_openclose.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
       	/**显示隐藏**/      
    	$("#contract_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(contract_turnOff){
    			$("#contract_formSearch #searchMore").slideDown("low");
    			contract_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#contract_formSearch #searchMore").slideUp("low");
    			contract_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
    	
    };
    	return oInit;
};

var contract_PageInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
    	var scbj = $("#contract_formSearch a[id^='scbj_id_']");
    	$.each(scbj, function(i, n){
    		var id = $(this).attr("id");
    		var code = id.substring(id.lastIndexOf('_')+1,id.length);
    		if(code === '0'){
    			addTj('scbj',code,'contract_formSearch');
    		}
    	});
    }
    return oInit;
}
    
function contractResult(isTurnBack){
	//关闭高级搜索条件
	$("#contract_formSearch #searchMore").slideUp("low");
	contract_turnOff=true;
	$("#contract_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#contract_formSearch #contract_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#contract_formSearch #contract_list').bootstrapTable('refresh');
	}
}
contractRunEvery = function () {
    $.ajax({
        "dataType" : 'json',
        "type" : "POST",
        "url" : $("#contract_formSearch #urlPrefix").val()+"/contract/contract/pagedataQueryOverTime",
        "data" : { "access_token" : $("#ac_tk").val()},
        "success" : function(data) {
            $("#contractjjNum").text(data.overdueString);
            $("#contractcsNum").text(data.overString);
            let timeoutID = setTimeout( contractRunEvery, 1000 * 600 );
        }
    });
}

window.onbeforeunload = function(){
    try{
        clearTimeout(timeoutID);
    }catch(error){
    };
}

function contractQueryCs(flag){
    var title = "即将超时的合同单";
    if("cs"==flag){
        title = "已超时的合同单";
    }
    $.showDialog($("#contract_formSearch #urlPrefix").val()+"/contract/contract/pagedataQueryOverTable?flg="+flag+"&access_token="+$("#ac_tk").val(),title,contractOverTimeConfig);
}
var contractOverTimeConfig = {
	width		: "1500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
$(function(){

	//0.界面初始化
    var oInit = new contract_PageInit();
    oInit.Init();
	
	// 1.初始化Table
	var oTable = new contract_TableInit();
	oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new contract_ButtonInit();
    oButtonInit.Init();
    contractRunEvery();
	// 所有下拉框添加choose样式
	jQuery('#contract_formSearch .chosen-select').chosen({width: '100%'});
	
	$("#contract_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	
});