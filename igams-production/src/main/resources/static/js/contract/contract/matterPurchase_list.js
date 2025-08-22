var matterPurchase_turnOff=true;
var matterPurchase_TableInit = function () { 
	 var oTableInit = new Object();
	    
	// 初始化Table
	oTableInit.Init = function () {
		$('#matterPurchase_formSearch #tb_list').bootstrapTable({
			url: $("#matterPurchase_formSearch #urlPrefix").val()+'/contract/contract/pageGetListMatterPurchase',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#matterPurchase_formSearch #toolbar', // 工具按钮用哪个容器
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
			columns: [{
				checkbox: true
			}, {
				'field': '',
			    'title': '序号',
			    'align': 'center',
			    'width': '3%',
			    'formatter': function (value, row, index) {
			    　　var options = $('#matterPurchase_formSearch #tb_list').bootstrapTable('getOptions'); 
			    　　return options.pageSize * (options.pageNumber - 1) + index + 1; 
			    }
			},{				
				field: 'qgid',
				title: '请购ID',
				width: '8%',
				align: 'left',
				visible: false
			},{
				field: 'sqrmc',
				title: '申请人',
				width: '5%',
				align: 'left',
				visible: false
			},{
				field: 'htnbbh',
				title: '合同内部编号',
				width: '11%',
				align: 'left',
				formatter:htbhformat,
				visible: true,
				sortable: true,
			}, {				
				field: 'htwbbh',
				title: '合同外部编号',
				width: '11%',
				align: 'left',
				visible: false,
				sortable: true,
			},{
				field: 'gysmc',
				title: '供应商',
				width: '8%',
				align: 'left',
				visible: true
			}, {
				field: 'djh',
				title: '请购单号',
				width: '10%',
				align: 'left',
				formatter:djhformat,
				visible: true,
				sortable: true,
			}, {
				field: 'qgsl',
				title: '请购数量',
				width: '5%',
				align: 'left',
				visible: false,
				sortable: true,
			}, {
				field: 'fph',
				title: '发票号',
				width: '12%',
				align: 'left',
				visible: true,
				formatter:fphformat,
				sortable: true,
			}, {
				field: 'fpsl',
				title: '发票数量',
				width: '6%',
				align: 'left',
				visible: true,
				sortable: true,
			}, {
				field: 'fpzje',
				title: '发票总金额',
				width: '6%',
				align: 'left',
				visible: true,
				sortable: true,
			}, {
				field: 'u8rkdh',
				title: 'U8入库单号',
				width: '12%',
				align: 'left',
				visible: true,
				formatter:u8rkdhformat,
				sortable: true,
			}, {
				field: 'rkzsl',
				title: '入库总数量',
				width: '5%',
				align: 'left',
				visible: true,
				sortable: true,
			}, {
				field: 'bhgsl',
				title: '不合格数量',
				width: '5%',
				align: 'left',
				visible: true,
				sortable: true,
			}, {
				field: 'hsdj',
				title: '单价',
				width: '6%',
				align: 'left',
				visible: true,
				sortable: true,
			}, {
				field: 'wlbm',
				title: '物料编码',
				width: '6%',
				align: 'left',
				formatter:wlbmformat,
				visible: true,
				sortable: true,
			}, {				
				field: 'wlmc',
				title: '物料名称',
				width: '6%',
				align: 'left',
				visible: true,
				sortable: true,
			}, {				
				field: 'wllbmc',
				title: '物料类别',
				width: '8%',
				align: 'left',
				visible: false,
				sortable: true,
			}, {				
				field: 'wlzlbmc',
				title: '物料子类别',
				width: '8%',
				align: 'left',
				visible: false,
				sortable: true,
			}, {				
				field: 'wlzlbtcmc',
				title: '物料子类别统称',
				width: '8%',
				align: 'left',
				visible: false,
				sortable: true,
			}, {				
				field: 'gg',
				title: '规格',
				width: '5%',
				align: 'left',
				visible: false,
				sortable: true,
			}, {				
				field: 'ychh',
				title: '货号',
				width: '5%',
				align: 'left',
				visible: false,
				sortable: true,
			}, {
				field: 'jldw',
				title: '单位',
				width: '4%',
				align: 'left',
				visible: true,
				sortable: true,
			}, {
				field: 'sl',
				title: '数量',
				width: '6%',
				align: 'left',
				visible: true,
				sortable: true,
			}, {
				field: 'hjje',
				title: '合计金额',
				width: '6%',
				align: 'left',
				visible: true,
				sortable: true,
			}, {				
				field: 'kdmc',
				title: '快递',
				width: '4%',
				align: 'left',
				formatter:kdlxformat,
				visible: false,
				sortable: true,
			}, {				
				field: 'kddh',
				title: '快递单号',
				width: '8%',
				align: 'left',
				visible: false,
				sortable: true,
			}, {				
				field: 'jhdhrq',
				title: '计划到货日期',
				width: '6%',
				align: 'left',
				visible: true,
				sortable: true,
			}, {
				field: 'dhrq',
				title: '到货日期',
				width: '7%',
				align: 'left',
				formatter:dhrqformat,
				visible: true,
				sortable: true,
			}, {
				field: 'dhdh',
				title: '到货单号',
				width: '8%',
				align: 'left',
				formatter:dhdhformat,
				visible: false,
				sortable: true,
			}, {				
				field: 'sl',
				title: '到货数量',
				width: '8%',
				align: 'left',
				visible: false,
				sortable: true,
			}, {
				field: 'fplrsj',
				title: '交给财务时间',
				width: '8%',
				align: 'left',
				formatter:fplrsjformat,
				visible: true,
				sortable: true,
			},  {
				field: 'htfxmc',
				title: '合同风险程度',
				width: '8%',
				align: 'left',
				visible: true,
				sortable: true,
			},  {
				field: 'ccdg',
				title: '初次订购',
				width: '8%',
				align: 'left',
				formatter:sfccdgformat,
				visible: false,
				sortable: true,
			},  {
				field: 'wlflmc',
				title: '物料分类',
				width: '8%',
				align: 'left',
				visible: false,
				sortable: true,
			},{
				field: 'fkfsmc',
				title: '付款方式',
				width: '6%',
				align: 'left',
				visible: false,
				sortable: true,
			}, {
				field: 'fkrq',
				title: '付款日期',
				width: '6%',
				align: 'left',
				visible: false,
				formatter:fkrqformat,
				sortable: true,
			}, {
				field: 'fkje',
				title: '付款金额',
				width: '6%',
				align: 'left',
				visible: false,
				formatter:fkjeformat,
				sortable: true,
			}, {
				field: 'wfkje',
				title: '未付款金额',
				width: '6%',
				align: 'left',
				visible: false,
				sortable: true,
			}, {
				field: 'bz',
				title: '备注',
				width: '6%',
				align: 'left',
				visible: false,
				sortable: true,
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				matterPurchaseById(row.htmxid,'view',$("#matterPurchase_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#matterPurchase_formSearch #tb_list").colResizable({
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
        sortLastName: "htmx.htid", // 防止同名排位用
        sortLastOrder: "desc" // 防止同名排位用
        // 搜索框使用
        // search:params.search
    };
    return matterPurchaseSearchData(map);
	};
	return oTableInit;
};
/**
 * 是否初次订购
 * @returns
 */
function sfccdgformat(value,row,index) {
	var html="";
	if (row.ccdg == '1') {
		html="<span>"+"是"+"</span>";
	}else if (row.ccdg == '0'){
		html="<span>"+"否"+"</span>";
	}
	return html;
}
/**
 * U8入库单号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function u8rkdhformat(value,row,index){
	var html="";
	if(row.u8rkdh!=null&&row.u8rkdh!=''){
		var split = row.u8rkdh.split(',');
		if(split!=null&&split.length>0){
			for(var i=0;i<split.length;i++){
				html+="<span class='col-md-12 col-sm-12'>";
				var split1 = split[i].split("-");
				if(row.lbcskz1!=null&&row.lbcskz1!=''){
					if(split1[0]!=null&&split1[0]!=''){
						html+="<a href='javascript:void(0);' style='width:70%;line-height:24px;' onclick=\"getInfoByDhdh('"+split1[2]+"')\">"+split1[0]+"</a>";
					}
				}else{
					if(split1[0]!=null&&split1[0]!=''){
						html+="<a href='javascript:void(0);' style='width:70%;line-height:24px;' onclick=\"getInfoByRkid('"+split1[1]+"')\">"+split1[0]+"</a>";
					}
				}
				html+="</span>";
			}
		}
	}
	return html;
}

function getInfoByDhdh(dhid){
	var url=$("#matterPurchase_formSearch #urlPrefix").val()+"/storehouse/arrivalGoods/pagedataViewArrivalGoods?dhid="+dhid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'到货信息',viewDhConfig);
}
var viewDhConfig = {
	width		: "1600px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function getInfoByRkid(rkid){
	var url=$("#matterPurchase_formSearch #urlPrefix").val()+"/warehouse/putInStorage/pagedataViewPutInStorage?rkid="+rkid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'入库信息',viewHwConfig);
}
var viewHwConfig={
	width		: "1600px",
	modalName	:"viewHwModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

/**
 * 发票号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fphformat(value,row,index){
	var html="";
	if(row.fph!=null&&row.fph!=''){
		var delReplyChar1 = delReplyChar(row.fph);
		for(var i=0;i<delReplyChar1.length;i++){
			var split = delReplyChar1[i].split("-");
			html+="<span class='col-md-12 col-sm-12'>";
			html += "<a href='javascript:void(0);' onclick=\"queryByFpid('"+split[1]+"')\">"+split[0]+"</a>";
			html+="</span>";
		}
	}
	return html;
}

/**
 * 到货日期格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function dhrqformat(value,row,index){
	var html="";
	if(row.dhrq!=null&&row.dhrq!=''){
		var split1 = row.dhrq.split(",");
		for(var i=0;i<split1.length;i++){
			html+="<span class='col-md-12 col-sm-12'>";
			html += "<span>"+split1[i]+"</span>";
			html+="</span>";
		}
	}
	return html;
}

/**
 * 交给财务事件格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fplrsjformat(value,row,index){
	var html="";
	if(row.fplrsj!=null&&row.fplrsj!=''){
		var split1 = row.fplrsj.split(",");
		for(var i=0;i<split1.length;i++){
			html+="<span class='col-md-12 col-sm-12'>";
			html += "<span>"+split1[i]+"</span>";
			html+="</span>";
		}
	}
	return html;
}

/**
 * 付款日期格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fkrqformat(value,row,index){
	var html="";
	if(row.fkrq!=null&&row.fkrq!=''){
		var split1 = row.fkrq.split(",");
		for(var i=0;i<split1.length;i++){
			html+="<span class='col-md-12 col-sm-12'>";
			html += "<span>"+split1[i]+"</span>";
			html+="</span>";
		}
	}
	return html;
}

/**
 * 付款金额格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fkjeformat(value,row,index){
	var html="";
	if(row.fkje!=null&&row.fkje!=''){
		var split1 = row.fkje.split(",");
		for(var i=0;i<split1.length;i++){
			html+="<span class='col-md-12 col-sm-12'>";
			html += "<span>"+split1[i]+"</span>";
			html+="</span>";
		}
	}
	return html;
}

function queryByFpid(fpid){
	var url=$("#matterPurchase_formSearch #urlPrefix").val()+"/invoice/invoice/viewInvoice?fpid="+fpid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'发票详情',viewFpConfig);
}
var viewFpConfig = {
	width		: "1400px",
	modalName	:"viewInvoiceModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var delReplyChar = function (str) {
	var arr = str.split(',');
	var result = {};
	var resultArr = [];
	for (var i = 0; i < arr.length; i++) {
		if(!result[arr[i]]){
			result[arr[i]] = arr[i];
			resultArr.push(arr[i]);
		}
	}
	return resultArr;
};


/**
 * 快递类型格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function kdlxformat(value,row,index){
	var html = "";
	if(row.cskz2=="1" && row.kdlxmc!=null){
		html += "<span>"+row.kdlxmc+"</span>";
	}else if(row.kdmc!=null){
		html += "<span>"+row.kdmc+"</span>";
	}else{
		html +="-";
	}
	return html;
}

/**
 * 到货单号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function dhdhformat(value,row,index){
	var html = "";
	if(row.dhdh!=null){
		html += "<a href='javascript:void(0);' onclick=\"queryBydhdh('"+row.dhid+"')\">"+row.dhdh+"</a>";
	}
	return html;
}
function queryBydhdh(dhid){
	var url=$("#matterPurchase_formSearch #urlPrefix").val()+"/storehouse/arrivalGoods/pagedataViewArrivalGoods?dhid="+dhid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'到货信息',viewhwConfig);	
}

/**
 * 合同内部编号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function htbhformat(value,row,index){
	var html = "";
	if(row.htnbbh!=null){
			html += "<a href='javascript:void(0);' onclick=\"queryByhtid('"+row.htid+"')\">"+row.htnbbh+"</a>";	
	}else{
		html +="-"
	}
	return html;
}
function queryByhtid(htid){
	var url=$("#matterPurchase_formSearch #urlPrefix").val()+"/contract/contract/pagedataViewContract?htid="+htid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'合同信息',viewhtConfig);	
}
var viewhtConfig = {
		width		: "1500px",
		modalName	:"viewHtModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wlbmformat(value,row,index){
	var html = "";
	if(row.wlbm!=null && row.wlbm!=''){
		html += "<a href='javascript:void(0);' onclick=\"queryByhWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";
	}
	return html;
}
function queryByhWlbm(wlid){
	var url=$("#matterPurchase_formSearch #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'物料信息',viewWlConfig);	
}

var viewWlConfig = {
		width		: "800px",
		height		: "500px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
/**
 * 单据号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function djhformat(value,row,index){
	var html = ""; 	
	if(row.djh!=null && row.djh!=''){
		html += "<a href='javascript:void(0);' onclick=\"queryByDoubleqgid('"+row.qgid+"')\">"+row.djh+"</a>";
	}	
	return html;
}
function queryByDoubleqgid(qgid){
	var url=$("#matterPurchase_formSearch #urlPrefix").val()+"/purchase/purchase/viewPurchase?qgid="+qgid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'请购信息',viewqgConfig);	
}
var viewqgConfig={
		width		: "1500px",
		modalName	:"viewQgModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	}
/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
	if (row.zt == '10' && row.scbj =='0' ) {
		return "<span class='btn btn-warning' onclick=\"recallPurchase('" + row.htid + "',event)\" >撤回</span>";
	}else if(row.zt == '10' && row.scbj ==null ){
		return "<span class='btn btn-warning' onclick=\"recallPurchase('" + row.htid + "',event)\" >撤回</span>";
	}else{
		return "";
	}		
}

//撤回项目提交
function recallPurchase(htid,event){
	var auditType = $("#matterPurchase_formSearch #auditType").val();
	var msg = '您确定要撤回该合同吗？';
	var purchase_params = [];
	purchase_params.prefix = $("#matterPurchase_formSearch #urlPrefix").val();
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,htid,function(){
				searchMaterResult();
			},purchase_params);
		}
	});
}

function matterPurchaseSearchData(map){
	var cxtj=$("#matterPurchase_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#matterPurchase_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr;
	}else if(cxtj=="1"){
		map["htnbbh"]=cxnr;
	}else if(cxtj=="2"){
		map["htwbbh"]=cxnr;
	}else if (cxtj == "3") {
    	map["wlmc"] = cxnr;
	}else if (cxtj == "4") {
    	map["wlbm"] = cxnr;
	}else if (cxtj == "5") {
    	map["wllbmc"] = cxnr;
	}else if (cxtj == "6") {
    	map["wlzlbmc"] = cxnr;
	}else if (cxtj == "7") {
    	map["wlzlbtcmc"] = cxnr;
	}else if (cxtj == "8") {
    	map["djh"] = cxnr;
	}else if (cxtj == "9"){
		map["hwrkdh"] = cxnr;
	}else if (cxtj == "10"){
		map["kdmc"] = cxnr;
	}else if (cxtj == "11"){
		map["gysmc"] = cxnr;
	}
	
	// 发票方式
	var fpfs = jQuery('#matterPurchase_formSearch #fpfs_id_tj').val();
	map["fpfss"] = fpfs;

	// 货物标记
	var hwbj = jQuery('#matterPurchase_formSearch #hwbj_id_tj').val();
	map["hwbj"] = hwbj;
	// 初次订购
	var ccdg = jQuery('#matterPurchase_formSearch #ccdg_id_tj').val();
	map["ccdg"] = ccdg;

	// 付款方式
	var fkfs = jQuery('#matterPurchase_formSearch #fkfs_id_tj').val();
	map["fkfss"] = fkfs;
	//计划到货日期开始时间
	var jhdhrqstart = jQuery('#matterPurchase_formSearch #jhdhrqstart').val();
	map["jhdhrqstart"] = jhdhrqstart;
    //计划到货日期结束时间
	var jhdhrqend = jQuery('#matterPurchase_formSearch #jhdhrqend').val();
	map["jhdhrqend"] = jhdhrqend;
	//到货日期开始时间
	var dhrqstart = jQuery('#matterPurchase_formSearch #dhrqstart').val();
	map["dhrqstart"] = dhrqstart;
    //到货日期开始时间
	var dhrqend = jQuery('#matterPurchase_formSearch #dhrqend').val();
	map["dhrqend"] = dhrqend;
	//交给财务时间开始时间
	var lrsjstart = jQuery('#matterPurchase_formSearch #lrsjstart').val();
	map["lrsjstart"] = lrsjstart;
	//交给财务时间结束时间
	var lrsjend = jQuery('#matterPurchase_formSearch #lrsjend').val();
	map["lrsjend"] = lrsjend;
	//请购审核通过时间开始时间
	var qgshtgsjstart = jQuery('#matterPurchase_formSearch #qgshtgsjstart').val();
	map["qgshtgsjstart"] = qgshtgsjstart;
	//请购审核通过时间结束时间
	var qgshtgsjend = jQuery('#matterPurchase_formSearch #qgshtgsjend').val();
	map["qgshtgsjend"] = qgshtgsjend;
	//合同审核通过时间开始时间
	var htshtgsjstart = jQuery('#matterPurchase_formSearch #htshtgsjstart').val();
	map["htshtgsjstart"] = htshtgsjstart;
	//合同审核通过时间结束时间
	var htshtgsjend = jQuery('#matterPurchase_formSearch #htshtgsjend').val();
	map["htshtgsjend"] = htshtgsjend;
	return map;
}
function matterPurchaseResult(isTurnBack){
	if(isTurnBack){
		$('#matterPurchase_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#matterPurchase_formSearch #tb_list').bootstrapTable('refresh');
	}
}

//提供给导出用的回调函数
function ConSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="htgl.ddrq";
	map["sortLastOrder"]="desc";
	map["sortName"]="htgl.cjrq";
	map["sortOrder"]="desc";
	return matterPurchaseSearchData(map);
}



function matterPurchaseById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#matterPurchase_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?htmxid=" +id;
		$.showDialog(url,'合同详细信息',viewMatterPurchaseConfig);
	}else if(action == 'maintenance'){
		var url= tourl + "?ids=" +id;
		$.showDialog(url,'过程维护',maintenanceMatterPurchaseConfig);
	}
}
function preSubmitRecheck(){
	return true;
}
var viewMatterPurchaseConfig = {
	width		: "1600px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	} 
};

var maintenanceMatterPurchaseConfig = {
	width		: "800px",
	modalName	: "maintenanceModal",
	formName	: "maintenanceForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if($("#maintenanceForm #kdlx").val()==null || $("#maintenanceForm #kdlx").val()==""){
					$.alert("请填写完整信息");
					return false;
				}
				if($("#maintenanceForm #kddh").val()==null || $("#maintenanceForm #kddh").val()==""){
					$.alert("请填写完整信息");
					return false;
				}
				if($("#maintenanceForm #yzbj").val()=="1"){
					if($("#maintenanceForm #kdlxmc").val()==null || $("#maintenanceForm #kdlxmc").val()==""){
						$.alert("请填写完整信息");
						return false;
					}				
				}
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#maintenanceForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"maintenanceForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								matterPurchaseResult();
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

var matterPurchase_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_view = $("#matterPurchase_formSearch #btn_view");
		var btn_query = $("#matterPurchase_formSearch #btn_query");
		var btn_searchexport = $("#matterPurchase_formSearch #btn_searchexport");
    	var btn_selectexport = $("#matterPurchase_formSearch #btn_selectexport");
    	var btn_maintenance = $("#matterPurchase_formSearch #btn_maintenance");
    	
    	//添加日期控件
    	laydate.render({
    	   elem: '#matterPurchase_formSearch #jhdhrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#matterPurchase_formSearch #jhdhrqend'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#matterPurchase_formSearch #dhrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#matterPurchase_formSearch #dhrqend'
    	  ,theme: '#2381E9'
    	});
		//添加日期控件
		laydate.render({
			elem: '#matterPurchase_formSearch #lrsjstart'
			,theme: '#2381E9'
		});
		//添加日期控件
		laydate.render({
			elem: '#matterPurchase_formSearch #lrsjend'
			,theme: '#2381E9'
		});
		//添加日期控件
		laydate.render({
			elem: '#matterPurchase_formSearch #qgshtgsjstart'
			, theme: '#2381E9'
			, type: 'datetime'
			, format: 'yyyy-MM-dd HH:mm:ss'//保留时分
		});
		//添加日期控件
		laydate.render({
			elem: '#matterPurchase_formSearch #qgshtgsjend'
			, theme: '#2381E9'
			, type: 'datetime'
			, format: 'yyyy-MM-dd HH:mm:ss'//保留时分
		});
		//添加日期控件
		laydate.render({
			elem: '#matterPurchase_formSearch #htshtgsjstart'
			, theme: '#2381E9'
			, type: 'datetime'
			, format: 'yyyy-MM-dd HH:mm:ss'//保留时分
		});
		//添加日期控件
		laydate.render({
			elem: '#matterPurchase_formSearch #htshtgsjend'
			, theme: '#2381E9'
			, type: 'datetime'
			, format: 'yyyy-MM-dd HH:mm:ss'//保留时分
		});
    	
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			matterPurchaseResult(true);
    		});
    	}
  
       /* ---------------------------查看列表-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#matterPurchase_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			matterPurchaseById(sel_row[0].htmxid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
    	  /* ---------------------------过程维护-----------------------------------*/
    	btn_maintenance.unbind("click").click(function(){
    		var sel_row = $('#matterPurchase_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    		}else {		
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].htmxid;
        		}
    			ids=ids.substr(1);
    			matterPurchaseById(ids,"maintenance",btn_maintenance.attr("tourl"));
    		}
    	});
    	
    	   // 	  ---------------------------导出-----------------------------------
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#matterPurchase_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].htmxid;
        		}
        		ids = ids.substr(1);
        		$.showDialog($('#matterPurchase_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=PURCHASE_MATER_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});

    	btn_searchexport.unbind("click").click(function(){
    		$.showDialog($('#matterPurchase_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=PURCHASE_MATER_SEARCH&expType=search&callbackJs=ConSearchData"
    				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});
    	
    	/**显示隐藏**/      
    	$("#matterPurchase_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(matterPurchase_turnOff){
    			$("#matterPurchase_formSearch #searchMore").slideDown("low");
    			matterPurchase_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#matterPurchase_formSearch #searchMore").slideUp("low");
    			matterPurchase_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
    	
    };
    	return oInit;
    };

    function matterPurchaseResult(isTurnBack){
    	if(isTurnBack){
    		$('#matterPurchase_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
    	}else{
    		$('#matterPurchase_formSearch #tb_list').bootstrapTable('refresh');
    	}
    }


$(function(){

	//0.界面初始化
	// 1.初始化Table
	var oTable = new matterPurchase_TableInit();
	oTable.Init();
	
    //2.初始化Button的点击事件
    var oButtonInit = new matterPurchase_ButtonInit();
    oButtonInit.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#matterPurchase_formSearch .chosen-select').chosen({width: '100%'});
	
	$("#matterPurchase_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	
});