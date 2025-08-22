var OutDepotList=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#outDepot_formSearch #outDepot_list").bootstrapTable({
			url: $("#outDepot_formSearch #urlPrefix").val()+'/warehouse/outDepot/pageGetListOutDepot',
			method: 'get',                      // 请求方式（*）
			toolbar: '#outDepot_formSearch #toolbar',                // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         // 增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName: "ckgl.lrsj",				// 排序字段
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
			uniqueId: "ckid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true,
				width: '4%'
			},{
				field: 'ckdh',
				title: '出库单号',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'ckrq',
				title: '出库日期',
				width: '8%',
				align: 'left',
				visible: true
			},{
				field: 'ckmc',
				title: '出库仓库',
				width: '6%',
				align: 'left',
				visible: true
			},{
				field: 'cklbmc',
				title: '出库类别',
				width: '8%',
				align: 'left',
				visible: false
			},{
				field: 'llrmc',
				title: '领料人',
				width: '6%',
				align: 'left',
				visible: true
			},{
				field: 'flrmc',
				title: '发料人',
				width: '6%',
				align: 'left',
				visible: true
			},{
				field: 'lldh',
				title: '领料单号',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'u8ckdh',
				title: 'U8出库单号',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'djlxmc',
				title: '单据类型',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'bmmc',
				title: '部门',
				width: '8%',
				align: 'left',
				visible: true
			},{
				field: 'jlbh',
				title: '记录编号',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'bz',
				title: '备注',
				width: '20%',
				align: 'left',
				visible: true
			}, {
				field: 'zt',
				title: '审核状态',
				width: '6%',
				align: 'left',
				formatter:ckztformat,
				visible: true
			},{
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '5%',
				align: 'left',
				formatter:outDepot_czFormat,
				visible: true
			}],
			onLoadSuccess:function(){
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				outDepotById(row.ckid,'view',$("#outDepot_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#outDepot_formSearch #outDepot_list").colResizable({
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
			sortLastName: "ckgl.ckdh", // 防止同名排位用
			sortLastOrder: "desc" // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		return outDepotSearchData(map);
	};
	return oTableInit;
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function outDepot_czFormat(value,row,index) {
	if (row.zt == '10') {
		return "<span class='btn btn-warning' onclick=\"recallOutInStorage('" + row.ckid + "',event)\" >撤回</span>";
	}else if(row.zt == '10'){
		return "<span class='btn btn-warning' onclick=\"recallOutInStorage('" + row.ckid + "',event)\" >撤回</span>";
	}else{
		return "";
	}
}

//撤回项目提交
function recallOutInStorage(rkid,event){
	var auditType = "AUDIT_GOODS_DELIVERY";
	var msg = '您确定要撤回该出库单吗？';
	var putInStorage_params = [];
	putInStorage_params.prefix = $("#outDepot_formSearch #urlPrefix").val();
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,rkid,function(){
				outDepotResult();
			},putInStorage_params);
		}
	});
}


//状态格式化
function ckztformat(value,row,index){
	if (row.zt == '00') {
		return '未提交';
	}else if (row.zt == '80'){
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.ckid + "\",event,\"AUDIT_GOODS_DELIVERY\",{prefix:\"" + $('#outDepot_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
	}else if (row.zt == '15') {

		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.ckid + "\",event,\"AUDIT_GOODS_DELIVERY\",{prefix:\"" + $('#outDepot_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
	}else{
		// " + row.shxx_dqgwmc + "
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.ckid + "\",event,\"AUDIT_GOODS_DELIVERY\",{prefix:\"" + $('#outDepot_formSearch #urlPrefix').val() + "\"})' >"+row.shxx_dqgwmc+"审核中</a>";
	}
}


function outDepotSearchData(map){
	var outDepot_select=$("#outDepot_formSearch #outDepot_select").val();
	var outDepot_input=$.trim(jQuery('#outDepot_formSearch #outDepot_input').val());
	if(outDepot_select=="0"){
		map["entire"]=outDepot_input;
	}else if(outDepot_select=="1"){
		map["ckdh"]=outDepot_input;
	}else if(outDepot_select=="2"){
		map["ckmc"]=outDepot_input;
	}else if(outDepot_select=="3"){
		map["cklbmc"]=outDepot_input;
	}else if(outDepot_select=="4"){
		map["lldh"]=outDepot_input;
	}else if(outDepot_select=="5"){
		map["u8ckdh"]=outDepot_input;
	}

	var lbs = jQuery('#outDepot_formSearch #lb_id_tj').val();
	map["lbs"] = lbs;
	return map;
}
function outDepotById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#outDepot_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?ckid=" +id;
		$.showDialog(url,'详细信息',viewContractConfig);
	}else if(action =='outpunch'){
		var url= tourl + "?ckid=" +id;
		$.showDialog(url,'出库红字',punchOutDepotConfig);
	}else if(action =='mod'){
		var url= tourl + "?ckid=" +id;
		$.showDialog(url,'修改',punchOutDepotConfig);
	}else if(action =='outsourceDelivery'){
		var url= tourl;
		$.showDialog(url,'委外出库',outsourceDeliveryConfig);
	}
}
function preSubmitRecheck(){
	return true;
}

/**
 * 出库页面模态框
 */
var outsourceDeliveryConfig ={
	width		: "1550px",
	modalName	: "outsourceDeliveryModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "保 存",
			className : "btn-primary",
			callback : function() {
				if(!$("#outsourceDeliveryForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}

				var $this = this;
				var opts = $this["options"]||{};
				var ck = $("#outsourceDeliveryForm #ck option:selected");
				var attr = ck.attr("ckdm");
				$("#outsourceDeliveryForm #ckdm").val(attr);
				var ckJson = [];
				for(var i=0;i<t_map.rows.length;i++){
					if(t_map.rows[i].ckJson.length>0){
						var parse = JSON.parse(t_map.rows[i].ckJson);
						var sum=0;
						for (var j = 0; j < parse.length; j++) {
							if(parse[j].sl){
								sum=sum+parseFloat(parse[j].sl);
								var sz = {"hwid":'',"wlid":'',"kcl":'',"sl":'',"ckkcl":'',"ckhwid":'',"kcsl":'',"wlmc":'',"scph":'',"cpjgmxid":'',"htmxid":''};
								sz.hwid = parse[j].hwid;
								sz.wlid = parse[j].wlid;
								sz.kcl = parse[j].kcl;
								sz.sl = parse[j].sl;
								sz.ckkcl =parse[j].ckkcl;
								sz.ckhwid =parse[j].ckhwid;
								sz.kcsl = parse[j].kcsl;
								sz.wlmc = parse[j].wlmc;
								sz.scph = parse[j].scph;
								sz.cpjgmxid = t_map.rows[i].cpjgmxid;
								sz.htmxid = t_map.rows[i].htmxid;
								ckJson.push(sz);
							}
						}
						var klsl=parseFloat(t_map.rows[i].klsl);
						if(klsl<sum){
							$.error("物料名称："+ckJson[i].wlmc+"生产批号："+ckJson[i].scph+"的货物信息所填数量总和大于可领数量！");
							return false;
						}
					}
				}
				for (var i = 0; i < ckJson.length; i++) {
					var num=0;
					var kcl=parseFloat(ckJson[i].kcl);
					for (var j = 0; j < ckJson.length; j++) {
						if(ckJson[i].hwid==ckJson[j].hwid){
							num=num+parseFloat(ckJson[j].sl);
						}
					}
					if(kcl<num){
						$.error("物料名称："+ckJson[i].wlmc+"生产批号："+ckJson[i].scph+"的货物信息所填数量总和大于可出数量！");
						return false;
					}
				}
				$("#outsourceDeliveryForm #ckmx_json").val(JSON.stringify(ckJson));

				$("#outsourceDeliveryForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"outsourceDeliveryForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						if(opts.offAtOnce){
							$.closeModal(opts.modalName);
						}
						// searchReceiveMaterielResult();
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							outDepotResult();
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
}

/**
 * 出库红字修改模态框
 */
var punchOutDepotConfig={
	width		: "1600px",
	modalName	: "punchOutDepotModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "提交",
			className : "btn-primary",
			callback : function() {
				if(!$("#editOutPunchForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}
				var json = [];
				if(t_map.rows != null && t_map.rows.length > 0){
					for(var i=0;i<t_map.rows.length;i++){
						if(t_map.rows[i].cksl==null || t_map.rows[i].cksl==''){
							$.alert("出库数量不能为空！");
							return false;
						}
						if(t_map.rows[i].scph==null || t_map.rows[i].scph==''){
							$.alert("生产批号不能为空！");
							return false;
						}
						if(t_map.rows[i].scrq==null || t_map.rows[i].scrq==''){
							$.alert("生产日期不能未空！");
							return false;
						}
						if(t_map.rows[i].yxq==null || t_map.rows[i].yxq==''){
							$.alert("有效期不能为空！");
							return false;
						}
						if(t_map.rows[i].cksl==0){
							$.alert("出库数量不能为0！");
							return false;
						}
						var sz = {"ckmxid":'',"yymxckd":'',"hwid":'',"wlid":'',"sl":'',"kcl":'',"yxq":'',"ckid":'',"kwbh":'',"bz":'',"scrq":'',"scph":'',"htmxid":'',"qgmxid":'',"cpzch":''};
						sz.wlid = t_map.rows[i].wlid;
						sz.hwid = t_map.rows[i].hwid;
						sz.ckmxid = t_map.rows[i].ckmxid;
						sz.yymxckd = t_map.rows[i].yymxckd;
						sz.sl = t_map.rows[i].cksl;
						sz.kcl = t_map.rows[i].cksl;
						sz.yxq = t_map.rows[i].yxq;
						sz.ckid = $("#editOutPunchForm #ck").val();
						sz.kwbh = t_map.rows[i].kwbh;
						sz.bz = t_map.rows[i].bz;
						sz.scrq = t_map.rows[i].scrq;
						sz.scph = t_map.rows[i].scph;
						sz.htmxid = t_map.rows[i].htmxid;
						sz.qgmxid = t_map.rows[i].qgmxid;
						sz.cpzch = t_map.rows[i].cpzch;
						json.push(sz);
					}
					$("#editOutPunchForm #hwxx_json").val(JSON.stringify(json));
				}else{
					$.alert("出库明细信息不能为空！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#editOutPunchForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"editOutPunchForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							var auditType = $("#editOutPunchForm #auditType").val();
							var receiveMateriel_params=[];
							receiveMateriel_params.prefix=$('#editOutPunchForm #urlPrefix').val();
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							//提交审核
							showAuditFlowDialog(auditType, responseText["ywid"],function(){
								outDepotResult();
							},null,receiveMateriel_params);

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
		successtwo : {
			label : "保存",
			className : "btn-success",
			callback : function() {
				if(!$("#editOutPunchForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}
				var json = [];
				if(t_map.rows != null && t_map.rows.length > 0){
					for(var i=0;i<t_map.rows.length;i++){
						if(t_map.rows[i].cksl==null || t_map.rows[i].cksl==''){
							$.alert("出库数量不能为空！");
							return false;
						}
						if(t_map.rows[i].scph==null || t_map.rows[i].scph==''){
							$.alert("生产批号不能为空！");
							return false;
						}
						if(t_map.rows[i].scrq==null || t_map.rows[i].scrq==''){
							$.alert("生产日期不能未空！");
							return false;
						}
						if(t_map.rows[i].yxq==null || t_map.rows[i].yxq==''){
							$.alert("有效期不能为空！");
							return false;
						}
						if(t_map.rows[i].cksl==0){
							$.alert("出库数量不能为0！");
							return false;
						}
						var sz = {"ckmxid":'',"yymxckd":'',"hwid":'',"wlid":'',"sl":'',"kcl":'',"yxq":'',"ckid":'',"kwbh":'',"bz":'',"scrq":'',"scph":'',"htmxid":'',"qgmxid":'',"cpzch":''};
						sz.wlid = t_map.rows[i].wlid;
						sz.hwid = t_map.rows[i].hwid;
						sz.ckmxid = t_map.rows[i].ckmxid;
						sz.yymxckd = t_map.rows[i].yymxckd;
						sz.sl = t_map.rows[i].cksl;
						sz.kcl = t_map.rows[i].cksl;
						sz.yxq = t_map.rows[i].yxq;
						sz.ckid = $("#editOutPunchForm #ck").val();
						sz.kwbh = t_map.rows[i].kwbh;
						sz.bz = t_map.rows[i].bz;
						sz.scrq = t_map.rows[i].scrq;
						sz.scph = t_map.rows[i].scph;
						sz.htmxid = t_map.rows[i].htmxid;
						sz.qgmxid = t_map.rows[i].qgmxid;
						sz.cpzch = t_map.rows[i].cpzch;
						json.push(sz);
					}
					$("#editOutPunchForm #hwxx_json").val(JSON.stringify(json));
				}else{
					$.alert("出库明细信息不能为空！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#editOutPunchForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"editOutPunchForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							outDepotResult();
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
var viewContractConfig = {
	width		: "1200px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var outDepot_turnOff = true;
var outDepot_ButtonInit = function(){
	var oInit = new Object();
	var postdata = {};

	oInit.Init = function () {
		var btn_query = $("#outDepot_formSearch #btn_query");
		var btn_del = $("#outDepot_formSearch #btn_del");
		var btn_ckdprint = $("#outDepot_formSearch #btn_ckdprint");//出库单打印
		var btn_view = $("#outDepot_formSearch #btn_view");//出库单打印
		var btn_outpunch = $("#outDepot_formSearch #btn_outpunch");//出库红字
		var btn_mod = $("#outDepot_formSearch #btn_mod");//修改
		var btn_discard = $("#outDepot_formSearch #btn_discard");//废弃
		var btn_outsourceDelivery = $("#outDepot_formSearch #btn_outsourcedelivery");//委外出库

		/* --------------------------- 委外出库 -----------------------------------*/
		btn_outsourceDelivery.unbind("click").click(function(){
			outDepotById(null, "outsourceDelivery", btn_outsourceDelivery.attr("tourl"));
		});
		/* --------------------------- 出库红字 -----------------------------------*/
		btn_outpunch.unbind("click").click(function(){
			var sel_row = $('#outDepot_formSearch #outDepot_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
				if(sel_row[0].djlxdm=="HD"){
					$.alert("该数据单据类型为红单！");
				}else{
					outDepotById(sel_row[0].ckid, "outpunch", btn_outpunch.attr("tourl"));
				}
			}else if(sel_row.length > 1){
				$.alert("只能选中一条！");
			}else{
				outDepotById("", "outpunch", btn_outpunch.attr("tourl"));
			}
		});

		/*-----------------------显示隐藏------------------------------------*/
		$("#outDepot_formSearch #sl_searchMore").on("click", function(ev){
			var ev=ev||event;
			if(outDepot_turnOff){
				$("#outDepot_formSearch #searchMore").slideDown("low");
				outDepot_turnOff=false;
				this.innerHTML="基本筛选";
			}else{
				$("#outDepot_formSearch #searchMore").slideUp("low");
				outDepot_turnOff=true;
				this.innerHTML="高级筛选";
			}
			ev.cancelBubble=true;
		});
		/* --------------------------- 修改出库信息 -----------------------------------*/
		btn_mod.unbind("click").click(function(){
			var sel_row = $('#outDepot_formSearch #outDepot_list').bootstrapTable('getSelections');//获取选择行数据
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
				if(sel_row[i].zt !='00' && sel_row[i].zt !='15'){
					$.error("该记录在审核中或已审核，不允许废弃!");
					return;
				}
			}
			if(sel_row.length == 1){
				if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
					outDepotById(sel_row[0].ckid, "mod", btn_mod.attr("tourl"));
				}else{
					$.alert("该记录在审核中或已审核，不允许修改!");
				}
			}else{
				$.error("请选中一行");
			}
		});
		if(btn_query != null){
			btn_query.unbind("click").click(function(){
				outDepotResult(true);
			});
		}

		/* ------------------------------废弃出库单-----------------------------*/
		btn_discard.unbind("click").click(function(){
			var sel_row = $('#outDepot_formSearch #outDepot_list').bootstrapTable('getSelections');//获取选择行数据
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
					ids= ids + ","+ sel_row[i].ckid;
				}
				ids=ids.substr(1);
				$.confirm('您确定要废弃所选择的信息吗？',function(result){
					if(result){
						jQuery.ajaxSetup({async:false});
						var url=$('#outDepot_formSearch #urlPrefix').val() + btn_discard.attr("tourl");
						jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
							setTimeout(function(){
								if(responseText["status"] == 'success'){
									$.success(responseText["message"],function() {
										outDepotResult(true);
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
		/* --------------------------- 查看 -----------------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row = $('#outDepot_formSearch #outDepot_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				outDepotById(sel_row[0].ckid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/* --------------------------- 打印出库单 -----------------------------------*/
		btn_ckdprint.unbind("click").click(function(){
			var sel_row = $('#outDepot_formSearch #outDepot_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length ==0){
				$.error("请至少选中一行");
				return;
			}
			var ids="";
			var cklbmc=sel_row[0].cklbmc;
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
				if(sel_row[i].cklbmc!=cklbmc){
					$.error("请选择出库类别一致的数据！");
					return;
				}else{
					ids = ids + ","+ sel_row[i].ckid;
				}

			}
			ids = ids.substr(1);
			var url=$('#outDepot_formSearch #urlPrefix').val()+btn_ckdprint.attr("tourl")+"?ckids="+ids.toString()+"&cklbcskz1="+sel_row[0].cklbcskz1+"&access_token="+$("#ac_tk").val();
			window.open(url);
		});
		/* ------------------------------删除信息-----------------------------*/
		btn_del.unbind("click").click(function(){
			var sel_row = $('#outDepot_formSearch #outDepot_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==0){
				$.error("请至少选中一行");
				return;
			}else {
				var ids = "";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + "," + sel_row[i].ckid;
				}
				ids = ids.substr(1);
				$.confirm('您确定要删除所选择的信息吗？', function (result) {
					if (result) {
						jQuery.ajaxSetup({async: false});
						var url = $('#outDepot_formSearch #urlPrefix').val()+ btn_del.attr("tourl");
						jQuery.post(url, {ids: ids, "access_token": $("#ac_tk").val()}, function (responseText) {
							setTimeout(function () {
								if (responseText["status"] == 'success') {
									$.success(responseText["message"], function () {
										outDepotResult(true);
									});
								} else if (responseText["status"] == "fail") {
									$.error(responseText["message"], function () {
									});
								} else {
									$.alert(responseText["message"], function () {
									});
								}
							}, 1);

						}, 'json');
						jQuery.ajaxSetup({async: true});
					}
				});
			}
		});
	};
	return oInit;
};

var outDepot_PageInit = function(){
	var oInit = new Object();
	var postdata = {};

	oInit.Init = function () {
		var scbj = $("#outDepot_formSearch a[id^='scbj_id_']");
		$.each(scbj, function(i, n){
			var id = $(this).attr("id");
			var code = id.substring(id.lastIndexOf('_')+1,id.length);
			if(code === '0'){
				addTj('scbj',code,'outDepot_formSearch');
			}
		});
	}
	return oInit;
}

function outDepotResult(isTurnBack){
	//关闭高级搜索条件
	$("#outDepot_formSearch #searchMore").slideUp("low");
	outDepot_turnOff=true;
	$("#outDepot_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#outDepot_formSearch #outDepot_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#outDepot_formSearch #outDepot_list').bootstrapTable('refresh');
	}
}


$(function(){

	//0.界面初始化
	var oInit = new outDepot_PageInit();
	oInit.Init();

	// 1.初始化Table
	var oTable = new OutDepotList();
	oTable.Init();

	//2.初始化Button的点击事件
	var oButtonInit = new outDepot_ButtonInit();
	oButtonInit.Init();

	// 所有下拉框添加choose样式
	jQuery('#outDepot_formSearch .chosen-select').chosen({width: '100%'});

	$("#outDepot_ButtonInit [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});

});