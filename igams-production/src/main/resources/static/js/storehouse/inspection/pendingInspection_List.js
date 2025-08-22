var pendingInspection_turnOff=true;
var selOptSet = new Set();
var pendingInspection_TableInit = function () { 
	 var oTableInit = new Object();
	var flag = $("#pendingInspection_formSearch #inspectionType").val();
	var url="/inspectionGoods/pendingInspection/pageGetListPendingone";
	if (flag=="2"){
		url ="/inspectionGoods/pendingInspection/pageGetListPendingtwo";
	}else if (flag=="4"){
		url ="/inspectionGoods/pendingInspection/pageGetListPendingfour";
	}else if (flag=="5"){
		url ="/inspectionGoods/pendingInspection/pageGetListPendingfive";
	}
	// 初始化Table
	oTableInit.Init = function () {
		$('#pendingInspection_formSearch #pendingInspection_list').bootstrapTable({
			url: $("#pendingInspection_formSearch #urlPrefix").val()+url,         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#pendingInspection_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"dhrq",					// 排序字段
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
			uniqueId: "hwid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮                 								
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true,
				width: '1%'
			},{
            	title: '序号',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '1%',
                align: 'left',
                visible:true
            },{				
				field: 'wlbm',
				title: '物料编码',
				width: '4%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'wlmc',
				title: '物料名称',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'dhdh',
				title: '到货单号',
				width: '4%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'thdh',
				title: '退货单号',
				width: '4%',
				align: 'left',
				sortable: true,
				visible: flag=="4"?true:false
			}, {
				field: 'dhlxmc',
				title: '到货类型',
				width: '4%',
				align: 'left',
				sortable: true,
				visible: false
			},{				
				field: 'rklbmc',
				title: '入库类型',
				width: '4%',
				align: 'left',
				sortable: true,
				visible: flag!="4"?true:false
			}, {				
				field: 'gysmc',
				title: '供应商',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: flag!="4"?true:false
			},{				
				field: 'dhrq',
				title: '到货日期',
				width: '4%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'lbcsmc',
				title: '类别',
				width: '3%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'reduce',
				title: '数量',
				width: '3%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'dhsl',
				title: '到货数量',
				width: '3%',
				align: 'left',
				sortable: true,
				visible: false
			}, {				
				field: 'zjthsl',
				title: '初验退回数量',
				width: '3%',
				align: 'left',
				sortable: true,
				visible: false
			}, {				
				field: 'jldw',
				title: '单位',
				width: '4%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'gg',
				title: '规格',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'zsh',
				title: '追溯号',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'htnbbh',
				title: '合同单号',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: flag!="4"?true:false
			},{
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '7%',
				align: 'left',
				formatter:czFormat,
				visible: true
			}],
			onLoadSuccess: function (map) {
				count=map.jycCount;
				refreshSelInspection();
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				pendingInspectionById(row.hwid,'view',$("#pendingInspection_formSearch #btn_view").attr("tourl"));
			}
		});
		$("#pendingInspection_formSearch #pendingInspection_list").colResizable({
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
        sortLastName: "hwid", // 防止同名排位用
        sortLastOrder: "desc", // 防止同名排位用
        lbcskz1:$("#inspectionType").val(),
        // 搜索框使用
        // search:params.search
    };
    return pendingInspectionByIdSearchData(map);
	};
	return oTableInit;
};

function pendingInspectionByIdSearchData(map){
	var cxtj=$("#pendingInspection_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#pendingInspection_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr;
	}else if(cxtj=="1"){
		map["wlbm"]=cxnr;
	}else if(cxtj=="2"){
		map["wlmc"]=cxnr;
	}else if(cxtj=="3"){
		map["dhdh"]=cxnr;
	}else if(cxtj=="4"){
		map["htnbbh"]=cxnr;
	}else if(cxtj=="5"){
		map["gysmc"]=cxnr;
	}
	// 类别
	var lbs = jQuery('#pendingInspection_formSearch #lb_id_tj').val();
	map["lbs"] = lbs;
	
	// 到货类型
	var dhlxs = jQuery('#pendingInspection_formSearch #dhlx_id_tj').val();
	map["dhlxs"] = dhlxs;
	
	// 创建开始日期
	var dhsjstart = jQuery('#pendingInspection_formSearch #dhsjstart').val();
	map["dhsjstart"] = dhsjstart;
	
	// 创建结束日期
	var dhsjend = jQuery('#pendingInspection_formSearch #dhsjend').val();
	map["dhsjend"] = dhsjend;
	return map;
}
//提供给导出用的回调函数
function penInspectionSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="dhxx.dhrq";
	map["sortLastOrder"]="desc";
	map["sortName"]="dhxx.dhdh";
	map["sortOrder"]="desc";
	//传递扩展参数
	map["kzcs"] = $("#inspectionType").val();
	return pendingInspectionByIdSearchData(map);
}

function pendingInspectionById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#pendingInspection_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?hwid=" +id;
		$.showDialog(url,'待检验详细信息',viewpendingInspectionConfig);
	}else if(action=='shopping'){
		if($("#jycCount").val()=='0'){
			return;
		}
		var url=tourl+"?lbcskz1="+$("#inspectionType").val();
		$.showDialog(url,'新增检验信息',addInspectionConfig);
	}
}

var viewpendingInspectionConfig = {
	width		: "1600px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	} 
};

var pendingInspection_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_view = $("#pendingInspection_formSearch #btn_view");
		var btn_query = $("#pendingInspection_formSearch #btn_query");
		var btn_searchexport = $("#pendingInspection_formSearch #btn_searchexport");
    	var btn_selectexport = $("#pendingInspection_formSearch #btn_selectexport");
    	var btn_inspectioncar = $("#pendingInspection_formSearch #btn_inspectioncar");
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
    			pendingInspectionResult(true);
    		});
    	}
  
		  
		
       /* ---------------------------查看列表-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#pendingInspection_formSearch #pendingInspection_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			pendingInspectionById(sel_row[0].hwid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	//---------------------------导出--------------------------------------------------
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#pendingInspection_formSearch #pendingInspection_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].hwid;
        		}
        		ids = ids.substr(1);
        		$.showDialog($('#pendingInspection_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=PENDINGINSPECTION_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});

    	btn_searchexport.unbind("click").click(function(){
    		$.showDialog($('#pendingInspection_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=PENDINGINSPECTION_SEARCH&expType=search&callbackJs=penInspectionSearchData"
    				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});	
    	btn_inspectioncar.unbind("click").click(function(){
    		pendingInspectionById(null,"shopping",btn_inspectioncar.attr("tourl"));
    	});
    	
    	
       	/**显示隐藏**/      
    	$("#pendingInspection_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(pendingInspection_turnOff){
    			$("#pendingInspection_formSearch #searchMore").slideDown("low");
    			pendingInspection_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#pendingInspection_formSearch #searchMore").slideUp("low");
    			pendingInspection_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
    	
    };
    	return oInit;
};

    
function pendingInspectionResult(isTurnBack){
	//关闭高级搜索条件
	$("#pendingInspection_formSearch #searchMore").slideUp("low");
	pendingInspection_turnOff=true;
	$("#pendingInspection_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#pendingInspection_formSearch #pendingInspection_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#pendingInspection_formSearch #pendingInspection_list').bootstrapTable('refresh');
	}
}


//刷新检验车的数量
var count = 0;
function refreshSelInspection(){
	$("#jycCount").val(count);
	if(count>0){
		var strnum=count;
		if(count>99){
			strnum='99+';
		}
		var html="";
		if($("#pendingInspection_formSearch #insection_num").length>0){
			$("#pendingInspection_formSearch #insection_num").remove();
		}
		html+="<span id='insection_num' style='position:absolute;background-color:red;color:white;border-radius: 15px;border:0px;width:20px;height:20px;top:-10px;font-size:10px;line-height:150%;'>"+strnum+"</span>";
		$("#pendingInspection_formSearch #btn_inspectioncar").append(html);
		$("#pendingInspection_formSearch #btn_inspectioncar-gm").append(html);
	}else{
		$("#pendingInspection_formSearch #insection_num").remove();
	}
}
var addInspectionConfig = {
		width		: "1500px",
		modalName	: "addInspectionModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			/*success : {
				label : "提 交",
				className : "btn-primary",
				callback : function(){
					if(!$("#inspectionAddForm").valid()){// 表单验证
						return false;
					}
					if($.trim($("#inspectionAddForm #fjcfbDto").html())==""&&$("#fjids").val()==""){
						$.alert("请上传附件");
						return false;
					}
					//拼接hwxx列表提交的数据
					var allTableData = $('#inspectionAddForm #jychwList').bootstrapTable('getData');
					if(!allTableData.length>0){
						$.alert("没有要检验的货物信息，不能提交");
						return false;
					}
					var hwxxlist = [];
			        $.each(allTableData,function(i,v){
			             var hwid = v.hwid;
			             var qyl = $("#qyl"+i).val();
			             var qyrq =$("#qyrq"+i).val();
			             var zjthsl =$("#zjthsl"+i).val();
			             var hwxx = {
			            		 hwid:v.hwid,
			            		 qyl:qyl,
			            		 qyrq:qyrq,
			            		 zjthsl:zjthsl
			             }
			             hwxxlist.push(hwxx);
			        });
			        $("#hwxxlist").val(JSON.stringify(hwxxlist));
			        $("#delids").val(JSON.stringify(delhwids));
			        var $this = this;
			        var opts = $this["options"]||{};
					$("#inspectionAddForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"inspectionAddForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								//提交审核流程
								var purchase_params=[];
								purchase_params.prefix=$('#inspectionAddForm #urlPrefix').val();
								var auditType = $("#inspectionAddForm #auditType").val();
								var ywid = responseText["dyjyid"];
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								showAuditFlowDialog(auditType,ywid,function(){
									
								},null,purchase_params);
								$("#insection_num").text("0");
								$("#jychwids").val("");
								$("#jycCount").val("");
								count=0;
								refreshSelInspection();
								pendingInspectionResult();
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
				},
			},*/
			save : {//保存货物信息,方便下次进行提交操作
				label : "保存",
				className : "btn-info",
				callback : function(){
					//拼接hwxx列表提交的数据
					var allTableData = $('#inspectionAddForm #jychwList').bootstrapTable('getData'); 
					if(!allTableData.length>0){
						$.error("不存在待检验的货物信息，无法保存！");
						return;
					}
					var hwxxlist = [];
					var str="";
			        $.each(allTableData,function(i,v){
			             var hwid = v.hwid;
			             var qyl = $("#qyl"+i).val();
			             var qyrq =$("#qyrq"+i).val();
			             var zjthsl =$("#zjthsl"+i).val();
			             var bgdh = $("#bgdh_"+i).val();
			             var hwfj = $("#hwfj_"+i).val().split(",");
			             var jysl = $("#jysl"+i).val();
			             var hwxx = {
			            		 wlid:v.wlid,
			            		 hwid:v.hwid,
			            		 qyl:qyl,
			            		 qyrq:qyrq,
			            		 zjthsl:zjthsl,
			            		 bgdh:bgdh,
			            		 fjids:hwfj,
			            		 jysl:jysl,
							 	dhlxdm:v.dhlxdm
			             }
			             hwxxlist.push(hwxx);
			        });
					if (hwxxlist.length>0){
						for (var i = 0; i < hwxxlist.length; i++) {
							if (hwxxlist[i].dhlxdm!=hwxxlist[0].dhlxdm){
								$.error("到货类型不一致的货物不允许一起质检！");
								return;
							}
						}
					}
			        $("#hwxxlist").val(JSON.stringify(hwxxlist));
			        $("#delids").val(JSON.stringify(delhwids));
			        $("#bcbj").val("1");//保存操作
			        var $this = this;
			        var opts = $this["options"]||{};
					$("#inspectionAddForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"inspectionAddForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							var auditType = $("#inspectionAddForm #auditType_ll").val();
							var inspection_params=[];
							inspection_params.prefix=$('#inspectionAddForm #urlPrefix').val();
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								$("#pendingInspection_formSearch #jychwids").val(responseText["jychwids"]);
								count = count - delhwids.length;
								refreshSelInspection();//刷新数量
								pendingInspectionResult();//刷新列表
								// if(responseText["ywid"]!="" && responseText["ywid"]!=null){
								// 	//提交审核
								// 	showAuditFlowDialog(auditType,responseText["ywid"],function(){
								//
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
/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
	var jychwids=$("#jychwids").val().split(",");
	if($("#pendingInspection_formSearch #btn_inspectioncar").length>0){
		for(var j=0;j<jychwids.length;j++){
			if(row.hwid==jychwids[j]){
				return "<div id='jy"+row.hwid+"'><span id='t"+row.hwid+"' class='btn btn-danger' title='移出检验车' onclick=\"delCheckMater('" + row.hwid + "',event)\" >取消检验</span></div>";
			}
		}
		return "<div id='jy"+row.hwid+"'><span id='t"+row.hwid+"' class='btn btn-info' title='加入检验车' onclick=\"addJycCheckMater('" + row.hwid + "',event)\" >检验</span></div>";
	}
}
/**
 * 加入检验车
 * @param hwid
 * @returns
 */
function addJycCheckMater(hwid){
	var sfyhcgqx=$("#pendingInspection_formSearch #btn_inspectioncar");
	var sfyhcgqx_gm=$("#pendingInspection_formSearch #btn_inspectioncar-gm");
	if(sfyhcgqx.length==0&&sfyhcgqx_gm==0){
		$.confirm("很抱歉,您没有此权限!");
	}else{
		$.ajax({ 
		    type:'post',  
		    url:$('#pendingInspection_formSearch #urlPrefix').val()+"/inspectionGoods/pendingInspection/pagedataAddCheckMater", 
		    cache: false,
		    data: {"hwid":hwid,"access_token":$("#ac_tk").val(),"cskz1":$("#inspectionType").val()},  
		    dataType:'json', 
		    success:function(data){
		    	//返回值
		    	if(data.status=='success'){
		    		$("#jychwids").val(data.jychwids);
		    		addOrDelNum("add");//刷新总数
		    		$("#pendingInspection_formSearch #t"+hwid).remove();
		    		var html="<span id='t"+hwid+"' class='btn btn-danger' title='移出检验车' onclick=\"delCheckMater('" + hwid + "',event)\" >取消检验</span>";
		    		$("#pendingInspection_formSearch #jy"+hwid).append(html);
		    	}
		    }
		});
	}
}
/**
 * 从检验车中删除
 * @param hwid
 * @returns
 */
function delCheckMater(hwid){
	var sfyhcgqx=$("#pendingInspection_formSearch #btn_inspectioncar");
	var sfyhcgqx_gm=$("#pendingInspection_formSearch #btn_inspectioncar-gm");
	if(sfyhcgqx.length==0&&sfyhcgqx_gm==0){
		$.confirm("很抱歉,您没有此权限!");
	}else{
		$.ajax({ 
		    type:'post',  
		    url:$('#pendingInspection_formSearch #urlPrefix').val()+"/inspectionGoods/pendingInspection/pagedataDelCheckMater", 
		    cache: false,
		    data: {"hwid":hwid,"access_token":$("#ac_tk").val(),"cskz1":$("#inspectionType").val()},  
		    dataType:'json', 
		    success:function(data){
		    	//返回值
		    	if(data.status=='success'){
		    		$("#jychwids").val(data.jychwids);
		    		addOrDelNum("del");//刷新总数
		    		$("#pendingInspection_formSearch #t"+hwid).remove();
		    		var html="<span id='t"+hwid+"' class='btn btn-info' title='加入检验车' onclick=\"addJycCheckMater('" + hwid + "',event)\" >检验</span>";
		    		$("#pendingInspection_formSearch #jy"+hwid).append(html);
		    	}
		    }
		});
	}
}
/**
 * 检验测车数量加减
 * @param ation
 * @returns
 */
function addOrDelNum(action){
	if(action=='add'){
		count=parseInt(count)+1;
	}else{
		count=parseInt(count)-1;
	}
	if((count==1 && action=='add') || (count==0 && action=='del')){
		refreshSelInspection();
	}
	$("#insection_num").text(count);
	$("#jycCount").val(count);
}
$(function(){
	
	// 1.初始化Table
	var oTable = new pendingInspection_TableInit();
	oTable.Init();
	if($("#jycCount").val()==null || $("#jycCount").val()==''){
		count=0;
	}else{
		count=$("#jycCount").val();
	}
	refreshSelInspection();
    //2.初始化Button的点击事件
    var oButtonInit = new pendingInspection_ButtonInit();
    oButtonInit.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#pendingInspection_formSearch .chosen-select').chosen({width: '100%'});
	
	$("#pendingInspection_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	
});