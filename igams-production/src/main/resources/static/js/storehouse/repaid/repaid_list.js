var repaid_turnOff=true;
var repaid_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#repaid_formSearch #repaid_list").bootstrapTable({
			url: $("#repaid_formSearch #urlPrefix").val()+'/repaid/repaid/pageGetListRepaid',         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#repaid_formSearch #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   //是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "ghrq",				//排序字段
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
			uniqueId: "jcghid",                     //每一行的唯一标识，一般为主键列
			showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: [{
				checkbox: true,
				width: '3%'
			},{
				field: 'jcghid',
				title: '借出归还ID',
				width: '10%',
				align: 'left',
				visible:false
			},{
				field: 'jcjyid',
				title: '借出借用id',
				width: '10%',
				align: 'left',
				visible: false
			},{
				field: 'ghdh',
				title: 'OA归还单号',
				width: '10%',
				align: 'left',
				sortable:true,
				visible: true
			},{
				field: 'u8ghdh',
				title: 'U8归还单号',
				width: '10%',
				align: 'left',
				sortable:true,
				visible: true
			},{
				field: 'bmmc',
				title: '部门',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'dwlxmc',
				title: '单位类型',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'dwmc',
				title: '单位',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'ghrq',
				title: '归还日期',
				width: '10%',
				align: 'left',
				sortable:true,
				visible:true
			},{
				field: 'sfzfyf',
				title: '支付运费',
				width: '8%',
				align: 'left',
				sortable:true,
				formatter:sfzfyfFormat,
				visible:true
			},{
				field: 'bz',
				title: '备注',
				width: '20%',
				align: 'left',
				visible: true
			},{
				field: 'zt',
				title: '状态',
				width: '15%',
				align: 'left',
				formatter:ztFormat,
				visible: true
			},{
				field: 'cz',
				title: '操作',
				width: '5%',
				align: 'left',
				formatter:czFormat,
				visible: true
			}
			],
			onLoadSuccess:function(){
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				repaid__DealById(row.jcghid,"view",$("#repaid_formSearch #btn_view").attr("tourl"));
			},
		});
			  
	};
		oTableInit.queryParams=function(params){
			var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
	        	pageSize: params.limit,   // 页面大小
	        	pageNumber: (params.offset / params.limit) + 1,  // 页码
	            access_token:$("#ac_tk").val(),
	            sortName: params.sort,      // 排序列名
	            sortOrder: params.order, // 排位命令（desc，asc）
	            sortLastName: "jcghid", // 防止同名排位用
	            sortLastOrder: "asc" // 防止同名排位用
	        };
			return getRepaidSearchData(map);
		}
	return oTableInit;
}
function getRepaidSearchData(map){
	var cxtj=$("#repaid_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#repaid_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr
	}else if(cxtj=="0"){
		map["ghdh"]=cxnr
	}else if(cxtj=="1"){
		map["bmmc"]=cxnr
	}
	var sfzfyf = jQuery('#repaid_formSearch #sfzfyf_id_tj').val();
	map["sfzfyf"] = sfzfyf;

	var zts = jQuery('#repaid_formSearch #zt_id_tj').val();
	map["zts"] = zts;

	// 采集日期
	var ghrqstart = jQuery('#repaid_formSearch #ghrqstart').val();
	map["ghrqstart"] = ghrqstart;
	var ghrqend = jQuery('#repaid_formSearch #ghrqend').val();
	map["ghrqend"] = ghrqend;

	return map;
}

/**
 * 审核列表的状态格式化函数
 * @returns
 */
function ztFormat(value,row,index) {
	if (row.zt == '00') {
		return '未提交';
	}else if (row.zt == '80'){
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.jcghid + "\",event,\"AUDIT_REPAID\",{prefix:\"" + $('#repaid_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
	}else if (row.zt == '15') {
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.jcghid + "\",event,\"AUDIT_REPAID\",{prefix:\"" + $('#repaid_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
	}else{
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.jcghid + "\",event,\"AUDIT_REPAID\",{prefix:\"" + $('#repaid_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
	}
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
	if (row.zt == '10') {
		return "<span class='btn btn-warning' onclick=\"recallRepaidCheck('" + row.jcghid +"','" + row.shlx+ "',event)\" >撤回</span>";
	}else{
		return "";
	}
}

//撤回项目提交
function recallRepaidCheck(jcghid,event){
	var auditType = $("#repaid_formSearch #auditType").val();
	var msg = '您确定要撤回该条审核吗？';
	var purchase_params = [];
	purchase_params.prefix = $("#repaid_formSearch #urlPrefix").val();
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,jcghid,function(){
				searchRepaidResult();
			},purchase_params);
		}
	});
}

//标记格式化
function sfzfyfFormat(value,row,index){
	var html="";
	if(row.sfzfyf=='1'){
		html="<span style='color:green;'>"+"是"+"</span>";
	}else{
		html="<span style='color:red;'>"+"否"+"</span>";
	}
	return html;
}


var repaid_oButtton=function(){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#repaid_formSearch #btn_query");
		var btn_view = $("#repaid_formSearch #btn_view");
		var btn_repaid = $("#repaid_formSearch #btn_repaid");
		var btn_resubmit=$("#repaid_formSearch #btn_resubmit");// 重新提交
		var btn_discard=$("#repaid_formSearch #btn_discard");
		var btn_del=$("#repaid_formSearch #btn_del");
		var btn_ysdprint=$("#repaid_formSearch #btn_ysdprint");
		//添加日期控件
		laydate.render({
			elem: '#ghrqstart'
			,theme: '#2381E9'
		});
		//添加日期控件
		laydate.render({
			elem: '#ghrqend'
			,theme: '#2381E9'
		});
		/*-----------------------模糊查询------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchRepaidResult(); 
    		});
		}
		/*-------------------新增------------------*/
		btn_repaid.unbind("click").click(function(){
			repaid__DealById(null,"repaid",btn_repaid.attr("tourl"));
		});
		/*----------------------查看----------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row=$('#repaid_formSearch #repaid_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				repaid__DealById(sel_row[0].jcghid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*--------------------------------重新提交---------------------------*/
		btn_resubmit.unbind("click").click(function(){
			var sel_row=$('#repaid_formSearch #repaid_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
					repaid__DealById(sel_row[0].jcghid,"resubmit",btn_resubmit.attr("tourl"));
				}else{
					$.alert("该状态不允许提交!");
				}
			}else{
				$.error("请选中一行");
			}
		});
		/* --------------------------- 打印请验单 -----------------------------------*/
		btn_ysdprint.unbind("click").click(function(){
			var sel_row = $('#repaid_formSearch #repaid_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length ==0){
				$.error("请至少选中一行");
				return;
			}
			var ids="";
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
				ids = ids + ","+ sel_row[i].jcghid;
			}
			ids = ids.substr(1);
			var url=$('#repaid_formSearch #urlPrefix').val()+btn_ysdprint.attr("tourl")+"?jcghids="+ids.toString()+"&access_token="+$("#ac_tk").val();
			window.open(url);
		});
		/* ------------------------------废弃-----------------------------*/
		btn_discard.unbind("click").click(function(){
			var sel_row = $('#repaid_formSearch #repaid_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==0){
				$.error("请至少选中一行");
				return;
			}else {
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					if(sel_row[i].zt=='00'||sel_row[i].zt=='15'){
						ids= ids + ","+ sel_row[i].jcghid;
					}else{
						$.error("请选择未提交的数据！");
						return;
					}
				}
				ids=ids.substr(1);
				$.confirm('您确定要废弃所选择的信息吗？',function(result){
					if(result){
						jQuery.ajaxSetup({async:false});
						var url= $('#repaid_formSearch #urlPrefix').val() + btn_discard.attr("tourl");
						jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
							setTimeout(function(){
								if(responseText["status"] == 'success'){
									$.success(responseText["message"],function() {
										searchRepaidResult();
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
			var sel_row = $('#repaid_formSearch #repaid_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==0){
				$.error("请至少选中一行");
				return;
			}else {
				var ids="";
				var tgids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					if(sel_row[i].zt=='80'){
						tgids= tgids + ","+ sel_row[i].jcghid;
					}else{
						ids= ids + ","+ sel_row[i].jcghid;
					}
				}
				ids=ids.substr(1);
				tgids=tgids.substr(1);
				$.confirm('您确定要删除所选择的信息吗？',function(result){
					if(result){
						jQuery.ajaxSetup({async:false});
						var url= $('#repaid_formSearch #urlPrefix').val() + btn_del.attr("tourl");
						jQuery.post(url,{ids:ids,tgids:tgids,"access_token":$("#ac_tk").val()},function(responseText){
							setTimeout(function(){
								if(responseText["status"] == 'success'){
									$.success(responseText["message"],function() {
										searchRepaidResult();
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
		/**显示隐藏**/
		$("#repaid_formSearch #sl_searchMore").on("click", function(ev){
			var ev=ev||event;
			if(repaid_turnOff){
				$("#repaid_formSearch #searchMore").slideDown("low");
				repaid_turnOff=false;
				this.innerHTML="基本筛选";
			}else{
				$("#repaid_formSearch #searchMore").slideUp("low");
				repaid_turnOff=true;
				this.innerHTML="高级筛选";
			}
			ev.cancelBubble=true;
		});
	}
	return oInit;
}
function repaid__DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl=$("#repaid_formSearch #urlPrefix").val()+tourl;
	if(action=="repaid"){
		var url=tourl;
		$.showDialog(url,'借出归还',addRepaidConfig);
	}else if(action=="view"){
		var url=tourl+"?jcghid="+id;
		$.showDialog(url,'查看信息',ViewRepaidConfig);
	}else if(action=="resubmit"){
		var url=tourl+"?jcghid="+id;
		$.showDialog(url,'重新提交',resubmitRepaidConfig);
	}
}

var resubmitRepaidConfig = {
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
						var sz = {"jymxid":'',"jyxxid":'',"hwid":'',"ghsl":'',"bz":'',"sfyzgh":'',"ckid":'',"kwbh":'',"wlid":'',"ghmxid":'',"zsh":''};
						sz.jymxid = t_map.rows[i].jymxid;
						sz.jyxxid = t_map.rows[i].jyxxid;
						sz.hwid = t_map.rows[i].hwid;
						sz.ghsl = t_map.rows[i].ghsl;
						sz.bz = t_map.rows[i].bz;
						sz.sfyzgh = t_map.rows[i].sfyzgh;
						sz.ckid = t_map.rows[i].ckid;
						sz.kwbh = t_map.rows[i].kwbh;
						sz.wlid = t_map.rows[i].wlid;
						sz.ghmxid = t_map.rows[i].ghmxid;
						sz.zsh = t_map.rows[i].zsh;
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
							searchRepaidResult();
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

var addRepaidConfig = {
		width		: "600px",
		modalName	:"addRepaidConfig",
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
					var $this = this;
					var opts = $this["options"]||{};
					$("#repaidForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"repaidForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchRepaidResult();
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

var ViewRepaidConfig = {
		width		: "1600px",
		modalName	:"ViewRepaidConfig",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
function searchRepaidResult(isTurnBack){
	//关闭高级搜索条件
	$("#repaid_formSearch #searchMore").slideUp("low");
	repaid_turnOff=true;
	$("#repaid_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#repaid_formSearch #repaid_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#repaid_formSearch #repaid_list').bootstrapTable('refresh');
	}
}
$(function(){
	var oTable= new repaid_TableInit();
		oTable.Init();
	var oButtonInit = new repaid_oButtton();
		oButtonInit.Init();
		jQuery('#repaid_formSearch .chosen-select').chosen({width: '100%'});
	$("#repaid_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
})