var supplier_turnOff=true;

var supplier_TableInit = function () {
	var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#supplierlist_formSearch #tb_list').bootstrapTable({
			url: $("#supplierlist_formSearch #urlPrefix").val()+'/warehouse/supplier/pageGetListSupplier',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#supplierlist_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"gysxx.fzrq",					// 排序字段
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
			uniqueId: "gysid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true
			}, {
				field: 'gysid',
				title: '供应商ID',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'gysdm',
				title: '供应商代码',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'gysmc',
				title: '供应商名称',
				width: '15%',
				align: 'left',
				visible: true
			}, {
				field: 'gysjc',
				title: '供应商简称',
				width: '15%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'gfbh',
				title: '供方编号',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'dq',
				title: '地址',
				width: '8%',
				align: 'left',
				visible: true
			}, {
				field: 'fzrq',
				title: '发展日期',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'lxr',
				title: '联系人',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'fddbr',
				title: '法定代表人',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'dh',
				title: '电话',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'sj',
				title: '手机',
				width: '10%',
				align: 'left',
				visible: false
			},{
				field: 'qq',
				title: 'QQ',
				width: '10%',
				align: 'left',
				visible: false
			},{
				field: 'wx',
				title: '微信',
				width: '10%',
				align: 'left',
				visible: false
			},{
				field: 'cz',
				title: '传真',
				width: '10%',
				align: 'left',
				visible: false
			},{
				field: 'yx',
				title: '邮箱',
				width: '10%',
				align: 'left',
				visible: false
			},{
				field: 'gfgllbmc',
				title: '供方管理类别',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'sfmc',
				title: '省份',
				width: '8%',
				align: 'left',
				visible: true
			}, {
				field: 'tysj',
				title: '停用时间',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'tyrymc',
				title: '停用人员',
				width: '8%',
				align: 'left',
				visible: false
			},{
				field: 'lrsj',
				title: '录入时间',
				width: '8%',
				align: 'left',
				visible: false
			},{
				field: 'lrrymc',
				title: '录入人员',
				width: '8%',
				align: 'left',
				visible: false
			},{
				field: 'khh',
				title: '开户行',
				width: '10%',
				align: 'left',
				visible: false
			},{
				field: 'zh',
				title: '账户',
				width: '10%',
				align: 'left',
				visible: false
			},{
				field: 'sl',
				title: '税率',
				width: '5%',
				align: 'left',
				visible: false
			},{
				field: 'sfxs',
				title: '货期显示',
				width: '5%',
				align: 'left',
				formatter:sfxsmat,
				visible: false
			},{
				field: 'scbj',
				title: '状态',
				width: '5%',
				align: 'left',
				formatter:ztmat,
				visible: true
			},{
				field: 'bz',
				title: '备注',
				width: '8%',
				align: 'left',
				visible: true
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				InspectionDealById(row.gysid, 'view',$("#supplierlist_formSearch #btn_view").attr("tourl"));
			},
		});
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
        sortLastName: "gysxx.gysid", // 防止同名排位用
        sortLastOrder: "asc" // 防止同名排位用
        // 搜索框使用
        // search:params.search
    };
    return getSupplierData(map);
	};
	return oTableInit;
};
//货期显示
function sfxsmat(value, row, index) {
	if (row.sfxs == '0') {
		return '否';
	}
	else{
		return "是";
	}
}//状态显示
function ztmat(value, row, index) {
	var html ="";
	if(row.scbj=='0'){
		html="<span style='color:green;'>"+"正常"+"</span>";
	}else if(row.scbj=='2'){
		html="<span style='color:red;'>"+"停用"+"</span>";
	}
	return html;
}
function searchMore(){
	var ev=ev||event; 
	if(supplier_turnOff){
		$("#supplierlist_formSearch #searchMore").slideDown("low");
		supplier_turnOff=false;
		this.innerHTML="基本筛选";
	}else{
		$("#supplierlist_formSearch #searchMore").slideUp("low");
		supplier_turnOff=true;
		this.innerHTML="高级筛选";
	}
	ev.cancelBubble=true;
	}
function searchSrcResult(){
	var pageNumber =$("#supplierlist_formSearch #tb_list").bootstrapTable('getOptions').pageNumber;
	$('#supplierlist_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:pageNumber});
	//关闭高级搜索条件
	$("#supplierlist_formSearch #searchMore").slideUp("low");
	supplier_turnOff=true;
	$("#supplierlist_formSearch #sl_searchMore").html("高级筛选");
}
	
function getSupplierData(map){
	var cxtj=$("#supplierlist_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#supplierlist_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr
	}else if(cxtj=="1"){
		map["gysdm"]=cxnr
	}else if(cxtj=="2"){
		map["gysmc"]=cxnr
	}else if(cxtj=="3"){
		map["dq"]=cxnr
	}else if(cxtj=="4"){
		map["lxr"]=cxnr
	}else if(cxtj=="5"){
		map["dh"]=cxnr
	}else if(cxtj=="6"){
		map["sj"]=cxnr
	}else if(cxtj=="7"){
		map["qq"]=cxnr
	}else if(cxtj=="8"){
		map["wx"]=cxnr
	}else if(cxtj=="9"){
		map["gysjc"]=cxnr
	}else if(cxtj=="10"){
		map["gfgllbmc"]=cxnr
	}else if(cxtj=="11"){
		map["bz"]=cxnr
	}else if(cxtj=="12"){
        map["gfbh"]=cxnr
    }
	// 采样开始时间
	var fzkssj = jQuery('#supplierlist_formSearch #fzkssj').val();
		map["fzkssj"] = fzkssj;
	// 采样开始时间
	var fzjssj = jQuery('#supplierlist_formSearch #fzjssj').val();
		map["fzjssj"] = fzjssj;	
	//供应商管理类别
	var gfgllbs = jQuery("#supplierlist_formSearch #gfgllb_id_tj").val();
	var	re = new RegExp("'","g");
		gfgllbs=gfgllbs.replace(re,"");
	map["gfgllbs"] = gfgllbs;
	//是否显示
	var sfxss=jQuery('#supplierlist_formSearch #sfxs_id_tj').val();
	map["sfxss"] = sfxss.replace(/'/g, "");
	//是否停用
	var zts=jQuery('#supplierlist_formSearch #zt_id_tj').val();
	map["zts"] = zts.replace(/'/g, "");
	 return map;
}


//添加日期控件
laydate.render({
   elem: '#fzkssj'
  ,theme: '#2381E9'
});
laydate.render({
   elem: '#fzjssj'
  ,theme: '#2381E9'
});

function ProjectSupplierResult(isTurnBack){
	if(isTurnBack){
		$('#supplierlist_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#supplierlist_formSearch #tb_list').bootstrapTable('refresh');
	}
}

function InspectionDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl=$("#supplierlist_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?gysid=" +id;
		$.showDialog(url,'供应商详细信息',viewSupplierConfig);
	}else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'新建供应商',addSupplierConfig);
	}else if(action =='mod'){
		var url=tourl + "?gysid=" +id;
		$.showDialog(url,'编辑供应商信息',modSupplierConfig);
	}else if(action =='copy'){
		var url=tourl + "?gysid=" +id;
		$.showDialog(url,'选择服务器',chanceSupplierTypeConfig);
	}else if(action =='deliveryset'){
		var url=tourl + "?ids=" +id;
		$.showDialog(url,'货期设置',deliverysetConfig);
	}else if(action =='disable'){
		var url=tourl + "?gysid=" +id;
		$.showDialog(url,'停用供应商',disableConfig);
	}
}

//自动检查报告压缩进度
var checkReportZipStatus = function(intervalTime,redisKey){
	$.ajax({
		type : "POST",
		url : $('#supplierlist_formSearch #urlPrefix').val()+"/common/export/commCheckExport",
		data : {"wjid" : redisKey +"","access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.cancel){//取消导出则直接return
				return;
			}
			if(data.result == false){
				if(data.msg && data.msg!=""){
					$.error(data.msg);
					if($("#cardiv")) $("#cardiv").remove();
				}else{
					if(intervalTime < 5000)
						intervalTime = intervalTime + 1000;
					if($("#exportCount")){
						$("#exportCount").html(data.currentCount)
						if(("/"+data.currentCount) == $("#totalCount").text()){
							$("#totalCount").html($("#totalCount").text()+" 压缩中....")
						}
					}
					setTimeout("checkReportZipStatus("+intervalTime+",'"+ redisKey +"')",intervalTime);
				}
			}else{
				if(data.msg && data.msg!=""){
					$.error(data.msg);
					if($("#cardiv")) $("#cardiv").remove();
				}
				else{
					if($("#cardiv")) $("#cardiv").remove();
					window.open($('#supplierlist_formSearch #urlPrefix').val()+"/common/export/commDownloadExport?wjid="+redisKey + "&access_token="+$("#ac_tk").val());
				}
			}
		}
	});
}
var supplier_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_view = $("#supplierlist_formSearch #btn_view");
		var btn_add = $("#supplierlist_formSearch #btn_add");
		var btn_mod = $("#supplierlist_formSearch #btn_mod");
		var btn_del = $("#supplierlist_formSearch #btn_del");
		var btn_query = $("#supplierlist_formSearch #btn_query");
		var btn_copy = $("#supplierlist_formSearch #btn_copy");
		var btn_deliveryset = $("#supplierlist_formSearch #btn_deliveryset");
		var btn_download = $("#supplierlist_formSearch #btn_download");
		var btn_disable = $("#supplierlist_formSearch #btn_disable");
		var btn_enable = $("#supplierlist_formSearch #btn_enable");

		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			ProjectSupplierResult(true);
    		});
    	}
        /*---------------------------查看送检信息表-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#supplierlist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			InspectionDealById(sel_row[0].gysid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*---------------------------新增项目研发任务信息-----------------------------------*/
    	btn_add.unbind("click").click(function(){
    		InspectionDealById(null,"add",btn_add.attr("tourl"));
    	});
    	/*---------------------------复制供应商信息-----------------------------------*/
    	btn_copy.unbind("click").click(function(){
    		var sel_row = $('#supplierlist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			InspectionDealById(sel_row[0].gysid,"copy",btn_copy.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*---------------------------编辑定时任务信息-----------------------------------*/
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#supplierlist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
				if (sel_row[0].scbj=='2'){
					$.error(sel_row[0].gysmc+"已停用，不允许修改");
					return;
				}
    			InspectionDealById(sel_row[0].gysid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*---------------------------删除项目研发任务信息-----------------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#supplierlist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].gysid;
    		}
    		ids = ids.substr(1);
    		$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= $("#supplierlist_formSearch #urlPrefix").val()+btn_del.attr("tourl");
    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								ProjectSupplierResult();
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
		/*---------------------------删除项目研发任务信息-----------------------------------*/
		btn_download.unbind("click").click(function(){
			var sel_row = $('#supplierlist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length ==0){
				$.error("请至少选中一行");
				return;
			}
			var ids="";
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
				ids = ids + ","+ sel_row[i].gysid;
			}
			ids = ids.substr(1);
			$.confirm('确认下载',function(result){
				if(result){
					jQuery.ajaxSetup({async:false});
					var url= $("#supplierlist_formSearch #urlPrefix").val()+btn_download.attr("tourl");
					jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
						setTimeout(function(){
							if(responseText["status"] == 'success'){
								var cardiv =document.createElement("div");
								cardiv.id="cardiv";
								var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span id="totalCount" style="color:red;font-weight:600;">/' + responseText["count"] + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
								cardiv.innerHTML=s_str;
								//将上面创建的P元素加入到BODY的尾部
								document.body.appendChild(cardiv);
								setTimeout("checkReportZipStatus(2000,'"+ responseText["redisKey"]+ "')",1000);
								//绑定导出取消按钮事件
								$("#exportCancel").click(function(){
									//先移除导出提示，然后请求后台
									if($("#cardiv").length>0) $("#cardiv").remove();
									$.ajax({
										type : "POST",
										url : $('#supplierlist_formSearch #urlPrefix').val()+"/common/export/commCancelExport",
										data : {"wjid" : responseText["redisKey"]+"","access_token":$("#ac_tk").val()},
										dataType : "json",
										success:function(res){
											if(res != null && res.result==false){
												if(res.msg && res.msg!="")
													$.error(res.msg);
											}

										}
									});
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
		/*---------------------------货期设置-----------------------------------*/
		btn_deliveryset.unbind("click").click(function(){
			var sel_row = $('#supplierlist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length ==0){
				$.error("请至少选中一行");
				return;
			}
			var ids="";
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
				ids = ids + ","+ sel_row[i].gysid;
			}
			ids = ids.substr(1);
			InspectionDealById(ids,"deliveryset",btn_deliveryset.attr("tourl"));
		});
		/*---------------------------停用供应商-----------------------------------*/
		btn_disable.unbind("click").click(function(){
			var sel_row = $('#supplierlist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length ==1){
				if (sel_row[0].scbj=='2'){
					$.error(sel_row[0].gysmc+"已停用，请勿重复操作");
					return;
				}
				InspectionDealById(sel_row[0].gysid,"disable",btn_disable.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*---------------------------启用供应商-----------------------------------*/
		btn_enable.unbind("click").click(function(){
			var sel_row = $('#supplierlist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length ==1){
				if (sel_row[0].scbj=='0'){
					$.error(sel_row[0].gysmc+"已启用，请勿重复操作");
					return;
				}
			}else{
				$.error("请选中一行");
				return;
			}
			$.confirm('您确定要启用所选择的记录吗？',function(result){
				if(result){
					jQuery.ajaxSetup({async:false});
					var url= $("#supplierlist_formSearch #urlPrefix").val()+btn_enable.attr("tourl");
					jQuery.post(url,{"gysid":sel_row[0].gysid,"access_token":$("#ac_tk").val()},function(responseText){
						setTimeout(function(){
							if(responseText["status"] == 'success'){
								$.success(responseText["message"],function() {
									ProjectSupplierResult();
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
    };
    return oInit;
};


var viewSupplierConfig = {
		width		: "1400px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
var copySupplierConfig = {
	width		: "800px",
	modalName	: "copySupplierModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}

				var $this = this;
				var opts = $this["options"]||{};

				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								ProjectSupplierResult();
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
var addSupplierConfig = {
		width		: "800px",
		modalName	: "addSupplierModal",
		formName	: "ajaxForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#ajaxForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					
					$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
					
					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									ProjectSupplierResult ();
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

var modSupplierConfig = {
		width		: "800px",
		modalName	: "modSupplierModal",
		formName	: "ajaxForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#ajaxForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					
					var $this = this;
					var opts = $this["options"]||{};
					
					$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
					
					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									ProjectSupplierResult();
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

var chanceSupplierTypeConfig={
	width		: "600px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	modalName	: "chancePurchaseTypeModal",
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
var deliverysetConfig = {
	width		: "800px",
	modalName	: "deliverysetModal",
	formName	: "deliverysetForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#deliverysetForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				$("#deliverysetForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"deliverysetForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								ProjectSupplierResult ();
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
var disableConfig = {
	width		: "800px",
	modalName	: "disableModal",
	formName	: "supplierdisableForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#supplierdisableForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				$("#supplierdisableForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"supplierdisableForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								ProjectSupplierResult ();
							}
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.confirm(responseText["message"]+'</br>还要继续停用吗？',function(result){
							if(result){
								jQuery.ajaxSetup({async:false});
								var url= $("#supplierlist_formSearch #urlPrefix").val()+'/warehouse/supplier/disableSaveSupplier';
								jQuery.post(url,{'gysid':$("#supplierdisableForm #gysid").val()
									,'tysj':$("#supplierdisableForm #tysj").val()
									,'tyry':$("#supplierdisableForm #tyry").val()
									,'bz':$("#supplierdisableForm #bz").val()
									,'disableFlag':'save'
									,"access_token":$("#ac_tk").val()},function(responseText){
									setTimeout(function(){
										if(responseText["status"] == 'success'){
											$.success(responseText["message"],function() {
												if(opts.offAtOnce){
													$.closeModal(opts.modalName);
													ProjectSupplierResult ();
												}
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
							}else {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									ProjectSupplierResult ();
								}
							}
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
var supplier_PageInit = function(){
	var oInit = new Object();
	var postdata = {};

	oInit.Init = function () {
		addTj('zt','0','supplierlist_formSearch');
	}
	return oInit;
}
$(function(){
	var oInit = new supplier_PageInit();
	oInit.Init();
	//0.界面初始化
	// 1.初始化Table
	var oTable = new supplier_TableInit();
	oTable.Init();
	
    //2.初始化Button的点击事件
    var oButtonInit = new supplier_ButtonInit();
    oButtonInit.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#supplierlist_formSearch .chosen-select').chosen({width: '100%'});
	
	// 初始绑定显示更多的事件
//	$("#projectresearch_formSearch [name='more']").each(function(){
//		$(this).on("click", s_showMoreFn);
//	});
});