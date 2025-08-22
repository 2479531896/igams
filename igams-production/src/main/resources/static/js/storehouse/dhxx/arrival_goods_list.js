var arrivalGoods_turnOff=true;
var dhmx_json = [];
var arrivalGoods_TableInit = function () { 
	 var oTableInit = new Object();
	    
	// 初始化Table
	oTableInit.Init = function () {
		$('#arrivalGoods_formSearch #arrivalGoods_list').bootstrapTable({
			url: $("#arrivalGoods_formSearch #urlPrefix").val()+'/storehouse/arrivalGoods/pageGetListArrivalGoods',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#arrivalGoods_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"zt,dhrq",					// 排序字段
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
			uniqueId: "dhid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮                 								
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true,
				width: '4%'
			},{
            	title: '序号',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '4%',
                align: 'center',
                visible:true
            },{				
				field: 'dhdh',
				title: '到货单号',
				width: '12%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'dhlxmc',
				title: '到货类型',
				width: '12%',
				align: 'left',
				sortable: true,
				visible: false
			},{				
				field: 'rklxmc',
				title: '入库类别',
				width: '12%',
				align: 'left',
				sortable: true,
				formatter:arrivalGoods_rklbFormat,
				visible: true
			},{				
				field: 'gysmc',
				title: '供应商名称',
				width: '15%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'dhrq',
				title: '到货日期',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'bz',
				title: '备注',
				width: '25%',
				align: 'left',
				visible: true
			}, {
				field: 'zldh',
				title: '指令单号',
				width: '14%',
				align: 'left',
				visible: true
			},{				
				field: 'wcbj',
				title: '是否完成',
				width: '10%',
				align: 'center',
				formatter:arrivalGoods_wcbjformat,
				visible: true
			}, {				
				field: 'qrbj',
				title: '确认标记',
				width: '10%',
				align: 'left',
				formatter:arrivalGoods_qrbjformat,
				visible: true
			}, {				
				field: 'zsxm',
				title: '确认人员',
				width: '10%',
				align: 'left',
				visible: false
			}, {				
				field: 'qrsj',
				title: '确认时间',
				width: '10%',
				align: 'left',
				visible: false
			}, {				
				field: 'zt',
				title: '审核状态',
				width: '10%',
				align: 'left',
				formatter:arrivalGoods_ztformat,
				visible: true
			}, {
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '10%',
				align: 'center',
				formatter:arrivalGoods_czFormat,
				visible: true
			}, {
				field: 'jybj',
				title: '借用标记',
				titleTooltip:'借用标记',
				width: '10%',
				align: 'center',
				formatter:arrivalGoods_jybjFormat,
				visible: true
			}, {
				field: 'jyzl',
				title: '借用总量',
				titleTooltip:'借用总量',
				width: '10%',
				align: 'center',
				visible: true
			}, {
				field: 'ghzl',
				title: '归还总量',
				titleTooltip:'归还总量',
				width: '10%',
				align: 'center',
				visible: true
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				arrivalGoodsById(row.dhid,'view',$("#arrivalGoods_formSearch #btn_view").attr("tourl"));
//				arrivalGoodsById(row.dhid,'viewCommon',$("#arrivalGoods_formSearch #btn_viewCommon").attr("tourl"));
			},
		});
		$("#arrivalGoods_formSearch #arrivalGoods_list").colResizable({
			liveDrag:true, 
			gripInnerHtml:"<div class='grip'></div>", 
			draggingClass:"dragging", 
			resizeMode:'fit', 
			postbackSafe:false,
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
        sortLastName: "dhid", // 防止同名排位用
        sortLastOrder: "desc" // 防止同名排位用
        // 搜索框使用
        // search:params.search
    };
    return arrivalGoodsSearchData(map);
	};
	return oTableInit;
};

//操作格式化
function arrivalGoods_czFormat(value,row,index){
	if (row.zt == '10' && row.scbj =='0' ) {
		return "<span class='btn btn-warning' onclick=\"recallarrivalGoods('" + row.dhid + "','" + row.rklxdm + "',event)\" >撤回</span>";
	}else if(row.zt == '10' && row.scbj ==null ){
		return "<span class='btn btn-warning' onclick=\"recallarrivalGoods('" + row.dhid + "','" + row.rklxdm + "',event)\" >撤回</span>";
	}else{
		return "";
	}	
}

//状态格式化
function arrivalGoods_ztformat(value,row,index){
	   if (row.zt == '00') {
	        return '未提交';
	    }else if (row.zt == '80'){
			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.dhid + "\",event,\"AUDIT_GOODS_ARRIVAL,AUDIT_GOODS_PENDING\",{prefix:\"" + $('#arrivalGoods_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
	    }else if (row.zt == '15') {

			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.dhid + "\",event,\"AUDIT_GOODS_ARRIVAL,AUDIT_GOODS_PENDING\",{prefix:\"" + $('#arrivalGoods_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
	    }else{

			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.dhid + "\",event,\"AUDIT_GOODS_ARRIVAL,AUDIT_GOODS_PENDING\",{prefix:\"" + $('#arrivalGoods_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
	    }
}

function arrivalGoods_jybjFormat(value,row,index){
	   if (row.jybj == '0') {
	        return "<span style='color:green;'>无需借用</span>";
	    }else if(row.jybj == '1') {
	    	return "<span style='color:orange;'>需要借用</span>";
	    }else if(row.jybj == null) {
	    	return "<span style='color:green;'>无需借用</span>";
	    }
}

function arrivalGoods_rklbFormat(value,row,index){
	if (row.rklxmc==null){
		return "";
	}
	if (row.rklxdm == 'cghz') {
		return "<span style='color:red;'>"+row.rklxmc+"</span>";
	}else{
		return "<span>"+row.rklxmc+"</span>";
	}
}

//完成标记格式化
function arrivalGoods_wcbjformat(value,row,index){
	   if (row.wcbj == '0') {
	        return '否';
	    }else{
	    	return '是';
	    }
}

//确认标记格式化
function arrivalGoods_qrbjformat(value,row,index){
	   if (row.qrbj == '0') {
	        return '否';
	    }else if (row.qrbj == '1'){
	    	return '是';
	    }else{
	    	return '-';
	    }
}

function arrivalGoodsSearchData(map){
	var cxtj=$("#arrivalGoods_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#arrivalGoods_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["wlmc"]=cxnr;
	}else if(cxtj=="1"){
		map["dhdh"]=cxnr;
	}else if(cxtj=="2"){
		map["gysmc"]=cxnr;
	}else if(cxtj=="3"){
		map["htnbbh"]=cxnr;
	}else if(cxtj=="4"){
		map["wlbm"]=cxnr;
	}else if(cxtj=="5"){
		map["rkdh"]=cxnr;
	}else if(cxtj=="6"){
		map["jydh"]=cxnr;
	}else if(cxtj=="7"){
		map["u8rkdh"]=cxnr;
	}
	
	// 到货开始日期
	var dhsjstart = jQuery('#arrivalGoods_formSearch #dhsjstart').val();
	map["dhsjstart"] = dhsjstart;
	
	// 到货结束日期
	var dhsjend = jQuery('#arrivalGoods_formSearch #dhsjend').val();
	map["dhsjend"] = dhsjend;
	
	// 到货类型
	var dhlxs = jQuery('#arrivalGoods_formSearch #dhlx_id_tj').val();
	map["dhlxs"] = dhlxs;
	
	// 入库类别
	var rklbs = jQuery('#arrivalGoods_formSearch #rklb_id_tj').val();
	map["rklbs"] = rklbs;
	
	return map;
}


//提供给导出用的回调函数
function ArrGoodsSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="dhxx.dhrq";
	map["sortLastOrder"]="desc";
	map["sortName"]="dhxx.dhdh";
	map["sortOrder"]="desc";
	return arrivalGoodsSearchData(map);
}

function arrivalGoodsById(id,action,tourl,rklxdm,clbj){
	if(!tourl){
		return;
	}
	tourl = $("#arrivalGoods_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?dhid=" +id;
		$.showDialog(url,'到货详细信息',viewArrivalGoodsConfig);
	}else if(action =='add'){
		var url=tourl+"?dhlxdm=PT";
		$.showDialog(url,'新增到货信息',chanceTypeConfig);
	}else if(action =='submit'){
		if (rklxdm == "cghz"&&clbj!='1'){
			tourl = $("#arrivalGoods_formSearch #urlPrefix").val()+"/inspectionGoods/pending/pagedataPurchasePendingMod";
			var url=tourl+"?dhid="+id;
			$.showDialog(url,'采购红字提交',purchaseSubmitConfig);
		}else{
			var url= tourl+"?dhid="+id;
			$.showDialog(url,'提交到货信息',submitArrivalGoodsConfig);
		}

	}else if(action=="mod"){
		if (rklxdm == "cghz"&&clbj!='1'){
			tourl = $("#arrivalGoods_formSearch #urlPrefix").val()+"/inspectionGoods/pending/pagedataPurchasePendingMod";
			var url=tourl+"?dhid="+id;
			$.showDialog(url,'采购红字修改',purchaseModConfig);
		}else{
			var url=tourl+"?dhid="+id;
			$.showDialog(url,'到货信息修改',modArrivalGoodsConfig);
		}

	}else if(action=="viewCommon"){
		var url=tourl+"?dhid="+id;
		$.showDialog(url,'到货详细信息 共通',viewArrivalGoodsConfig);
	}else if(action=="confirm"){
		var url=tourl+"?dhid="+id;
		$.showDialog(url,'确认到货信息 ',confirmArrivalGoodsConfig);
	}else if(action=="borrow"){
		var url=tourl+"?dhid="+id;
		$.showDialog(url,'借用到货信息',borrowArrivalGoodsConfig);
	}else if(action=="returnback"){
		var url=tourl+"?dhid="+id;
		$.showDialog(url,'归还到货信息',returnbackArrivalGoodsConfig);
	}else if(action=="advancedmod"){
		if (rklxdm == "cghz"&&clbj!='1'){
			tourl = $("#arrivalGoods_formSearch #urlPrefix").val()+"/inspectionGoods/pending/pagedataPurchasePendingMod";
			var url=tourl+"?dhid="+id;
			$.showDialog(url,'采购红字修改',purchaseModConfig);
		}else{
			var url=tourl+"?dhid="+id;
			$.showDialog(url,'到货信息高级修改',advancedmodArrivalGoodsConfig);
		}
	}else if(action=="deliver"){
		var url=tourl;
		$.showDialog(url,'销售退货',deliverGoodsConfig);
	}else if(action=="system"){
		var url=tourl+"?dhlxdm=OA";
		$.showDialog(url,'OA新增',chanceTypeConfig);
	}else if(action=="labelprinting"){
		var url=tourl+"?dhid="+id;
		$.showDialog(url,'标签打印',labelprintingConfig);
	}
	
}

var chanceTypeConfig={
		width		: "600px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		modalName	: "chanceTypeModal",
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		} 	
};
var labelprintingConfig = {
	width		: "1500px",
	modalName	: "labelprintingModal",
	formName	: "labelPrinting_formSearch",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#labelPrinting_formSearch").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var sel_row = $('#labelPrinting_formSearch #labelPrinting_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length ==0){
					$.error("请至少选中一行");
					return;
				}
				var hwids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					hwids = hwids + ","+ sel_row[i].hwid;

				}
				hwids = hwids.substr(1);
				$("#labelPrinting_formSearch input[name='access_token']").val($("#ac_tk").val());
				var url=$('#labelPrinting_formSearch #urlPrefix').val()+"/storehouse/arrivalGoods/pagedataLabelPrintingFreight"+"?hwids="+hwids+"&access_token="+$("#ac_tk").val();
				window.open(url);
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
//销售退货
var deliverGoodsConfig = {
		width		: "1600px",
		modalName	: "deliverGoodsModel",
		formName	: "deliverGoodsForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if (!$("#deliverGoodsForm").valid()) {
						$.alert("请填写完整信息");
						return false;
					}
					let json = [];
					if(t_map.rows != null && t_map.rows.length > 0){
						for(let i=0;i<t_map.rows.length;i++){
							// fhmx.fhid,fhmx.fhmxid,wlgl.wlid,wlgl.wlbm,wlgl.wlmc,wlgl.gg,jc_wllb.csmc as wllbmc,
							// jc_wlzlb.csmc as wlzlbmc,fhmx.fhsl,(COALESCE(fhmx.fhsl,0)-COALESCE(fhmx.thsl,0)) as kthsl,
							// 	wlgl.ychh,wlgl.jldw,fhgl.fhdh,fhmx.hwid,ckxx.ckmc,ck_hw.ckmc as hwmc,hwxx.kwbh as kw,hwxx.ckid as ck,fhgl.kh,khgl.khmc,khgl.khjc,
							// 	hwxx.yxq,hwxx.scrq,hwxx.scph,jc_zllb.csmc as zllbmc
							let sz = {"hwid":'',"wlid":'',"bzq":'',"thsl":'',"fhsl":'',"ck":'',"ckdm":'',"hw":'',"bz":'',"glfhmxid":'',"xsdd":'',"ddmxid":'',"wlbm":'',"scph":'',"yxq":'',"wlmc":'',"kl":'',"kl2":'',"wsdj":'',"hsdj":'',"bj":'',"suil":''};
							sz.hwid = t_map.rows[i].hwid;
							sz.wlid = t_map.rows[i].wlid;
							sz.bzq = t_map.rows[i].bzq;
							sz.thsl = t_map.rows[i].thsl;
							sz.fhsl = t_map.rows[i].thsl;
							sz.ck = t_map.rows[i].ck;
							sz.ckdm = t_map.rows[i].ckdm;
							sz.hw = t_map.rows[i].kw;
							sz.bz = t_map.rows[i].dhbz;
							sz.glfhmxid = t_map.rows[i].fhmxid;
							sz.xsdd = t_map.rows[i].xsdd;
							sz.ddmxid = t_map.rows[i].ddmxid;
							sz.wlbm = t_map.rows[i].wlbm;
							sz.scph = t_map.rows[i].scph;
							sz.yxq = t_map.rows[i].yxq;
							sz.wlmc = t_map.rows[i].wlmc;
							sz.kl = t_map.rows[i].kl;
							sz.kl2 = t_map.rows[i].kl2;
							sz.wsdj = t_map.rows[i].wsdj;
							sz.hsdj = t_map.rows[i].hsdj;
							sz.bj = t_map.rows[i].bj;
							sz.suil = t_map.rows[i].suil;
							json.push(sz);
						}
						$("#deliverGoodsForm #thmx_json").val(JSON.stringify(json));
					}else{
						$.alert("退货明细不能为空！");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};

					$("#deliverGoodsForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"deliverGoodsForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								$.closeModal(opts.modalName);
								arrivalGoodsResult(true);
							});
						}else if(responseText["status"] == "fail"){
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

//归还
var returnbackArrivalGoodsConfig = {
		width		: "1400px",
		modalName	: "arrivalGoodsReturnbackModel",
		formName	: "arrivalGoodsReturnbackForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					var $this = this;
					var opts = $this["options"]||{};
					
					var dhmxJson = $("#arrivalGoodsReturnbackForm #dhmxJson").val(JSON.stringify(t_map.rows));
					$("#arrivalGoodsReturnbackForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"arrivalGoodsReturnbackForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								$.closeModal(opts.modalName);
								arrivalGoodsResult(true);
							});
						}else if(responseText["status"] == "fail"){
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

//借用
var borrowArrivalGoodsConfig = {
		width		: "1400px",
		modalName	: "arrivalGoodsBorrowModel",
		formName	: "arrivalGoodsBorrowForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					var $this = this;
					var opts = $this["options"]||{};
					
					var dhmxJson = $("#arrivalGoodsBorrowForm #dhmxJson").val(JSON.stringify(t_map.rows));
					$("#arrivalGoodsBorrowForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"arrivalGoodsBorrowForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								$.closeModal(opts.modalName);
								arrivalGoodsResult(true);
							});
						}else if(responseText["status"] == "fail"){
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

//撤回项目提交
function recallarrivalGoods(dhid,rklxdm,event){
	var auditType= "";
	if (rklxdm == "cghz"){
		auditType = $("#arrivalGoods_formSearch #auditTypeEnum").val();
	}else{
		auditType = $("#arrivalGoods_formSearch #auditType").val();
	}
	var msg = '您确定要撤回该条到货信息吗？';
	var arrivalGoods_params = [];
	arrivalGoods_params.prefix = $("#arrivalGoods_formSearch #urlPrefix").val();
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,dhid,function(){
				arrivalGoodsResult(true);
			},arrivalGoods_params);
		}
	});
}


var advancedmodArrivalGoodsConfig = {
		width		: "1400px",
		modalName	: "modArrivalGoodsModel",
		formName	: "arrivalGoodsEditForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#arrivalGoodsEditForm").valid()){
						return false;
					}
					if(kwlist==null || kwlist.length<=0){
						$.confirm("该仓库下库位信息为空，请先进行维护！");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					if(t_map.rows != null && t_map.rows.length > 0){
						for(var i=0;i<t_map.rows.length;i++){
							t_map.rows[i].dataPermissionModel=null;
							t_map.rows[i].dataFilterModel=null;
							if(parseInt(t_map.rows[i].dhsl*100)/100<parseInt(t_map.rows[i].cythsl*100)/100){
								$.confirm("第"+(i+1)+"行退货数量不能大于到货数量！");
								return false;
							}
							
							if(parseInt(t_map.rows[i].dhsl*100)/100==0){
								$.confirm("到货数量不得小于等于0，若该物料未到货，请删除！");
								return false;
							}
						}
						$("#arrivalGoodsEditForm #dhmxJson").val(JSON.stringify(t_map.rows));
					}else{
						$.confirm("到货信息不能为空！");
						return false;
					}
					$("#arrivalGoodsEditForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"arrivalGoodsEditForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									arrivalGoodsResult(true);
								}
							});
						}else if(responseText["status"] == "fail"){
							// 加载页面提示div
							$.alert(responseText["message"],function() {
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
}

var viewArrivalGoodsConfig = {
	width		: "1600px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	} 
};

var confirmArrivalGoodsConfig = {
		width		: "1600px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					var dhid = $("#ajaxForm #dhid").val();
    				var url= $('#arrivalGoods_formSearch #urlPrefix').val() + "/storehouse/arrivalGoods/confirmSaveArrivalGoods";
    				jQuery.post(url,{dhid:dhid,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								arrivalGoodsResult(true);
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
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		} 
	};
var purchaseSubmitConfig = {
	width: "1600px",
	modalName: "purchaseSubmitModel",
	offAtOnce: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "保 存",
			className : "btn-success",
			callback : function() {
				if (!$("#pendingpurchaseForm").valid()) {
					$.alert("请填写正确信息");
					return false;
				}
				var json = [];
				if(t_map.rows != null && t_map.rows.length > 0){
					for(var i=0;i<t_map.rows.length;i++){
						var sz = {"hwid":'',"chhwid":'',"dhsl":'',"bz":''};
						sz.hwid = t_map.rows[i].hwid;
						sz.chhwid = t_map.rows[i].chhwid;
						sz.dhsl = t_map.rows[i].hcsl;
						sz.bz = t_map.rows[i].bz;
						json.push(sz);
					}
					$("#pendingpurchaseForm #hwxx_json").val(JSON.stringify(json));
				}else{
					$.alert("明细信息不能为空！");
					return false;
				}
				var $this = this;
				var opts = $this["options"] || {};
				$("#pendingpurchaseForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"] || "pendingpurchaseForm", function (responseText, statusText) {
					if (responseText["status"] == 'success') {
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								//新增提交审
								var arrivalGoods_params=[];
								arrivalGoods_params.prefix=$('#pendingpurchaseForm #urlPrefix').val();
								var auditType = responseText["auditType"];
								var ywid = responseText["ywid"];
								showAuditFlowDialog(auditType,ywid,function(){
									$.closeModal(opts.modalName);
									arrivalGoodsResult(true);
								},null,arrivalGoods_params);
								$.closeModal(opts.modalName);
							}
						});
					} else if (responseText["status"] == "fail") {
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"], function () {
						});
					} else {
						preventResubmitForm(".modal-footer > button", false);
						$.alert(responseText["message"], function () {
						});
					}
				}, ".modal-footer > button");
				return false;
			}
		},
		cancel: {
			label: "关 闭",
			className: "btn-default"
		}
	}
}
var purchaseModConfig = {
	width: "1600px",
	modalName: "purchaseModModel",
	offAtOnce: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "保 存",
			className : "btn-success",
			callback : function() {
				if (!$("#pendingpurchaseForm").valid()) {
					$.alert("请填写正确信息");
					return false;
				}
				var json = [];
				if(t_map.rows != null && t_map.rows.length > 0){
					for(var i=0;i<t_map.rows.length;i++){
						var sz = {"hwid":'',"chhwid":'',"dhsl":'',"bz":'',"wlbm":''};
						sz.hwid = t_map.rows[i].hwid;
						sz.chhwid = t_map.rows[i].chhwid;
						sz.dhsl = t_map.rows[i].hcsl;
						sz.bz = t_map.rows[i].bz;
						sz.wlbm = t_map.rows[i].wlbm;
						json.push(sz);
					}
					$("#pendingpurchaseForm #hwxx_json").val(JSON.stringify(json));
				}else{
					$.alert("明细信息不能为空！");
					return false;
				}
				var $this = this;
				var opts = $this["options"] || {};
				$("#pendingpurchaseForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"] || "pendingpurchaseForm", function (responseText, statusText) {
					if (responseText["status"] == 'success') {
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							arrivalGoodsResult(true);
						});
					} else if (responseText["status"] == "fail") {
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"], function () {
						});
					} else {
						preventResubmitForm(".modal-footer > button", false);
						$.alert(responseText["message"], function () {
						});
					}
				}, ".modal-footer > button");
				return false;
			}
		},
		cancel: {
			label: "关 闭",
			className: "btn-default"
		}
	}
}

var modArrivalGoodsConfig = {
		width		: "1400px",
		modalName	: "modArrivalGoodsModel",
		formName	: "arrivalGoodsEditForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#arrivalGoodsEditForm").valid()){
						return false;
					}
					if((kwlist==null || kwlist.length<=0)&&cskz3!='CGHZ'){
						$.confirm("该仓库下库位信息为空，请先进行维护！");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#arrivalGoodsEditForm #dhmxJson").val("");
					if(rklbbj != "1" && cskz3 != "1"  && gjxgbj != '1'){
						if(t_map.rows != null && t_map.rows.length > 0){
							for(var i=0;i<t_map.rows.length;i++){
								t_map.rows[i].dataPermissionModel=null;
								t_map.rows[i].dataFilterModel=null;
								if(parseInt(t_map.rows[i].dhsl*100)/100<parseInt(t_map.rows[i].cythsl*100)/100&&cskz3!='CGHZ'){
									$.confirm("第"+(i+1)+"行退货数量不能大于到货数量！");
									return false;
								}

								// if(parseInt(t_map.rows[i].dhsl*100)/100==0){
								// 	$.confirm("到货数量不得小于等于0，若该物料未到货，请删除！");
								// 	return false;
								// }
							}
							// 初始化htmxJson
		            		var json = [];
							for (var i = 0; i < t_map.rows.length; i++) {
		            			var sz = {"yhwkcl":'',"yhwbhgsl":'',"ckid":'',"chhwid":'',"ckdh":'',"rkid":'',"hwid":'',"htmxid":'',"qgmxid":'',"wlid":'',"yxq":'',"dhsl":'',"dhbz":'',"kwbh":'',"scrq":'',"zsh":'',"scph":'',"cskw":'',"cythsl":'',"htid":'',"htsl":'',"wlbm":'',"cpzch":''};
								sz.yhwkcl = t_map.rows[i].yhwkcl;
								sz.yhwbhgsl = t_map.rows[i].yhwbhgsl;
								sz.ckid = t_map.rows[i].ckid;
		            			sz.chhwid = t_map.rows[i].chhwid;
		            			sz.ckdh = t_map.rows[i].ckdh;
		            			sz.rkid = t_map.rows[i].rkid;
		            			sz.hwid = t_map.rows[i].hwid;
		            			sz.htmxid = t_map.rows[i].htmxid;
								sz.qgmxid = t_map.rows[i].qgmxid;
								sz.wlid = t_map.rows[i].wlid;
								sz.yxq = t_map.rows[i].yxq;
								sz.dhsl = t_map.rows[i].dhsl;
								sz.dhbz = t_map.rows[i].dhbz;
								sz.kwbh = t_map.rows[i].kwbh;
								sz.scrq = t_map.rows[i].scrq;
								sz.zsh = t_map.rows[i].zsh;
								sz.scph = t_map.rows[i].scph;
								sz.cskw = t_map.rows[i].cskw;						
								sz.cythsl = t_map.rows[i].cythsl;
								sz.htid = t_map.rows[i].htid;
								sz.htsl = t_map.rows[i].htsl;					
								sz.wlbm = t_map.rows[i].wlbm;
								sz.cpzch = t_map.rows[i].cpzch;
								json.push(sz);
								if (cskz3=='CGHZ'){
									$("#arrivalGoodsEditForm #ckid").val(sz.ckid);
								}
		    				}
		            		$("#arrivalGoodsEditForm #dhmxJson").val(JSON.stringify(json));
						}else{
							$.confirm("到货信息不能为空！");
							return false;
						}
						dhmx_json = $("#arrivalGoodsEditForm #dhmx_json").val();
						//去除无关json
						$("#arrivalGoodsEditForm #dhmx_json").val("");
					}else if(cskz3 == "1"  && gjxgbj != '1'){
						if(t_map.rows != null && t_map.rows.length > 0){
							for(var i=0;i<t_map.rows.length;i++){
								t_map.rows[i].dataPermissionModel=null;
								t_map.rows[i].dataFilterModel=null;

								if(t_map.rows[i].dhsl==null || t_map.rows[i].dhsl==''){
									$.confirm("请填写到货数量！");
									return false;
								}
								// if(parseInt(t_map.rows[i].dhsl*100)/100==0){
								// 	$.confirm("到货数量不得小于等于0!");
								// 	return false;
								// }
							}
							var json = [];
							for (var i = 0; i < t_map.rows.length; i++) {
								var sz = {"sczlmxid":'',"qgmxid":'',"wlid":'',"yxq":'',"dhsl":'',"dhbz":'',"kwbh":'',"scrq":'',"zsh":'',"scph":'',"cskw":'',"cythsl":'',"htid":'',"htsl":'',"wlbm":''};
								sz.sczlmxid = t_map.rows[i].sczlmxid;
								sz.qgmxid = t_map.rows[i].qgmxid;
								sz.wlid = t_map.rows[i].wlid;
								sz.yxq = t_map.rows[i].yxq;
								sz.dhsl = t_map.rows[i].dhsl;
								sz.dhbz = t_map.rows[i].dhbz;
								sz.kwbh = t_map.rows[i].kwbh;
								sz.scrq = t_map.rows[i].scrq;
								sz.zsh = t_map.rows[i].zsh;
								sz.scph = t_map.rows[i].scph;
								sz.cskw = t_map.rows[i].cskw;
								sz.cythsl = t_map.rows[i].cythsl;
								sz.wlbm = t_map.rows[i].wlbm;
								json.push(sz);
							}
							$("#arrivalGoodsEditForm #dhmxJson").val(JSON.stringify(json));
							$("#arrivalGoodsEditForm #hwxx_json").val(JSON.stringify(json));
						}else{
							$.confirm("到货信息不能为空！");
							return false;
						}
					}else{
						if(t_map.rows != null && t_map.rows.length > 0){
							var hwJson = [];
							for (var i = 0; i < t_map.rows.length; i++) {
								var sz={"wlid":"","zsh":"","scph":"","scrq":"","yxq":"","cythsl":"","dhsl":"","cskw":"","kwbh":"","dhbz":"","hwid":"","wlbm":"","cpzch":"","sbysid":""};
								sz.wlid = t_map.rows[i].wlid;		
								if(t_map.rows[i].zsh!="" && t_map.rows[i].zsh!=null){
									sz.zsh = t_map.rows[i].zsh;
								}else{
									$.alert("追溯号不能为空！");
									return false;
								}
								if(t_map.rows[i].scph!="" && t_map.rows[i].scph!=null){
									sz.scph = t_map.rows[i].scph;
								}else{
									$.alert("生产批号不能为空！");
									return false;
								}
								if(t_map.rows[i].scrq!="" && t_map.rows[i].scrq!=null){
									sz.scrq = t_map.rows[i].scrq;
								}else{
									$.alert("生产日期不能为空！");
									return false;
								}
								if(t_map.rows[i].yxq!="" && t_map.rows[i].yxq!=null){
									sz.yxq = t_map.rows[i].yxq;
								}else{
									$.alert("失效日期不能为空！");
									return false;
								}
								sz.cythsl = t_map.rows[i].cythsl;
								if(t_map.rows[i].dhsl!="" && t_map.rows[i].dhsl!=null){
									sz.dhsl = t_map.rows[i].dhsl;
								}else{
									$.alert("到货数量不能为空！");
									return false;
								}
								if(t_map.rows[i].cskw!="" && t_map.rows[i].cskw!=null){
									sz.cskw = t_map.rows[i].cskw;
									sz.kwbh = t_map.rows[i].kwbh;
									sz.dhbz = t_map.rows[i].dhbz;
								}else{
									$.alert("库位不能为空！");
									return false;
								}
								sz.wlbm = t_map.rows[i].wlbm;
								sz.cpzch = t_map.rows[i].cpzch;
								sz.sbysid = t_map.rows[i].sbysid;
								hwJson.push(sz);
							}
							$("#arrivalGoodsEditForm #hwxx_json").val(JSON.stringify(hwJson));
						}else{
							$.confirm("物料不能为空！");
							return false;
						}
					}
					$("#arrivalGoodsEditForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"arrivalGoodsEditForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									arrivalGoodsResult(true);
								}
							});
						}else if(responseText["status"] == "fail"){
							$("#arrivalGoodsEditForm #dhmx_json").val(dhmx_json);
							preventResubmitForm(".modal-footer > button", false);
							// 加载页面提示div
							$.alert(responseText["message"],function() {
							});
						} else{
							$("#arrivalGoodsEditForm #dhmx_json").val(dhmx_json);
							preventResubmitForm(".modal-footer > button", false);
							// 加载页面提示div
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
}

var submitArrivalGoodsConfig = {
		width		: "1400px",
		modalName	: "submitArrivalGoodsModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "提交",
				className : "btn-primary",
				callback : function() {
					if(!$("#arrivalGoodsEditForm").valid()){
						return false;
					}
					if((kwlist==null || kwlist.length<=0)&&cskz3!='CGHZ'){
						$.confirm("该仓库下库位信息为空，请先进行维护！");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#arrivalGoodsEditForm input[name='access_token']").val($("#ac_tk").val());
					
					$("#arrivalGoodsEditForm #dhmxJson").val("");
					if(rklbbj != "1"  && cskz3 != "1"  && gjxgbj != '1'){
						if(t_map.rows != null && t_map.rows.length > 0){
							for(var i=0;i<t_map.rows.length;i++){
								t_map.rows[i].dataPermissionModel=null;
								t_map.rows[i].dataFilterModel=null;
								if(parseInt(t_map.rows[i].dhsl*100)/100<parseInt(t_map.rows[i].cythsl*100)/100&&cskz3!='CGHZ'){
									$.confirm("第"+(i+1)+"行退货数量不能大于到货数量！");
									return false;
								}
								
								// if(parseInt(t_map.rows[i].dhsl*100)/100==0){
								// 	$.confirm("到货数量不得小于等于0，若该物料为到货，请删除！");
								// 	return false;
								// }
							}
							// 初始化htmxJson
		            		var json = [];
							for (var i = 0; i < t_map.rows.length; i++) {
		            			var sz = {"yhwkcl":'',"yhwbhgsl":'',"ckid":'',"chhwid":'',"ckdh":'',"rkid":'',"hwid":'',"htmxid":'',"qgmxid":'',"wlid":'',"yxq":'',"dhsl":'',"dhbz":'',"kwbh":'',"scrq":'',"zsh":'',"scph":'',"cskw":'',"cythsl":'',"htid":'',"htsl":'',"wlbm":'',"cpzch":''};
								sz.yhwkcl = t_map.rows[i].yhwkcl;
								sz.yhwbhgsl = t_map.rows[i].yhwbhgsl;
								sz.ckid = t_map.rows[i].ckid;
		            			sz.chhwid = t_map.rows[i].chhwid;
		            			sz.ckdh = t_map.rows[i].ckdh;
		            			sz.rkid = t_map.rows[i].rkid;
		            			sz.hwid = t_map.rows[i].hwid;
		            			sz.htmxid = t_map.rows[i].htmxid;
								sz.qgmxid = t_map.rows[i].qgmxid;
								sz.wlid = t_map.rows[i].wlid;
								sz.yxq = t_map.rows[i].yxq;
								sz.dhsl = t_map.rows[i].dhsl;
								sz.dhbz = t_map.rows[i].dhbz;
								sz.kwbh = t_map.rows[i].kwbh;
								sz.scrq = t_map.rows[i].scrq;
								sz.zsh = t_map.rows[i].zsh;
								sz.scph = t_map.rows[i].scph;
								sz.cskw = t_map.rows[i].cskw;						
								sz.cythsl = t_map.rows[i].cythsl;
								sz.htid = t_map.rows[i].htid;
								sz.htsl = t_map.rows[i].htsl;					
								sz.wlbm = t_map.rows[i].wlbm;
								sz.cpzch = t_map.rows[i].cpzch;
								json.push(sz);
								if (cskz3=='CGHZ'){
									$("#arrivalGoodsEditForm #ckid").val(sz.ckid);
								}
		    				}
		            		$("#arrivalGoodsEditForm #dhmxJson").val(JSON.stringify(json));
						}else{
							$.confirm("到货信息不能为空！");
							return false;
						}
						dhmx_json = $("#arrivalGoodsEditForm #dhmx_json").val();
						//去除无关json
						$("#arrivalGoodsEditForm #dhmx_json").val("");
					}else if(cskz3 == "1"  && gjxgbj != '1'){
						if(t_map.rows != null && t_map.rows.length > 0){
							for(var i=0;i<t_map.rows.length;i++){
								t_map.rows[i].dataPermissionModel=null;
								t_map.rows[i].dataFilterModel=null;

								if(t_map.rows[i].dhsl==null || t_map.rows[i].dhsl==''){
									$.confirm("请填写到货数量！");
									return false;
								}
								// if(parseInt(t_map.rows[i].dhsl*100)/100==0){
								// 	$.confirm("到货数量不得小于等于0!");
								// 	return false;
								// }
							}
							var json = [];
							for (var i = 0; i < t_map.rows.length; i++) {
								var sz = {"sczlmxid":'',"qgmxid":'',"wlid":'',"yxq":'',"dhsl":'',"dhbz":'',"kwbh":'',"scrq":'',"zsh":'',"scph":'',"cskw":'',"cythsl":'',"htid":'',"htsl":'',"wlbm":''};
								sz.sczlmxid = t_map.rows[i].sczlmxid;
								sz.qgmxid = t_map.rows[i].qgmxid;
								sz.wlid = t_map.rows[i].wlid;
								sz.yxq = t_map.rows[i].yxq;
								sz.dhsl = t_map.rows[i].dhsl;
								sz.dhbz = t_map.rows[i].dhbz;
								sz.kwbh = t_map.rows[i].kwbh;
								sz.scrq = t_map.rows[i].scrq;
								sz.zsh = t_map.rows[i].zsh;
								sz.scph = t_map.rows[i].scph;
								sz.cskw = t_map.rows[i].cskw;
								sz.cythsl = t_map.rows[i].cythsl;
								sz.wlbm = t_map.rows[i].wlbm;
								json.push(sz);
							}
							$("#arrivalGoodsEditForm #dhmxJson").val(JSON.stringify(json));
							$("#arrivalGoodsEditForm #hwxx_json").val(JSON.stringify(json));
						}else{
							$.confirm("到货信息不能为空！");
							return false;
						}
					}else{
						if(t_map.rows != null && t_map.rows.length > 0){
							var hwJson = [];
							for (var i = 0; i < t_map.rows.length; i++) {
								var sz={"wlid":"","zsh":"","scph":"","scrq":"","yxq":"","cythsl":"","dhsl":"","cskw":"","kwbh":"","dhbz":"","wlbm":"","cpzch":"","sbysid":""};
								sz.wlid = t_map.rows[i].wlid;		
								if(t_map.rows[i].zsh!="" && t_map.rows[i].zsh!=null){
									sz.zsh = t_map.rows[i].zsh;
								}else{
									$.alert("追溯号不能为空！");
									return false;
								}
								if(t_map.rows[i].scph!="" && t_map.rows[i].scph!=null){
									sz.scph = t_map.rows[i].scph;
								}else{
									$.alert("生产批号不能为空！");
									return false;
								}
								if(t_map.rows[i].scrq!="" && t_map.rows[i].scrq!=null){
									sz.scrq = t_map.rows[i].scrq;
								}else{
									$.alert("生产日期不能为空！");
									return false;
								}
								if(t_map.rows[i].yxq!="" && t_map.rows[i].yxq!=null){
									sz.yxq = t_map.rows[i].yxq;
								}else{
									$.alert("失效日期不能为空！");
									return false;
								}
								sz.cythsl = t_map.rows[i].cythsl;
								if(t_map.rows[i].dhsl!="" && t_map.rows[i].dhsl!=null){
									sz.dhsl = t_map.rows[i].dhsl;
								}else{
									$.alert("到货数量不能为空！");
									return false;
								}
								if(t_map.rows[i].cskw!="" && t_map.rows[i].cskw!=null){
									sz.cskw = t_map.rows[i].cskw;
									sz.kwbh = t_map.rows[i].kwbh;
									sz.dhbz = t_map.rows[i].dhbz;
								}else{
									$.alert("库位不能为空！");
									return false;
								}
								sz.wlbm = t_map.rows[i].wlbm;
								sz.cpzch = t_map.rows[i].cpzch;
								sz.sbysid = t_map.rows[i].sbysid;
								hwJson.push(sz);
							}
							$("#arrivalGoodsEditForm #hwxx_json").val(JSON.stringify(hwJson));
						}else{
							$.confirm("物料不能为空！");
							return false;
						}
					}


					submitForm(opts["formName"]||"arrivalGoodsEditForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							if(opts.offAtOnce){
								//新增提交审
								var arrivalGoods_params=[];
								arrivalGoods_params.prefix=$('#arrivalGoodsEditForm #urlPrefix').val();
								var auditType = $("#arrivalGoodsEditForm #auditType").val();
								var ywid = $("#arrivalGoodsEditForm #dhid").val();
								showAuditFlowDialog(auditType,ywid,function(){
									$.closeModal(opts.modalName);
									arrivalGoodsResult(true);
								},null,arrivalGoods_params);
								$.closeModal(opts.modalName);
							}
						}else if(responseText["status"] == "fail"){
							$("#arrivalGoodsEditForm #dhmx_json").val(dhmx_json);
							$.error(responseText["message"],function() {
								preventResubmitForm(".modal-footer > button", false);
							});
						} else{
							$("#arrivalGoodsEditForm #dhmx_json").val(dhmx_json);
							preventResubmitForm(".modal-footer > button", false);
							// 加载页面提示div
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
}

var addArrivalGoodsConfig = {
		width		: "1800px",
		modalName	: "addArrivalGoodsModel",
		formName	: "arrivalGoodsEditForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#arrivalGoodsEditForm").valid()){
						return false;
					}
					if((kwlist==null || kwlist.length<=0)&&cskz3!='CGHZ'){
						$.confirm("该仓库下库位信息为空，请先进行维护！");
						return false;
					}
					$("#arrivalGoodsEditForm #dhmxJson").val("");
					if(rklbbj != "1" && cskz3 !='1' && gjxgbj != '1'){
						if(t_map.rows != null && t_map.rows.length > 0){
							for(var i=0;i<t_map.rows.length;i++){
								t_map.rows[i].dataPermissionModel=null;
								t_map.rows[i].dataFilterModel=null;
								if(parseInt(t_map.rows[i].dhsl*100)/100<parseInt(t_map.rows[i].cythsl*100)/100&&cskz3!='CGHZ'){
									$.confirm(t_map.rows[i].wlbm +"退货数量不能大于到货数量！");
									return false;
								}
							}
							
							// 初始化htmxJson
		            		var json = [];
							for (var i = 0; i < t_map.rows.length; i++) {
		            			var sz = {"yhwkcl":'',"yhwbhgsl":'',"ckid":'',"ckdh":'',"rkid":'',"hwid":'',"htmxid":'',"qgmxid":'',"wlid":'',"yxq":'',"dhsl":'',"dhbz":'',"kwbh":'',"scrq":'',"zsh":'',"scph":'',"cskw":'',"cythsl":'',"htid":'',"htsl":'',"htbz":'',"wlbm":'',"cpzch":''};
								sz.yhwkcl = t_map.rows[i].yhwkcl;
								sz.yhwbhgsl = t_map.rows[i].yhwbhgsl;
								sz.ckid = t_map.rows[i].ckid;
								sz.ckdh = t_map.rows[i].ckdh;
								sz.rkid = t_map.rows[i].rkid;
								sz.hwid = t_map.rows[i].hwid;
		            			sz.htmxid = t_map.rows[i].htmxid;
								sz.qgmxid = t_map.rows[i].qgmxid;
								sz.wlid = t_map.rows[i].wlid;
								sz.yxq = t_map.rows[i].yxq;
								sz.dhsl = t_map.rows[i].dhsl;
								sz.dhbz = t_map.rows[i].dhbz;
								sz.kwbh = t_map.rows[i].kwbh;
								sz.scrq = t_map.rows[i].scrq;
								sz.zsh = t_map.rows[i].zsh;
								sz.scph = t_map.rows[i].scph;
								sz.cskw = t_map.rows[i].cskw;						
								sz.cythsl = t_map.rows[i].cythsl;
								sz.htid = t_map.rows[i].htid;
								sz.htsl = t_map.rows[i].htsl;
								sz.htbz = t_map.rows[i].htbz;
								sz.wlbm = t_map.rows[i].wlbm;
								sz.cpzch = t_map.rows[i].cpzch;
								json.push(sz);
								if (cskz3=='CGHZ'){
									$("#arrivalGoodsEditForm #ckid").val(sz.ckid);
								}
		    				}
		            		$("#arrivalGoodsEditForm #dhmxJson").val(JSON.stringify(json));
						}else{
							$.confirm("到货信息不能为空！");
							return false;
						}
						dhmx_json = $("#arrivalGoodsEditForm #dhmx_json").val();
						//去除无关json
						$("#arrivalGoodsEditForm #dhmx_json").val("");
					}else if(cskz3 == "1"  && gjxgbj != '1'){
						if(t_map.rows != null && t_map.rows.length > 0){
							for(var i=0;i<t_map.rows.length;i++){
								t_map.rows[i].dataPermissionModel=null;
								t_map.rows[i].dataFilterModel=null;
								// if(parseInt(t_map.rows[i].dhsl*100)/100==0){
								// 	$.confirm("到货数量不得小于等于0!");
								// 	return false;
								// }
							}
							var json = [];
							for (var i = 0; i < t_map.rows.length; i++) {
								var sz = {"sczlmxid":'',"qgmxid":'',"wlid":'',"yxq":'',"dhsl":'',"dhbz":'',"kwbh":'',"scrq":'',"zsh":'',"scph":'',"cskw":'',"cythsl":'',"htid":'',"htsl":'',"wlbm":''};
								sz.sczlmxid = t_map.rows[i].sczlmxid;
								sz.qgmxid = t_map.rows[i].qgmxid;
								sz.wlid = t_map.rows[i].wlid;
								sz.yxq = t_map.rows[i].yxq;
								sz.dhsl = t_map.rows[i].dhsl;
								sz.dhbz = t_map.rows[i].dhbz;
								sz.kwbh = t_map.rows[i].kwbh;
								sz.scrq = t_map.rows[i].scrq;
								sz.zsh = t_map.rows[i].zsh;
								sz.scph = t_map.rows[i].scph;
								sz.cskw = t_map.rows[i].cskw;
								sz.cythsl = t_map.rows[i].cythsl;
								sz.wlbm = t_map.rows[i].wlbm;
								json.push(sz);
							}
							$("#arrivalGoodsEditForm #dhmxJson").val(JSON.stringify(json));
							$("#arrivalGoodsEditForm #hwxx_json").val(JSON.stringify(json));
						}else{
							$.confirm("到货信息不能为空！");
							return false;
						}
					}else{
						if(t_map.rows != null && t_map.rows.length > 0){
							var hwJson = [];
							for (var i = 0; i < t_map.rows.length; i++) {
								var sz={"wlid":"","zsh":"","scph":"","scrq":"","yxq":"","cythsl":"","dhsl":"","cskw":"","kwbh":"","dhbz":"","htbz":'',"wlbm":'',"cpzch":'',"sbysid":''};
								sz.wlid = t_map.rows[i].wlid;		
								sz.htbz = t_map.rows[i].htbz;
								if(t_map.rows[i].zsh!="" && t_map.rows[i].zsh!=null){
									sz.zsh = t_map.rows[i].zsh;
								}else{
									$.alert("追溯号不能为空！");
									return false;
								}
								if(t_map.rows[i].scph!="" && t_map.rows[i].scph!=null){
									sz.scph = t_map.rows[i].scph;
								}else{
									$.alert("生产批号不能为空！");
									return false;
								}
								if(t_map.rows[i].scrq!="" && t_map.rows[i].scrq!=null){
									sz.scrq = t_map.rows[i].scrq;
								}else{
									$.alert("生产日期不能为空！");
									return false;
								}
								if(t_map.rows[i].yxq!="" && t_map.rows[i].yxq!=null){
									sz.yxq = t_map.rows[i].yxq;
								}else{
									$.alert("失效日期不能为空！");
									return false;
								}
								sz.cythsl = t_map.rows[i].cythsl;
								if(t_map.rows[i].dhsl!="" && t_map.rows[i].dhsl!=null){
									sz.dhsl = t_map.rows[i].dhsl;
								}else{
									$.alert("到货数量不能为空！");
									return false;
								}
								if(t_map.rows[i].cskw!="" && t_map.rows[i].cskw!=null){
									sz.cskw = t_map.rows[i].cskw;
									sz.kwbh = t_map.rows[i].kwbh;
									sz.dhbz = t_map.rows[i].dhbz;
								}else{
									$.alert("库位不能为空！");
									return false;
								}
								sz.wlbm = t_map.rows[i].wlbm;
								sz.cpzch = t_map.rows[i].cpzch;
								sz.sbysid = t_map.rows[i].sbysid;
								hwJson.push(sz);
							}
							$("#arrivalGoodsEditForm #hwxx_json").val(JSON.stringify(hwJson));
						}else{
							$.confirm("物料不能为空！");
							return false;
						}
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#arrivalGoodsEditForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"arrivalGoodsEditForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							var auditType = $("#arrivalGoodsEditForm #auditType").val();
							var arrivalGoodsEdit_params=[];
							arrivalGoodsEdit_params.prefix=$('#arrivalGoodsEditForm #urlPrefix').val();
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								//提交审核
								showAuditFlowDialog(auditType,responseText["ywid"],function(){
									arrivalGoodsResult(true);
								},null,arrivalGoodsEdit_params);
							});
						}else if(responseText["status"] == "fail"){
							$("#arrivalGoodsEditForm #dhmx_json").val(dhmx_json);
							preventResubmitForm(".modal-footer > button", false);
							// 加载页面提示div
							$.alert(responseText["message"],function() {
							});
						} else{
							$("#arrivalGoodsEditForm #dhmx_json").val(dhmx_json);
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
}

var arrivalGoods_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_view = $("#arrivalGoods_formSearch #btn_view");
		var btn_query = $("#arrivalGoods_formSearch #btn_query");
		var btn_del = $("#arrivalGoods_formSearch #btn_del");
		var btn_add = $("#arrivalGoods_formSearch #btn_add");
		var btn_mod = $("#arrivalGoods_formSearch #btn_mod");
		var btn_submit = $("#arrivalGoods_formSearch #btn_submit");
		var btn_searchexport = $("#arrivalGoods_formSearch #btn_searchexport");
    	var btn_selectexport = $("#arrivalGoods_formSearch #btn_selectexport");
    	var btn_print = $("#arrivalGoods_formSearch #btn_print");
		// var btn_hwprint = $("#arrivalGoods_formSearch #btn_hwprint");
    	var btn_ysdprint = $("#arrivalGoods_formSearch #btn_ysdprint");
    	var btn_viewCommon = $("#arrivalGoods_formSearch #btn_viewCommon");
    	var btn_confirm = $("#arrivalGoods_formSearch #btn_confirm");//确认
    	var btn_borrow = $("#arrivalGoods_formSearch #btn_borrow");//借用/storehouse/arrivalGoods/borrowArrivalGoods
    	var btn_returnback = $("#arrivalGoods_formSearch #btn_returnback");
    	var btn_discard = $("#arrivalGoods_formSearch #btn_discard");//到货废弃
    	var btn_advancedmod = $("#arrivalGoods_formSearch #btn_advancedmod");
    	var btn_deliver = $("#arrivalGoods_formSearch #btn_deliver"); //销售退货
    	var btn_system = $("#arrivalGoods_formSearch #btn_system"); //OA新增
		var btn_labelprinting = $("#arrivalGoods_formSearch #btn_labelprinting"); //OA新增
		//添加日期控件
    	laydate.render({
    	   elem: '#dhsjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#dhsjend'
    	  ,theme: '#2381E9'
    	});
    	
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			arrivalGoodsResult(true);
    		});
    	}
		  
		/* --------------------------- 退货 -----------------------------------*/
		btn_deliver.unbind("click").click(function(){
    		arrivalGoodsById(null, "deliver", btn_deliver.attr("tourl"));
    	});
		  
		/* --------------------------- 新增到货信息 -----------------------------------*/
    	btn_add.unbind("click").click(function(){
    		arrivalGoodsById(null, "add", btn_add.attr("tourl"));
    	});
    	/* --------------------------- OA新增 -----------------------------------*/
    	btn_system.unbind("click").click(function(){
    		arrivalGoodsById(null, "system", btn_system.attr("tourl"));
    	});
    	/* --------------------------- 修改到货信息 -----------------------------------*/
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#arrivalGoods_formSearch #arrivalGoods_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length == 1){
    			if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
					arrivalGoodsById(sel_row[0].dhid, "mod", btn_mod.attr("tourl"), sel_row[0].rklxdm, sel_row[0].clbj);
    			}else{
    				$.alert("该记录在审核中或已审核，不允许修改!");
    			}
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/* --------------------------- 修改到货信息 -----------------------------------*/
    	btn_advancedmod.unbind("click").click(function(){
    		var sel_row = $('#arrivalGoods_formSearch #arrivalGoods_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length == 1){
    			arrivalGoodsById(sel_row[0].dhid, "advancedmod", btn_advancedmod.attr("tourl"), sel_row[0].rklxdm, sel_row[0].clbj);
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/* ---------------------------提交-----------------------------------*/
    	btn_submit.unbind("click").click(function(){
    		var sel_row = $('#arrivalGoods_formSearch #arrivalGoods_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			if(sel_row[0].zt !='00' && sel_row[0].zt !='15' && sel_row[0].zt !=null){
    				$.error("您好！该条记录已提交或者已通过，不允许重复提交！");
    				return false;
    			}
    			arrivalGoodsById(sel_row[0].dhid,"submit",btn_submit.attr("tourl"), sel_row[0].rklxdm, sel_row[0].clbj);
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/* ---------------------------确认-----------------------------------*/
    	btn_confirm.unbind("click").click(function(){
    		var sel_row = $('#arrivalGoods_formSearch #arrivalGoods_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			arrivalGoodsById(sel_row[0].dhid,"confirm",btn_confirm.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
       /* ---------------------------查看列表-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#arrivalGoods_formSearch #arrivalGoods_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			arrivalGoodsById(sel_row[0].dhid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/* ------------------------------废弃到货信息-----------------------------*/
		btn_discard.unbind("click").click(function(){
			var sel_row = $('#arrivalGoods_formSearch #arrivalGoods_list').bootstrapTable('getSelections');//获取选择行数据
			let pos = 0;
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
				if(sel_row[i].zt !='00' && sel_row[i].zt !='15'){
					$.error("该记录在审核中或已审核，不允许废弃!");
					return;
				}
				if (sel_row[i].rklxdm == "cghz"){
					pos++;
				}
			}
			if (pos!=sel_row.length && pos!=0){
				$.error("不允许红字和其它类型同时废弃！");
				return;
			}
			if(sel_row.length==0){
				$.error("请至少选中一行");
				return;
			}else{
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids= ids + ","+ sel_row[i].dhid;
				}
				ids=ids.substr(1);
				let tourl = btn_del.attr("tourl");
				$.confirm('您确定要废弃所选择的信息吗？',function(result){
					if(result){
						jQuery.ajaxSetup({async:false});
						var url=$('#arrivalGoods_formSearch #arrivalGoods_list').val() + tourl;
						jQuery.post(url,{ids:ids,"rklbdm":sel_row[0].rklxdm,"access_token":$("#ac_tk").val()},function(responseText){
							setTimeout(function(){
								if(responseText["status"] == 'success'){
									$.success(responseText["message"],function() {
										arrivalGoodsResult(true);
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
    	
    	 //---------------------------删除----------------------------------
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#arrivalGoods_formSearch #arrivalGoods_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
			let pos = 0;
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].dhid;
				if (sel_row[i].rklxdm == "cghz"){
					pos++;
				}
    		}
			if (pos!=sel_row.length && pos!=0){
				$.error("不允许红字和其它类型同时删除！");
				return;
			}
    		ids = ids.substr(1);
			let tourl = btn_del.attr("tourl");
    		$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= $('#arrivalGoods_formSearch #urlPrefix').val() + tourl;
    				jQuery.post(url,{ids:ids,"rklbdm":sel_row[0].rklxdm,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								arrivalGoodsResult(true);
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
    	
    	//---------------------------导出--------------------------------------------------
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#arrivalGoods_formSearch #arrivalGoods_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].dhid;
        		}
        		ids = ids.substr(1);
        		$.showDialog($('#arrivalGoods_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=ARRIVALGOODS_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});

    	btn_searchexport.unbind("click").click(function(){
    		$.showDialog($('#arrivalGoods_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=ARRIVALGOODS_SEARCH&expType=search&callbackJs=ArrGoodsSearchData"
    				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});	
 
    	/* --------------------------- 打印请检单 -----------------------------------*/
    	btn_print.unbind("click").click(function(){
    		var sel_row = $('#arrivalGoods_formSearch #arrivalGoods_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].dhid;
				}
				ids = ids.substr(1);
				var url=$('#arrivalGoods_formSearch #urlPrefix').val()+btn_print.attr("tourl")+"?dhids="+ids.toString()+"&access_token="+$("#ac_tk").val();
				window.open(url);
			}else{
				$.error("请选中一行");
			}
    	});

		/* --------------------------- 标签打印 -----------------------------------*/
		btn_labelprinting.unbind("click").click(function(){
			var sel_row = $('#arrivalGoods_formSearch #arrivalGoods_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length !=1){
				$.error("请选中一行");
				return;
			}
			arrivalGoodsById(sel_row[0].dhid, "labelprinting", btn_labelprinting.attr("tourl"), "");
		});
		/* --------------------------- 打印货位卡 -----------------------------------*/
		// btn_hwprint.unbind("click").click(function(){
		// 	var sel_row = $('#arrivalGoods_formSearch #arrivalGoods_list').bootstrapTable('getSelections');//获取选择行数据
		// 	if(sel_row.length ==0){
		// 		$.error("请至少选中一行");
		// 		return;
		// 	}
		// 	var ids="";
		// 	for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
		// 		ids = ids + ","+ sel_row[i].dhid;
		// 	}
		// 	ids = ids.substr(1);
		// 	var url=$('#arrivalGoods_formSearch #urlPrefix').val()+btn_hwprint.attr("tourl")+"?dhids="+ids.toString()+"&access_token="+$("#ac_tk").val();
		// 	window.open(url);
		// 	// $.ajax({
		// 	// 	type : 'get',
		// 	// 	url : url,
		// 	// 	success:function(map){
		// 	// 		if ( map.hwlist.length>0 && map.hwlist!=null){
		// 	// 			$.each(  map.hwlist,function (index,value) {
		// 	// 				let url1 = $('#arrivalGoods_formSearch #urlPrefix').val()+"/storehouse/arrivalGoods/freightPrintOpen"+"?ysdh="+value.ysdh+
		// 	// 					"&wlbm="+value.wlbm+"&wlmc="+value.wlmc+"&zsh="+value.zsh+"&jldw="+value.jldw+"&gg="+value.gg+"&yxq="+value.yxq+
		// 	// 					"&gysmc="+value.gysmc+"&scph="+value.scph+"&dhrq="+value.dhrq+"&dhdh="+value.dhdh+"&lrry="+value.lrry+"&dhsl="+value.dhsl+
		// 	// 					"&cskwmc="+value.cskwmc+"&access_token="+$("#ac_tk").val();
		// 	// 				window.open(url1);
		// 	// 			})
		// 	// 		}
		// 	// 	}
		// 	// });
		// });
		/* --------------------------- 打印到货验收单 -----------------------------------*/
		btn_ysdprint.unbind("click").click(function(){
			var sel_row = $('#arrivalGoods_formSearch #arrivalGoods_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length ==0){
				$.error("请至少选中一行");
				return;
			}
			var ids="";
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
				ids = ids + ","+ sel_row[i].dhid;
			}
			ids = ids.substr(1);
			var url=$('#arrivalGoods_formSearch #urlPrefix').val()+btn_ysdprint.attr("tourl")+"?dhids="+ids.toString()+"&access_token="+$("#ac_tk").val();
			window.open(url);
		});
        /* ---------------------------共通 查看列表-----------------------------------*/
    	btn_viewCommon.unbind("click").click(function(){
    		var sel_row = $('#arrivalGoods_formSearch #arrivalGoods_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			arrivalGoodsById(sel_row[0].dhid,"viewCommon",btn_viewCommon.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/* --------------------------- 借用 -----------------------------------*/
    	btn_borrow.unbind("click").click(function(){
    		var sel_row = $('#arrivalGoods_formSearch #arrivalGoods_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			if(sel_row[0].zt != '80'){
    				$.error("您好！该条记录还未通过审核，不允许修改！");
    				return false;
    			}
    			arrivalGoodsById(sel_row[0].dhid,"borrow",btn_borrow.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/* --------------------------- 归还 -----------------------------------*/
    	btn_returnback.unbind("click").click(function(){
    		var sel_row = $('#arrivalGoods_formSearch #arrivalGoods_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			if(sel_row[0].zt != '80'){
    				$.error("您好！该条记录还未通过审核，不允许修改！");
    				return false;
    			}
    			arrivalGoodsById(sel_row[0].dhid,"returnback",btn_returnback.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/* --------------------------- 结束 -----------------------------------*/
       	/**显示隐藏**/      
    	$("#arrivalGoods_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(arrivalGoods_turnOff){
    			$("#arrivalGoods_formSearch #searchMore").slideDown("low");
    			arrivalGoods_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#arrivalGoods_formSearch #searchMore").slideUp("low");
    			arrivalGoods_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
    	
    };
    	return oInit;
};

    
function arrivalGoodsResult(isTurnBack){
	//关闭高级搜索条件
	$("#arrivalGoods_formSearch #searchMore").slideUp("low");
	arrivalGoods_turnOff=true;
	$("#arrivalGoods_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#arrivalGoods_formSearch #arrivalGoods_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#arrivalGoods_formSearch #arrivalGoods_list').bootstrapTable('refresh');
	}
}


$(function(){
	
	
	// 1.初始化Table
	var oTable = new arrivalGoods_TableInit();
	oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new arrivalGoods_ButtonInit();
    oButtonInit.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#arrivalGoods_formSearch .chosen-select').chosen({width: '100%'});
	
	$("#arrivalGoods_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	
});