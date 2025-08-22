var inspection_turnOff=true;
var lbbj = $("#inspectionGoods_formSearch #lbbj").val();
var lbcskz2 = $("#inspectionGoods_formSearch #lbcskz2").val();
var arraysColumns = [{
	checkbox: true,
	width: '1%'
},{
	title: '序号',
	formatter: function (value, row, index) {
		return index+1;
	},
	titleTooltip:'序号',
	width: '2%',
	align: 'left',
	visible:true
},{
	field: 'jydh',
	title: '检验单号',
	width: '8%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'jyrq',
	title: '检验日期',
	width: '10%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'jyfzrmc',
	title: '检验负责人',
	width: '8%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'bz',
	title: '备注',
	width: '20%',
	align: 'left',
	visible: true
},{
	field: 'csmc',
	title: '检验结果',
	width: '5%',
	align: 'left',
	visible: true
},{
	field: 'sflyck',
	title: '是否留样出库',
	width: '5%',
	align: 'left',
	formatter:sflyckformat,
	visible: true
},{
	field: 'wcbj',
	title: '是否完成',
	width: '4%',
	align: 'left',
	formatter:inspection_wcbjformat,
	visible: false
}, {
	field: 'sffqlld',
	title: '是否废弃领料',
	width: '5%',
	align: 'left',
	formatter:inspection_sffqlldformat,
	visible: true
}, {
	field: 'shsj',
	title: '审核时间',
	width: '8%',
	align: 'left',
	visible: true
},{
	field: 'zt',
	title: '审核状态',
	width: '5%',
	align: 'left',
	formatter:inspection_ztformat,
	visible: true
},{
	field: 'cz',
	title: '操作',
	titleTooltip:'操作',
	width: '7%',
	align: 'left',
	formatter:chczFormat,
	visible: true
}]
var arraysWlColumns = [{
	checkbox: true,
	width: '2%'
},{
	title: '序号',
	formatter: function (value, row, index) {
		return index+1;
	},
	titleTooltip:'序号',
	width: '2%',
	align: 'left',
	visible:true
},{
	field: 'bgdh',
	title: '报告单号',
	width: '7%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'jyfzrmc',
	title: '检验负责人',
	width: '5%',
	align: 'left',
	sortable: true,
	visible: true
},{
	field: 'wlbm',
	title: '物料编码',
	width: '7%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'wlmc',
	title: '物料名称',
	width: '8%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'gg',
	title: '规格',
	width: '6%',
	align: 'left',
	sortable: true,
	visible: true
}, {
	field: 'scph',
	title: '生产批号',
	width: '8%',
	align: 'left',
	visible: true
},{
	field: 'sl',
	title: '数量',
	width: '5%',
	align: 'left',
	visible: true
},{
	field: 'jydh',
	title: '检验单号',
	width: '7%',
	align: 'left',
	visible: true
},{
	field: 'jygs',
	title: '检验工时(小时)',
	width: '6%',
	align: 'left',
	visible: true
},{
	field: 'csmc',
	title: '检验结果',
	width: '4%',
	align: 'left',
	visible: true
},{
	field: 'sflyck',
	title: '留样',
	width: '3%',
	align: 'left',
	formatter:sflyckformat,
	visible: true
}, {
	field: 'sffqlld',
	title: '废弃领料单',
	width: '5%',
	align: 'left',
	formatter:inspection_sffqlldformat,
	visible: true
}, {
	field: 'shsj',
	title: '审核时间',
	width: '8%',
	align: 'left',
	visible: true
},{
	field: 'zt',
	title: '审核状态',
	width: '4%',
	align: 'left',
	formatter:inspection_ztformat,
	visible: true
},{
	field: 'bz',
	title: '备注',
	width: '4%',
	align: 'left',
	visible: true
},{
	field: 'jyrq',
	title: '检验日期',
	width: '6%',
	align: 'left',
	visible: false
}]
var inspection_TableInit = function () { 
	 var oTableInit = new Object();
	    
	// 初始化Table
	oTableInit.Init = function () {
		$('#inspectionGoods_formSearch #inspectionGoods_list').bootstrapTable({
			url: $("#inspectionGoods_formSearch #urlPrefix").val()+'/inspectionGoods/inspectionGoods/pageGetListInspectionGoods',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#inspectionGoods_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"jyrq",					// 排序字段
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
			uniqueId: "1"==lbbj?"hwid":"dhjyid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮                 								
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: "1"==lbbj?arraysWlColumns:arraysColumns,
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				if($("#inspectionGoods_formSearch #btn_viewmore").length>0){
					inspectionById(row.dhjyid,'viewmore',$("#inspectionGoods_formSearch #btn_viewmore").attr("tourl"));
				}else {
					inspectionById(row.dhjyid,'view',$("#inspectionGoods_formSearch #btn_view").attr("tourl"));
				}
			}
		});
		$("#inspectionGoods_formSearch #inspectionGoods_list").colResizable({
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
        sortLastName: "dhjyid", // 防止同名排位用
        sortLastOrder: "desc", // 防止同名排位用
        lbbj: lbbj, // 列表标记
        lbcskz2: lbcskz2
        // 搜索框使用
        // search:params.search
    };
    return inspectionSearchData(map);
	};
	return oTableInit;
};

//状态格式化
function inspection_ztformat(value,row,index){
		var auditType="";
	  	if(row.lbcskz1=="1"){
	  		auditType="AUDIT_GOODS_INSTRUMENT";
	  	}else{
	  		auditType="AUDIT_GOODS_REAGENT";
	  	}
	  	if (row.zt == '00') {
	        return '未提交';
	    }else if (row.zt == '80'){
			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.dhjyid + "\",event,\""+auditType+"\",{prefix:\"" + $('#inspectionGoods_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
	    }else if (row.zt == '15') {

			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.dhjyid + "\",event,\""+auditType+"\",{prefix:\"" + $('#inspectionGoods_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
	    }else{

			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.dhjyid + "\",event,\""+auditType+"\",{prefix:\"" + $('#inspectionGoods_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
	    }
}

//完成标记格式化
function inspection_wcbjformat(value,row,index){
	   if (row.wcbj == '0') {
	        return '否';
	    }else{
	    	return '是';
	    }
}


//是否废弃领料单格式化
function inspection_sffqlldformat(value,row,index){
	   if (row.sffqlld == '1') {
	        return '是';
	    }else{
	    	return '否';
	    }
}
//是否留样出库
function sflyckformat(value,row,index){
	   if (row.sflyck == '1') {
	        return '是';
	    }else{
	    	return '否';
	    }
}

function inspectionSearchData(map){
	var cxtj=$("#inspectionGoods_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#inspectionGoods_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr;
	}else if(cxtj=="1"){
		map["jydh"]=cxnr;
	}else if(cxtj=="2"){
		map["jyfzrmc"]=cxnr;
	} else if(cxtj=="3"){
		map["wlbm"]=cxnr;
	} else if(cxtj=="4"){
		map["wlmc"]=cxnr;
	} else if(cxtj=="5"){
		map["bgdh"]=cxnr;
	} else if(cxtj=="6"){
		map["scph"]=cxnr;
	}
	// 创建开始日期
	var shsjstart = jQuery('#inspectionGoods_formSearch #shsjstart').val();
	map["shsjstart"] = shsjstart;
	
	// 创建结束日期
	var shsjend = jQuery('#inspectionGoods_formSearch #shsjend').val();
	map["shsjend"] = shsjend;
	//检验结果类别
	var jyjg = jQuery('#inspectionGoods_formSearch #jyjg_id_tj').val();
	map["jyjgs"] = jyjg;
	//是否留样出库
	var sflyck = jQuery('#inspectionGoods_formSearch #sflyck_id_tj').val();
	map["sflyck"] = sflyck;
	// 状态
	var zts = jQuery('#inspectionGoods_formSearch #zt_id_tj').val();
	map["zts"] = zts;
	return map;
}

//提供给导出用的回调函数
function insSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="dhjy.jyrq";
	map["sortLastOrder"]="desc";
	map["sortName"]="dhjy.jydh";
	map["sortOrder"]="desc";
	return inspectionSearchData(map);
}

function inspectionById(id,action,tourl,auditType){
	if(!tourl){
		return;
	}
	tourl = $("#inspectionGoods_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?dhjyid=" +id;
		$.showDialog(url,'到货检验详细信息',viewInspectionConfig);
	}else if(action == 'mod'){
		var url = tourl + "?dhjyid=" +id+"&auditType="+auditType;
		$.showDialog(url,'修改到货检验信息',modInspectionConfig);
	}else if(action == 'advancedmod'){
		var url = tourl + "?dhjyid=" +id+"&auditType="+auditType;
		$.showDialog(url,'高级修改到货检验信息',modInspectionConfig);
	}else if(action=='submit'){
		var url = tourl + "?dhjyid=" +id+"&auditType="+auditType;
		$.showDialog(url,'提交到货检验信息',submitInspectionConfig);
	}else if(action=="viewCommon"){
		var url=tourl+"?dhjyid="+id;
		$.showDialog(url,'到货检验详细信息 共通',viewInspectionConfig);
	}else if(action=="upload"){
		var url=tourl+"?hwid="+id+"&lbbj="+lbbj;
		$.showDialog(url,'上传',uploadConfig);
	}else if(action=="viewmore"){
		var url=tourl+"?dhjyid="+id+"&flag=viewmore";
		$.showDialog(url,'详细查看',viewInspectionConfig);
	}else if(action=="backdrop"){
        var url=tourl+"?hwid="+id;
        $.showDialog(url,'背景信息上传',backdropConfig);
    }
	
}

var backdropConfig={
	width		: "800px",
	modalName	: "backdropModal",
	formName	: "backdropInspectionGoodsForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#backdropInspectionGoodsForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}

				var $this = this;
				var opts = $this["options"]||{};

				$("#backdropInspectionGoodsForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"backdropInspectionGoodsForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								inspectionResult ();
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

var uploadConfig={
	width		: "800px",
	modalName	: "uploadModal",
	formName	: "uploadInspectionGoodsForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#uploadInspectionGoodsForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}

				var $this = this;
				var opts = $this["options"]||{};

				$("#uploadInspectionGoodsForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"uploadInspectionGoodsForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								inspectionResult ();
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
var viewInspectionConfig = {
	width		: "1600px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	} 
};
var modInspectionConfig = {
		width		: "1600px",
		modalName	: "modInspectionModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			save : {//保存货物信息,方便下次进行提交操作
				label : "保存",
				className : "btn-info",
				callback : function(){
					//拼接hwxx列表提交的数据
					var allTableData = $('#inspectionModForm #dhjyhwList').bootstrapTable('getData');
					if(!allTableData.length>0){
						$.error("不存在待检验的货物信息，无法保存！");
						return;
					}
					var hwxxlist = [];
			        $.each(allTableData,function(i,v){
			             var hwid = v.hwid;
			             var qyl = $("#qyl"+i).val();
			             var qyrq =$("#qyrq"+i).val();
			             var zjthsl =$("#zjthsl"+i).val();
			             var bgdh = $("#bgdh_"+i).val();
			             var hwfj = $("#hwfj_"+i).val().split(",");
						var rklbdm = $("#rklbdm_"+i).val();
			             var hwxx = {
			            		 hwid:v.hwid,
			            		 wlid:v.wlid,
			            		 qyl:qyl,
			            		 qyrq:qyrq,
			            		 zjthsl:zjthsl,
			            		 bgdh:bgdh,
			            		 fjids:hwfj,
							 rklbdm:rklbdm
			             }
			             hwxxlist.push(hwxx);
			        });
			        $("#hwxxlist").val(JSON.stringify(hwxxlist));
			        $("#delids").val(JSON.stringify(delhwids));
			        $("#bcbj").val("1");//保存操作
			        var $this = this;
			        var opts = $this["options"]||{};
					$("#inspectionModForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"inspectionModForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							var auditType = $("#inspectionModForm #auditType_ll").val();
							var inspection_params=[];
							inspection_params.prefix=$('#inspectionModForm #urlPrefix').val();
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									inspectionResult();//刷新列表
								}
								// if(responseText["ywid"]!="" && responseText["ywid"]!=null){
								// 	//提交审核
								// 	showAuditFlowDialog(auditType,responseText["ywid"],function(){
								// 		inspectionResult();//刷新列表
								// 	},null,inspection_params);
								// }
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
				},
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		} 
	};

var inspection_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_view = $("#inspectionGoods_formSearch #btn_view");
		var btn_query = $("#inspectionGoods_formSearch #btn_query");
		var btn_searchexport = $("#inspectionGoods_formSearch #btn_searchexport");
    	var btn_selectexport = $("#inspectionGoods_formSearch #btn_selectexport");
    	var btn_del = $("#inspectionGoods_formSearch #btn_del");//删除
    	var btn_submit=$("#inspectionGoods_formSearch #btn_submit");//提交
    	var btn_discard=$("#inspectionGoods_formSearch #btn_discard");//废弃
    	var btn_mod = $("#inspectionGoods_formSearch #btn_mod");//修改
		var btn_advancedmod = $("#inspectionGoods_formSearch #btn_advancedmod");//高级修改
    	var btn_print = $("#inspectionGoods_formSearch #btn_print");
    	var btn_viewCommon = $("#inspectionGoods_formSearch #btn_viewCommon");
    	var btn_certificateprint = $("#inspectionGoods_formSearch #btn_certificateprint");
    	var btn_upload = $("#inspectionGoods_formSearch #btn_upload");
    	var btn_viewmore = $("#inspectionGoods_formSearch #btn_viewmore");
    	var btn_backdrop = $("#inspectionGoods_formSearch #btn_backdrop");
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
    			inspectionResult(true);
    		});
    	}
  
		  
		
       /* ---------------------------查看列表-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#inspectionGoods_formSearch #inspectionGoods_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			inspectionById(sel_row[0].dhjyid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
        
    	//---------------------------导出--------------------------------------------------
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#inspectionGoods_formSearch #inspectionGoods_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].dhjyid;
        		}
        		ids = ids.substr(1);
        		if(lbbj=='1'){
        		    $.showDialog($('#inspectionGoods_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=INSPECTION_GOODS_SELECT&expType=select&ids="+ids
                                                				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        		}else{
        		    $.showDialog($('#inspectionGoods_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=INSPECTION_SELECT&expType=select&ids="+ids
                            				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        		}

    		}else{
    			$.error("请选择数据");
    		}
    	});
		//---------------------------上传报告和记录-----------------------------------
		btn_upload.unbind("click").click(function(){
			var sel_row = $('#inspectionGoods_formSearch #inspectionGoods_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				inspectionById(sel_row[0].hwid,"upload",btn_upload.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		//---------------------------上传报告和记录-----------------------------------
        btn_backdrop.unbind("click").click(function(){
            var sel_row = $('#inspectionGoods_formSearch #inspectionGoods_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                inspectionById(sel_row[0].hwid,"backdrop",btn_backdrop.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
    	btn_searchexport.unbind("click").click(function(){
            if(lbbj=='1'){
                 $.showDialog($('#inspectionGoods_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=INSPECTION_GOODS_SEARCH&expType=search&callbackJs=insSearchData"
                                                    ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.showDialog($('#inspectionGoods_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=INSPECTION_SEARCH&expType=search&callbackJs=insSearchData"
                                    ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }

    	});	
    	
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#inspectionGoods_formSearch #inspectionGoods_list').bootstrapTable('getSelections');// 获取选择行数据
    		if (sel_row.length==0) {
				$.error("请至少选中一行");
				return;
			}else{
				var ids="";
    			for (var i = 0; i < sel_row.length; i++) {// 循环读取选中行数据
        			ids= ids + ","+ sel_row[i].dhjyid;
        		}
    			ids=ids.substr(1);
    			$.confirm('您确定要删除所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= btn_del.attr("tourl");
        				url = $("#inspectionGoods_formSearch #urlPrefix").val()+url
        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								inspectionResult(false);
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
    	//提交
    	btn_submit.unbind("click").click(function(){
    		var sel_row = $('#inspectionGoods_formSearch #inspectionGoods_list').bootstrapTable('getSelections');// 获取选择行数据
    		if (sel_row.length==1) {
				var dhjyid = "";
				var auditType="";
				for (var i = 0; i < sel_row.length; i++) {// 循环读取选中行数据
					if(sel_row[i].zt !='15'&& sel_row[i].zt !='00'){
	    				$.error("只能对退回或未提交的数据进行提交");
	    				return;
	    			}
        			dhjyid = sel_row[i].dhjyid;
					if (sel_row[i].lbcskz1=="1"){
						auditType = "AUDIT_GOODS_INSTRUMENT";
					}else if (sel_row[i].lbcskz1=="2"){
						auditType = "AUDIT_GOODS_REAGENT";
					}else if (sel_row[i].lbcskz1=="4"){
						auditType = "AUDIT_GOODS_THORGH";
					}else if (sel_row[i].lbcskz1=="5"){
						auditType = "AUDIT_GOODS_CONSUMABLE";
					}
        		}
				inspectionById(sel_row[0].dhjyid,"submit",btn_submit.attr("tourl"),auditType);
			}else {
				$.error("请选择一条数据");
				return;
			}
    	});
    	//废弃
    	btn_discard.unbind("click").click(function(){
    		var sel_row = $('#inspectionGoods_formSearch #inspectionGoods_list').bootstrapTable('getSelections');// 获取选择行数据
    		if (sel_row.length==0) {
    			$.error("请至少选中一行");
				return;
			}else{
				var ids="";
    			for (var i = 0; i < sel_row.length; i++) {// 循环读取选中行数据
    				if(sel_row[i].zt !='00' && sel_row[i].zt !='15'){
	    				$.error("该到货检验正在审核中或已审核，不允许废弃");
	    				return;
	    			}
        			ids= ids + ","+ sel_row[i].dhjyid;
        		}
    			$.confirm('您确定要废弃所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= btn_del.attr("tourl");
        				url = $("#inspectionGoods_formSearch #urlPrefix").val()+url
        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								inspectionResult(false);
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
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#inspectionGoods_formSearch #inspectionGoods_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			if("00"!=sel_row[0].zt&&"15"!=sel_row[0].zt){
    				$.error("当前状态不允许修改");
    				return;
    			}
				var auditType="";
				if (sel_row[0].lbcskz1=="1"){
					auditType = "AUDIT_GOODS_INSTRUMENT";
				}else if (sel_row[0].lbcskz1=="2"){
					auditType = "AUDIT_GOODS_REAGENT";
				}else if (sel_row[0].lbcskz1=="4"){
					auditType = "AUDIT_GOODS_THORGH";
				}else if (sel_row[0].lbcskz1=="5"){
					auditType = "AUDIT_GOODS_CONSUMABLE";
				}
    			inspectionById(sel_row[0].dhjyid,"mod",btn_mod.attr("tourl"),auditType);
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/* --------------------------- 高级修改 -----------------------------------*/
		btn_advancedmod.unbind("click").click(function(){
			var sel_row = $('#inspectionGoods_formSearch #inspectionGoods_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
				var auditType="";
				if (sel_row[0].lbcskz1=="1"){
					auditType = "AUDIT_GOODS_INSTRUMENT";
				}else if (sel_row[0].lbcskz1=="2"){
					auditType = "AUDIT_GOODS_REAGENT";
				}else if (sel_row[0].lbcskz1=="4"){
					auditType = "AUDIT_GOODS_THORGH";
				}else if (sel_row[0].lbcskz1=="5"){
					auditType = "AUDIT_GOODS_CONSUMABLE";
				}
				inspectionById(sel_row[0].dhjyid, "advancedmod", btn_advancedmod.attr("tourl"),auditType);
			}else{
				$.error("请选中一行");
			}
		});
    	
    	/* --------------------------- 打印请检单 -----------------------------------*/
    	btn_print.unbind("click").click(function(){
    		var sel_row = $('#inspectionGoods_formSearch #inspectionGoods_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].dhjyid;	
    		}
    		ids = ids.substr(1);
    		var url=$('#inspectionGoods_formSearch #urlPrefix').val()+btn_print.attr("tourl")+"?dhjyids="+ids.toString()+"&access_token="+$("#ac_tk").val();
			window.open(url);
    	});
		/* --------------------------- 打印合格证 -----------------------------------*/
    	btn_certificateprint.unbind("click").click(function(){
    		var sel_row = $('#inspectionGoods_formSearch #inspectionGoods_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].hwid;
    		}
    		ids = ids.substr(1);
    		var url=$('#inspectionGoods_formSearch #urlPrefix').val()+btn_certificateprint.attr("tourl")+"?hwids="+ids.toString()+"&access_token="+$("#ac_tk").val();
			window.open(url);
    	});
    	/* ---------------------------共通 查看列表-----------------------------------*/
    	btn_viewCommon.unbind("click").click(function(){
    		var sel_row = $('#inspectionGoods_formSearch #inspectionGoods_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			inspectionById(sel_row[0].dhjyid,"viewCommon",btn_viewCommon.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/* ---------------------------详细查看-----------------------------------*/
		btn_viewmore.unbind("click").click(function(){
			var sel_row = $('#inspectionGoods_formSearch #inspectionGoods_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				inspectionById(sel_row[0].dhjyid,"viewmore",btn_viewmore.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
       	/**显示隐藏**/      
    	$("#inspectionGoods_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(inspection_turnOff){
    			$("#inspectionGoods_formSearch #searchMore").slideDown("low");
    			inspection_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#inspectionGoods_formSearch #searchMore").slideUp("low");
    			inspection_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
    	
    };
    	return oInit;
};
var submitInspectionConfig = {
		width		: "1500px",
		modalName	: "submitInspectionModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "提 交",
				className : "btn-primary",
				callback : function(){
					//拼接hwxx列表提交的数据
					var allTableData = $('#inspectionModForm #dhjyhwList').bootstrapTable('getData'); 
					if(!allTableData.length>0){
						$.error("不存在待检的货物信息，无法保存！");
						return;
					}
					var hwxxlist = [];
					var result = "0";
			        $.each(allTableData,function(i,v){			        	
			             var hwid = v.hwid;
			             var qyl = $("#qyl"+i).val();
			             var qyrq =$("#qyrq"+i).val();
			             var zjthsl =$("#zjthsl"+i).val();
			             var lysl =$("#lysl"+i).val();
			             var bgdh = $("#bgdh_"+i).val();
			             var hwfj = $("#hwfj_"+i).val().split(",");
			             var fjbj = $('#inspectionModForm #dhjyhwList').bootstrapTable('getData')[i].fjbj;
			             if(fjbj!="0"){
			            	 result = "1";
			             }
			             var hwxx = {
			            		 hwid:v.hwid,
			            		 wlid:v.wlid,
			            		 qyl:qyl,
			            		 qyrq:qyrq,
			            		 zjthsl:zjthsl,
			            		 bgdh:bgdh,
			            		 fjids:hwfj,
			            		 lysl:lysl
			             }
			             hwxxlist.push(hwxx);
			        });
			       /* if(result=="1"){
			        	$.error("请上传附件！");
						return false;
			        }*/
			        $("#hwxxlist").val(JSON.stringify(hwxxlist));
			        $("#delids").val(JSON.stringify(delhwids));
			        var $this = this;
			        var opts = $this["options"]||{};
					$("#inspectionModForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"inspectionModForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							//提交审核流程
							var purchase_params=[];
							purchase_params.prefix=$('#inspectionModForm #urlPrefix').val();
							var auditType = $("#inspectionModForm #auditType").val();
							var ywid = responseText["dyjyid"];
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							showAuditFlowDialog(auditType,ywid,function(){

							},null,purchase_params);
							inspectionResult();
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
				},
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

    
function inspectionResult(isTurnBack){
	//关闭高级搜索条件
	$("#inspectionGoods_formSearch #searchMore").slideUp("low");
	inspection_turnOff=true;
	$("#inspectionGoods_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#inspectionGoods_formSearch #inspectionGoods_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#inspectionGoods_formSearch #inspectionGoods_list').bootstrapTable('refresh');
	}
}
/**
 * 撤回操作
 */
function chczFormat(value,row,index) {
	if(row.zt=='10'){
		return "<div id='dhjyid"+row.dhjyid+"'><span id='t"+row.dhjyid
		+"' class='btn btn-danger' title='撤回审核' onclick=\"inspectionBack('" + row.dhjyid + "','"+row.lbcskz1+"',event)\" >撤回</span></div>";
	}
}
function inspectionBack(dhjyid,auditType){
	if("1"==auditType){
		auditType="AUDIT_GOODS_INSTRUMENT";
	}else{
		auditType="AUDIT_GOODS_REAGENT";
	}
	var msg = '您确定要撤回该检验信息吗？';
	var purchase_params = [];
	purchase_params.prefix = $("#inspectionGoods_formSearch #urlPrefix").val();
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,dhjyid,function(){
				inspectionResult(false);
			},purchase_params);
		}
	});
}
$(function(){

	
	// 1.初始化Table
	var oTable = new inspection_TableInit();
	oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new inspection_ButtonInit();
    oButtonInit.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#inspectionGoods_formSearch .chosen-select').chosen({width: '100%'});
	
	$("#inspectionGoods_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	
});