var ReceiveMateriel_turnOff=true;

var ReceiveMateriel_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#receiveAdministrationMaterielList_formSearch #receiveAdministrationMateriel_list").bootstrapTable({
			url: $("#receiveAdministrationMaterielList_formSearch #urlPrefix").val()+'/storehouse/receiveAdministrationMateriel/pageGetListReceiveAdministrationMateriel',         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#receiveAdministrationMaterielList_formSearch #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   //是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "xzllgl.lrsj",				//排序字段
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
			uniqueId: "xzllid",                     //每一行的唯一标识，一般为主键列
			showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: [{
				checkbox: true,
				width: '4%'
			},{
				title: '序号',
				width: '4%',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '4%',
				align: 'left',
				visible:true
			},{
				field: 'xzllid',
				title: '领料ID',
				width: '12%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'lldh',
				title: '领料单号',
				width: '12%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'sqbm',
				title: '申请部门',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'llry',
				title: '领料人员',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'flrymc',
				title: '发料人员',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'sqrq',
				title: '申请日期',
				width: '12%',
				align: 'left',
				visible: true
			},{
				field: 'bz',
				title: '备注',
				width: '20%',
				align: 'left',
				visible: true
			},{
				field: 'lllx',
				title: '领料类型',
				width: '6%',
				align: 'left',
				formatter:lllxformat,
				visible: true
			},{
				field: 'zt',
				title: '状态',
				width: '10%',
				align: 'left',
				formatter: ztformat,
				visible: true
			},{
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '7%',
				align: 'left',
				formatter:czFormat,
				visible: true
			}],
			onLoadSuccess:function(){
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				ReceiveMaterielDealById(row.xzllid,'view',$("#receiveAdministrationMaterielList_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#receiveAdministrationMaterielList_formSearch #receiveAdministrationMateriel_list").colResizable({
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
			sortLastName: "xzllgl.xzllid", // 防止同名排位用
			sortLastOrder: "asc" // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		return getReceiveMaterielSearchData(map);
	}
	return oTableInit
}

function lllxformat(value,row,index) {
	if (row.lllx == '1') {
		return "设备领料";
	}else if (row.lllx == '0') {
		return "普通领料";
	}
	return "";
}
//领料列表的提交状态格式化函数
function ztformat(value,row,index) {
	if (row.zt == '00') {
		return '未提交';
	}else if (row.zt == '80'){
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.xzllid + "\",event,\"AUDIT_ADMINISTRATION,AUDIT_ADMINISTRATION_DEVICE\",{prefix:\"" + $('#receiveAdministrationMaterielList_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
	}else if (row.zt == '15') {
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.xzllid + "\",event,\"AUDIT_ADMINISTRATION,AUDIT_ADMINISTRATION_DEVICE\",{prefix:\"" + $('#receiveAdministrationMaterielList_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
	}else{
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.xzllid + "\",event,\"AUDIT_ADMINISTRATION,AUDIT_ADMINISTRATION_DEVICE\",{prefix:\"" + $('#receiveAdministrationMaterielList_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
	}
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
	if (row.zt == '10'  ) {
		if (row.lllx=='1'){
			return "<span class='btn btn-warning' onclick=\"recallRequisitions('" + row.xzllid +"','AUDIT_ADMINISTRATION_DEVICE',event)\" >撤回</span>";
		}
		return "<span class='btn btn-warning' onclick=\"recallRequisitions('" + row.xzllid +"','AUDIT_ADMINISTRATION',event)\" >撤回</span>";
	}else{
		return "";
	}
}
//撤回请购单提交
function recallRequisitions(xzllid,shlx,event){
	var auditType = null;
	if(shlx){
		auditType = shlx;
	}else{
		auditType = $("#receiveAdministrationMaterielList_formSearch #auditType").val();
	}
	var msg = '您确定要撤回该行政领料单吗？';
	var purchase_params = [];
	purchase_params.prefix = $("#receiveAdministrationMaterielList_formSearch #urlPrefix").val();
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,xzllid,function(){
				searchReceiveMaterielResult();
			},purchase_params);
		}
	});
}

//条件搜索
function getReceiveMaterielSearchData(map){
	var cxtj=$("#receiveAdministrationMaterielList_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#receiveAdministrationMaterielList_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr
	}else if(cxtj=="1"){
		map["lldh"]=cxnr
	}else if(cxtj=="2"){
		map["sqbm"]=cxnr
	}else if(cxtj=="3"){
		map["llry"]=cxnr
	}
	else if(cxtj=="4"){
		map["flry"]=cxnr
	}	else if(cxtj=="5"){
		map["hwmc"]=cxnr
	}	else if(cxtj=="6"){
		map["hwbz"]=cxnr
	}
	// 高级筛选
	// 审核状态
	var kws = jQuery('#receiveAdministrationMaterielList_formSearch #kw_id_tj').val();
	map["kws"] = kws.replace(/'/g, "");
	// 领料开始日期
	var sqrqstart = jQuery('#receiveAdministrationMaterielList_formSearch #sqrqstart').val();
	map["sqrqstart"] = sqrqstart;
	// 领料结束日期
	var sqrqend = jQuery('#receiveAdministrationMaterielList_formSearch #sqrqend').val();
	map["sqrqend"] = sqrqend;
	// 领料类型
	var lllxs = jQuery('#receiveAdministrationMaterielList_formSearch #lllx_id_tj').val();
	map["lllxs"] = lllxs.replace(/'/g, "");
	return map;
}
//提供给导出用的回调函数
function ReceiveMaterielSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="xzllgl.xzllid";
	map["sortLastOrder"]="asc";
	map["sortName"]="xzllgl.lldh";
	map["sortOrder"]="asc";
	return getReceiveMaterielSearchData(map);
}

var ReceiveMateriel_ButtonInit= function (){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#receiveAdministrationMaterielList_formSearch #btn_query");// 模糊查询
		var btn_view=$("#receiveAdministrationMaterielList_formSearch #btn_view");// 查看详情
		var btn_del = $("#receiveAdministrationMaterielList_formSearch #btn_del");// 删除
		var btn_mod=$("#receiveAdministrationMaterielList_formSearch #btn_mod");// 修改
		var btn_submit=$("#receiveAdministrationMaterielList_formSearch #btn_submit");// 提交
		var btn_add=$("#receiveAdministrationMaterielList_formSearch #btn_add");// 修改
		var btn_release=$("#receiveAdministrationMaterielList_formSearch #btn_release");// 修改
		var btn_discard=$("#receiveAdministrationMaterielList_formSearch #btn_discard");// 废弃
		var btn_searchexport = $("#receiveAdministrationMaterielList_formSearch #btn_searchexport");
		var btn_selectexport = $("#receiveAdministrationMaterielList_formSearch #btn_selectexport");
		var btn_devicematering = $("#receiveAdministrationMaterielList_formSearch #btn_devicematering");

		//添加日期控件
		laydate.render({
			elem: '#receiveAdministrationMaterielList_formSearch #sqrqstart'
			,theme: '#2381E9'
		});
		//添加日期控件
		laydate.render({
			elem: '#receiveAdministrationMaterielList_formSearch #sqrqend'
			,theme: '#2381E9'
		});
		/*--------------------------------模糊查询---------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchReceiveMaterielResult(true);
			});
		}
		/* --------------------------- 新增领料信息 -----------------------------------*/
		btn_add.unbind("click").click(function(){
			ReceiveMaterielDealById(null, "add", btn_add.attr("tourl"));
		});
		/* --------------------------- 设备领料新增 -----------------------------------*/
		btn_devicematering.unbind("click").click(function(){
			ReceiveMaterielDealById(null, "devicematering", btn_devicematering.attr("tourl"));
		});
		/* --------------------------- 修改领料信息 -----------------------------------*/
		btn_mod.unbind("click").click(function(){
			var sel_row = $('#receiveAdministrationMaterielList_formSearch #receiveAdministrationMateriel_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
				if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
					ReceiveMaterielDealById(sel_row[0].xzllid, "mod", btn_mod.attr("tourl"));
				}else{
					$.alert("该记录在审核中或已审核，不允许修改!");
				}
			}else{
				$.error("请选中一行");
			}
		});
		/* --------------------------- 出库领料信息 -----------------------------------*/
		btn_release.unbind("click").click(function(){
			var sel_row = $('#receiveAdministrationMaterielList_formSearch #receiveAdministrationMateriel_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
				if(sel_row[0].flry!=null&&sel_row[0].flry!=''){
					$.alert("该记录已经出库!");
				}else{
					if(sel_row[0].zt=="80"){
						ReceiveMaterielDealById(sel_row[0].xzllid, "release", btn_release.attr("tourl"));
					}else{
						$.alert("该记录未审核通过，不允许出库!");
					}
				}
			}else{
				$.error("请选中一行");
			}
		});
		/* --------------------------- 修改提交领料信息 -----------------------------------*/
		btn_submit.unbind("click").click(function(){
			var sel_row = $('#receiveAdministrationMaterielList_formSearch #receiveAdministrationMateriel_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
				if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
					ReceiveMaterielDealById(sel_row[0].xzllid, "submit", btn_submit.attr("tourl"));
				}else{
					$.alert("该状态不允许提交!");
				}
			}else{
				$.error("请选中一行");
			}
		});
		/* ------------------------------查看领料信息-----------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row=$('#receiveAdministrationMaterielList_formSearch #receiveAdministrationMateriel_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				ReceiveMaterielDealById(sel_row[0].xzllid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		// 	  ---------------------------导出-----------------------------------
		btn_selectexport.unbind("click").click(function(){
			var sel_row = $('#receiveAdministrationMaterielList_formSearch #receiveAdministrationMateriel_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].xzllid;
				}
				ids = ids.substr(1);
				$.showDialog($('#receiveAdministrationMaterielList_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=XZLL_SELECT&expType=select&ids="+ids
					,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
			}else{
				$.error("请选择数据");
			}
		});

		btn_searchexport.unbind("click").click(function(){
			$.showDialog($('#receiveAdministrationMaterielList_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=XZLL_SEARCH&expType=search&callbackJs=ReceiveMaterielSearchData"
				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
		});
		/* ------------------------------删除领料信息-----------------------------*/
		btn_del.unbind("click").click(function(){
			var sel_row = $('#receiveAdministrationMaterielList_formSearch #receiveAdministrationMateriel_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==0){
				$.error("请至少选中一行");
				return;
			}else {
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids= ids + ","+ sel_row[i].xzllid;
				}
				ids=ids.substr(1);
				$.confirm('您确定要删除所选择的信息吗？',function(result){
					if(result){
						jQuery.ajaxSetup({async:false});
						var url= $("#receiveAdministrationMaterielList_formSearch #urlPrefix").val()+btn_del.attr("tourl");
						jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
							setTimeout(function(){
								if(responseText["status"] == 'success'){
									$.success(responseText["message"],function() {
										searchReceiveMaterielResult();
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
		/* ------------------------------废弃领料信息-----------------------------*/
		btn_discard.unbind("click").click(function(){
			var sel_row = $('#receiveAdministrationMaterielList_formSearch #receiveAdministrationMateriel_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==0){
				$.error("请至少选中一行");
				return;
			}else {
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					if(sel_row[i].zt=="00" || sel_row[i].zt=="15"){
						ids= ids + ","+ sel_row[i].xzllid;
					}else{
						$.error("请选择未提交或者审核未通过的数据！");
						return;
					}
				}
				ids=ids.substr(1);
				$.confirm('您确定要废弃所选择的信息吗？',function(result){
					if(result){
						jQuery.ajaxSetup({async:false});
						var url= $("#receiveAdministrationMaterielList_formSearch #urlPrefix").val()+btn_discard.attr("tourl");
						jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
							setTimeout(function(){
								if(responseText["status"] == 'success'){
									$.success(responseText["message"],function() {
										searchReceiveMaterielResult();
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
		$("#receiveAdministrationMaterielList_formSearch #sl_searchMore").on("click", function(ev){
			var ev=ev||event;
			if(ReceiveMateriel_turnOff){
				$("#receiveAdministrationMaterielList_formSearch #searchMore").slideDown("low");
				ReceiveMateriel_turnOff=false;
				this.innerHTML="基本筛选";
			}else{
				$("#receiveAdministrationMaterielList_formSearch #searchMore").slideUp("low");
				ReceiveMateriel_turnOff=true;
				this.innerHTML="高级筛选";
			}
			ev.cancelBubble=true;
		});
	}
	return oInit;
}

//按钮操作
function ReceiveMaterielDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#receiveAdministrationMaterielList_formSearch #urlPrefix").val()+tourl;
	if(action =='mod'){
		var url= tourl+"?xzllid="+id;
		$.showDialog(url,'修改领料信息',modReceiveMaterielConfig);
	}else if(action =='view'){
		var url= tourl+"?xzllid="+id;
		$.showDialog(url,'查看领料信息',viewReceiveMaterielConfig);
	}else if(action=="del"){
		var url=tourl+"?xzllid="+id;
		$.showDialog(url,'删除领料信息',delReceiveMaterielConfig);
	}else if(action=="submit"){
		var url=tourl+"?xzllid="+id;
		$.showDialog(url,'提交领料信息',subReceiveMaterielConfig);
	}else if(action=="add"){
		var url=tourl;
		$.showDialog(url,'新增领料信息',modReceiveMaterielConfig);
	}else if(action=="release"){
		var url=tourl+"?xzllid="+id;
		$.showDialog(url,'出库',releaseReceiveMaterielConfig);
	}else if(action=="devicematering"){
		var url=tourl;
		$.showDialog(url,'新增领料信息',modReceiveMaterielConfig);
	}
}

/**
 * 查看页面模态框
 */
var viewReceiveMaterielConfig={
	width		: "1600px",
	modalName	: "viewReceiveMaterielModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}
/**
 * 修改页面模态框
 */
var modReceiveMaterielConfig ={
	width		: "1600px",
	modalName	: "modReceiveMaterielModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		/*success : {
			label : "提 交",
			className : "btn-primary",
			callback : function() {
				if(!$("#editXzPickingForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				let jgdm= $("#editXzPickingForm #jgdm").val()
				if(!jgdm){
					$.alert("所选部门存在异常！");
					return false;
				}

				var nowDate=new Date();//当前系统时间
				var $this = this;
				var opts = $this["options"]||{};
				var json = [];
				if(t_map.rows != null && t_map.rows.length > 0){
					for(var i=0;i<t_map.rows.length;i++){
						if((t_map.rows[i].qls==null || t_map.rows[i].qls=='')){
							$.alert("领料数量不能为空！");
							return false;
						}
						if(parseFloat(t_map.rows[i].qls)>parseFloat(t_map.rows[i].kcl)){
							$.alert("请领数不能大于库存量！");
							return false;
						}
						if(t_map.rows[i].qls==0){
							$.alert("请领数量不能为0！");
							return false;
						}
						var sz = {"xzllid":'',"xzllmxid":'',"xzrkmxid":'',"qls":'',"hwmc":'',"hwgg":'',"bz":'',"xh":'',"hwjldw":''};
						sz.xzllid = t_map.rows[i].xzllid;
						sz.xzllmxid = t_map.rows[i].xzllmxid;
						sz.xzrkmxid = t_map.rows[i].xzrkmxid;
						sz.qls = t_map.rows[i].qls;
						sz.hwmc = t_map.rows[i].hwmc;
						sz.hwgg = t_map.rows[i].hwgg;
						sz.hwjldw = t_map.rows[i].hwjldw;
						sz.bz = t_map.rows[i].bz;
						sz.xh = i+1;
						json.push(sz);
					}
					$("#editXzPickingForm #llmx_json").val(JSON.stringify(json));
				}else{
					$.alert("领料信息不能为空！");
					return false;
				}

				$("#editXzPickingForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"editXzPickingForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						var auditType = $("#editXzPickingForm #auditType").val();
						var picking_params=[];
						picking_params.prefix=$('#editXzPickingForm #urlPrefix').val();
						//提交审核
						showAuditFlowDialog(auditType,responseText["ywid"],function(){
							$("#administrationStock_formSearch #idswl").val("1,2")
							$("#administrationStock_formSearch #ll_num").remove();
							$.closeModal(opts.modalName);
							stockResult();
						},null,picking_params);

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
		},*/
		success : {
			label : "保 存",
			className : "btn-success",
			callback : function() {
				if(!$("#editXzPickingForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}

				var $this = this;
				var opts = $this["options"]||{};
				var json = [];
				if(t_map.rows != null && t_map.rows.length > 0){
					for(var i=0;i<t_map.rows.length;i++){
						if(t_map.rows[i].qlsl==null || t_map.rows[i].qlsl==''){
							$.alert("领料数量不能为空！");
							return false;
						}
						if(parseFloat(t_map.rows[i].qlsl)>parseFloat(t_map.rows[i].klsl)){
							$.alert("领用数量不能大于可领数量！");
							return false;
						}
						if(t_map.rows[i].qlsl==0){
							$.alert("请领数量不能为0！");
							return false;
						}
						var sz = {"xzkcid":'',"qlsl":'',"xh":'',"yds":'',"sbysid":''};
						sz.xzkcid = t_map.rows[i].xzkcid;
						sz.qlsl = t_map.rows[i].qlsl;
						sz.yds = t_map.rows[i].yds;
						sz.sbysid = t_map.rows[i].sbysid;
						sz.xh = i+1;
						json.push(sz);
					}
					$("#editXzPickingForm #llmx_json").val(JSON.stringify(json));
				}else{
					$.alert("领料信息不能为空！");
					return false;
				}

				$("#editXzPickingForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"editXzPickingForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						if(opts.offAtOnce){
							$.closeModal(opts.modalName);
						}
						// searchReceiveMaterielResult();
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							searchReceiveMaterielResult();
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
 * 出库页面模态框
 */
var releaseReceiveMaterielConfig ={
	width		: "1600px",
	modalName	: "modReceiveMaterielModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		/*success : {
			label : "提 交",
			className : "btn-primary",
			callback : function() {
				if(!$("#editXzPickingForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				let jgdm= $("#editXzPickingForm #jgdm").val()
				if(!jgdm){
					$.alert("所选部门存在异常！");
					return false;
				}

				var nowDate=new Date();//当前系统时间
				var $this = this;
				var opts = $this["options"]||{};
				var json = [];
				if(t_map.rows != null && t_map.rows.length > 0){
					for(var i=0;i<t_map.rows.length;i++){
						if((t_map.rows[i].qls==null || t_map.rows[i].qls=='')){
							$.alert("领料数量不能为空！");
							return false;
						}
						if(parseFloat(t_map.rows[i].qls)>parseFloat(t_map.rows[i].kcl)){
							$.alert("请领数不能大于库存量！");
							return false;
						}
						if(t_map.rows[i].qls==0){
							$.alert("请领数量不能为0！");
							return false;
						}
						var sz = {"xzllid":'',"xzllmxid":'',"xzrkmxid":'',"qls":'',"hwmc":'',"hwgg":'',"bz":'',"xh":'',"hwjldw":''};
						sz.xzllid = t_map.rows[i].xzllid;
						sz.xzllmxid = t_map.rows[i].xzllmxid;
						sz.xzrkmxid = t_map.rows[i].xzrkmxid;
						sz.qls = t_map.rows[i].qls;
						sz.hwmc = t_map.rows[i].hwmc;
						sz.hwgg = t_map.rows[i].hwgg;
						sz.hwjldw = t_map.rows[i].hwjldw;
						sz.bz = t_map.rows[i].bz;
						sz.xh = i+1;
						json.push(sz);
					}
					$("#editXzPickingForm #llmx_json").val(JSON.stringify(json));
				}else{
					$.alert("领料信息不能为空！");
					return false;
				}

				$("#editXzPickingForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"editXzPickingForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						var auditType = $("#editXzPickingForm #auditType").val();
						var picking_params=[];
						picking_params.prefix=$('#editXzPickingForm #urlPrefix').val();
						//提交审核
						showAuditFlowDialog(auditType,responseText["ywid"],function(){
							$("#administrationStock_formSearch #idswl").val("1,2")
							$("#administrationStock_formSearch #ll_num").remove();
							$.closeModal(opts.modalName);
							stockResult();
						},null,picking_params);

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
		},*/
		success : {
			label : "保 存",
			className : "btn-success",
			callback : function() {
				if(!$("#editXzPickingForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}

				var $this = this;
				var opts = $this["options"]||{};
				var json = [];
				if(t_map.rows != null && t_map.rows.length > 0){
					for(var i=0;i<t_map.rows.length;i++){
						if(t_map.rows[i].qlsl==null || t_map.rows[i].qlsl==''){
							$.alert("领料数量不能为空！");
							return false;
						}
						if(parseFloat(t_map.rows[i].qlsl)>parseFloat(t_map.rows[i].klsl)){
							$.alert("领用数量不能大于可领数量！");
							return false;
						}
						if(t_map.rows[i].qlsl==0){
							$.alert("请领数量不能为0！");
							return false;
						}
						var sz = {"xzkcid":'',"cksl":'',"xh":'',"yds":'',"xzllmxid":'',"sbysid":''};
						sz.xzkcid = t_map.rows[i].xzkcid;
						sz.cksl = t_map.rows[i].qlsl;
						sz.yds = t_map.rows[i].yds;
						sz.xzllmxid = t_map.rows[i].xzllmxid;
						sz.sbysid = t_map.rows[i].sbysid;
						sz.xh = i+1;
						json.push(sz);
					}
					$("#editXzPickingForm #llmx_json").val(JSON.stringify(json));
				}else{
					$.alert("领料信息不能为空！");
					return false;
				}

				$("#editXzPickingForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"editXzPickingForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						if(opts.offAtOnce){
							$.closeModal(opts.modalName);
						}
						// searchReceiveMaterielResult();
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							searchReceiveMaterielResult();
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
 * 提交页面模态框
 */
var subReceiveMaterielConfig ={
	width		: "1600px",
	modalName	: "subReceiveMaterielModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "提 交",
			className : "btn-primary",
			callback : function() {
				if(!$("#editXzPickingForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}



				var $this = this;
				var opts = $this["options"]||{};
				var json = [];
				if(t_map.rows != null && t_map.rows.length > 0){
					for(var i=0;i<t_map.rows.length;i++){
						if(t_map.rows[i].qlsl==null || t_map.rows[i].qlsl==''){
							$.alert("领料数量不能为空！");
							return false;
						}
						if(parseFloat(t_map.rows[i].qlsl)>parseFloat(t_map.rows[i].klsl)){
							$.alert("领用数量不能大于可领数量！");
							return false;
						}
						if(t_map.rows[i].qlsl==0){
							$.alert("请领数量不能为0！");
							return false;
						}
						var sz = {"xzkcid":'',"qlsl":'',"xh":'',"yds":'',"sbysid":''};
						sz.xzkcid = t_map.rows[i].xzkcid;
						sz.qlsl = t_map.rows[i].qlsl;
						sz.yds = t_map.rows[i].yds;
						sz.sbysid = t_map.rows[i].sbysid;
						sz.xh = i+1;
						json.push(sz);
					}
					$("#editXzPickingForm #llmx_json").val(JSON.stringify(json));
				}else{
					$.alert("领料信息不能为空！");
					return false;
				}

				$("#editXzPickingForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"editXzPickingForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						var auditType = $("#editXzPickingForm #auditType").val();
						var picking_params=[];
						picking_params.prefix=$('#editXzPickingForm #urlPrefix').val();
						//提交审核
						showAuditFlowDialog(auditType,responseText["ywid"],function(){
							$.closeModal(opts.modalName);
							searchReceiveMaterielResult();
						},null,picking_params);

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
		/*success : {
			label : "保 存",
			className : "btn-success",
			callback : function() {
				if(t_map.rows != null && t_map.rows.length > 0){
					for(var i=0;i<t_map.rows.length;i++){
						var sz = {"xzllid":'',"xzllmxid":'',"xzrkmxid":'',"qls":'',"hwmc":'',"hwgg":'',"bz":'',"xh":'',"hwjldw":''};
						sz.xzllid = t_map.rows[i].xzllid;
						sz.xzllmxid = t_map.rows[i].xzllmxid;
						sz.xzrkmxid = t_map.rows[i].xzrkmxid;
						sz.qls = t_map.rows[i].qls;
						sz.hwmc = t_map.rows[i].hwmc;
						sz.hwgg = t_map.rows[i].hwgg;
						sz.hwjldw = t_map.rows[i].hwjldw;
						sz.bz = t_map.rows[i].bz;
						sz.xh = i+1;
						json.push(sz);
					}
					$("#editXzPickingForm #llmx_json").val(JSON.stringify(json));
				}else{
					$.alert("领料信息不能为空！");
					return false;
				}

				$("#editXzPickingForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"editXzPickingForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$("#administrationStock_formSearch #ll_num").remove();
						$("#administrationStock_formSearch #idswl").val("1,2")
						if(opts.offAtOnce){
							$.closeModal(opts.modalName);
						}
						// searchReceiveMaterielResult();
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							searchReceiveMaterielResult();
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
		},*/
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}


//列表刷新
function searchReceiveMaterielResult(isTurnBack){
	//关闭高级搜索条件
	$("#receiveAdministrationMaterielList_formSearch #searchMore").slideUp("low");
	ReceiveMateriel_turnOff=true;
	$("#receiveAdministrationMaterielList_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#receiveAdministrationMaterielList_formSearch #receiveAdministrationMateriel_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#receiveAdministrationMaterielList_formSearch #receiveAdministrationMateriel_list').bootstrapTable('refresh');
	}
}

$(function(){
	// 1.初始化Table
	var oTable = new ReceiveMateriel_TableInit();
	oTable.Init();
	//2.初始化Button的点击事件
	var oButtonInit = new ReceiveMateriel_ButtonInit();
	oButtonInit.Init();
	jQuery('#receiveAdministrationMaterielList_formSearch .chosen-select').chosen({width: '100%'});
})